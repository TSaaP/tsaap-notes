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

    }
}
