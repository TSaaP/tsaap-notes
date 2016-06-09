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
      1.1.00  13-Apr-13
      1.1.01  18-Jun-13
*/
package org.tsaap.lti.tp;

import java.util.Calendar;


/**
 * Class to represent an LTI User.
 *
 * @author Stephen P Vickers
 * @version 1.1.01 (18-Jun-13)
 */
public class ToolConsumer {

    /**
     * Local name of tool consumer.
     */
    private String name = null;
    /**
     * Shared secret.
     */
    private String secret = null;
    /**
     * LTI version (as reported by last tool consumer connection).
     */
    private String ltiVersion = null;
    /**
     * Name of tool consumer (as reported by last tool consumer connection).
     */
    private String consumerName = null;
    /**
     * Tool consumer version (as reported by last tool consumer connection).
     */
    private String consumerVersion = null;
    /**
     * Tool consumer GUID (as reported by first tool consumer connection).
     */
    private String consumerGUID = null;
    /**
     * Optional CSS path (as reported by last tool consumer connection).
     */
    private String cssPath = null;
    /**
     * True if the tool consumer instance is protected by matching the consumer_guid value in incoming requests.
     */
    private boolean protect = false;
    /**
     * True if the tool consumer instance is enabled to accept incoming connection requests.
     */
    private boolean enabled = false;
    /**
     * Date/time from which the the tool consumer instance is enabled to accept incoming connection requests.
     */
    private Calendar enableFrom = null;
    /**
     * Date/time until which the tool consumer instance is enabled to accept incoming connection requests.
     */
    private Calendar enableUntil = null;
    /**
     * Date of last connection from this tool consumer.
     */
    private Calendar lastAccess = null;
    /**
     * Default scope to use when generating an Id value for a user.
     */
    private int idScope = ToolProvider.ID_SCOPE_ID_ONLY;
    /**
     * Default email address (or email domain) to use when no email address is provided for a user.
     */
    private String defaultEmail = "";
    /**
     * Date/time when the object was created.
     */
    private Calendar created = null;
    /**
     * Date/time when the object was last updated.
     */
    private Calendar updated = null;

    /**
     * Consumer key value.
     */
    private String key = null;
    /**
     * Data connector object or string.
     */
    private DataConnector dataConnector = null;

    /**
     * Class constructor.
     *
     * @param key           consumer key
     * @param dataConnector data connection object
     * @param autoEnable    <code>true</code> if the tool consumers is to be enabled automatically
     */
    public ToolConsumer(String key, DataConnector dataConnector, boolean autoEnable) {

        this.dataConnector = dataConnector;
        if ((key != null) && (key.length() > 0)) {
            this.load(key, autoEnable);
        } else {
            this.secret = Utils.getRandomString(32);
        }

    }

    /**
     * Initialise the tool consumer.
     */
    public void initialise() {

        this.key = null;
        this.name = null;
        this.secret = null;
        this.ltiVersion = null;
        this.consumerName = null;
        this.consumerVersion = null;
        this.consumerGUID = null;
        this.cssPath = null;
        this.protect = false;
        this.enabled = false;
        this.enableFrom = null;
        this.enableUntil = null;
        this.lastAccess = null;
        this.idScope = ToolProvider.ID_SCOPE_ID_ONLY;
        this.defaultEmail = "";
        this.created = null;
        this.updated = null;

    }

    /**
     * Save the tool consumer to the database.
     *
     * @return boolean True if the object was successfully saved
     */
    public boolean save() {

        return this.dataConnector.saveToolConsumer(this);

    }

    /**
     * Delete the tool consumer from the database.
     *
     * @return boolean True if the object was successfully deleted
     */
    public boolean delete() {

        return this.dataConnector.deleteToolConsumer(this);

    }

    /**
     * Get the tool consumer key.
     *
     * @return string Consumer key value
     */
    public String getKey() {

        return this.key;

    }

    public String getConsumerGUID() {
        return this.consumerGUID;
    }

    public void setConsumerGUID(String consumerGUID) {
        this.consumerGUID = consumerGUID;
    }

    public String getConsumerName() {
        return this.consumerName;
    }

    public void setConsumerName(String consumerName) {
        this.consumerName = consumerName;
    }

    public String getConsumerVersion() {
        return this.consumerVersion;
    }

    public void setConsumerVersion(String consumerVersion) {
        this.consumerVersion = consumerVersion;
    }

    public String getCssPath() {
        return this.cssPath;
    }

    public void setCssPath(String cssPath) {
        this.cssPath = cssPath;
    }

    public String getDefaultEmail() {
        return this.defaultEmail;
    }

    public void setDefaultEmail(String defaultEmail) {
        this.defaultEmail = defaultEmail;
    }

    public Calendar getEnableFrom() {
        Calendar calendar = null;
        if (this.enableFrom != null) {
            calendar = (Calendar) this.enableFrom.clone();
        }
        return calendar;
    }

    public void setEnableFrom(Calendar enableFrom) {
        if (enableFrom != null) {
            this.enableFrom = (Calendar) enableFrom.clone();
        } else {
            this.enableFrom = null;
        }
    }

    public Calendar getEnableUntil() {
        Calendar calendar = null;
        if (this.enableUntil != null) {
            calendar = (Calendar) this.enableUntil.clone();
        }
        return calendar;
    }

    public void setEnableUntil(Calendar enableUntil) {
        if (enableUntil != null) {
            this.enableUntil = (Calendar) enableUntil.clone();
        } else {
            this.enableUntil = null;
        }
    }

    public boolean isProtect() {
        return this.protect;
    }

    public void setProtect(boolean protect) {
        this.protect = protect;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getIdScope() {
        return this.idScope;
    }

    public Calendar getLastAccess() {
        Calendar calendar = null;
        if (this.lastAccess != null) {
            calendar = (Calendar) this.lastAccess.clone();
        }
        return calendar;
    }

    public void setLastAccess(Calendar lastAccess) {
        if (lastAccess != null) {
            this.lastAccess = (Calendar) lastAccess.clone();
        } else {
            this.lastAccess = null;
        }
    }

    public String getLtiVersion() {
        return this.ltiVersion;
    }

    public void setLtiVersion(String ltiVersion) {
        this.ltiVersion = ltiVersion;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecret() {
        return this.secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Calendar getCreated() {
        Calendar calendar = null;
        if (this.created != null) {
            calendar = (Calendar) this.created.clone();
        }
        return calendar;
    }

    public void setCreated(Calendar created) {
        if (created != null) {
            this.created = (Calendar) created.clone();
        } else {
            this.created = null;
        }
    }

    public Calendar getUpdated() {
        Calendar calendar = null;
        if (this.updated != null) {
            calendar = (Calendar) this.updated.clone();
        }
        return calendar;
    }

    public void setUpdated(Calendar updated) {
        if (updated != null) {
            this.updated = (Calendar) updated.clone();
        } else {
            this.updated = null;
        }
    }

    /**
     * Get the data connector.
     *
     * @return mixed Data connector object or string
     */
    public DataConnector getDataConnector() {

        return this.dataConnector;

    }

    /**
     * Is the consumer key available to accept launch requests?
     *
     * @return boolean True if the consumer key is enabled and within any date constraints
     */
    public boolean isAvailable() {

        boolean ok = this.enabled;
        Calendar now = Calendar.getInstance();
        if (ok && (this.enableFrom != null)) {
            ok = !this.enableFrom.after(now);
        }
        if (ok && (this.enableUntil != null)) {
            ok = this.enableUntil.after(now);
        }

        return ok;

    }


///
///  PRIVATE METHOD
///

    /**
     * Load the tool consumer from the database.
     *
     * @param string  key        The consumer key value
     * @param boolean autoEnable True if the consumer should be enabled (optional, default if false)
     * @return boolean True if the consumer was successfully loaded
     */
    private boolean load(String key, boolean autoEnable) {

        this.initialise();
        this.key = key;
        boolean ok = this.dataConnector.loadToolConsumer(this);
        if (!ok) {
            this.enabled = autoEnable;
        }

        return ok;

    }

}
