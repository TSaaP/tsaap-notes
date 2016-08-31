package org.tsaap.assignments.interactions

import groovy.json.JsonSlurper
import spock.lang.Specification

/**
 * Created by franck on 31/08/2016.
 */
class ResponseSubmissionSpecificationTest extends Specification {

    void "test validation and json output for a valid spec"() {
        given: "a valid specification"
        ResponseSubmissionSpecification spec = new ResponseSubmissionSpecification()
        spec.studentsProvideConfidenceDegree = true
        spec.studentsProvideExplanation = true
        spec.choiceInteractionType = ChoiceInteractionType.EXCLUSIVE.name()
        spec.itemCount = 4
        spec.expectedChoiceList = [1, 3]

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
        map.choiceInteractionType == ChoiceInteractionType.EXCLUSIVE.name()
        map.itemCount == 4
        map.expectedChoiceList == [1, 3]
    }

    void "test json output for an invalid spec"() {
        given: "a valid specification"
        ResponseSubmissionSpecification spec = new ResponseSubmissionSpecification()
        spec.studentsProvideConfidenceDegree = null
        spec.studentsProvideExplanation = true
        spec.choiceInteractionType = ChoiceInteractionType.EXCLUSIVE.name()
        spec.itemCount = 4

        when: "getting the json output"
        String jsonStr = spec.jsonString

        then: "the no string is available"
        jsonStr == null

        and: "the spec has errors"
        spec.hasErrors()
        println ">>>>>> Errors: ${spec.errors.allErrors}"
    }

    void "test spec creation based on a valid json specification"() {
        given: "a specification build on a valid json specification"
        ResponseSubmissionSpecification spec = new ResponseSubmissionSpecification('''
                {
                    "choiceInteractionType":"MULTIPLE",
                    "itemCount":4,
                    "studentsProvideConfidenceDegree":true,
                    "studentsProvideExplanation":false
                }
        ''')

        expect: "expect properties are set correctly"
        spec.choiceInteractionType == ChoiceInteractionType.MULTIPLE.name()
        spec.itemCount == 4
        spec.studentsProvideConfidenceDegree
        !spec.studentsProvideExplanation

    }

}
