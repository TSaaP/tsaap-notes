package org.tsaap.assignments.interactions

import groovy.json.JsonException
import spock.lang.Specification

/**
 * Created by franck on 31/08/2016.
 */
class JsonInteractionSpecificationSpec extends Specification {

    void "test creation of a valid json interaction spec"() {
        given:"a json string"
        String jsonStr = '{"title": "a title", "list": [1,2,3]}'

        when: "creating a json interaction spec"
        JsonInteractionSpecification jsonInteractionSpecification = new JsonInteractionSpecification(jsonStr)

        then: "properties are set correctly"
        jsonInteractionSpecification.getSpecificationProperty("title") == "a title"
        jsonInteractionSpecification.getSpecificationProperty("list") == [1,2,3]
    }

    void "test creation based on an invalid json string"() {
        given:"a no-json string"
        String str = "no valid json"

        when: "creating a json interaction spec"
        JsonInteractionSpecification jsonInteractionSpecification = new JsonInteractionSpecification(str)

        then: "an exception is thrown"
        JsonException e = thrown()
        println ">>>>>>> ${e.message}"

        and: "the specification is not built"
        jsonInteractionSpecification == null
    }

    void "test creation based on properties set"() {
        given: "a specifiction"
        JsonInteractionSpecification jsonInteractionSpecification = new JsonInteractionSpecification()

        when: "setting some properties"
        jsonInteractionSpecification.setSpecificationProperty("title", "a title")
        jsonInteractionSpecification.setSpecificationProperty("list", [1,2,3])

        and: "getting the json string"
        String jsonStr = jsonInteractionSpecification.jsonString

        then: "the json str is OK"
        jsonStr == '{"title":"a title","list":[1,2,3]}'

    }

}
