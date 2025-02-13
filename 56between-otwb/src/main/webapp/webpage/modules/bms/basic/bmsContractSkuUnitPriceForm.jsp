<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>合同商品价格管理</title>
    <meta name="decorator" content="ani"/>
</head>
<body>
<form:form id="inputForm" modelAttribute="bmsContractSkuPriceEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="orgId"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-15"><label class="pull-right asterisk">系统合同编号</label></td>
                <td class="width-35">
                    <sys:grid title="选择合同" url="${ctx}/bms/basic/bmsContract/data" cssClass="form-control required"
                              fieldId="" fieldName="" fieldKeyName=""
                              displayFieldId="sysContractNo" displayFieldName="sysContractNo"
                              displayFieldKeyName="sysContractNo" displayFieldValue="${bmsContractSkuPriceEntity.sysContractNo}"
                              fieldLabels="结算对象编码|结算对象名称|系统合同编号|客户合同编号"
                              fieldKeys="settleObjectCode|settleObjectName|sysContractNo|contractNo"
                              searchLabels="结算对象编码|结算对象名称|系统合同编号|客户合同编号"
                              searchKeys="settleObjectCode|settleObjectName|sysContractNo|contractNo"
                              queryParams="orgId" queryParamValues="orgId"/>
                </td>
                <td class="width-15"><label class="pull-right">品类</label></td>
                <td class="width-35">
                    <sys:grid title="选择商品分类" url="${ctx}/bms/basic/skuClassification/grid" cssClass="form-control"
                              fieldId="skuClass" fieldName="skuClass"
                              fieldKeyName="code" fieldValue="${bmsContractSkuPriceEntity.skuClass}"
                              displayFieldId="skuClassName" displayFieldName="skuClassName"
                              displayFieldKeyName="name" displayFieldValue="${bmsContractSkuPriceEntity.skuClassName}"
                              fieldLabels="编码|名称" fieldKeys="code|name"
                              searchLabels="编码|名称" searchKeys="code|name"
                              queryParams="orgId" queryParamValues="orgId"/>
                </td>
            </tr>
            <tr>
                <td class="width-15"><label class="pull-right">商品编码</label></td>
                <td class="width-35">
                    <sys:grid title="选择商品" url="${ctx}/bms/basic/settlementSku/data" cssClass="form-control"
                              fieldId="" fieldName="" fieldKeyName=""
                              displayFieldId="skuCode" displayFieldName="skuCode"
                              displayFieldKeyName="skuCode" displayFieldValue="${bmsContractSkuPriceEntity.skuCode}"
                              fieldLabels="货主编码|货主名称|商品编码|商品名称" fieldKeys="ownerCode|ownerName|skuCode|skuName"
                              searchLabels="货主编码|货主名称|商品编码|商品名称" searchKeys="ownerCode|ownerName|skuCode|skuName"
                              queryParams="skuClass|orgId" queryParamValues="skuClass|orgId"
                              afterSelect="skuAfterSelect"/>
                </td>
                <td class="width-15"><label class="pull-right">商品名称</label></td>
                <td class="width-35">
                    <form:input path="skuName" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td class="width-15"><label class="pull-right asterisk">未税单价</label></td>
                <td class="width-35">
                    <form:input path="price" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 8, 0)"/>
                </td>
                <td class="width-15"><label class="pull-right asterisk">含税单价</label></td>
                <td class="width-35">
                    <form:input path="taxPrice" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 8, 0)"/>
                </td>
            </tr>
            <tr>
                <td class="width-15"><label class="pull-right">单位</label></td>
                <td class="width-35">
                    <form:select path="unit" htmlEscape="false" class="form-control">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('BMS_CONTRACT_UNIT')}" itemLabel="label" itemValue="value"/>
                    </form:select>
                </td>
                <td class="width-15"></td>
                <td class="width-35"></td>
            </tr>
            </tbody>
        </table>
    </div>
</form:form>
<script type="text/javascript">
    $(document).ready(function () {
        if (!$("#id").val()) {
            $("#orgId").val(jp.getCurrentOrg().orgId);
        }
    });

    function doSubmit($table, $topIndex) {
        var validate = bq.headerSubmitCheck("#inputForm");
        if (!validate.isSuccess) {
            jp.bqError(validate.msg);
            return;
        }
        jp.loading();
        jp.post("${ctx}/bms/basic/bmsContractSkuPrice/save", $('#inputForm').serialize(), function (data) {
            if (data.success) {
                $table.bootstrapTable('refresh');
                jp.success(data.msg);
                jp.close($topIndex);//关闭dialog
            } else {
                jp.bqError(data.msg);
            }
        });
    }

    function skuAfterSelect(row) {
        if (row) {
            $('#skuName').val(row.skuName);
        }
    }
</script>
</body>
</html>