<%@ page import="org.springframework.web.servlet.support.RequestContextUtils; org.tsaap.assignments.Schedule" %>

<div class="form-group fieldcontain ${hasErrors(bean: scheduleInstance, field: 'startDate', 'error')} ">
    <label for="startDate">
        <g:message code="schedule.startdate.label" default="Start Date"/>
        <span class="required-indicator">*</span>
    </label>
    <div class='input-group date' id='datetimepicker1'>
        <input type='text' required="" class="form-control" id="startDate" name="startDate" value="${scheduleInstance?.startDate}"/>
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
        <input type='text' class="form-control" id="endDate" name="endDate" value="${scheduleInstance?.endDate}"/>
        <span class="input-group-addon">
            <span class="glyphicon glyphicon-calendar"></span>
        </span>
    </div>
</div>

<r:script>
	$(function () {
	    var now = moment()
		$('#datetimepicker1').datetimepicker({
            locale: "${RequestContextUtils.getLocale(request).language}",
            defaultDate: now
        });
        $('#datetimepicker2').datetimepicker({
            locale: "${RequestContextUtils.getLocale(request).language}",
            defaultDate: moment(now).add(1, 'day')
        });
	});
</r:script>

