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
 * Created by dorian on 11/05/15.
 */

/**
 * Abstract data record base class. This base class contains only
 * a reference to the data identifier of the record and implements
 * the standard {@link Object} equality, hash code, and string
 * representation methods based on the identifier.
 */
public abstract class AbstractDataRecord implements DataRecord {

    /**
     * The binary identifier;
     */
    private final DataIdentifier identifier;

    /**
     * Creates a data record with the given identifier.
     *
     * @param identifier data identifier
     */
    public AbstractDataRecord(DataIdentifier identifier) {
        this.identifier = identifier;
    }

    /**
     * Returns the data identifier.
     *
     * @return data identifier
     */
    public DataIdentifier getIdentifier() {
        return identifier;
    }

    /**
     * Returns the string representation of the data identifier.
     *
     * @return string representation
     */
    public String toString() {
        return identifier.toString();
    }

    /**
     * Checks if the given object is a data record with the same identifier
     * as this one.
     *
     * @param object other object
     * @return <code>true</code> if the other object is a data record and has
     * the same identifier as this one, <code>false</code> otherwise
     */
    public boolean equals(Object object) {
        return (object instanceof DataRecord)
                && identifier.equals(((DataRecord) object).getIdentifier());
    }

    /**
     * Returns the hash code of the data identifier.
     *
     * @return hash code
     */
    public int hashCode() {
        return identifier.hashCode();
    }

}
