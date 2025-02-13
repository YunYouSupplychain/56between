<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var currentRow_detail; // 明细当前行
var currentRow_serial; // 明细当前行
var isShowTab = false; // 是否显示右边tab页
var isShowTab_serial = false; // 是否显示右边tab页
var currentLoginName = '${fns:getUser().loginName}';
var disabledObj = [];

$(document).ready(function () {
    // 初始化
    init();
    // 初始化明细tab
    initDetailTable();
    // 初始化序列号明细tab
    initSerialTable();
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
    var detailTabIndex = sessionStorage.getItem("SC_" + currentLoginName +"_detailTab");
    detailTabIndex = !detailTabIndex ? 0 : detailTabIndex;
    // 默认选择加载Tab页
    $("#detailTab li:eq(" + detailTabIndex + ") a").tab('show');
}

/**
 * 明细Tab页索引切换
 */
function detailTabChange(index) {
    sessionStorage.setItem("SC_" + currentLoginName + "_detailTab", index);
}

/**
 * 表头保存
 * @returns {boolean}
 */
function save() {
    if (beforeSaveHeader()) {
        var isValidate = bq.headerSubmitCheck('#inputForm');
        if (isValidate.isSuccess) {
            openDisable('#inputForm');
            jp.loading();
            jp.post("${ctx}/wms/inventory/banQinWmCountSerial/save", $('#inputForm').serialize(), function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    window.location = "${ctx}/wms/inventory/banQinWmCountSerial/form?id=" + data.body.entity.id;
                } else {
                    closeDisable();
                    jp.bqError(data.msg);
                }
            })
        } else {
            jp.bqError(isValidate.msg);
        }
    }
}

function beforeSaveHeader() {
    var flag = true;
    var errorMsg = '';
    if ($('#countRange').val() === '3') {
        if (!$('#takeStartTime').val()) {
            errorMsg = '动盘时间从不能为空\n';
            flag = false;
        }
        if (!$('#takeEndTime').val()) {
            errorMsg += '动盘时间从不能为空';
            flag = false;
        }
    } else if ($('#countRange').val() === '2') {
        if (!$('#randomRate').val()) {
            errorMsg = '抽盘比例不能为空\n';
            flag = false;
        }
        if (!$('#randomNum').val()) {
            errorMsg += '抽盘比例不能为空';
            flag = false;
        }
    }
    if (errorMsg) {
        jp.bqError(errorMsg);
    }

    return flag;
}

function createCount() {
    commonMethod('createCount');
}

function createRecount() {
    commonMethod('createRecount');
}

function cancelCountTask() {
    commonMethod('cancelCountTask');
}

function closeCount() {
    commonMethod('closeCount');
}

function cancelCount() {
    commonMethod('cancelCount');
}

function createAd() {
    jp.loading();
    jp.get("${ctx}/wms/inventory/banQinWmCountSerial/createAd?id=" + $('#id').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/inventory/banQinWmCountSerial/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

function commonMethod(method) {
    jp.loading();
    jp.get("${ctx}/wms/inventory/banQinWmCountSerial/" + method + "?ids=" + $('#id').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/inventory/banQinWmCountSerial/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 控制单头按钮
 * 1、只有创建状态的才能删除、保存,才能 生成普通盘点任务
 * 2、只有完全盘点的盘点单才能 生成复盘任务
 * 3、部分盘点或者完全盘点时才能 关闭盘点
 * 4、盘点单为创建或者生成任务,可以 取消盘点
 * 5、只有生成任务状态的盘点单才可以 取消盘点任务
 * 6、完全盘点并且没有生成过调整单的才能 生成调整单
 */
function controlHeaderEditor() {
    enableCountButton(true);
    switch ($('#status').val()) {
        case '00':
            $('#header_cancelCount').attr('disabled', false);
            $('#header_createCount').attr('disabled', false);
            $('#header_save').attr('disabled', false);
            break;
        case '10':
            $('#header_cancelCountTask').attr('disabled', false);
            $('#header_cancelCount').attr('disabled', false);
            break;
        case '70':
            $('#header_closeCount').attr('disabled', false);
            break;
        case '80':
            $('#header_closeCount').attr('disabled', false);
            // 判断是否生成过复盘任务
            if ($('#isCreateCheck').val() === 'N') {
                $('#header_createRecount').attr('disabled', false);
            }
            // 判断是否生成过调整单
            if ($('#isCreateAdCheck').val() === 'N') {
                $('#header_createAd').attr('disabled', false);
            }
            break;
        default:
            break;
    }
}

function setTaskToolbarByCount() {
    // 如果盘点单是创建、关闭和取消的，或是动态盘点，不能对任务进行任何操作
    var status = $('#status').val();
    if ('90' === status || '99' === status || '80' === status || '00' === status || 'D' === $('#countMethod').val()) {
        lockTaskToolbar(true);
    }
    // 如果盘点单是完全盘点的，不能对任务进行删除和盘点确认
    if ('80' === status) {
        $('#detail_confirm').attr('disabled', true);
        $('#detail_add').attr('disabled', true);
        $('#detail_remove').attr('disabled', true);
        $('#header_createRecount').attr('disabled', false);
        $('#header_createAd').attr('disabled', false);
    }
}

/**
 * 根据盘点任务的状态设置盘点任务按钮的灰置
 */
function setTaskToolbarByTask() {
    // 任务是完成的状态
    if (currentRow_detail.status === '99') {
        enableTaskForm(true);
        $('#detail_confirm').attr('disabled', true);
        $('#detail_remove').attr('disabled', true);
    }
    // 任务是创建的状态
    else if (currentRow_detail.status === '00') {
        // 任务是系统自动生成的
        if (currentRow_detail.dataSource === 'SYSTEM') {
            enableTaskForm(true);
        }
        // 任务是手工创建的
        else if (currentRow_detail.dataSource === 'MANUAL') {
            enableTaskForm(false);
        }
    }
}

/**
 * 是否允许盘点任务编辑面板可以编辑
 */
function enableTaskForm(isEnabled) {
    $('#detail_ownerName').prop('readonly', isEnabled);
    $('#detail_ownerSelectId').prop('disabled', isEnabled);
    $('#detail_ownerDeleteId').prop('disabled', isEnabled);
    $('#detail_skuName').prop('readonly', isEnabled);
    $('#detail_skuSelectId').prop('disabled', isEnabled);
    $('#detail_skuDeleteId').prop('disabled', isEnabled);
    $('#detail_locCode').prop('readonly', isEnabled);
    $('#detail_locSelectId').prop('disabled', isEnabled);
    $('#detail_locDeleteId').prop('disabled', isEnabled);
    $('#detail_traceId').prop('readonly', isEnabled);
    $('#detail_qtyCountEa').prop('readonly', isEnabled);

    $("input[id^='detail_lotAtt']").each(function() {
        $(this).prop('disabled', isEnabled);
    });
    $("select[id^='detail_lotAtt']").each(function() {
        $(this).prop('disabled', isEnabled);
    });
}

/**
 * 是否盘点单按钮可以点击
 */
function enableCountButton(isEnabled) {
    $('#header_save').attr('disabled', isEnabled);
    $('#header_createCount').attr('disabled', isEnabled);
    $('#header_createRecount').attr('disabled', isEnabled);
    $('#header_cancelCountTask').attr('disabled', isEnabled);
    $('#header_closeCount').attr('disabled', isEnabled);
    $('#header_cancelCount').attr('disabled', isEnabled);
    $('#header_createAd').attr('disabled', isEnabled);
}

/**
 * 序列号页签按钮控制
 */
function controlSerial() {
    if ($('#status').val() === '10' || $('#status').val() === '70') {
        if (isShowTab_serial) {
            if ($('#serial_status').val() === '00') {
                $('#serial_comfirmSerial').attr('disabled', false);
                $('#serial_comfirmLoss').attr('disabled', false);
                $('#serial_deleteSerial').attr('disabled', true);
            } else {
                $('#serial_comfirmSerial').attr('disabled', true);
                $('#serial_comfirmLoss').attr('disabled', true);
                $('#serial_deleteSerial').attr('disabled', false);
            }
        } else {
            $('#serial_comfirmSerial').attr('disabled', false);
            $('#serial_comfirmLoss').attr('disabled', false);
            $('#serial_deleteSerial').attr('disabled', false);
        }
    }
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
 */
function showTabRight($tabRight, $tabLeft) {
    $($tabRight).show();
    $($tabLeft).addClass("div-left");
}

/**
 * 隐藏右边tab
 */
function hideTabRight($tabRight, $tabLeft) {
    $($tabRight).hide();
    $($tabLeft).removeClass("div-left");
}

/**
 * 初始化
 */
function init() {
    // 初始化标签
    initLabel();
    // 初始化按钮
    if (!$('#id').val()) {
        enableCountButton(true);
        lockTaskToolbar(true);
        $('#header_save').attr('disabled', false);
    } else {
        controlHeaderEditor();
        setTaskToolbarByCount();
    }
    $('#lotAtt01').datetimepicker({format: "YYYY-MM-DD"});
    $('#lotAtt02').datetimepicker({format: "YYYY-MM-DD"});
    $('#lotAtt03').datetimepicker({format: "YYYY-MM-DD"});
}

function lockTaskToolbar(isLock) {
    if ($('#id').val() && $('#countMethod').val() === 'D') {
        $('#detail_confirm').attr('disabled', true);
        $('#serial_comfirmSerial').attr('disabled', true);
        $('#serial_comfirmLoss').attr('disabled', true);
        $('#serial_addSerial').attr('disabled', true);
        $('#serial_deleteSerial').attr('disabled', true);
        $('#serial_duplicateSerial').attr('disabled', true);
    } else {
        $('#detail_confirm').attr('disabled', isLock);
        $('#serial_comfirmSerial').attr('disabled', isLock);
        $('#serial_comfirmLoss').attr('disabled', isLock);
        $('#serial_addSerial').attr('disabled', isLock);
        $('#serial_deleteSerial').attr('disabled', isLock);
        $('#serial_duplicateSerial').attr('disabled', isLock);
    }
}

/**
 * 初始化标签状态
 */
function initLabel() {
    if (!$('#id').val()) {
        $('#orgId').val(jp.getCurrentOrg().orgId);
        $("#countMethod option:eq(1)").attr("selected", 'selected');
        $("#countMode option:eq(1)").attr("selected", 'selected');
        $('#isSerial').val('Y');
    } else {
        if ($('#countRange').val() === '3') {
            $('#takeStartTime').prop('disabled', false);
            $('#takeEndTime').prop('disabled', false);
            laydate.render({elem: '#takeStartTime', theme: '#393D49', type: 'datetime'});
            laydate.render({elem: '#takeEndTime', theme: '#393D49', type: 'datetime'});
        } else if ($('#countRange').val() === '2') {
            $('#randomRate').prop('readonly', false);
            $('#randomNum').prop('readonly', false);
        }
    }
}

/**
 * 初始化明细table
 */
function initDetailTable() {
    $('#wmTaskCountTable').bootstrapTable({
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inventory/banQinWmTaskCount/data",
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
            field: 'countId',
            title: '盘点任务Id',
            sortable: true
        },
        {
            field: 'status',
            title: '盘点状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_TASK_STATUS'))}, value, "-");
            }
        },
        {
            field: 'ownerName',
            title: '货主',
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
            field: 'qty',
            title: '库存数EA',
            sortable: true,
        },
        {
            field: 'qtyCountEa',
            title: '盘点数EA',
            sortable: true
        },
        {
            field: 'qtyDiff',
            title: '损益数EA',
            sortable: true,
            cellStyle: function (value, row, index) {
                return {css: {"background-color": "#fdfdfe"}};
                // if (row.qtyCountEa > row.qty){
                //     return {css: {"background-color": "#50ff11"}}
                // } else if (row.qtyCountEa < row.qty) {
                //     return {css: {"background-color": "#FF0000"}}
                // } else {
                //     return {css: {"background-color": "#fdfdfe"}};
                // }
            }
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
            title: '品质',
            sortable: true
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
        // onClickCell: function (field, value, row, $element) {
        //     if (field === 'qtyCountEa') {
        //         $element.css('backgroundColor', 'white');
        //         $element.attr('contenteditable', true);
        //         $element.blur(function() {
        //             var index = $element.parent().data('index');
        //             var tdValue = $element.html();
        //             saveData(index, field, tdValue);
        //             saveData(index, 'qtyDiff', row.qtyCountEa - row.qty);
        //         });
        //     }
        // }
    });
}

function saveData(index, field, value) {
    $("#wmTaskCountTable").bootstrapTable('updateCell', {
        index: index,
        field: field,
        value: value
    });
}

/**
 * 获取表格勾选行Ids
 */
function getIdSelections() {
    return $.map($("#wmTaskCountTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

/**
 * 获取表格勾选行
 */
function getSelections() {
    return $.map($("#wmTaskCountTable").bootstrapTable('getSelections'), function (row) {
        return row
    });
}

/**
 * 单击表格事件
 * @param row 当前行
 */
function clickDetail(row) {
    currentRow_detail = row;
    if (isShowTab) {
        // 表单赋值
        evaluate('detail', 'currentRow_detail');
        // 设置明细按钮
        lockTaskToolbar(false);
        setTaskToolbarByTask();
        setTaskToolbarByCount();
    }
}

/**
 * 双击表格事件
 * @param row 当前行
 */
function dbClickDetail(row) {
    currentRow_detail = row;
    if (!isShowTab) {
        // 显示右边tab
        showTabRight('#tab1-right', '#tab1-left');
        isShowTab = true;
        // 加载批次属性控件
        loadLotAtt(row);
        // 表单赋值
        evaluate('detail', 'currentRow_detail');
        // 设置明细按钮
        setTaskToolbarByTask();
    } else {
        // 隐藏右边tab
        hideTabRight('#tab1-right', '#tab1-left');
        isShowTab = false;
        lockTaskToolbar(false);
        setTaskToolbarByCount();
    }
}

/**
 * 单击表格事件
 * @param row 当前行
 */
function clickSerial(row) {
    currentRow_serial = row;
    if (isShowTab_serial) {
        // 表单赋值
        evaluate('serial', 'currentRow_serial');
        // 设置按钮
        controlSerial();
        enableSerialForm(true);
    }
}

/**
 * 双击表格事件
 * @param row 当前行
 */
function dbClickSerial(row) {
    currentRow_serial = row;
    if (!isShowTab_serial) {
        // 显示右边tab
        showTabRight('#tab2-right', '#tab2-left');
        isShowTab_serial = true;
        // 加载批次属性控件
        loadSerialLotAtt(row);
        // 表单赋值
        evaluate('serial', 'currentRow_serial');
        controlSerial();
        enableSerialForm(true);
    } else {
        // 隐藏右边tab
        hideTabRight('#tab2-right', '#tab2-left');
        isShowTab_serial = false;
        controlSerial();
        enableSerialForm(true);
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
 * 盘点确认
 */
function confirmDetail() {
    var rows= [];
    if (!isShowTab) {
        rows = getSelections();
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        var validate = bq.detailSubmitCheck('#tab1-right');
        if (validate.isSuccess) {
            openDisable("#inputForm1");
            rows.push($('#inputForm1').serializeJSON());
        } else {
            closeDisable();
            jp.bqError(validate.msg);
            return;
        }
    }
    jp.loading();
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(rows),
        url: "${ctx}/wms/inventory/banQinWmTaskCountSerial/confirmSerialCount",
        success: function (data) {
            if (data.success) {
                window.location = "${ctx}/wms/inventory/banQinWmCountSerial/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
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
    });
}

/**
 * 加载批次属性控件
 */
function loadSerialLotAtt(row) {
    var params = "ownerCode=" + row.ownerCode + "&skuCode=" + row.skuCode + "&orgId=" + row.orgId;
    bq.get("${ctx}/wms/basicdata/banQinCdWhLotDetail/getInfo?" + params, false, function (data) {
        if (data) {
            $('#serialLotAttTab').empty();
            var html = bq.getLotAttTab(data, 'serial_lotAtt');
            $('#serialLotAttTab').append(html);
        }
    });
}

function selectOwner(rows) {
    var ownerCodes = [], ownerNames = [];
    for (var index in rows) {
        ownerCodes.push(rows[index].ebcuCustomerNo);
        ownerNames.push(rows[index].ebcuNameCn);
    }
    if ($('#ownerCode').val() !== ownerCodes.join(',')) {
        $('#skuCode').val('');
        $('#skuName').val('');
    }
    $('#ownerCode').val(ownerCodes.join(','));
    $('#ownerName').val(ownerNames.join(','));
}

function selectSku(rows) {
    var skuCodes = [], skuNames = [], ownerCodes = [], ownerNames = [];
    for (var index in rows) {
        skuCodes.push(rows[index].skuCode);
        skuNames.push(rows[index].skuName);
        if (ownerCodes.indexOf(rows[index].ownerCode) !== -1) {
            ownerCodes.push(rows[index].ownerCode);
            ownerNames.push(rows[index].ownerName);
        }
    }
    if (!$('#ownerCode').val()) {
        $('#ownerCode').val(ownerCodes.join(','));
        $('#ownerName').val(ownerNames.join(','));
    }
    $('#skuCode').val(skuCodes.join(','));
    $('#skuName').val(skuNames.join(','));
}

function selectArea(rows) {
    var areaCodes = [], areaNames = [];
    for (var index in rows) {
        areaCodes.push(rows[index].areaCode);
        areaNames.push(rows[index].areaName);
    }
    $('#areaCode').val(areaCodes.join(','));
    $('#areaName').val(areaNames.join(','));
}

function selectZone(rows) {
    var zoneCodes = [], zoneNames = [];
    for (var index in rows) {
        zoneCodes.push(rows[index].zoneCode);
        zoneNames.push(rows[index].zoneName);
    }
    $('#zoneCode').val(zoneCodes.join(','));
    $('#zoneName').val(zoneNames.join(','));
}

/**
 * 初始化序列号明细table
 */
function initSerialTable() {
    $('#wmTaskCountSerialTable').bootstrapTable({
        cache: false,
        pagination: true,
        sidePagination: "server",
        url: "${ctx}/wms/inventory/banQinWmTaskCountSerial/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.countNo = !$('#countNo').val() ? "#" : $('#countNo').val();
            searchParam.orgId = $('#orgId').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function(row, $el) {
            jp.changeTableStyle($el);
            clickSerial(row);
        },
        onDblClickRow: function (row, $el) {
            dbClickSerial(row);
        },
        columns: [{
            checkbox: true
        },
        {
            field: 'status',
            title: '盘点状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_TASK_STATUS'))}, value, "-");
            }
        },
        {
            field: 'countResult',
            title: '盘点结果',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_COUNT_RESULT'))}, value, "-");
            }
        },
        {
            field: 'ownerName',
            title: '货主',
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
            field: 'serialNo',
            title: '序列号',
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
        }]
    });
}

/**
 * 是否允许盘点序列号编辑面板可以编辑
 */
function enableSerialForm(isEnabled) {
    $('#serial_ownerName').prop('readonly', isEnabled);
    $('#serial_ownerSelectId').prop('disabled', isEnabled);
    $('#serial_ownerDeleteId').prop('disabled', isEnabled);
    $('#serial_skuName').prop('readonly', isEnabled);
    $('#serial_skuSelectId').prop('disabled', isEnabled);
    $('#serial_skuDeleteId').prop('disabled', isEnabled);
    $('#serial_serialNo').prop('disabled', isEnabled);
    $('#serial_lotNum').prop('readonly', isEnabled);
    $('#serial_lotNumSelectId').prop('disabled', isEnabled);
    $('#serial_lotNumDeleteId').prop('disabled', isEnabled);

    $("input[id^='serial_lotAtt']").each(function() {
        $(this).prop('disabled', isEnabled);
    });
    $("select[id^='serial_lotAtt']").each(function() {
        $(this).prop('disabled', isEnabled);
    });
}

/**
 * 序列号盘点确认
 */
function comfirmSerHandler() {
    if (isShowTab_serial) {
        var isValidate = bq.detailSubmitCheck('#inputForm2');
        if (isValidate.isSuccess) {
            openDisable("#inputForm2");
            jp.loading();
            jp.post("${ctx}/wms/inventory/banQinWmTaskCountSerial/scanSerialNo", $('#inputForm2').bq_serialize(), function (data) {
                if (data.success) {
                    window.location = "${ctx}/wms/inventory/banQinWmCountSerial/form?id=" + $('#id').val();
                    jp.success(data.msg);
                } else {
                    closeDisable();
                    jp.bqError(data.msg);
                }
            });
        } else {
            jp.bqError(isValidate.msg);
        }
    } else {
        var rows = $('#wmTaskCountSerialTable').bootstrapTable('getSelections');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录");
            return;
        }
        jp.loading();
        $.ajax({
            headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
            type: 'POST',
            dataType: "json",
            data: JSON.stringify(rows),
            url: "${ctx}/wms/inventory/banQinWmTaskCountSerial/batchComfirmCount",
            success: function (data) {
                if (data.success) {
                    window.location = "${ctx}/wms/inventory/banQinWmCountSerial/form?id=" + $('#id').val();
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            }
        });
    }
}

/**
 * 序列号盘点，盘亏确认
 */
function comfirmLossHandler() {
    if (isShowTab_serial) {
        openDisable("#inputForm2");
        jp.loading();
        jp.post("${ctx}/wms/inventory/banQinWmTaskCountSerial/comfirmLoss", $('#inputForm2').bq_serialize(), function (data) {
            if (data.success) {
                window.location = "${ctx}/wms/inventory/banQinWmCountSerial/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                closeDisable();
                jp.bqError(data.msg);
            }
        });
    } else {
        var rows = $('#wmTaskCountSerialTable').bootstrapTable('getSelections');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录");
            return;
        }
        jp.loading();
        $.ajax({
            headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
            type: 'POST',
            dataType: "json",
            data: JSON.stringify(rows),
            url: "${ctx}/wms/inventory/banQinWmTaskCountSerial/batchComfirmLoss",
            success: function (data) {
                if (data.success) {
                    window.location = "${ctx}/wms/inventory/banQinWmCountSerial/form?id=" + $('#id').val();
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            }
        });
    }
}

/**
 * 取消序列号盘点确认
 */
function deleteSerHandler() {
    var rows = [];
    if (isShowTab_serial) {
        openDisable("#inputForm2");
        rows.push($('#inputForm2').serializeJSON());
    } else {
        var row = $('#wmTaskCountSerialTable').bootstrapTable('getSelections');
        if (row.length === 0) {
            jp.bqError("请选择一条记录");
            return;
        }
        rows = row;
    }
    jp.loading();
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(rows),
        url: "${ctx}/wms/inventory/banQinWmTaskCountSerial/deleteSerialTask",
        success: function (data) {
            if (data.success) {
                window.location = "${ctx}/wms/inventory/banQinWmCountSerial/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

/**
 * 新增
 */
function addSerHandler() {
    // 显示右侧信息
    isShowTab_serial = true;
    showTabRight('#tab2-right', '#tab2-left');
    // 清空表单
    $(':input', '#inputForm2').val('');
    $('#serial_status').val('00');
    $('#serial_orgId').val($('#orgId').val());
    $('#serial_countNo').val($('#countNo').val());
    $('#serial_headerId').val($('#id').val());
    enableSerialForm(false);
    controlSerial();
}

function afterSelectSku(row) {
    loadSerialLotAtt(row);
}

function afterSelectOwner(row) {
    $('#serial_skuCode').val('');
    $('#serial_skuName').val('')
}

</script>