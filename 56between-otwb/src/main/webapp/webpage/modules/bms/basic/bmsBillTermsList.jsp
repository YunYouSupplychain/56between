<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>计费条款管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsBillTermsList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">计费条款列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="bmsBillTermsEntity" class="form form-horizontal well clearfix">
                            <table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
                                <tr>
                                    <td class="width-10"><label class="pull-right" title="计费条款代码：">计费条款代码</label></td>
                                    <td class="width-15">
                                        <form:input path="billTermsCode" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right" title="计费条款说明：">计费条款说明</label></td>
                                    <td class="width-15">
                                        <form:input path="billTermsDesc" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right" title="费用模块：">费用模块</label></td>
                                    <td class="width-15">
                                        <form:select path="billModule" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('BMS_BILL_MODULE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right" title="输出对象：">输出对象</label></td>
                                    <td class="width-15">
                                        <form:select path="outputObjects" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('BMS_OUTPUT_OBJECT')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right" title="行业：">行业</label></td>
                                    <td class="width-15">
                                        <form:input path="useIndustry" htmlEscape="false" class="form-control "/>
                                    </td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="bms:bmsBillTerms:add">
                    <a id="add" class="btn btn-primary" onclick="add()">新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsBillTerms:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()">修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsBillTerms:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsBillTerms:duplicate">
                    <button id="duplicate" class="btn btn-primary" disabled onclick="duplicate()">复制</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="bmsBillTermsTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>