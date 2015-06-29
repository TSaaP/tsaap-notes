package org.tsaap.attachement

import grails.test.mixin.TestFor
import spock.lang.*

@TestFor(AttachementController)
class AttachementControllerSpec extends Specification {

    AttachementService attachementService

    void "test view attachement"() {

        given: "an attachement"
        AttachementDto attachementDto = new AttachementDto(
                size: 6,
                typeMime: 'image/png',
                name: 'grails.png',
                originalFileName: 'grails.png',
                bytes: [2, 3, 4, 5, 6, 7]
        )
        Attachement myAttachement = attachementService.createAttachement(attachementDto, 10)
        controller.params.id = myAttachement.id

        when: "I call view attachement"
        controller.viewAttachement()

        then: "I get controller response"
        controller.response.getHeader("Content-disposition") == "filename=grails.png"
        controller.response.contentType == myAttachement.typeMime
        controller.response.outputStream != null
    }

    void "test view for an html attachement"() {

        given: "an attachement"
        AttachementDto attachementDto = new AttachementDto(
                size: 6,
                typeMime: 'html',
                name: 'grails.html',
                originalFileName: 'grails.html',
                bytes: [2, 3, 4, 5, 6, 7]
        )
        Attachement myAttachement = attachementService.createAttachement(attachementDto, 10)
        controller.params.id = myAttachement.id

        when: "I call view attachement"
        controller.viewAttachement()

        then: "I get controller response"
        controller.response.getHeader("Content-disposition") == "filename=grails.html"
        controller.response.contentType == myAttachement.typeMime
        controller.response.contentAsByteArray == [2, 3, 4, 5, 6, 7]
    }
}
