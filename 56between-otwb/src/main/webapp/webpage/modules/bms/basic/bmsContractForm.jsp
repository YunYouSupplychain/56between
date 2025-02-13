<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>合同管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsContractForm.js"%>
</head>
<body>
<div class="toolbar" style="margin-left: 10px;">
    <shiro:hasPermission name="bms:bmsContract:edit">
        <a type="button" class="btn btn-primary" onclick="save()">保存</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="bmsContractEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="orgId"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-10"><label class="pull-right">系统合同编号</label></td>
                <td class="width-15">
                    <form:input path="sysContractNo" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right asterisk">结算对象</label></td>
                <td class="width-15">
                    <sys:grid title="选择结算对象" url="${ctx}/bms/basic/bmsSettleObject/grid" cssClass="form-control required"
                              fieldId="" fieldName="" fieldKeyName=""
                              displayFieldId="settleObjectCode" displayFieldName="settleObjectCode"
                              displayFieldKeyName="settleObjectCode" displayFieldValue="${bmsContractEntity.settleObjectCode}"
                              fieldLabels="结算对象代码|结算对象名称" fieldKeys="settleObjectCode|settleObjectName"
                              searchLabels="结算对象代码|结算对象名称" searchKeys="settleObjectCode|settleObjectName"
                              queryParams="orgId" queryParamValues="orgId"
                              afterSelect="settleObjectAfterSelect"/>
                </td>
                <td class="width-10"><label class="pull-right">结算对象名称</label></td>
                <td class="width-15">
                    <form:input path="settleObjectName" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">合同状态</label></td>
                <td class="width-15">
                    <form:select path="contractStatus" class="form-control m-b" disabled="true">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('BMS_CONTRACT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right asterisk">有效开始日期</label></td>
                <td class="width-15">
                    <div class='input-group form_datetime' id='effectiveDateFm'>
                        <input type='text' name="effectiveDateFm" class="form-control required"
                               value="<fmt:formatDate value="${bmsContractEntity.effectiveDateFm}" pattern="yyyy-MM-dd"/>"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </td>
                <td class="width-10"><label class="pull-right asterisk">有效结束日期</label></td>
                <td class="width-15">
                    <div class='input-group form_datetime' id='effectiveDateTo'>
                        <input type='text' name="effectiveDateTo" class="form-control required"
                               value="<fmt:formatDate value="${bmsContractEntity.effectiveDateTo}" pattern="yyyy-MM-dd"/>"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </td>
                <td class="width-10"><label class="pull-right asterisk">客户合同编号</label></td>
                <td class="width-15">
                    <form:input path="contractNo" htmlEscape="false" class="form-control required"/>
                </td>
                <td class="width-10"><label class="pull-right">子合同编号</label></td>
                <td class="width-15">
                    <form:input path="subcontractNo" htmlEscape="false" class="form-control"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">合同类型</label></td>
                <td class="width-15">
                    <form:select path="contractType" class="form-control m-b">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('BMS_CONTRACT_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">合同类别</label></td>
                <td class="width-15">
                    <form:select path="contractCategory" class="form-control m-b">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('BMS_CONTRACT_CATEGORY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">合同归属</label></td>
                <td class="width-15">
                    <form:input path="contractAttribution" htmlEscape="false" class="form-control"/>
                </td>
                <td class="width-10"><label class="pull-right">归属公司</label></td>
                <td class="width-15">
                    <form:input path="belongToCompany" htmlEscape="false" class="form-control"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">签约人</label></td>
                <td class="width-15">
                    <form:input path="contractor" htmlEscape="false" class="form-control"/>
                </td>
                <td class="width-10"><label class="pull-right">客户对账人</label></td>
                <td class="width-15">
                    <form:input path="checkAccountsPerson" htmlEscape="false" class="form-control"/>
                </td>
                <td class="width-10"><label class="pull-right">对账负责人</label></td>
                <td class="width-15">
                    <form:input path="checkAccountsDirector" htmlEscape="false" class="form-control"/>
                </td>
                <td class="width-10"><label class="pull-right">对账时间</label></td>
                <td class="width-15">
                    <div class='input-group form_datetime' id='checkAccountsTime'>
                        <input type='text' name="checkAccountsTime" class="form-control"
                               value="<fmt:formatDate value="${bmsContractEntity.checkAccountsTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">开票要求</label></td>
                <td class="width-15">
                    <form:input path="billingRequirement" htmlEscape="false" class="form-control"/>
                </td>
                <td class="width-10"><label class="pull-right">发票类型</label></td>
                <td class="width-15">
                    <form:select path="invoiceType" class="form-control m-b">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('BMS_INVOICE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">发票税率</label></td>
                <td class="width-15">
                    <form:input path="invoiceTaxRate" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                </td>
                <td class="width-10"><label class="pull-right">机构</label></td>
                <td class="width-15">
                    <form:input path="orgName" class="form-control" htmlEscape="false" disabled="true"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">合同概述</label></td>
                <td class="width-15" colspan="7">
                    <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="255"/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</form:form>
<div class="tabs-container">
    <ul class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">费用项目</a></li>
    </ul>
    <div class="tab-content">
        <div id="tab-1" class="tab-pane fade in active">
            <div class="toolbar" style="margin: 5px 0;">
                <shiro:hasPermission name="bms:bmsContract:add">
                    <button id="addRow" class="btn btn-primary" onclick="addRow()" disabled title="新增">新增</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContract:edit">
                    <button id="editRow" class="btn btn-primary" onclick="editRow()" disabled title="修改">修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContract:del">
                    <button id="delRow" class="btn btn-danger" onclick="delRow()" disabled title="删除">删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContract:servicePrice">
                    <button id="storagePrice" class="btn btn-primary" onclick="storagePriceForm()" disabled title="仓储价格">仓储价格</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContract:transportPrice">
                    <button id="transportPrice" class="btn btn-primary" onclick="transportPriceForm()" disabled title="运输价格">运输价格</button>
                </shiro:hasPermission>
            </div>
            <table id="bmsContractCostItemTable" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>