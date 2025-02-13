<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var skuDetail_currentRow; // 商品明细当前行
var skuDetail_isShowTab = false; // 是否显示右边商品明细tab页

$(document).ready(function () {
    // 隐藏右边div
    hideTabRight('#skuDetail_tab-right', '#skuDetail_tab-left');
    // 初始化商品明细tab
    initWmSaleDetailTab();
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
 * 明细表单验证
 * @returns {boolean}
 */
function detailSubmitCheck(obj) {
    var flag = true;
    $(obj).find(".required").each(function() {
        if (!$(this).val()) {
            $(this).addClass('form-error');
            $(this).attr("placeholder", "不能为空");
            flag = false;
        }
    });
    return flag;
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

/**************************************PO单头开始**********************************************/

/**
 * 保存
 */
function save() {
    var isValidate = bq.headerSubmitCheck('#inputForm');
    if (isValidate.isSuccess) {
        jp.loading();
        var disabledObjs = bq.openDisabled('#inputForm');
        var params = $('#inputForm').bq_serialize();
        bq.closeDisabled(disabledObjs);
        jp.post("${ctx}/wms/inbound/banQinWmSaleHeader/save", params, function (data) {
            if (data.success) {
                jp.success(data.msg);
                window.location = "${ctx}/wms/inbound/banQinWmSaleHeader/form?id=" + data.body.entity.id;
            } else {
                jp.bqError(data.msg);
            }
        })
    } else {
        jp.bqError(isValidate.msg);
    }
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
 * 生成SO
 */
function createSo() {
    if ($('#auditStatus').val() === '00') {
        jp.warning(rows[i].poNo + "未审核，不能操作");
        return;
    }
    if ($('#status').val() === '90') {
        jp.warning(rows[i].poNo + "已取消，不能操作");
        return;
    }
    if ($('#status').val() === '99') {
        jp.warning(rows[i].poNo + "已关闭，不能操作");
        return;
    }
    var params = "ownerCode=" + $('#ownerCode').val() + "&consigneeCode=" + $('#consigneeCode').val() + "&id=" + $('#id').val() + "&orgId=" + $('#orgId').val();
    jp.openBQDialog("生成SO", "${ctx}/wms/outbound/banQinWmSaleHeader/createSoForm?" + params, "90%", "90%", $('#banQinWmSaleHeaderTable'));
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
 * 公共调用方法
 * @param method 方法名
 */
function commonMethod(method) {
    jp.loading();
    jp.get("${ctx}/wms/inbound/banQinWmSaleHeader/" + method + "?ids=" + $('#id').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/inbound/banQinWmSaleHeader/form?id=" + $('#id').val();
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
    } else {
        $('#poType').prop('disabled', true);
        $('#ownerCode').prop('disabled', true);
        $('#ownerSelectId').prop('disabled', true);
        $('#ownerDeleteId').prop('disabled', true);
    }
    // 公共信息
    $('#orderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#fmEta').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#toEta').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#auditTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
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
            $('#header_cancelOrder').attr('disabled', true);
            if (status === '90' || status === '99') {
                $('#header_closeOrder').attr('disabled', true);
            }
        } else {
            $('#header_closeOrder').attr('disabled', true);
            // 审核状态
            if (auditStatus === '99') {
                $('#header_save').attr('disabled', true);
                $('#header_audit').attr('disabled', true);
                $('#header_cancelOrder').attr('disabled', true);
            } else {
                $('#header_cancelAudit').attr('disabled', true);
                if (auditStatus === '90') {
                    $('#header_audit').attr('disabled', true);
                }
            }
        }
    }
    //根据单头的状态控制明细编辑
    controlDetailByHeader();
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
    $('#header_createSo').attr('disabled', flag);
    $('#header_closeOrder').attr('disabled', flag);
    $('#header_cancelOrder').attr('disabled', flag);
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
        $('#skuDetail_cancel').attr('disabled', false);
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
    $('#skuDetail_cancel').attr('disabled', flag);
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
        if (!$('#id').val() && ($('#status').val() !== '00' || $('#auditStatus').val() === '99' || $('#holdStatus').val() === '99')) {
            $('#skuDetail_add').attr('disabled', true);
            $('#skuDetail_save').attr('disabled', true);
            $('#skuDetail_remove').attr('disabled', true);
            $('#skuDetail_duplicate').attr('disabled', true);
            $('#skuDetail_cancel').attr('disabled', true);
        }
        if (!skuDetail_currentRow || !$('#skuDetail_id').val()) {
            $('#skuDetail_remove').attr('disabled', true);
            $('#skuDetail_duplicate').attr('disabled', true);
            $('#skuDetail_cancel').attr('disabled', true);
        } else {
            if (skuDetail_currentRow.status !== '00') {
                $('#skuDetail_save').attr('disabled', true);
                $('#skuDetail_remove').attr('disabled', true);
                $('#skuDetail_cancel').attr('disabled', true);
            } else {
                $('#skuDetail_cancel').attr('disabled', false);
            }
        }
    } else {
        $('#skuDetail_save').attr('disabled', true);
    }
}

/**************************************PO单头结束********************************************/
/**************************************弹出框开始********************************************/
function afterSkuSelect(event) {
    loadLotAtt({ownerCode: $('#ownerCode').val(), skuCode: event.skuCode, orgId: event.orgId});
}

function afterSelectUom(row) {
    $('#skuDetail_uomQty').val(row.cdprQuantity);
    calc();
}

/**
 * 单位 数量改变后 计算EA数
 */
function calc() {
    var num = !$('#skuDetail_uomQty').val() ? 0 : $('#skuDetail_uomQty').val();
    var qtySaleEa = !$('#skuDetail_qtySaleEa').val() ? 0 : $('#skuDetail_qtySaleEa').val();
    if (!$('#skuDetail_qtySoEa').val()) {
        $('#skuDetail_qtySoEa').val('0');
    }
    var qtySoEa = $('#skuDetail_qtySoEa').val();
    if (!$('#skuDetail_qtyAllocEa').val()) {
        $('#skuDetail_qtyAllocEa').val('0');
    }
    var qtyAllocEa = $('#skuDetail_qtyAllocEa').val();
    if (!$('#skuDetail_qtyPkEa').val()) {
        $('#skuDetail_qtyPkEa').val('0');
    }
    var qtyPkEa = $('#skuDetail_qtyPkEa').val();
    if (!$('#skuDetail_qtyShipEa').val()) {
        $('#skuDetail_qtyShipEa').val('0');
    }
    var qtyShipEa = $('#skuDetail_qtyShipEa').val();
    if (num > 0) {
        $('#skuDetail_qtySaleUom').val(qtySaleEa / num);
        $('#skuDetail_qtySoUom').val(qtySoEa / num);
        $('#skuDetail_qtyAllocUom').val(qtyAllocEa / num);
        $('#skuDetail_qtyPkUom').val(qtyPkEa / num);
        $('#skuDetail_qtyShipUom').val(qtyShipEa / num);
    } else {
        $('#skuDetail_qtySaleUom').val('0');
        $('#skuDetail_qtySoUom').val('0');
        $('#skuDetail_qtyAllocUom').val('0');
        $('#skuDetail_qtyPkUom').val('0');
        $('#skuDetail_qtyShipUom').val('0');
    }

    if (parseFloat(qtySaleEa) < parseFloat(qtySoEa)) {
        jp.warning("销售数必须大于订货数");
    }
}

/**************************************弹出框结束********************************************/
/**************************************商品明细开始********************************************/
/**
 * 初始化明细table
 */
function initWmSaleDetailTab() {
    $('#wmSaleDetailTable').bootstrapTable({
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inbound/banQinWmSaleDetail/data",
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
            skuDetailClick(row);
            jp.changeTableStyle($el);
        },
        onDblClickRow: function (row, $el) {
            skuDetailDbClick(row);
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
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_PO_STATUS'))}, value, "-");
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
            field: 'qtySaleEa',
            title: '销售数EA',
            sortable: true
        }, {
            field: 'qtySoEa',
            title: '订货数EA',
            sortable: true
        }, {
            field: 'qtyAllocEa',
            title: '分配数EA',
            sortable: true
        }, {
            field: 'qtyPkEa',
            title: '拣货数EA',
            sortable: true
        }, {
            field: 'qtyShipEa',
            title: '发货数EA',
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
            field: 'price',
            title: '单价',
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
    } else {
        // 隐藏右边tab
        hideTabRight('#skuDetail_tab-right', '#skuDetail_tab-left');
        skuDetail_isShowTab = false;
    }
    // 按钮控制
    controlDetailEditor();
}

/**
 * 加载批次属性控件
 */
function loadLotAtt(row) {
    var params = "ownerCode=" + row.ownerCode + "&skuCode=" + row.skuCode + "&orgId=" + row.orgId;
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
    $('#skuDetail_qtySaleEa').val('0');
    $('#skuDetail_qtySoUom').val('0');
    $('#skuDetail_qtySoEa').val('0');
    $('#skuDetail_qtyAllocUom').val('0');
    $('#skuDetail_qtyAllocEa').val('0');
    $('#skuDetail_qtyPkUom').val('0');
    $('#skuDetail_qtyPkEa').val('0');
    $('#skuDetail_qtyShipUom').val('0');
    $('#skuDetail_qtyShipEa').val('0');
}

/**
 * 预收数输入同步到EA
 */
function qtySaleChange() {
    var num = !$('#skuDetail_uomQty').val() ? 0 : $('#skuDetail_uomQty').val();
    var qtySaleUom = !$('#skuDetail_qtySaleUom').val() ? 0 : $('#skuDetail_qtySaleUom').val();
    if (num !== 0) {
        $('#skuDetail_qtySaleEa').val(qtySaleUom * num);
    } else {
        $('#skuDetail_qtySaleEa').val('');
    }

    if (parseFloat(qtySaleUom) < parseFloat($('#skuDetail_qtySoUom').val())) {
        jp.warning("销售数必须大于订货数");
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
    // 表单验证
    var validate = bq.detailSubmitCheck('#skuDetail_tab-right');
    if (validate.isSuccess) {
        // 保存前赋值
        beforeSave();
        jp.loading("正在保存中...");
        var disabledObjs = bq.openDisabled("#skuDetailForm");
        var params = $('#skuDetailForm').bq_serialize();
        bq.closeDisabled(disabledObjs);
        jp.post("${ctx}/wms/inbound/banQinWmSaleDetail/save", params, function (data) {
            if (data.success) {
                // tab页刷新
                tabRefresh();
                // 按钮控制
                controlDetailEditor();
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
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
    $('#wmSaleDetailTable').bootstrapTable('refresh');
    hideTabRight('#skuDetail_tab-right', '#skuDetail_tab-left');
    skuDetail_isShowTab = false;
}

/**
 * 保存前赋值
 */
function beforeSave() {
    if (!$('#skuDetail_id').val()) {
        $('#skuDetail_orgId').val($('#orgId').val());
        $('#skuDetail_saleNo').val($('#saleNo').val());
        $('#skuDetail_ownerCode').val($('#ownerCode').val());
        $('#skuDetail_headId').val($('#id').val());
        $('#skuDetail_logisticNo').val($('#logisticNo').val());
    }
}

/**
 * 删除明细
 */
function removeSkuDetail() {
    var rows = getSelections('#wmPoDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    var lineNoArray = new Array();
    for (var x in rows) {
        lineNoArray.push(rows[x].lineNo);
    }
    jp.confirm('确认要删除吗？删除将不能恢复？', function () {
        var params = "saleNo=" + $('#saleNo').val();
        params += "&lineNo=" + lineNoArray.join(',');
        params += "&orgId=" + $('#orgId').val();
        jp.loading("正在删除中...");
        skuDetailCommonMethod('deleteAll', params);
    })
}

/**
 * 复制明细
 */
function duplicatSkuDetail() {
    
}

/**
 * 取消订单行
 */
function cancelSkuDetail() {
    var rows = getSelections('#wmPoDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    var lineNoArray = new Array();
    for (var x in rows) {
        lineNoArray.push(rows[x].lineNo);
    }

    var params = "saleNo=" + $('#saleNo').val();
    params += "&lineNo=" + lineNoArray.join(',');
    params += "&orgId=" + $('#orgId').val();
    jp.loading("取消订单行中...");
    skuDetailCommonMethod('cancel', params);
}

/**
 * 商品明细公共方法
 * @param method
 * @param params
 */
function skuDetailCommonMethod(method, params) {
    jp.get("${ctx}/wms/inbound/banQinWmSaleDetail/" + method + "?" + params, function (data) {
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

/**************************************商品明细结束*******************************************/

</script>