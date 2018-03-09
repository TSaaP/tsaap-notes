%{--
  -
  -  Copyright (C) 2017 Ticetime
  -
  -      This program is free software: you can redistribute it and/or modify
  -      it under the terms of the GNU Affero General Public License as published by
  -      the Free Software Foundation, either version 3 of the License, or
  -      (at your option) any later version.
  -
  -      This program is distributed in the hope that it will be useful,
  -      but WITHOUT ANY WARRANTY; without even the implied warranty of
  -      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  -      GNU Affero General Public License for more details.
  -
  -      You should have received a copy of the GNU Affero General Public License
  -      along with this program.  If not, see <http://www.gnu.org/licenses/>.
  -
  --}%
<%@ page import="org.tsaap.assignments.InteractionType" %>
<%@ page import="org.tsaap.assignments.StateType" %>


<g:render template="/assignment/player/sequence/steps/steps-elaastic"
          model="[sequence              : sequenceInstance,
                  showStatistics        : true,
                  stateByInteractionType: [
                      (InteractionType.ResponseSubmission): sequenceInstance.responseSubmissionInteraction?.stateForRegisteredUsers(),
                      (InteractionType.Evaluation)        : sequenceInstance.evaluationInteraction?.stateForRegisteredUsers(),
                      (InteractionType.Read)              : sequenceInstance.readInteraction?.stateForRegisteredUsers(),
                  ]]"/>

<g:render template="/assignment/player/sequence/teacher/command"
          model="[interactionInstance: sequenceInstance.activeInteraction, user: user, attempt: 1]"/>

<g:render template="/assignment/player/sequence/teacher/sequenceInfo/sequenceInfo"
          model="[sequence              : sequenceInstance,
                  activeInteraction     : sequenceInstance.activeInteraction,
                  activeInteractionState: sequenceInstance.activeInteraction.state]"/>

<g:render template="/assignment/player/statement/show-elaastic"
          model="[statementInstance: sequenceInstance.statement, hideStatement: false]"/>

<g:set var="currentInteraction" value="${sequenceInstance.activeInteraction}"/>
<g:render
    template="/assignment/player/${currentInteraction.interactionType}/teacher/${currentInteraction.stateForTeacher(user)}-elaastic"
    model="[interactionInstance: currentInteraction, user: user, attempt: 1]"/>

<div class="ui hidden divider"></div>