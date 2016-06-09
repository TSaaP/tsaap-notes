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
 * Class to represent a resource link share key.
 * <p>
 * A share key is a value which is generated to enable a different resource
 * link to be treated as equivalent to the issuing resource link.
 *
 * @author Stephen P Vickers
 * @version 1.1.01 (18-Jun-13)
 */
public class ResourceLinkShareKey {

    /**
     * Maximum permitted life for a share key value.
     */
    private static final int MAX_SHARE_KEY_LIFE = 168;  // in hours (1 week)
    /**
     * Default life for a share key value.
     */
    private static final int DEFAULT_SHARE_KEY_LIFE = 24;  // in hours
    /**
     * Minimum length for a share key value.
     */
    private static final int MIN_SHARE_KEY_LENGTH = 5;
    /**
     * Maximum length for a share key value.
     */
    private static final int MAX_SHARE_KEY_LENGTH = 32;
    /**
     * Consumer key for resource link being shared.
     */
    private String primaryConsumerKey = null;
    /**
     * Resource link ID being shared.
     */
    private String primaryResourceLinkId = null;
    /**
     * Length of share key.
     */
    private Integer length = null;
    /**
     * Life of share key.
     */
    private Integer life = null;  // in hours
    /**
     * True if the sharing arrangement should be automatically approved when first used.
     */
    private boolean autoApprove = false;
    /**
     * Date/time when the share key expires.
     */
    private Calendar expires = null;
    /**
     * Share key value.
     */
    private String id = null;
    /**
     * Data connector.
     */
    private DataConnector dataConnector = null;

    /**
     * Construct a share key for a resource link with a specified ID (may be null if
     * a random ID is to be generated).
     *
     * @param resourceLink ResourceLink object
     * @param id           share key value
     */
    public ResourceLinkShareKey(ResourceLink resourceLink, String id) {

        this.initialise();
        this.dataConnector = resourceLink.getConsumer().getDataConnector();
        this.id = id;
        if ((id != null) && (id.length() > 0)) {
            this.load();
        } else {
            this.primaryConsumerKey = resourceLink.getKey();
            this.primaryResourceLinkId = resourceLink.getId();
        }

    }

    /**
     * Initialise the resource link share key.
     */
    public final void initialise() {

        this.primaryConsumerKey = null;
        this.primaryResourceLinkId = null;
        this.length = null;
        this.life = null;
        this.autoApprove = false;
        this.expires = null;

    }

    /**
     * Save the resource link share key to the database.
     *
     * @return <code>true</code> if the share key was successfully saved
     */
    public boolean save() {

        if (this.life == null) {
            this.life = DEFAULT_SHARE_KEY_LIFE;
        } else {
            this.life = Math.max(Math.min(this.life, MAX_SHARE_KEY_LIFE), 0);
        }
        this.expires = Calendar.getInstance();
        this.expires.add(Calendar.HOUR_OF_DAY, this.life);
        if (this.id == null) {
            if (this.length == null) {
                this.length = MAX_SHARE_KEY_LENGTH;
            } else {
                this.length = Math.max(Math.min(this.length, MAX_SHARE_KEY_LENGTH), MIN_SHARE_KEY_LENGTH);
            }
            this.id = Utils.getRandomString(this.length);
        }

        return this.dataConnector.saveResourceLinkShareKey(this);

    }

    /**
     * Delete the resource link share key from the database.
     *
     * @return <code>true</code> if the share key was successfully deleted
     */
    public boolean delete() {

        return this.dataConnector.deleteResourceLinkShareKey(this);

    }

    /**
     * Returns the share key value.
     *
     * @return share key value
     */
    public String getId() {

        return this.id;

    }

    /**
     * Returns the length to use when generating an ID.
     *
     * @return length
     */
    public Integer getLength() {
        return this.length;
    }

    /**
     * Set the length to use when generating a random ID value.
     *
     * @param length
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * Returns the life of the share key (the period of time after creation when
     * the share key will automatically expire).
     *
     * @return life
     */
    public Integer getLife() {
        return this.life;
    }

    /**
     * Returns the date/time when the share key expires.
     *
     * @return expiry date/time
     */
    public Calendar getExpires() {
        Calendar calendar = null;
        if (this.expires != null) {
            calendar = (Calendar) this.expires.clone();
        }
        return calendar;
    }

    /**
     * Set the expiry date/time.
     *
     * @param expires expiry date/time
     */
    public void setExpires(Calendar expires) {
        if (expires != null) {
            this.expires = (Calendar) expires.clone();
        } else {
            this.expires = null;
        }
    }

    /**
     * Returns <code>true</code> if a used share key should be automatically approved
     * and be given access to the issuing resource link.
     *
     * @return <code>true</code> if the share key is to be auto-approved when used
     */
    public boolean isAutoApprove() {
        return this.autoApprove;
    }

    /**
     * Sets whether the share key should be automatically approved when used.
     *
     * @param autoApprove <code>true</code> if the share key should be auto approved
     */
    public void setAutoApprove(boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    /**
     * Returns the consumer key for the resource link which issued the share key.
     *
     * @return primary consumer key
     */
    public String getPrimaryConsumerKey() {
        return this.primaryConsumerKey;
    }

    /**
     * Set consumer key representing the issuing resource link.
     *
     * @param primaryConsumerKey primary consumer key
     */
    public void setPrimaryConsumerKey(String primaryConsumerKey) {
        this.primaryConsumerKey = primaryConsumerKey;
    }

    /**
     * Returns the resource link ID for the resource link which issued the share key.
     *
     * @return primary resource link ID
     */
    public String getPrimaryResourceLinkId() {
        return this.primaryResourceLinkId;
    }

    /**
     * Set ID for the issuing resource link.
     *
     * @param primaryResourceLinkId primary resource link ID
     */
    public void setPrimaryResourceLinkId(String primaryResourceLinkId) {
        this.primaryResourceLinkId = primaryResourceLinkId;
    }

///
///  PRIVATE METHOD
///

    /**
     * Load the resource link share key from the database.
     */
    private void load() {

        this.initialise();
        this.dataConnector.loadResourceLinkShareKey(this);
        if (this.id != null) {
            this.length = this.id.length();
        }
        if (this.expires != null) {
            this.life = Math.round((this.expires.getTimeInMillis() - System.currentTimeMillis()) / 60 / 60 / 1000);
        }

    }

}
