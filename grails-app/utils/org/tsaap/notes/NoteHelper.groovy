package org.tsaap.notes

import groovy.transform.CompileStatic

import java.util.regex.Pattern

/**
 *
 * @author franck Silvestre
 */
@CompileStatic
class NoteHelper {

  /**
   * Extract a tag list from a content. A tag is a word prefixed by '#'.
   * The extracted tags are transformed as lower case words.
   * @param content the content to be processed
   */
  List tagsFromContent(String content) {
    listFromContentAndPrefixedPattern(content,~/#\w+/ )
  }

  /**
   * Extract a mention list from a content. A mention is a username prefixed
   * by '@'.
   * @param content the content to be processed
   */
  List mentionsFromContent(String content) {
    listFromContentAndPrefixedPattern(content,~/@\w+/ )
  }


  private List listFromContentAndPrefixedPattern(String content, Pattern pattern) {
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
