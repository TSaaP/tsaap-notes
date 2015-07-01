package org.tsaap.lti;

import grails.plugins.springsecurity.BCryptPasswordEncoder;
import grails.plugins.springsecurity.SpringSecurityService;
import groovy.sql.Sql;
import org.apache.log4j.Logger;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.tsaap.directory.UserProvisionAccountService;
import org.tsaap.lti.tp.Callback;
import org.tsaap.lti.tp.DataConnector;
import org.tsaap.lti.tp.ToolProvider;
import org.tsaap.lti.tp.dataconnector.JDBC;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;


/**
 * Created by dorian on 15/06/15.
 */
public class Launch extends HttpServlet implements Callback {

    private static final long serialVersionUID = 7955577706944298060L;
    Db db;
    LmsUserService lmsUserService;
    LmsContextService lmsContextService;
    private static Logger logger = Logger.getLogger(Launch.class);

    public void initialiseLmsUserService() {
        lmsUserService = new LmsUserService();
        LmsUserHelper lmsUserHelper = new LmsUserHelper();
        UserProvisionAccountService userProvisionAccountService = new UserProvisionAccountService();
        SpringSecurityService springSecurityService = new SpringSecurityService();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        springSecurityService.setPasswordEncoder(bCryptPasswordEncoder);
        lmsUserService.setLmsUserHelper(lmsUserHelper);
        lmsUserService.setSpringSecurityService(springSecurityService);
        lmsUserService.setUserProvisionAccountService(userProvisionAccountService);
        lmsUserService.getUserProvisionAccountService().setLmsUserHelper(lmsUserHelper);
        lmsUserService.getUserProvisionAccountService().setSpringSecurityService(springSecurityService);

    }

    public void initialiseLmsContextService() {
        lmsContextService = new LmsContextService();
        LmsContextHelper lmsContextHelper = new LmsContextHelper();
        lmsContextService.setLmsContextHelper(lmsContextHelper);
        LmsUserHelper lmsUserHelper = new LmsUserHelper();
        lmsContextService.setLmsUserHelper(lmsUserHelper);

    }

    @Override
    public boolean execute(ToolProvider toolProvider) {
            // Check the user has an appropriate role
            boolean isAnUser = toolProvider.getUser().isLearner() || toolProvider.getUser().isStaff();
            if (isAnUser) {
                try {
                    // Initialise the user session
                    toolProvider.getRequest().getSession().setAttribute("consumer_key", toolProvider.getConsumer().getKey());
                    toolProvider.getRequest().getSession().setAttribute("resource_id", toolProvider.getResourceLink().getId());
                    toolProvider.getRequest().getSession().setAttribute("user_consumer_key",
                            toolProvider.getUser().getResourceLink().getConsumer().getKey());
                    toolProvider.getRequest().getSession().setAttribute("user_id", toolProvider.getUser().getId());
                    toolProvider.getRequest().getSession().setAttribute("isStudent", toolProvider.getUser().isLearner());
                    toolProvider.getRequest().getSession().setAttribute("lti_context_id", toolProvider.getResourceLink().getLtiContextId());

                    Connection connection = db.getConnection();
                    Sql sql = new Sql(connection);
                    String consumerKey = toolProvider.getConsumer().getKey();
                    Boolean isLearner = toolProvider.getUser().isLearner();

                    //Check the user
                    initialiseLmsUserService();
                    String username = (String) lmsUserService.findOrCreateUser(sql, toolProvider.getUser().getId(), toolProvider.getUser().getFirstname(), toolProvider.getUser().getLastname(),
                            toolProvider.getUser().getEmail(), consumerKey, isLearner);

                    //Check context
                    initialiseLmsContextService();
                    ArrayList context = (ArrayList) lmsContextService.findOrCreateContext(sql, consumerKey, toolProvider.getResourceLink().getId(), toolProvider.getResourceLink().getLtiContextId(), toolProvider.getConsumer().getConsumerName(),
                            toolProvider.getResourceLink().getTitle(), username, isLearner);
                    // Redirect the user to display the list of items for the resource link
                    String serverUrl = toolProvider.getRequest().getRequestURL().toString();
                    serverUrl = serverUrl.substring(0, serverUrl.lastIndexOf("/"));
                    serverUrl = serverUrl + "/notes/index/?displaysAll=on&contextName=" + context.get(0) + "&contextId=" + context.get(1) + "&kind=standard";
                    toolProvider.setRedirectUrl(serverUrl);
                }
                finally {
                    try {
                        db.closeConnection();
                    } catch (SQLException e) {
                        logger.error(e.getMessage());
                    }
                }
            } else {
                toolProvider.setReason("Invalid role.");
            }
        return isAnUser;

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
