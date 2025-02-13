<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>运输价格体系管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsTransportGroupForm.js" %>
</head>
<body>
<div style="margin-left: 10px;">
    <shiro:hasPermission name="bms:bmsTransportGroup:add">
        <a class="btn btn-primary" onclick="save();">保存</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="bmsTransportGroupEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="orgId"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-10"><label class="pull-right">运输体系编码</label></td>
                <td class="width-15">
                    <form:input path="transportGroupCode" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right asterisk">运输体系名称</label></td>
                <td class="width-15">
                    <form:input path="transportGroupName" htmlEscape="false" class="form-control required"/>
                </td>
                <td class="width-10"><label class="pull-right">机构</label></td>
                <td class="width-15">
                    <form:input path="orgName" htmlEscape="false" class="form-control " readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">备注</label></td>
                <td class="width-15">
                    <form:input path="remarks" htmlEscape="false" class="form-control "/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</form:form>
<div style="margin: 10px; height: calc(100% - 150px); background-color: #FFFFFF;">
    <div class="col-xs-4 col-sm-4 col-md-4" style="padding: 0;">
        <shiro:hasPermission name="bms:bmsTransportGroup:add">
            <a id="addDetail" class="btn btn-primary" disabled="true" onclick="addDetail();">新增</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="bms:bmsTransportGroup:edit">
            <a id="editDetail" class="btn btn-primary" disabled="true" onclick="editDetail();">修改</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="bms:bmsTransportGroup:del">
            <a id="delDetail" class="btn btn-danger" disabled="true" onclick="delDetail();">删除</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="bms:bmsTransportGroup:importPrice">
            <a id="importPrice" class="btn btn-info" disabled="true"><i class="fa fa-file-excel-o"></i> 导入</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="bms:bmsTransportGroup:exportPrice">
            <a id="exportPrice" class="btn btn-info" disabled="true"><i class="fa fa-file-excel-o"></i> 导出</a>
        </shiro:hasPermission>
    </div>
    <div class="col-xs-2 col-md-2 col-sm-2" style="padding: 0;">
        <input type="text" id="startPlace" class="form-control" onkeyup="routeSearch(event)" placeholder="输入起点回车查询"/>
    </div>
    <div class="col-xs-2 col-md-2 col-sm-2" style="padding: 0;">
        <input type="text" id="endPlace" class="form-control" onkeyup="routeSearch(event)" placeholder="输入终点回车查询"/>
    </div>
    <div class="col-xs-8 col-sm-8 col-md-8" style="padding: 0">
        <table id="bmsTransportPriceTable" class="table table-bordered text-nowrap"></table>
    </div>
    <div class="col-xs-4 col-sm-4 col-md-4" style="padding: 0">
        <table id="bmsTransportStepPriceTable" class="table table-bordered text-nowrap"></table>
    </div>
</div>
<!-- Excel上传弹出框 -->
<div id="uploadModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" style="width:500px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">上传文件</h4>
            </div>
            <div class="modal-body">
                <form class="form" enctype="multipart/form-data">
                    <table class="table">
                        <tbody>
                        <tr>
                            <td><input type="file" id="uploadFile" name="uploadFile"/></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="downloadTemplate()">下载模板</button>
                <button type="button" class="btn btn-primary" onclick="uploadFile()">上传</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>