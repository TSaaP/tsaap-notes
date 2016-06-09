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
package org.tsaap.lti.tp.dataconnector;

import org.tsaap.lti.tp.*;

import java.util.*;


/**
 * Class which implements a dummy data connector with no database persistence.
 *
 * @author Stephen P Vickers
 * @version 1.1.01 (18-Jun-13)
 */
public class None extends DataConnector {

    /**
     * Constructs a dummy data connector object which implements simulated methods
     * without persisting any data.
     */
    public None() {
    }


///
///  ToolConsumer methods
///

    /**
     * Load tool consumer object with a default secret of <code>secret</code> and enabled.
     *
     * @param consumer ToolConsumer object
     * @return <code>true</code> if the tool consumer object was successfully loaded
     */
    @Override
    public boolean loadToolConsumer(ToolConsumer consumer) {
        consumer.setSecret("secret");
        consumer.setEnabled(true);
        Calendar now = Calendar.getInstance();
        consumer.setCreated(now);
        consumer.setUpdated(now);
        return true;
    }

    /**
     * Save tool consumer object.
     *
     * @param consumer ToolConsumer object
     * @return <code>true</code> if the tool consumer object was successfully saved
     */
    @Override
    public boolean saveToolConsumer(ToolConsumer consumer) {
        consumer.setUpdated(Calendar.getInstance());
        return true;
    }

    /**
     * Delete tool consumer object.
     *
     * @param consumer ToolConsumer object
     * @return <code>true</code> if the tool consumer object was successfully deleted
     */
    @Override
    public boolean deleteToolConsumer(ToolConsumer consumer) {
        consumer.initialise();
        return true;
    }

    /**
     * Load tool consumer objects.
     *
     * @return array of all defined ToolConsumer objects
     */
    @Override
    public List<ToolConsumer> getToolConsumers() {
        return new ArrayList<ToolConsumer>();
    }


///
///  ResourceLink methods
///

    /**
     * Load resource link object.
     *
     * @param resourceLink ResourceLink object
     * @return <code>true</code> if the resource link object was successfully loaded
     */
    @Override
    public boolean loadResourceLink(ResourceLink resourceLink) {
        Calendar now = Calendar.getInstance();
        resourceLink.setCreated(now);
        resourceLink.setUpdated(now);
        return true;
    }

    /**
     * Save resource link object.
     *
     * @param resourceLink ResourceLink object
     * @return <code>true</code> if the resource link object was successfully saved
     */
    @Override
    public boolean saveResourceLink(ResourceLink resourceLink) {
        resourceLink.setUpdated(Calendar.getInstance());
        return true;
    }

    /**
     * Delete resource link object.
     *
     * @param resourceLink ResourceLink object
     * @return <code>true</code> if the resourceLink object was successfully deleted
     */
    @Override
    public boolean deleteResourceLink(ResourceLink resourceLink) {
        resourceLink.initialise();
        return true;
    }

    /**
     * Get array of user objects.
     *
     * @param resourceLink ResourceLink object
     * @param localOnly    <code>true</code> if only users for the resource link are to be returned (excluding users sharing this resource link)
     * @param scope        Scope value to use for user IDs
     * @return array of User objects
     */
    @Override
    public Map<String, User> getUserResultSourcedIDs(ResourceLink resourceLink, boolean localOnly, int scope) {
        return new HashMap<String, User>();
    }

    /**
     * Get shares defined for a resource link.
     *
     * @param resourceLink ResourceLink object
     * @return array of resourceLinkShare objects
     */
    @Override
    public List<ResourceLinkShare> getShares(ResourceLink resourceLink) {
        return new ArrayList<ResourceLinkShare>();
    }


///
///  Nonce methods
///

    /**
     * Load nonce object.
     *
     * @param nonce Nonce object
     * @return <code>true</code> if the nonce object was successfully loaded
     */
    @Override
    public boolean loadConsumerNonce(Nonce nonce) {
        return false;  // assume the nonce does not already exist
    }

    /**
     * Save nonce object.
     *
     * @param nonce Nonce object
     * @return <code>true</code> if the nonce object was successfully saved
     */
    @Override
    public boolean saveConsumerNonce(Nonce nonce) {
        return true;
    }


///
///  ResourceLinkShareKey methods
///

    /**
     * Load resource link share key object.
     *
     * @param shareKey Resource link share key object
     * @return <code>true</code> if the resource link share key object was successfully loaded
     */
    @Override
    public boolean loadResourceLinkShareKey(ResourceLinkShareKey shareKey) {
        return true;
    }

    /**
     * Save resource link share key object.
     *
     * @param shareKey Resource link share key object
     * @return <code>true</code> if the resource link share key object was successfully saved
     */
    @Override
    public boolean saveResourceLinkShareKey(ResourceLinkShareKey shareKey) {
        return true;
    }

    /**
     * Delete resource link share key object.
     *
     * @param shareKey Resource link share key object
     * @return <code>true</code> if the resource link share key object was successfully deleted
     */
    @Override
    public boolean deleteResourceLinkShareKey(ResourceLinkShareKey shareKey) {
        return true;
    }


///
///  User methods
///

    /**
     * Load user object.
     *
     * @param user User object
     * @return <code>true</code> if the user object was successfully loaded
     */
    @Override
    public boolean loadUser(User user) {
        Calendar now = Calendar.getInstance();
        user.setCreated(now);
        user.setUpdated(now);
        return true;
    }

    /**
     * Save user object.
     *
     * @param user User object
     * @return <code>true</code> if the user object was successfully saved
     */
    @Override
    public boolean saveUser(User user) {
        user.setUpdated(Calendar.getInstance());
        return true;
    }

    /**
     * Delete user object.
     *
     * @param user User object
     * @return <code>true</code> if the user object was successfully deleted
     */
    @Override
    public boolean deleteUser(User user) {
        user.initialise();
        return true;
    }

}
