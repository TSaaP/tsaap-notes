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
     * @param session user session
     * @param checkSession <code>true</code> to check the session for a valid connection
     *
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
