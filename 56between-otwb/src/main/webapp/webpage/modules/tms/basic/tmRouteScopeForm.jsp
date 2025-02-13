<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>路由范围管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp"%>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmRouteScopeForm.js"%>
    <style type="text/css">
        #left {
            width: 50%;
            float: left;
        }

        #right {
            width: 50%;
            float: right;
        }
    </style>
</head>
<body>
<div class="hide">
    <input type="hidden" id="carrierType" value="CARRIER"/>
</div>
<form:form id="inputForm" modelAttribute="tmRouteScopeEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="delFlag"/>
    <form:hidden path="recVer"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">基本信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tr>
                        <td class="width-8"><label class="pull-right asterisk">编码</label></td>
                        <td class="width-12">
                            <form:input path="code" htmlEscape="false" class="form-control required" maxlength="35"/>
                        </td>
                        <td class="width-8"><label class="pull-right asterisk">名称</label></td>
                        <td class="width-12">
                            <form:input path="name" htmlEscape="false" class="form-control required" maxlength="50"/>
                        </td>
                        <td class="width-8"><label class="pull-right asterisk">承运商</label></td>
                        <td class="width-12">
                            <sys:grid title="选择承运商" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control required"
                                      fieldId="carrierCode" fieldName="carrierCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmRouteScopeEntity.carrierCode}"
                                      displayFieldId="carrierName" displayFieldName="carrierName"
                                      displayFieldKeyName="transportObjCode" displayFieldValue="${tmRouteScopeEntity.carrierName}"
                                      fieldLabels="编码|名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="编码|名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="carrierType|orgId"/>
                        </td>
                        <td class="width-8"><label class="pull-right">备注信息</label></td>
                        <td class="width-12">
                            <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="255"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">起始点</h3>
            </div>
            <div class="panel-body">
                <div id="left-toolbar">
                    <shiro:hasPermission name="tms:basic:routeScope:add">
                        <a class="add btn btn-primary" onclick="add('1')"> 添加</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="tms:basic:routeScope:del">
                        <button type="button" class="remove btn btn-danger" disabled onclick="remove('1')"> 删除</button>
                    </shiro:hasPermission>
                </div>
                <table id="left-table" class="text-nowrap" data-toolbar="#left-toolbar"></table>
            </div>
        </div>
    </div>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">目的地</h3>
            </div>
            <div class="panel-body">
                <div id="right-toolbar">
                    <shiro:hasPermission name="tms:basic:routeScope:add">
                        <a class="add btn btn-primary" onclick="add('2')"> 添加</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="tms:basic:routeScope:del">
                        <button type="button" class="remove btn btn-danger" disabled onclick="remove('2')"> 删除</button>
                    </shiro:hasPermission>
                </div>
                <table id="right-table" class="text-nowrap" data-toolbar="#right-toolbar"></table>
            </div>
        </div>
    </div>
</form:form>
<div id="addModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:1000px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">业务对象</h4>
            </div>
            <div class="modal-body">
                <div class="wrapper wrapper-content">
                    <!-- 搜索 -->
                    <div class="accordion-group">
                        <div class="accordion-inner">
                            <input type="hidden" id="type"/>
                            <form id="searchForm" class="form well clearfix" onsubmit="return false;">
                                <div class="col-xs-12 col-sm-6 col-md-3">
                                    <input name="transportObjCode" maxlength="64" class="form-control" placeholder="编码"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 col-md-3">
                                    <input name="transportObjName" maxlength="64" class="form-control" placeholder="名称"/>
                                </div>
                                <div class="col-xs-12 col-sm-12 col-md-12">
                                    <div style="margin-top:5px">
                                        <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                                        <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                                        <a class="btn btn-primary" onclick="addConfirm('SELECT')">勾选确认</a>
                                        <a class="btn btn-primary" onclick="addConfirm('ALL')">全选确认</a>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    <!-- 表格 -->
                    <table id="table" class="text-nowrap"></table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>