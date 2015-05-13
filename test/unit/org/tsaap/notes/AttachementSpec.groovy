package org.tsaap.notes

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(Attachement)
class AttachementSpec extends Specification {

    @Unroll
    void "test attachement validity of valid attachement"(String aPath, String aName, String anOriginalName, Integer aSize, Dimension aDimension, String aTypeMime, Note aNote, Context aContext) {

        given: "an attachement correctly initialize"
        Attachement attachement = new Attachement(path: aPath, name: aName, originalName: anOriginalName, size: aSize, dimension: Mock(Dimension), typeMime: aTypeMime, note: Mock(Note), context: Mock(Context))

        expect: "the attachement is valid"
        attachement.validate() == true

        where:

        aPath           | aName        | anOriginalName | aSize | aDimension     | aTypeMime    | aNote     | aContext
        "/home/dorian"  | "grails.png" | "grails.png"   | 1     | Mock(Dimension)| "image/png"  | Mock(Note)| Mock(Context)
        "/home/dorian"  | "grails.png" | "grails.png"   | null  | Mock(Dimension)| "image/png"  | Mock(Note)| Mock(Context)
        "/home/dorian"  | "grails.png" | null           | 1     | Mock(Dimension)| "image/png"  | Mock(Note)| Mock(Context)
        "/home/dorian"  | "grails.png" | "grails.png"   | 1     | Mock(Dimension)| null         | Mock(Note)| Mock(Context)
        "/home/dorian"  | "grails.png" | "grails.png"   | 1     | null           | "image/png"  | Mock(Note)| Mock(Context)
        "/home/dorian"  | "grails.png" | "grails.png"   | 1     | Mock(Dimension)| "image/png"  | Mock(Note)| null

    }
}
