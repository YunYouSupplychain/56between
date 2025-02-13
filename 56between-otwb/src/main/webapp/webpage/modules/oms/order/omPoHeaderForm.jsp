<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>采购订单管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        let $table; // 父页面table表格id
        let $topIndex;//弹出窗口的 index
        let disabledObj = [];// 取消disabled对象，用于恢复disabled
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            $table = table;
            $topIndex = index;
            jp.loading();
            save();
        }

        $(document).ready(function () {
            init();

            $("#saleOrderSBtn").click(saleOrderSelect);
            $("#saleOrderDBtn").click(saleOrderDelete);
        });

        function save() {
            let success = true;
            $(".head").find("table").each(function () {
                let validate = bq.headerSubmitCheck($(this));
                if (!validate.isSuccess) {
                    success = false;
                    jp.alert(validate.msg);
                    return false;
                }
            });
            if (success) {
                $(".detail").find("table").each(function () {
                    let validate = bq.tableValidate($(this));
                    if (!validate.isSuccess) {
                        success = false;
                        jp.alert(validate.msg);
                        return false;
                    }
                });
            }
            if (!success) {
                return;
            }

            let checkQty = checkQtyMultiple('#omPoDetailList');
            if (!checkQty) {
                return;
            }
            openDisable('#inputForm');
            jp.post("${ctx}/oms/order/omPoHeader/save", $('#inputForm').serialize(), function (data) {
                if (data.success) {
                    $table.bootstrapTable('refresh');
                    jp.success(data.msg);
                    jp.close($topIndex);
                } else {
                    jp.alert(data.msg);
                }
            });
            closeDisable();
        }

        function checkQtyMultiple(obj) {
            let checkQty = true;
            $(obj).find("tr").each(function () {
                let skuCode = $(this).find($("input[id$='_skuCode']")).eq(0).val();
                let qty = $(this).find($("input[id$='_qty']")).eq(0).val();
                let purchaseMultiple = $(this).find($("input[id$='_purchaseMultiple']")).eq(0).val();
                if (purchaseMultiple && purchaseMultiple > 0) {
                    $(this).find($("input[id$='_qty']")).eq(0).removeClass('form-error');
                    if (math.eval(qty % purchaseMultiple) !== 0) {
                        jp.alert(skuCode + "采购倍数为" + purchaseMultiple + ", 请输入正确数量！");
                        $(this).find($("input[id$='_qty']")).eq(0).focus().addClass('form-error');
                        checkQty = false;
                        return;
                    }
                }
            });
            return checkQty;
        }

        function openDisable(obj) {
            $(obj + " :disabled").each(function () {
                if (parseInt($(this).val()) !== -1) {
                    $(this).prop("disabled", false);
                    disabledObj.push($(this));
                }
            });
            return true;
        }

        function closeDisable() {
            for (let i = 0; i < disabledObj.length; i++) {
                $(disabledObj[i]).prop("disabled", true);
            }
            disabledObj = [];
            return true;
        }

        function init() {
            if (!$('#id').val()) {
                $('#orgId').val(jp.getCurrentOrg().orgId);
                $("#orgName").val(jp.getCurrentOrg().orgName);
                $('#poType').val(1);
                $('#channel').val('purchase');
                $('#subOrgId').val(jp.getCurrentOrg().orgId);
                $('#subOrgName').val(jp.getCurrentOrg().orgName);
                $('#orderDateValue').val(jp.dateFormat(new Date(), "yyyy-MM-dd"));
                $("#validityPeriodV").val(jp.dateFormat(new Date().addTime("Year", 1), "yyyy-MM-dd hh:mm:ss"));
                $("#purchaseMethod").val("002");// 现购
                $("#currency").val("CNY");
                $("#exchangeRate").val(1);
                $("#payStatus").val("N");
            }
            $('#parentId').val($('#orgId').val());
            $('#orderDate').datetimepicker({format: "YYYY-MM-DD"});
            $('#validityPeriod').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
            $('#arrivalTime').datetimepicker({format: "YYYY-MM-DD HH:mm"});
            $('#deliveryDate').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
            $('#auditDate').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
            removeZero();
        }

        function addRow(list, idx, tpl, row) {
            if (!$('#owner').val()) {
                jp.alert("请选择货主");
                return;
            }
            if (!$('#supplierCode').val()) {
                jp.alert("请选择供应商");
                return;
            }
            if (!$('#channel').val()) {
                jp.alert("请选择渠道");
                return;
            }
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            $(list + idx).find("select").each(function () {
                $(this).val($(this).attr("data-value"));
            });
            $(list + idx).find("input[type='checkbox'], input[type='radio']").each(function () {
                let ss = $(this).attr("data-value").split(',');
                for (let i = 0; i < ss.length; i++) {
                    if ($(this).val() === ss[i]) {
                        $(this).attr("checked", "checked");
                    }
                }
            });
            $(list + idx).find(".form_datetime").each(function () {
                $(this).datetimepicker({
                    format: "YYYY-MM-DD HH:mm:ss"
                });
            });
        }

        function delRow(obj, prefix) {
            let id = $(prefix + "_id");
            let delFlag = $(prefix + "_delFlag");
            if (id.val() === "") {
                $(obj).parent().parent().remove();
            } else if (delFlag.val() === "0") {
                delFlag.val("1");
                $(obj).html("&divide;").attr("title", "撤销删除");
                $(obj).parent().parent().addClass("error");
            } else if (delFlag.val() === "1") {
                delFlag.val("0");
                $(obj).html("&times;").attr("title", "删除");
                $(obj).parent().parent().removeClass("error");
            }
            // 统计合计金额
            countTotal();
        }

        function skuSelect(data, idx) {
            // 清除
            skuDelete(idx);
            if (data) {
                $('#omPoDetailList' + idx + "_skuCode").val(data.skuCode);
                $('#omPoDetailList' + idx + "_skuName").val(data.skuName);
                $('#omPoDetailList' + idx + "_spec").val(data.spec);
                $('#omPoDetailList' + idx + '_itemPriceId').val(data.id);
                $('#omPoDetailList' + idx + '_auxiliaryUnit').val(data.auxiliaryUnit);
                $('#omPoDetailList' + idx + '_unit').val(data.unit);
                $('#omPoDetailList' + idx + '_discount').val(data.discount);
                $('#omPoDetailList' + idx + '_taxPrice').val(data.taxPrice);
                $("#omPoDetailList" + idx + "_taxRate").prop("disabled", ("N" === data.isAllowAdjustment));
                $("#omPoDetailList" + idx + "_taxPrice").prop("readonly", ("N" === data.isAllowAdjustment));
                $("#omPoDetailList" + idx + "_discount").prop("readonly", ("N" === data.isAllowAdjustment));
                if (data.taxRate === null || data.taxRate === undefined || data.taxRate === "") {
                    $("#omPoDetailList" + idx + "_taxRate").val($("#taxRate").val());
                } else {
                    $("#omPoDetailList" + idx + "_taxRate").val(data.taxRate);
                }
                $("#omPoDetailList" + idx + "_purchaseMultiple").val(data.purchaseMultiple);
                $('#omPoDetailList' + idx + '_ratio').val(data.convertRatio);
            }
        }

        function skuDelete(idx) {
            $("input[id^=omPoDetailList" + idx + "]").each(function () {
                let $Id = $(this).attr("id");
                let id = "omPoDetailList" + idx + "_id";
                let delFlag = "omPoDetailList" + idx + "_delFlag";
                let ratio = "omPoDetailList" + idx + "_ratio";
                if ($Id === delFlag) {
                    $('#' + $Id).val('0');
                } else if ($Id === ratio) {
                    $('#' + $Id).val('1');
                } else if ($Id !== id){
                    $('#' + $Id).val('');
                }
            });
            $("select[id^=omPoDetailList" + idx + "]").each(function () {
                let $Id = $(this).attr("id");
                $('#' + $Id).val('');
            });
        }

        function channelChange() {
            $("#omPoDetailList").empty();
        }

        function qtyChange(idx) {
            let qtyValue = $('#omPoDetailList' + idx + '_qty').val();
            let ratioValue = $('#omPoDetailList' + idx + '_ratio').val();
            let purchaseMultiple = $("#omPoDetailList" + idx + "_purchaseMultiple").val();

            let qty = math.bignumber(!qtyValue ? 0 : qtyValue);
            let ratio = math.bignumber(!ratioValue ? 1 : ratioValue);
            let auxiliaryQty = math.bignumber((qty.mul(ratio)).toFixed(4));
            $('#omPoDetailList' + idx + '_auxiliaryQty').val(auxiliaryQty);

            priceChange(idx, '1');
        }

        function priceChange(idx, flag) {
            // 税率
            let taxRateValue = $('#omPoDetailList' + idx + '_taxRate').val();
            let taxRate = math.bignumber(!taxRateValue ? 0 : taxRateValue);
            // 折扣
            let discountValue = $('#omPoDetailList' + idx + '_discount').val();
            let discount = math.bignumber(!discountValue ? 0 : discountValue);
            // 数量
            let qtyValue = $('#omPoDetailList' + idx + '_qty').val();
            let qty = math.bignumber(!qtyValue ? 0 : qtyValue);
            // 含税单价、单价
            let taxPriceValue = $('#omPoDetailList' + idx + '_taxPrice').val();
            let priceValue = $('#omPoDetailList' + idx + '_price').val();
            let taxPrice = math.bignumber(!taxPriceValue ? 0 : taxPriceValue);
            let price = math.bignumber(!priceValue ? 0 : priceValue);
            if (flag === '1') {
                price = math.bignumber((taxPrice.sub(taxPrice.mul(taxRate))).toFixed(6));
                $('#omPoDetailList' + idx + '_price').val(price);
            } else if (flag === '2') {
                taxPrice = math.bignumber((price.mul(taxRate.add(1))).toFixed(6));
                $('#omPoDetailList' + idx + '_taxPrice').val(taxPrice);
            }
            // 金额
            let amount = math.bignumber((price.mul(qty)).toFixed(2));
            $('#omPoDetailList' + idx + '_amount').val(amount);
            $('#omPoDetailList' + idx + '_amountV').val(amount);
            // 含税金额
            let taxAmount = math.bignumber((taxPrice.mul(qty)).toFixed(2));
            $('#omPoDetailList' + idx + '_taxAmount').val(taxAmount);
            $('#omPoDetailList' + idx + '_taxAmountV').val(taxAmount);
            // 税金
            let taxMoney = math.bignumber((taxAmount.sub(amount)).toFixed(2));
            $('#omPoDetailList' + idx + '_taxMoney').val(taxMoney);
            $('#omPoDetailList' + idx + '_taxMoneyV').val(taxMoney);
            // 成交价税合计
            let sumTransactionPriceTax = math.bignumber((taxPrice.mul(qty).mul(discount)).toFixed(2));
            $('#omPoDetailList' + idx + '_sumTransactionPriceTax').val(sumTransactionPriceTax);
            $('#omPoDetailList' + idx + '_sumTransactionPriceTaxV').val(sumTransactionPriceTax);
            // 统计合计金额
            countTotal();
        }

        function countTotal() {
            let amount = math.bignumber(0), taxMoney = math.bignumber(0), taxAmount = math.bignumber(0);
            let $amountArr = $(".amount");
            let $taxMoneyArr = $(".taxMoney");
            let $taxAmountArr = $(".taxAmount");

            $.map($amountArr, function (obj) {
                let prefix = $(obj).attr("id").split('_')[0];
                if ($("#" + prefix + "_delFlag").val() === '0') {
                    let amountValue = !$(obj).val() ? 0 : $(obj).val();
                    amount = amount.add(amountValue);
                }
            });
            $.map($taxMoneyArr, function (obj) {
                let prefix = $(obj).attr("id").split('_')[0];
                if ($("#" + prefix + "_delFlag").val() === '0') {
                    let taxMoneyValue = !$(obj).val() ? 0 : $(obj).val();
                    taxMoney = taxMoney.add(taxMoneyValue);
                }
            });
            $.map($taxAmountArr, function (obj) {
                let prefix = $(obj).attr("id").split('_')[0];
                if ($("#" + prefix + "_delFlag").val() === '0') {
                    let taxAmountValue = !$(obj).val() ? 0 : $(obj).val();
                    taxAmount = taxAmount.add(taxAmountValue);
                }
            });
            // 合计金额
            amount = math.bignumber(amount.toFixed(2));
            $("#totalAmount").val(amount);
            $("#totalAmountV").val(amount);
            // 合计税额
            taxMoney = math.bignumber(taxMoney.toFixed(2));
            $("#totalTax").val(taxMoney);
            $("#totalTaxV").val(taxMoney);
            // 合计含税金额
            taxAmount = math.bignumber(taxAmount.toFixed(2));
            $("#totalTaxInAmount").val(taxAmount);
            $("#totalTaxInAmountV").val(taxAmount);
        }

        function afterSelectOwner(row) {
            $("#omPoDetailList").empty();
        }

        function afterSelectSupplier(row) {
            $("#omPoDetailList").empty();
            if (row.ebcuType.indexOf("SETTLEMENT") !== -1) {
                $("#settlement").val(row.ebcuCustomerNo);
                $("#settlementName").val(row.ebcuNameCn);
            }
            $("#exchangeRate").val(math.bignumber(row.ebcuExchangeRate));
        }

        function saleOrderSelect() {
            let $saleOrderNos = $("#saleOrderNos");
            top.layer.open({
                type: 2,
                area: ['800px', '500px'],
                title: "选择销售订单",
                auto: true,
                name: 'friend',
                content: "${ctx}/tag/grid?url=" + encodeURIComponent("${ctx}/oms/order/omSaleHeader/getUnAssociatedPoData?orgId=" + jp.getCurrentOrg().orgId + "|owner=" + $('#owner').val() + "|saleOrderNos=" + $saleOrderNos.val()) + "&fieldLabels=" + encodeURIComponent("销售单号|客户名称|销售总金额") + "&fieldKeys=" + encodeURIComponent("saleNo|customerName|totalAmount") + "&searchLabels=" + encodeURIComponent("销售单号|客户名称") + "&searchKeys=" + encodeURIComponent("saleNo|customerName") + "&isMultiSelected=true",
                btn: ['确定', '关闭'],
                yes: function (index, layero) {
                    let iframeWin = layero.find('iframe')[0].contentWindow;
                    let items = iframeWin.getSelections();
                    if (items === "") {
                        jp.warning("必须选择一条数据!");
                        return;
                    }
                    let saleOrderNos = $.map(items, function (row) {
                        return row.saleNo;
                    }).join(",");
                    if ($saleOrderNos.val()) {
                        saleOrderNos = $saleOrderNos.val() + "," + saleOrderNos;
                    }
                    $saleOrderNos.val(saleOrderNos);
                    top.layer.close(index);
                },
                cancel: function (index) {
                }
            });
        }

        function saleOrderDelete() {
            $("#saleOrderNos").val('');
        }

        /*去除小数位多余的0*/
        function removeZero() {
            let omPoHeaderEntity = ${fns:toJson(omPoHeaderEntity)};
            $("#exchangeRate").val(omPoHeaderEntity.exchangeRate);// 汇率
            $("#totalAmountV").val(omPoHeaderEntity.totalAmount);// 合计金额
            $("#totalTaxV").val(omPoHeaderEntity.totalTax);// 合计税额
            $("#totalTaxInAmountV").val(omPoHeaderEntity.totalTaxInAmount);// 合计含税金额
            $("#freightCharge").val(omPoHeaderEntity.freightCharge);// 运费金额
            $("#actualPaidAmount").val(omPoHeaderEntity.actualPaidAmount);// 实付金额
        }
    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="omPoHeaderEntity" method="post" class="form">
    <input type="hidden" id="priceType" value="P"/>
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="taxRate"/>
    <form:hidden path="taxRate"/>
    <sys:message content="${message}"/>
    <div class="tabs-container head" style="height: 350px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#orderInfo" aria-expanded="true">订单信息</a></li>
            <li class=""><a data-toggle="tab" href="#consigneeInfo" aria-expanded="true">收货信息</a></li>
            <li class=""><a data-toggle="tab" href="#shipperInfo" aria-expanded="true">发货信息</a></li>
            <li class=""><a data-toggle="tab" href="#logisticsInfo" aria-expanded="true">物流信息</a></li>
            <li class=""><a data-toggle="tab" href="#settleInfo" aria-expanded="true">结算信息</a></li>
            <li class=""><a data-toggle="tab" href="#auditInfo" aria-expanded="true">审核信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="orderInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-8"><label class="pull-right">采购单号</label></td>
                        <td class="width-12">
                            <form:input path="poNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>货主</label></td>
                        <td class="width-12">
                            <input id="ownerType" value="OWNER" type="hidden">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择货主" cssClass="form-control required"
                                           fieldId="owner" fieldName="owner" allowInput="true" inputSearchKey="codeAndName"
                                           fieldKeyName="ebcuCustomerNo" fieldValue="${omPoHeaderEntity.owner}"
                                           displayFieldId="ownerName" displayFieldName="ownerName"
                                           displayFieldKeyName="ebcuNameCn" displayFieldValue="${omPoHeaderEntity.ownerName}"
                                           selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                           fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="ownerType" afterSelect="afterSelectOwner">
                            </sys:popSelect>
                        </td>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>供应商</label></td>
                        <td class="width-12">
                            <input id="supplierType" value="SUPPLIER" type="hidden">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择供应商" cssClass="form-control required"
                                           fieldId="supplierCode" fieldName="supplierCode" allowInput="true" inputSearchKey="codeAndName"
                                           fieldKeyName="ebcuCustomerNo" fieldValue="${omPoHeaderEntity.supplierCode}"
                                           displayFieldId="supplierName" displayFieldName="supplierName"
                                           displayFieldKeyName="ebcuNameCn" displayFieldValue="${omPoHeaderEntity.supplierName}"
                                           selectButtonId="supplierSelectId" deleteButtonId="supplierDeleteId"
                                           fieldLabels="供应商编码|供应商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="供应商编码|供应商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           concatId="taxRate,exchangeRate,currency,settlement,settlementName"
                                           concatName="ebcuTaxRate,ebcuExchangeRate,ebcuCurrency,ebcuCustomerNo,ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="supplierType" afterSelect="afterSelectSupplier">
                            </sys:popSelect>
                        </td>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>订单类型</label></td>
                        <td class="width-12">
                            <form:select path="poType" class="form-control required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_PO_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right">订单状态</label></td>
                        <td class="width-12">
                            <form:select path="status" class="form-control" disabled="true">
                                <form:options items="${fns:getDictList('OMS_PO_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>订单日期</label></td>
                        <td class="width-12">
                            <div class='input-group form_datetime' id='orderDate'>
                                <input id="orderDateValue" type='text' name="orderDate" class="form-control required"
                                       value="<fmt:formatDate value="${omPoHeaderEntity.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-8"><label class="pull-right">采购方式</label></td>
                        <td class="width-12">
                            <form:select path="purchaseMethod" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_PURCHASE_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right">有效期至</label></td>
                        <td class="width-12">
                            <div class='input-group form_datetime' id='validityPeriod'>
                                <input id="validityPeriodV" type='text' name="validityPeriod" class="form-control"
                                       value="<fmt:formatDate value="${omPoHeaderEntity.validityPeriod}" pattern="yyyy-MM-dd"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-8"><label class="pull-right">客户订单号</label></td>
                        <td class="width-12">
                            <form:input path="customerNo" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">项目</label></td>
                        <td class="width-12">
                            <sys:popSelect url="${ctx}/oms/basic/omProject/popData" title="选择项目" cssClass="form-control"
                                           fieldId="project" fieldName="project" allowInput="true" inputSearchKey="codeAndName"
                                           fieldKeyName="projectCode" fieldValue="${omPoHeaderEntity.project}"
                                           displayFieldId="projectName" displayFieldName="projectName"
                                           displayFieldKeyName="projectName" displayFieldValue="${omPoHeaderEntity.projectName}"
                                           selectButtonId="projectSelectId" deleteButtonId="projectDeleteId"
                                           fieldLabels="项目编码|项目名称" fieldKeys="projectCode|projectName"
                                           searchLabels="项目编码|项目名称" searchKeys="projectCode|projectName">
                            </sys:popSelect>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>下发机构</label></td>
                        <td class="width-12">
                            <input id="parentId" type="hidden">
                            <sys:popSelect url="${ctx}/sys/office/companyData" title="选择机构" cssClass="form-control required"
                                           fieldId="subOrgId" fieldName="subOrgId"
                                           fieldKeyName="id" fieldValue="${omPoHeaderEntity.subOrgId}"
                                           displayFieldId="subOrgName" displayFieldName="subOrgName"
                                           displayFieldKeyName="name" displayFieldValue="${omPoHeaderEntity.subOrgName}"
                                           selectButtonId="outWarhouseSelectId" deleteButtonId="outWarhouseDeleteId"
                                           fieldLabels="机构编码|机构名称" fieldKeys="code|name"
                                           searchLabels="机构编码|机构名称" searchKeys="code|name"
                                           queryParams="id" queryParamValues="parentId">
                            </sys:popSelect>
                        </td>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>渠道价格</label></td>
                        <td class="width-12">
                            <form:select path="channel" class="form-control required" onchange="channelChange()">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_CHANNEL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right">合同号</label></td>
                        <td class="width-12">
                            <form:input path="contractNo" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">币种</label></td>
                        <td class="width-12">
                            <form:select path="currency" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_CURRENCY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right">汇率</label></td>
                        <td class="width-12">
                            <form:input path="exchangeRate" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">合计金额</label></td>
                        <td class="width-12">
                            <form:hidden path="totalAmount"/>
                            <input id="totalAmountV" class="form-control " readonly/>
                        </td>
                        <td class="width-8"><label class="pull-right">合计税额</label></td>
                        <td class="width-12">
                            <form:hidden path="totalTax"/>
                            <input id="totalTaxV" class="form-control " readonly/>
                        </td>
                        <td class="width-8"><label class="pull-right">合计含税金额</label></td>
                        <td class="width-12">
                            <form:hidden path="totalTaxInAmount"/>
                            <input id="totalTaxInAmountV" class="form-control " readonly/>
                        </td>
                        <td class="width-8"><label class="pull-right">备注信息</label></td>
                        <td width="32%" colspan="3" rowspan="2">
                            <form:textarea path="remarks" htmlEscape="false" rows="2" class="form-control "/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">销售单号</label></td>
                        <td width="32%" colspan="5" rowspan="2">
                            <div class="input-group" style="width: 100%;height: 100%">
                                <form:textarea path="saleOrderNos" htmlEscape="false" rows="3" class="form-control" readonly="true" cssStyle="resize: none;"/>
                                <span class="input-group-btn" style="height: 100%">
                                    <button id="saleOrderSBtn" class="btn btn-primary" type="button" style="height: 100%"><i class="fa fa-search"></i></button>
                                    <button id="saleOrderDBtn" class="close" data-dismiss="alert" style="position: absolute; top: 40%; right: 53px; z-index: 999; display: block;">×</button>
                                </span>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="consigneeInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-8"><label class="pull-right">收货人</label></td>
                        <td class="width-12">
                            <form:input path="consignee" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">联系电话</label></td>
                        <td class="width-12">
                            <form:input path="consigneeTel" htmlEscape="false" class="form-control " maxlength="32"/>
                        </td>
                        <td class="width-8"><label class="pull-right">收货人区域</label></td>
                        <td class="width-12">
                            <sys:treeselect title="选择收货人区域" url="/sys/area/treeData" cssClass="form-control"
                                            id="consigneeArea" name="consigneeArea" value="${omPoHeaderEntity.consigneeArea}"
                                            labelName="area.name" labelValue="${omPoHeaderEntity.consigneeAreaName}"/>
                        </td>
                        <td class="width-8"><label class="pull-right">收货人地址</label></td>
                        <td colspan="12%">
                            <form:input path="consigneeAddress" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">收货人地址区域</label></td>
                        <td class="width-12">
                            <form:input path="consigneeAddressArea" htmlEscape="false" class="form-control" maxlength="128"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">预计到货时间</label></td>
                        <td class="width-12">
                            <div class='input-group form_datetime' id='arrivalTime'>
                                <input type='text' name="arrivalTime" class="form-control"
                                       value="<fmt:formatDate value="${omPoHeaderEntity.arrivalTime}" pattern="yyyy-MM-dd HH:mm"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="shipperInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-8"><label class="pull-right">发货人</label></td>
                        <td class="width-12">
                            <form:input path="shipper" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">联系电话</label></td>
                        <td class="width-12">
                            <form:input path="shipperTel" htmlEscape="false" class="form-control " maxlength="32"/>
                        </td>
                        <td class="width-8"><label class="pull-right">发货人区域</label></td>
                        <td class="width-12">
                            <sys:treeselect title="选择发货人区域" url="/sys/area/treeData" cssClass="form-control"
                                            id="shipperArea" name="shipperArea" value="${omPoHeaderEntity.shipperArea}"
                                            labelName="area.name" labelValue="${omPoHeaderEntity.shipperAreaName}"/>
                        </td>
                        <td class="width-8"><label class="pull-right">发货人地址</label></td>
                        <td colspan="12%">
                            <form:input path="shipperAddress" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">发货人地址区域</label></td>
                        <td class="width-12">
                            <form:input path="shipperAddressArea" htmlEscape="false" class="form-control" maxlength="128"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">发货时间</label></td>
                        <td class="width-12">
                            <div class='input-group form_datetime' id='deliveryDate'>
                                <input type='text' name="deliveryDate" class="form-control"
                                       value="<fmt:formatDate value="${omPoHeaderEntity.deliveryDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="logisticsInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-8"><label class="pull-right">运输方式</label></td>
                        <td class="width-12">
                            <form:select path="transportMode" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_TRANSPORT_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right">承运商</label></td>
                        <td class="width-12">
                            <input id="carrierType" value="CARRIER" type="hidden">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData"
                                           title="选择承运商" cssClass="form-control" allowInput="true" inputSearchKey="codeAndName"
                                           fieldId="carrier" fieldName="carrier"
                                           fieldKeyName="ebcuCustomerNo" fieldValue="${omPoHeaderEntity.carrier}"
                                           displayFieldId="carrierName" displayFieldName="carrierName"
                                           displayFieldKeyName="ebcuNameCn" displayFieldValue="${omPoHeaderEntity.carrierName}"
                                           selectButtonId="carrierSelectId" deleteButtonId="carrierDeleteId"
                                           fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="carrierType">
                            </sys:popSelect>
                        </td>
                        <td class="width-8"><label class="pull-right">物流单号</label></td>
                        <td class="width-12">
                            <form:input path="logisticsNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">车牌号</label></td>
                        <td class="width-12">
                            <form:input path="vehicleNo" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">司机</label></td>
                        <td class="width-12">
                            <form:input path="driver" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">联系电话</label></td>
                        <td class="width-12">
                            <form:input path="contactTel" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">运费金额</label></td>
                        <td class="width-12">
                            <form:input path="freightCharge" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
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
            </div>
            <div id="settleInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-8"><label class="pull-right">预付金额</label></td>
                        <td class="width-12">
                            <form:input path="prepaidAmount" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>结算对象</label></td>
                        <td class="width-12">
                            <input id="settlementType" value="SETTLEMENT" type="hidden">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择结算方" cssClass="form-control required" allowInput="true" inputSearchKey="codeAndName"
                                           fieldId="settlement" fieldName="settlement" fieldKeyName="ebcuCustomerNo" fieldValue="${omPoHeaderEntity.settlement}"
                                           displayFieldId="settlementName" displayFieldName="settlementName" displayFieldKeyName="ebcuNameCn"
                                           displayFieldValue="${omPoHeaderEntity.settlementName}"
                                           selectButtonId="settlementSelectId" deleteButtonId="settlementDeleteId"
                                           fieldLabels="结算方编码|结算方名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="结算方编码|结算方名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="settlementType">
                            </sys:popSelect>
                        </td>
                        <td class="width-8"><label class="pull-right">结算方式</label></td>
                        <td class="width-12">
                            <form:select path="settleMode" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('BMS_SETTLEMENT_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right">支付状态</label></td>
                        <td class="width-12">
                            <form:select path="payStatus" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_PAY_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right">实付金额</label></td>
                        <td class="width-12">
                            <form:input path="actualPaidAmount" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">付款账户</label></td>
                        <td class="width-12">
                            <form:input path="payAccount" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">付款单号</label></td>
                        <td class="width-12">
                            <form:input path="payNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">联系人</label></td>
                        <td class="width-12">
                            <form:input path="settlementContact" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">联系电话</label></td>
                        <td class="width-12">
                            <form:input path="settlementContactTel" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">开户银行</label></td>
                        <td class="width-12">
                            <form:input path="depositBank" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">出纳备注</label></td>
                        <td colspan="7">
                            <form:textarea path="cashierRemarks" rows="1" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="auditInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-8"><label class="pull-right">机构</label></td>
                        <td class="width-12">
                            <form:hidden path="organization"/>
                            <form:input path="orgName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">部门</label></td>
                        <td class="width-12">
                            <form:hidden path="department"/>
                            <form:input path="departName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">业务人</label></td>
                        <td class="width-12">
                            <form:input path="businessBy" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">制单人</label></td>
                        <td class="width-12">
                            <form:input path="preparedBy" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">创建时间</label></td>
                        <td class="width-12">
                            <div class='input-group form_datetime'>
                                <input id='createDate' name="createDate" class="form-control" readonly
                                       value="<fmt:formatDate value="${omPoHeaderEntity.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">审核人</label></td>
                        <td class="width-12">
                            <form:input path="auditBy" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">审核时间</label></td>
                        <td class="width-12">
                            <div class='input-group form_datetime'>
                                <input id='auditDate' name="auditDate" class="form-control" readonly
                                       value="<fmt:formatDate value="${omPoHeaderEntity.auditDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-8"><label class="pull-right">更新人</label></td>
                        <td class="width-12">
                            <form:input path="updateBy.name" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">更新时间</label></td>
                        <td class="width-12">
                            <div class='input-group form_datetime'>
                                <input id='updateDate' name="updateDate" class="form-control" readonly
                                       value="<fmt:formatDate value="${omPoHeaderEntity.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-8"/>
                        <td class="width-12"/>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="tabs-container detail" style="min-height: 300px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">采购订单明细</a></li>
        </ul>
        <div class="tab-content" style="overflow-x: auto">
            <div id="tab-1" class="tab-pane fade in active">
                <shiro:hasPermission name="order:omPoHeader:edit">
                    <c:if test="${omPoHeaderEntity.status == '10' || omPoHeaderEntity.status == '20' || omPoHeaderEntity.status == null}">
                        <a class="btn btn-white btn-sm" onclick="addRow('#omPoDetailList', omPoDetailRowIdx, omPoDetailTpl);omPoDetailRowIdx = omPoDetailRowIdx + 1;"
                           title="新增"><i class="fa fa-plus"></i> 新增</a>
                    </c:if>
                </shiro:hasPermission>
                <table class="table well text-nowrap" style="min-width: 1500px;">
                    <thead>
                    <tr>
                        <th class="hide"></th>
                        <th class="asterisk">商品编码</th>
                        <th>商品名称</th>
                        <th>规格属性</th>
                        <th>辅助单位</th>
                        <th>辅助单位数量</th>
                        <th>基本单位</th>
                        <th class="asterisk">数量</th>
                        <th class="asterisk">税率</th>
                        <th class="asterisk">单价</th>
                        <th class="asterisk">含税单价</th>
                        <th>税金</th>
                        <th>金额</th>
                        <th>含税金额</th>
                        <th>折扣</th>
                        <th>成交价税合计</th>
                        <th>备注</th>
                        <th width="10">&nbsp;</th>
                    </tr>
                    </thead>
                    <tbody id="omPoDetailList">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<script type="text/template" id="omPoDetailTpl">//<!--
<tr id="omPoDetailList{{idx}}">
    <td class="hide">
        <input id="omPoDetailList{{idx}}_id" name="omPoDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="omPoDetailList{{idx}}_delFlag" name="omPoDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="omPoDetailList{{idx}}_ratio" name="omPoDetailList[{{idx}}].ratio" type="hidden" value="{{row.ratio}}"/>
        <input id="omPoDetailList{{idx}}_purchaseMultiple" name="omPoDetailList[{{idx}}].purchaseMultiple" type="hidden" value="{{row.purchaseMultiple}}"/>
        <input id="omPoDetailList{{idx}}_itemPriceId" name="omPoDetailList[{{idx}}].itemPriceId" type="hidden" value="{{row.itemPriceId}}"/>
        <input id="omPoDetailList{{idx}}_isAllowAdjustment" name="omPoDetailList[{{idx}}].isAllowAdjustment" type="hidden" value="{{row.isAllowAdjustment}}"/>
    </td>
    <td class="width-12">
        <sys:grid title="商品" url="${ctx}/oms/basic/omItemPrice/popData" cssClass="form-control required"
                  fieldId="" fieldName="" fieldKeyName="" readonly="true"
                  displayFieldId="omPoDetailList{{idx}}_skuCode" displayFieldName="omPoDetailList[{{idx}}].skuCode"
                  displayFieldKeyName="skuCode" displayFieldValue="{{row.skuCode}}"
                  fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                  searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                  queryParams="ownerCode|customerNo|channel|priceType|effectiveTime|expirationTime|orgId"
                  queryParamValues="owner|supplierCode|channel|priceType|orderDateValue|orderDateValue|orgId"
                  afterSelect="skuSelect({{idx}})"/>
    </td>
    <td class="width-12">
        <input id="omPoDetailList{{idx}}_skuName" name="omPoDetailList[{{idx}}].skuName" type="text" value="{{row.skuName}}" class="form-control" readonly/>
    </td>
    <td class="width-8">
        <input id="omPoDetailList{{idx}}_spec" name="omPoDetailList[{{idx}}].spec" type="text" value="{{row.spec}}" class="form-control" readonly/>
    </td>
    <td width="5%">
        <select id="omPoDetailList{{idx}}_auxiliaryUnit" name="omPoDetailList[{{idx}}].auxiliaryUnit" data-value="{{row.auxiliaryUnit}}" class="form-control m-b" disabled>
            <c:forEach items="${fns:getDictList('OMS_UNIT')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td width="5%">
        <input id="omPoDetailList{{idx}}_auxiliaryQty" name="omPoDetailList[{{idx}}].auxiliaryQty" type="text" value="{{row.auxiliaryQty}}" class="form-control" readonly/>
    </td>
    <td width="5%">
        <select id="omPoDetailList{{idx}}_unit" name="omPoDetailList[{{idx}}].unit" data-value="{{row.unit}}" class="form-control m-b" disabled>
            <c:forEach items="${fns:getDictList('OMS_ITEM_UNIT')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td width="5%">
        <input id="omPoDetailList{{idx}}_qty" name="omPoDetailList[{{idx}}].qty" type="text" value="{{row.qty}}" class="form-control required" onkeyup="bq.numberValidator(this, 0, 0);qtyChange({{idx}});"/>
    </td>
    <td width="4%">
        <select id="omPoDetailList{{idx}}_taxRate" name="omPoDetailList[{{idx}}].taxRate" data-value="{{row.taxRate}}" class="form-control m-b required" disabled onchange="priceChange({{idx}}, '1')">
            <c:forEach items="${fns:getDictList('OMS_TAX_RATE')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td width="5%">
        <input id="omPoDetailList{{idx}}_price" name="omPoDetailList[{{idx}}].price" type="text" value="{{row.price}}" class="form-control required" readonly />
    </td>
    <td width="5%">
        <input id="omPoDetailList{{idx}}_taxPrice" name="omPoDetailList[{{idx}}].taxPrice" type="text" value="{{row.taxPrice}}" class="form-control required" readonly onkeyup="bq.numberValidator(this, 6, 0);priceChange({{idx}}, '1');"/>
    </td>
    <td width="5%">
        <input id="omPoDetailList{{idx}}_taxMoney" name="omPoDetailList[{{idx}}].taxMoney" type="hidden" value="{{row.taxMoney}}" class="form-control taxMoney" readonly/>
        <input id="omPoDetailList{{idx}}_taxMoneyV" type="text" value="{{row.taxMoney}}" class="form-control" readonly/>
    </td>
    <td width="5%">
        <input id="omPoDetailList{{idx}}_amount" name="omPoDetailList[{{idx}}].amount" type="hidden" value="{{row.amount}}" class="amount"/>
        <input id="omPoDetailList{{idx}}_amountV" type="text" value="{{row.amount}}" class="form-control" readonly/>
    </td>
    <td width="5%">
        <input id="omPoDetailList{{idx}}_taxAmount" name="omPoDetailList[{{idx}}].taxAmount" type="hidden" value="{{row.taxAmount}}" class="form-control taxAmount" readonly/>
        <input id="omPoDetailList{{idx}}_taxAmountV" type="text" value="{{row.taxAmount}}" class="form-control" readonly/>
    </td>
    <td width="4%">
        <input id="omPoDetailList{{idx}}_discount" name="omPoDetailList[{{idx}}].discount" type="text" value="{{row.discount}}" class="form-control" readonly onkeyup="bq.numberValidator(this, 8, 0);priceChange({{idx}}, '1');"/>
    </td>
    <td width="5%">
        <input id="omPoDetailList{{idx}}_sumTransactionPriceTax" name="omPoDetailList[{{idx}}].sumTransactionPriceTax" type="hidden" value="{{row.sumTransactionPriceTax}}" class="form-control" readonly/>
        <input id="omPoDetailList{{idx}}_sumTransactionPriceTaxV" type="text" value="{{row.sumTransactionPriceTax}}" class="form-control" readonly/>
    </td>
    <td width="10%">
        <input id="omPoDetailList{{idx}}_remarks" name="omPoDetailList[{{idx}}].remarks" type="text" value="{{row.remarks}}" class="form-control"/>
    </td>
    <td class="text-center" width="10">
        {{#delBtn}}<span class="close" onclick="delRow(this, '#omPoDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
    </td>
</tr>//-->
</script>
<script type="text/javascript">
    let omPoDetailRowIdx = 0,
        omPoDetailTpl = $("#omPoDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $(document).ready(function () {
        let data = ${fns:toJson(omPoHeaderEntity.omPoDetailList)};
        let status = $('#status').val();
        for (let i = 0; i < data.length; i++) {
            addRow('#omPoDetailList', omPoDetailRowIdx, omPoDetailTpl, data[i]);
            if (status && status !== '20') {
                $('#omPoDetailList' + omPoDetailRowIdx + "_qty").prop("readonly", true);
                $('#omPoDetailList' + omPoDetailRowIdx + "_taxPrice").prop("readonly", true);
                $('#omPoDetailList' + omPoDetailRowIdx + "_taxRate").prop("disabled", true);
                $('#omPoDetailList' + omPoDetailRowIdx + "_discount").prop("readonly", true);
            } else {
                let isAllowEdit = $('#omPoDetailList' + omPoDetailRowIdx + "_isAllowAdjustment").val();
                if (isAllowEdit && isAllowEdit === 'N') {
                    $('#omPoDetailList' + omPoDetailRowIdx + "_qty").prop("readonly", true);
                    $('#omPoDetailList' + omPoDetailRowIdx + "_taxPrice").prop("readonly", true);
                    $('#omPoDetailList' + omPoDetailRowIdx + "_taxRate").prop("disabled", true);
                    $('#omPoDetailList' + omPoDetailRowIdx + "_discount").prop("readonly", true);
                } else {
                    $('#omPoDetailList' + omPoDetailRowIdx + "_qty").prop("readonly", false);
                    $('#omPoDetailList' + omPoDetailRowIdx + "_taxPrice").prop("readonly", false);
                    $('#omPoDetailList' + omPoDetailRowIdx + "_taxRate").prop("disabled", false);
                    $('#omPoDetailList' + omPoDetailRowIdx + "_discount").prop("readonly", false);
                }
            }
            omPoDetailRowIdx = omPoDetailRowIdx + 1;
        }
    });
</script>
</body>
</html>