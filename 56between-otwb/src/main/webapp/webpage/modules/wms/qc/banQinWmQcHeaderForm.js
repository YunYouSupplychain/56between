<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var skuDetail_currentRow; // 商品明细当前行
var qcDetail_currentRow; // 质检明细当前行
var paTask_currentRow; // 上架任务明细当前行
var skuDetail_isShowTab = false; // 是否显示右边商品明细tab页
var qcDetail_isShowTab = false; // 是否显示右边质检明细tab页
var paTask_isShowTab = false; // 是否显示右边上架任务tab页
var currentLoginName = '${fns:getUser().loginName}';
var disabledObj = [];

$(document).ready(function () {
    // 初始化商品明细tab
    initWmSkuDetailTab();
    // 初始化质检明细tab
    initWmQcDetailTab();
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
    var detailTabIndex = sessionStorage.getItem("QC_" + currentLoginName +"_detailTab");
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
        case "qcDetail":
            qcDetailClick(row);
            break;
        case "paTask":
            paTaskClick(row);
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
        case "qcDetail":
            qcDetailDbClick(row);
            break;
        case "paTask":
            paTaskDbClick(row);
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

/**************************************质检单头开始**********************************************/

/**
 * 保存
 */
function save() {
    var validate = bq.headerSubmitCheck('#inputForm');
    if (validate.isSuccess) {
        openDisable('#inputForm');
        jp.loading();
        jp.post("${ctx}/wms/qc/banQinWmQcHeader/save", $('#inputForm').bq_serialize(), function (data) {
            if (data.success) {
                jp.success(data.msg);
                window.location = "${ctx}/wms/qc/banQinWmQcHeader/form?id=" + data.body.entity.id;
            } else {
                closeDisable();
                jp.bqError(data.msg);
            }
        })
    } else {
        jp.bqError(validate.msg);
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
 * 关闭订单
 */
function closeOrder() {
    commonMethod('closeOrder');
}

/**
 * 公共调用方法
 * @param method 方法名
 */
function commonMethod(method) {
    jp.loading();
    jp.get("${ctx}/wms/qc/banQinWmQcHeader/" + method + "?ids=" + $('#id').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/qc/banQinWmQcHeader/form?id=" + $('#id').val();
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
    }
    // 公共信息
    $('#fmEtq').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#toEtq').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
}

/**
 * 初始化按钮
 */
function controlHeaderEditor() {
    controlListButton(false);
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
            if (status === '99') {
                $('#header_closeOrder').attr('disabled', true);
            }
        } else {
            $('#header_closeOrder').attr('disabled', true);
            // 审核状态
            if (auditStatus === '99') {
                $('#header_save').attr('disabled', true);
                $('#header_audit').attr('disabled', true);
            } else {
                $('#header_cancelAudit').attr('disabled', true);
                if (auditStatus === '90') {
                    $('#header_audit').attr('disabled', true);
                }
            }
        }
    }
    // 根据单头的状态控制明细编辑
    controlSkuEditor();
    controlDetailEditor();
}

/**
 * 初始化单头按钮
 * @param flag
 */
function controlListButton(flag) {
    $('#header_save').attr('disabled', flag);
    $('#header_audit').attr('disabled', flag);
    $('#header_cancelAudit').attr('disabled', flag);
    $('#header_closeOrder').attr('disabled', flag);
}

/**
 * 根据单头的状态控制明细编辑
 */
function controlSkuEditor() {
    $('#skuDetail_remove').attr('disabled', false);
    $('#skuDetail_qcConfirm').attr('disabled', false);
    $('#skuDetail_cancelQcConfirm').attr('disabled', false);
    $('#skuDetail_createPaTask').attr('disabled', false);
    $('#skuDetail_qcActSuggest').attr('disabled', false);
    // 非创建状态，已审核，不能操作
    if ($('#auditStatus').val() === '99') {
        $('#skuDetail_remove').attr('disabled', true);
    }
    if ($('#auditStatus').val() === '00') {
        $('#skuDetail_qcConfirm').attr('disabled', true);
        $('#skuDetail_cancelQcConfirm').attr('disabled', true);
    }
    if ('PA' !== $('#qcPhase').val()) {
        $('#skuDetail_createPaTask').attr('disabled', true);
    }
    if (skuDetail_isShowTab) {
        if ($('#status').val() === '90' || $('#status').val() === '99' || $('#status').val() !== '00') {
            $('#skuDetail_remove').attr('disabled', true);
            $('#skuDetail_qcConfirm').attr('disabled', true);
            $('#skuDetail_qcActSuggest').attr('disabled', true);
        }
        if (!$('#skuDetail_pctQua').val()) {
            $('#skuDetail_qcConfirm').attr('disabled', true);
        }
        if ($('#status').val() === '00') {
            $('#skuDetail_cancelQcConfirm').attr('disabled', true);
        }
        if ('PA' !== $('#qcPhase').val() || $('#status').val() !== '20') {
            $('#skuDetail_createPaTask').attr('disabled', true);
        }
    }
}

/**
 * 控制质检明细编辑
 */
function controlDetailEditor() {
    $('#qcDetail_save').attr('disabled', false);
    if (qcDetail_isShowTab) {
        if (qcDetail_currentRow && (qcDetail_currentRow.status !== '00' || $('#status').val() === '99' || $('#auditStatus').val() === '00')) {
            $('#qcDetail_save').attr('disabled', true);
        }
    } else {
        $('#qcDetail_save').attr('disabled', true);
    }
}

/**
 * 控制上架任务编辑
 */
function controlTaskPaEditor() {
    // 上架任务
    controlTaskPaButton(false);
    if (!$('#id').val() || $('#status').val() === '00' || $('#auditStatus').val() === '00') {
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

/**************************************质检单头结束********************************************/

/**************************************商品明细开始********************************************/
/**
 * 初始化明细table
 */
function initWmSkuDetailTab() {
    $('#wmQcSkuDetailTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/qc/banQinWmQcSku/data",
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
        }, {
            field: 'lineNo',
            title: '行号',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_STATUS'))}, value, "-");
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
            field: 'locCode',
            title: '库位',
            sortable: true
        }, {
            field: 'traceId',
            title: '跟踪号',
            sortable: true
        }, {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        }, {
            field: 'qtyAvailQcUom',
            title: '可质检数',
            sortable: true
        }, {
            field: 'qtyAvailQcEa',
            title: '可质检数EA',
            sortable: true
        }, {
            field: 'qtyPlanQcUom',
            title: '计划质检数',
            sortable: true
        }, {
            field: 'qtyPlanQcEa',
            title: '计划质检数EA',
            sortable: true
        }, {
            field: 'qtyQcQuaEa',
            title: '质检合格数EA',
            sortable: true
        }, {
            field: 'qtyQcUnquaEa',
            title: '质检不合格数EA',
            sortable: true
        }, {
            field: 'pctQua',
            title: '合格率',
            sortable: true
        }, {
            field: 'qcSuggest',
            title: '质检处理建议',
            sortable: true
        }, {
            field: 'qcActSuggest',
            title: '实际质检处理',
            sortable: true
        }, {
            field: 'packDesc',
            title: '包装规格',
            sortable: true
        }, {
            field: 'uom',
            title: '包装单位',
            sortable: true
        }, {
            field: 'qcRuleName',
            title: '质检规则',
            sortable: true
        }, {
            field: 'itemGroupName',
            title: '质检项',
            sortable: true
        }, {
            field: 'orderNo',
            title: '单据号',
            sortable: true
        }, {
            field: 'orderLineNo',
            title: '单据行号',
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
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_ATT'))}, value, "-");
            }
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
        controlSkuEditor();
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
        // 按钮控制
        controlSkuEditor();
    } else {
        // 隐藏右边tab
        hideTabRight('#skuDetail_tab-right', '#skuDetail_tab-left');
        skuDetail_isShowTab = false;
        // 按钮控制
        controlSkuEditor();
    }
}

/**
 * 加载批次属性控件
 */
function loadLotAtt(row) {
    var params = "ownerCode=" + row.ownerCode + "&skuCode=" + row.skuCode + "&orgId=" + row.orgId;
    bq.get("${ctx}/wms/basicdata/banQinCdWhLotDetail/getInfo?" + params, false, function (data) {
        if (data) {
            data[3].inputControl = 'O';
            $('#skuDetailLotAttTab').empty();
            var html = bq.getLotAttTab(data, 'skuDetail_lotAtt');
            html = html.replace(new RegExp("required", "g"), "");
            html = html.split('<font color="red">*</font>').join('');
            $('#skuDetailLotAttTab').append(html);
            $('#skuDetailLotAttTab .form-control').each(function() {
                $(this).prop('disabled', true);
            });
        }
    })
}

/**
 * tab页刷新
 */
function tabRefresh() {
    // 商品明细tab
    $('#wmQcSkuDetailTable').bootstrapTable('refresh');
    hideTabRight('#skuDetail_tab-right', '#skuDetail_tab-left');
    skuDetail_isShowTab = false;
    // 收货明细tab
    $('#wmQcDetailTable').bootstrapTable('refresh');
    hideTabRight('#qcDetail_tab-right', '#qcDetail_tab-left');
    qcDetail_isShowTab = false;
}

/**
 * 删除明细
 */
function remove() {
    var rows = getSelections('#wmQcSkuDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    var lineNoArray = [];
    for (var x in rows) {
        lineNoArray.push(rows[x].lineNo);
    }
    jp.confirm('确认要删除吗？删除将不能恢复？', function () {
        var params = "qcNo=" + $('#qcNo').val();
        params += "&lineNo=" + lineNoArray.join(',');
        params += "&orgId=" + $('#orgId').val();
        jp.loading("正在删除中...");
        skuDetailCommonMethod('remove', params);
    })
}

/**
 * 质检确认
 */
function qcConfirm() {
    var rows = [];
    if (!skuDetail_isShowTab) {
        rows = getSelections('#wmQcSkuDetailTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        // 非空校验
        var validate = bq.detailSubmitCheck('#skuDetail_tab-right');
        if(!validate.isSuccess) {
            jp.bqError(validate.msg);
            return;
        }
        openDisable("#skuDetailForm");
        var row = {};
        bq.copyProperties(skuDetail_currentRow, row);
        $.extend(row, bq.serializeJson($('#skuDetailForm')));
        rows.push(row);
    }
    jp.loading();
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(rows),
        url: "${ctx}/wms/qc/banQinWmQcSku/qcConfirm",
        success: function (data) {
            if (data.success) {
                window.location = "${ctx}/wms/qc/banQinWmQcHeader/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                closeDisable();
                jp.bqError(data.msg);
            }
        }
    });
}

/**
 * 取消质检
 */
function cancelQcConfirm() {
    var rows = [];
    if (!skuDetail_isShowTab) {
        rows = getSelections('#wmQcSkuDetailTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        openDisable("#skuDetailForm");
        rows.push($('#skuDetailForm').serializeJSON());
    }
    var lineNoArray = [];
    for (var x in rows) {
        lineNoArray.push(rows[x].lineNo);
    }
    var params = "qcNo=" + $('#qcNo').val();
    params += "&lineNo=" + lineNoArray.join(',');
    params += "&orgId=" + $('#orgId').val();
    jp.get("${ctx}/wms/qc/banQinWmQcSku/cancelQcConfirm?" + params, function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/qc/banQinWmQcHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 生成上架任务
 */
function createPaTask() {
    var rows = [];
    if (!skuDetail_isShowTab) {
        rows = getSelections('#wmQcSkuDetailTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        // 非空校验
        var validate = bq.detailSubmitCheck('#qskuDetail_tab-right');
        if(!validate.isSuccess) {
            jp.bqError(validate.msg);
            return;
        }
        openDisable("#skuDetailForm");
        var row = {};
        bq.copyProperties(skuDetail_currentRow, row);
        $.extend(row, bq.serializeJson($('#skuDetailForm')));
        rows.push(row);
    }
    jp.loading();
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(rows),
        url: "${ctx}/wms/qc/banQinWmQcSku/createPaTask",
        success: function (data) {
            if (data.success) {
                // 页面重载
                window.location = "${ctx}/wms/qc/banQinWmQcHeader/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                closeDisable();
                jp.bqError(data.msg);
            }
        }
    });
}

/**
 * 商品明细公共方法
 * @param method
 * @param params
 */
function skuDetailCommonMethod(method, params) {
    jp.get("${ctx}/wms/qc/banQinWmQcSku/" + method + "?" + params, function (data) {
        if (data.success) {
            // tab页刷新
            tabRefresh();
            // 按钮控制
            controlSkuEditor();
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
    sessionStorage.setItem("QC_" + currentLoginName + "_detailTab", index);
}

/**
 * 加载质检项
 */
function loadItemGroup() {
    $('#itemGroupCodeTable').bootstrapTable('destroy');
    $('#itemGroupCodeTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/basicdata/banQinCdWhQcItemDetail/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.itemGroupCode = !$('#skuDetail_itemGroupCode').val() ? '#' : $('#skuDetail_itemGroupCode').val();
            searchParam.orgId = $('#orgId').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'lineNo',
            title: '行号',
            sortable: true
        }, {
            field: 'qcItem',
            title: '质检项名称',
            sortable: true
        }, {
            field: 'qcWay',
            title: '质检方式',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_WAY'))}, value, "-");
            }
        }, {
            field: 'qcRef',
            title: '质检参考标准',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注',
            sortable: true
        }, {
            field: 'def1',
            title: '自定义1',
            sortable: true
        }, {
            field: 'def2',
            title: '自定义2',
            sortable: true
        }, {
            field: 'def3',
            title: '自定义3',
            sortable: true
        }, {
            field: 'def4',
            title: '自定义4',
            sortable: true
        }, {
            field: 'def5',
            title: '自定义5',
            sortable: true
        }]
    });
}

/**************************************商品明细结束*******************************************/

/**************************************质检明细开始********************************************/
/**
 * 初始化明细table
 */
function initWmQcDetailTab() {
    $('#wmQcDetailTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/qc/banQinWmQcDetail/data",
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
            clickDetail('qcDetail', row);
            jp.changeTableStyle($el);
        },
        onDblClickRow: function (row, $el) {
            dbClickDetail('qcDetail', row);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'qcLineNo',
            title: '质检单行号',
            sortable: true
        }, {
            field: 'lineNo',
            title: '行号',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_STATUS'))}, value, "-");
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
            field: 'locCode',
            title: '库位',
            sortable: true
        }, {
            field: 'quaLoc',
            title: '良品库位',
            sortable: true
        }, {
            field: 'unquaLoc',
            title: '不良品库位',
            sortable: true
        }, {
            field: 'traceId',
            title: '跟踪号',
            sortable: true
        }, {
            field: 'quaTraceId',
            title: '良品跟踪号',
            sortable: true
        }, {
            field: 'unquaTraceId',
            title: '不良品跟踪号',
            sortable: true
        }, {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        }, {
            field: 'quaLotNum',
            title: '良品批次号',
            sortable: true
        }, {
            field: 'unquaLotNum',
            title: '不良品批次号',
            sortable: true
        }, {
            field: 'qtyAvailQcUom',
            title: '可质检数',
            sortable: true
        }, {
            field: 'qtyAvailQcEa',
            title: '可质检数EA',
            sortable: true
        }, {
            field: 'qtyQcQuaEa',
            title: '质检合格数EA',
            sortable: true
        }, {
            field: 'qtyQcUnquaEa',
            title: '质检不合格数EA',
            sortable: true
        }, {
            field: 'packDesc',
            title: '包装规格',
            sortable: true
        }, {
            field: 'uom',
            title: '包装单位',
            sortable: true
        }, {
            field: 'qcRuleName',
            title: '质检规则',
            sortable: true
        }, {
            field: 'itemGroupName',
            title: '质检项',
            sortable: true
        }, {
            field: 'qcTime',
            title: '质检时间',
            sortable: true
        }, {
            field: 'orderNo',
            title: '单据号',
            sortable: true
        }, {
            field: 'orderLineNo',
            title: '单据行号',
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
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_ATT'))}, value, "-");
            }
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
        }, {
            field: 'cdType',
            title: '越库类型',
            sortable: true
        }, {
            field: 'cdRcvId',
            title: '越库收货明细号',
            sortable: true
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'soLineNo',
            title: '出库单行号',
            sortable: true
        }]
    });
}

/**
 * 质检明细行单击事件
 * @param tab tab页名称
 * @param row 当前行
 */

function qcDetailClick(row) {
    qcDetail_currentRow = row;
    if (qcDetail_isShowTab) {
        // 表单赋值
        evaluate('qcDetail', 'qcDetail_currentRow');
        // 按钮控制
        controlDetailEditor();
    }
}

/**
 * 质检明细行双击事件
 * @param row 当前行
 */
function qcDetailDbClick(row) {
    qcDetail_currentRow = row;
    if (!qcDetail_isShowTab) {
        // 显示右边tab
        showTabRight('#qcDetail_tab-right', '#qcDetail_tab-left');
        qcDetail_isShowTab = true;
        // 批次屬性控件賦值
        qcDetailLoadLotAtt(row);
        // 表单赋值
        evaluate('qcDetail', 'qcDetail_currentRow');
        // 字段不可用
        $('#qcDetail_status').prop('disabled', true);
    } else {
        // 隐藏右边tab
        hideTabRight('#qcDetail_tab-right', '#qcDetail_tab-left');
        qcDetail_isShowTab = false;
    }
    // 按钮控制
    controlDetailEditor();
}

/**
 * 加载批次属性控件
 */
function qcDetailLoadLotAtt(row) {
    var params = "ownerCode=" + row.ownerCode + "&skuCode=" + row.skuCode + "&orgId=" + row.orgId;
    bq.get("${ctx}/wms/basicdata/banQinCdWhLotDetail/getInfo?" + params, false, function (data) {
        if (data) {
            data[3].inputControl = 'O';
            $('#qcDetailLotAttTab').empty();
            var html = bq.getLotAttTab(data, 'qcDetail_lotAtt');
            html = html.replace(new RegExp("required", "g"), "");
            html = html.split('<font color="red">*</font>').join('');
            $('#qcDetailLotAttTab').append(html);
            $('#qcDetailLotAttTab .form-control').each(function() {
                $(this).prop('disabled', true);
            });
        }
    })
}

/**
 * 保存质检明细
 */
function saveQcDetail() {
    if (!qcDetail_isShowTab) {
        jp.warning("当前无保存记录!");
        return;
    }
    // 表单验证
    var validate = bq.detailSubmitCheck('#qcDetail_tab-right');
    if (validate.isSuccess) {
        if ($('#qcDetail_qtyAvailQcEa').val() < (parseFloat($('#qcDetail_qtyQcQuaEa').val()) + parseFloat($('#qcDetail_qtyQcUnquaEa').val()))) {
            jp.bqError("合格数EA加上不合格数EA不能大于可质检数EA");
            return;
        }
        if (('*' !== $('#qcDetail_quaTraceId').val() && '*' !== $('#qcDetail_unquaTraceId').val()) && $('#qcDetail_quaTraceId').val() === $('#qcDetail_unquaTraceId').val() && ($('#qcDetail_qtyAvailQcEa').val() === ($('#qcDetail_qtyQcQuaEa').val() + $('#qcDetail_qtyQcUnquaEa').val()) && !($('#qcDetail_qtyQcQuaEa').val() === '0' || $('#qcDetail_qtyQcUnquaEa').val() === '0'))) {
            jp.bqError("良品跟踪号和不良品跟踪号不能一致");
            return;
        }
        // 保存前赋值
        openDisable("#qcDetailForm");
        jp.loading("正在保存中...");
        var row = {};
        bq.copyProperties(qcDetail_currentRow, row);
        $.extend(row, bq.serializeJson($('#qcDetailForm')));
        $.ajax({
            url: "${ctx}/wms/qc/banQinWmQcDetail/save",
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
                    closeDisable();
                    jp.bqError(data.msg);
                }
            }
        });
    } else {
        jp.bqError(validate.msg);
    }
}

/**************************************质检明细结束*******************************************/

/**************************************上架任务开始********************************************/
/**
 * 初始化上架任务Tab
 */
function initPaTaskTab() {
    // 隐藏右边tab页
    hideTabRight('#paTask_tab-right', '#paTask_tab-left');
    // 销毁之前表格
    $("#wmQcPaTaskTable").bootstrapTable('destroy');
    // 初始化明细table
    initPaTaskTable();
    // 控制按钮
    controlTaskPaEditor();
}

/**
 * 初始化明细table
 */
function initPaTaskTable() {
    $('#wmQcPaTaskTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/task/banQinWmTaskPa/grid",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orderNo = !$('#qcNo').val() ? '#' : $('#qcNo').val();
            searchParam.orderType = 'QC';
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
        }, {
            field: 'paId',
            title: '上架任务Id',
            sortable: true
        }, {
            field: 'lineNo',
            title: '上架任务行号',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_TASK_STATUS'))}, value, "-");
            }
        }, {
            field: 'orderType',
            title: '单据类型',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ORDER_TYPE'))}, value, "-");
            }
        }, {
            field: 'orderNo',
            title: '单据号',
            sortable: true
        }, {
            field: 'ownerCode',
            title: '货主编码',
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
            field: 'fmLoc',
            title: '源库位',
            sortable: true
        }, {
            field: 'fmId',
            title: '源跟踪号',
            sortable: true
        }, {
            field: 'suggestLoc',
            title: '推荐库位',
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
            field: 'qtyPaUom',
            title: '上架数',
            sortable: true
        }, {
            field: 'qtyPaEa',
            title: '上架数EA',
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
            field: 'paOp',
            title: '上架人',
            sortable: true
        }, {
            field: 'paTime',
            title: '上架时间',
            sortable: true
        }, {
            field: 'reserveCode',
            title: '上架库位指定规则',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_RESERVE_CODE'))}, value, "-");
            }
        }, {
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
        rows = getSelections('#wmQcPaTaskTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        // 校验批次属性非空
        openDisable("#paTaskForm");
        var row = {};
        bq.copyProperties(paTask_currentRow, row);
        $.extend(row, bq.serializeJson($('#paTaskForm')));
        rows.push(row);
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
            url: "${ctx}/wms/qc/banQinWmQcDetail/deleteAll",
            success: function (data) {
                if (data.success) {
                    // 上架任务tab
                    $('#wmQcPaTaskTable').bootstrapTable('refresh');
                    hideTabRight('#paTask_tab-right', '#paTask_tab-left');
                    paTask_isShowTab = false;
                    jp.success(data.msg);
                } else {
                    closeDisable();
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
        rows = getSelections('#wmQcPaTaskTable');
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
        openDisable("#paTaskForm");
        var row = {};
        bq.copyProperties(paTask_currentRow, row);
        $.extend(row, bq.serializeJson($('#paTaskForm')));
        rows.push(row);
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
        url: "${ctx}/wms/qc/banQinWmQcDetail/putAway",
        success: function (data) {
            if (data.success) {
                // 上架任务tab
                $('#wmQcPaTaskTable').bootstrapTable('refresh');
                hideTabRight('#paTask_tab-right', '#paTask_tab-left');
                paTask_isShowTab = false;
                jp.success(data.msg);
            } else {
                closeDisable();
                jp.bqError(data.msg);
            }
        }
    });
}

/**************************************上架任务结束*******************************************/
    
</script>