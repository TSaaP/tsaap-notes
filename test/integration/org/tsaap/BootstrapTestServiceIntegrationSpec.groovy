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

import grails.plugin.spock.IntegrationSpec
import org.tsaap.BootstrapTestService

/**
 *
 * @author franck Silvestre
 */

class BootstrapTestServiceIntegrationSpec extends IntegrationSpec {

  BootstrapTestService bootstrapTestService

  def "user initialization"() {

    when: bootstrapTestService.initializeUsers()

    then: bootstrapTestService.learnerPaul != null
    bootstrapTestService.learnerPaul.username == "learner_paul"
    bootstrapTestService.teacherJeanne != null
    bootstrapTestService.teacherJeanne.normalizedUsername == "teacher_jeanne"
    bootstrapTestService.learnerMary != null
    bootstrapTestService.learnerMary.username == "learner_Mary"
    bootstrapTestService.learnerMary.normalizedUsername == "learner_mary"

  }


}
