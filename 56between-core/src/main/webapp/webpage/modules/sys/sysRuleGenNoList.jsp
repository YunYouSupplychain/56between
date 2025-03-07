<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>编号生成规则管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="sysRuleGenNoList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">编号生成规则列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="sysRuleGenNo">
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right" title="编号类型">编号类型</label></td>
                                    <td class="width-15">
                                        <form:input path="billType" htmlEscape="false" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right" title="前缀">前缀</label></td>
                                    <td class="width-15">
                                        <form:input path="prefix" htmlEscape="false" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right" title="说明">说明</label></td>
                                    <td class="width-15">
                                        <form:input path="remarks" htmlEscape="false" class="form-control"/>
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
                <shiro:hasPermission name="sys:ruleGenNo:add">
                    <a id="add" class="btn btn-primary" title="新建" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:ruleGenNo:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:ruleGenNo:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="sysRuleGenNoTable" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>