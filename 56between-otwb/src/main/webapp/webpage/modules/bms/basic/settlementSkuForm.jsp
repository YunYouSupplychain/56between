<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>结算商品管理</title>
    <meta name="decorator" content="ani"/>
</head>
<body>
<div class="hidden">
    <input type="hidden" id="ownerType" value="OWNER"/>
    <input type="hidden" id="supplierType" value="SUPPLIER"/>
</div>
<form:form id="inputForm" modelAttribute="settlementSkuEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="orgId"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">基础信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="width-8"><label class="pull-right asterisk">货主编码</label></td>
                        <td class="width-12">
                            <sys:popSelect title="选择货主" url="${ctx}/bms/basic/bmsCustomer/pop" cssClass="form-control required"
                                           fieldId="" fieldKeyName="" fieldName="" fieldValue=""
                                           displayFieldId="ownerCode" displayFieldKeyName="ebcuCustomerNo"
                                           displayFieldName="ownerCode" displayFieldValue="${settlementSkuEntity.ownerCode}"
                                           selectButtonId="ownerSBtnId" deleteButtonId="ownerDBtnId"
                                           fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="ownerType"
                                           concatId="ownerName" concatName="ebcuNameCn"/>
                        </td>
                        <td class="width-8"><label class="pull-right asterisk">货主名称</label></td>
                        <td class="width-12">
                            <form:input path="ownerName" htmlEscape="false" class="form-control required" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right asterisk">商品编码</label></td>
                        <td class="width-12">
                            <form:input path="skuCode" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td class="width-8"><label class="pull-right asterisk">商品名称</label></td>
                        <td class="width-12">
                            <form:input path="skuName" htmlEscape="false" class="form-control required"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">品类</label></td>
                        <td class="width-12">
                            <sys:grid title="选择商品分类" url="${ctx}/bms/basic/skuClassification/grid" cssClass="form-control"
                                      fieldId="skuClass" fieldName="skuClass"
                                      fieldKeyName="code" fieldValue="${settlementSkuEntity.skuClass}"
                                      displayFieldId="skuClassName" displayFieldName="skuClassName"
                                      displayFieldKeyName="name" displayFieldValue="${settlementSkuEntity.skuClassName}"
                                      fieldLabels="编码|名称" fieldKeys="code|name"
                                      searchLabels="编码|名称" searchKeys="code|name"
                                      queryParams="orgId" queryParamValues="orgId"/>
                        </td>
                        <td class="width-8"><label class="pull-right">商品课别</label></td>
                        <td class="width-12">
                            <form:select path="skuType" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('sku_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right">商品温层</label></td>
                        <td class="width-12">
                            <form:select path="skuTempLayer" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_TEMPR_CATEGORY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right">规格</label></td>
                        <td class="width-12">
                            <form:input path="skuSpec" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">毛重</label></td>
                        <td class="width-12">
                            <form:input path="grossWeight" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-8"><label class="pull-right">净重</label></td>
                        <td class="width-12">
                            <form:input path="netWeight" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-8"><label class="pull-right">件-换算比例</label></td>
                        <td class="width-12">
                            <form:input path="eaQuantity" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-8"><label class="pull-right">小包装-换算比例</label></td>
                        <td class="width-12">
                            <form:input path="ipQuantity" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">箱-换算比例</label></td>
                        <td class="width-12">
                            <form:input path="csQuantity" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-8"><label class="pull-right">托-换算比例</label></td>
                        <td class="width-12">
                            <form:input path="plQuantity" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-8"><label class="pull-right">大包装-换算比例</label></td>
                        <td class="width-12">
                            <form:input path="otQuantity" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-8"><label class="pull-right">长</label></td>
                        <td class="width-12">
                            <form:input path="length" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">宽</label></td>
                        <td class="width-12">
                            <form:input path="width" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-8"><label class="pull-right">高</label></td>
                        <td class="width-12">
                            <form:input path="height" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-8"><label class="pull-right">体积</label></td>
                        <td class="width-12">
                            <form:input path="volume" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">供应商信息</h3>
            </div>
            <div class="panel-body">
                <a class="btn btn-white"
                   onclick="addRow('#skuSuppliers', skuSupplierRowIdx, skuSupplierTpl, {'lineNo':skuSupplierRowIdx + 1});skuSupplierRowIdx = skuSupplierRowIdx + 1;"
                   title="新增"><i class="fa fa-plus"></i> 新增</a>
                <table class="table">
                    <thead>
                    <tr>
                        <th class="hide"></th>
                        <th width="50">序号</th>
                        <th><font color="red">*</font>供应商编码</th>
                        <th><font color="red">*</font>供应商名称</th>
                        <th width="10">&nbsp;</th>
                    </tr>
                    </thead>
                    <tbody id="skuSuppliers"></tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<script type="text/template" id="skuSupplierTpl">//<!--
<tr id="skuSuppliers{{idx}}">
    <td class="hide">
        <input id="skuSuppliers{{idx}}_id" name="skuSuppliers[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="skuSuppliers{{idx}}_delFlag" name="skuSuppliers[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="skuSuppliers{{idx}}_orgId" name="skuSuppliers[{{idx}}].orgId" type="hidden" value="{{row.orgId}}"/>
        <input id="skuSuppliers{{idx}}_skuId" name="skuSuppliers[{{idx}}].skuId" type="hidden" value="{{row.skuId}}"/>
    </td>
    <td>
        <input id="skuSuppliers{{idx}}_lineNo" name="skuSuppliers[{{idx}}].lineNo" type="text" value="{{row.lineNo}}" class="form-control " readonly/>
    </td>
    <td>
        <sys:popSelect title="选择供应商" url="${ctx}/bms/basic/bmsCustomer/pop" cssClass="form-control required"
                       fieldId="" fieldKeyName="" fieldName="" fieldValue=""
                       displayFieldId="skuSuppliers{{idx}}_supplierCode" displayFieldKeyName="ebcuCustomerNo"
                       displayFieldName="skuSuppliers[{{idx}}].supplierCode" displayFieldValue="{{row.supplierCode}}"
                       selectButtonId="skuSuppliers{{idx}}_supplierSBtnId" deleteButtonId="skuSuppliers{{idx}}_supplierDBtnId"
                       fieldLabels="供应商编码|供应商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                       searchLabels="供应商编码|供应商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                       queryParams="ebcuType" queryParamValues="supplierType"
                       concatId="skuSuppliers{{idx}}_supplierName" concatName="ebcuNameCn"/>
    </td>
    <td>
        <input id="skuSuppliers{{idx}}_supplierName" name="skuSuppliers[{{idx}}].supplierName" type="text" value="{{row.supplierName}}" class="form-control required" readonly/>
    </td>
    <td class="text-center" width="10">
        {{#delBtn}}<span class="close" onclick="delRow(this, '#skuSuppliers{{idx}}')" title="删除">&times;</span>{{/delBtn}}
    </td>
</tr>//-->
</script>
<script type="text/javascript">
    var $table; // 父页面table表格id
    var $topIndex;//弹出窗口的 index
    var skuSupplierRowIdx = 0, skuSupplierTpl = $("#skuSupplierTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $(document).ready(function () {
        if (!$("#id").val()) {
            $("#orgId").val(jp.getCurrentOrg().orgId);
            $("#eaQuantity").val("1");
            $("#ipQuantity").val("1");
            $("#csQuantity").val("1");
            $("#plQuantity").val("1");
            $("#otQuantity").val("1");
        } else {
            $("#ownerSBtnId").prop("disabled", true);
            $("#ownerDBtnId").prop("disabled", true);
            $("#ownerName").prop("readonly", true);
            $("#skuCode").prop("readonly", true);
            $("#skuName").prop("readonly", true);
        }

        var data = ${fns:toJson(settlementSkuEntity.skuSuppliers)};
        for (var i = 0; i < data.length; i++) {
            data[i].lineNo = skuSupplierRowIdx + 1;
            addRow('#skuSuppliers', skuSupplierRowIdx, skuSupplierTpl, data[i]);
            skuSupplierRowIdx = skuSupplierRowIdx + 1;
        }
    });

    function doSubmit($table, $topIndex) {
        var validate = bq.tableValidate("#skuSuppliers");
        if (!validate.isSuccess) {
            jp.bqError(validate.msg);
            return;
        }
        validate = bq.headerSubmitCheck("#inputForm");
        if (!validate.isSuccess) {
            jp.bqError(validate.msg);
            return;
        }
        jp.loading();
        jp.post("${ctx}/bms/basic/settlementSku/save", $('#inputForm').serialize(), function (data) {
            if (data.success) {
                $table.bootstrapTable('refresh');
                jp.success(data.msg);
                jp.close($topIndex);//关闭dialog
            } else {
                jp.bqError(data.msg);
            }
        });
    }

    function addRow(list, idx, tpl, row) {
        $(list).append(Mustache.render(tpl, {
            idx: idx, delBtn: true, row: row
        }));
    }

    function delRow(obj, prefix) {
        var id = $(prefix + "_id");
        var delFlag = $(prefix + "_delFlag");
        if (id.val() == "") {
            $(obj).parent().parent().remove();
        } else if (delFlag.val() == "0") {
            delFlag.val("1");
            $(obj).html("&divide;").attr("title", "撤销删除");
            $(obj).parent().parent().addClass("error");
        } else if (delFlag.val() == "1") {
            delFlag.val("0");
            $(obj).html("&times;").attr("title", "删除");
            $(obj).parent().parent().removeClass("error");
        }
    }
</script>
</body>
</html>