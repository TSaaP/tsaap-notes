package org.tsaap.attachement

import org.tsaap.BootstrapTestService
import org.tsaap.notes.Context
import org.tsaap.notes.Note
import spock.lang.Specification

/**
 * Created by dylan on 13/05/15.
 */
class AttachementServiceIntegrationSpec extends Specification {

    BootstrapTestService bootstrapTestService
    AttachementService attachementService
    List<Note> noteList = new ArrayList<Note>()

    def setup() {
        bootstrapTestService.initializeTests()
    }

    def "deleteAttachementForNote"() {
        given: "an attachement for a note"
        AttachementDto attachementDto = new AttachementDto(
                size: 6,
                typeMime: 'image/png',
                name: 'grails.png',
                originalFileName: 'grails.png',
                bytes: [2, 3, 4, 5, 6, 7]
        )
        Attachement myAttachement = attachementService.createAttachement(attachementDto, 10)
        Note myNote = bootstrapTestService.note1
        myAttachement = attachementService.addNoteToAttachement(myNote, myAttachement)

        when: "i want to delete the attachement"
        attachementService.deleteAttachementForNote(myNote)

        then: "the attachement is correctly delete"
        Attachement.findById(myAttachement.id) == null
    }

    def "searchAttachementInNoteList"() {

        given: "a note List"

        Note myNote1 = bootstrapTestService.note1
        noteList.add(myNote1)
        Note myNote2 = bootstrapTestService.note2
        noteList.add(myNote2)

        and: "an attachement"
        AttachementDto attachementDto = new AttachementDto(
                size: 6,
                typeMime: 'image/png',
                name: 'grails.png',
                originalFileName: 'grails.png',
                bytes: [2, 3, 4, 5, 6, 7]
        )
        Attachement myAttachement = attachementService.createAttachement(attachementDto, 10)

        and: "the attachement bind to the note"
        myAttachement = attachementService.addNoteToAttachement(bootstrapTestService.note1,myAttachement)

        when:"i want all the attachement bind with notes"
        Map<Note,Attachement> myMap
        myMap = attachementService.searchAttachementInNoteList(noteList)

        then:"we get the map with attachement and note"
        myMap.containsKey(bootstrapTestService.note1)
        myMap.containsValue(myAttachement)
        !myMap.containsKey(bootstrapTestService.note2)
    }

    def "addNoteAndContextToAttachement"() {

        given: "an attachement"
        AttachementDto attachementDto = new AttachementDto(
                size: 6,
                typeMime: 'image/png',
                name: 'grails.png',
                originalFileName: 'grails.png',
                bytes: [2, 3, 4, 5, 6, 7]
        )
        Attachement myAttachement = attachementService.createAttachement(attachementDto, 10)

        and: "a note"
        Note myNote = bootstrapTestService.note1

        and: "a context"
        Context myContext = bootstrapTestService.context1

        when: "adding context and note to an attachement"
        myAttachement = attachementService.addNoteAndContextToAttachement(myContext, myNote, myAttachement)

        then: "the context and the note is really add to the attachement"
        //myAttachement.context == myContext
        myAttachement.note == myNote

    }

    def "addNoteToAttachement"() {

        given: "an attachement"
        AttachementDto attachementDto = new AttachementDto(
                size: 6,
                typeMime: 'image/png',
                name: 'grails.png',
                originalFileName: 'grails.png',
                bytes: [2, 3, 4, 5, 6, 7]
        )
        Attachement myAttachement = attachementService.createAttachement(attachementDto, 10)

        and: "a note"
        Note myNote = bootstrapTestService.note2
        println ">>>>>>>>>>>>>>>>>>>>>>>> $myNote"

        when: "adding a note to an attachement"
        myAttachement = attachementService.addNoteToAttachement(myNote, myAttachement)

        then: "the note is really add to the attachement"
        myAttachement.note == myNote
    }
}
