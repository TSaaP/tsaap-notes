%{--
  - Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
  -
  -     This program is free software: you can redistribute it and/or modify
  -     it under the terms of the GNU Affero General Public License as published by
  -     the Free Software Foundation, either version 3 of the License, or
  -     (at your option) any later version.
  -
  -     This program is distributed in the hope that it will be useful,
  -     but WITHOUT ANY WARRANTY; without even the implied warranty of
  -     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  -     GNU Affero General Public License for more details.
  -
  -     You should have received a copy of the GNU Affero General Public License
  -     along with this program.  If not, see <http://www.gnu.org/licenses/>.
  --}%

<g:if test="${displaysAll}">
    <g:each in="${responses}" var="response" status="i">
        <g:set var="explanation" value="${response?.explanation}"/>
        <g:if test="${explanation}">
            <div class="alert alert-info">
                <g:set var="grade" value="${explanation.findOrUpdateMeanGrade()}"/>
                <strong>
                    <g:if test="${grade != null}">
                        <g:formatNumber number="${grade}" type="number"
                                        maxFractionDigits="2"/>/5
                    </g:if>
                    @${explanation.author.username}
                </strong>
                <g:if test="${grade != null}">
                    ${message(code: "questions.explanation.evaluated")} ${explanation.evaluationCount()} ${message(code: "questions.explanation.contributors")}
                </g:if>
                <br/>
                ${raw(explanation.content)}
            </div>

        </g:if>
    </g:each>
</g:if>
<g:else>
    <g:each var="i" in="${(0..<Math.min(responses.size(), 3))}">
        <g:set var="theResponse" value="${responses.get(i)}"/>
        <g:set var="explanation" value="${theResponse?.explanation}"/>
        <g:if test="${explanation}">
            <div class="alert alert-info">
                <strong><g:formatNumber number="${explanation.findOrUpdateMeanGrade()}" type="number"
                                        maxFractionDigits="2"/>/5  @${explanation.author.username}</strong> ${message(code: "questions.explanation.evaluated")} ${explanation.evaluationCount()} ${message(code: "questions.explanation.contributors")}<br/>
                ${raw(explanation.content)}
            </div>
        </g:if>

    </g:each>
    <button type="button" class="btn btn-default btn-xs" data-toggle="modal"
            data-target="#all_explanations_for_${note.id}">
        ${message(code: "questions.explanation.all.button")}
    </button>

    <div id="all_explanations_for_${note.id}" class="modal fade" tabindex="-1" role="dialog"
         aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">${message(code: "questions.explanation.all")}</h4>
                </div>

                <div class="modal-body">
                    <g:if test="${badResponses != null}">
                        <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                            <div class="panel panel-default">
                                <div class="panel-heading" role="tab" id="headingOne">
                                    <h4 class="panel-title">
                                        <g:set var="responsesChoice" value="${responses.get(0).prettyAnswers()}"/>
                                        <a role="button" data-toggle="collapse" data-parent="#accordion"
                                           href="#collapseOne"
                                           aria-expanded="true" aria-controls="collapseOne">
                                            ${message(code: "liveSessionResponse.users.responses")}: ${responsesChoice}<strong>-</strong>
                                            ${message(code: "liveSessionResponse.user.score", args: [100])}<strong>-</strong>
                                            ${message(code: "questions.responseCount")}: ${responses.size()}
                                        </a>
                                    </h4>
                                </div>

                                <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel"
                                     aria-labelledby="headingOne">
                                    <div class="panel-body">
                                        <g:render template="/questions/ExplanationList"
                                                  model="[responses: responses, note: note, displaysAll: true]"/>
                                    </div>
                                </div>
                            </div>

                            <g:each in="${badResponses}" var="answerMap" status="i">
                                <g:each in="${answerMap.value}" var="explanationList" status="j">
                                    <div class="panel panel-default">
                                        <div class="panel-heading" role="tab" id="heading${explanationList.key}">
                                            <g:set var="answerGroup" value="${i}_${j}"/>
                                            <h4 class="panel-title">
                                                <a role="button" data-toggle="collapse" data-parent="#accordion"
                                                   href="#collapse${answerGroup}" aria-expanded="true"
                                                   aria-controls="collapse${answerGroup}">
                                                    ${message(code: "liveSessionResponse.users.responses")}: ${explanationList.key}<strong>-</strong>
                                                    ${message(code: "liveSessionResponse.user.score", args: [answerMap.key])}<strong>-</strong>
                                                    ${message(code: "questions.responseCount")}: ${explanationList.value.size()}
                                                </a>
                                            </h4>
                                        </div>

                                        <div id="collapse${answerGroup}" class="panel-collapse collapse" role="tabpanel"
                                             aria-labelledby="heading${answerGroup}">
                                            <div class="panel-body">
                                                <g:render template="/questions/ExplanationList"
                                                          model="[responses: explanationList.value, note: note, displaysAll: true]"/>
                                            </div>
                                        </div>
                                    </div>
                                </g:each>
                            </g:each>
                        </div>
                    </g:if>
                    <g:else>
                        <g:render template="/questions/ExplanationList"
                                  model="[responses: responses, note: note, displaysAll: true]"/>
                    </g:else>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default"
                                data-dismiss="modal">${message(code: "questions.explanation.close.button")}</button>
                    </div>
                </div>
            </div>
        </div>
    </div>

</g:else>


