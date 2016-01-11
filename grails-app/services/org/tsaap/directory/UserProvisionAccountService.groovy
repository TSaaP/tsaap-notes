package org.tsaap.directory

import grails.plugins.springsecurity.SpringSecurityService
import groovy.sql.Sql
import org.tsaap.lti.LmsUserHelper

class UserProvisionAccountService {

    SpringSecurityService springSecurityService
    LmsUserHelper lmsUserHelper

    def generatePassword() {
        def password = ""
        def alphabet = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789"
        Random rand = new Random()
        for(int i = 0; i < 8; i++) {
            password += alphabet.charAt(rand.nextInt(alphabet.length()))
        }
        password = springSecurityService.encodePassword(password)
        password
    }

    def generateUsername(Sql sql, String firstName, String lastName) {
        def indexLastname = Math.min(3,lastName.length())
        def username = firstName.substring(0,1)+lastName.substring(0,indexLastname)
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
        username
    }
}
