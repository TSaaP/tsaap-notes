package org.tsaap.notes

import org.tsaap.attachement.AttachementService


class AttachementController {

    static scope = "singleton"

    AttachementService attachementService

    /**
     *
     * Action to visualize a file
     */
    def viewAttachement() {
        Attachement attachement = Attachement.get(params.id)
        response.setHeader("Content-disposition", "filename=${attachement.originalName}")
        response.contentType = attachement.typeMime
        def is = attachementService.getInputStreamForAttachement(attachement)
        if (attachement.typeMime?.contains('html')) {
            render(is.text)
        } else {
            response.outputStream << is
            response.outputStream.flush()
        }
    }


}