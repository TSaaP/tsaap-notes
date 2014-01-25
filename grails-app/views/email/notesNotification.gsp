%{--
  - Copyright 2013 Tsaap Development Group
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


<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Notification Tsaap-Notes</title>
</head>

<body>

<p>Hi ${user.first_name}</p>


<g:each in="${contextList}" var="context">
  <g:set var="noteCount" value="${context.count_notes}"/>
  <g:if test="${noteCount > 1}">
    <p>
      There are ${noteCount} new notes on scope <strong>${context.context_name}</strong> since yesterday. <br>
      Come and see : <g:createLink absolute="true"
                                   params="[displaysAll: 'on', contextName: context.context_name, contextId: context.context_id]"
                                   controller="notes"/>
    </p>
  </g:if>
  <g:elseif test="${noteCount == 1}">
    <p>
      There is one new note on scope <strong>${context.context_name}<strong> since yesterday. <br>
      Come and see : <g:createLink absolute="true"
                                   params="[displaysAll: 'on', contextName: context.context_name, contextId: context.context_id]"
                                   controller="notes"/>
    </p>
  </g:elseif>
</g:each>


</body>
</html>