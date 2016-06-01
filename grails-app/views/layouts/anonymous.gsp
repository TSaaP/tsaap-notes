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
        <li><a href="https://github.com/TSaaP/tsaap-notes" target="_blank">${message(code: "layout.home.github")}</a></li>
        <li><a href="https://github.com/TSaaP/tsaap-notes/issues?state=open" target="_blank">${message(code: "layout.home.bug")}</a></li>
        <li><a href="mailto:franck.silvestre@irit.fr">Contact</a></li>
      </ul>


    </div><!--/.nav-collapse -->
  </div>
</div>
<br>
<g:layoutBody/>
<r:layoutResources/>

<footer class="container">
    <p>&copy; Tsaap Development Group 2013 - Tsaap-Notes version <g:meta name="app.version"/> - <a href="${grailsApplication.config.grails.serverURL}/terms">Mentions</a></p>
</footer>
</body>
</html>