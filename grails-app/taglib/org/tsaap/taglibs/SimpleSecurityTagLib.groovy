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

package org.tsaap.taglibs

import grails.plugins.springsecurity.SpringSecurityService
import org.tsaap.directory.User

class SimpleSecurityTagLib {

    //static defaultEncodeAs = 'html'
    //static encodeAsForTags = [tagName: 'raw']
    static namespace = "tsaap"

    SpringSecurityService springSecurityService

    /**
     * Renders the body if the user is authenticated.  */
    def ifLoggedIn = { attrs, body ->
        if (springSecurityService.isLoggedIn()) {
            out << body()
        }
    }

    /**
     * Renders the body if the user is not authenticated.  */
    def ifNotLoggedIn = { attrs, body ->
        if (!springSecurityService.isLoggedIn()) {
            out << body()
        }
    }

    /**
     * Renders the body if the user is authorized to create users
     */
    def ifUserOwner = { attrs, body ->
        User user = springSecurityService.currentUser
        if (user.canBeUserOwner) {
            out << body()
        }
    }

}
