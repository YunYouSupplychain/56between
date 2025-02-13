<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#orderDateFm").datetimepicker({format: "YYYY-MM-DD"});
    $("#orderDateTo").datetimepicker({format: "YYYY-MM-DD"});
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#omSaleHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#omSaleHeaderTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#omSaleHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/oms/order/omSaleHeader/data",
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
            field: 'customerNo',
            title: '客户订单号',
            sortable: true
        }, {
            field: 'shop',
            title: '店铺',
            sortable: true
        }, {
            field: 'orderDate',
            title: '订单日期',
            sortable: true
        }, {
            field: 'saleType',
            title: '订单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_SALE_TYPE'))}, value, "-");
            }
        }, {
            field: 'status',
            title: '订单状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_SALE_STATUS'))}, value, "-");
            }
        }, {
            field: 'ownerName',
            title: '货主',
            sortable: true
        }, {
            field: 'outWarhouseName',
            title: '下发机构',
            sortable: true
        }, {
            field: 'customerName',
            title: '客户',
            sortable: true
        }, {
            field: 'vipStatus',
            title: '会员级别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_VIP_LEVEL'))}, value, "-");
            }
        }, {
            field: 'saleMethod',
            title: '销售方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_SALE_METHOD'))}, value, "-");
            }
        }, {
            field: 'channel',
            title: '渠道价格',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_CHANNEL'))}, value, "-");
            }
        }, {
            field: 'clerkName',
            title: '业务员',
            sortable: true
        }, {
            field: 'projectName',
            title: '项目',
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
            field: 'couponAmount',
            title: '优惠金额',
            sortable: true
        }, {
            field: 'orderSettleAmount',
            title: '订单结算金额',
            sortable: true
        }, {
            field: 'freightCharge',
            title: '运费金额',
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
            field: 'updateBy',
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
        }]
    });
    $('#omSaleHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#omSaleHeaderTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#audit').prop('disabled', !length);
        $('#cancelAudit').prop('disabled', !length);
        $('#createChainOrder').prop('disabled', !length);
        $('#closeOrder').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#omSaleHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getStatusSelections() {
    return $.map($("#omSaleHeaderTable").bootstrapTable('getSelections'), function (row) {
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
    jp.confirm('确认要删除该销售订单记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/oms/order/omSaleHeader/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#omSaleHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增销售订单', "${ctx}/oms/order/omSaleHeader/form", '100%', '100%', $('#omSaleHeaderTable'));
}

function edit(id, status) {
    if (id === undefined) {
        id = getIdSelections();
    }
    if (status === undefined) {
        status = getStatusSelections();
    }
    <shiro:lacksPermission name="order:omSaleHeader:edit">
        jp.openDialogView('查看销售订单', "${ctx}/oms/order/omSaleHeader/form?id=" + id, '100%', '100%', $('#omSaleHeaderTable'));
    </shiro:lacksPermission>
    <shiro:hasPermission name="order:omSaleHeader:edit">
        jp.openDialog('编辑销售订单', "${ctx}/oms/order/omSaleHeader/form?id=" + id, '100%', '100%', $('#omSaleHeaderTable'));
    </shiro:hasPermission>

}

function view(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="order:omSaleHeader:view">
        jp.openDialogView('查看销售订单', "${ctx}/oms/order/omSaleHeader/form?id=" + id, '100%', '100%', $('#omSaleHeaderTable'));
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
    jp.get("${ctx}/oms/order/omSaleHeader/audit?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omSaleHeaderTable').bootstrapTable('refresh');
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
    jp.get("${ctx}/oms/order/omSaleHeader/cancelAudit?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omSaleHeaderTable').bootstrapTable('refresh');
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
    jp.get("${ctx}/oms/order/omSaleHeader/createChainOrder?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omSaleHeaderTable').bootstrapTable('refresh');
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
    jp.get("${ctx}/oms/order/omSaleHeader/closeOrder?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omSaleHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    })
}

function getOrgIdSelections() {
    return $.map($("#omSaleHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}
    /**
     * 打印发货清单
     */
function printShipOrder() {
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
    bq.openPostWindow("${ctx}/oms/order/omSaleHeader/printShipOrder", 'ids', rowIds.join(','), '打印销售发货单');
}

</script>