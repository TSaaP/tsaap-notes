/*
 * Copyright 2013 Tsaap Development Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
