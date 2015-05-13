package org.tsaap.attachement

/**
 * Created by dylan on 13/05/15.
 */
/**
 * @author John Tranier
 */
class AttachementDto {
    long size
    String typeMime
    String name
    String originalFileName
    byte[] bytes

    InputStream getInputStream() {
        return new ByteArrayInputStream(bytes)
    }
}
