%{--
  - Copyright 2014 Tsaap Development Group
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
    <title>${message(code: "tsaap.terms.title")}</title>
    <meta name="layout" content="error">
    <r:require module="tsaap_ui_home"/>
</head>
<body>
<div class="container">
    <h1>${message(code: "tsaap.terms.title")}</h1>
    <p>
        ${message(code: "tsaap.terms")}
    </p>
    <p>${message(code: "tsaap.terms.question")}</p>
    <g:link controller="userAccount" action="ltiConnection"
            params='[agree: "true", username: params.username, contextId: params.contextId, contextName: params.contextName, displaysAll: "on", kind: "standard"]'>${message(code: "tsaap.terms.agree")}</g:link>
    <g:link controller="userAccount" action="ltiConnection" params='[agree: "false"]'>${message(code: "tsaap.terms.disagree")}</g:link>
</div>
</body>
</html>