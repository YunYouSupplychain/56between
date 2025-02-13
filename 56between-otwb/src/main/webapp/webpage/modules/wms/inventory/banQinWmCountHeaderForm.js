<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var currentRow; // 明细当前行
var isShowTab = false; // 是否显示右边tab页
var disabledObj = [];

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
    if (beforeSaveHeader()) {
        var isValidate = bq.headerSubmitCheck('#inputForm');
        if (isValidate.isSuccess) {
            openDisable('#inputForm');
            jp.loading();
            jp.post("${ctx}/wms/inventory/banQinWmCountHeader/save", $('#inputForm').serialize(), function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    window.location = "${ctx}/wms/inventory/banQinWmCountHeader/form?id=" + data.body.entity.id;
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
    jp.get("${ctx}/wms/inventory/banQinWmCountHeader/createAd?id=" + $('#id').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/inventory/banQinWmCountHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

function commonMethod(method) {
    jp.loading();
    jp.get("${ctx}/wms/inventory/banQinWmCountHeader/" + method + "?ids=" + $('#id').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/inventory/banQinWmCountHeader/form?id=" + $('#id').val();
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
    if (currentRow.status === '99') {
        enableTaskForm(true);
        $('#detail_confirm').attr('disabled', true);
        $('#detail_remove').attr('disabled', true);
    }
    // 任务是创建的状态
    else if (currentRow.status === '00') {
        // 任务是系统自动生成的
        if (currentRow.dataSource === 'SYSTEM') {
            enableTaskForm(true);
            $('#detail_qtyCountEa').prop('readonly', false);
        }
        // 任务是手工创建的
        else if (currentRow.dataSource === 'MANUAL') {
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
        $('#detail_add').attr('disabled', true);
        $('#detail_remove').attr('disabled', true);
    } else {
        $('#detail_confirm').attr('disabled', isLock);
        $('#detail_add').attr('disabled', isLock);
        $('#detail_remove').attr('disabled', isLock);
    }
}

/**
 * 初始化标签状态
 */
function initLabel() {
    if (!$('#id').val()) {
        $('#orgId').val(jp.getCurrentOrg().orgId);
        $("#countRange option:eq(1)").attr("selected", true);
        $("#countMethod option:eq(1)").attr("selected", 'selected');
        $("#countMode option:eq(1)").attr("selected", 'selected');
        $('#isCreateCheck').val('N');
        $('#isSerial').val('N');
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

function countRangeChange() {
    $('#takeStartTime').val('');
    $('#takeEndTime').val('');
    $('#randomRate').val('');
    $('#randomNum').val('');
    enableTakeRan(true);
    switch ($('#countRange').val()) {
        case '1':
        case '4':
            break;
        case '3':
            $('#takeStartTime').prop('disabled', false);
            $('#takeEndTime').prop('disabled', false);
            laydate.render({elem: '#takeStartTime', theme: '#393D49', type: 'datetime'});
            laydate.render({elem: '#takeEndTime', theme: '#393D49',type: 'datetime'});
            break;
        case '2':
            $('#randomRate').prop('readonly', false);
            $('#randomNum').prop('readonly', false);
            break;
    }
}

/**
 * 是否允许动盘和抽盘的可以编辑,并设置值为空
 */
function enableTakeRan(isEnabled) {
    // 编辑框设置
    $('#takeStartTime').prop('disabled', isEnabled);
    $('#takeEndTime').prop('disabled', isEnabled);
    $('#randomRate').prop('readonly', isEnabled);
    $('#randomNum').prop('readonly', isEnabled);
}

/**
 * 初始化明细table
 */
function initDetailTable() {
    $('#wmTaskCountTable').bootstrapTable({
        cache: false,// 是否使用缓存
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
        }, {
            field: 'countId',
            title: '盘点任务Id',
            sortable: true
        }, {
            field: 'status',
            title: '盘点状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_TASK_STATUS'))}, value, "-");
            }
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
        }, {
            field: 'locCode',
            title: '库位',
            sortable: true
        }, {
            field: 'traceId',
            title: '跟踪号',
            sortable: true
        }, {
            field: 'qty',
            title: '库存数EA',
            sortable: true,
        }, {
            field: 'qtyCountEa',
            title: '盘点数EA',
            sortable: true
        }, {
            field: 'qtyDiff',
            title: '损益数EA',
            sortable: true,
            cellStyle: function (value, row, index) {
                if (row.qtyCountEa > row.qty){
                    return {css: {"background-color": "#50ff11"}}
                } else if (row.qtyCountEa < row.qty) {
                    return {css: {"background-color": "#FF0000"}}
                } else {
                    return {css: {"background-color": "#fdfdfe"}};
                }
            }
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
            title: '品质',
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
        }],
        onClickCell: function (field, value, row, $element) {
            if (field === 'qtyCountEa') {
                $element.css('backgroundColor', 'white');
                $element.attr('contenteditable', true);
                $element.blur(function() {
                    var index = $element.parent().data('index');
                    var tdValue = $element.html();
                    saveData(index, field, tdValue);
                    saveData(index, 'qtyDiff', row.qtyCountEa - row.qty);
                });
            }
        }
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
    currentRow = row;
    if (isShowTab) {
        // 表单赋值
        evaluate();
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
    currentRow = row;
    if (!isShowTab) {
        // 显示右边tab
        showTabRight();
        // 加载批次属性控件
        loadLotAtt(row);
        // 表单赋值
        evaluate();
        // 设置明细按钮
        setTaskToolbarByTask();
    } else {
        // 隐藏右边tab
        hideTabRight();
        lockTaskToolbar(false);
        setTaskToolbarByCount();
    }
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
}

/**
 * 初始化明细控件状态
 */
function initDetailLabel() {
    $('#detail_status').prop('disabled', true).find('option').eq(0).prop('selected', true);
    $('#detail_remove').attr('disabled', true);
    $('#detail_qty').val('0');
    $('#detailLotAttTab').empty();
    enableTaskForm(false);
}

/**
 * 盘点确认
 */
function confirmDetail() {
    var rows= [];
    var method = "confirmDetail";
    if (!isShowTab) {
        rows = getSelections();
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        if (!$('#detail_qtyCountEa').val()) {
            jp.bqError("盘点数不能为空");
            return;
        }
        if ($('#detail_id').val()) {
            var row = {};
            bq.copyProperties(currentRow, row);
            $.extend(row, bq.serializeJson($('#inputForm1')));
            row.qtyDiff = row.qtyCountEa - row.qty;
            rows.push(row);
        } else {
            var validate = bq.detailSubmitCheck('#tab1-right');
            if (validate.isSuccess) {
                beforeSave();
                openDisable("#inputForm1");
                var row = {};
                bq.copyProperties(currentRow, row);
                $.extend(row, bq.serializeJson($('#inputForm1')));
                rows.push(row);
                method = "addConfirmCount";
            } else {
                closeDisable();
                jp.bqError(validate.msg);
                return;
            }
        }
    }

    jp.loading();
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(rows),
        url: "${ctx}/wms/inventory/banQinWmTaskCount/" + method,
        success: function (data) {
            if (data.success) {
                window.location = "${ctx}/wms/inventory/banQinWmCountHeader/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

/**
 * 保存前赋值
 */
function beforeSave() {
    if (!$('#detail_id').val()) {
        $('#detail_orgId').val($('#orgId').val());
        $('#detail_countNo').val($('#countNo').val());
        $('#detail_headerId').val($('#id').val());
        $('#detail_dataSource').val('MANUAL');
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
        var rows = getSelections();
        var delRows = [];
        for (var i = 0; i < rows.length; i++) {
            if (rows[i].status === '00') {
                delRows.push(rows[i].id);
            }
        }
        if (delRows.length < 1) {
            // 对完成的盘点明细进行删除增加提示信息
            jp.bqError('盘点任务已完成，不能操作');
            return;
        }
        jp.loading("正在删除中...");
        jp.get("${ctx}/wms/inventory/banQinWmTaskCount/deleteAll?ids=" + delRows.join(",") + "&headerId=" + $('#id').val(), function (data) {
            if (data.success) {
                $('#wmTaskCountTable').bootstrapTable('refresh');
                hideTabRight();
                // 按钮控制
                controlHeaderEditor();
                jp.success(data.msg);
            } else {
                closeDisable();
                jp.bqError(data.msg);
            }
        })
    })
}

function afterSelectSku(row) {
    loadLotAtt({ownerCode: $('#ownerCode').val(), skuCode: row.skuCode, orgId: row.orgId});
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

function selectOwner(rows) {
    if (Array.isArray(rows)) {
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
    } else {
        $('#ownerCode').val(rows.ebcuCustomerNo);
        $('#ownerName').val(rows.ebcuNameCn);
    }
}

function selectSku(rows) {
    if (Array.isArray(rows)) {
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
    } else {
        $('#skuCode').val(rows.skuCode);
        $('#skuName').val(rows.skuName);
    }
}

function selectArea(rows) {
    if (Array.isArray(rows)) {
        var areaCodes = [], areaNames = [];
        for (var index in rows) {
            areaCodes.push(rows[index].areaCode);
            areaNames.push(rows[index].areaName);
        }
        $('#areaCode').val(areaCodes.join(','));
        $('#areaName').val(areaNames.join(','));
    } else {
        $('#areaCode').val(rows.areaCode);
        $('#areaName').val(rows.areaName);
    }
}

function selectZone(rows) {
    if (Array.isArray(rows)) {
        var zoneCodes = [], zoneNames = [];
        for (var index in rows) {
            zoneCodes.push(rows[index].zoneCode);
            zoneNames.push(rows[index].zoneName);
        }
        $('#zoneCode').val(zoneCodes.join(','));
        $('#zoneName').val(zoneNames.join(','));
    } else {
        $('#zoneCode').val(rows.zoneCode);
        $('#zoneName').val(rows.zoneName);
    }
}

</script>