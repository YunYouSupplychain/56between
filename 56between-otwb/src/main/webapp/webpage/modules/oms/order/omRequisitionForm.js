<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var omRequisitionDetailRowIdx = 0, omRequisitionDetailTpl;
$(document).ready(function () {
    omRequisitionDetailTpl = $("#omRequisitionDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $('#orderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#planShipTime').datetimepicker({format: "YYYY-MM-DD HH:mm"});
    $('#planArrivalTime').datetimepicker({format: "YYYY-MM-DD HH:mm"});
    $('#createDate').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#auditDate').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#updateDate').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initV();
    editControl();
    $('#omRequisitionDetailForm input[name="btSelectAll"]').on('click', function () {
        $('#omRequisitionDetailForm input[name="btSelectItem"]').prop('checked', $('#omRequisitionDetailForm input[name="btSelectAll"]').eq(0).prop('checked'));
    });
    top.layer.closeAll('dialog');// 关闭所有的信息框
});

function initV() {
    if (!$('#id').val()) {
        $("#orgId").val(jp.getCurrentOrg().orgId);
        $("#status").val("10");
    } else {
        initDetail(${fns:toJson(omRequisitionEntity.omRequisitionDetailList)});
    }
}

function editControl() {
    var isNew = !$('#id').val();
    var status = $('#status').val();
    if (status === "10") {
        $('#save').removeAttr('disabled');
        if (isNew) {
            $('#audit').attr('disabled', true);
            $('#cancelAudit').attr('disabled', true);
            $('#createTask').attr('disabled', true);
            $('#close').attr('disabled', true);
            $('#cancel').attr('disabled', true);
        } else {
            $('#audit').removeAttr('disabled');
            $('#cancelAudit').attr('disabled', true);
            $('#createTask').attr('disabled', true);
            $('#close').attr('disabled', true);
            $('#cancel').removeAttr('disabled');
        }
    } else if (status === "20" || status === "35") {
        $('#save').attr('disabled', true);
        $('#audit').attr('disabled', true);
        $('#cancelAudit').removeAttr('disabled');
        $('#createTask').removeAttr('disabled');
        $('#close').attr('disabled', true);
        $('#cancel').attr('disabled', true);
    } else if (status === "40") {
        $('#save').attr('disabled', true);
        $('#audit').attr('disabled', true);
        $('#cancelAudit').attr('disabled', true);
        $('#createTask').attr('disabled', true);
        $('#close').removeAttr('disabled');
        $('#cancel').attr('disabled', true);
    }
    if (!isNew && status === "10") {
        $('#addDetail').removeAttr('disabled');
        $('#saveDetail').removeAttr('disabled');
        $('#removeDetail').removeAttr('disabled');
    } else {
        $('#addDetail').attr('disabled', true);
        $('#saveDetail').attr('disabled', true);
        $('#removeDetail').attr('disabled', true);
    }
}

/*表格增加行*/
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

/*表格删除行*/
function delRow(list, url) {
    jp.confirm('确认要删除选中记录吗？', function () {
        jp.loading();
        var ids = [];// 获取选中行ID
        var idxs = [];// 获取选中行索引
        $.map($(list).find("input[name='btSelectItem']:checked"), function ($element) {
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

function ownerSelect(row) {
    if (row) {
        $('#ownerName').val(row.ebcuNameCn);
    }
}

function carrierSelect(row) {
    if (row) {
    }
}

function skuSelect(row, idx) {
    if (row) {
        $('#omRequisitionDetailList' + idx + '_skuName').val(row.skuName);
        $('#omRequisitionDetailList' + idx + '_skuSpec').val(row.spec);
    }
}

function save() {
    jp.loading();
    var validator = bq.headerSubmitCheck('#inputForm');
    if (!validator.isSuccess) {
        jp.warning(validator.msg);
        return;
    }
    var objs = bq.openDisabled("#inputForm");
    var params = $('#inputForm').serialize();
    bq.closeDisabled(objs);
    jp.post("${ctx}/oms/order/omRequisition/save", params, function (data) {
        if (data.success) {
            jp.success(data.msg);
            jp.loading("加载数据...");
            window.location = "${ctx}/oms/order/omRequisition/form?id=" + data.body.entity.id;
        } else {
            jp.error(data.msg);
        }
    });
}

function audit() {
    jp.loading();
    var id = $('#id').val();
    jp.get("${ctx}/oms/order/omRequisition/audit?ids=" + id, function (data) {
        if (data.success) {
            jp.success(data.msg);
            jp.loading("加载数据...");
            window.location = "${ctx}/oms/order/omRequisition/form?id=" + id;
        } else {
            jp.error(data.msg);
        }
    });
}

function cancelAudit() {
    jp.loading();
    var id = $('#id').val();
    jp.get("${ctx}/oms/order/omRequisition/cancelAudit?ids=" + id, function (data) {
        if (data.success) {
            jp.success(data.msg);
            jp.loading("加载数据...");
            window.location = "${ctx}/oms/order/omRequisition/form?id=" + id;
        } else {
            jp.error(data.msg);
        }
    });
}

function closeOrder() {
    jp.loading();
    var id = $('#id').val();
    jp.get("${ctx}/oms/order/omRequisition/close?ids=" + id, function (data) {
        if (data.success) {
            jp.success(data.msg);
            jp.loading("加载数据...");
            window.location = "${ctx}/oms/order/omRequisition/form?id=" + id;
        } else {
            jp.error(data.msg);
        }
    });
}

function cancelOrder() {
    jp.loading();
    var id = $('#id').val();
    jp.get("${ctx}/oms/order/omRequisition/cancel?ids=" + id, function (data) {
        if (data.success) {
            jp.success(data.msg);
            jp.loading("加载数据...");
            window.location = "${ctx}/oms/order/omRequisition/form?id=" + id;
        } else {
            jp.error(data.msg);
        }
    });
}

function getNewLineNo() {
    var maxLineNo = 0, digits = 4, tbl = [];
    $.map($('#omRequisitionDetailList .lineNo'), function (obj) {
        var lineNo = Number($(obj).val());
        if (maxLineNo < lineNo) {
            maxLineNo = lineNo;
        }
    });
    var newLineNo = maxLineNo + 1;
    return (0 >= (digits = digits - newLineNo.toString().length)) ? newLineNo : (tbl[digits] || (tbl[digits] = Array(digits + 1).join(0))) + newLineNo;
}

function initDetail(rows) {
    if (rows === undefined || rows.length <= 0) return;
    $('#omRequisitionDetailList').empty();
    omRequisitionDetailRowIdx = 0;
    for (var i = 0; i < rows.length; i++) {
        addDetail(rows[i]);
    }
}

function addDetail(row) {
    if (row === undefined) {
        row = {reqNo: $('#reqNo').val(), lineNo: getNewLineNo(), ownerCode: $('#ownerCode').val(), orgId: $('#orgId').val(), recVer: 0};
    }
    addRow('#omRequisitionDetailList', omRequisitionDetailRowIdx, omRequisitionDetailTpl, row);
    omRequisitionDetailRowIdx = omRequisitionDetailRowIdx + 1;
}

function saveDetail() {
    jp.loading();
    var validator = bq.tableValidate('#omRequisitionDetailForm');
    if (!validator.isSuccess){
        jp.warning(validator.msg);
        return;
    }
    var id = $('#id').val();
    var objs = bq.openDisabled("#omRequisitionDetailForm");
    var params = $('#omRequisitionDetailForm').serialize() + "&id=" + id;
    bq.closeDisabled(objs);
    jp.post("${ctx}/oms/order/omRequisition/detail/save", params, function (data) {
        if (data.success) {
            initDetail(data.body.entity.omRequisitionDetailList);
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

function removeDetail() {
    delRow('#omRequisitionDetailList', "${ctx}/oms/order/omRequisition/detail/deleteAll");
}

function createTask() {
    jp.loading();
    var selectItems = $('#omRequisitionDetailList').find("input[name='btSelectItem']:checked");
    if (selectItems.length <= 0) {
        jp.warning("请选择记录");
        return;
    }
    var params = {id: $('#id').val(), omRequisitionDetailList: []};
    for (var i = 0; i < selectItems.length; i++) {
        var $el = selectItems[i];
        var idx = $($el).data("index");
        var id = $("#omRequisitionDetailList" + idx + "_id").val();
        var lineNo = $("#omRequisitionDetailList" + idx + "_lineNo").val();
        var qty = Number($("#omRequisitionDetailList" + idx + "_qty").val());
        var taskQty = Number($("#omRequisitionDetailList" + idx + "_taskQty").val());
        var planTaskQty = Number($("#omRequisitionDetailList" + idx + "_planTaskQty").val());
        if (planTaskQty === 0) {
            continue;
        }
        if (qty - taskQty < planTaskQty) {
            jp.warning("行号【" + lineNo + "】计划任务量超出订单量");
            return;
        }
        params.omRequisitionDetailList.push({id: id, planTaskQty: planTaskQty});
    }
    if (params.omRequisitionDetailList.length === 0) {
        jp.warning("已全部生成任务");
        return;
    }
    $.ajax({
        url: "${ctx}/oms/order/omRequisition/detail/createTask",
        method: "post",
        contentType: "application/json; charset=UTF-8",
        data: JSON.stringify(params),
        success: function (data) {
            if (data.success) {
                jp.success(data.msg);
                jp.loading("加载数据...");
                window.location = "${ctx}/oms/order/omRequisition/form?id=" + $('#id').val();
            } else {
                jp.error(data.msg);
            }
        },
        error: function () {
            jp.error("操作异常");
        }
    });
}
</script>
