%{--
  - Copyright (C) 2013-2016 Université Toulouse 3 Paul Sabatier
  -
  -     This program is free software: you can redistribute it and/or modify
  -     it under the terms of the GNU Affero General Public License as published by
  -     the Free Software Foundation, either version 3 of the License, or
  -     (at your option) any later version.
  -
  -     This program is distributed in the hope that it will be useful,
  -     but WITHOUT ANY WARRANTY; without even the implied warranty of
  -     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  -     GNU Affero General Public License for more details.
  -
  -     You should have received a copy of the GNU Affero General Public License
  -     along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
        <p class="navbar-brand">TsaaP-Notes</p>
    </div>
</div>
<r:layoutResources/>

<footer class="container">
    <p>&copy; Tsaap Development Group 2013 - Tsaap-Notes version <g:meta name="app.version"/> - <a
            href="${grailsApplication.config.grails.serverURL}/terms">Mentions</a></p>
</footer>
</body>
</html>