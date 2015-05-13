package org.tsaap.attachement

import groovy.transform.ToString
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.tsaap.notes.Attachement
import org.tsaap.notes.Dimension
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
     * encapsulant la pièce jointe
     * @param file the file from the request
     * @return l'objet Attachment
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
                ),
        )
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
        attachement.size = dataRecord.identifier.toString()
        if (attachement.estUneImageAffichable()) {
            attachement.dimension = determinerDimension(attachementDto.inputStream)
        }
        attachement.save()
        return attachement
    }

    /**
     * Create an Attachement from an ImageIds object
     * @param file l'objet de type ImageIds
     * @return l'objet Attachment
     */
    @Transactional
    Attachement createAttachementForImageIds(
            ImageIds file) {

        // par defaut un nouvel attachement est marque a supprimer
        // c'est à la création d'un lien vers un item qu'il faut le
        // considérer comme attaché et donc comme non à supprimer
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
     * Retourne l'objet File correspondant à un attachement
     * @param attachement l'attachement
     *
     * @param config le config object
     * @return l'objet de type File
     */
    InputStream getInputStreamForAttachement(Attachement attachement) {
        DataRecord dataRecord = dataStore.getRecord(new DataIdentifier(attachement.size))
        dataRecord.stream
    }

    /**
     * Encode en base 64 un attachement
     * @param attachement l'attachement à econder
     * @return l'attachement encodé
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
     * Determine les dimensions d'un image.
     * @param imageFile objet du fichier de l'image à analyser
     * @param fileName non d'origine du fichier.
     * @return les dimensions du fichier.
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
}

/**
 * Class représentant une image déjà chargée dans le DataStore (une image
 * importée par exemple)
 */
@ToString
class ImageIds {
    String sourceId
    String fileName
    String dataSoreId
    String contentType
    Long size

}