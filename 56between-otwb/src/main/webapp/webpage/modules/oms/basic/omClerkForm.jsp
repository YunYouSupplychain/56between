<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务员管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        function doSubmit($table, $topIndex) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            var validate = bq.headerSubmitCheck("#inputForm");
            if (!validate.isSuccess) {
                jp.bqError(validate.msg);
                return false;
            }
            jp.loading();
            var disabledObjs = bq.openDisabled("#inputForm");
            var params = $('#inputForm').serialize();
            bq.closeDisabled(disabledObjs);
            jp.post("${ctx}/oms/basic/omClerk/save", params, function (data) {
                if (data.success) {
                    $table.bootstrapTable('refresh');
                    jp.success(data.msg);
                    jp.close($topIndex);//关闭dialog
                } else {
                    jp.bqError(data.msg);
                }
            });
        }

        $(document).ready(function () {
            if (!$("#id").val()) {
                $("#orgId").val(jp.getCurrentOrg().orgId);
            }
        });
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="omClerk" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>业务员代码</label></td>
            <td class="width-35">
                <form:input path="clerkCode" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>业务员名称</label></td>
            <td class="width-35">
                <form:input path="clerkName" htmlEscape="false" class="form-control required"/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">联系电话</label></td>
            <td class="width-35">
                <form:input path="phone" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">备注信息</label></td>
            <td class="width-35">
                <form:textarea path="remarks" htmlEscape="false" rows="4" class="form-control "/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>