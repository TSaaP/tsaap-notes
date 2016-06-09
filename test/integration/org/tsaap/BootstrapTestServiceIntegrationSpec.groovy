/*
 * Copyright 2013 Tsaap Development Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
