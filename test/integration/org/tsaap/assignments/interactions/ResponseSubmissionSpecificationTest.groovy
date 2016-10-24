package org.tsaap.assignments.interactions

import groovy.json.JsonSlurper
import spock.lang.Specification

/**
 * Created by franck on 31/08/2016.
 */
class ResponseSubmissionSpecificationTest extends Specification {

    void "test validation and json output for a valid choice spec"() {
        given: "a valid specification"
        ResponseSubmissionSpecification spec = new ResponseSubmissionSpecification()
        spec.studentsProvideConfidenceDegree = true
        spec.studentsProvideExplanation = true
        spec.choiceInteractionType = ChoiceInteractionType.EXCLUSIVE.name()
        spec.itemCount = 4
        spec.expectedChoiceList = [new InteractionChoice(1, 50), new InteractionChoice(3, 50)]

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
        map.expectedChoiceList.size() == 2
        map.expectedChoiceList[0].index == 1
        map.expectedChoiceList[0].score == 50.0
        map.expectedChoiceList[1].index == 3
        map.expectedChoiceList[1].score == 50.0

    }

    void "test json output for an invalid choice spec"() {
        given: "a non valid specification"
        ResponseSubmissionSpecification spec = new ResponseSubmissionSpecification()
        spec.studentsProvideConfidenceDegree = confidenceDegree
        spec.studentsProvideExplanation = explanation
        spec.choiceInteractionType = choiceInteractionType
        spec.itemCount = itemCount
        spec.expectedChoiceList = expectedChoiceList

        when: "getting the json output"
        String jsonStr = spec.jsonString

        then: "the no string is available"
        jsonStr == null

        and: "the spec has errors"
        spec.hasErrors()
        println ">>>>>> Errors: ${spec.errors.allErrors}"

        where:
        confidenceDegree | explanation | choiceInteractionType                  | itemCount | expectedChoiceList
        null             | true        | ChoiceInteractionType.EXCLUSIVE.name() | 4         | [new InteractionChoice(1, 50), new InteractionChoice(3, 50)]
        true             | null        | ChoiceInteractionType.EXCLUSIVE.name() | 4         | [new InteractionChoice(1, 50), new InteractionChoice(3, 50)]
        true             | true        | ChoiceInteractionType.EXCLUSIVE.name() | 4         | null
        true             | true        | ChoiceInteractionType.EXCLUSIVE.name() | 4         | []
        true             | false       | null                                   | null      | null

    }

    void "test validation and json output for a valid open spec"() {
        given: "a valid specification"
        ResponseSubmissionSpecification spec = new ResponseSubmissionSpecification()
        spec.studentsProvideConfidenceDegree = true
        spec.studentsProvideExplanation = true
        spec.choiceInteractionType = null
        spec.itemCount = null
        spec.expectedChoiceList = null

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
        map.choiceInteractionType == null
        map.itemCount == null
        map.expectedChoiceList == null

    }


    void "test spec creation based on a valid json specification"() {
        given: "a specification build on a valid json specification"
        ResponseSubmissionSpecification spec = new ResponseSubmissionSpecification('''
                {
                    "choiceInteractionType":"MULTIPLE",
                    "itemCount":4,
                    "studentsProvideConfidenceDegree":true,
                    "studentsProvideExplanation":false,
                    "expectedChoiceList":[{"index":1,"score":50},{"index":3,"score":50}]
                }
        ''')

        expect: "expect properties are set correctly"
        spec.choiceInteractionType == ChoiceInteractionType.MULTIPLE.name()
        spec.itemCount == 4
        spec.studentsProvideConfidenceDegree
        !spec.studentsProvideExplanation
        spec.expectedChoiceList.size() == 2
        spec.expectedChoiceList[0] instanceof InteractionChoice
        spec.expectedChoiceList[0].index == 1
        spec.expectedChoiceList[0].score == 50.0
        spec.expectedChoiceList[1] instanceof InteractionChoice
        spec.expectedChoiceList[1].index == 3
        spec.expectedChoiceList[1].score == 50.0
        spec.expectedChoiceListContainsChoiceWithIndex(1)
        !spec.expectedChoiceListContainsChoiceWithIndex(2)
        spec.expectedChoiceListContainsChoiceWithIndex(3)

    }

}
