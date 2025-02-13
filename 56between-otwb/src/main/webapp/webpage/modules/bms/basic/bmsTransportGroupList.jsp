<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>运输价格体系管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsTransportGroupList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">运输价格体系列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="bmsTransportGroupEntity" class="form">
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">运输体系编码</label></td>
                                    <td class="width-15">
                                        <form:input path="transportGroupCode" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">运输体系名称</label></td>
                                    <td class="width-15">
                                        <form:input path="transportGroupName" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
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
                <shiro:hasPermission name="bms:bmsTransportGroup:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsTransportGroup:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsTransportGroup:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsTransportGroup:copy">
                    <button id="copy" class="btn btn-primary" disabled onclick="copy()"> 复制</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="bmsTransportGroupTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
<!-- 复制弹出窗 -->
<div id="copyModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:400px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">复制</h4>
            </div>
            <div class="modal-body">
                <form id="copyForm" class="form" enctype="multipart/form-data">
                    <table class="table">
                        <tbody>
                        <tr>
                            <td style="width: 80px;padding-top: 5px;"><label class="pull-right asterisk">机构</label></td>
                            <td style="width: 220px;padding-top: 5px;">
                                <sys:popSelect url="${ctx}/sys/office/companyData" title="选择机构" cssClass="form-control required"
                                               fieldId="orgId" fieldName="orgId" fieldKeyName="id" fieldValue=""
                                               displayFieldId="orgName" displayFieldName="orgName" displayFieldKeyName="name" displayFieldValue=""
                                               selectButtonId="orgSelectId" deleteButtonId="orgDeleteId"
                                               fieldLabels="机构编码|机构名称" fieldKeys="code|name"
                                               searchLabels="机构编码|机构名称" searchKeys="code|name">
                                </sys:popSelect>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="copySave()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>