<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>商品信息管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="omItemList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="ownerType" value="OWNER"/>
    <input type="hidden" id="orgId"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">商品信息列表</h3>
        </div>
        <div class="panel-body">
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="omItemEntity">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">货主编码</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择货主" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                                  fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo"
                                                  displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn"
                                                  fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                  searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                  queryParams="ebcuType|orgId" queryParamValues="ownerType|orgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">商品编码</label></td>
                                    <td class="width-15">
                                        <form:input path="skuCode" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">商品名称</label></td>
                                    <td class="width-15">
                                        <form:input path="skuName" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户商品编码</label></td>
                                    <td class="width-15">
                                        <form:input path="customerNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">规格</label></td>
                                    <td class="width-15">
                                        <form:input path="spec" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">品类</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择商品分类" url="" cssClass="form-control"
                                                  fieldId="skuClass" fieldName="skuClass" fieldKeyName="code"
                                                  displayFieldId="skuClassName" displayFieldName="skuClassName" displayFieldKeyName="name"
                                                  fieldLabels="编码|名称" fieldKeys="code|name"
                                                  searchLabels="编码|名称" searchKeys="code|name"
                                                  queryParams="orgId" queryParamValues="orgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">温层</label></td>
                                    <td class="width-15">
                                        <form:select path="skuTempLayer" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('temperature_layer')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">课别</label></td>
                                    <td class="width-15">
                                        <form:select path="skuType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('sku_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="basic:omItem:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:omItem:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:omItem:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="omItemTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>