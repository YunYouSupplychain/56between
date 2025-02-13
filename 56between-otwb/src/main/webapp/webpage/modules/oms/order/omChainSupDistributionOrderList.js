<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#orderDateFm").datetimepicker({format: "YYYY-MM-DD"});
    $("#orderDateTo").datetimepicker({format: "YYYY-MM-DD"});
    initTable();

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
        url: "${ctx}/oms/order/omChainSupDistributionOrder/data",
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
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_BUSINESS_ORDER_TYPE_SUP'))}, value, "-");
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
            field: 'dataSource',
            title: '数据来源',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_DATA_SOURCE'))}, value, "-");
            }
        }, {
            field: 'def10',
            title: '客户机构',
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
            field: 'orgName',
            title: '机构',
            sortable: true
        }, {
            field: 'principalName',
            title: '委托方',
            sortable: true
        }, {
            field: 'preSaleNo',
            title: '预售订单号',
            sortable: true
        }, {
            field: 'validityPeriod',
            title: '有效期',
            sortable: true
        }, {
            field: 'arrivalTime',
            title: '到货时间',
            sortable: true
        }, {
            field: 'settlementName',
            title: '结算对象',
            sortable: true
        }, {
            field: 'settleMode',
            title: '结算方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_SETTLEMENT_TYPE'))}, value, "-");
            }
        }, {
            field: 'currency',
            title: '币种',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_CURRENCY'))}, value, "-");
            }
        }, {
            field: 'exchangeRate',
            title: '汇率',
            sortable: true
        }, {
            field: 'prepaidAmount',
            title: '预付金额',
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
    jp.confirm('确认要删除该供应商配送订单记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/oms/order/omChainSupDistributionOrder/deleteAll?ids=" + getIdSelections(), function (data) {
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
    jp.openDialog('新增供应商配送订单', "${ctx}/oms/order/omChainSupDistributionOrder/form", '90%', '90%', $('#omChainHeaderTable'));
}

function edit(id, status) {//没有权限时，不显示确定按钮
    if (id === undefined) {
        id = getIdSelections();
    }
    if (status === undefined) {
        status = getStatsusSelections();
    }
    <shiro:lacksPermission name="order:omChainSupDistributionOrder:edit">
        jp.openDialogView('查看供应商配送订单', "${ctx}/oms/order/omChainSupDistributionOrder/form?id=" + id, '90%', '90%', $('#omChainHeaderTable'));
    </shiro:lacksPermission>
    <shiro:hasPermission name="order:omChainSupDistributionOrder:edit">
        jp.openDialog('编辑供应商配送订单', "${ctx}/oms/order/omChainSupDistributionOrder/form?id=" + id, '90%', '90%', $('#omChainHeaderTable'));
    </shiro:hasPermission>
}

function view(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="order:omChainSupDistributionOrder:view">
        jp.openDialogView('查看供应商配送订单', "${ctx}/oms/order/omChainSupDistributionOrder/form?id=" + id, '90%', '90%', $('#omChainHeaderTable'));
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
    jp.get("${ctx}/oms/order/omChainSupDistributionOrder/audit?ids=" + getIdSelections(), function (data) {
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
    jp.get("${ctx}/oms/order/omChainSupDistributionOrder/cancelAudit?ids=" + getIdSelections(), function (data) {
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
    var rows = $("#omChainHeaderTable").bootstrapTable('getSelections');
    if (rows[0].status == '20' || rows[0].status == '10') {
        jp.error("未审核，不能生成任务！");
        return;
    }
    if (rows[0].status == '40') {
        jp.error("已完全生成任务！");
        return;
    }
    var $table = $('#omChainHeaderTable');
    top.layer.open({
        type: 2,
        area: ['1300', '700'],
        title: '作业任务生成',
        auto: true,
        maxmin: true, //开启最大化最小化按钮
        content: "${ctx}/oms/order/omChainCreateTask/createTaskForm?id=" + getIdSelections(),
        btn: ['全部生成', '生成任务', '取消'],
        yes: function (index, layero) {
            var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
            if (!$table) {//如果不传递table对象过来，按约定的默认id获取table对象
                $table = $('#table')
            }
            iframeWin.contentWindow.saveAll($table, index);
        },
        btn2: function (index, layero) {
            var iframeWin = layero.find('iframe')[0]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
            if (!$table) {//如果不传递table对象过来，按约定的默认id获取table对象
                $table = $('#table')
            }
            iframeWin.contentWindow.save($table, index);
            return false;
        },
        cancel: function (index) {
        },
        end: function () {
            $('#omChainHeaderTable').bootstrapTable('refresh');
        }
    });
}

function importOrder(systemType, uploadType) {
    $("#uploadModal").modal();
    $("#uploadFileName").val('');
    $("#systemType").val(systemType);
    $("#uploadType").val(uploadType);
}

function downloadTemplate() {
    var systemType = $("#systemType").val();
    if (null != systemType && systemType == 'WMS') {
        window.location = '${ctx}/oms/order/omChainSupDistributionOrder/importWms/template';
    } else {
        window.location = '${ctx}/oms/order/omChainSupDistributionOrder/importDC/template';
    }

}

function uploadFile() {
    var systemType = $("#systemType").val();
    var uploadType = $("#uploadType").val();
    if (!$("#uploadFileName").val()) {
        jp.alert("请选择需要上传的文件");
        return;
    }
    jp.loading("正在导入中...");
    if (null != systemType && systemType == 'WMS') {
        upload(uploadType, "${ctx}/oms/order/omChainSupDistributionOrder/importWms");
    } else {
        upload(uploadType, "${ctx}/oms/order/omChainSupDistributionOrder/importDC");
    }
}

function upload(uploadType, url) {
    var file = $("#uploadFileName").get(0).files[0];
    var fm = new FormData();
    fm.append('file', file);
    fm.append('orgId', jp.getCurrentOrg().orgId);
    fm.append('uploadType', uploadType);
    $.ajax({
        type: "post",
        url: url,
        data: fm,
        cache: false,
        contentType: false,
        processData: false,
        // 传送请求数据
        success: function (data) {
            $("#uploadModal").modal('hide');
            $('#omChainHeaderTable').bootstrapTable('refresh');
            jp.alert(data.msg);
        },
        error: function (data) {
            $("#uploadModal").modal('hide');
            jp.alert(data.msg);
        }
    });
}

</script>
