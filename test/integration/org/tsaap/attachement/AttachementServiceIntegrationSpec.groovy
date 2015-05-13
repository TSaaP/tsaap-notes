package org.tsaap.attachement

import org.tsaap.BootstrapTestService
import org.tsaap.notes.Attachement
import org.tsaap.notes.Note
import org.tsaap.notes.NoteService
import spock.lang.Specification

/**
 * Created by dylan on 13/05/15.
 */
class AttachementServiceIntegrationSpec extends Specification {

    BootstrapTestService bootstrapTestService
    NoteService noteService
    AttachementService attachementService

    def setup() {
        bootstrapTestService.initializeTests()
    }

    def "addNoteToAttachement"(){

        given: "an attachement"
        AttachementDto attachementDto = new AttachementDto(
                size: 6,
                typeMime: 'image/png',
                name: 'grails.png',
                originalFileName: 'grails.png',
                bytes: [2,3,4,5,6,7]
        )
        Attachement myAttachement = attachementService.createAttachement(attachementDto,10)

        and: "a note"
        Note myNote = bootstrapTestService.note1

        when: "adding a note to an attachement"
        myAttachement = attachementService.addNoteToAttachement(myNote,myAttachement)

        then: "the note is really add to the attachement"
        myAttachement.note == myNote
    }
}
