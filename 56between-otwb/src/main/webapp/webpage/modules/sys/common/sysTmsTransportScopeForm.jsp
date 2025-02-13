<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务服务范围管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        $(document).ready(function () {
            if ($("#id").val().length > 0) {
                $('#code').prop('readonly', true);
            } else {
                var $dataSet = ${fns:toJson(fns:getUserDataSet())};
                $("#dataSet").val($dataSet.code);
                $("#dataSetName").val($dataSet.name);
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);
        });

        function doSubmit($table, index) {
            jp.loading();
            var validator = bq.headerSubmitCheck("#inputForm");
            if (!validator.isSuccess) {
                jp.bqWaring(validator.msg);
                return;
            }
            var disabledObjs = bq.openDisabled("#inputForm");
            jp.post("${ctx}/sys/common/tms/transportScope/save", $('#inputForm').serialize(), function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    $table.bootstrapTable('refresh');
                    jp.close(index);
                } else {
                    bq.closeDisabled(disabledObjs);
                    jp.bqError(data.msg);
                }
            });
        }

    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="sysTmsTransportScope" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="delFlag"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tr>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>服务范围编码：</label></td>
            <td class="width-35">
                <form:input path="code" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>服务范围名称：</label></td>
            <td class="width-35">
                <form:input path="name" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
        </tr>
        <tr>
            <td class="width-10 active"><label class="pull-right"><font color="red">*</font>数据套：</label></td>
            <td class="width-15">
                <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                          fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysTmsTransportScope.dataSet}"
                          displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysTmsTransportScope.dataSetName}"
                          fieldLabels="编码|名称" fieldKeys="code|name"
                          searchLabels="编码|名称" searchKeys="code|name"/>
            </td>
            <td class="width-15 active"><label class="pull-right">描述：</label></td>
            <td class="width-15">
                <form:input path="remarks" htmlEscape="false" class="form-control " maxlength="255"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>