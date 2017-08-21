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

import groovy.transform.ToString
import org.gcontracts.annotations.Requires
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.tsaap.assignments.Statement
import org.tsaap.directory.User
import org.tsaap.uploadImage.DataIdentifier
import org.tsaap.uploadImage.DataRecord
import org.tsaap.uploadImage.DataStore

import javax.imageio.ImageIO
import javax.imageio.ImageReader
import javax.imageio.stream.MemoryCacheImageInputStream

class AttachementService {

    static transactional = false
    DataStore dataStore

    /**
     * Get attachement by id
     * @param id the id of the wanted attachement
     * @return the corresponding attachment or null
     */
    Attachement getAttachementById(Long id) {
        return Attachement.get(id)
    }

    /**
     * Create an Attachement object from a HTTP request
     * with join file
     * @param file the file from the request
     * @return Attachement object
     */
    @Transactional
    Attachement createAttachementForMultipartFile(
            MultipartFile file) {
        if (!file || file.isEmpty()) {
            throw new AttachementUploadException("file.empty")
        }
        if (!file.name) {
            throw new AttachementUploadException("file.name.null")
        }

        return createAttachement(
                new AttachementDto(
                        size: file.size,
                        typeMime: file.contentType,
                        name: file.originalFilename,
                        originalFileName: file.originalFilename,
                        bytes: file.bytes
                ), 10)
    }


    @Transactional
    Attachement createAttachement(AttachementDto attachementDto,
                                  def maxSizeEnMega = 10) {

        if (attachementDto.size > 1024 * 1024 * maxSizeEnMega) {
            throw new AttachementUploadException("file.toobig")
        }
        Attachement attachement = new Attachement(
                size: attachementDto.size,
                typeMime: attachementDto.typeMime,
                name: attachementDto.name,
                originalName: attachementDto.originalFileName,
                toDelete: true
        )

        DataRecord dataRecord = dataStore.addRecord(attachementDto.inputStream)
        attachement.path = dataRecord.identifier.toString()
        if (attachement.imageIsDisplayable()) {
            attachement.dimension = determinerDimension(attachementDto.inputStream)
        }
        attachement.save()
        return attachement
    }

    /**
     * Create an Attachement from an ImageIds object
     * @param file ImageIds object type
     * @return Attachement object
     */
    @Transactional
    Attachement createAttachementForImageIds(
            ImageIds file) {

        Attachement attachement = new Attachement(
                size: file.size,
                typeMime: file.contentType,
                name: file.fileName,
                originalName: file.fileName,
                toDelete: true
        )
        attachement.path = file.dataSoreId
        attachement.save()
        return attachement
    }

    /**
     * Return File object matching to an Attachement
     * @param attachement the attachement
     * @return File object type
     */
    InputStream getInputStreamForAttachement(Attachement attachement) {
        DataRecord dataRecord = dataStore.getRecord(new DataIdentifier(attachement.path))
        dataRecord.stream
    }

    /**
     * Determine image dimensions
     * @param imageFile file to analyse
     * @param fileName file original name
     * @return file dimensions
     */
    private Dimension determinerDimension(InputStream inputStream) {

        ImageReader reader

        try {
            def memInputStream = new MemoryCacheImageInputStream(inputStream)
            def imageReaders = ImageIO.getImageReaders(memInputStream)

            if (imageReaders.hasNext()) {
                reader = imageReaders.next()
                reader.input = memInputStream
                return new Dimension(
                        width: reader.getWidth(reader.minIndex),
                        height: reader.getHeight(reader.minIndex)
                )
            }

        } finally {
            reader?.dispose()
        }
    }


    /**
     * Add file to statement
     * @param file the file to attach
     * @param statemement the statement
     * @return the attached attachment
     */
    Attachement addFileToStatement(MultipartFile file, Statement statement) {
        Attachement attachment = createAttachementForMultipartFile(file)
        addStatementToAttachment(statement, attachment)
        attachment
    }

    /**
     * Add a statement to an attachment
     * @param statement the statement
     * @param attachment the attachment
     * @return the modified attachment
     */
    Attachement addStatementToAttachment(Statement statement, Attachement attachment) {
       attachment.statement = statement
        attachment.toDelete = false
        attachment.save()
        attachment
    }


    /**
     * Duplicate an attachement
     * @param original
     * @return
     */
    Attachement duplicateAttachment(Attachement original) {
        if (!original) {
            return null
        }
        Attachement attachement = new Attachement(
                size: original.size,
                typeMime: original.typeMime,
                name: original.name,
                originalName: original.originalName,
                path: original.path,
                toDelete: original.toDelete,

        )
        if (original.dimension) {
            attachement.dimension = new Dimension(width: original.dimension.width,
                    height: original.dimension.height)
        }
        attachement.save()
        attachement
    }


    /**
     * Detach an attachment
     * @param myAttachement the attachment to detach
     * @return the detached attachment
     */
    @Requires({ !author })
    Attachement detachAttachement(Attachement myAttachement, User author = null) {
        myAttachement.statement = null
        myAttachement.toDelete = true
        myAttachement.save(flush: true)
        myAttachement
    }

    /**
     * Check if there are attachment to delete and delete them in this case.
     */
    def deleteAttachementAndFileInSystem() {
        def attachementToRemoveList = Attachement.findAllByToDelete(true)
        def iteratorAttachement = attachementToRemoveList.iterator()
        while (iteratorAttachement.hasNext()) {
            def attachementToDelete = iteratorAttachement.next()
            boolean deleteInSystem = true
            Attachement.findAllByPathAndIdNotEqual(attachementToDelete.path, attachementToDelete.id).each {
                if (!it.toDelete) {
                    deleteInSystem = false
                } else {
                    attachementToRemoveList.remove(it) // todo fsil : not clean, to improve
                    it.delete(flush: true)
                }
            }
            if (deleteInSystem) {
                String attachementPath = attachementToDelete.path
                String finalPath = "${dataStore.path}/${attachementPath.substring(0, 2)}/${attachementPath.substring(2, 4)}/" +
                        "${attachementPath.substring(4, 6)}/$attachementPath"
                new File(finalPath).delete()
            }
            attachementToDelete.delete(flush: true)
        }
    }


}

/**
 * Class reprensenting an image already upload in DataStore
 */
@ToString
class ImageIds {
    String sourceId
    String fileName
    String dataSoreId
    String contentType
    Long size

}