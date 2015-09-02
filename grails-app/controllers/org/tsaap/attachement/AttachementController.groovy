package org.tsaap.attachement

import org.tsaap.attachement.Attachement
import org.tsaap.attachement.AttachementService


class AttachementController {

    static scope = "singleton"

    AttachementService attachementService

    /**
     *
     * Action to visualize a file
     */
    def viewAttachement() {
        Attachement attachement = attachementService.getAttachementById(params.id)
        def is = attachementService.getInputStreamForAttachement(attachement)
        def ct = attachement.typeMime
        render(file:is,contentType:attachement.typeMime,fileName:attachement.originalName)
    }


}