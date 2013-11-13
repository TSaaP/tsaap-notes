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
<!DOCTYPE html>
<html>
<head>
  <title>Tsaap-Notes</title>
  <meta name="layout" content="home">
  <r:require module="tsaap_ui_home"/>
</head>

<body>
<div class="container">

  <div class="alert">

    <g:if test="${flash.message}">
      <p>${flash.message}</p>
    </g:if>
    <g:else>
      <p>
        Sorry, something went wrong.
      </p>
    </g:else>
    <p>
      You can continue on Tsaap-Notes following <g:link uri="/login/auth">this link</g:link>.
    </p>

  </div>

  <g:if env="development">
    <p>
      <a href="#"
         onclick="showException()">Click to see detail of the error.</a>
    </p>

    <div id="exception" style="display:none">
      <g:renderException exception="${exception}"/>
    </div>
  </g:if>
</div>
<r:script>
  function showException() {
    $('#exception').show()
  }

</r:script>
</body>
</html>