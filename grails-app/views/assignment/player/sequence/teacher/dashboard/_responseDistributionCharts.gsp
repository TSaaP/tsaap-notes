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

<div style="font-size: 1rem;" id="interaction_${interactionInstance.id}_result">
  <g:set var="choiceSpecification" value="${interactionInstance.sequence.statement.getChoiceSpecificationObject()}"/>
  <g:if test="${interactionInstance.sequence.statement.hasChoices()}">
    <g:set var="resultList" value="${interactionInstance.resultsByAttempt()["1"]}"/>
    <g:set var="resultList2" value="${interactionInstance.resultsByAttempt()?.get("2")}"/>
    <g:each var="i" in="${(1..choiceSpecification.itemCount)}">
      <g:set var="choiceStatus"
             value="${choiceSpecification.expectedChoiceListContainsChoiceWithIndex(i) ? 'green' : 'red'}"/>
      <g:set var="percentResult" value="${resultList?.get(i)}"/>
      <g:set var="percentResult2" value="${resultList2?.get(i)}"/>
      <div class="ui ${choiceStatus} top attached small message">
        <div class="header">
          ${message(code: "player.sequence.interaction.choice.label")} ${i}
        </div>
      </div>

      <div class="ui bottom attached segment">
        <div id="interaction_${interactionInstance.id}_choice_${i}_result" class="ui ${choiceStatus} compact progress"
             data-percent="${percentResult}">
          <div class="bar">
            <div class="progress"></div>
          </div>
        </div>

        <g:if test="${percentResult2 != null}">

          <div class="ui  ${choiceStatus} compact progress" data-percent="${percentResult2}">
            <div class="bar">
              <div class="progress"></div>
            </div>
          </div>

        </g:if>
      </div>

    </g:each>
    <g:set var="percentResult" value="${resultList?.get(0)}"/>
    <g:set var="percentResult2" value="${resultList2?.get(0)}"/>

    <g:if test="${percentResult || percentResult2}">
      <div class="ui top attached warning small message">
        <div class="header">
          ${message(code: "player.sequence.interaction.NoResponse.label")}
        </div>
      </div>

      <div class="ui bottom attached segment" style="margin-bottom: 1rem;">

        <div class="ui warning compact progress"
             data-percent="${percentResult}">
          <div class="bar">
            <div class="progress"></div>
          </div>
        </div>

        <g:if test="${percentResult2 != null}">
          <div class="ui yellow compact progress"
               data-percent="${percentResult2}">
            <div class="bar">
              <div class="progress"></div>
            </div>
          </div>
        </g:if>
      </div>

    </g:if>
  </g:if>
</div>
<r:script>
$('#interaction_${interactionInstance.id}_result .ui.progress').progress({
  autoSuccess: false,
  showActivity: false
});
</r:script>




