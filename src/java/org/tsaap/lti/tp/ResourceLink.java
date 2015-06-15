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
      1.1.01  18-Jun-13  Fixed bug with OutcomesService when a resource link is shared with a different tool consumer
                         Separated User from Outcome object
*/
package org.tsaap.lti.tp;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.jdom.Document;
import org.jdom.Element;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;


/**
 * Class to represent an LTI resource link.
 *
 * @author      Stephen P Vickers
 * @version     1.1.01 (18-Jun-13)
 */
public class ResourceLink {

/**
 * Read action.
 */
  public static final int EXT_READ = 1;
/**
 * Write (create/update) action.
 */
  public static final int EXT_WRITE = 2;
/**
 * Delete action.
 */
  public static final int EXT_DELETE = 3;
/**
 * Decimal outcome type.
 */
  public static final String EXT_TYPE_DECIMAL = "decimal";
/**
 * Percentage outcome type.
 */
  public static final String EXT_TYPE_PERCENTAGE = "percentage";
/**
 * Ratio outcome type.
 */
  public static final String EXT_TYPE_RATIO = "ratio";
/**
 * Letter (A-F) outcome type.
 */
  public static final String EXT_TYPE_LETTER_AF = "letteraf";
/**
 * Letter (A-F) with optional +/- outcome type.
 */
  public static final String EXT_TYPE_LETTER_AF_PLUS = "letterafplus";
/**
 * Pass/fail outcome type.
 */
  public static final String EXT_TYPE_PASS_FAIL = "passfail";
/**
 * Free text outcome type.
 */
  public static final String EXT_TYPE_TEXT = "freetext";

/**
 * The XML document for the last extension service request.
 */
  private static final int TIMEOUT = 30000;

/**
 * Resource link ID as supplied in the last launch request.
 */
  private String ltiContextId = null;
/**
 * Resource link ID as supplied in the last launch request.
 */
  private String ltiResourceLinkId = null;
/**
 * Resource link title.
 */
  private String title = null;
/**
 * Setting values (selected LTI parameters, custom parameters and local parameters).
 */
  private Map<String,String> settings = null;
/**
 * User group sets (null if the groups enhancement is not supported)
 */
  private Map<String,GroupSet> groupSets = null;
/**
 * User groups (null if the groups enhancement is not supported)
 */
  private Map<String,Group> groups = null;
/**
 * Request for last extension service request.
 */
  private String extRequest = null;
/**
 * Response from last extension service request.
 */
  private String extResponse = null;
/**
 * Consumer key value for resource link being shared (if any).
 */
  private String primaryConsumerKey = null;
/**
 * Resource link ID value being shared (if any).
 */
  private String primaryResourceLinkId = null;
/**
 * <code>True</code> if the sharing request has been approved by the primary resource link.
 */
  private Boolean shareApproved = null;
/**
 * Date/time when the object was created.
 */
  private Calendar created = null;
/**
 * Date/time when the object was last updated.
 */
  private Calendar updated = null;
/**
 * ToolConsumer object for this context.
 */
  private ToolConsumer consumer = null;
/**
 * ID for this resource link.
 */
  private String id = null;
/**
 * <code>True</code> if the settings value have changed since last saved.
 */
  private boolean settingsChanged = false;
/**
 * The XML document from the last service request.
 */
  private Document extDoc = null;


/**
 * Class to represent a group set.
 * <p>
 * A group set is a collection of related groups, normally a user would only be
 * a member of one of the groups.  Users are not members of the group set itself.
 */
  public class GroupSet {

/**
 * Title of group set.
 */
    private String title = null;
/**
 * List of groups which form part of the group set.
 */
    private List<String> groups = null;
/**
 * Total number of users within all the groups forming part of the set.
 */
    private int numMembers = 0;
/**
 * Total number of staff users within all the groups forming part of the set.
 */
    private int numStaff = 0;
/**
 * Total number of learners within all the groups forming part of the set.
 */
    private int numLearners = 0;

/**
 * Construct a group set with the specified title.
 */
    public GroupSet(String title) {
      this.title = title;
      this.groups = new ArrayList<String>();
    }

/**
 *  Returns the title of the group set.
 *
 * @return title
 */
    public String getTitle() {
      return title;
    }

/**
 * Returns a list of the group IDs for the group set.
 *
 * @return list of group IDs
 */
    public List<String> getGroups() {
      return Collections.unmodifiableList(groups);
    }

/**
 * Add a group ID to the group set.
 *
 * @param id Group ID
 */
    public void addGroup(String id) {
      if (!this.groups.contains(id)) {
        this.groups.add(id);
      }
    }

/**
 * Returns the number of users belonging to the groups within the group set.
 *
 * @return number of users
 */
    public int getNumMembers() {
      return this.numMembers;
    }

/**
 * Increments the number of users belonging to the groups within the group set.
 */
    public void incNumMembers() {
      this.numMembers++;
    }

/**
 * Returns the number of staff users belonging to the groups within the group set.
 *
 * @return number of staff users
 */
    public int getNumStaff() {
      return this.numStaff;
    }

/**
 * Increments the number of staff users belonging to the groups within the group set.
 */
    public void incNumStaff() {
      this.numStaff++;
    }

/**
 * Returns the number of learners belonging to the groups within the group set.
 *
 * @return number of learners
 */
    public int getNumLearners() {
      return this.numLearners;
    }

/**
 * Increments the number of users belonging to the groups within the group set.
 */
    public void incNumLearners() {
      this.numLearners++;
    }

  }


/**
 * Class to represent a group.
 */
  public class Group {

/**
 * Title of group.
 */
    private String title = null;
/**
 * Id for the set which the group belongs to, may be null.
 */
    private String setId = null;

/**
 * Construct a group with the specified title and set ID.
 */
    public Group(String title, String setId) {
      this.title = title;
      this.setId = setId;
    }

/**
 * Returns the title of the group.
 *
 * @return title
 */
    public String getTitle() {
      return title;
    }

/**
 * Returns the set ID of the group.
 *
 * @return set ID
 */
    public String getSetId() {
      return setId;
    }

  }


/**
 * Class constructor.
 *
 * @param consumer Consumer key value
 * @param id       Resource link ID value
 */
  public ResourceLink(ToolConsumer consumer, String id) {

    this.consumer = consumer;
    this.id = id;
    if ((id != null) && (id.length() > 0)) {
      this.load();
    } else {
      this.initialise();
    }

  }

/**
 * Initialise the resource link properties.
 */
  public final void initialise() {

    this.ltiContextId = null;
    this.ltiResourceLinkId = null;
    this.title = "";
    this.settings = new HashMap<String,String>();
    this.groupSets = new HashMap<String,GroupSet>();
    this.groups = new HashMap<String,Group>();
    this.primaryConsumerKey = null;
    this.primaryResourceLinkId = null;
    this.shareApproved = null;
    this.created = null;
    this.updated = null;

  }

/**
 * Save the resource link to the database.
 *
 * @return <code>true</code> if the resource link was successfully saved.
 */
  public boolean save() {

    boolean ok = this.consumer.getDataConnector().saveResourceLink(this);
    if (ok) {
      this.settingsChanged = false;
    }

    return ok;

  }

/**
 * Delete the resource link from the database.
 *
 * @return <code>true</code> if the resource link was successfully deleted.
 */
  public boolean delete() {

    return this.consumer.getDataConnector().deleteResourceLink(this);

  }

/**
 * Returns the tool consumer for this resource link.
 *
 * @return ToolConsumer object
 */
  public ToolConsumer getConsumer() {

    return this.consumer;

  }

/**
 * Returns the tool consumer key for this resource link.
 *
 * @return Consumer key value
 */
  public String getKey() {

    return this.consumer.getKey();

  }

/**
 * Returns the resource link ID.
 *
 * @return resource link ID
 */
  public String getId() {

    return this.id;

  }

/**
 * Returns the resource link title.
 *
 * @return title
 */
  public String getTitle() {
    return this.title;
  }

/**
 * Set the resource link title.
 *
 * @param title  resource link title
 */
  public void setTitle(String title) {
    this.title = title;
  }

/**
 * Returns the context ID for the resource link.
 *
 * @return context ID
 */
  public String getLtiContextId() {
    return this.ltiContextId;
  }

/**
 * Set the context ID for the resource link.
 *
 * @param ltiContextId  context ID
 */
  public void setLtiContextId(String ltiContextId) {
    this.ltiContextId = ltiContextId;
  }

/**
 * Returns the resource link ID provided by the tool consumer on launch.
 *
 * @return LTI resource link ID
 */
  public String getLtiResourceLinkId() {
    return this.ltiResourceLinkId;
  }

/**
 * Set the resource link ID provided by the tool consumer on launch.
 *
 * @param ltiResourceLinkId  LTI resource link ID
 */
  public void setLtiResourceLinkId(String ltiResourceLinkId) {
    this.ltiResourceLinkId = ltiResourceLinkId;
  }

/**
 * Returns the group sets for the resource link.
 *
 * @return map of group sets with group set ID as key
 */
  public Map<String,GroupSet> getGroupSets() {
    return Collections.unmodifiableMap(this.groupSets);
  }

/**
 * Returns the groups for the resource link.
 *
 * @return map of groups with group ID as key
 */
  public Map<String,Group> getGroups() {
    return Collections.unmodifiableMap(this.groups);
  }

/**
 * Returns the content of the last service request.
 *
 * @return request content
 */
  public String getExtRequest() {
    return this.extRequest;
  }

/**
 * Returns the content of the last service response.
 *
 * @return response content
 */
  public String getExtResponse() {
    return this.extResponse;
  }

/**
 * Returns the XML document for the last service response.
 *
 * @return XML document
 */
  public Document getExtDoc() {
    return this.extDoc;
  }

/**
 * Returns the consumer key for the resource link with which this resource link is shared.
 *
 * @return consumer key
 */
  public String getPrimaryConsumerKey() {
    return this.primaryConsumerKey;
  }

/**
 * Set the consumer key for the resource link with which this resource link is shared.
 *
 * @param primaryConsumerKey  consumer key
 */
  public void setPrimaryConsumerKey(String primaryConsumerKey) {
    this.primaryConsumerKey = primaryConsumerKey;
  }

/**
 * Returns the resource link ID for the resource link with which this resource link is shared.
 *
 * @return resource link ID
 */
  public String getPrimaryResourceLinkId() {
    return this.primaryResourceLinkId;
  }

/**
 * Set the resource link ID for the resource link with which this resource link is shared.
 *
 * @param primaryResourceLinkId  resource link ID
 */
  public void setPrimaryResourceLinkId(String primaryResourceLinkId) {
    this.primaryResourceLinkId = primaryResourceLinkId;
  }

/**
 * Returns <code>true</code> if the share with another resource link has been approved.
 * <p>
 * If no sharing arrangement has been set up this property should have a value of null.
 *
 * @return <code>true</code> if a sharing arrangement is active
 */
  public Boolean getShareApproved() {
    return this.shareApproved;
  }

/**
 * Set whether the sharing arrangement is approved (active).
 * <p>
 * This value should be null if no arrangement is in place.
 *
 * @param shareApproved  <code>true</code> if the resource link is shared
 */
  public void setShareApproved(Boolean shareApproved) {
    this.shareApproved = shareApproved;
  }

/**
 * Returns a named setting value.
 *
 * @param name  name of setting
 *
 * @return setting value (or an empty string if it does not exist)
 */
  public String getSetting(String name) {
    return getSetting(name, "");
  }

/**
 * Returns a named setting value or the specified default value if the setting does not exist.
 *
 * @param name          name of setting
 * @param defaultValue  value to return if the setting does not exist
 *
 * @return setting value
 */
  public String getSetting(String name, String defaultValue) {

    String value = defaultValue;
    if (this.settings.containsKey(name)) {
      value = this.settings.get(name);
    }

    return value;

  }

/**
 * Set a setting value with a specified name.
 *
 * @param name   name of setting
 * @param value  value to set, use an empty or null value to delete a setting
 */
  public void setSetting(String name, String value) {

    String oldValue = this.getSetting(name);
    if (value != null) {
      if (!value.equals(oldValue)) {
        if (value.length() > 0) {
          this.settings.put(name, value);
        } else {
          this.settings.remove(name);
        }
        this.settingsChanged = true;
      }
    } else if (oldValue != null) {
      this.settings.remove(name);
      this.settingsChanged = true;
    }

  }

/**
 * Returns a map of all setting values.
 *
 * @return Map of setting values
 */
  public Map<String,String> getSettings() {

    return Collections.unmodifiableMap(this.settings);

  }

/**
 * Sets setting values to the specified map.
 *
 * @param settings  map of setting values
 */
  public void setSettings(Map<String, String> settings) {
    this.settings.clear();
    if (settings != null) {
      this.settings.putAll(settings);
    }
  }

/**
 * Save setting values.
 *
 * @return <code>true</code> if the settings were successfully saved
 */
  public boolean saveSettings() {

    boolean ok = true;
    if (this.settingsChanged) {
      ok = this.save();
    }

    return ok;

  }

/**
 * Returns <code>true</code> if the Outcomes service is supported for the resource link (either the LTI 1.1 or extension service).
 *
 * @return <code>true</code> if Outcomes service is supported
 */
  public boolean hasOutcomesService() {

    String url = this.getSetting("ext_ims_lis_basic_outcome_url") + this.getSetting("lis_outcome_service_url");

    return url.length() > 0;

  }

/**
 * Returns <code>true</code> if the Memberships service is supported for the resource link.
 *
 * @return <code>true</code> if Memberships service is supported
 */
  public boolean hasMembershipsService() {

    String url = this.getSetting("ext_ims_lis_memberships_url");

    return url.length() > 0;

  }

/**
 * Returns <code>true</code> if the Setting service is supported for the resource link.
 *
 * @return <code>true</code> if the Setting service is supported
 */
  public boolean hasSettingService() {

    String url = this.getSetting("ext_ims_lti_tool_setting_url");

    return url.length() > 0;

  }

/**
 * @deprecated use <code>{@link ResourceLink#doOutcomesService(int, Outcome, User)}</code> instead.
 * <p>
 * Perform an Outcomes service request.
 * <p>
 * The action type parameter should be one of the pre-defined constants.
 *
 * @see #EXT_READ
 * @see #EXT_WRITE
 * @see #EXT_DELETE
 *
 * @param action      action type
 * @param ltiOutcome  Outcome object
 *
 * @return <code>true</code> if the request was successfully processed
 */
  @Deprecated
  public boolean doOutcomesService(int action, Outcome ltiOutcome) {
    return doOutcomesService(action, ltiOutcome, null);
  }

/**
 * Perform an Outcomes service request.
 * <p>
 * The action type parameter should be one of the pre-defined constants.
 *
 * @see #EXT_READ
 * @see #EXT_WRITE
 * @see #EXT_DELETE
 *
 * @param action      action type
 * @param ltiOutcome  Outcome object
 * @param user        User object
 *
 * @return <code>true</code> if the request was successfully processed
 */
  public boolean doOutcomesService(int action, Outcome ltiOutcome, User user) {

    boolean response = false;
    this.extResponse = null;
//
/// Lookup service details from the source resource link appropriate to the user (in case the destination is being shared)
//
    ResourceLink sourceResourceLink = this;
    String sourcedId = ltiOutcome.getSourcedId();
    if (user != null) {
      sourceResourceLink = user.getResourceLink();
      sourcedId = user.getLtiResultSourcedId();
    }
//
/// Use LTI 1.1 service in preference to extension service if it is available
//
    String urlLTI11 = sourceResourceLink.getSetting("lis_outcome_service_url");
    String urlExt = sourceResourceLink.getSetting("ext_ims_lis_basic_outcome_url");
    String doString = null;
    if ((urlExt.length() > 0) || (urlLTI11.length() > 0)) {
      switch (action) {
        case EXT_READ:
          if ((urlLTI11.length() > 0) && (ltiOutcome.getType().equals(EXT_TYPE_DECIMAL))) {
            doString = "readResult";
          } else if (urlExt.length() > 0) {
            urlLTI11 = "";
            doString = "basic-lis-readresult";
          }
          break;
        case EXT_WRITE:
          if ((urlLTI11.length() > 0) && this.checkValueType(ltiOutcome, EXT_TYPE_DECIMAL)) {
            doString = "replaceResult";
          } else if (this.checkValueType(ltiOutcome, null)) {
            urlLTI11 = "";
            doString = "basic-lis-updateresult";
          }
          break;
        case EXT_DELETE:
          if ((urlLTI11.length() > 0) && ltiOutcome.getType().equals(EXT_TYPE_DECIMAL)) {
            doString = "deleteResult";
          } else if (urlExt.length() > 0) {
            urlLTI11 = "";
            doString = "basic-lis-deleteresult";
          }
          break;
      }
    }
    if (doString != null) {
      String value = ltiOutcome.getValue();
      if (value == null) {
        value = "";
      }
      if (urlLTI11.length() > 0) {
        StringBuilder xml = new StringBuilder();
        xml.append("      <resultRecord>\n");
        xml.append("        <sourcedGUID>\n");
        xml.append("          <sourcedId>").append(sourcedId).append("</sourcedId>\n");
        xml.append("        </sourcedGUID>\n");
        if (action == EXT_WRITE) {
          xml.append("        <result>\n");
          xml.append("          <resultScore>\n");
          xml.append("            <language>").append(ltiOutcome.getLanguage()).append("</language>\n");
          xml.append("            <textString>").append(ltiOutcome.getValue()).append("</textString>\n");
          xml.append("          </resultScore>\n");
          xml.append("        </result>\n");
        }
        xml.append("      </resultRecord>\n");
        if (this.doLTI11Service(doString, urlLTI11, xml.toString())) {
          switch (action) {
            case EXT_READ:
              value = Utils.getXmlChildValue(this.extDoc.getRootElement(), "textString");
              if (value == null) {
                  break;
              } else {
                ltiOutcome.setValue(value);
              }
            case EXT_WRITE:
            case EXT_DELETE:
              response = true;
              break;
          }
        }
      } else {
        Map<String,String> params = new HashMap<String,String>();
        params.put("sourcedid", sourcedId);
        params.put("result_resultscore_textstring", value);
        if (ltiOutcome.getLanguage() != null) {
          params.put("result_resultscore_language", ltiOutcome.getLanguage());
        }
        if (ltiOutcome.getStatus() != null) {
          params.put("result_statusofresult", ltiOutcome.getStatus());
        }
        if (ltiOutcome.getDate() != null) {
          params.put("result_date", ltiOutcome.getDate());
        }
        if (ltiOutcome.getType() != null) {
          params.put("result_resultvaluesourcedid", ltiOutcome.getType());
        }
        if (ltiOutcome.getDataSource() != null) {
          params.put("result_datasource", ltiOutcome.getDataSource());
        }
        if (this.doService(doString, urlExt, params)) {
          switch (action) {
            case EXT_READ:
              value = Utils.getXmlChildValue(this.extDoc.getRootElement(), "textstring");
              if (value != null) {
                ltiOutcome.setValue(value);
              }
            case EXT_WRITE:
            case EXT_DELETE:
              response = true;
              break;
          }
        }
      }
    }

    return response;

  }

/**
 * Perform a Memberships service request.
 * <p>
 * The user table is updated with the new list of user objects.  If the groups enhancement
 * is not supported for the resource link, then only membership data will be returned.
 *
 * @param withGroups  <code>true</code> if group information should also be requested
 *
 * @return list of User objects
 */
  public List<User> doMembershipsService(boolean withGroups) {

    List<User> users = new ArrayList<User>();
    Map<String,User> oldUsers = this.getUserResultSourcedIDs(true, ToolProvider.ID_SCOPE_RESOURCE);
    this.extResponse = null;
    String url = this.getSetting("ext_ims_lis_memberships_url");
    Map<String,String> params = new HashMap<String,String>();
    params.put("id", this.getSetting("ext_ims_lis_memberships_id"));
    boolean ok = false;
    if (withGroups) {
      ok = this.doService("basic-lis-readmembershipsforcontextwithgroups", url, params);
    }
    if (ok) {
      this.groupSets = new HashMap<String,GroupSet>();
      this.groups = new HashMap<String,Group>();
    } else {
      ok = this.doService("basic-lis-readmembershipsforcontext", url, params);
    }

    if (ok) {
      Element el = Utils.getXmlChild(this.extDoc.getRootElement(), "memberships");
      if (el != null) {
        List<Element> members = el.getChildren("member");
        for (Iterator<Element> iter = members.iterator(); iter.hasNext();) {
          el = iter.next();
          String value = Utils.getXmlChildValue(el, "user_id");
          User user = new User(this, value);
//
/// Set the user name
//
          String firstname = Utils.getXmlChildValue(el, "person_name_given");
          String lastname = Utils.getXmlChildValue(el, "person_name_family");
          String fullname = Utils.getXmlChildValue(el, "person_name_full");
          user.setNames(firstname, lastname, fullname);
//
/// Set the user email
//
          value = Utils.getXmlChildValue(el, "person_contact_email_primary");
          if (value == null) {
            value = "";
          }
          user.setEmail(value, this.consumer.getDefaultEmail());
//
/// Set the user roles
//
          value = Utils.getXmlChildValue(el, "roles");
          if (value != null) {
            user.setRoles(value);
          }
//
/// Set the user groups
//
          el = Utils.getXmlChild(el, "groups");
          if (el != null) {
            List<Element> memberGroups = el.getChildren("group");
            for (Iterator<Element> iter2 = memberGroups.iterator(); iter2.hasNext();) {
              Element group = iter2.next();
              String groupId = Utils.getXmlChildValue(group, "id");
              Element set = Utils.getXmlChild(group, "set");
              String setId = null;
              if (set != null) {
                setId = Utils.getXmlChildValue(set, "id");
                GroupSet groupSet = this.groupSets.get(setId);
                if (groupSet == null) {
                  groupSet = new GroupSet(Utils.getXmlChildValue(set, "title"));
                  this.groupSets.put(setId, groupSet);
                }
                groupSet.incNumMembers();
                if (user.isStaff()) {
                  groupSet.incNumStaff();
                }
                if (user.isLearner()) {
                  groupSet.incNumLearners();
                }
                groupSet.addGroup(groupId);
              }
              this.groups.put(groupId, new Group(Utils.getXmlChildValue(group, "title"), setId));
              user.addGroup(groupId);
            }
          }
//
/// If a result sourcedid is provided save the user
//
          value = Utils.getXmlChildValue(el, "lis_result_sourcedid");
          if (value != null) {
            user.setLtiResultSourcedId(value);
            user.save();
          }
          users.add(user);
//
/// Remove old user (if it exists)
//
          oldUsers.remove(user.getId(ToolProvider.ID_SCOPE_RESOURCE));
        }
//
/// Delete any old users which were not in the latest list from the tool consumer
//
        for (Iterator<String> iter = oldUsers.keySet().iterator(); iter.hasNext();) {
          String userId = iter.next();
          User user = oldUsers.get(userId);
          user.delete();
        }
      }
    } else {
      users = null;
    }

    return users;

  }

/**
 * Perform a Setting service request.
 * <p>
 * The current setting value can also be read by using
 * <code>getSetting("ext_ims_lti_tool_setting")</code>
 *
 * @param action  action type
 *
 * @see #EXT_READ
 * @see #EXT_DELETE
 * @see #doSettingService(int,String)
 * @see #getSetting(String)
 *
 * @return current setting value, null if an error occurs
 */
  public String doSettingService(int action) {

    String value = null;
    if (doSettingService(action, null)) {
      value = this.getSetting("ext_ims_lti_tool_setting");
    }

    return value;

  }

/**
 * Perform a Setting service request.
 *
 * @see #EXT_READ
 * @see #EXT_WRITE
 * @see #EXT_DELETE
 * @see #doSettingService(int)

 * @param action  action type
 * @param value   setting value (for write actions)
 *
 * @return <code>true</code> the action was successful
 */
  public boolean doSettingService(int action, String value) {

    boolean response = false;

    this.extResponse = null;
    String actionString = null;
    switch (action) {
      case EXT_READ:
        actionString = "basic-lti-loadsetting";
        break;
      case EXT_WRITE:
        actionString = "basic-lti-savesetting";
        break;
      case EXT_DELETE:
        actionString = "basic-lti-deletesetting";
        break;
    }
    if (actionString != null) {

      String url = this.getSetting("ext_ims_lti_tool_setting_url");
      Map<String,String> params = new HashMap<String,String>();
      params.put("id", this.getSetting("ext_ims_lti_tool_setting_id"));
      if (value == null) {
        value = "";
      }
      params.put("setting", value);

      if (this.doService(actionString, url, params)) {
        switch (action) {
          case EXT_READ:
            Element el = Utils.getXmlChild(this.extDoc.getRootElement(), "setting");
            if (el != null) {
              this.setSetting("ext_ims_lti_tool_setting", Utils.getXmlChildValue(el, "value"));
            }
            response = true;
            break;
          case EXT_WRITE:
            this.setSetting("ext_ims_lti_tool_setting", value);
            this.saveSettings();
            response = true;
            break;
          case EXT_DELETE:
            response = true;
            break;
        }
      }

    }

    return response;

  }

/**
 * Returns a map of User objects for users with a result sourcedId.
 * <p>
 * The array may include users from other contexts which are sharing this resource link.
 * It uses the user ID of the specified scope as the key.
 *
 * @param localOnly  <code>true</code> if only users from the resource link are to be returned, not users from shared contexts
 * @param idScope      scope to use for ID values
 *
 * @return map of User objects
 */
  public Map<String,User> getUserResultSourcedIDs(boolean localOnly, int idScope) {

    return this.consumer.getDataConnector().getUserResultSourcedIDs(this, localOnly, idScope);

  }

/**
 * Returns a list of ResourceLinkShare objects for each resource link which is sharing this resource link.
 *
 * @return list of ResourceLinkShare objects
 */
  public List<ResourceLinkShare> getShares() {

    return this.consumer.getDataConnector().getShares(this);

  }

/**
 * Returns the date/time when this resource link was created.
 *
 * @return date/time of creation
 */
  public Calendar getCreated() {
    Calendar calendar = null;
    if (this.created != null) {
      calendar = (Calendar)this.created.clone();
    }
    return calendar;
  }

/**
 * Set the date/time when the resource link was created.
 *
 * @param created  date/time of creation
 */
  public void setCreated(Calendar created) {
    if (created != null) {
      this.created = (Calendar)created.clone();
    } else {
      this.created = null;
    }
  }

/**
 * Returns the date/time when this resource link was last updated.
 *
 * @return date/time of last update
 */
  public Calendar getUpdated() {
    Calendar calendar = null;
    if (this.updated != null) {
      calendar = (Calendar)this.updated.clone();
    }
    return calendar;
  }

/**
 * Set the date/time when the resource link was last updated.
 *
 * @param updated  date/time of last update
 */
  public void setUpdated(Calendar updated) {
    if (updated != null) {
      this.updated = (Calendar)updated.clone();
    } else {
      this.updated = null;
    }
  }

/**
 * Returns a string representation of this resource link.
 * <p>
 * The string representation consists of the resource link ID, title, group sets,
 * groups and settings.
 *
 * @return  a string representation of this resource link
 */
  @Override
  public String toString() {

    StringBuilder value = new StringBuilder();
    value.append(ResourceLink.class.getName()).append("\n");
    value.append("  id: ").append(this.id).append(" (").append(this.getKey()).append(")").append("\n");
    value.append("  title: ").append(this.title).append("\n");
    value.append("  group sets:\n");
    String key;
    GroupSet set;
    String sep = "";
    for (Iterator<String> iter = this.groupSets.keySet().iterator(); iter.hasNext();) {
      key = iter.next();
      set = this.groupSets.get(key);
      value.append("    ").append(key).append(" (").append(set.getTitle()).append("): ");
      for (Iterator<String> iter2 = set.getGroups().iterator(); iter2.hasNext();) {
        value.append(sep).append(iter2.next());
        sep = ", ";
      }
      value.append("\n");
    }
    value.append("  groups:\n");
    Group group;
    for (Iterator<String> iter = this.groups.keySet().iterator(); iter.hasNext();) {
      key = iter.next();
      group = this.groups.get(key);
      value.append("    ").append(key).append(" (").append(group.getTitle()).append(")\n");
    }
    value.append("  settings:\n");
    for (Iterator<String> iter = this.settings.keySet().iterator(); iter.hasNext();) {
      key = iter.next();
      value.append("    ").append(key).append("=[").append(this.settings.get(key)).append("]\n");
    }

    return value.toString();

  }


///
///  PRIVATE METHODS
///

/**
 * Load the resource link from the database.
 *
 * @return <code>true</code> if resource link was successfully loaded
 */
  private boolean load() {

    this.initialise();
    return this.consumer.getDataConnector().loadResourceLink(this);

  }

/**
 * Convert outcome data type of value to a supported type if possible.
 *
 * @param ltiOutcome     Outcome object
 * @param supportedType  data type to be used (if null the reported supported types are used)
 *
 * @return <code>true</code> if the type/value are valid and supported
 */
  private boolean checkValueType(Outcome ltiOutcome, String supportedType) {

    List<String> supportedTypes = new ArrayList<String>();
    String type;
    if (supportedType != null) {
      supportedTypes.add(supportedType);
    } else {
      type = this.getSetting("ext_ims_lis_resultvalue_sourcedids", EXT_TYPE_DECIMAL).toLowerCase();
      type = type.replaceAll(" ", "");
      String[] types = type.split(",");
      supportedTypes.addAll(Arrays.asList(types));
    }
    type = ltiOutcome.getType();
    String value = ltiOutcome.getValue();
// Check whether the type is supported or there is no value
    boolean ok = supportedTypes.contains(type) || (value.length() <= 0);
    if (!ok) {
// Convert numeric values to decimal
      if (type.equals(EXT_TYPE_PERCENTAGE)) {
        if (value.endsWith("%")) {
          value = value.substring(0, value.length() - 1);
        }
        Float fValue = Utils.stringToFloat(value);
        ok = (fValue != null) && (fValue.compareTo(0.0f) >= 0) && (fValue.compareTo(100.0f) <= 0);
        if (ok) {
          ltiOutcome.setValue(Utils.floatToString(fValue / 100.0f));
          ltiOutcome.setType(EXT_TYPE_DECIMAL);
        }
      } else if (type.equals(EXT_TYPE_RATIO)) {
        String[] parts = value.split("/", 2);
        ok = (parts.length == 2);
        if (ok) {
          Float fValue1 = Utils.stringToFloat(parts[0]);
          Float fValue2 = Utils.stringToFloat(parts[1]);
          ok = (fValue1 != null) && (fValue2 != null) && (fValue1.compareTo(0.0f) >= 0) && (fValue2.compareTo(0.0f) > 0);
          if (ok) {
            ltiOutcome.setValue(Utils.floatToString(fValue1 / fValue2));
            ltiOutcome.setType(EXT_TYPE_DECIMAL);
          }
        }
// Convert letter_af to letter_af_plus or text
      } else if (type.equals(EXT_TYPE_LETTER_AF)) {
        if (supportedTypes.contains(EXT_TYPE_LETTER_AF_PLUS)) {
          ok = true;
          ltiOutcome.setType(EXT_TYPE_LETTER_AF_PLUS);
        } else if (supportedTypes.contains(EXT_TYPE_TEXT)) {
          ok = true;
          ltiOutcome.setType(EXT_TYPE_TEXT);
        }
// Convert letter_af_plus to letter_af or text
      } else if (type.equals(EXT_TYPE_LETTER_AF_PLUS)) {
        if (supportedTypes.contains(EXT_TYPE_LETTER_AF) && (value.length() == 1)) {
          ok = true;
          ltiOutcome.setType(EXT_TYPE_LETTER_AF);
        } else if (supportedTypes.contains(EXT_TYPE_TEXT)) {
          ok = true;
          ltiOutcome.setType(EXT_TYPE_TEXT);
        }
// Convert text to decimal
      } else if (type.equals(EXT_TYPE_TEXT)) {
        Float fValue = Utils.stringToFloat(value);
        ok = (fValue != null) && (fValue.compareTo(0.0f) >= 0) && (fValue.compareTo(1.0f) <= 0);
        if (ok) {
          ltiOutcome.setType(EXT_TYPE_DECIMAL);
        } else if (value.endsWith("%")) {
          value = value.substring(0, value.length() - 1);
          fValue = Utils.stringToFloat(value);
          ok = (fValue != null) && (fValue.compareTo(0.0f) >= 0) && (fValue.compareTo(100.0f) <= 0);
          if (ok) {
            if (supportedTypes.contains(EXT_TYPE_PERCENTAGE)) {
              ltiOutcome.setType(EXT_TYPE_PERCENTAGE);
            } else {
              ltiOutcome.setValue(Utils.floatToString(fValue / 100.0f));
              ltiOutcome.setType(EXT_TYPE_DECIMAL);
            }
          }
        }
      }
    }

    return ok;

  }

/**
 * Send a service request to the tool consumer.
 *
 * @param type    message type
 * @param url     URL to send request to
 * @param params  map of parameter values to be passed
 *
 * @return <code>true</code> if the request successfully obtained a response
 */
  private boolean doService(String type, String url, Map<String,String> params) {

    this.extResponse = null;
    if ((url != null) && (url.length() > 0)) {
// Add standard parameters
      params.put("oauth_consumer_key", this.consumer.getKey());
      params.put("lti_version", ToolProvider.LTI_VERSION);
      params.put("lti_message_type", type);
      HashSet<Map.Entry<String,String>> httpParams = new HashSet<Map.Entry<String,String>>();
      httpParams.addAll(params.entrySet());
// Check for query parameters which need to be included in the signature
      Map<String,String> queryParams = new HashMap<String,String>();
      String urlNoQuery = url;
      try {
        URI uri = new URI(url, false);
        String query = uri.getQuery();
        if (query != null) {
          urlNoQuery = urlNoQuery.substring(0, urlNoQuery.length() - query.length() - 1);
          String[] queryItems = query.split("&");
          for (int i = 0; i < queryItems.length; i++) {
            String[] queryItem = queryItems[i].split("=", 2);
            if (queryItem.length > 1) {
              queryParams.put(queryItem[0], queryItem[1]);
            } else {
              queryParams.put(queryItem[0], "");
            }
          }
          httpParams.addAll(queryParams.entrySet());
        }
      } catch (URIException e) {
      }
// Add OAuth signature
      List<Map.Entry<String,String>> reqParams = new ArrayList<Map.Entry<String,String>>();
      OAuthMessage oAuthMessage = new OAuthMessage("POST", urlNoQuery, httpParams);
      OAuthConsumer oAuthConsumer = new OAuthConsumer("about:blank", this.consumer.getKey(), this.consumer.getSecret(), null);
      OAuthAccessor oAuthAccessor = new OAuthAccessor(oAuthConsumer);
      try {
        oAuthMessage.addRequiredParameters(oAuthAccessor);
        reqParams.addAll(oAuthMessage.getParameters());
      } catch (OAuthException e) {
      } catch (URISyntaxException e) {
      } catch (IOException e) {
      }
// Remove parameters being passed on the query string
      for (Iterator<Map.Entry<String,String>> iter = queryParams.entrySet().iterator(); iter.hasNext();) {
        Map.Entry<String,String> entry = iter.next();
        reqParams.remove(entry);
      }
// Connect to tool consumer
      this.extResponse = this.doPostRequest(url, Utils.getHTTPParams(reqParams), null, null);
// Parse XML response
      if (this.extResponse != null) {
        this.extDoc = Utils.getXMLDoc(this.extResponse);
        boolean ok = this.extDoc != null;
        if (ok) {
          Element el = Utils.getXmlChild(this.extDoc.getRootElement(), "statusinfo");
          ok = el != null;
          if (ok) {
            String responseCode = Utils.getXmlChildValue(el, "codemajor");
            ok = responseCode != null;
            if (ok) {
              ok = responseCode.equals("Success");
            }
          }
        }
        if (!ok) {
          this.extResponse = null;
        }
      }
    }

    return this.extResponse != null;

  }

/**
 * Send a service request to the tool consumer.
 *
 * @param type  Message type
 * @param url   URL to send request to
 * @param xml   XML of message request
 *
 * @return <code>true</code> if the request successfully obtained a response
 */
  private boolean doLTI11Service(String type, String url, String xml) {

    this.extResponse = null;
    if ((url != null) && (url.length() > 0)) {
      String messageId = UUID.randomUUID().toString();
      StringBuilder xmlRequest = new StringBuilder();
      xmlRequest.append("<?xml version = \"1.0\" encoding = \"UTF-8\"?>\n");
      xmlRequest.append("<imsx_POXEnvelopeRequest xmlns = \"http://www.imsglobal.org/services/ltiv1p1/xsd/imsoms_v1p0\">\n");
      xmlRequest.append("  <imsx_POXHeader>\n");
      xmlRequest.append("    <imsx_POXRequestHeaderInfo>\n");
      xmlRequest.append("      <imsx_version>V1.0</imsx_version>\n");
      xmlRequest.append("      <imsx_messageIdentifier>").append(messageId).append("</imsx_messageIdentifier>\n");
      xmlRequest.append("    </imsx_POXRequestHeaderInfo>\n");
      xmlRequest.append("  </imsx_POXHeader>\n");
      xmlRequest.append("  <imsx_POXBody>\n");
      xmlRequest.append("    <").append(type).append("Request>\n");
      xmlRequest.append(xml);
      xmlRequest.append("    </").append(type).append("Request>\n");
      xmlRequest.append("  </imsx_POXBody>\n");
      xmlRequest.append("</imsx_POXEnvelopeRequest>\n");
// Calculate body hash
      String hash = Base64.encodeBase64String(DigestUtils.sha1(xmlRequest.toString()));
      Map<String,String> params = new HashMap<String,String>();
      params.put("oauth_body_hash", hash);
      HashSet<Map.Entry<String,String>> httpParams = new HashSet<Map.Entry<String,String>>();
      httpParams.addAll(params.entrySet());
// Check for query parameters which need to be included in the signature
      Map<String,String> queryParams = new HashMap<String,String>();
      String urlNoQuery = url;
      try {
        URI uri = new URI(url, false);
        String query = uri.getQuery();
        if (query != null) {
          urlNoQuery = urlNoQuery.substring(0, urlNoQuery.length() - query.length() - 1);
          String[] queryItems = query.split("&");
          for (int i = 0; i < queryItems.length; i++) {
            String[] queryItem = queryItems[i].split("=", 2);
            if (queryItem.length > 1) {
              queryParams.put(queryItem[0], queryItem[1]);
            } else {
              queryParams.put(queryItem[0], "");
            }
          }
          httpParams.addAll(queryParams.entrySet());
        }
      } catch (URIException e) {
      }
// Add OAuth signature
      Map<String,String> header = new HashMap<String,String>();
      OAuthMessage oAuthMessage = new OAuthMessage("POST", urlNoQuery, httpParams);
      OAuthConsumer oAuthConsumer = new OAuthConsumer("about:blank", this.consumer.getKey(), this.consumer.getSecret(), null);
      OAuthAccessor oAuthAccessor = new OAuthAccessor(oAuthConsumer);
      try {
        oAuthMessage.addRequiredParameters(oAuthAccessor);
        header.put("Authorization", oAuthMessage.getAuthorizationHeader(null));
        header.put("Content-Type", "application/xml");
      } catch (OAuthException e) {
      } catch (URISyntaxException e) {
      } catch (IOException e) {
      }
      StringRequestEntity entity = new StringRequestEntity(xmlRequest.toString());
// Connect to tool consumer
      this.extResponse = this.doPostRequest(url, Utils.getHTTPParams(params), header, entity);
// Parse XML response
      if (this.extResponse != null) {
        this.extDoc = Utils.getXMLDoc(this.extResponse);
        boolean ok = this.extDoc != null;
        if (ok) {
          Element el = Utils.getXmlChild(this.extDoc.getRootElement(), "imsx_statusInfo");
          ok = el != null;
          if (ok) {
            String responseCode = Utils.getXmlChildValue(el, "imsx_codeMajor");
            ok = responseCode != null;
            if (ok) {
              ok = responseCode.equals("success");
            }
          }
        }
        if (!ok) {
          this.extResponse = null;
        }
      }
    }

    return (this.extResponse != null);

  }

/**
 * Performs an HTTP POST request.
 *
 * @param url     URL to send request to
 * @param params  map of parameter values to be passed
 * @param header  values to include in the request header
 *
 * @return response returned from request, null if an error occurred
 */
  private String doPostRequest(String url, NameValuePair[] params, Map<String,String> header,
     StringRequestEntity entity) {

    String fileContent = null;

    HttpClient client = new HttpClient();
    client.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT);
    PostMethod httpPost = new PostMethod(url);
    httpPost.setFollowRedirects(false);
    httpPost.addParameters(params);
    try {
      if (entity != null) {
        httpPost.setRequestEntity(entity);
      }
      if (header != null) {
        String name;
        for (Iterator<String> iter = header.keySet().iterator(); iter.hasNext();) {
          name = iter.next();
          httpPost.addRequestHeader(name, header.get(name));
        }
      }
      httpPost.setURI(new URI(url, false));
      int resp = client.executeMethod(httpPost);
      if (resp < 400) {
        fileContent = httpPost.getResponseBodyAsString();
      }
    } catch (IOException e) {
      fileContent = null;
    }
    httpPost.releaseConnection();

    return fileContent;

  }

}
