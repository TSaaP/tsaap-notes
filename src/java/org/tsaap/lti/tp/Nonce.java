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
 * Class to represent a Nonce value.
 *
 * @author      Stephen P Vickers
 * @version     1.1.01 (18-Jun-13)
 */
public class Nonce {

/**
 * Maximum age nonce values will be retained for (in minutes).
 */
  private static final int MAX_NONCE_AGE = 5;  // in minutes (matches default timestamp window used by OAuth library)
/**
 * Date/time when the nonce value expires.
 */
  private Calendar expires = null;
/**
 * ToolConsumer object to which this nonce applies.
 */
  private ToolConsumer consumer = null;
/**
 * Nonce value.
 */
  private String value = null;

/**
 * Construct a nonce value for a tool consumer with the specified value.
 *
 * @param consumer Tool Consumer object
 * @param value    Nonce value
 */
  public Nonce(ToolConsumer consumer, String value) {

    this(consumer, value, MAX_NONCE_AGE);

  }

/**
 * Construct a nonce value for a tool consumer with the specified value and maximum life.
 *
 * @param consumer Tool Consumer object
 * @param value    Nonce value
 * @param life     Maximum life of nonce value
 */
  public Nonce(ToolConsumer consumer, String value, int life) {

    this.consumer = consumer;
    this.value = value;
    this.expires = Calendar.getInstance();
    this.expires.add(Calendar.MINUTE, life);

  }

/**
 * Load a nonce value from the database.
 *
 * @return <code>true</code> if the nonce value was successfully loaded
 */
  public boolean load() {

    return this.consumer.getDataConnector().loadConsumerNonce(this);

  }

/**
 * Save a nonce value in the database.
 *
 * @return <code>true</code> if the nonce value was successfully saved
 */
  public boolean save() {

    return this.consumer.getDataConnector().saveConsumerNonce(this);

  }

/**
 * Returns the tool consumer.
 *
 * @return tool consumer object for this nonce
 */
  public ToolConsumer getConsumer() {

    return this.consumer;

  }

/**
 * Returns the tool consumer key.
 *
 * @return tool consumer key value
 */
  public String getKey() {

    return this.consumer.getKey();

  }

/**
 * Returns the outcome value.
 *
 * @return outcome value
 */
  public String getValue() {

    return this.value;

  }

/**
 * Returns the date/time when the nonce value is due to expire.
 *
 * @return expiry date/time
 */
  public Calendar getExpires() {
    Calendar calendar = null;
    if (this.expires != null) {
      calendar = (Calendar)this.expires.clone();
    }
    return calendar;
  }

}
