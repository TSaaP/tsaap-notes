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
