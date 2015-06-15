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

import java.util.List;
import java.util.Map;


/**
 * Abstract class to represent a data connector.
 * <p>
 * A data connector provides methods to load and save LTI objects.
 *
 * @author      Stephen P Vickers
 * @version     1.1.01 (18-Jun-13)
 */
public abstract class DataConnector {

/**
 * Default name for database table used to store tool consumers.
 */
  public static final String CONSUMER_TABLE_NAME = "lti_consumer";
/**
 * Default name for database table used to store resource links.
 */
  public static final String RESOURCE_LINK_TABLE_NAME = "lti_context";
/**
 * Default name for database table used to store users.
 */
  public static final String USER_TABLE_NAME = "lti_user";
/**
 * Default name for database table used to store resource link share keys.
 */
  public static final String RESOURCE_LINK_SHARE_KEY_TABLE_NAME = "lti_share_key";
/**
 * Default name for database table used to store nonce values.
 */
  public static final String NONCE_TABLE_NAME = "lti_nonce";


///
///  ToolConsumer methods
///

/**
 * Load tool consumer object.
 *
 * @param consumer  ToolConsumer object
 *
 * @return <code>true</code> if the tool consumer object was successfully loaded
 */
  public abstract boolean loadToolConsumer(ToolConsumer consumer);

/**
 * Save tool consumer object.
 *
 * @param consumer  ToolConsumer object
 *
 * @return <code>true</code> if the tool consumer object was successfully saved
 */
  public abstract boolean saveToolConsumer(ToolConsumer consumer);

/**
 * Delete tool consumer object.
 *
 * @param consumer  ToolConsumer object
 *
 * @return <code>true</code> if the tool consumer object was successfully deleted
 */
  public abstract boolean deleteToolConsumer(ToolConsumer consumer);

/**
 * Load tool consumer objects.
 *
 * @return array of all defined ToolConsumer objects
 */
  public abstract List<ToolConsumer> getToolConsumers();


///
///  ResourceLink methods
///

/**
 * Load resource link object.
 *
 * @param resourceLink  ResourceLink object
 *
 * @return <code>true</code> if the resource link object was successfully loaded
 */
  public abstract boolean loadResourceLink(ResourceLink resourceLink);

/**
 * Save resource link object.
 *
 * @param resourceLink  ResourceLink object
 *
 * @return <code>true</code> if the resource link object was successfully saved
 */
  public abstract boolean saveResourceLink(ResourceLink resourceLink);

/**
 * Delete resource link object.
 *
 * @param resourceLink  ResourceLink object
 *
 * @return <code>true</code> if the resourceLink object was successfully deleted
 */
  public abstract boolean deleteResourceLink(ResourceLink resourceLink);

/**
 * Get array of user objects.
 *
 * @param resourceLink  ResourceLink object
 * @param localOnly     <code>true</code> if only users for the resource link are to be returned (excluding users sharing this resource link)
 * @param scope         Scope value to use for user IDs
 *
 * @return array of User objects
 */
  public abstract Map<String,User> getUserResultSourcedIDs(ResourceLink resourceLink, boolean localOnly, int scope);

/**
 * Get shares defined for a resource link.
 *
 * @param resourceLink  ResourceLink object
 *
 * @return array of resourceLinkShare objects
 */
  public abstract List<ResourceLinkShare> getShares(ResourceLink resourceLink);


///
///  Nonce methods
///

/**
 * Load nonce object.
 *
 * @param nonce  Nonce object
 *
 * @return <code>true</code> if the nonce object was successfully loaded
 */
  public abstract boolean loadConsumerNonce(Nonce nonce);

/**
 * Save nonce object.
 *
 * @param nonce  Nonce object
 *
 * @return <code>true</code> if the nonce object was successfully saved
 */
  public abstract boolean saveConsumerNonce(Nonce nonce);


///
///  ResourceLinkShareKey methods
///

/**
 * Load resource link share key object.
 *
 * @param shareKey Resource link share key object
 *
 * @return <code>true</code> if the resource link share key object was successfully loaded
 */
  public abstract boolean loadResourceLinkShareKey(ResourceLinkShareKey shareKey);

/**
 * Save resource link share key object.
 *
 * @param shareKey  Resource link share key object
 *
 * @return <code>true</code> if the resource link share key object was successfully saved
 */
  public abstract boolean saveResourceLinkShareKey(ResourceLinkShareKey shareKey);

/**
 * Delete resource link share key object.
 *
 * @param  shareKey  Resource link share key object
 *
 * @return <code>true</code> if the resource link share key object was successfully deleted
 */
  public abstract boolean deleteResourceLinkShareKey(ResourceLinkShareKey shareKey);


///
///  User methods
///

/**
 * Load user object.
 *
 * @param user  User object
 *
 * @return <code>true</code> if the user object was successfully loaded
 */
  public abstract boolean loadUser(User user);

/**
 * Save user object.
 *
 * @param user  User object
 *
 * @return <code>true</code> if the user object was successfully saved
 */
  public abstract boolean saveUser(User user);

/**
 * Delete user object.
 *
 * @param user  User object
 *
 * @return <code>true</code> if the user object was successfully deleted
 */
  public abstract boolean deleteUser(User user);

}
