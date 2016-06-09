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

import java.util.regex.Pattern

class NoteHelper {

    /**
     * Extract a tag list from a content. A tag is a word prefixed by '#'.
     * The extracted tags are transformed as lower case words.
     * @param content the content to be processed
     */
    static List tagsFromContent(String content) {
        listFromContentAndPrefixedPattern(content, ~/#\w+/)
    }

    /**
     * Extract a mention list from a content. A mention is a username prefixed
     * by '@'.
     * @param content the content to be processed
     */
    static List mentionsFromContent(String content) {
        listFromContentAndPrefixedPattern(content, ~/@\w+/)
    }


    private static List listFromContentAndPrefixedPattern(String content, Pattern pattern) {
        def res = []
        content.eachMatch(pattern) { String it ->
            def item = it.substring(1).toLowerCase()
            if (!res.contains(item)) {
                res << item
            }
        }
        res
    }


}
