<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务对象服务范围管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            if ($("#id").val().length <= 0) {
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
            jp.post("${ctx}/tms/basic/tmTransportObjScope/save", $('#inputForm').serialize(), function (data) {
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
<form:form id="inputForm" modelAttribute="tmTransportObjScopeEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="delFlag"/>
    <form:hidden path="recVer"/>
    <table class="table">
        <tr>
            <td style="width: 15%;"><label class="pull-right"><font color="red">*</font>业务对象</label></td>
            <td style="width: 35%;">
                <sys:grid title="业务对象" url="${ctx}/tms/basic/tmTransportObj/grid"
                          fieldId="transportObjCode" fieldName="transportObjCode" fieldKeyName="transportObjCode" fieldValue="${tmTransportObjScopeEntity.transportObjCode}"
                          displayFieldId="transportObjName" displayFieldName="transportObjName" displayFieldKeyName="transportObjName" displayFieldValue="${tmTransportObjScopeEntity.transportObjName}"
                          fieldLabels="业务对象编码|业务对象名称" fieldKeys="transportObjCode|transportObjName"
                          searchLabels="业务对象编码|业务对象名称" searchKeys="transportObjCode|transportObjName"
                          queryParams="orgId" queryParamValues="orgId"
                          cssClass="form-control required"/>
            </td>
            <td style="width: 15%;"><label class="pull-right"><font color="red">*</font>业务服务范围</label></td>
            <td style="width: 35%;">
                <sys:grid title="业务服务范围" url="${ctx}/tms/basic/tmTransportScope/grid"
                          fieldId="transportScopeCode" fieldName="transportScopeCode" fieldKeyName="code" fieldValue="${tmTransportObjScopeEntity.transportScopeCode}"
                          displayFieldId="transportScopeName" displayFieldName="transportScopeName" displayFieldKeyName="name" displayFieldValue="${tmTransportObjScopeEntity.transportScopeName}"
                          fieldLabels="业务服务范围编码|业务服务范围名称" fieldKeys="code|name"
                          searchLabels="业务服务范围编码|业务服务范围名称" searchKeys="code|name"
                          queryParams="orgId" queryParamValues="orgId"
                          cssClass="form-control required"/>
            </td>
        </tr>
        <tr>
            <td style="width: 15%;"><label class="pull-right"><font color="red">*</font>服务范围类型</label></td>
            <td style="width: 35%;">
                <form:select path="transportScopeType" class="form-control required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('TMS_TRANSPORT_SCOPE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td style="width: 15%;"><label class="pull-right">最大装载重量</label></td>
            <td style="width: 35%;">
                <form:input path="maxLoadWeight" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
            </td>
        </tr>
        <tr>
            <td style="width: 15%;"><label class="pull-right">最大装载体积</label></td>
            <td style="width: 35%;">
                <form:input path="maxLoadCubic" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
            </td>
            <td style="width: 15%;"><label class="pull-right">最大金额</label></td>
            <td style="width: 35%;">
                <form:input path="maxAmount" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 8, 0)"/>
            </td>
        </tr>
        <tr>
            <td style="width: 15%;"><label class="pull-right">备注信息</label></td>
            <td colspan="3">
                <form:input path="remarks" htmlEscape="false" class="form-control " maxlength="255"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>