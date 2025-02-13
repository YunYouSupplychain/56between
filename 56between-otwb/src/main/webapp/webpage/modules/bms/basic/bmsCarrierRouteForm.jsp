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
            var disabledObj = bq.openDisabled("#inputForm");
            jp.post("${ctx}/bms/basic/bmsCarrierRoute/save", $('#inputForm').serialize(), function (data) {
                if (data.success) {
                    $table.bootstrapTable('refresh');
                    jp.success(data.msg);
                    jp.close($topIndex);//关闭dialog
                } else {
                    jp.bqError(data.msg);
                }
            });
            bq.closeDisabled(disabledObj);
            return true;
        }

        $(document).ready(function () {
            if (!$("#id").val()) {
                $('#orgId').val(jp.getCurrentOrg().orgId);
                $('#orgName').val(jp.getCurrentOrg().orgName);
            }
        });

    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="bmsCarrierRouteEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="orgId"/>
    <table class="table">
        <tbody>
        <tr>
            <td class="width-15"><label class="pull-right">路由编码</label></td>
            <td class="width-35">
                <form:input path="routeCode" htmlEscape="false" class="form-control" readonly="true"/>
            </td>
            <td class="width-15"><label class="pull-right asterisk">路由名称</label></td>
            <td class="width-35">
                <form:input path="routeName" htmlEscape="false" class="form-control required"/>
            </td>
        </tr>
        <tr>
            <td class="width-15"><label class="pull-right asterisk">承运商</label></td>
            <td class="width-35">
                <input type="hidden" id="carrierType" value="CARRIER"/>
                <sys:grid title="承运商" url="${ctx}/bms/basic/bmsCustomer/pop" cssClass="form-control required"
                          fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo" fieldValue="${bmsCarrierRouteEntity.carrierCode}"
                          displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${bmsCarrierRouteEntity.carrierName}"
                          fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                          searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                          queryParams="ebcuType|orgId" queryParamValues="carrierType|orgId"/>
            </td>
            <td class="width-15"><label class="pull-right">标准里程(km)</label></td>
            <td class="width-35">
                <form:input path="mileage" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 8, 0)"/>
            </td>
        </tr>
        <tr>
            <td class="width-15"><label class="pull-right">标准时效</label></td>
            <td class="width-35">
                <form:input path="timeliness" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 8, 0)"/>
            </td>
            <td class="width-15"><label class="pull-right">机构</label></td>
            <td class="width-35">
                <form:input path="orgName" htmlEscape="false" class="form-control" readonly="true"/>
            </td>
        </tr>
        <tr>
            <td class="width-15"><label class="pull-right asterisk">起始地</label></td>
            <td class="width-35">
                <sys:area id="startAreaId" name="startAreaId" value="${bmsCarrierRouteEntity.startAreaId}"
                                labelName="startAreaName" labelValue="${bmsCarrierRouteEntity.startAreaName}"
                                allowSearch="true" showFullName="true" cssClass="form-control required"/>
            </td>
            <td class="width-15"><label class="pull-right asterisk">目的地</label></td>
            <td class="width-35">
                <sys:area id="endAreaId" name="endAreaId" value="${bmsCarrierRouteEntity.endAreaId}"
                                labelName="endAreaName" labelValue="${bmsCarrierRouteEntity.endAreaName}"
                                allowSearch="true" showFullName="true" cssClass="form-control required"/>
            </td>

        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>