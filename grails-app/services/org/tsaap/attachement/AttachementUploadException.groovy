package org.tsaap.attachement

/**
 * Created by dylan on 13/05/15.
 */
/**
 * Describe an error from an attachement upload
 * @author John Tranier
 */
class AttachementUploadException extends IllegalArgumentException {

    AttachementUploadException(String s) {
        super(s)
    }

    AttachementUploadException(String s, Throwable throwable) {
        super(s, throwable)
    }

}
