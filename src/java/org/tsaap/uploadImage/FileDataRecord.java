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
     * @param file       file that contains the binary stream
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
