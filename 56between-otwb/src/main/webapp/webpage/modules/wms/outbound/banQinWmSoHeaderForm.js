<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var skuDetail_currentRow; // 商品明细当前行
var preAllocDetail_currentRow; // 预配明细当前行
var allocDetail_currentRow; // 分配明细当前行
var skuDetail_isShowTab = false; // 是否显示右边商品明细tab页
var preAllocDetail_isShowTab = false; // 是否显示右边收货明细tab页
var allocDetail_isShowTab = false; // 是否显示右边上架任务tab页
var currentLoginName = '${fns:getUser().loginName}';
var disabledObj = [];

$(document).ready(function () {
    // 初始化商品明细tab
    initWmSoDetailTab();
    // 初始化预配明细tab
    initWmSoPreAllocTab();
    // 初始化分配明细tab
    initWmSoAllocTab();
    // 初始化序列号tab
    initWmSoSerialTab();
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
    var detailTabIndex = sessionStorage.getItem("SO_" + currentLoginName +"_detailTab");
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
        case "skuDetail": 
            skuDetailClick(row);
            break;
        case "preAllocDetail":
            preAllocDetailClick(row);
            break;
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
        case "skuDetail":
            skuDetailDbClick(row);
            break;
        case "preAllocDetail":
            preAllocDetailDbClick(row);
            break;
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

/**************************************SO单头开始********************************************/

/**
 * SO单头保存
 */
function save() {
    var isValidate = bq.headerSubmitCheck('#inputForm');
    if (isValidate.isSuccess) {
        openDisable('#inputForm');
        jp.loading();
        jp.post("${ctx}/wms/outbound/banQinWmSoHeader/save", $('#inputForm').bq_serialize(), function (data) {
            if (data.success) {
                jp.success(data.msg);
                window.location = "${ctx}/wms/outbound/banQinWmSoHeader/form?id=" + data.body.entity.id;
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
 * 复制
 */
function duplicate() {
    jp.loading();
    jp.get("${ctx}/wms/outbound/banQinWmSoHeader/duplicate" + "?soNo=" + $('#soNo').val() + "&orgId=" + $('#orgId').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/outbound/banQinWmSoHeader/form?id=" + data.body.entity.id;
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
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
 * 生成波次
 */
function generateWave() {
    if ($('#auditStatus') === '00') {
        jp.bqError("订单未审核");
        return;
    }
    if ($('#waveNo').val()) {
        jp.bqError("已经生成波次");
        return;
    }

    $('#wvRuleModal').modal();
    $('#ruleCode').val('');
    $('#ruleName').val('');
}

function wvRuleConfirm() {
    jp.loading();
    jp.get("${ctx}/wms/outbound/banQinWmSoHeader/generateWave?soNo=" + $('#soNo').val() + "&waveRuleCode=" + $('#ruleCode').val() + "&orgId=" + $('#orgId').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/outbound/banQinWmSoHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
            $('#wvRuleModal').modal('hide');
        } else {
            jp.bqError(data.msg);
        }
    })
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
    jp.loading();
    jp.get("${ctx}/wms/outbound/banQinWmSoHeader/" + method + "?soNo=" + $('#soNo').val() + "&orgId=" + $('#orgId').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/outbound/banQinWmSoHeader/form?id=" + $('#id').val();
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
        $('#orderTimeId').val(jp.dateFormat(new Date(), 'yyyy-MM-dd hh:mm:ss'));
    } else {
        $('#soType').prop('disabled', true);
        $('#ownerSelectId').prop('disabled', true);
        $('#ownerDeleteId').prop('disabled', true);
        $('#ownerCode').prop('disabled', true);
        $('#ownerName').prop('disabled', true);
    }
    // 公共信息
    $('#orderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#fmEta').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#toEta').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#auditTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#ediSendTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    laydate.render({elem: '#skuDetail_outboundTime', theme: '#393D49', type: 'datetime'});
}

/**
 * 功能按扭控制
 */
function buttonControl() {
    buttonAllByHeader(false);
    buttonAllByDetail(false);
    buttonAllByPrealloc(false);
    buttonAllByAlloc(false);
    buttonAllByVas(false);
    buttonAllBySpecFee(false);
    // 订单按扭控制
    buttonControlByHeader();
    // 商品明细按扭控制
    buttonControlByDetailExpand();
    buttonControlByDetailCollapse();
    // 预配明细按扭控制
    buttonControlByPrealloc();
    // 分配明细按扭控制
    buttonControlByAlloc();
    if ($('#id').val()) {
        // 冻结状态控制
        if ($('#holdStatus').val() === '00') {
            // 无冻结
            $('#header_holdOrder').css({"pointer-events": "auto", "color": "#1E1E1E"});
            $('#header_cancelHold').css({"pointer-events": "none", "color": "#8A8A8A"});
        } else {
            buttonAllByHeader(true);
            buttonAllByDetail(true);
            buttonAllByPrealloc(true);
            buttonAllByAlloc(true);
            // 冻结
            $('#header_duplicate').attr('disabled', false);
            $('#header_cancelHold').css({"pointer-events": "auto", "color": "#1E1E1E"});
            $('#header_intercept').css({"pointer-events": "auto", "color": "#1E1E1E"});
        }
        // 拦截状态控制
        if ($('#interceptStatus').val() === '90'|| $('#interceptStatus').val() === '00') {
            // 无拦截
            $('#header_intercept').css({"pointer-events": "none", "color": "#8A8A8A"});
        } else {
            // 拦截
            buttonAllByHeader(true);
            buttonAllByDetail(true);
            buttonAllByPrealloc(true);
            buttonAllByAlloc(true);
            $('#header_duplicate').attr('disabled', false);
            $('#header_intercept').css({"pointer-events": "auto", "color": "#1E1E1E"});
        }
        // 订单关闭||订单取消
        if ($('#status').val() === '90' || $('#status').val() === '99') {
            buttonAllByHeader(true);
            $('#header_duplicate').attr('disabled', false);
            if ($('#status').val() === '99') {
                // 订单关闭后，可重新计费
            }
            buttonAllByDetail(true);
            buttonAllByPrealloc(true);
            buttonAllByAlloc(true);
        }
    }
}

/**
 * 订单header全部 功能按扭
 */
function buttonAllByHeader(flag) {
    $('#header_save').attr('disabled', flag);
    $('#header_duplicate').attr('disabled', flag);
    $('#header_audit').attr('disabled', flag);
    $('#header_cancelAudit').attr('disabled', flag);
    $('#header_generateWave').attr('disabled', flag);
    $('#header_preAlloc').attr('disabled', flag);
    $('#header_alloc').attr('disabled', flag);
    $('#header_picking').attr('disabled', flag);
    $('#header_shipment').attr('disabled', flag);
    $('#header_closeOrder').attr('disabled', flag);
    $('#header_cancelOrder').attr('disabled', flag);
    $('#header_cancelPreAlloc').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
    $('#header_cancelAlloc').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
    $('#header_cancelPicking').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
    $('#header_cancelShipment').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
    $('#header_generateLd').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
    $('#header_ldTransfer').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
    $('#header_ldCancelTransfer').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
    $('#header_holdOrder').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
    $('#header_cancelHold').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
    $('#header_intercept').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
}

/**
 * 商品明细全部 功能按扭
 */
function buttonAllByDetail(flag) {
    $('#skuDetail_add').attr('disabled', flag);
    $('#skuDetail_save').attr('disabled', flag);
    $('#skuDetail_remove').attr('disabled', flag);
    $('#skuDetail_duplicate').attr('disabled', flag);
    $('#skuDetail_preAlloc').attr('disabled', flag);
    $('#skuDetail_alloc').attr('disabled', flag);
    $('#skuDetail_manuaPreAlloc').attr('disabled', flag);
    $('#skuDetail_manuaAlloc').attr('disabled', flag);
    $('#skuDetail_picking').attr('disabled', flag);
    $('#skuDetail_cancelPreAlloc').attr('disabled', flag);
    $('#skuDetail_cancelAlloc').attr('disabled', flag);
    $('#skuDetail_cancelPicking').attr('disabled', flag);
    $('#skuDetail_cancel').attr('disabled', flag);
}

/**
 * 预配明细全部 功能按扭
 * @param flag
 */
function buttonAllByPrealloc(flag) {
}

/**
 * 分配明细全部 功能按扭
 * @param flag
 */
function buttonAllByAlloc(flag) {
    $('#allocDetail_add').attr('disabled', flag);
    $('#allocDetail_save').attr('disabled', flag);
    $('#allocDetail_pick').attr('disabled', flag);
    $('#allocDetail_manualPick').attr('disabled', flag);
    $('#allocDetail_shipment').attr('disabled', flag);
    $('#allocDetail_cancelAlloc').attr('disabled', flag);
    $('#allocDetail_cancelPick').attr('disabled', flag);
    $('#allocDetail_cancelShipment').attr('disabled', flag);
}

/**
 * 增值费用全部 功能按扭
 */
function buttonAllByVas(flag) {
}

/**
 * 特殊费用全部 功能按扭
 */ 
function buttonAllBySpecFee(flag) {
}

/**
 * 出库单 功能按扭控制
 */
function buttonControlByHeader() {
    if (!$('#id').val()) {
        buttonAllByHeader(true);
        $('#header_save').attr('disabled', false);
        buttonAllByVas(true);
        buttonAllBySpecFee(true);
        return;
    }
    // 审核状态控制(未审核，不能执行)
    if ($('#auditStatus').val() === '00') {
        $('#header_cancelAudit').attr('disabled', true);
        $('#header_generateWave').attr('disabled', true);
        $('#header_preAlloc').attr('disabled', true);
        $('#header_alloc').attr('disabled', true);
        $('#header_picking').attr('disabled', true);
        $('#header_shipment').attr('disabled', true);
        $('#header_closeOrder').attr('disabled', true);
        $('#header_cancelPreAlloc').css({"pointer-events": "none", "color": "#8A8A8A"});
        $('#header_cancelAlloc').css({"pointer-events": "none", "color": "#8A8A8A"});
        $('#header_cancelPicking').css({"pointer-events": "none", "color": "#8A8A8A"});
        $('#header_cancelShipment').css({"pointer-events": "none", "color": "#8A8A8A"});
        $('#header_generateLd').css({"pointer-events": "none", "color": "#8A8A8A"});
        $('#header_ldTransfer').css({"pointer-events": "none", "color": "#8A8A8A"});
        $('#header_ldCancelTransfer').css({"pointer-events": "none", "color": "#8A8A8A"});
        return;
    }
    // 审核，不可执行
    buttonAllByHeader(true);
    // 审核，可执行
    $('#header_duplicate').attr('disabled', false);
    // 订单新增状态
    if ($('#status').val() === '00') {
        // 不审核状态
        if ($('#auditStatus').val() === '90') {
            $('#header_save').attr('disabled', false);
            $('#header_cancelOrder').attr('disabled', false);
            $('#header_cancelAudit').attr('disabled', true);
        } else {
            $('#header_cancelAudit').attr('disabled', false);
        }
        $('#header_generateWave').attr('disabled', false);
        // 两步分配，预配按扭可用，分配不可用;反之
        $('#header_alloc').attr('disabled', false);
    } else if ($('#status').val() === '10') {
        $('#header_preAlloc').attr('disabled', false);
        $('#header_alloc').attr('disabled', false);
        $('#header_cancelPreAlloc').css({"pointer-events": "auto", "color": "#1E1E1E"});
    } else if ($('#status').val() === '20') {
        $('#header_alloc').attr('disabled', false);
        $('#header_cancelPreAlloc').css({"pointer-events": "auto", "color": "#1E1E1E"});
    } else if ($('#status').val() === '30') {
        // 两步分配，预配按扭可用，分配不可用;反之
        $('#header_alloc').attr('disabled', false);
        $('#header_cancelPreAlloc').css({"pointer-events": "auto", "color": "#1E1E1E"});
        $('#header_picking').attr('disabled', false);
    } else if ($('#status').val() === '40') {
        $('#header_cancelAlloc').css({"pointer-events": "auto", "color": "#1E1E1E"});
        $('#header_picking').attr('disabled', false);
    } else if ($('#status').val() === '50') {
        // 两步分配，预配按扭可用，分配不可用;反之
        $('#header_alloc').attr('disabled', false);
        $('#header_cancelAlloc').css({"pointer-events": "auto", "color": "#1E1E1E"});
        $('#header_picking').attr('disabled', false);
        $('#header_cancelPicking').css({"pointer-events": "auto", "color": "#1E1E1E"});
        $('#header_shipment').attr('disabled', false);
        if (!$('#ldStatus').val()) {
            $('#header_generateLd').css({"pointer-events": "auto", "color": "#1E1E1E"});
        }
    } else if ($('#status').val() === '60') {
        $('#header_cancelPicking').css({"pointer-events": "auto", "color": "#1E1E1E"});
        $('#header_shipment').attr('disabled', false);
        if (!$('#ldStatus').val()) {
            $('#header_generateLd').css({"pointer-events": "auto", "color": "#1E1E1E"});
        }
    } else if ($('#status').val() === '70') {
        // 两步分配，预配按扭可用，分配不可用;反之
        $('#header_alloc').attr('disabled', false);
        $('#header_cancelAlloc').css({"pointer-events": "auto", "color": "#1E1E1E"});
        $('#header_picking').attr('disabled', false);
        $('#header_cancelPicking').css({"pointer-events": "auto", "color": "#1E1E1E"});
        $('#header_shipment').attr('disabled', false);
        $('#header_cancelShipment').attr('disabled', false);
        // 是否只有SO完全发运才可以关闭SO，N:可以关闭
        $('#header_closeOrder').attr('disabled', false);
        if (!$('#ldStatus').val()) {
            $('#header_generateLd').css({"pointer-events": "auto", "color": "#1E1E1E"});
        }
    } else if ($('#status').val() === '80') {
        $('#header_cancelShipment').css({"pointer-events": "auto", "color": "#1E1E1E"});
        $('#header_closeOrder').attr('disabled', false);
        // 完全发货、装车状态为空，可以装车交接
        if (!$('#ldStatus').val()) {
            $('#header_generateLd').css({"pointer-events": "auto", "color": "#1E1E1E"});
            $('#header_ldTransfer').css({"pointer-events": "auto", "color": "#1E1E1E"});
        }
        // 完全发货、完全交接，可以取消交接
        if ($('#ldStatus').val() === '60') {
            $('#header_ldCancelTransfer').css({"pointer-events": "auto", "color": "#1E1E1E"});
            $('#header_cancelShipment').css({"pointer-events": "auto", "color": "#1E1E1E"});
        }
    }
}

/**
 * 商品明细 批量操作 功能按扭控制
 */
function buttonControlByDetailExpand() {
    if (!$('#id').val()) {
        buttonAllByDetail(true);
        return;
    }
    // 商品明细批量操作
    if (!skuDetail_isShowTab) {
        $('#skuDetail_save').attr('disabled', true);
        // 审核状态控制
        if ($('#auditStatus').val() === '00') {
            $('#skuDetail_preAlloc').attr('disabled', true);
            $('#skuDetail_alloc').attr('disabled', true);
            $('#skuDetail_manuaPreAlloc').attr('disabled', true);
            $('#skuDetail_manuaAlloc').attr('disabled', true);
            $('#skuDetail_picking').attr('disabled', true);
            $('#skuDetail_cancelPreAlloc').attr('disabled', true);
            $('#skuDetail_cancelAlloc').attr('disabled', true);
            $('#skuDetail_cancelPicking').attr('disabled', true);
            return;
        }
        // 己审核，不可执行
        buttonAllByDetail(true);
        if ($('#status').val() === '00') {
            $('#skuDetail_add').attr('disabled', true);
            if ($('#auditStatus').val() === '90') {
                $('#skuDetail_add').attr('disabled', false);
                $('#skuDetail_remove').attr('disabled', false);
            }
            // 两步分配，预配按扭可用，分配不可用;反之
            $('#skuDetail_alloc').attr('disabled', false);
            $('#skuDetail_manuaAlloc').attr('disabled', false);
            $('#skuDetail_cancel').attr('disabled', false);
        } else if ($('#status').val() === '10') {
            $('#skuDetail_preAlloc').attr('disabled', false);
            $('#skuDetail_alloc').attr('disabled', false);
            $('#skuDetail_manuaPreAlloc').attr('disabled', false);
            $('#skuDetail_manuaAlloc').attr('disabled', false);
            $('#skuDetail_cancelPreAlloc').attr('disabled', false);
            $('#skuDetail_cancel').attr('disabled', false);
        } else if ($('#status').val() === '20') {
            $('#skuDetail_alloc').attr('disabled', false);
            $('#skuDetail_cancelPreAlloc').attr('disabled', false);
        } else if ($('#status').val() === '30') {
            $('#skuDetail_alloc').attr('disabled', false);
            $('#skuDetail_manuaAlloc').attr('disabled', false);
            $('#skuDetail_cancelAlloc').attr('disabled', false);
            $('#skuDetail_picking').attr('disabled', false);
            $('#skuDetail_cancel').attr('disabled', false);
        } else if ($('#status').val() === '40') {
            $('#skuDetail_cancelAlloc').attr('disabled', false);
            $('#skuDetail_picking').attr('disabled', false);
        } else if ($('#status').val() === '50') {
            $('#skuDetail_alloc').attr('disabled', false);
            $('#skuDetail_manuaAlloc').attr('disabled', false);
            $('#skuDetail_cancelAlloc').attr('disabled', false);
            $('#skuDetail_picking').attr('disabled', false);
            $('#skuDetail_cancelPicking').attr('disabled', false);
        } else if ($('#status').val() === '60') {
            $('#skuDetail_cancelPicking').attr('disabled', false);
        } else if ($('#status').val() === '70') {
            $('#skuDetail_alloc').attr('disabled', false);
            $('#skuDetail_manuaAlloc').attr('disabled', false);
            $('#skuDetail_cancelAlloc').attr('disabled', false);
            $('#skuDetail_picking').attr('disabled', true);
            $('#skuDetail_cancelPicking').attr('disabled', true);
            $('#skuDetail_cancel').attr('disabled', false);
        }
    }
}

/**
 * 商品明细 单行操作 功能按扭控制
 */
function buttonControlByDetailCollapse() {
    if (!skuDetail_isShowTab) {
        return;
    }
    if (!skuDetail_currentRow || !$('#skuDetail_id').val()) {
        buttonAllByDetail(true);
        $('#skuDetail_save').attr('disabled', false);
        return;
    }
    // 审核状态控制
    if ($('#auditStatus').val() === '00') {
        $('#skuDetail_preAlloc').attr('disabled', true);
        $('#skuDetail_alloc').attr('disabled', true);
        $('#skuDetail_manuaPreAlloc').attr('disabled', true);
        $('#skuDetail_manuaAlloc').attr('disabled', true);
        $('#skuDetail_picking').attr('disabled', true);
        $('#skuDetail_cancelPicking').attr('disabled', true);
        $('#skuDetail_cancelPreAlloc').attr('disabled', true);
        $('#skuDetail_cancelAlloc').attr('disabled', true);
        return;
    }
    // 已审核，不可执行
    buttonAllByDetail(true);
    if ($('#status').val() === '00') {
        if ($('#auditStatus').val() === '90') {
            $('#skuDetail_add').attr('disabled', false);
            $('#skuDetail_save').attr('disabled', false);
            $('#skuDetail_remove').attr('disabled', false);
            $('#skuDetail_duplicate').attr('disabled', false);
        }
        // 两步分配，预配按扭可用，分配不可用;反之
        $('#skuDetail_alloc').attr('disabled', false);
        $('#skuDetail_manuaAlloc').attr('disabled', false);
        $('#skuDetail_cancel').attr('disabled', false);
    } else if ($('#status').val() === '10') {
        $('#skuDetail_preAlloc').attr('disabled', false);
        $('#skuDetail_manuaPreAlloc').attr('disabled', false);
        $('#skuDetail_manuaAlloc').attr('disabled', false);
        $('#skuDetail_alloc').attr('disabled', false);
        $('#skuDetail_cancelPreAlloc').attr('disabled', false);
    } else if ($('#status').val() === '20') {
        $('#skuDetail_alloc').attr('disabled', false);
        $('#skuDetail_cancelPreAlloc').attr('disabled', false);
    } else if ($('#status').val() === '30') {
        $('#skuDetail_manuaAlloc').attr('disabled', false);
        $('#skuDetail_alloc').attr('disabled', false);
        $('#skuDetail_cancelAlloc').attr('disabled', false);
        $('#skuDetail_picking').attr('disabled', false);
    } else if ($('#status').val() === '40') {
        $('#skuDetail_cancelAlloc').attr('disabled', false);
        $('#skuDetail_picking').attr('disabled', false);
    } else if ($('#status').val() === '50') {
        $('#skuDetail_manuaAlloc').attr('disabled', false);
        $('#skuDetail_alloc').attr('disabled', false);
        $('#skuDetail_cancelAlloc').attr('disabled', false);
        $('#skuDetail_picking').attr('disabled', false);
        $('#skuDetail_cancelPicking').attr('disabled', false);
    } else if ($('#status').val() === '60') {
        $('#skuDetail_cancelPicking').attr('disabled', false);
    } else if ($('#status').val() === '70') {
        $('#skuDetail_manuaAlloc').attr('disabled', false);
        $('#skuDetail_alloc').attr('disabled', false);
        $('#skuDetail_cancelAlloc').attr('disabled', false);
        $('#skuDetail_picking').attr('disabled', false);
        $('#skuDetail_cancelPicking').attr('disabled', false);
    }
}

/**
 * 预配明细 功能按扭控制
 */
function buttonControlByPrealloc() {
    
}

/**
 * 分配明细 功能按扭控制
 */
function buttonControlByAlloc() {
    if (!$('#id').val()) {
        buttonAllByAlloc(true);
        return;
    }
    // 未审核
    if ($('#auditStatus').val() === '00') {
        buttonAllByAlloc(true);
        return;
    }
    if (!allocDetail_isShowTab) {
        if ($('#status').val() === '00') {
            buttonAllByAlloc(true);
            $('#allocDetail_add').attr('disabled', false);
        } else if ($('#status').val() === '10') {
            buttonAllByAlloc(true);
            $('#allocDetail_add').attr('disabled', false);
        } else if ($('#status').val() === '20') {
            buttonAllByAlloc(true);
        } else if ($('#status').val() === '30') {
            buttonAllByAlloc(true);
            $('#allocDetail_add').attr('disabled', false);
            $('#allocDetail_pick').attr('disabled', false);
            $('#allocDetail_manualPick').attr('disabled', false);
            $('#allocDetail_cancelAlloc').attr('disabled', false);
        } else if ($('#status').val() === '40') {
            buttonAllByAlloc(true);
            $('#allocDetail_cancelAlloc').attr('disabled', false);
            $('#allocDetail_pick').attr('disabled', false);
            $('#allocDetail_manualPick').attr('disabled', false);
        } else if ($('#status').val() === '50') {
            buttonAllByAlloc(false);
            $('#allocDetail_save').attr('disabled', true);
        } else if ($('#status').val() === '60') {
            buttonAllByAlloc(true);
            $('#allocDetail_cancelPick').attr('disabled', false);
            $('#allocDetail_shipment').attr('disabled', false);
        } else if ($('#status').val() === '70') {
            buttonAllByAlloc(false);
            $('#allocDetail_save').attr('disabled', true);
        } else if ($('#status').val() === '80') {
            buttonAllByAlloc(true);
            $('#allocDetail_cancelShipment').attr('disabled', false);
        }
        return;
    }
    // 审核，可执行
    buttonAllByAlloc(true);
    if (!allocDetail_currentRow || !$('#allocDetail_id').val()) {
        $('#allocDetail_add').attr('disabled', false);
        $('#allocDetail_save').attr('disabled', false);
    } else {
        if (allocDetail_currentRow.status === '40') {
            $('#allocDetail_save').attr('disabled', false);
            $('#allocDetail_pick').attr('disabled', false);
            $('#allocDetail_manualPick').attr('disabled', false);
            $('#allocDetail_cancelAlloc').attr('disabled', false);
        } else if (allocDetail_currentRow.status === '60') {
            $('#allocDetail_cancelPick').attr('disabled', false);
            $('#allocDetail_shipment').attr('disabled', false);
        } else if (allocDetail_currentRow.status === '80') {
            $('#allocDetail_cancelShipment').attr('disabled', false);
        }
    }
}

/**************************************SO单头结束********************************************/
/**************************************弹出框开始********************************************/
function afterSelectSku(row) {
    loadLotAtt({ownerCode: $('#ownerCode').val(), skuCode: row.skuCode, orgId: row.orgId});
}

function afterSelectDetailPack(row) {
    $('#skuDetail_uomQty').val(row.cdprQuantity);
    qtySoChange();
}

function afterSelectAllocSku(row) {
    // 单位换算数量
    var uomQty = !row.uomQty ? 0 : row.uomQty;
    // 分配数
    var qtyEa = !row.qtyAvailable ? 0 : row.qtyAvailable;
    $('#allocDetail_qtyUom').val(Math.floor(qtyEa * 100) / 100 / uomQty);
}

function afterSelectAllocPack(row) {
    $('#allocDetail_uomQty').val(row.cdprQuantity);
    allocSoChange();
}

function afterSelectPickPack(row) {
    $('#allocDetail_uomQty').val(row.cdprQuantity);
    pickSoChange();
}

/**************************************弹出框开始********************************************/
/**************************************商品明细开始********************************************/
/**
 * 初始化明细table
 */
function initWmSoDetailTab() {
    $('#wmSoDetailTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/outbound/banQinWmSoDetail/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.headId = !$('#id').val() == true ? "#" : $('#id').val();
            searchParam.orgId = $('#orgId').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function(row, $el) {
            clickDetail('skuDetail', row);
            jp.changeTableStyle($el);
        },
        onDblClickRow: function (row, $el) {
            dbClickDetail('skuDetail', row);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'lineNo',
            title: '行号',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_STATUS'))}, value, "-");
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
            field: 'packCode',
            title: '包装规格',
            sortable: true
        }, {
            field: 'uom',
            title: '包装单位',
            sortable: true
        }, {
            field: 'cdType',
            title: '越库类型',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_CD_TYPE'))}, value, "-");
            }
        }]
    });
    // 初始化
    initWmSoDetail();
}

/**
 * 初始化
 */
function initWmSoDetail() {
}

/**
 * 商品明细行单击事件
 * @param tab tab页名称
 * @param row 当前行
 */
function skuDetailClick(row) {
    skuDetail_currentRow = row;
    if (skuDetail_isShowTab) {
        // 表单赋值
        evaluate('skuDetail', 'skuDetail_currentRow');
        // 按钮控制
        buttonControl();
    }
}

/**
 * 商品明细行双击事件
 * @param row 当前行
 */
function skuDetailDbClick(row) {
    skuDetail_currentRow = row;
    if (!skuDetail_isShowTab) {
        // 显示右边tab
        showTabRight('#skuDetail_tab-right', '#skuDetail_tab-left');
        skuDetail_isShowTab = true;
        // 加载批次控件
        loadLotAtt(row);
        // 表单赋值
        evaluate('skuDetail', 'skuDetail_currentRow');
        // 字段不可用
        $('#skuDetail_status').prop('disabled', true);
        $('#skuDetail_isPalletize').prop('disabled', true);
    } else {
        // 隐藏右边tab
        hideTabRight('#skuDetail_tab-right', '#skuDetail_tab-left');
        skuDetail_isShowTab = false;
    }
    $('#wmSoDetailTable').bootstrapTable('resetView');
    // 按钮控制
    buttonControl();
}

/**
 * 加载批次属性控件
 */
function loadLotAtt(row) {
    var params = "ownerCode=" + encodeURIComponent(row.ownerCode) + "&skuCode=" + encodeURIComponent(row.skuCode) + "&orgId=" + row.orgId;
    bq.get("${ctx}/wms/basicdata/banQinCdWhLotDetail/getInfo?" + params, false, function (data) {
        if (data) {
            $('#skuDetailLotAttTab').empty();
            var html = bq.getLotAttTab(data, 'skuDetail_lotAtt');
            html = html.replace(new RegExp("required", "g"), "");
            html = html.split('<font color="red">*</font>').join('');
            $('#skuDetailLotAttTab').append(html);
            $('#skuDetailLotAttTab .detail-date').each(function() {
                laydate.render({
                    elem: '#' + $(this).prop('id'),
                    theme: '#393D49'
                });
            });
        }
    })
}

/**
 * 新增明细
 */
function addSkuDetail() {
    // 显示右边Tab
    showTabRight('#skuDetail_tab-right', '#skuDetail_tab-left');
    skuDetail_isShowTab = true;
    skuDetail_currentRow = null;
    // 清空表单
    $(':input', '#skuDetailForm').val('');
    // 初始化
    initAddSkuDetail();
    // 按钮控制
    buttonControlByDetailCollapse();
}

/**
 * 初始化明细
 */
function initAddSkuDetail() {
    $('#skuDetail_status').prop('disabled', true);
    $("#skuDetail_status option:first").prop("selected", true);
    $('#skuDetail_qtySoEa').val('0');
    $('#skuDetail_qtyAllocUom').val('0');
    $('#skuDetail_qtyAllocEa').val('0');
    $('#skuDetail_qtyPkUom').val('0');
    $('#skuDetail_qtyPkEa').val('0');
    $('#skuDetail_qtyShipUom').val('0');
    $('#skuDetail_qtyShipEa').val('0');
    $('#skuDetail_qtyPreallocUom').val('0');
    $('#skuDetail_qtyPreallocEa').val('0');
}

/**
 * 订货数输入同步到EA
 */
function qtySoChange() {
    // 单位换算数量
    var uomQty = !$('#skuDetail_uomQty').val() ? 0 : $('#skuDetail_uomQty').val();
    // 订货数
    var qtySoUom = !$('#skuDetail_qtySoUom').val() ? 0 : $('#skuDetail_qtySoUom').val();
    $('#skuDetail_qtySoEa').val(Math.floor(qtySoUom * 100) / 100 * uomQty);
}

/**
 * 保存明细
 */
function saveSkuDetail() {
    if (!skuDetail_isShowTab) {
        jp.warning("当前无保存记录!");
        return;
    }
    if (!$('#id').val()) {
        jp.warning("请先保存订单头!");
        return;
    }
    if (parseFloat($('#skuDetail_qtySoUom').val()) <= 0) {
        jp.bqError("订货数必须大于0");
        return;
    }
    // 表单验证
    var validate = bq.detailSubmitCheck('#skuDetail_tab-right');
    if (validate.isSuccess) {
        // 保存前赋值
        beforeSave();
        openDisable("#skuDetailForm");
        jp.loading("正在保存中...");
        var row = {};
        bq.copyProperties(skuDetail_currentRow, row);
        $.extend(row, bq.serializeJson($('#skuDetailForm')));
        row.outboundTime = row.outboundTime ? row.outboundTime : jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss");
        $.ajax({
            url: "${ctx}/wms/outbound/banQinWmSoDetail/save",
            type: 'POST',
            data: JSON.stringify(row),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    // 商品明细tab
                    $('#wmSoDetailTable').bootstrapTable('refresh');
                    hideTabRight('#skuDetail_tab-right', '#skuDetail_tab-left');
                    skuDetail_isShowTab = false;
                    // 按钮控制
                    buttonControl();
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
 * 保存前赋值
 */
function beforeSave() {
    if (!$('#skuDetail_id').val()) {
        $('#skuDetail_orgId').val($('#orgId').val());
        $('#skuDetail_soNo').val($('#soNo').val());
        $('#skuDetail_ownerCode').val($('#ownerCode').val());
        $('#skuDetail_headId').val($('#id').val());
        $('#skuDetail_logisticNo').val($('#logisticNo').val());
    }
}

/**
 * 删除明细
 */
function removeSkuDetail() {
    var rows = getSelections('#wmSoDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    jp.confirm('确认要删除吗？删除将不能恢复？', function () {
        var params = "soNo=" + $('#soNo').val();
        params += "&lineNo=" + $(rows).map(function() {return this.lineNo;}).get().join(',');
        params += "&orgId=" + $('#orgId').val();
        jp.loading("正在删除中...");
        jp.get("${ctx}/wms/outbound/banQinWmSoDetail/deleteAll?" + params, function (data) {
            if (data.success) {
                // 商品明细tab
                $('#wmSoDetailTable').bootstrapTable('refresh');
                hideTabRight('#skuDetail_tab-right', '#skuDetail_tab-left');
                skuDetail_isShowTab = false;
                // 按钮控制
                buttonControlByDetailExpand();
                jp.success(data.msg);
            } else {
                closeDisable();
                jp.bqError(data.msg);
            }
        })
    })
}

/**
 * 复制明细
 */
function duplicateSkuDetail() {
    var rows = getSelections('#wmSoDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    if (rows.length > 1) {
        jp.bqError("只能勾选一条记录!");
        return;
    }
    jp.loading();
    var params = "soNo=" + $('#soNo').val();
    params += "&lineNo=" + rows[0].lineNo;
    params += "&orgId=" + $('#orgId').val();
    jp.get("${ctx}/wms/outbound/banQinWmSoDetail/duplicate?" + params, function (data) {
        if (data.success) {
            // 商品明细tab
            $('#wmSoDetailTable').bootstrapTable('refresh');
            hideTabRight('#skuDetail_tab-right', '#skuDetail_tab-left');
            skuDetail_isShowTab = false;
            // 按钮控制
            buttonControlByDetailExpand();
            jp.success(data.msg);
        } else {
            closeDisable();
            jp.bqError(data.msg);
        }
    })
}

/**
 * 预配
 */
function preAllocSkuDetail() {
    jp.loading("预配中...");
    commonSkuDetail('preAlloc');
}

/**
 * 分配
 */
function allocSkuDetail() {
    jp.loading("分配中...");
    commonSkuDetail('alloc');
}

/**
 * 手工预配
 */
function manuaPreAllocSkuDetail() {
    
}

/**
 * 手工分配
 */
function manuaAllocSkuDetail() {
    // 调用分配明细新增
    addAllocDetail();
}

/**
 * 拣货确认
 */
function pickingSkuDetail() {
    jp.loading("拣货确认中...");
    commonSkuDetail('picking');
}

/**
 * 取消预配
 */
function cancelPreAllocSkuDetail() {
    jp.loading("取消预配中...");
    commonSkuDetail('cancelPreAlloc');
}

/**
 * 取消分配
 */
function cancelAllocSkuDetail() {
    jp.loading("取消分配中...");
    commonSkuDetail('cancelAlloc');
}

/**
 * 取消拣货
 */
function cancelPickingSkuDetail() {
    jp.loading("取消拣货中...");
    commonSkuDetail('cancelPicking');
}

/**
 * 取消订单行
 */
function cancelSkuDetail() {
    jp.loading("取消订单行中...");
    commonSkuDetail('cancel');
}

/**
 * 商品明细公用方法
 * @param method 方法名
 */
function commonSkuDetail(method) {
    var rows = [];
    if (!skuDetail_isShowTab) {
        rows = getSelections('#wmSoDetailTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        rows.push(skuDetail_currentRow);
    }
    var params = "?soNo=" + $('#soNo').val();
    params += "&lineNo=" + $(rows).map(function() {return this.lineNo;}).get().join(',');
    params += "&orgId=" + $('#orgId').val();
    jp.get("${ctx}/wms/outbound/banQinWmSoDetail/" + method + params, function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/outbound/banQinWmSoHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            closeDisable();
            jp.bqError(data.msg);
        }
    })
}

/**
 * 明细Tab页索引切换
 */
function detailTabChange(index) {
    sessionStorage.setItem("SO_" + currentLoginName + "_detailTab", index);
}

/**************************************商品明细结束*******************************************/

/**************************************预配明细开始********************************************/
/**
 * 初始化明细table
 */
function initWmSoPreAllocTab() {
    $('#wmSoPreAllocTable').bootstrapTable({
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/outbound/banQinWmSoPrealloc/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.headId = !$('#id').val() == true ? "#" : $('#id').val();
            searchParam.orgId = $('#orgId').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function(row, $el) {
            clickDetail('preAllocDetail', row);
            jp.changeTableStyle($el);
        },
        onDblClickRow: function (row, $el) {
            dbClickDetail('preAllocDetail', row);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'preallocId',
            title: '预配Id',
            sortable: true
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
            field: '商品名称',
            title: 'skuName',
            sortable: true
        }, {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        }, {
            field: 'qtyPreallocUom',
            title: '预配数',
            sortable: true
        }, {
            field: 'qtyPreallocEa',
            title: '预配数EA',
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
    // 初始化
    initWmSoPreAlloc();
}

/**
 * 初始化
 */
function initWmSoPreAlloc() {
    
}

/**
 * 预配明细行单击事件
 * @param tab tab页名称
 * @param row 当前行
 */
function preAllocDetailClick(row) {
    preAllocDetail_currentRow = row;
    if (preAllocDetail_isShowTab) {
        // 表单赋值
        evaluate('preAllocDetail', 'preAllocDetail_currentRow');
    }
}

/**
 * 预配明细行双击事件
 * @param row 当前行
 */
function preAllocDetailDbClick(row) {
    preAllocDetail_currentRow = row;
    if (!preAllocDetail_isShowTab) {
        // 显示右边tab
        showTabRight('#preAllocDetail_tab-right', '#preAllocDetail_tab-left');
        preAllocDetail_isShowTab = true;
        // 表单赋值
        evaluate('preAllocDetail', 'preAllocDetail_currentRow');
    } else {
        // 隐藏右边tab
        hideTabRight('#preAllocDetail_tab-right', '#preAllocDetail_tab-left');
        preAllocDetail_isShowTab = false;
    }
}

/**************************************预配明细结束*******************************************/

/**************************************分配明细开始********************************************/
/**
 * 初始化分配明细Tab
 */
function initWmSoAllocTab() {
    // 隐藏右边tab页
    hideTabRight('#allocDetail_tab-right', '#allocDetail_tab-left');
    // 初始化明细table
    initWmSoAllocTable();
}

/**
 * 初始化明细table
 */
function initWmSoAllocTable() {
    $('#wmSoAllocTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/outbound/banQinWmSoAlloc/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orgId = $('#orgId').val();
            searchParam.soNo = !$('#soNo').val() ? '#' : $('#soNo').val();
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
    buttonControl();
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
    buttonControl();
}

/**
 * 新增分配明细
 */
function addAllocDetail() {
    var rows = getSelections('#wmSoDetailTable');
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
    if ($('#auditStatus').val() === '00') {
        jp.bqError("订单未审核，不能操作");
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
    $('#allocDetail_save').attr('disabled', false);
    // 初始化
    initAddAllocDetail(rows[0]);
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
    $('#allocDetail_packStatus').val('00');
}

/**
 * 保存分配明细
 */
function saveAllocDetail() {
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
            url: "${ctx}/wms/outbound/banQinWmSoAlloc/manualAlloc",
            type: 'POST',
            data: JSON.stringify(row),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    window.location = "${ctx}/wms/outbound/banQinWmSoHeader/form?id=" + $('#id').val();
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
 * 数量同步到EA
 */
function pickSoChange() {
    // 单位换算数量
    var uomQty = !$('#pickUomQty').val() ? 0 : $('#pickUomQty').val();
    // 拣货数
    var qtyUom = !$('#pickQtyUom').val() ? 0 : $('#pickQtyUom').val();
    $('#pickQtyEa').val(Math.floor(qtyUom * 100) / 100 * uomQty);
}

/**
 * 拣货确认
 */
function pickAllocDetail() {
    var rows = [];
    if (!allocDetail_isShowTab) {
        rows = getSelections('#wmSoAllocTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        var detailValidate = bq.detailSubmitCheck('#allocDetailForm');
        if (detailValidate.isSuccess) {
            openDisable("#allocDetailForm");
            var row = {};
            bq.copyProperties(allocDetail_currentRow, row);
            $.extend(row, bq.serializeJson($('#allocDetailForm')));
            row.qtyPkUom = row.qtyUom;
            row.qtyPkEa = row.qtyEa;
            row.qtyUom = allocDetail_currentRow.qtyUom;
            row.qtyEa = allocDetail_currentRow.qtyEa;
            rows.push(row);
        } else {
            jp.bqError(detailValidate.msg);
            return;
        }
    }
    commonAllocDetail('picking', rows);
}

/**
 * 手工拣货确认
 */
var pickModalView;
function manualPickAllocDetail() {
    var row;
    if (!allocDetail_isShowTab) {
        var rows = getSelections('#wmSoAllocTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
        if (rows.length > 1) {
            jp.bqError("只能选择一条记录!");
            return;
        }
        row = rows[0];
        pickModalView = 'list';
        if (row.status !== '40') {
            jp.bqError("不是完全分配状态，不能操作!");
            return;
        }
    } else {
        var obj = {};
        bq.copyProperties(allocDetail_currentRow, obj);
        $.extend(obj, bq.serializeJson($('#allocDetailForm')));
        row = obj;

        pickModalView = 'detail';
    }
    // 打开模态框并赋值
    $('#manualPickModal').modal();
    $('#pickSkuCode').val(row.skuCode);
    $('#pickPackCode').val(row.packCode);
    $('#pickUom').val(row.uom);
    $('#pickUomDesc').val(row.uomDesc);
    $('#pickLoc').val(row.locCode);
    $('#pickQtyUom').val(row.qtyUom);
    $('#pickQtyEa').val(row.qtyEa);
    $('#pickUomQty').val(row.uomQty);
    $('#pickToLoc').val(row.toLoc);
    $('#pickToId').val(row.toId);
    window['pickUomDesc'] = row.uomDesc;
    window['pickLoc'] = row.locCode;
    window['pickToLoc'] = row.toLoc;
}

function manualPickConfirm() {
    var pickUom = $('#pickUom').val();
    if (!pickUom) {
        jp.bqError('包装单位不能为空');
        return;
    }
    var pickLoc = $('#pickLoc').val();
    if (!pickLoc) {
        jp.bqError('拣货库位不能为空');
    }
    var pickQtyUom = $('#pickQtyUom').val();
    if (!pickQtyUom) {
        jp.bqError('拣货库位不能为空');
        return;
    }
    if (pickQtyUom === 0) {
        jp.bqError('拣货数量必须大于0');
        return;
    }
    var pickToLoc = $('#pickToLoc').val();
    if (!pickToLoc) {
        jp.bqError('目标库位不能为空');
        return;
    }
    var pickToId = $('#pickToId').val();
    if (!pickToId) {
        jp.bqError('目标跟踪号不能为空');
        return;
    }
    var selectRow;
    if (pickModalView === 'detail') {
        openDisable("#allocDetailForm");
        var obj = {};
        bq.copyProperties(allocDetail_currentRow, obj);
        $.extend(obj, bq.serializeJson($('#allocDetailForm')));
        selectRow = obj;
        selectRow.qtyUom = allocDetail_currentRow.qtyUom;
        selectRow.qtyEa = allocDetail_currentRow.qtyEa;
    } else {
        selectRow = getSelections('#wmSoAllocTable')[0];
    }
    selectRow.qtyPkUom = pickQtyUom;
    selectRow.qtyPkEa = $('#pickQtyEa').val();
    selectRow.pickLoc = pickLoc;
    selectRow.pickLotNum = $('#pickLotNum').val();
    selectRow.pickTraceId = $('#pickTraceId').val();
    selectRow.pickToLoc = pickToLoc;
    selectRow.pickToId = pickToId;
    jp.loading();
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(selectRow),
        url: "${ctx}/wms/outbound/banQinWmSoAlloc/manualPicking",
        success: function (data) {
            if (data.success) {
                window.location = "${ctx}/wms/outbound/banQinWmSoHeader/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                closeDisable();
                jp.bqError(data.msg);
            }
        }
    });

}

/**
 * 发货确认
 */
function shipmentAllocDetail() {
    var rows = [];
    if (!allocDetail_isShowTab) {
        rows = getSelections('#wmSoAllocTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        rows.push(allocDetail_currentRow);
    }
    commonAllocDetail('shipment', rows);
}

/**
 * 取消分配
 */
function cancelAllocAllocDetail() {
    var rows = [];
    if (!allocDetail_isShowTab) {
        rows = getSelections('#wmSoAllocTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        rows.push(allocDetail_currentRow);
    }
    commonAllocDetail('cancelAlloc', rows);
}

/**
 * 取消拣货
 */
function cancelPickAllocDetail() {
    if (!allocDetail_isShowTab) {
        var rows = getSelections('#wmSoAllocTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    }
    view = "soAlloc";
    initCancelPickWindow();
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
function cancelShipmentAllocDetail() {
    var rows = [];
    if (!allocDetail_isShowTab) {
        rows = getSelections('#wmSoAllocTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        rows.push(allocDetail_currentRow);
    }
    commonAllocDetail('cancelShipment', rows);
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
        url: "${ctx}/wms/outbound/banQinWmSoAlloc/" + method,
        success: function (data) {
            if (data.success) {
                window.location = "${ctx}/wms/outbound/banQinWmSoHeader/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                closeDisable();
                jp.bqError(data.msg);
            }
        }
    });
}

/**
 * 打印面单
 */
function printWayBill() {
    var allocIds = $.map($('#wmSoAllocTable').bootstrapTable('getSelections'), function (row) {return row.allocId});
    if (allocIds.length === 0) {
        jp.warning("请选择记录");
        return;
    }
    jp.loading();
    var params = {allocId: allocIds.join(','), orgId: $('#orgId').val()};
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(params),
        url: "${ctx}/wms/outbound/banQinWmSoAlloc/printWaybill",
        success: function (data) {
            if (data.success) {
                jp.success(data.msg);
                // 后台打印
                bq.printWayBill(data.body.imageList);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

/**************************************分配明细结束*******************************************/
function initWmSoSerialTab() {
    $('#serialTable').bootstrapTable({
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/outbound/banQinWmSoSerial/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.soNo = !$('#soNo').val() ? '#': $('#soNo').val();
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
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'lineNo',
            title: '出库单行号',
            sortable: true
        }, {
            field: 'allocId',
            title: '分配Id',
            sortable: true
        }, {
            field: 'serialNo',
            title: '序列号',
            sortable: true
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
            field: 'lotNum',
            title: '批次号',
            sortable: true
        }]
    });
}

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
            searchParam.orderNo = !$('#soNo').val() ? '#': $('#soNo').val();
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
var view = '';
function createPaTaskCancelAlloc() {
    var rows = getSelections('#cancelAllocTable');
    if (rows.length === 0) {
        jp.bqError("请选择记录");
        return;
    }
    view = 'delAlloc';
    initPaTaskWindow();
}

function initPaTaskWindow() {
    $('#createTaskPaModal').modal();
    $('#isTaskPa').prop('checked', false).prop('disabled', false).val('N');
    $('#allocLoc').prop('checked', true).prop('disabled', true);
    $('#paRuleLoc').prop('checked', false).prop('disabled', true);
}

function createTaskPaConfirm() {
    switch (view) {
        case "soAlloc": createTaskBySo(); break;
        case "delAlloc": createTaskByDel(); break;
    }
}

function createTaskBySo() {
    var allocRows = [];
    if (!allocDetail_isShowTab) {
        allocRows = getSelections('#wmSoAllocTable');
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
        url: "${ctx}/wms/outbound/banQinWmSoAlloc/cancelPick",
        success: function (data) {
            if (data.success) {
                window.location = "${ctx}/wms/outbound/banQinWmSoHeader/form?id=" + $('#id').val();
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
    for (var i = 0; i < rows.length; i++) {
        if (rows[i].status === '60') {
            rows[i].soNo = rows[i].orderNo;
            allocRows.push(rows[i]);
        }
    }
    if (allocRows.length === 0) {
        jp.bqError('请选择一条记录');
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
                window.location = "${ctx}/wms/outbound/banQinWmSoHeader/form?id=" + $('#id').val();
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

function updateConsigneeInfo() {
    if (!$('#id').val()) {
        jp.bqError("单头未保存，不能操作！");
        return;
    }
    $('#updateConsigneeInfoModal').modal();
    $('#consignee_update').val($('#contactName').val());
    $('#consigneeTel_update').val($('#contactTel').val());
    $('#consigneeArea_update').val($('#def17').val());
    $('#consigneeAddress_update').val($('#contactAddr').val());
}

function updateConsigneeInfoConfirm() {
    var consignee = $('#consignee_update').val();
    var consigneeTel = $('#consigneeTel_update').val();
    var consigneeArea = $('#consigneeArea_update').val();
    var consigneeAddress = $('#consigneeAddress_update').val();
    jp.loading();
    var params = {contactName: consignee, contactTel: consigneeTel, contactAddr: consigneeAddress, def17: consigneeArea, id: $('#id').val()};
    jp.post("${ctx}/wms/outbound/banQinWmSoHeader/updateConsigneeInfo", params, function(data) {
        if (data.success) {
            $('#updateConsigneeInfoModal').modal('hide');
            window.location = "${ctx}/wms/outbound/banQinWmSoHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

function updateCarrierInfo() {
    if (!$('#id').val()) {
        jp.bqError("单头未保存，不能操作！");
        return;
    }
    $('#updateCarrierInfoModal').modal();
    $('#carrier_carrierCode').val($('#carrierCode').val());
    $('#carrier_carrierName').val($('#carrierName').val());
}

function updateCarrierInfoConfirm() {
    var carrier = $('#carrier_carrierCode').val();
    jp.loading();
    var params = {carrierCode: carrier, id: $('#id').val()};
    jp.post("${ctx}/wms/outbound/banQinWmSoHeader/updateCarrierInfo", params, function(data) {
        if (data.success) {
            $('#updateCarrierInfoModal').modal('hide');
            window.location = "${ctx}/wms/outbound/banQinWmSoHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**************************************取消分配日志结束*******************************************/

</script>