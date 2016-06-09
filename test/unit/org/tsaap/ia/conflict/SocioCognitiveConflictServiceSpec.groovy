/*
 * Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
        def userIds = [1, 2, 3, 4, 5, 6]

        and: "a short list of explanation ids (3 or less elements)"
        def explIds = [10, 11, 12]

        when: "building the map"
        Map<Long, List<Long>> res = socioCognitiveConflictService.explanationIdListByUserIdRoundRobinAlgorithm(userIds, explIds)

        then: "all users are mapped with all explanations"
        res.keySet().each {
            println "$it, ${it.class.name}"
        }
        res.get(1l) == [10, 11, 12]
        res.get(2l) == [10, 11, 12]
        res.get(3l) == [10, 11, 12]
        res.size() == 6
        res.get(5l) == [10, 11, 12]

        when: "the list of explanation ids has more than 3 elements"
        explIds = [10, 11, 12, 13]

        and: "building the map"
        res = socioCognitiveConflictService.explanationIdListByUserIdRoundRobinAlgorithm(userIds, explIds)

        then: "the map si built with round robin algorithm to affect 3 explanations by user"
        res.get(1l) == [10, 11, 12]
        res.get(2l) == [13, 10, 11]
        res.get(3l) == [12, 13, 10]
        res.size() == 6
        res.get(5l) == [10, 11, 12]

    }

    void "test round robin for socio cognitive conflict"() {
        given: "a list of key response Id"
        def keyIds = [1, 2, 3, 4, 5]

        and: "a short list of key values ids (3 or less elements)"
        def valIds = [10, 11, 12]

        when: "building the map"
        Map<Long, Long> res = socioCognitiveConflictService.responseValByResponseKey(keyIds, valIds)

        then: "responses are mapped OK"
        res.get(1l) == 10
        res.get(2l) == 11
        res.get(3l) == 12
        res.get(4l) == 10
        res.get(5l) == 11

        when: "has more then 4 values"
        valIds = [10, 11, 12, 13, 14]
        res = socioCognitiveConflictService.responseValByResponseKey(keyIds, valIds)

        then: "we stay on the 4 firts"
        res.get(1l) == 10
        res.get(2l) == 11
        res.get(3l) == 12
        res.get(4l) == 13
        res.get(5l) == 10

        when: "has only 1 value"
        valIds = [10]
        res = socioCognitiveConflictService.responseValByResponseKey(keyIds, valIds)

        then: "everyone have the same conflict"
        res.get(1l) == 10
        res.get(2l) == 10
        res.get(3l) == 10
        res.get(4l) == 10
        res.get(5l) == 10

        when: "no key value ids"
        keyIds = []
        res = socioCognitiveConflictService.responseValByResponseKey(keyIds, valIds)

        then: "the map is empty"
        res.isEmpty()
        res.get(1l) == null


        when: "no value ids"
        keyIds = [1, 2, 3, 4, 5]
        valIds = []
        res = socioCognitiveConflictService.responseValByResponseKey(keyIds, valIds)

        then: "each key has null for value"
        res.get(1l) == null
        res.get(2l) == null
        res.get(3l) == null
        res.get(4l) == null
        res.get(5l) == null


    }

}
