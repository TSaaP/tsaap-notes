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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.tsaap.lti.tp.*;

import java.sql.*;
import java.sql.Date;
import java.util.*;


/**
 * Class to represent a data connector for a JDBC database connection.
 *
 * @author Stephen P Vickers
 * @version 1.1.01 (18-Jun-13)
 */
public class JDBC extends DataConnector {

    private String prefix = null;
    private Connection conn = null;


    /**
     * Constructs a data connector object using the specified database table name
     * prefix and JDBC database connection.
     *
     * @param prefix table name prefix
     * @param conn   database connection
     */
    public JDBC(String prefix, Connection conn) {

        this.prefix = prefix;
        this.conn = conn;

    }


///
///  ToolConsumer methods
///

    /**
     * Load tool consumer object.
     *
     * @param consumer ToolConsumer object
     * @return <code>true</code> if the tool consumer object was successfully loaded
     */
    @Override
    public boolean loadToolConsumer(ToolConsumer consumer) {

        boolean ok;
        String sql = "SELECT name, secret, lti_version, consumer_name, consumer_version, consumer_guid, css_path, protected, enabled, enable_from, enable_until, last_access, created, updated " +
                "FROM " + this.prefix + DataConnector.CONSUMER_TABLE_NAME + " " +
                "WHERE consumer_key = ?";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, consumer.getKey());
            ResultSet rs = stmt.executeQuery();
            ok = rs.next();
            if (ok) {
                consumer.setName(rs.getString("name"));
                consumer.setSecret(rs.getString("secret"));
                consumer.setLtiVersion(rs.getString("lti_version"));
                consumer.setConsumerName(rs.getString("consumer_name"));
                consumer.setConsumerVersion(rs.getString("consumer_version"));
                consumer.setConsumerGUID(rs.getString("consumer_guid"));
                consumer.setCssPath(rs.getString("css_path"));
                consumer.setProtect(rs.getInt("protected") == 1);
                consumer.setEnabled(rs.getInt("enabled") == 1);
                Calendar cal = Calendar.getInstance();
                consumer.setEnableFrom(null);
                if (rs.getTimestamp("enable_from") != null) {
                    cal.setTime(rs.getTimestamp("enable_from"));
                    consumer.setEnableFrom(cal);
                }
                consumer.setEnableUntil(null);
                if (rs.getTimestamp("enable_until") != null) {
                    cal.setTime(rs.getTimestamp("enable_until"));
                    consumer.setEnableUntil(cal);
                }
                consumer.setLastAccess(null);
                if (rs.getDate("last_access") != null) {
                    cal.setTime(rs.getDate("last_access"));
                    consumer.setLastAccess(cal);
                }
                cal.setTime(rs.getTimestamp("created"));
                consumer.setCreated(cal);
                cal.setTime(rs.getTimestamp("updated"));
                consumer.setUpdated(cal);
            }
        } catch (SQLException e) {
            ok = false;
        }

        return ok;

    }

    /**
     * Save tool consumer object.
     *
     * @param consumer ToolConsumer object
     * @return <code>true</code> if the tool consumer object was successfully saved
     */
    @Override
    public boolean saveToolConsumer(ToolConsumer consumer) {

        boolean ok;

        int protect = 0;
        if (consumer.isProtect()) {
            protect = 1;
        }
        int enabled = 0;
        if (consumer.isEnabled()) {
            enabled = 1;
        }
        Calendar now = Calendar.getInstance();
        Timestamp time = new Timestamp(now.getTimeInMillis());
        Timestamp from = null;
        if (consumer.getEnableFrom() != null) {
            from = new Timestamp(consumer.getEnableFrom().getTimeInMillis());
        }
        Timestamp until = null;
        if (consumer.getEnableUntil() != null) {
            until = new Timestamp(consumer.getEnableUntil().getTimeInMillis());
        }
        Date last = null;
        if (consumer.getLastAccess() != null) {
            last = new Date(consumer.getLastAccess().getTimeInMillis());
        }
        String sql;
        if (consumer.getCreated() == null) {
            sql = "INSERT INTO " + this.prefix + DataConnector.CONSUMER_TABLE_NAME + " " +
                    "(consumer_key, name, secret, lti_version, consumer_name, consumer_version, consumer_guid, css_path, protected, enabled, enable_from, enable_until, last_access, created, updated) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE " + this.prefix + DataConnector.CONSUMER_TABLE_NAME + " " +
                    "SET name = ?, secret = ?, lti_version = ?, " +
                    "consumer_name = ?, consumer_version = ?, consumer_guid = ?, css_path = ?, " +
                    "protected = ?, enabled = ?, enable_from = ?, enable_until = ?, last_access = ?, updated = ? " +
                    "WHERE consumer_key = ?";
        }
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            int n = 0;
            if (consumer.getCreated() == null) {
                stmt.setString(1, consumer.getKey());
                n = 1;
            }
            stmt.setString(n + 1, consumer.getName());
            stmt.setString(n + 2, consumer.getSecret());
            stmt.setString(n + 3, consumer.getLtiVersion());
            stmt.setString(n + 4, consumer.getConsumerName());
            stmt.setString(n + 5, consumer.getConsumerVersion());
            stmt.setString(n + 6, consumer.getConsumerGUID());
            stmt.setString(n + 7, consumer.getCssPath());
            stmt.setInt(n + 8, protect);
            stmt.setInt(n + 9, enabled);
            stmt.setTimestamp(n + 10, from);
            stmt.setTimestamp(n + 11, until);
            stmt.setDate(n + 12, last);
            stmt.setTimestamp(n + 13, time);  // created or updated
            if (consumer.getCreated() == null) {
                stmt.setTimestamp(15, time);  // updated
            } else {
                stmt.setString(14, consumer.getKey());
            }
            ok = stmt.executeUpdate() == 1;
            if (ok) {
                if (consumer.getCreated() == null) {
                    consumer.setCreated(now);
                }
                consumer.setUpdated(now);
            }
        } catch (SQLException e) {
            ok = false;
        }

        return ok;

    }

    /**
     * Delete tool consumer object.
     *
     * @param consumer ToolConsumer object
     * @return <code>true</code> if the tool consumer object was successfully deleted
     */
    @Override
    public boolean deleteToolConsumer(ToolConsumer consumer) {

        boolean ok;

        try {
// Delete any nonce values for this consumer
            String sql = "DELETE FROM " + this.prefix + DataConnector.NONCE_TABLE_NAME + " WHERE consumer_key = ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, consumer.getKey());
            stmt.executeUpdate();

// Delete any outstanding share keys for contexts for this consumer
            sql = "DELETE FROM " + this.prefix + DataConnector.RESOURCE_LINK_SHARE_KEY_TABLE_NAME + " WHERE primary_consumer_key = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, consumer.getKey());
            stmt.executeUpdate();

// Delete any users in contexts for this consumer
            sql = "DELETE FROM " + this.prefix + DataConnector.USER_TABLE_NAME + " WHERE consumer_key = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, consumer.getKey());
            stmt.executeUpdate();

// Update any contexts for which this consumer is acting as a primary context
            sql = "UPDATE " + this.prefix + DataConnector.RESOURCE_LINK_TABLE_NAME + " " +
                    "SET primary_consumer_key = NULL AND primary_context_id = NULL " +
                    "WHERE primary_consumer_key = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, consumer.getKey());
            stmt.executeUpdate();

// Delete any contexts for this consumer
            sql = "DELETE FROM " + this.prefix + DataConnector.RESOURCE_LINK_TABLE_NAME + " WHERE consumer_key = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, consumer.getKey());
            stmt.executeUpdate();

// Delete consumer
            sql = "DELETE FROM " + this.prefix + DataConnector.CONSUMER_TABLE_NAME + " WHERE consumer_key = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, consumer.getKey());
            ok = stmt.executeUpdate() == 1;

            if (ok) {
                consumer.initialise();
            }

        } catch (SQLException e) {
            ok = false;
        }

        return ok;

    }

    /**
     * Load tool consumer objects.
     *
     * @return array of all defined ToolConsumer objects
     */
    @Override
    public List<ToolConsumer> getToolConsumers() {

        List<ToolConsumer> consumers = new ArrayList<ToolConsumer>();

        String sql = "SELECT consumer_key, name, secret, lti_version, consumer_name, consumer_version, consumer_guid, css_path, " +
                "protected, enabled, enable_from, enable_until, last_access, created, updated " +
                "FROM " + this.prefix + DataConnector.CONSUMER_TABLE_NAME + " " +
                "ORDER BY name";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ToolConsumer consumer = new ToolConsumer(rs.getString("consumer_key"), this, false);
                consumer.setName(rs.getString("name"));
                consumer.setSecret(rs.getString("secret"));
                consumer.setLtiVersion(rs.getString("lti_version"));
                consumer.setConsumerName(rs.getString("consumer_name"));
                consumer.setConsumerVersion(rs.getString("consumer_version"));
                consumer.setConsumerGUID(rs.getString("consumer_guid"));
                consumer.setCssPath(rs.getString("css_path"));
                consumer.setProtect(rs.getInt("protected") == 1);
                consumer.setEnabled(rs.getInt("enabled") == 1);
                Calendar cal = Calendar.getInstance();
                consumer.setEnableFrom(null);
                if (rs.getTimestamp("enable_from") != null) {
                    cal.setTime(rs.getTimestamp("enable_from"));
                    consumer.setEnableFrom(cal);
                }
                consumer.setEnableUntil(null);
                if (rs.getTimestamp("enable_until") != null) {
                    cal.setTime(rs.getTimestamp("enable_until"));
                    consumer.setEnableUntil(cal);
                }
                consumer.setLastAccess(null);
                if (rs.getDate("last_access") != null) {
                    cal.setTime(rs.getDate("last_access"));
                    consumer.setLastAccess(cal);
                }
                cal.setTime(rs.getTimestamp("created"));
                consumer.setCreated(cal);
                cal.setTime(rs.getTimestamp("updated"));
                consumer.setUpdated(cal);
                consumers.add(consumer);
            }
        } catch (SQLException e) {
            consumers.clear();
        }

        return consumers;

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

        boolean ok;
        String sql = "SELECT consumer_key, context_id, lti_context_id, lti_resource_id, title, settings, " +
                "primary_consumer_key, primary_context_id, share_approved, created, updated " +
                "FROM " + this.prefix + DataConnector.RESOURCE_LINK_TABLE_NAME + " " +
                "WHERE (consumer_key = ?) AND (context_id = ?)";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, resourceLink.getKey());
            stmt.setString(2, resourceLink.getId());
            ResultSet rs = stmt.executeQuery();
            ok = rs.next();
            if (ok) {
                resourceLink.setLtiContextId(rs.getString("lti_context_id"));
                resourceLink.setLtiResourceLinkId(rs.getString("lti_resource_id"));
                resourceLink.setTitle(rs.getString("title"));
                Gson gson = new Gson();
                String settingsValue = rs.getString("settings");
                resourceLink.setSettings(null);
                if ((settingsValue != null) && (settingsValue.length() > 0)) {
                    try {
                        Map<String, String> settings = gson.fromJson(settingsValue, new TypeToken<Map<String, String>>() {
                        }.getType());
                        resourceLink.setSettings(settings);
                    } catch (JsonSyntaxException e) {
                    }
                }
                resourceLink.setPrimaryConsumerKey(rs.getString("primary_consumer_key"));
                resourceLink.setPrimaryResourceLinkId(rs.getString("primary_context_id"));
                resourceLink.setShareApproved(null);
                if (rs.getString("share_approved") != null) {
                    resourceLink.setShareApproved(rs.getInt("share_approved") == 1);
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(rs.getTimestamp("created"));
                resourceLink.setCreated(cal);
                cal.setTime(rs.getTimestamp("updated"));
                resourceLink.setUpdated(cal);
            }
        } catch (SQLException e) {
            ok = false;
        }

        return ok;

    }

    /**
     * Save resource link object.
     *
     * @param resourceLink ResourceLink object
     * @return <code>true</code> if the resource link object was successfully saved
     */
    @Override
    public boolean saveResourceLink(ResourceLink resourceLink) {

        boolean ok;

//    settingsValue = serialize(context.settings);
        Gson gson = new Gson();
        String settingsValue = null;
        Map<String, String> settings = resourceLink.getSettings();
        if (settings.size() > 0) {
            settingsValue = gson.toJson(settings);
        }
        int shareApproved = 0;
        if ((resourceLink.getShareApproved() != null) && resourceLink.getShareApproved()) {
            shareApproved = 1;
        }
        Calendar now = Calendar.getInstance();
        Timestamp time = new Timestamp(now.getTimeInMillis());
        String sql;
        if (resourceLink.getCreated() == null) {
            sql = "INSERT INTO " + this.prefix + DataConnector.RESOURCE_LINK_TABLE_NAME + " " +
                    "(consumer_key, context_id, lti_context_id, lti_resource_id, title, settings, " +
                    "primary_consumer_key, primary_context_id, share_approved, created, updated) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE " + this.prefix + DataConnector.RESOURCE_LINK_TABLE_NAME + " " +
                    "SET lti_context_id = ?, lti_resource_id = ?, title = ?, settings = ?, " +
                    "primary_consumer_key = ?, primary_context_id = ?, share_approved = ?, updated = ? " +
                    "WHERE (consumer_key = ?) AND (context_id = ?)";
        }
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            int n = 0;
            if (resourceLink.getCreated() == null) {
                stmt.setString(1, resourceLink.getKey());
                stmt.setString(2, resourceLink.getId());
                n = 2;
            }
            stmt.setString(n + 1, resourceLink.getLtiContextId());
            stmt.setString(n + 2, resourceLink.getLtiResourceLinkId());
            stmt.setString(n + 3, resourceLink.getTitle());
            stmt.setString(n + 4, settingsValue);
            stmt.setString(n + 5, resourceLink.getPrimaryConsumerKey());
            stmt.setString(n + 6, resourceLink.getPrimaryResourceLinkId());
            if (resourceLink.getShareApproved() != null) {
                stmt.setInt(n + 7, shareApproved);
            } else {
                stmt.setNull(n + 7, java.sql.Types.NUMERIC);
            }
            stmt.setTimestamp(n + 8, time);  // created or updated
            if (resourceLink.getCreated() == null) {
                stmt.setTimestamp(11, time);  // updated
            } else {
                stmt.setString(9, resourceLink.getKey());
                stmt.setString(10, resourceLink.getId());
            }
            ok = stmt.executeUpdate() == 1;
            if (ok) {
                if (resourceLink.getCreated() == null) {
                    resourceLink.setCreated(now);
                }
                resourceLink.setUpdated(now);
            }
        } catch (SQLException e) {
            ok = false;
        }

        return ok;

    }

    /**
     * Delete resource link object.
     *
     * @param resourceLink ResourceLink object
     * @return <code>true</code> if the resourceLink object was successfully deleted
     */
    @Override
    public boolean deleteResourceLink(ResourceLink resourceLink) {

        boolean ok = true;

        try {
// Delete any outstanding share keys for contexts for this consumer
            String sql = "DELETE FROM " + this.prefix + DataConnector.RESOURCE_LINK_SHARE_KEY_TABLE_NAME + " " +
                    "WHERE (primary_consumer_key = ?) AND (primary_context_id = ?)";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, resourceLink.getKey());
            stmt.setString(2, resourceLink.getId());
            stmt.executeUpdate();

// Delete users
            if (ok) {
                sql = "DELETE FROM " + this.prefix + DataConnector.USER_TABLE_NAME + " " +
                        "WHERE (consumer_key = ?) AND (context_id = ?)";
                stmt = this.conn.prepareStatement(sql);
                stmt.setString(1, resourceLink.getKey());
                stmt.setString(2, resourceLink.getId());
                stmt.executeUpdate();
            }

// Update any contexts for which this is the primary context
            if (ok) {
                sql = "UPDATE " + this.prefix + DataConnector.RESOURCE_LINK_TABLE_NAME + " " +
                        "SET primary_consumer_key = NULL AND primary_context_id = NULL " +
                        "WHERE (consumer_key = ?) AND (context_id = ?)";
                stmt = this.conn.prepareStatement(sql);
                stmt.setString(1, resourceLink.getKey());
                stmt.setString(2, resourceLink.getId());
                stmt.executeUpdate();
            }

// Delete context
            if (ok) {
                sql = "DELETE FROM " + this.prefix + DataConnector.RESOURCE_LINK_TABLE_NAME + " " +
                        "WHERE (consumer_key = ?) AND (context_id = ?)";
                stmt = this.conn.prepareStatement(sql);
                stmt.setString(1, resourceLink.getKey());
                stmt.setString(2, resourceLink.getId());
                ok = stmt.executeUpdate() == 1;
            }

            if (ok) {
                resourceLink.initialise();
            }

        } catch (SQLException e) {
            ok = false;
        }

        return ok;

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

        Map<String, User> users = new HashMap<String, User>();

        String sql;
        if (localOnly) {
            sql = "SELECT u.consumer_key, u.context_id, u.user_id, u.lti_result_sourcedid " +
                    "FROM " + this.prefix + DataConnector.USER_TABLE_NAME + " AS u " +
                    "INNER JOIN " + this.prefix + DataConnector.RESOURCE_LINK_TABLE_NAME + " AS c " +
                    "ON u.consumer_key = c.consumer_key AND u.context_id = c.context_id " +
                    "WHERE (c.consumer_key = ?) AND (c.context_id = ?) AND (c.primary_consumer_key IS NULL) AND (c.primary_context_id IS NULL)";
        } else {
            sql = "SELECT u.consumer_key, u.context_id, u.user_id, u.lti_result_sourcedid " +
                    "FROM " + this.prefix + DataConnector.USER_TABLE_NAME + " AS u " +
                    "INNER JOIN " + this.prefix + DataConnector.RESOURCE_LINK_TABLE_NAME + " AS c " +
                    "ON u.consumer_key = c.consumer_key AND u.context_id = c.context_id " +
                    "WHERE ((c.consumer_key = ?) AND (c.context_id = ?) AND (c.primary_consumer_key IS NULL) AND (c.primary_context_id IS NULL)) OR " +
                    "((c.primary_consumer_key = ?) AND (c.primary_context_id = ?) AND (share_approved = 1))";
        }
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, resourceLink.getKey());
            stmt.setString(2, resourceLink.getId());
            if (!localOnly) {
                stmt.setString(3, resourceLink.getKey());
                stmt.setString(4, resourceLink.getId());
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(resourceLink, rs.getString("user_id"));
                user.setLtiResultSourcedId(rs.getString("lti_result_sourcedid"));
                users.put(user.getId(scope), user);
            }
        } catch (SQLException e) {
            users.clear();
        }

        return users;

    }

    /**
     * Get shares defined for a resource link.
     *
     * @param resourceLink ResourceLink object
     * @return array of resourceLinkShare objects
     */
    @Override
    public List<ResourceLinkShare> getShares(ResourceLink resourceLink) {

        List<ResourceLinkShare> shares = new ArrayList<ResourceLinkShare>();

        String sql = "SELECT consumer_key, context_id, title, share_approved " +
                "FROM " + this.prefix + DataConnector.RESOURCE_LINK_TABLE_NAME + " " +
                "WHERE (primary_consumer_key = ?) AND (primary_context_id = ?) " +
                "ORDER BY consumer_key";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, resourceLink.getKey());
            stmt.setString(2, resourceLink.getId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ResourceLinkShare share = new ResourceLinkShare();
                share.setConsumerKey(rs.getString("consumer_key"));
                share.setResourceLinkId(rs.getString("context_id"));
                share.setTitle(rs.getString("title"));
                share.setApproved(rs.getInt("share_approved") == 1);
                shares.add(share);
            }
        } catch (SQLException e) {
            shares.clear();
        }

        return shares;

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

        boolean ok;

        try {
// Delete any expired nonce values
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String sql = "DELETE FROM " + this.prefix + DataConnector.NONCE_TABLE_NAME + " WHERE expires <= ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setTimestamp(1, now);
            int n = stmt.executeUpdate();
// Load the nonce
            sql = "SELECT value AS T FROM " + this.prefix + DataConnector.NONCE_TABLE_NAME + " WHERE (consumer_key = ?) AND (value = ?)";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, nonce.getKey());
            stmt.setString(2, nonce.getValue());
            ResultSet rs = stmt.executeQuery();
            ok = rs.next();
        } catch (SQLException e) {
            ok = false;
        }

        return ok;

    }

    /**
     * Save nonce object.
     *
     * @param nonce Nonce object
     * @return <code>true</code> if the nonce object was successfully saved
     */
    @Override
    public boolean saveConsumerNonce(Nonce nonce) {

        boolean ok;

        String sql = "INSERT INTO " + this.prefix + DataConnector.NONCE_TABLE_NAME + " (consumer_key, value, expires) VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, nonce.getKey());
            stmt.setString(2, nonce.getValue());
            stmt.setTimestamp(3, new Timestamp(nonce.getExpires().getTimeInMillis()));
            ok = stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            ok = false;
        }

        return ok;

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

        boolean ok;

        try {
// Clear expired share keys
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String sql = "DELETE FROM " + this.prefix + DataConnector.RESOURCE_LINK_SHARE_KEY_TABLE_NAME + " WHERE expires <= ?";
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setTimestamp(1, now);
            stmt.executeUpdate();

// Load share key
            sql = "SELECT share_key_id, primary_consumer_key, primary_context_id, auto_approve, expires " +
                    "FROM " + this.prefix + DataConnector.RESOURCE_LINK_SHARE_KEY_TABLE_NAME + " " +
                    "WHERE share_key_id = ?";
            stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, shareKey.getId());
            ResultSet rs = stmt.executeQuery();
            ok = rs.next();
            if (ok) {
                shareKey.setPrimaryConsumerKey(rs.getString("primary_consumer_key"));
                shareKey.setPrimaryResourceLinkId(rs.getString("primary_context_id"));
                shareKey.setAutoApprove(rs.getInt("auto_approve") == 1);
                Calendar cal = Calendar.getInstance();
                cal.setTime(rs.getTimestamp("expires"));
                shareKey.setExpires(cal);
            }
        } catch (SQLException e) {
            ok = false;
        }

        return ok;

    }

    /**
     * Save resource link share key object.
     *
     * @param shareKey Resource link share key object
     * @return <code>true</code> if the resource link share key object was successfully saved
     */
    @Override
    public boolean saveResourceLinkShareKey(ResourceLinkShareKey shareKey) {

        boolean ok;

        int approve = 0;
        if (shareKey.isAutoApprove()) {
            approve = 1;
        }
        String sql;
        sql = "INSERT INTO " + this.prefix + DataConnector.RESOURCE_LINK_SHARE_KEY_TABLE_NAME + " " +
                "(share_key_id, primary_consumer_key, primary_context_id, auto_approve, expires) " +
                "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, shareKey.getId());
            stmt.setString(2, shareKey.getPrimaryConsumerKey());
            stmt.setString(3, shareKey.getPrimaryResourceLinkId());
            stmt.setInt(4, approve);
            stmt.setTimestamp(5, new Timestamp(shareKey.getExpires().getTimeInMillis()));
            ok = stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            ok = false;
        }

        return ok;

    }

    /**
     * Delete resource link share key object.
     *
     * @param shareKey Resource link share key object
     * @return <code>true</code> if the resource link share key object was successfully deleted
     */
    @Override
    public boolean deleteResourceLinkShareKey(ResourceLinkShareKey shareKey) {

        boolean ok;

        String sql = "DELETE FROM " + this.prefix + DataConnector.RESOURCE_LINK_SHARE_KEY_TABLE_NAME + " WHERE share_key_id = ?";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, shareKey.getId());
            ok = stmt.executeUpdate() == 1;
            if (ok) {
                shareKey.initialise();
            }
        } catch (SQLException e) {
            ok = false;
        }

        return ok;

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

        boolean ok;

        String sql = "SELECT lti_result_sourcedid, created, updated " +
                "FROM " + this.prefix + DataConnector.USER_TABLE_NAME + " " +
                "WHERE (consumer_key = ?) AND (context_id = ?) AND (user_id = ?)";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, user.getResourceLink().getKey());
            stmt.setString(2, user.getResourceLink().getId());
            stmt.setString(3, user.getId(ToolProvider.ID_SCOPE_ID_ONLY));
            ResultSet rs = stmt.executeQuery();
            ok = rs.next();
            if (ok) {
                user.setLtiResultSourcedId(rs.getString("lti_result_sourcedid"));
                Calendar cal = Calendar.getInstance();
                cal.setTime(rs.getTimestamp("created"));
                user.setCreated(cal);
                cal.setTime(rs.getTimestamp("updated"));
                user.setUpdated(cal);
            }
        } catch (SQLException e) {
            ok = false;
        }

        return ok;

    }

    /**
     * Save user object.
     *
     * @param user User object
     * @return <code>true</code> if the user object was successfully saved
     */
    @Override
    public boolean saveUser(User user) {

        boolean ok;

        Calendar now = Calendar.getInstance();
        Timestamp time = new Timestamp(now.getTimeInMillis());
        String sql;
        if (user.getCreated() == null) {
            sql = "INSERT INTO " + this.prefix + DataConnector.USER_TABLE_NAME + " (consumer_key, context_id, " +
                    "user_id, lti_result_sourcedid, created, updated) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            sql = "UPDATE " + this.prefix + DataConnector.USER_TABLE_NAME + " " +
                    "SET lti_result_sourcedid = ?, updated = ? " +
                    "WHERE consumer_key = ? AND context_id = ? AND user_id = ?";
        }
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            int n = 0;
            if (user.getCreated() == null) {
                stmt.setString(1, user.getResourceLink().getKey());
                stmt.setString(2, user.getResourceLink().getId());
                stmt.setString(3, user.getId(ToolProvider.ID_SCOPE_ID_ONLY));
                n = 3;
            }
            stmt.setString(n + 1, user.getLtiResultSourcedId());
            stmt.setTimestamp(n + 2, time);  // created or updated
            if (user.getCreated() == null) {
                stmt.setTimestamp(6, time);  // updated
            } else {
                stmt.setString(3, user.getResourceLink().getKey());
                stmt.setString(4, user.getResourceLink().getId());
                stmt.setString(5, user.getId(ToolProvider.ID_SCOPE_ID_ONLY));
            }
            ok = stmt.executeUpdate() == 1;
            if (ok) {
                if (user.getCreated() == null) {
                    user.setCreated(now);
                }
                user.setUpdated(now);
            }
        } catch (SQLException e) {
            ok = false;
        }

        return ok;

    }

    /**
     * Delete user object.
     *
     * @param user User object
     * @return <code>true</code> if the user object was successfully deleted
     */
    @Override
    public boolean deleteUser(User user) {

        boolean ok;

        String sql = "DELETE FROM " + this.prefix + DataConnector.USER_TABLE_NAME + " " +
                "WHERE (consumer_key = ?) AND (context_id = ?) AND (user_id = ?)";
        try {
            PreparedStatement stmt = this.conn.prepareStatement(sql);
            stmt.setString(1, user.getResourceLink().getKey());
            stmt.setString(2, user.getResourceLink().getId());
            stmt.setString(3, user.getId(ToolProvider.ID_SCOPE_ID_ONLY));
            ok = stmt.executeUpdate() == 1;

            if (ok) {
                user.initialise();
            }

        } catch (SQLException e) {
            ok = false;
        }

        return ok;

    }

}
