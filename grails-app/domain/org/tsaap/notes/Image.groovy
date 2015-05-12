package org.tsaap.notes

class Image {

    String myFile

    static constraints = {
        // Limit upload file size to 2MB
        myFile maxSize: 1024 * 1024 * 2
    }
}
