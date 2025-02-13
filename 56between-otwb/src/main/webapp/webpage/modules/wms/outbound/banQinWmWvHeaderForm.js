<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var allocDetail_currentRow; // 分配明细当前行
var allocDetail_isShowTab = false; // 是否显示右边上架任务tab页
var currentLoginName = '${fns:getUser().loginName}';
var disabledObj = [];

$(document).ready(function () {
    // 初始化发运订单tab
    initWvSoTable();
    // 初始化发运订单tab
    initWvDetailTable();
    // 初始化分配明细tab
    initWmWvAllocTab();
    // 初始化取消分配日志tab
    initCancelAllocTableTab();
    // 初始化
    init();
    // 激活默认Tab页
    activeTab();
    // 监听事件
    listener();
});

/**
 * 监听失去焦点事件
 */
function listener() {
    $('.required').on('blur', function () {
        if ($(this).hasClass('form-error') && $(this).val()) {
            $(this).removeClass('form-error');
        }
    });
}

/**
 * 激活默认Tab页
 */
function activeTab() {
    var detailTabIndex = sessionStorage.getItem("WV_" + currentLoginName +"_detailTab");
    detailTabIndex = !detailTabIndex ? 0 : detailTabIndex;
    // 默认选择加载Tab页
    $("#detailTab li:eq(" + detailTabIndex + ") a").tab('show');
}

/**
 * 修改表单中disable状态
 */
function openDisable(obj) {
    $(obj + " :disabled").each(function () {
        if ($(this).val()) {
            $(this).prop("disabled", false);
            disabledObj.push($(this));
        }
    });
    return true;
}

/**
 * 打开disabled状态
 */
function closeDisable() {
    for (var i = 0; i < disabledObj.length; i++) {
        $(disabledObj[i]).prop("disabled", true);
    }
    disabledObj = [];
    return true;
}

/**
 * 显示右边tab
 * @param obj1 右边div Id
 * @param obj2 左边div Id
 */
function showTabRight(obj1, obj2) {
    $(obj1).show();
    $(obj2).addClass("div-left");
}

/**
 * 隐藏右边tab
 * @param obj1 右边div Id
 * @param obj2 左边div Id
 */
function hideTabRight(obj1, obj2) {
    $(obj1).hide();
    $(obj2).removeClass("div-left");
}

/**
 * 单击表格事件
 * @param tab tab页名称
 * @param row 当前行
 */
function clickDetail(tab, row) {
    switch (tab) {
        case "allocDetail":
            allocClick(row);
            break;
    }
}

/**
 * 双击表格事件
 * @param tab tab页名称
 * @param row 当前行
 */
function dbClickDetail(tab, row) {
    switch (tab) {
        case "allocDetail":
            allocDbClick(row);
            break;
    }
}

/**
 * 赋值
 */
function evaluate(prefix, currentRow) {
    $("input[id^=" + prefix + "]").each(function() {
        var $Id = $(this).attr('id');
        var $Name = $(this).attr('name');
        $('#' + $Id).val(eval("" + currentRow + "." + $Name));
    });
    $("select[id^=" + prefix + "]").each(function() {
        var $Id = $(this).attr('id');
        var $Name = $(this).attr('name');
        $('#' + $Id).val(eval("" + currentRow + "." + $Name));
    });
}

/**
 * 获取表格勾选行Ids
 * @param obj 表格Id
 */
function getSelections(obj) {
    return $.map($(obj).bootstrapTable('getSelections'), function (row) {
        return row
    });
}

/**************************************波次单头开始********************************************/

/**
 * 波次单头保存
 */
function save() {
    var isValidate = bq.headerSubmitCheck('#inputForm');
    if (isValidate.isSuccess) {
        openDisable('#inputForm');
        jp.loading();
        jp.post("${ctx}/wms/outbound/banQinWmWvHeader/save", $('#inputForm').bq_serialize(), function (data) {
            if (data.success) {
                jp.success(data.msg);
                window.location = "${ctx}/wms/outbound/banQinWmWvHeader/form?id=" + data.body.entity;
            } else {
                closeDisable();
                jp.bqError(data.msg);
            }
        })
    } else {
        jp.bqError(isValidate.msg);
    }
}

/**
 * 分配
 */
function alloc() {
    commonMethod('alloc');
}

/**
 * 分派拣货
 */
function dispathPk() {
    $('#dispatchPKModal').modal();
}

/**
 * 分派拣货确认
 */
function dispatchPKConfirm() {
    $('#dispatchPKModal').modal('hide');
    jp.loading();
    var $form = $('#dispathPkForm').bq_serialize();
    $form.waveNos = $('#waveNo').val();
    $form.orgId = $('#orgId').val();
    jp.post("${ctx}/wms/outbound/banQinWmWvHeader/dispathPk", $form, function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/outbound/banQinWmWvHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
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
 * 公共调用方法
 * @param method 方法名
 */
function commonMethod(method) {
    jp.loading();
    jp.get("${ctx}/wms/outbound/banQinWmWvHeader/" + method + "?waveNo=" + $('#waveNo').val() + "&orgId=" + $('#orgId').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/outbound/banQinWmWvHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 是否分区库
 * @param flag
 */
function isByZoneChange(flag) {
    $('#isByZone').val(flag ? 'Y' : 'N');
}

/**
 * 是否托盘任务
 * @param flag
 */
function isPlTaskChange(flag) {
    $('#isPlTask').val(flag ? 'Y' : 'N');
    $('#plLimit').prop('readonly', !flag);
    $('#plFloat').prop('readonly', !flag);
}

/**
 * 是否箱拣任务
 * @param flag
 */
function isCsTaskChange(flag) {
    $('#isCsTask').val(flag ? 'Y' : 'N');
    $('#csLimit').prop('readonly', !flag);
    $('#csFloat').prop('readonly', !flag);
}

/**
 * 是否件拣任务
 * @param flag
 */
function isEaTaskChange(flag) {
    $('#isEaTask').val(flag ? 'Y' : 'N');
    $('#eaLimit').prop('readonly', !flag);
    $('#eaFloat').prop('readonly', !flag);
}

/**
 * 初始化
 */
function init() {
    // 初始化表头label
    initLabel();
    // 初始化按钮
    buttonControl();
}

/**
 * 初始化表头label
 */
function initLabel() {
    if (!$('#id').val()) {
        $('#orgId').val(jp.getCurrentOrg().orgId);
    }
}

/**
 * 功能按扭控制
 */
function buttonControl() {
    buttonAllByHeader(false);
    buttonAllBySoHeader(false);
    buttonAllByDetail(false);
    buttonAllByAlloc(false);
    buttonControlByHeader();
    buttonControlBySoHeader();
    buttonControlByDetail();
    buttonControlByAlloc();
}

/**
 * 波次计划 单据 功能按扭控制
 */
function buttonAllByHeader(flag) {
    $('#header_save').attr('disabled', flag);
    $('#header_alloc').attr('disabled', flag);
    $('#header_dispathPk').attr('disabled', flag);
    $('#header_picking').attr('disabled', flag);
    $('#header_shipment').attr('disabled', flag);
    $('#header_getWaybill').attr('disabled', flag);
    $('#header_printWaybill').attr('disabled', flag);
    $('#header_getAndPrintWaybill').attr('disabled', flag);
    $('#header_cancelAlloc').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
    $('#header_cancelPicking').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
    $('#header_cancelShipment').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
}

/**
 * 波次计划 出库单列表 功能按扭控制
 */
function buttonAllBySoHeader(flag) {
    $('#so_alloc').attr('disabled', flag);
    $('#so_picking').attr('disabled', flag);
    $('#so_shipment').attr('disabled', flag);
    $('#so_cancelAlloc').attr('disabled', flag);
    $('#so_cancelPicking').attr('disabled', flag);
    $('#so_cancelShipment').attr('disabled', flag);
}

/**
 * 波次计划 明细 功能按扭控制
 */
function buttonAllByDetail(flag) {
    $('#wv_alloc').attr('disabled', flag);
    $('#wv_manualAlloc').attr('disabled', flag);
    $('#wv_cancelAlloc').attr('disabled', flag);
}

/**
 * 波次计划 分配明细 功能按扭控制
 */
function buttonAllByAlloc(flag) {
    $('#alloc_add').attr('disabled', flag);
    $('#alloc_save').attr('disabled', flag);
    $('#alloc_pick').attr('disabled', flag);
    $('#alloc_shipment').attr('disabled', flag);
    $('#alloc_cancelAlloc').attr('disabled', flag);
    $('#alloc_cancelPick').attr('disabled', flag);
    $('#alloc_cancelShipment').attr('disabled', flag);
}

/**
 * 波次单header 功能按扭控制
 */
function buttonControlByHeader() {
    buttonAllByHeader(true);
    // 根据状态控制按扭
    // 创建
    if ($('#status').val() === '00') {
        $('#header_save').attr('disabled', false);
        $('#header_alloc').attr('disabled', false);
    }
    // 部分预配
    else if ($('#status').val() === '10') {
        $('#header_alloc').attr('disabled', false);
    }
    // 完全预配
    else if ($('#status').val() === '20') {
        $('#header_alloc').attr('disabled', false);
    }
    // 部分分配
    else if ($('#status').val() === '30') {
        $('#header_alloc').attr('disabled', false);
        $('#header_dispathPk').attr('disabled', false);
        $('#header_cancelAlloc').css({"pointer-events": "auto", "color": "#1E1E1E"});
        $('#header_picking').attr('disabled', false);
        $('#header_getWaybill').attr('disabled', false);
        $('#header_printWaybill').attr('disabled', false);
        $('#header_getAndPrintWaybill').attr('disabled', false);
    }
    // 完全分配
    else if ($('#status').val() === '40') {
        $('#header_dispathPk').attr('disabled', false);
        $('#header_cancelAlloc').css({"pointer-events": "auto", "color": "#1E1E1E"});
        $('#header_picking').attr('disabled', false);
        $('#header_getWaybill').attr('disabled', false);
        $('#header_printWaybill').attr('disabled', false);
        $('#header_getAndPrintWaybill').attr('disabled', false);
    }
    // 部分拣货
    else if ($('#status').val() === '50') {
        buttonAllByHeader(false);
        $('#header_save').attr('disabled', true);
        $('#header_cancelShipment').css({"pointer-events": "none", "color": "#8A8A8A"});
        $('#header_getWaybill').attr('disabled', false);
        $('#header_printWaybill').attr('disabled', false);
        $('#header_getAndPrintWaybill').attr('disabled', false);
    }
    // 完全拣货
    else if ($('#status').val() === '60') {
        $('#header_shipment').attr('disabled', false);
        $('#header_cancelPicking').css({"pointer-events": "auto", "color": "#1E1E1E"});
        $('#header_getWaybill').attr('disabled', false);
        $('#header_printWaybill').attr('disabled', false);
        $('#header_getAndPrintWaybill').attr('disabled', false);
    }
    // 部分发货
    else if ($('#status').val() === '70') {
        buttonAllByHeader(false);
        $('#header_save').attr('disabled', true);
        $('#header_getWaybill').attr('disabled', false);
        $('#header_printWaybill').attr('disabled', false);
        $('#header_getAndPrintWaybill').attr('disabled', false);
    }
    // 完全发货
    else if ($('#status').val() === '80') {
        $('#header_cancelShipment').css({"pointer-events": "auto", "color": "#1E1E1E"});
    }
}

/**
 * 发运单 功能按扭控制
 */
function buttonControlBySoHeader() {
    buttonAllBySoHeader(true);
    // 根据状态控制按扭
    // 创建状态
    if ($('#status').val() === '00') {
        $('#so_alloc').attr('disabled', false);
    }
    // 部分预配
    else if ($('#status').val() === '10') {
        $('#so_alloc').attr('disabled', false);
    }
    // 完全预配
    else if ($('#status').val() === '20') {
        $('#so_alloc').attr('disabled', false);
    }
    // 部分分配
    else if ($('#status').val() === '30') {
        $('#so_alloc').attr('disabled', false);
        $('#so_cancelAlloc').attr('disabled', false);
    }
    // 完全分配
    else if ($('#status').val() === '40') {
        $('#so_cancelAlloc').attr('disabled', false);
        $('#so_picking').attr('disabled', false);
    }
    // 部分拣货
    else if ($('#status').val() === '50') {
        buttonAllBySoHeader(false);
        $('#so_cancelShipment').attr('disabled', true);
    }
    // 完全拣货
    else if ($('#status').val() === '60') {
        $('#so_cancelPicking').attr('disabled', false);
        $('#so_shipment').attr('disabled', false);
    }
    // 部分发货
    else if ($('#status').val() === '70') {
        buttonAllBySoHeader(false);
    }
    // 完全发货
    else if ($('#status').val() === '80') {
        $('#so_cancelShipment').attr('disabled', false);
    }
}

/**
 * 波次明细 功能按扭控制
 */
function buttonControlByDetail() {
    buttonAllByDetail(true);
    //根据状态控制按扭
    //创建状态
    if ($('#status').val() === '00') {
        $('#wv_alloc').attr('disabled', false);
        $('#wv_manualAlloc').attr('disabled', false);
    }
    // 部分预配
    else if ($('#status').val() === '10') {
        $('#wv_alloc').attr('disabled', false);
        $('#wv_manualAlloc').attr('disabled', false);
    }
    // 完全预配
    else if ($('#status').val() === '20') {
        $('#wv_alloc').attr('disabled', false);
    }
    // 部分分配
    else if ($('#status').val() === '30') {
        buttonAllByDetail(false);
    }
    // 完全分配
    else if ($('#status').val() === '40') {
        $('#wv_cancelAlloc').attr('disabled', false);
    }
    // 部分拣货
    else if ($('#status').val() === '50') {
        buttonAllByDetail(false);
    }
    // 部分发货
    else if ($('#status').val() === '70') {
        buttonAllByDetail(false);
    }
}

/**
 * 分配明细 功能按扭控制
 */
function buttonControlByAlloc() {
    $('#alloc_save').attr('disabled', true);
    buttonAllByAlloc(true);
    // 批量操作
    if (!allocDetail_isShowTab) {
        //创建状态
        if ($('#status').val() === '00') {
            $('#alloc_add').attr('disabled', false);
        }
        // 部分预配
        else if ($('#status').val() === '10') {
            $('#alloc_add').attr('disabled', false);
        }
        // 部分分配
        else if ($('#status').val() === '30') {
            $('#alloc_add').attr('disabled', false);
            $('#alloc_cancelAlloc').attr('disabled', false);
            $('#alloc_pick').attr('disabled', false);
        }
        // 完全分配
        else if ($('#status').val() === '40') {
            $('#alloc_cancelAlloc').attr('disabled', false);
            $('#alloc_pick').attr('disabled', false);
        }
        // 部分拣货
        else if ($('#status').val() === '50') {
            buttonAllByAlloc(false);
            $('#alloc_save').attr('disabled', true);
            $('#alloc_cancelShipment').attr('disabled', true);
        }
        // 完全拣货
        else if ($('#status').val() === '60') {
            $('#alloc_cancelPick').attr('disabled', false);
            $('#alloc_shipment').attr('disabled', false);
        }
        // 部分发货
        else if ($('#status').val() === '70') {
            buttonAllByAlloc(false);
            $('#alloc_save').attr('disabled', true);
        }
        // 完全发货
        else if ($('#status').val() === '80') {
            $('#alloc_cancelShipment').attr('disabled', false);
        }
    } else {
        // 单记录操作
        if (!$('#allocDetail_id').val()) {
            $('#alloc_add').attr('disabled', false);
            $('#alloc_save').attr('disabled', false);
            return;
        }
        // 完全分配
        if (allocDetail_currentRow.status === '40') {
            $('#alloc_save').attr('disabled', false);
            $('#alloc_pick').attr('disabled', false);
            $('#alloc_cancelAlloc').attr('disabled', false);
        }
        // 完全拣货
        else if (allocDetail_currentRow.status === '60') {
            $('#alloc_cancelPick').attr('disabled', false);
            $('#alloc_shipment').attr('disabled', false);
        }
        // 完全发货
        else if (allocDetail_currentRow.status === '80') {
            $('#alloc_cancelShipment').attr('disabled', false);
        }
    }
}

/**************************************SO单头结束********************************************/
/**************************************弹出框开始********************************************/
function afterSelectAllocPack(row) {
    $('#allocDetail_uomQty').val(row.cdprQuantity);
    allocSoChange();
}

/**************************************弹出框开始********************************************/
/**************************************发运订单开始********************************************/
function initWvSoTable() {
    $('#wvSoTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/outbound/banQinWmSoHeader/grid",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = $('#id').val() ? $('#orgId').val() : jp.getCurrentOrg().orgId;
            searchParam.waveNo = $('#id').val() ? $('#waveNo').val() : '#';
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function (row, $el) {
            jp.changeTableStyle($el);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true
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
            field: 'orderTime',
            title: '订单时间',
            sortable: true
        }, {
            field: 'priority',
            title: '优先级别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_PRIORITY'))}, value, "-");
            }
        }, {
            field: 'status',
            title: '订单状态',
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
            field: 'auditOp',
            title: '审核人',
            sortable: true
        }, {
            field: 'auditTime',
            title: '审核时间',
            sortable: true
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
            field: 'holdOp',
            title: '冻结人',
            sortable: true
        }, {
            field: 'holdTime',
            title: '冻结时间',
            sortable: true
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'carrierCode',
            title: '承运商编码',
            sortable: true
        }, {
            field: 'settleCode',
            title: '结算人编码',
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
        }]
    });
}

function getSoSelections() {
    return $.map($('#wvSoTable').bootstrapTable('getSelections'), function (row) {
        return row.soNo
    });
}

/**
 * 明细Tab页索引切换
 */
function detailTabChange(index) {
    sessionStorage.setItem("WV_" + currentLoginName + "_detailTab", index);
}

/**
 * 分配
 */
function soAlloc() {
    commonSoMethod('soAlloc');
}

/**
 * 拣货确认
 */
function soPicking() {
    commonSoMethod('soPicking');
}

/**
 * 发货确认
 */
function soShipment() {
    commonSoMethod('soShipment');
}

/**
 * 取消分配
 */
function soCancelAlloc() {
    commonSoMethod('soCancelAlloc');
}

/**
 * 取消拣货
 */
function soCancelPicking() {
    commonSoMethod('soCancelPicking');
}

/**
 * 取消发货
 */
function soCancelShipment() {
    commonSoMethod('soCancelShipment');
}

/**
 * 获取面单
 */
function getWaybill() {
    commonMethod('getWaybill');
}

/**
 * 打印面单
 */
function printWaybill() {
    jp.loading();
    jp.get("${ctx}/wms/outbound/banQinWmWvHeader/printWaybill?waveNo=" + $('#waveNo').val() + "&orgId=" + $('#orgId').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/outbound/banQinWmWvHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
            // 后台打印
            bq.printWayBill(data.body.imageList);
        } else {
            jp.bqError(data.msg);
        }
    });
}

/**
 * 获取面单并打印
 */
function getAndPrintWaybill() {
    jp.loading();
    jp.get("${ctx}/wms/outbound/banQinWmWvHeader/getAndPrintWaybill?waveNo=" + $('#waveNo').val() + "&orgId=" + $('#orgId').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/outbound/banQinWmWvHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
            // 后台打印
            bq.printWayBill(data.body.imageList);
        } else {
            jp.bqError(data.msg);
        }
    });
}

/**
 * 公共调用方法
 * @param method 方法名
 */
function commonSoMethod(method) {
    var soNos = getSoSelections();
    if (soNos.length == 0) {
        jp.bqWaring("请选择记录");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/outbound/banQinWmWvDetail/" + method + "?soNo=" + getSoSelections().join(',') + "&orgId=" + $('#orgId').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/outbound/banQinWmWvHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**************************************发运订单结束*******************************************/
/**************************************波次明细开始*******************************************/
function initWvDetailTable() {
    $('#wvDetailTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/outbound/banQinWmSoDetail/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = $('#id').val() ? $('#orgId').val() : jp.getCurrentOrg().orgId;
            searchParam.waveNo = $('#id').val() ? $('#waveNo').val() : '#';
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function (row, $el) {
            jp.changeTableStyle($el);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'lineNo',
            title: '行号',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_STATUS'))}, value, "-");
            }
        }, {
            field: 'ownerName',
            title: '货主',
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
            field: 'qtySoEa',
            title: '订货数',
            sortable: true
        }, {
            field: 'qtyAllocEa',
            title: '分配数',
            sortable: true
        }, {
            field: 'qtyPkEa',
            title: '拣货数',
            sortable: true
        }, {
            field: 'qtyShipEa',
            title: '发货数',
            sortable: true
        }, {
            field: 'packDesc',
            title: '包装规格',
            sortable: true
        }, {
            field: 'uomDesc',
            title: '包装单位',
            sortable: true
        }, {
            field: 'cdType',
            title: '越库类型',
            sortable: true
        }, {
            field: 'oldLineNo',
            title: '原行号',
            sortable: true
        }]
    });
}

/**
 * 分配
 */
function wvAlloc() {
    commonWvDetailMethod('wvAlloc');
}

/**
 * 手工分配
 */
function wvManualAlloc() {
    allocAdd();
}

/**
 * 取消分配
 */
function wvCancelAlloc() {
    commonWvDetailMethod('wvCancelAlloc');
}

function getWvDetailSelections() {
    return $.map($('#wvDetailTable').bootstrapTable('getSelections'), function (row) {
        return "('" + row.soNo + "','" + row.lineNo + "')"
    });
}

/**
 * 公共调用方法
 * @param method 方法名
 */
function commonWvDetailMethod(method) {
    var soNos = getWvDetailSelections();
    if (soNos.length == 0) {
        jp.bqWaring("请选择记录");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/outbound/banQinWmWvDetail/" + method + "?soNo=" + getWvDetailSelections().join('@') + "&orgId=" + $('#orgId').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/outbound/banQinWmWvHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 打印标签
 */
function printLabel() {
    $('#printLabelModal').modal();
}

function printLabelConfirm() {
    var skuCode = $('#printLabel_skuCode').val();
    bq.openPostWindow("${ctx}/wms/outbound/banQinWmWvDetail/printLabel", 'waveNo|skuCode|orgId', $('#waveNo').val() + "|" + skuCode + "|" + $('#orgId').val(), '打印标签');
}

/**************************************波次明细结束*******************************************/

/**************************************分配明细开始********************************************/
/**
 * 初始化分配明细Tab
 */
function initWmWvAllocTab() {
    // 隐藏右边tab页
    hideTabRight('#allocDetail_tab-right', '#allocDetail_tab-left');
    // 初始化明细table
    initWmWvAllocTable();
}

/**
 * 初始化明细table
 */
function initWmWvAllocTable() {
    $('#wmWvAllocTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/outbound/banQinWmSoAlloc/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orgId = $('#id').val() ? $('#orgId').val() : jp.getCurrentOrg.orgId;
            searchParam.waveNo = $('#id').val() ? $('#waveNo').val() : '#';
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function(row, $el) {
            clickDetail('allocDetail', row);
            jp.changeTableStyle($el);
        },
        onDblClickRow: function (row, $el) {
            dbClickDetail('allocDetail', row);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'allocId',
            title: '分配Id',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_STATUS'))}, value, "-");
            }
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'lineNo',
            title: '出库单行号',
            sortable: true
        }, {
            field: 'ownerCode',
            title: '货主',
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
            field: 'lotNum',
            title: '批次号',
            sortable: true
        }, {
            field: 'locCode',
            title: '库位',
            sortable: true
        }, {
            field: 'traceId',
            title: '跟踪号',
            sortable: true
        }, {
            field: 'toLoc',
            title: '目标库位',
            sortable: true
        }, {
            field: 'toId',
            title: '目标跟踪号',
            sortable: true
        }, {
            field: 'qtyUom',
            title: '数量',
            sortable: true
        }, {
            field: 'qtyEa',
            title: '数量EA',
            sortable: true
        }, {
            field: 'packCode',
            title: '包装规格',
            sortable: true
        }, {
            field: 'uom',
            title: '包装单位',
            sortable: true
        }, {
            field: 'pickOp',
            title: '拣货人',
            sortable: true
        }, {
            field: 'pickTime',
            title: '拣货时间',
            sortable: true
        }, {
            field: 'pickNo',
            title: '拣货单号',
            sortable: true
        }, {
            field: 'checkOp',
            title: '复核人',
            sortable: true
        }, {
            field: 'checkTime',
            title: '复核时间',
            sortable: true
        }, {
            field: 'checkStatus',
            title: '复核状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_CHECK_STATUS'))}, value, "-");
            }
        }, {
            field: 'trackingNo',
            title: '快递单号',
            sortable: true
        }, {
            field: 'caseNo',
            title: '打包箱号',
            sortable: true
        }, {
            field: 'packOp',
            title: '打包人',
            sortable: true
        }, {
            field: 'packTime',
            title: '打包时间',
            sortable: true
        }, {
            field: 'packWeight',
            title: '包裹总重量',
            sortable: true
        }, {
            field: 'shipOp',
            title: '发货人',
            sortable: true
        }, {
            field: 'shipTime',
            title: '发货时间',
            sortable: true
        }, {
            field: 'lotAtt01',
            title: '生产日期',
            sortable: true
        }, {
            field: 'lotAtt02',
            title: '失效日期',
            sortable: true
        }, {
            field: 'lotAtt03',
            title: '入库日期',
            sortable: true
        }, {
            field: 'lotAtt04',
            title: '批次属性4',
            sortable: true
        }, {
            field: 'lotAtt05',
            title: '批次属性5',
            sortable: true
        }, {
            field: 'lotAtt06',
            title: '批次属性6',
            sortable: true
        }, {
            field: 'lotAtt07',
            title: '批次属性7',
            sortable: true
        }, {
            field: 'lotAtt08',
            title: '批次属性8',
            sortable: true
        }, {
            field: 'lotAtt09',
            title: '批次属性9',
            sortable: true
        }, {
            field: 'lotAtt10',
            title: '批次属性10',
            sortable: true
        }, {
            field: 'lotAtt11',
            title: '批次属性11',
            sortable: true
        }, {
            field: 'lotAtt12',
            title: '批次属性12',
            sortable: true
        }]
    });
}

/**
 * 分配明细行单击事件
 * @param tab tab页名称
 * @param row 当前行
 */
function allocClick(row) {
    allocDetail_currentRow = row;
    if (allocDetail_isShowTab) {
        // 表单赋值
        evaluate('allocDetail', 'allocDetail_currentRow');
        // 分配明细当前行SKU
        $('#allocDetail_skuCodeParam').val(row.skuCode);
    }
    // 按钮控制
    buttonControlByAlloc();
}

/**
 * 分配明细行双击事件
 * @param row 当前行
 */
function allocDbClick(row) {
    allocDetail_currentRow = row;
    if (!allocDetail_isShowTab) {
        // 显示右边tab
        showTabRight('#allocDetail_tab-right', '#allocDetail_tab-left');
        allocDetail_isShowTab = true;
        // 表单赋值
        evaluate('allocDetail', 'allocDetail_currentRow');
        // 分配明细当前行SKU
        $('#allocDetail_skuCodeParam').val(row.skuCode);
    } else {
        // 隐藏右边tab
        hideTabRight('#allocDetail_tab-right', '#allocDetail_tab-left');
        allocDetail_isShowTab = false;
    }
    // 按钮控制
    buttonControlByAlloc();
}

/**
 * 新增分配明细
 */
function allocAdd() {
    var rows = getSelections('#wvDetailTable');
    if (rows.length !== 1) {
        jp.bqError("请选择一条商品明细!");
        return;
    }
    if (rows[0].status === '90') {
        jp.bqError("订单行[" + rows[0].soNo + "][" + rows[0].lineNo + "已取消，不能操作");
        return;
    }
    if (rows[0].status === '20' || rows[0].status === '40' || rows[0].status === '60' || rows[0].status === '80') {
        jp.bqError("非创建，非部分预配，非部分分配，非部分拣货，非部分发货状态，不能操作");
        return;
    }
    if (rows[0].cdType) {
        jp.bqError("[" + rows[0].soNo + "][" + rows[0].lineNo + "]属于越库，不能操作");
        return;
    }

    // 跳转到分配明细tab页
    $('a[href="#allocDetailInfo"]').tab('show');
    // 显示右边Tab
    showTabRight('#allocDetail_tab-right', '#allocDetail_tab-left');
    allocDetail_isShowTab = true;
    allocDetail_currentRow = null;
    // 清空表单
    $(':input', '#allocDetailForm').val('');
    $('#alloc_save').attr('disabled', false);
    // 初始化
    initAddAllocDetail(rows[0]);
    buttonControl();
}

/**
 * 初始化新增分配明细
 */
function initAddAllocDetail(row) {
    $('#allocDetail_waveNo').val(row.waveNo);
    $('#allocDetail_soNo').val(row.soNo);
    $('#allocDetail_lineNo').val(row.lineNo);
    $('#allocDetail_ownerCode').val(row.ownerCode);
    $('#allocDetail_ownerName').val(row.ownerName);
    $('#allocDetail_status').val('40');
    $('#allocDetail_toLoc').val('SORTATION');
    $('#allocDetail_skuCodeParam').val(row.skuCode);
}

/**
 * 保存分配明细
 */
function allocSave() {
    if (!allocDetail_isShowTab) {
        return;
    }
    // 表单验证
    var validate = bq.detailSubmitCheck('#allocDetail_tab-right');
    if (validate.isSuccess) {
        // 保存前赋值
        allocBeforeSave();
        openDisable("#allocDetailForm");
        jp.loading("正在保存中...");
        var row = {};
        bq.copyProperties(allocDetail_currentRow, row);
        $.extend(row, bq.serializeJson($('#allocDetailForm')));
        $.ajax({
            url: "${ctx}/wms/outbound/banQinWmWvDetail/allocSave",
            type: 'POST',
            data: JSON.stringify(row),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    window.location = "${ctx}/wms/outbound/banQinWmWvHeader/form?id=" + $('#id').val();
                    jp.success(data.msg);
                } else {
                    closeDisable();
                    jp.bqError(data.msg);
                }
            }
        });
    } else {
        jp.bqError(validate.msg);
    }
}

/**
 * 手工分配保存
 */
function allocBeforeSave() {
    if (!$('#allocDetail_id').val()) {
        $('#allocDetail_orgId').val($('#orgId').val());
    }
}
/**
 * 数量同步到EA
 */
function allocSoChange() {
    // 单位换算数量
    var uomQty = !$('#allocDetail_uomQty').val() ? 0 : $('#allocDetail_uomQty').val();
    // 分配数
    var qtyUom = !$('#allocDetail_qtyUom').val() ? 0 : $('#allocDetail_qtyUom').val();
    $('#allocDetail_qtyEa').val(Math.floor(qtyUom * 100) / 100 * uomQty);
}

/**
 * 拣货确认
 */
function allocPick() {
    var rows = [];
    if (!allocDetail_isShowTab) {
        rows = getSelections('#wmWvAllocTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        openDisable("#allocDetailForm");
        var row = {};
        bq.copyProperties(allocDetail_currentRow, row);
        $.extend(row, bq.serializeJson($('#allocDetailForm')));
        rows.push(row);
    }
    commonAllocDetail('allocPick', rows);
}

/**
 * 发货确认
 */
function allocShipment() {
    var rows = [];
    if (!allocDetail_isShowTab) {
        rows = getSelections('#wmWvAllocTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        rows.push(allocDetail_currentRow);
    }
    commonAllocDetail('allocShipment', rows);
}

/**
 * 取消分配
 */
function allocCancelAlloc() {
    var rows = [];
    if (!allocDetail_isShowTab) {
        rows = getSelections('#wmWvAllocTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        rows.push(allocDetail_currentRow);
    }
    commonAllocDetail('allocCancelAlloc', rows);
}

/**
 * 取消拣货
 */
function allocCancelPick() {
    var rows = [];
    if (!allocDetail_isShowTab) {
        rows = getSelections('#wmWvAllocTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    }
    initCancelPickWindow();
    view = "wvAlloc";
}

function initCancelPickWindow() {
    $('#createTaskPaModal').modal();
    $('#isTaskPa').prop('checked', false).prop('disabled', false).val('N');
    $('#allocLoc').prop('checked', true).prop('disabled', true);
    $('#paRuleLoc').prop('checked', false).prop('disabled', true);
}

/**
 * 取消发货
 */
function allocCancelShipment() {
    var rows = [];
    if (!allocDetail_isShowTab) {
        rows = getSelections('#wmWvAllocTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        rows.push(allocDetail_currentRow);
    }
    commonAllocDetail('allocCancelShipment', rows);
}

/**
 * 分配明细公用方法
 * @param method 方法名
 */
function commonAllocDetail(method, rows) {
    jp.loading();
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(rows),
        url: "${ctx}/wms/outbound/banQinWmWvDetail/" + method,
        success: function (data) {
            if (data.success) {
                window.location = "${ctx}/wms/outbound/banQinWmWvHeader/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                closeDisable();
                jp.bqError(data.msg);
            }
        }
    });
}

/**************************************分配明细结束*******************************************/
/**************************************取消分配日志开始*******************************************/
/**
 * 初始化分配明细Tab
 */
function initCancelAllocTableTab() {
    // 初始化明细table
    initCancelAllocTable();
}

/**
 * 初始化明细table
 */
function initCancelAllocTable() {
    $('#cancelAllocTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/outbound/banQinWmDelAlloc/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orderType = "SO";
            searchParam.waveNo = !$('#waveNo').val() ? '#': $('#waveNo').val();
            searchParam.orgId = $('#orgId').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function(row, $el) {
            jp.changeTableStyle($el);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'allocId',
            title: '分配Id',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_STATUS'))}, value, "-");
            }
        }, {
            field: 'waveNo',
            title: '波次单号',
            sortable: true
        }, {
            field: 'orderNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'lineNo',
            title: '出库单行号',
            sortable: true
        }, {
            field: 'ownerCode',
            title: '货主',
            sortable: true
        }, {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        }, {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        }, {
            field: 'locCode',
            title: '库位',
            sortable: true
        }, {
            field: 'traceId',
            title: '跟踪号',
            sortable: true
        }, {
            field: 'pkSeq',
            title: '拣货顺序',
            sortable: true
        }, {
            field: 'toLoc',
            title: '目标库位',
            sortable: true
        }, {
            field: 'toId',
            title: '目标跟踪号',
            sortable: true
        }, {
            field: 'qtyUom',
            title: '分配数',
            sortable: true
        }, {
            field: 'qtyEa',
            title: '分配数EA',
            sortable: true
        }, {
            field: 'packCode',
            title: '包装规格',
            sortable: true
        }, {
            field: 'uom',
            title: '包装单位',
            sortable: true
        }, {
            field: 'op',
            title: '操作人',
            sortable: true
        }, {
            field: 'opTime',
            title: '操作时间',
            sortable: true
        }]
    });
}

/**
 * 生成上架任务
 */
var view = "";
function createPaTaskCancelAlloc() {
    initPaTaskWindow();
    view = "delAlloc";
}

function initPaTaskWindow() {
    $('#createTaskPaModal').modal();
    $('#isTaskPa').prop('checked', false).prop('disabled', false).val('N');
    $('#allocLoc').prop('checked', true).prop('disabled', true);
    $('#paRuleLoc').prop('checked', false).prop('disabled', true);
}

function createTaskPaConfirm() {
    switch (view) {
        case "wvAlloc": createTaskByWave(); break;
        case "delAlloc": createTaskByDel(); break;
    }
}

function createTaskByWave() {
    var allocRows = [];
    if (!allocDetail_isShowTab) {
        allocRows = getSelections('#wmWvAllocTable');
        if (allocRows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        allocRows.push(allocDetail_currentRow)
    }
    var isAllocLoc = $('#allocLoc').prop('checked') ? 'Y' : 'N';
    var isTaskPa = $('#isTaskPa').val();
    for (var index = 0, length = allocRows.length; index < length; index++) {
        allocRows[index].isTaskPa = isTaskPa;
        allocRows[index].isAllocLoc = isAllocLoc;
    }
    jp.loading();
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(allocRows),
        url: "${ctx}/wms/outbound/banQinWmWvDetail/allocCancelPick",
        success: function (data) {
            if (data.success) {
                window.location = "${ctx}/wms/outbound/banQinWmWvHeader/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                closeDisable();
                jp.bqError(data.msg);
            }
        }
    });
}

function createTaskByDel() {
    var allocRows = [];
    var rows = getSelections('#cancelAllocTable');
    if (rows.length === 0) {
        jp.bqError("请选择记录");
        return;
    }
    for (var i = 0; i < rows.length; i++) {
        if (rows[i].status === '60') {
            rows[i].soNo = rows[i].orderNo;
            allocRows.push(rows[i]);
        }
    }
    if (allocRows.length === 0) {
        jp.bqError('请选择取消拣货的记录');
        return;
    }
    var isAllocLoc = $('#allocLoc').prop('checked') ? 'Y' : 'N';
    var isTaskPa = $('#isTaskPa').val();
    for (var index = 0, length = allocRows.length; index < length; index++) {
        allocRows[index].isTaskPa = isTaskPa;
        allocRows[index].isAllocLoc = isAllocLoc;
    }
    jp.loading();
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(allocRows),
        url: "${ctx}/wms/outbound/banQinWmDelAlloc/cancelPick",
        success: function (data) {
            if (data.success) {
                window.location = "${ctx}/wms/outbound/banQinWmWvHeader/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                closeDisable();
                jp.bqError(data.msg);
            }
        }
    });
}

function isTaskPaChange(flag) {
    $('#isTaskPa').val(flag ? 'Y' : 'N');
    $('#allocLoc').prop('disabled', !flag);
    $('#paRuleLoc').prop('disabled', !flag);
    if (!flag) {
        $('#allocLoc').prop('checked', true);
        $('#paRuleLoc').prop('checked', false);
    }
}

/**************************************取消分配日志结束*******************************************/

</script>