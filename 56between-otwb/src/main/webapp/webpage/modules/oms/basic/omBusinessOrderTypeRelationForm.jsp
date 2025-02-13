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
                        jp.warning(validate.msg);
                        return;
                    }
                    jp.post("${ctx}/oms/basic/omBusinessOrderTypeRelation/save", $('#inputForm').serialize(), function (data) {
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
                $('#orgId').val(jp.getCurrentOrg().orgId);
            } else {
                resetPushOrderType();
                $('#pushOrderType').val('${omBusinessOrderTypeRelation.pushOrderType}');
            }
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
<form:form id="inputForm" modelAttribute="omBusinessOrderTypeRelation" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <sys:message content="${message}"/>
    <table class="table">
        <tbody>
        <tr>
            <td style="width: 30%"><label class="pull-right asterisk">业务订单类型</label></td>
            <td style="width: 70%">
                <form:select path="businessOrderType" class="form-control required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('OMS_BUSINESS_ORDER_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td><label class="pull-right asterisk">作业任务类型</label></td>
            <td>
                <form:select path="orderType" class="form-control required" onchange="resetPushOrderType()">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('OMS_TASK_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td><label class="pull-right asterisk">下发系统</label></td>
            <td>
                <form:select path="pushSystem" class="form-control required" onchange="resetPushOrderType()">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('OMS_PUSH_SYSTEM')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td><label class="pull-right asterisk">下发订单类型</label></td>
            <td>
                <select id="pushOrderType" name="pushOrderType" class="form-control required"></select>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>