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

import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import org.tsaap.BootstrapTestService
import org.tsaap.assignments.Statement
import spock.lang.Specification

/**
 * Created by dylan on 13/05/15.
 */
class AttachementServiceIntegrationSpec extends Specification {

    BootstrapTestService bootstrapTestService
    AttachementService attachementService


    def setup() {
        bootstrapTestService.initializeTests()
    }

    def "test create attachment for multipart file"() {
        given: "some multipart files"
        byte[] content = "Attachment".getBytes()
        MultipartFile multipartFile = new MockMultipartFile("grails", "grails", "text/plain", content)
        MultipartFile multipartFile2 = null

        when: "I want to create attachment for this multipart file"
        Attachement attachement = attachementService.createAttachementForMultipartFile(multipartFile)

        then: "the attachment is correctly created"
        attachement != null
        attachement.name == "grails"

        when: "I want to create attachment for null multipart file"
        attachementService.createAttachementForMultipartFile(multipartFile2)

        then: "I get exception"
        thrown(AttachementUploadException)

    }

    def "test create attachement with to big size"() {

        given: "an attachmentDto"
        AttachementDto attachementDto = new AttachementDto(
                size: 100000000000000000000,
                typeMime: 'image/png',
                name: 'grails.png',
                originalFileName: 'grails.png',
                bytes: [2, 3, 4, 5, 6, 7]
        )

        when: "I try to create the attachement"
        attachementService.createAttachement(attachementDto, 10)

        then: "I get an exception"
        thrown(AttachementUploadException)
    }

    def "test create attachement from an ImageIds"() {

        given: "an attachment in datastore and his ImageIds"
        AttachementDto attachementDto = new AttachementDto(
                size: 6,
                typeMime: 'image/png',
                name: 'grails.png',
                originalFileName: 'grails.png',
                bytes: [2, 3, 4, 5, 6, 7]
        )
        Attachement myAttachement = attachementService.createAttachement(attachementDto, 10)
        ImageIds imageIds = new ImageIds()
        imageIds.contentType = myAttachement.typeMime
        imageIds.fileName = myAttachement.name
        imageIds.size = myAttachement.size
        imageIds.dataSoreId = myAttachement.path

        when: "I want to create an attachment from an ImageIds"
        Attachement myAttachementIds = attachementService.createAttachementForImageIds(imageIds)

        then: "the attachment is correctly create from the ImageIds"
        myAttachementIds.name == imageIds.fileName
        myAttachementIds.path == imageIds.dataSoreId

    }

    def "test get input stream for an attachment"() {

        given: "an attachment"
        AttachementDto attachementDto = new AttachementDto(
                size: 6,
                typeMime: 'image/png',
                name: 'grails.png',
                originalFileName: 'grails.png',
                bytes: [2, 3, 4, 5, 6, 7]
        )
        Attachement myAttachement = attachementService.createAttachement(attachementDto, 10)

        when: "I want to get an input stream for a given attachement"
        InputStream inputStream = attachementService.getInputStreamForAttachement(myAttachement)

        then: "I get the input stream"
        inputStream != null
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


        when: "i want to detach the attachement"
        def detachedAttachment = attachementService.detachAttachement(myAttachement)

        then: "the attachement is correctly detach"
        detachedAttachment.statement == null
        detachedAttachment.toDelete
        !detachedAttachment.hasErrors()
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
        def path = "${attachementService.dataStore.path}/${attachement1.path.substring(0, 2)}/${attachement1.path.substring(2, 4)}/" +
                "${attachement1.path.substring(4, 6)}/$attachement1.path"
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
        def path = "${attachementService.dataStore.path}/${attachement1.path.substring(0, 2)}/${attachement1.path.substring(2, 4)}/" +
                "${attachement1.path.substring(4, 6)}/$attachement1.path"
        Attachement attachement2 = attachementService.createAttachement(attachementDto, 10)

        and: "one of them is attach to a statement"
        Statement statement = bootstrapTestService.statement1
        attachementService.addStatementToAttachment(statement, attachement2)

        when: "the garbage collector is running"
        attachementService.deleteAttachementAndFileInSystem()

        then: "only attachement1 is delete"
        Attachement.findById(attachement1.id) == null
        Attachement.findById(attachement2.id)
        new File(path).exists()
    }

    def "test add file to statement"() {

        given: "a statement and a multipart file"
        Statement statement = bootstrapTestService.statement1
        byte[] content = "Attachment".getBytes()
        MultipartFile multipartFile = new MockMultipartFile("grails", "grails", "text/plain", content)

        when: "I want to add multipart file to statement"
        Attachement attachement = attachementService.addFileToStatement(multipartFile, statement)

        then: "The file is correctly add to note"
        attachement != null
        attachement.statement == statement
        statement.attachment

    }

    def "test duplication of an attachment"() {
        given: "an attachement"
        AttachementDto attachementDto = new AttachementDto(
                size: 6,
                typeMime: 'image/png',
                name: 'grails.png',
                originalFileName: 'grails.png',
                bytes: [2, 3, 4, 5, 6, 7]
        )
        Attachement original = attachementService.createAttachement(attachementDto, 10)

        when: "the attachment is duplicated"
        Attachement copy = attachementService.duplicateAttachment(original)

        then: "the copy has no errors"
        //copy.errors.allErrors.each { println it }
        !copy.hasErrors()

        and: "all properties are set correctly"
        copy.size == original.size
        copy.typeMime == original.typeMime
        copy.name == original.name
        copy.originalName == original.originalName
        copy.dimension?.height == original.dimension?.height
        copy.dimension?.width == original.dimension?.width
        copy.path == original.path
        copy.toDelete == original.toDelete


    }


}
