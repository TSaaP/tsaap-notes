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
      1.1.01  18-Jun-13  Removed reference to user sourcedid from Outcome object
*/
package org.tsaap.lti.tp;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Class to represent an LTI Outcome.
 *
 * @author Stephen P Vickers
 * @version 1.1.01 (18-Jun-13)
 */
public class Outcome {

    /**
     * Language value.
     */
    private String language = null;
    /**
     * Outcome status value.
     */
    private String status = null;
    /**
     * Outcome date value.
     */
    private String date = null;
    /**
     * Outcome type value.
     */
    private String type = null;
    /**
     * Outcome data source value.
     */
    private String dataSource = null;

    /**
     * @deprecated Use property from User object instead
     * <p>
     * Result sourcedId.
     */
    @Deprecated
    private String sourcedId = null;
    /**
     * Outcome value.
     */
    private String value = null;

    /**
     * Construct an empty outcome object.
     * <p>
     * The language defaults to <code>en-US</code>, and a type of <code>decimal</code>
     * and the current date/time are used.
     */
    public Outcome() {
        this(null, null);
    }

    /**
     * Construct an outcome object using the specified value.
     * <p>
     * The language defaults to <code>en-US</code>, and a type of <code>decimal</code>
     * and the current date/time are used.
     *
     * @param value outcome value, may be null
     */
    public Outcome(String value) {
        this(null, value);
    }

    /**
     * @param sourcedId sourcedId value for the user/context
     * @param value     outcome value, may be null
     * @deprecated use <code>{@link Outcome#Outcome(String)}</code> instead.
     * <p>
     * Construct an outcome object using the specified sourcedId and value.
     * <p>
     * The language defaults to <code>en-US</code>, and a type of <code>decimal</code>
     * and the current date/time are used.
     */
    @Deprecated
    public Outcome(String sourcedId, String value) {

        this.sourcedId = sourcedId;
        this.value = value;
        this.language = "en-US";
        SimpleDateFormat dateFormat = new SimpleDateFormat("y-MM-dd'T'HH:mm:ssZ");
        this.date = dateFormat.format(new Date());
        this.type = "decimal";

    }

    /**
     * Returns the type.
     *
     * @return type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Set the type.
     * <p>
     * The type should be one of the pre-defined constants.
     *
     * @param type Outcome value
     * @see ResourceLink#EXT_TYPE_DECIMAL
     * @see ResourceLink#EXT_TYPE_PERCENTAGE
     * @see ResourceLink#EXT_TYPE_RATIO
     * @see ResourceLink#EXT_TYPE_LETTER_AF
     * @see ResourceLink#EXT_TYPE_LETTER_AF_PLUS
     * @see ResourceLink#EXT_TYPE_PASS_FAIL
     * @see ResourceLink#EXT_TYPE_TEXT
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return result sourcedId value
     * @deprecated Use property from User object instead
     * <p>
     * Returns the result sourcedId value.
     */
    @Deprecated
    public String getSourcedId() {
        return this.sourcedId;
    }

    /**
     * Get the outcome value.
     *
     * @return string Outcome value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Set the outcome value.
     *
     * @param value Outcome value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Returns the language.
     *
     * @return language
     */
    public String getLanguage() {
        return this.language;
    }

    /**
     * Returns the status.
     *
     * @return status
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Returns the date.
     *
     * @return date
     */
    public String getDate() {
        return this.date;
    }

    /**
     * Returns the data source.
     *
     * @return data source
     */
    public String getDataSource() {
        return this.dataSource;
    }

    /**
     * Returns a string representation of this outcome.
     * <p>
     * The string representation consists of the outcome's type and value.
     *
     * @return a string representation of this outcome
     */
    @Override
    public String toString() {

        StringBuilder oValue = new StringBuilder();
        oValue.append(Outcome.class.getName()).append("\n");
        oValue.append("  type: ").append(this.getType()).append("\n");
        oValue.append("  value: ").append(this.getValue()).append("\n");

        return oValue.toString();

    }

}
