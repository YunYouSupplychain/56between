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
    jp.post("${ctx}/oms/order/omChainHeader/save", params, function (data) {
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
});

function init() {
    $('#orderDate').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#arrivalTime').datetimepicker({format: "YYYY-MM-DD HH:mm"});
    $("#deliveryDate").datetimepicker({format: "YYYY-MM-DD HH:mm"});
    $("#payDate").datetimepicker({format: "YYYY-MM-DD HH:mm"});
    $("#auditDate").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    if (!$('#id').val()) {
        $('#status').val("00");
        $('#orderSource').val("99");
        $('#orgId').val(jp.getCurrentOrg().orgId);
        $('#warehouse').val(jp.getCurrentOrg().orgId);
        $('#warehouseName').val(jp.getCurrentOrg().orgName);
        $('#orderDate input').val(jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"));
    }
    $('#parentId').val($('#orgId').val());
    removeZero();
    initDetail();
}

function initDetail() {
    var data = ${fns:toJson(omChainHeaderEntity.omChainDetailList)};
    for (var i = 0; i < data.length; i++) {
        addDetail(data[i]);
    }
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
    $(list).append(Mustache.render(tpl, {idx: idx, delBtn: true, row: row}));
    $(list + idx).find("select").each(function () {
        $(this).val($(this).attr("data-value"));
    });
    $(list + idx).find(".form_datetime").each(function () {
        $(this).datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
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

function clearDetails() {
    $("#omChainDetailList").empty();
}

function businessOrderTypeChange() {
    $("#omChainDetailList").empty();
}

function qtyChange(idx) {
    priceChange(idx);
}

function priceChange(idx) {
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
    var taxPrice = math.bignumber(!taxPriceValue ? 0 : taxPriceValue);
    var price = math.bignumber((taxPrice.sub(taxPrice.mul(taxRate))).toFixed(6));
    $('#omChainDetailList' + idx + '_price').val(price);
    // 金额
    var amount = math.bignumber((price.mul(qty)).toFixed(2));
    $('#omChainDetailList' + idx + '_amount').val(amount);
    $('#omChainDetailList' + idx + '_amountV').val(amount);
    // 含税金额
    var taxAmount = math.bignumber((taxPrice.mul(qty)).toFixed(2));
    $('#omChainDetailList' + idx + '_taxAmount').val();
    $('#omChainDetailList' + idx + '_taxAmountV').val(taxAmount);
    // 税金
    var taxMoney = math.bignumber((taxAmount.sub(amount)).toFixed(2));
    $('#omChainDetailList' + idx + '_taxMoney').val(taxMoney);
    $('#omChainDetailList' + idx + '_taxMoneyV').val(taxMoney);
    // 成交价税合计
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
        amount = amount.add(!$(obj).val()?0:$(obj).val());
    });
    $.map($taxMoneyArr, function (obj) {
        taxMoney = taxMoney.add(!$(obj).val()?0:$(obj).val());
    });
    $.map($taxAmountArr, function (obj) {
        taxAmount = taxAmount.add(!$(obj).val()?0:$(obj).val());
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

function removeZero() {
    var omChainHeaderEntity = ${fns:toJson(omChainHeaderEntity)};
    $("#exchangeRate").val(omChainHeaderEntity.exchangeRate);// 汇率
    $("#freightCharge").val(omChainHeaderEntity.freightCharge);// 运费金额
    $("#totalAmountV").val(omChainHeaderEntity.totalAmount);// 合计金额
    $("#totalTaxV").val(omChainHeaderEntity.totalTax);// 合计税额
    $("#totalTaxInAmountV").val(omChainHeaderEntity.totalTaxInAmount);// 合计含税金额
}

</script>
