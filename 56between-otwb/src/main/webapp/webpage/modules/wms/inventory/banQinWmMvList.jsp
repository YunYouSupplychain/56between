<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>库存移动单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmMvList.js" %>
</head>
<body>
<div class="hide">
    <input id="ownerType" value="OWNER" type="hidden">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">库存移动单列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>

            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmMvEntity" class="form">
                            <input id="orgId" name="orgId" type="hidden">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="移动单号：">移动单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="mvNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="货主：">货主</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                       fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="ownerType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="状态：">状态</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="status"  class="form-control m-b">
                                            <form:option value=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_MV_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="审核状态：">审核状态</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="auditStatus"  class="form-control m-b">
                                            <form:option value=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
                <shiro:hasPermission name="inventory:banQinWmMv:add">
                    <a id="add" class="btn btn-primary" onclick="add()">新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmMv:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()">修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmMv:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmMv:audit">
                    <button id="audit" class="btn btn-primary" disabled onclick="audit()">审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmMv:cancelAudit">
                    <button id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAudit()">取消审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmMv:executeMv">
                    <button id="executeMv" class="btn btn-primary" disabled onclick="executeMv()">执行移动</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmMv:closeOrder">
                    <button id="closeOrder" class="btn btn-primary" disabled onclick="closeOrder()">关闭订单</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmMv:cancelOrder">
                    <button id="cancelOrder" class="btn btn-primary" disabled onclick="cancelOrder()">取消订单</button>
                </shiro:hasPermission>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">打印<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <shiro:hasPermission name="inventory:banQinWmAdHeader:printMvOrder">
                            <li><a id="printMvOrder" onclick="printMvOrder()">打印移动单</a></li>
                        </shiro:hasPermission>
                    </ul>
                </div>
                <shiro:hasPermission name="inventory:banQinWmMv:export">
                    <button id="export" class="btn btn-primary" onclick="exportData()"><i class="fa fa-file-excel-o"></i> 导出</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmMvTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>