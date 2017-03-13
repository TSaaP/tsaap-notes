package org.tsaap.assignments.interactions

import groovy.json.JsonSlurper
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by franck on 31/08/2016.
 */
class ResponseSubmissionSpecificationTest extends Specification {

    void "test validation and json output for a valid choice spec"() {
        given: "a valid specification"
        ResponseSubmissionSpecification spec = new ResponseSubmissionSpecification()
        spec.studentsProvideConfidenceDegree = true
        spec.studentsProvideExplanation = true

        when: "validating the spec"
        def isValid = spec.validateSpecification()
        if (spec.hasErrors()) {
            println ">>>>>> Errors: ${spec.errors.allErrors}"
        }

        then: "the spec is valid"
        isValid

        when: "getting the json output and parsing the output"
        String jsonStr = spec.jsonString
        println ">>>>>> jsonStr: ${jsonStr}"
        JsonSlurper jsonSlurper = new JsonSlurper()
        def map = jsonSlurper.parseText(jsonStr)

        then: "the resulting map contains all values of the spec"
        map.studentsProvideConfidenceDegree == true
        map.studentsProvideExplanation == true
    }

    @Unroll
    void "test json output for an invalid choice spec with conf deg #confidenceDegree and expl #explanation"() {
        given: "a non valid specification"
        ResponseSubmissionSpecification spec = new ResponseSubmissionSpecification()
        spec.studentsProvideConfidenceDegree = confidenceDegree
        spec.studentsProvideExplanation = explanation

        when: "getting the json output"
        String jsonStr = spec.jsonString

        then: "the no string is available"
        jsonStr == null

        and: "the spec has errors"
        spec.hasErrors()
        println ">>>>>> Errors: ${spec.errors.allErrors}"

        where:
        confidenceDegree | explanation
        null             | true
        true             | null

    }

    void "test validation and json output for a valid open spec"() {
        given: "a valid specification"
        ResponseSubmissionSpecification spec = new ResponseSubmissionSpecification()
        spec.studentsProvideConfidenceDegree = true
        spec.studentsProvideExplanation = true

        when: "validating the spec"
        def isValid = spec.validateSpecification()
        if (spec.hasErrors()) {
            println ">>>>>> Errors: ${spec.errors.allErrors}"
        }

        then: "the spec is valid"
        isValid

        when: "getting the json output and parsing the output"
        String jsonStr = spec.jsonString
        println ">>>>>> jsonStr: ${jsonStr}"
        JsonSlurper jsonSlurper = new JsonSlurper()
        def map = jsonSlurper.parseText(jsonStr)

        then: "the resulting map contains all values of the spec"
        map.studentsProvideConfidenceDegree == true
        map.studentsProvideExplanation == true
    }


    void "test spec creation based on a valid json specification"() {
        given: "a specification build on a valid json specification"
        ResponseSubmissionSpecification spec = new ResponseSubmissionSpecification('''
                {
                    "studentsProvideConfidenceDegree":true,
                    "studentsProvideExplanation":false
                }
        ''')

        expect: "expect properties are set correctly"
        spec.studentsProvideConfidenceDegree
        !spec.studentsProvideExplanation
    }

}
