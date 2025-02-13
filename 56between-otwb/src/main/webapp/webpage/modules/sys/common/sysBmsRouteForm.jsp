<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>路由管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        function doSubmit($table, $topIndex) {
            jp.loading();
            var validate = bq.headerSubmitCheck("#inputForm");
            if (!validate.isSuccess) {
                jp.bqError(validate.msg);
                return false;
            }
            var disabledObjs = bq.openDisabled("#inputForm");
            var params = $('#inputForm').serialize();
            bq.closeDisabled(disabledObjs);
            jp.post("${ctx}/sys/common/bms/route/save", params, function (data) {
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
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);
        });

    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="sysBmsRoute" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="startAreaCode"/>
    <form:hidden path="endAreaCode"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-8"><label class="pull-right">路由编码：</label></td>
            <td class="width-12">
                <form:input path="routeCode" htmlEscape="false" class="form-control" readonly="true"/>
            </td>
            <td class="width-8"><label class="pull-right"><font color="red">*</font>路由名称：</label></td>
            <td class="width-12">
                <form:input path="routeName" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-8"><label class="pull-right">标准里程(km)：</label></td>
            <td class="width-12">
                <form:input path="mileage" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
            </td>
            <td class="width-8"><label class="pull-right">标准时效：</label></td>
            <td class="width-12">
                <form:input path="timeliness" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
            </td>
            <td class="width-8"><label class="pull-right"><font color="red">*</font>数据套：</label></td>
            <td class="width-12">
                <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                          fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysBmsRoute.dataSet}"
                          displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysBmsRoute.dataSetName}"
                          fieldLabels="编码|名称" fieldKeys="code|name"
                          searchLabels="编码|名称" searchKeys="code|name"/>
            </td>
        </tr>
        <tr>
            <td class="width-8"><label class="pull-right"><font color="red">*</font>起始地：</label></td>
            <td class="width-12">
                <sys:area id="startAreaId" name="startAreaId" value="${sysBmsRoute.startAreaId}"
                          labelName="startAreaName" labelValue="${sysBmsRoute.startAreaName}"
                          cssClass="form-control required" allowSearch="true" showFullName="true"/>
            </td>
            <td class="width-8"><label class="pull-right"><font color="red">*</font>目的地：</label></td>
            <td class="width-12">
                <sys:area id="endAreaId" name="endAreaId" value="${sysBmsRoute.endAreaId}"
                          labelName="endAreaName" labelValue="${sysBmsRoute.endAreaName}"
                          cssClass="form-control required" allowSearch="true" showFullName="true"/>
            </td>
            <td class="width-8"></td>
            <td class="width-12"></td>
            <td class="width-8"></td>
            <td class="width-12"></td>
            <td class="width-8"></td>
            <td class="width-12"></td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>