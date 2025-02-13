<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>客户管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            if (validateForm.form()) {
                $table = table;
                $topIndex = index;
                jp.loading();
                $("#inputForm").submit();
                return true;
            }
            return false;
        }

        $(document).ready(function () {
            validateForm = $("#inputForm").validate({
                submitHandler: function (form) {
                    bq.openDisabled("#inputForm");
                    jp.post("${ctx}/wms/customer/banQinEbCustomer/save", $('#inputForm').bq_serialize(), function (data) {
                        if (data.success) {
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog
                        } else {
                            jp.bqError(data.msg);
                        }
                    })
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });

            init();
        });

        function init() {
            if (!$('#id').val()) {
                $('#orgId').val(jp.getCurrentOrg().orgId);
                $('#ebcuIsGeneralTaxpayer').prop('checked', false).val('N');
                $('#recVer').val('0');
            } else {
                var ebcuIsGeneralTaxpayer = '${banQinEbCustomer.ebcuIsGeneralTaxpayer}';
                $('#ebcuIsGeneralTaxpayer').prop('checked', ebcuIsGeneralTaxpayer === 'Y').val(ebcuIsGeneralTaxpayer);
                $('#ebcuCustomerNo').prop('readonly', true);
            }
        }

        function isGeneralTaxpayerChange(flag) {
            $('#ebcuIsGeneralTaxpayer').val(flag ? 'Y' : 'N');
        }
    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="banQinEbCustomer" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="pmCode"/>
    <form:hidden path="recVer"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">基础信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>客户代码</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuCustomerNo" htmlEscape="false" class="form-control required" maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>中文名称</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuNameCn" htmlEscape="false" class="form-control required" maxlength="256"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">英文名称</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuNameEn" htmlEscape="false" class="form-control" maxlength="128"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">简称</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuShortName" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">状态</label></td>
                        <td class="" width="12%">
                            <form:select path="ebcuCustomerStatus" class="form-control m-b required" disabled="true">
                                <form:options items="${fns:getDictList('SYS_IS_STOP')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="" width="8%"><label class="pull-right">地址</label></td>
                        <td class="" width="12%" colspan="5">
                            <form:input path="ebcuAddress" htmlEscape="false" class="form-control" maxlength="512"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">电话</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuTel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">传真</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuFax" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">邮编</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuZipCode" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">行业类型</label></td>
                        <td class="" width="12%">
                            <form:select path="ebcuIndustryType" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_INDUSTRY_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">主营业务</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuMainBusiness" htmlEscape="false" class="form-control" maxlength="128"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">快速录入码</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuQuickCode" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">财务代码</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuFinanceCode" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">工商登记号</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuBusinessNo" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">平台税务登记号</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuTaxRegistNo" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">是否一般纳税人</label></td>
                        <td class="" width="12%">
                            <input id="ebcuIsGeneralTaxpayer" type="checkbox" name="ebcuIsGeneralTaxpayer" htmlEscape="false" class="myCheckbox" onclick="isGeneralTaxpayerChange(this.checked)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">税率</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuTaxRate" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">税率值</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuTaxRateValue" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">客户类型</label></td>
                        <td colspan="7">
                            <sys:checkbox id="ebcuType" name="ebcuType" items="${fns:getDictList('WMS_CUSTOMER_TYPE')}" values="${banQinEbCustomer.ebcuType}" cssClass="i-checks required"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">预留信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">区域</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuSubstr1" htmlEscape="false" class="form-control" maxlength="64" placeholder="示例江苏省:苏州市:吴中区"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">预留信息2</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuSubstr2" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">预留信息3</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuSubstr3" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">预留信息4</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuSubstr4" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">预留信息5</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuSubstr5" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">预留信息6</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuSubstr6" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">预留信息7</label></td>
                        <td class="" width="12%">
                            <form:input path="ebcuSubstr7" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">预留信息8</label></td>
                        <td class="" width="12%" colspan="3">
                            <form:input path="ebcuSubstr8" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
</body>
</html>