package org.tsaap.assignments.statement

import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.web.ControllerUnitTestMixin
import spock.lang.Specification

/**
 * Created by qsaieb on 06/03/2017.
 */
@TestMixin(ControllerUnitTestMixin)
class ChoiceSpecificationTest extends Specification {

  def "test GetJsonString"() {
    given: 'a new ChoiceSpecification'
    ChoiceSpecification choiceSpecification = new ChoiceSpecification()

    and: 'give an itemCount'
    int givenItemCount = 3
    choiceSpecification.itemCount = givenItemCount

    and: 'give a choiceInteractionType'
    String givenChoiceInteractionType = 'MULTIPLE'
    choiceSpecification.choiceInteractionType = givenChoiceInteractionType

    and: 'given a expectedChoiceList'
    List<ChoiceItemSpecification> givenExpectedChoiceList = [
        new ChoiceItemSpecification(0, 50f),
        new ChoiceItemSpecification(1, 50f)
    ]
    choiceSpecification.expectedChoiceList = givenExpectedChoiceList

    when: 'getJsonString representation'
    String jsonStringRepresentation = choiceSpecification.getJsonString()

    then: 'jsonStringRepresentation equal choiceSpecification json string representation'
    jsonStringRepresentation ==  '{"expectedChoiceList":[{"index":0,"score":50.0},{"index":1,"score":50.0}],"choiceInteractionType":"MULTIPLE","itemCount":3}'

  }

  def "test GetJsonString with empty ChoiceSpecification"() {
    given: 'a new ChoiceSpecification'
    ChoiceSpecification choiceSpecification = new ChoiceSpecification()

    when: 'getJsonString representation'
    String jsonStringRepresentation = choiceSpecification.getJsonString()

    then: 'jsonStringRepresentation equals empty json object'
    jsonStringRepresentation ==  '{}'

  }

  def "test GetChoiceInteractionType"() {
    given: "a new ChoiceSpecification"
    ChoiceSpecification choiceSpecification = new ChoiceSpecification()

    and: 'given a choiceInteractionType'
    String givenChoiceInteractionType = 'MULTIPLE'
    choiceSpecification.choiceInteractionType = givenChoiceInteractionType

    when: 'get a choiceInteractionType'
    String  choiceInteractionType = choiceSpecification.getChoiceInteractionType()

    then: 'choiceInteractionType equals MULTIPLE'
    choiceInteractionType == givenChoiceInteractionType
  }

  def "test GetItemCount"() {
    given: "a new ChoiceSpecification"
    ChoiceSpecification choiceSpecification = new ChoiceSpecification()

    and: 'given an Item Count'
    int givenItemCount = 5
    choiceSpecification.itemCount = givenItemCount

    when: 'get an itemCount'
    int  itemCount = choiceSpecification.getItemCount()

    then: 'itemCount equals givenItemCount'
    itemCount == givenItemCount
  }

  def "test GetExpectedChoiceList"() {
    given: "a new ChoiceSpecification"
    ChoiceSpecification choiceSpecification = new ChoiceSpecification()

    and: 'given a expectedChoiceList'
    List<ChoiceItemSpecification> givenExpectedChoiceList = [
        new ChoiceItemSpecification(0, 50f),
        new ChoiceItemSpecification(1, 50f)
    ]
    choiceSpecification.expectedChoiceList = givenExpectedChoiceList

    when: 'get an expectedChoiceList'
    List<ChoiceItemSpecification> expectedChoiceList = choiceSpecification.getExpectedChoiceList()

    then: 'expectedChoiceList equals givenExpectedChoiceList'
    expectedChoiceList[0] == givenExpectedChoiceList[0]
    expectedChoiceList[1] == givenExpectedChoiceList[1]
    expectedChoiceList[0] != givenExpectedChoiceList[1]
  }

  def "test SetChoiceInteractionType"() {
    given: "a new ChoiceSpecification"
    ChoiceSpecification choiceSpecification = new ChoiceSpecification()

    and: 'given a choiceInteractionType'
    String givenChoiceInteractionType = 'MULTIPLE'
    choiceSpecification.choiceInteractionType = givenChoiceInteractionType

    when: 'set a choiceInteractionType'
    String newChoiceInteractionType = 'EXCLUSIVE'
    choiceSpecification.choiceInteractionType = newChoiceInteractionType

    then: 'choiceInteractionType not equals givenChoiceInteractionType'
    choiceSpecification.choiceInteractionType != givenChoiceInteractionType

    and: 'choiceInteractionType equals newChoiceInteractionType'
    choiceSpecification.choiceInteractionType == newChoiceInteractionType
  }

  def "test SetItemCount"() {
    given: "a new ChoiceSpecification"
    ChoiceSpecification choiceSpecification = new ChoiceSpecification()

    and: 'given an Item Count'
    int givenItemCount = 5
    choiceSpecification.itemCount = givenItemCount

    when: 'set an itemCount'
    int newItemCount = 8
    choiceSpecification.itemCount = newItemCount

    then: 'itemCount not equals givenItemCount'
    choiceSpecification.itemCount != givenItemCount
    and: 'itemCount equal newItemCount'
    choiceSpecification.itemCount == newItemCount
  }

  def "test SetExpectedChoiceList"() {
    given: "a new ChoiceSpecification"
    ChoiceSpecification choiceSpecification = new ChoiceSpecification()

    and: 'given a expectedChoiceList'
    List<ChoiceItemSpecification> givenExpectedChoiceList = [
        new ChoiceItemSpecification(0, 50f),
        new ChoiceItemSpecification(1, 50f)
    ]
    choiceSpecification.expectedChoiceList = givenExpectedChoiceList

    when: 'set an expectedChoiceList'
    List<ChoiceItemSpecification> newExpectedChoiceList = [
        new ChoiceItemSpecification(0, 33f),
        new ChoiceItemSpecification(1, 33f),
        new ChoiceItemSpecification(2, 33f)
    ]
    choiceSpecification.expectedChoiceList = newExpectedChoiceList

    then: 'expectedChoiceList not equals givenExpectedChoiceList'
    choiceSpecification.expectedChoiceList[0] != givenExpectedChoiceList[0]
    choiceSpecification.expectedChoiceList[1] != givenExpectedChoiceList[1]
    choiceSpecification.expectedChoiceList[0] != givenExpectedChoiceList[1]

    and: 'expectedChoiceList equals newExpectedChoiceList'
    choiceSpecification.expectedChoiceList[0] == newExpectedChoiceList[0]
    choiceSpecification.expectedChoiceList[1] == newExpectedChoiceList[1]
    choiceSpecification.expectedChoiceList[2] == newExpectedChoiceList[2]
    choiceSpecification.expectedChoiceList[2] != newExpectedChoiceList[0]

  }

  def "test ChoiceWithIndexInExpectedChoiceList"() {
    given: "a new ChoiceSpecification"
    ChoiceSpecification choiceSpecification = new ChoiceSpecification()

    and: 'given a expectedChoiceList'
    List<ChoiceItemSpecification> givenExpectedChoiceList = [
        new ChoiceItemSpecification(0, 50f),
        new ChoiceItemSpecification(1, 50f)
    ]
    choiceSpecification.expectedChoiceList = givenExpectedChoiceList

    when: 'choiceWithIndexInExpectedChoiceList'
    ChoiceItemSpecification item1 = choiceSpecification.choiceWithIndexInExpectedChoiceList(0);
    ChoiceItemSpecification item2 = choiceSpecification.choiceWithIndexInExpectedChoiceList(1);

    then: 'items equals given item'
    item1 == choiceSpecification.expectedChoiceList[0]
    item2 == choiceSpecification.expectedChoiceList[1]

  }
}
