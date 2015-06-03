%{--
  - Copyright 2015 Tsaap Development Group
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -    http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  --}%

<g:if test="${responses.size() < 4 || displaysAll}">
    <g:each in="${responses}" var="response" status="i">
        <g:set var="explanation" value="${response?.explanation}"/>
        <g:if test="${explanation}">
            <div class="alert alert-info">
                <strong><g:formatNumber number="${explanation.grade}" type="number" maxFractionDigits="2"/> /5  @${explanation.author.username}</strong> ${message(code: "questions.explanation.evaluated")} ${explanation.evaluationCount()} ${message(code: "questions.explanation.contributors")}<br/>
                ${explanation.content}
            </div>

        </g:if>
    </g:each>
</g:if>
<g:else>
    <g:each var="i" in="${ (0..<3) }">
        <g:set var="theResponse" value="${responses.get(i)}"/>
        <g:set var="explanation" value="${theResponse?.explanation}"/>
        <g:if test="${explanation}">
            <div class="alert alert-info">
                <strong><g:formatNumber number="${explanation.grade}" type="number" maxFractionDigits="2"/> /5  @${explanation.author.username}</strong> ${message(code: "questions.explanation.evaluated")} ${explanation.evaluationCount()} ${message(code: "questions.explanation.contributors")}<br/>
                ${explanation.content}
            </div>
        </g:if>

    </g:each>
    <button type="button" class="btn btn-default btn-xs" data-toggle="modal" data-target="#all_explanations_for_${note.id}">
        ${message(code: "questions.explanation.all.button")}
    </button>
    <div id="all_explanations_for_${note.id}" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">${message(code: "questions.explanation.all")}</h4>
                </div>
                <div class="modal-body">
                    <g:render template="/questions/ExplanationList" model="[responses:responses, note:note,displaysAll:true]"/>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">${message(code: "questions.explanation.close.button")}</button>
                </div>
            </div>
        </div>
    </div>
</g:else>


