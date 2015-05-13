package org.tsaap.notes

/**
 * Classe représentant un fichier attaché à un objet du domaine
 * @author franck Silvestre
 */
class Attachement {

    private static final ArrayList<String> TYPES_MIME_IMG_AFFICHABLE = [
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

    static constraints = {
        size nullable: true
        typeMime nullable: true
        originalName nullable: true
        dimension nullable: true
        context nullable: true
    }

    static embedded = ['dimension']

    boolean estUneImageAffichable() {
        return typeMime in TYPES_MIME_IMG_AFFICHABLE
    }

    /**
     * Calcule la dimension rendu en fonction d'une dimension max donnée.
     * @param dimMax
     * @return
     */
    Dimension calculeDimensionRendu(Dimension dimMax) {
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