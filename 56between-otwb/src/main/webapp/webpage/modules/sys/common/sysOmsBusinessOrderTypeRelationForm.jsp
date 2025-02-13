<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>订单类型关联管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var smsAsnTypeList = ${fns:toJson(fns:getDictList('SMS_ASN_ORDER_TYPE'))};
        var smsSoTypeList = ${fns:toJson(fns:getDictList('SMS_BUSINESS_ORDER_TYPE'))};
        var wmsAsnTypeList = ${fns:toJson(fns:getDictList('SYS_WM_ASN_TYPE'))};
        var wmsSoTypeList = ${fns:toJson(fns:getDictList('SYS_WM_SO_TYPE'))};
        var tmsToTypeList = ${fns:toJson(fns:getDictList('TMS_TRANSPORT_ORDER_TYPE'))};

        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
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
            validateForm = $("#inputForm").validate({
                submitHandler: function (form) {
                    var validate = bq.headerSubmitCheck('#inputForm');
                    if (!validate.isSuccess) {
                        jp.alert(validate.msg);
                        return;
                    }
                    jp.post("${ctx}/sys/common/oms/businessOrderTypeRelation/save", $('#inputForm').serialize(), function (data) {
                        if (data.success) {
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog
                        } else {
                            jp.error(data.msg);
                        }
                    });
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

            if (!$('#id').val()) {
                var $dataSet = ${fns:toJson(fns:getUserDataSet())};
                $('#dataSet').val($dataSet.code);
                $('#dataSetName').val($dataSet.name);
            } else {
                resetPushOrderType();
                $('#pushOrderType').val('${sysOmsBusinessOrderTypeRelation.pushOrderType}');
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);
        });

        function initPushOrderType(orderType, pushSystem) {
            $('#pushOrderType').empty();
            $('#pushOrderType').append('<option></option>');
            if (orderType == "1" && pushSystem == "SMS") {
                $.map(smsSoTypeList, function (row) {
                    $('#pushOrderType').append('<option value="' + row.value + '">' + row.label + '</option>');
                });
            } else if (orderType == "1" && pushSystem == "WMS") {
                $.map(wmsSoTypeList, function (row) {
                    $('#pushOrderType').append('<option value="' + row.value + '">' + row.label + '</option>');
                });
            } else if (orderType == "2" && pushSystem == "SMS") {
                $.map(smsAsnTypeList, function (row) {
                    $('#pushOrderType').append('<option value="' + row.value + '">' + row.label + '</option>');
                });
            } else if (orderType == "2" && pushSystem == "WMS") {
                $.map(wmsAsnTypeList, function (row) {
                    $('#pushOrderType').append('<option value="' + row.value + '">' + row.label + '</option>');
                });
            } else if (orderType == "3" && pushSystem == "TMS") {
                $.map(tmsToTypeList, function (row) {
                    $('#pushOrderType').append('<option value="' + row.value + '">' + row.label + '</option>');
                });
            }
        }

        function resetPushOrderType() {
            initPushOrderType($('#orderType').val(), $('#pushSystem').val());
        }
    </script>
</head>
<body class="bg-white">
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-md-12">
            <form:form id="inputForm" modelAttribute="sysOmsBusinessOrderTypeRelation" class="form form-horizontal">
                <form:hidden path="id"/>
                <form:hidden path="oldBusinessOrderType"/>
                <form:hidden path="oldOrderType"/>
                <form:hidden path="oldPushSystem"/>
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="width-20"><label class="pull-right"><font color="red">*</font>业务订单类型</label></td>
                        <td class="width-80">
                            <form:select path="businessOrderType" class="form-control required" onchange="priceTypeChange()">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_BUSINESS_ORDER_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-20"><label class="pull-right"><font color="red">*</font>作业任务类型</label></td>
                        <td class="width-80">
                            <form:select path="orderType" class="form-control required" onchange="resetPushOrderType()">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_TASK_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-20"><label class="pull-right"><font color="red">*</font>下发系统</label></td>
                        <td class="width-80">
                            <form:select path="pushSystem" class="form-control required" onchange="resetPushOrderType()">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_PUSH_SYSTEM')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-20"><label class="pull-right"><font color="red">*</font>下发订单类型</label></td>
                        <td class="width-80">
                            <select id="pushOrderType" name="pushOrderType" class="form-control required"></select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-20"><label class="pull-right"><font color="red">*</font>数据套</label></td>
                        <td class="width-80">
                            <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                                      fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysOmsBusinessOrderTypeRelation.dataSet}"
                                      displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysOmsBusinessOrderTypeRelation.dataSetName}"
                                      fieldLabels="编码|名称" fieldKeys="code|name"
                                      searchLabels="编码|名称" searchKeys="code|name"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form:form>
        </div>
    </div>
</div>
</body>
</html>