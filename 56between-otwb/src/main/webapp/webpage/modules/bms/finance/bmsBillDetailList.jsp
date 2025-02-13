<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>费用明细管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsBillDetailList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="orgId"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading"><h3 class="panel-title">费用明细列表</h3></div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="bmsBillDetailEntity" class="form">
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">业务日期从</label></td>
                                    <td class="width-15">
                                        <div class='input-group form_datetime' id='businessDateFm'>
                                            <input type='text' name="businessDateFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">业务日期到</label></td>
                                    <td class="width-15">
                                        <div class='input-group form_datetime' id='businessDateTo'>
                                            <input type='text' name="businessDateTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">结算对象编码</label></td>
                                    <td class="width-15">
                                        <form:input path="settleObjectCode" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">结算对象名称</label></td>
                                    <td class="width-15">
                                        <form:input path="settleObjectName" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">费用模块</label></td>
                                    <td class="width-15">
                                        <form:select path="billModule" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('BMS_BILL_MODULE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">费用类别</label></td>
                                    <td class="width-15">
                                        <form:select path="billCategory" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('BMS_BILL_SUBJECT_CATEGORY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">费用科目</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择费用科目" url="${ctx}/bms/basic/bmsBillSubject/grid" cssClass="form-control"
                                                  fieldId="billSubjectCode" fieldName="billSubjectCode" fieldKeyName="billSubjectCode"
                                                  displayFieldId="billSubjectName" displayFieldName="billSubjectName" displayFieldKeyName="billSubjectName"
                                                  fieldLabels="费用科目代码|费用科目名称" fieldKeys="billSubjectCode|billSubjectName"
                                                  searchLabels="费用科目代码|费用科目名称" searchKeys="billSubjectCode|billSubjectName"
                                                  queryParams="orgId" queryParamValues="orgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">计费条款</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择计费条款" url="${ctx}/bms/basic/bmsBillTerms/grid" cssClass="form-control"
                                                  fieldId="billTermsCode" fieldName="billTermsCode" fieldKeyName="billTermsCode"
                                                  displayFieldId="billTermsDesc" displayFieldName="billTermsDesc" displayFieldKeyName="billTermsDesc"
                                                  fieldLabels="计费条款代码|计费条款说明" fieldKeys="billTermsCode|billTermsDesc"
                                                  searchLabels="计费条款代码|计费条款说明" searchKeys="billTermsCode|billTermsDesc"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">系统合同编号</label></td>
                                    <td class="width-15">
                                        <form:input path="sysContractNo" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户合同编号</label></td>
                                    <td class="width-15">
                                        <form:input path="contractNo" htmlEscape="false" maxlength="35" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">模型编码</label></td>
                                    <td class="width-15">
                                        <form:input path="settleModelCode" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">应收应付</label></td>
                                    <td class="width-15">
                                        <form:select path="receivablePayable" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('BMS_RECEIVABLE_PAYABLE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">费用单号</label></td>
                                    <td class="width-15">
                                        <form:input path="billNo" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">系统订单号</label></td>
                                    <td class="width-15">
                                        <form:input path="sysOrderNo" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户订单号</label></td>
                                    <td class="width-15">
                                        <form:input path="customerOrderNo" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right" title="仓库：">仓库</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择仓库" url="${ctx}/sys/office/companyData" cssClass="form-control"
                                                  fieldId="warehouseCode" fieldName="warehouseCode" fieldKeyName="code"
                                                  displayFieldId="warehouseName" displayFieldName="warehouseName" displayFieldKeyName="name"
                                                  fieldLabels="仓库编码|仓库名称" fieldKeys="code|name"
                                                  searchLabels="仓库编码|仓库名称" searchKeys="code|name"
                                                  queryParams="id" queryParamValues="orgId"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">供应商编码</label></td>
                                    <td class="width-15">
                                        <form:input path="supplierCode" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">供应商名称</label></td>
                                    <td class="width-15">
                                        <form:input path="supplierName" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">商品编码</label></td>
                                    <td class="width-15">
                                        <form:input path="skuCode" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">商品名称</label></td>
                                    <td class="width-15">
                                        <form:input path="skuName" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">状态</label></td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('BMS_BILL_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="bms:finance:bmsBillDetail:add">
                    <a id="add" class="btn btn-primary" onclick="add()">新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:finance:bmsBillDetail:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">删除</button>
                    <button id="removeByBillNo" class="btn btn-danger" onclick="removeByBillNo()">按费用单号删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:finance:bmsBillDetail:genBill">
                    <button id="genBill" class="btn btn-primary" onclick="genBill()">生成账单</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:finance:bmsBillDetail:import">
                        <button id="btnImport" class="btn btn-info"><i class="fa fa-file-excel-o"></i> 手工费</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:finance:bmsBillDetail:export">
                    <button id="export" class="btn btn-info" onclick="exportExcel()"><i class="fa fa-file-excel-o"></i> 导出</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="bmsBillDetailTable" data-toolbar="#toolbar" class="text-nowrap"></table>
            <div id="footer" style="display: none;">
                <table class="table-condensed" style="border-collapse: collapse;">
                    <tr>
                        <td><label class="pull-left">发生量：<span id="occurrenceQty"></span></label></td>
                        <td><label class="pull-left">计费量：<span id="billQty"></span></label></td>
                        <td><label class="pull-left">费用：<span id="cost"></span></label></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
<div id="removeBox" class="hide">
    <input type="hidden" id="parentId"/>
    <form id="removeForm" method="post" enctype="multipart/form-data">
        <table class="table table-bordered">
            <tbody>
            <tr>
                <td><label class="pull-right asterisk">费用单号</label></td>
                <td>
                    <input id="r_billNo" name="billNo" class="form-control required">
                </td>
            </tr>
            </tbody>
        </table>
    </form>
</div>
</body>
</html>