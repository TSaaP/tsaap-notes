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

<%--
  Created by IntelliJ IDEA.
  User: fsil
  Date: 18/11/13
  Time: 11:47
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Email checking - Tsaap-Notes</title>
</head>

<body>
<p>Hi ${user.first_name}</p>

<p>
  You have successfully subscribed to Tsaap-Notes. To activate your account you need click on the link below or copy-paste it in you favorite browser.<br><br>
  <g:createLink absolute="true"
                params='[actKey: "${actKey}", id:"${user.user_id}"]' action="doEnableUser"
                controller="userAccount"/>
</p>
</body>
</html>