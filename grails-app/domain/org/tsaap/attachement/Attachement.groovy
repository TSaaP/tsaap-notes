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

import org.tsaap.assignments.Statement
import org.tsaap.notes.Context
import org.tsaap.notes.Note

/**
 * class reprensenting a file attach to a model object
 * @author franck Silvestre
 */
class Attachement {

    private static final ArrayList<String> TYPES_MIME_IMG_DISPLAYABLE = [
            'image/gif',
            'image/jpeg',
            'image/png'
    ]

    String path
    String name
    String originalName
    Integer size
    Dimension dimension
    String typeMime
    Note note
    Context context
    Statement statement
    Boolean toDelete = false

    static constraints = {
        size nullable: true
        typeMime nullable: true
        originalName nullable: true
        dimension nullable: true
        context nullable: true
        note nullable: true
        statement nullable: true
    }

    static embedded = ['dimension']

    boolean imageIsDisplayable() {
        return typeMime in TYPES_MIME_IMG_DISPLAYABLE
    }

    boolean textIsDisplayable() {
        return typeMime?.startsWith('text/')
    }

    /**
     * compute the image dimension
     * @param dimMax
     * @return
     */
    Dimension calculateDisplayDimension(Dimension dimMax) {
        def l = dimension.width
        def h = dimension.height
        def ratio = [l / dimMax.width, h / dimMax.height].max()

        if (ratio > 1) {
            l = (l / ratio as Double).trunc()
            h = (h / ratio as Double).trunc()
        }
        assert (l <= dimMax.width && h <= dimMax.height)
        new Dimension(width: l, height: h)
    }

}

class Dimension implements Comparable<Dimension> {
    Integer width
    Integer height

    String toString() {
        "dim    h: $height     l: $width"
    }

    @Override
    int compareTo(Dimension other) {
        if (width == other.width && height == other.height) {
            return 0
        }

        if (width > other.width || height > other.height) {
            return 1
        }

        return -1

    }
}