<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>日历管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        function doSubmit($table, $topIndex) {
            var validate = bq.headerSubmitCheck("#inputForm");
            if (!validate.isSuccess) {
                jp.bqError(validate.msg);
                return false;
            }
            jp.loading();
            jp.post("${ctx}/bms/basic/calendar/save", $('#inputForm').serialize(), function (data) {
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
            $('#date').datetimepicker({format: "YYYY-MM-DD"});

            if (!$("#id").val()) {
                $("#orgId").val(jp.getCurrentOrg().orgId);
            }
        });
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="bmsCalendarEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <table class="table">
        <tbody>
        <tr>
            <td style="width: 20%"><label class="pull-right asterisk">日期</label></td>
            <td style="width: 80%">
                <div class='input-group date' id='date'>
                    <input type='text' name="date" class="form-control required"/>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
            </td>
        </tr>
        <tr>
            <td style="width: 20%"><label class="pull-right asterisk">类型</label></td>
            <td style="width: 80%">
                <form:select path="type" cssClass="form-control input-sm required">
                    <form:options items="${fns:getDictList('BMS_CALENDAR_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td style="width: 20%"><label class="pull-right">日历系数</label></td>
            <td style="width: 80%">
                <form:input path="coefficient" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
            </td>
        </tr>
        <tr>
            <td><label class="pull-right">备注</label></td>
            <td>
                <form:input path="remarks" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>