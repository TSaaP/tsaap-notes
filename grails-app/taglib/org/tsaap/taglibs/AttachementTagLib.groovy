package org.tsaap.taglibs

import org.tsaap.attachement.AttachementService
import org.tsaap.notes.Attachement
import org.tsaap.notes.Dimension

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
            if (attachement.estUneImageAffichable()) {

                out << '<img src="' << link << '"'
                if (attrs.width && attrs.height) {
                    def dimInitial = new Dimension(largeur: attrs.width, hauteur: attrs.height)
                    def dimensionRendu = attachement.calculeDimensionRendu(dimInitial)
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
            } else if (attachement.estUnTexteAffichable()) {
                def is = attachementService.getInputStreamForAttachement(attachement)
                out << '<span style="white-space: pre;">' << is.text.encodeAsHTML() << '</span>'
            } else {
                out << '<a target="_blank" href="' << link << '">' <<
                        g.message(code: "attachement.acces") <<
                        '</a>'
            }
        }
    }
}