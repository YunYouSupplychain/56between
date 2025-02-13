<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#orderDateFm").datetimepicker({format: "YYYY-MM-DD"});
    $("#orderDateTo").datetimepicker({format: "YYYY-MM-DD"});
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#omPoHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#omPoHeaderTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#omPoHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/oms/order/omPoHeader/data",
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
            field: 'poNo',
            title: '采购单号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'orderDate',
            title: '订单日期',
        }, {
            field: 'poType',
            title: '订单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_PO_TYPE'))}, value, "-");
            }
        }, {
            field: 'status',
            title: '订单状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_PO_STATUS'))}, value, "-");
            }
        }, {
            field: 'ownerName',
            title: '货主',
            sortable: true
        }, {
            field: 'subOrgName',
            title: '下发机构',
            sortable: true
        }, {
            field: 'supplierName',
            title: '供应商',
            sortable: true
        }, {
            field: 'purchaseMethod',
            title: '采购方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_PURCHASE_METHOD'))}, value, "-");
            }
        }, {
            field: 'channel',
            title: '渠道价格',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_CHANNEL'))}, value, "-");
            }
        }, {
            field: 'project',
            title: '项目',
            sortable: true
        }, {
            field: 'contractNo',
            title: '合同号',
            sortable: true
        }, {
            field: 'settlementName',
            title: '结算方',
            sortable: true
        }, {
            field: 'settleMode',
            title: '结算方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_SETTLEMENT_TYPE'))}, value, "-");
            }
        }, {
            field: 'payStatus',
            title: '支付状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_PAY_STATUS'))}, value, "-");
            }
        }, {
            field: 'totalAmount',
            title: '合计金额',
            sortable: true
        }, {
            field: 'totalTax',
            title: '合计税额',
            sortable: true
        }, {
            field: 'totalTaxInAmount',
            title: '合计含税金额',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注信息',
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
            field: 'preparedBy',
            title: '制单人',
            sortable: true
        }, {
            field: 'auditBy',
            title: '审核人',
            sortable: true
        }, {
            field: 'auditDate',
            title: '审核时间',
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
            title: '机构',
            sortable: true
        }, {
            field: 'customerNo',
            title: '客户订单号',
            sortable: true
        }]
    });
    $('#omPoHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#omPoHeaderTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $("#audit").prop('disabled', !length);
        $("#cancelAudit").prop('disabled', !length);
        $("#createChainOrder").prop('disabled', !length);
        $('#closeOrder').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#omPoHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getStatusSelections() {
    return $.map($("#omPoHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.status
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
    jp.confirm('确认要删除该采购订单记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/oms/order/omPoHeader/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#omPoHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增采购订单', "${ctx}/oms/order/omPoHeader/form", '100%', '100%', $('#omPoHeaderTable'));
}

function getOrgIdSelections() {
    return $.map($("#omPoHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}

/**
 * 打印采购订单
 */
function printPo() {
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    bq.openPostWindow("${ctx}/oms/order/omPoHeader/printPo", 'ids', rowIds.join(','), '打印采购订单');
}

function edit(id, status) {//没有权限时，不显示确定按钮
    if (id === undefined) {
        id = getIdSelections();
    }
    if (status === undefined) {
        status = getStatusSelections();
    }
    <shiro:hasPermission name="order:omPoHeader:edit">
        jp.openDialog('编辑采购订单', "${ctx}/oms/order/omPoHeader/form?id=" + id, '100%', '100%', $('#omPoHeaderTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="order:omPoHeader:edit">
        jp.openDialogView('查看采购订单', "${ctx}/oms/order/omPoHeader/form?id=" + id, '100%', '100%', $('#omPoHeaderTable'));
    </shiro:lacksPermission>
}

function view(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="order:omPoHeader:view">
        jp.openDialogView('查看采购订单', "${ctx}/oms/order/omPoHeader/form?id=" + id, '100%', '100%', $('#omPoHeaderTable'));
    </shiro:hasPermission>
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
    jp.get("${ctx}/oms/order/omPoHeader/audit?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omPoHeaderTable').bootstrapTable('refresh');
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
    jp.get("${ctx}/oms/order/omPoHeader/cancelAudit?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omPoHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    })
}

function createChainOrder() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/oms/order/omPoHeader/createChainOrder?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omPoHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    })
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
    jp.get("${ctx}/oms/order/omPoHeader/closeOrder?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omPoHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    })
}

</script>
