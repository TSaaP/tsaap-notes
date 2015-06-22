package org.tsaap.lti

import grails.plugins.springsecurity.SpringSecurityService
import groovy.sql.Sql
import org.springframework.security.crypto.password.StandardPasswordEncoder

import javax.sql.DataSource

/**
 * Created by dorian on 18/06/15.
 */
class LmsUserHelper {

    SpringSecurityService springSecurityService

    /**
     * Find if an user who connect from LMS have a TsaaP-Notes account linked with his LMS id
     * If it's the case: the user is automatically log to his account
     * Else a Tsaap-Notes account is create for this user and the user is also log with it
     * @param db the database connection
     * @param user_id user id given by LMS
     * @param firstName user firstname
     * @param lastName user lastname
     * @param email user email
     * @param key lti consumer key
     * @return
     */
    def findUserIsKnowOrCreateIt(Db db, String user_id, String firstName, String lastName, String email, String key, Boolean role) {
        def sql = new Sql(db.getConnection())
        def username
        def password
        springSecurityService = new SpringSecurityService()
        StandardPasswordEncoder encoder = new StandardPasswordEncoder("secret");


        // Verify if the user have already an account
        def req = """SELECT * from lms_user where lti_user_id=$user_id;"""
        def row = sql.firstRow(req)
        if(row == null) {
            // if not create an account for it
            username = firstName.substring(0,1)+lastName.substring(0,3)
            // check if the new username is not already use
            def checkUsername = "SELECT username from user where username LIKE '"+username+"%' order by username desc;"
            def userRow = sql.firstRow(checkUsername)
            if(userRow != null) {
                int number = 2
                if(userRow.username.length() > 4) {
                    number = Integer.parseInt(userRow.username.substring(4))
                    number++
                }
                username = username+number
            }

            // create password for user
            def alphabet = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789"
            Random rand = new Random()
            password = ""
            for(int i = 0; i < 8; i++) {
                password += alphabet.charAt(rand.nextInt(alphabet.length()))
            }
            password = encoder.encode(password);

            // add user in database
            def insertUser = """ INSERT INTO user (account_expired,account_locked,
                                 email,enabled,first_name,last_name,normalized_username,
                                 password,password_expired,username,version,language) VALUES (0,0,$email,1,$firstName,$lastName,$username,$password,0,$username,0,'en');"""
            sql.execute(insertUser)

            def getNewUserId = "Select id from user where username = $username;"
            def userId = sql.firstRow(getNewUserId).id

            def insertUserRole
            if(role){
                insertUserRole = """INSERT INTO user_role VALUES (2,$userId); """
            }
            else{
                insertUserRole = """INSERT INTO user_role VALUES (3,$userId); """
            }
            sql.execute(insertUserRole)

            def insertLms = """INSERT INTO lms_user VALUES ($userId,$key,$user_id);"""
            sql.execute(insertLms)
        }
        else {
            // get the user username and password to connect it
            def getUsernameAndPassword = """Select username,password from user,lms_user where tsaap_user_id = id and lti_user_id = $user_id;"""
            def user = sql.firstRow(getUsernameAndPassword)
            username = user.username
            password = user.password
        }
        sql.close()
        // Connect it
        springSecurityService.reauthenticate(username,password)
    }
}
