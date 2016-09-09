<%@ page import="org.springframework.web.servlet.support.RequestContextUtils; org.tsaap.assignments.Schedule" %>

<div class="form-group fieldcontain ${hasErrors(bean: scheduleInstance, field: 'startDate', 'error')} ">
    <label for="startDate">
        <g:message code="schedule.startdate.label" default="Start Date"/>
        <span class="required-indicator">*</span>
    </label>
    <div class='input-group date' id='datetimepicker1'>
        <input type='text' required="" class="form-control" id="startDate" name="startDate" value="${scheduleInstance?.startDate?.format(message(code:'date.startDate.format'))}"/>
        <span class="input-group-addon">
            <span class="glyphicon glyphicon-calendar"></span>
        </span>
    </div>
</div>

<div class="form-group fieldcontain ${hasErrors(bean: scheduleInstance, field: 'endDate', 'error')} ">
    <label for="endDate">
        <g:message code="schedule.enddate.label" default="End Date"/>

    </label>

    <div class='input-group date' id='datetimepicker2'>
        <input type='text' class="form-control" id="endDate" name="endDate" value="${scheduleInstance?.endDate?.format(message(code:'date.endDate.format'))}"/>
        <span class="input-group-addon">
            <span class="glyphicon glyphicon-calendar"></span>
        </span>
    </div>
</div>

<r:script>
		$('#datetimepicker1').datetimepicker({
            locale: "${RequestContextUtils.getLocale(request).language}",
            defaultDate: getDateFromElement($('#startDate'),"${message(code:'date.startDate.format')}")
        });
        $('#datetimepicker2').datetimepicker({
            locale: "${RequestContextUtils.getLocale(request).language}",
            defaultDate: getDateFromElement($('#endDate'),"${message(code:'date.endDate.format')}")
        });
</r:script>
