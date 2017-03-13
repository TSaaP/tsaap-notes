package org.tsaap.assignments

import org.tsaap.assignments.interactions.ResponseSubmissionSpecification
import spock.lang.Specification

/**
 * Created by franck on 15/11/2016.
 */
class PlayerControllerTest extends Specification {

    PlayerController playerController = new PlayerController()

    def "GetChoiceListFromParams multiple choice nominal case"() {

        given: "params containing several integer as string"
        def params = [choiceList:["1", "2"]]

        and: "specification of statement"
        def statement = Mock(Statement) {
            isMultipleChoice() >> true
        }

        when: "extracting choice list from params"
        def choices = playerController.getChoiceListFromParams(statement, params)

        then: "choices is a list of integers "
        choices == [1,2]
    }

    def "GetChoiceListFromParams multiple choice  case with one null value"() {

        given: "params containing several integer as string"
        def params = [choiceList:["1", "null"]]

        and: "specification of statement"
        def statement = Mock(Statement) {
            isMultipleChoice() >> true
        }
        when: "extracting choice list from params"
        def choices = playerController.getChoiceListFromParams(statement, params)

        then: "choices is a list of integer "
        choices == [1]
    }

    def "GetChoiceListFromParams multiple choice case with two null values"() {

        given: "params containing several integer as string"
        def params = [choiceList:["null", "null"]]

        and: "specification of statement"
        def statement = Mock(Statement) {
            isMultipleChoice() >> true
        }

        when: "extracting choice list from params"
        def choices = playerController.getChoiceListFromParams(statement, params)

        then: "choices is an empty list  "
        choices == []
    }

    def "GetChoiceListFromParams multiple choice  case one  null value as null object"() {

        given: "params containing several integer as string"
        def params = [choiceList:[null]]

        and: "specification of statement"
        def statement = Mock(Statement) {
            isMultipleChoice() >> true
        }

        when: "extracting choice list from params"
        def choices = playerController.getChoiceListFromParams(statement, params)

        then: "choices is an empty list  "
        choices == []
    }

    def "GetChoiceListFromParams exclusive choice  case with one null value"() {

        given: "params containing several integer as string"
        def params = [exclusiveChoice:"null"]

        and: "specification of statement"
        def statement = Mock(Statement) {
            isMultipleChoice() >> true
        }

        when: "extracting choice list from params"
        def choices = playerController.getChoiceListFromParams(statement, params)

        then: "choices is an empty list  "
        choices == []
    }

    def "GetChoiceListFromParams exclusive choice  case with one null value as null object"() {

        given: "params containing several integer as string"
        def params = [exclusiveChoice:null]

        and: "specification of statement"
        def statement = Mock(Statement) {
            isMultipleChoice() >> true
        }

        when: "extracting choice list from params"
        def choices = playerController.getChoiceListFromParams(statement, params)

        then: "choices is an empty list  "
        choices == []
    }

    def "GetChoiceListFromParams exclusive choice nominal  case"() {

        given: "params containing several integer as string"
        def params = [exclusiveChoice:"2"]

        and: "specification of statement"
        def statement = Mock(Statement) {
            isMultipleChoice() >> false
        }

        when: "extracting choice list from params"
        def choices = playerController.getChoiceListFromParams(statement, params)

        then: "choices is ok  "
        choices == [2]
    }
}
