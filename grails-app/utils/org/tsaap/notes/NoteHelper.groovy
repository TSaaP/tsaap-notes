package org.tsaap.notes

/**
 *
 * @author franck Silvestre
 */
class NoteHelper {

  /**
   * Extract a tag list from a content. A tag is a word prefixed by '#'.
   * The extracted tags are transformed as lower case words.
   * @param content the content to be processed
   */
  List tagsFromContent(String content) {
    def tags = []
    content.eachMatch(~/#[^ \t\n\r\u0085\u2028\u2029]*/) {
      def tag = it.substring(1).toLowerCase()
      if (!tags.contains(tag)) {
        tags << tag
      }
    }
    tags
  }

  /**
   * Extract a mention list from a content. A mention is a username prefixed
   * by '@'.
   * @param content the content to be processed
   */
  List mentionsFromContent(String content) {
    def mentions = []
    content.eachMatch(~/@[^ \t\n\r\u0085\u2028\u2029]*/) {
      def mention = it.substring(1)
      if (!mentions.contains(mention)) {
        mentions << mention
      }
    }
    mentions
  }

}
