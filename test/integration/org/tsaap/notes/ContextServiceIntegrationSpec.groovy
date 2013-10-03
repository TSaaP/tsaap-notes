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

import org.gcontracts.PreconditionViolation
import org.tsaap.BootstrapTestService
import spock.lang.Specification
import spock.lang.Unroll

class ContextServiceIntegrationSpec extends Specification {

  BootstrapTestService bootstrapTestService
  ContextService contextService
  NoteService noteService

  def setup() {
    bootstrapTestService.initializeTests()
  }

  @Unroll
  def "add context with name '#contextName' has errors: #contextHasErrors"() {

    when: Context context = contextService.saveContext(new Context(owner: bootstrapTestService.learnerPaul, contextName: contextName, url: url, descriptionAsNote: descContent))

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

  def "delete contexte"() {

    given: "a context with notes"
    Context context = contextService.saveContext(new Context(owner: bootstrapTestService.learnerPaul, contextName: "aContext"))
    Note note = noteService.addNote(bootstrapTestService.learnerMary, "a note...", context)

    when: "trying to delete the context"
    contextService.deleteContext(context, context.owner)

    then: "the delete fails due to precondition on attached notes"
    thrown(PreconditionViolation)

    when: "a context has no notes and trying to delete the context"
    noteService.deleteNoteByAuthor(note, bootstrapTestService.learnerMary)
    contextService.deleteContext(context, context.owner)

    then: "the delete is OK"
    Context.get(context.id) == null

    when: "the user trying to delete the context is not the owner"
    context = contextService.saveContext(new Context(owner: bootstrapTestService.learnerPaul, contextName: "aContext"))
    contextService.deleteContext(context, bootstrapTestService.learnerMary)

    then: "the delete fails"
    thrown(PreconditionViolation)

  }

  def "subscribe a user on a context"() {

    given: "a context"
    Context context = contextService.saveContext(new Context(owner: bootstrapTestService.learnerPaul, contextName: "aContext"))

    when: "the owner of the context wants to subscribe to his context"
    contextService.subscribeUserOnContext(bootstrapTestService.learnerPaul, context)

    then: "the subscription fails"
    thrown PreconditionViolation

    when: "when the user is not the owner"
    ContextFollower follower = contextService.subscribeUserOnContext(bootstrapTestService.learnerMary, context)

    then: "subscription is OK"
    follower != null
    !follower.hasErrors()
    follower.id != null

    when: "the user was a follower and wants to follow again"
    contextService.unsuscribeUserOnContext(bootstrapTestService.learnerMary, context)
    follower = contextService.subscribeUserOnContext(bootstrapTestService.learnerMary, context)

    then: "the subscription is OK and there is always one ContextFollower"
    follower != null
    !follower.hasErrors()
    follower.id != null
    ContextFollower.countByContextAndFollower(context, bootstrapTestService.learnerMary) == 1
  }

  def "unsubscribe a user on a context"() {

    given: "a user following a context"
    Context context = contextService.saveContext(new Context(owner: bootstrapTestService.learnerPaul, contextName: "aContext"))
    contextService.subscribeUserOnContext(bootstrapTestService.learnerMary, context)

    when: "the user unsubscribes"
    ContextFollower follower = contextService.unsuscribeUserOnContext(bootstrapTestService.learnerMary, context)

    then: "the objet ContextFollower is marked as unsubscription but not destroyed"
    !context.isFollowedByUser(bootstrapTestService.learnerMary)
    follower.isNoMoreSubscribed
    follower.unsusbscriptionDate


  }

}
