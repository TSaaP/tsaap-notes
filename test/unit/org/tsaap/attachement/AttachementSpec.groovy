package org.tsaap.attachement

import grails.test.mixin.TestFor
import org.tsaap.attachement.Attachement
import org.tsaap.notes.Context
import org.tsaap.notes.Note
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(Attachement)
class AttachementSpec extends Specification {

    @Shared
    Dimension myDimension

    def setup() {
        myDimension = new Dimension()
        myDimension.height = 1
        myDimension.width = 1
    }

    @Unroll
    void "test attachement validity of valid attachement"(String aPath, String aName, String anOriginalName, Integer aSize, Dimension aDimension, String aTypeMime, Note aNote, Context aContext) {

        given: "an attachement correctly initialize"

        Attachement myAttachement = new Attachement(path: aPath, name: aName, originalName: anOriginalName, size: aSize, dimension: myDimension, typeMime: aTypeMime, note: aNote, context: aContext)


        expect: "the attachement is valid"
        if(!myAttachement.validate()){
            println myAttachement.getErrors()
        }
        myAttachement.validate() == true

        where:

        aPath           | aName        | anOriginalName | aSize | aDimension     | aTypeMime    | aNote     | aContext
        "/home/dorian"  | "grails.png" | "grails.png"   | 1     | myDimension    | "image/png"  | Mock(Note)| Mock(Context)
        "/home/dorian"  | "grails.png" | "grails.png"   | null  | myDimension    | "image/png"  | Mock(Note)| Mock(Context)
        "/home/dorian"  | "grails.png" | null           | 1     | myDimension    | "image/png"  | Mock(Note)| Mock(Context)
        "/home/dorian"  | "grails.png" | "grails.png"   | 1     | myDimension    | null         | Mock(Note)| Mock(Context)
        "/home/dorian"  | "grails.png" | "grails.png"   | 1     | null           | "image/png"  | Mock(Note)| Mock(Context)
        "/home/dorian"  | "grails.png" | "grails.png"   | 1     | myDimension    | "image/png"  | Mock(Note)| null
        "/home/dorian"  | "grails.png" | "grails.png"   | 1     | myDimension    | "image/png"  | null      | Mock(Context)

    }

    void "test dimension compare to"() {

        given: "A dimensions"
        Dimension dimension1 = new Dimension(width: 100, height: 100)

        when: "I compare it to others Dimension"
        def res1 = dimension1.compareTo(new Dimension(width: 100, height: 100))
        def res2 = dimension1.compareTo(new Dimension(width: 50, height: 50))
        def res3 = dimension1.compareTo(new Dimension(width: 150, height: 150))

        then: "I get good results"
        res1 == 0
        res2 == 1
        res3 == -1

    }

    void "test dimension to string"() {

        given: "A dimension"
        Dimension dimension1 = new Dimension(width: 100, height: 100)

        when: "I want a String with dimension properties"
        def res = dimension1.toString()

        then: "I get a String with dimension properties"
        res == "dim    h: 100     l: 100"
    }

    void "test dimension calculate display dimension"() {

        given: "An attachment and a maximum display dimension"
        Attachement attachement = new Attachement()
        Dimension dimension1 = new Dimension(width: 800, height: 600)
        attachement.dimension = dimension1
        Dimension dimensionMax = new Dimension(width: 600, height: 600)

        when: "I want to calculate the display dimension"
        Dimension givenDimension = attachement.calculateDisplayDimension(dimensionMax)

        then: "I get the display dimension"
        givenDimension.height == 450
        givenDimension.width == 600
    }

    void "test if attachment is displayable"() {

        given: "A displayable text attachement"
        Attachement attachement = new Attachement()
        attachement.typeMime = 'text/'

        when: "I want to know is the attachment is a text displayable"
        def res = attachement.textIsDisplayable()

        then: "I get a true answer"
        res
    }

}
