<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>路由范围管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmRouteScopeList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="orgId"/>
    <input type="hidden" id="carrierType" value="CARRIER"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">路由范围列表</h3>
        </div>
        <div class="panel-body">
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmRouteScopeEntity">
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">编码</label></td>
                                    <td class="width-15">
                                        <form:input path="code" cssClass="form-control" htmlEscape="false"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">名称</label></td>
                                    <td class="width-15">
                                        <form:input path="name" cssClass="form-control" htmlEscape="false"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">承运商</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择承运商" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control"
                                                  fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="transportObjCode"
                                                  fieldLabels="编码|名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="编码|名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="carrierType|orgId"/>
                                    </td>
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
                <shiro:hasPermission name="tms:basic:routeScope:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:basic:routeScope:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:basic:routeScope:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:basic:routeScope:genCarrierRoute">
                    <button id="genRoute" class="btn btn-primary" onclick="genRoute()"> 生成业务路由</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="tmRouteScopeTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>