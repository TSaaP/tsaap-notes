<%@ page import="org.springframework.web.servlet.support.RequestContextUtils; org.tsaap.assignments.Schedule" %>
<div class="form-inline">
    <div class="form-group fieldcontain ${hasErrors(bean: scheduleInstance, field: 'startDate', 'error')} ">
        <label for="startDate" class="small">
            <g:message code="schedule.startdate.label" default="Start Date"/>
            <span class="required-indicator">*</span>
        </label>
        <div class='input-group date' id='datetimepicker1'>
            <input type='text' class="form-control" id="startDate" name="startDate" value="${scheduleInstance?.startDate?.format(message(code:'date.startDate.format'))}"/>
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

        <div class='input-group date' id='datetimepicker2'>
            <input type='text' class="form-control" id="endDate" name="endDate" value="${scheduleInstance?.endDate?.format(message(code:'date.endDate.format'))}"/>
            <span class="input-group-addon">
                <span class="glyphicon glyphicon-calendar"></span>
            </span>
        </div>
    </div>
</div>
<r:script>
	$(function () {
	    var defaultStartDate = null
	    var defaultEndDate = null
	    //var defaultStartDate = moment()
	    //var defaultEndDate = moment(defaultStartDate).add(1,'days')
	    var startDate = moment($('#startDate').val(),"${message(code:'date.startDate.format')}") ;
	    if (startDate.isValid()) {
	        defaultStartDate = startDate
	    }
	    var endDate = moment($('#endDate').val(),"${message(code:'date.endDate.format')}") ;
	    if (endDate.isValid()) {
	        defaultEndDate = endDate
	    }
		$('#datetimepicker1').datetimepicker({
            locale: "${RequestContextUtils.getLocale(request).language}",
            defaultDate: defaultStartDate
        });
        $('#datetimepicker2').datetimepicker({
            locale: "${RequestContextUtils.getLocale(request).language}",
            defaultDate: defaultEndDate
        });
	});
</r:script>

