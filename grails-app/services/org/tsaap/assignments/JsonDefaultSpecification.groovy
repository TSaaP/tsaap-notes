package org.tsaap.assignments

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.tsaap.assignments.JsonSpecification

/**
 * Abstract class for Json based specification
 */
class JsonDefaultSpecification implements JsonSpecification {

    protected Map specificationProperties

    /**
     * Default constructor
     */
    JsonDefaultSpecification() {
        specificationProperties = new HashMap<String,Object>()
    }

    /**
     * Construct a specification based on the json string description
     * @param jsonString the specification as json string
     */
    JsonDefaultSpecification(String jsonString) {
        JsonSlurper jsonSlurper = new JsonSlurper()
        specificationProperties = jsonSlurper.parseText(jsonString)

    }

    @Override
    String getJsonString() {
        if (validateSpecification()) {
            return JsonOutput.toJson(specificationProperties)
        }
        null
    }

    /**
     * Validate a specification
     */
    boolean validateSpecification() {
        true
    }


    /**
     * Get the value corresponding to the property name
     * @param property name
     */
    def getSpecificationProperty(String propertyName) {
        specificationProperties.get(propertyName)
    }

    /**
     * Set a property value
     * @param propertyName the property name
     * @param propertyValue the property value
     */
    def setSpecificationProperty(String propertyName, Object propertyValue) {
        specificationProperties.put(propertyName, propertyValue)
    }

}
