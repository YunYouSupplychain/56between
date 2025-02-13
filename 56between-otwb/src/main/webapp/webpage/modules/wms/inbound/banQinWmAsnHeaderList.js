<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#beginOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#endOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#auditTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#holdTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#createDate').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#updateDate').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinWmAsnHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmAsnHeaderTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#banQinWmAsnHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inbound/banQinWmAsnHeader/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'asnNo',
            title: '入库单号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'asnType',
            title: '入库单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ASN_TYPE'))}, value, "-");
            }
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ASN_STATUS'))}, value, "-");
            }
        }, {
            field: 'auditStatus',
            title: '审核状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_AUDIT_STATUS'))}, value, "-");
            }
        }, {
            field: 'holdStatus',
            title: '冻结状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ORDER_HOLD_STATUS'))}, value, "-");
            }
        }, {
            field: 'supplierName',
            title: '供应商',
            sortable: true
        }, {
            field: 'settleName',
            title: '结算方',
            sortable: true
        }, {
            field: 'orderTime',
            title: '订单时间',
            sortable: true
        }, {
            field: 'def1',
            title: '商流订单号',
            sortable: true
        }, {
            field: 'def2',
            title: '供应链订单号',
            sortable: true
        }, {
            field: 'def3',
            title: '作业任务号',
            sortable: true
        }, {
            field: 'def4',
            title: '客户订单号',
            sortable: true
        }, {
            field: 'def5',
            title: '外部单号',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }, {
            field: 'qcStatus',
            title: '质检状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_STATUS'))}, value, "-");
            }
        }, {
            field: 'logisticNo',
            title: '物流单号',
            sortable: true
        }, {
            field: 'carrierName',
            title: '承运商',
            sortable: true
        }, {
            field: 'auditOp',
            title: '审核人',
            sortable: true
        }, {
            field: 'auditTime',
            title: '审核时间',
            sortable: true
        }, {
            field: 'holdOp',
            title: '冻结人',
            sortable: true
        }, {
            field: 'holdTime',
            title: '冻结时间',
            sortable: true
        }, {
            field: 'createBy.name',
            title: '创建人',
            sortable: true
        }, {
            field: 'createDate',
            title: '创建时间',
            sortable: true
        }, {
            field: 'updateBy.name',
            title: '修改人',
            sortable: true
        }, {
            field: 'updateDate',
            title: '修改时间',
            sortable: true
        }, {
            field: 'fmEta',
            title: '预计到货时间从',
            sortable: true
        }, {
            field: 'toEta',
            title: '预计到货时间到',
            sortable: true
        }, {
            field: 'priority',
            title: '优先级别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_PRIORITY'))}, value, "-");
            }
        }, {
            field: 'ediSendTime',
            title: 'EDI发送时间',
            sortable: true
        }, {
            field: 'isEdiSend',
            title: 'EDI发送标识',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('WM_EDI_SEND_FLAG'))}, value, "-");
            }
        }]
    });
    $('#banQinWmAsnHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinWmAsnHeaderTable').bootstrapTable('getSelections').length;
        var cStyle = length ? "auto" : "none";
        var fColor = length ? "#1E1E1E" : "#8A8A8A";
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#duplicate').prop('disabled', length !== 1);
        $('#audit').prop('disabled', !length);
        $('#cancelAudit').prop('disabled', !length);
        $('#costAlloc').prop('disabled', !length);
        $('#createQc').prop('disabled', !length);
        $('#closeOrder').prop('disabled', !length);
        $('#cancelOrder').prop('disabled', !length);
        $('#feedBackRT').prop('disabled', !length);
        $('#holdOrder').css({"pointer-events": cStyle, "color": fColor});
        $('#cancelHold').css({"pointer-events": cStyle, "color": fColor});
        $('#createVoucherNo').css({"pointer-events": cStyle, "color": fColor});
        $('#cancelVoucherNo').css({"pointer-events": cStyle, "color": fColor});
        $('#printReceivingOrder').css({"pointer-events": cStyle, "color": fColor});
        $('#printReceivingOrderLandscape').css({"pointer-events": cStyle, "color": fColor});
        $('#printTraceLabel').css({"pointer-events": cStyle, "color": fColor});
        $('#printCheckReceiveOrder').css({"pointer-events": cStyle, "color": fColor});
    });
}

function getIdSelections() {
    return $.map($("#banQinWmAsnHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmAsnHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}

/**
 * 新增
 */
function add() {
    jp.openBQDialog('新增入库单', "${ctx}/wms/inbound/banQinWmAsnHeader/form", '90%', '90%', $('#banQinWmAsnHeaderTable'));
}

/**
 * 查看
 * @param id
 */
function view(id) {
    jp.openBQDialog('查看入库单', "${ctx}/wms/inbound/banQinWmAsnHeader/form?id=" + id, '90%', '90%', $('#banQinWmAsnHeaderTable'));
}

/**
 * 修改
 */
function edit() {
    jp.openBQDialog('编辑入库单', "${ctx}/wms/inbound/banQinWmAsnHeader/form?id=" + getIdSelections(), '90%', '90%', $('#banQinWmAsnHeaderTable'));
}

/**
 * 删除
 */
function deleteAll() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.confirm('确认要删除该入库单记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/inbound/banQinWmAsnHeader/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinWmAsnHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

/**
 * 复制
 */
function duplicate() {
    commonMethod('duplicate');
}

/**
 * 审核
 */
function audit() {
    commonMethod('audit');
}

/**
 * 采购成本分摊
 */
function costAlloc() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }

    $('#apportionRuleModal').modal();
    $('#apportion_rule').val('');
}

function apportionRuleConfirm() {
    if (!$('#apportion_rule').val()) {
        jp.bqError("请选择分摊策略");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/inbound/banQinWmAsnHeader/costAlloc?ids=" + getIdSelections() + "&strategy=" + $('#apportion_rule').val(), function (data) {
        if (data.success) {
            $('#apportionRuleModal').modal('hide');
            $('#banQinWmAsnHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 取消审核
 */
function cancelAudit() {
    commonMethod('cancelAudit');
}

/**
 * 生成质检单
 */
function createQc() {
    commonMethod('createQc');
}

/**
 * 关闭订单
 */
function closeOrder() {
    commonMethod('closeOrder');
}

/**
 * 取消订单
 */
function cancelOrder() {
    commonMethod('cancelOrder');
}

/**
 * 冻结订单
 */
function holdOrder() {
    commonMethod('holdOrder');
}

/**
 * 取消冻结
 */
function cancelHold() {
    commonMethod('cancelHold');
}

/**
 * 生成凭证号
 */
function createVoucherNo() {
    commonMethod('createVoucherNo');
}

/**
 * 取消凭证号
 */
function cancelVoucherNo() {
    commonMethod('cancelVoucherNo');
}

/**
 * 公共调用方法
 * @param method 方法名
 */
function commonMethod(method) {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/inbound/banQinWmAsnHeader/" + method + "?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#banQinWmAsnHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 收货反馈
 */
function feedBackRT() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/interface/rt/receiveFeedBackWms?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#banQinWmAsnHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 打印收货单
 */
function printReceivingOrder() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/inbound/banQinWmAsnHeader/printReceivingOrder", 'ids', rowIds.join(','), '打印收货单');
}

/**
 * 打印收货单（横版）
 */
function printReceivingOrderLandscape() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/inbound/banQinWmAsnHeader/printReceivingOrderLandscape", 'ids', rowIds.join(','), '打印收货单');
}

/**
 * 打印托盘标签
 */
function printTraceLabel() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/inbound/banQinWmAsnHeader/printTraceLabel", 'ids', rowIds.join(','), '打印托盘标签');
}

/**
 * 打印托盘标签（二维码）
 */
function printTraceLabelQrCode() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/inbound/banQinWmAsnHeader/printTraceLabelQrCode", 'ids', rowIds.join(','), '打印托盘标签');
}

/**
 * 打印验收单
 */
function printCheckReceiveOrder() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/inbound/banQinWmAsnHeader/printCheckReceiveOrder", 'ids', rowIds.join(','), '打印验收单');
}

function importOrder() {
    $("#uploadModal").modal();
    $("#uploadFileName").val('');
}

function downloadTemplate() {
    window.location = '${ctx}/wms/inbound/banQinWmAsnHeader/import/template';
}

function uploadFile() {
    if (!$("#uploadFileName").val()) {
        jp.alert("请选择需要上传的文件");
        return;
    }
    jp.loading("正在导入中...");
    var file = $("#uploadFileName").get(0).files[0];
    var fm = new FormData();
    fm.append('file', file);
    fm.append('orgId', jp.getCurrentOrg().orgId);
    $.ajax({
        type: "post",
        url: "${ctx}/wms/inbound/banQinWmAsnHeader/import",
        data: fm,
        cache: false,
        contentType: false,
        processData: false,
        // 传送请求数据
        success: function (data) {
            $("#uploadModal").modal('hide');
            $('#banQinWmAsnHeaderTable').bootstrapTable('refresh');
            jp.alert(data.msg);
        },
        error: function (data) {
            $("#uploadModal").modal('hide');
            jp.alert(data.msg);
        }
    });
}

function exportData() {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    bq.exportExcel("${ctx}/wms/inbound/banQinWmAsnHeader/export", "入库单记录" + jp.dateFormat(new Date(), "yyyyMMddhhmmss"), $("#searchForm"), function () {
        jp.close();
    });
}

function serialReceive() {
    $("#serialReceiveModal").modal();
    $("#serialReceiveFile").val('');
}

function serialReceiveConfirm() {
    if (!$("#serialReceiveFile").val()) {
        jp.alert("请选择需要上传的文件");
        return;
    }
    jp.loading("正在导入收货中...");
    var file = $("#serialReceiveFile").get(0).files[0];
    var fm = new FormData();
    fm.append('file', file);
    fm.append('orgId', jp.getCurrentOrg().orgId);
    $.ajax({
        type: "post",
        url: "${ctx}/wms/inbound/banQinWmAsnHeader/serialReceive",
        data: fm,
        cache: false,
        contentType: false,
        processData: false,
        // 传送请求数据
        success: function (data) {
            $("#serialReceiveModal").modal('hide');
            $('#banQinWmAsnHeaderTable').bootstrapTable('refresh');
            jp.alert(data.msg);
        },
        error: function (data) {
            $("#serialReceiveModal").modal('hide');
            jp.alert(data.msg);
        }
    });
}

</script>