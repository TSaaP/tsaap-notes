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

import java.io.InputStream;

/**
 * Immutable data record that consists of a binary stream.
 */
public interface DataRecord {

    /**
     * Returns the identifier of this record.
     *
     * @return data identifier
     */
    DataIdentifier getIdentifier();

    /**
     * Returns the length of the binary stream in this record.
     *
     * @return length of the binary stream
     * @throws DataStoreException if the record could not be accessed
     */
    long getLength() throws DataStoreException;

    /**
     * Returns the the binary stream in this record.
     *
     * @return binary stream
     * @throws DataStoreException if the record could not be accessed
     */
    InputStream getStream() throws DataStoreException;

    /**
     * Returns the last modified of the record.
     *
     * @return last modified time of the binary stream
     */
    long getLastModified();
}