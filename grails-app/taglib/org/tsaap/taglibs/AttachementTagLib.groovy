package org.tsaap.taglibs

import org.tsaap.attachement.Attachement
import org.tsaap.attachement.AttachementService
import org.tsaap.attachement.Dimension

/**
 * Created by dylan on 19/05/15.
 */
class AttachementTagLib {

    AttachementService attachementService

    static namespace = "tsaap"

/**
 * Affiche un fichier.
 *
 * @attr attachement REQUIRED the attachement to show
 */
    def viewAttachement = { attrs ->
        if (attrs.attachement) {
            Attachement attachement = attrs.attachement
            String link = g.createLink(action: 'viewAttachement',
                    controller: 'attachement',
                    id: attachement.id)
            if (attachement.imageIsDisplayable()) {

                out << '<img src="' << link << '"'
                if (attrs.width && attrs.height) {
                    def dimInitial = new Dimension(width: attrs.width, height: attrs.height)
                    def dimensionRendu = attachement.calculateDisplayDimension(dimInitial)
                    out << ' width="' << dimensionRendu.width << '"'
                    out << ' height="' << dimensionRendu.height << '"'
                }
                if (attrs.id) {
                    out << ' id="' << attrs.id << '"'
                }
                if (attrs.class) {
                    out << ' class="' << attrs.class << '"'
                }
                out << '/>'
            } else if (attachement.textIsDisplayable()) {
                def is = attachementService.getInputStreamForAttachement(attachement)
                out << '<span style="white-space: pre;">' << is.text.encodeAsHTML() << '</span>'
            } else {
                out << '<a target="_blank" href="' << link << '">' <<
                        attachement.originalName <<
                        '</a>'
            }
        }
    }
}