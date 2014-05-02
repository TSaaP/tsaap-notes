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
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta name="description"
        content="Tsaap Notes - microblogging for learners and teachers">
  <meta name="author" content="Tsaap Development Group">
  <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon">
  <title><g:layoutTitle default="TsaaP-Notes"/></title>
  <r:layoutResources/>
</head>

<body>
<g:layoutBody/>
<div class="navbar navbar-inverse navbar-fixed-top">
  <div class="container">
    <button type="button" class="navbar-toggle" data-toggle="collapse"
            data-target=".nav-collapse">
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
    </button>
    <g:link class="navbar-brand" uri="/">TsaaP-Notes</g:link>

    <div class="nav-collapse collapse">
      <ul class="nav navbar-nav">
        <li><a href="https://github.com/TSaaP/tsaap-notes" target="_blank">On Github</a></li>
        <li><a href="https://github.com/TSaaP/tsaap-notes/issues?state=open" target="_blank">Bug tracker</a></li>
        <li><a href="mailto:franck.silvestre@irit.fr">Contact</a></li>
        %{--<li class="dropdown">--}%
          %{--<a href="#" class="dropdown-toggle" data-toggle="dropdown">Dropdown <b--}%
                  %{--class="caret"></b></a>--}%
          %{--<ul class="dropdown-menu">--}%
            %{--<li><a href="#">Action</a></li>--}%
            %{--<li><a href="#">Another action</a></li>--}%
            %{--<li><a href="#">Something else here</a></li>--}%
            %{--<li class="divider"></li>--}%
            %{--<li class="nav-header">Nav header</li>--}%
            %{--<li><a href="#">Separated link</a></li>--}%
            %{--<li><a href="#">One more separated link</a></li>--}%
          %{--</ul>--}%
        %{--</li>--}%
      </ul>

      <div class="nav-collapse collapse pull-right">
        <ul class="nav navbar-nav">
          <tsaap:ifNotLoggedIn>
          <li><g:link controller="login" action="auth">Already an account ? Sign in &raquo;</g:link></li>
          </tsaap:ifNotLoggedIn>
          <tsaap:ifLoggedIn>
            <li><g:link controller="notes" params="[displaysMyNotes:'on']">Welcome back @<sec:username/> !</g:link></li>
          </tsaap:ifLoggedIn>
        </ul>
      </div>
    </div><!--/.nav-collapse -->
  </div>
</div>
<r:layoutResources/>

<footer class="container">
    <p>&copy; Tsaap Development Group 2013 - Tsaap-Notes version <g:meta name="app.version"/> - <a href="${grailsApplication.config.grails.serverURL}/terms.html">Mentions</a></p>
</footer>
</body>
</html>