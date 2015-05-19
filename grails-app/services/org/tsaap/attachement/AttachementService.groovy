package org.tsaap.attachement

import groovy.transform.ToString
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.tsaap.notes.Attachement
import org.tsaap.notes.Context
import org.tsaap.notes.Dimension
import org.tsaap.notes.Note
import org.tsaap.uploadImage.DataIdentifier
import org.tsaap.uploadImage.DataRecord
import org.tsaap.uploadImage.DataStore

import javax.imageio.ImageIO
import javax.imageio.ImageReader
import javax.imageio.stream.MemoryCacheImageInputStream
import javax.mail.internet.MimeUtility

class AttachementService {

    static transactional = false
    DataStore dataStore

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
        )

        DataRecord dataRecord = dataStore.addRecord(attachementDto.inputStream)
        attachement.path = dataRecord.identifier.toString()
        if (attachement.estUneImageAffichable()) {
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
     * Encode in base 64 an attachement
     * @param attachement attachement to encode
     * @return the encode attachement
     */
    String encodeToBase64(Attachement attachement) {
        ByteArrayOutputStream bos
        OutputStream b64os
        try {
            bos = new ByteArrayOutputStream()
            b64os = MimeUtility.encode(bos, 'base64')
            b64os << getInputStreamForAttachement(attachement)
            b64os.flush()
            bos.toString()
        } finally {
            bos?.close()
            b64os?.close()
        }
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

    Attachement addNoteToAttachement(Note myNote, Attachement myAttachement) {
        myAttachement.note = myNote
        myAttachement.save()
        myAttachement
    }

    Attachement addNoteAndContextToAttachement(Context myContext, Note myNote, Attachement myAttachement) {
        myAttachement.setContext(myContext)
        myAttachement.setNote(myNote)
        myAttachement.save()
        myAttachement
    }

    Map<Note,Attachement> searchAttachementInNoteList(List<Note> noteList) {
        Map<Note,Attachement> result = new HashMap<Note,Attachement>()
        noteList.each {
            if (Attachement.findByNote(it)) {
                Attachement theAttachement = Attachement.findByNote(it)
                result.put(it,theAttachement)
            }
        }
        result
    }

    def deleteAttachementForNote(Note myNote) {
        if(Attachement.findByNote(myNote)) {
            Attachement myAttachement = Attachement.findByNote(myNote)
            myAttachement.delete()
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