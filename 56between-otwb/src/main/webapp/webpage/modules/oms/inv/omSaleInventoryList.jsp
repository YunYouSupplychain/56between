<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>销售库存管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="omSaleInventoryList.js" %>
</head>
<body>
<div class="hide">
    <input id="parentId" type="hidden"/>
    <input id="ownerType" value="OWNER" type="hidden"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">销售库存列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <form:form id="searchForm" modelAttribute="omSaleInventoryEntity" class="form">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">仓库</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/sys/office/companyData" title="选择仓库" cssClass="form-control required"
                                           fieldId="warehouse" fieldName="warehouse" fieldKeyName="id" fieldValue=""
                                           displayFieldId="warehouseName" displayFieldName="warehouseName" displayFieldKeyName="name" displayFieldValue=""
                                           selectButtonId="outWarhouseSelectId" deleteButtonId="outWarhouseDeleteId"
                                           fieldLabels="仓库编码|仓库名称" fieldKeys="code|name"
                                           searchLabels="仓库编码|仓库名称" searchKeys="code|name"
                                           queryParams="id" queryParamValues="parentId">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">货主</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择货主" cssClass="form-control" allowInput="true"
                                           fieldId="owner" fieldName="owner" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                           displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                           selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                           fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           queryParams="ebcuType" queryParamValues="ownerType">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">商品</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/oms/basic/omItem/popData" title="选择商品" cssClass="form-control" allowInput="true"
                                           fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue=""
                                           displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                           selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                           fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                           searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="codeAndName"
                                           queryParams="ownerCode" queryParamValues="owner">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">状态</label></td>
                        <td class="width-15">
                            <form:select path="status" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_INVENTORY_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form:form>
            <!-- 工具栏 -->
            <div id="toolbar">
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
            </div>
            <!-- 表格 -->
            <table id="omSaleInventoryTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>