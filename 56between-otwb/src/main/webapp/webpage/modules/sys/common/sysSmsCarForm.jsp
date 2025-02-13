<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>车辆信息管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
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
                    jp.post("${ctx}/sys/common/sms/car/save", $('#inputForm').serialize(), function (data) {
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

            if (!$('#id').val()) {
                var $dataSet = ${fns:toJson(fns:getUserDataSet())};
                $('#dataSet').val($dataSet.code);
                $('#dataSetName').val($dataSet.name);
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);
        });
    </script>
</head>
<div class="hide">
    <input id="carrierType" type="hidden" value="CARRIER"/>
</div>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="sysSmsCar" class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-10 active"><label class="pull-right">编码：</label></td>
            <td class="width-15">
                <form:input path="code" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-10 active"><label class="pull-right">车号：</label></td>
            <td class="width-15">
                <form:input path="carNo" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-10 active"><label class="pull-right"><font color="red">*</font>承运商编码：</label></td>
            <td class="width-15">
                <sys:grid title="选择承运商" url="${ctx}/sys/common/sms/customer/grid" cssClass="form-control required"
                          fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="code" fieldValue="${sysSmsCar.carrierCode}"
                          displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="nameCn" displayFieldValue="${sysSmsCar.carrierName}"
                          fieldLabels="承运商编码|承运商名称" fieldKeys="code|nameCn"
                          searchLabels="承运商编码|承运商名称" searchKeys="code|nameCn"
                          queryParams="dataSet|customerType" queryParamValues="dataSet|carrierType"/>
            </td>
            <td class="width-10 active"><label class="pull-right"><font color="red">*</font>数据套：</label></td>
            <td class="width-15">
                <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                          fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysSmsCar.dataSet}"
                          displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysSmsCar.dataSetName}"
                          fieldLabels="编码|名称" fieldKeys="code|name"
                          searchLabels="编码|名称" searchKeys="code|name"/>
            </td>
        </tr>
        <tr>
            <td class="width-10 active"><label class="pull-right">车辆类型：</label></td>
            <td class="width-15">
                <sys:grid title="选择车辆类型" url="${ctx}/sys/common/sms/carType/grid" cssClass="form-control required"
                          fieldId="carType" fieldName="carType" fieldKeyName="typeCode" fieldValue="${sysSmsCar.carType}"
                          displayFieldId="carTypeName" displayFieldName="carTypeName" displayFieldKeyName="typeName" displayFieldValue="${sysSmsCar.carTypeName}"
                          fieldLabels="类型编码|类型名称" fieldKeys="typeCode|typeName"
                          searchLabels="类型编码|类型名称" searchKeys="typeCode|typeName"
                          queryParams="dataSet" queryParamValues="dataSet"/>
            </td>
            <td class="width-10 active"><label class="pull-right">发动机号：</label></td>
            <td class="width-15">
                <form:input path="engineNo" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-10 active"><label class="pull-right">承载量：</label></td>
            <td class="width-15">
                <form:input path="carryingCapacity" htmlEscape="false" onkeyup="this.value=this.value.replace(/[^0-9-]+/,'');" class="form-control "/>
            </td>
            <td class="width-10 active"><label class="pull-right">保险证号：</label></td>
            <td class="width-15">
                <form:input path="safeCode" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-10 active"><label class="pull-right">司机：</label></td>
            <td class="width-15">
                <form:input path="diver" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-10 active"><label class="pull-right">手机号码：</label></td>
            <td class="width-15">
                <form:input path="phone" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-10 active"><label class="pull-right">承载体积：</label></td>
            <td class="width-15">
                <form:input path="bearingVolume" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-10 active"></td>
            <td class="width-15"></td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>