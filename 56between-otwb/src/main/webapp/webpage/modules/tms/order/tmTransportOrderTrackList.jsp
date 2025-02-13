<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>物流节点管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmTransportOrderTrackList.js" %>
</head>
<body>
<div class="hidden">
    <input type="hidden" id="principalType" value="CUSTOMER">
    <input type="hidden" id="settlementType" value="SETTLEMENT"/>
    <input type="hidden" id="customerType" value="OWNER">
    <input type="hidden" id="outletType" value="OUTLET">
    <input type="hidden" id="carrierType" value="CARRIER">
    <input type="hidden" id="shipType" value="SHIP">
    <input type="hidden" id="consigneeType" value="CONSIGNEE">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">物流节点列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmTransportOrderTrack" class="form">
                            <form:hidden path="orgId"/>
                            <form:hidden path="baseOrgId"/>
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">运输单号</label></td>
                                    <td class="width-15">
                                        <form:input path="transportNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户单号</label></td>
                                    <td class="width-15">
                                        <form:input path="customerNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">标签号</label></td>
                                    <td class="width-15">
                                        <form:input path="labelNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">派车单号</label></td>
                                    <td class="width-15">
                                        <form:input path="dispatchNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="tms:order:transport:track:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:transport:track:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:transport:track:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:transport:track:import">
                    <button id="uploadFile" class="btn btn-info" onclick="openUploadFile()"><i class="fa fa-file-excel-o"></i> 导入</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="tmTransportOrderTrackTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
<!-- Excel上传弹出框 -->
<div class="modal fade" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:500px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">上传文件</h4>
            </div>
            <div class="modal-body">
                <form class="form" id="fileUploadForm" enctype="multipart/form-data">
                    <table class="table well">
                        <tbody>
                        <tr>
                            <td class="active" style="width:70%">
                                <input type="file" id="uploadFileName" name="file"/>
                            </td>
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
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>
</html>