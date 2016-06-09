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
/**
 * Exception thrown by the Data Store module.
 */
public class DataStoreException extends Exception {

    /**
     * Constructs a new instance of this class with the specified detail
     * message.
     *
     * @param message the detailed message.
     */
    public DataStoreException(String message) {
        super(message);
    }

    /**
     * Constructs a new instance of this class with the specified detail
     * message and root cause.
     *
     * @param message the detailed message.
     * @param cause   root failure cause
     */
    public DataStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new instance of this class with the specified root cause.
     *
     * @param rootCause root failure cause
     */
    public DataStoreException(Throwable rootCause) {
        super(rootCause);
    }

}