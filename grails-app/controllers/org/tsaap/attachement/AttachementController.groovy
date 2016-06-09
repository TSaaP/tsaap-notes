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