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

modules = {
  application {
    resource url: 'js/application.js'
  }

  tsaap_ui {
    dependsOn('jquery')
    resource url: 'js/bootstrap.min.js'
    resource url: 'js/jquery.linkify.js'
    resource url: 'css/bootstrap.min.css'
  }

  tsaap_icons {
    dependsOn('tsaap_ui')
    resource url: 'css/bootstrap-glyphicons.css'
    resource url: 'css/docs.css'
  }

  tsaap_ui_home {
    dependsOn('tsaap_ui')
    resource url: 'css/jumbotron.css'
  }

  tsaap_ui_signin {
    dependsOn('tsaap_ui')
    resource url: 'css/signin.css'
  }

  tsaap_ui_notes {
    dependsOn('tsaap_ui')
    resource url: 'css/notes.css'
  }
}