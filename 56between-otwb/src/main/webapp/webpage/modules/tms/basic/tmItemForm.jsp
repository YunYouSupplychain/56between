<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>商品信息管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@ include file="tmItemForm.js" %>
</head>
<body>
<form:form id="inputForm" modelAttribute="tmItemEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="skuClass"/>
    <form:hidden path="orgId"/>
    <form:hidden path="delFlag"/>
    <form:hidden path="recVer"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">基本信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tr>
                        <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>客户</label></td>
                        <td style="width: 12%;">
                            <input type="hidden" id="transportObjType" value="OWNER" />
                            <sys:grid title="选择客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="transportObjCode" fieldValue="${tmItemEntity.ownerCode}"
                                      displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="transportObjName" displayFieldValue="${tmItemEntity.ownerName}"
                                      fieldLabels="客户编码|客户名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="客户编码|客户名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="transportObjType|orgId"
                                      cssClass="form-control required"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>商品编码</label></td>
                        <td style="width: 12%;">
                            <form:input path="skuCode" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>商品名称</label></td>
                        <td style="width: 12%;">
                            <form:input path="skuName" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">商品类型</label></td>
                        <td style="width: 12%;">
                            <form:select path="skuType" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_SKU_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">商品型号</label></td>
                        <td style="width: 12%;">
                            <form:select path="skuModel" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_SKU_MODEL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">毛重</label></td>
                        <td style="width: 12%;">
                            <form:input path="grossweight" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">净重</label></td>
                        <td style="width: 12%;">
                            <form:input path="netweight" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">体积</label></td>
                        <td style="width: 12%;">
                            <form:input path="cubic" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">长</label></td>
                        <td style="width: 12%;">
                            <form:input path="length" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">宽</label></td>
                        <td style="width: 12%;">
                            <form:input path="width" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">高</label></td>
                        <td style="width: 12%;">
                            <form:input path="height" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">单价</label></td>
                        <td style="width: 12%;">
                            <form:input path="price" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">币种</label></td>
                        <td style="width: 12%;">
                            <form:select path="currency" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_CURRENCY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">单位</label></td>
                        <td style="width: 12%;">
                            <form:select path="unit" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_UNIT')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">备注</label></td>
                        <td style="width: 12%;" colspan="3">
                            <form:input path="remarks" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">自定义1</label></td>
                        <td style="width: 12%;">
                            <form:input path="def1" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">自定义2</label></td>
                        <td style="width: 12%;">
                            <form:input path="def2" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">自定义3</label></td>
                        <td style="width: 12%;">
                            <form:input path="def3" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">自定义4</label></td>
                        <td style="width: 12%;">
                            <form:input path="def4" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</form:form>
<div class="wrapper wrapper-content-my">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">商品条码</h3>
        </div>
        <div class="panel-body">
            <div id="toolbar">
                <shiro:hasPermission name="basic:tmItemBarcode:add">
                    <a id="skuBarcode_add" class="btn btn-primary" disabled onclick="addSkuBarcode()"> 添加</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmItemBarcode:edit">
                    <a id="skuBarcode_edit" class="btn btn-primary" disabled onclick="editSkuBarcode()"> 修改</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmItemBarcode:del">
                    <a id="skuBarcode_remove" class="btn btn-danger" disabled onclick="delSkuBarcode()"> 删除</a>
                </shiro:hasPermission>
            </div>
            <!-- 表格 -->
            <table id="tmItemBarcodeTable" class="text-nowrap" data-toolbar="#toolbar"></table>
            <div id="tmItemBarcodeModal" class="modal fade" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel" aria-hidden="true">
                <form id="tmItemBarcodeForm" class="form-horizontal">
                    <input type="hidden" id="skuBarcode_id" name="id">
                    <input type="hidden" id="skuBarcode_ownerCode" name="ownerCode">
                    <input type="hidden" id="skuBarcode_skuCode" name="skuCode">
                    <input type="hidden" id="skuBarcode_orgId" name="orgId">
                    <div class="modal-dialog" style="width:1200px; height: 500px">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">商品条码</h4>
                            </div>
                            <div class="modal-body">
                                <table class="table table-striped table-bordered table-condensed">
                                    <tr>
                                        <td style="width: 13%;"><label class="pull-right"><font color="red">*</font>条码</label></td>
                                        <td style="width: 20%;">
                                            <input id="skuBarcode_barcode" name="barcode" class="form-control required" maxlength="64"/>
                                        </td>
                                        <td style="width: 13%;"><label class="pull-right">是否默认</label></td>
                                        <td style="width: 20%;">
                                            <input type="checkbox" id="skuBarcode_isDefault" name="isDefault" class="form-control " onclick="isDefaultClick(this)"/>
                                        </td>
                                        <td style="width: 13%;"></td>
                                        <td style="width: 20%;"></td>
                                    </tr>
                                </table>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" onclick="saveSkuBarcode()">确认 </button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

</body>
</html>