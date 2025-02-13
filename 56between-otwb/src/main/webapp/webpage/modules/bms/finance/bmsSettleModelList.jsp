<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>结算模型管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsSettleModelList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">结算模型列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="bmsSettleModelEntity" class="form">
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">模型编码</label></td>
                                    <td class="width-15">
                                        <form:input path="settleModelCode" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">结算对象编码</label></td>
                                    <td class="width-15">
                                        <form:input path="settleObjectCode" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">结算对象名称</label></td>
                                    <td class="width-15">
                                        <form:input path="settleObjectName" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">负责人</label></td>
                                    <td class="width-15">
                                        <form:input path="responsiblePerson" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="bms:finance:bmsSettleModel:add">
                    <a id="add" class="btn btn-primary" onclick="add()">新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:finance:bmsSettleModel:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()">修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:finance:bmsSettleModel:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:finance:bmsSettleModel:copy">
                    <button id="copy" class="btn btn-primary" disabled onclick="copy()">复制</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:finance:bmsSettleModel:calc">
                    <button id="calc" class="btn btn-primary" disabled onclick="calc()">计算</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:finance:bmsSettleModel:batchCalc">
                    <button id="batchCalc" class="btn btn-primary" onclick="batchCalc()">批量计算</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="bmsSettleModelTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
<!-- 计算弹出窗 -->
<div id="calcModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" style="width:400px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">结算日期范围</h4>
            </div>
            <div class="modal-body">
                <form id="calcForm" class="form" enctype="multipart/form-data">
                    <table class="table">
                        <tbody>
                        <tr>
                            <td class="width-15"><label class="pull-right asterisk">日期从</label></td>
                            <td class="width-35">
                                <div class='input-group form_datetime' id='fmDate'>
                                    <input type='text' name="fmDate" class="form-control required"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-15"><label class="pull-right asterisk">日期到</label></td>
                            <td class="width-35">
                                <div class='input-group form_datetime' id='toDate'>
                                    <input type='text' name="toDate" class="form-control required"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="calculate">计算</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>