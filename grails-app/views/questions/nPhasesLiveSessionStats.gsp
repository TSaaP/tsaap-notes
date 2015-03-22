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

<%--
  Created by IntelliJ IDEA.
  User: franck
  Date: 22/03/15
  Time: 09:02
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <g:if test="${params.inline && params.inline == 'on'}">
        <meta name="layout" content="inline"/>
    </g:if>
    <g:else>
        <meta name="layout" content="main"/>
    </g:else>
    <r:require modules="tsaap_ui_notes,tsaap_icons"/>
    <r:require module="export"/>
    <title>Tsaap-Notes statistics</title>
</head>

<body>
<div class="container">
    <export:formats params="[id:stats.liveSessionId]" controller="question" action="statistics"/>
    ${stats.toString()}
    <hr/>
</div>

</body>
</html>