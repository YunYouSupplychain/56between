<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务服务范围管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            jp.loading();
            var validate = bq.headerSubmitCheck("#inputForm");
            if (!validate.isSuccess) {
                jp.alert(validate.msg);
                return false;
            }
            jp.post("${ctx}/oms/basic/omBusinessServiceScope/save", $('#inputForm').serialize(), function (data) {
                if (data.success) {
                    table.bootstrapTable('refresh');
                    jp.success(data.msg);
                    jp.close(index);//关闭dialog
                } else {
                    jp.alert(data.msg);
                }
            });
            return true;
        }

        $(document).ready(function () {
            if (!$("#id").val()) {
                $("#orgId").val(jp.getCurrentOrg().orgId);
                $("#orgName").val(jp.getCurrentOrg().orgName);
            }
        });
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="omBusinessServiceScopeEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-15 active"><label class="pull-right">编码</label></td>
            <td class="width-35">
                <form:input path="groupCode" htmlEscape="false" class="form-control" readonly="true"/>
            </td>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>名称</label></td>
            <td class="width-35">
                <form:input path="groupName" htmlEscape="false" class="form-control required"/>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">机构</label></td>
            <td class="width-35">
                <form:input path="orgName" htmlEscape="false" class="form-control " readonly="true"/>
            </td>
            <td class="width-15 active"><label class="pull-right">备注信息</label></td>
            <td class="width-35">
                <form:input path="remarks" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>