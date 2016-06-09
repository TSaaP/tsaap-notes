/*
 * Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
