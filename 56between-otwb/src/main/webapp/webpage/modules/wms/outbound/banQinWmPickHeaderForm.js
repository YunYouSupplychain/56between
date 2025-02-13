<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var allocDetail_currentRow; // 分配明细当前行
var allocDetail_isShowTab = false; // 是否显示右边上架任务tab页

$(document).ready(function () {
    // 初始化分配明细tab
    initWmPickAllocTab();
    // 初始化
    init();
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

/**************************************拣货单头开始********************************************/
/**
 * 初始化
 */
function init() {
    // 初始化按钮
    buttonControl();
}

/**
 * 功能按扭控制
 */
function buttonControl() {
    buttonAllByAlloc(false);
    buttonControlByAlloc();
}

/**
 * 拣货单 分配明细 功能按扭控制
 */
function buttonAllByAlloc(flag) {
    $('#alloc_pick').attr('disabled', flag);
    $('#alloc_shipment').attr('disabled', flag);
    $('#alloc_cancelAlloc').attr('disabled', flag);
    $('#alloc_cancelPick').attr('disabled', flag);
    $('#alloc_cancelShipment').attr('disabled', flag);
}

/**
 * 分配明细 功能按扭控制
 */
function buttonControlByAlloc() {
    buttonAllByAlloc(true);
    // 批量操作
    if (allocDetail_isShowTab) {
        // 完全分配
        if (allocDetail_currentRow.status === '40') {
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

/**************************************弹出框结束********************************************/

/**************************************分配明细开始********************************************/
/**
 * 初始化分配明细Tab
 */
function initWmPickAllocTab() {
    // 隐藏右边tab页
    hideTabRight('#allocDetail_tab-right', '#allocDetail_tab-left');
    // 初始化明细table
    initWmPickAllocTable();
}

/**
 * 初始化明细table
 */
function initWmPickAllocTable() {
    $('#wmPickAllocTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/outbound/banQinWmSoAlloc/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orgId = $('#id').val() ? $('#orgId').val() : jp.getCurrentOrg.orgId;
            searchParam.pickNo = $('#id').val() ? $('#pickNo').val() : '#';
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
            field: 'preallocId',
            title: '预配Id',
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
        rows = getSelections('#wmPickAllocTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        var disabledObjs = bq.openDisabled("#allocDetailForm");
        var row = {};
        bq.copyProperties(allocDetail_currentRow, row);
        $.extend(row, bq.serializeJson($('#allocDetailForm')));
        rows.push(row);
        bq.closeDisabled(disabledObjs);
    }
    commonAllocDetail('allocPick', rows);
}

/**
 * 发货确认
 */
function allocShipment() {
    var rows = [];
    if (!allocDetail_isShowTab) {
        rows = getSelections('#wmPickAllocTable');
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
        rows = getSelections('#wmPickAllocTable');
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
        rows = getSelections('#wmPickAllocTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    }
    initCancelPickWindow();
    view = "pickAlloc";
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
        rows = getSelections('#wmPickAllocTable');
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
        url: "${ctx}/wms/outbound/banQinWmPickDetail/" + method,
        success: function (data) {
            if (data.success) {
                window.location = "${ctx}/wms/outbound/banQinWmPickHeader/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

/**************************************分配明细结束*******************************************/
</script>