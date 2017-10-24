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

import org.tsaap.lti.LtiContextInitialisationException
import org.tsaap.lti.LtiUserException
import org.tsaap.skin.SkinUtil

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/scope/$action?/$id?"(controller: 'context')
        "/"(controller: 'index')
        "/elaastic-skin/home"(view: "/elaastic-skin/home")
        "/terms"(controller: 'index', action: 'showTerms')
        "/lti/terms"(controller: 'index', action: 'showLtiTerms')
        "500"(controller: 'index', action: 'showErrorPage')
        "500"(controller: 'index', action: 'showLtiErrorPage', exception: LtiContextInitialisationException)
        "500"(controller: 'index', action: 'showLtiErrorPage', exception: LtiUserException)
        "404"(controller: 'index', action: 'notFound')

        "/ckeditor/config.js"(view: '/staticjs/ckeditorconfig')
    }
}
