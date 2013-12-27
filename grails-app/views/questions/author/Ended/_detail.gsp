<%@ page import="org.tsaap.questions.TextBlock" %>
<g:set var="question" value="${note.question}"/>
<div class="question" id="question_${note.id}">

        <p><strong>${question.title}</strong></p>
        <g:each var="block" in="${question.blockList}">
            <p>
                <g:if test="${block instanceof TextBlock}">
                    ${block.text}
                </g:if>
                <g:else>
                    <g:render template="/questions/${question.questionType.name()}AnswerBlock" model="[block: block]"/>
                </g:else>
            </p>
        </g:each>
    <g:remoteLink action="startLiveSession" controller="question" params="[liveSessId:liveSession.id,noteId:note.id]"
                  class="btn btn-warning btn-xs" update="question_${note.id}">Temporary Replay</g:remoteLink>

</div>