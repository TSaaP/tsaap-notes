package org.tsaap.uploadImage;

/**
 * Created by dylan on 11/05/15.
 */
import java.io.InputStream;

/**
 * Immutable data record that consists of a binary stream.
 */
public interface DataRecord {

    /**
     * Returns the identifier of this record.
     *
     * @return data identifier
     */
    DataIdentifier getIdentifier();

    /**
     * Returns the length of the binary stream in this record.
     *
     * @return length of the binary stream
     * @throws DataStoreException if the record could not be accessed
     */
    long getLength() throws DataStoreException;

    /**
     * Returns the the binary stream in this record.
     *
     * @return binary stream
     * @throws DataStoreException if the record could not be accessed
     */
    InputStream getStream() throws DataStoreException;

    /**
     * Returns the last modified of the record.
     *
     * @return last modified time of the binary stream
     */
    long getLastModified();
}