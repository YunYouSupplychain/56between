<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var skuRows = []; // 当前扫描商品的所有相关记录
var soNo; // 订单号
var ownerCode; // 货主
var lotNums = []; // 批次号
var allocIds = []; // 分配ID
var soSerialModel; // 序列号
var soSerialList = []; // 出库序列号列表
var qtyEa = 0; // 复核输入数量EA
var flagSerial = false; // 序列号扫描标志=控制不允许多次Enter
$(document).ready(function () {
    initWmCheckTable();
    // 初始化
    init();
    // 回车监听事件
    document.onkeydown = function(e) {
        var ev = document.all ? window.event : e;
        if(ev.keyCode === 13) {
            enterEvent(e);
        }
    };
    // 鼠标监听事件
    document.onclick = function (ev) {
        // 光标定位商品扫描
        if ($(ev.target).prop('id') === 'qty_editor') {
            mouseClickQty();
        }
    };
});

function initWmCheckTable() {
    $('#banQinWmCheckTable').bootstrapTable({
        height: $(window).height() - 335,
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.soNo = $('#soNo_query').val();
            searchParam.toId = $('#toId_query').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onLoadSuccess: function(data) {
            queryCallBack(data);
        },
        onClickRow: function(row, $el) {
            jp.changeTableStyle($el);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'ownerName',
            title: '货主名称',
            sortable: false
        }, {
            field: 'skuCode',
            title: '商品编码',
            sortable: false
        }, {
            field: 'skuName',
            title: '商品名称',
            sortable: false
        }, {
            field: 'isSerial',
            title: '是否序列号管理',
            sortable: false,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'qtyUom',
            title: '拣货数',
            sortable: false
        }, {
            field: 'qtyEa',
            title: '拣货数EA',
            sortable: false
        }, {
            field: 'qtyCheckUom',
            title: '复核数',
            sortable: false
        }, {
            field: 'qtyCheckEa',
            title: '复核数EA',
            sortable: false
        }, {
            field: 'packDesc',
            title: '包装规格',
            sortable: false
        }, {
            field: 'uomDesc',
            title: '包装单位',
            sortable: false
        }, {
            field: 'trackingNo',
            title: '快递单号',
            sortable: false
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: false
        }, {
            field: 'lineNo',
            title: '出库单行号',
            sortable: false
        }, {
            field: 'toId',
            title: '箱号',
            sortable: false
        }, {
            field: 'status',
            title: '状态',
            sortable: false,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ALLOC_STATUS'))}, value, "-");
            }
        }, {
            field: 'checkStatus',
            title: '复核状态',
            sortable: false,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_CHECK_STATUS'))}, value, "-");
            }
        }, {
            field: 'checkOp',
            title: '复核人',
            sortable: false
        }, {
            field: 'checkTime',
            title: '复核时间',
            sortable: false
        }, {
            field: 'allocId',
            title: '分配Id',
            sortable: false
        }]
    });
}

function init() {
    $('#soRadio').prop('checked', true);
    $('#isPrintContainer').val('Y');
    // 是否输入快递单号
    isTrackChange();
    // 默认单选控件选择出库单号
    $('#soNo_query').focus();
    clickRadioButton();
}

function enterEvent(e) {
    var inputId = $(e.target).prop('id');
    switch (inputId) {
        // 光标定位soNo，traceId，执行查询
        case "soNo_query":
            query();
            break;
        case "toId_query":
            query();
            break;
        // 光标定位商品扫描
        case "skuCode_editor":
            scanSku();
            break;
        // 光标定位数量
        case "qty_editor":
            enterQty();
            break;
        // 序列号扫描
        case "serialNo_editor":
            scanSerial();
            break;
        // 校验是否全部复核，如果全部复核，那么提示是否复核确认
        case "trackingNo_editor":
            // 校验是否全部复核，如果全部复核，那么提示是否复核确认
            if (!isCheckAll()) {
                return;
            }
            // 光标定位快递单号，复核确认
            checkConfirmHandler();
            break;
    }
}

/**
 * 查询
 */
function query() {
    if ($('#soRadio').prop('checked')) {
        if (!$('#soNo_query').val()) {
            jp.bqWaring("出库单号不能为空", function () {$('#soNo_query').focus();});
            return;
        }
    } else if ($('#boxRadio').prop('checked')) {
        if (!$('#toId_query').val()) {
            jp.bqWaring("箱号不能为空", function () {$('#toId_query').focus();});
            return;
        }
    }
    $('#banQinWmCheckTable').bootstrapTable("refresh", {'url': "${ctx}/wms/outbound/banQinWmCheck/data"});
}

/**
 * 查询后回调
 */
function queryCallBack(data) {
    if (data.success) {
        var wmCheckEntity = data.body.entity;
        $("input[id$=_editor]").each(function() {
            $(this).val('');
        });
        $('#serialNo_editor').prop('readonly', true); //序列号默认不可编辑
        $('#isSingle').prop('disabled', false);
        // 是否单件扫描控制
        isSingleScan();
        // 刷新列表
        $('#banQinWmCheckTable').bootstrapTable('load', wmCheckEntity);
        // 写快递单号
        var trackingNo = '';
        if (wmCheckEntity.rows.length > 0) {
            trackingNo = wmCheckEntity.rows[0].soTrackingNo;
        }
        $('#trackingNo_editor').val(trackingNo);
        // 写统计
        setQtyCount();
        // 写包装单位
        getCheckRecord($('#banQinWmCheckTable').bootstrapTable('getData'));
        //光标定位商品扫描
        $('#skuCode_editor').focus();
        // 需扫描数
        $('#qtySkuPickEa_editor').val('');
        // //未扫描数
        $('#qtySkuNotScanEa_editor').val('');
        $('#qtyEa_editor').val('');
        // 功能按扭控制
        buttonControl();
    } else {
        jp.bqWaring(data.msg);
    }
}

/**
 * 重置
 */
function reset() {
    $('#soNo_query').val('');
    $('#toId_query').val('');
    $("input[id$=_editor]").each(function() {
        $(this).val('');
    });
    $('#serialNo_editor').prop('readonly', true);
    $('#banQinWmCheckTable').bootstrapTable('removeAll');
    $('#isSingle').prop('disabled', false);
    setQtyCount();
    // 是否单件扫描控制
    isSingleScan();
    // 单选控制控制
    clickRadioButton();
    $('#soRadio').prop('disabled', false);
    $('#boxRadio').prop('disabled', false);
    // 功能按扭控制
    buttonControl();
}

/**
 * 如果输入商品后，没有按ENTER键，直接将光标定位到数量输入框，那么默认根据SKU和单位 写包装单位
 */
function mouseClickQty() {
    // 获取当前商品所有记录
    if (!getSkuRows()) {
        return;
    }
    // 改变单位
    changeUom();
    // 光标定位数量
    $('#qty_editor').focus();
}

/**
 * 商品扫描后
 */
function scanSku() {
    var skuCode = $('#skuCode_editor').val();
    if (!skuCode) {
        jp.bqWaring("商品不能为空", function () {$('#skuCode_editor').focus();});
        return;
    }
    // 获取当前商品所有记录
    if (!getSkuRows()) {
        return;
    }
    $('#skuCode_editor').blur();
    // 校验是否全部复核，如果全部复核，那么提示是否复核确认
    if (!isCheckAll()) {
        return;
    }
    // 校验商品是否需要序列号扫描
    if (skuRows.length > 0) {
        var tmpRecord = skuRows[0];
        ownerCode = tmpRecord.ownerCode;
        soNo = tmpRecord.soNo;
        if (tmpRecord.isSerial === 'Y') {
            // 需要序列号扫描，光标定位序列号
            $('#serialNo_editor').prop('readonly', false).focus();
            // 只能单件扫描,且不可编辑
            $('#isSingle').prop('checked', true).prop('disabled', true);
            // 如果勾选，是单件扫描，那么包装单位为EA,数量为1,并且灰置不可编辑
            $('#uom_editor').val('EA').prop('disabled', true);
            $('#qty_editor').val(1).prop('readonly', true);
            $('#qtyEa_editor').val(1);
            // 写单位换算单位
            $('#uomQty_editor').val(1);
            return;
        } else {
            $('#serialNo_editor').prop('readonly', true);
            $('#isSingle').prop('disabled', false);
        }
    }

    // 是否单件扫描
    if ($('#isSingle').prop('checked')) {
        // 写单位换算单位
        $('#uomQty_editor').val(1);
        // 写复核
        enterQty();
    } else {
        // 搜索商品、写包装
        if (!getCheckRecord(skuRows)) {
            jp.bqWaring("不存在可以操作的商品[" + skuCode + "]记录", function () {$('#skuCode_editor').select().focus();});
            return;
        }
        $('#qty_editor').val('').focus();
        $('#qtyEa_editor').val('');
    }
}

/**
 * 扫描序列号后
 */
function scanSerial() {
    // 序列号扫描标志=>控制不允许多次Enter
    if (flagSerial) {
        return;
    }
    flagSerial = true;
    // 商品
    var skuCode = $('#skuCode_editor').val();
    if (!skuCode) {
        jp.bqWaring("商品不能为空", function () {$('#skuCode_editor').focus();});
        flagSerial = false;
        return;
    }
    // 序列号
    var serialNo = $('#serialNo_editor').val();
    if (!serialNo) {
        jp.bqWaring("序列号不能为空", function () {$('#serialNo_editor').focus();});
        flagSerial = false;
        return;
    }

    // 校验是否全部复核，如果全部复核，那么提示是否复核确认
    if (!isCheckAll()) {
        flagSerial = false;
        return;
    }
    // 缓存出库序列号
    var serialNos = $.map(soSerialList, function (row) { return row.serialNo });
    // 扣数时，校验是否未扫描
    if ($('#isSubtract').prop('checked')) {
        if (serialNos.indexOf($('#serialNo_editor').val()) === -1) {
            //序列号未扫描，不能操作
            jp.bqWaring($('#serialNo_editor').val() + "序列号未扫描，不能操作");
            flagSerial = false;
            return;
        }
        // 写复核
        enterQty();
    } else {
        // 校验序列号是否存在 校验序列号批次号与拣货记录的批次号是否一致 校验序列号是否唯一
        var loading = jp.loading();
        var params = { soNo: soNo, ownerCode: ownerCode, skuCode: skuCode, serialNo: serialNo, lotNums: lotNums, allocIds: allocIds, orgId: jp.getCurrentOrg().orgId };
        $.ajax({
            headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
            type: 'POST',
            dataType: "json",
            data: JSON.stringify(params),
            url: "${ctx}/wms/outbound/banQinWmCheck/checkSoSerial",
            success: function (data) {
                jp.close(loading);
                if (data.success) {
                    soSerialModel = data.body.entity;
                    // 校验是否重复
                    if (!$('#isSubtract').prop('checked')) {
                        if (serialNos.indexOf(soSerialModel.serialNo) !== -1) {
                            // 序列号已扫描，不能操作
                            jp.bqWaring(soSerialModel.serialNo + "序列号已扫描，不能操作", function () {$('#serialNo_editor').select().focus();});
                            flagSerial = false;
                            return;
                        }
                    }
                    // 写复核
                    enterQty();
                } else {
                    flagSerial = false;
                    jp.bqWaring(data.msg, function () {$('#serialNo_editor').focus();});
                }
            }
        });
    }
    flagSerial = false;
}

/**
 * 获取当前扫描商品所有记录
 * return true:表示获取到商品集 false:表示无该商品
 */
function getSkuRows() {
    skuRows = [];
    lotNums = [];
    allocIds = [];
    var rows = $('#banQinWmCheckTable').bootstrapTable('getData');
    for (var i = 0, length = rows.length; i < length; i++) {
        // 如果光标定位商品扫描，那么搜索该商品单位，未复核数据
        // if (rows[i].skuCode === $('#skuCode_editor').val() && rows[i].checkStatus === '00') {
        if (isContainStr(rows[i].barcode, $('#skuCode_editor').val()) && rows[i].checkStatus === '00') {
            lotNums.push(rows[i].lotNum);
            allocIds.push(rows[i].allocId);
            skuRows.push(rows[i]);
        }
    }
    if (skuRows.length === 0) {
        jp.bqWaring("不存在可以操作的商品" + ($('#skuCode_editor').val() ? $('#skuCode_editor').val() : '')  + "记录", function () {$('#skuCode_editor').select().focus();});
        return false;
    }
    return true;
}

/**
 * 输入数量后操作
 */
function enterQty() {
    var qtyUom = $('#qty_editor').val() ? parseFloat($('#qty_editor').val()) : 0;
    // 扫描EA数量(将输入框中的单位数量换算成EA数 计算)
    qtyEa = qtyUom * ($('#uomQty_editor').val() ? parseFloat($('#uomQty_editor').val()) : 0);
    var skuCode = $('#skuCode_editor').val();
    if (!skuCode) {
        jp.bqWaring("商品不能为空", function () {$('#skuCode_editor').focus();});
        // 扫描序列号，不允许多次扫描
        flagSerial = false;
        return;
    }
    if (qtyEa === 0) {
        jp.bqWaring("数量不能为空", function () {$('#qty_editor').focus();});
        // 扫描序列号，不允许多次扫描
        flagSerial = false;
        return;
    }
    // 写复核数
    if (setQtyCheckUom()) {
        // 是否复核确认
        if (!isCheckAll()) {
            // 扫描序列号，不允许多次扫描
            flagSerial = false;
            return;
        }
        // 清空序列号，并且灰置,【是否单件扫描】可以编辑
        $('#serialNo_editor').val('').prop('readonly', true);
        $('#isSingle').prop('disabled', false);
        // 判断是否单件扫描
        isSingleScan();
        // 出库单或者箱号不可编辑
        $('#soNo_query').prop('readonly', true);
        $('#toId_query').prop('readonly', true);
        $('#soRadio').prop('disabled', true);
        $('#boxRadio').prop('disabled', true);
        // 成功，光标定位商品并清空
        $('#skuCode_editor').val('').focus();
    } else {
        // 扫描序列号，不允许多次扫描
        flagSerial = false;
    }
}

/**
 * 是否确认复核
 */
function clickCheckComfirmHandler() {
    checkConfirmHandler();
}

/**
 * 写入复核数
 * return true:写入成功  false 写入失败
 */
function setQtyCheckUom() {
    // 校验超量复核\超量扣数
    if (!checkOverOrSubtract()) {
        //扫描序列号，不允许多次扫描
        flagSerial=false;
        return false;
    }
    if ($('#isSubtract').prop('checked')) {
        // 如果扣数
        // 写扫描包装相同的商品行记录
        subtractSkuUomEqual();
        // 写扫描包装不同的商品行记录
        subtractSkuUomDiff();
    } else {
        // 写扫描包装相同的商品行记录
        checkSkuUomEqual();
        // 写扫描包装不同的商品行记录
        checkSkuUomDiff();
    }
    // 列表统计
    setQtyCount();
    return true;
}

/**
 * 校验超量复核\超量扣数
 */
function checkOverOrSubtract() {
    // 需要复核的数量
    var qtyCheckEa = 0;
    // 可扣数的复核
    var qtySubtractEa = 0;
    for (var i = 0, length = skuRows.length; i < length; i++) {
        // 搜索当前扫描的商品
        // if (skuRows[i].skuCode === $('#skuCode_editor').val()) {
        if (isContainStr(skuRows[i].barcode, $('#skuCode_editor').val())) {
            // 如果扣数，那么统计可扣减的复核数
            if ($('#isSubtract').prop('checked')) {
                qtySubtractEa += skuRows[i].qtyCheckEa;
            } else {
                // 如果不是扣数，那么统计需要复核的数量
                qtyCheckEa += (skuRows[i].qtyEa - skuRows[i].qtyCheckEa);
            }
        }
    }
    // 如果可扣数 < 当前输入的扣数，那么提示超量扣数
    if ($('#isSubtract').prop('checked') && qtySubtractEa < qtyEa) {
        jp.bqWaring("超量扣数，不能操作")
        return false;
    }
    // 如果需复核数 < 当前输入的复核数，那么提示超量复核
    else if (!$('#isSubtract').prop('checked') && qtyCheckEa < qtyEa) {
        jp.bqWaring("超量复核，不能操作");
        return false;
    }
    return true;
}

/**
 * 写复核数+，写保存出库序列号
 */
function addSerialItemAndQtyCheck(record) {
    // 写缓存序列号
    var soSerialItem = record;
    soSerialItem.serialNo = $('#serialNo_editor').val();
    if (soSerialModel != null) {
        soSerialItem.id = soSerialModel.id;
    }
    soSerialList.push(soSerialItem);
    //写复核数
    checkQtyCheckEa(record);
}

/**
 * 复核+ 获取与扫描包装相同的商品行记录
 * param  skuRows 全局变量，当前扫描商品的所有记录集
 * 		  qtyEa:全局变量，当前输入的数量EA(由界面单位数量换算)
 * return true:表示获取到 false：表示获取不到
 */
function checkSkuUomEqual() {
    for (var i = 0, length = skuRows.length; i < length; i++) {
        if (qtyEa <= 0) {
            // 写复核数完成,退出
            // 扫描序列号，不允许多次扫描
            flagSerial = false;
            return;
        }
        // 搜索未完全复核的记录,并且包装单位与扫描包装相同,可复核(包装数>复核数)
        if (skuRows[i].checkStatus === '00' && skuRows[i].uom === $('#uom_editor').val() && skuRows[i].qtyEa > skuRows[i].qtyCheckEa) {
            // 如果扫描序列号,
            if (skuRows[i].isSerial === 'Y') {
                // 批次号一致
                if (!soSerialModel.allocId && skuRows[i].lotNum === soSerialModel.lotNum) {
                    // 写缓存序列号，并且写复核数
                    addSerialItemAndQtyCheck(skuRows[i]);
                } else if (soSerialModel.allocId && skuRows[i].allocId === soSerialModel.allocId) {
                    // 如果分配ID不为空，那么分配ID相同
                    // 写缓存序列号，并且写复核数
                    addSerialItemAndQtyCheck(skuRows[i]);
                }
            } else {
                // 写复核数
                checkQtyCheckEa(skuRows[i]);
            }
        }
    }
}

/**
 * 复核+ 获取与扫描包装不相同的商品行记录
 * param  skuRows 全局变量，当前扫描商品的所有记录集
 * 		  qtyEa:全局变量，当前输入的数量EA(由界面单位数量换算)
 * return true:表示获取到 false：表示获取不到
 */
function checkSkuUomDiff() {
    for (var i = 0, length = skuRows.length; i < length; i++) {
        if (qtyEa <= 0) {
            // 写复核数完成,退出
            // 扫描序列号，不允许多次扫描
            flagSerial = false;
            return;
        }
        // 搜索未完全复核的记录,并且需要复核的(包装数>复核数)
        if (skuRows[i].checkStatus === '00' && skuRows[i].qtyEa > skuRows[i].qtyCheckEa) {
            // 如果扫描序列号
            if (skuRows[i].isSerial === 'Y') {
                // 批次号一致
                if (!soSerialModel.allocId && skuRows[i].lotNum === soSerialModel.lotNum) {
                    // 写缓存序列号，并且写复核数
                    addSerialItemAndQtyCheck(skuRows[i]);
                } else if (soSerialModel.allocId && skuRows[i].allocId === soSerialModel.allocId) {
                    // 如果分配ID不为空，那么分配ID相同
                    // 写缓存序列号，并且写复核数
                    addSerialItemAndQtyCheck(skuRows[i]);
                }
            } else {
                // 写复核数
                checkQtyCheckEa(skuRows[i]);
            }
        }
    }
}

/**
 * 复核+ 写复核数
 */
function checkQtyCheckEa(record) {
    // 需要复核数量 = 包装数-复核数
    var qtyCheckEa = (record.qtyEa ? parseFloat(record.qtyEa) : 0) - (record.qtyCheckEa ? parseFloat(record.qtyCheckEa) : 0);
    // 如果总复核数量 >= 当前行复核数
    if (qtyEa >= qtyCheckEa) {
        // 写复核数ea
        record.qtyCheckEa += qtyCheckEa;
        qtyEa -= qtyCheckEa;
    } else {
        record.qtyCheckEa += qtyEa;
        qtyEa -= qtyEa;
    }
    // 计算复核数(单位换算)
    record.qtyCheckUom = (record.qtyCheckEa ? parseFloat(record.qtyCheckEa) : 0) / (record.uomQty ? parseFloat(record.uomQty) : 1);
    // 光标定位
    setCurrentRowIndex(record);
    // 记录扫描商品的扫描数
    setSkuQtyValue(record);
}

/**
 * 写复核数-扣数，删除出库序列号
 */
function removeSerialItemAndQtyCheck(record) {
    for (var i = 0; i < soSerialList.length; i++) {
        var soSerialItem = soSerialList[i];
        // 记录的分配ID与序列号的分配ID相同，定位记录
        if (record.allocId === soSerialItem.allocId) {
            // 如果记录存在，那么删除缓存中的出库序列号
            if (soSerialItem.serialNo === $('#serialNo_editor').val()) {
                delete soSerialList[i];
                // 写复核数
                subtractQtyCheckEa(record);
                return true;
            }
        }
    }
    return false;
}

/**
 * 扣数- 获取与扫描包装相同的商品行记录
 * param  skuRows 全局变量，当前扫描商品的所有记录集
 * 		  qtyEa:全局变量，当前输入的数量EA(由界面单位数量换算)
 * return true:表示获取到 false：表示获取不到
 */
function subtractSkuUomEqual() {
    for (var i = 0, length = skuRows.length; i < length; i++) {
        if (qtyEa <= 0) {
            // 写复核数完成,退出
            // 扫描序列号，不允许多次扫描
            flagSerial = false;
            return;
        }
        // 搜索未复核完成，并且包装单位与扫描包装相同,可扣数(复核数量>0)
        if (skuRows[i].checkStatus === '00' && skuRows[i].uom === $('#uom_editor').val() && skuRows[i].qtyCheckEa > 0) {
            // 如果扫描序列号
            if (skuRows[i].isSerial === 'Y') {
                if (!removeSerialItemAndQtyCheck(skuRows[i])) {
                    continue;
                }
            } else {
                // 写复核数
                subtractQtyCheckEa(skuRows[i]);
            }
        }
    }
}

/**
 * 扣减- 获取与扫描包装不相同的商品行记录
 * param  skuRows 全局变量，当前扫描商品的所有记录集
 * 		  qtyEa:全局变量，当前输入的数量EA(由界面单位数量换算)
 * return true:表示获取到 false：表示获取不到
 */
function subtractSkuUomDiff() {
    for (var i = 0, length = skuRows.length; i < length; i++) {
        if (qtyEa <= 0) {
            // 写复核数完成,退出
            // 扫描序列号，不允许多次扫描
            flagSerial = false;
            return;
        }
        // 搜索未复核完成，可扣数(复核数量>0)
        if (skuRows[i].checkStatus === '00' && skuRows[i].qtyCheckEa > 0) {
            // 扣数
            // 如果扫描序列号,
            if (skuRows[i].isSerial === 'Y') {
                if (!removeSerialItemAndQtyCheck(skuRows[i])) {
                    continue;
                }
            } else {
                //写复核数
                subtractQtyCheckEa(skuRows[i]);
            }
        }
    }
}

/**
 * 扣数 - 扣减复核数
 */
function subtractQtyCheckEa(record) {
    // 可扣复核数量 = 复核数
    var qtyCheckEa = (record.qtyCheckEa ? parseFloat(record.qtyCheckEa) : 0);
    // 当前复核总数>=当前行可复核数
    if (qtyEa >= qtyCheckEa) {
        // 写复核数ea
        record.qtyCheckEa -= qtyCheckEa;
        qtyEa -= qtyCheckEa;
    } else {
        record.qtyCheckEa -= qtyEa;
        qtyEa -= qtyEa;
    }
    // 计算复核数(单位换算)
    record.qtyCheckUom = (record.qtyCheckEa ? parseFloat(record.qtyCheckEa) : 0) / (record.uomQty ? parseFloat(record.uomQty) : 1);
    // 光标定位
    setCurrentRowIndex(record);
    // 记录扫描商品的扫描数
    setSkuQtyValue(record);
}

/**
 * 完全复核校验
 * return true:未完全复核，false 完全复核
 */
function isCheckAll() {
    // 列表对象
    var arr = $('#banQinWmCheckTable').bootstrapTable('getData');
    var flag = false;
    for (var i = 0, length = arr.length; i < length; i++) {
        // 不扣数，搜索未完全复核的记录,并且可复核的记录(包装数!= 复核数)
        if (!$('#isSubtract').prop('checked') && arr[i].checkStatus === '00' && arr[i].qtyEa > arr[i].qtyCheckEa) {
            // 查找到记录
            flag = true;
            return true;
        } else if ($('#isSubtract').prop('checked') && arr[i].checkStatus === '00') {
            flag = true;
        }
    }
    if (!flag) {
        // 如果光标已经在快递单号中，那么弹出确认复核
        if ($("#trackingNo_editor").is(":focus")) {
            jp.confirm("是否确认复核", clickCheckComfirmHandler);
            return false;
        }
        // 如果已经复核完成，并且快递单号必须输入时，那么光标定位快递单号
        if ($('#isTrack').prop('checked')) {
            $('#trackingNo_editor').focus();
            return false;
        }
        jp.confirm("是否确认复核", clickCheckComfirmHandler);
        return false;
    }
    return true;
}

/**
 * 扫描时，写包装单位数量和计算扫描数
 */
function setSkuQtyValue(record) {
    $('#qtyEa_editor').val($('#uomQty_editor').val());
    $('#qtySkuPickEa_editor').val(record.qtyEa);
    $('#qtySkuNotScanEa_editor').val(record.qtyEa - record.qtyCheckEa);
}

/**
 * 初始赋值 - 定位行，并且写包装单位、换算数量等
 * 	如果有记录，那么定位，如果没有记录，根据具体情况提示
 */
function getCheckRecord(arr) {
    // 列表对象
    var flag = false;
    for (var i = 0, length = arr.length; i < length; i++) {
        // 如果是扣数，那么未完全复核的记录并且可扣数的记录
        if ($('#isSubtract').prop('checked') && arr[i].checkStatus === '00' && arr[i].qtyCheckEa > 0) {
            // 写包装单位
            setPackUom(arr[i]);
            // 查找到记录
            flag = true;
            // 写扫描数
            setSkuQtyValue(arr[i]);
            return true;
        }
        // 不扣数，搜索未完全复核的记录,并且可复核的记录(包装数!= 复核数)
        else if (!$('#isSubtract').prop('checked') && arr[i].checkStatus === '00' && arr[i].qtyEa > arr[i].qtyCheckEa) {
            // 写包装单位
            setPackUom(arr[i]);
            // 查找到记录
            flag = true;
            // 写扫描数
            setSkuQtyValue(arr[i]);
            return true;
        }
    }
    if (!flag) {
        return false;
    }
    return true;
}

/**
 * 定位行，并且写包装单位、换算数量等
 * param record 当前获取到的行记录
 */
function setPackUom(record) {
    // 如果不是单件扫描，写包装(单件扫描，默认EA)
    if (!$('#isSingle').prop('checked')) {
        // 包装单位设置
        $('#uom_editor').val(record.uom);
        // 单位换算数量
        $('#uomQty_editor').val((record.uomQty ? parseFloat(record.uomQty) : 0));
    }
    // 写包装代码
    $('#packCode_editor').val(record.packCode);
    // 光标设置
    setCurrentRowIndex(record);
}

/**
 * 写统计
 */
function setQtyCount() {
    var rows = $('#banQinWmCheckTable').bootstrapTable('getData');
    // 商品数对象
    var skuArr = [];
    // 单品数
    var qtyAllEa = 0;
    // 总重量
    var grossWeight = 0;
    // 总体积
    var cubic = 0;
    // 总已扫描数
    var qtyAllScanEa = 0;
    // 总未扫描数
    var qtyAllNotScanEa = 0;
    for (var i = 0; i < rows.length; i++) {
        var item = rows[i];
        // 商品数
        if (skuArr.indexOf(item.skuCode) === -1) {
            skuArr.push(item.skuCode);
        }
        // 单品数量
        qtyAllEa += (item.qtyEa ? parseFloat(item.qtyEa) : 0);
        // 总重量
        grossWeight += (item.grossWeight ? parseFloat(item.grossWeight) : 0) * (item.qtyEa ? parseFloat(item.qtyEa) : 0);
        // 总体积
        cubic += (item.cubic ? parseFloat(item.cubic) : 0) * (item.qtyEa ? parseFloat(item.qtyEa) : 0);
        // 总已扫描数
        qtyAllScanEa += (item.qtyCheckEa ? parseFloat(item.qtyCheckEa) : 0);
        // 总未扫描数
        qtyAllNotScanEa += (item.qtyEa ? parseFloat(item.qtyEa) : 0) - (item.qtyCheckEa ? parseFloat(item.qtyCheckEa) : 0);
    }
    // 显示界面
    $('#qtySku_editor').val(skuArr.length);
    $('#qtyAllEa_editor').val(qtyAllEa);
    $('#grossWeight_editor').val(grossWeight);
    $('#cubic_editor').val(cubic);
    $('#qtyAllScanEa_editor').val(qtyAllScanEa);
    $('#qtyAllNotScanEa_editor').val(qtyAllNotScanEa);
}

/**
 * 包装单位修改
 */
function changeUom() {
    // 如果商品扫描为空，那么修改后定位商品扫描
    if (!$('#skuCode_editor').val()) {
        $('#skuCode_editor').focus();
        return;
    }
    // 获取商品包装单位，及单位换算数量
    jp.post("${ctx}/wms/outbound/banQinWmCheck/getPackageInfo", { packCode: $('#packCode_editor').val(), uom: $('#uom_editor').val(), orgId: jp.getCurrentOrg().orgId }, function (data) {
        if (data.success) {
            var packageEntity = data.body.entity;
            // 写包装换算数量
            $('#uomQty_editor').val(packageEntity.cdprQuantity);
            $('#qtyEa_editor').val(packageEntity.cdprQuantity);
            // 光标定位数量
            $('#qty_editor').focus();
        } else {
            // 重置包装
            // (不提示，可以重置就重置，不能重置就不修改包装单位)
            getCheckRecord(skuRows);
            jp.bqWaring("该商品没有配置该包装单位");
        }
    });
}

/**
 * 数量修改，换算
 */
function changeQty() {
    if ($('#qty_editor').val()) {
        $('#qtyEa_editor').val(($('#qty_editor').val() ? parseFloat($('#qty_editor').val()) : 0) * ($('#uomQty_editor').val() ? parseFloat($('#uomQty_editor').val()) : 0));
    }
}

/**
 * 单选控件控制
 */
function clickRadioButton() {
    // 选中出库单号
    if ($('#soRadio').prop('checked')) {
        if ($('#toId_query').val() && $('#banQinWmCheckTable').bootstrapTable('getDate').length > 0) {
            reset();
        }
        clickRadioSoNo();
    } else if ($('#boxRadio').prop('checked')) {
        if ($('#soNo_query').val() && $('#banQinWmCheckTable').bootstrapTable('getDate').length > 0) {
            reset();
        }
        clickRadioToId();
    }
}

/**
 * 单选控件控制,选择出库单号
 */
function clickRadioSoNo() {
    // 光标定位出库单号输入框
    $('#soNo_query').focus();
    // 非空标记
    // view.lbl_soNo.required=true;
    $('#soNo_query').prop('readonly', false);
    // 清除箱号非空标记
    // view.lbl_toId.required=false;
    $('#toId_query').val('').prop('readonly', true);
}

/**
 * 单选控件控制,选择箱号
 */
function clickRadioToId() {
    // 光标定位箱号号输入框
    $('#toId_query').focus();
    // 非空标记
    // view.lbl_toId.required=true;
    $('#toId_query').prop('readonly', false);
    // 清除出库单号非空标记
    // view.lbl_soNo.required=false;
    $('#soNo_query').val('').prop('readonly', true);
}

/**
 * 复核确认完成校验
 */
function checkoutIsConFirm(rows) {
    for (var i = 0; i < rows.length; i++) {
        var item = rows[i];
        // 如果包装数>复核数，示复核完成
        if (item.qtyUom > item.qtyCheckUom) {
            jp.bqWaring("未复核完成，不能操作");
            return false;
        }
    }
    return true;
}

/**
 * 复核确认
 */
function checkConfirmHandler() {
    var rows = [];
    var processByCode = null;
    var objNos = []; // 发货条件
    // 按出库单 复核，允许部分复核，勾选 记录
    if ($('#soRadio').prop('checked')) {
        // 复核控制参数，是否全部拣货并且全部复核
        if ('N' === 'N') {
            // 可部分拣拣货和部分复核
            // rows = this.pagingGrid.store.getChangedItems(); //修改的记录
            rows = $('#banQinWmCheckTable').bootstrapTable('getData');
        } else {
            // 全部拣货并且全部复核
            rows = $('#banQinWmCheckTable').bootstrapTable('getData');
        }
        processByCode = 'BY_SO'; // 按订单发货
        objNos.push($('#soNo_query').val());
    } else if ($('#boxRadio').prop('checked')) {
        // 按箱号 复核，必须全部复核
        rows = $('#banQinWmCheckTable').bootstrapTable('getData');
        processByCode = 'BY_TOID'; // 按目标跟踪号
        objNos.push($('#toId_query').val());
    }

    // 复核确认完成校验,包装数、复核数是否相等
    if (!checkoutIsConFirm(rows)) {
        return;
    }
    var trackingNo = $('#trackingNo_editor').val();
    // 校验快递单号是否空
    if ($('#isTrack').prop('checked') && !$('#trackingNo').val()) {
        jp.bqWaring("快递单号不能为空");
        return;
    }
    if (!$('#isTrack').val()) {
        trackingNo = null;
    }

    var allocIds = $.map(rows, function (row) { return row.allocId });
    // var isPrintContainer:String=this.view.isPrintContainer.value as String; //是否打印装箱清单
    var isPrintContainer = 'Y'; // 是否打印装箱清单
    jp.loading();
    var params = { allocIds: allocIds, soSerialList: soSerialList, trackingNo: trackingNo, objNos: objNos, processByCode: processByCode, isPrintContainer: isPrintContainer, orgId: jp.getCurrentOrg().orgId };
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(params),
        url: "${ctx}/wms/outbound/banQinWmCheck/checkConfirm",
        success: function (data) {
            if (data.success) {
                // 按出库单复核，部分复核/完全复核的返回控制
                if ($('#soRadio').prop('checked')) {
                    // 如果存在未复核的记录，那么重新查询，否则清空重置
                    var rows = $('#banQinWmCheckTable').bootstrapTable('getData');
                    var resetFlag = true; // 是否清空标记
                    for (var i = 0; i < rows.length; i++) {
                        var item = rows[i];
                        // 如果包装数>复核数，示复核完成
                        if (item.qtyCheckUom === 0) {
                            resetFlag = false; // 表示不清空
                            break;
                        }
                    }
                    if (resetFlag) {
                        // 清空列表
                        $('#banQinWmCheckTable').bootstrapTable('removeAll');
                        // 清空编辑区
                        reset();
                    } else {
                        // 存在未复核记录，重新查询
                        query();
                    }
                } else if ($('#boxRadio').prop('checked')) {
                    // 清空列表
                    $('#banQinWmCheckTable').bootstrapTable('removeAll');
                    // 清空编辑区
                    reset();
                }
            }
            jp.alert(data.msg);
        }
    });
}

/**
 * 取消复核
 */
function cancelCheckHandler() {
    var rows = $.map($("#banQinWmCheckTable").bootstrapTable('getSelections'), function (row) { return row });
    if (rows.length === 0) {
        jp.bqWaring("请选择记录");
        return;
    }
    var allocIds = $.map(rows, function (row) { return row.allocId });
    var params = { allocIds: allocIds, orgId: jp.getCurrentOrg().orgId };
    jp.loading();
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(params),
        url: "${ctx}/wms/outbound/banQinWmCheck/cancelCheck",
        success: function (data) {
            if (data.success) {
                // 刷新列表
                query();
                $('#soRadio').prop('disabled', false);
                $('#boxRadio').prop('disabled', false);
            }
            jp.alert(data.msg);
        }
    });
}

/**
 * 批量复核
 */
function batchCheckHandler() {
    var rows = $('#banQinWmCheckTable').bootstrapTable('getData');
    if (rows.length === 0) {
        // 没有记录，不执行批量复核
        return;
    }
    // 校验是否序列号扫描
    for (var i = 0; i < rows.length; i++) {
        if (rows[i].isSerial === 'Y') {
            jp.bqWaring("存在需要序列号管理的商品，请扫描复核");
            return;
        }
    }
    // 校验快递单号是否空
    if ($('#isTrack').prop('checked') && !$('#trackingNo_editor').val()) {
        jp.bqWaring("快递单号不能为空", function () {$('#trackingNo_editor').focus();});
        return;
    }
    var allocIds = $.map(rows, function (row) { return row.allocId });
    var trackingNo = $('#trackingNo_editor').val();
    // var isPrintContainer = this.view.isPrintContainer.value as String; //是否打印装箱清单
    var isPrintContainer = "Y";
    var processByCode = null; // 发货纬度
    var objNos = []; // 发货条件
    // 选中出库单号
    if ($('#soRadio').prop('checked')) {
        processByCode = 'BY_SO'; // 按订单发货
        objNos.push($('#soNo_query').val());
    } else if ($('#boxRadio').prop('checked')) {
        processByCode = 'BY_TOID'; // 按目标跟踪号
        objNos.push($('#toId_query').val());
    }
    jp.loading();
    var params = { allocIds: allocIds, soSerialList: soSerialList, trackingNo: trackingNo, objNos: objNos, processByCode: processByCode, isPrintContainer: isPrintContainer, orgId: jp.getCurrentOrg().orgId };
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(params),
        url: "${ctx}/wms/outbound/banQinWmCheck/checkConfirm",
        success: function (data) {
            if (data.success) {
                // 清空列表
                $('#banQinWmCheckTable').bootstrapTable('removeAll');
                // 清空编辑区
                reset();
            }
            jp.alert(data.msg);
        }
    });
}

/**
 * 光标定位
 */
function setCurrentRowIndex(record) {
    var rowIndex = $("#banQinWmCheckTable").bootstrapTable("getIndexByField", { field: "id", values: [record.id]});
    if (rowIndex.length > 0) {
        $('#banQinWmCheckTable').bootstrapTable('updateRow', {index: rowIndex[0] - 1, row: record});
        jp.changeTableStyle($("#banQinWmCheckTable tr")[rowIndex[0]]);
    }
}

/**
 * 是否单件扫描，值处理
 */
function isSingleScan() {
    if ($('#isSingle').prop('checked')) {
        // 如果勾选，是单件扫描，那么包装单位为EA,数量为1,并且灰置不可编辑
        $('#uom_editor').val('EA').prop('disabled', true);
        $('#qty_editor').val(1).prop('readonly', true);
        $('#qtyEa_editor').val(1);
        // 光标定位商品
        $('#skuCode_editor').focus();
    } else {
        // 如果不勾选，不单件扫描，那么默认列表当前可扫描最大单位，数量不修改
        getCheckRecord($('#banQinWmCheckTable').bootstrapTable('getData'));
        // 可编辑
        $('#uom_editor').prop('disabled', false);
        $('#qty_editor').prop('readonly', false).val('');
        $('#qtyEa_editor').val('');
        // 光标定位商品
        $('#skuCode_editor').focus();
    }
}

/**
 * 是否扫描快递单号
 */
function isTrackChange() {
    if ($('#isTrack').prop('checked')) {
        // 如果勾选，扫描快递单号 光标定位
        $('#trackingNo_editor').prop('readonly', false).focus();
    } else {
        // 如果不勾选，不扫描快递单号
        $('#trackingNo_editor').prop('readonly', true);
    }
}

/**
 * 勾选记录，根据当前分配ID获取出库序列号
 */
function getSoSerial() {
    var rows = $.map($('#banQinWmCheckTable').bootstrapTable('getSelections'), function (row) { return row });
    if (rows.length === 0) {
        jp.bqWaring("请选择记录");
        return;
    }
    // 弹出界面
    $('#soSerialListModal').modal();
    // 勾选的分配ID
    var allocIds = $.map(rows, function (row) { return row.allocId });
    initSoSerialTable();

    // 列表结果集
    var resultList = [];
    // 循环缓存
    for (var i = 0; i < soSerialList.length; i++) {
        var item = soSerialList[i];
        // 分配ID存在于勾选的分配ID数组中
        if (allocIds.indexOf(item.allocId) !== -1) {
            resultList.push(item);
        }
    }
    // 获取已复核记录的序列号
    // 已复核的分配ID
    var allocChecks = [];
    if (rows.length > 0) {
        for (var j = 0; j < rows.length; j++) {
            if (rows[j].checkStatus === '99') {
                allocChecks.push(rows[j].allocId);
            }
        }
        if (allocChecks.length > 0) {
            $.ajax({
                headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
                type: 'POST',
                dataType: "json",
                data: JSON.stringify({allocIds: allocChecks, orgId: jp.getCurrentOrg().orgId}),
                url: "${ctx}/wms/outbound/banQinWmCheck/getSoSerialItemByAllocIds",
                success: function (data) {
                    if (data.success) {
                        var result = data.body.entity;
                        if (result.length > 0) {
                            for (var k = 0; k < result.length; k ++) {
                                resultList.push(result[j]);
                            }
                        }
                    }
                }
            });
        }
    }
    $('#soSerialTable').bootstrapTable('append', resultList);
}

function initSoSerialTable() {
    $('#soSerialTable').bootstrapTable('destroy').bootstrapTable({
        height: $(window).height() - 360,
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'serialNo',
            title: '序列号',
            sortable: false
        }, {
            field: 'ownerName',
            title: '货主',
            sortable: false
        }, {
            field: 'skuCode',
            title: '商品编码',
            sortable: false
        }, {
            field: 'skuName',
            title: '商品名称',
            sortable: false
        }, {
            field: 'lotNum',
            title: '批次号',
            sortable: false
        }, {
            field: 'allocId',
            title: '分配Id',
            sortable: false
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: false
        }, {
            field: 'lineNo',
            title: '出库单行号',
            sortable: false
        }]
    });
}

/**
 * 按钮使用控制
 */
function buttonControl() {
    if ($('#banQinWmCheckTable').bootstrapTable('getData').length > 0) {
        $('#checkConfirm').prop('disabled', false);
        $('#cancelCheck').prop('disabled', false);
        $('#batchCheck').prop('disabled', false);
    } else {
        // 列表没有记录，则按扭禁用
        $('#checkConfirm').prop('disabled', true);
        $('#cancelCheck').prop('disabled', true);
        $('#batchCheck').prop('disabled', true);
    }
}

function isContainStr(source, str) {
    if (!source) return false;
    return $.inArray(str, source.split(",")) >= 0;
}

</script>