<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>客户管理</title>
    <meta name="decorator" content="ani">
    <script type="text/javascript">
        function doSubmit(table, index) {
            jp.loading();
            var validate = bq.headerSubmitCheck("#inputForm");
            if(!validate.isSuccess){
                jp.bqError(validate.msg);
                return ;
            }
            jp.post("${ctx}/bms/basic/bmsCustomer/save", $('#inputForm').serialize(), function (data) {
                if (data.success) {
                    table.bootstrapTable('refresh');
                    jp.success(data.msg);
                    jp.close(index);//关闭dialog
                } else {
                    jp.bqError(data.msg);
                }
            });
        }

        $(document).ready(function () {
            if (!$("#id").val()) {
                $("#orgId").val(jp.getCurrentOrg().orgId);
            } else {
                $("#ebcuCustomerNo").attr("readonly", true);
                $("#ebcuNameCn").attr("readonly", true);
            }

        });

        function isGeneralTaxpayerChange(flag) {
            $('#ebcuIsGeneralTaxpayer').val(flag ? 'Y' : 'N');
        }
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="bmsCustomer" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="orgId"/>
    <form:hidden path="areaCode"/>
    <table class="table">
        <tbody>
        <tr>
            <td class="width-10"><label class="pull-right asterisk">客户代码</label></td>
            <td class="width-15">
                <form:input path="ebcuCustomerNo" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-10"><label class="pull-right asterisk">中文名称</label></td>
            <td class="width-15">
                <form:input path="ebcuNameCn" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-10"><label class="pull-right">简称</label></td>
            <td class="width-15">
                <form:input path="ebcuShortName" htmlEscape="false" class="form-control"/>
            </td>
            <td class="width-10"><label class="pull-right">英文名称</label></td>
            <td class="width-15">
                <form:input path="ebcuNameEn" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right">行业类型</label></td>
            <td class="width-15">
                <form:select path="ebcuIndustryType"  cssClass="form-control input-sm" >
                    <form:options items="${fns:getDictList('SYS_INDUSTRY_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false" />
                </form:select>
            </td>
            <td class="width-10"><label class="pull-right">电话</label></td>
            <td class="width-15">
                <form:input path="ebcuTel" htmlEscape="false" class="form-control"/>
            </td>
            <td class="width-10"><label class="pull-right">传真</label></td>
            <td class="width-15">
                <form:input path="ebcuFax" htmlEscape="false" class="form-control"/>
            </td>
            <td class="width-10"><label class="pull-right">邮编</label></td>
            <td class="width-15">
                <form:input path="ebcuZipCode" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
            <td class="width-10"><label class="pull-right">所属城市</label></td>
            <td class="width-15">
                <sys:area id="areaId" name="areaId" value="${bmsCustomer.areaId}"
                          labelName="areaName" labelValue="${bmsCustomer.areaName}"
                          cssClass="form-control"/>
            </td>
            <td class="width-10"><label class="pull-right">主营业务</label></td>
            <td class="width-15">
                <form:input path="ebcuMainBusiness" htmlEscape="false" class="form-control"/>
            </td>
            <td class="width-10"><label class="pull-right">地址</label></td>
            <td class="width-15" colspan="3">
                <form:input path="ebcuAddress" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right">财务代码</label></td>
            <td class="width-15">
                <form:input path="ebcuFinanceCode" htmlEscape="false" class="form-control"/>
            </td>
            <td class="width-10"><label class="pull-right">工商登记号</label></td>
            <td class="width-15">
                <form:input path="ebcuBusinessNo" htmlEscape="false" class="form-control"/>
            </td>
            <td class="width-10"><label class="pull-right">平台税务登记号</label></td>
            <td class="width-15">
                <form:input path="ebcuTaxRegistNo" htmlEscape="false" class="form-control"/>
            </td>
            <td class="width-10"><label class="pull-right">对账人</label></td>
            <td class="width-15">
                <form:input path="checkPerson" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right">是否一般纳税人</label></td>
            <td class="width-15">
                <input id="ebcuIsGeneralTaxpayer" type="checkbox" name="ebcuIsGeneralTaxpayer" class="myCheckbox" onclick="isGeneralTaxpayerChange(this.checked)"/>
            </td>
            <td class="width-10"><label class="pull-right">税率</label></td>
            <td class="width-15">
                <form:input path="ebcuTaxRate" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
            </td>
            <td class="width-10"><label class="pull-right">税率值</label></td>
            <td class="width-15">
                <form:input path="ebcuTaxRateValue" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
            </td>
            <td class="width-10"><label class="pull-right">项目</label></td>
            <td class="width-15">
                <form:input path="project" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right asterisk">客户类型</label></td>
            <td colspan="7">
                <sys:checkbox id="ebcuType" name="ebcuType" items="${fns:getDictList('SYS_COMMON_CUSTOMER_TYPE')}" values="${bmsCustomer.ebcuType}" cssClass="i-checks required"/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>