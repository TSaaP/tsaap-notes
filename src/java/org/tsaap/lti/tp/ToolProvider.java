/*
    LTIToolProvider - Classes to handle connections with an LTI 1 compliant tool consumer
    Copyright (C) 2013  Stephen P Vickers

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

    Contact: stephen@spvsoftwareproducts.com

    Version history:
      1.0.00   2-Jan-13  Initial release
      1.1.00  13-Apr-13  Added option for error callback method
                         Updated to support latest release of OAuth class library
      1.1.01  18-Jun-13  Altered order of checks in authenticate
                         Fixed bug with not updating a resource link before redirecting to a shared resource link
                         Fixed bug causing user records not to be saved
*/
package org.tsaap.lti.tp;

import net.oauth.*;
import org.tsaap.lti.tp.net.oauth.server.OAuthServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


/**
 * Class to represent an LTI Tool Provider.
 *
 * @author Stephen P Vickers
 * @version 1.1.01 (18-Jun-13)
 */
public class ToolProvider {

    /**
     * LTI version for messages.
     */
    public static final String LTI_VERSION = "LTI-1p0";
    /**
     * Use ID value only.
     */
    public static final int ID_SCOPE_ID_ONLY = 0;
    /**
     * Prefix an ID with the consumer key.
     */
    public static final int ID_SCOPE_GLOBAL = 1;
    /**
     * Prefix the ID with the consumer key and context ID.
     */
    public static final int ID_SCOPE_CONTEXT = 2;
    /**
     * Prefix the ID with the consumer key and resource link ID.
     */
    public static final int ID_SCOPE_RESOURCE = 3;
    /**
     * Character used to separate each element of an ID.
     */
    public static final char ID_SCOPE_SEPARATOR = ':';

    /**
     * Default error message for invalid launch requests.
     */
    private static final String CONNECTION_ERROR_MESSAGE = "Sorry, there was an error connecting you to the application.";

    /**
     * True if the last request was successful.
     */
    private boolean ok = true;
    /**
     * ToolConsumer object.
     */
    private ToolConsumer consumer = null;
    /**
     * Return URL provided by tool consumer.
     */
    private String returnUrl = null;
    /**
     * User object.
     */
    private User user = null;
    /**
     * ResourceLink object.
     */
    private ResourceLink resourceLink = null;
    /**
     * Data connector object.
     */
    private DataConnector dataConnector = null;
    /**
     * Default email domain.
     */
    private String defaultEmail = "";
    /**
     * Scope to use for user IDs.
     */
    private int idScope = ID_SCOPE_ID_ONLY;
    /**
     * True if shared resource links arrangements are permitted.
     */
    private boolean allowSharing = false;
    /**
     * Message for last request processed.
     */
    private String message = CONNECTION_ERROR_MESSAGE;
    /**
     * Error message for last request processed.
     */
    private String reason = null;
    /**
     * User HTTP request object.
     */
    private HttpServletRequest request = null;
    /**
     * User HTTP response object.
     */
    private HttpServletResponse response = null;
    /**
     * URL to redirect user to on successful completion of the request.
     */
    private String redirectUrl = null;
    /**
     * Callback functions for handling launch requests.
     */
    private Map<String, Callback> callbackHandlers = null;
    /**
     * URL to redirect user to if the request is not successful.
     */
    private String error = null;
    /**
     * True if debug messages explaining the cause of errors are to be returned to the tool consumer.
     */
    private boolean debugMode = false;
    /**
     * Array of LTI parameters for auto validation checks.
     */
    private Map<String, ParameterConstraint> parameterConstraints = null;
    /**
     * Names of LTI parameters to be retained in the settings property.
     */
    private List<String> ltiSettingsNames = new ArrayList<String>() {
        private static final long serialVersionUID = 3109256773218160485L;

        {
            add("ext_resource_link_content");
            add("ext_resource_link_content_signature");
            add("lis_result_sourcedid");
            add("lis_outcome_service_url");
            add("ext_ims_lis_basic_outcome_url");
            add("ext_ims_lis_resultvalue_sourcedids");
            add("ext_ims_lis_memberships_id");
            add("ext_ims_lis_memberships_url");
            add("ext_ims_lti_tool_setting");
            add("ext_ims_lti_tool_setting_id");
            add("ext_ims_lti_tool_setting_url");
        }
    };


    /**
     * Class to represent a launch parameter constraint
     */
    class ParameterConstraint {

        /**
         * <code>true</code> if the parameter is required
         */
        private boolean required;
        /**
         * Maximum permitted length of parameter value, null if any length is acceptable
         */
        private Integer maxLength;

        /**
         * Constructs a ParameterConstraint using the specified values for the required
         * and maximum length properties.
         */
        public ParameterConstraint(boolean required, Integer maxLength) {
            this.required = required;
            this.maxLength = maxLength;
        }

        /**
         * Returns <code>true</code> if the parameter is required in each launch request.
         *
         * @return <code>true</code> if the parameter is required
         */
        public boolean isRequired() {
            return this.required;
        }

        /**
         * Returns the maximum length permitted for the parameter, may be null.
         *
         * @return maximum length
         */
        public Integer getMaxLength() {
            return this.maxLength;
        }

    }

    /**
     * Constructs a ToolProvider object for handling an LTI launch request.
     *
     * @param request       User's HTTP request object
     * @param response      User's HTTP response object
     * @param dataConnector DataConnector object representing the database connection to use for accessing the tables used by the package
     */
    private ToolProvider(HttpServletRequest request, HttpServletResponse response, DataConnector dataConnector) {

        this.request = request;
        this.response = response;
        this.callbackHandlers = new HashMap<String, Callback>();
        this.dataConnector = dataConnector;
        this.parameterConstraints = new HashMap<String, ParameterConstraint>();

    }

    /**
     * Constructs a ToolProvider object for handling an LTI launch request.
     *
     * @param request         User's HTTP request object
     * @param response        User's HTTP response object
     * @param callbackHandler Callback function for a connect request
     * @param dataConnector   DataConnector object representing the database connection to use for accessing the tables used by the package
     */
    public ToolProvider(HttpServletRequest request, HttpServletResponse response,
                        Callback callbackHandler, DataConnector dataConnector) {

        this(request, response, dataConnector);
        this.callbackHandlers.put("connect", callbackHandler);

    }

    /**
     * Constructs a ToolProvider object for handling an LTI launch request.
     *
     * @param request          User's HTTP request object
     * @param response         User's HTTP response object
     * @param callbackHandlers Map containing callback functions for each request type (e.g. connect, error)
     * @param dataConnector    DataConnector object representing the database connection to use for accessing the tables used by the package
     */
    public ToolProvider(HttpServletRequest request, HttpServletResponse response,
                        Map<String, Callback> callbackHandlers, DataConnector dataConnector) {

        this(request, response, dataConnector);
        this.callbackHandlers.putAll(callbackHandlers);

    }

    /**
     * Process a launch request
     *
     * @return <code>true</code> if the launch request was successful
     */
    public boolean execute() {

//
/// Set return URL if available
//
        this.returnUrl = this.request.getParameter("launch_presentation_return_url");
//
/// Perform action
//
        if (this.authenticate()) {
            this.doCallback();
        }
        this.result();

        return this.ok;

    }

    /**
     * Returns <code>true</code> if no errors have been encountered.
     *
     * @return <code>true</code> if there have not been any errors
     */
    public boolean isOk() {
        return ok;
    }

    /**
     * Returns the tool consumer object relating to the source of the launch request.
     *
     * @return ToolConsumer object
     */
    public ToolConsumer getConsumer() {
        return consumer;
    }

    /**
     * Returns the User object relating to the user performing launch request.
     *
     * @return user object
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Returns the HTTP request object relating to the current launch request.
     *
     * @return HTTP request object
     */
    public HttpServletRequest getRequest() {
        return this.request;
    }

    /**
     * Returns the ResourceLink object relating to the source of the launch request.
     *
     * @return ResourceLink object
     */
    public ResourceLink getResourceLink() {
        return resourceLink;
    }

    /**
     * Returns the URL provided on launch to use when returning a user to the tool consumer.
     *
     * @return return URL
     */
    public String getReturnUrl() {
        return returnUrl;
    }

    /**
     * Returns the URL to which the user should be redirected on successful completion
     * of processing the request.
     *
     * @return redirect URL
     */
    public String getRedirectUrl() {
        return this.redirectUrl;
    }

    /**
     * Set the URL to which users should be redirected on successful completion of a
     * launch request.
     *
     * @param redirectUrl redirect URL, may be null
     */
    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    /**
     * Returns the default email domain to be used when auto-generating email addresses.
     * <p>
     * When the domain starts with <code>@</code> the user's ID will be added as a prefix when
     * generating an email address.
     *
     * @return default email domain
     */
    public String getDefaultEmail() {
        return defaultEmail;
    }

    /**
     * Set the the default email domain to be used when auto-generating email addresses.
     * <p>
     * When the domain starts with <code>@</code> the user's ID will be added as a prefix when
     * generating an email address.
     *
     * @param defaultEmail default email domain
     */
    public void setDefaultEmail(String defaultEmail) {
        this.defaultEmail = defaultEmail;
    }

    /**
     * Returns the default scope to use when generating user IDs.
     * <p>
     * The value is one of the pre-defined constants: <code>ID_SCOPE_ID_ONLY</code>,
     * <code>ID_SCOPE_GLOBAL</code>, <code>ID_SCOPE_CONTEXT</code>,
     * <code>ID_SCOPE_RESOURCE</code>.
     *
     * @return default scope
     */
    public int getIdScope() {
        return idScope;
    }

    /**
     * Set the default scope to use when generating user IDs.
     * <p>
     * The value should be one of the pre-defined constants: <code>ID_SCOPE_ID_ONLY</code>,
     * <code>ID_SCOPE_GLOBAL</code>, <code>ID_SCOPE_CONTEXT</code>,
     * <code>ID_SCOPE_RESOURCE</code>.
     *
     * @param idScope default scope
     */
    public void setIdScope(int idScope) {
        this.idScope = idScope;
    }

    /**
     * Returns <code>true</code> if the sharing facility is to be permitted.
     * <p>
     * Sharing allows launches from different resource link IDs to all be directed
     * to the same one.
     *
     * @return <code>true</code> if sharing is permitted
     */
    public boolean isAllowSharing() {
        return allowSharing;
    }

    /**
     * Set whether the sharing facility is permitted for launch requests.
     * <p>
     * Sharing allows launches from different resource link IDs to all be directed
     * to the same one.
     *
     * @param allowSharing <code>true</code> if sharing is permitted
     */
    public void setAllowSharing(boolean allowSharing) {
        this.allowSharing = allowSharing;
    }

    /**
     * Add a parameter constraint to be validated on launch.
     * <p>
     * A parameter may be validated based on its presence (required) and/or
     * a specified maximum length.
     *
     * @param name      Name of parameter to be validated
     * @param required  True if parameter is required
     * @param maxLength Maximum permitted length of parameter value (optional, default is NULL)
     */
    public void setParameterConstraint(String name, boolean required, Integer maxLength) {

        name = name.trim();
        if (name.length() > 0) {
            this.parameterConstraints.put(name, new ParameterConstraint(required, maxLength));
        }

    }

    /**
     * Returns a default message returned from an invalid launch.
     *
     * @return message string
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Set the default message to return on an invalid launch.
     *
     * @param message message string
     */
    public void setMessage(String message) {
        if (message != null) {
            this.message = message;
        } else {
            this.message = CONNECTION_ERROR_MESSAGE;
        }
    }

    /**
     * Returns an explanation for the error identified in the launch.
     *
     * @return error description (null if no error arose)
     */
    public String getReason() {
        return this.reason;
    }

    /**
     * Set the explanation for an error identified in the launch.
     *
     * @param reason error description, may be null
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Get a list of tool consumers defined in this tool provider.
     *
     * @return list of ToolConsumer objects
     */
    public List<ToolConsumer> getConsumers() {

        return this.dataConnector.getToolConsumers();

    }


///
///  PRIVATE METHODS
///

    /**
     * Call any callback method for local handling of validated launch requests.
     * <p>
     * The callback method may set the redirectURL.
     *
     * @return <code>true</code> if no error reported
     */
    private boolean doCallback() {

        Callback callbackHandler = this.callbackHandlers.get("connect");
        if (callbackHandler != null) {
            this.ok = callbackHandler.execute(this);
        }

        return this.ok;

    }

    /**
     * Perform the result of the request.
     * <p>
     * This method normally redirects the user to another URL, possibly returning
     * to the tool consumer with an error message.
     */
    private void result() {

        boolean processed = false;
        if (!this.ok && this.callbackHandlers.containsKey("error")) {
            Callback callbackHandler = this.callbackHandlers.get("error");
            processed = callbackHandler.execute(this);
        }
        if (!processed) {
            processed = true;
            try {
                if (!this.ok) {
//
/// If not valid, return an error message to the tool consumer if a return URL is provided
//
                    if (this.returnUrl != null) {
                        this.error = this.returnUrl;
                        if (this.error.indexOf("?") >= 0) {
                            this.error += '&';
                        } else {
                            this.error += '?';
                        }
                        if (this.debugMode && (this.reason != null)) {
                            this.error += "lti_errormsg=" + Utils.urlEncode("Debug error: " + this.reason);
                        } else {
                            this.error += "lti_errormsg=" + Utils.urlEncode(this.message);
                            if (this.reason != null) {
                                this.error += "&lti_errorlog=" + Utils.urlEncode("Debug error: " + this.reason);
                            }
                        }
                    } else if (this.debugMode) {
                        this.error = this.reason;
                    }
                    if (this.error == null) {
                        this.error = this.message;
                    }
                    if (this.error.startsWith("http://") || this.error.startsWith("https://")) {
                        this.response.sendRedirect(this.error);
                    } else {
                        processed = false;
                    }
                } else if (this.redirectUrl != null) {
                    this.response.sendRedirect(this.redirectUrl);
                } else if (this.returnUrl != null) {
                    this.response.sendRedirect(this.returnUrl);
                } else {
                    processed = false;
                }
                if (!processed) {
                    this.response.sendError(401, this.error);
                }
            } catch (IOException e) {
            }
        }

    }

    /**
     * Check the authenticity of the LTI launch request.
     * <p>
     * The consumer, resource link and user objects will be initialised if the request is valid.
     *
     * @return <code>true</code> if the request has been successfully validated.
     */
    private boolean authenticate() {

//
/// Set debug mode
//
        this.debugMode = (this.request.getParameter("custom_debug") != null) &&
                this.request.getParameter("custom_debug").equalsIgnoreCase("true");
//
/// Get the consumer
//
        boolean doSaveConsumer = false;
// Check all required launch parameters
        this.ok = this.request.getParameter("oauth_consumer_key") != null;
        if (this.ok) {
            this.ok = (this.request.getParameter("lti_message_type") != null) &&
                    this.request.getParameter("lti_message_type").equals("basic-lti-launch-request");
        }
        if (this.ok) {
            this.ok = (this.request.getParameter("lti_version") != null) &&
                    this.request.getParameter("lti_version").equals(LTI_VERSION);
        }
        if (this.ok) {
            this.ok = (this.request.getParameter("resource_link_id") != null) &&
                    this.request.getParameter("resource_link_id").trim().length() > 0;
        }
// Check consumer key
        if (this.ok) {
            this.consumer = new ToolConsumer(this.request.getParameter("oauth_consumer_key"), this.dataConnector, false);
            this.ok = this.consumer.getCreated() != null;
            if (this.debugMode && !this.ok) {
                this.reason = "Invalid consumer key.";
            }
        }
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());
        if (this.ok) {
            if (this.consumer.getLastAccess() == null) {
                doSaveConsumer = true;
            } else {
                Calendar last = this.consumer.getLastAccess();
                doSaveConsumer = doSaveConsumer || (last.get(Calendar.YEAR) != now.get(Calendar.YEAR)) ||
                        (last.get(Calendar.DAY_OF_YEAR) != now.get(Calendar.DAY_OF_YEAR));
            }
            this.consumer.setLastAccess(now);
            OAuthConsumer oAuthConsumer = new OAuthConsumer("about:blank", this.consumer.getKey(), this.consumer.getSecret(), null);
            OAuthAccessor oAuthAccessor = new OAuthAccessor(oAuthConsumer);
            OAuthValidator oAuthValidator = new SimpleOAuthValidator();
            OAuthMessage oAuthMessage = OAuthServlet.getMessage(this.request, null);
            try {
                oAuthValidator.validateMessage(oAuthMessage, oAuthAccessor);
            } catch (Exception e) {
                this.ok = false;
                if (this.reason == null) {
                    this.reason = "OAuth signature check failed - perhaps an incorrect secret or timestamp.";
                }
            }
        }
        if (this.ok && this.consumer.isProtect()) {
            if (this.consumer.getConsumerGUID() != null) {
                this.ok = (this.request.getParameter("tool_consumer_instance_guid") != null) &&
                        (this.request.getParameter("tool_consumer_instance_guid").length() > 0) &&
                        this.consumer.getConsumerGUID().equals(this.request.getParameter("tool_consumer_instance_guid"));
                if (this.debugMode && !this.ok) {
                    this.reason = "Request is from an invalid tool consumer.";
                }
            } else {
                this.ok = this.request.getParameter("tool_consumer_instance_guid") != null;
                if (this.debugMode && !this.ok) {
                    this.reason = "A tool consumer GUID must be included in the launch request.";
                }
            }
        }
        if (this.ok) {
            this.ok = this.consumer.isEnabled();
            if (this.debugMode && !this.ok) {
                this.reason = "Tool consumer has not been enabled by the tool provider.";
            }
        }
        if (this.ok) {
            this.ok = (this.consumer.getEnableFrom() == null) || now.after(this.consumer.getEnableFrom());
            if (this.ok) {
                this.ok = (this.consumer.getEnableUntil() == null) || this.consumer.getEnableUntil().after(now);
                if (this.debugMode && !this.ok) {
                    this.reason = "Tool consumer access has expired.";
                }
            } else if (this.debugMode) {
                this.reason = "Tool consumer access is not yet available.";
            }
        }
// Check nonce value
        if (this.ok) {
            Nonce nonce = new Nonce(this.consumer, this.request.getParameter("oauth_nonce"));
            this.ok = !nonce.load();
            if (this.ok) {
                this.ok = nonce.save();
            }
            if (this.debugMode && !this.ok) {
                this.reason = "Invalid nonce.";
            }
        }
//
/// Validate launch parameters
//
        if (this.ok) {
            List<String> invalidParameters = new ArrayList<String>();
            for (Iterator<String> iter = this.parameterConstraints.keySet().iterator(); iter.hasNext(); ) {
                String name = iter.next();
                ParameterConstraint parameterConstraint = this.parameterConstraints.get(name);
                boolean err = false;
                if (parameterConstraint.isRequired()) {
                    if ((this.request.getParameter(name) == null) || (this.request.getParameter(name).trim().length() <= 0)) {
                        invalidParameters.add(name);
                        err = true;
                    }
                }
                if (!err && (parameterConstraint.getMaxLength() != null) && (this.request.getParameter(name) != null)) {
                    if (this.request.getParameter(name).trim().length() > parameterConstraint.getMaxLength()) {
                        invalidParameters.add(name);
                    }
                }
            }
            if (invalidParameters.size() > 0) {
                this.ok = false;
                if (this.reason == null) {
                    StringBuilder msg = new StringBuilder("Invalid parameter(s): ");
                    for (int i = 0; i < invalidParameters.size(); i++) {
                        if (i > 0) {
                            msg.append(", ");
                        }
                        msg.append(invalidParameters.get(i));
                    }
                    this.reason = msg.toString();
                }
            }
        }

        if (this.ok) {
            this.consumer.setDefaultEmail(this.defaultEmail);
//
/// Set the request context/resource link
//
            this.resourceLink = new ResourceLink(this.consumer, this.request.getParameter("resource_link_id").trim());
            if (this.request.getParameter("context_id") != null) {
                this.resourceLink.setLtiContextId(this.request.getParameter("context_id").trim());
            }
            this.resourceLink.setLtiResourceLinkId(this.request.getParameter("resource_link_id").trim());
            StringBuilder title = new StringBuilder();
            if (this.request.getParameter("context_title") != null) {
                title.append(this.request.getParameter("context_title").trim());
            }
            if ((this.request.getParameter("resource_link_title") != null) &&
                    (this.request.getParameter("resource_link_title").trim().length() > 0)) {
                if (title.length() > 0) {
                    title.append(": ");
                }
                title.append(this.request.getParameter("resource_link_title").trim());
            }
            if (title.length() <= 0) {
                title.append("Course ").append(this.resourceLink.getId());
            }
            this.resourceLink.setTitle(title.toString());
// Save LTI parameters
            for (Iterator<String> iter = this.ltiSettingsNames.iterator(); iter.hasNext(); ) {
                String name = iter.next();
                this.resourceLink.setSetting(name, this.request.getParameter(name));
            }
// Delete any existing custom parameters
            Map<String, String> settings = this.resourceLink.getSettings();
            for (Iterator<String> iter = settings.keySet().iterator(); iter.hasNext(); ) {
                String name = iter.next();
                if (name.startsWith("custom_")) {
                    this.resourceLink.setSetting(name, null);
                }
            }
// Save custom parameters
            for (Iterator<String> iter = settings.keySet().iterator(); iter.hasNext(); ) {
                String name = iter.next();
                if (name.startsWith("custom_")) {
                    this.resourceLink.setSetting(name, settings.get(name));
                }
            }
//
/// Set the user instance
//
            String userId = "";
            if (this.request.getParameter("user_id") != null) {
                userId = this.request.getParameter("user_id").trim();
            }
            this.user = new User(this.resourceLink, userId);
//
/// Set the user name
//
            String firstname = "";
            if (this.request.getParameter("lis_person_name_given") != null) {
                firstname = this.request.getParameter("lis_person_name_given");
            }
            String lastname = "";
            if (this.request.getParameter("lis_person_name_family") != null) {
                lastname = this.request.getParameter("lis_person_name_family");
            }
            String fullname = "";
            if (this.request.getParameter("lis_person_name_full") != null) {
                fullname = this.request.getParameter("lis_person_name_full");
            }
            this.user.setNames(firstname, lastname, fullname);
//
/// Set the user email
//
            String email = "";
            if (this.request.getParameter("lis_person_contact_email_primary") != null) {
                email = this.request.getParameter("lis_person_contact_email_primary");
            }
            this.user.setEmail(email, this.defaultEmail);
//
/// Set the user roles
//
            if (this.request.getParameter("roles") != null) {
                this.user.setRoles(this.request.getParameter("roles"));
            }
//
/// Save the user instance
//
            if (this.request.getParameter("lis_result_sourcedid") != null) {
                if (!this.request.getParameter("lis_result_sourcedid").equals(this.user.getLtiResultSourcedId())) {
                    this.user.setLtiResultSourcedId(this.request.getParameter("lis_result_sourcedid"));
                    this.user.save();
                }
            } else if (this.user.getLtiResultSourcedId() != null) {
                this.user.delete();
            }
//
/// Initialise the consumer and check for changes
//
            if (!this.request.getParameter("lti_version").equals(this.consumer.getLtiVersion())) {
                this.consumer.setLtiVersion(this.request.getParameter("lti_version"));
                doSaveConsumer = true;
            }
            if (this.request.getParameter("tool_consumer_instance_name") != null) {
                if (!this.request.getParameter("tool_consumer_instance_name").equals(this.consumer.getConsumerName())) {
                    this.consumer.setConsumerName(this.request.getParameter("tool_consumer_instance_name"));
                    doSaveConsumer = true;
                }
            }
            if (this.request.getParameter("tool_consumer_info_product_family_code") != null) {
                String version = this.request.getParameter("tool_consumer_info_product_family_code");
                if (this.request.getParameter("tool_consumer_info_version") != null) {
                    version += "-" + this.request.getParameter("tool_consumer_info_version");
                }
// do not delete any existing consumer version if none is passed
                if (!version.equals(this.consumer.getConsumerVersion())) {
                    this.consumer.setConsumerVersion(version);
                    doSaveConsumer = true;
                }
            } else if ((this.request.getParameter("ext_lms") != null) &&
                    !this.request.getParameter("ext_lms").equals(this.consumer.getConsumerName())) {
                this.consumer.setConsumerVersion(this.request.getParameter("ext_lms"));
                doSaveConsumer = true;
            }
            if ((this.request.getParameter("tool_consumer_instance_guid") != null) &&
                    (this.consumer.getConsumerGUID() == null)) {
                this.consumer.setConsumerGUID(this.request.getParameter("tool_consumer_instance_guid"));
                doSaveConsumer = true;
            }
            if (this.request.getParameter("launch_presentation_css_url") != null) {
                if (!this.request.getParameter("launch_presentation_css_url").equals(this.consumer.getCssPath())) {
                    this.consumer.setCssPath(this.request.getParameter("launch_presentation_css_url"));
                    doSaveConsumer = true;
                }
            } else if ((this.request.getParameter("ext_launch_presentation_css_url") != null) &&
                    !this.request.getParameter("ext_launch_presentation_css_url").equals(this.consumer.getCssPath())) {
                this.consumer.setCssPath(this.request.getParameter("ext_launch_presentation_css_url"));
                doSaveConsumer = true;
            } else if (this.consumer.getCssPath() != null) {
                this.consumer.setCssPath(null);
                doSaveConsumer = true;
            }
        }
//
/// Persist changes to consumer
//
        if (doSaveConsumer) {
            this.consumer.save();
        }

        if (this.ok) {
//
/// Check if a share arrangement is in place for this resource link
//
            this.ok = this.checkForShare();
//
/// Persist changes to resource link
//
            this.resourceLink.save();
        }

        return this.ok;

    }

    /**
     * Check if a share arrangement is in place.
     *
     * @return <code>true</code> if no error is reported
     */
    private boolean checkForShare() {

        boolean isOk = true;
        boolean doSaveResourceLink = true;

        String key = this.resourceLink.getPrimaryConsumerKey();
        String id = this.resourceLink.getPrimaryResourceLinkId();

        String shareKeyValue = this.request.getParameter("custom_share_key");
        boolean isShareRequest = (shareKeyValue != null) && (shareKeyValue.length() > 0);
        if (isShareRequest) {
            if (!this.allowSharing) {
                isOk = false;
                this.reason = "Your sharing request has been refused because sharing is not being permitted.";
            } else {
// Check if this is a new share key
                ResourceLinkShareKey shareKey = new ResourceLinkShareKey(this.resourceLink, shareKeyValue);
                if ((shareKey.getPrimaryConsumerKey() != null) && (shareKey.getPrimaryResourceLinkId() != null)) {
// Update resource link with sharing primary resource link details
                    key = shareKey.getPrimaryConsumerKey();
                    id = shareKey.getPrimaryResourceLinkId();
                    isOk = !key.equals(this.consumer.getKey()) || !id.equals(this.resourceLink.getId());
                    if (isOk) {
                        this.resourceLink.setPrimaryConsumerKey(key);
                        this.resourceLink.setPrimaryResourceLinkId(id);
                        this.resourceLink.setShareApproved(shareKey.isAutoApprove());
                        isOk = this.resourceLink.save();
                        if (isOk) {
                            doSaveResourceLink = false;
                            this.user.getResourceLink().setPrimaryConsumerKey(key);
                            this.user.getResourceLink().setPrimaryResourceLinkId(id);
                            this.user.getResourceLink().setShareApproved(shareKey.isAutoApprove());
                            this.user.getResourceLink().setUpdated(Calendar.getInstance());
// Remove share key
                            shareKey.delete();
                        } else {
                            this.reason = "An error occurred initialising your share arrangement.";
                        }
                    } else {
                        this.reason = "It is not possible to share your resource link with yourself.";
                    }
                }
                if (isOk) {
                    isOk = key != null;
                    if (!isOk) {
                        this.reason = "You have requested to share a resource link but none is available.";
                    } else {
                        isOk = (this.user.getResourceLink().getShareApproved() != null) && this.user.getResourceLink().getShareApproved();
                        if (!isOk) {
                            this.reason = "Your share request is waiting to be approved.";
                        }
                    }
                }
            }
        } else {
// Check no share is in place
            isOk = key == null;
            if (!isOk) {
                this.reason = "You have not requested to share a resource link but an arrangement is currently in place.";
            }
        }

// Look up primary resource link
        if (isOk && (key != null)) {
            ToolConsumer primaryConsumer = new ToolConsumer(key, this.dataConnector, false);
            ResourceLink primaryResourceLink = null;
            isOk = primaryConsumer.getCreated() != null;
            if (isOk) {
                primaryResourceLink = new ResourceLink(primaryConsumer, id);
                isOk = primaryResourceLink.getCreated() != null;
            }
            if (isOk) {
                if (doSaveResourceLink) {
                    this.resourceLink.save();
                }
                this.resourceLink = primaryResourceLink;
            } else {
                this.reason = "Unable to load resource link being shared.";
            }
        }

        return isOk;

    }

}
