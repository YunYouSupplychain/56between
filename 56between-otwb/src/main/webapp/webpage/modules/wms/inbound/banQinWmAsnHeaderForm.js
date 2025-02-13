<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var skuDetail_currentRow; // 商品明细当前行
var receiveDetail_currentRow; // 收货明细当前行
var paTask_currentRow; // 上架任务明细当前行
var serial_currentRow; // 序列号明细当前行
var skuDetail_isShowTab = false; // 是否显示右边商品明细tab页
var receiveDetail_isShowTab = false; // 是否显示右边收货明细tab页
var paTask_isShowTab = false; // 是否显示右边上架任务tab页
var serial_isShowTab = false; // 是否显示右边序列号tab页
var currentLoginName = '${fns:getUser().loginName}';

$(document).ready(function () {
    // 初始化商品明细tab
    initWmAsnDetailTab();
    // 初始化收货明细tab
    initWmAsnDetailReceiveTab();
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
    var detailTabIndex = sessionStorage.getItem("ASN_" + currentLoginName +"_detailTab");
    detailTabIndex = !detailTabIndex ? 0 : detailTabIndex;
    // 默认选择加载Tab页
    $("#detailTab li:eq(" + detailTabIndex + ") a").tab('show');
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
        case "receiveDetail":
            receiveDetailClick(row);
            break;
        case "paTask":
            paTaskClick(row);
            break;
        case "serial":
            serialClick(row);
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
        case "receiveDetail":
            receiveDetailDbClick(row);
            break;
        case "paTask":
            paTaskDbClick(row);
            break;
        case "serial":
            serialDbClick(row);
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

/**
 * 获取表格勾选行Ids
 * @param obj 表格Id
 */
function getIdSelections(obj) {
    return $.map($(obj).bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

/**************************************ASN单头开始**********************************************/

/**
 * 保存
 */
function save() {
    var validate = bq.headerSubmitCheck('#inputForm');
    if (validate.isSuccess) {
        jp.loading();
        var disabledObjs = bq.openDisabled('#inputForm');
        var params = $('#inputForm').bq_serialize();
        bq.closeDisabled(disabledObjs);
        jp.post("${ctx}/wms/inbound/banQinWmAsnHeader/save", params, function (data) {
            if (data.success) {
                jp.success(data.msg);
                window.location = "${ctx}/wms/inbound/banQinWmAsnHeader/form?id=" + data.body.entity.id;
            } else {
                jp.bqError(data.msg);
            }
        })
    } else {
        jp.bqError(validate.msg);
    }
}

/**
 * 复制
 */
function duplicate() {
    jp.loading();
    jp.get("${ctx}/wms/inbound/banQinWmAsnHeader/duplicate?ids=" + $('#id').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/inbound/banQinWmAsnHeader/form?id=" + data.body.entity.id;
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
 * 采购成本分摊
 */
function costAlloc() {
    $('#apportionRuleModal').modal();
    $('#apportion_rule').val('');
}

function apportionRuleConfirm() {
    if (!$('#apportion_rule').val()) {
        jp.bqError("请选择分摊策略");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/inbound/banQinWmAsnHeader/costAlloc?ids=" + $('#id').val() + "&strategy=" + $('#apportion_rule').val(), function (data) {
        if (data.success) {
            $('#apportionRuleModal').modal('hide');
            window.location = "${ctx}/wms/inbound/banQinWmAsnHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
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
    jp.loading();
    jp.get("${ctx}/wms/inbound/banQinWmAsnHeader/" + method + "?ids=" + $('#id').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/inbound/banQinWmAsnHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 初始化label
 */
function init() {
    // 初始化表头label
    initLabel();
    // 初始化按钮
    controlHeaderEditor();
}

/**
 * 初始化表头label
 */
function initLabel() {
    if (!$('#id').val()) {
        $('#orgId').val(jp.getCurrentOrg().orgId);
        $('#orderTimeId').val(jp.dateFormat(new Date(), 'yyyy-MM-dd hh:mm:ss'));
    } else {
        $('#asnType').prop('disabled', true);
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
    laydate.render({elem: '#skuDetail_inboundTime', theme: '#393D49', type: 'datetime'});
}

/**
 * 初始化按钮
 */
function controlHeaderEditor() {
    if (!$('#id').val()) {
        controlListButton(true);
        $('#header_save').attr('disabled', false);
    } else {
        var status = $('#status').val();
        var auditStatus = $('#auditStatus').val();
        // 非创建状态，不能保存，审核，取消审核，删除，关闭
        if (status !== '00' ) {
            $('#header_save').attr('disabled', true);
            $('#header_audit').attr('disabled', true);
            $('#header_cancelAudit').attr('disabled', true);
            $('#header_costAlloc').attr('disabled', true);
            $('#header_cancelOrder').attr('disabled', true);
            if (status === '90' || status === '99') {
                $('#header_closeOrder').attr('disabled', true);
                $('#header_createQc').attr('disabled', true);
            }
            if (status === '90') {
                $('#header_createVoucherNo').css({"pointer-events": "none", "color": "#8A8A8A"});
                $('#header_cancelVoucherNo').css({"pointer-events": "none", "color": "#8A8A8A"});
            }
        } else {
            $('#header_closeOrder').attr('disabled', true);
            $('#header_createVoucherNo').css({"pointer-events": "none", "color": "#8A8A8A"});
            $('#header_cancelVoucherNo').css({"pointer-events": "none", "color": "#8A8A8A"});
            // 审核状态
            if (auditStatus === '99') {
                $('#header_save').attr('disabled', true);
                $('#header_audit').attr('disabled', true);
                $('#header_cancelOrder').attr('disabled', true);
            } else {
                $('#header_createQc').attr('disabled', true);
                $('#header_cancelAudit').attr('disabled', true);
                $('#header_costAlloc').attr('disabled', true);
                if (auditStatus === '90') {
                    $('#header_audit').attr('disabled', true);
                    $('#header_createQc').attr('disabled', false);
                }
            }
        }
        // 冻结状态
        if ($('#holdStatus').val() === '99') {
            controlListButton(true);
            $('#header_cancelHold').css({"pointer-events": "auto", "color": "#1E1E1E"});
            $('#header_duplicate').attr('disabled', false);
        } else {
            $('#header_cancelHold').css({"pointer-events": "none", "color": "#8A8A8A"});
        }
    }
    //根据单头的状态控制明细编辑
    controlDetailByHeader();
    controlDetailReceiveEditor();
}

/**
 * 初始化单头按钮
 * @param flag
 */
function controlListButton(flag) {
    $('#header_save').attr('disabled', flag);
    $('#header_duplicate').attr('disabled', flag);
    $('#header_audit').attr('disabled', flag);
    $('#header_cancelAudit').attr('disabled', flag);
    $('#header_costAlloc').attr('disabled', flag);
    $('#header_createQc').attr('disabled', flag);
    $('#header_closeOrder').attr('disabled', flag);
    $('#header_cancelOrder').attr('disabled', flag);
    $('#header_holdOrder').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
    $('#header_cancelHold').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
    $('#header_createVoucherNo').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
    $('#header_cancelVoucherNo').css({"pointer-events": (flag ? "none" : "auto"), "color": (flag ? "#8A8A8A" : "#1E1E1E")});
}

/**
 * 根据单头的状态控制明细编辑
 */
function controlDetailByHeader() {
    controlDetailButton(false);
    if (!$('#id').val() || ($('#status').val() !== '00' || $('#holdStatus').val() === '99')) {
        controlDetailButton(true);
    }
    if ($('#id').val() && $('#auditStatus').val() === '99') {
        $('#skuDetail_add').attr('disabled', true);
        $('#skuDetail_save').attr('disabled', true);
        $('#skuDetail_remove').attr('disabled', true);
        $('#skuDetail_duplicate').attr('disabled', true);
        $('#skuDetail_palletize').attr('disabled', false);
        $('#skuDetail_cancelPalletize').attr('disabled', false);
        $('#skuDetail_createQc').attr('disabled', false);
        $('#skuDetail_cancel').attr('disabled', false);
        $('#skuDetail_apportionWeight').attr('disabled', true);
    }
    if (!skuDetail_isShowTab) {
        $('#skuDetail_save').attr('disabled', true);
    } else {
        controlDetailEditor();
    }
}

/**
 * 明细列表按钮控制
 * @param flag
 */
function controlDetailButton(flag) {
    $('#skuDetail_add').attr('disabled', flag);
    $('#skuDetail_save').attr('disabled', flag);
    $('#skuDetail_remove').attr('disabled', flag);
    $('#skuDetail_duplicate').attr('disabled', flag);
    $('#skuDetail_palletize').attr('disabled', flag);
    $('#skuDetail_cancelPalletize').attr('disabled', flag);
    $('#skuDetail_createQc').attr('disabled', flag);
    $('#skuDetail_cancel').attr('disabled', flag);
    $('#skuDetail_apportionWeight').attr('disabled', flag);
}

/**
 * 控制明细编辑
 */
function controlDetailEditor() {
    // 商品明细
    controlDetailButton(false);
    // 非创建状态，已审核，或已冻结，不能操作
    if (skuDetail_isShowTab) {
        // 非创建状态，已审核，或已冻结，不能操作
        if ($('#id').val() && ($('#status').val() !== '00' || $('#auditStatus').val() === '99' || $('#holdStatus').val() === '99')) {
            $('#skuDetail_add').attr('disabled', true);
            $('#skuDetail_save').attr('disabled', true);
            $('#skuDetail_remove').attr('disabled', true);
            $('#skuDetail_duplicate').attr('disabled', true);
            $('#skuDetail_cancel').attr('disabled', true);
            $('#skuDetail_apportionWeight').attr('disabled', true);
        }
        if (!skuDetail_currentRow || !$('#skuDetail_id').val()) {
            $('#skuDetail_remove').attr('disabled', true);
            $('#skuDetail_duplicate').attr('disabled', true);
            $('#skuDetail_cancel').attr('disabled', true);
            $('#skuDetail_palletize').attr('disabled', true);
            $('#skuDetail_cancelPalletize').attr('disabled', true);
            $('#skuDetail_createQc').attr('disabled', true);
            $('#skuDetail_apportionWeight').attr('disabled', true);
        } else {
            if (skuDetail_currentRow.status !== '00') {
                $('#skuDetail_save').attr('disabled', true);
                $('#skuDetail_remove').attr('disabled', true);
                $('#skuDetail_cancel').attr('disabled', true);
                $('#skuDetail_apportionWeight').attr('disabled', true);
            } else {
                $('#skuDetail_cancel').attr('disabled', false);
                if ($('#holdStatus').val() === '99' || $('#status').val() === '90' || $('#status').val() === '99') {
                    $('#skuDetail_cancel').attr('disabled', true);
                }
            }
            if (skuDetail_currentRow.qcStatus === '20' || skuDetail_currentRow.qcStatus === '10') {
                $('#skuDetail_save').attr('disabled', true);
            }
            if (skuDetail_currentRow.status === '90' || ($('#id').val() && ($('#auditStatus').val() === '00' || $('#status').val() === '90' || $('#status').val() === '99' || $('#holdStatus').val() === '99'))) {
                $('#skuDetail_palletize').attr('disabled', true);
                $('#skuDetail_cancelPalletize').attr('disabled', true);
                $('#skuDetail_createQc').attr('disabled', true);
            }
            if (skuDetail_currentRow.qcStatus === '20') {
                $('#skuDetail_createQc').attr('disabled', true);
            }
            if (skuDetail_currentRow.isPalletize === 'Y') {
                if (skuDetail_currentRow.status === '20') {
                    $('#skuDetail_cancelPalletize').attr('disabled', true);
                }
                $('#skuDetail_palletize').attr('disabled', true);
                $('#skuDetail_save').attr('disabled', true);
                $('#skuDetail_remove').attr('disabled', true);
                $('#skuDetail_apportionWeight').attr('disabled', true);
            } else {
                $('#skuDetail_cancelPalletize').attr('disabled', true);

            }
        }
    } else {
        $('#skuDetail_save').attr('disabled', true);
    }
}

/**
 * 控制收货明细编辑
 */
function controlDetailReceiveEditor() {
    // 收货明细
    controlRcvButton(true);
    controlRcvField(false);
    if ($('#id').val() && $('#auditStatus').val() !== '00' && $('#holdStatus').val() === '00' && $('#status').val() !== '90' && $('#status').val() !== '99') {
        if (receiveDetail_isShowTab) {
            if (receiveDetail_currentRow && $('#receiveDetail_id').val()) {
                if ($('#status').val() !== '90' && $('#status').val() !== '99') {
                    if (receiveDetail_currentRow.status === '20') {
                        $('#receiveDetail_cancelReceive').attr('disabled', false);
                        $('#receiveDetail_createVoucherNo').attr('disabled', false);
                        $('#receiveDetail_cancelVoucherNo').attr('disabled', false);
                    }
                    if (receiveDetail_currentRow.status === '00') {
                        $('#receiveDetail_receiveConfirm').attr('disabled', false);
                        // 如果有计划上架库位，不能安排库位
                        if (!$('#receiveDetail_planPaLoc').val()) {
                            $('#receiveDetail_arrangeLoc').attr('disabled', false);
                        } else {
                            $('#receiveDetail_cancelArrangeLoc').attr('disabled', false);
                        }
                    }
                }
                // 完全收货，编辑区不能编辑
                if (receiveDetail_currentRow.status === '20') {
                    $('#receiveDetail_createTaskPa').attr('disabled', false);
                    // 编辑区字段
                    controlRcvField(true);
                }
            }
        } else {
            controlRcvButton(false);
        }
    }
}

/**
 * 控制收货明细编辑
 */
function controlRcvButton(flag) {
    $('#receiveDetail_arrangeLoc').attr('disabled', flag);
    $('#receiveDetail_cancelArrangeLoc').attr('disabled', flag);
    $('#receiveDetail_receiveConfirm').attr('disabled', flag);
    $('#receiveDetail_cancelReceive').attr('disabled', flag);
    $('#receiveDetail_createTaskPa').attr('disabled', flag);
    $('#receiveDetail_createVoucherNo').attr('disabled', flag);
    $('#receiveDetail_cancelVoucherNo').attr('disabled', flag);
}

/**
 * 控制收货明细编辑
 */
function controlRcvField(flag) {
    $('#receiveDetailUomSelect').prop('disabled', flag);
    $('#receiveDetailUomDelete').prop('disabled', flag);
    $('#receiveDetail_currentQtyRcvUom').prop('readonly', flag);
    $('#receiveDetailToLocSelect').prop('disabled', flag);
    $('#receiveDetailToLocDelete').prop('disabled', flag);
    $('#receiveDetail_rcvTime').prop('readonly', flag);
    $('#receiveDetail_price').prop('readonly', flag);
}

/**
 * 控制上架任务编辑
 */
function controlTaskPaEditor() {
    // 上架任务
    controlTaskPaButton(false);
    if (!$('#id').val() || $('#holdStatus').val() === '90' || $('#status').val() === '00' || $('#status').val() === '90' || $('#auditStatus').val() === '00') {
        controlTaskPaButton(true);
    }
    if (paTask_isShowTab && (paTask_currentRow && paTask_currentRow.status === '99')) {
        controlTaskPaButton(true); 
    }
}

/**
 * 控制上架任务按钮
 */
function controlTaskPaButton(flag) {
    $('#paTask_remove').attr('disabled', flag);
    $('#paTask_confirm').attr('disabled', flag);
}

/**************************************ASN单头结束********************************************/
/**************************************弹出框开始********************************************/

function afterSelectSku(row) {
    loadLotAtt({ownerCode: $('#ownerCode').val(), skuCode: row.skuCode, orgId: row.orgId, isQc: row.isQc});
    // 加载质检项信息
    loadSkuQcInfo(row);
}

function afterSelectDetailPack(row) {
    $('#skuDetail_uomQty').val(row.cdprQuantity);
    calc();
}

function afterSelectReceivePack(row) {
    $('#receiveDetail_uomQty').val(row.cdprQuantity);
    calRcv();
}

function loadSkuQcInfo(row) {
    $('#skuDetail_isQc').prop('checked', row.isQc === 'Y').val(row.isQc);
    $('#skuDetail_qcPhase').val(row.qcPhase);
    $('#skuDetail_qcRule').val(row.qcRule);
    $('#skuDetail_qcRuleName').val(row.qcRuleName);
    $('#skuDetail_itemGroupCode').val(row.itemGroupCode);
    $('#skuDetail_itemGroupName').val(row.itemGroupName);
}

/**
 * 单位 数量改变后 计算EA数
 */
function calc() {
    var num = !$('#skuDetail_uomQty').val() ? 0 : $('#skuDetail_uomQty').val();
    var qtyAsnEa = !$('#skuDetail_qtyAsnEa').val() ? 0 : $('#skuDetail_qtyAsnEa').val();
    var qtyRcvEa = !$('#skuDetail_qtyRcvEa').val() ? 0 : $('#skuDetail_qtyRcvEa').val();
    if (num > 0) {
        $('#skuDetail_qtyAsnUom').val(qtyAsnEa / num);
        $('#skuDetail_qtyRcvUom').val(qtyRcvEa / num);
    } else {
        $('#skuDetail_qtyAsnUom').val('');
        $('#skuDetail_qtyRcvUom').val('');
    }
}

/**
 * 单位 数量改变后 计算EA数
 */
function calRcv() {
    var num = !$('#receiveDetail_uomQty').val() ? 0 : $('#receiveDetail_uomQty').val();
    var qtyRcvEa = !$('#receiveDetail_currentQtyRcvEa').val() ? 0 : $('#receiveDetail_currentQtyRcvEa').val();
    var qtyPlanEa = !$('#receiveDetail_qtyPlanEa').val() ? 0 : $('#receiveDetail_qtyPlanEa').val();
    if (num !== 0) {
        $('#receiveDetail_currentQtyRcvUom').val(qtyRcvEa / num);
        $('#receiveDetail_qtyPlanUom').val(qtyPlanEa / num);
    } else {
        $('#receiveDetail_currentQtyRcvUom').val('');
        $('#receiveDetail_qtyPlanUom').val('');
    }
}

/**************************************弹出框结束********************************************/
/**************************************商品明细开始********************************************/
/**
 * 初始化明细table
 */
function initWmAsnDetailTab() {
    $('#wmAsnDetailTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inbound/banQinWmAsnDetail/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.headId = !$('#id').val() ? "#" : $('#id').val();
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
        },
        {
            field: 'lineNo',
            title: '行号',
            sortable: true
        },
        {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ASN_STATUS'))}, value, "-");
            }
        },
        {
            field: 'isPalletize',
            title: '码盘标识',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        },
        {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        },
        {
            field: 'skuName',
            title: '商品名称',
            sortable: true
        },
        {
            field: 'planToLoc',
            title: '计划收货库位',
            sortable: true
        },
        {
            field: 'traceId',
            title: '计划跟踪号',
            sortable: true
        },
        {
            field: 'qtyAsnUom',
            title: '预收数',
            sortable: true
        },
        {
            field: 'qtyAsnEa',
            title: '预收数EA',
            sortable: true
        },
        {
            field: 'qtyRcvUom',
            title: '已收数',
            sortable: true
        },
        {
            field: 'qtyRcvEa',
            title: '已收数EA',
            sortable: true
        },
        {
            field: 'packCode',
            title: '包装规格',
            sortable: true
        },
        {
            field: 'uom',
            title: '包装单位',
            sortable: true
        },
        {
            field: 'reserveCode',
            title: '上架库位指定规则',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_RESERVE_CODE'))}, value, "-");
            }
        },
        {
            field: 'paRule',
            title: '上架规则',
            sortable: true
        },
        {
            field: 'price',
            title: '单价',
            sortable: true
        },
        {
            field: 'logisticLineNo',
            title: '物流单行号',
            sortable: true
        },
        {
            field: 'poNo',
            title: '采购单号',
            sortable: true
        },
        {
            field: 'poLineNo',
            title: '采购单行号',
            sortable: true
        },
        {
            field: 'isQc',
            title: '是否质检管理',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        },
        {
            field: 'qcStatus',
            title: '质检状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_STATUS'))}, value, "-");
            }
        },
        {
            field: 'qcPhase',
            title: '质检阶段',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_PHASE'))}, value, "-");
            }
        },
        {
            field: 'qcRule',
            title: '质检规则',
            sortable: true
        },
        {
            field: 'itemGroupCode',
            title: '质检项',
            sortable: true
        },
        {
            field: 'lotAtt01',
            title: '生产日期',
            sortable: true
        },
        {
            field: 'lotAtt02',
            title: '失效日期',
            sortable: true
        },
        {
            field: 'lotAtt03',
            title: '入库日期',
            sortable: true
        },
        {
            field: 'lotAtt04',
            title: '批次属性4',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_ATT'))}, value, "-");
            }
        },
        {
            field: 'lotAtt05',
            title: '批次属性5',
            sortable: true
        },
        {
            field: 'lotAtt06',
            title: '批次属性6',
            sortable: true
        },
        {
            field: 'lotAtt07',
            title: '批次属性7',
            sortable: true
        },
        {
            field: 'lotAtt08',
            title: '批次属性8',
            sortable: true
        },
        {
            field: 'lotAtt09',
            title: '批次属性9',
            sortable: true
        },
        {
            field: 'lotAtt10',
            title: '批次属性10',
            sortable: true
        },
        {
            field: 'lotAtt11',
            title: '批次属性11',
            sortable: true
        },
        {
            field: 'lotAtt12',
            title: '批次属性12',
            sortable: true
        }]
    });
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
        controlDetailEditor();
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
        $('#skuDetail_isPalletize').prop('disabled', true).prop('checked', skuDetail_currentRow.isPalletize === 'Y');
        // 质检控制
        $('#skuDetail_isQc').prop('checked', skuDetail_currentRow.isQc === 'Y');
        isQcControl(skuDetail_currentRow.isQc === 'Y');
        // 按钮控制
        controlDetailEditor();
    } else {
        // 隐藏右边tab
        hideTabRight('#skuDetail_tab-right', '#skuDetail_tab-left');
        skuDetail_isShowTab = false;
        // 按钮控制
        controlDetailByHeader();
    }
}

/**
 * 加载批次属性控件
 */
function loadLotAtt(row) {
    var params = "ownerCode=" + encodeURIComponent(row.ownerCode) + "&skuCode=" + encodeURIComponent(row.skuCode) + "&orgId=" + row.orgId;
    bq.get("${ctx}/wms/basicdata/banQinCdWhLotDetail/getInfo?" + params, false, function (data) {
        if (data) {
            if (row.isQc === 'Y') {
                data[3].inputControl = 'R';
            }
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
    controlDetailEditor();
}

/**
 * 初始化明细
 */
function initAddSkuDetail() {
    $('#skuDetail_status').prop('disabled', true);
    $("#skuDetail_status option:first").prop("selected", true);
    $('#skuDetail_isPalletize').prop('disabled', true).val('N');
    $('#skuDetail_qtyAsnEa').val('0');
    $('#skuDetail_qtyRcv').val('0');
    $('#skuDetail_qtyRcvEa').val('0');
}

/**
 * 预收数输入同步到EA
 */
function qtyAsnChange() {
    var num = !$('#skuDetail_uomQty').val() ? 0 : $('#skuDetail_uomQty').val();
    var qtyAsnUom = !$('#skuDetail_qtyAsnUom').val() ? 0 : $('#skuDetail_qtyAsnUom').val();
    if (num !== 0) {
        $('#skuDetail_qtyAsnEa').val(Math.floor(qtyAsnUom * 100) / 100 * num);
    } else {
        $('#skuDetail_qtyAsnEa').val('');
    }
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
    if (parseFloat($('#skuDetail_qtyAsnUom').val()) <= 0) {
        jp.bqError("预收数必须大于0");
        return;
    }
    // 表单验证
    var validate = bq.detailSubmitCheck('#skuDetail_tab-right');
    if (validate.isSuccess) {
        // 保存前赋值
        beforeSave();
        jp.loading("正在保存中...");
        var disabledObjs = bq.openDisabled("#skuDetailForm");
        var row = {};
        bq.copyProperties(skuDetail_currentRow, row);
        $.extend(row, bq.serializeJson($('#skuDetailForm')));
        row.inboundTime = row.inboundTime ? row.inboundTime : jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss");
        bq.closeDisabled(disabledObjs);
        $.ajax({
            url: "${ctx}/wms/inbound/banQinWmAsnDetail/save",
            type: 'POST',
            data: JSON.stringify(row),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    // tab页刷新
                    tabRefresh();
                    // 按钮控制
                    controlDetailEditor();
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            }
        });
    } else {
        jp.bqError(validate.msg);
    }
}

/**
 * tab页刷新
 */
function tabRefresh() {
    // 商品明细tab
    $('#wmAsnDetailTable').bootstrapTable('refresh');
    hideTabRight('#skuDetail_tab-right', '#skuDetail_tab-left');
    skuDetail_isShowTab = false;
    // 收货明细tab
    $('#wmAsnDetailReceiveTable').bootstrapTable('refresh');
    hideTabRight('#receiveDetail_tab-right', '#receiveDetail_tab-left');
    receiveDetail_isShowTab = false;
}

/**
 * 保存前赋值
 */
function beforeSave() {
    if (!$('#skuDetail_id').val()) {
        $('#skuDetail_orgId').val($('#orgId').val());
        $('#skuDetail_asnNo').val($('#asnNo').val());
        $('#skuDetail_ownerCode').val($('#ownerCode').val());
        $('#skuDetail_headId').val($('#id').val());
        $('#skuDetail_logisticNo').val($('#logisticNo').val());
    }
}

/**
 * 删除明细
 */
function removeSkuDetail() {
    var rows = getSelections('#wmAsnDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    var lineNoArray = [];
    for (var x in rows) {
        lineNoArray.push(rows[x].lineNo);
    }
    jp.confirm('确认要删除吗？删除将不能恢复？', function () {
        var params = "headId=" + $('#id').val();
        params += "&lineNo=" + lineNoArray.join(',');
        jp.loading("正在删除中...");
        skuDetailCommonMethod('deleteAll', params);
    })
}

/**
 * 复制明细
 */
function duplicateSkuDetail() {
    var rows = getSelections('#wmAsnDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    if (rows.length > 1) {
        jp.bqError("只能勾选一条记录!");
        return;
    }
    var params = "asnNo=" + $('#asnNo').val();
    params += "&lineNo=" + rows[0].lineNo;
    params += "&orgId=" + $('#orgId').val();
    jp.loading();
    skuDetailCommonMethod('duplicate', params);
}

/**
 * 码盘
 */
function palletizeSkuDetail() {
    var rows = getSelections('#wmAsnDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    var lineNoArray = [];
    for (var x in rows) {
        lineNoArray.push(rows[x].lineNo);
    }
    var params = "headId=" + $('#id').val();
    params += "&lineNo=" + lineNoArray.join(',');

    jp.loading("码盘中...");
    skuDetailCommonMethod('createPalletize', params);
}

/**
 * 取消码盘
 */
function cancelPalletizeSkuDetail() {
    var rows = getSelections('#wmAsnDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    var lineNoArray = [];
    for (var x in rows) {
        lineNoArray.push(rows[x].lineNo);
    }
    
    var params = "headId=" + $('#id').val();
    params += "&lineNo=" + lineNoArray.join(',');
    jp.loading("取消码盘中...");
    skuDetailCommonMethod('cancelPalletize', params);
}

/**
 * 生成质检单
 */
function createQcSkuDetail() {
    var rows = getSelections('#wmAsnDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    var lineNoArray = [];
    for (var x in rows) {
        lineNoArray.push(rows[x].lineNo);
    }
    
    var params = "asnNo=" + $('#asnNo').val();
    params += "&lineNo=" + lineNoArray.join(',');
    params += "&orgId=" + $('#orgId').val();
    jp.loading("生成质检单中...");
    skuDetailCommonMethod('createQc', params);
}

/**
 * 取消订单行
 */
function cancelSkuDetail() {
    var rows = getSelections('#wmAsnDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    var lineNoArray = [];
    for (var x in rows) {
        lineNoArray.push(rows[x].lineNo);
    }

    var params = "headId=" + $('#id').val();
    params += "&lineNo=" + lineNoArray.join(',');
    jp.loading("取消订单行中...");
    skuDetailCommonMethod('cancelAsnDetail', params);
}

/**
 * 分摊重量
 */
function apportionWeightSkuDetail() {
    var rows = getSelections('#wmAsnDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    var lineNoArray = [];
    for (var x in rows) {
        lineNoArray.push(rows[x].lineNo);
    }

    var params = "headId=" + $('#id').val();
    params += "&lineNo=" + lineNoArray.join(',');
    jp.loading("分摊重量中...");
    skuDetailCommonMethod('apportionWeight', params);
}

/**
 * 商品明细公共方法
 * @param method
 * @param params
 */
function skuDetailCommonMethod(method, params) {
    jp.get("${ctx}/wms/inbound/banQinWmAsnDetail/" + method + "?" + params, function (data) {
        if (data.success) {
            // tab页刷新
            tabRefresh();
            // 按钮控制
            controlDetailEditor();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 明细Tab页索引切换
 */
function detailTabChange(index) {
    sessionStorage.setItem("ASN_" + currentLoginName + "_detailTab", index);
}

/**
 * 是否质检
 * @param flag
 */
function isQcChange(flag) {
    $('#skuDetail_isQc').val(flag ? 'Y' : 'N');
    isQcControl(flag);
    // 控制品质批次属性控件
}

/**
 * 是否质检管理
 */
function isQcControl(flag) {
    if (flag) {
        $('#skuDetail_qcPhase').addClass('required');
        $('#skuDetail_qcRuleName').addClass('required');
        $('#skuDetail_itemGroupName').addClass('required');
        $('#skuDetail_qcPhaseLabel').html(getStartStyle() + $('#skuDetail_qcPhaseLabel').text().replace('*', ''));
        $('#skuDetail_qcRuleLabel').html(getStartStyle() + $('#skuDetail_qcRuleLabel').text().replace('*', ''));
        $('#skuDetail_itemGroupLabel').html(getStartStyle() + $('#skuDetail_itemGroupLabel').text().replace('*', ''));
    } else {
        $('#skuDetail_qcPhase').removeClass('required');
        $('#skuDetail_qcRuleName').removeClass('required');
        $('#skuDetail_itemGroupName').removeClass('required');
        $('.myStart').remove();
    }
}

function getStartStyle() {
    return '<font class="myStart" color="red">*</font>';
}

/**************************************商品明细结束*******************************************/

/**************************************收货明细开始********************************************/
/**
 * 初始化明细table
 */
function initWmAsnDetailReceiveTab() {
    $('#wmAsnDetailReceiveTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inbound/banQinWmAsnDetailReceive/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.headId = !$('#id').val() ? "#" : $('#id').val();
            searchParam.orgId = $('#orgId').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function(row, $el) {
            clickDetail('receiveDetail', row);
            jp.changeTableStyle($el);
        },
        onDblClickRow: function (row, $el) {
            dbClickDetail('receiveDetail', row);
        },
        columns: [{
            checkbox: true
        },
        {
            field: 'lineNo',
            title: '行号',
            sortable: true
        },
        {
            field: 'asnLineNo',
            title: '入库单行号',
            sortable: true
        },
        {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ASN_STATUS'))}, value, "-");
            }
        },
        {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        },
        {
            field: 'skuName',
            title: '商品名称',
            sortable: true
        },
        {
            field: 'toLoc',
            title: '收货库位',
            sortable: true
        },
        {
            field: 'planPaLoc',
            title: '计划上架库位',
            sortable: true
        },
        {
            field: 'toId',
            title: '收货跟踪号',
            sortable: true
        },
        {
            field: 'planId',
            title: '码盘跟踪号',
            sortable: true
        },
        {
            field: 'qtyPlanUom',
            title: '预收数',
            sortable: true
        },
        {
            field: 'qtyPlanEa',
            title: '预收数EA',
            sortable: true
        },
        {
            field: 'qtyRcvUom',
            title: '已收数',
            sortable: true
        },
        {
            field: 'qtyRcvEa',
            title: '已收数EA',
            sortable: true
        },
        {
            field: 'packCode',
            title: '包装规格',
            sortable: true
        },
        {
            field: 'uom',
            title: '包装单位',
            sortable: true
        },
        {
            field: 'reserveCode',
            title: '上架指定规则',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_RESERVE_CODE'))}, value, "-");
            }
        },
        {
            field: 'paRule',
            title: '上架规则',
            sortable: true
        },
        {
            field: 'price',
            title: '单价',
            sortable: true
        },
        {
            field: 'rcvTime',
            title: '收货时间',
            sortable: true
        },
        {
            field: 'logisticLineNo',
            title: '物流单行号',
            sortable: true
        },
        {
            field: 'poNo',
            title: '采购单号',
            sortable: true
        },
        {
            field: 'poLineNo',
            title: '采购单行号',
            sortable: true
        },
        {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        },
        {
            field: 'voucherNo',
            title: '凭证号',
            sortable: true
        },
        {
            field: 'isQc',
            title: '是否质检管理',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        },
        {
            field: 'qcStatus',
            title: '质检状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_STATUS'))}, value, "-");
            }
        },
        {
            field: 'qcPhase',
            title: '质检阶段',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_PHASE'))}, value, "-");
            }
        },
        {
            field: 'qcRule',
            title: '质检规则',
            sortable: true
        },
        {
            field: 'itemGroupCode',
            title: '质检项',
            sortable: true
        },
        {
            field: 'qcRcvId',
            title: '质检收货明细号',
            sortable: true
        },
        {
            field: 'lotAtt01',
            title: '生产日期',
            sortable: true
        },
        {
            field: 'lotAtt02',
            title: '失效日期',
            sortable: true
        },
        {
            field: 'lotAtt03',
            title: '入库日期',
            sortable: true
        },
        {
            field: 'lotAtt04',
            title: '批次属性4',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_ATT'))}, value, "-");
            }
        },
        {
            field: 'lotAtt05',
            title: '批次属性5',
            sortable: true
        },
        {
            field: 'lotAtt06',
            title: '批次属性6',
            sortable: true
        },
        {
            field: 'lotAtt07',
            title: '批次属性7',
            sortable: true
        },
        {
            field: 'lotAtt08',
            title: '批次属性8',
            sortable: true
        },
        {
            field: 'lotAtt09',
            title: '批次属性9',
            sortable: true
        },
        {
            field: 'lotAtt10',
            title: '批次属性10',
            sortable: true
        },
        {
            field: 'lotAtt11',
            title: '批次属性11',
            sortable: true
        },
        {
            field: 'lotAtt12',
            title: '批次属性12',
            sortable: true
        },
        {
            field: 'cdType',
            title: '越库类型',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_CD_TYPE'))}, value, "-");
            }
        },
        {
            field: 'cdRcvId',
            title: '越库收货明细号',
            sortable: true
        },
        {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        },
        {
            field: 'soLineNo',
            title: '出库单行号',
            sortable: true
        }]
    });

    // 初始化
    initWmASnDetailReceive();
}

/**
 * 初始化
 */
function initWmASnDetailReceive() {
    // 日期控件
    $('#receiveDetail_rcvTimeF').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#receiveDetail_lotAtt01F').datetimepicker({format: "YYYY-MM-DD"});
    $('#receiveDetail_lotAtt02F').datetimepicker({format: "YYYY-MM-DD"});
    $('#receiveDetail_lotAtt03F').datetimepicker({format: "YYYY-MM-DD"});
}

/**
 * 初始化控件状态
 */
function initDetailReceiveLabel() {
    $('#receiveDetail_status').prop('disabled', true);
    $('#receiveDetailSkuSelectId').prop('disabled', true);
    $('#receiveDetailSkuDeleteId').prop('disabled', true);
    $('#receiveDetailPackSelectId').prop('disabled', true);
    $('#receiveDetailPackDeleteId').prop('disabled', true);
}

/**
 * 收货明细行单击事件
 * @param tab tab页名称
 * @param row 当前行
 */

function receiveDetailClick(row) {
    receiveDetail_currentRow = row;
    if (receiveDetail_isShowTab) {
        // 表单赋值
        evaluate('receiveDetail', 'receiveDetail_currentRow');
        // 按钮控制
        controlDetailReceiveEditor();
    }
}

/**
 * 收货明细行双击事件
 * @param row 当前行
 */
function receiveDetailDbClick(row) {
    receiveDetail_currentRow = row;
    if (!receiveDetail_isShowTab) {
        // 显示右边tab
        showTabRight('#receiveDetail_tab-right', '#receiveDetail_tab-left');
        receiveDetail_isShowTab = true;
        // 批次屬性控件賦值
        receiveLoadLotAtt(row);
        // 表单赋值
        evaluate('receiveDetail', 'receiveDetail_currentRow');
        // 初始化控件状态
        initDetailReceiveLabel();
    } else {
        // 隐藏右边tab
        hideTabRight('#receiveDetail_tab-right', '#receiveDetail_tab-left');
        receiveDetail_isShowTab = false;
    }
    // 按钮控制
    controlDetailReceiveEditor();
}

/**
 * 加载批次属性控件
 */
function receiveLoadLotAtt(row) {
    var params = "ownerCode=" + encodeURIComponent(row.ownerCode) + "&skuCode=" + encodeURIComponent(row.skuCode) + "&orgId=" + row.orgId;
    bq.get("${ctx}/wms/basicdata/banQinCdWhLotDetail/getInfo?" + params, false, function (data) {
        if (data) {
            if (row.isQc === 'Y') {
                data[3].inputControl = 'R';
            }
            $('#receiveDetailLotAttTab').empty();
            var html = bq.getLotAttTab(data, 'receiveDetail_lotAtt');
            $('#receiveDetailLotAttTab').append(html);
            $('#receiveDetailLotAttTab .detail-date').each(function() {
                laydate.render({
                    elem: '#' + $(this).prop('id'),
                    theme: '#393D49'
                });
            });
        }
    })
}

/**
 * 可收数输入同步到EA
 */
function qtyRCvChange() {
    var num = !$('#receiveDetail_uomQty').val() ? 0 : $('#receiveDetail_uomQty').val();
    var qtyRcvUom = !$('#receiveDetail_currentQtyRcvUom').val() ? 0 : $('#receiveDetail_currentQtyRcvUom').val();
    if (num !== 0) {
        $('#receiveDetail_currentQtyRcvEa').val(num * Math.floor(qtyRcvUom * 100) / 100);
    } else {
        $('#receiveDetail_currentQtyRcvEa').val('');
    }
}

/**
 * 安排库位
 */
function arrangeLocReceiveDetail() {
    var rows = [];
    if (!receiveDetail_isShowTab) {
        rows = getSelections('#wmAsnDetailReceiveTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        // 收货前质检，未质检，不能操作
        if ("QC_PHASE_B_RCV" === receiveDetail_currentRow.qcPhase
            && "QC_FULL_QC" !== receiveDetail_currentRow.qcStatus
            && "Y" === receiveDetail_currentRow.isQc) {
            jp.warning("商品需要质检，不能操作");
            return;
        }
        // 校验批次属性非空
        var validate = bq.detailSubmitCheck('#receiveDetailForm');
        if (!validate.isSuccess) {
            jp.bqError(validate.msg);
            return;
        }
        var disabledObjs = bq.openDisabled("#receiveDetailForm");
        var row = {};
        bq.copyProperties(receiveDetail_currentRow, row);
        $.extend(row, bq.serializeJson($('#receiveDetailForm')));
        rows.push(row);
        bq.closeDisabled(disabledObjs);
    }
    jp.loading("正在安排库位中...");
    receiveDetailCommonMethod('arrangeLoc', rows);
}

/**
 * 取消安排库位
 */
function cancelArrangeLocReceiveDetail() {
    var rows = [];
    if (!receiveDetail_isShowTab) {
        rows = getSelections('#wmAsnDetailReceiveTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        rows.push(receiveDetail_currentRow);
    }
    jp.loading("取消安排库位中...");
    receiveDetailCommonMethod('cancelArrangeLoc', rows);
}

/**
 * 收货确认
 */
function receiveConfirmReceiveDetail() {
    var rows = [];
    if (!receiveDetail_isShowTab) {
        rows = getSelections('#wmAsnDetailReceiveTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        // 非空校验
        var validate = bq.detailSubmitCheck('#receiveDetail_tab-right');
        if(!validate.isSuccess) {
            jp.bqError(validate.msg);
            return;
        }
        // 收货前质检，未质检，不能操作
        if ("QC_PHASE_B_RCV" === receiveDetail_currentRow.qcPhase
            && "QC_FULL_QC" !== receiveDetail_currentRow.qcStatus
            && "Y" === receiveDetail_currentRow.isQc) {
            jp.warning("商品需要质检，不能操作");
            return;
        }
        // 现收数必须大于0
        if (parseFloat($('#receiveDetail_currentQtyRcvUom').val()) <= 0) {
            jp.warning("现收数必须大于0!");
            return;
        }
        // 校验批次属性非空
        var disabledObjs = bq.openDisabled("#receiveDetailForm");
        var row = {};
        bq.copyProperties(receiveDetail_currentRow, row);
        $.extend(row, bq.serializeJson($('#receiveDetailForm')));
        rows.push(row);
        bq.closeDisabled(disabledObjs);
    }
    jp.loading("收货确认中...");
    receiveDetailCommonMethod('receiving', rows);
}

/**
 * 取消收货
 */
function cancelReceiveReceiveDetail() {
    var rows = [];
    if (!receiveDetail_isShowTab) {
        rows = getSelections('#wmAsnDetailReceiveTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        rows.push(receiveDetail_currentRow);
    }
    jp.loading("取消收货中...");
    receiveDetailCommonMethod('cancelReceiving', rows);
}

/**
 * 生成上架任务
 */
function createTaskPaReceiveDetail() {
    var rows = [];
    if (!receiveDetail_isShowTab) {
        rows = getSelections('#wmAsnDetailReceiveTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        // 非空校验
        var validate = bq.detailSubmitCheck('#receiveDetail_tab-right');
        if(!validate.isSuccess) {
            jp.bqError(validate.msg);
            return;
        }
        // 校验批次属性非空
        var disabledObjs = bq.openDisabled("#receiveDetailForm");
        var row = {};
        bq.copyProperties(receiveDetail_currentRow, row);
        $.extend(row, bq.serializeJson($('#receiveDetailForm')));
        rows.push(row);
        bq.closeDisabled(disabledObjs);
    }
    jp.loading("生成上架任务中...");
    receiveDetailCommonMethod('createTaskPa', rows);
}

/**
 * 生成凭证号
 */
function createVoucherNoReceiveDetail() {
    var rows = getSelections('#wmAsnDetailReceiveTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    var lineNoArray = [];
    for (var x in rows) {
        lineNoArray.push(rows[x].lineNo);
    }
    var params = "headId=" + $('#id').val();
    params += "&lineNo=" + lineNoArray.join(',');
    jp.loading("生成凭证号中...");
    jp.get("${ctx}/wms/inbound/banQinWmAsnDetailReceive/createVoucherNo?" + params, function (data) {
        if (data.success) {
            // tab页刷新
            tabRefresh();
            // 按钮控制
            controlDetailReceiveEditor();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 取消凭证号
 */
function cancelVoucherNoReceiveDetail() {
    var rows = getSelections('#wmAsnDetailReceiveTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    var lineNoArray = [];
    for (var x in rows) {
        lineNoArray.push(rows[x].lineNo);
    }
    var params = "headId=" + $('#id').val();
    params += "&lineNo=" + lineNoArray.join(',');
    jp.loading("取消凭证号中...");
    jp.get("${ctx}/wms/inbound/banQinWmAsnDetailReceive/cancelVoucherNo?" + params, function (data) {
        if (data.success) {
            // tab页刷新
            tabRefresh();
            // 按钮控制
            controlDetailReceiveEditor();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 打印托盘标签
 */
function printTraceLabelReceiveDetail() {
    var rowIds = getIdSelections('#wmAsnDetailReceiveTable');
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/inbound/banQinWmAsnDetailReceive/printTraceLabel", 'ids', rowIds.join(','), '打印托盘标签');
}

/**
 * 打印托盘标签（二维码）
 */
function printTraceLabelQrCodeReceiveDetail() {
    var rowIds = getIdSelections('#wmAsnDetailReceiveTable');
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/inbound/banQinWmAsnDetailReceive/printTraceLabelQrCode", 'ids', rowIds.join(','), '打印托盘标签');
}

/**
 * 收货明细公共方法
 * @param method
 * @param params
 */
function receiveDetailCommonMethod(method, params) {
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(params),
        url: "${ctx}/wms/inbound/banQinWmAsnDetailReceive/" + method,
        success: function (data) {
            if (data.success) {
                // 页面重载
                window.location = "${ctx}/wms/inbound/banQinWmAsnHeader/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

/**************************************收货明细结束*******************************************/

/**************************************上架任务开始********************************************/
/**
 * 初始化上架任务Tab
 */
function initWmAsnPaTaskTab() {
    // 隐藏右边tab页
    hideTabRight('#paTask_tab-right', '#paTask_tab-left');
    // 销毁之前表格
    $("#wmAsnPaTaskTable").bootstrapTable('destroy');
    // 初始化明细table
    initWmAsnPaTaskTable();
    // 控制按钮
    controlTaskPaEditor();
}

/**
 * 初始化明细table
 */
function initWmAsnPaTaskTable() {
    $('#wmAsnPaTaskTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/task/banQinWmTaskPa/grid",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orderNo = !$('#asnNo').val() ? '#' : $('#asnNo').val();
            searchParam.orderType = 'ASN';
            searchParam.orgId = $('#orgId').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function(row, $el) {
            clickDetail('paTask', row);
            jp.changeTableStyle($el);
        },
        onDblClickRow: function (row, $el) {
            dbClickDetail('paTask', row);
        },
        columns: [{
            checkbox: true
        },
        {
            field: 'paId',
            title: '上架任务Id',
            sortable: true
        },
        {
            field: 'lineNo',
            title: '上架任务行号',
            sortable: true
        },
        {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_TASK_STATUS'))}, value, "-");
            }
        },
        {
            field: 'orderType',
            title: '单据类型',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ORDER_TYPE'))}, value, "-");
            }
        },
        {
            field: 'orderNo',
            title: '单据号',
            sortable: true
        },
        {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        },
        {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        },
        {
            field: 'skuName',
            title: '商品名称',
            sortable: true
        },
        {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        },
        {
            field: 'fmLoc',
            title: '源库位',
            sortable: true
        },
        {
            field: 'fmId',
            title: '源跟踪号',
            sortable: true
        },
        {
            field: 'suggestLoc',
            title: '推荐库位',
            sortable: true
        },
        {
            field: 'packCode',
            title: '包装规格',
            sortable: true
        },
        {
            field: 'uom',
            title: '包装单位',
            sortable: true
        },
        {
            field: 'qtyPaUom',
            title: '上架数',
            sortable: true
        },
        {
            field: 'qtyPaEa',
            title: '上架数EA',
            sortable: true
        },
        {
            field: 'toLoc',
            title: '目标库位',
            sortable: true
        },
        {
            field: 'toId',
            title: '目标跟踪号',
            sortable: true
        },
        {
            field: 'paOp',
            title: '上架人',
            sortable: true
        },
        {
            field: 'paTime',
            title: '上架时间',
            sortable: true
        },
        {
            field: 'reserveCode',
            title: '上架库位指定规则',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_RESERVE_CODE'))}, value, "-");
            }
        },
        {
            field: 'paRule',
            title: '上架规则',
            sortable: true
        }]
    });
}

/**
 * 上架任务明细行单击事件
 * @param tab tab页名称
 * @param row 当前行
 */
function paTaskClick(row) {
    paTask_currentRow = row;
    if (paTask_isShowTab) {
        // 表单赋值
        evaluate('paTask', 'paTask_currentRow');
    }
}

/**
 * 上架任务明细行双击事件
 * @param row 当前行
 */
function paTaskDbClick(row) {
    paTask_currentRow = row;
    if (!paTask_isShowTab) {
        // 显示右边tab
        showTabRight('#paTask_tab-right', '#paTask_tab-left');
        paTask_isShowTab = true;
        // 表单赋值
        evaluate('paTask', 'paTask_currentRow');
    } else {
        // 隐藏右边tab
        hideTabRight('#paTask_tab-right', '#paTask_tab-left');
        paTask_isShowTab = false;
    }
}

/**
 * 删除上架任务
 */
function removePaTask() {
    var rows = [];
    if (!paTask_isShowTab) {
        rows = getSelections('#wmAsnPaTaskTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        // 校验批次属性非空
        var disabledObjs = bq.openDisabled("#paTaskForm");
        var row = {};
        bq.copyProperties(paTask_currentRow, row);
        $.extend(row, bq.serializeJson($('#paTaskForm')));
        rows.push(row);
        bq.closeDisabled(disabledObjs);
    }
    jp.confirm('确认要删除该上架任务记录吗？', function () {
        jp.loading();
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: 'POST',
            dataType: "json",
            data: JSON.stringify(rows),
            url: "${ctx}/wms/task/banQinWmTaskPa/deleteAll",
            success: function (data) {
                if (data.success) {
                    // 上架任务tab
                    $('#wmAsnPaTaskTable').bootstrapTable('refresh');
                    hideTabRight('#paTask_tab-right', '#paTask_tab-left');
                    paTask_isShowTab = false;
                    // 收货明细tab
                    $('#wmAsnDetailReceiveTable').bootstrapTable('refresh');
                    hideTabRight('#receiveDetail_tab-right', '#receiveDetail_tab-left');
                    receiveDetail_isShowTab = false;
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            }
        });
    })
}

/**
 * 上架确认
 */
function confirmPaTask() {
    var rows = [];
    if (!paTask_isShowTab) {
        rows = getSelections('#wmAsnPaTaskTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        // 非空校验
        var validate = bq.detailSubmitCheck('#paTask_tab-right');
        if(!validate.isSuccess) {
            jp.bqError(validate.msg);
            return;
        }
        var disabledObjs = bq.openDisabled("#paTaskForm");
        var row = {};
        bq.copyProperties(paTask_currentRow, row);
        $.extend(row, bq.serializeJson($('#paTaskForm')));
        rows.push(row);
        bq.closeDisabled(disabledObjs);
    }
    jp.loading("上架确认中...");
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(rows),
        url: "${ctx}/wms/task/banQinWmTaskPa/putAway",
        success: function (data) {
            if (data.success) {
                // 上架任务tab
                $('#wmAsnPaTaskTable').bootstrapTable('refresh');
                hideTabRight('#paTask_tab-right', '#paTask_tab-left');
                paTask_isShowTab = false;
                // 收货明细tab
                $('#wmAsnDetailReceiveTable').bootstrapTable('refresh');
                hideTabRight('#receiveDetail_tab-right', '#receiveDetail_tab-left');
                receiveDetail_isShowTab = false;
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

/**
 * 打印上架任务单
 */
function printPaTask() {
    var rowIds = getIdSelections('#wmAsnPaTaskTable');
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/task/banQinWmTaskPa/printPaTask", 'ids', rowIds.join(','), '打印上架任务单');
}

function qtyTaskChange() {
    var num = !$('#paTask_uomQty').val() ? 1 : $('#paTask_uomQty').val();
    var qtyTask = !$('#paTask_currentPaQtyEa').val() ? 0 : $('#paTask_currentPaQtyEa').val();
    if (num !== 0) {
        $('#paTask_qtyPaUom').val(qtyTask / num);
    } else {
        $('#paTask_qtyPaUom').val('');
    }
}

/**************************************上架任务结束*******************************************/
/**************************************序列号开始*******************************************/
/**
 * 初始化序列号Tab
 */
function initWmAsnSerialTab() {
    // 隐藏右边tab页
    hideTabRight('#serial_tab-right', '#serial_tab-left');
    // 销毁之前表格
    $("#wmAsnSerialTable").bootstrapTable('destroy');
    // 初始化明细table
    initWmAsnSerialTable();
}

/**
 * 初始化明细table
 */
function initWmAsnSerialTable() {
    $('#wmAsnSerialTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inbound/banQinWmAsnSerial/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.headId = !$('#id').val() ? "#" : $('#id').val();
            searchParam.orgId = $('#orgId').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function(row, $el) {
            clickDetail('serial', row);
            jp.changeTableStyle($el);
        },
        onDblClickRow: function (row, $el) {
            dbClickDetail('serial', row);
        },
        columns: [{
            checkbox: true
        },
        {
            field: 'asnNo',
            title: '入库单号',
            sortable: true
        },
        {
            field: 'rcvLineNo',
            title: '收货明细行号',
            sortable: true
        },
        {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        },
        {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        },
        {
            field: 'skuName',
            title: '商品名称',
            sortable: true
        },
        {
            field: 'serialNo',
            title: '序列号',
            sortable: true
        },
        {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ASN_SERIAL_STATUS'))}, value, "-");
            }
        },
        {
            field: 'scanOp',
            title: '扫描人',
            sortable: true
        },
        {
            field: 'scanTime',
            title: '扫描时间',
            sortable: true
        },
        {
            field: 'dataSource',
            title: '数据来源',
            sortable: true
        }]
    });
}

/**
 * 序列号行单击事件
 * @param tab tab页名称
 * @param row 当前行
 */
function serialClick(row) {
    serial_currentRow = row;
    if (serial_isShowTab) {
        // 表单赋值
        evaluate('serial', 'serial_currentRow');
    }
}

/**
 * 序列号明细行双击事件
 * @param row 当前行
 */
function serialDbClick(row) {
    serial_currentRow = row;
    if (!serial_isShowTab) {
        // 显示右边tab
        showTabRight('#serial_tab-right', '#serial_tab-left');
        serial_isShowTab = true;
        // 表单赋值
        evaluate('serial', 'serial_currentRow');
    } else {
        // 隐藏右边tab
        hideTabRight('#serial_tab-right', '#serial_tab-left');
        serial_isShowTab = false;
    }
    $('#wmAsnSerialTable').bootstrapTable('resetView');
}

/**
 * 新增
 */
function addSerial() {
    // 显示右边Tab
    showTabRight('#serial_tab-right', '#serial_tab-left');
    serial_isShowTab = true;
    serial_currentRow = null;
    // 清空表单
    $(':input', '#serialForm').val('');
    // 初始化
    initAddSerial();
}

function initAddSerial() {
    $('#serial_orgId').val($('#orgId').val());
    $('#serial_headId').val($('#id').val());
    $('#serial_ownerCode').val($('#ownerCode').val());
    $('#serial_asnNo').val($('#asnNo').val());
    $('#serial_status').val('30');
}

/**
 * 保存
 */
function saveSerial() {
    if (!serial_isShowTab) {
        jp.warning("当前无保存记录!");
        return;
    }
    if (!$('#id').val()) {
        jp.warning("请先保存订单头!");
        return;
    }
    // 表单验证
    var validate = bq.detailSubmitCheck('#serial_tab-right');
    if (validate.isSuccess) {
        jp.loading("正在保存中...");
        var disabledObjs = bq.openDisabled("#serialForm");
        var row = {};
        bq.copyProperties(serial_currentRow, row);
        $.extend(row, bq.serializeJson($('#serialForm')));
        bq.closeDisabled(disabledObjs);
        $.ajax({
            url: "${ctx}/wms/inbound/banQinWmAsnSerial/save",
            type: 'POST',
            data: JSON.stringify(row),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    $('#wmAsnSerialTable').bootstrapTable('refresh');
                    hideTabRight('#serial_tab-right', '#serial_tab-left');
                    serial_isShowTab = false;
                    // 按钮控制
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            }
        });
    } else {
        jp.bqError(validate.msg);
    }
}

/**
 * 删除
 */
function removeSerial() {
    var ids = $.map($('#wmAsnSerialTable').bootstrapTable('getSelections'), function (row) {return row.id});
    if (ids.length === 0) {
        jp.bqError("请选择记录");
        return;
    }
    jp.confirm('确认要删除该序列号记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/inbound/banQinWmAsnSerial/deleteAll?ids=" + ids, function (data) {
            if (data.success) {
                $('#wmAsnSerialTable').bootstrapTable('refresh');
                hideTabRight('#serial_tab-right', '#serial_tab-left');
                serial_isShowTab = false;
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

function downloadTemplate() {
    window.location = '${ctx}/wms/inbound/banQinWmAsnSerial/import/template';
}

function importSerial() {
    if (!$('#id').val()) {
        jp.bqError("请先保存单头");
        return;
    }

    $("#uploadModal").modal();
    $("#uploadFileName").val('');
}

function uploadFile() {
    if (!$("#uploadFileName").val()) {
        jp.alert("请选择需要上传的文件");
        return;
    }
    var file = $("#uploadFileName").get(0).files[0];
    var fm = new FormData();
    fm.append('file', file);
    fm.append('orgId', $('#orgId').val());
    fm.append('ownerCode', $('#ownerCode').val());
    fm.append('asnNo', $('#asnNo').val());
    $.ajax({
        type: "post",
        url: "${ctx}/wms/inbound/banQinWmAsnSerial/import",
        data: fm,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        // 传送请求数据
        success: function (data) {
            $("#uploadModal").modal('hide');
            $('#wmAsnSerialTable').bootstrapTable('refresh');
            jp.alert(data.msg);
        },
        error: function (data) {
            $("#uploadModal").modal('hide');
            jp.alert(data.msg);
        }
    });
}

/**************************************序列号结束*******************************************/

</script>