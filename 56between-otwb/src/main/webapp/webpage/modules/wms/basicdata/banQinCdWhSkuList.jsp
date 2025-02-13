<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>商品管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinCdWhSkuList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">商品列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinCdWhSkuEntity">
                            <input id="orgId" name="orgId" type="hidden">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="货主">货主</label>
                                    </td>
                                    <td class="width-15">
                                        <input id="ebcuType" value="OWNER" type="hidden">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                       fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="ebcuType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="商品编码">商品编码</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="skuCode" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="商品名称">商品名称</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="skuName" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="basicdata:banQinCdWhSku:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basicdata:banQinCdWhSku:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basicdata:banQinCdWhSku:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basicdata:banQinCdWhSku:import">
                    <button id="import" class="btn btn-info" onclick="importSku()"><i class="fa fa-file-excel-o"></i>导入</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basicdata:banQinCdWhSku:export">
                    <button id="export" class="btn btn-info" onclick="exportData()"><i class="fa fa-file-excel-o"></i>导出</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinCdWhSkuTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
<!-- Excel上传弹出框 -->
<div class="modal fade" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
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
                                <input type="file" id="uploadFileName" name="file"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="downloadTemplate()">下载导入模板</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="uploadFile()">上传</button>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>