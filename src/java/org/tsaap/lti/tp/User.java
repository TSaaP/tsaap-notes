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
      1.1.01  18-Jun-13  Tightened up setting of roles - now case sensitive and use fully qualified URN
*/
package org.tsaap.lti.tp;

import java.util.*;


/**
 * Class to represent an LTI User.
 *
 * @author      Stephen P Vickers
 * @version     1.1.01 (18-Jun-13)
 */
public class User {

/**
 * User's first name.
 */
  private String firstname = "";
/**
 * User's last name (surname or family name).
 */
  private String lastname = "";
/**
 * User's fullname.
 */
  private String fullname = "";
/**
 * User's email address.
 */
  private String email = "";
/**
 * Array of roles for user.
 */
  private List<String> roles = new ArrayList<String>();
/**
 * Array of groups for user.
 */
  private List<String>groups = new ArrayList<String>();
/**
 * User's result sourcedId.
 */
  private String ltiResultSourcedId = null;
/**
 * Date/time the record was created.
 */
  private Calendar created = null;
/**
 * Date/time the record was last updated.
 */
  private Calendar updated = null;
/**
 * ResourceLink object.
 */
  private ResourceLink resourceLink = null;
/**
 * User ID value.
 */
  private String id = null;

/**
 * Constructs a User object for the specified resource link and user ID.
 *
 * @param resourceLink  ResourceLink object
 * @param id            User ID value
 */
  public User(ResourceLink resourceLink, String id) {

    this.initialise();
    this.resourceLink = resourceLink;
    this.id = id;
    this.load();

  }

/**
 * Initialise the user object properties.
 */
  public final void initialise() {

    this.firstname = "";
    this.lastname = "";
    this.fullname = "";
    this.email = "";
    this.roles = new ArrayList<String>();
    this.groups = new ArrayList<String>();
    this.ltiResultSourcedId = null;
    this.created = null;
    this.updated = null;

  }

/**
 * Load the user from the database.
 *
 * @return <code>true</code> if the user object was successfully loaded
 */
  public final boolean load() {

    this.initialise();
    return this.resourceLink.getConsumer().getDataConnector().loadUser(this);

  }

/**
 * Save the user to the database.
 *
 * @return <code>true</code> if the user object was successfully saved
 */
  public boolean save() {

    boolean ok = true;
    if (this.ltiResultSourcedId != null) {
      ok = this.resourceLink.getConsumer().getDataConnector().saveUser(this);
    }

    return ok;

  }

/**
 * Delete the user from the database.
 *
 * @return <code>true</code> if the user object was successfully deleted
 */
  public boolean delete() {

    return this.resourceLink.getConsumer().getDataConnector().deleteUser(this);

  }

/**
 * Return the resource link object for the user.
 *
 * @return ResourceLink object
 */
  public ResourceLink getResourceLink() {

    return this.resourceLink;

  }

/**
 * Return the user ID using the default scope.
 * <p>
 * The ID value may be a compound of the tool consumer and resource link IDs.
 *
 * @see ToolProvider#setIdScope(int)
 *
 * @return User ID value
 */
  public String getId() {
    return getId(null);
  }

/**
 * Return the user ID using the specified scope.
 * <p>
 * The ID value may be a compound of the tool consumer and resource link IDs.
 *
 * @return User ID value
 */
  public String getId(Integer idScope) {

    String idValue;
    if (idScope == null) {
      idScope = this.resourceLink.getConsumer().getIdScope();
    }
    switch (idScope) {
      case ToolProvider.ID_SCOPE_GLOBAL:
        idValue = this.resourceLink.getKey() + ToolProvider.ID_SCOPE_SEPARATOR + this.id;
        break;
      case ToolProvider.ID_SCOPE_CONTEXT:
        idValue = this.resourceLink.getKey();
        if (this.resourceLink.getLtiContextId() != null) {
          idValue += ToolProvider.ID_SCOPE_SEPARATOR + this.resourceLink.getLtiContextId();
        }
        idValue += ToolProvider.ID_SCOPE_SEPARATOR + this.id;
        break;
      case ToolProvider.ID_SCOPE_RESOURCE:
        idValue = this.resourceLink.getKey();
        if (this.resourceLink.getLtiResourceLinkId() != null) {
          idValue += ToolProvider.ID_SCOPE_SEPARATOR + this.resourceLink.getLtiResourceLinkId();
        }
        idValue += ToolProvider.ID_SCOPE_SEPARATOR + this.id;
        break;
      default:
        idValue = this.id;
        break;
    }

    return idValue;

  }

/**
 * Set the user's name using values for first, last and/or full names.
 * <p>
 * A default name based on the user's ID is generated if no values are passed.
 *
 * @param firstname  User's first name.
 * @param lastname   User's last name.
 * @param fullname   User's full name.
 */
  public void setNames(String firstname, String lastname, String fullname) {

    String[] names = new String[2];
    names[0] = "";
    names[1] = "";
    if ((fullname != null) && (fullname.length() > 0)) {
      this.fullname = fullname.trim();
      names = this.fullname.split("[\\s]+", 2);
    }
    if ((firstname != null) && (firstname.length() > 0)) {
      this.firstname = firstname.trim();
      names[0] = this.firstname;
    } else if (names[0].length() > 0) {
      this.firstname = names[0];
    } else {
      this.firstname = "User";
    }
    if ((lastname != null) && (lastname.length() > 0)) {
      this.lastname = lastname.trim();
      names[1] = this.lastname;
    } else if (names[1].length() > 0) {
      this.lastname = names[1];
    } else {
      this.lastname = this.id;
    }
    if ((fullname == null) || (fullname.length() <= 0)) {
      this.fullname = this.firstname + " " + this.lastname;
    }

  }

/**
 * Set the user's email address.
 * <p>
 * If the default value passed begins with <code>@</code> it is prefixed
 * with the user's ID if no email is provided.
 *
 * @param email         Email address value
 * @param defaultEmail  Value to use if no email is provided
 */
  public void setEmail(String email, String defaultEmail) {

    if ((email != null) && (email.length() > 0)) {
      this.email = email;
    } else if (defaultEmail != null) {
      this.email = defaultEmail;
      if (this.email.startsWith("@")) {
        this.email = this.getId() + this.email;
      }
    } else {
      this.email = "";
    }

  }

/**
 * Set the user's roles to those specified in the list provided.
 *
 * @param rolesList  a list of role values
 */
  public void setRoles(List<String> rolesList) {
    this.roles.clear();
    for (String role : rolesList) {
      role = role.trim();
      if (role.length() > 0) {
        if (!role.startsWith("urn:")) {
          role = "urn:lti:role:ims/lis/" + role;
        }
        this.roles.add(role);
      }
    }
  }

/**
 * Set the user's roles to those specified in the comma separated list provided.
 *
 * @param rolesString  a string containing a list of roles
 */
  public void setRoles(String rolesString) {
    this.setRoles(Arrays.asList(rolesString.split(",")));
  }

/**
 * Returns the user's result sourcedId value.
 *
 * @return result sourcedId
 */
  public String getLtiResultSourcedId() {
    return this.ltiResultSourcedId;
  }

/**
 * Sets the user's result sourcedId value.
 *
 * @param ltiResultSourcedId  result sourcedId value
 */
  public void setLtiResultSourcedId(String ltiResultSourcedId) {
    this.ltiResultSourcedId = ltiResultSourcedId;
  }

/**
 * Returns the date/time when the object was created.
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
 * Sets the creation date/time for the object.
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
 * Returns the date/time when the object was last updated.
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
 * Sets the last update date/time for the object.
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
 * Return <code>true</code> if the user is an administrator.
 *
 * @return <code>true</code> if the user has a role of administrator
 */
  public boolean isAdmin() {

    return this.hasRole("Administrator") || this.hasRole("urn:lti:sysrole:ims/lis/SysAdmin") ||
           this.hasRole("urn:lti:sysrole:ims/lis/Administrator") || this.hasRole("urn:lti:instrole:ims/lis/Administrator");

  }

/**
 * Return <code>true</code> if the user is staff.
 *
 * @return <code>true</code> if the user has a role of instructor, contentdeveloper or teachingassistant
 */
  public boolean isStaff() {

    return (this.hasRole("Instructor") || this.hasRole("ContentDeveloper") || this.hasRole("TeachingAssistant"));

  }

/**
 * Return <code>true</code> if the user is a learner.
 *
 * @return <code>true</code> if the user has a role of learner
 */
  public boolean isLearner() {

    return this.hasRole("Learner");

  }

/**
 * Returns the user's firstname.
 *
 * @return firstname
 */
  public String getFirstname() {
    return firstname;
  }

/**
 * Set the user's firstname.
 *
 * @see #setNames(String, String, String)
 *
 * @param firstname string containing the user's firstname
 */
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

/**
 * Returns the user's lastname.
 *
 * @return lastname
 */
  public String getLastname() {
    return lastname;
  }

/**
 * Set the user's lastname.
 *
 * @see #setNames(String, String, String)
 *
 * @param lastname string containing the user's lastname
 */
  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

/**
 * Returns the user's fullname.
 *
 * @return fullname
 */
  public String getFullname() {
    return fullname;
  }

/**
 * Set the user's fullname.
 *
 * @see #setNames(String, String, String)
 *
 * @param fullname string containing the user's fullname
 */
  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

/**
 * Returns the user's email.
 *
 * @return email
 */
  public String getEmail() {
    return email;
  }

/**
 * Set the user's email.
 *
 * @param email  user's email address
 */
  public void setEmail(String email) {
    this.email = email;
  }

/**
 * Returns a list of group IDs for which the user is a member.
 *
 * @return list of group IDs
 */
  public List<String> getGroups() {
    return Collections.unmodifiableList(groups);
  }

/**
 * Add a group ID for the user.
 *
 * @param id  group ID
 */
  public void addGroup(String id) {
    this.groups.add(id);
  }

/**
 * Returns a string representation of this user.
 * <p>
 * The string representation consists of the user's ID, firstname, lastname,
 * fullname, email address, result sourcedId, roles and groups.
 *
 * @return  a string representation of this user
 */
  @Override
  public String toString() {

    StringBuilder value = new StringBuilder();
    value.append(User.class.getName()).append("\n");
    value.append("  id: ").append(this.id).append(" (").append(this.resourceLink.getConsumer().getKey()).append(")").append("\n");
    value.append("  firstname: ").append(this.firstname).append("\n");
    value.append("  lastname: ").append(this.lastname).append("\n");
    value.append("  fullname: ").append(this.fullname).append("\n");
    value.append("  email: ").append(this.email).append("\n");
    value.append("  resultSourcedId: ").append(this.ltiResultSourcedId).append("\n");
    value.append("  roles: ");
    String sep = "";
    for (Iterator<String> iter = this.roles.iterator(); iter.hasNext();) {
      value.append(sep).append(iter.next());
      sep = ", ";
    }
    value.append("\n");
    value.append("  groups: ");
    sep = "";
    for (Iterator<String> iter = this.groups.iterator(); iter.hasNext();) {
      value.append(sep).append(iter.next());
      sep = ", ";
    }
    value.append("\n");

    return value.toString();

  }

///
///  PRIVATE METHODS
///

/**
 * Check whether the user has a specified role name.
 *
 * @param role Name of role
 *
 * @return <code>true</code> if the user has the specified role
 */
  private boolean hasRole(String role) {

    boolean found = false;

    if (!role.startsWith("urn:")) {
      role = "urn:lti:role:ims/lis/" + role;
    }
    for (Iterator<String> iter = this.roles.iterator(); iter.hasNext();) {
      String aRole = iter.next();
      if (aRole.indexOf(role) >= 0) {
        found = true;
        break;
      }
    }

    return found;

  }

}
