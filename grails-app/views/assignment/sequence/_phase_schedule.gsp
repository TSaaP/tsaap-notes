<%@ page import="org.springframework.web.servlet.support.RequestContextUtils; org.tsaap.assignments.Schedule" %>
<div class="form-inline">
    <div class="form-group fieldcontain ${hasErrors(bean: scheduleInstance, field: 'startDate', 'error')} ">
        <label for="startDate" class="small">
            <g:message code="schedule.startdate.label" default="Start Date"/>
            <span class="required-indicator">*</span>
        </label>

        <div class='input-group date' id="startdatetimepicker${indexSchedule}">
            <input type='text' class="form-control" id="startDate" name="startDate"
                   value="${scheduleInstance?.startDate?.format(message(code: 'date.startDate.format'))}"/>
            <span class="input-group-addon">
                <span class="glyphicon glyphicon-calendar"></span>
            </span>
        </div>
    </div>

    <div class="form-group fieldcontain ${hasErrors(bean: scheduleInstance, field: 'endDate', 'error')} ">
        <label for="endDate" class="small">
            <g:message code="schedule.enddate.label" default="End Date"/>
            <span class="required-indicator">*</span>
        </label>

        <div class='input-group date' id="enddatetimepicker${indexSchedule}">
            <input type='text' class="form-control" id="endDate" name="endDate"
                   value="${scheduleInstance?.endDate?.format(message(code: 'date.endDate.format'))}"/>
            <span class="input-group-addon">
                <span class="glyphicon glyphicon-calendar"></span>
            </span>
        </div>
    </div>
</div>
<r:script>
		$('#startdatetimepicker${indexSchedule}').datetimepicker({
            locale: "${RequestContextUtils.getLocale(request).language}",
            defaultDate: getDateFromElement($('#startDate'),"${message(code: 'date.startDate.format')}")
        });
        $('#enddatetimepicker${indexSchedule}').datetimepicker({
            locale: "${RequestContextUtils.getLocale(request).language}",
            defaultDate: getDateFromElement($('#endDate'),"${message(code: 'date.endDate.format')}")
        });
</r:script>
