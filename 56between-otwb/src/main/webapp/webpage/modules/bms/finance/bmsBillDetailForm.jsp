<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>费用明细管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="bmsBillDetailForm.js" %>
</head>
<body>
<form:form id="inputForm" modelAttribute="bmsBillDetailEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="status"/>
    <form:hidden path="settleMethod"/>
    <form:hidden path="settleCategory"/>
    <form:hidden path="billModule"/>
    <form:hidden path="billCategory"/>
    <form:hidden path="warehouseCode"/>
    <form:hidden path="warehouseName"/>
    <form:hidden path="billTermsCode"/>
    <form:hidden path="billTermsDesc"/>
    <form:hidden path="billStandard"/>
    <form:hidden path="billQty"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-15"><label class="pull-right asterisk">结算对象编码</label></td>
                <td class="width-35">
                    <sys:grid title="选择结算对象" url="${ctx}/bms/basic/bmsSettleObject/grid" cssClass="form-control required"
                              fieldId="" fieldName="" fieldKeyName=""
                              displayFieldId="settleObjectCode" displayFieldName="settleObjectCode"
                              displayFieldKeyName="settleObjectCode" displayFieldValue="${bmsBillDetailEntity.settleObjectCode}"
                              fieldLabels="结算对象编码|结算对象名称" fieldKeys="settleObjectCode|settleObjectName"
                              searchLabels="结算对象编码|结算对象名称" searchKeys="settleObjectCode|settleObjectName"
                              queryParams="orgId" queryParamValues="orgId"
                              afterSelect="settleObjectAfterSelect"/>
                </td>
                <td class="width-15"><label class="pull-right">结算对象名称</label></td>
                <td class="width-35">
                    <form:input path="settleObjectName" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td class="width-15"><label class="pull-right asterisk">费用科目</label></td>
                <td class="width-35">
                    <sys:grid title="选择费用科目" url="${ctx}/bms/basic/bmsBillSubject/grid" cssClass="form-control required"
                              fieldId="billSubjectCode" fieldName="billSubjectCode"
                              fieldKeyName="billSubjectCode" fieldValue="${bmsBillDetailEntity.billSubjectCode}"
                              displayFieldId="billSubjectName" displayFieldName="billSubjectName"
                              displayFieldKeyName="billSubjectName" displayFieldValue="${bmsBillDetailEntity.billSubjectName}"
                              fieldLabels="费用科目代码|费用科目名称" fieldKeys="billSubjectCode|billSubjectName"
                              searchLabels="费用科目代码|费用科目名称" searchKeys="billSubjectCode|billSubjectName"
                              queryParams="orgId" queryParamValues="orgId"
                              afterSelect="billSubjectAfterSelect"/>
                </td>
                <td class="width-15"><label class="pull-right asterisk">应收应付</label></td>
                <td class="width-35">
                    <form:select path="receivablePayable" class="form-control required">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('BMS_RECEIVABLE_PAYABLE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td class="width-15"><label class="pull-right asterisk">业务日期</label></td>
                <td class="width-35">
                    <div class='input-group form_datetime' id='businessDate'>
                        <input type='text' name="businessDate" class="form-control"
                               value="<fmt:formatDate value="${bmsBillDetailEntity.businessDate}" pattern="yyyy-MM-dd"/>"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </td>
                <td class="width-15"><label class="pull-right">合同</label></td>
                <td class="width-35">
                    <sys:grid title="选择合同" url="${ctx}/bms/basic/bmsContract/grid" cssClass="form-control"
                              fieldId="" fieldName="" fieldKeyName=""
                              displayFieldId="sysContractNo" displayFieldName="sysContractNo"
                              displayFieldKeyName="sysContractNo" displayFieldValue="${bmsBillDetailEntity.sysContractNo}"
                              fieldLabels="系统合同编号|客户合同编号" fieldKeys="sysContractNo|contractNo"
                              searchLabels="系统合同编号|客户合同编号" searchKeys="sysContractNo|contractNo"
                              queryParams="orgId" queryParamValues="orgId"/>
                </td>
            </tr>
            <tr>
                <td class="width-15"><label class="pull-right">系统订单号</label></td>
                <td class="width-35">
                    <form:input path="sysOrderNo" htmlEscape="false" class="form-control"/>
                </td>
                <td class="width-15"><label class="pull-right">客户订单号</label></td>
                <td class="width-35">
                    <form:input path="customerOrderNo" htmlEscape="false" class="form-control"/>
                </td>
            </tr>
            <tr>
                <td class="width-15"><label class="pull-right">发生量</label></td>
                <td class="width-35">
                    <form:input path="occurrenceQty" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 8, 0)"/>
                </td>
                <td class="width-15"><label class="pull-right asterisk">费用</label></td>
                <td class="width-35">
                    <form:input path="cost" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 8)"/>
                </td>
            </tr>
            <tr>
                <td class="width-15"><label class="pull-right">备注</label></td>
                <td colspan="3">
                    <form:input path="remarks" htmlEscape="false" class="form-control"/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</form:form>
</body>
</html>