<%@ taglib prefix="from" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>费用账单管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="bmsBillForm.js" %>
    <style type="text/css">
        .td-label {
            width: 8%;
            height: 50px;
        }

        .td-val {
            width: 12%;
            height: 50px;
        }
    </style>
</head>
<body>
<div style="margin-left: 10px;">
    <shiro:hasPermission name="bms:bmsBill:edit">
        <button id="save" class="btn btn-primary" onclick="save()">保存</button>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="bmsBill" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="status"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-10"><label class="pull-right">账单号</label></td>
                <td class="width-15">
                    <form:input path="confirmNo" htmlEscape="false" class="form-control " readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right asterisk">结算对象</label></td>
                <td class="width-15">
                    <sys:grid title="选择结算对象" url="${ctx}/bms/basic/bmsSettleObject/grid" cssClass="form-control required"
                              fieldId="settleObjCode" fieldName="settleObjCode" fieldKeyName="settleObjectCode" fieldValue="${bmsBill.settleObjCode}"
                              displayFieldId="settleObjName" displayFieldName="settleObjName" displayFieldKeyName="settleObjectName" displayFieldValue="${bmsBill.settleObjName}"
                              fieldLabels="结算对象代码|结算对象名称" fieldKeys="settleObjectCode|settleObjectName"
                              searchLabels="结算对象代码|结算对象名称" searchKeys="settleObjectCode|settleObjectName"
                              queryParams="orgId" queryParamValues="orgId"/>
                </td>
                <td class="width-10"><label class="pull-right">金额</label></td>
                <td class="width-15">
                    <form:input path="amount" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 8, 0)" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">核销金额</label></td>
                <td class="width-15">
                    <form:input path="writeOffAmount" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 8, 0)"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">备注</label></td>
                <td colspan="7">
                    <form:input path="remarks" htmlEscape="false" class="form-control"/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div id="bmsBillTabs" class="tabs-container">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#bmsBillStatistics" data-target-table="#bmsBillStatisticsTable" aria-expanded="true">费用统计</a></li>
            <li class=""><a data-toggle="tab" href="#billDetail" data-target-table="#bmsBillDetailTable" aria-expanded="true">费用明细</a></li>
        </ul>
        <div class="tab-content">
            <div id="bmsBillStatistics" class="tab-pane fade in active">
                <table id="bmsBillStatisticsTable" class="text-nowrap"></table>
            </div>
            <div id="billDetail" class="tab-pane fade">
                <div id="toolbar" style="margin: 5px 0;">
                    <a id="view" class="btn btn-primary" disabled onclick="viewBillDetail()">查看费用</a>
                    <shiro:hasPermission name="bms:bmsBill:addBillDetail">
                        <a id="add" class="btn btn-primary" disabled onclick="addBillDetail()">添加费用</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="bms:bmsBill:removeBillDetail">
                        <a id="remove" class="btn btn-danger" disabled onclick="removeBillDetail()">移除费用</a>
                    </shiro:hasPermission>
                </div>
                <table id="bmsBillDetailTable" class="text-nowrap"></table>
            </div>
        </div>
    </div>
</form:form>
<!-- 添加费用 -->
<div id="addBillDetailModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 80%">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">添加费用</h4>
            </div>
            <div class="modal-body">
                <form id="searchForm" class="form">
                    <input type="hidden" id="add_orgId" name="orgId"/>
                    <input type="hidden" id="add_status" name="status" value="01"/>
                    <input type="hidden" id="add_settleObjectCode" name="settleObjectCode">
                    <table class="table">
                        <tr>
                            <td class="width-10"><label class="pull-right">业务时间从</label></td>
                            <td class="width-15">
                                <div class='input-group date' id='add_billDateFm'>
                                    <input type='text' name="billDateFm" class="form-control"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td class="width-10"><label class="pull-right">业务时间到</label></td>
                            <td class="width-15">
                                <div class='input-group date' id='add_billDateTo'>
                                    <input type='text' name="billDateTo" class="form-control"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td class="width-10"><label class="pull-right">系统合同编号</label></td>
                            <td class="width-15">
                                <input id="add_sysContractNo" name="sysContractNo" maxlength="64" class="form-control"/>
                            </td>
                            <td class="width-10"><label class="pull-right">客户合同编号</label></td>
                            <td class="width-15">
                                <input id="add_contractNo" name="contractNo" maxlength="64" class="form-control"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-10"><label class="pull-right">费用科目</label></td>
                            <td class="width-15">
                                <sys:grid title="选择费用科目" url="${ctx}/bms/basic/bmsBillSubject/grid" cssClass="form-control "
                                          fieldId="add_billSubjectCode" fieldName="billSubjectCode" fieldKeyName="billSubjectCode"
                                          displayFieldId="add_billSubjectName" displayFieldName="billSubjectName" displayFieldKeyName="billSubjectName"
                                          fieldLabels="费用科目代码|费用科目名称" fieldKeys="billSubjectCode|billSubjectName"
                                          searchLabels="费用科目代码|费用科目名称" searchKeys="billSubjectCode|billSubjectName"
                                          queryParams="orgId" queryParamValues="orgId"/>
                            </td>
                            <td class="width-10"><label class="pull-right">费用单号</label></td>
                            <td class="width-15">
                                <input id="add_billNo" name="billNo" maxlength="64" class="form-control"/>
                            </td>
                            <td class="width-10"><label class="pull-right">结算模型编码</label></td>
                            <td class="width-15">
                                <input id="add_settleModelCode" name="settleModelCode" maxlength="64" class="form-control"/>
                            </td>
                            <td class="width-10"><label class="pull-right">系统订单号</label></td>
                            <td class="width-15">
                                <input id="add_sysOrderNo" name="sysOrderNo" maxlength="64" class="form-control"/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="7">
                                <a id="search" class="btn btn-primary">查询</a>
                                <a id="reset" class="btn btn-default">重置</a>
                            </td>
                        </tr>
                    </table>
                </form>
                <table id="addBillDetailTable" class="table text-nowrap"></table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="addBillDetailConfirm()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>