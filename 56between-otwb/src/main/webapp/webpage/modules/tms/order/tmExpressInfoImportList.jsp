<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>快递单号导入更新管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@ include file="tmExpressInfoImportList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">快递单号导入更新列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>

            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmExpressInfoImport" cssClass="form">
                            <form:hidden path="orgId"/>
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">导入批次号</label></td>
                                    <td class="width-15">
                                        <form:input path="importNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">文件名称</label></td>
                                    <td class="width-15">
                                        <form:input path="fileName" htmlEscape="false" maxlength="20" class=" form-control"/>
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
                <shiro:hasPermission name="tms:order:tmExpressInfoImport:import">
                    <button id="importOrder" class="btn btn-info" onclick="importOrder()"><i class="fa fa-file-excel-o"></i> 导入</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="tmExpressInfoImportTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
<!-- Excel上传弹出框 -->
<div class="modal fade" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 500px;">
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
                            <td><label class="pull-right">导入原因</label></td>
                            <td>
                                <input type="text" id="importReason" class="form-control" maxlength="64"/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input type="file" id="uploadFileName" name="file"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="downloadTemplate()">下载导入模板</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="uploadFile()">上传</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>