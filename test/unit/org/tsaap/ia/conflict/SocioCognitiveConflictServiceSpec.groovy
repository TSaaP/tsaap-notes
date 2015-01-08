package org.tsaap.ia.conflict

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(SocioCognitiveConflictService)
class SocioCognitiveConflictServiceSpec extends Specification {

    SocioCognitiveConflictService socioCognitiveConflictService

    def setup() {
        socioCognitiveConflictService = new SocioCognitiveConflictService()
    }

    def cleanup() {
    }

    void "test round robin algorithm for distribution of explanation to evaluate"() {
        given: "a list of user Id"
        def userIds = [1,2,3,4,5,6]

        and: "a short list of explanation ids (3 or less elements)"
        def explIds = [10,11,12]

        when: "building the map"
        Map<Long, List<Long>> res = socioCognitiveConflictService.explanationIdListByUserIdRoundRobinAlgorithm(userIds,explIds)

        then: "all users are mapped with all explanations"
        res.keySet().each {
            println "$it, ${it.class.name}"
        }
        res.get(1l) == [10,11,12]
        res.get(2l) == [10,11,12]
        res.get(3l) == [10,11,12]
        res.size() == 6
        res.get(5l) == [10,11,12]

        when: "the list of explanation ids has more than 3 elements"
        explIds = [10,11,12,13]

        and: "building the map"
        res = socioCognitiveConflictService.explanationIdListByUserIdRoundRobinAlgorithm(userIds,explIds)

        then: "the map si built with round robin algorithm to affect 3 explanations by user"
        res.get(1l) == [10,11,12]
        res.get(2l) == [13,10,11]
        res.get(3l) == [12,13,10]
        res.size() == 6
        res.get(5l) == [10,11,12]

    }
}
