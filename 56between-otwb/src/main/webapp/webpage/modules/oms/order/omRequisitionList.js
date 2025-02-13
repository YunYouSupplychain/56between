<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#orderDateFm").datetimepicker({format: "YYYY-MM-DD"});
    $("#orderDateTo").datetimepicker({format: "YYYY-MM-DD"});
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#omRequisitionTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#omRequisitionTable').bootstrapTable('refresh');
    });
});

function initTable() {
    var $table = $('#omRequisitionTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/oms/order/omRequisition/data",
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
        },
        {
            field: 'reqNo',
            title: '调拨单号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        },
        {
            field: 'orderTime',
            title: '订单日期',
        },
        {
            field: 'orderType',
            title: '订单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_BUSINESS_ORDER_TYPE'))}, value, "-");
            }
        },
        {
            field: 'status',
            title: '订单状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_RO_STATUS'))}, value, "-");
            }
        },
        {
            field: 'ownerName',
            title: '货主',
            sortable: true
        },
        {
            field: 'fmOrgName',
            title: '源仓库',
            sortable: true
        },
        {
            field: 'toOrgName',
            title: '目标仓库',
            sortable: true
        },
        {
            field: 'customerNo',
            title: '客户单号',
            sortable: true
        },
        {
            field: 'reqReason',
            title: '调拨原因',
            sortable: true
        },
        {
            field: 'logisticsNo',
            title: '物流单号',
            sortable: true
        },
        {
            field: 'carrierName',
            title: '承运商',
            sortable: true
        },
        {
            field: 'transportMode',
            title: '运输方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_METHOD'))}, value, "-");
            }
        },
        {
            field: 'vehicleNo',
            title: '车牌号',
            sortable: true
        },
        {
            field: 'driver',
            title: '司机',
            sortable: true
        },
        {
            field: 'driverTel',
            title: '司机电话',
            sortable: true
        },
        {
            field: 'freightAmount',
            title: '运费',
            sortable: true
        },
        {
            field: 'shipper',
            title: '发货人',
            sortable: true
        },
        {
            field: 'shipperTel',
            title: '发货人联系电话',
            sortable: true
        },
        {
            field: 'shipArea',
            title: '发货人区域',
            sortable: true
        },
        {
            field: 'shipAddress',
            title: '发货人地址',
            sortable: true
        },
        {
            field: 'consignee',
            title: '收货人',
            sortable: true
        },
        {
            field: 'consigneeTel',
            title: '收货人联系电话',
            sortable: true
        },
        {
            field: 'consigneeArea',
            title: '收货人区域',
            sortable: true
        },
        {
            field: 'consigneeAddress',
            title: '收货人地址',
            sortable: true
        },
        {
            field: 'auditor',
            title: '审核人',
            sortable: true
        },
        {
            field: 'auditTime',
            title: '审核时间',
            sortable: true
        },
        {
            field: 'createBy.name',
            title: '创建人',
            sortable: true
        },
        {
            field: 'createDate',
            title: '创建时间',
            sortable: true
        },
        {
            field: 'updateBy.name',
            title: '修改人',
            sortable: true
        },
        {
            field: 'updateDate',
            title: '修改时间',
            sortable: true
        },
        {
            field: 'customerNo',
            title: '客户订单号',
            sortable: true
        },
        {
            field: 'orgName',
            title: '机构',
            sortable: true
        },
        {
            field: 'remarks',
            title: '备注信息',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $("#audit").prop('disabled', !length);
        $("#cancelAudit").prop('disabled', !length);
        $("#createTask").prop('disabled', length !== 1);
        $('#closeOrder').prop('disabled', !length);
        $('#cancelOrder').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#omRequisitionTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getOrgIdSelections() {
    return $.map($("#omRequisitionTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}

function deleteAll() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.confirm('确认要删除该记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/oms/order/omRequisition/deleteAll?ids=" + getIdSelections(), function (data) {
            $('#omRequisitionTable').bootstrapTable('refresh');
            if (data.success) {
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        });
    });
}

function add() {
    jp.openBQDialog('调拨单', "${ctx}/oms/order/omRequisition/form", '90%', '90%', $('#omRequisitionTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openBQDialog('调拨单', "${ctx}/oms/order/omRequisition/form?id=" + id, '90%', '90%', $('#omRequisitionTable'));
}

function audit() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/oms/order/omRequisition/audit?ids=" + getIdSelections(), function (data) {
        $('#omRequisitionTable').bootstrapTable('refresh');
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

function cancelAudit() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/oms/order/omRequisition/cancelAudit?ids=" + getIdSelections(), function (data) {
        $('#omRequisitionTable').bootstrapTable('refresh');
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    })
}

function createTask() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.post("${ctx}/oms/order/omRequisition/createTask?ids=" + getIdSelections(), {}, function (data) {
        $('#omRequisitionTable').bootstrapTable('refresh');
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

function closeOrder() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/oms/order/omRequisition/close?ids=" + getIdSelections(), function (data) {
        $('#omRequisitionTable').bootstrapTable('refresh');
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

function cancelOrder() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/oms/order/omRequisition/cancel?ids=" + getIdSelections(), function (data) {
        $('#omRequisitionTable').bootstrapTable('refresh');
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}


</script>
