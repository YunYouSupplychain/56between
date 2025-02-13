<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var currentLoginName = '${fns:getUser().loginName}';
var orgId = '${fns:getUser().office.id}';
var ldStatus = '';

$(document).ready(function () {
    // 初始化订单明细tab
    initOrderDetailTab();
    // 初始化包裹明细tab
    initTraceIdTab();
    // 初始化待装车明细tab
    initLoadingTab();
    // 初始化已装车明细tab
    initCancelTab();
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

    $('#packDetail_traceId').bind('keydown', function (event) {
        var event = window.event || arguments.callee.caller.arguments[0];
        if (event.keyCode === 13){
            enterHandler();
        }
    });
}

/**
 * 激活默认Tab页
 */
function activeTab() {
    var detailTabIndex = sessionStorage.getItem("LD_" + currentLoginName +"_detailTab");
    detailTabIndex = !detailTabIndex ? 0 : detailTabIndex;
    // 默认选择加载Tab页
    $("#detailTab li:eq(" + detailTabIndex + ") a").tab('show');
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

/**
 * 明细Tab页索引切换
 */
function detailTabChange(index) {
    sessionStorage.setItem("LD_" + currentLoginName + "_detailTab", index);
}

/**************************************装车单头开始********************************************/

/**
 * SO单头保存
 */
function save() {
    var isValidate = bq.headerSubmitCheck('#inputForm');
    if (isValidate.isSuccess) {
        jp.loading();
        var disabledObjs = bq.openDisabled('#inputForm');
        var params = $('#inputForm').bq_serialize();
        bq.closeDisabled(disabledObjs);
        jp.post("${ctx}/wms/outbound/banQinWmLdHeader/save", params, function (data) {
            if (data.success) {
                jp.success(data.msg);
                window.location = "${ctx}/wms/outbound/banQinWmLdHeader/form?id=" + data.body.entity.id;
            } else {
                jp.bqError(data.msg);
            }
        })
    } else {
        jp.bqError(isValidate.msg);
    }
}

/**
 * 发货确认
 */
function shipment() {
    commonMethod('shipment');
}

/**
 * 装车交接
 */
function loadDelivery() {
    commonMethod('loadDelivery');
}

/**
 * 取消发货
 */
function cancelShipment() {
    commonMethod('cancelShipment');
}

/**
 * 取消交接
 */
function cancelDelivery() {
    commonMethod('cancelDelivery');
}

/**
 * 公共调用方法
 * @param method 方法名
 */
function commonMethod(method) {
    jp.loading();
    jp.get("${ctx}/wms/outbound/banQinWmLdHeader/" + method + "?ldNo=" + $('#ldNo').val() + "&orgId=" + $('#orgId').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/outbound/banQinWmLdHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 初始化
 */
function init() {
    // 获取装车单系统控制参数
    bq.get("${ctx}/sys/sysControlParams/getValue?paramCode=LOAD_ALLOW_STATUS&orgId=" + jp.getCurrentOrg().orgId, false, function (data) {
        if (!data || !(status === 'PK' || status === 'SP')) {
            ldStatus = 'PK';
        } else {
            ldStatus = data;
        }
    });

    lay('.laydate').each(function(){
        laydate.render({
            elem: this, theme: '#393D49', type: 'datetime'
        });
    });

    // 初始化表头label
    initLabel();
    // 初始化按钮
    buttonControl();
    // 加载明细
    loadDetail();
}

function loadDetail() {
    if ($('#id').val()) {
        var params = {ldNo: $('#ldNo').val(), orgId: $('#orgId').val()};
        $.ajax({
            url: "${ctx}/wms/outbound/banQinWmLdDetail/detailData",
            type: "post",
            data: JSON.stringify(params),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    $("#orderDetailTable").bootstrapTable('removeAll').bootstrapTable('load', data.body.detailList);
                    $("#traceIdTable").bootstrapTable('removeAll').bootstrapTable('load', data.body.traceList);
                    $("#loadingTable").bootstrapTable('removeAll').bootstrapTable('load', data.body.noLoadList);
                    $("#cancelTable").bootstrapTable('removeAll').bootstrapTable('load', data.body.hasLoadList);
                }
            }
        })
    }
}

/**
 * 初始化表头label
 */
function initLabel() {
    if (!$('#id').val()) {
        $('#orgId').val(jp.getCurrentOrg().orgId);
        if (ldStatus === 'SP') {
            $('#soStatus').val('80');
        } else {
            $('#soStatus').val('60');
        }
    } else {
        $('#ldType').prop('disabled', true);
    }
    // 公共信息
    $('#fmLdTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#toLdTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#deliverTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
}

function buttonControl() {
    buttonAllByHeader(false); //订单头
    buttonAllBySoOrder(false); //出库订单明细
    buttonAllByTraceId(false); //包裹明细
    buttonAllByLoading(false); //待装车明细
    buttonAllByCancelLoading(false); //已装车明细
    buttonControlByHeader(); //装车单按钮控制
    buttonControlByDetail(); //明细按钮控制
}

function buttonAllByHeader(flag) {
    $('#header_save').attr('disabled', flag);
    $('#header_shipment').attr('disabled', flag);
    $('#header_loadDelivery').attr('disabled', flag);
    $('#header_cancelShipment').attr('disabled', flag);
    $('#header_cancelDelivery').attr('disabled', flag);
}

function buttonAllBySoOrder(flag) {
    $('#btn_add_so').attr('disabled', flag);
    $('#btn_remove_so').attr('disabled', flag);
    $('#btn_loading_so').attr('disabled', flag);
}

function buttonAllByTraceId(flag) {
    $('#btn_scanning').attr('disabled', flag);
    $('#btn_removeTraceId').attr('disabled', flag);
    $('#btn_loadingByTraceId').attr('disabled', flag);
}

function buttonAllByLoading(flag) {
    $('#btn_addPicking').attr('disabled', flag);
    $('#btn_removePicking').attr('disabled', flag);
    $('#btn_batchLoading').attr('disabled', flag);
    $('#btn_ldList').attr('disabled', flag);
}

function buttonAllByCancelLoading(flag) {
    $('#btn_cancel').attr('disabled', flag);
}

function buttonControlByHeader() {
    if (!$('#id').val()) {
        buttonAllByHeader(true);
        $('#header_save').attr('disabled', false);
        // dockButtonAll(false); //月台按钮全灰置
        return;
    }
    buttonAllByHeader(true);
    if ($('#status').val() === '10') {
        $('#header_save').attr('disabled', false);
    } else if ($('#status').val() === '30') {
        // 部分装车，除新增都不能使用
    } else if ($('#status').val() === '40') {
        // 完全装车
        if ($('#soStatus').val() === '60') {
            $('#header_shipment').attr('disabled', false);
        } else if ($('#soStatus').val() === '80') {
            // 出库单状态 = 完全拣发货
            if (ldStatus === 'PK') {
                $('#header_cancelShipment').attr('disabled', false);
            }
            $('#header_loadDelivery').attr('disabled', false);
        }
    } else if ($('#status').val() === '60') {
        $('#header_cancelDelivery').attr('disabled', false);
    }
}

function buttonControlByDetail() {
    if (!$('#id').val()) {
        buttonAllBySoOrder(true); //订单明细
        buttonAllByTraceId(true); //包裹明细
        buttonAllByLoading(true); //待装车明细
        buttonAllByCancelLoading(true); //已装车明细
        return;
    }
    // 全列表操作
    if ($('#status').val() === '10') {
        // 装车单状态=创建，全可操作
    } else if ($('#status').val() === '30') {
        // 装车单状态 = 部分装车
        if ($('#soStatus').val() === '60') {
            // 出库单状态 = 完全拣货，全可操作
        } else if ($('#soStatus').val() === '80') {
            // 出库单状态 = 完全发货
            if (ldStatus === 'PK') {
                // 完全发货在装车确认之后，发货后只能装车交接或取消发货
                buttonAllBySoOrder(true); //订单明细
                buttonAllByTraceId(true); //包裹明细
                buttonAllByLoading(true); //待装车明细
                buttonAllByCancelLoading(true); //已装车明细
            } else if (ldStatus === 'SP') {
                // 如果控制参数是发货SP,全部可用
            }
        }
    } else if ($('#status').val() === '40') {
        // 装车单状态 = 完全装车
        if ($('#soStatus').val() === '60') {
            // 出库单状态 = 完全拣货，可以新增，以及发货
            buttonAllBySoOrder(true); // 订单明细
            $('#btn_add_so').attr('disabled', false);
            buttonAllByTraceId(true); // 包裹明细
            $('#btn_scanning').attr('disabled', false);
            buttonAllByLoading(true); // 待装车明细
            $('#btn_addPicking').attr('disabled', false);
            buttonAllByCancelLoading(false); // 已装车明细
        } else if ($('#soStatus').val() === '80') {
            // 出库单状态 = 完全发货
            if (ldStatus === 'PK') {
                // 完全发货在装车确认之后，发货后只能装车交接或取消发货
                buttonAllBySoOrder(true); //订单明细
                buttonAllByTraceId(true); //包裹明细
                buttonAllByLoading(true); //待装车明细
                buttonAllByCancelLoading(true); //已装车明细
            } else if (ldStatus === 'SP') {
                // 完全发货在装车确认之后，发货后只能装车交接或取消发货
                buttonAllBySoOrder(true); //订单明细
                $('#btn_add_so').attr('disabled', false);
                buttonAllByTraceId(true); //包裹明细
                $('#btn_scanning').attr('disabled', false);
                buttonAllByLoading(true); //待装车明细
                $('#btn_addPicking').attr('disabled', false);
                buttonAllByCancelLoading(false); //已装车明细
            }
        }
    } else if ($('#status').val() === '60') {
        // 装车单状态 = 完全交接
        buttonAllBySoOrder(true); //订单明细
        buttonAllByTraceId(true); //包裹明细
        buttonAllByLoading(true); //待装车明细
        buttonAllByCancelLoading(true); //已装车明细
    }
}

/**************************************装车单头结束********************************************/

function initOrderDetailTab() {
    $('#orderDetailTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        onClickRow: function(row, $el) {
            jp.changeTableStyle($el);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'rowNum',
            title: '序号',
            sortable: true
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'logisticNo',
            title: '物流单号',
            sortable: true
        }, {
            field: 'soType',
            title: '出库单类型',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_TYPE'))}, value, "-");
            }
        }, {
            field: 'ldStatus',
            title: '装车状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_LD_STATUS'))}, value, "-");
            }
        }, {
            field: 'fmEtd',
            title: '预计发货时间从',
            sortable: true
        }, {
            field: 'toEtd',
            title: '预计发货时间到',
            sortable: true
        }]
    });
}

function initTraceIdTab() {
    $('#traceIdTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        onClickRow: function(row, $el) {
            jp.changeTableStyle($el);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'rowNum',
            title: '序号',
            sortable: true
        }, {
            field: 'toId',
            title: '箱号',
            sortable: true
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'logisticNo',
            title: '物流单号',
            sortable: true
        }, {
            field: 'trackingNo',
            title: '快递单号',
            sortable: true
        }, {
            field: 'wsaQtyEa',
            title: '总数量EA',
            sortable: true
        }, {
            field: 'wsaNetWeight',
            title: '总重量',
            sortable: true
        }, {
            field: 'wsaCubic',
            title: '总体积',
            sortable: true
        }, {
            field: 'wsaGrossWeight',
            title: '总毛重',
            sortable: true
        }, {
            field: 'consigneeCode',
            title: '收货人编码',
            sortable: true
        }, {
            field: 'consigneeName',
            title: '收货人名称',
            sortable: true
        }, {
            field: 'consigneeTel',
            title: '收货人电话',
            sortable: true
        }, {
            field: 'consigneeAddr',
            title: '收货人地址',
            sortable: true
        }, {
            field: 'contactName',
            title: '联系人名称',
            sortable: true
        }, {
            field: 'contactTel',
            title: '联系人电话',
            sortable: true
        }, {
            field: 'contactAddr',
            title: '联系人地址',
            sortable: true
        }]
    });
}

function initLoadingTab() {
    $('#loadingTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        onClickRow: function(row, $el) {
            jp.changeTableStyle($el);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'lineNo',
            title: '行号',
            sortable: true
        }, {
            field: 'status',
            title: '装车状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_LD_STATUS'))}, value, "-");
            }
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主名称',
            sortable: true
        }, {
            field: 'allocId',
            title: '分配ID',
            sortable: true
        }, {
            field: 'allocStatus',
            title: '分配明细状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ALLOC_STATUS'))}, value, "-");
            }
        }, {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        }, {
            field: 'skuName',
            title: '商品名称',
            sortable: true
        }, {
            field: 'packOp',
            title: '打包人',
            sortable: true
        }, {
            field: 'uomDesc',
            title: '包装单位',
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
            field: 'loadQty',
            title: '装载数量',
            sortable: true
        }, {
            field: 'wsaCubic',
            title: '体积',
            sortable: true
        }, {
            field: 'wsaGrossWeight',
            title: '毛重',
            sortable: true
        }, {
            field: 'toId',
            title: '目标跟踪号',
            sortable: true
        }]
    });
}

function initCancelTab() {
    $('#cancelTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        onClickRow: function(row, $el) {
            jp.changeTableStyle($el);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'lineNo',
            title: '行号',
            sortable: true
        }, {
            field: 'status',
            title: '装车状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_LD_STATUS'))}, value, "-");
            }
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主名称',
            sortable: true
        }, {
            field: 'allocId',
            title: '分配ID',
            sortable: true
        }, {
            field: 'allocStatus',
            title: '分配明细状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ALLOC_STATUS'))}, value, "-");
            }
        }, {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        }, {
            field: 'skuName',
            title: '商品名称',
            sortable: true
        }, {
            field: 'uomDesc',
            title: '包装单位',
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
            field: 'qtyUom',
            title: '装载数量',
            sortable: true
        }, {
            field: 'wsaCubic',
            title: '体积',
            sortable: true
        }, {
            field: 'wsaGrossWeight',
            title: '毛重',
            sortable: true
        }, {
            field: 'toId',
            title: '目标跟踪号',
            sortable: true
        }]
    });
}

/****************************************订单明细开始***********************************************/

function addSoListHandler() {
    $('#orderDetailModal').modal();
    $('#allocDetailTable').bootstrapTable('destroy').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = $('#orderDetail_searchForm1').serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            if (ldStatus === 'PK') {
                searchParam.statuss = '60';
            } else if (ldStatus === 'SP') {
                searchParam.statuss = '80';
            }
            searchParam.soTypes = searchParam.soType ? [searchParam.soType] : null;
            searchParam.ownerCodes = searchParam.ownerCode ? [searchParam.ownerCode] : null;
            searchParam.carrierCodes = searchParam.carrierCode ? [searchParam.carrierCode] : null;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主名称',
            sortable: true
        }, {
            field: 'logisticNo',
            title: '物流单号',
            sortable: true
        }, {
            field: 'soType',
            title: '出库单类型',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_TYPE'))}, value, "-");
            }
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_STATUS'))}, value, "-");
            }
        }, {
            field: 'orderTime',
            title: '订单时间',
            sortable: true
        }, {
            field: 'fmEtd',
            title: '预计发货时间从',
            sortable: true
        }, {
            field: 'toEtd',
            title: '预计发货时间到',
            sortable: true
        }]
    });
}

function queryAlloc() {
    $('#allocDetailTable').bootstrapTable('refresh', {'url': "${ctx}/wms/outbound/banQinWmLdDetail/allocData"});
}

function resetAlloc() {
    $(':input', '#orderDetail_searchForm1').val('');
}

function orderDetailSelectConfirm() {
    var rows = getSelections('#allocDetailTable');
    if (rows.length === 0) {
        jp.warning("请选择记录！");
        return;
    }
    var soNos = $.map(rows, function(n, i) {return n.soNo;});
    var params = {ldNo: $('#ldNo').val(), soNos: soNos, orgId: $('#orgId').val()};
    jp.loading();
    $.ajax({
        url: "${ctx}/wms/outbound/banQinWmLdDetail/addLdDetailBySoNo",
        type: 'POST',
        data: JSON.stringify(params),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                $('#orderDetailModal').modal('hide');
                jp.success(data.msg);
                window.location = "${ctx}/wms/outbound/banQinWmLdHeader/form?id=" + $('#id').val();
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

function removeSoListHandler() {
    var rows = getSelections('#orderDetailTable');
    if (rows.length === 0) {
        jp.warning("请选择记录！");
        return;
    }

    var soNos = $.map(rows, function(n, i) {return n.soNo;});
    var params = {ldNo: $('#ldNo').val(), soNos: soNos, orgId: $('#orgId').val()};
    jp.loading();
    $.ajax({
        url: "${ctx}/wms/outbound/banQinWmLdDetail/removeByLdNoAndSoNo",
        type: 'POST',
        data: JSON.stringify(params),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                $('#orderDetailModal').modal('hide');
                jp.success(data.msg);
                window.location = "${ctx}/wms/outbound/banQinWmLdHeader/form?id=" + $('#id').val();
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

function loadingBySoNoHandler() {
    var rows = getSelections('#orderDetailTable');
    if (rows.length === 0) {
        jp.warning("请选择记录！");
        return;
    }

    var soNos = $.map(rows, function(n, i) {return n.soNo;});
    var params = {ldNo: $('#ldNo').val(), soNos: soNos, orgId: $('#orgId').val()};
    jp.loading();
    $.ajax({
        url: "${ctx}/wms/outbound/banQinWmLdDetail/outboundLoadingBySoNo",
        type: 'POST',
        data: JSON.stringify(params),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                $('#orderDetailModal').modal('hide');
                jp.success(data.msg);
                window.location = "${ctx}/wms/outbound/banQinWmLdHeader/form?id=" + $('#id').val();
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

/****************************************订单明细结束***********************************************/
/****************************************包裹明细开始***********************************************/

function scanningHandler() {
    $('#packDetailModal').modal();
    $('#packDetail_traceId').val('');
    $('#packDetail_checkNum').prop('checked', false);
    $('#packDetail_num').val('');
    $('#packDetailTable').bootstrapTable('destroy').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        columns: [{
            checkbox: true
        }, {
            field: 'num',
            title: '序号',
            sortable: true
        }, {
            field: 'toId',
            title: '箱号',
            sortable: true
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'trackingNo',
            title: '快递单号',
            sortable: true
        }, {
            field: 'qtyEa',
            title: '总数量EA',
            sortable: true
        }, {
            field: 'grossWeight',
            title: '总毛重',
            sortable: true
        }, {
            field: 'netWeight',
            title: '总净重',
            sortable: true
        }, {
            field: 'cubic',
            title: '总体积',
            sortable: true
        }]
    });
}

function enterHandler() {
    var traceId = $('#packDetail_traceId').val();
    if (traceId) {
        var isCheck = $('#packDetail_checkNum').prop('checked');
        if (isCheck) {
            var array = $('#packDetailTable').bootstrapTable('getData');
            for (var index in array) {
                if (array[index].toId === traceId) {
                    $('#packDetailTable').bootstrapTable('remove', {field: 'toId', values: [traceId]});
                }
            }
            var sum = $('#packDetailTable').bootstrapTable('getData').length;
            $('#packDetail_num').val(sum);
        } else {
            var array = $('#packDetailTable').bootstrapTable('getData');
            for (var index in array) {
                if (array[index].toId === traceId) {
                    jp.bqWaring("箱号已存在!", function () {$('#packDetail_traceId').val('').focus();});
                    return;
                }
            }

            var disabledObjs = bq.openDisabled('#inputForm');
            var current = bq.serializeJson($('#inputForm'));
            current.traceId = traceId;
            bq.closeDisabled(disabledObjs);
            $.ajax({
                url: "${ctx}/wms/outbound/banQinWmLdDetail/enterByTraceId",
                type: 'POST',
                data: JSON.stringify(current),
                contentType: "application/json",
                success: function (data) {
                    if (data.success) {
                        var sum = $('#packDetailTable').bootstrapTable('getData').length;
                        var row = data.body.entity;
                        row.num = sum + 1;
                        $('#packDetailTable').bootstrapTable('append', row);
                        $('#packDetail_num').val(sum + 1);
                        $('#packDetail_traceId').val('').focus();
                    } else {
                        jp.bqWaring(data.msg, function () {$('#packDetail_traceId').val('').focus();});
                    }
                }
            });
        }
    }
}

function removePackDetail() {
    var rows = getSelections('#packDetailTable');
    if (rows.length === 0) {
        jp.warning("请选择记录！");
        return;
    }
    var sum = $('#packDetailTable').bootstrapTable('getData').length;
    for (var index in rows) {
        $('#packDetailTable').bootstrapTable('remove', {field: 'toId', values: [rows[index].toId]});
    }

    $('#packDetail_num').val(sum - rows.length);
}

function confirmPackDetail() {
    var rows = $('#packDetailTable').bootstrapTable('getData');
    if (rows.length === 0) {
        jp.warning("请扫描箱号");
        return;
    }
    var toIds = $.map($("#packDetailTable").bootstrapTable('getData'), function (row) {return row.toId;});
    var params = {ldNo: $('#ldNo').val(), traceId: toIds.join(','), orgId: $('#orgId').val()};
    jp.loading();
    $.ajax({
        url: "${ctx}/wms/outbound/banQinWmLdDetail/addLdDetailByTraceId",
        type: 'POST',
        data: JSON.stringify(params),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                $('#orderDetailModal').modal('hide');
                jp.success(data.msg);
                window.location = "${ctx}/wms/outbound/banQinWmLdHeader/form?id=" + $('#id').val();
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

function removeTraceIdHandler() {
    var rows = getSelections('#traceIdTable');
    if (rows.length === 0) {
        jp.warning("请选择记录！");
        return;
    }
    jp.confirm("是否确认删除？", function () {
        var toIds = $.map(rows, function(n, i) {return n.toId;});
        var params = {ldNo: $('#ldNo').val(), toIds: toIds, orgId: $('#orgId').val()};
        jp.loading();
        $.ajax({
            url: "${ctx}/wms/outbound/banQinWmLdDetail/removeByLdNoAndTraceId",
            type: 'POST',
            data: JSON.stringify(params),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    $('#orderDetailModal').modal('hide');
                    jp.success(data.msg);
                    window.location = "${ctx}/wms/outbound/banQinWmLdHeader/form?id=" + $('#id').val();
                } else {
                    jp.bqError(data.msg);
                }
            }
        });
    });
}

function loadingByTraceIdHandler() {
    if ($('#status').val() === '40') {
        jp.error("完全装车状态，不能操作");
        return;
    }
    var rows = getSelections('#traceIdTable');
    if (rows.length === 0) {
        jp.warning("请选择记录！");
        return;
    }
    var toIds = $.map(rows, function(n, i) {return n.toId;});
    var params = {ldNo: $('#ldNo').val(), toIds: toIds, orgId: $('#orgId').val()};
    jp.loading();
    $.ajax({
        url: "${ctx}/wms/outbound/banQinWmLdDetail/outboundLoadingByTraceId",
        type: 'POST',
        data: JSON.stringify(params),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                $('#orderDetailModal').modal('hide');
                jp.success(data.msg);
                window.location = "${ctx}/wms/outbound/banQinWmLdHeader/form?id=" + $('#id').val();
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

/****************************************包裹明细结束***********************************************/
/****************************************待装车明细开始***********************************************/

function addPickingDetailHandler() {
    $('#loadingDetailModal').modal();
    $('#loadingDetailTable').bootstrapTable('destroy').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = $('#loadingDetail_searchForm1').serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            if (ldStatus === 'PK') {
                searchParam.status = '60';
            } else if (ldStatus === 'SP') {
                searchParam.status = '80';
            }
            searchParam.soTypes = searchParam.soType ? [searchParam.soType] : null;
            searchParam.ownerCodes = searchParam.ownerCode ? [searchParam.ownerCode] : null;
            searchParam.carrierCodes = searchParam.carrierCode ? [searchParam.carrierCode] : null;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'allocId',
            title: '分配Id',
            sortable: true
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主名称',
            sortable: true
        }, {
            field: 'logisticNo',
            title: '物流单号',
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
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ALLOC_STATUS'))}, value, "-");
            }
        }, {
            field: 'soType',
            title: '出库单类型',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_TYPE'))}, value, "-");
            }
        }, {
            field: 'uomDesc',
            title: '包装单位',
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
            field: 'orderTime',
            title: '订单时间',
            sortable: true
        }, {
            field: 'fmEtd',
            title: '预计发货时间从',
            sortable: true
        }, {
            field: 'toEtd',
            title: '预计发货时间到',
            sortable: true
        }]
    });
}

function queryLoading() {
    $('#loadingDetailTable').bootstrapTable('refresh', {'url': "${ctx}/wms/outbound/banQinWmLdDetail/loadingData"});
}

function resetLoading() {
    $(':input', '#loadingDetail_searchForm1').val('');
}

function loadingDetailSelectConfirm() {
    var rows = getSelections('#loadingDetailTable');
    if (rows.length === 0) {
        jp.warning("请选择记录！");
        return;
    }
    var allocIds = $.map(rows, function(n, i) {return n.allocId;});
    var params = {ldNo: $('#ldNo').val(), allocIds: allocIds, orgId: $('#orgId').val()};
    jp.loading();
    $.ajax({
        url: "${ctx}/wms/outbound/banQinWmLdDetail/addLdDetailByAllocId",
        type: 'POST',
        data: JSON.stringify(params),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                $('#loadingDetailModal').modal('hide');
                jp.success(data.msg);
                window.location = "${ctx}/wms/outbound/banQinWmLdHeader/form?id=" + $('#id').val();
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

function removePickingDetailHandler() {
    var rows = getSelections('#loadingTable');
    if (rows.length === 0) {
        jp.warning("请选择记录！");
        return;
    }
    jp.confirm("是否确认删除？", function () {
        var lineNo = $.map(rows, function(n, i) {return n.lineNo;});
        var params = {ldNo: $('#ldNo').val(), lineNo: lineNo.join(','), orgId: $('#orgId').val()};
        jp.loading();
        $.ajax({
            url: "${ctx}/wms/outbound/banQinWmLdDetail/removeByLdNoAndLineNo",
            type: 'POST',
            data: JSON.stringify(params),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    window.location = "${ctx}/wms/outbound/banQinWmLdHeader/form?id=" + $('#id').val();
                } else {
                    jp.bqError(data.msg);
                }
            }
        });
    });
}

function batchLoadingHandler() {
    if ($('#status').val() === '40') {
        jp.error("完全装车状态，不能操作");
        return;
    }
    var rows = getSelections('#loadingTable');
    if (rows.length === 0) {
        jp.warning("请选择记录！");
        return;
    }
    jp.loading();
    $.ajax({
        url: "${ctx}/wms/outbound/banQinWmLdDetail/outboundBatchLoading",
        type: 'POST',
        data: JSON.stringify(rows),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                jp.success(data.msg);
                window.location = "${ctx}/wms/outbound/banQinWmLdHeader/form?id=" + $('#id').val();
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

function ldListHandler() {
    var rows = getSelections('#loadingTable');
    if (rows.length === 0) {
        jp.warning("请选择记录！");
        return;
    }
    var params = bq.serializeJson($('#inputForm'));
    params.ldDetail10Entity = rows;
    jp.loading();
    $.ajax({
        url: "${ctx}/wms/outbound/banQinWmLdDetail/newLd",
        type: 'POST',
        data: JSON.stringify(params),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                jp.success(data.msg);
                window.location = "${ctx}/wms/outbound/banQinWmLdHeader/form?id=" + $('#id').val();
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

/****************************************待装车明细结束***********************************************/

function cancelLoadingHandler() {
    var rows = getSelections('#cancelTable');
    if (rows.length === 0) {
        jp.warning("请选择记录！");
        return;
    }
    var lineNo = $.map(rows, function(n, i) {return n.lineNo;});
    var params = {ldNo: $('#ldNo').val(), lineNo: lineNo.join(','), orgId: $('#orgId').val()};
    jp.loading();
    $.ajax({
        url: "${ctx}/wms/outbound/banQinWmLdDetail/cancelByLdNoAndLineNo",
        type: 'POST',
        data: JSON.stringify(params),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                jp.success(data.msg);
                window.location = "${ctx}/wms/outbound/banQinWmLdHeader/form?id=" + $('#id').val();
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

</script>