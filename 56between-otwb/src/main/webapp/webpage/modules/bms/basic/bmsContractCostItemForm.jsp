<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>合同费用明细管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsContractCostItemForm.js" %>
</head>
<body>
<form:form id="inputForm" modelAttribute="bmsContractCostItemEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="sysContractNo"/>
    <form:hidden path="settleObjectCode"/>
    <form:hidden path="transportGroupCode"/>
    <form:hidden path="orgId"/>.
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-10"><label class="pull-right asterisk">费用模块</label></td>
                <td class="width-15">
                    <form:select path="billModule" class="form-control required" onchange="billModuleChange()">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('BMS_BILL_MODULE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right asterisk">费用科目代码</label></td>
                <td class="width-15">
                    <sys:grid title="选择费用科目" url="${ctx}/bms/basic/bmsBillSubject/grid" cssClass="form-control required"
                              fieldId="" fieldName="" fieldKeyName=""
                              displayFieldId="billSubjectCode" displayFieldName="billSubjectCode"
                              displayFieldKeyName="billSubjectCode" displayFieldValue="${bmsContractCostItemEntity.billSubjectCode}"
                              fieldLabels="费用科目代码|费用科目名称" fieldKeys="billSubjectCode|billSubjectName"
                              searchLabels="费用科目代码|费用科目名称" searchKeys="billSubjectCode|billSubjectName"
                              queryParams="billModule|orgId" queryParamValues="billModule|orgId"
                              afterSelect="billSubjectAfterSelect"/>
                </td>
                <td class="width-10"><label class="pull-right">费用科目名称</label></td>
                <td class="width-15">
                    <form:input path="billSubjectName" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right asterisk">应收应付</label></td>
                <td class="width-15">
                    <form:select path="receivablePayable" class="form-control required">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('BMS_RECEIVABLE_PAYABLE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right asterisk">计费条款</label></td>
                <td class="width-15">
                    <sys:grid title="选择费用科目" url="${ctx}/bms/basic/bmsBillTerms/grid" cssClass="form-control required"
                              fieldId="billTermsCode" fieldName="billTermsCode"
                              fieldKeyName="billTermsCode" fieldValue="${bmsContractCostItemEntity.billTermsCode}"
                              displayFieldId="billTermsDesc" displayFieldName="billTermsDesc"
                              displayFieldKeyName="billTermsDesc" displayFieldValue="${bmsContractCostItemEntity.billTermsDesc}"
                              fieldLabels="计费条款代码|计费条款说明" fieldKeys="billTermsCode|billTermsDesc"
                              searchLabels="计费条款代码|计费条款说明" searchKeys="billTermsCode|billTermsDesc"
                              queryParams="billModule" queryParamValues="billModule"
                              afterSelect="billTermsAfterSelect"/>
                </td>
                <td class="width-10"><label class="pull-right asterisk">计费公式</label></td>
                <td class="width-15">
                    <sys:grid title="选择计费公式" url="${ctx}/bms/basic/bmsBillFormula/grid" cssClass="form-control required"
                              fieldId="formulaCode" fieldKeyName="formulaCode"
                              fieldName="formulaCode" fieldValue="${bmsContractCostItemEntity.formulaCode}"
                              displayFieldId="formulaName" displayFieldKeyName="formulaName"
                              displayFieldName="formulaName" displayFieldValue="${bmsContractCostItemEntity.formulaName}"
                              fieldLabels="公式编码|公式名称" fieldKeys="formulaCode|formulaName"
                              searchLabels="公式编码|公式名称" searchKeys="formulaCode|formulaName"/>
                </td>
                <td class="width-10"><label class="pull-right">最小金额</label></td>
                <td class="width-15">
                    <form:input path="minAmount" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 8, 0)"/>
                </td>
                <td class="width-10"><label class="pull-right">最大金额</label></td>
                <td class="width-15">
                    <form:input path="maxAmount" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 8, 0)"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">税率</label></td>
                <td class="width-15">
                    <form:input path="taxRate" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                </td>
                <td class="width-10"><label class="pull-right">合同系数</label></td>
                <td class="width-15">
                    <form:input path="coefficient" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="tabs-container">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#include-params" aria-expanded="true">条款参数配置(包含)</a></li>
            <li class=""><a data-toggle="tab" href="#exclude-params" aria-expanded="true">条款参数配置(排除)</a></li>
        </ul>
        <div class="tab-content">
            <div id="include-params" class="tab-pane fade in active">
                <table id="includeParams" class="table text-nowrap"></table>
            </div>
            <div id="exclude-params" class="tab-pane fade">
                <table id="excludeParams" class="table text-nowrap"></table>
            </div>
            <div id="detail-info" class="tab-pane fade">
                <table id="detailInfo" class="table text-nowrap"></table>
            </div>
        </div>
    </div>
</form:form>
</body>
</html>