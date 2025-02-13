<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>加工单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmKitHeaderList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">加工单列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmKitEntity" class="form">
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">加工单号</label></td>
                                    <td class="width-15">
                                        <form:input path="kitNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">加工类型</label></td>
                                    <td class="width-15">
                                        <form:select path="kitType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_KIT_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">状态</label></td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_KIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">审核状态</label></td>
                                    <td class="width-15">
                                        <form:select path="auditStatus" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">物流单号</label></td>
                                    <td class="width-15">
                                        <form:input path="logisticNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">货主编码</label></td>
                                    <td class="width-15">
                                        <form:input path="ownerCode" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">预计加工时间从</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='fmEtk'>
                                            <input type='text' name="fmEtk" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">预计加工时间到</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='toEtk'>
                                            <input type='text' name="toEtk" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">计划加工台</label></td>
                                    <td class="width-15">
                                        <form:input path="kitLoc" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="kit:banQinWmKitHeader:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitHeader:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitHeader:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitHeader:duplicate">
                    <button id="duplicate" class="btn btn-primary" disabled onclick="duplicateHandler()">复制</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitHeader:audit">
                    <button id="audit" class="btn btn-primary" disabled onclick="auditHandler()">审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitHeader:cancelAudit">
                    <button id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAuditHandler()">取消审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitHeader:close">
                    <button id="close" class="btn btn-primary" disabled onclick="closeHandler()">关闭订单</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitHeader:cancel">
                    <button id="cancel" class="btn btn-primary" disabled onclick="cancelHeader()">取消订单</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmKitHeaderTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>