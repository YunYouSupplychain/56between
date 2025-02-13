<%@ page contentType="text/html;charset=UTF-8"%>
<script>
var tmDemandPlanDetailRowIdx = 0, tmDemandPlanDetailTpl;
$(document).ready(function () {
    tmDemandPlanDetailTpl = $("#tmDemandPlanDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $("#orderTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#arrivalTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initV();
    editController();

    /*表格全选复选框绑定事件*/
    $("#demandPlanDetailTable").find("input[name='btSelectAll']").on('click', function () {
        $(this).parents("table").eq(0).find("input[name='btSelectItem']").prop('checked', $(this).prop('checked'));
    });
    initDemandPlanDetail(${fns:toJson(tmDemandPlanEntity.tmDemandPlanDetailList)});
});

/*初始化值*/
function initV() {
    if ($("#id").val().length > 0) return;
    $("#orgId").val(jp.getCurrentOrg().orgId);
    $("#baseOrgId").val(tmOrg.id);
    $("#status").val("00");// 新建
    $("#orderTime input").val(jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"));
}

/*编辑控制*/
function editController() {
    var isNew = $("#id").val().length <= 0;// 是否是新增未保存数据
    var status = $("#status").val();// 状态
    if ("00" === status) {// 新建
        $("#save").attr('disabled', false);
        $("#demandPlanDetail_add").attr('disabled', isNew);
        $("#demandPlanDetail_save").attr('disabled', isNew);
        $("#demandPlanDetail_remove").attr('disabled', isNew);
    } else if ("10" === status) {// 已处理
        $("#save").attr('disabled', true);
        $("#demandPlanDetail_add").attr('disabled', true);
        $("#demandPlanDetail_save").attr('disabled', true);
        $("#demandPlanDetail_remove").attr('disabled', true);
    }
    $("#ownerName").prop('readonly', !isNew);
    $("#ownerNameSBtnId").prop('disabled', !isNew);
    $("#ownerNameDBtnId").prop('disabled', !isNew);
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

/*保存单头*/
function save() {
    jp.loading();
    var validator = bq.headerSubmitCheck("#inputForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var objs = bq.openDisabled("#inputForm");
    jp.post("${ctx}/tms/order/tmDemandPlan/save", $('#inputForm').serialize(), function (data) {
        bq.closeDisabled(objs);
        if (data.success) {
            jp.success(data.msg);
            window.location = "${ctx}/tms/order/tmDemandPlan/form?id=" + data.body.entity.id;
        } else {
            jp.bqError(data.msg);
        }
    });
}

/*初始化明细*/
function initDemandPlanDetail(rows) {
    if (rows === undefined || rows.length <= 0) return;
    $("#tmDemandPlanDetailList").empty();

    tmDemandPlanDetailRowIdx = 0;
    for (var i = 0; i < rows.length; i++) {
        addDetail(rows[i]);
    }
}

/*新增明细*/
function addDetail(row) {
    if (row === undefined) {
        row = {planOrderNo: $("#planOrderNo").val(), ownerCode: $("#ownerCode").val(), orgId: $("#orgId").val(), recVer: 0, baseOrgId: $("#baseOrgId").val()};
    }
    addRow('#tmDemandPlanDetailList', tmDemandPlanDetailRowIdx, tmDemandPlanDetailTpl, row);
    tmDemandPlanDetailRowIdx = tmDemandPlanDetailRowIdx + 1;
}

/*保存明细*/
function saveDetail() {
    jp.loading();
    var validator = bq.tableValidate("#demandPlanDetailForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var planOrderNo = $("#planOrderNo").val();
    var orgId = $("#orgId").val();

    var params = bq.serializeJson($('#demandPlanDetailForm'));
    params.planOrderNo = planOrderNo;
    params.orgId = orgId;
    jp.post("${ctx}/tms/order/tmDemandPlan/detail/save", params, function (data) {
        if (data.success) {
            jp.post("${ctx}/tms/order/tmDemandPlan/detail/data", {planOrderNo: planOrderNo, orgId: orgId}, function (data) {
                initDemandPlanDetail(data);
            });
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    });
}

/*删除明细*/
function delDetail() {
    delRow('#tmDemandPlanDetailList', '${ctx}/tms/order/tmDemandPlan/detail/delete');
}

/*订单明细商品选择后事件*/
function demandPlanDetailSkuSelect(data, idx) {
    if (!data) return;
    $("#tmDemandPlanDetailList" + idx + "_skuName").val(data.skuName);
}

</script>