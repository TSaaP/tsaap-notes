package org.tsaap.attachement

class AttachementController {

    static scope = "singleton"

    AttachementService attachementService

    /**
     *
     * Action to visualize a file
     */
    def viewAttachement() {
        Attachement attachement = attachementService.getAttachementById(params.id as Long)
        def is = attachementService.getInputStreamForAttachement(attachement)
        render(file: is, contentType: attachement.typeMime, fileName: attachement.originalName)
    }


}