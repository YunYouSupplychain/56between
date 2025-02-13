<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务对象服务范围管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmTransportObjScopeList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">业务对象服务范围列表</h3>
        </div>
        <div class="panel-body">
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmTransportObjScopeEntity">
                            <form:hidden path="orgId" />
                            <table class="table well">
                                <tr>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">业务对象</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <sys:grid title="业务对象" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="transportObjCode" fieldName="transportObjCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="transportObjName" displayFieldName="transportObjName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="业务对象编码|业务对象名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="业务对象编码|业务对象名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="orgId" queryParamValues="orgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">业务服务范围</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <sys:grid title="业务服务范围" url="${ctx}/tms/basic/tmTransportScope/grid"
                                                  fieldId="transportScopeCode" fieldName="transportScopeCode" fieldKeyName="code"
                                                  displayFieldId="transportScopeName" displayFieldName="transportScopeName" displayFieldKeyName="name"
                                                  fieldLabels="业务服务范围编码|业务服务范围名称" fieldKeys="code|name"
                                                  searchLabels="业务服务范围编码|业务服务范围名称" searchKeys="code|name"
                                                  queryParams="orgId" queryParamValues="orgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">服务范围类型</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:select path="transportScopeType" class="form-control ">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_TRANSPORT_SCOPE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"></td>
                                    <td style="width:15%;vertical-align:middle;"></td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="basic:tmTransportObjScope:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmTransportObjScope:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmTransportObjScope:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmTransportObjScope:genCarrierRoute">
                    <button id="genCarrierRoute" class="btn btn-primary" onclick="genCarrierRoute()"> 生成承运商路由</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="tmTransportObjScopeTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>