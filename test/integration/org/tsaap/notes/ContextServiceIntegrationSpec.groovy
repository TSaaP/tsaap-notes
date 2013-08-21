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



package org.tsaap.notes

import org.tsaap.BootstrapTestService
import spock.lang.Specification
import spock.lang.Unroll

class ContextServiceIntegrationSpec extends Specification {

  BootstrapTestService bootstrapTestService
  ContextService contextService

  @Unroll
  def "add context with name '#contextName' has errors: #contextHasErrors"() {

    bootstrapTestService.initializeTests()

    when: Context context = contextService.addContext(new Context(owner: bootstrapTestService.learnerPaul, contextName: contextName, url: url, descriptionAsNote: descContent))

    then: context.hasErrors() == contextHasErrors
    if (!context.hasErrors()) {
      context.contextName == contextName
      context.descriptionAsNote == descContent
      context.url == url
    }

    where: contextHasErrors | descContent                                  | contextName  | url
    true                    | 'NR'                                         | 'not a name' | 'http://www.irit.fr'
    false                   | 'Un context avec des #tags et des @mentions' | 'ivvq_sd1'   | 'http://www.irit.fr'

  }


}
