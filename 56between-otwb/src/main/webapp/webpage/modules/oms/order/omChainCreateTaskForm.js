<%@ page contentType="text/html;charset=UTF-8" %>
<script type="text/javascript">
var omChainDetailRowIdx = 0, omChainDetailTpl;
$(document).ready(function () {
    omChainDetailTpl = $("#omChainDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    init();
});

function init() {
    if (!$('#orgId').val()) {
        $('#orgId').val(jp.getCurrentOrg().orgId);
    }
    $('#parentId').val($('#orgId').val());
    initDetail(${fns:toJson(omChainHeaderEntity.omChainDetailList)});
}

function initDetail(omChainDetailList) {
    $('#omChainDetailList').empty();
    omChainDetailRowIdx = 0;
    for (var i = 0; i < omChainDetailList.length; i++) {
        addRow('#omChainDetailList', omChainDetailRowIdx, omChainDetailTpl, omChainDetailList[i]);
        $('#omChainDetailList' + omChainDetailRowIdx + "_qty").prop("readonly", true);
        omChainDetailRowIdx = omChainDetailRowIdx + 1;
    }
}

function addRow(list, idx, tpl, row) {
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

function save($topTable, $topIndex) {
    jp.loading();
    if ($(".listCheckBox:checked").length <= 0) {
        jp.alert("请选择记录");
        return;
    }
    var warehouse = $('#warehouse').val();
    if (warehouse == '' || warehouse == null) {
        jp.alert("请选择下发仓库");
        return;
    }

    var disabledObjs = bq.openDisabled('#inputForm');
    // 选中的明细生成作业任务
    var entity = getFormData($('#inputForm'));
    $(".listCheckBox").each(function () {
        // 剔除非选中明细
        if (!$(this).prop("checked")) {
            var row = $(this).prop("name").split('.')[0];
            entity = removeUncheckedDetail(entity, row);
        }
    });
    bq.closeDisabled(disabledObjs);
    submit(entity, $topTable, $topIndex);
}

function saveAll($topTable, $topIndex) {
    jp.loading();
    var warehouse = $('#warehouse').val();
    if (warehouse == '' || warehouse == null) {
        jp.alert("请选择下发仓库");
        return;
    }

    var disabledObjs = bq.openDisabled('#inputForm');
    var entity = getFormData($('#inputForm'));
    bq.closeDisabled(disabledObjs);
    submit(entity, $topTable, $topIndex);
}

function submit(entity, $topTable, $topIndex) {
    jp.post("${ctx}/oms/order/omChainCreateTask/createTask", entity, function (data) {
        if (data.success) {
            jp.success(data.msg);
            if (data.body.hasOwnProperty("omChainDetailList") && data.body.omChainDetailList.length > 0) {
                initDetail(data.body.omChainDetailList);
            } else {
                $topTable.bootstrapTable('refresh');
                jp.close($topIndex);
            }
        } else {
            jp.alert(data.msg);
        }
    });
}

function planTaskQtyChange(idx) {
    var $planTaskQty = $('#omChainDetailList' + idx + "_planTaskQty");
    var $qty = $('#omChainDetailList' + idx + "_qty");
    var $taskQty = $('#omChainDetailList' + idx + "_taskQty");
    var $availableQty = $('#omChainDetailList' + idx + "_availableQty");

    var planTaskQty = Number($planTaskQty.val());
    var taskQty = Number($taskQty.val());
    var qty = Number($qty.val());
    var availableQty = Number($availableQty.val());
    if (planTaskQty + taskQty > qty) {
        $planTaskQty.val(0);
        jp.alert("计划任务数量超出订单数量");
    }
    if ($('#isHaveSo').val() == 'Y') {
        if ($('#isAvailableStock').val() == 'Y' && planTaskQty > availableQty) {
            $planTaskQty.val(0);
            jp.alert("库存可用数量不足");
        }
    }
}

function allCheckBoxClick() {
    $(".listCheckBox").prop("checked", $("#allCheckBox").is(':checked'));
}

function checkboxClick() {
    $("#allCheckBox").prop("checked", $(".listCheckBox:checked").length > 0);
}

function removeUncheckedDetail(omChainHeaderEntity, row) {
    for (var key in omChainHeaderEntity) {
        if (key.indexOf("omChainDetailList") !== -1 && key.indexOf(row) !== -1) {
            delete omChainHeaderEntity[key];
        }
    }
    return omChainHeaderEntity;
}

/**
 * 表单数据转JSON
 */
function getFormData($form) {
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};
    $.map(unindexed_array, function (n, i) {
        indexed_array[n['name']] = n['value'];
    });
    return indexed_array;
}

/**
 * 选择下发机构后的回调方法
 */
function afterSelectOffice() {
    // 选择下发机构之后，出库类型的订单，应重新刷新库存可用数量。
    if ($('#isHaveSo').val() !== 'Y') {
        return;
    }
    var disabledObjs = bq.openDisabled('#inputForm');
    var entity = getFormData($('#inputForm'));
    bq.closeDisabled(disabledObjs);
    jp.post("${ctx}/oms/order/omChainCreateTask/resetAvailableQty", entity, function (data) {
        if (data.success) {
            $('#omChainDetailList').empty();
            omChainDetailRowIdx = 0;
            var details = data.body.entity.omChainDetailList;
            var status = $('#status').val();
            for (var i = 0; i < details.length; i++) {
                addRow('#omChainDetailList', omChainDetailRowIdx, omChainDetailTpl, details[i]);
                if (status && status !== '10') {
                    $('#omChainDetailList' + omChainDetailRowIdx + "_qty").prop("readonly", true);
                }
                omChainDetailRowIdx = omChainDetailRowIdx + 1;
            }
        } else {
            jp.alert(data.msg);
        }
    });
}
</script>