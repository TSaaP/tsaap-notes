package org.tsaap.uploadImage;

/**
 * Created by dorian on 11/05/15.
 */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Data record that is based on a normal file.
 */
public class FileDataRecord extends AbstractDataRecord {

    /**
     * The file that contains the binary stream.
     */
    private final File file;

    /**
     * Creates a data record based on the given identifier and file.
     *
     * @param identifier data identifier
     * @param file file that contains the binary stream
     */
    public FileDataRecord(DataIdentifier identifier, File file) {
        super(identifier);
        assert file.isFile();
        this.file = file;
    }

    /**
     * {@inheritDoc}
     */
    public long getLength() {
        return file.length();
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getStream() throws DataStoreException {
        try {
            return new LazyFileInputStream(file);
        } catch (IOException e) {
            throw new DataStoreException("Error opening input stream of " + file.getAbsolutePath(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public long getLastModified() {
        return file.lastModified();
    }
}
