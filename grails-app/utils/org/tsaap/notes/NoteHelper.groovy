package org.tsaap.notes

import groovy.json.JsonBuilder

import java.util.regex.Pattern

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

  /**
   * Return a JSon representation of a note
   * @param note
   * @return
   */
  static String noteAsJson(Note note) {
    assert note
    def builder = new JsonBuilder()
    builder.note {
      globalId note.globalId
      sourceId note.id
      dateCreated note.dateCreated
      lastUpdated note.lastUpdated
      author {
        globalId note.author.globalId
        sourceId note.author.id
        username note.author.username
      }
      content note.content
      if (note.rootResource) {
        rootResource {
          url note.rootResource.url
          sourceId note.rootResource.id
        }
      }
      if (note.parentNote) {
        parentNote {
          globalId note.parentNote.globalId
          sourceId note.parentNote.id
        }
      }
    }
    builder.toPrettyString()
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
