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
    "a simple #content with no #spaces in the content" | ["content","spaces"]
    "a simple #content with no #spaces in the content but with #content a copy" | ["content","spaces"]
    "a simple #content\n #another\r #tag3\n with #spaces\t in the content" | ["content","another","tag3","spaces"]
    "a simple with not tags" | []
    "a simple with not @tags" | []
    "Is it #LOWERCASE ?" | ["lowercase"]

  }

}
