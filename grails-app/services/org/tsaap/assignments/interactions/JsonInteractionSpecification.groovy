package org.tsaap.assignments.interactions

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

/**
 * Abstract class for Json based specification
 */
class JsonInteractionSpecification implements InteractionSpecification {

    protected Map specificationProperties

    /**
     * Default constructor
     */
    JsonInteractionSpecification() {
        specificationProperties = new HashMap<String,Object>()
    }

    /**
     * Construct a specification based on the json string description
     * @param jsonString the specification as json string
     */
    JsonInteractionSpecification(String jsonString) {
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
