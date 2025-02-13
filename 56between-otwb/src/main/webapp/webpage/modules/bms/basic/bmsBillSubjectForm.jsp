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
            jp.post("${ctx}/bms/basic/bmsBillSubject/save", $('#inputForm').serialize(), function (data) {
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
                $("#orgId").val(jp.getCurrentOrg().orgId);
            } else {
                $("#billSubjectCode").prop("readonly", true);
            }
        });
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="bmsBillSubject" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <table class="table">
        <tbody>
        <tr>
            <td class="width-15"><label class="pull-right asterisk">费用科目代码</label></td>
            <td class="width-35">
                <form:input path="billSubjectCode" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-15"><label class="pull-right asterisk">费用科目名称</label></td>
            <td class="width-35">
                <form:input path="billSubjectName" htmlEscape="false" class="form-control required"/>
            </td>
        </tr>
        <tr>
            <td class="width-15"><label class="pull-right asterisk">费用模块</label></td>
            <td class="width-35">
                <form:select path="billModule" class="form-control required" onchange="billModuleChange()">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('BMS_BILL_MODULE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-15"><label class="pull-right">费用类别</label></td>
            <td class="width-35">
                <form:select path="billCategory" class="form-control">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('BMS_BILL_SUBJECT_CATEGORY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-15"><label class="pull-right">备注信息</label></td>
            <td colspan="3">
                <form:input path="remarks" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>
