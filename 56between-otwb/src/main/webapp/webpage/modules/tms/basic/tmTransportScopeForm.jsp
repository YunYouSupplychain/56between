<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务服务范围管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            if ($("#id").val().length > 0) {
                $('#code').prop('readonly', true);
            } else {
                $("#orgId").val(tmOrg.id);
            }
        });

        function doSubmit($table, index) {
            jp.loading();
            var validator = bq.headerSubmitCheck("#inputForm");
            if (!validator.isSuccess) {
                jp.bqWaring(validator.msg);
                return;
            }
            var disabledObjs = bq.openDisabled("#inputForm");
            jp.post("${ctx}/tms/basic/tmTransportScope/save", $('#inputForm').serialize(), function (data) {
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
<body class="bg-white">
<form:form id="inputForm" modelAttribute="tmTransportScope" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="delFlag"/>
    <form:hidden path="recVer"/>
    <table class="table">
        <tr>
            <td class="width-20"><label class="pull-right"><font color="red">*</font>服务范围编码</label></td>
            <td class="width-80">
                <form:input path="code" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right"><font color="red">*</font>服务范围名称</label></td>
            <td class="width-80">
                <form:input path="name" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right">描述</label></td>
            <td class="width-80">
                <form:input path="remarks" htmlEscape="false" class="form-control " maxlength="255"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>