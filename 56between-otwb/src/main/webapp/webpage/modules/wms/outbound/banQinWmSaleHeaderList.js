<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#beginOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#endOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinWmSaleHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmSaleHeaderTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#banQinWmSaleHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inbound/banQinWmSaleHeader/data",
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
            field: 'saleNo',
            title: '销售单号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'logisticNo',
            title: '物流单号',
            sortable: true
        }, {
            field: 'saleType',
            title: '销售单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SALE_TYPE'))}, value, "-");
            }
        }, {
            field: 'orderTime',
            title: '订单时间',
            sortable: true
        }, {
            field: 'fmEta',
            title: '预计到货时间从',
            sortable: true
        }, {
            field: 'toEta',
            title: '预计到货时间到',
            sortable: true
        }, {
            field: 'status',
            title: '订单状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SALE_STATUS'))}, value, "-");
            }
        }, {
            field: 'auditStatus',
            title: '审核状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_AUDIT_STATUS'))}, value, "-");
            }
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主名称',
            sortable: true
        }, {
            field: 'carrierCode',
            title: '承运商编码',
            sortable: true
        }, {
            field: 'carrierName',
            title: '承运商名称',
            sortable: true
        }, {
            field: 'auditOp',
            title: '审核人',
            sortable: true
        }, {
            field: 'auditTime',
            title: '审核时间',
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
    $('#banQinWmSaleHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = getIdSelections().length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#duplicate').prop('disabled', length !== 1);
        $('#createSo').prop('disabled', length !== 1);
        $('#audit').prop('disabled', !length);
        $('#cancelAudit').prop('disabled', !length);
        $('#closeOrder').prop('disabled', !length);
        $('#cancelOrder').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#banQinWmSaleHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getSelections() {
    return $.map($("#banQinWmSaleHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmSaleHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}

/**
 * 新增
 */
function add() {
    jp.openBQDialog('新增采购单', "${ctx}/wms/inbound/banQinWmPoHeader/form", '100%', '100%', $('#banQinWmSaleHeaderTable'));
}

/**
 * 查看
 * @param id
 */
function view(id) {
    jp.openBQDialog('查看采购单', "${ctx}/wms/inbound/banQinWmPoHeader/form?id=" + id, '100%', '100%', $('#banQinWmSaleHeaderTable'));
}

/**
 * 修改
 */
function edit() {
    jp.openBQDialog('编辑采购单', "${ctx}/wms/inbound/banQinWmPoHeader/form?id=" + getIdSelections(), '100%', '100%', $('#banQinWmSaleHeaderTable'));
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
    jp.confirm('确认要删除该采购单记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/inbound/banQinWmSaleHeader/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinWmSaleHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

/**
 * 复制
 */
function duplicate() {

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
    var rows = getSelections();
    var rowIds = "";
    for (var i = 0, length = rows.length; i < length; i++) {
        if (rows[i].auditStatus === '00') {
            jp.warning(rows[i].poNo + "未审核，不能操作");
            return;
        }
        if (rows[i].status === '90') {
            jp.warning(rows[i].poNo + "已取消，不能操作");
            return;
        }
        if (rows[i].status === '99') {
            jp.warning(rows[i].poNo + "已关闭，不能操作");
            return;
        }
        if (i > 0) {
            if (rows[0].ownerCode !== rows[i].ownerCode || rows[0].consigneeCode !== rows[i].consigneeName) {
                jp.warning("货主或者收货人不一致，不能操作");
                return;
            }
        }
        rowIds = rowIds + rows[i].id + ",";
    }
    var params = "ownerCode=" + rows[0].ownerCode + "&consigneeCode=" + rows[0].consigneeCode + "&id=" + rowIds + "&orgId=" + rows[0].orgId;
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
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/inbound/banQinWmSaleHeader/" + method + "?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#banQinWmSaleHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

</script>