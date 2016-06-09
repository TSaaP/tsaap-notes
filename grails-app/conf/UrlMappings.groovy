import org.tsaap.lti.LtiContextInitialisationException
import org.tsaap.lti.LtiUserException

/*
 * Copyright 2013 Tsaap Development Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }

        "/scope/$action?/$id?"(controller: 'context')

        "/"(view: "/index")
        "/terms"(view: "/terms")
        "/lti/terms"(view: "/lti/terms")
        "500"(view: '/error')
        "500"(view: '/lti/error', exception: LtiContextInitialisationException)
        "500"(view: '/lti/error', exception: LtiUserException)
    }
}
