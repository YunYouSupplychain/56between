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
            bq.openDisabled("#inputForm");
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
                    jp.post("${ctx}/oms/basic/omCustomer/save", $('#inputForm').bq_serialize(), function (data) {
                        if (data.success) {
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog
                        } else {
                            jp.error(data.msg);
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
            } else {
                var ebcuIsGeneralTaxpayer = '${omCustomerEntity.ebcuIsGeneralTaxpayer}';
                $('#ebcuIsGeneralTaxpayer').prop('checked', ebcuIsGeneralTaxpayer === 'Y').val(ebcuIsGeneralTaxpayer);
                $('#ebcuCustomerNo').prop('readonly', true);
            }
            removeZero();
        }

        function isGeneralTaxpayerChange(flag) {
            $('#ebcuIsGeneralTaxpayer').val(flag ? 'Y' : 'N');
        }

        function removeZero() {
            var omCustomerEntity = ${fns:toJson(omCustomerEntity)};
            $("#ebcuExchangeRate").val(omCustomerEntity.ebcuExchangeRate);
        }
    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="omCustomerEntity" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <form:hidden path="pmCode"/>
    <form:hidden path="brand"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">基础信息</h3>
            </div>
            <div class="panel-body">
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
                        <td class="width-10"><label class="pull-right">英文名称</label></td>
                        <td class="width-15">
                            <form:input path="ebcuNameEn" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">简称</label></td>
                        <td class="width-15">
                            <form:input path="ebcuShortName" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">城市</label></td>
                        <td class="width-15">
                            <sys:area id="ebcuEbplCityCode" name="ebcuEbplCityCode" value="${omCustomerEntity.ebcuEbplCityCode}"
                                      labelName="ebcuEbplCityName" labelValue="${omCustomerEntity.ebcuEbplCityName}"
                                      cssClass="form-control" allowSearch="true" showFullName="true"/>
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
                    <tr>
                        <td class="width-10"><label class="pull-right">行业类型</label></td>
                        <td class="width-15">
                            <form:select path="ebcuIndustryType" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_INDUSTRY_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">大类</label></td>
                        <td class="width-15">
                            <form:select path="majorClass" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_MAJOR_CLASS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">范围</label></td>
                        <td class="width-15">
                            <form:select path="rangeType" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_RANGE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">快速录入码</label></td>
                        <td class="width-15">
                            <form:input path="ebcuQuickCode" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">财务代码</label></td>
                        <td class="width-15">
                            <form:input path="ebcuFinanceCode" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">业务员</label></td>
                        <td class="width-15">
                            <sys:popSelect title="选择业务员" url="${ctx}/oms/basic/omClerk/popData" cssClass="form-control"
                                           fieldId="clerkCode" fieldKeyName="clerkCode"
                                           fieldName="clerkCode" fieldValue="${omCustomerEntity.clerkCode}"
                                           displayFieldId="clerkName" displayFieldKeyName="clerkName"
                                           displayFieldName="clerkName" displayFieldValue="${omCustomerEntity.clerkName}"
                                           selectButtonId="clerkSBtnId" deleteButtonId="clerkDBtnId"
                                           fieldLabels="业务员代码|业务员名称" fieldKeys="clerkCode|clerkName"
                                           searchLabels="业务员代码|业务员名称" searchKeys="clerkCode|clerkName"
                                           allowInput="true" inputSearchKey="codeAndName"/>
                        </td>
                        <td class="width-10"><label class="pull-right">地址</label></td>
                        <td class="width-15" colspan="3">
                            <form:input path="ebcuAddress" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">客户类型</label></td>
                        <td colspan="7">
                            <sys:checkbox id="ebcuType" name="ebcuType" cssClass="i-checks required"
                                          items="${fns:getDictList('SYS_COMMON_CUSTOMER_TYPE')}" values="${omCustomerEntity.ebcuType}"/>
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
                        <td class="width-10"><label class="pull-right">预留信息1</label></td>
                        <td class="width-15">
                            <form:input path="ebcuSubstr1" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">预留信息2</label></td>
                        <td class="width-15">
                            <form:input path="ebcuSubstr2" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">预留信息3</label></td>
                        <td class="width-15">
                            <form:input path="ebcuSubstr3" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">预留信息4</label></td>
                        <td class="width-15">
                            <form:input path="ebcuSubstr4" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">预留信息5</label></td>
                        <td class="width-15">
                            <form:input path="ebcuSubstr5" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">预留信息6</label></td>
                        <td class="width-15">
                            <form:input path="ebcuSubstr6" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">预留信息7</label></td>
                        <td class="width-15">
                            <form:input path="ebcuSubstr7" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">预留信息8</label></td>
                        <td class="width-15">
                            <form:input path="ebcuSubstr8" htmlEscape="false" class="form-control"/>
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