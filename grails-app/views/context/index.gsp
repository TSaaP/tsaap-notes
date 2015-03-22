<%@ page import="org.tsaap.notes.FilterReservedValue; org.tsaap.notes.Context" %>
<!DOCTYPE html>
<html>
<head>
  <meta name="layout" content="main">
  <r:require modules="tsaap_ui_notes,tsaap_icons"/>
  <g:set var="entityName"
         value="${message(code: 'context.label', default: 'Scope')}"/>
  <title>Tsaap Notes - <g:message code="default.list.label"
                                  args="[entityName]"/></title>
</head>

<body>
<div class="container">
  <ol class="breadcrumb">
    <li class="active"><g:message code="default.list.label"
                                  args="[entityName]"/></li>
  </ol>
  <g:link class="btn btn-primary btn-sm pull-right" action="create"><span
          class="glyphicon glyphicon-plus"></span> Add scope</g:link>
  <g:form controller="context" method="get" role="form">
    <div class="row">
      <div class="col-lg-6">
        <div class="input-group">
          <input type="text" class="form-control" placeholder="containing"
                 name="filter" value="${!(params.filter in org.tsaap.notes.FilterReservedValue.values()*.name()) ? params.filter : ''}">
          <span class="input-group-btn">
            <button class="btn btn-default" type="submit">Filter</button>
          </span>
        </div><!-- /input-group -->
      </div><!-- /.col-lg-6 -->
    </div><!-- /.row -->

  </g:form>
    <small>Scope count: ${contextCount}</small>
</div>
<div class="container">
    <div class="list-group container pull-left" style="margin-top: 40px;">
        <g:link class="list-group-item ${params.filter == org.tsaap.notes.FilterReservedValue.__MINE__.name() ? 'active' : ''}" controller="context" action="index" params="[filter:FilterReservedValue.__MINE__.name()]">
            My scopes
        </g:link>
        <g:link class="list-group-item ${params.filter == org.tsaap.notes.FilterReservedValue.__FOLLOWED__.name() ? 'active' : ''}" controller="context" action="index" params="[filter:FilterReservedValue.__FOLLOWED__.name()]">
            Followed scopes
        </g:link>
        <g:link class="list-group-item ${(!params.filter || params.filter == org.tsaap.notes.FilterReservedValue.__ALL__.name()) ? 'active' : ''}" controller="context" action="index">All</g:link>
    </div>
<div id="list-context" class="container pull-right" style="width: 540px">
  <g:if test="${flash.message}">
    <div class="alert alert-info" role="status">${flash.message}</div>
  </g:if>

  <table class="table table-striped table-hover">
    <thead>
    <tr>

      <th>&nbsp;</th>


      <th>&nbsp;</th>
      <th>&nbsp;</th>

    </tr>
    </thead>
    <tbody>
    <g:each in="${contextList}" status="i" var="context">
      <tr>

        <td><strong><g:link action="show"
                    id="${context.id}">${fieldValue(bean: context, field: "contextName")}</g:link></strong> <small>@${context.owner}</small>
          <g:if test="${context.url}">
          <br/>
          <small>${fieldValue(bean: context, field: "url")}&nbsp<a
                href="${context?.url}"><span
                  class="glyphicon glyphicon-link"></span></a></small>
          </g:if>
          <p>${fieldValue(bean: context, field: "descriptionAsNote")}</p>

        </td>


        %{--<td>${fieldValue(bean: context, field: "descriptionAsNote")}</td>--}%

        <g:if test="${context.owner != user}">
          <g:if test="${context.isFollowedByUser(user)}">
          <td><g:link controller="context" action="unfollowContext" id="${context.id}" style="width: 90px;"
                      class="btn btn-info btn-xs" onmouseover='updateFollowLink($(this),"Unfollow","btn-danger")'
                      onmouseout='updateFollowLink($(this),"Following","btn-info")' params="${[filter:params.filter?:'']}">Following</g:link></td>
          </g:if>
          <g:else>
            <td>
            <g:link controller="context" action="followContext" style="width: 90px;"
                                  class="btn btn-default btn-xs" id="${context.id}" params="${[filter:params.filter?:'']}">Follow</g:link>
            </td>
          </g:else>
        </g:if>
        <g:else>
          <td>
              <g:form action="duplicateContext" controller="context" id="${context.id}"><button class="btn btn-default btn-xs" type="submit">Duplicate</button></g:form>
          </td>
          <td>
              <g:link action="statistics" controller="context" id="${context.id}" class="btn btn-default btn-xs">Generate stats</g:link>
          </td>
        </g:else>
        <td><g:link controller="notes"
                           params="[displaysAll: 'on', contextName: context.contextName, contextId: context.id]" class="btn btn-default btn-xs">Les notes</g:link></td>
      </tr>
    </g:each>
    </tbody>
  </table>
  <div class="note-list-pagination">
      <tsaap:paginate class="pull-right" prev="&laquo;" next="&raquo;" total="${contextCount ?: 0}" params="${[filter:params.filter?:'']}"/>
  </div>
</div>

</div>


<r:script>
  $(".nav li").removeClass('active');
  $("#mainLinkContexts").addClass('active');

  function updateFollowLink(followLink, text, classBtn) {
    $(followLink).text(text);
    $(followLink).removeClass();
    $(followLink).addClass("btn btn-xs "+classBtn);
  }
</r:script>
</body>
</html>
