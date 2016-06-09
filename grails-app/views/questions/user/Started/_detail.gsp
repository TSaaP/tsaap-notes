<%@ page import="org.tsaap.questions.TextBlock" %>
<g:set var="question" value="${note.question}"/>

<div class="question" id="question_${note.id}">
    <g:if test="${liveSession.getResponseForUser(user)}">
        <div class="alert alert-success">
            ${message(code: "questions.user.started.wait")} &quot;<strong>${question.title}</strong>&quot;...
            <g:remoteLink action="refresh" controller="question" params="[noteId: note.id]" title="Refresh"
                          update="question_${note.id}"
                          onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"><span
                    class="glyphicon glyphicon-refresh">&nbsp;</span></g:remoteLink>
            (response count :${liveSession.responseCount()})
        </div>
    </g:if>
    <g:else>
        <g:form>
            <g:hiddenField name="liveSessId" value="${liveSession.id}"/>
            <g:hiddenField name="noteId" value="${note.id}"/>
            <p><strong>${question.title}</strong></p>
            <g:each var="block" in="${question.blockList}">
                <p>
                    <g:if test="${block instanceof TextBlock}">
                        ${block.text}
                    </g:if>
                    <g:else>
                        <g:render template="/questions/${question.questionType.name()}AnswerBlock"
                                  model="[block: block]"/>
                    </g:else>
                </p>
            </g:each>
            <g:submitToRemote action="submitResponse" controller="question" update="question_${note.id}"
                              class="btn btn-primary btn-xs" value="${message(code: "questions.user.submit")}"
                              onComplete="MathJax.Hub.Queue(['Typeset',MathJax.Hub,'question_${note.id}'])"/>
        </g:form>
    </g:else>
</div>