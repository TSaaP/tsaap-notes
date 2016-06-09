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

package org.tsaap.lti;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * Created by dorian on 15/06/15.
 */
public class Utils {
    /**
     * Initialises the application session and database connection.
     *
     * @param session      user session
     * @param checkSession <code>true</code> to check the session for a valid connection
     * @return database object
     */
    public static Db initialise(HttpSession session, Boolean checkSession) {

        boolean ok = true;
        Db db = null;

        if ((checkSession != null) && checkSession) {
            ok = ok && (session.getAttribute("consumer_key") != null);
            ok = ok && (session.getAttribute("resource_id") != null);
            ok = ok && (session.getAttribute("user_consumer_key") != null);
            ok = ok && (session.getAttribute("user_id") != null);
            ok = ok && (session.getAttribute("isStudent") != null);
        }
        if (!ok) {
            session.setAttribute("error_message", "Unable to open session.");
        } else {
            // Open database connection
            try {
                db = new Db();
            } catch (SQLException e) {
                ok = false;
                if ((checkSession != null) && checkSession) {
                    // Display a more user-friendly error message to LTI users
                    session.setAttribute("error_message", "Unable to open database.");
                } else {
                    session.setAttribute("error_message", e.getMessage());
                }
            }
        }
        if (!ok) {
            db = null;
        }
        return db;
    }
}
