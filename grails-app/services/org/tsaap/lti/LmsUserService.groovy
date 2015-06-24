package org.tsaap.lti

import grails.plugins.springsecurity.SpringSecurityService
import groovy.sql.Sql
import org.springframework.security.crypto.password.StandardPasswordEncoder

import java.sql.Connection

class LmsUserService {

    SpringSecurityService springSecurityService

    /**
     * Find or create a tsaap account for a given lti user
     * @param ltiUserId lti user id
     * @param firstName lti user first name
     * @param lastName lti user last name
     * @param email lti user email
     * @param key lti consumer key
     * @param role true if the lti user is a learner else false
     */
    def findOrCreateUser(Sql sql, String ltiUserId, String firstName, String lastName, String email, String key, Boolean role) {
        LmsUserHelper lmsUserHelper = new LmsUserHelper()
        springSecurityService = new SpringSecurityService()
        def username
        def password
        StandardPasswordEncoder encoder = new StandardPasswordEncoder("secret")

        // Verify if the user have already an account
        def result = lmsUserHelper.selectLmsUser(sql,ltiUserId)
        if(result == null) {
            // If not, create an account for it
            username = firstName.substring(0,1)+lastName.substring(0,3)
            // Check if the new username is not already use
            def checkUsername = lmsUserHelper.selectUsernameIfExist(sql,username)
            if(checkUsername != null) {
                int number = 2
                if(checkUsername.length() > 4) {
                    number = Integer.parseInt(checkUsername.substring(4))
                    number++
                }
                username = username+number
            }

            // Create password for user
            def alphabet = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789"
            Random rand = new Random()
            password = ""
            for(int i = 0; i < 8; i++) {
                password += alphabet.charAt(rand.nextInt(alphabet.length()))
            }
            password = encoder.encode(password)

            // Add user in database
            lmsUserHelper.insertUserInDatabase(sql,email,firstName,lastName,username,password)
            Long tsaapUserId = lmsUserHelper.selectUserId(sql,username)
            if(role){
                lmsUserHelper.insertUserRoleInDatabase(sql,2,tsaapUserId)
            }
            else {
                lmsUserHelper.insertUserRoleInDatabase(sql,3,tsaapUserId)
            }
            lmsUserHelper.insertLmsUserInDatabase(sql,tsaapUserId,key,ltiUserId)

        }
        else {
            // Get the user username and password to connect it
            def user = lmsUserHelper.selectUsernameAndPassword(sql,ltiUserId)
            username = user.username
            password = user.password
        }
        // Connect it
        springSecurityService.reauthenticate(username,password)
    }
}
