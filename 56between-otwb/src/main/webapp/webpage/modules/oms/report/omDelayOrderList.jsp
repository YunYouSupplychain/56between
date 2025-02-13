<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>供应链订单卡单报表管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="/webpage/include/treeview.jsp" %>
    <%@include file="omDelayOrderList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">供应链订单卡单报表管理</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>

            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="omDelayOrderEntity" cssClass="form">
                            <input id="orgId" name="orgId" type="hidden">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">客户订单号</label></td>
                                    <td class="width-15">
                                        <form:input path="customerNo" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">外部单号</label></td>
                                    <td class="width-15">
                                        <form:input path="def1" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">业务订单类型</label></td>
                                    <td class="width-15">
                                        <form:select path="businessOrderType" class="form-control required">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_BUSINESS_ORDER_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单状态</label></td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_CHAIN_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">商流订单号</label></td>
                                    <td class="width-15">
                                        <form:input path="businessNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">供应链订单号</label></td>
                                    <td class="width-15">
                                        <form:input path="chainNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单日期(从)</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderDateFm'>
                                            <input type='text' name="orderDateFm" class="form-control required" title="订单日期(从)"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单日期(到)</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderDateTo'>
                                            <input type='text' name="orderDateTo" class="form-control required" title="订单日期(到)"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">货主</label></td>
                                    <td class="width-15">
                                        <input id="ownerType" value="OWNER" type="hidden">
                                        <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择货主" cssClass="form-control" allowInput="true"
                                                       fieldId="owner" fieldName="owner" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                       displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="ownerType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"><label class="pull-right">供应商</label></td>
                                    <td class="width-15">
                                        <input id="supplierType" value="SUPPLIER" type="hidden">
                                        <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择供应商" cssClass="form-control" allowInput="true"
                                                       fieldId="supplierCode" fieldName="supplierCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                       displayFieldId="supplierName" displayFieldName="supplierName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="supplierSelectId" deleteButtonId="supplierDeleteId"
                                                       fieldLabels="供应商编码|供应商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="供应商编码|供应商名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="supplierType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户</label></td>
                                    <td class="width-15">
                                        <input id="customerType" value="CONSIGNEE" type="hidden">
                                        <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择客户" cssClass="form-control" allowInput="true"
                                                       fieldId="customer" fieldName="customer" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                       displayFieldId="customerName" displayFieldName="customerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="customerSelectId" deleteButtonId="customerDeleteId"
                                                       fieldLabels="客户编码|客户名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="客户编码|客户名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="customerType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"><label class="pull-right">渠道</label></td>
                                    <td class="width-15">
                                        <form:select path="channel" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_CHANNEL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">订单来源</label></td>
                                    <td class="width-15">
                                        <form:select path="orderSource" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_ORDER_SOURCE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">拦截状态</label></td>
                                    <td class="width-15">
                                        <form:select path="interceptStatus" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_INTERCEPT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">收货联系人</label></td>
                                    <td class="width-15">
                                        <form:input path="consignee" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">收货联系人电话</label></td>
                                    <td class="width-15">
                                        <form:input path="consigneeTel" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">收货三级地址</label></td>
                                    <td class="width-15">
                                        <form:input path="consigneeAddressArea" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">商品</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/oms/basic/omItem/grid" title="选择商品" cssClass="form-control" allowInput="true"
                                                       fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue=""
                                                       displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                                       selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                                       fieldLabels="货主编码|货主名称|商品编码|商品名称" fieldKeys="ownerCode|ownerName|skuCode|skuName"
                                                       searchLabels="货主编码|货主名称|商品编码|商品名称" searchKeys="ownerCode|ownerName|skuCode|skuName" inputSearchKey="codeAndName">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>

            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="report:omDelayOrder:export">
                    <button class="btn btn-info" onclick="exportData()"><i class="fa fa-file-excel-o"></i> 导出</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>

            <!-- 表格 -->
            <table id="omDelayOrderTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>