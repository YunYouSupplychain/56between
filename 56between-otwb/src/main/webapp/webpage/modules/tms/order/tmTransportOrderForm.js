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

    initOrderDetail(${fns:toJson(tmTransportOrderEntity.tmTransportOrderSkuList)});
    initOrderCost(${fns:toJson(tmTransportOrderEntity.tmTransportOrderCostList)});
    initOrderLabel();
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
    var orderStatus = $("#orderStatus").val();// 状态
    if ("00" === orderStatus) {// 新建
        $("#saveOrder").attr('disabled', false);
        $("#audit").attr('disabled', isNew);
        $("#orderDetail_add").attr('disabled', isNew);
        $("#orderDetail_save").attr('disabled', isNew);
        $("#orderDetail_remove").attr('disabled', isNew);
    } else if ("10" === orderStatus) {// 已审核
        $("#addLabel").attr('disabled', false);
        $("#cancelAudit").attr('disabled', false);
        $("#receive").attr('disabled', false);
    } else if ("25" === orderStatus) {// 部分收货
        $("#addLabel").attr('disabled', false);
        $("#receive").attr('disabled', false);
        $("#cancelReceive").attr('disabled', false);
        $("#sign").attr('disabled', false);
        $("#receipt").attr('disabled', false);
    } else if ("30" === orderStatus) {// 全部收货
        $("#cancelReceive").attr('disabled', false);
        $("#addLabel").attr('disabled', false);
        $("#sign").attr('disabled', false);
    } else if ("35" === orderStatus) {// 部分签收
        $("#addLabel").attr('disabled', false);
        $("#receive").attr('disabled', false);
        $("#cancelReceive").attr('disabled', false);
        $("#sign").attr('disabled', false);
        $("#receipt").attr('disabled', false);
    } else if ("40" === orderStatus) {// 全部签收
        $("#addLabel").attr('disabled', false);
        $("#receipt").attr('disabled', false);
    } else if ("45" === orderStatus) {// 部分回单
        $("#addLabel").attr('disabled', false);
        $("#receive").attr('disabled', false);
        $("#cancelReceive").attr('disabled', false);
        $("#sign").attr('disabled', false);
        $("#receipt").attr('disabled', false);
    } else if ("50" === orderStatus) {// 全部回单

    } else if ("90" === orderStatus) {// 取消

    } else if ("99" === orderStatus) {// 关闭

    }
    $("#customerName").prop('readonly', !isNew);
    $("#customerNameSBtnId").prop('disabled', !isNew);
    $("#customerNameDBtnId").prop('disabled', !isNew);
    $("#orgName").prop('readonly', !isNew);
    $("#orgNameSBtnId").prop('disabled', !isNew);
    $("#orgNameDBtnId").prop('disabled', !isNew);
    $("#orderCost_add").attr('disabled', isNew);
    $("#orderCost_save").attr('disabled', isNew);
    $("#orderCost_remove").attr('disabled', isNew);
    $("#orderLabel_show").attr('disabled', isNew);
    $("#annex").attr('disabled', isNew);
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
    jp.post("${ctx}/tms/order/tmTransportOrder/save", $('#inputForm').serialize(), function (data) {
        bq.closeDisabled(objs);
        if (data.success) {
            jp.success(data.msg);
            window.location = "${ctx}/tms/order/tmTransportOrder/form?id=" + data.body.entity.id;
        } else {
            jp.bqError(data.msg);
        }
    });
}

/*审核*/
function audit() {
    jp.loading();
    var id = $("#id").val();
    jp.post("${ctx}/tms/order/tmTransportOrder/audit", {ids: id}, function (data) {
        if (data.success) {
            window.location = "${ctx}/tms/order/tmTransportOrder/form?id=" + id;
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

/*取消审核*/
function cancelAudit() {
    jp.loading();
    var id = $("#id").val();
    jp.post("${ctx}/tms/order/tmTransportOrder/cancelAudit", {ids: id}, function (data) {
        if (data.success) {
            window.location = "${ctx}/tms/order/tmTransportOrder/form?id=" + id;
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

/*打开添加标签模态框*/
function openAddLabelModal() {
    $("#addLabelForm input").val('');
    $('#addLabel_transportNo').val($('#transportNo').val());
    $('#addLabel_orgId').val($('#orgId').val());
    $("#addLabelModal").modal({backdrop: 'static', keyboard: false});
}

/*添加标签*/
function addLabel() {
    jp.loading();
    var validator = bq.headerSubmitCheck("#addLabelForm");
    if (!validator.isSuccess) {
        jp.warning(validator.msg);
        return;
    }
    var id = $("#id").val();
    var labelQty = $("#addLabelForm #labelQty").val();
    var lineNo = $("#lineNo").val();
    var skuCode = $("#addLabelForm #skuCode").val();
    var totalQty = Number($("#addLabelForm #totalQty").val());
    var totalWeight = Number($("#addLabelForm #totalWeight").val());
    var totalCubic = Number($("#addLabelForm #totalCubic").val());
    var data = {ids: id, lineNo: lineNo, skuCode: skuCode, labelQty: labelQty, totalQty: totalQty, totalWeight: totalWeight, totalCubic: totalCubic};
    jp.post("${ctx}/tms/order/transport/label/add", data, function (data) {
        if (data.success) {
            $("#addLabelModal").modal('hide');
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

function afterSelectSku(row) {
    $('#lineNo').val(row.lineNo);
}

/*揽收*/
function receive() {
    jp.loading();
    var id = $("#id").val();
    jp.post("${ctx}/tms/order/tmTransportOrder/receive", {ids: id}, function (data) {
        window.location = "${ctx}/tms/order/tmTransportOrder/form?id=" + id;
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

/*取消揽收*/
function cancelReceive() {
    jp.loading();
    var id = $("#id").val();
    jp.post("${ctx}/tms/order/tmTransportOrder/cancelReceive", {ids: id}, function (data) {
        window.location = "${ctx}/tms/order/tmTransportOrder/form?id=" + id;
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

/*打开签收模态框*/
function openSignModal() {
    $("#signForm input").val('');
    $("#sign_signTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#signModal").modal({backdrop: 'static', keyboard: false});
}

/*签收*/
function sign() {
    jp.loading();
    var validator = bq.headerSubmitCheck("#signForm");
    if (!validator.isSuccess) {
        jp.warning(validator.msg);
        return;
    }
    var id = $("#id").val();
    var signBy = $("#sign_signBy").val();
    var signTime = $("#sign_signTime").find("input").val();
    var remarks = $("#sign_remarks").val();
    var data = {signBy: signBy, signTime: signTime, remarks: remarks};
    jp.post("${ctx}/tms/order/tmTransportOrder/sign?ids=" + id, data, function (data) {
        if (data.success) {
            $("#signModal").modal('hide');
            window.location = "${ctx}/tms/order/tmTransportOrder/form?id=" + id;
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

/*打开回单模态框*/
function openReceiptModal() {
    $("#receiptForm input").val('');
    $("#receipt_receiptTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#receiptModal").modal({backdrop: 'static', keyboard: false});
}

/*回单*/
function receipt() {
    jp.loading();
    var validator = bq.headerSubmitCheck("#receiptForm");
    if (!validator.isSuccess) {
        jp.warning(validator.msg);
        return;
    }
    var id = $("#id").val();
    var receiptBy = $("#receipt_receiptBy").val();
    var receiptTime = $("#receipt_receiptTime").find("input").val();
    var remarks = $("#receipt_remarks").val();
    var data = {receiptBy: receiptBy, receiptTime: receiptTime, remarks: remarks};
    jp.post("${ctx}/tms/order/tmTransportOrder/receipt?ids=" + id, data, function (data) {
        if (data.success) {
            $("#receiptModal").modal('hide');
            window.location = "${ctx}/tms/order/tmTransportOrder/form?id=" + id;
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
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
    jp.post("${ctx}/tms/order/transport/sku/save", params, function (data) {
        if (data.success) {
            jp.post("${ctx}/tms/order/transport/sku/data", {transportNo: transportNo, orgId: orgId}, function (data) {
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
    delRow('#tmTransportOrderSkuList', '${ctx}/tms/order/transport/sku/delete');
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

/*初始化标签*/
function initOrderLabel() {
    var $orderLabelTable = $("#orderLabelTable");
    $orderLabelTable.bootstrapTable('destroy');
    $orderLabelTable.bootstrapTable({
        cache: false,//是否使用缓存，默认为true
        pagination: true,//是否显示分页
        sidePagination: "server",//分页方式：client客户端分页，server服务端分页
        queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
            var searchParam = {};
            searchParam.transportNo = $("#transportNo").val();
            searchParam.orgId = $("#orgId").val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        formatNoMatches: function () {
            return "";
        },
        columns: [{
            checkbox: true
        }, {
            field: 'labelNo',
            title: '标签',
            sortable: true
        }, {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        }, {
            field: 'skuName',
            title: '商品名称',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_ORDER_LABEL_STATUS'))}, value, "-");
            }
        }, {
            field: 'qty',
            title: '数量',
            sortable: true
        }, {
            field: 'weight',
            title: '重量',
            sortable: true
        }, {
            field: 'cubic',
            title: '体积',
            sortable: true
        }]
    });
    $orderLabelTable.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $orderLabelTable.bootstrapTable('getSelections').length;
        $('#orderLabel_del').attr('disabled', !length);
    });
}

/*查看标签*/
function showLabel() {
    $("#orderLabelTable").bootstrapTable('refresh', {'url': '${ctx}/tms/order/transport/label/page'});
}

function delLabel() {
    jp.loading();
    var ids = $.map($("#orderLabelTable").bootstrapTable('getSelections'), function (row) {
        return row.id;
    }).join(',');
    jp.post("${ctx}/tms/order/transport/label/deleteAll", {ids: ids}, function (data) {
        showLabel();
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

/*初始化费用*/
function initOrderCost(rows) {
    if (rows === undefined || rows.length <= 0) return;
    $("#tmTransportOrderCostList").empty();

    tmTransportOrderCostRowIdx = 0;
    for (var i = 0; i < rows.length; i++) {
        addOrderCost(rows[i]);
    }
}

/*新增费用*/
function addOrderCost(row) {
    if (row === undefined) {
        row = {transportNo: $("#transportNo").val(), orgId: $("#orgId").val(), recVer: 0, baseOrgId: $("#baseOrgId").val(), isBill: 'N'};
    }
    addRow('#tmTransportOrderCostList', tmTransportOrderCostRowIdx, tmTransportOrderCostTpl, row);
    tmTransportOrderCostRowIdx = tmTransportOrderCostRowIdx + 1;
}

/*保存费用*/
function saveOrderCost() {
    jp.loading();
    var validator = bq.tableValidate("#orderCostForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var params = bq.serializeJson($('#inputForm'));``
    params.transportNo = $("#transportNo").val();
    params.orgId = $("#orgId").val();
    jp.post("${ctx}/tms/order/transport/cost/save", params, function (data) {
        if (data.success) {
            initOrderCost(data.body.data);
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    });
}

/*删除费用*/
function delOrderCost() {
    delRow('#tmTransportOrderCostList', '${ctx}/tms/order/transport/cost/delete');
}


/******************************************附件start***********************************************************/
function annex() {
    $("#annexModal").modal({backdrop: 'static', keyboard: false});
    initAnnexTable();
}

function initAnnexTable() {
    $("#annexTable").bootstrapTable('destroy');
    $("#annexTable").bootstrapTable({
        height: 450,
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/annex/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.type = 4;
            searchParam.pkId = $('#id').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        },{
            field: 'fileName',
            title: '附件名称'
        },{
            field: 'fileSize',
            title: '附件大小'
        },{
            field: 'uploadBy.name',
            title: '上传人'
        },{
            field: 'uploadDate',
            title: '上传时间'
        },{
            field: 'operate',
            title: '操作',
            align: 'center',
            formatter: function operateFormatter(value, row, index) {
                return [
                    '<a href="${ctx}/sys/annex/download?id=' + row.id + '" class="btn btn-primary">下载附件 </a>',
                ].join('');
            }
        }]
    });
}

function uploadAnnex() {
    $("#uploadFileModal").modal({backdrop: 'static', keyboard: false});
    $("#uploadFile").val('');
}

function removeAnnex() {
    var ids = $.map($("#annexTable").bootstrapTable('getSelections'), function (row) {
        return row.id;
    });
    if (ids.length <= 0) {
        jp.warning("请选择记录");
        return;
    }
    jp.loading();
    jp.post("${ctx}/sys/annex/delete", {ids: ids.join(",")}, function (data) {
        $("#annexTable").bootstrapTable('refresh');
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

function uploadFileFnc() {
    jp.loading("上传中...");
    var formData = new FormData();
    formData.append("type", 4);
    formData.append("pkId", $('#id').val());
    var files = $("#uploadFile")[0].files;
    for (var i = 0; i < files.length; i++) {
        formData.append("files", files[i]);
    }
    $.ajax({
        url: "${ctx}/sys/annex/upload",
        type: "post",
        cache: false,
        contentType: false,
        processData: false,
        data: formData,
        success: function (data) {
            if (data.success) {
                jp.success(data.msg);
                $("#uploadFileModal").modal('hide');
                $("#annexTable").bootstrapTable('refresh');
            } else {
                jp.error(data.msg);
            }
        }, error: function (xhr, textStatus, errorThrown) {
            jp.error("附件大小超出限制");
        }
    });
}
/******************************************附件end***********************************************************/

</script>