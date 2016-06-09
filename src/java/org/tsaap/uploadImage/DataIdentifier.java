/*
 * Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.tsaap.uploadImage;

/**
 * Created by dylan on 11/05/15.
 */

import java.io.Serializable;

/**
 * Opaque data identifier used to identify records in a data store.
 * All identifiers must be serializable and implement the standard
 * object equality and hash code methods.
 */
public final class DataIdentifier implements Serializable {

    /**
     * Serial version UID.
     */
    private static final long serialVersionUID = -9197191401131100016L;

    /**
     * Array of hexadecimal digits.
     */
    private static final char[] HEX = "0123456789abcdef".toCharArray();

    /**
     * Data identifier.
     */
    private final String identifier;

    /**
     * Creates a data identifier from the given string.
     *
     * @param identifier data identifier
     */
    public DataIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Creates a data identifier from the hexadecimal string
     * representation of the given bytes.
     *
     * @param identifier data identifier
     */
    public DataIdentifier(byte[] identifier) {
        char[] buffer = new char[identifier.length * 2];
        for (int i = 0; i < identifier.length; i++) {
            buffer[2 * i] = HEX[(identifier[i] >> 4) & 0x0f];
            buffer[2 * i + 1] = HEX[identifier[i] & 0x0f];
        }
        this.identifier = new String(buffer);
    }

    //-------------------------------------------------------------< Object >

    /**
     * Returns the identifier string.
     *
     * @return identifier string
     */
    public String toString() {
        return identifier;
    }

    /**
     * Checks if the given object is a data identifier and has the same
     * string representation as this one.
     *
     * @param object other object
     * @return <code>true</code> if the given object is the same identifier,
     * <code>false</code> otherwise
     */
    public boolean equals(Object object) {
        return (object instanceof DataIdentifier)
                && identifier.equals(object.toString());
    }

    /**
     * Returns the hash code of the identifier string.
     *
     * @return hash code
     */
    public int hashCode() {
        return identifier.hashCode();
    }

}