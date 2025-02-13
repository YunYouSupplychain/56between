<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>运输人员信息管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmDriverList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">运输人员信息列表</h3>
        </div>
        <div class="panel-body">
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmDriverEntity">
                            <form:hidden path="orgId" />
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">编码</label></td>
                                    <td class="width-15">
                                        <form:input path="code" htmlEscape="false" class="form-control" maxlength="35" />
                                    </td>
                                    <td class="width-10"><label class="pull-right">姓名</label></td>
                                    <td class="width-15">
                                        <form:input path="name" htmlEscape="false" class="form-control" maxlength="64" />
                                    </td>
                                    <td class="width-10"><label class="pull-right">承运商</label></td>
                                    <td class="width-15">
                                        <input type="hidden" id="transportObjType" value="CARRIER">
                                        <sys:grid title="承运商" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="承运商编码|承运商名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="承运商编码|承运商名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="transportObjType|orgId"
                                                  cssClass="form-control"/>
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
                <shiro:hasPermission name="basic:tmDriver:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmDriver:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmDriver:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmDriver:enable">
                    <button id="enable" class="btn btn-primary" disabled onclick="enable('0')"> 启用</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmDriver:unable">
                    <button id="unable" class="btn btn-primary" disabled onclick="enable('1')"> 停用</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="tmDriverTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>