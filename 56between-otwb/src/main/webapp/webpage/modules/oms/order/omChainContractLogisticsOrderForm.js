<%@ page contentType="text/html;charset=UTF-8" %>
<script type="text/javascript">
var omChainDetailRowIdx = 0, omChainDetailTpl;
function doSubmit($table, $topIndex) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
    jp.loading();
    var validate = bq.headerSubmitCheck("#inputForm");
    if (!validate.isSuccess) {
        jp.alert(validate.msg);
        return;
    }
    var disabledObjs = bq.openDisabled('#inputForm');
    var params = $('#inputForm').serialize();
    bq.closeDisabled(disabledObjs);
    jp.post("${ctx}/oms/order/omChainContractLogisticsOrder/save", params, function (data) {
        if (data.success) {
            $table.bootstrapTable('refresh');
            jp.success(data.msg);
            jp.close($topIndex);//关闭dialog
        } else {
            jp.alert(data.msg);
        }
    })
}

$(document).ready(function () {
    omChainDetailTpl = $("#omChainDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    init();
    initDetail();
});

function init() {
    if ($('#sourceOrderType').val()) {
        editControl(true);
    } else {
        editControl(false);
    }
    $('#orderDate').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#validityPeriod').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#arrivalTime').datetimepicker({format: "YYYY-MM-DD HH:mm"});
    $("#deliveryDate").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#auditDate").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    if (!$('#orgId').val()) {
        $('#status').val("00");
        $('#orgId').val(jp.getCurrentOrg().orgId);
        $('#warehouse').val(jp.getCurrentOrg().orgId);
        $('#warehouseName').val(jp.getCurrentOrg().orgName);
        $('#orderDateValue').val(jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"));
        $('#orderSource').val("IMP");
    }
    $('#parentId').val($('#orgId').val());
    removeZero();

    $("#customerDeleteId").unbind("click").click(function(e){
        $("#customer").val('');
        $("#customerName").val('');
        var businessOrderType = $('#businessOrderType').val();
        if ("2" == businessOrderType || "3" == businessOrderType) {
            clearDetails();
        }
    });
    $("#supplierDeleteId").unbind("click").click(function(e){
        $("#supplierCode").val('');
        $("#supplierName").val('');
        var businessOrderType = $('#businessOrderType').val();
        if ("1" == businessOrderType || "4" == businessOrderType) {
            clearDetails();
        }
    });
}

function initDetail() {
    var data = ${fns:toJson(omChainHeaderEntity.omChainDetailList)};
    var status = $('#status').val();
    for (var i = 0; i < data.length; i++) {
        addDetail(data[i]);
    }
}

function editControl(value) {
    $('#businessOrderType').prop("disabled", value);
    $('#channel').prop("disabled", value);
}

function addDetail(row) {
    if (row === undefined) {
        row = {"orgId": $('#orgId').val(), "recVer": 0};
    }
    addRow('#omChainDetailList', omChainDetailRowIdx, omChainDetailTpl, row);
    omChainDetailRowIdx = omChainDetailRowIdx + 1;
}

function addRow(list, idx, tpl, row) {
    if (!$('#businessOrderType').val()) {
        jp.alert("请选择订单类型");
        return;
    }
    if (!$('#owner').val()) {
        jp.alert("请选择货主");
        return;
    }
    var businessOrderType = $('#businessOrderType').val();
    var supplierCode = $('#supplierCode').val();
    var customer = $('#customer').val();
    $(list).append(Mustache.render(tpl, {
        idx: idx, delBtn: true, row: row
    }));
    $(list + idx).find("select").each(function () {
        $(this).val($(this).attr("data-value"));
    });
    $(list + idx).find(".form_datetime").each(function () {
        $(this).datetimepicker({
            format: "YYYY-MM-DD HH:mm:ss"
        });
    });
}

function delRow(obj, prefix) {
    var id = $(prefix + "_id");
    var delFlag = $(prefix + "_delFlag");
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

function skuSelectAfter(row, idx) {
    if (row) {
        $('#omChainDetailList' + idx + '_skuName').val(row.skuName);
        $('#omChainDetailList' + idx + '_spec').val(row.spec);
        $('#omChainDetailList' + idx + '_unit').val(row.unit);
        $('#omChainDetailList' + idx + '_auxiliaryUnit').val(row.auxiliaryUnit);
    }
}

function skuSelect(idx) {
    var businessOrderType = $("#businessOrderType").val();
    var params = "?orgId=" + jp.getCurrentOrg().orgId + "|ownerCode=" + $('#owner').val() + "|channel=" + $('#channel').val() + "|effectiveTime=" + $('#orderDateValue').val() + "|expirationTime=" + $('#orderDateValue').val();
    if ("1" == businessOrderType || "4" == businessOrderType) {
        params = params + "|customerNo=" + $('#supplierCode').val() + "|priceType=P";
    } else if ("2" == businessOrderType || "3" == businessOrderType) {
        params = params + "|customerNo=" + $('#customer').val() + "|priceType=S";
    }
    top.layer.open({
        type: 2,
        area: ['800px', '500px'],
        title: "选择商品",
        auto: true,
        name: 'friend',
        content: "${ctx}/tag/grid?url=" + encodeURIComponent("${ctx}/oms/basic/omItemPrice/popData" + params) + "&fieldLabels=" + encodeURIComponent("商品编码|商品名称") + "&fieldKeys=" + encodeURIComponent("skuCode|skuName") + "&searchLabels=" + encodeURIComponent("商品编码|商品名称") + "&searchKeys=" + encodeURIComponent("skuCode|skuName") + "&isMultiSelected=false",
        btn: ['确定', '关闭'],
        yes: function (index, layero) {
            var iframeWin = layero.find('iframe')[0].contentWindow;
            var items = iframeWin.getSelections();
            if (items === "") {
                jp.alert("必须选择一条数据!");
                return;
            }
            // 清除
            skuDelete(idx);
            var data = items[0];
            // SKU信息
            $('#omChainDetailList' + idx + "_skuCode").val(data.skuCode);
            $('#omChainDetailList' + idx + "_skuName").val(data.skuName);
            $('#omChainDetailList' + idx + "_spec").val(data.spec);
            // 价格信息
            $('#omChainDetailList' + idx + '_itemPriceId').val(data.id);
            $('#omChainDetailList' + idx + '_auxiliaryUnit').val(data.auxiliaryUnit);
            $('#omChainDetailList' + idx + '_unit').val(data.unit);
            $('#omChainDetailList' + idx + '_discount').val(data.discount);
            $('#omChainDetailList' + idx + '_taxPrice').val(data.taxPrice);
            $("#omChainDetailList" + idx + "_taxRate").prop("disabled", ("Y" != data.isAllowAdjustment));
            $("#omChainDetailList" + idx + "_taxPrice").prop("readonly", ("Y" != data.isAllowAdjustment));
            $("#omChainDetailList" + idx + "_discount").prop("readonly", ("Y" != data.isAllowAdjustment));
            if (data.taxRate == null || data.taxRate == undefined || data.taxRate == '') {
                var businessOrderType = $('#businessOrderType').val();
                if ("1" == businessOrderType || "4" == businessOrderType) {
                    $("#omChainDetailList" + idx + "_taxRate").val($("#supplierTaxRate").val());
                } else {
                    $("#omChainDetailList" + idx + "_taxRate").val($("#ownerTaxRate").val());
                }
            } else {
                $("#omPoDetailList" + idx + "_taxRate").val(data.taxRate);
            }
            $('#omChainDetailList' + idx + '_ratio').val(data.convertRatio);
            top.layer.close(index);
        },
        cancel: function (index) {
        }
    });
}

function skuDelete(idx) {
    $("input[id^=omChainDetailList" + idx + "]").each(function () {
        var $Id = $(this).attr("id");
        var delFlag = "omChainDetailList" + idx + "_delFlag";
        var ratio = "omChainDetailList" + idx + "_ratio";
        if ($Id == delFlag) {
            $('#' + $Id).val('0');
        } else if ($Id == ratio) {
            $('#' + $Id).val('1');
        } else {
            $('#' + $Id).val('');
        }
    });
    $("select[id^=omChainDetailList" + idx + "]").each(function () {
        var $Id = $(this).attr("id");
        $('#' + $Id).val('');
    });
}

function clearDetails() {
    // $("#omChainDetailList").empty();
    $("tr[id^='omChainDetailList']").each(function () {
        var row = $(this).prop("id");
        var id = $("#" + row + "_id");
        if (id.val() === "") {
            $(this).remove();
        } else {
            $("#" + row + "_delFlag").val("1");
            $(this).addClass("error");
        }
        $(this).hide();
    });
}

function businessOrderTypeChange() {
    // $("#omChainDetailList").empty();
    clearDetails();
}

function qtyChange(idx) {
    var qtyValue = $('#omChainDetailList' + idx + '_qty').val();
    var ratioValue = !$('#omChainDetailList' + idx + '_ratio').val() ? 0 : $('#omChainDetailList' + idx + '_ratio').val();

    var qty = math.bignumber(!qtyValue ? 0 : qtyValue);
    var ratio = math.bignumber(!ratioValue ? 0 : ratioValue);
    var auxiliaryQty = math.bignumber((qty.mul(ratio)).toFixed(4));
    $('#omChainDetailList' + idx + '_auxiliaryQty').val(auxiliaryQty.toFixed(2));

    priceChange(idx, 1);
}

function priceChange(idx, flag) {
    // 税率
    var taxRateValue = $('#omChainDetailList' + idx + '_taxRate').val();
    var taxRate = math.bignumber(!taxRateValue ? 0 : taxRateValue);
    // 折扣
    var discountValue = $('#omChainDetailList' + idx + '_discount').val();
    var discount = math.bignumber(!discountValue ? 0 : discountValue);
    // 数量
    var qtyValue = $('#omChainDetailList' + idx + '_qty').val();
    var qty = math.bignumber(!qtyValue ? 0 : qtyValue);
    // 含税单价、单价
    var taxPriceValue = $('#omChainDetailList' + idx + '_taxPrice').val();
    var priceValue = $('#omChainDetailList' + idx + '_price').val();
    var taxPrice = math.bignumber(!taxPriceValue ? 0 : taxPriceValue);
    var price = math.bignumber(!priceValue ? 0 : priceValue);
    if (flag == '1') {
        price = math.bignumber((taxPrice.sub(taxPrice.mul(taxRate))).toFixed(6));
        $('#omChainDetailList' + idx + '_price').val(price);
    } else if (flag == '2') {
        taxPrice = math.bignumber((price.mul(taxRate.add(1))).toFixed(6));
        $('#omChainDetailList' + idx + '_taxPrice').val(taxPrice);
    }
    // 金额
    // var amount = math.format(math.chain(price).multiply(qty).round(2).done());
    var amount = math.bignumber((price.mul(qty)).toFixed(2));
    $('#omChainDetailList' + idx + '_amount').val(amount);
    $('#omChainDetailList' + idx + '_amountV').val(amount);
    // 含税金额
    // var taxAmount = math.format(math.chain(taxPrice).multiply(qty).round(2).done());
    var taxAmount = math.bignumber((taxPrice.mul(qty)).toFixed(2));
    $('#omChainDetailList' + idx + '_taxAmount').val();
    $('#omChainDetailList' + idx + '_taxAmountV').val(taxAmount);
    // 税金
    // var taxMoney = math.format(math.chain(taxRate).multiply(qty).multiply(price).round(2).done());
    // var taxMoney = math.format(math.chain(taxAmount).subtract(amount).round(2).done());
    var taxMoney = math.bignumber((taxAmount.sub(amount)).toFixed(2));
    $('#omChainDetailList' + idx + '_taxMoney').val(taxMoney);
    $('#omChainDetailList' + idx + '_taxMoneyV').val(taxMoney);
    // 成交价税合计
    // var sumTransactionPriceTax = math.format(math.chain(taxPrice).multiply(qty).multiply(discount).round(2).done());
    var sumTransactionPriceTax = math.bignumber((taxPrice.mul(qty).mul(discount)).toFixed(2));
    $('#omChainDetailList' + idx + '_sumTransactionPriceTax').val(sumTransactionPriceTax);
    $('#omChainDetailList' + idx + '_sumTransactionPriceTaxV').val(sumTransactionPriceTax);
    // 统计合计金额
    countTotal();
}

function countTotal() {
    var amount = math.bignumber(0), taxMoney = math.bignumber(0), taxAmount = math.bignumber(0);
    var $amountArr = $(".amount");
    var $taxMoneyArr = $(".taxMoney");
    var $taxAmountArr = $(".taxAmount");

    $.map($amountArr, function (obj) {
        amount = amount.add($(obj).val());
    });
    $.map($taxMoneyArr, function (obj) {
        taxMoney = taxMoney.add($(obj).val());
    });
    $.map($taxAmountArr, function (obj) {
        taxAmount = taxAmount.add($(obj).val());
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

function allCheckBoxClick() {
    $(".listCheckBox").prop("checked", $("#allCheckBox").is(':checked'));
}

function checkboxClick() {
    $("#allCheckBox").prop("checked", $(".listCheckBox:checked").length > 0);
}

function afterSelectOwner(row) {
    clearDetails();
}

function afterSelectCustomer(row) {
    $("#def2").val(row.brand);
    $("#def3").val(row.majorClass);
    $("#def4").val(row.ebcuEbplCityCode);
    $("#def5").val(row.ebcuIndustryType);
    $("#def6").val(row.rangeType);
    var businessOrderType = $('#businessOrderType').val();
    if ("2" == businessOrderType || "3" == businessOrderType) {
        clearDetails();
    }
}

function afterSelectSupplier(row) {
    var businessOrderType = $('#businessOrderType').val();
    if ("1" == businessOrderType || "4" == businessOrderType) {
        clearDetails();
    }
}

function removeZero() {
    var omChainHeaderEntity = ${fns:toJson(omChainHeaderEntity)};
    $("#exchangeRate").val(omChainHeaderEntity.exchangeRate);// 汇率
    $("#freightCharge").val(omChainHeaderEntity.freightCharge);// 运费金额
    $("#totalAmountV").val(omChainHeaderEntity.totalAmount);// 合计金额
    $("#totalTaxV").val(omChainHeaderEntity.totalTax);// 合计税额
    $("#totalTaxInAmountV").val(omChainHeaderEntity.totalTaxInAmount);// 合计含税金额
}

</script>
