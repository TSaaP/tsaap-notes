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
  <meta name="layout" content="home"/>
  <r:require module="tsaap_ui_home"/>
</head>

<body>

<div class="container">

  <div class="body-content">

    <!-- Example row of columns -->
    <div class="row">
      <div class="col-lg-6">

        <h2><img src="images/LogoTsaapNotes.png"></h2>
        

        <p>${message(code: "index.tsaap.presentation1")}<a
                href="http://www.irit.fr"
                target="_blank">IRIT</a> ${message(code: "index.tsaap.presentation2")}
        </p>
        <p>
          <a class="twitter-timeline" href="https://twitter.com/TsaapNotes" data-widget-id="389395203288608769">Tweets de @TsaapNotes</a>
          <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>

        </p>
      </div>

      <tsaap:ifNotLoggedIn>
        <div class="col-lg-6">
          <h2>${message(code: "index.newUser.signUp.message")}</h2>
          <g:form controller="userAccount" action="doSubscribe">
            <fieldset>

              <g:if test="${user?.hasErrors()}">
                <div class="alert">
                  <g:eachError bean="${user}">
                    <li><g:message error="${it}"/></li>
                  </g:eachError>
                </div>
              </g:if>
              <div class="form-group">
                <div class="row">
                  <div class="col-lg-5">
                    <input type="text" class="form-control" id="firstName"
                           name="firstName"
                           value="${fieldValue(bean: user, field: 'firstName')}"
                           placeholder="${message(code: "index.signUp.form.firstName.placeholder")}">
                  </div>

                  <div class="col-lg-5">
                    <input type="text" class="form-control" id="lastName"
                           name="lastName"
                           value="${fieldValue(bean: user, field: 'lastName')}"
                           placeholder="${message(code: "index.signUp.form.lastName.placeholder")}">
                  </div>
                </div>
              </div>

              <div class="form-group">
                <input type="text" class="form-control" id="email" name="email"
                       placeholder="${message(code: "index.signUp.form.email.placeholder")}"
                       value="${fieldValue(bean: user, field: 'email')}">
              </div>

              <div class="form-group">
                <label class="radio-inline">
                  <input type="radio" name="role" id="STUDENT_ROLE"
                         value="STUDENT_ROLE" checked="checked"> ${message(code: "index.signUp.form.learner.radio")}
                </label>
                <label class="radio-inline">
                  <input type="radio" name="role" id="TEACHER_ROLE"
                         value="TEACHER_ROLE"> ${message(code: "index.signUp.form.teacher.radio")}
                </label>

              </div>

              <div class="form-group">
                <input type="text" class="form-control"
                       id="username" placeholder="${message(code: "index.signUp.form.username.placeholder")}"
                       name="username"
                       value="${fieldValue(bean: user, field: 'username')}">
              </div>


              <div class="form-group">
                <input type="password" class="form-control"
                       id="password" placeholder="${message(code: "index.signUp.form.password.placeholder")}" name="password">
              </div>

              <div class="form-group">
                <input type="password" class="form-control"
                       id="password2" placeholder="${message(code: "index.signUp.form.confirmPassword.placeholder")}"
                       name="password2">
              </div>

              <g:hiddenField name="language" id="language"/>

              <p class="help-block">${message(code: "index.signUp.form.agreements")} <a
                      href="terms" target="_blank">${message(code: "index.signUp.form.terms")}</a>.</p>
              <button type="submit"
                      class="btn btn-primary">${message(code: "index.signUp.form.button")} &raquo;</button>
            </fieldset>
          </g:form>

        </div>
      </tsaap:ifNotLoggedIn>
    </div>

</div>
</div> <!-- /container -->

<r:script>
    $(document).ready(function () {
        if (navigator.browserLanguage) {
            $("#language").val(navigator.browserLanguage);
        }
        else {
            $("#language").val(navigator.languages[0].slice(0,2));
        }
        console.log($("#language").val());
    });
</r:script>

</body>
</html>