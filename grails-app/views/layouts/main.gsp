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
    <a class="navbar-brand" href="#">TsaaP-Notes</a>

    <div class="nav-collapse collapse">
      %{--<ul class="nav navbar-nav">
        <li class="active"><a href="#" title="an action">an action</a></li>
      </ul>--}%

      <div class="nav-collapse collapse pull-right">
        <ul class="nav navbar-nav">
          <sec:ifLoggedIn>
            <li class="dropdown">
                      <a href="#" class="dropdown-toggle" data-toggle="dropdown">${user.fullname}
                        <span class="glyphicon glyphicon-cog"></span> <b class="caret"></b></a>
                      <ul class="dropdown-menu">
                        <li><a href="#">My account</a></li>
                        <li class="divider"></li>
                        <li><g:link controller="logout">disconnect</g:link></li>
                      </ul>
                    </li>
          </sec:ifLoggedIn>
        </ul>
      </div>
    </div><!--/.nav-collapse -->
  </div>
</div>
<r:layoutResources/>

</body>
</html>