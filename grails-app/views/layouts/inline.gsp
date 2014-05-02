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
    <script type="text/javascript"
            src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=TeX-AMS-MML_HTMLorMML">
    </script>
</head>

<body style="padding-top: 10px;">
<div class="container">
  @<sec:username/> - <g:link controller="notes" params="[displaysMyNotes:'on']" target="_blank">Tsaap-Notes</g:link>
</div>

<g:layoutBody/>

<r:layoutResources/>
<footer class="container">
    <p>&copy; Tsaap Development Group 2013 - Tsaap-Notes version <g:meta name="app.version"/> - <a href="${grailsApplication.config.grails.serverURL}/terms.html">Mentions</a></p>
</footer>
</body>
</html>