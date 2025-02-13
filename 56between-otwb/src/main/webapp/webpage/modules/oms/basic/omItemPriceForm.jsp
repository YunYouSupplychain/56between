<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>商品价格管理</title>
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
                    var disabledObjs = bq.openDisabled("#inputForm");
                    var params = $('#inputForm').serialize()
                    bq.closeDisabled(disabledObjs);
                    jp.post("${ctx}/oms/basic/omItemPrice/save", params, function (data) {
                        if (data.success) {
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog
                        } else {
                            jp.error(data.msg);
                        }
                    });
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

            $('#effectiveTime').datetimepicker({format: "YYYY-MM-DD"});
            $('#expirationTime').datetimepicker({format: "YYYY-MM-DD"});
            $('#auditDate').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
            if (!$("#id").val()) {
                init();
            }
            removeZero();
        });

        function init() {
            $("#isAllowAdjustment").val("Y");
            $("#isEnable").val("Y");
            $("#convertRatio").val(1);
            if (!$("#orgId").val()) {
                $("#orgId").val(jp.getCurrentOrg().orgId);
            }
            $("#auditStatus").val("00");
        }

        function customerSelect(data) {
            $('#customerNo').val(data.ebcuCustomerNo);
            $('#customerName').val(data.ebcuNameCn);
            if ($('#taxRate').val() == null || $('#taxRate').val() == undefined || $('#taxRate').val() == '') {
                $('#taxRate').val(data.ebcuTaxRate);
            }
        }

        function customerDelete() {
            $('#customerNo').val('');
            $('#customerName').val('');
            $('#taxRate').val('');
            $("#skuCode").val('');
            $("#skuName").val('');
            $("#taxPrice").val('');
            $("#price").val('');
        }

        function priceTypeChange() {
            $("#customerNo").val('');
            $("#customerName").val('');
            $("#taxRate").val('');
            $("#skuCode").val('');
            $("#skuName").val('');
            $("#taxPrice").val('');
            $("#price").val('');
        }

        function priceChange(flag) {
            var taxRateValue = !$("#taxRate").val() ? 0 : $("#taxRate").val();
            var taxRate = math.bignumber(taxRateValue);
            if (flag == '1') {
                var taxPriceValue = !$("#taxPrice").val() ? 0 : $("#taxPrice").val();
                var taxPrice = math.bignumber(taxPriceValue);
                price = taxPrice.sub(taxRate.mul(taxRate));
                $("#price").val(price);
            } else if (flag == '2') {
                var priceValue = !$("#price").val() ? 0 : $("#price").val();
                var price = math.bignumber(priceValue);
                $("#taxPrice").val(math.format(math.chain(price).multiply(math.add(1, taxRate)).round(2).done()));
            }
        }

        function removeZero() {
            var omItemPriceEntity = ${fns:toJson(omItemPriceEntity)};
            $("#taxPrice").val(omItemPriceEntity.taxPrice);
            $("#price").val(omItemPriceEntity.price);
            $("#convertRatio").val(omItemPriceEntity.convertRatio);
            $("#discount").val(omItemPriceEntity.discount);
            $("#purchaseMultiple").val(omItemPriceEntity.purchaseMultiple);
            $("#saleMultiple").val(omItemPriceEntity.saleMultiple);
        }
    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="omItemPriceEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="auditor"/>
    <sys:message content="${message}"/>
    <table class="table well">
        <tbody>
        <tr>
            <td class="width-10"><label class="pull-right asterisk">价格类型</label></td>
            <td class="width-15">
                <form:select path="priceType" class="form-control required" onchange="priceTypeChange()">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('OMS_PRICE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-10"><label class="pull-right asterisk">渠道价格</label></td>
            <td class="width-15">
                <form:select path="channel" class="form-control required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('OMS_CHANNEL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-10"><label class="pull-right">往来户</label></td>
            <td class="width-15">
                <sys:grid title="选择往来户" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                          fieldId="customerNo" fieldName="customerNo"
                          fieldKeyName="ebcuCustomerNo" fieldValue="${omItemPriceEntity.customerNo}"
                          displayFieldId="customerName" displayFieldName="customerName"
                          displayFieldKeyName="ebcuNameCn" displayFieldValue="${omItemPriceEntity.customerName}"
                          fieldLabels="往来户编码|往来户名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                          searchLabels="往来户编码|往来户称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                          queryParams="orgId" queryParamValues="orgId"
                          afterSelect="customerSelect"/>
            </td>
            <td class="width-10"><label class="pull-right asterisk">货主</label></td>
            <td class="width-15">
                <input id="ownerType" value="OWNER" type="hidden">
                <sys:grid title="选择货主" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control required"
                          fieldId="ownerCode" fieldName="ownerCode"
                          fieldKeyName="ebcuCustomerNo" fieldValue="${omItemPriceEntity.ownerCode}"
                          displayFieldId="ownerName" displayFieldName="ownerName"
                          displayFieldKeyName="ebcuNameCn" displayFieldValue="${omItemPriceEntity.ownerName}"
                          fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                          searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                          queryParams="ebcuType|orgId" queryParamValues="ownerType|orgId"/>
            </td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right asterisk">商品编码</label></td>
            <td class="width-15">
                <sys:popSelect title="选择商品" url="${ctx}/oms/basic/omItem/grid" cssClass="form-control required"
                               fieldId="skuCode" fieldName="skuCode"
                               fieldKeyName="skuCode" fieldValue="${omItemPriceEntity.skuCode}"
                               displayFieldId="skuName" displayFieldName="skuName"
                               displayFieldKeyName="skuName" displayFieldValue="${omItemPriceEntity.skuName}"
                               selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                               fieldLabels="货主编码|货主名称|商品编码|商品名称" fieldKeys="ownerCode|ownerName|skuCode|skuName"
                               searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                               concatId="unit,auxiliaryUnit,ownerCode,ownerName" concatName="unit,auxiliaryUnit,ownerCode,ownerName"
                               queryParams="ownerCode" queryParamValues="ownerCode">
                </sys:popSelect>
            </td>
            <td class="width-10"><label class="pull-right">是否启用</label></td>
            <td class="width-15">
                <form:select path="isEnable" class="form-control m-b">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-10"><label class="pull-right asterisk">生效时间</label></td>
            <td class="width-15">
                <div class='input-group form_datetime' id='effectiveTime'>
                    <input type='text' name="effectiveTime" class="form-control required"
                           value="<fmt:formatDate value="${omItemPriceEntity.effectiveTime}" pattern="yyyy-MM-dd"/>"/>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
            </td>
            <td class="width-10"><label class="pull-right asterisk">失效时间</label></td>
            <td class="width-15">
                <div class='input-group form_datetime' id='expirationTime'>
                    <input type='text' name="expirationTime" class="form-control required"
                           value="<fmt:formatDate value="${omItemPriceEntity.expirationTime}" pattern="yyyy-MM-dd"/>"/>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
            </td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right">基本单位</label></td>
            <td class="width-15">
                <form:select path="unit" class="form-control m-b">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('OMS_ITEM_UNIT')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-10"><label class="pull-right">辅助单位</label></td>
            <td class="width-15">
                <form:select path="auxiliaryUnit" class="form-control m-b">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('OMS_UNIT')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-10"><label class="pull-right">换算比例</label></td>
            <td class="width-15">
                <form:input path="convertRatio" onkeyup="bq.numberValidator(this, 8, 0)" class="form-control"/>
            </td>
            <td class="width-10"><label class="pull-right asterisk">折扣(比例)</label></td>
            <td class="width-15">
                <form:input path="discount" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 8, 0)"/>
            </td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right">采购倍数</label></td>
            <td class="width-15">
                <form:input path="purchaseMultiple" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 0, 0)"/>
            </td>
            <td class="width-10"><label class="pull-right">销售倍数</label></td>
            <td class="width-15">
                <form:input path="saleMultiple" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 0, 0)"/>
            </td>
            <td class="width-10"><label class="pull-right">审核状态</label></td>
            <td class="width-15">
                <form:select path="auditStatus" class="form-control m-b" disabled="true">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('SYS_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-10"><label class="pull-right">审核时间</label></td>
            <td class="width-15">
                <div class='input-group form_datetime' id='auditDate'>
                    <input type='text' name="auditDate" class="form-control"
                           value="<fmt:formatDate value="${omItemPriceEntity.auditDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
            </td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right">税率</label></td>
            <td class="width-15">
                <form:select path="taxRate" class="form-control">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('OMS_TAX_RATE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-10"><label class="pull-right asterisk">含税单价</label></td>
            <td class="width-15">
                <form:hidden path="price"/>
                <form:input path="taxPrice" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 6, 0);priceChange('1');"/>
            </td>
            <td class="width-10"><label class="pull-right">是否允许修改单价</label></td>
            <td class="width-15">
                <form:select path="isAllowAdjustment" class="form-control">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right">备注信息</label></td>
            <td class="width-15" colspan="7">
                <form:input path="remarks" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>