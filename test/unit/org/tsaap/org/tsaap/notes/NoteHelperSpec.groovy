package org.tsaap.org.tsaap.notes

/**
 *
 * @author franck Silvestre
 */
import org.tsaap.notes.NoteHelper
import spock.lang.Specification


class NoteHelperSpec extends Specification {

  def "contents and corresponding tags"() {

    setup:
    NoteHelper noteHelper = new NoteHelper()

    expect:
    noteHelper.tagsFromContent(content) == tags

    where:
    content | tags
    "a simple #content, with no #spaces in the content" | ["content","spaces"]
    "a simple #content with no #spaces in the content but with #content a copy" | ["content","spaces"]
    "a simple #content\n #another\r #tag3\n with #spaces\t in the content" | ["content","another","tag3","spaces"]
    "a simple with no tags" | []
    "a simple with no @tags" | []
    "Is it #LOWERCASE ?" | ["lowercase"]

  }

  def "contents and corresponding mentions"() {

      setup:
      NoteHelper noteHelper = new NoteHelper()

      expect:
      noteHelper.mentionsFromContent(content) == mentions

      where:
      content | mentions
      "a simple @content, with no @spaces in the content" | ["content","spaces"]
      "a simple @content with no @spaces in the content but with @content a copy" | ["content","spaces"]
      "a simple @content\n @another\r @tag3\n with @spaces\t in the content" | ["content","another","tag3","spaces"]
      "a simple with no mentions" | []
      "a simple with no #mentions" | []
      "Is it @LOWERCASE ?" | ["lowercase"]

    }

}
