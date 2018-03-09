%{--
  -
  -  Copyright (C) 2017 Ticetime
  -
  -      This program is free software: you can redistribute it and/or modify
  -      it under the terms of the GNU Affero General Public License as published by
  -      the Free Software Foundation, either version 3 of the License, or
  -      (at your option) any later version.
  -
  -      This program is distributed in the hope that it will be useful,
  -      but WITHOUT ANY WARRANTY; without even the implied warranty of
  -      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  -      GNU Affero General Public License for more details.
  -
  -      You should have received a copy of the GNU Affero General Public License
  -      along with this program.  If not, see <http://www.gnu.org/licenses/>.
  -
  --}%


<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="left_menu-elaastic">
  <r:require modules="semantic_ui,jquery,elaastic_ui"/>
</head>

<body>

<h2 class="ui header">
  <i class="users icon"></i>
  <div class="content">
    ${message(code: "useraccount.batchcreation.title")}
  </div>
</h2>

<g:if test="${flash.message}">
  <div class="ui info message" role="status"><i class="close icon"></i> ${raw(flash.message)}</div>
</g:if>


<div class="ui top attached message">
  ${message(code: "useraccount.batchcreation.indication")}
</div>

<div class="ui attached segment"><g:img file="exampleCSV.png"/></div>

<div class="ui bottom attached segment">
  <g:form controller="userAccountBatchCreation"
          class="ui form"
          action="doProcessCSVFile"
          method="POST"
          enctype="multipart/form-data">

    <div class="ui field">
      <input type="file" name="csvFile" id="csvFile"/>
    </div>
    <button type="submit"
            class="ui primary button">${message(code: "useraccount.uploadBatchFile.button")}</button><br/>

  </g:form>
</div>

<r:script>
  $(document)
      .ready(function () {

    $('.message .close')
        .on('click', function () {
      $(this)
          .closest('.message')
          .transition('fade')
      ;
    });

  });

</r:script>

</body>
</html>