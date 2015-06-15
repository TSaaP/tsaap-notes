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


/**
 * Class to represent a sharing context.
 * <p>
 * A resource link may approve one or more other resource links to share with.
 *
 * @author      Stephen P Vickers
 * @version     1.1.01 (18-Jun-13)
 */
public class ResourceLinkShare {

/**
 * Consumer key value.
 */
  private String consumerKey = null;
/**
 * Resource link ID value.
 */
  private String resourceLinkId = null;
/**
 * Title of sharing context.
 */
  private String title = null;
/**
 * True if sharing request is to be automatically approved on first use.
 */
  private Boolean approved = null;

/**
 * Constructs an empty context share.
 */
  public ResourceLinkShare() {
  }

/**
 * Returns the consumer key for the sharing resource link.
 *
 * @return primary consumer key
 */
  public String getConsumerKey() {
    return this.consumerKey;
  }

/**
 * Set consumer key for the sharing resource link.
 *
 * @param consumerKey primary consumer key
 */
  public void setConsumerKey(String consumerKey) {
    this.consumerKey = consumerKey;
  }

/**
 * Returns the sharing resource link ID.
 *
 * @return primary resource link ID
 */
  public String getResourceLinkId() {
    return this.resourceLinkId;
  }

/**
 * Set resource ink ID for the sharing resource link.
 *
 * @param resourceLinkId primary resource link ID
 */
  public void setResourceLinkId(String resourceLinkId) {
    this.resourceLinkId = resourceLinkId;
  }

/**
 * Returns the sharing resource title.
 *
 * @return title
 */
  public String getTitle() {
    return this.title;
  }

/**
 * Set title for the sharing resource link.
 *
 * @param title  title of sharing resource link
 */
  public void setTitle(String title) {
    this.title = title;
  }

/**
 * Returns <code>true</code> if the share has been approved.
 *
 * @return <code>true</code> if the share key is approved
 */
  public Boolean getApproved() {
    return this.approved;
  }

/**
 * Sets whether the share key has been approved.
 *
 * @param approved  <code>true</code> if the share key is approved
 */
  public void setApproved(Boolean approved) {
    this.approved = approved;
  }

}
