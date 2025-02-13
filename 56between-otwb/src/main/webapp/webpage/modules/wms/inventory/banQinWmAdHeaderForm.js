<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var currentRow; // 明细当前行
var isShowTab = false; // 是否显示右边tab页
$(document).ready(function () {
    // 初始化
    init();
    // 初始化明细tab
    initDetailTable();
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
 * 表头保存
 * @returns {boolean}
 */
function save() {
    var isValidate = bq.headerSubmitCheck('#inputForm');
    if (isValidate.isSuccess) {
        jp.loading();
        var disabledObjs = bq.openDisabled('#inputForm');
        var params = $('#inputForm').serialize();
        bq.closeDisabled(disabledObjs);
        jp.post("${ctx}/wms/inventory/banQinWmAdHeader/save", params, function (data) {
            if (data.success) {
                jp.success(data.msg);
                window.location = "${ctx}/wms/inventory/banQinWmAdHeader/form?id=" + data.body.entity.id;
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
 * 表头执行调整
 */
function adjust() {
    commonMethod('adjust');
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
 * @param method
 */
function commonMethod(method) {
    jp.loading();
    jp.get("${ctx}/wms/inventory/banQinWmAdHeader/" + method + "?ids=" + $('#id').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/inventory/banQinWmAdHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 控制单头按钮
 */
    function controlHeaderEditor() {
    $('#header_save').attr('disabled', true);
    $('#header_audit').attr('disabled', true);
    $('#header_cancelAudit').attr('disabled', true);
    $('#header_adjust').attr('disabled', true);
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
            $('#header_adjust').attr('disabled', false);
        }
    }
    if ($('#status').val() === '00' && $('#auditStatus').val() === '99') {
        $('#header_cancelAudit').attr('disabled', false);
        $('#header_adjust').attr('disabled', false);
    }
    if ($('#status').val() === '10') {
        $('#header_adjust').attr('disabled', false);
        $('#header_closeOrder').attr('disabled', false);
    }
    if ($('#status').val() === '20') {
        $('#header_closeOrder').attr('disabled', false);
    }
}

/**
 * 控制明细按钮
 */
function controlDetailEditor() {
    if ($('#status').val() !== '00' || $('#auditStatus') === '99') {
        $('#detail_add').attr('disabled', true);
    } else {
        $('#detail_add').attr('disabled', false);
    }
    $('#detail_save').attr('disabled', true);
    $('#detail_getInv').attr('disabled', true);
    $('#detail_remove').attr('disabled', true);
    $('#detail_adjust').attr('disabled', true);
    $('#detail_cancel').attr('disabled', true);
    $('#serial_add').attr('disabled', true);
    $('#serial_remove').attr('disabled', true);
    if (isShowTab) {
        if (!$('#detail_id').val() && $('#status').val() === '00' && $('#auditStatus').val() !== '99') {
            $('#detail_save').attr('disabled', false);
            $('#serial_add').attr('disabled', false);
            $('#serial_remove').attr('disabled', false);
            $('#detail_getInv').attr('disabled', false);
        }
        if ($('#status').val() === '00' && ($('#status').val() === '00' || $('#status').val() === '10')) {
            if ($('#auditStatus').val() === '99') {
                $('#detail_adjust').attr('disabled', false);
            } else {
                $('#detail_save').attr('disabled', false);
                $('#serial_add').attr('disabled', false);
                $('#serial_remove').attr('disabled', false);
                $('#detail_getInv').attr('disabled', false);
                $('#detail_remove').attr('disabled', false);
                $('#detail_cancel').attr('disabled', false);
                if ($('#auditStatus').val() === '90') {
                    $('#detail_adjust').attr('disabled', false);
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
                    $('#detail_adjust').attr('disabled', false);
                }
            } else {
                $('#detail_adjust').attr('disabled', false);
            }
        }
        if ($('#status').val() === '10') {
            $('#detail_adjust').attr('disabled', false);
        }
    }
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
 * 初始化
 */
function init() {
    // 初始化标签
    initLabel();
    // 初始化按钮
    controlHeaderEditor();
    controlDetailEditor();
}

/**
 * 初始化标签状态
 */
function initLabel() {
    if (!$('#id').val()) {
        $('#orgId').val(jp.getCurrentOrg().orgId);
        $('#adOp').val('${fns:getUser().loginName}');
        $('#adTime').val(jp.dateFormat(new Date(), 'yyyy-MM-dd hh:mm:ss'));
    } else {
        $('#ownerSelectId').prop('disabled', true);
        $('#ownerDeleteId').prop('disabled', true);
        $('#ownerCode').prop('disabled', true);
        $('#ownerName').prop('disabled', true);
    }
    // 公共状态
    $('#adNo').prop('readonly', true);
    $('#status').prop('disabled', true);
    $('#auditStatus').prop('disabled', true);
    $('#countNo').prop('readonly', true);
    $('#auditOp').prop('readonly', true);
    $('#auditTime').prop('readonly', true);
    $('#adTimeF').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
}

/**
 * 初始化明细table
 */
function initDetailTable() {
    $('#wmAdDetailTable').bootstrapTable({
        cache: false,
        sidePagination: "server",
        url: "${ctx}/wms/inventory/banQinWmAdDetail/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.headerId = !$('#id').val() ? "#" : $('#id').val();
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
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_AD_STATUS'))}, value, "-");
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
            field: 'packDesc',
            title: '包装规格',
            sortable: true
        },
        {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        },
        {
            field: 'locCode',
            title: '库位',
            sortable: true
        },
        {
            field: 'traceId',
            title: '跟踪号',
            sortable: true
        },
        {
            field: 'adMode',
            title: '调整方式',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_AD_MODE'))}, value, "-");
            }
        },
        {
            field: 'packCode',
            title: '包装编码',
            sortable: true
        },
        {
            field: 'uom',
            title: '包装单位',
            sortable: true
        },
        {
            field: 'qtyAdUom',
            title: '调整操作数',
            sortable: true
        },
        {
            field: 'qtyAdEa',
            title: '调整操作数EA',
            sortable: true
        }]
    });

    // 初始化明细控件
    initDetail();
}

/**
 * 初始化明细控件
 */
function initDetail() {
    $('#detail_lotAtt01F').datetimepicker({format: "YYYY-MM-DD"});
    $('#detail_lotAtt02F').datetimepicker({format: "YYYY-MM-DD"});
    $('#detail_lotAtt03F').datetimepicker({format: "YYYY-MM-DD"});
}

/**
 * 获取表格勾选行Ids
 */
function getIdSelections() {
    return $.map($("#wmAdDetailTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

/**
 * 获取表格勾选行
 */
function getSelections() {
    return $.map($("#wmAdDetailTable").bootstrapTable('getSelections'), function (row) {
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
        loadLotAtt(row);
        // 表单赋值
        evaluate();
        // 根据状态判断
        if (row.status === '00') {
            initDetailLabel();
        }
        $('#detail_isSerial').val(row.isSerial).prop('checked', 'Y' === row.isSerial);
        initSerialTab();
    } else {
        // 隐藏右边tab
        hideTabRight();
    }
    $("#wmAdDetailTable").bootstrapTable('resetView');
    // 按钮控制
    controlDetailEditor();
}

/**
 * 赋值
 */
function evaluate() {
    $("input[id^='detail']").each(function() {
        var $id = $(this).attr('id');
        var $name = $(this).attr('name');
        $('#' + $id).val(eval("currentRow." + $name));
    });

    $("select[id^='detail']").each(function() {
        var $id = $(this).attr('id');
        var $name = $(this).attr('name');
        $('#' + $id).val(eval("currentRow." + $name));
    });
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
    initDetailLabel();
    // 按钮控制
    controlDetailEditor();
}

/**
 * 初始化明细控件状态
 */
function initDetailLabel() {
    $('#detail_status').prop('disabled', true).find('option').eq(0).prop('selected', true);
}

/**
 * 明细行保存
 */
function saveDetail() {
    if (!isShowTab) {
        jp.warning("当前无保存记录!");
        return;
    }

    if (!$('#id').val()) {
        jp.warning("请先保存订单头!");
        return;
    }
    
    var validate = bq.detailSubmitCheck('#tab1-right');
    if (validate.isSuccess) {
        // 保存前赋值
        beforeSave();
        jp.loading("正在保存中...");
        var disabledObjs = bq.openDisabled("#inputForm1");
        var row = {};
        bq.copyProperties(currentRow, row);
        $.extend(row, bq.serializeJson($('#inputForm1')));
        bq.closeDisabled(disabledObjs);
        $.ajax({
            url: "${ctx}/wms/inventory/banQinWmAdDetail/save",
            type: 'POST',
            data: JSON.stringify(row),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    $('#wmAdDetailTable').bootstrapTable('refresh');
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
        jp.bqError(validate.msg);
    }
}

/**
 * 保存前赋值
 */
function beforeSave() {
    if (!$('#detail_id').val()) {
        $('#detail_orgId').val($('#orgId').val());
        $('#detail_adNo').val($('#adNo').val());
        $('#detail_ownerCode').val($('#ownerCode').val());
        $('#detail_headerId').val($('#id').val());
    }
}

/**
 * 明细行删除
 */
function removeDetail() {
    var rowIds = getIdSelections();
    if (rowIds.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    jp.confirm('确认要删除吗？删除将不能恢复？', function () {
        jp.loading("正在删除中...");
        jp.get("${ctx}/wms/inventory/banQinWmAdDetail/deleteAll?id=" + rowIds + "&headerId=" + $('#id').val(), function (data) {
            if (data.success) {
                $('#wmAdDetailTable').bootstrapTable('refresh');
                hideTabRight();
                // 按钮控制
                controlHeaderEditor();
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

/**
 * 明细行执行调整
 */
function adjustDetail() {
    var rowIds = getIdSelections();
    if (rowIds.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    jp.loading("正在执行调整...");
    jp.get("${ctx}/wms/inventory/banQinWmAdDetail/adjustDetail?ids=" + rowIds + "&headerId=" + $('#id').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/inventory/banQinWmAdHeader/form?id=" + $('#id').val();
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
        jp.bqError("请选择一条记录!");
        return;
    }
    jp.loading("正在取消中...");
    jp.get("${ctx}/wms/inventory/banQinWmAdDetail/cancelDetail?ids=" + rowIds + "&headerId=" + $('#id').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/inventory/banQinWmAdHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 调整数同步修改EA数
 */
function qtyAddChange() {
    var uomQty = !$('#detail_cdprQuantity').val() ? 0 : $('#detail_cdprQuantity').val();
    var qtyUom = !$('#detail_qtyAdUom').val() ? 0 : $('#detail_qtyAdUom').val();
    $('#detail_qtyAdEa').val(uomQty * Math.floor(qtyUom * 100) / 100);
}

function afterSelectSku(row) {
    $('#detail_isSerial').val(row.isSerial).prop('checked', 'Y' === row.isSerial);
    loadLotAtt({ownerCode: $('#ownerCode').val(), skuCode: row.skuCode, orgId: row.orgId});
}

function afterSelectLot(row) {
    var row = row;
    $("input[id^='detail_lotAtt']").each(function() {
        var $id = $(this).attr('id');
        var $name = $(this).attr('name');
        $('#' + $id).val(eval("row." + $name));
    });

    $("select[id^='detail_lotAtt']").each(function() {
        var $id = $(this).attr('id');
        var $name = $(this).attr('name');
        $('#' + $id).val(eval("row." + $name));
    });
}

function afterSelectPack(row) {
    // 修改调整数
    $('#detail_cdprQuantity').val(row.cdprQuantity);
    qtyAddChange();
}

/**
 * 加载批次属性控件
 */
function loadLotAtt(row) {
    var params = "ownerCode=" + row.ownerCode + "&skuCode=" + row.skuCode + "&orgId=" + row.orgId;
    bq.get("${ctx}/wms/basicdata/banQinCdWhLotDetail/getInfo?" + params, false, function (data) {
        if (data) {
            $('#detailLotAttTab').empty();
            var html = bq.getLotAttTab(data, 'detail_lotAtt');
            $('#detailLotAttTab').append(html);
            $('#detailLotAttTab .detail-date').each(function() {
                laydate.render({
                    elem: '#' + $(this).prop('id'),
                    theme: '#393D49'
                });
            });
        }
    })
}

/**
 * 按库存获取
 */
function getByInventory() {
    top.layer.open({
        type: 2,
        area: ['1400px', '800px'],
        title:"选择商品批次库位库存",
        auto:true,
        name:'friend',
        content: "${ctx}/tag/grid?url="+encodeURIComponent("${ctx}/wms/inventory/banQinWmInvLotLoc/data?ownerCode=" + $('#ownerCode').val() + "|orgId=" + jp.getCurrentOrg().orgId)
        +"&fieldLabels="+encodeURIComponent("商品编码|商品名称|货主编码|货主名称|批次号|库位|跟踪号|库存数|库存可用数|冻结数|分配数|拣货数|上架待出数|移动待出数|补货待出数|生产日期|失效日期|入库日期|品质|批次属性5|批次属性6|批次属性7|批次属性8|批次属性9|批次属性10|批次属性11|批次属性12")
        +"&fieldKeys="+encodeURIComponent("skuCode|skuName|ownerCode|ownerName|lotNum|locCode|traceId|qty|qtyAvailable|qtyHold|qtyAlloc|qtyPk|qtyPaOut|qtyMvOut|qtyRpOut|lotAtt01|lotAtt02|lotAtt03|lotAtt04|lotAtt05|lotAtt06|lotAtt07|lotAtt08|lotAtt09|lotAtt10|lotAtt11|lotAtt12")
        +"&searchLabels="+encodeURIComponent("商品编码|批次号|库位|跟踪号")+"&searchKeys="+encodeURIComponent("skuCode|lotNum|locCode|traceId")+"&isMultiSelected=false",
        btn: ['确定', '关闭'],
        yes: function(index, layero) {
            // 得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
            var iframeWin = layero.find('iframe')[0].contentWindow;
            var items = iframeWin.getSelections();
            if(items == ""){
                jp.warning("必须选择一条数据!");
                return;
            }
            // 初始化明细
            initDetailLabel();
            $('#detail_skuCode').val(items[0].skuCode);
            $('#detail_skuName').val(items[0].skuName);
            $('#detail_lotNum').val(items[0].lotNum);
            $('#detail_locCode').val(items[0].locCode);
            $('#detail_traceId').val(items[0].traceId);
            $('#detail_packCode').val(items[0].packCode);
            $('#detail_packDesc').val(items[0].packDesc);
            $('#detail_uom').val(items[0].printUom);
            $('#detail_cdprDesc').val(items[0].uomDesc);
            $('#detail_cdprQuantity').val(items[0].uomQty);
            $('#detail_isSerial').prop('checked', items[0].isSerial === 'Y').val(items[0].isSerial);
            loadLotAtt(items[0]);
            afterSelectLot(items[0]);
            top.layer.close(index);
        },
        cancel: function(index){
        }
    });
}

function initSerialTab() {
    var params = "adNo=" + $('#adNo').val() + "&skuCode=" + $('#detail_skuCode').val() + "&ownerCode=" + $('#detail_ownerCode').val() + "&orgId=" + $('#detail_orgId').val();
    $.get("${ctx}/wms/inventory/banQinWmAdSerial/data?" + params, function (data) {
        $('#serialList').empty();
        serialRowIdx = 0;
        for (var i = 0; i < data.rows.length; i++) {
            addRow('#serialList', i, serialTpl, data.rows[i]);
            serialRowIdx += 1;
        }
    });
}

function addSerial() {
    addRow('#serialList', serialRowIdx, serialTpl);
    serialRowIdx = serialRowIdx + 1;
}

/**
 * 序列号新增
 */
function addRow(list, idx, tpl, row) {
    if (!row) {
        tpl = tpl.replace("{{row.orgId}}", $('#orgId').val());
        tpl = tpl.replace("{{row.headerId}}", $('#id').val());
        tpl = tpl.replace("{{row.adNo}}", $('#adNo').val());
        tpl = tpl.replace("{{row.ownerCode}}", $('#ownerCode').val());
        tpl = tpl.replace("{{row.skuCode}}", $('#detail_skuCode').val());
        tpl = tpl.replace("{{row.ownerName}}", $('#ownerName').val());
        tpl = tpl.replace("{{row.skuName}}", $('#detail_skuName').val());
        tpl = tpl.replace("{{row.lotNum}}", $('#detail_lotNum').val());
    }
    $(list).append(Mustache.render(tpl, {
        idx: idx, delBtn: true, row: row
    }));
    $(list + idx).find("select").each(function () {
        $(this).val($(this).attr("data-value"));
    });
}

function serialSelectChange(flag) {
    $("input[name='serialSelect']:checkbox").prop('checked', flag);
}

/**
 * 删除序列号
 */
function removeSerial() {
    var selectRows = $("input[name='serialSelect']:checked");
    if (selectRows.length === 0) {
        jp.bqError("请选择记录");
        return;
    }
    selectRows.each(function() {
        $(this).parent().parent().remove();
        serialRowIdx --;
    });
}

</script>