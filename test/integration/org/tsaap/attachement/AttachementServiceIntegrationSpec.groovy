package org.tsaap.attachement

import org.tsaap.BootstrapTestService
import org.tsaap.notes.Context
import org.tsaap.notes.Note
import spock.lang.Specification

import java.nio.file.Files

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

    def "test detachAttachement"() {
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

        when: "i want to detach the attachement"
        def detachedAttachment = attachementService.detachAttachement(myAttachement)

        then: "the attachement is correctly detach"
        detachedAttachment.context == null
        detachedAttachment.note == null
        detachedAttachment.toDelete == true
        !detachedAttachment.hasErrors()
    }

    def "test searchAttachementInNoteList"() {

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

    def "test add Note with context To Attachement"() {

        given: "an attachement"
        AttachementDto attachementDto = new AttachementDto(
                size: 6,
                typeMime: 'image/png',
                name: 'grails.png',
                originalFileName: 'grails.png',
                bytes: [2, 3, 4, 5, 6, 7]
        )
        Attachement myAttachement = attachementService.createAttachement(attachementDto, 10)

        and: "a note with a context"
        Note myNote = bootstrapTestService.note1
        myNote.context = bootstrapTestService.context1

        when: "adding the note with context to an attachement"
        myAttachement = attachementService.addNoteToAttachement(myNote, myAttachement)

        then: "the context and the note is really add to the attachement"
        myAttachement.context == myNote.context
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

        when: "adding a note to an attachement"
        myAttachement = attachementService.addNoteToAttachement(myNote, myAttachement)

        then: "the note is really add to the attachement"
        myAttachement.note == myNote
    }

    def "test the delete of an attachment and file system"() {
        given: "two attachments to the same image"
        AttachementDto attachementDto = new AttachementDto(
                size: 6,
                typeMime: 'image/png',
                name: 'grails.png',
                originalFileName: 'grails.png',
                bytes: [2, 3, 4, 5, 6, 7]
        )
        Attachement attachement1 = attachementService.createAttachement(attachementDto, 10)
        def path = "/opt/shared/tsaap-repo/${attachement1.path.substring(0,2)}/${attachement1.path.substring(2,4)}/" +
                "${attachement1.path.substring(4,6)}/$attachement1.path"
        Attachement attachement2 = attachementService.createAttachement(attachementDto, 10)

        when: "the garbage collector is running and the two attachments are to delete"
        attachementService.deleteAttachementAndFileInSystem()

        then: "the two attachments are deleted and the image record in system too"
        Attachement.findById(attachement1.id) == null
        Attachement.findById(attachement2.id) == null
        !new File(path).exists()

    }

    def "test the delete of an attachment but not delete file system"() {
        given: "two attachments to the same image"
        AttachementDto attachementDto = new AttachementDto(
                size: 6,
                typeMime: 'image/png',
                name: 'grails.png',
                originalFileName: 'grails.png',
                bytes: [2, 3, 4, 5, 6, 7]
        )
        Attachement attachement1 = attachementService.createAttachement(attachementDto, 10)
        def path = "/opt/shared/tsaap-repo/${attachement1.path.substring(0,2)}/${attachement1.path.substring(2,4)}/" +
                "${attachement1.path.substring(4,6)}/$attachement1.path"
        Attachement attachement2 = attachementService.createAttachement(attachementDto, 10)

        and: "one of them is attach to a note"
        Note myNote = bootstrapTestService.note2
        attachementService.addNoteToAttachement(myNote,attachement2)

        when: "the garbage collector is running"
        attachementService.deleteAttachementAndFileInSystem()

        then: "only attachement1 is delete"
        Attachement.findById(attachement1.id) == null
        Attachement.findById(attachement2.id)
        new File(path).exists()
    }
}
