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
  <meta name="layout" content="main"/>
  <r:require module="tsaap_ui_home"/>
</head>

<body>

<div class="navbar navbar-inverse navbar-fixed-top">
  <div class="container">
    <button type="button" class="navbar-toggle" data-toggle="collapse"
            data-target=".nav-collapse">
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
    </button>
    <a class="navbar-brand" href="#">TsaaP-Notes</a>

    <div class="nav-collapse collapse">
      <ul class="nav navbar-nav">
        <li class="active"><a href="#">Home</a></li>
        <li><a href="#about">About</a></li>
        <li><a href="#contact">Contact</a></li>
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b
                  class="caret"></b></a>
          <ul class="dropdown-menu">
            <li><a href="#">Action</a></li>
            <li><a href="#">Another action</a></li>
            <li><a href="#">Something else here</a></li>
            <li class="divider"></li>
            <li class="nav-header">Nav header</li>
            <li><a href="#">Separated link</a></li>
            <li><a href="#">One more separated link</a></li>
          </ul>
        </li>
      </ul>

      <div class="nav-collapse collapse pull-right">
        <ul class="nav navbar-nav">
          <li><g:link
                  uri='/login/auth'>Already an account ? Sign in &raquo;</g:link></li>
        </ul>
      </div>
    </div><!--/.nav-collapse -->
  </div>
</div>

<div class="container">

  <div class="body-content">

    <!-- Example row of columns -->
    <div class="row">
      <div class="col-lg-6">
        <h2>TsaaP-Notes</h2>

        <p>Microblogging tool dedicated to learners and teachers. TsaaP-notes is born in the french research laboratory <a
                href="http://www.irit.fr"
                target="_blank">IRIT</a> to help designing intelligent functions in technology-enhanced learning software.
        </p>
      </div>


      <div class="col-lg-6">
        <h2>New to TsaaP-Notes ? Sign up</h2>
        <g:form controller="userAccount" action="doSubscribe">
          <fieldset>

            <g:if test="${user?.hasErrors()}">
              <div class="alert">
                <g:eachError bean="${user}">
                  <li><g:message code="${it.code}"/></li>
                </g:eachError>
              </div>
            </g:if>
            <div class="form-group">
              <div class="row">
                <div class="col-lg-5">
                  <input type="text" class="form-control" id="firstName"
                         name="firstName"
                         placeholder="First name">
                </div>

                <div class="col-lg-5">
                  <input type="text" class="form-control" id="lastName"
                         name="lastName"
                         placeholder="Last name">
                </div>
              </div>
            </div>

            <div class="form-group">
              <input type="text" class="form-control"
                     id="username" placeholder="Choose your username"
                     name="username">
            </div>

            <div class="form-group">
              <input type="text" class="form-control" id="email" name="email"
                     placeholder="Your email">
            </div>

            <div class="form-group">
              <input type="password" class="form-control"
                     id="password" placeholder="Password" name="password">
            </div>

            <div class="form-group">
              <label class="radio-inline">
                <input type="radio" name="role" id="STUDENT_ROLE"
                       value="STUDENT_ROLE" checked="checked"> Learner
              </label>
              <label class="radio-inline">
                <input type="radio" name="role" id="TEACHER_ROLE"
                       value="TEACHER_ROLE"> Teacher
              </label>

            </div>

            <p class="help-block">By clicking Sign up, you agree to our <a
                    href="terms.html" target="_blank">Terms</a>.</p>
            <button type="submit"
                    class="btn btn-primary">Sign up &raquo;</button>
          </fieldset>
        </g:form>

      </div>
    </div>

    <hr>

    <footer>
      <p>&copy; Tsaap Development Group 2013</p>
    </footer>
  </div>

</div> <!-- /container -->

</body>
</html>