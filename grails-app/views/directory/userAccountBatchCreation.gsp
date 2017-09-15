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


<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <r:require modules="tsaap_ui_notes,tsaap_icons"/>
</head>

<body>
<div class="container context-nav">

    <ol class="breadcrumb">
        <li class="active">${message(code: "useraccount.batchcreation.title")}</li>
    </ol>

    <div class="body-content">
        <g:if test="${flash.message}">
            <div class="alert alert-info" role="status">${message(code:flash.message)}</div>
        </g:if>

        <p>
            ${message(code: "useraccount.batchcreation.indication")}
        </p>
        <p><g:img file="exampleCSV.png"/></p>

        <!-- Example row of columns -->
        <div class="row">

            <div class="col-lg-6">
                <g:form controller="userAccountBatchCreation" action="doProcessCSVFile" method="POST" enctype="multipart/form-data">
                    <fieldset>
                        <div class="form-group">
                            <input type="file" name="csvFile" id="csvFile"/>
                        </div>
                        <button type="submit"
                                class="btn btn-primary">${message(code: "useraccount.uploadBatchFile.button")}</button><br/>

                    </fieldset>
                </g:form>

            </div>

        </div>

    </div>
    <hr>
</div> <!-- /container -->
<script>
    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    })
</script>

</body>
</html>