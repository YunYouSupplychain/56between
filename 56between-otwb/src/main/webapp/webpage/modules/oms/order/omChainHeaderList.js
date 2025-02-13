<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#orderDateFm").datetimepicker({format: "YYYY-MM-DD"});
    $("#orderDateTo").datetimepicker({format: "YYYY-MM-DD"});
    $('#parentId').val(jp.getCurrentOrg().orgId);
    $('#orgId').val(jp.getCurrentOrg().orgId);
    initTable();

    $("#btnImport").click(function () {
        bq.importExcel("${ctx}/oms/order/omChainHeader/import");
    });
    $("#search").click("click", function () {// 绑定查询按扭
        $('#omChainHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#omChainHeaderTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#omChainHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/oms/order/omChainHeader/data",
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
            field: 'chainNo',
            title: '供应链订单号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'orderDate',
            title: '订单时间',
            sortable: true
        }, {
            field: 'status',
            title: '订单状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_CHAIN_STATUS'))}, value, "-");
            }
        }, {
            field: 'businessOrderType',
            title: '业务订单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_BUSINESS_ORDER_TYPE'))}, value, "-");
            }
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
            field: 'businessNo',
            title: '商流订单号',
            sortable: true
        }, {
            field: 'customerNo',
            title: '客户订单号',
            sortable: true
        }, {
            field: 'warehouseName',
            title: '下发机构',
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
            field: 'consigneeAreaName',
            title: '收货人区域',
            sortable: true
        }, {
            field: 'consigneeAddress',
            title: '收货人地址',
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
            field: 'shipperAreaName',
            title: '发货人区域',
            sortable: true
        }, {
            field: 'shipperAddress',
            title: '发货人地址',
            sortable: true
        }, {
            field: 'carrierName',
            title: '承运商',
            sortable: true
        }, {
            field: 'transportMode',
            title: '运输方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_METHOD'))}, value, "-");
            }
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
            field: 'interceptStatus',
            title: '拦截状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_INTERCEPT_STATUS'))}, value, "-");
            }
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
        var length = $("#omChainHeaderTable").bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#audit').prop('disabled', !length);
        $('#cancelAudit').prop('disabled', !length);
        $('#createTask').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#omChainHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getStatsusSelections() {
    return $.map($("#omChainHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.status
    });
}

function getOrgIdSelections() {
    return $.map($("#omChainHeaderTable").bootstrapTable('getSelections'), function (row) {
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
    jp.confirm('确认要删除该供应链订单记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/oms/order/omChainHeader/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#omChainHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增供应链订单', "${ctx}/oms/order/omChainHeader/form", '90%', '90%', $('#omChainHeaderTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="order:omChainHeader:edit">
        jp.openDialog('编辑供应链订单', "${ctx}/oms/order/omChainHeader/form?id=" + id, '90%', '90%', $('#omChainHeaderTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="order:omChainHeader:edit">
        view(id);
    </shiro:lacksPermission>
}

function view(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openDialogView('查看供应链订单', "${ctx}/oms/order/omChainHeader/form?id=" + id, '90%', '90%', $('#omChainHeaderTable'));
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
    jp.get("${ctx}/oms/order/omChainHeader/audit?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omChainHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    })
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
    jp.get("${ctx}/oms/order/omChainHeader/cancelAudit?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omChainHeaderTable').bootstrapTable('refresh');
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
    var $table = $("#omChainHeaderTable");
    var rows = $table.bootstrapTable('getSelections');
    if (rows[0].status == '20' || rows[0].status == '10') {
        jp.error("未审核，不能生成任务！");
        return;
    }
    if (rows[0].status == '40') {
        jp.error("已完全生成任务！");
        return;
    }
    top.layer.open({
        type: 2,
        area: ['1300', '700'],
        title: '作业任务生成',
        auto: true,
        maxmin: true, //开启最大化最小化按钮
        content: "${ctx}/oms/order/omChainCreateTask/createTaskForm?id=" + getIdSelections(),
        btn: ['全部生成', '生成任务', '取消'],
        btn1: function (index, layero) {
            layero.find('iframe')[0].contentWindow.saveAll($table, index);
        },
        btn2: function (index, layero) {
            layero.find('iframe')[0].contentWindow.save($table, index);
            return false;
        },
        cancel: function (index) {
        }
    });
}
</script>
