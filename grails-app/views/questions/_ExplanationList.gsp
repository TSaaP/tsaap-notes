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

<g:if test="${responses.size() < 4 || displaysAll}">
    <g:each in="${responses}" var="response" status="i">
        <g:set var="explanation" value="${response?.explanation}"/>
        <g:if test="${explanation}">
            <div class="alert alert-info">
                <strong><g:formatNumber number="${explanation.findOrUpdateMeanGrade()}" type="number"
                                        maxFractionDigits="2"/> /5  @${explanation.author.username}</strong> ${message(code: "questions.explanation.evaluated")} ${explanation.evaluationCount()} ${message(code: "questions.explanation.contributors")}<br/>
                ${explanation.content}
            </div>

        </g:if>
    </g:each>
</g:if>
<g:else>
    <g:each var="i" in="${(0..<3)}">
        <g:set var="theResponse" value="${responses.get(i)}"/>
        <g:set var="explanation" value="${theResponse?.explanation}"/>
        <g:if test="${explanation}">
            <div class="alert alert-info">
                <strong><g:formatNumber number="${explanation.findOrUpdateMeanGrade()}" type="number"
                                        maxFractionDigits="2"/> /5  @${explanation.author.username}</strong> ${message(code: "questions.explanation.evaluated")} ${explanation.evaluationCount()} ${message(code: "questions.explanation.contributors")}<br/>
                ${explanation.content}
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
                    <g:render template="/questions/ExplanationList"
                              model="[responses: responses, note: note, displaysAll: true]"/>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-default"
                            data-dismiss="modal">${message(code: "questions.explanation.close.button")}</button>
                </div>
            </div>
        </div>
    </div>
</g:else>


