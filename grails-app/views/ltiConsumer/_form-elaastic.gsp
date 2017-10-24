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

<%@ page import="org.tsaap.lti.LtiConsumer" %>

<div class="field ${hasErrors(bean: ltiConsumerInstance, field: 'id', 'error')} ">
  <label for="id">
    <g:message code="ltiConsumer.consumerKey.label" default="Consumer Key"/>
  </label>
  <g:textField name="id" maxlength="255" value="${ltiConsumerInstance?.id}" class="form-control"/>
</div>

<div class="field ${hasErrors(bean: ltiConsumerInstance, field: 'consumerName', 'error')} ">
  <label for="consumerName">
    <g:message code="ltiConsumer.consumerName.label" default="Consumer Name"/>
  </label>
  <g:textField name="consumerName" maxlength="45" value="${ltiConsumerInstance?.consumerName}" class="form-control"/>
</div>

<div class="field ${hasErrors(bean: ltiConsumerInstance, field: 'secret', 'error')} ">
  <label for="secret">
    <g:message code="ltiConsumer.secret.label" default="Secret"/>
  </label>
  <g:textField name="secret" maxlength="32" value="${ltiConsumerInstance?.secret}" class="form-control"/>
</div>

<div class="field ${hasErrors(bean: ltiConsumerInstance, field: 'ltiVersion', 'error')} ">
  <label for="ltiVersion">
    <g:message code="ltiConsumer.ltiVersion.label" default="Lti Version"/>
  </label>
  <g:textField name="ltiVersion" value="${ltiConsumerInstance?.ltiVersion}" class="form-control"/>
</div>

<div class="field ${hasErrors(bean: ltiConsumerInstance, field: 'productName', 'error')} ">
  <label for="productName">
    <g:message code="ltiConsumer.productName.label" default="Product Name"/>
  </label>
  <g:textField name="productName" value="${ltiConsumerInstance?.productName}" class="form-control"/>
</div>

<div class="field ${hasErrors(bean: ltiConsumerInstance, field: 'productVersion', 'error')} ">
  <label for="productVersion">
    <g:message code="ltiConsumer.productVersion.label" default="Product Version"/>
  </label>
  <g:textField name="productVersion" value="${ltiConsumerInstance?.productVersion}" class="form-control"/>
</div>

<div class="field ${hasErrors(bean: ltiConsumerInstance, field: 'productGuid', 'error')} ">
  <label for="productGuid">
    <g:message code="ltiConsumer.productGuid.label" default="Product Guid"/>
  </label>
  <g:textField name="productGuid" value="${ltiConsumerInstance?.productGuid}" class="form-control"/>
</div>

<div class="field ${hasErrors(bean: ltiConsumerInstance, field: 'cssPath', 'error')} ">
  <label for="cssPath">
    <g:message code="ltiConsumer.cssPath.label" default="Css Path"/>
  </label>
  <g:textField name="cssPath" value="${ltiConsumerInstance?.cssPath}" class="form-control"/>
</div>

<div class="field ${hasErrors(bean: ltiConsumerInstance, field: 'isProtected', 'error')} required">
  <label for="isProtected">
    <g:message code="ltiConsumer.isProtected.label" default="Is Protected"/>
  </label>
  <g:field name="isProtected"
           type="number"
           value="${ltiConsumerInstance.isProtected}"
           required=""
           class="form-control"/>
</div>

<div class="field ${hasErrors(bean: ltiConsumerInstance, field: 'isEnabled', 'error')} required">
  <label for="isEnabled">
    <g:message code="ltiConsumer.isEnabled.label" default="Is Enabled"/>
  </label>
  <g:field name="isEnabled" type="number" value="${ltiConsumerInstance.isEnabled}" required="" class="form-control"/>
</div>

<div class="inline field ${hasErrors(bean: ltiConsumerInstance, field: 'enableFrom', 'error')} ">
  <label for="enableFrom">
    <g:message code="ltiConsumer.enableFrom.label" default="Enable From"/>
  </label>
  <g:datePicker name="enableFrom"
                precision="day"
                value="${ltiConsumerInstance?.enableFrom}" default="none"
                noSelection="['': '']"/>
</div>

<div class="inline field ${hasErrors(bean: ltiConsumerInstance, field: 'enableUntil', 'error')} ">
  <label for="enableUntil">
    <g:message code="ltiConsumer.enableUntil.label" default="Enable Until"/>
  </label>
  <g:datePicker name="enableUntil" precision="day" value="${ltiConsumerInstance?.enableUntil}" default="none"
                noSelection="['': '']" class="form-control"/>
</div>

<div class="inline field ${hasErrors(bean: ltiConsumerInstance, field: 'lastAccess', 'error')} required">
  <label for="lastAccess">
    <g:message code="ltiConsumer.lastAccess.label" default="Last Access"/>
  </label>
  <g:datePicker name="lastAccess" precision="day" value="${ltiConsumerInstance?.lastAccess}" class="form-control"/>
</div>

