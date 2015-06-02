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
<html lang="en">
<head>
  <title>${message(code: "springSecurity.login.page.title")}</title>
  <meta name='layout' content='home'/>
  <r:require module="tsaap_ui_signin"/>
</head>

<body>
<div class="container">

  <form class="form-signin" action='${postUrl}' id='loginForm' method="post">
    <h3>${message(code: "springSecurity.login.connexion.title")}</h3>
    <g:if test='${flash.message}'>
      <div class='alert'>${flash.message}</div>
    </g:if>
    <div class="form-group">
      <input type="text" class="input-block-level" placeholder=${message(code: "springSecurity.login.username.placeholder")}
             autofocus name='j_username' id='username'>
    </div>

    <div class="form-group">
      <input type="password" class="input-block-level" placeholder=${message(code: "springSecurity.login.password.placeholder")}
             name='j_password' id='password'>
    </div>

    <div class="form-group">
      <label class="checkbox">
        <g:if test="${hasCookie}">
          <input type="checkbox" value="remember-me"
                 name='${rememberMeParameter}'
                 id='remember_me' checked='checked'/> ${message(code: "springSecurity.login.rememberMe.checkbox")}
        </g:if>
        <g:else>
          <input type="checkbox" value="remember-me"
                 name='${rememberMeParameter}'
                 id='remember_me'/> ${message(code: "springSecurity.login.rememberMe.checkbox")}
        </g:else>
      </label>
    </div>
    <button class="btn btn-large btn-primary btn-block"
            type="submit"
            id="submit">${message(code: "springSecurity.login.button")}</button>
  </form>

</div>

</body>
</html>
