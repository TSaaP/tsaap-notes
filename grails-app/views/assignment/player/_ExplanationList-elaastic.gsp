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

<div style="font-size: 1rem">
  <g:if test="${displaysAll}">
    <g:set var="explanationCount" value="${0}"/>
    <g:each in="${responses}" var="response" status="i">
      <g:set var="explanation" value="${response?.explanation}"/>
      <g:if test="${explanation}">
        <g:set var="explanationCount" value="${explanationCount + 1}"/>
        <div class="ui info message explanation">
          <g:set var="grade" value="${response.meanGrade}"/>
          <strong>
            <g:if test="${grade != null}">
              <g:formatNumber number="${grade}" type="number"
                              maxFractionDigits="2"/>/5
            </g:if>
            @${response.learner.username}
          </strong>
          <g:if test="${grade != null}">
            ${message(code: "player.sequence.explanation.evaluated")} ${response.evaluationCount()} ${message(code: "player.sequence.explanation.contributors")}
          </g:if>
          <br/>
          ${raw(explanation)}
        </div>

      </g:if>
    </g:each>
    <g:if test="${!explanationCount}">
      <div class="ui warning message">
        ${message(code: "player.sequence.explanation.noExplanation")}
      </div>
    </g:if>
  </g:if>
  <g:else>
    <g:each var="i" in="${(0..<Math.min(responses.size(), 3))}">
      <g:set var="theResponse" value="${responses.get(i)}"/>
      <g:set var="explanation" value="${theResponse?.explanation}"/>
      <g:if test="${explanation}">
        <div class="ui info message explanation">
          <strong><g:formatNumber number="${theResponse.meanGrade}" type="number"
                                  maxFractionDigits="2"/>/5  @${theResponse.learner.username}</strong> ${message(code: "player.sequence.explanation.evaluated")} ${theResponse.evaluationCount()} ${message(code: "player.sequence.explanation.contributors")}<br/>
          ${raw(explanation)}
        </div>
      </g:if>

    </g:each>
    <button type="button"
            class="ui button"
            onclick="$('#all_explanations_for_${sequence.id}').modal('show');">
      ${message(code: "player.sequence.explanation.all.button")}
    </button>

    <div id="all_explanations_for_${sequence.id}"
         class="ui modal"
         tabindex="-1" role="dialog"
         aria-hidden="true">

      <div class="header">
        ${message(code: "player.sequence.explanation.all")}
      </div>

      <div class="scrolling content">
        <g:if test="${badResponses}">
          <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <div class="panel panel-default">
              <div class="panel-heading" role="tab" id="headingOne">
                <h4 class="panel-title">
                  <g:set var="responsesChoice"
                         value="${responses.size() > 0 ? responses.get(0).choiceListSpecification : null}"/>
                  <a role="button" data-toggle="collapse" data-parent="#accordion"
                     href="#collapseOne${sequence.id}"
                     aria-expanded="true" aria-controls="collapseOne${sequence.id}">
                    ${message(code: "player.sequence.users.responses")}: ${responsesChoice}<strong>-</strong>
                    ${message(code: "player.sequence.user.score", args: [100])}<strong>-</strong>
                    ${message(code: "player.sequence.responseCount")}: ${responses.size()}
                  </a>
                </h4>
              </div>

              <div id="collapseOne${sequence.id}" class="panel-collapse collapse in" role="tabpanel"
                   aria-labelledby="headingOne${sequence.id}">
                <div class="panel-body">
                  <g:render template="/assignment/player/ExplanationList-elaastic"
                            model="[responses: responses, sequence: sequence, displaysAll: true]"/>
                </div>
              </div>
            </div>

            <g:each in="${badResponses}" var="answerMap" status="i">
              <g:each in="${answerMap.value}" var="explanationList" status="j">
                <div class="panel panel-default">
                  <div class="panel-heading" role="tab" id="heading${explanationList.key}">
                    <g:set var="answerGroup" value="${sequence.id}_${i}_${j}"/>
                    <h4 class="panel-title">
                      <a role="button" data-toggle="collapse" data-parent="#accordion"
                         href="#collapse${answerGroup}" aria-expanded="true"
                         aria-controls="collapse${answerGroup}">
                        ${message(code: "player.sequence.users.responses")}: ${explanationList.key}<strong>-</strong>
                        ${message(code: "player.sequence.user.score", args: [answerMap.key])}<strong>-</strong>
                        ${message(code: "player.sequence.responseCount")}: ${explanationList.value.size()}
                      </a>
                    </h4>
                  </div>

                  <div id="collapse${answerGroup}" class="panel-collapse collapse" role="tabpanel"
                       aria-labelledby="heading${answerGroup}">
                    <div class="panel-body">
                      <g:render template="/assignment/player/ExplanationList-elaastic"
                                model="[responses: explanationList.value, sequence: sequence, displaysAll: true]"/>
                    </div>
                  </div>
                </div>
              </g:each>
            </g:each>
          </div>
        </g:if>
        <g:else>
          <g:render template="/assignment/player/ExplanationList-elaastic"
                    model="[responses: responses, sequence: sequence, displaysAll: true]"/>
        </g:else>
      </div>

      <div class="actions">
        <div class="ui cancel button">${message(code: "player.sequence.explanation.close.button")}</div>
      </div>

    </div>

  </g:else>
</div>

