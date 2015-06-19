package org.tsaap.lti;

import org.tsaap.lti.tp.Callback;
import org.tsaap.lti.tp.DataConnector;
import org.tsaap.lti.tp.ToolProvider;
import org.tsaap.lti.tp.dataconnector.JDBC;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


/**
 * Created by dorian on 15/06/15.
 */
public class Launch extends HttpServlet implements Callback {

    private static final long serialVersionUID = 7955577706944298060L;
    Db db;

    @Override
    public boolean execute(ToolProvider toolProvider) {

        // Check the user has an appropriate role
        boolean ok = toolProvider.getUser().isLearner() || toolProvider.getUser().isStaff();
        if (ok) {

            // Initialise the user session
            toolProvider.getRequest().getSession().setAttribute("consumer_key", toolProvider.getConsumer().getKey());
            toolProvider.getRequest().getSession().setAttribute("resource_id", toolProvider.getResourceLink().getId());
            toolProvider.getRequest().getSession().setAttribute("user_consumer_key",
                    toolProvider.getUser().getResourceLink().getConsumer().getKey());
            toolProvider.getRequest().getSession().setAttribute("user_id", toolProvider.getUser().getId());
            toolProvider.getRequest().getSession().setAttribute("isStudent", toolProvider.getUser().isLearner());

            //Check the user
            LmsUserHelper lms = new LmsUserHelper();
            lms.findUserIsKnowOrCreateIt(db, toolProvider.getUser().getId(), toolProvider.getUser().getFirstname(), toolProvider.getUser().getLastname(),
                    toolProvider.getUser().getEmail(), toolProvider.getConsumer().getKey());

            // Redirect the user to display the list of items for the resource link
            String serverUrl = toolProvider.getRequest().getRequestURL().toString();
            serverUrl = serverUrl.substring(0, serverUrl.lastIndexOf("/"));
            toolProvider.setRedirectUrl(serverUrl);
        } else {
            toolProvider.setReason("Invalid role.");
        }
        try {
            db.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ok;

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        // Cancel any existing session and start a new session
        request.getSession().invalidate();
        request.getSession(true);

        request.setCharacterEncoding("UTF-8");

        // Initialise database
        db = Utils.initialise(request.getSession(), false);

        DataConnector dc = null;
        if (db != null) {
            dc = new JDBC(Config.DB_TABLENAME_PREFIX, db.getConnection());
        }
        ToolProvider tp = new ToolProvider(request, response, this, dc);
        tp.setParameterConstraint("oauth_consumer_key", true, 50);
        tp.setParameterConstraint("resource_link_id", true, 50);
        tp.setParameterConstraint("user_id", true, 50);
        tp.setParameterConstraint("roles", true, null);
        tp.execute();

    }

}
