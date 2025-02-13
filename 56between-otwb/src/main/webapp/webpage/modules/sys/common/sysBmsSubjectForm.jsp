<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>费用科目管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        function doSubmit($table, $topIndex) {
            var validate = bq.headerSubmitCheck("#inputForm");
            if (!validate.isSuccess) {
                jp.warning(validate.msg);
                return false;
            }
            jp.loading();
            jp.post("${ctx}/sys/common/bms/subject/save", $('#inputForm').serialize(), function (data) {
                if (data.success) {
                    $table.bootstrapTable('refresh');
                    jp.success(data.msg);
                    jp.close($topIndex);//关闭dialog
                } else {
                    jp.bqError(data.msg);
                }
            });
            return true;
        }

        $(document).ready(function () {
            if (!$("#id").val()) {
                var $dataSet = ${fns:toJson(fns:getUserDataSet())};
                $('#dataSet').val($dataSet.code);
                $('#dataSetName').val($dataSet.name);
            } else {
                $("#billSubjectCode").prop("readonly", true);
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);
        });
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="sysBmsSubject" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-8"><label class="pull-right"><font color="red">*</font>费用科目代码：</label></td>
            <td class="width-12">
                <form:input path="billSubjectCode" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-8"><label class="pull-right"><font color="red">*</font>费用科目名称：</label></td>
            <td class="width-12">
                <form:input path="billSubjectName" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-8"><label class="pull-right"><font color="red">*</font>费用模块：</label></td>
            <td class="width-12">
                <form:select path="billModule" class="form-control required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('BMS_BILL_MODULE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-8"><label class="pull-right">费用类别：</label></td>
            <td class="width-12">
                <form:select path="billCategory" class="form-control">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('BMS_BILL_SUBJECT_CATEGORY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-8"><label class="pull-right"><font color="red">*</font>数据套：</label></td>
            <td class="width-12">
                <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                          fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysBmsSubject.dataSet}"
                          displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysBmsSubject.dataSetName}"
                          fieldLabels="编码|名称" fieldKeys="code|name"
                          searchLabels="编码|名称" searchKeys="code|name"/>
            </td>
        </tr>
        <tr>
            <td class="width-8"><label class="pull-right">备注信息：</label></td>
            <td colspan="9">
                <form:input path="remarks" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>