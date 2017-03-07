package org.tsaap.assignments.statement

import spock.lang.Specification

/**
 * Created by qsaieb on 07/03/2017.
 */
class ChoiceItemSpecificationTest extends Specification {
  def "test Constructor"() {
    given: 'ChoiceItemSpecification null object'
    ChoiceItemSpecification choiceItemSpecification = null

    when: 'create new instance of ChoiceItemSpecification'
    choiceItemSpecification = new ChoiceItemSpecification(
        index: 0,
        score: 100f
    )

    then: 'choiceItemSpecification'
    choiceItemSpecification.getIndex() == 0
    choiceItemSpecification.getScore() == 100f
  }

  def "test Constructor with null index"() {
    given: 'a new instance of ChoiceItemSpecification'
    ChoiceItemSpecification choiceItemSpecification = new ChoiceItemSpecification(
        index: null,
        score: 100f
    )
    when: 'validate choiceItemSpecification'
    choiceItemSpecification.validate()

    then:
    thrown IllegalArgumentException
  }

  def "test Constructor with null score"() {
    given: 'a new instance of ChoiceItemSpecification'
    ChoiceItemSpecification choiceItemSpecification = new ChoiceItemSpecification(
        index: 0,
        score: null
    )
    when: 'validate choiceItemSpecification'
    choiceItemSpecification.validate()

    then:
    thrown IllegalArgumentException
  }

  def "test validation with score > 100f"() {
    given: 'a new instance of ChoiceItemSpecification'
    ChoiceItemSpecification choiceItemSpecification = new ChoiceItemSpecification(
        index: 0,
        score: 150f
    )
    when: 'validate choiceItemSpecification'
    choiceItemSpecification.validate()

    then:
    thrown IllegalArgumentException
  }

  def "GetIndex"() {
    given: 'a new ChoiceItemSpecification'
    ChoiceItemSpecification choiceItemSpecification = new ChoiceItemSpecification()

    and: 'given an index'
    int givenIndex = 4
    choiceItemSpecification.index = givenIndex

    when: 'get an index'
    int index = choiceItemSpecification.index

    then: 'index equals givenIndex'
    index == givenIndex
  }

  def "SetIndex"() {
    given: 'a new ChoiceItemSpecification'
    ChoiceItemSpecification choiceItemSpecification = new ChoiceItemSpecification()

    and: 'given an index'
    int givenIndex = 1
    choiceItemSpecification.index = givenIndex

    when: 'set a index'
    int newIndex = 2
    choiceItemSpecification.index = newIndex

    then: 'index not equals givenIndex'
    choiceItemSpecification.index != givenIndex

    and: 'index equals newIndex'
    choiceItemSpecification.index == newIndex
  }

  def "GetScore"() {
    given: 'a new ChoiceItemSpecification'
    ChoiceItemSpecification choiceItemSpecification = new ChoiceItemSpecification()

    and: 'given a score'
    float givenScore = 50f
    choiceItemSpecification.score = givenScore

    when: 'get a score'
    float score = choiceItemSpecification.score

    then: 'index equals givenIndex'
    score == givenScore
  }

  def "SetScore"() {
    given: 'a new ChoiceItemSpecification'
    ChoiceItemSpecification choiceItemSpecification = new ChoiceItemSpecification()

    and: 'given a score'
    float givenScore = 50f
    choiceItemSpecification.score = givenScore

    when: 'set a score'
    float newScore = 33f
    choiceItemSpecification.score = newScore

    then: 'score not equals givenScore'
    choiceItemSpecification.score != givenScore

    and: 'score equals newScore'
    choiceItemSpecification.score == newScore
  }
}
