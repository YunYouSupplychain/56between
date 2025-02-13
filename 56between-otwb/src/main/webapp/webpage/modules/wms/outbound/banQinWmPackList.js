<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var fmSkuRows = [];
var toSkuRows = [];
var changeSkuRows = [];
var soNo;
var ownerCode;
var lotNums = [];
var allocIds = [];
var soSerialModel = {};
var soSerialList = [];
var qtyEa = 0;
var isCloseLabel = false;
$(document).ready(function () {
    initFmTable();
    initToTable();
    // 初始化
    init();
    // 回车监听事件
    document.onkeyup = function(e) {
        var ev = document.all ? window.event : e;
        if (ev.keyCode === 13) {
            enterEvent(e);
        }
        var keyCode = ev.keyCode || ev.which || ev.charCode;
        var shiftKey = ev.shiftKey || ev.metaKey;
        if (shiftKey && keyCode === 38) {
            packConfirmHandler();
        }
    };
    // 鼠标监听事件
    document.onclick = function (ev) {
        // 光标定位商品扫描
        if ($(ev.target).prop('id') === 'qty_fmEditor') {
            mouseClickQty();
        }
    };
});

function initFmTable() {
    $('#fmTable').bootstrapTable({
        height: $(window).height() - 435,
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        uniqueId: "unqId",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.soNo = $('#soNo_query').val();
            searchParam.caseNo = $('#toId_query').val();
            // 如果是出库单号打包，那么查询时，过滤已打包记录
            searchParam.isPacked = $('#soRadio').prop('checked') ? 'N' : 'Y';
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onLoadSuccess: function(data) {
            queryCallBack(data);
        },
        onClickRow: function(row, $el) {
            jp.changeTargetTableStyle('#fmTable', $el);
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
            field: 'qtyPackUom',
            title: '打包数',
            sortable: false
        }, {
            field: 'qtyPackEa',
            title: '打包数EA',
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
            field: 'packOpName',
            title: '打包人',
            sortable: false
        }, {
            field: 'packTime',
            title: '打包时间',
            sortable: false
        }, {
            field: 'allocId',
            title: '分配Id',
            sortable: false
        }]
    });
}

function initToTable() {
    $('#toTable').bootstrapTable({
        height: $(window).height() - 435,
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        uniqueId: "unqId",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function(row, $el) {
            jp.changeTargetTableStyle('#toTable', $el);
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
            field: 'qtyPackUom',
            title: '打包数',
            sortable: false
        }, {
            field: 'qtyPackEa',
            title: '打包数EA',
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
        }]
    });
}

function init() {
    // 是否自动生成箱号
    isCheckCartonNo();
    // 默认单选控件选择出库单号，光标定位出库单号
    $('#soRadio').prop('checked', true);
    clickRadioButton();
    // 快递单号
    isTrackChange();
    // 重量
    isWeighChange();
    // 默认单件扫描
    $('#isSingle').prop('checked', true);
    isSingleScan();
    // 默认复核
    $('#isCheck').prop('checked', true);
    // 默认打印装箱标签
    $('#isPrintLabel').prop('checked', true);
    // 默认光标包材
    $('#packMaterial_toEditor').focus();
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
        // 光标定位目标箱号扫描
        case "toId_toEditor":
            scanToId();
            break;
        // 光标定位商品扫描
        case "skuCode_fmEditor":
            scanSku();
            break;
        // 光标定位序列号扫描
        case "serialNo_fmEditor":
            scanSerial();
            break;
        // 光标定位数量
        case "qty_fmEditor":
            enterQty();
            break;
        // 光标定位包材
        case "packMaterial_toEditor":
            clickRadioButton();
            break;
    }
}

/**
 * 查询
 */
function query() {
    if ($('#soRadio').prop('checked')) {
        if (!$('#soNo_query').val()) {
            bq.failVoice();
            jp.bqWaring("出库单号不能为空");
            return;
        }
    } else if ($('#boxRadio').prop('checked')) {
        if (!$('#toId_query').val()) {
            bq.failVoice();
            jp.bqWaring("原箱号不能为空");
            return;
        }
    }
    $('#fmTable').bootstrapTable("refresh", {'url': "${ctx}/wms/outbound/banQinWmPack/data"});
}

/**
 * 查询方法的回调函数
 */
function queryCallBack(msg) {
    if (msg.success) {
        var wmPackEntity= msg.body.entity;
        $("input[id$=_fmEditor]").each(function() { $(this).val(''); });
        $('#qty_fmEditor').val('');
        $('#serialNo_fmEditor').val('').prop('readonly', true);
        soSerialList = [];
        $('#qtySkuPickEa_fmEditor').val('');
        $('#qtySkuScanEa_fmEditor').val('');
        $('#packWeight_toEditor').val('');
        // 是否单件扫描控制
        isSingleScan();
        // 是否整箱扫描
        isSingleCsScan();
        // 刷新列表
        $('#fmTable').bootstrapTable('removeAll').bootstrapTable('load', wmPackEntity);
        $('#toTable').bootstrapTable('removeAll');
        // 写快递单号
        var trackingNo = '';
        if (wmPackEntity.rows.length > 0) {
            trackingNo = wmPackEntity.rows[0].soTrackingNo;
        }
        $('#trackingNo_toEditor').val(trackingNo);
        // 原箱号写统计
        setQtyCountByFm();
        // 目标箱号写统计
        setQtyCountByTo({}, 0, true);
        // 写包装单位
        getPackRecord($('#fmTable').bootstrapTable('getData'));
        $('#qtyEa_fmEditor').val('');
        // 如果目标箱号为空，那么光标定位目标箱号
        if (!$('#toId_toEditor').val()) {
            // 光标目标箱号
            $('#toId_toEditor').focus();
        } else {
            // 光标定位商品扫描
            $('#skuCode_fmEditor').focus();
        }
        // 功能按扭控制
        buttonControl();
        bq.successVoice();
    } else {
        bq.failVoice();
        jp.bqWaring(msg.msg);
    }
}

/**
 * 原箱号 写统计
 */
function setQtyCountByFm() {
    var rows = $('#fmTable').bootstrapTable('getData');
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
        grossWeight += (item.grossWeight ? parseFloat(item.grossWeight) : 0) * (item.grossWeight ? parseFloat(item.grossWeight) : 0);
        // 总体积
        cubic += (item.cubic ? parseFloat(item.cubic) : 0) * (item.qtyEa ? parseFloat(item.qtyEa) : 0);
        // 总已扫描数
        qtyAllScanEa += (item.qtyPackEa ? parseFloat(item.qtyPackEa) : 0);
        // 总未扫描数
        qtyAllNotScanEa += (item.qtyEa ? parseFloat(item.qtyEa) : 0) - (item.qtyPackEa ? parseFloat(item.qtyPackEa) : 0);
    }
    // 显示界面
    $('#qtySku_fmEditor').val(skuArr.length);
    $('#qtyAllEa_fmEditor').val(qtyAllEa);
    $('#grossWeight_fmEditor').val(grossWeight);
    $('#cubic_fmEditor').val(cubic);
    $('#qtyAllScanEa_fmEditor').val(qtyAllScanEa);
    $('#qtyAllNotScanEa_fmEditor').val(qtyAllNotScanEa);
}

/**
 * 目标箱号 写统计
 */
function setQtyCountByTo(record, qtyEa, flag) {
    var rows = $('#toTable').bootstrapTable('getData');
    // 商品数对象
    var skuArr = [];
    // 单品数
    var qtyAllEa = 0;
    // 总重量
    var grossWeight = 0;
    // 总体积
    var cubic = 0;
    // 如果目标列表写入记录，那么统计计算
    if (rows.length > 0) {
        rows.forEach(function (tmpReocrd) {
            // 商品数
            if (skuArr.indexOf(tmpReocrd.skuCode) === -1) {
                skuArr.push(tmpReocrd.skuCode);
                // 批量打包+
                if (!record && qtyEa === 0 && flag) {
                    // 单品数量
                    qtyAllEa += (tmpReocrd.qtyEa ? parseFloat(tmpReocrd.qtyEa) : 0);
                    // 总重量
                    grossWeight += (tmpReocrd.grossWeight ? parseFloat(tmpReocrd.grossWeight) : 0) * (tmpReocrd.qtyEa ? parseFloat(tmpReocrd.qtyEa) : 0);
                    // 总体积
                    cubic += (tmpReocrd.cubic ? parseFloat(tmpReocrd.cubic) : 0) * (tmpReocrd.qtyEa ? parseFloat(tmpReocrd.qtyEa) : 0);
                }
            }
        });
        // 如果打包，统计+
        if (record && qtyEa > 0 && flag) {
            // 单品数量
            qtyAllEa = ($('#qtyAllEa_toEditor').val() ? parseFloat($('#qtyAllEa_toEditor').val()) : 0) + qtyEa;
            // 总重量
            grossWeight = ($('#grossWeight_toEditor').val() ? parseFloat($('#grossWeight_toEditor').val()) : 0) + (record.grossWeight ? parseFloat(record.grossWeight) : 0) * qtyEa;
            // 总体积
            cubic = ($('#cubic_toEditor').val() ? parseFloat($('#cubic_toEditor').val()) : 0) + (record.cubic ? parseFloat(record.cubic) : 0) * qtyEa;
        } else if (record && qtyEa > 0 && !flag) {
            // 如果扣数，统计-
            // 单品数量
            qtyAllEa = ($('#qtyAllEa_toEditor').val() ? parseFloat($('#qtyAllEa_toEditor').val()) : 0) - qtyEa;
            // 总重量
            grossWeight = ($('#grossWeight_toEditor').val() ? parseFloat($('#grossWeight_toEditor').val()) : 0) - (record.grossWeight ? parseFloat(record.grossWeight) : 0) * qtyEa;
            // 总体积
            cubic = ($('#cubic_toEditor').val() ? parseFloat($('#cubic_toEditor').val()) : 0) - (record.cubic ? parseFloat(record.cubic) : 0) * qtyEa;
        }
    }
    // 显示界面
    $('#qtySku_toEditor').val(skuArr.length);
    $('#qtyAllEa_toEditor').val(qtyAllEa);
    $('#grossWeight_toEditor').val(grossWeight);
    $('#cubic_toEditor').val(cubic);
}

/**
 * 重置
 */
function reset() {
    // 清空
    $("input[id$=_query]").each(function() { $(this).val(''); });
    $("input[id$=_fmEditor]").each(function() { $(this).val(''); });
    $('#qty_fmEditor').val('');
    $('#serialNo_fmEditor').val('').prop('readonly', true);
    $('#qtySkuPickEa_fmEditor').val('');
    $('#qtySkuScanEa_fmEditor').val('');
    soSerialList = [];
    if (!$('#isAutoCartonNo').prop('checked')) {
        $("input[id$=_toEditor]").each(function() { $(this).val(''); });
    }
    $('#packWeight_toEditor').val('');
    $('#fmTable').bootstrapTable('removeAll');
    setQtyCountByFm();
    $('#toTable').bootstrapTable('removeAll');
    setQtyCountByTo(null, null, null);
    // 是否单件扫描控制
    isSingleScan();
    // 整箱扫描控制
    isSingleCsScan();
    // 单选控制控制
    clickRadioButton();
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
    $('#qty_fmEditor').focus();
}

/**
 * 扫描目标箱号后
 */
function scanToId() {
    var toId = $('#toId_toEditor').val();
    if (!toId) {
        bq.failVoice();
        jp.bqWaring("目标箱号不能为空");
        return;
    }
    // 扫描目标箱号，自动带出目标箱号的快递单号
    jp.post("${ctx}/wms/outbound/banQinWmPack/getTrackingNoByToId", {toId: toId, orgId: jp.getCurrentOrg().orgId},function (data) {
        $('#trackingNo_toEditor').val(data.body.result);
        // 光标重新定位商品扫描区
        $('#skuCode_fmEditor').focus();
    });
}

/**
 * 商品扫描后
 */
function scanSku() {
    var skuCode = $('#skuCode_fmEditor').val();
    if (!skuCode) {
        bq.failVoice();
        jp.bqWaring("商品不能为空");
        return;
    }
    // 获取当前商品所有记录(原箱号对象/目标箱号对象)
    if (!getSkuRows()) {
        return;
    }
    // 校验商品是否需要序列号扫描
    if (fmSkuRows.length > 0) {
        var tmpRecord = fmSkuRows[0];
        soNo = tmpRecord.soNo;
        ownerCode = tmpRecord.ownerCode;
        if (tmpRecord.isSerial === 'Y') {
            // 需要序列号扫描，光标定位序列号
            $('#serialNo_fmEditor').prop('readonly', false).focus();
            // 只能单件扫描,且不可编辑
            $('#isSingle').val('Y').prop('disabled', true).prop('checked', true);
            $('#isSingleCs').val('N').prop('disabled', true).prop('checked', false);
            // 如果勾选，是单件扫描，那么包装单位为EA,数量为1,并且灰置不可编辑
            $('#uom_fmEditor').val('EA').prop('disabled', true);
            $('#qty_fmEditor').val(1).prop('readonly', true);
            $('#qtyEa_fmEditor').val(1);
            // 写单位换算单位
            $('#uomQty_fmEditor').val(1);
            return;
        } else {
            $('#serialNo_fmEditor').prop('readonly', true);
            $('#isSingle').prop('disabled', false);
        }
    }
    // 是否单件扫描
    if ($('#isSingle').prop('checked')) {
        // 整箱扫描
        if ($('#isSingleCs').prop('checked')) {
            // 写单位换算单位
            $('#uom_fmEditor').val('CS');
        } else {
            // 写单位换算单位
            $('#uom_fmEditor').val('EA');
        }
        // 获取商品包装单位，及单位换算数量
        var packCode = $('#packCode_fmEditor').val();
        // 获取EA单位描述信息
        jp.post("${ctx}/wms/outbound/banQinWmPack/getPackageRelationAndQtyUom", {packCode: packCode, uom: $('#uom_fmEditor').val(), orgId: jp.getCurrentOrg().orgId}, function (data) {
            if (data.success) {
                var packageEntity = data.body.entity;
                $('#uomQty_fmEditor').val(packageEntity.cdprQuantity);
                $('#qtyEa_fmEditor').val(packageEntity.cdprQuantity);
                // 写包装换算数量
                $('#uomDesc_fmEditor').val(packageEntity.cdprDesc);
            }
            // 写打包数
            enterQty();
        });
    } else {
        if ($('#isSubtract').prop('checked')) {
            // 搜索商品、写包装
            if (!getPackRecord(toSkuRows)) {
                bq.failVoice();
                jp.bqWaring("不存在可以操作的商品[" + skuCode + "]记录");
                return;
            }
        } else {
            // 搜索商品、写包装
            if (!getPackRecord(fmSkuRows)) {
                bq.failVoice();
                jp.bqWaring("不存在可以操作的商品[" + skuCode + "]记录");
                return;
            }
        }
        $('#qty_fmEditor').val('').focus();
        $('#qtyEa_fmEditor').val('');
    }
}

/**
 * 获取当前扫描商品所有记录(原箱号对象/目标箱号对象)
 */
function getSkuRows() {
    fmSkuRows = [];
    toSkuRows = [];
    lotNums = [];
    allocIds= [];
    var tmpToRecord; // 目标箱号对象(用于收货人、承运人、路线 校验)
    var tmpFmRecord; // 原箱号对象(用于收货人、承运人、路线 校验)
    // 目标箱号列表
    var toRows = $('#toTable').bootstrapTable('getData');
    toRows.forEach(function (toRecord) {
        // 如果光标定位商品扫描，那么搜索该商品单位
        // if (toRecord.skuCode === $('#skuCode_fmEditor').val()) {
        if (isContainStr(toRecord.barcode, $('#skuCode_fmEditor').val())) {
            toSkuRows.push(toRecord);
            // 随机获取一条目标箱号缓存记录，校验收货人、承运人、路线是否一致
            tmpToRecord = toRecord;
        }
    });
    // 如果扣数
    if ($('#isSubtract').prop('checked')) {
        if (toSkuRows.length === 0) {
            bq.failVoice();
            jp.bqWaring("不存在可以操作的商品[" + $('#skuCode_fmEditor').val() + "]记录", function () {
                $('#skuCode_fmEditor').focus();
            });
            return false;
        }
    }

    // 原列表
    var fmRows = $('#fmTable').bootstrapTable('getData');
    for (var index in fmRows) {
        var fmRecord = fmRows[index];
        // 如果扣数
        if ($('#isSubtract').prop('checked')) {
            // 如果光标定位商品扫描，那么搜索该商品单位
            // if (fmRecord.skuCode === $('#skuCode_fmEditor').val()) {
            if (isContainStr(fmRecord.barcode, $('#skuCode_fmEditor').val())) {
                fmSkuRows.push(fmRecord);
                lotNums.push(fmRecord.lotNum);
                allocIds.push(fmRecord.allocId);
            }
        } else {
            if (fmRecord.qtyEa - fmRecord.qtyPackEa === 0) {
                // 可打包数必须大于0
                continue;
            }
            //如果光标定位商品扫描，那么搜索该商品单位
            // if (fmRecord.skuCode === $('#skuCode_fmEditor').val()) {
            if (isContainStr(fmRecord.barcode, $('#skuCode_fmEditor').val())) {
                if ($('#isSingleCs').prop('checked') && fmRecord.uom !== $('#uom_fmEditor').val()) {
                    // 如果整箱扫描，单位不为CS，继续搜索
                    continue;
                }
                // 校验收货人、承运人、路线 是否相同，不相同，则不可放入同一个包裹中
                if (!checkConsigneeAndCarrierAndLine(fmRecord, tmpToRecord)) {
                    tmpFmRecord = fmRecord;
                    continue;
                }
                fmSkuRows.push(fmRecord);
                lotNums.push(fmRecord.lotNum);
                allocIds.push(fmRecord.allocId);
            }
        }
    }
    if (fmSkuRows.length === 0 && tmpFmRecord) {
        bq.failVoice();
        jp.bqWaring("收货人、承运人、路线不同，不可操作");
        return false;
    }
    if (fmSkuRows.length === 0) {
        bq.failVoice();
        jp.bqWaring("不存在可以操作的商品[" + $('#skuCode_fmEditor').val() + "]记录", function () {
            $('#skuCode_fmEditor').focus().select();
        });
        return false;
    }
    return true;
}

/**
 * 输入序列号后操作
 */
function scanSerial() {
    // 商品
    var skuCode = $('#skuCode_fmEditor').val();
    if (!skuCode) {
        bq.failVoice();
        jp.warning("商品不能为空", function () {$('#skuCode_fmEditor').focus();});
        return;
    }
    // 序列号
    var serialNo = $('#serialNo_fmEditor').val();
    if (!serialNo) {
        bq.failVoice();
        jp.warning("序列号不能为空", function () {$('#serialNo_fmEditor').focus();});
        return;
    }
    // 缓存出库序列号
    var serialNos = $.map(soSerialList, function (row) { return row.serialNo; });
    // 扣数时，校验是否未扫描
    if ($('#isSubtract').prop('checked')) {
        if (serialNos.indexOf($('#serialNo_fmEditor').val()) === -1) {
            bq.failVoice();
            jp.warning("出库序列号[" + $('#serialNo_fmEditor').val() + "]未扫描，不能操作")
            return;
        }
        // 写复核
        enterQty();
    } else {
        // 校验序列号是否存在 校验序列号批次号与拣货记录的批次号是否一致 校验序列号是否唯一
        var serialIndex = jp.loading();
        var params = { soNo: soNo, ownerCode: ownerCode, skuCode: skuCode, serialNo: serialNo, lotNums: lotNums, allocIds: allocIds, orgId: jp.getCurrentOrg().orgId };
        $.ajax({
            headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
            type: 'POST',
            dataType: "json",
            data: JSON.stringify(params),
            url: "${ctx}/wms/outbound/banQinWmCheck/checkSoSerial",
            success: function (data) {
                if (data.success) {
                    soSerialModel = data.body.entity;
                    // 不扣数时，校验是否重复扫
                    if (!$('#isSubtract').prop('checked')) {
                        if (serialNos.indexOf(soSerialModel.serialNo) !== -1) {
                            bq.failVoice();
                            jp.bqWaring("序列号[" + soSerialModel.serialNo + "]已扫描");
                            return;
                        }
                    }
                    // 写复核
                    enterQty();
                    bq.successVoice();
                    jp.close(serialIndex);
                } else {bq.failVoice();
                    jp.bqWaring(data.msg, function () {$('#serialNo_fmEditor').focus().select()});
                }
            }
        });
    }
}

/**
 * 输入数量后操作
 */
function enterQty() {
    // 扫描EA数量(将输入框中的单位数量换算成EA数 计算)
    qtyEa = ($('#qty_fmEditor').val() ? parseFloat($('#qty_fmEditor').val()) : 0) * ($('#uomQty_fmEditor').val() ? parseFloat($('#uomQty_fmEditor').val()) : 0);
    // 商品扫描
    var skuCode = $('#skuCode_fmEditor').val();
    var toId = $('#toId_toEditor').val();
    if (!toId) {
        bq.failVoice();
        jp.bqWaring("目标箱号不能为空");
        // 光标定位目标箱号
        $('#toId_toEditor').focus();
        return;
    }
    if (!skuCode) {
        bq.failVoice();
        jp.bqWaring("商品不能为空");
        // 光标定位商品扫描
        $('#skuCode_fmEditor').focus();
        return;
    }
    if (qtyEa === 0) {
        bq.failVoice();
        jp.bqWaring("数量不能为空");
        // 光标定位数量
        $('#qty_fmEditor').focus();
        return;
    }
    // 写复核数
    if (setQtyPackUom()) {
        // 成功，光标定位商品
        $('#skuCode_fmEditor').focus().val('');
        // 清空序列号，并且灰置
        $('#serialNo_fmEditor').val('').prop('readonly', true);
        $('#isSingle').prop('disabled', false);
        // 判断是否单件扫描
        isSingleScan();
        // 能按扭控制
        buttonControl();
        bq.successVoice();
    }
}

/**
 * 写包装数
 */
function setQtyPackUom() {
    // 校验超量复核超量扣数
    if (!packOverOrSubtract()) {
        return false;
    }
    // 如果扣数
    if ($('#isSubtract').prop('checked')) {
        var toTableAllRows = $('#toTable').bootstrapTable('getData');
        // 写扫描包装相同的商品行记录
        subtractSkuUomEqual(toTableAllRows);
        // 写扫描包装不同的商品行记录
        subtractSkuUomDiff(toTableAllRows);
    } else {
        // 写扫描包装相同的商品行记录
        packSkuUomEqual();
        // 写扫描包装不同的商品行记录
        packSkuUomDiff();
    }
    return true;
}

/**
 * 校验超量打包超量扣数
 */
function packOverOrSubtract() {
    // 需要复核的数量
    var qtyPackEa = 0;
    // 可扣数的复核
    var qtySubtractEa = 0;
    // 超量打包校验
    if (!$('#isSubtract').prop('checked')) {
        fmSkuRows.forEach(function (record) {
            // 搜索当前扫描的商品
            // if (record.skuCode === $('#skuCode_fmEditor').val()) {
            if (isContainStr(record.barcode, $('#skuCode_fmEditor').val())) {
                // 统计可打包的数量
                qtyPackEa += (record.qtyEa - record.qtyPackEa);
            }
        });
        if (qtyPackEa < qtyEa) {
            bq.failVoice();
            jp.bqWaring("超量打包,不能操作");
            return false;
        }
    } else {
        // 超量扣数校验
        var toRows = $('#toTable').bootstrapTable('getData');
        // 目标列表
        toRows.forEach(function (toRecord) {
            // 搜索当前扫描的商品
            // if (toRecord.skuCode === $('#skuCode_fmEditor').val()) {
            if (isContainStr(toRecord.barcode, $('#skuCode_fmEditor').val())) {
                // 统计可打包的数量
                qtySubtractEa += toRecord.qtyPackEa;
            }
        });
        if (qtySubtractEa < qtyEa) {
            bq.failVoice();
            jp.bqWaring("超量扣数,不能操作");
            return false;
        }
    }
    return true;
}

/**
 * 写复核数+，写缓存出库序列号
 */
function addSerialItemAndQtyPack(fmRecord, toRecord) {
    // 写缓存序列号
    var soSerialItem = {};
    bq.copyProperties(soSerialModel, soSerialItem);
    bq.copyProperties(fmRecord, soSerialItem);
    soSerialItem.serialNo = $('#serialNo_fmEditor').val();
    if (soSerialModel.id) {
        soSerialItem.id = soSerialModel.id;
    } else {
        soSerialItem.id = null;
    }
    soSerialList.push(soSerialItem);
    // 写复核数
    packQtyPackEa(fmRecord, toRecord);
}

/**
 * 打包时，校验收货人、承运人、路线是否一致
 */
function checkConsigneeAndCarrierAndLine(fmRecord, toRecord) {
    if (fmRecord && toRecord) {
        // 收货人同名同址(收货人名称、电话、地址+联系人名称、电话、地址)才可以打包到同一个包裹里面
        if (fmRecord.consigneeName !== toRecord.consigneeName || fmRecord.consigneeTel !== toRecord.consigneeTel || fmRecord.consigneeAddr !== toRecord.consigneeAddr || fmRecord.contactName !== toRecord.contactName || fmRecord.contactTel !== toRecord.contactTel || fmRecord.contactAddr !== toRecord.contactAddr) {
            return false;
        }
        if (fmRecord.carrierCode !== toRecord.carrierCode) {
            return false;
        }
        if (fmRecord.line !== toRecord.line) {
            return false;
        }
    }
    return true;
}

/**
 * 打包复核+ 获取与扫描包装相同的商品行记录
 * skuRows 全局变量，当前扫描商品的所有记录集
 * qtyEa:全局变量，当前输入的数量EA(由界面单位数量换算)
 */
function packSkuUomEqual() {
    // 目标箱号列表
    var toRecord = null;
    // 收货人、承运人、路线校验对象
    var checkFmRecord = null;
    // 原列表
    for (var i = 0; i < fmSkuRows.length; i++) {
        var fmRecord = fmSkuRows[i];
        if (qtyEa <= 0) {
            // 写复核数完成,退出
            return;
        }
        // 目标列表
        var toRows = $('#toTable').bootstrapTable('getData');
        // 目标列表数据写入
        for (var record in toRows) {
            // 如果可打包行商品 = 目标箱号列表商品，那么该行记录商品打包数量累加
            // if ($('#skuCode_fmEditor').val() === toRows[record].skuCode && $('#uom_fmEditor').val() === toRows[record].uom) {
            if (isContainStr(toRows[record].barcode, $('#skuCode_fmEditor').val()) && $('#uom_fmEditor').val() === toRows[record].uom) {
                toRecord = toRows[record];
                break;
            }
        }
        // 搜索包装单位与扫描包装相同,可打包复核(包装数>打包数)
        if (fmRecord.uom === $('#uom_fmEditor').val() && fmRecord.qtyEa > fmRecord.qtyPackEa) {
            // 打包时，校验收货人、承运人、路线是否一致
            if (!checkConsigneeAndCarrierAndLine(fmRecord, toRecord)) {
                // 不一致，继续循环下一行
                continue;
            }
            // 如果扫描序列号,
            if (fmRecord.isSerial === 'Y') {
                // 批次号一致
                if (!soSerialModel.allocId && fmRecord.lotNum === soSerialModel.lotNum) {
                    // 写缓存序列号，并且写复核数
                    addSerialItemAndQtyPack(fmRecord, toRecord);
                } else if (soSerialModel.allocId && fmRecord.allocId === soSerialModel.allocId) {
                    // 如果分配ID不为空，那么分配ID相同
                    // 写缓存序列号，并且写复核数
                    addSerialItemAndQtyPack(fmRecord, toRecord);
                }
            } else {
                // 写打包复核数
                packQtyPackEa(fmRecord, toRecord);
            }
        }
    }
}

/**
 * 打包复核+ 获取与扫描包装不相同的商品行记录
 * skuRows 全局变量，当前扫描商品的所有记录集
 * qtyEa:全局变量，当前输入的数量EA(由界面单位数量换算)
 */
function packSkuUomDiff() {
    // 目标箱号列表
    var toRecord = null;
    // 原列表
    for (var i in fmSkuRows) {
        var fmRecord = fmSkuRows[i];
        if (qtyEa <= 0) {
            // 写复核数完成,退出
            return;
        }
        // 目标列表
        var toRows = $('#toTable').bootstrapTable('getData');
        // 目标列表数据写入
        for (var record in toRows) {
            // 如果可打包行商品 = 目标箱号列表商品，那么该行记录商品打包数量累加
            // if ($('#skuCode_fmEditor').val() === toRows[record].skuCode && $('#skuCode_fmEditor').val() === toRows[record].uom) {
            if (isContainStr(toRows[record].barcode, $('#skuCode_fmEditor').val()) && $('#uom_fmEditor').val() === toRows[record].uom) {
                toRecord = toRows[record];
                break;
            }
        }
        // 搜索包装单位与扫描包装不相同,可打包复核(包装数>打包数)
        if (fmRecord.qtyEa > fmRecord.qtyPackEa) {
            // 打包时，校验收货人、承运人、路线是否一致
            if (!checkConsigneeAndCarrierAndLine(fmRecord, toRecord)) {
                // 不一致，继续循环下一行
                continue;
            }
            // 如果扫描序列号,
            if (fmRecord.isSerial === 'Y') {
                // 批次号一致
                if (!soSerialModel.allocId && fmRecord.lotNum === soSerialModel.lotNum) {
                    // 写缓存序列号，并且写复核数
                    addSerialItemAndQtyPack(fmRecord, toRecord);
                } else if (soSerialModel.allocId && fmRecord.allocId === soSerialModel.allocId) {
                    // 如果分配ID不为空，那么分配ID相同
                    // 写缓存序列号，并且写复核数
                    addSerialItemAndQtyPack(fmRecord, toRecord);
                }
            } else {
                //写打包复核数
                packQtyPackEa(fmRecord, toRecord);
            }
        }
    }
}

/**
 * 打包复核+ 写包装复核数
 */
function packQtyPackEa(fmRecord, toRecord) {
    // 目标列表空记录标记,如果空记录，并且整箱扫描，那么直接关箱
    var flagNull = false;
    // 原列表需要打包数量 = 包装数-已打包数
    var qtyPackEa = (fmRecord.qtyEa ? parseFloat(fmRecord.qtyEa) : 0) - (fmRecord.qtyPackEa ? parseFloat(fmRecord.qtyPackEa) : 0);
    // 操作数
    var qtyOpEa = 0;
    // 如果可打包行商品 = 目标箱号列表商品，那么该行记录商品打包数量累加
    if (!toRecord) {
        flagNull = true;
        // 写目标列表
        toRecord = {};
        bq.copyProperties(fmRecord, toRecord);
        // 打包数 == 0
        toRecord.qtyUom = 0;
        toRecord.qtyEa = 0;
        toRecord.qtyPackEa = 0;
        toRecord.qtyPackUom = 0;
        toRecord.qtyCheckEa = 0;
        toRecord.qtyCheckUom = 0;
        toRecord.uom = $('#uom_fmEditor').val();
        toRecord.uomDesc = $('#uomDesc_fmEditor').val();
        toRecord.unqId = guid();
        //写入目标列表
        $('#toTable').bootstrapTable('append', toRecord);
    }
    // 如果总打包数量>=当前行打包数
    if (qtyEa >= qtyPackEa) {
        qtyOpEa = qtyPackEa;
    } else {
        qtyOpEa = qtyEa;
    }
    // 原列表写打包数
    fmRecord.qtyPackEa += qtyOpEa;
    // 目标列表记录写打包数
    toRecord.qtyPackEa += qtyOpEa;
    // 目标箱号列表写统计
    setQtyCountByTo(toRecord, qtyOpEa, true);
    // 如果列表单位换算数量>扫描单位的换算数量，那么扫描时，由小单位(目标),大单位(原)
    // 如果列表单位换算数量<扫描单位的换算数量，那么扫描时，由大单位(目标),小单位(原)
    // 如果原列表记录可打包数量>当前需打包数量
    if (fmRecord.qtyEa > fmRecord.qtyPackEa) {
        getNewPackageUom(fmRecord);
    }
    // 总操作剩余数
    qtyEa -= qtyOpEa;
    // 原列表计算打包数(单位换算)
    fmRecord.qtyPackUom = (fmRecord.qtyPackEa ? parseFloat(fmRecord.qtyPackEa) : 0) / (fmRecord.uomQty ? parseFloat(fmRecord.uomQty) : 1);
    // 原箱号列表光标定位
    setCurrentRowIndexById('fmTable', fmRecord);
    // 原箱列表统计
    setQtyCountByFm();
    // 记录扫描商品的扫描数
    $('#qtySkuPickEa_fmEditor').val(fmRecord.qtyEa);
    $('#qtySkuScanEa_fmEditor').val(fmRecord.qtyPackEa);
    // 目标列表计算打包数(单位换算)
    toRecord.qtyPackUom = (toRecord.qtyPackEa ? parseFloat(toRecord.qtyPackEa) : 0) / ($('#uomQty_fmEditor').val() ? parseFloat($('#uomQty_fmEditor').val()) : 1);
    // 需要复核，复核数==打包数
    if ($('#isCheck').prop('checked')) {
        toRecord.qtyCheckEa = toRecord.qtyPackEa;
        toRecord.qtyCheckUom = toRecord.qtyPackUom;
    } else if (fmRecord.checkStatus === '99') {
        // 如果原记录已经复核，那么复核数==打包数
        toRecord.qtyCheckEa += qtyOpEa;
        toRecord.qtyCheckUom = (toRecord.qtyCheckEa ? parseFloat(toRecord.qtyCheckEa) : 0) / ($('#uomQty_fmEditor').val() ? parseFloat($('#uomQty_fmEditor').val()) : 1);
    }
    // 目标列表光标定位
    setCurrentRowIndex('toTable', toRecord);
    // 整箱扫描后直接关箱
    if (flagNull && $('#isSingleCs').prop('checked') && toRecord) {
        packConfirmHandler();
    }
}

/**
 * 写复核数-扣数，删除出库序列号
 */
function removeSerialItemAndQtyPack(fmRecord, toRecord) {
    for (var i = 0; i < soSerialList.length; i++) {
        var soSerialItem = soSerialList[i];
        // 记录的分配ID与序列号的分配ID相同，定位记录
        if (fmRecord.allocId === soSerialItem.allocId) {
            // 如果记录存在，那么删除缓存中的出库序列号
            if (soSerialItem.serialNo === $('#serialNo_fmEditor').val()) {
                soSerialList.splice(i, 1);
                i--;
                // 写复核数
                subtractQtyPackEa(fmRecord, toRecord);
                return true;
            }
        }
    }
    return false;
}

/**
 * 打包复核扣数- 获取与扫描包装相同的商品行记录
 * skuRows 全局变量，当前扫描商品的所有记录集
 * qtyEa:全局变量，当前输入的数量EA(由界面单位数量换算)
 * arr: 目标列表记录集
 */
function subtractSkuUomEqual(arr) {
    // 目标箱号列表
    var toRecord;
    // 目标列表
    for (var i in arr) {
        var record = arr[i];
        // 如果可打包行商品 = 目标箱号列表商品，那么该行记录商品打包数量累加 包装单位相同
        if (isContainStr(record.barcode, $('#skuCode_fmEditor').val()) && $('#uom_fmEditor').val() === record.uom) {
            toRecord = record;
        }
        if (!toRecord) {
            continue;
        }
        // 原列表，包装相同
        for (var j in fmSkuRows) {
            var fmRecordEqual = fmSkuRows[j];
            if (qtyEa <= 0) {
                // 写复核数完成,退出
                return;
            }
            // 搜索包装单位与扫描包装相同,可扣减打包数(打包数)
            if (fmRecordEqual.uom === $('#uom_fmEditor').val() && fmRecordEqual.qtyPackEa > 0) {
                // 如果扫描序列号,
                if (fmRecordEqual.isSerial === 'Y') {
                    if (!removeSerialItemAndQtyPack(fmRecordEqual, toRecord)) {
                        continue;
                    }
                } else {
                    // 扣数
                    subtractQtyPackEa(fmRecordEqual, toRecord);
                }
            }
        }
        // 原列表，包装不同
        for (var k in fmSkuRows) {
            var fmRecordDiff = fmSkuRows[k];
            if (qtyEa <= 0) {
                // 写复核数完成,退出
                return;
            }
            // 搜索包装单位与扫描包装相同,可扣减打包数(打包数)
            if (fmRecordDiff.qtyPackEa > 0) {
                // 如果扫描序列号,
                if (fmRecordDiff.isSerial === 'Y') {
                    if (!removeSerialItemAndQtyPack(fmRecordDiff, toRecord)) {
                        continue;
                    }

                } else {
                    // 扣数
                    subtractQtyPackEa(fmRecordDiff, toRecord);
                }
            }
        }
    }
}

/**
 * 打包复核扣数- 获取与扫描包装不相同的商品行记录
 */
function subtractSkuUomDiff(arr) {
    // 目标箱号列表
    var toRecord = null;
    // 目标列表
    for (var i in arr) {
        var record = arr[i];
        // 如果可打包行商品 = 目标箱号列表商品，那么该行记录商品打包数量累加
        // if ($('#skuCode_fmEditor').val() === record.skuCode) {
        if (isContainStr(record.barcode, $('#skuCode_fmEditor').val())) {
            toRecord = record;
        }
        // 原列表
        for (var j in fmSkuRows) {
            var fmRecord = fmSkuRows[j];
            if (qtyEa <= 0) {
                // 写复核数完成,退出
                return;
            }
            // 包装不限,可扣减打包数(打包数)
            if (fmRecord.qtyPackEa > 0) {
                // 如果扫描序列号,
                if (fmRecord.isSerial === 'Y') {
                    if (!removeSerialItemAndQtyPack(fmRecord, toRecord)) {
                        continue;
                    }
                } else {
                    // 扣数
                    subtractQtyPackEa(fmRecord, toRecord);
                }
            }
        }
    }
}

/**
 * 打包复核扣数- 扣减包装复核数
 */
function subtractQtyPackEa(fmRecord, toRecord) {
    // 目标列表需要打包数量 = 已打包数
    var qtyPackEa = (fmRecord.qtyPackEa ? parseFloat(fmRecord.qtyPackEa) : 0);
    // 可扣减数必须要 > 0
    if (qtyPackEa === 0) {
        return;
    }
    // 操作数
    var qtyOpEa = 0;
    // 如果可扣减打包数量 >= 当前行打包数
    if (qtyEa >= qtyPackEa) {
        qtyOpEa = qtyPackEa;
    } else {
        qtyOpEa = qtyEa;
    }
    // 目标列表记录写打包数
    toRecord.qtyPackEa -= qtyOpEa;
    // 原列表记录写打包数
    fmRecord.qtyPackEa -= qtyOpEa;
    // 如果列表单位换算数量>扫描单位的换算数量，那么扫描时，由小单位(目标),大单位(原)
    // 如果列表单位换算数量<扫描单位的换算数量，那么扫描时，由大单位(目标),小单位(原)
    // 如果原列表记录可打包数量>当前需打包数量
    if (fmRecord.qtyEa > fmRecord.qtyPackEa) {
        getNewPackageUom(fmRecord);
    }
    // 数量输入框输入值变量
    qtyEa -= qtyOpEa;
    // 目标箱号列表写统计
    setQtyCountByTo(toRecord, qtyOpEa, false);
    // 原列表计算打包数(单位换算)
    fmRecord.qtyPackUom = (fmRecord.qtyPackEa ? parseFloat(fmRecord.qtyPackEa) : 0) / (fmRecord.uomQty ? parseFloat(fmRecord.uomQty) : 1);
    // 原箱号列表光标定位
    setCurrentRowIndexById('fmTable', fmRecord);
    // 原箱号列表写统计
    setQtyCountByFm();
    // 记录扫描商品的扫描数
    $('#qtySkuPickEa_fmEditor').val(fmRecord.qtyEa);
    $('#qtySkuScanEa_fmEditor').val(fmRecord.qtyPackEa);
    // 如果目标列表记录打包数=0，那么删除行
    if (toRecord.qtyPackEa === 0) {
        $('#toTable').bootstrapTable('removeByUniqueId', toRecord.unqId);
    } else {
        // 目标列表计算打包数(单位换算)
        toRecord.qtyPackUom = (toRecord.qtyPackEa ? parseFloat(toRecord.qtyPackEa) : 0) / ($('#uomQty_fmEditor').val() ? parseFloat($('#uomQty_fmEditor').val()) : 1);
        // 需要复核，复核数==打包数
        if ($('#isCheck').prop('checked')) {
            toRecord.qtyCheckEa = toRecord.qtyPackEa;
            toRecord.qtyCheckUom = toRecord.qtyPackUom;
        } else if (fmRecord.checkStatus === '99') {
            // 如果原记录已复核，那么复核数-
            toRecord.qtyCheckEa -= qtyOpEa;
            toRecord.qtyCheckUom = (toRecord.qtyCheckEa ? parseFloat(toRecord.qtyCheckEa) : 0) / ($('#uomQty_fmEditor').val() ? parseFloat($('#uomQty_fmEditor')).val() : 1);
        }
        // 目标列表光标定位
        setCurrentRowIndex('toTable', toRecord);
    }
}

/**
 * 获取下一级包装单位
 */
function getNewPackageUom(record) {
    $.ajax({
        type: 'POST',
        async: false,
        data: {packCode: record.packCode, uomQty: record.uomQty, qtyEa: record.qtyEa - record.qtyPackEa, orgId: jp.getCurrentOrg().orgId},
        url: "${ctx}/wms/outbound/banQinWmPack/getCdWhPackageRelation",
        success: function (data) {
            if (data.success) {
                // 写目标单位，换算数量
                record.toUom = data.body.entity.cdprUnitLevel;
                record.toUomDesc = data.body.entity.cdprDesc;
                record.toUomQty = data.body.entity.cdprQuantity;
            } else {
                bq.failVoice();
                jp.bqWaring(data.msg);
            }
        }
    });
}

/**
 * 初始赋值 - 定位行，并且写包装单位、换算数量等
 * 如果有记录，那么定位，如果没有记录，根据具体情况提示
 */
function getPackRecord(arr) {
    // 列表对象
    var flag = false;
    for (var i in arr) {
        var record = arr[i];
        // 如果是扣数，那么未完全复核的记录并且可扣数的记录
        if ($('#isSubtract').prop('checked') && record.qtyPackEa > 0) {
            // 写包装单位
            setPackUom(record);
            // 查找到记录
            flag = true;
            return true;
        }
        // 不扣数，搜索未完全复核的记录,并且可打包的记录(包装数!= 复核数)
        else if (!$('#isSubtract').prop('checked') && record.qtyEa > record.qtyPackEa) {
            // 写包装单位
            setPackUom(record);
            // 查找到记录
            flag = true;
            return true;
        }
    }
    return flag;
}

/**
 * 定位行，并且写包装单位、换算数量等
 */
function setPackUom(record) {
    // 如果不是单件扫描，写包装(单件扫描，默认EA)
    if (!$('#isSingle').prop('checked')) {
        // 包装单位设置
        $('#uom_fmEditor').val(record.uom);
        $('#uomDesc_fmEditor').val(record.uomDesc);
        // 单位换算数量
        $('#uomQty_fmEditor').val(record.uomQty ? parseFloat(record.uomQty) : 1);
        $('#qtyEa_fmEditor').val(record.uomQty ? parseFloat(record.uomQty) : 1);
    }
    // 写包装代码
    $('#packCode_fmEditor').val(record.packCode);
    // 如果扣数，光标定位目标列表
    if ($('#isSubtract').prop('checked')) {
        // 光标设置
        setCurrentRowIndex('toTable', record);
    } else {
        // 光标定位原列表
        setCurrentRowIndexById('fmTable', record);
    }
}

/**
 * 包装单位修改
 */
function changeUom() {
    // 如果商品扫描为空，那么修改后定位商品扫描
    if (!$('#skuCode_fmEditor').val()) {
        $('#skuCode_fmEditor').focus();
        return;
    }
    // 获取商品包装单位，及单位换算数量
    jp.post("${ctx}/wms/outbound/banQinWmPack/getPackageRelationAndQtyUom",  { packCode: $('#packCode_fmEditor').val(), uom: $('#uom_fmEditor').val(), orgId: jp.getCurrentOrg().orgId }, function (data) {
        if (data.success) {
            var packageEntity = data.body.entity;
            // 写包装换算数量
            $('#uomQty_fmEditor').val(packageEntity.cdprQuantity);
            $('#uomDesc_fmEditor').val(packageEntity.cdprDesc);
            $('#qtyEa_fmEditor').val(packageEntity.cdprQuantity);
            // 光标定位数量
            $('#qty_fmEditor').focus();
        } else {
            // 重置包装
            //( 不提示，可以重置就重置，不能重置就不修改包装单位)
            if ($('#isSubtract').prop('checked')) {
                getPackRecord($('#toTable').bootstrapTable('getData'));
            } else {
                getPackRecord(fmSkuRows);
            }
            jp.bqWaring(data.msg);
        }
    });
}

/**
 * 数量修改，换算
 */
function changeQty() {
    if ($('#qty_fmEditor').val()) {
        $('qtyEa_fmEditor').val(parseFloat($('#qty_fmEditor').val()) * parseFloat($('#uomQty_fmEditor').val()));
    }
}

/**
 * 单选控件控制
 */
function clickRadioButton() {
    // 选中出库单号
    if ($('#soRadio').prop('checked')) {
        // 光标定位出库单号输入框
        $('#soNo_query').focus().prop('readonly', false).select();
        // 清除箱号非空标记
        $('#toId_query').val('').prop('readonly', true);
    } else if ($('#boxRadio').prop('checked')) {
        // 光标定位箱号号输入框
        $('#toId_query').focus().prop('readonly', false).select();
        // 清除出库单号非空标记
        $('#soNo_query').val('').prop('readonly', true);
    }
}

/**
 * 批量打包 将左侧原箱号列表记录全部移至右侧 移动前必须判断是否有箱号
 */
function batchPackHandler() {
    if (!$('#toId_toEditor').val()) {
        bq.failVoice();
        jp.bqWaring("目标箱号不能为空");
        return;
    }
    var rows = $('#fmTable').bootstrapTable('getData');
    if (rows.length === 0) {
        return;
    }
    allocIds = [];
    var tmpToRecord = null;
    var toRows = [];
    // 原列表
    for (var i in rows) {
        var fmRecord = rows[i];
        // 如果包装数=打包数，那么搜索下一条记录
        if (fmRecord.qtyEa === fmRecord.qtyPackEa) {
            continue;
        }
        // 如果商品有序列号管理，并且未复核、未打包，那么必须扫描打包
        if (fmRecord.isSerial === 'Y' && fmRecord.checkStatus === '00' && !fmRecord.packOp && !fmRecord.packTime) {
            bq.failVoice();
            jp.bqWaring("存在需要序列号管理的商品，请扫描序列号打包");
            return;
        }
        // 打包数 = 包装数
        fmRecord.qtyPackUom = fmRecord.qtyUom;
        fmRecord.qtyPackEa= fmRecord.qtyEa;
        // 目标单位
        fmRecord.toUom = fmRecord.uom;
        fmRecord.toUomDesc = fmRecord.uomDesc;
        fmRecord.toUomQty = fmRecord.uomQty;
        // 写目标列表
        var toRecord = {};
        bq.copyProperties(fmRecord, toRecord);
        if ($('#isCheck').prop('checked')) {
            toRecord.qtyCheckUom = toRecord.qtyPackUom;
            toRecord.qtyCheckEa= toRecord.qtyPackEa;
        }
        if (tmpToRecord) {
            if (!checkConsigneeAndCarrierAndLine(fmRecord, tmpToRecord)) {
                bq.failVoice();
                jp.bqWaring("收货人、承运人、路线不同，不能操作");
                return;
            }
        }
        tmpToRecord = fmRecord;
        // 写入目标列表
        toRows.push(toRecord);
        allocIds.push(fmRecord.allocId);
    }
    // 写入目标列表
    $('#toTable').bootstrapTable('append', toRows);
    // 写目标统计
    setQtyCountByTo(null, null, true);
    // 写缓存序列号
    if (allocIds.length > 0) {
        $.ajax({
            headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
            type: 'POST',
            dataType: "json",
            data: JSON.stringify({allocIds: allocIds, orgId: jp.getCurrentOrg().orgId}),
            url: "${ctx}/wms/outbound/banQinWmCheck/getSoSerialItemByAllocIds",
            success: function (data) {
                bq.successVoice();
                soSerialList = data.body.entity;
            }
        });
    }
    // 功能按扭控制
    buttonControl();
}

/**
 * 更新箱号(执行换箱)
 */
function packConfirmHandler() {
    if (isCloseLabel) {
        jp.bqWaring("正在操作中，请稍后操作...");
        return;
    }
    // 校验快递单号是否空
    var trackingNo = $('#trackingNo_toEditor').val();
    if ($('#isTrack').prop('checked') && !trackingNo) {
        bq.failVoice();
        jp.bqWaring("快递单号不能为空");
        // 光标快递单号
        $('#trackingNo_toEditor').focus();
        return;
    }
    var packWeight = $('#packWeight_toEditor').val();
    if ($('#isWeigh').prop('checked') && !('#packWeight_toEditor').val()) {
        // 重量不能为空
        bq.failVoice();
        jp.bqWaring("重量不能为空");
        // 重量
        $('#packWeight_toEditor').focus();
        return;
    }
    var fmRows = $('#fmTable').bootstrapTable('getData');
    changeSkuRows = [];
    var rows = [];
    var isPackAll = true;
    // 循环原箱号列表记录
    for (var record in fmRows) {
        if (fmRows[record].qtyPackEa > 0) {
            if (fmRows[record].toUom) {
                // 写目标包装单位
                fmRows[record].uom = fmRows[record].toUom;
                fmRows[record].uomDesc = fmRows[record].toUomDesc;
                fmRows[record].uomQty = fmRows[record].toUomQty;
            }
            fmRows[record].packWeight = packWeight;
            changeSkuRows.push(fmRows[record]);
            rows.push(fmRows[record]);
        }
        if (fmRows[record].qtyPackEa !== fmRows[record].qtyEa) {
            isPackAll = false;
        }
    }
    if (rows.length === 0) {
        return;
    }
    if (!isPackAll) {
        bq.failVoice();
        jp.confirm("还有未打包的商品，是否确认要关箱?", function () {
            packConfirm(rows, soSerialList, trackingNo);
        }, function () {
            $('#skuCode_fmEditor').focus();
        });
    } else {
        packConfirm(rows, soSerialList, trackingNo);
    }
}

function packConfirm(rows, soSerialList, trackingNo) {
    var toId = $('#toId_toEditor').val() + $("#packMaterial_toEditor").val();
    var isCheck = $('#isCheck').prop('checked') ? 'Y' : 'N';
    var isPrintContainer = $('#isPrintContainer').prop('checked') ? 'Y' : 'N';
    var isPrintLabel = $('#isPrintLabel').prop('checked') ? 'Y' : 'N';
    if (!$('#isTrack').prop('checked')) {
        trackingNo = null;
    }
    jp.loading();
    isCloseLabel = true;
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'POST',
        dataType: "json",
        data: JSON.stringify({allocItems: rows, toId: toId, soSerialList: soSerialList, isCheck: isCheck, isPrintContainer: isPrintContainer, isPrintLabel: isPrintLabel, orgId: jp.getCurrentOrg().orgId}),
        url: "${ctx}/wms/outbound/banQinWmPack/updateTraceId",
        success: function (data) {
            // 是否自动生成箱号
            if ($('#isAutoCartonNo').prop('checked')) {
                autoCartonNo();
            } else {
                $('#toId_toEditor').val('').prop('readonly', false);
            }
            if (data.success) {
                // 清空目标箱号列表
                $('#toTable').bootstrapTable('removeAll');
                // 修改原箱号列表记录
                for (var record in changeSkuRows) {
                    if (changeSkuRows[record].toUom) {
                        // 修改单位，数量
                        changeSkuRows[record].uom = changeSkuRows[record].toUom;
                        changeSkuRows[record].uomQty = changeSkuRows[record].toUomQty;
                        changeSkuRows[record].uomDesc = changeSkuRows[record].toUomDesc;
                    }
                    changeSkuRows[record].qtyEa = changeSkuRows[record].qtyEa - changeSkuRows[record].qtyPackEa;
                    changeSkuRows[record].qtyUom = changeSkuRows[record].qtyEa / changeSkuRows[record].uomQty;
                    changeSkuRows[record].qtyPackEa = 0;
                    changeSkuRows[record].qtyPackUom = 0;
                    changeSkuRows[record].toUom = null;
                    changeSkuRows[record].toUomQty = null;
                    changeSkuRows[record].toUomDesc = null;
                    if (changeSkuRows[record].qtyEa === 0) {
                        // 删除记录
                        $('#fmTable').bootstrapTable('remove', {field: 'id', values: [changeSkuRows[record].id]});
                    }
                }
                // 快递单号清空
                $('#trackingNo_toEditor').val('');
                $('#qtySkuPickEa_fmEditor').val();
                $('#qtySkuScanEa_fmEditor').val();
                $('#packWeight_toEditor').val('');
                $('#qtyEa_fmEditor').val('');
                $('#packMaterial_toEditor').val('');
                // 清空出库序列号缓存
                soSerialList = [];
                // 重新统计
                setQtyCountByFm();
                // 后台打印
                bq.printWayBill(data.body.imageList);
                jp.bqWaring(data.msg, function () {$('#packMaterial_toEditor').focus();});
                bq.successVoice();
            } else {
                bq.failVoice();
                jp.bqWaring(data.msg);
            }
            isCloseLabel = false;
            // 功能按扭控制
            buttonControl();
        }
    });
}

/**
 * 取消打包
 * 取消右侧列表未关箱的记录 对勾选记录行取消装箱，删除记录，左侧列表的原记录复核数量扣减
 */
function cancelPackHandler() {
    var selectRows = $.map($('#toTable').bootstrapTable('getSelections'), function (row) { return row });
    if (selectRows.length === 0) {
        bq.failVoice();
        jp.bqWaring("请选择记录");
        return;
    }
    // 清除目标勾选取消记录, 清除原箱号的包装数
    for (var selectRecord in selectRows) {
        qtyEa = selectRows[selectRecord].qtyPackEa;
        // 清除目标勾选取消记录
        $('#toTable').bootstrapTable('removeByUniqueId', selectRows[selectRecord].unqId);
        // remove后再getSelection获取的数据异常
        $("#toTable").bootstrapTable('load',$("#toTable").bootstrapTable('getData'));
        // 扣减原箱号的包装数
        var fmRows = $('#fmTable').bootstrapTable('getData');
        // 原列表，包装相同
        // for(var fmRecordEqual in fmRows) {
        //     if (qtyEa <= 0) {
        //         // 写复核数完成,退出
        //         break;
        //     }
        //     // 搜索包装单位与扫描包装相同,可扣减打包数(打包数)
        //     if (fmRows[fmRecordEqual].uom === selectRows[selectRecord].uom && fmRows[fmRecordEqual].qtyPackEa > 0 && fmRows[fmRecordEqual].skuCode === selectRows[selectRecord].skuCode) {
        //         // 扣数
        //         subtractQtyPackEa(fmRows[fmRecordEqual], selectRows[selectRecord]);
        //     }
        // }
        // 原列表，包装不同
        for (var fmRecordDiff in fmRows) {
            if (qtyEa <= 0) {
                // 写复核数完成,退出
                break;
            }
            // 搜索包装单位与扫描包装相同,可扣减打包数(打包数)
            if (fmRows[fmRecordDiff].qtyPackEa > 0 && fmRows[fmRecordDiff].skuCode === selectRows[selectRecord].skuCode) {
                // 扣数
                subtractQtyPackEa(fmRows[fmRecordDiff], selectRows[selectRecord]);
            }
        }
        // 清除出库序列号
        var serialLen = soSerialList.length;
        while (serialLen--) {
            if (selectRows[selectRecord].skuCode === soSerialList[serialLen].skuCode) {
                soSerialList.splice(serialLen, 1);
            }
        }

    }
    // 功能按扭控制
    buttonControl();
}

/**
 * 勾选记录，根据当前分配ID获取出库序列号
 */
function getSoSerial() {
    var rows = $.map($('#toTable').bootstrapTable('getSelections'), function (row) { return row });
    if (rows.length === 0) {
        bq.failVoice();
        jp.bqWaring("请选择记录");
        return;
    }
    // 弹出界面
    $('#soSerialListModal').modal();
    // 勾选的分配ID
    var allocIds = $.map(rows, function (row) { return row.allocId });
    var skuCodes = $.map(rows, function (row) { return row.skuCode });
    initSoSerialTable();

    // 列表结果集
    var resultList = [];
    // 循环缓存
    for (var i = 0; i < soSerialList.length; i++) {
        var item = soSerialList[i];
        // 分配ID存在于勾选的分配ID数组中
        // if (allocIds.indexOf(item.allocId) !== -1) {
        //     resultList.push(item);
        // }
        if (skuCodes.indexOf(item.skuCode) !== -1) {
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
 * 是否自动生成箱号改变
 */
function changeIsAutoCartonNo() {
    if (!$('#isAutoCartonNo').prop('checked')) {
        $('#isAutoCartonNo').val('N');
        // 目标箱号手动输入
        $('#toId_toEditor').prop('readonly', false);
    } else {
        $('#isAutoCartonNo').val('Y');
        // 目标箱号灰置
        $('#toId_toEditor').prop('readonly', true);
        // 自动生成箱号
        autoCartonNo();
    }
}

/**
 * 自动生成箱号
 */
function autoCartonNo() {
    if ($('#isAutoCartonNo').prop('checked')) {
        // 目标箱号灰置
        $('#toId_toEditor').prop('readonly', true);
        jp.get("${ctx}/wms/outbound/banQinWmPack/cartonNoGen", function (data) {
            if (data.success) {
                $('#toId_toEditor').val(data.body.entity);
            } else {
                bq.failVoice();
                jp.bqWaring(data.msg)
            }
        });
    }
}

/**
 * 是否自动生成箱号
 */
function isCheckCartonNo() {
    // 是否自动生成目标箱号(控制参数)
    if ('Y' === 'Y') {
        // 自动生成
        $('#isAutoCartonNo').val('Y').prop('checked', true);
        // 目标箱号灰置
        $('#toId_toEditor').prop('readonly', true);
        // 自动生成箱号
        autoCartonNo();
    } else {
        // 不自动生成
        $('#isAutoCartonNo').val('N').prop('checked', false);
        // 目标箱号手动输入
        $('#toId_toEditor').prop('readonly', false);
    }
}

/**
 * 是否单件扫描，值处理
 */
function isSingleScan() {
    if ($('#isSingle').prop('checked')) {
        // 如果勾选，是单件扫描，那么包装单位为EA,数量为1,并且灰置不可编辑
        $('#uom_fmEditor').val('EA').prop('disabled', true);
        $('#qty_fmEditor').val(1).prop('readonly', true);
        $('#qtyEa_fmEditor').val(1);
        // 光标定位商品
        $('#skuCode_fmEditor').focus();
    } else {
        // 如果不勾选，不单件扫描，那么默认列表当前可扫描最大单位，数量不修改
        getPackRecord($('#fmTable').bootstrapTable('getData'));
        // 可编辑
        $('#uom_fmEditor').prop('disabled', false);
        $('#qty_fmEditor').prop('readonly', false).val('');
        $('#qtyEa_fmEditor').val('');
        // 光标定位商品
        $('#skuCode_fmEditor').focus();
    }
}

/**
 * 是否整箱扫描，值处理
 * 1、如果勾选整箱扫描，那么单件扫描必须勾选并且灰置
 * 2、整箱扫描时，如果目标列表为空，那么扫描时直接关箱/换箱
 */
function isSingleCsScan() {
    if ($('#isSingleCs').prop('checked')) {
        // 如果勾选，是整箱扫描，那么包装单位为CS,数量为1,并且灰置不可编辑
        $('#isSingle').val('Y');
        $('#uom_fmEditor').val('CS').prop('disabled', true);
        $('#qty_fmEditor').val(1).prop('disabled', true);
        $('#qtyEa_fmEditor').val('');
        $('#isSingle').prop('disabled', true);
        if ($('#skuCode_fmEditor').val()) {
            // 单位换算
            changeUom();
        }
        // 光标定位商品
        $('#skuCode_fmEditor').focus();
    } else {
        // 如果不勾选，不整箱扫描，那么默认列表当前可扫描最大单位，数量不修改
        getPackRecord($('#fmTable').bootstrapTable('getData'));
        // 可编辑
        $('#uom_fmEditor').prop('disabled', false);
        $('#qty_fmEditor').prop('readonly', false).val('');
        $('#isSingle').prop('disabled', false);
        isSingleScan();
        // 光标定位商品
        $('#skuCode_fmEditor').focus();
    }
}

/**
 * 是否扫描快递单号
 */
function isTrackChange() {
    if ($('#isTrack').prop('checked')) {
        // 如果勾选，扫描快递单号 光标定位
        $('#trackingNo_toEditor').prop('readonly', false).focus();
    } else {
        // 如果不勾选，不扫描快递单号
        $('#trackingNo_toEditor').prop('readonly', true);
    }
}

/**
 * 是否称重 勾选，必须输入重量才可关箱
 */
function isWeighChange() {
    if ($('#isWeigh').prop('checked')) {
        // 如果勾选 光标定位
        $('#packWeight_toEditor').prop('readonly', false).focus();
    } else {
        // 如果不勾选
        $('#packWeight_toEditor').prop('readonly', true);
    }
}

/**
 * 光标定位
 */
function setCurrentRowIndex(grid, record) {
    var rowIndex = $("#" + grid).bootstrapTable("getIndexByField", { field: "unqId", values: [record.unqId]});
    if (rowIndex.length > 0) {
        $('#' + grid).bootstrapTable('updateRow', {index: rowIndex[0] - 1, row: record});
        jp.changeTargetTableStyle(('#' + grid), $("#" + grid + " tr")[rowIndex[0]]);
    }
}

function setCurrentRowIndexById(grid, record) {
    var rowIndex = $("#" + grid).bootstrapTable("getIndexByField", { field: "id", values: [record.id]});
    if (rowIndex.length > 0) {
        $('#' + grid).bootstrapTable('updateRow', {index: rowIndex[0] - 1, row: record});
        jp.changeTargetTableStyle(('#' + grid), $("#" + grid + " tr")[rowIndex[0]]);
    }
}

/**
 * 按钮使用控制
 */
function buttonControl() {
    if ($('#fmTable').bootstrapTable('getData').length === 0) {
        $('#changeId').prop('disabled', true);
        $('#cancelPack').prop('disabled', true);
        $('#batchCheck').prop('disabled', true);
    } else {
        if ($('#toTable').bootstrapTable('getData').length > 0) {
            $('#changeId').prop('disabled', false);
            $('#cancelPack').prop('disabled', false);
            $('#batchCheck').prop('disabled', true);
        } else {
            $('#changeId').prop('disabled', true);
            $('#cancelPack').prop('disabled', true);
            $('#batchCheck').prop('disabled', false);
        }
    }
}

function isContainStr(source, str) {
    if (!source) return false;
    return $.inArray(str, source.split(",")) >= 0;
}

function guid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
        var r = Math.random() * 16 | 0,
            v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}
</script>