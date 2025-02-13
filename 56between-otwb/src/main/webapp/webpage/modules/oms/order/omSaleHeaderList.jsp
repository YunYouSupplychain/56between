<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>销售订单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="omSaleHeaderList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">销售订单列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="omSaleHeaderEntity" class="form">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">销售单号</label></td>
                                    <td class="width-15">
                                        <form:input path="saleNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单日期(从)</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderDateFm'>
                                            <input type='text' name="orderDateFm" class="form-control required" title="订单日期(从)"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单日期(到)</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderDateTo'>
                                            <input type='text' name="orderDateTo" class="form-control required" title="订单日期(到)"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单状态</label></td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_SALE_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">订单类型</label></td>
                                    <td class="width-15">
                                        <form:select path="saleType" class="form-control required">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_SALE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">下发机构</label></td>
                                    <td class="width-15">
                                        <input id="parentId" value="${fns:getUser().office.id}" type="hidden">
                                        <sys:popSelect url="${ctx}/sys/office/companyData" title="选择机构" cssClass="form-control required"
                                                       fieldId="outWarhouse" fieldName="outWarhouse" fieldKeyName="id" fieldValue="${omSaleHeaderEntity.outWarhouse}"
                                                       displayFieldId="outWarhouseName" displayFieldName="outWarhouseName" displayFieldKeyName="name"
                                                       displayFieldValue="${omSaleHeaderEntity.outWarhouseName}"
                                                       selectButtonId="outWarhouseSelectId" deleteButtonId="outWarhouseDeleteId"
                                                       fieldLabels="机构编码|机构名称" fieldKeys="code|name"
                                                       searchLabels="机构编码|机构名称" searchKeys="code|name"
                                                       queryParams="id" queryParamValues="parentId">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户</label></td>
                                    <td class="width-15">
                                        <input id="customerType" value="CONSIGNEE" type="hidden">
                                        <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择客户" cssClass="form-control" allowInput="true"
                                                       fieldId="customer" fieldName="customer" fieldKeyName="ebcuCustomerNo" fieldValue="${omSaleHeaderEntity.customer}"
                                                       displayFieldId="customerName" displayFieldName="customerName" displayFieldKeyName="ebcuNameCn"
                                                       displayFieldValue="${omSaleHeaderEntity.customerName}"
                                                       selectButtonId="customerSelectId" deleteButtonId="customerDeleteId"
                                                       fieldLabels="客户编码|客户名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="客户编码|客户名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="customerType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"><label class="pull-right">货主</label></td>
                                    <td class="width-15">
                                        <input id="ownerType" value="OWNER" type="hidden">
                                        <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择货主" cssClass="form-control" allowInput="true"
                                                       fieldId="owner" fieldName="owner" fieldKeyName="ebcuCustomerNo" fieldValue="${omPoHeaderEntity.owner}"
                                                       displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${omPoHeaderEntity.ownerName}"
                                                       selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="ownerType">
                                        </sys:popSelect>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">渠道</label></td>
                                    <td class="width-15">
                                        <form:select path="channel" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_CHANNEL')}" itemLabel="label"
                                                          itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户订单号</label></td>
                                    <td class="width-15">
                                        <form:input path="customerNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">销售方式</label></td>
                                    <td class="width-15">
                                        <form:select path="saleMethod" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_SALE_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
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
                <shiro:hasPermission name="order:omSaleHeader:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omSaleHeader:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omSaleHeader:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omSaleHeader:audit">
                    <button id="audit" class="btn btn-primary" disabled onclick="audit()"> 审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omSaleHeader:cancelAudit">
                    <button id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAudit()"> 取消审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omSaleHeader:createChainOrder">
                    <button id="createChainOrder" class="btn btn-primary" disabled onclick="createChainOrder()"> 生成供应链订单</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omSaleHeader:closeOrder">
                    <button id="closeOrder" class="btn btn-primary" disabled onclick="closeOrder()"> 关闭</button>
                </shiro:hasPermission>
                <button id="printShipOrder" class="btn btn-primary" onclick="printShipOrder()">打印销售发货单</button>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="omSaleHeaderTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>