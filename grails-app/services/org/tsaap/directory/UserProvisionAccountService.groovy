/*
 * Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.tsaap.directory

import grails.plugins.springsecurity.SpringSecurityService
import groovy.sql.Sql
import org.tsaap.lti.LmsUserHelper

import java.text.Normalizer
import java.util.regex.Pattern

class UserProvisionAccountService {

    SpringSecurityService springSecurityService
    LmsUserHelper lmsUserHelper

    /**
     * Generate an encoded password
     * @return the encoded password
     */
    String generateEncodedPassword() {
        springSecurityService.encodePassword(generatePassword())
    }

    /**
     * Generate a password (not encoded)
     * @return
     */
    String generatePassword() {
        def password = ""
        def alphabet = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789"
        Random rand = new Random()
        for (int i = 0; i < 8; i++) {
            password += alphabet.charAt(rand.nextInt(alphabet.length()))
        }
        password
    }

    /**
     * Generate username from firstname and lastname
     * @param sql the sql connection to check existing username
     * @param firstName the firstname
     * @param lastName the lastname
     * @return
     */
    def generateUsername(Sql sql, String firstName, String lastName) {
        def indexLastname = Math.min(MAX_INDEX_LASTNAME, lastName.length())
        def indexFirstName = Math.min(MAX_INDEX_FIRSTNAME, firstName.length())
        def username = replaceAccent(firstName.replaceAll("\\s","").toLowerCase().substring(0, indexFirstName)) +
                replaceAccent(lastName.replaceAll("\\s","").toLowerCase().substring(0, indexLastname))
        def existingUsername = findMostRecentUsernameStartingWithUsername(sql, username)
        if (existingUsername) {
            def matcher = existingUsername =~ /[0-9]+/
            if (matcher.count == 0) {
                username = username + 2
            } else {
                int number = Integer.parseInt(matcher[0])
                number++
                username = username + number
            }
        }
        username
    }

    private static final MAX_INDEX_LASTNAME = 4
    private static final MAX_INDEX_FIRSTNAME = 3

    /**
     * Replace accents in a string
     * @param str the string to modify
     * @return
     */
    private String replaceAccent(final String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    /**
     * Get the most recent user who begin with username param
     * @param sql the sql object
     * @param username the username
     * @return a username if found else null
     */
    String findMostRecentUsernameStartingWithUsername(Sql sql, String username) {
        def userNameLike = '^' + username + '[0-9]*$'
        def req = sql.firstRow("SELECT username FROM user WHERE username RLIKE $userNameLike ORDER BY username DESC")
        def res = req?.username
        res
    }

}
