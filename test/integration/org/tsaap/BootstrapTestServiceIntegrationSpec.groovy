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

package org.tsaap

import spock.lang.Specification

/**
 *
 * @author franck Silvestre
 */

class BootstrapTestServiceIntegrationSpec extends Specification {

    BootstrapTestService bootstrapTestService

    def "user initialization"() {

        when:
        bootstrapTestService.initializeUsers()

        then:
        bootstrapTestService.learnerPaul != null
        bootstrapTestService.learnerPaul.username == "learner_paul"
        bootstrapTestService.learnerPaul.settings.language == 'fr'
        bootstrapTestService.teacherJeanne != null
        bootstrapTestService.teacherJeanne.normalizedUsername == "teacher_jeanne"
        bootstrapTestService.teacherJeanne.settings.language == 'fr'
        bootstrapTestService.learnerMary != null
        bootstrapTestService.learnerMary.username == "learner_Mary"
        bootstrapTestService.learnerMary.normalizedUsername == "learner_mary"
        bootstrapTestService.learnerMary.settings.language == 'fr'

    }

    def "note initialization"() {

        when:
        bootstrapTestService.initializeUsers()

        and:
        bootstrapTestService.initializeNotes()

        then:
        bootstrapTestService.note1 != null
        bootstrapTestService.note1.author.firstName == "Paul"
        bootstrapTestService.note1.content == "content note 1"
        bootstrapTestService.note2 != null
        bootstrapTestService.note2.author.firstName == "Mary"
        bootstrapTestService.note2.content == "content note 2"
    }

    def "context initialization"() {

        when:
        bootstrapTestService.initializeUsers()

        and:
        bootstrapTestService.initializeContexts()

        then:
        bootstrapTestService.context1 != null
        bootstrapTestService.context1.contextName == "Context1"
        bootstrapTestService.context2 != null
        bootstrapTestService.context2.contextName == "Context2"
    }
}
