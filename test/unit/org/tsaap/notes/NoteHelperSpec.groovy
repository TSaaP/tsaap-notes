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
