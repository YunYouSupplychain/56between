<%@ page contentType="text/html;charset=UTF-8"%>
<script>
var tmTransportOrderSkuRowIdx = 0, tmTransportOrderSkuTpl;
var tmTransportOrderCostRowIdx = 0, tmTransportOrderCostTpl;
$(document).ready(function () {
    tmTransportOrderSkuTpl = $("#tmTransportOrderSkuTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    tmTransportOrderCostTpl = $("#tmTransportOrderCostTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $("#orderTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#planArriveTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#signTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#receiptTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initV();
    editController();

    /*表格全选复选框绑定事件*/
    $("#orderDetailTable,#orderCostTable").find("input[name='btSelectAll']").on('click', function () {
        $(this).parents("table").eq(0).find("input[name='btSelectItem']").prop('checked', $(this).prop('checked'));
    });

    $("#shipCityIdName").on('focus', function () {
        var $shipCityName = $("#shipCityName");
        if ($shipCityName.val() !== $(this).val()){
            $shipCityName.val($(this).val());
        }
    });
    $("#consigneeCityIdName").on('focus', function () {
        var $consigneeCityName = $("#consigneeCityName");
        if ($consigneeCityName.val() !== $(this).val()){
            $consigneeCityName.val($(this).val());
        }
    });

    initOrderDetail(${fns:toJson(tmPreTransportOrderEntity.tmTransportOrderSkuList)});
});

/*初始化值*/
function initV() {
    if ($("#id").val().length > 0) return;
    $("#orgId").val(jp.getCurrentOrg().orgId);
    $("#baseOrgId").val(tmOrg.id);
    $("#orderType").val("1");// 正常
    $("#orderStatus").val("00");// 创建
    $("#orderTime input").val(jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"));
    $("#transportMethod").val("3");// 陆运
    $("#dataSource").val("00");// 手工单
    $("#orderDelivery\\.virtualBillingStatus").val("N");
    $("#orderDelivery\\.isException").val("N");
    $("#orderDelivery\\.receiptStatus").val("N");
    $("#orderDelivery\\.signStatus").val("N");
}

/*编辑控制*/
function editController() {
    var isNew = $("#id").val().length <= 0;// 是否是新增未保存数据
    $("#saveOrder").attr('disabled', false);
    $("#orderDetail_add").attr('disabled', isNew);
    $("#orderDetail_save").attr('disabled', isNew);
    $("#orderDetail_remove").attr('disabled', isNew);
    $("#customerName").prop('readonly', !isNew);
    $("#customerNameSBtnId").prop('disabled', !isNew);
    $("#customerNameDBtnId").prop('disabled', !isNew);
    $("#orgName").prop('readonly', !isNew);
    $("#orgNameSBtnId").prop('disabled', !isNew);
    $("#orgNameDBtnId").prop('disabled', !isNew);
    $("#orderCost_add").attr('disabled', isNew);
    $("#orderCost_save").attr('disabled', isNew);
    $("#orderCost_remove").attr('disabled', isNew);
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

/*客户选择*/
function customerSelect(data) {
    if (data && data.hasOwnProperty("transportObjType") && data.transportObjType.indexOf("CONSIGNEE")) {
        $('#consigneeCode').val(data.transportObjCode);
        $('#consigneeName').val(data.transportObjName);
        consigneeSelect(data);
    }
}

/*发货人选择*/
function shipSelect(data) {
    if (data) {
        $("#shipCityIdId").val(data.areaId);
        $("#shipCityIdName").val(data.area);
        $("#shipCityName").val(data.area);
        $("#shipper").val(data.contact);
        $("#shipperTel").val(data.phone);
        $("#shipAddress").val(data.address);
    }
}

/*收货人选择*/
function consigneeSelect(data) {
    if (data) {
        $("#consigneeCityIdId").val(data.areaId);
        $("#consigneeCityIdName").val(data.area);
        $("#consigneeCityName").val(data.area);
        $("#consignee").val(data.contact);
        $("#consigneeTel").val(data.phone);
        $("#signBy").val(data.signBy);
        $("#consigneeAddress").val(data.address);
    }
}

/*保存运输订单基本信息、运输信息、配送信息*/
function saveOrder() {
    jp.loading();
    var validator = bq.headerSubmitCheck("#inputForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var objs = bq.openDisabled("#inputForm");
    jp.post("${ctx}/tms/order/tmPreTransportOrder/save", $('#inputForm').serialize(), function (data) {
        bq.closeDisabled(objs);
        if (data.success) {
            jp.success(data.msg);
            window.location = "${ctx}/tms/order/tmPreTransportOrder/form?id=" + data.body.entity.id;
        } else {
            jp.bqError(data.msg);
        }
    });
}

/*初始化明细*/
function initOrderDetail(rows) {
    if (rows === undefined || rows.length <= 0) return;
    $("#tmTransportOrderSkuList").empty();

    tmTransportOrderSkuRowIdx = 0;
    for (var i = 0; i < rows.length; i++) {
        addOrderDetail(rows[i]);
    }
}

/*新增明细*/
function addOrderDetail(row) {
    if (row === undefined) {
        row = {transportNo: $("#transportNo").val(), ownerCode: $("#customerCode").val(), orgId: $("#orgId").val(), recVer: 0, baseOrgId: $("#baseOrgId").val(), skuWeight: 0, skuCubic: 0};
    }
    addRow('#tmTransportOrderSkuList', tmTransportOrderSkuRowIdx, tmTransportOrderSkuTpl, row);
    tmTransportOrderSkuRowIdx = tmTransportOrderSkuRowIdx + 1;
}

/*保存明细*/
function saveOrderDetail() {
    jp.loading();
    var validator = bq.tableValidate("#orderDetailForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var transportNo = $("#transportNo").val();
    var orgId = $("#orgId").val();

    var params = bq.serializeJson($('#orderDetailForm'));
    params.transportNo = transportNo;
    params.orgId = orgId;
    jp.post("${ctx}/tms/order/pre/transport/sku/save", params, function (data) {
        if (data.success) {
            jp.post("${ctx}/tms/order/pre/transport/sku/data", {transportNo: transportNo, orgId: orgId}, function (data) {
                initOrderDetail(data);
            });
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    });
}

/*删除明细*/
function delOrderDetail() {
    delRow('#tmTransportOrderSkuList', '${ctx}/tms/order/pre/transport/sku/delete');
}

/*订单明细商品选择后事件*/
function orderDetailSkuSelect(data, idx) {
    if (!data) return;
    $("#tmTransportOrderSkuList" + idx + "_skuName").val(data.skuName);
    if (data.grossweight) {
        $("#orderDetail" + idx + "_skuWeight").val(data.grossweight);
    }
    if (data.cubic) {
        $("#orderDetail" + idx + "_skuCubic").val(data.cubic);
    }
    orderDetailSkuQtyChange(idx);
}

/*订单明细商品数量Change事件*/
function orderDetailSkuQtyChange(idx) {
    var qty = $("#tmTransportOrderSkuList" + idx + "_qty").val();
    if (!qty) {
        qty = 0;
    } else {
        qty = Number(qty);
    }
    var weight = Number($("#orderDetail" + idx + "_skuWeight").val());
    var cubic = Number($("#orderDetail" + idx + "_skuCubic").val());
    $("#tmTransportOrderSkuList" + idx + "_weight").val(qty.mul(weight));
    $("#tmTransportOrderSkuList" + idx + "_cubic").val(qty.mul(cubic));
}

</script>