<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>编号生成规则管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        $(function () {
            if (!$('#id').val()) {
                $('#currentSerialNo').val(0);
            }
        })

        function doSubmit($table, $topIndex) {
            var validate = bq.headerSubmitCheck("#inputForm");
            if (!validate.isSuccess) {
                jp.bqError(validate.msg);
                return false;
            }
            jp.loading();
            var disabledObjs = bq.openDisabled("#inputForm");
            var params = $('#inputForm').serialize();
            bq.closeDisabled(disabledObjs);
            jp.post("${ctx}/sys/ruleGenNo/save", params, function (data) {
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
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="sysRuleGenNo" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="currentSerialNo"/>
    <table class="table">
        <tbody>
        <tr>
            <td class="width-15"><label class="pull-right asterisk">编号类型</label></td>
            <td class="width-35">
                <form:input path="billType" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-15"><label class="pull-right">时间戳</label></td>
            <td class="width-35">
                <form:select path="stamp" class="form-control">
                    <form:option value="" label=""/>
                    <form:option value="yyMMdd" label="yyMMdd"/>
                    <form:option value="yyyyMMdd" label="yyyyMMdd"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-15"><label class="pull-right">规则清空周期</label></td>
            <td class="width-35">
                <form:select path="clearCycle" class="form-control">
                    <form:option value="" label=""/>
                    <form:option value="DAY" label="DAY"/>
                </form:select>
            </td>
            <td class="width-15"><label class="pull-right">前缀</label></td>
            <td class="width-35">
                <form:input path="prefix" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        <tr>
            <td class="width-15"><label class="pull-right asterisk">流水长度</label></td>
            <td class="width-35">
                <form:input path="serialNoMaxLength" type="number" class="form-control required" onkeyup="bq.numberValidator(this, 0, 0)"/>
            </td>
            <td class="width-15"><label class="pull-right">说明</label></td>
            <td class="width-35">
                <form:input path="remarks" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>