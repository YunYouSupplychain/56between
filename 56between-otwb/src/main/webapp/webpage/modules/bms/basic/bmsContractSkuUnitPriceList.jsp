<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>合同商品价格管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsContractSkuUnitPriceList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="orgId"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">合同商品价格列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="bmsContractSkuPriceEntity" class="form">
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">系统合同编号</label></td>
                                    <td class="width-15">
                                        <form:input path="sysContractNo" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">结算对象编码</label></td>
                                    <td class="width-15">
                                        <form:input path="settleObjectCode" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">结算对象名称</label></td>
                                    <td class="width-15">
                                        <form:input path="settleObjectName" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">品类</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择商品分类" url="${ctx}/bms/basic/skuClassification/grid" cssClass="form-control"
                                                  fieldId="skuClass" fieldName="skuClass" fieldKeyName="code"
                                                  displayFieldId="skuClassName" displayFieldName="skuClassName" displayFieldKeyName="name"
                                                  fieldLabels="编码|名称" fieldKeys="code|name"
                                                  searchLabels="编码|名称" searchKeys="code|name"
                                                  queryParams="orgId" queryParamValues="orgId"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">商品编码</label></td>
                                    <td class="width-15">
                                        <form:input path="skuCode" htmlEscape="false" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">商品名称</label></td>
                                    <td class="width-15">
                                        <form:input path="skuName" htmlEscape="false" class=" form-control"/>
                                    </td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="bms:bmsContractSkuPrice:add">
                    <a id="add" class="btn btn-primary" onclick="add()">新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContractSkuPrice:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()">修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContractSkuPrice:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContractSkuPrice:import">
                    <button id="btnImport" class="btn btn-info"><i class="fa fa-file-excel-o"></i> 导入</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContractSkuPrice:export">
                    <button id="export" class="btn btn-info" onclick="exportExcel()"><i class="fa fa-file-excel-o"></i> 导出</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="bmsContractSkuPriceTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>