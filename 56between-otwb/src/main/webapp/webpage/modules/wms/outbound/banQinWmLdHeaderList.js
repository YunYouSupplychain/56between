<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#fmLdTimeFm').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#fmLdTimeTo').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#toLdTimeFm').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#toLdTimeTo').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#deliverTimeFm').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#deliverTimeTo').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#createDateFm').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#createDateTo').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinWmLdHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmLdHeaderTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#banQinWmLdHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/outbound/banQinWmLdHeader/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'ldNo',
            title: '装车单号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_LD_STATUS'))}, value, "-");
            }
        }, {
            field: 'soStatus',
            title: '出库单状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ALLOC_STATUS'))}, value, "-");
            }
        }, {
            field: 'ldType',
            title: '装车单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_LD_TYPE'))}, value, "-");
            }
        }, {
            field: 'vehicleNo',
            title: '车牌号',
            sortable: true
        }, {
            field: 'vehicleType',
            title: '车型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_CAR_TYPE'))}, value, "-");
            }
        }, {
            field: 'driver',
            title: '驾驶员',
            sortable: true
        }, {
            field: 'fmLdTime',
            title: '预计装车时间从',
            sortable: true
        }, {
            field: 'toLdTime',
            title: '预计装车时间到',
            sortable: true
        }, {
            field: 'deliverTime',
            title: '装车交接时间',
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
        }, {
            field: 'orgName',
            title: '仓库',
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
        }]
    });
    $('#banQinWmLdHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinWmLdHeaderTable').bootstrapTable('getSelections').length;
        var cStyle = length ? "auto" : "none";
        var fColor = length ? "#1E1E1E" : "#8A8A8A";
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#shipment').prop('disabled', !length);
        $('#loadDelivery').prop('disabled', !length);
        $('#generateWave').prop('disabled', !length);
        $('#preAlloc').prop('disabled', !length);
        $('#alloc').prop('disabled', !length);
        $('#picking').prop('disabled', !length);
        $('#cancelShipment').prop('disabled', !length);
        $('#cancelDelivery').prop('disabled', !length);
        $('#closeOrder').prop('disabled', !length);
        $('#printStoreCheckAcceptOrder').css({"pointer-events": cStyle, "color": fColor});
    });
}

function getSelections() {
    return $.map($("#banQinWmLdHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row
    });
}

function getLdNoSelections() {
    return $.map($("#banQinWmLdHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.ldNo;
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmLdHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}

function getIdSelections() {
    return $.map($("#banQinWmLdHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

/**
 * 新增
 */
function add() {
    jp.openBQDialog('新增装车单', "${ctx}/wms/outbound/banQinWmLdHeader/form", '90%', '90%', $('#banQinWmLdHeaderTable'));
}

/**
 * 查看
 * @param id
 */
function view(id) {
    jp.openBQDialog('查看装车单', "${ctx}/wms/outbound/banQinWmLdHeader/form?id=" + id, '90%', '90%', $('#banQinWmLdHeaderTable'));
}

/**
 * 修改
 */
function edit() {
    jp.openBQDialog('编辑装车单', "${ctx}/wms/outbound/banQinWmLdHeader/form?id=" + getIdSelections(), '90%', '90%', $('#banQinWmLdHeaderTable'));
}

/**
 * 删除
 */
function deleteAll() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.confirm('确认要删除该出库单记录吗？', function () {
        if (!jp.isExistDifOrg(getOrgIdSelections())) {
            jp.warning("不同平台数据不能批量操作");
            return;
        } 
        commonMethod("deleteAll");
    })
}

/**
 * 发货确认
 */
function shipment() {
    commonMethod('shipment');
}

/**
 * 装车交接
 */
function loadDelivery() {
    commonMethod('loadDelivery');
}

/**
 * 取消发货
 */
function cancelShipment() {
    commonMethod('cancelShipment');
}

/**
 * 取消交接
 */
function cancelDelivery() {
    commonMethod('cancelDelivery');
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
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/outbound/banQinWmLdHeader/" + method + "?ldNo=" + getLdNoSelections() + "&orgId=" + jp.getCurrentOrg().orgId, function (data) {
        if (data.success) {
            $('#banQinWmLdHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 打印门店验收单
 */
function printStoreCheckAcceptOrder() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/outbound/banQinWmLdHeader/printStoreCheckAcceptOrder", 'ids', rowIds.join(','), '打印门店验收单');
}

/**
 * 打印装车清单
 */
function printLdOrder() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/outbound/banQinWmLdHeader/printLdOrder", 'ids', rowIds.join(','), '打印装车清单');
}

</script>