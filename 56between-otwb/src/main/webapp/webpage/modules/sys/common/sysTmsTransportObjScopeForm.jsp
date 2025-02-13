<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务对象服务范围管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        $(document).ready(function () {
            if (!$("#id").val()) {
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
            jp.post("${ctx}/sys/common/tms/transportObjScope/save", $('#inputForm').serialize(), function (data) {
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
<form:form id="inputForm" modelAttribute="sysTmsTransportObjScopeEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="delFlag"/>
    <form:hidden path="recVer"/>
    <form:hidden path="oldTransportScopeCode"/>
    <form:hidden path="oldTransportObjCode"/>
    <form:hidden path="oldTransportScopeType"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tr>
            <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>业务对象：</label></td>
            <td style="width: 12%;">
                <sys:grid title="业务对象" url="${ctx}/sys/common/tms/transportObj/grid"
                          fieldId="transportObjCode" fieldName="transportObjCode" fieldKeyName="transportObjCode" fieldValue="${sysTmsTransportObjScopeEntity.transportObjCode}"
                          displayFieldId="transportObjName" displayFieldName="transportObjName" displayFieldKeyName="transportObjName" displayFieldValue="${sysTmsTransportObjScopeEntity.transportObjName}"
                          fieldLabels="业务对象编码|业务对象名称" fieldKeys="transportObjCode|transportObjName"
                          searchLabels="业务对象编码|业务对象名称" searchKeys="transportObjCode|transportObjName"
                          queryParams="dataSet" queryParamValues="dataSet"
                          cssClass="form-control required"/>
            </td>
            <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>业务服务范围：</label></td>
            <td style="width: 12%;">
                <sys:grid title="业务服务范围" url="${ctx}/sys/common/tms/transportScope/grid"
                          fieldId="transportScopeCode" fieldName="transportScopeCode" fieldKeyName="code" fieldValue="${sysTmsTransportObjScopeEntity.transportScopeCode}"
                          displayFieldId="transportScopeName" displayFieldName="transportScopeName" displayFieldKeyName="name" displayFieldValue="${sysTmsTransportObjScopeEntity.transportScopeName}"
                          fieldLabels="业务服务范围编码|业务服务范围名称" fieldKeys="code|name"
                          searchLabels="业务服务范围编码|业务服务范围名称" searchKeys="code|name"
                          queryParams="dataSet" queryParamValues="dataSet"
                          cssClass="form-control required"/>
            </td>
            <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>服务范围类型：</label></td>
            <td style="width: 12%;">
                <form:select path="transportScopeType" class="form-control required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('TMS_TRANSPORT_SCOPE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td style="width: 8%;"><label class="pull-right">最大装载重量：</label></td>
            <td style="width: 12%;">
                <form:input path="maxLoadWeight" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
            </td>
            <td style="width: 8%;"><label class="pull-right">最大装载体积：</label></td>
            <td style="width: 12%;">
                <form:input path="maxLoadCubic" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
            </td>
        </tr>
        <tr>
            <td style="width: 8%;"><label class="pull-right">最大金额：</label></td>
            <td style="width: 12%;">
                <form:input path="maxAmount" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 8, 0)"/>
            </td>
            <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>数据套：</label></td>
            <td style="width: 12%;">
                <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                          fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysTmsTransportObjScopeEntity.dataSet}"
                          displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysTmsTransportObjScopeEntity.dataSetName}"
                          fieldLabels="编码|名称" fieldKeys="code|name"
                          searchLabels="编码|名称" searchKeys="code|name"/>
            </td>
            <td style="width: 8%;"><label class="pull-right">备注信息：</label></td>
            <td colspan="5">
                <form:input path="remarks" htmlEscape="false" class="form-control " maxlength="255"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>