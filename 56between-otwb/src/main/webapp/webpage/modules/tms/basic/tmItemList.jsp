<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>商品信息管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmItemList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="transportObjType" value="OWNER"/>
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
                        <form:form id="searchForm" modelAttribute="tmItemEntity">
                            <form:hidden path="orgId" />
                            <table class="table well">
                                <tr>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">商品编码</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:input path="skuCode" htmlEscape="false" class=" form-control" maxlength="64" />
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">商品名称</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:input path="skuName" htmlEscape="false" class=" form-control" maxlength="64" />
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">客户</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <sys:grid title="选择客户" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control"
                                                  fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="客户编码|客户名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="客户编码|客户名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="transportObjType|orgId"/>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">商品类型</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:select path="skuType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_SKU_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
                <shiro:hasPermission name="basic:tmItem:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmItem:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmItem:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmItem:enable">
                    <button id="enable" class="btn btn-primary" disabled onclick="enable('0')"> 启用</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmItem:unable">
                    <button id="unable" class="btn btn-primary" disabled onclick="enable('1')"> 停用</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="tmItemTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>