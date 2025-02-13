<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var queryP;
$(document).ready(function () {
    $('#beginOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#endOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#auditTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#holdTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#createDateFm').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#createDateTo').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinWmSoHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm textarea").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmSoHeaderTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#banQinWmSoHeaderTable').bootstrapTable({
        method: 'post',//请求方法
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            queryP = params;
        },
        ajax: function (request) {
            var queryParams = initQueryParams();
            $.ajax({
                type: "post",
                url: "${ctx}/wms/outbound/banQinWmSoHeader/data",
                data: queryParams,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    request.success(data);
                    request.complete();
                }
            });
        },
        columns: [{
            checkbox: true
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'waveNo',
            title: '波次单号',
            sortable: true
        }, {
            field: 'soType',
            title: '出库单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_TYPE'))}, value, "-");
            }
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_STATUS'))}, value, "-");
            }
        }, {
            field: 'auditStatus',
            title: '审核状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_AUDIT_STATUS'))}, value, "-");
            }
        }, {
            field: 'interceptStatus',
            title: '拦截状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_INTERCEPT_STATUS'))}, value, "-");
            }
        }, {
            field: 'holdStatus',
            title: '冻结状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ORDER_HOLD_STATUS'))}, value, "-");
            }
        }, {
            field: 'packStatus',
            title: '打包状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_PACK_STATUS'))}, value, "-");
            }
        }, {
            field: 'ldStatus',
            title: '装车状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_LD_STATUS'))}, value, "-");
            }
        }, {
            field: 'orderTime',
            title: '订单时间',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主',
            sortable: true
        }, {
            field: 'consigneeName',
            title: '客户名称',
            sortable: true
        }, {
            field: 'settleName',
            title: '结算单位',
            sortable: true
        }, {
            field: 'priority',
            title: '优先级别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_PRIORITY'))}, value, "-");
            }
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
            field: 'def5',
            title: '客户订单号',
            sortable: true
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
            field: 'contactName',
            title: '联系人',
            sortable: true
        }, {
            field: 'contactTel',
            title: '联系人电话',
            sortable: true
        }, {
            field: 'def17',
            title: '联系人三级地址',
            sortable: true
        }, {
            field: 'contactAddr',
            title: '联系人地址',
            sortable: true
        }, {
            field: 'consigneeName',
            title: '收货人',
            sortable: true
        }, {
            field: 'consigneeTel',
            title: '收货人手机',
            sortable: true
        }, {
            field: 'consigneeAddr',
            title: '收货人地址',
            sortable: true
        }, {
            field: 'isPushed',
            title: '是否已下发运输',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
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
        }]
    });
    $('#banQinWmSoHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' + 'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinWmSoHeaderTable').bootstrapTable('getSelections').length;
        var cStyle = length ? "auto" : "none";
        var fColor = length ? "#1E1E1E" : "#8A8A8A";
        var singleStyle = length === 1 ? "auto" : "none";
        var singleColor = length === 1 ? "#1E1E1E" : "#8A8A8A";
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#duplicate').prop('disabled', length !== 1);
        $('#audit').prop('disabled', !length);
        $('#cancelAudit').prop('disabled', !length);
        $('#generateWave').css({"pointer-events": cStyle, "color": fColor});
        $('#preAlloc').prop('disabled', !length);
        $('#alloc').prop('disabled', !length);
        $('#picking').prop('disabled', !length);
        $('#shipment').prop('disabled', !length);
        $('#closeOrder').prop('disabled', !length);
        $('#cancelOrder').prop('disabled', !length);
        $('#cancelPreAlloc').css({"pointer-events": cStyle, "color": fColor});
        $('#cancelAlloc').css({"pointer-events": cStyle, "color": fColor});
        $('#cancelPicking').css({"pointer-events": cStyle, "color": fColor});
        $('#cancelShipment').css({"pointer-events": cStyle, "color": fColor});
        $('#generateLd').css({"pointer-events": cStyle, "color": fColor});
        $('#ldTransfer').css({"pointer-events": cStyle, "color": fColor});
        $('#ldCancelTransfer').css({"pointer-events": cStyle, "color": fColor});
        $('#holdOrder').css({"pointer-events": cStyle, "color": fColor});
        $('#cancelHold').css({"pointer-events": cStyle, "color": fColor});
        $('#intercept').css({"pointer-events": cStyle, "color": fColor});
        $('#updateConsigneeInfo').css({"pointer-events": singleStyle, "color": singleColor});
        $('#updateCarrierInfo').css({"pointer-events": singleStyle, "color": singleColor});
        $('#pushToTms').css({"pointer-events": cStyle, "color": fColor});
        $('#cancelPushToTms').css({"pointer-events": cStyle, "color": fColor});
    });
}

function getSelections() {
    return $.map($("#banQinWmSoHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row
    });
}

function getIdSelections() {
    return $.map($("#banQinWmSoHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getSoNoSelections() {
    return $.map($("#banQinWmSoHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.soNo
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmSoHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}

/**
 * 新增
 */
function add() {
    jp.openBQDialog('新增出库单', "${ctx}/wms/outbound/banQinWmSoHeader/form", '90%', '90%', $('#banQinWmSoHeaderTable'));
}

/**
 * 查看
 * @param id
 */
function view(id) {
    jp.openBQDialog('查看出库单', "${ctx}/wms/outbound/banQinWmSoHeader/form?id=" + id, '90%', '90%', $('#banQinWmSoHeaderTable'));
}

/**
 * 修改
 */
function edit() {
    jp.openBQDialog('编辑出库单', "${ctx}/wms/outbound/banQinWmSoHeader/form?id=" + getIdSelections(), '90%', '90%', $('#banQinWmSoHeaderTable'));
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
    jp.confirm('确认要删除该出库单记录吗？', function () {
        if (!jp.isExistDifOrg(getOrgIdSelections())) {
            jp.warning("不同平台数据不能批量操作");
            return;
        } 
        commonMethod("deleteAll");
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
 * 取消审核
 */
function cancelAudit() {
    commonMethod('cancelAudit');
}

/**
 * 生成波次计划
 */
function generateWave() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var soNos = [];
    var rows = getSelections();
    if (rows.length === 0) {
        jp.warning('请选择记录');
    }
    var errorMsg = '';
    for (var i = 0; i < rows.length; i++) {
        if (rows[i].status !== '00') {
            errorMsg += rows[i].soNo + "不是新建状态\n";
        }
        if (rows[i].auditStatus === '00') {
            errorMsg +=rows[i].soNo+ "未审核\n";
        }
    }
    if (errorMsg) {
        jp.bqError(errorMsg);
        return;
    }
    // 过滤已经生成波次的订单
    for (var j = 0; j < rows.length; j++) {
        if (!rows[j].waveNo) {
            soNos.push(rows[j].soNo);
        }
    }
    $('#wvParams').val(soNos.join(','));
    $('#wvRuleModal').modal();
    $('#ruleCode').val('');
    $('#ruleName').val('');
}

function wvRuleConfirm() {
    jp.loading();
    jp.get("${ctx}/wms/outbound/banQinWmSoHeader/generateWave?soNo=" + $('#wvParams').val() + "&waveRuleCode=" + $('#ruleCode').val() + "&orgId=" + jp.getCurrentOrg().orgId, function (data) {
        if (data.success) {
            $('#banQinWmSoHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
            $('#wvRuleModal').modal('hide');
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 预配
 */
function preAlloc() {
    commonMethod('preAlloc');
}

/**
 * 分配
 */
function alloc() {
    commonMethod('alloc');
}

/**
 * 拣货确认
 */
function picking() {
    commonMethod('picking');
}

/**
 * 发货确认
 */
function shipment() {
    commonMethod('shipment');
}

/**
 * 下发运输
 */
function pushToTms() {
    commonMethod('pushToTms');
}

/**
 * 下发运输
 */
function pushToTmsBatch() {
    jp.loading();
    $.ajax({
        type: "post",
        url: "${ctx}/wms/outbound/banQinWmSoHeader/pushToTms",
        data: initQueryParams(),
        cache: false,
        contentType: false,
        processData: false,
        success : function (data) {
            $('#banQinWmSoHeaderTable').bootstrapTable('refresh');
            if (data.success) {
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

/**
 * 取消下发运输
 */
function cancelPushToTms() {
    commonMethod('cancelPushToTms');
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
 * 取消预配
 */
function cancelPreAlloc() {
    commonMethod('cancelPreAlloc');
}

/**
 * 取消分配
 */
function cancelAlloc() {
    commonMethod('cancelAlloc');
}

/**
 * 取消拣货
 */
function cancelPicking() {
    commonMethod('cancelPicking');
}

/**
 * 取消发货
 */
function cancelShipment() {
    commonMethod('cancelShipment');
}

/**
 * 生成装车单
 */
function generateLd() {
    commonMethod('generateLd');
}

/**
 * 装车交接
 */
function ldTransfer() {
    commonMethod('ldTransfer');
}

/**
 * 取消交接
 */
function ldCancelTransfer() {
    commonMethod('ldCancelTransfer');
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
 * 拦截订单
 */
function intercept() {
    commonMethod('intercept');
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
    jp.get("${ctx}/wms/outbound/banQinWmSoHeader/" + method + "?soNo=" + getSoNoSelections() + "&orgId=" + jp.getCurrentOrg().orgId, function (data) {
        $('#banQinWmSoHeaderTable').bootstrapTable('refresh');
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 打印拣货清单
 */
function printPickingOrder() {
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
    bq.openPostWindow("${ctx}/wms/outbound/banQinWmSoHeader/printPickingOrder", 'ids', rowIds.join(','), '打印拣货清单');
}

/**
 * 打印拣货清单（横版）
 */
function printPickingOrderLandscape() {
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
    bq.openPostWindow("${ctx}/wms/outbound/banQinWmSoHeader/printPickingOrderLandscape", 'ids', rowIds.join(','), '打印拣货清单');
}

/**
 * 打印出库确认交接单
 */
function printShipHandoverOrder() {
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
    bq.openPostWindow("${ctx}/wms/outbound/banQinWmSoHeader/printShipHandoverOrder", 'ids', rowIds.join(','), '打印出库确认交接单');
}


/**
 * 打印发货清单
 */
function printShipOrder() {
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
    bq.openPostWindow("${ctx}/wms/outbound/banQinWmSoHeader/printShipOrder", 'ids', rowIds.join(','), '打印拣货清单');
}

/**
 * 打印发货清单
 */
function printShipOrderNew() {
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
    bq.openPostWindow("${ctx}/wms/outbound/banQinWmSoHeader/printShipOrderNew", 'ids', rowIds.join(','), '打印发货清单');
}

function updateConsigneeInfo() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rows = getSelections();
    $('#updateConsigneeInfoModal').modal();
    $('#consignee_update').val(rows[0].contactName);
    $('#consigneeTel_update').val(rows[0].contactTel);
    $('#consigneeArea_update').val(rows[0].def17);
    $('#consigneeAddress_update').val(rows[0].contactAddr);
    $('#orderId').val(rows[0].id);
}

function updateConsigneeInfoConfirm() {
    var consignee = $('#consignee_update').val();
    var consigneeTel = $('#consigneeTel_update').val();
    var consigneeArea = $('#consigneeArea_update').val();
    var consigneeAddress = $('#consigneeAddress_update').val();
    jp.loading();
    var params = {contactName: consignee, contactTel: consigneeTel, contactAddr: consigneeAddress, def17: consigneeArea, id: $('#orderId').val()};
    jp.post("${ctx}/wms/outbound/banQinWmSoHeader/updateConsigneeInfo", params, function(data) {
        if (data.success) {
            $('#banQinWmSoHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
            $('#updateConsigneeInfoModal').modal('hide');
        } else {
            jp.bqError(data.msg);
        }
    })
}

function updateCarrierInfo() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rows = getSelections();
    $('#updateCarrierInfoModal').modal();
    $('#carrier_carrierCode').val(rows[0].carrierCode);
    $('#carrier_carrierName').val(rows[0].carrierName);
    $('#carrier_orderId').val(rows[0].id);
}

function updateCarrierInfoConfirm() {
    var carrier = $('#carrier_carrierCode').val();
    jp.loading();
    var params = {carrierCode: carrier, id: $('#carrier_orderId').val()};
    jp.post("${ctx}/wms/outbound/banQinWmSoHeader/updateCarrierInfo", params, function(data) {
        if (data.success) {
            $('#banQinWmSoHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
            $('#updateCarrierInfoModal').modal('hide');
        } else {
            jp.bqError(data.msg);
        }
    })
}

function createWaveByGroup() {
    $('#wvRuleGroupModal').modal();
    $(':input', '#waveGroupForm').val('');
    $('#orderDateFmC').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#orderDateToC').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
}

function selectOwner(rows) {
    if (Array.isArray(rows)) {
        var ownerCodes = [], ownerNames = [];
        for (var index in rows) {
            ownerCodes.push(rows[index].ebcuCustomerNo);
            ownerNames.push(rows[index].ebcuNameCn);
        }
        if ($('#ownerCodeC').val() !== ownerCodes.join(',')) {
            $('#skuCodeC').val('');
            $('#skuNameC').val('');
        }
        $('#ownerCodeC').val(ownerCodes.join(','));
        $('#ownerNameC').val(ownerNames.join(','));
    } else {
        $('#ownerCodeC').val(rows.ebcuCustomerNo);
        $('#ownerNameC').val(rows.ebcuNameCn);
    }
}

function selectSku(rows) {
    if (Array.isArray(rows)) {
        var skuCodes = [], skuNames = [], ownerCodes = [], ownerNames = [];
        for (var index in rows) {
            skuCodes.push(rows[index].skuCode);
            skuNames.push(rows[index].skuName);
            if ($('#ownerCodeC').val().indexOf(rows[index].ownerCode) === -1) {
                ownerCodes.push(rows[index].ownerCode);
                ownerNames.push(rows[index].ownerName);
            }
        }
        if (!$('#ownerCodeC').val()) {
            $('#ownerCodeC').val(ownerCodes.join(','));
            $('#ownerNameC').val(ownerNames.join(','));
        }
        $('#skuCodeC').val(skuCodes.join(','));
        $('#skuNameC').val(skuNames.join(','));
    } else {
        $('#skuCodeC').val(rows.skuCode);
        $('#skuNameC').val(rows.skuName);
    }
}

function afterSelectGroup(row) {
    var curDate = jp.dateFormat(new Date(), 'yyyy-MM-dd');
    $('#orderDateFm').val(curDate + ' ' + row.orderDateFm);
    $('#orderDateTo').val(curDate + ' ' +row.orderDateTo);
}

function wvRuleGroupConfirm() {
    jp.loading();
    var validate = bq.detailSubmitCheck('#waveGroupForm');
    if (validate.isSuccess) {
        var params = bq.serializeJson($('#waveGroupForm'));
        params.ownerCode = params.ownerCodeC;
        params.skuCode = params.skuCodeC;
        params.orgId = jp.getCurrentOrg().orgId;
        jp.post("${ctx}/wms/outbound/banQinWmSoHeader/createWaveByGroup", params, function (data) {
            if (data.success) {
                $('#banQinWmSoHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
                $('#wvRuleGroupModal').modal('hide');
            } else {
                jp.bqError(data.msg);
            }
        })
    } else {
        jp.warning(validate.msg);
    }
}

function importOrder() {
    $("#uploadModal").modal();
    $("#uploadFileName").val('');
}

function downloadTemplate() {
    window.location = '${ctx}/wms/outbound/banQinWmSoHeader/import/template';
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
        url: "${ctx}/wms/outbound/banQinWmSoHeader/import",
        data: fm,
        cache: false,
        contentType: false,
        processData: false,
        // 传送请求数据
        success: function (data) {
            $("#uploadModal").modal('hide');
            $('#banQinWmSoHeaderTable').bootstrapTable('refresh');
            jp.alert(data.msg);
        },
        error: function (data) {
            $("#uploadModal").modal('hide');
            jp.alert(data.msg);
        }
    });
}

function loadFile(file, displayName) {
    $("#" + displayName).val(file.name);
}

function deleteFile(filePath) {
    $("#" + filePath).val('');
}

function initQueryParams() {
    var fm = new FormData();
    fm.append('orgId', jp.getCurrentOrg().orgId);
    if ($('#customerNoFile').val()) {
        fm.append('customerNoFile', $("#customerNoFile").get(0).files[0]);
    }
    if ($('#extendNoFile').val()) {
        fm.append('extendNoFile', $("#extendNoFile").get(0).files[0]);
    }

    var queryForm = $("#searchForm").serializeJSON();
    queryForm.pageNo = queryP.limit === undefined ? "1" : queryP.offset / queryP.limit + 1;
    queryForm.pageSize = queryP.limit === undefined ? -1 : queryP.limit;
    queryForm.orderBy = queryP.sort === undefined ? "" : queryP.sort + " " + queryP.order;
    $(Object.keys(queryForm)).each(function(idx, e) {
        if (queryForm[e]) {fm.append(e, queryForm[e]);}
    });
    return fm;
}

function exportData() {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    bq.exportExcel("${ctx}/wms/outbound/banQinWmSoHeader/export", "出库单记录" + jp.dateFormat(new Date(), "yyyyMMddhhmmss"), $("#searchForm"), function () {
        jp.close();
    });
}

</script>