<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>销售订单管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        let $table; // 父页面table表格id
        let $topIndex;//弹出窗口的 index
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            $table = table;
            $topIndex = index;
            jp.loading();
            let validate = bq.headerSubmitCheck('#inputForm');
            let checkQty = checkQtyMultiple('#omSaleDetailList');
            if (!checkQty) {
                return;
            }
            if (validate.isSuccess) {
                let disableObjs = bq.openDisabled('#inputForm');
                jp.post("${ctx}/oms/order/omSaleHeader/save", $('#inputForm').serialize(), function (data) {
                    if (data.success) {
                        $table.bootstrapTable('refresh');
                        jp.success(data.msg);
                        jp.close($topIndex);
                    } else {
                        jp.alert(data.msg);
                    }
                });
                bq.closeDisabled(disableObjs);
            } else {
                jp.alert(validate.msg);
            }
        }

        $(document).ready(function () {
            init();
        });

        function checkQtyMultiple(obj) {
            let checkQty = true;
            $(obj).find("tr").each(function () {
                let skuCode = $(this).find($("input[id$='_skuCode']")).eq(0).val();
                let qty = $(this).find($("input[id$='_qty']")).eq(0).val();
                let saleMultiple = $(this).find($("input[id$='_saleMultiple']")).eq(0).val();
                if (saleMultiple && saleMultiple > 0) {
                    $(this).find($("input[id$='_qty']")).eq(0).removeClass('form-error');
                    if (math.eval(qty % saleMultiple) !== 0) {
                        jp.alert(skuCode + "销售倍数为" + saleMultiple + ", 请输入正确数量！");
                        $(this).find($("input[id$='_qty']")).eq(0).focus().addClass('form-error');
                        checkQty = false;
                        return;
                    }
                }
            });
            return checkQty;
        }

        function init() {
            if (!$('#id').val()) {
                $('#orgId').val(jp.getCurrentOrg().orgId);
                $('#saleType').val('1');
                $('#channel').val('online');
                $('#outWarhouse').val(jp.getCurrentOrg().orgId);
                $('#outWarhouseName').val(jp.getCurrentOrg().orgName);
                $('#orderDateValue').val(jp.dateFormat(new Date(), "yyyy-MM-dd"));
                $('#isAvailableStock').val('Y');
                $("#currency").val("CNY");
                $("#payStatus").val("N");
                $("#saleMethod").val("002");// 现销
            }

            $('#parentId').val($('#orgId').val());
            $('#saleNo').prop('readonly', true);
            $('#orderDate').datetimepicker({format: "YYYY-MM-DD"});
            $("#payDate").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
            removeZero();
        }

        function addRow(list, idx, tpl, row) {
            if (!$('#owner').val()) {
                jp.alert("请选择货主");
                return;
            }
            if (!$('#customer').val()) {
                jp.alert("请选择客户");
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
                // SKU信息
                $('#omSaleDetailList' + idx + "_skuCode").val(data.skuCode);
                $('#omSaleDetailList' + idx + "_skuName").val(data.skuName);
                $('#omSaleDetailList' + idx + "_spec").val(data.spec);
                // 价格信息
                $('#omSaleDetailList' + idx + '_itemPriceId').val(data.id);
                $('#omSaleDetailList' + idx + '_auxiliaryUnit').val(data.auxiliaryUnit);
                $('#omSaleDetailList' + idx + '_unit').val(data.unit);
                $('#omSaleDetailList' + idx + '_discount').val(data.discount);
                $('#omSaleDetailList' + idx + '_taxPrice').val(data.taxPrice);
                $("#omSaleDetailList" + idx + "_taxRate").prop("disabled", ("N" == data.isAllowAdjustment));
                $("#omSaleDetailList" + idx + "_taxPrice").prop("readonly", ("N" == data.isAllowAdjustment));
                $("#omSaleDetailList" + idx + "_discount").prop("readonly", ("N" == data.isAllowAdjustment));
                if (data.taxRate == null || data.taxRate == undefined || data.taxRate == '') {
                    $("#omSaleDetailList" + idx + "_taxRate").val($("#taxRate").val());
                } else {
                    $("#omSaleDetailList" + idx + "_taxRate").val(data.taxRate);
                }
                $("#omSaleDetailList" + idx + "_saleMultiple").val(data.saleMultiple);
                $('#omSaleDetailList' + idx + '_ratio').val(data.convertRatio);
            }
        }

        function skuDelete(idx) {
            $("input[id^=omSaleDetailList" + idx + "]").each(function () {
                let $Id = $(this).attr("id");
                let id = "omSaleDetailList" + idx + "_id";
                let delFlag = "omSaleDetailList" + idx + "_delFlag";
                let ratio = "omSaleDetailList" + idx + "_ratio";
                if ($Id === delFlag) {
                    $('#' + $Id).val('0');
                } else if ($Id === ratio) {
                    $('#' + $Id).val('1');
                } else if ($Id !== id){
                    $('#' + $Id).val('');
                }
            });
            $("select[id^=omSaleDetailList" + idx + "]").each(function () {
                let $Id = $(this).attr("id");
                $('#' + $Id).val('');
            });
        }

        function channelChange() {
            $("#omSaleDetailList").empty();
        }

        function qtyChange(idx) {
            let qtyValue = $('#omSaleDetailList' + idx + '_qty').val();
            let ratioValue = $('#omSaleDetailList' + idx + '_ratio').val();

            let qty = math.bignumber(!qtyValue ? 0 : qtyValue);
            let ratio = math.bignumber(!ratioValue ? 1 : ratioValue);
            let auxiliaryQty = math.bignumber((qty.mul(ratio)).toFixed(4));
            $('#omSaleDetailList' + idx + '_auxiliaryQty').val(auxiliaryQty);

            priceChange(idx, '1');
        }

        function priceChange(idx, flag) {
            // 税率
            let taxRateValue = $('#omSaleDetailList' + idx + '_taxRate').val();
            let taxRate = math.bignumber(!taxRateValue ? 0 : taxRateValue);
            // 折扣
            let discountValue = $('#omSaleDetailList' + idx + '_discount').val();
            let discount = math.bignumber(!discountValue ? 0 : discountValue);
            // 数量
            let qtyValue = $('#omSaleDetailList' + idx + '_qty').val();
            let qty = math.bignumber(!qtyValue ? 0 : qtyValue);
            // 含税单价、单价
            let taxPriceValue = $('#omSaleDetailList' + idx + '_taxPrice').val();
            let priceValue = $('#omSaleDetailList' + idx + '_price').val();
            let taxPrice = math.bignumber(!taxPriceValue ? 0 : taxPriceValue);
            let price = math.bignumber(!priceValue ? 0 : priceValue);
            if (flag === '1') {
                price = math.bignumber((taxPrice.sub(taxPrice.mul(taxRate))).toFixed(6));
                $('#omSaleDetailList' + idx + '_price').val(price);
            } else if (flag === '2') {
                taxPrice = math.bignumber((price.mul(taxRate.add(1))).toFixed(6));
                $('#omSaleDetailList' + idx + '_taxPrice').val(taxPrice);
            }
            // 金额
            let amount = math.bignumber((price.mul(qty)).toFixed(2));
            $('#omSaleDetailList' + idx + '_amount').val(amount);
            $('#omSaleDetailList' + idx + '_amountV').val(amount);
            // 含税金额
            let taxAmount = math.bignumber((taxPrice.mul(qty)).toFixed(2));
            $('#omSaleDetailList' + idx + '_taxAmount').val(taxAmount);
            $('#omSaleDetailList' + idx + '_taxAmountV').val(taxAmount);
            // 税金
            // let taxMoney = math.format(math.chain(taxRate).multiply(qty).multiply(price).round(2).done());
            // let taxMoney = math.format(math.chain(taxAmount).subtract(amount).round(2).done());
            let taxMoney = math.bignumber((taxAmount.sub(amount)).toFixed(2));
            $('#omSaleDetailList' + idx + '_taxMoney').val(taxMoney);
            $('#omSaleDetailList' + idx + '_taxMoneyV').val(taxMoney);
            // 成交价税合计
            // let sumTransactionPriceTax = math.format(math.chain(taxPrice).multiply(qty).multiply(discount).round(2).done());
            let sumTransactionPriceTax = math.bignumber((taxPrice.mul(qty).mul(discount)).toFixed(2));
            $('#omSaleDetailList' + idx + '_sumTransactionPriceTax').val(sumTransactionPriceTax);
            $('#omSaleDetailList' + idx + '_sumTransactionPriceTaxV').val(sumTransactionPriceTax);
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
            // 订单结算金额
            calcOrderSettleAmount();
        }

        function afterSelectOwner(row) {
            $("#omSaleDetailList").empty();
        }

        function afterSelectCustomer(row) {
            $("#omSaleDetailList").empty();
            if (row.ebcuType.indexOf("SETTLEMENT") !== -1) {
                $("#settlement").val(row.ebcuCustomerNo);
                $("#settlementName").val(row.ebcuNameCn);
            }
            $("#exchangeRate").val(math.bignumber(row.ebcuExchangeRate));
        }

        function uploadAnnex(annex) {
            if (!$("#id").val()) {
                jp.alert("请生成订单后再上传附件");
                return;
            }
            jp.open({
                type: 1,
                area: [500, 300],
                title: "上传附件",
                content: $("#upload").html(),
                btn: ['确定', '关闭'],
                btn1: function (index, layero) {
                    jp.loading('  正在上传，请稍等...');
                    let file = top.$("#uploadFile").get(0).files[0];
                    let fm = new FormData();
                    fm.append("file", file);
                    fm.append("annexType", annex);
                    fm.append("oldAnnex", $("#" + annex).val());
                    fm.append("id", $("#id").val());
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/oms/order/omSaleHeader/uploadAnnex",
                        data: fm,
                        async: false,
                        contentType: false,
                        processData: false,
                        success: function (result) {
                            if (result.success) {
                                $("#" + annex).val(result.body.annex);
                                jp.success(result.msg);
                                jp.close(index);
                            } else {
                                jp.bqError(result.msg);
                            }
                        },
                        error: function (result) {
                            jp.bqError(result.msg)
                        }
                    });
                },
                btn2: function (index, layero) {
                    jp.close(index);
                }
            });
        }

        function downloadAnnex(annex) {
            let httpRequest = new XMLHttpRequest();
            httpRequest.open("POST", "${ctx}/oms/order/omSaleHeader/downloadAnnex", true);
            httpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            httpRequest.responseType = "blob";
            httpRequest.onload = function (oEvent) {
                let content = httpRequest.response;
                let elink = document.createElement('a');
                elink.download = "故障单" + new Date().format("yyyyMMddhhmmss") + ".xlsx"; // 下载文件名
                elink.style.display = 'none';
                let blob = new Blob([content]);
                elink.href = URL.createObjectURL(blob);
                document.body.appendChild(elink);
                elink.click();
                document.body.removeChild(elink);
            };
            let json = {};
            json.path = $("#" + annex).val();
            httpRequest.send(JSON.stringify(json));
        }

        function wholeDiscount() {
            if (!$("#id").val()) {
                jp.alert("请生成订单");
                return;
            }
            jp.open({
                type: 1,
                area: ['350px', '200px'],
                title: "整单折扣率",
                content: $("#discountData").html(),
                btn: ['确定', '关闭'],
                btn1: function (index, layero) {
                    let $table = top.$("table");
                    let $wholeDiscountRate = $table.find("#wholeDiscountRate");
                    if (!$wholeDiscountRate.val() || $wholeDiscountRate.val() > 1 || $wholeDiscountRate.val() <= 0) {
                        jp.bqError("折扣率须大于0且小于等于1");
                        return;
                    }
                    let $status = $("#status");
                    if ("10" !== $status.val() && "20" !== $status.val()) {
                        jp.bqError("非新建/确认状态不能进行整单折扣调整");
                        return;
                    }
                    let discountRate = $table.find("#wholeDiscountRate").val();
                    $("#discountRate").val(discountRate);
                    $("#omSaleDetailList").find("tr").each(function () {
                        let prefix = $(this).attr("id");
                        let taxPrice = math.bignumber($("#" + prefix + "_taxPrice").val());
                        let qty = math.bignumber($("#" + prefix + "_qty").val());
                        let sumTransactionPriceTax = math.bignumber((taxPrice.mul(qty).mul(discountRate)).toFixed(2));
                        $("#" + prefix + "_discount").val(discountRate);
                        $("#" + prefix + "_sumTransactionPriceTax").val(sumTransactionPriceTax);
                        $("#" + prefix + "_sumTransactionPriceTaxV").val(sumTransactionPriceTax);
                    });
                    calcOrderSettleAmount();
                    jp.close(index);
                },
                btn2: function (index) {
                    jp.close(index);
                },
                success: function (layero) {
                    layero.find("#wholeDiscountRate").val($("#discountRate").val());
                }
            });
        }

        function calcOrderSettleAmount() {
            let $totalTaxInAmount = $("#totalTaxInAmount");
            let $couponAmount = $("#couponAmount");
            let $discountRate = $("#discountRate");
            let $freightCharge = $("#freightCharge");

            let totalTaxInAmount = math.bignumber(!$totalTaxInAmount.val() ? 0 : $totalTaxInAmount.val());
            let couponAmount = math.bignumber(!$couponAmount.val() ? 0 : $couponAmount.val());
            let discountRate = math.bignumber(!$discountRate.val() ? 1 : $discountRate.val());
            let freightCharge = math.bignumber(!$freightCharge.val() ? 0 : $freightCharge.val());
            let orderSettleAmount = ((totalTaxInAmount.sub(couponAmount)).mul(discountRate).add(freightCharge)).toFixed(2);
            let skuSettleAmount = ((totalTaxInAmount.sub(couponAmount)).mul(discountRate)).toFixed(2);
            $("#orderSettleAmount").val(math.bignumber(orderSettleAmount));
            $("#skuSettleAmount").val(math.bignumber(skuSettleAmount));
        }

        function removeZero() {
            let omSaleHeaderEntity = ${fns:toJson(omSaleHeaderEntity)};
            $("#exchangeRate").val(omSaleHeaderEntity.exchangeRate);// 汇率
            $("#freightCharge").val(omSaleHeaderEntity.freightCharge);// 运费金额
            $("#discountRate").val(omSaleHeaderEntity.discountRate);// 整单折扣
            $("#totalAmountV").val(omSaleHeaderEntity.totalAmount);// 合计金额
            $("#totalTaxV").val(omSaleHeaderEntity.totalTax);// 合计税额
            $("#totalTaxInAmountV").val(omSaleHeaderEntity.totalTaxInAmount);// 合计含税金额
            $("#actualReceivedAmount").val(omSaleHeaderEntity.actualReceivedAmount);// 实收金额
            $("#couponAmount").val(omSaleHeaderEntity.couponAmount);// 优惠券金额
            $("#orderSettleAmount").val(omSaleHeaderEntity.orderSettleAmount);// 订单结算金额
            $("#skuSettleAmount").val(omSaleHeaderEntity.skuSettleAmount);// 货物结算金额
        }
    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="omSaleHeaderEntity" method="post" class="form">
    <input type="hidden" id="priceType" value="S"/>
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="taxRate"/>
    <input id="priceType" value="S" type="hidden"/>
    <sys:message content="${message}"/>
    <div class="tabs-container" style="height: 420px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#orderInfo" aria-expanded="true">订单信息</a></li>
            <li class=""><a data-toggle="tab" href="#consigneeInfo" aria-expanded="true">收货信息</a></li>
            <li class=""><a data-toggle="tab" href="#shipperInfo" aria-expanded="true">发货信息</a></li>
            <li class=""><a data-toggle="tab" href="#logisticsInfo" aria-expanded="true">物流信息</a></li>
            <li class=""><a data-toggle="tab" href="#invoiceInfo" aria-expanded="true">开票信息</a></li>
            <li class=""><a data-toggle="tab" href="#settleInfo" aria-expanded="true">结算信息</a></li>
            <li class=""><a data-toggle="tab" href="#auditInfo" aria-expanded="true">审核信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="orderInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-8"><label class="pull-right">销售单号</label></td>
                        <td class="width-12">
                            <form:input path="saleNo" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>订单日期</label></td>
                        <td class="width-12">
                            <div class='input-group form_datetime' id='orderDate'>
                                <input id="orderDateValue" type='text' name="orderDate" class="form-control required"
                                       value="<fmt:formatDate value="${omSaleHeaderEntity.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>订单类型</label></td>
                        <td class="width-12">
                            <form:select path="saleType" class="form-control required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_SALE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right">订单状态</label></td>
                        <td class="width-12">
                            <form:select path="status" class="form-control" disabled="true">
                                <form:options items="${fns:getDictList('OMS_SALE_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>下发机构</label></td>
                        <td class="width-12">
                            <input id="parentId" type="hidden">
                            <sys:popSelect url="${ctx}/sys/office/companyData" title="选择仓库" cssClass="form-control required"
                                           fieldId="outWarhouse" fieldName="outWarhouse" fieldKeyName="id" fieldValue="${omSaleHeaderEntity.outWarhouse}"
                                           displayFieldId="outWarhouseName" displayFieldName="outWarhouseName" displayFieldKeyName="name"
                                           displayFieldValue="${omSaleHeaderEntity.outWarhouseName}"
                                           selectButtonId="outWarhouseSelectId" deleteButtonId="outWarhouseDeleteId"
                                           fieldLabels="仓库编码|仓库名称" fieldKeys="code|name"
                                           searchLabels="仓库编码|仓库名称" searchKeys="code|name"
                                           queryParams="id" queryParamValues="parentId">
                            </sys:popSelect>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>客户名称</label></td>
                        <td class="width-12">
                            <input id="customerType" value="CONSIGNEE" type="hidden">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择客户"
                                           allowInput="true" inputSearchKey="codeAndName" cssClass="form-control required"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                           displayFieldId="customerName" displayFieldName="customerName"
                                           displayFieldKeyName="ebcuNameCn" displayFieldValue="${omSaleHeaderEntity.customerName}"
                                           selectButtonId="customerSelectId" deleteButtonId="customerDeleteId"
                                           fieldLabels="客户编码|客户名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="客户编码|客户名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           concatId="customer,taxRate,exchangeRate,currency,clerkCode,clerkName,settlement,settlementName,vipStatus"
                                           concatName="ebcuCustomerNo,ebcuTaxRate,ebcuExchangeRate,ebcuCurrency,clerkCode,clerkName,ebcuCustomerNo,ebcuNameCn,vipStatus"
                                           queryParams="ebcuType" queryParamValues="customerType" afterSelect="afterSelectCustomer">
                            </sys:popSelect>
                        </td>
                        <td class="width-8"><label class="pull-right">户头编号</label></td>
                        <td class="width-12">
                            <form:input path="customer" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">会员级别</label></td>
                        <td class="width-12">
                            <form:select path="vipStatus" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_VIP_LEVEL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right">客户订单号</label></td>
                        <td class="width-12">
                            <form:input path="customerNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">销售方式</label></td>
                        <td class="width-12">
                            <form:select path="saleMethod" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_SALE_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>货主</label></td>
                        <td class="width-12">
                            <input id="ownerType" value="OWNER" type="hidden">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择货主"
                                           allowInput="true" inputSearchKey="codeAndName" cssClass="form-control required"
                                           fieldId="owner" fieldName="owner" fieldKeyName="ebcuCustomerNo" fieldValue="${omSaleHeaderEntity.owner}"
                                           displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn"
                                           displayFieldValue="${omSaleHeaderEntity.ownerName}"
                                           selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                           fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="ownerType" afterSelect="afterSelectOwner"/>
                        </td>
                        <td class="width-8"><label class="pull-right">业务员</label></td>
                        <td class="width-12">
                            <sys:popSelect url="${ctx}/oms/basic/omClerk/popData" title="选择业务员"
                                           allowInput="true" inputSearchKey="codeAndName" cssClass="form-control"
                                           fieldId="clerkCode" fieldName="clerkCode" fieldKeyName="clerkCode" fieldValue="${omSaleHeaderEntity.clerkCode}"
                                           displayFieldId="clerkName" displayFieldName="clerkName"
                                           displayFieldKeyName="clerkName" displayFieldValue="${omSaleHeaderEntity.clerkName}"
                                           selectButtonId="clerkSBtnId" deleteButtonId="clerkDBtnId"
                                           fieldLabels="业务员代码|业务员名称" fieldKeys="clerkCode|clerkName"
                                           searchLabels="业务员代码|业务员名称" searchKeys="clerkCode|clerkName"/>
                        </td>
                        <td class="width-8"><label class="pull-right">店铺名</label></td>
                        <td class="width-12">
                            <form:input path="shop" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">销售人</label></td>
                        <td class="width-12">
                            <form:input path="saleBy" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>渠道价格</label></td>
                        <td class="width-12">
                            <form:select path="channel" class="form-control required" onchange="channelChange()">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_CHANNEL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">汇率</label></td>
                        <td class="width-12">
                            <form:input path="exchangeRate" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                        <td class="width-8"><label class="pull-right">币种</label></td>
                        <td class="width-12">
                            <form:select path="currency" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_CURRENCY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right">项目</label></td>
                        <td class="width-12">
                            <sys:popSelect url="${ctx}/oms/basic/omProject/popData" title="选择项目"
                                           cssClass="form-control" allowInput="true" inputSearchKey="codeAndName"
                                           fieldId="project" fieldName="project" fieldKeyName="projectCode" fieldValue="${omSaleHeaderEntity.project}"
                                           displayFieldId="projectName" displayFieldName="projectName"
                                           displayFieldKeyName="projectName" displayFieldValue="${omSaleHeaderEntity.projectName}"
                                           selectButtonId="projectSelectId" deleteButtonId="projectDeleteId"
                                           fieldLabels="项目编码|项目名称" fieldKeys="projectCode|projectName"
                                           searchLabels="项目编码|项目名称" searchKeys="projectCode|projectName">
                            </sys:popSelect>
                        </td>
                        <td class="width-8"><label class="pull-right">校验库存充足</label></td>
                        <td class="width-12">
                            <form:select path="isAvailableStock" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8" ><label class="pull-right">备注信息</label></td>
                        <td class="width-12" colspan="9">
                            <form:input path="remarks" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="consigneeInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>收货人</label></td>
                        <td class="width-12">
                            <form:input path="consignee" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">联系电话</label></td>
                        <td class="width-12">
                            <form:input path="consigneeTel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-8"><label class="pull-right">收货人区域</label></td>
                        <td class="width-12">
                            <sys:treeselect title="选择收货人区域" url="/sys/area/treeData" cssClass="form-control"
                                            id="consigneeArea" name="consigneeArea" value="${omSaleHeaderEntity.consigneeArea}"
                                            labelName="area.name" labelValue="${omSaleHeaderEntity.consigneeAreaName}"/>
                        </td>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>收货人地址</label></td>
                        <td class="width-12">
                            <form:input path="consigneeAddress" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">收货人地址区域</label></td>
                        <td class="width-12">
                            <form:input path="consigneeAddressArea" htmlEscape="false" class="form-control" maxlength="128"/>
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
                            <form:input path="shipper" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">联系电话</label></td>
                        <td class="width-12">
                            <form:input path="shipperTel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-8"><label class="pull-right">发货人区域</label></td>
                        <td class="width-12">
                            <sys:treeselect title="选择发货人区域" url="/sys/area/treeData" cssClass="form-control"
                                            id="shipperArea" name="shipperArea" value="${omSaleHeaderEntity.shipperArea}"
                                            labelName="area.name" labelValue="${omSaleHeaderEntity.shipperAreaName}"/>
                        </td>
                        <td class="width-8"><label class="pull-right">发货人地址</label></td>
                        <td class="width-12">
                            <form:input path="shipperAddress" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">发货人地址区域</label></td>
                        <td class="width-12">
                            <form:input path="shipperAddressArea" htmlEscape="false" class="form-control" maxlength="128"/>
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
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择承运商" cssClass="form-control" allowInput="true" inputSearchKey="codeAndName"
                                           fieldId="carrier" fieldName="carrier" fieldKeyName="ebcuCustomerNo" fieldValue="${omSaleHeaderEntity.carrier}"
                                           displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn"
                                           displayFieldValue="${omSaleHeaderEntity.carrierName}"
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
                            <form:input path="vehicleNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">司机</label></td>
                        <td class="width-12">
                            <form:input path="driver" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">联系电话</label></td>
                        <td class="width-12">
                            <form:input path="contactTel" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">运费金额</label></td>
                        <td class="width-12">
                            <form:input path="freightCharge" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0);calcOrderSettleAmount();"/>
                        </td>
                        <td class="width-8">
                        <td class="width-12">
                        <td class="width-8">
                        <td class="width-12">
                        <td class="width-8">
                        <td class="width-12">
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="invoiceInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-8"><label class="pull-right">发票类型</label></td>
                        <td class="width-12">
                            <form:select path="invoiceType" cssClass="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('BMS_INVOICE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right">公司名称</label></td>
                        <td class="width-12">
                            <form:input path="companyName" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">纳税人识别号</label></td>
                        <td class="width-12">
                            <form:input path="taxpayerIdentityNumber" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">开户银行名称</label></td>
                        <td class="width-12">
                            <form:input path="depositBank" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">开户银行账号</label></td>
                        <td class="width-12">
                            <form:input path="depositBankAccount" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">注册地址</label></td>
                        <td class="width-12">
                            <form:input path="registeredArea" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">联系电话</label></td>
                        <td class="width-12">
                            <form:input path="registeredTelephone" htmlEscape="false" class="form-control" maxlength="20"/>
                        </td>
                        <td class="width-8"><label class="pull-right">收票人名称</label></td>
                        <td class="width-12">
                            <form:input path="ticketCollectorName" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">收票人手机</label></td>
                        <td class="width-12">
                            <form:input path="ticketCollectorPhone" htmlEscape="false" class="form-control" maxlength="20"/>
                        </td>
                        <td class="width-8"><label class="pull-right">收票人地址</label></td>
                        <td class="width-12">
                            <form:input path="ticketCollectorAddress" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
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
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择结算对象" cssClass="form-control required" allowInput="true" inputSearchKey="codeAndName"
                                           fieldId="settlement" fieldName="settlement" fieldKeyName="ebcuCustomerNo" fieldValue="${omSaleHeaderEntity.settlement}"
                                           displayFieldId="settlementName" displayFieldName="settlementName" displayFieldKeyName="ebcuNameCn"
                                           displayFieldValue="${omSaleHeaderEntity.settlementName}"
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
                        <td class="width-8"><label class="pull-right">付款时间</label></td>
                        <td class="width-12">
                            <div class='input-group form_datetime' id='payDate'>
                                <input type='text' name="payDate" class="form-control"
                                       value="<fmt:formatDate value="${omSaleHeaderEntity.payDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8">
                            <button id="discount" type="button" class="btn btn-success pull-right" onclick="wholeDiscount()">整单折扣</button>
                            <div id="discountData" class="hide">
                                <table style="margin: 0 auto">
                                    <tr>
                                        <td style="padding-top: 5px;"><label class="pull-right" style=""><font color="red">*</font>折扣率</label></td>
                                        <td style="padding-top: 5px;">
                                            <input id="wholeDiscountRate" onkeyup="bq.numberValidator(this, 8, 0);discountRateLimit(this)" class="form-control"/>
                                            <script type="text/javascript">
                                                function discountRateLimit(obj) {
                                                    if (parseFloat(obj.value) > 1) {
                                                        obj.value = "";
                                                    }
                                                }
                                            </script>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                        <td class="width-12">
                            <form:input path="discountRate" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">合计金额</label></td>
                        <td class="width-12">
                            <form:hidden path="totalAmount"/>
                            <input id="totalAmountV" class="form-control" readonly/>
                        </td>
                        <td class="width-8"><label class="pull-right">合计税额</label></td>
                        <td class="width-12">
                            <form:hidden path="totalTax"/>
                            <input id="totalTaxV" class="form-control" readonly/>
                        </td>
                        <td class="width-8"><label class="pull-right">合计含税金额</label></td>
                        <td class="width-12">
                            <form:hidden path="totalTaxInAmount"/>
                            <input id="totalTaxInAmountV" class="form-control" readonly/>
                        </td>
                        <td class="width-8"><label class="pull-right">实收金额</label></td>
                        <td class="width-12">
                            <form:input path="actualReceivedAmount" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">优惠券金额</label></td>
                        <td class="width-12">
                            <form:input path="couponAmount" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0);calcOrderSettleAmount();"/>
                        </td>
                        <td class="width-8"><label class="pull-right">货物结算金额</label></td>
                        <td class="width-12">
                            <form:input path="skuSettleAmount" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">订单结算金额</label></td>
                        <td class="width-12">
                            <form:input path="orderSettleAmount" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">收款账户</label></td>
                        <td class="width-12">
                            <form:input path="receiptAccount" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right">收款单号</label></td>
                        <td class="width-12">
                            <form:input path="receiptNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">出纳备注</label></td>
                        <td colspan="9">
                            <form:input path="cashierRemarks" htmlEscape="false" class="form-control"/>
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
                        <td class="width-8"><label class="pull-right">制单人</label></td>
                        <td class="width-12">
                            <form:input path="preparedBy" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">创建时间</label></td>
                        <td class="width-12">
                            <div class='input-group form_datetime'>
                                <input id='createDate' name="createDate" class="form-control" readonly
                                       value="<fmt:formatDate value="${omSaleHeaderEntity.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-8"><label class="pull-right">备注</label></td>
                        <td class="width-12">
                            <form:input path="preparedRemarks" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr style="border-top: 1px solid #ddd">
                        <td colspan="10"><label class="pull-left">APP审核信息</label></td>
                    </tr>
                    <tr style="border-bottom: 1px solid #ddd">
                        <td class="width-8"><label class="pull-right">审核人</label></td>
                        <td class="width-12">
                            <form:input path="appAuditBy" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">审核时间</label></td>
                        <td class="width-12">
                            <div class='input-group form_datetime'>
                                <input id='appAuditDate' name="appAuditDate" class="form-control" readonly
                                       value="<fmt:formatDate value="${omSaleHeaderEntity.appAuditDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-8">
                            <button type="button" class="pull-right btn btn-primary hide" onclick="uploadAnnex('appAnnex')">上传附件</button>
                        </td>
                        <td class="width-12">
                            <form:hidden path="appAnnex"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="10"><label class="pull-left">进销存系统审核信息</label></td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">机构</label></td>
                        <td class="width-12">
                            <form:hidden path="auditOrgId"/>
                            <form:input path="auditOrgName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">部门</label></td>
                        <td class="width-12">
                            <form:hidden path="auditDepartment"/>
                            <form:input path="auditDepartName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">审核人</label></td>
                        <td class="width-12">
                            <form:input path="auditBy" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">审核时间</label></td>
                        <td class="width-12">
                            <div class='input-group form_datetime'>
                                <input id='auditDate' name="auditDate" class="form-control" readonly
                                       value="<fmt:formatDate value="${omSaleHeaderEntity.auditDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-8">
                            <button type="button" class="pull-right btn btn-primary hide" onclick="uploadAnnex('annex')">上传附件</button>
                        </td>
                        <td class="width-12">
                            <form:hidden path="annex"/>
                        </td>
                    </tr>
                    <tr style="border-bottom: 1px solid #ddd">
                        <td class="width-8"><label class="pull-right">备注</label></td>
                        <td colspan="7">
                            <form:input path="auditRemarks" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-8"></td>
                        <td class="width-12"></td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">机构</label></td>
                        <td class="width-12">
                            <form:hidden path="updateOrgId"/>
                            <form:input path="updateOrgName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">部门</label></td>
                        <td class="width-12">
                            <form:hidden path="updateDepartment"/>
                            <form:input path="updateDepartName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">更新人</label></td>
                        <td class="width-12">
                            <form:input path="updateBy.name" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-8"><label class="pull-right">更新时间</label></td>
                        <td class="width-12">
                            <div class='input-group form_datetime'>
                                <input id='updateDate' name="updateDate" class="form-control" readonly
                                       value="<fmt:formatDate value="${omSaleHeaderEntity.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-8"></td>
                        <td class="width-12"></td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right">更新内容</label></td>
                        <td colspan="7">
                            <form:input path="updateContent" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-8"></td>
                        <td class="width-12"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="tabs-container">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">销售订单明细</a></li>
        </ul>
        <div class="tab-content" style="overflow-x: auto">
            <div id="tab-1" class="tab-pane fade in active">
                <shiro:hasPermission name="order:omSaleHeader:edit">
                    <c:if test="${omSaleHeaderEntity.status == '10' || omSaleHeaderEntity.status == '20' || omSaleHeaderEntity.status == null}">
                        <a class="btn btn-white btn-sm" onclick="addRow('#omSaleDetailList', omSaleDetailRowIdx, omSaleDetailTpl);omSaleDetailRowIdx = omSaleDetailRowIdx + 1;"
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
                        <th>属性</th>
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
                    <tbody id="omSaleDetailList">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<div id="upload" class="hide">
    <input id="uploadFile" name="file" type="file" style="width:330px"/><br/>　　
</div>
<script type="text/template" id="omSaleDetailTpl">//<!--
<tr id="omSaleDetailList{{idx}}">
    <td class="hide">
        <input id="omSaleDetailList{{idx}}_id" name="omSaleDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="omSaleDetailList{{idx}}_delFlag" name="omSaleDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="omSaleDetailList{{idx}}_ratio" name="omSaleDetailList[{{idx}}].ratio" type="hidden" value="{{row.ratio}}"/>
        <input id="omSaleDetailList{{idx}}_saleMultiple" name="omSaleDetailList[{{idx}}].saleMultiple" type="hidden" value="{{row.saleMultiple}}"/>
        <input id="omSaleDetailList{{idx}}_itemPriceId" name="omSaleDetailList[{{idx}}].itemPriceId" type="hidden" value="{{row.itemPriceId}}"/>
        <input id="omSaleDetailList{{idx}}_isAllowAdjustment" name="omSaleDetailList[{{idx}}].isAllowAdjustment" type="hidden" value="{{row.isAllowAdjustment}}"/>
    </td>
    <td width="10%">
        <sys:grid title="商品" url="${ctx}/oms/basic/omItemPrice/popData" cssClass="form-control required"
                  fieldId="" fieldName="" fieldKeyName="" readonly="true"
                  displayFieldId="omSaleDetailList{{idx}}_skuCode" displayFieldName="omSaleDetailList[{{idx}}].skuCode"
                  displayFieldKeyName="skuCode" displayFieldValue="{{row.skuCode}}"
                  fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                  searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                  queryParams="ownerCode|customerNo|channel|priceType|effectiveTime|expirationTime|orgId"
                  queryParamValues="owner|customer|channel|priceType|orderDateValue|orderDateValue|orgId"
                  afterSelect="skuSelect({{idx}})"/>
    </td>
    <td width="14%">
        <input id="omSaleDetailList{{idx}}_skuName" name="omSaleDetailList[{{idx}}].skuName" type="text" value="{{row.skuName}}" class="form-control" readonly/>
    </td>
    <td class="width-8">
        <input id="omSaleDetailList{{idx}}_spec" name="omSaleDetailList[{{idx}}].spec" type="text" value="{{row.spec}}" class="form-control" readonly/>
    </td>
    <td width="5%">
        <input id="omSaleDetailList{{idx}}_riceNum" name="omSaleDetailList[{{idx}}].riceNum" type="text" value="{{row.riceNum}}" class="form-control" readonly/>
    </td>
    <td width="5%">
        <select id="omSaleDetailList{{idx}}_auxiliaryUnit" name="omSaleDetailList[{{idx}}].auxiliaryUnit" data-value="{{row.auxiliaryUnit}}" class="form-control m-b" disabled>
            <c:forEach items="${fns:getDictList('OMS_UNIT')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td width="5%">
        <input id="omSaleDetailList{{idx}}_auxiliaryQty" name="omSaleDetailList[{{idx}}].auxiliaryQty" type="text" value="{{row.auxiliaryQty}}" class="form-control" readonly/>
    </td>
    <td width="5%">
        <select id="omSaleDetailList{{idx}}_unit" name="omSaleDetailList[{{idx}}].unit" data-value="{{row.unit}}" class="form-control m-b" disabled>
            <c:forEach items="${fns:getDictList('OMS_ITEM_UNIT')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td width="5%">
        <input id="omSaleDetailList{{idx}}_qty" name="omSaleDetailList[{{idx}}].qty" type="text" value="{{row.qty}}" class="form-control required" onkeyup="bq.numberValidator(this, 0, 0);qtyChange({{idx}})"/>
    </td>
    <td width="4%">
        <select id="omSaleDetailList{{idx}}_taxRate" name="omSaleDetailList[{{idx}}].taxRate" data-value="{{row.taxRate}}" class="form-control m-b required" disabled onchange="priceChange({{idx}}, '1')">
            <c:forEach items="${fns:getDictList('OMS_TAX_RATE')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td width="5%">
        <input id="omSaleDetailList{{idx}}_price" name="omSaleDetailList[{{idx}}].price" type="text" value="{{row.price}}" class="form-control required" readonly />
    </td>
    <td width="5%">
        <input id="omSaleDetailList{{idx}}_taxPrice" name="omSaleDetailList[{{idx}}].taxPrice" type="text" value="{{row.taxPrice}}" class="form-control required" readonly onkeyup="bq.numberValidator(this, 6);priceChange({{idx}}, '1');"/>
    </td>
    <td width="5%">
        <input id="omSaleDetailList{{idx}}_taxMoney" name="omSaleDetailList[{{idx}}].taxMoney" type="hidden" value="{{row.taxMoney}}" class="form-control taxMoney" readonly/>
        <input id="omSaleDetailList{{idx}}_taxMoneyV" type="text" value="{{row.taxMoney}}" class="form-control" readonly/>
    </td>
    <td width="5%">
        <input id="omSaleDetailList{{idx}}_amount" name="omSaleDetailList[{{idx}}].amount" type="hidden" value="{{row.amount}}" class="form-control amount" readonly/>
        <input id="omSaleDetailList{{idx}}_amountV" type="text" value="{{row.amount}}" class="form-control" readonly/>
    </td>
    <td width="5%">
        <input id="omSaleDetailList{{idx}}_taxAmount" name="omSaleDetailList[{{idx}}].taxAmount" type="hidden" value="{{row.taxAmount}}" class="form-control taxAmount" readonly/>
        <input id="omSaleDetailList{{idx}}_taxAmountV" type="text" value="{{row.taxAmount}}" class="form-control" readonly/>
    </td>
    <td width="4%">
        <input id="omSaleDetailList{{idx}}_discount" name="omSaleDetailList[{{idx}}].discount" type="text" value="{{row.discount}}" class="form-control" readonly onkeyup="bq.numberValidator(this, 8, 0);priceChange({{idx}}, '1');"/>
    </td>
    <td width="5%">
        <input id="omSaleDetailList{{idx}}_sumTransactionPriceTax" name="omSaleDetailList[{{idx}}].sumTransactionPriceTax" type="hidden" value="{{row.sumTransactionPriceTax}}" class="form-control" readonly/>
        <input id="omSaleDetailList{{idx}}_sumTransactionPriceTaxV" type="text" value="{{row.sumTransactionPriceTax}}" class="form-control" readonly/>
    </td>
    <td width="10%">
        <input id="omSaleDetailList{{idx}}_remarks" name="omSaleDetailList[{{idx}}].remarks" type="text" value="{{row.remarks}}" class="form-control"/>
    </td>
    <td class="text-center" width="10">
        {{#delBtn}}<span class="close" onclick="delRow(this, '#omSaleDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
    </td>
</tr>//-->
</script>
<script type="text/javascript">
    let omSaleDetailRowIdx = 0, omSaleDetailTpl = $("#omSaleDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $(document).ready(function () {
        let data = ${fns:toJson(omSaleHeaderEntity.omSaleDetailList)};
        let status = $('#status').val();
        for (let i = 0; i < data.length; i++) {
            addRow('#omSaleDetailList', omSaleDetailRowIdx, omSaleDetailTpl, data[i]);
            if (status && status !== '20' && status !== '10') {
                $('#omSaleDetailList' + omSaleDetailRowIdx + "_qty").prop("readonly", true);
                $('#omSaleDetailList' + omSaleDetailRowIdx + "_taxPrice").prop("readonly", true);
                $('#omSaleDetailList' + omSaleDetailRowIdx + "_taxRate").prop("disabled", true);
                $('#omSaleDetailList' + omSaleDetailRowIdx + "_discount").prop("readonly", true);
            } else {
                let isAllowEdit = $('#omSaleDetailList' + omSaleDetailRowIdx + "_isAllowAdjustment").val();
                if (isAllowEdit && isAllowEdit === 'N') {
                    $('#omSaleDetailList' + omSaleDetailRowIdx + "_qty").prop("readonly", true);
                    $('#omSaleDetailList' + omSaleDetailRowIdx + "_taxPrice").prop("readonly", true);
                    $('#omSaleDetailList' + omSaleDetailRowIdx + "_taxRate").prop("disabled", true);
                    $('#omSaleDetailList' + omSaleDetailRowIdx + "_discount").prop("readonly", true);
                } else {
                    $('#omSaleDetailList' + omSaleDetailRowIdx + "_qty").prop("readonly", false);
                    $('#omSaleDetailList' + omSaleDetailRowIdx + "_taxPrice").prop("readonly", false);
                    $('#omSaleDetailList' + omSaleDetailRowIdx + "_taxRate").prop("disabled", false);
                    $('#omSaleDetailList' + omSaleDetailRowIdx + "_discount").prop("readonly", false);
                }
            }
            omSaleDetailRowIdx = omSaleDetailRowIdx + 1;
        }
    });
</script>
</body>
</html>