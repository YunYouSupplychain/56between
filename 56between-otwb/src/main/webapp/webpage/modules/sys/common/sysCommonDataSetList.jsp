<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>数据套管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="sysCommonDataSetList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">数据套列表</h3>
        </div>
        <div class="panel-body">
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="sysCommonDataSet">
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">编码</label></td>
                                    <td class="width-15">
                                        <form:input path="code" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">名称</label></td>
                                    <td class="width-15">
                                        <form:input path="name" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">是否默认</label></td>
                                    <td class="width-15">
                                        <form:select path="isDefault" cssClass="form-control">
                                            <form:option value=""/>
                                            <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
                <shiro:hasPermission name="sys:common:dataSet:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:dataSet:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:dataSet:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:dataSet:setDefault">
                    <button id="setDefault" class="btn btn-primary" disabled onclick="setDefault()"> 设置默认</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:dataSet:sync">
                    <button id="syncData" class="btn btn-primary" disabled onclick="syncData()"> 同步平台数据</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo"><i class="fa fa-search"></i> 检索</a>
            </div>
            <!-- 表格 -->
            <table id="sysCommonDataSetTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>