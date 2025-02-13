<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var validateForm;
var $table; // 父页面table表格id
var $topIndex;//弹出窗口的 index
var barcodeRowIdx = 0, skuBarcodeTpl;
var supplierRowIdx = 0, skuSupplierTpl;
var locRowIdx = 0, skuLocTpl;
$(document).ready(function () {
    validateForm = $("#inputForm").validate({
        submitHandler: function (form) {
            jp.post("${ctx}/sys/common/sku/save", $('#inputForm').serialize(), function (data) {
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
    $('#dataSetName').prop('readonly', true);
    $('#dataSetNameSBtnId').prop('disabled', true);
    $('#dataSetNameDBtnId').prop('disabled', true);
    $("#filingTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#effectiveDate").datetimepicker({format: "YYYY-MM-DD"});
    $("#expirationDate").datetimepicker({format: "YYYY-MM-DD"});

    if ($('#id').val()) {
        $('#skuCode').prop('readonly', true);
        // 是否温控
        var isCold = '${sysCommonSku.isCold}';
        $('#isCold').prop('checked', isCold === 'Y');
        isColdControl(isCold === 'Y');
        // 是否危险品
        var isDg = '${sysCommonSku.isDg}';
        $('#isDg').prop('checked', isDg === 'Y');
        isDgControl(isDg === 'Y');
        // 是否做效期控制
        var isValidity = '${sysCommonSku.isValidity}';
        $('#isValidity').prop('checked', isValidity === 'Y');
        isValidityControl(isValidity === 'Y');
        // 是否允许超收
        var isOverRcv = '${sysCommonSku.isOverRcv}';
        $('#isOverRcv').prop('checked', isOverRcv === 'Y');
        isOverRcvControl(isOverRcv === 'Y');
        // 是否质检管理
        var isQc = '${sysCommonSku.isQc}';
        $('#isQc').prop('checked', isQc === 'Y');
        isQcControl(isQc === 'Y');
        // 是否序列号管理
        $('#isSerial').prop('checked', '${sysCommonSku.isSerial}' === 'Y').val('${sysCommonSku.isSerial}');
        // 是否父件
        $('#isParent').prop('checked', '${sysCommonSku.isParent}' === 'Y').val('${sysCommonSku.isParent}');
    } else {
        var $dataSet = ${fns:toJson(fns:getUserDataSet())};
        $('#dataSet').val($dataSet.code);
        $('#dataSetName').val($dataSet.name);
        $("#filingTime").val(jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"));
        // 是否温控
        isColdControl(false);
        // 是否危险品
        isDgControl(false);
        // 是否做效期控制
        isValidityControl(false);
        // 是否允许超收控制
        isOverRcvControl(false);
        // 是否质检管理控制
        isQcControl(false);
        // 是否序列号控制
        isSerialControl(false);
        // 是否父件控制
        isParentControl(false);
    }
    initBarcode(${fns:toJson(sysCommonSku.skuBarcodeList)})
    initSupplier(${fns:toJson(sysCommonSku.skuSupplierList)});
    initLoc(${fns:toJson(sysCommonSku.skuLocList)});
}

function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
    var validator = bq.headerSubmitCheck("#inputForm");
    if (!validator.isSuccess) {
        jp.warning(validator.msg);
        return false;
    }
    if (validateForm.form()) {
        $table = table;
        $topIndex = index;
        jp.loading();
        $("#inputForm").submit();
        return true;
    }
    return false;
}

function ownerSelect(row) {
    if (row) {
        $('#dataSet').val(row.dataSet);
    }
}

/**
 * 是否做效期控制
 */
function isValidityControl(flag) {
    $('#isValidity').val(flag ? "Y" : "N");
    $('#lifeType').prop('disabled', !flag);
    $('#inLifeDays').prop('readonly', !flag);
    $('#outLifeDays').prop('readonly', !flag);
}

/**
 * 是否危险品控制
 */
function isDgControl(flag) {
    $('#isDg').val(flag ? "Y" : "N");
    $('#dgClass').prop('disabled', !flag);
    $('#unno').prop('readonly', !flag);
}

/**
 * 是否温控控制
 */
function isColdControl(flag) {
    $('#isCold').val(flag ? "Y" : "N");
    $('#minTemp').prop('readonly', !flag);
    $('#maxTemp').prop('readonly', !flag);
}

/**
 * 是否序列号管理
 */
function isSerialControl(flag) {
    $('#isSerial').val(flag ? "Y" : "N");
}

/**
 * 是否父件控制
 */
function isParentControl(flag) {
    $('#isParent').val(flag ? "Y" : "N");
}

/**
 * 是否质检管理控制
 */
function isQcControl(flag) {
    $('#isQc').val(flag ? "Y" : "N");
    $('#qcPhase').prop('disabled', !flag);
    $('#qcRuleSBtnId').prop('disabled', !flag);
    $('#qcRuleDBtnId').prop('disabled', !flag);
    $('#qcRule').prop('readonly', !flag);
    $('#qcRuleName').prop('readonly', !flag);
    $('#itemGroupSBtnId').prop('disabled', !flag);
    $('#itemGroupDBtnId').prop('disabled', !flag);
    $('#itemGroupCode').prop('readonly', !flag);
    $('#itemGroupName').prop('readonly', !flag);
    if (flag) {
        $('#qcPhase').addClass('required');
        $('#qcRuleName').addClass('required');
        $('#itemGroupName').addClass('required');
        $('#qcPhaseLabel').html(getStartStyle() + $('#qcPhaseLabel').text());
        $('#qcRuleLabel').html(getStartStyle() + $('#qcRuleLabel').text());
        $('#itemGroupLabel').html(getStartStyle() + $('#itemGroupLabel').text());
    } else {
        $('#qcPhase').removeClass('required');
        $('#qcRuleName').removeClass('required');
        $('#itemGroupName').removeClass('required');
        $('.myStart').remove();
    }
}

/**
 * 是否允许超收控制
 */
function isOverRcvControl(flag) {
    $('#isOverRcv').val(flag ? "Y" : "N");
    $('#overRcvPct').prop('readonly', !flag);
}

function getStartStyle() {
    return '<font class="myStart" color="red">*</font>';
}

function isDefaultChange(list, idx) {
    $(list).find("input[type='checkbox']").each(function () {
        if ($(this).prop('name').indexOf('isDefault') !== -1) {
            $(this).prop('checked', false).val('N');
        }
    });
    $(list + idx + '_isDefault').prop('checked', true).val('Y');
}

function supplierAfterSelect(row, idx) {
    if (row) {
        $('#skuSupplierList' + idx + '_supplierName').val(row.name);
    }
}

function addRow(list, idx, tpl, row) {
    $(list).append(Mustache.render(tpl, {
        idx: idx, delBtn: true, row: row
    }));
    $(list + idx).find("select").each(function () {
        $(this).val($(this).attr("data-value"));
    });
    $(list + idx).find("input[type='checkbox']").each(function () {
        if (!$(this).val()) {
            $(this).val('N');
            return;
        }
        $(this).prop("checked", ("Y" === $(this).val()));
    });
    $(list + idx).find(".form_datetime").each(function () {
        $(this).datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    });
    $(list + idx).find("input[name='btSelectItem']").on('click', function () {
        var $table = $(this).parents("table").eq(0);
        $table.find("input[name='btSelectAll']").prop('checked', $table.find("input[name='btSelectItem']").not("input:checked").length <= 0);
    });
}

function delRow(list, url) {
    jp.confirm('确认要删除选中记录吗？', function () {
        jp.loading();
        var ids = [];// 获取选中行ID
        var idxs = [];// 获取选中行索引
        $.map($(list).find("tr input[type='checkbox']:checked"), function ($element) {
            var idx = $($element).data("index");
            idxs.push(idx);
            var id = $(list + idx + "_id").val();
            if (id) {
                ids.push(id);
            }
        });
        del = function (indexs) {// 页面表格删除
            $.map(indexs, function (idx) {
                $(list + idx).remove();
            });
            jp.success("操作成功");
        };
        if (url && ids.length > 0) {
            jp.post(url, {ids: ids.join(',')}, function (data) {
                if (data.success) {
                    del(idxs);
                } else {
                    jp.error(data.msg);
                }
            });
        } else {
            del(idxs);
        }
    });
}

function initBarcode(rows) {
    skuBarcodeTpl = $("#skuBarcodeTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $("#skuBarcodeTable").find("input[name='btSelectAll']").on('click', function () {// 表格全选复选框绑定事件
        $(this).parents("table").eq(0).find("input[name='btSelectItem']").prop('checked', $(this).prop('checked'));
    });
    if (rows === undefined || rows.length <= 0) return;
    $("#skuBarcodeList").empty();

    barcodeRowIdx = 0;
    for (var i = 0; i < rows.length; i++) {
        addBarcode(rows[i]);
    }
}

function addBarcode(row) {
    if (row === undefined) {
        row = {recVer: 0};
    }
    addRow('#skuBarcodeList', barcodeRowIdx, skuBarcodeTpl, row);
    barcodeRowIdx = barcodeRowIdx + 1;
}

function delBarcode() {
    delRow('#skuBarcodeList', '${ctx}/sys/common/sku/barcode/deleteAll');
}

function initSupplier(rows) {
    skuSupplierTpl = $("#skuSupplierTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $("#skuSupplierTable").find("input[name='btSelectAll']").on('click', function () {// 表格全选复选框绑定事件
        $(this).parents("table").eq(0).find("input[name='btSelectItem']").prop('checked', $(this).prop('checked'));
    });
    if (rows === undefined || rows.length <= 0) return;
    $("#skuSupplierList").empty();

    supplierRowIdx = 0;
    for (var i = 0; i < rows.length; i++) {
        addSupplier(rows[i]);
    }
}

function addSupplier(row) {
    if (row === undefined) {
        row = {recVer: 0};
    }
    addRow('#skuSupplierList', supplierRowIdx, skuSupplierTpl, row);
    supplierRowIdx = supplierRowIdx + 1;
}

function delSupplier() {
    delRow('#skuSupplierList', '${ctx}/sys/common/sku/supplier/deleteAll');
}

function initLoc(rows) {
    skuLocTpl = $("#skuLocTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $("#skuLocTable").find("input[name='btSelectAll']").on('click', function () {// 表格全选复选框绑定事件
        $(this).parents("table").eq(0).find("input[name='btSelectItem']").prop('checked', $(this).prop('checked'));
    });
    if (rows === undefined || rows.length <= 0) return;
    $("#skuLocList").empty();

    locRowIdx = 0;
    for (var i = 0; i < rows.length; i++) {
        addLoc(rows[i]);
    }
}

function addLoc(row) {
    if (row === undefined) {
        row = {recVer: 0};
    }
    addRow('#skuLocList', locRowIdx, skuLocTpl, row);
    locRowIdx = locRowIdx + 1;
}

function delLoc() {
    delRow('#skuLocList', '${ctx}/sys/common/sku/loc/deleteAll');
}

function checkboxChange(flag, obj) {
    $(obj).val(flag ? 'Y' : 'N');
}
</script>