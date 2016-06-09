package org.tsaap.attachement

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(AttachementController)
@Mock(Attachement)
class AttachementControllerSpec extends Specification {


    void "test view attachement"() {

        given: "an attachement"

        Attachement attachement = new Attachement(
                size: 6,
                typeMime: 'image/png',
                name: 'grails.png',
                originalName: 'grails.png',
                id: 1,
                toDelete: true
        )
        controller.params.id = attachement.id

        and: "the the needed collaborators"
        controller.attachementService = Mock(AttachementService) {
            getInputStreamForAttachement(attachement) >> new ByteArrayInputStream([0, 1, 2] as byte[])
            getAttachementById(attachement.id) >> attachement
        }



        when: "I call view attachement"
        controller.viewAttachement()

        then: "I get controller response"
        controller.response.getHeader("Content-disposition") == "attachment;filename=grails.png"
        controller.response.contentType == "${attachement.typeMime};charset=utf-8"
        controller.response.outputStream != null
    }


}
