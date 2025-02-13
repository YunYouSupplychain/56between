<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#orderDateFm").datetimepicker({format: "YYYY-MM-DD"});
    $("#orderDateTo").datetimepicker({format: "YYYY-MM-DD"});
    $("#parentId").val(jp.getCurrentOrg().orgId);
    $("#orgId").val(jp.getCurrentOrg().orgId);
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#omTaskHeaderTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#omTaskHeaderTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#omTaskHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/oms/order/omTaskHeader/data",
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
            field: 'taskNo',
            title: '作业任务号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'warehouseName',
            title: '仓库',
            sortable: true
        }, {
            field: 'orderDate',
            title: '订单日期',
            sortable: true
        }, {
            field: 'status',
            title: '任务状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_TASK_STATUS'))}, value, "-");
            }
        }, {
            field: 'taskType',
            title: '任务类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_TASK_TYPE'))}, value, "-");
            }
        }, {
            field: 'pushSystem',
            title: '下发系统',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_PUSH_SYSTEM'))}, value, "-");
            }
        }, {
            field: 'chainNo',
            title: '供应链订单号',
            sortable: true,
        }, {
            field: 'customerNo',
            title: '客户订单号',
            sortable: true
        }, {
            field: 'businessNo',
            title: '商流订单号',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主',
            sortable: true
        }, {
            field: 'customerName',
            title: '客户',
            sortable: true
        }, {
            field: 'supplierName',
            title: '供应商',
            sortable: true
        }, {
            field: 'principalName',
            title: '委托方',
            sortable: true
        }, {
            field: 'def10',
            title: '客户机构',
            sortable: true
        }, {
            field: 'consignee',
            title: '收货人',
            sortable: true
        }, {
            field: 'consigneeTel',
            title: '收货人联系电话',
            sortable: true
        }, {
            field: 'consigneeAddressArea',
            title: '三级地址(收货)',
            sortable: true
        }, {
            field: 'consigneeAddress',
            title: '收货地址',
            sortable: true
        }, {
            field: 'shipper',
            title: '发货人',
            sortable: true
        }, {
            field: 'shipperTel',
            title: '发货人联系电话',
            sortable: true
        }, {
            field: 'shipperAddressArea',
            title: '三级地址(发货)',
            sortable: true
        }, {
            field: 'shipperAddress',
            title: '发货地址',
            sortable: true
        }, {
            field: 'arrivalTime',
            title: '预计到货时间',
            sortable: true
        }, {
            field: 'transportMode',
            title: '运输方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_METHOD'))}, value, "-");
            }
        }, {
            field: 'carrierName',
            title: '承运商',
            sortable: true
        }, {
            field: 'logisticsNo',
            title: '物流单号',
            sortable: true
        }, {
            field: 'vehicleNo',
            title: '车牌号',
            sortable: true
        }, {
            field: 'driver',
            title: '司机',
            sortable: true
        }, {
            field: 'contactTel',
            title: '联系电话',
            sortable: true
        }, {
            field: 'createDate',
            title: '创建时间',
            sortable: true
        }, {
            field: 'updateDate',
            title: '更新时间',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }]
    }).on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#omTaskHeaderTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#allocTask').prop('disabled', !length);
        $('#unAllocTask').prop('disabled', !length);
        $('#sendTask').prop('disabled', !length);
        $('#mergeTask').prop('disabled', !length);
        $('#restoreTask').prop('disabled', !length);
        $('#carrierAlloc').prop('disabled', !length);
        $('#carrierDesignate').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#omTaskHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}
function getOrgIdSelections() {
    return $.map($("#omTaskHeaderTable").bootstrapTable('getSelections'), function (row) {
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
    jp.confirm('确认要删除该供应链作业任务记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/oms/order/omTaskHeader/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#omTaskHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function view(id) {//没有权限时，不显示确定按钮
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openDialogView('查看供应链作业任务', "${ctx}/oms/order/omTaskHeader/form?id=" + id, '90%', '90%', $('#omTaskHeaderTable'));
}

function allocTask() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/oms/order/omTaskHeader/allocate?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omTaskHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    })
}

function unAllocTask() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/oms/order/omTaskHeader/unAllocate?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omTaskHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    })
}

function sendTask() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/oms/order/omTaskHeader/sendTask?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omTaskHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    })
}

function mergeTask() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/oms/order/omTaskHeader/mergeTask?ids=" + getIdSelections() + "&currOrgId=" + jp.getCurrentOrg().orgId, function (data) {
        if (data.success) {
            $('#omTaskHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    })
}

function restoreTask() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/oms/order/omTaskHeader/restore?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omTaskHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.alert(data.msg);
        }
    });
}

function carrierAlloc() {
    jp.loading();
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.post("${ctx}/oms/order/omTaskHeader/carrierAlloc", {ids:getIdSelections().join(',')},function (data) {
        if (data.success) {
            $('#omTaskHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.alert(data.msg);
        }
    });
}

function carrierDesignate() {
    jp.loading();
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var carrier = $("#carrierDesignateForm").find("#carrierCode").val();
    jp.post("${ctx}/oms/order/omTaskHeader/carrierDesignate", {ids:getIdSelections().join(','), carrier:carrier},function (data) {
        if (data.success) {
            $("#carrierDesignateModal").modal('hide');
            $('#omTaskHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.alert(data.msg);
        }
    });
}

</script>