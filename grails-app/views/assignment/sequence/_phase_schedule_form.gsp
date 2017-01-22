<%@ page import="org.springframework.web.servlet.support.RequestContextUtils; org.tsaap.assignments.Schedule" %>
<div class="form-inline">
    <div class="form-group fieldcontain ${hasErrors(bean: scheduleInstance, field: 'startDate', 'error')} ">
        <label for="startDate${indexSchedule}" class="small">
            <g:message code="schedule.startdate.label" default="Start Date"/>
            <span class="required-indicator">*</span>
        </label>
        <g:set var="defaultStartDate" value="${scheduleInstance?.startDate?.format(message(code: 'date.startDate.format')) ?: defaultDate?.format(message(code: 'date.startDate.format'))}"/>
        <div class='input-group date' id="startdatetimepicker${indexSchedule}">
            <input type='text' class="form-control" id="startDate${indexSchedule}" name="startDate${indexSchedule}"
                   value="${defaultStartDate}"/>
            <span class="input-group-addon">
                <span class="glyphicon glyphicon-calendar"></span>
            </span>
        </div>
    </div>

    <div class="form-group fieldcontain ${hasErrors(bean: scheduleInstance, field: 'endDate', 'error')} ">
        <label for="endDate${indexSchedule}" class="small">
            <g:message code="schedule.enddate.label" default="End Date"/>
        </label>

        <div class='input-group date' id="enddatetimepicker${indexSchedule}">
            <input type='text' class="form-control" id="endDate${indexSchedule}" name="endDate${indexSchedule}"
                   value="${scheduleInstance?.endDate?.format(message(code: 'date.endDate.format'))}"/>
            <span class="input-group-addon">
                <span class="glyphicon glyphicon-calendar"></span>
            </span>
        </div>
    </div>
</div>
<r:script>
		$('#startdatetimepicker${indexSchedule}').datetimepicker({
            //locale: "${RequestContextUtils.getLocale(request).language}",
            locale: "fr", // dirty work around
            defaultDate: getDateFromElement($("#startDate${indexSchedule}"),"${message(code: 'date.startDate.format')}")
        });
        $('#enddatetimepicker${indexSchedule}').datetimepicker({
            //locale: "${RequestContextUtils.getLocale(request).language}",
            locale: "fr", // dirty work around
            defaultDate: getDateFromElement($('#endDate${indexSchedule}'),"${message(code: 'date.endDate.format')}")
        });
</r:script>
