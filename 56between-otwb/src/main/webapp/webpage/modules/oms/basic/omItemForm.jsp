<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>商品信息管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var validateForm;
        var $table;
        var $topIndex;

        function doSubmit(table, index) {
            if (validateForm.form()) {
                $table = table;
                $topIndex = index;
                jp.loading();
                $("#inputForm").submit();
                return true;
            }
            return false;
        }

        $(document).ready(function () {
            init();
        });

        function init() {
            if ($('#id').val()) {
                $('#skuCode').prop('readonly', true);
            } else {
                $('#orgId').val(jp.getCurrentOrg().orgId);
            }
            validateForm = $("#inputForm").validate({
                submitHandler: function (form) {
                    jp.post("${ctx}/oms/basic/omItem/save", $('#inputForm').serialize(), function (data) {
                        if (data.success) {
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog
                        } else {
                            jp.error(data.msg);
                        }
                    })
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        }

        function addRow(list, idx, tpl, row) {
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            $(list + idx).find("select").each(function () {
                $(this).val($(this).attr("data-value"));
            });
            $(list + idx).find(".form_datetime").each(function () {
                $(this).datetimepicker({
                    format: "YYYY-MM-DD HH:mm:ss"
                });
            });
            if (!row) {
                $(list + idx + '_orgId').val(jp.getCurrentOrg().orgId);
            } else {
                if ('#omItemBarcodeList' === list) {
                    $(list + idx + '_isDefault').prop('checked', row.isDefault === 'Y').val(row.isDefault);
                }
            }
        }

        function delRow(obj, prefix) {
            var id = $(prefix + "_id");
            var delFlag = $(prefix + "_delFlag");
            if (id.val() === "") {
                $(obj).parent().parent().remove();
            } else if (delFlag.val() === "0") {
                delFlag.val("1");
                $(obj).html("&divide;").attr("title", "撤销删除");
                $(obj).parent().parent().addClass("error");
            } else if (delFlag.val() === "1") {
                delFlag.val("0");
                $(obj).html("&times;").attr("title", "删除");
                $(obj).parent().parent().removeClass("error");
            }
        }

        function isDefaultChange(idx) {
            $('#omItemBarcodeList').find("input[type='checkbox']").each(function () {
                if ($(this).prop('name').indexOf('isDefault') !== -1) {
                    $(this).prop('checked', false).val('N');
                }
            });
            $('#omItemBarcodeList' + idx + '_isDefault').prop('checked', true).val('Y');
        }
    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="omItemEntity" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">基础信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">货主</label></td>
                        <td class="width-15">
                            <input id="ownerType" value="OWNER" type="hidden">
                            <sys:grid title="选择货主" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control required"
                                      fieldId="ownerCode" fieldName="ownerCode"
                                      fieldKeyName="ebcuCustomerNo" fieldValue="${omItemEntity.ownerCode}"
                                      displayFieldId="ownerName" displayFieldName="ownerName"
                                      displayFieldKeyName="ebcuNameCn" displayFieldValue="${omItemEntity.ownerName}"
                                      fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                      searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                      queryParams="ebcuType|orgId" queryParamValues="ownerType|orgId"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">商品编码</label></td>
                        <td class="width-15">
                            <form:input path="skuCode" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">商品名称</label></td>
                        <td class="width-15">
                            <form:input path="skuName" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right">商品简称</label></td>
                        <td class="width-15">
                            <form:input path="shortName" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">包装规格</label></td>
                        <td class="width-15">
                            <sys:grid title="选择包装" url="${ctx}/oms/basic/omPackage/popData" cssClass="form-control required"
                                      fieldId="packCode" fieldName="packCode"
                                      fieldKeyName="cdpaCode" fieldValue="${omItemEntity.packCode}"
                                      displayFieldId="packDesc" displayFieldName="packDesc"
                                      displayFieldKeyName="cdpaFormat" displayFieldValue="${omItemEntity.packDesc}"
                                      fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                      searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat"
                                      queryParams="orgId" queryParamValues="orgId"/>
                        </td>
                        <td class="width-10"><label class="pull-right">规格</label></td>
                        <td class="width-15">
                            <form:input path="spec" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">品类</label></td>
                        <td class="width-15">
                            <sys:grid title="选择商品分类" url="${ctx}/oms/basic/skuClassification/grid" cssClass="form-control"
                                      fieldId="skuClass" fieldName="skuClass"
                                      fieldKeyName="code" fieldValue="${omItemEntity.skuClass}"
                                      displayFieldId="skuClassName" displayFieldName="skuClassName"
                                      displayFieldKeyName="name" displayFieldValue="${omItemEntity.skuClassName}"
                                      fieldLabels="编码|名称" fieldKeys="code|name"
                                      searchLabels="编码|名称" searchKeys="code|name"
                                      queryParams="orgId" queryParamValues="orgId"/>
                        </td>
                        <td class="width-10"><label class="pull-right">温层</label></td>
                        <td class="width-15">
                            <form:select path="skuTempLayer" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('temperature_layer')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">客户商品编码</label></td>
                        <td class="width-15">
                            <form:input path="customerNo" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">统一码</label></td>
                        <td class="width-15">
                            <form:input path="unicode" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">课别</label></td>
                        <td class="width-15">
                            <form:select path="skuType" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('sku_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">基本单位</label></td>
                        <td class="width-15">
                            <form:select path="unit" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_WARE_UNIT')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">毛重</label></td>
                        <td class="width-15">
                            <form:input path="grossWeight" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d.]/g,'')"/>
                        </td>
                        <td class="width-10"><label class="pull-right">净重</label></td>
                        <td class="width-15">
                            <form:input path="netWeight" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d.]/g,'')"/>
                        </td>
                        <td class="width-10"><label class="pull-right">体积</label></td>
                        <td class="width-15">
                            <form:input path="volume" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d.]/g,'')"/>
                        </td>
                        <td class="width-10"><label class="pull-right">皮重</label></td>
                        <td class="width-15">
                            <form:input path="tare" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d.]/g,'')"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">长</label></td>
                        <td class="width-15">
                            <form:input path="length" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d.]/g,'')"/>
                        </td>
                        <td class="width-10"><label class="pull-right">宽</label></td>
                        <td class="width-15">
                            <form:input path="width" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d.]/g,'')"/>
                        </td>
                        <td class="width-10"><label class="pull-right">高</label></td>
                        <td class="width-15">
                            <form:input path="height" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d.]/g,'')"/>
                        </td>
                        <td class="width-10"><label class="pull-right">备注信息</label></td>
                        <td class="width-15">
                            <form:textarea path="remarks" htmlEscape="false" rows="1" class="form-control" cssStyle="resize: none;"/>
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
                <h3 class="panel-title">条码信息</h3>
            </div>
            <div class="panel-body">
                <div id="tab-1">
                    <div style="padding-bottom: 5px;">
                        <a class="btn btn-primary"
                           onclick="addRow('#omItemBarcodeList', omItemBarcodeRowIdx, omItemBarcodeTpl);omItemBarcodeRowIdx = omItemBarcodeRowIdx + 1;">新增</a>
                    </div>
                    <table class="table">
                        <thead>
                        <tr>
                            <th class="hide"></th>
                            <th width="100">行号</th>
                            <th class="asterisk">条码</th>
                            <th width="80">是否默认</th>
                            <th width="10">&nbsp;</th>
                        </tr>
                        </thead>
                        <tbody id="omItemBarcodeList"></tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</form:form>
<script type="text/template" id="omItemBarcodeTpl">//<!--
<tr id="omItemBarcodeList{{idx}}">
    <td class="hide">
        <input id="omItemBarcodeList{{idx}}_id" name="omItemBarcodeList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="omItemBarcodeList{{idx}}_delFlag" name="omItemBarcodeList[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="omItemBarcodeList{{idx}}_orgId" name="omItemBarcodeList[{{idx}}].orgId" type="hidden" value="{{row.orgId}}"/>
    </td>
    <td width="100">
        <input id="omItemBarcodeList{{idx}}_lineNo" name="omItemBarcodeList[{{idx}}].lineNo" type="text" value="{{row.lineNo}}" class="form-control"/>
    </td>
    <td>
        <input id="omItemBarcodeList{{idx}}_barcode" name="omItemBarcodeList[{{idx}}].barcode" type="text" value="{{row.barcode}}" class="form-control required"/>
    </td>
    <td width="80">
        <input id="omItemBarcodeList{{idx}}_isDefault" name="omItemBarcodeList[{{idx}}].isDefault" type="checkbox" class="myCheckbox" onclick="isDefaultChange({{idx}})"/>
    </td>
    <td class="text-center" width="10">
        {{#delBtn}}<span class="close" onclick="delRow(this, '#omItemBarcodeList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
    </td>
</tr>//-->
</script>
<script type="text/javascript">
    var omItemBarcodeRowIdx = 0,
        omItemBarcodeTpl = $("#omItemBarcodeTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $(document).ready(function () {
        var data = ${fns:toJson(omItemEntity.omItemBarcodeList)};
        for (var i = 0; i < data.length; i++) {
            addRow('#omItemBarcodeList', omItemBarcodeRowIdx, omItemBarcodeTpl, data[i]);
            omItemBarcodeRowIdx = omItemBarcodeRowIdx + 1;
        }
    });
</script>
</body>
</html>