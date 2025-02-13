<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>质检单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmQcHeaderList.js" %>
</head>
<body>
<div class="hide">
    <input id="ownerType" value="OWNER" type="hidden">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">质检单列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmQcEntity" class="form">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="质检单号：">质检单号：</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="qcNo" htmlEscape="false" maxlength="32" class="form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="质检阶段：">质检阶段：</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="qcPhase" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_QC_PHASE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="状态：">状态：</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_QC_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="审核状态：">审核状态：</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="auditStatus" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="单据类型：">单据类型：</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="orderType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_ORDER_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="单据号：">单据号：</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="orderNo" htmlEscape="false" maxlength="32" class="form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="货主：">货主：</label>
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
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="qc:banQinWmQcHeader:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="qc:banQinWmQcHeader:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="qc:banQinWmQcHeader:audit">
                    <button id="audit" class="btn btn-primary" disabled onclick="audit()">审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="qc:banQinWmQcHeader:cancelAudit">
                    <button id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAudit()">取消审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="qc:banQinWmQcHeader:closeOrder">
                    <button id="close" class="btn btn-primary" disabled onclick="closeOrder()">关闭订单</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmQcHeaderTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>