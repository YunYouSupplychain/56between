<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var scanSerialList = [];// 已打包未关箱扫描序列号
var packedData = {};// 已打包未关箱记录{分配ID:分配记录}
var isClosing = false;// 是否正在关箱中
var isAutoPack = '';

$(function () {
    $("input[type='text']").attr('autocomplete', 'off');// 关闭自动完成
    $("input[name='isPacked']").eq(0).prop('checked', true);// 默认未打包
    $("#isSingle").prop('checked', true);// 默认单件
    $("#op").val("${fns:getUser().name}");// 操作人
    generateBoxNo();// 生成箱号
    bindEvent();// 绑定事件
    initTable();// 初始化表格
    $("#packMaterial").focus();// 默认聚焦包材输入框
    getAutoPackParam(); // 获取自动关箱系统控制参数
});

function getAutoPackParam() {
    var value = bq.getSysControlParam('IS_AUTO_PACK', jp.getCurrentOrg().orgId);
    isAutoPack = !value ? 'N' : value;
}

function resetV() {
    scanSerialList = [];
    packedData = {};
    $("#packMaterial").val('').focus();
    $("#soNo").val('');
    $("#skuCode").val('');
    $("#caseNo").val('');
    $("#serialNo").prop('readonly', true).val('');
    $("#isSingle").prop('checked', true);
    $("#isWeight").prop('checked', false);
    $("#weight").prop('readonly', true).val('');
    $("textarea").prop('readonly', true).val('');
    $("#scanQty").val(1);
    $("#banQinWmPackTable").bootstrapTable('removeAll');
}

function generateBoxNo() {
    jp.get("${ctx}/wms/outbound/banQinWmPack/cartonNoGen", function (data) {
        if (data.success) {
            $('#toId').val(data.body.entity);
        } else {
            bq.failVoice();
            jp.bqWaring(data.msg)
        }
    });
}

function bindEvent() {
    $("#packConfirm").off('click').on('click', function () {
        jp.confirm("是否确认关箱？", packConfirm);
    });
    $("#cancelPack").off('click').on('click', cancelPack);
    $("#batchPack").off('click').on('click', batchPackHandler);
    $("#querySoSerialNo").off('click').on('click', querySoSerial);
    // 快捷键监听
    document.onkeyup = function (e) {
        var ev = document.all ? window.event : e;
        var keyCode = ev.keyCode || ev.which || ev.charCode;
        if (keyCode === 13) {// 文本框绑定回车事件
            if ($(e.target).is("#wmPackForm input[type='text'],#wmPackForm textarea")) {
                $(e.target).val($(e.target).val().trim());
                textKeyupEvent($(e.target).attr('id'));
            }
        }
        var shiftKey = ev.shiftKey || ev.metaKey;
        if (shiftKey && keyCode === 38) {// shit+↑
            packConfirm();
        }
    };
    // 文本框绑定焦点丢失事件
    $("input[type='text']").off('blur').on('blur', function () {
        $(this).val($(this).val().trim());
    });
    // 禁用文本域数量回车
    $("#scanQty").off('keydown').on('keydown', function (event) {
        if (event.keyCode === 13) {
            return false;
        }
    });
    // 是否打包绑定点击事件
    $("input[name='isPacked']").off('click').on('click', isPackedEvent);
    // 单价绑定点击事件
    $("#isSingle").off('click').on('click', isSingleEvent);
    // 是否称重绑定点击事件
    $("#isWeight").off('click').on('click', isWeightEvent);
}

function initTable() {
    $("#banQinWmPackTable").bootstrapTable('destroy').bootstrapTable({
        minimumCountColumns: 2,//最低显示2行
        cache: false,//是否使用缓存
        uniqueId: 'allocId',
        columns: [{
            checkbox: true
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: false
        }, {
            field: 'allocId',
            title: '分配ID',
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
            field: 'packStatus',
            title: '状态',
            sortable: false,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_PACK_STATUS'))}, value, "-");
            }
        }, {
            field: 'qtySoEa',
            title: '订单数',
            sortable: false
        }, {
            field: 'qtyEa',
            title: '拣货数',
            sortable: false
        }, {
            field: 'qtyPackEa',
            title: '验货数',
            sortable: false
        }, {
            field: 'packScanCount',
            title: '扫描次数',
            sortable: false
        }, {
            field: 'unConfirmQty',
            title: '待验数量',
            sortable: false
        }]
    });
}

function initSerialTable() {
    $('#soSerialTable').bootstrapTable('destroy').bootstrapTable({
        height: $(window).height() - 360,
        minimumCountColumns: 2,//最低显示2行
        cache: false,//是否使用缓存
        columns: [{
            checkbox: true
        }, {
            field: 'serialNo',
            title: '序列号',
            sortable: false
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: false
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
            field: 'lotNum',
            title: '批次号',
            sortable: false
        }, {
            field: 'allocId',
            title: '分配ID',
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

function queryTableData() {
    var idx = jp.loading();
    var searchParam = {};
    searchParam.orgId = jp.getCurrentOrg().orgId;
    searchParam.soNo = $('#soNo').val();
    searchParam.isPacked = $("input[name='isPacked']:checked").val();
    jp.post("${ctx}/wms/outbound/banQinWmPack/data", searchParam, function (data) {
        if (data.success) {
            var rows = [], temp = data.body.entity.rows;
            if (temp.length > 0) {
                $.each(temp, function (i, row) {
                    if (!jQuery.isEmptyObject(packedData) && packedData.hasOwnProperty(row.allocId)) {
                        rows.push(packedData[row.allocId]);
                    } else {
                        if (row.packOp && row.packTime) {
                            row.qtyPackEa = row.qtyEa;
                            row.packStatus = "20";
                        } else {
                            row.qtyPackEa = 0;
                            row.packStatus = "00";
                        }
                        if (row.packScanCount === undefined) {
                            row.packScanCount = 0;
                        }
                        row.unConfirmQty = row.qtyEa - row.qtyPackEa;
                        rows.push(row);
                    }
                });
            }
            $("#banQinWmPackTable").bootstrapTable('load', rows);
            changeLineColor();
            $("#skuCode").focus();
            bq.successVoice();
            jp.close(idx);
        } else {
            bq.failVoice();
            jp.bqError(data.msg);
        }
    });
}

function querySoSerial() {
    var idx = jp.loading();
    var allocIds = $.map($("#banQinWmPackTable").bootstrapTable('getSelections'), function (row) {
        return row.allocId;
    });
    if (allocIds.length <= 0) {
        bq.failVoice();
        jp.warning("请选择记录");
        return;
    }
    initSerialTable();
    $.ajax({
        type: "post",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        async: false,
        data: JSON.stringify({allocIds: allocIds, orgId: jp.getCurrentOrg().orgId}),
        url: "${ctx}/wms/outbound/banQinWmCheck/getSoSerialItemByAllocIds",
        success: function (data) {
            if (data.success) {
                var result = data.body.entity;
                if (result.length > 0) {
                    $('#soSerialTable').bootstrapTable('load', result);
                } else {
                    $('#soSerialTable').bootstrapTable('removeAll');
                }
            }
        }
    });
    $("#soSerialListModal").modal();
    jp.close(idx);
}

function textKeyupEvent(id) {
    var isPacked = $("input[name='isPacked']:checked").val();
    switch (id) {
        case "packMaterial":
            $("#soNo").focus();
            break;
        case "soNo":
            scanSo();
            break;
        case "skuCode":
            if (isPacked === "N") {
                scanSku();
            } else {// 已打包，则按扫描商品再过滤
                filterSku();
            }
            break;
        case "serialNo":
            if (isPacked === "N") {
                scanSerial();
            }
            break;
        case "scanQty":
            if (isPacked === "N") {
                scanQty();
            }
            break;
    }
}

function isPackedEvent() {
    var $checked = $("input[name='isPacked']:checked");
    var isPacked = $checked.val();
    if (isPacked === 'Y' && !jQuery.isEmptyObject(packedData)) {// 已打包且存在打包数据
        jp.confirm("刚打包未关箱数据将丢失，是否确认?", function () {
            resetV();
            $("#isSingle").prop('disabled', true);
            $("#isWeight").prop('disabled', true);
        }, function () {
            $checked.prop('checked', false);
            $("input[name='isPacked']").eq(0).prop('checked', true);
            $("#isSingle").prop('disabled', false);
            $("#isWeight").prop('disabled', false);
        });
    } else if (isPacked === 'Y') {
        resetV();
        $("#isSingle").prop('disabled', true);
        $("#isWeight").prop('disabled', true);
    } else {
        resetV();
        $("#isSingle").prop('disabled', false);
        $("#isWeight").prop('disabled', false);
    }
}

function isSingleEvent() {
    var isSingle = $(this).prop('checked');
    if (isSingle) {
        $("#scanQty").prop('readonly', true).val("1");
        $("#skuCode").focus();
    } else {
        $("#scanQty").prop('readonly', false).val("1").focus().select();
    }
}

function isWeightEvent() {
    var isWeight = $(this).prop('checked');
    if (isWeight) {
        $("#weight").prop('readonly', false).val("").focus();
    } else {
        $("#weight").prop('readonly', true).val("");
    }

}

/**
 * 扫入出库单号后
 */
function scanSo() {
    if (!checkSo()) return;
    $("#status").val("");
    queryTableData();
}

/**
 * 扫入商品后
 */
function scanSku() {
    if (!checkSku()) return;
    if (checkSkuIsSerial()) {
        $("#serialNo").prop('readonly', false).focus();
        $("#isSingle").prop('checked', true).prop('disabled', true);
        $("#scanQty").prop('readonly', true).val('1');
    } else {
        $("#serialNo").prop('readonly', true);
        var isSingle = $("#isSingle").prop('disabled', false).prop('checked');
        if (isSingle) {
            pack($("#skuCode").val(), false, $("#isWeight").prop('checked'));
        } else {
            $("#scanQty").focus().select();
        }
    }
}

/**
 * 扫入序列号后
 */
function scanSerial() {
    if (!checkSerial()) return;
    pack($("#skuCode").val(), true, $("#isWeight").val());
}

/**
 * 扫入数量后
 */
function scanQty() {
    if (!checkSku()) return;
    pack($("#skuCode").val(), false, $("#isWeight").prop('checked'));
}

/**
 * 校验SKU是否序列化
 * @returns {boolean}
 */
function checkSkuIsSerial() {
    var isSerial = false;
    var skuCode = $("#skuCode").val();
    $.each($("#banQinWmPackTable").bootstrapTable('getData'), function (i, row) {
        if (row.hasOwnProperty("barcode") && row.barcode.length > 0 && $.inArray(skuCode, row.barcode.split(",")) >= 0
            && (row.isSerial === "Y" || row.isUnSerial === "Y")) {
            isSerial = true;
            return false;
        }
    });
    return isSerial;
}

/**
 * 校验出库单号
 * @returns {boolean}
 */
function checkSo() {
    var soNo = $("#soNo").val();
    if (soNo.length <= 0) {
        bq.failVoice();
        jp.warning("出库单号不能为空");
        $("#soNo").focus();
        return false;
    }
    return true;
}

/**
 * 校验商品
 * @returns {boolean}
 */
function checkSku() {
    if (!checkSo()) return false;

    var skuCode = $("#skuCode").val();
    if (skuCode.length <= 0) {
        bq.failVoice();
        jp.warning("商品编码不能为空");
        $("#skuCode").focus();
        return false;
    }
    var isExist = false;
    $.each($("#banQinWmPackTable").bootstrapTable('getData'), function (i, row) {
        if (row.hasOwnProperty("barcode") && row.barcode.length > 0 && $.inArray(skuCode, row.barcode.split(",")) >= 0) {
            isExist = true;
            return false;
        }
    });
    if (!isExist) {
        bq.failVoice();
        jp.warning("商品【" + skuCode + "】不存在，请重新扫描");
        $("#skuCode").focus().select();
        return false;
    }
    return true;
}

/**
 * 校验序列号
 * @returns {boolean}
 */
function checkSerial() {
    if (!checkSku()) return false;
    var skuCode = $("#skuCode").val();
    var serialNo = $("#serialNo").val();
    if (serialNo.length <= 0) {
        bq.failVoice();
        jp.warning("序列号不能为空");
        $("#serialNo").focus();
        return false;
    }
    var wmSoSerial = {};
    $.each($("#banQinWmPackTable").bootstrapTable('getData'), function (i, row) {
        if (row.hasOwnProperty("barcode") && row.barcode.length > 0 && $.inArray(skuCode, row.barcode.split(",")) >= 0
            && row.packStatus !== '20') {
            wmSoSerial.soNo = row.soNo;
            wmSoSerial.ownerCode = row.ownerCode;
            wmSoSerial.skuCode = row.skuCode;
            wmSoSerial.serialNo = serialNo;
            wmSoSerial.lotNums = [row.lotNum];
            wmSoSerial.allocIds = [row.allocId];
            wmSoSerial.isUnSerial = row.isUnSerial;
            wmSoSerial.orgId = jp.getCurrentOrg().orgId;
            return false;
        }
    });
    if (jQuery.isEmptyObject(wmSoSerial)) {
        bq.failVoice();
        jp.warning("序列号【+" + serialNo + "+】不存在，请重新扫描");
        $("#serialNo").focus().select();
        return false;
    }
    // 校验是否已扫描
    var isScanned = false;
    $.each(scanSerialList, function (i, row) {
        if (row.skuCode === wmSoSerial.skuCode && row.serialNo === wmSoSerial.serialNo) {
            isScanned = true;
            return false;
        }
    });
    if (isScanned) {
        bq.failVoice();
        jp.warning("序列号已扫描");
        $("#serialNo").focus();
        return false;
    }
    // 判断是否是非库存序列号管理
    if (wmSoSerial.isUnSerial === 'Y') {
        return true;
    }

    var isSuccess = false;
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'POST',
        dataType: "json",
        async: false,
        data: JSON.stringify(wmSoSerial),
        url: "${ctx}/wms/outbound/banQinWmCheck/checkSoSerial",
        success: function (data) {
            if (data.success) {
                isSuccess = true;
                bq.successVoice();
            } else {
                isSuccess = false;
                bq.failVoice();
                jp.bqWaring(data.msg);
                $("#serialNo").focus().select();
            }
        }
    });
    return isSuccess;
}

/**
 * 获取待验货数量
 * @param skuCode 商品编码
 * @returns {number} 待验货数量
 */
function getUnConfirmQty(skuCode) {
    var unConfirmQty = 0;
    $.each($("#banQinWmPackTable").bootstrapTable('getData'), function (i, row) {
        if (row.hasOwnProperty("barcode") && row.barcode.length > 0 && $.inArray(skuCode, row.barcode.split(",")) >= 0) {
            unConfirmQty += row.unConfirmQty;
        }
    });
    return unConfirmQty;
}

/**
 * 打包
 * @param skuCode 商品编码
 * @param isSerial 是否序列号
 * @param isWeight 是否称重
 */
function pack(skuCode, isSerial, isWeight) {
    var serialNo, weight;
    if (isSerial) {
        serialNo = $("#serialNo").val();
    }
    if (isWeight) {
        weight = $("#weight").val();
    }

    var $table = $("#banQinWmPackTable");
    var packQtyEa = Number($("#scanQty").val());
    if (packQtyEa <= 0) {
        bq.failVoice();
        jp.warning("打包数量必须大于零");
        return;
    }
    var rows = $table.bootstrapTable('getData');
    // 校验打包数量是否超出待验数量
    if (getUnConfirmQty(skuCode) < packQtyEa) {
        bq.failVoice();
        jp.warning("打包数量超出待验数量");
        return;
    }
    var tmpArr = [];
    // 打包
    for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        if (row.hasOwnProperty("barcode") && row.barcode.length > 0 && $.inArray(skuCode, row.barcode.split(",")) >= 0 && row.packStatus !== "20") {
            if (!checkConsigneeAndCarrierAndLine(row)) {
                bq.failVoice();
                jp.bqWaring("承运商或收货信息与已打包数据不同，不能打包到同一个箱号中");
                return;
            }
            var allocId = row.allocId;
            if (row.unConfirmQty > packQtyEa) {
                row.qtyPackEa += packQtyEa;
                row.unConfirmQty -= packQtyEa;
                packQtyEa = 0;
            } else {
                row.qtyPackEa += row.unConfirmQty;
                packQtyEa -= row.unConfirmQty;
                row.unConfirmQty = 0;
            }
            if (row.unConfirmQty === 0) {
                row.packStatus = "20";
            } else if (row.unConfirmQty === row.qtyEa) {
                row.packStatus = "00";
            } else {
                row.packStatus = "10";
            }
            if (isWeight) {
                row.packWeight = weight;
            }
            if (isSerial) {
                var wmSoSerial = {
                    soNo: row.soNo,
                    skuCode: row.skuCode,
                    serialNo: serialNo,
                    lineNo: row.lineNo,
                    allocId: row.allocId,
                    ownerCode: row.ownerCode,
                    lotNum: row.lotNum,
                    orgId: row.orgId,
                    isUnSerial: row.isUnSerial
                };
                scanSerialList.push(wmSoSerial);
            }
            row.packScanCount += 1;
            tmpArr.push(row);
            if (packQtyEa === 0) {
                $("#status").val(jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_PACK_STATUS'))}, rows[i].packStatus, "-"));
                break;
            }
        }
    }
    for (var j = 0; j < tmpArr.length; j++) {
        var data = tmpArr[j];
        packedData[data.allocId] = data;
        $table.bootstrapTable('updateByUniqueId', {id: data.allocId, row: data});
    }
    // 改变行颜色
    changeLineColor();
    // 重置数量
    $("#scanQty").val(1);

    // 是否序列化，是：光标定位到序列号文本框，否：光标定位到商品编码文本框
    if (isSerial) {
        // 判断当前SKU是否全部扫描
        var currentSkuRow = $table.bootstrapTable('getData').filter(function (value) {
            if (value.skuCode === skuCode) {
                return value;
            }
        });
        var hasNoPack = currentSkuRow.filter(function (value) {
            if (value.packStatus !== '20') {
                return value;
            }
        });
        $("#serialNo").val('');
        if (hasNoPack.length > 0) {
            $("#serialNo").focus();
        } else {
            $("#skuCode").val('').focus();
        }
    } else {
        $("#skuCode").val('').focus();
    }

    bq.successVoice();
    jp.success("操作成功");
    // 判断是否全部打包完成，如果是自动关箱
    if (isAutoPack === 'Y') {
        var allRows = $table.bootstrapTable('getData');
        var isFullPack = true;
        for (var k = 0; k < allRows.length; k++) {
            if (allRows[k].packStatus !== '20') {
                isFullPack = false;
                break;
            }
        }
        if (isFullPack) {
            packConfirm();
        }
    }
}

/**
 * 批量打包
 */
function batchPackHandler() {
    var $table = $('#banQinWmPackTable');
    var rows = $table.bootstrapTable('getData');
    if (rows.length === 0) {
        return;
    }
    var tmpArr = [];
    for (var i = 0; i < rows.length; i++) {
        var row = rows[i];
        // 如果商品有序列号管理，并且未复核、未打包，那么必须扫描打包
        if (row.isSerial === "Y" && row.checkStatus === "20" && row.packStatus !== "20") {
            bq.failVoice();
            jp.warning("存在需要序列号管理的商品，请扫描序列号打包");
            return;
        }
        if (!checkConsigneeAndCarrierAndLine(row)) {
            bq.failVoice();
            jp.bqWaring("承运商或收货信息与已打包数据不同，不能打包到同一个箱号中");
            return;
        }
        row.qtyPackEa += row.unConfirmQty;
        row.unConfirmQty = 0;
        row.packStatus = "20";
        row.packScanCount += 1;
        tmpArr.push(row);
    }
    for (var j = 0; j < tmpArr.length; j++) {
        var data = tmpArr[j];
        packedData[data.allocId] = data;
        $table.bootstrapTable('updateByUniqueId', {id: data.allocId, row: data});
    }
    changeLineColor();
    bq.successVoice();
    jp.success("操作成功");
}

/**
 * 关箱
 */
function packConfirm() {
    if (isClosing) {
        jp.loading("正在操作中，请稍后操作...");
        return;
    }
    jp.loading();
    if (jQuery.isEmptyObject(packedData) || packedData.length <= 0) {
        isClosing = false;
        bq.failVoice();
        jp.warning("未扫描打包，无法进行关箱操作");
        $("#packMaterial").focus();
        return;
    }
    var rows = [];
    isClosing = true;
    var toId = $("#toId").val() + $("#packMaterial").val();
    var caseNo = $("#caseNo").val();
    for (var k in packedData) {
        var row = packedData[k];
        row.caseNo = caseNo;
        rows.push(row);
    }
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'post',
        dataType: "json",
        data: JSON.stringify({
            allocItems: rows,
            toId: toId,
            soSerialList: scanSerialList,
            isCheck: 'Y',
            isPrintContainer: 'N',
            isPrintLabel: 'Y',
            orgId: jp.getCurrentOrg().orgId
        }),
        url: "${ctx}/wms/outbound/banQinWmPack/updateTraceId",
        success: function (data) {
            generateBoxNo();
            if (data.success) {
                bq.printWayBill(data.body.imageList);
                bq.successVoice();
                jp.success("关箱成功");
                window.location = "${ctx}/wms/outbound/banQinWmPack/newList";
            } else {
                bq.failVoice();
                jp.bqWaring(data.msg);
            }
            isClosing = false;
        },
        error: function () {
            generateBoxNo();
            isClosing = false;
        }
    });
}

/**
 * 取消打包
 */
function cancelPack() {
    jp.confirm("是否确认取消打包？", function () {
        jp.loading();
        var allocIds = $.map($("#banQinWmPackTable").bootstrapTable('getSelections'), function (row) {
            return row.allocId;
        });
        if (allocIds.length <= 0) {
            bq.failVoice();
            jp.warning("请选择记录");
            return;
        }
        var isPacked = $("input[name='isPacked']:checked").val();// 已打包Y 未打包：N
        if (isPacked === "Y") {
            cancelPackForPacked(allocIds);
        } else {
            cancelPackForUnpacked(allocIds);
        }
    });
}

/**
 * 取消打包 - 页面勾选已打包
 * @param allocIds 分配ID
 */
function cancelPackForPacked(allocIds) {
    // 提交后台取消打包
    $.ajax({
        type: 'post',
        contentType: 'application/json;charset=UTF-8',
        dataType: "json",
        data: JSON.stringify({allocIds: allocIds, orgId: jp.getCurrentOrg().orgId}),
        url: "${ctx}/wms/outbound/banQinWmPack/cancelPack",
        success: function (data) {
            generateBoxNo();
            if (data.success) {
                queryTableData();
                bq.successVoice();
                jp.success("操作成功");
            } else {
                bq.failVoice();
                jp.bqWaring(data.msg);
            }
        },
        error: function () {
            generateBoxNo();
        }
    });
}

/**
 * 取消打包 - 页面勾选未打包
 * @param allocIds 分配ID
 */
function cancelPackForUnpacked(allocIds) {
    $.each(allocIds, function (i, allocId) {
        delete packedData[allocId];

        var tmp = [];
        $.each(scanSerialList, function (i, scanSerial) {
            if (allocId !== scanSerial.allocId) {
                tmp.push(scanSerial);
            }
        });
        scanSerialList = tmp;

        var $table = $("#banQinWmPackTable");
        var row = $table.bootstrapTable('getRowByUniqueId', allocId);
        row.packStatus = "00";
        row.packScanCount = 0;
        row.qtyPackEa = 0;
        row.unConfirmQty = row.qtyEa;
        $table.bootstrapTable('updateByUniqueId', {id: allocId, row: row});
    });
    changeLineColor();
    $("#status").val();
    bq.successVoice();
    jp.success("操作成功");
}

/**
 * 改变行背景色 颜色(success绿 warning黄 info蓝)
 */
function changeLineColor() {
    var data = $("#banQinWmPackTable").bootstrapTable("getData");
    $.each(data, function (i, row) {
        var $tr = $("#banQinWmPackTable>tbody").find("tr").eq(i);
        $tr.removeClass("success").removeClass("warning").removeClass("info");
        if (row.unConfirmQty !== row.qtyEa) {
            $tr.addClass(row.unConfirmQty === 0 ? "success" : "info");
        }
    });
}

/**
 * 打包时，校验收货人、承运人、路线是否一致
 */
function checkConsigneeAndCarrierAndLine(fmRecord) {
    var toRecord;
    for (var allocId in packedData) {// 任取一条已打包的记录
        toRecord = packedData[allocId];
        break;
    }
    if (fmRecord && toRecord) {
        // 收货人同名同址(收货人名称、电话、地址+联系人名称、电话、地址)才可以打包到同一个包裹里面
        if (fmRecord.consigneeName !== toRecord.consigneeName || fmRecord.consigneeTel !== toRecord.consigneeTel
            || fmRecord.consigneeAddr !== toRecord.consigneeAddr || fmRecord.contactName !== toRecord.contactName
            || fmRecord.contactTel !== toRecord.contactTel || fmRecord.contactAddr !== toRecord.contactAddr) {
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

function filterSku() {
    var skuCode = $("#skuCode").val();
    if (!skuCode) {
        queryTableData();
        return;
    }
    var data = [];
    var rows = $("#banQinWmPackTable").bootstrapTable('getData');
    $.each(rows, function (i, row) {
        if (row.hasOwnProperty("barcode") && row.barcode.length > 0 && $.inArray(skuCode, row.barcode.split(",")) >= 0) {
            data.push(row);
        }
    });
    $("#banQinWmPackTable").bootstrapTable('load', data);
    changeLineColor();
}

</script>