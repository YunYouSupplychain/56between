<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>车辆类型管理</title>
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
                    jp.post("${ctx}/sys/common/sms/carType/save", $('#inputForm').serialize(), function (data) {
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
<body class="bg-white">
<form:form id="inputForm" modelAttribute="sysSmsCarType" class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-10 active"><label class="pull-right"><font color="red">*</font>车辆类型编码：</label></td>
            <td class="width-15">
                <form:input path="typeCode" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-10 active"><label class="pull-right">车辆类型名称：</label></td>
            <td class="width-15">
                <form:input path="typeName" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-10 active"><label class="pull-right">自定义1：</label></td>
            <td class="width-15">
                <form:input path="def1" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-10 active"><label class="pull-right"><font color="red">*</font>数据套：</label></td>
            <td class="width-15">
                <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                          fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysSmsCarType.dataSet}"
                          displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysSmsCarType.dataSetName}"
                          fieldLabels="编码|名称" fieldKeys="code|name"
                          searchLabels="编码|名称" searchKeys="code|name"/>
            </td>
        </tr>
        <tr>
            <td class="width-10 active"><label class="pull-right">自定义2：</label></td>
            <td class="width-15">
                <form:input path="def2" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-10 active"><label class="pull-right">自定义3：</label></td>
            <td class="width-15">
                <form:input path="def3" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-10 active"><label class="pull-right">自定义4：</label></td>
            <td class="width-15">
                <form:input path="def4" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-10 active"><label class="pull-right">自定义5：</label></td>
            <td class="width-15">
                <form:input path="def5" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>