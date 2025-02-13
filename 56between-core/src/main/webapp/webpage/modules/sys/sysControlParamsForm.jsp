<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>系统控制参数管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript">
        function doSubmit($table, $topIndex) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            var validate = bq.headerSubmitCheck("#inputForm");
            if (validate.isSuccess) {
                jp.loading();
                jp.post("${ctx}/sys/sysControlParams/save", $('#inputForm').serialize(), function (data) {
                    if (data.success) {
                        $table.bootstrapTable('refresh');
                        jp.success(data.msg);
                        jp.close($topIndex);//关闭dialog
                    } else {
                        jp.error(data.msg);
                    }
                });
            } else {
                jp.bqError(validate.msg);
            }
            return validate.isSuccess
        }

        $(document).ready(function () {
            init();
        });

        function init() {
            if ($("#id").val()) {
                $("#code").prop("readonly", true);
            }
        }
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="sysControlParams" class="form-horizontal">
    <form:hidden path="id"/>
    <table class="table">
        <tbody>
        <tr>
            <td style="width: 15%"><label class="pull-right"><font color="red">*</font>参数代码</label></td>
            <td style="width: 25%">
                <form:input path="code" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
            <td style="width: 15%"><label class="pull-right">参数值</label></td>
            <td style="width: 25%">
                <form:input path="value" htmlEscape="false" class="form-control " maxlength="64"/>
            </td>
        </tr>
        <tr>
            <td style="width: 15%"><label class="pull-right">类别</label></td>
            <td style="width: 25%">
                <form:input path="type" htmlEscape="false" class="form-control " maxlength="64"/>
            </td>
            <td style="width: 15%"><label class="pull-right">机构</label></td>
            <td style="width: 25%">
                <sys:popSelect title="选择机构" url="${ctx}/sys/office/companyData"
                               fieldId="orgId" fieldKeyName="id" fieldName="orgId" fieldValue="${sysControlParams.orgId}"
                               displayFieldId="orgName" displayFieldKeyName="name" displayFieldName="orgName" displayFieldValue="${sysControlParams.orgName}"
                               fieldLabels="机构代码|机构名称" fieldKeys="code|name"
                               searchLabels="机构代码|机构名称" searchKeys="code|name"
                               selectButtonId="orgSBtnId" deleteButtonId="orgDBtnId" cssClass="form-control"/>
            </td>
        </tr>
        <tr>
            <td style="width: 15%"><label class="pull-right">说明</label></td>
            <td colspan="3">
                <form:input path="remarks" htmlEscape="false" class="form-control " maxlength="255"/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>