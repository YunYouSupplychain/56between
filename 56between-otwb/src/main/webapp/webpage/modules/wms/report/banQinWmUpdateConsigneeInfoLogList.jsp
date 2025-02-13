<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>收货信息更新日志管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmUpdateConsigneeInfoLogList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">收货信息更新日志列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmUpdateConsigneeInfoLogEntity" class="form">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="出库单号：">出库单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="soNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="客户单号：">客户单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="customerNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="外部单号：">外部单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="extendNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="面单号：">面单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="trackingNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="创建时间从：">创建时间从</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='createDateFm'>
                                            <input type='text' name="createDateFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="创建时间到：">创建时间到</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='createDateTo'>
                                            <input type='text' name="createDateTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
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
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmUpdateConsigneeInfoLogTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>