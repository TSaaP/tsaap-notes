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
<head>
  <meta name="layout" content="main" />
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

      <form class="navbar-form form-inline pull-right">
        <input type="text" placeholder="Email">
        <input type="password" placeholder="Password">
        <button type="submit" class="btn">Sign in</button>
      </form>
    </div><!--/.nav-collapse -->
  </div>
</div>

<div class="container">

  <!-- Main jumbotron for a primary marketing message or call to action -->
  <div class="jumbotron">
    <h1>TsaaP-Notes</h1>

    <p>Microblogging tool dedicated to learners and teachers. TsaaP-notes is born in the french research laboratory <a href="http://www.irit.fr">IRIT</a> to help designing intelligent functions in software of the technology-enhanced learning domain.</p>

    <p><a class="btn btn-primary btn-large">Learn more &raquo;</a></p>
  </div>

  <div class="body-content">

    <!-- Example row of columns -->
    <div class="row">
      <div class="col-lg-4">
        <h2>Take your notes the collaborative way</h2>

        <p>Use TsaaP-Notes to take your notes on your current course, on your current work. Take a look at your friend notes and pick up your favorite to enrich your own notes.</p>

        <p><a class="btn btn-default" href="#">View details &raquo;</a></p>
      </div>

      <div class="col-lg-4">
        <h2>Discuss and exchange ideas</h2>

        <p>Use TsaaP-Notes to discuss on a particular topic of your last course, ask questions and listen to answers coming from peer learners or teachers.</p>

        <p><a class="btn btn-default" href="#">View details &raquo;</a></p>
      </div>

      <div class="col-lg-4">
        <h2>Take advantage of the tagging system</h2>

        <p>TsaaP-Notes comes out of the box with classic tagging features. In addition TsaaP-Notes allows a user to "pre-tag" a thread corresponding to a particular topic. Other users can follow and participate to the pre-tagged thread in an easy way.</p>

        <p><a class="btn btn-default" href="#">View details &raquo;</a></p>
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