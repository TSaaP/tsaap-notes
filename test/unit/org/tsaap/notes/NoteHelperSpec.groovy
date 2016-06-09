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

package org.tsaap.notes

import spock.lang.Specification
import spock.lang.Unroll


class NoteHelperSpec extends Specification {

    @Unroll
    def "#content contains these tags : #tags"() {
        expect:
        NoteHelper.tagsFromContent(content) == tags
        where:
        content                                                                     | tags
        "a simple #content, with no #spaces in the content"                         | ["content", "spaces"]
        "a simple #content with no #spaces in the content but with #content a copy" | ["content", "spaces"]
        "a simple #content\n #another\r #tag3\n with #spaces\t in the content"      | ["content", "another", "tag3", "spaces"]
        "a simple with no tags"                                                     | []
        "a simple with no @tags"                                                    | []
        "Is it #LOWERCASE ?"                                                        | ["lowercase"]
    }

    @Unroll
    def "#content contains these mentions : #mentions"() {
        expect:
        NoteHelper.mentionsFromContent(content) == mentions
        where:
        content                                                                     | mentions
        "a simple @content, with no @spaces in the content"                         | ["content", "spaces"]
        "a simple @content with no @spaces in the content but with @content a copy" | ["content", "spaces"]
        "a simple @content\n @another\r @tag3\n with @spaces\t in the content"      | ["content", "another", "tag3", "spaces"]
        "a simple with no mentions"                                                 | []
        "a simple with no #mentions"                                                | []
        "Is it @LOWERCASE ?"                                                        | ["lowercase"]
    }

}
