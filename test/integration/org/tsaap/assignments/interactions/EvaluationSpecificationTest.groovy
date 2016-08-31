package org.tsaap.assignments.interactions

import groovy.json.JsonSlurper
import spock.lang.Specification

/**
 * Created by franck on 31/08/2016.
 */
class EvaluationSpecificationTest extends Specification {

    void "test validation and json output for a valid spec"() {
        given: "a valid specification"
        EvaluationSpecification spec = new EvaluationSpecification()
        spec.responseToEvaluateCount = 3

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
        map.responseToEvaluateCount == 3
    }

    void "test json output for an invalid spec"() {
        given: "a valid specification"
        EvaluationSpecification spec = new EvaluationSpecification()
        spec.responseToEvaluateCount = null

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
        EvaluationSpecification spec = new EvaluationSpecification('{"responseToEvaluateCount":3}')

        expect: "expect properties are set correctly"
        spec.responseToEvaluateCount == 3

    }

}
