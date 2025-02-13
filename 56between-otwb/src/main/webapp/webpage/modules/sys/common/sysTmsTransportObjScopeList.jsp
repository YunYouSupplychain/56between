<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务对象服务范围管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="sysTmsTransportObjScopeList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">业务对象服务范围列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="sysTmsTransportObjScopeEntity" class="form form-horizontal well clearfix">
                            <table class="table table-striped table-bordered table-condensed">
                                <tr>
                                    <td class="width-10"><label class="pull-right">业务对象：</label></td>
                                    <td class="width-15">
                                        <sys:grid title="业务对象" url="${ctx}/sys/common/tms/transportObj/grid"
                                                  fieldId="transportObjCode" fieldName="transportObjCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="transportObjName" displayFieldName="transportObjName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="业务对象编码|业务对象名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="业务对象编码|业务对象名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="dataSet" queryParamValues="dataSet" cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">业务服务范围：</label></td>
                                    <td class="width-15">
                                        <sys:grid title="业务服务范围" url="${ctx}/sys/common/tms/transportScope/grid"
                                                  fieldId="transportScopeCode" fieldName="transportScopeCode" fieldKeyName="code"
                                                  displayFieldId="transportScopeName" displayFieldName="transportScopeName" displayFieldKeyName="name"
                                                  fieldLabels="业务服务范围编码|业务服务范围名称" fieldKeys="code|name"
                                                  searchLabels="业务服务范围编码|业务服务范围名称" searchKeys="code|name"
                                                  queryParams="dataSet" queryParamValues="dataSet" cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">服务范围类型：</label></td>
                                    <td class="width-15">
                                        <form:select path="transportScopeType" class="form-control ">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_TRANSPORT_SCOPE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
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
                <shiro:hasPermission name="sys:common:tms:transportObjScope:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:tms:transportObjScope:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:tms:transportObjScope:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:tms:transportObjScope:genCarrierRoute">
                    <button id="genCarrierRoute" class="btn btn-primary" onclick="genCarrierRoute()"> 生成承运商路由</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:tms:transportObjScope:sync">
                    <button id="syncSelect" class="btn btn-primary" onclick="syncSelect()"> 同步选择数据</button>
                    <button id="syncAll" class="btn btn-primary" onclick="syncAll()"> 同步全部数据</button>
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