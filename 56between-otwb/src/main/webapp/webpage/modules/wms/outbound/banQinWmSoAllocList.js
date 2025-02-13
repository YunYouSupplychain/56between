<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#beginOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#endOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#statuss").selectpicker('val', ['30', '40']).selectpicker('refresh');
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinWmSoAllocTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $("#statuss").selectpicker('val', ['30', '40']).selectpicker('refresh');
        $('#banQinWmSoAllocTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#banQinWmSoAllocTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/outbound/banQinWmSoAlloc/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.statuss = $('#statuss').selectpicker('val') ? $('#statuss').selectpicker('val').join(',') : '';
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'allocId',
            title: '拣货任务Id',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
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
            field: 'waveNo',
            title: '波次单号',
            sortable: true
        }, {
            field: 'pickNo',
            title: '拣货单号',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_STATUS'))}, value, "-");
            }
        }, {
            field: 'consigneeName',
            title: '收货人',
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
        }, {
            field: 'locCode',
            title: '源库位',
            sortable: true
        }, {
            field: 'traceId',
            title: '源跟踪号',
            sortable: true
        }, {
            field: 'consigneeName',
            title: '收货人',
            sortable: true
        }, {
            field: 'packDesc',
            title: '包装规格',
            sortable: true
        }, {
            field: 'uomDesc',
            title: '包装单位',
            sortable: true
        }, {
            field: 'qtyUom',
            title: '拣货数',
            sortable: true
        }, {
            field: 'qtyEa',
            title: '拣货数EA',
            sortable: true
        }, {
            field: 'lotAtt05',
            title: '批次属性5',
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
            field: 'createBy.name',
            title: '创建人',
            sortable: true
        }, {
            field: 'createDate',
            title: '创建时间',
            sortable: true
        }, {
            field: 'updateBy.name',
            title: '修改人',
            sortable: true
        }, {
            field: 'updateDate',
            title: '修改时间',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    }).on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinWmSoAllocTable').bootstrapTable('getSelections').length;
        $('#picking').prop('disabled', !length);
        $('#shipment').prop('disabled', !length);
        $('#cancelAlloc').prop('disabled', !length);
        $('#cancelPick').prop('disabled', !length);
        $('#cancelShipment').prop('disabled', !length);
        $('#generatePick').prop('disabled', !length);
    });
}

function getSelections() {
    return $.map($("#banQinWmSoAllocTable").bootstrapTable('getSelections'), function (row) {
        return row
    });
}

function getIdSelections() {
    return $.map($("#banQinWmSoAllocTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmSoAllocTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}

/**
 * 查看
 * @param id
 */
function view(id) {
    jp.openBQDialog('查看拣货任务', "${ctx}/wms/outbound/banQinWmSoAlloc/form?id=" + id, '80%', '60%', $('#banQinWmSoAllocTable'));
}

/**
 * 拣货确认
 */
function picking() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    commonMethod('picking');
}

/**
 * 发货确认
 */
function shipment() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    commonMethod('shipment');
}

/**
 * 取消分配
 */
function cancelAlloc() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    commonMethod('cancelAlloc');
}

/**
 * 取消拣货
 */
var allocRows = [];
function cancelPick() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    var rows = getSelections();
    if (rows.length === 0) {
        jp.bqError("请选择记录");
        return;
    }
    allocRows = [];
    for (var index = 0, length = rows.length; index < length; index++) {
        if (rows[index].status === '60') {
            allocRows.push(rows[index]);
        }
    }
    if (allocRows.length === 0) {
        jp.bqError('请选择取消拣货的记录');
        return;
    }
    initPaTaskWindow();
}

function initPaTaskWindow() {
    $('#createTaskPaModal').modal();
    $('#isTaskPa').prop('checked', false).prop('disabled', false).val('N');
    $('#allocLoc').prop('checked', true).prop('disabled', true);
    $('#paRuleLoc').prop('checked', false).prop('disabled', true);
}

function createTaskPaConfirm() {
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
                $('#createTaskPaModal').modal('hide');
                $('#banQinWmSoAllocTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

/**
 * 取消发货
 */
function cancelShipment() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    commonMethod('cancelShipment');
}

/**
 * 生产拣货单
 */
function generatePick() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    commonMethod('generatePick');
}

/**
 * 公共调用方法
 * @param method 方法名
 */
function commonMethod(method) {
    jp.loading();
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(getSelections()),
        url: "${ctx}/wms/outbound/banQinWmSoAlloc/" + method,
        success: function (data) {
            if (data.success) {
                $('#banQinWmSoAllocTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
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

function exportData() {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    bq.exportExcel("${ctx}/wms/outbound/banQinWmSoAlloc/export", "拣货任务记录", $("#searchForm"), function () {
        jp.close();
    });
}

</script>