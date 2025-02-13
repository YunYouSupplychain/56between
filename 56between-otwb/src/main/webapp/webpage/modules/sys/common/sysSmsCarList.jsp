<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>车辆信息管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="sysSmsCarList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">车辆信息列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="sysSmsCar" class="form form-horizontal well clearfix">
                            <div class="col-md-12">
                                <div class="col-xs-12 col-sm-6 col-md-2">
                                    <label class="label-item single-overflow pull-left" title="编码：">编码：</label>
                                    <form:input path="code" htmlEscape="false" maxlength="64" class=" form-control"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 col-md-2">
                                    <label class="label-item single-overflow pull-left" title="车号：">车号：</label>
                                    <form:input path="carNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 col-md-2">
                                    <label class="label-item single-overflow pull-left" title="承运商编码：">承运商：</label>
                                    <sys:grid title="选择承运商" url="${ctx}/sys/common/sms/customer/grid" cssClass="form-control required"
                                              fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="code"
                                              displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="nameCn"
                                              fieldLabels="承运商编码|承运商名称" fieldKeys="code|nameCn"
                                              searchLabels="承运商编码|承运商名称" searchKeys="code|nameCn"
                                              queryParams="dataSet" queryParamValues="dataSet"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 col-md-2">
                                    <label class="label-item single-overflow pull-left" title="车辆类型：">车辆类型：</label>
                                    <sys:grid title="选择车辆类型" url="${ctx}/sys/common/sms/carType/grid" cssClass="form-control required"
                                              fieldId="carType" fieldName="carType" fieldKeyName="typeCode"
                                              displayFieldId="carTypeName" displayFieldName="carTypeName" displayFieldKeyName="typeName"
                                              fieldLabels="类型编码|类型名称" fieldKeys="typeCode|typeName"
                                              searchLabels="类型编码|类型名称" searchKeys="typeCode|typeName"
                                              queryParams="dataSet" queryParamValues="dataSet"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 col-md-2">
                                    <label class="label-item single-overflow pull-left" title="发动机号：">发动机号：</label>
                                    <form:input path="engineNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 col-md-2">
                                    <label class="label-item single-overflow pull-left" title="保险证号：">保险证号：</label>
                                    <form:input path="safeCode" htmlEscape="false" maxlength="64" class=" form-control"/>
                                </div>
                            </div>
                            <div class="col-md-12">
                                <div class="col-xs-12 col-sm-6 col-md-2">
                                    <label class="label-item single-overflow pull-left" title="司机：">司机：</label>
                                    <form:input path="diver" htmlEscape="false" maxlength="64" class=" form-control"/>
                                </div>
                                <div class="col-xs-12 col-sm-6 col-md-2">
                                    <label class="label-item single-overflow pull-left" title="手机号码：">手机号码：</label>
                                    <form:input path="phone" htmlEscape="false" maxlength="64" class=" form-control"/>
                                </div>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="sys:common:sms:car:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:sms:car:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:sms:car:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:sms:car:sync">
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
            <table id="sysSmsCarTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>