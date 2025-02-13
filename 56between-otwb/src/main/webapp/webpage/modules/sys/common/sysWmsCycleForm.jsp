<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>循环级别管理</title>
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
                    jp.post("${ctx}/sys/common/wms/cycle/save", $('#inputForm').serialize(), function (data) {
                        if (data.success) {
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog

                        } else {
                            jp.bqError(data.msg);
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

            init();
        });

        function init() {
            if (!$('#id').val()) {
                var $dataSet = ${fns:toJson(fns:getUserDataSet())};
                $('#dataSet').val($dataSet.code);
                $('#dataSetName').val($dataSet.name);
            } else {
                $('#cycleCode').prop('readonly', true);
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);
        }
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="sysWmsCycle" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <table class="table">
        <tbody>
        <tr>
            <td class="width-20"><label class="pull-right"><font color="red">*</font>循环级别编码</label></td>
            <td class="width-80">
                <form:input path="cycleCode" htmlEscape="false" class="form-control required" maxlength="32"/>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right"><font color="red">*</font>循环级别名称</label></td>
            <td class="width-80">
                <form:input path="cycleName" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right"><font color="red">*</font>循环周期</label></td>
            <td class="width-80">
                <form:input path="cycleLife" htmlEscape="false" class="form-control " onkeyup="value=value.replace(/[^\d]/g,'')"/>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right">数据套</label></td>
            <td class="width-80">
                <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                          fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysWmsCycle.dataSet}"
                          displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysWmsCycle.dataSetName}"
                          fieldLabels="编码|名称" fieldKeys="code|name"
                          searchLabels="编码|名称" searchKeys="code|name"/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>