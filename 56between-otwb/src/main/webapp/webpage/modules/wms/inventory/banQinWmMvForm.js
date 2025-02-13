<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var currentRow; // 明细当前行
var isShowTab = false; // 是否显示右边tab页
var disabledObj = [];

$(document).ready(function () {
    // 初始化
    init();
    // 初始化明细table
    initWmTfDetailTable();
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
 */
function showTabRight() {
    $('#tab1-right').show();
    $('#tab1-left').addClass("div-left");
    isShowTab = true;
}

/**
 * 隐藏右边tab
 */
function hideTabRight() {
    $('#tab1-right').hide();
    $('#tab1-left').removeClass("div-left");
    isShowTab = false;
}

/**
 * 单头保存
 */
function save() {
    var isValidate = bq.headerSubmitCheck('#inputForm');
    if (isValidate.isSuccess) {
        jp.loading();
        var disabledObjs = bq.openDisabled('#inputForm');
        var serializeData = $('#inputForm').serialize();
        bq.closeDisabled(disabledObjs);
        jp.post("${ctx}/wms/inventory/banQinWmMv/save", serializeData, function (data) {
            if (data.success) {
                jp.success(data.msg);
                window.location = "${ctx}/wms/inventory/banQinWmMv/form?id=" + data.body.id;
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
 * 单头移动
 */
function executeMv() {
    commonMethod('executeMv');
}

/**
 * 取消订单
 */
function closeOrder() {
    commonMethod('closeOrder');
}

/**
 * 关闭订单
 */
function cancelOrder() {
    commonMethod('cancelOrder');
}

/**
 * 单头公共方法
 * @param method
 */
function commonMethod(method) {
    jp.get("${ctx}/wms/inventory/banQinWmMv/" + method + "?ids=" + $('#id').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/inventory/banQinWmMv/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 单头按钮控制
 */
function controlHeaderEditor() {
    $('#header_save').attr('disabled', true);
    $('#header_audit').attr('disabled', true);
    $('#header_cancelAudit').attr('disabled', true);
    $('#header_executeMv').attr('disabled', true);
    $('#header_closeOrder').attr('disabled', true);
    $('#header_cancelOrder').attr('disabled', true);
    if (!$('#id').val()) {
        $('#header_save').attr('disabled', false);
        return;
    }
    if ($('#status').val() === '00' && $('#auditStatus').val() !== '99') {
        $('#header_save').attr('disabled', false);
        $('#header_cancelOrder').attr('disabled', false);
        if ($('#auditStatus').val() === '00') {
            $('#header_audit').attr('disabled', false);
        } else {
            $('#header_executeMv').attr('disabled', false);
        }
    }
    if ($('#status').val() === '00' && $('#auditStatus').val() === '99') {
        $('#header_cancelAudit').attr('disabled', false);
        $('#header_executeMv').attr('disabled', false);
    }
    if ($('#status').val() === '10') {
        $('#header_executeMv').attr('disabled', false);
        $('#header_closeOrder').attr('disabled', false);
    }
    if ($('#status').val() === '20') {
        $('#header_closeOrder').attr('disabled', false);
    }
}

/**
 * 明细按钮控制
 */
function controlDetailEditor() {
    if ($('#status').val() !== '00' || $('#auditStatus').val() === '99') {
        $('#detail_add').attr('disabled', true);
    } else {
        $('#detail_add').attr('disabled', false);
    }
    $('#detail_save').attr('disabled', true);
    $('#detail_remove').attr('disabled', true);
    $('#detail_executeMv').attr('disabled', true);
    $('#detail_cancel').attr('disabled', true);
    $('#serial_remove').attr('disabled', true);
    if (isShowTab) {
        if (!$('#detail_id').val() && $('#status').val() == '00' && $('#auditStatus').val() !=='99') {
            $('#detail_save').attr('disabled', false);
        }
        if ($('#detail_status').val() === '00' && ($('#status').val() === '00' || $('#status').val() === '10')) {
            if ($('#auditStatus').val() === '99') {
                $('#detail_executeMv').attr('disabled', false);
            } else {
                $('#detail_save').attr('disabled', false);
                $('#detail_remove').attr('disabled', false);
                $('#detail_cancel').attr('disabled', false);
                $('#serial_remove').attr('disabled', false);
                if ($('#auditStatus').val() === '90') {
                    $('#detail_executeMv').attr('disabled', false);
                }
            }
        }
    } else {
        if (!$('#id').val()) {
            $('#detail_add').attr('disabled', true);
        }
        if ($('#status').val() === '00') {
            if ($('#auditStatus').val() !== '99') {
                $('#detail_remove').attr('disabled', false);
                $('#detail_cancel').attr('disabled', false);
                if ($('#auditStatus').val() === '90') {
                    $('#detail_executeMv').attr('disabled', false);
                }
            } else {
                $('#detail_executeMv').attr('disabled', false);
            }
        }
        if ($('#status').val() === '10') {
            $('#detail_executeMv').attr('disabled', false);
        }
    }
}

/**
 * 初始化
 */
function init() {
    initLabel();
    // 按钮控制
    controlHeaderEditor();
    controlDetailEditor();
}

/**
 * 初始化标签状态
 */
function initLabel() {
    if (!$('#id').val()) {
        $('#orgId').val(jp.getCurrentOrg().orgId);
        $('#mvOp').prop('readonly', true).val('${fns:getUser().getLoginName()}');
        $('#mvTime input').val(jp.dateFormat(new Date(), 'yyyy-MM-dd hh:mm:ss'));
    } else {
        $('#ownerSelectId').prop('disabled', true);
        $('#ownerDeleteId').prop('disabled', true);
        $('#ownerCode').prop('disabled', true);
        $('#ownerName').prop('disabled', true);
    }
    $('#mvNo').prop('readonly', true);
    $('#status').prop('disabled', true);
    $('#auditStatus').prop('disabled', true);
    $('#auditOp').prop('readonly', true);
    $('#auditTime').prop('readonly', true);
    $('#mvTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
}

/**
 * 初始化明细table
 */
function initWmTfDetailTable() {
    $('#wmMvDetailTable').bootstrapTable({
        cache: false,
        sidePagination: "server",
        url: "${ctx}/wms/inventory/banQinWmMv/detail/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.headerId = !$('#id').val() ? '#' : $('#id').val();
            searchParam.orgId = $('#orgId').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function(row, $el) {
            jp.changeTableStyle($el);
            clickDetail(row);
        },
        onDblClickRow: function (row, $el) {
            dbClickDetail(row);
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
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_MV_STATUS'))}, value, "-");
            }
        },
        {
            field: 'ownerCode',
            title: '货主',
            sortable: true
        },
        {
            field: 'skuCode',
            title: '商品',
            sortable: true
        },
        {
            field: 'packCode',
            title: '包装编码',
            sortable: true
        },
        {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        },
        {
            field: 'fmLocCode',
            title: '源库位',
            sortable: true
        },
        {
            field: 'fmTraceId',
            title: '源跟踪号',
            sortable: true
        },
        {
            field: 'fmUom',
            title: '源单位',
            sortable: true
        },
        {
            field: 'toLocCode',
            title: '目标库位',
            sortable: true
        },
        {
            field: 'toTraceId',
            title: '目标跟踪号',
            sortable: true
        },
        {
            field: 'toUom',
            title: '目标单位',
            sortable: true
        },
        {
            field: 'qtyMvUom',
            title: '移动操作数',
            sortable: true
        },
        {
            field: 'qtyMvEa',
            title: '移动操作数EA',
            sortable: true
        }]
    });
}

/**
 * 获取表格勾选行Ids
 */
function getIdSelections() {
    return $.map($("#wmMvDetailTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

/**
 * 获取表格勾选行
 */
function getSelections() {
    return $.map($("#wmMvDetailTable").bootstrapTable('getSelections'), function (row) {
        return row
    });
}

/**
 * 单击表格事件
 * @param row 当前行
 */
function clickDetail(row) {
    currentRow = row;
    if (isShowTab) {
        // 表单赋值
        evaluate();
    }
    // 按钮控制
    controlDetailEditor();
}

/**
 * 双击表格事件
 * @param row 当前行
 */
function dbClickDetail(row) {
    currentRow = row;
    if (!isShowTab) {
        // 显示右边tab
        showTabRight();
        // 加载批次属性控件
        loadFmLotAtt(row.ownerCode, row.skuCode, row.orgId);
        // 表单赋值
        evaluate();
    } else {
        // 隐藏右边tab
        hideTabRight();
    }
    
    // 按钮控制
    controlDetailEditor();
}

/**
 * 赋值
 */
function evaluate() {
    $("input[id^='detail']").each(function() {
        var $Id = $(this).attr('id');
        var $Name = $(this).attr('name');
        $('#' + $Id).val(eval("currentRow." + $Name));
    });

    $("select[id^='detail']").each(function() {
        var $Id = $(this).attr('id');
        var $Name = $(this).attr('name');
        $('#' + $Id).val(eval("currentRow." + $Name));
    });
}

/**
 * 加载批次属性控件
 */
function loadFmLotAtt(ownerCode, skuCode, orgId) {
    var params = "ownerCode=" + ownerCode + "&skuCode=" + skuCode + "&orgId=" + orgId;
    bq.get("${ctx}/wms/basicdata/banQinCdWhLotDetail/getInfo?" + params, false, function (data) {
        if (data) {
            $('#detailFmLotAttTab').empty();
            var html = bq.getLotAttTab(data, 'detail_lotAtt', 5);
            html = html.replace(new RegExp("required", "g"), "");
            html = html.split('<font color="red">*</font>').join('');
            $('#detailFmLotAttTab').append(html);
            $('#detailFmLotAttTab .form-control').each(function() {
                $(this).prop('disabled', true);
            });
        }
    })
}

/**
 * 新增明细
 */
function addDetail() {
    // 显示右边
    showTabRight();
    currentRow = null;
    // 清空表单
    $(':input', '#inputForm1').val('');
    // 初始控件状态
    $('#detail_status').find('option').eq(0).selected();
    // 按钮控制
    controlDetailEditor();
}

/**
 * 明细行保存
 */
function saveDetail() {
    if (!isShowTab) {
        jp.warning("当前无保存记录!");
        return;
    }
    if (parseFloat($('#detail_qtyMvEa').val()) === 0) {
        jp.warning("移动操作数量必须大于0！");
        return;
    }

    var validate1 = bq.detailSubmitCheck('#tab-1-1');
    var validate2 = bq.detailSubmitCheck('#tab-1-2', 2);
    if (validate1.isSuccess && validate2.isSuccess) {
        jp.loading("正在保存中...");
        beforeSave();
        var disabledObjs = bq.openDisabled("#inputForm1");
        var row = {};
        bq.copyProperties(currentRow, row);
        $.extend(row, bq.serializeJson($('#inputForm1')));
        bq.closeDisabled(disabledObjs);
        $.ajax({
            url: "${ctx}/wms/inventory/banQinWmMv/detail/save",
            type: 'POST',
            data: JSON.stringify(row),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    $('#wmMvDetailTable').bootstrapTable('refresh');
                    hideTabRight();
                    // 按钮控制
                    controlDetailEditor();
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            }
        });
    } else {
        jp.bqError(validate1.isSuccess ? validate2.msg : validate1.msg);
    }
}

/**
 * 保存前赋值
 */
function beforeSave() {
    if (!$('#detail_id').val()) {
        $('#detail_orgId').val($('#orgId').val());
        $('#detail_headerId').val($('#id').val());
        $('#detail_mvNo').val($('#mvNo').val());
        $('#detail_ownerCode').val($('#ownerCode').val());
    }
}

/**
 * 明细行删除
 */
function removeDetail() {
    var rowIds = getIdSelections();
    if (rowIds.length === 0) {
        jp.error("请选择一条记录!");
        return;
    }
    jp.confirm('确认要删除吗？删除将不能恢复？', function () {
        jp.loading("正在删除中...");
        jp.get("${ctx}/wms/inventory/banQinWmMv/detail/deleteAll?ids=" + rowIds + "&headerId=" + $('#id').val(), function (data) {
            if (data.success) {
                $('#wmMvDetailTable').bootstrapTable('refresh');
                // 按钮控制
                controlDetailEditor();
                hideTabRight();
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

/**
 * 明细行移动
 */
function executeMvDetail() {
    var rowIds = getIdSelections();
    if (rowIds.length === 0) {
        jp.error("请选择一条记录!");
        return;
    }
    jp.loading("操作中...");
    jp.get("${ctx}/wms/inventory/banQinWmMv/detail/executeMv?ids=" + rowIds + "&headerId=" + $('#id').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/inventory/banQinWmMv/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 明细行取消
 */
function cancelDetail() {
    var rowIds = getIdSelections();
    if (rowIds.length === 0) {
        jp.error("请选择一条记录!");
        return;
    }
    jp.loading("正在取消中...");
    jp.get("${ctx}/wms/inventory/banQinWmMv/detail/cancel?ids=" + rowIds, function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/inventory/banQinWmMv/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

function afterSelectFmSku(row) {
    loadFmLotAtt(row.ownerCode, row.skuCode, row.orgId);
    item = row;
    $('#detailFmLotAttTab .form-control').each(function() {
        var $name = $(this).prop('name').replace("fmL", "l");
        $(this).val(eval("item." + $name));
    });

    // 同步转出EA数
    fmQtyChange();
}

function afterSelectToOwner(row) {
    if ($('#detail_toSku').val()) {
        $('#detail_toSku').val('');
        $('#detail_toSkuName').val('');
    }
}

function afterSelectToSku(row) {
    if (!$('#detail_toOwner').val()) {
        $('#detail_toOwner').val(row.ownerCode);
        $('#detail_toOwnerName').val(row.ownerName);
    }
}

function afterSelectFmPack(row) {
    $('#detail_fmCdprQuantity').val(row.cdprQuantity);
    fmQtyChange();
}

function afterSelectToPack(row) {
    $('#detail_toCdprQuantity').val(row.cdprQuantity);
    toQtyChange();
}

/**
 * 转入数输入同步到EA
 */
function toQtyChange() {
    var qtyUom = !$('#detail_toCdprQuantity').val() ? 0 : $('#detail_toCdprQuantity').val();
    var toQtyUom = !$('#detail_qtyMvUom').val() ? 0 : $('#detail_qtyMvUom').val();
    $('#detail_qtyMvEa').val(qtyUom * Math.floor(toQtyUom * 100) / 100);
}

/**
 * 转出数输入同步到EA
 */
function fmQtyChange() {
    var qtyUom = !$('#detail_fmCdprQuantity').val() ? 0 : $('#detail_fmCdprQuantity').val();
    var fmQtyUom = !$('#detail_fmQtyUom').val() ? 0 : $('#detail_fmQtyUom').val();
    $('#detail_fmQtyEa').val(qtyUom * Math.floor(fmQtyUom * 100) / 100);
}

</script>