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

modules = {
    application {
        resource url: 'js/application.js'
    }

    tsaap_ui {
        dependsOn('jquery')
        resource url: 'js/moment.js'
        resource url: 'js/locale/fr.js'
        resource url: 'js/bootstrap.min.js'
        resource url: 'css/bootstrap.min.css'
        resource url: 'js/bootstrap-datetimepicker.js'
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