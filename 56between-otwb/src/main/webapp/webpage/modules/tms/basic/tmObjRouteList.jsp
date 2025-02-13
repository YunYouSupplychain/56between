<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务路由管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmObjRouteList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="orgId"/>
    <input type="hidden" id="outletType" value="OUTLET"/>
    <input type="hidden" id="carrierType" value="CARRIER"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">业务路由列表</h3>
        </div>
        <div class="panel-body">
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmObjRouteEntity">
                            <form:hidden path="orgId" />
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">起点对象</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择起点对象" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control"
                                                  fieldId="startObjCode" fieldName="startObjCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="startObjName" displayFieldName="startObjName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="编码|名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="编码|名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="outletType|orgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">终点对象</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择终点对象" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control"
                                                  fieldId="endObjCode" fieldName="endObjCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="endObjName" displayFieldName="endObjName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="编码|名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="编码|名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="outletType|orgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">承运商</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择承运商" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control"
                                                  fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="编码|名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="编码|名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="carrierType|orgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">审核状态</label></td>
                                    <td class="width-15">
                                        <form:select path="auditStatus" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
                <shiro:hasPermission name="tms:basic:objRoute:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:basic:objRoute:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:basic:objRoute:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:basic:objRoute:audit">
                    <button id="audit" class="btn btn-primary" disabled onclick="audit()"> 审核</button>
                    <button id="batchAudit" class="btn btn-primary" disabled onclick="batchAudit()"> 批量审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:basic:objRoute:cancelAudit">
                    <button id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAudit()"> 取消审核</button>
                    <button id="batchCancelAudit" class="btn btn-primary" disabled onclick="batchCancelAudit()"> 批量取消审核</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="tmObjRouteTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>