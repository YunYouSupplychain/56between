<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>商品信息管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="sysCommonSkuList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="ownerType" value="OWNER"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">商品信息列表</h3>
        </div>
        <div class="panel-body">
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="sysCommonSku">
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">货主</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择货主" url="${ctx}/sys/common/customer/grid" cssClass="form-control"
                                                  fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="code"
                                                  displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="name"
                                                  fieldLabels="货主编码|货主名称" fieldKeys="code|name"
                                                  searchLabels="货主编码|货主名称" searchKeys="code|name"
                                                  queryParams="type" queryParamValues="ownerType"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">商品编码</label></td>
                                    <td class="width-15">
                                        <form:input path="skuCode" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">商品名称</label></td>
                                    <td class="width-15">
                                        <form:input path="skuName" htmlEscape="false" maxlength="255" class=" form-control"/>
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
                <shiro:hasPermission name="sys:common:sku:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:sku:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:sku:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:sku:import">
                    <button id="import" class="btn btn-primary" onclick="importSku()">导入</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:sku:sync">
                    <button id="syncSelect" class="btn btn-primary" onclick="syncSelect()"> 同步选择数据</button>
                    <button id="syncAll" class="btn btn-primary" onclick="syncAll()"> 同步全部数据</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo"><i class="fa fa-search"></i> 检索</a>
            </div>
            <!-- 表格 -->
            <table id="sysCommonSkuTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
<!-- Excel上传弹出框 -->
<div class="modal fade" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
    <form class="form-horizontal" id="fileUploadForm" enctype="multipart/form-data">
        <div class="modal-dialog" style="width: 500px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">上传文件</h4>
                </div>
                <div class="modal-body">
                    <table class="table table-striped table-bordered table-condensed">
                        <tbody>
                        <tr>
                            <td class="active" style="width: 70%;">
                                <input type="file" id="uploadFileName" name="file" accept=".xlsx,.xls"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-info" onclick="downloadTemplate()">下载模板</button>
                    <button type="button" class="btn btn-primary" onclick="uploadFile()">上传</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>