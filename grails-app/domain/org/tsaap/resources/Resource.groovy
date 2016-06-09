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

package org.tsaap.resources

class Resource {

    Date dateCreated
    String url

    /**
     * The description note allows the description of the resource with tags and
     * mentions following the way they are used in a note (# or @ prefix).
     * All mentions and tags on the resource will be automatically bind to each
     * note taken by a user on this resource
     **/
    String descriptionAsNote
    String metadata


    static constraints = {
        url url: true, unique: true
        metadata nullable: true
        descriptionAsNote nullable: true, maxSize: 280
    }
}
