<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinWmTfHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmTfHeaderTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#banQinWmTfHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inventory/banQinWmTfHeader/data",
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
            field: 'tfNo',
            title: '转移单号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_TF_STATUS'))}, value, "-");
            }
        }, {
            field: 'auditStatus',
            title: '审核状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_AUDIT_STATUS'))}, value, "-");
            }
        }, {
            field: 'reasonCode',
            title: '原因编码',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_TF_REASON'))}, value, "-");
            }
        }, {
            field: 'reason',
            title: '原因描述',
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
            field: 'auditComment',
            title: '审核意见',
            sortable: true
        }, {
            field: 'tfOp',
            title: '转移操作人',
            sortable: true
        }, {
            field: 'tfTime',
            title: '转移时间',
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
    });
    $('#banQinWmTfHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinWmTfHeaderTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#audit').prop('disabled', !length);
        $('#cancelAudit').prop('disabled', !length);
        $('#transfer').prop('disabled', !length);
        $('#closeOrder').prop('disabled', !length);
        $('#cancelOrder').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#banQinWmTfHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmTfHeaderTable").bootstrapTable('getSelections'), function (row) {
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
    jp.confirm('确认要删除该转移单记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/inventory/banQinWmTfHeader/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinWmTfHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

function add() {
    jp.openBQDialog('新增库存转移', "${ctx}/wms/inventory/banQinWmTfHeader/form", '90%', '90%', $('#banQinWmTfHeaderTable'));
}

function view(id) {
    jp.openBQDialog('查看库存转移', "${ctx}/wms/inventory/banQinWmTfHeader/form?id=" + id, '90%', '90%', $('#banQinWmTfHeaderTable'));
}

function edit() {
    jp.openBQDialog('编辑库存转移', "${ctx}/wms/inventory/banQinWmTfHeader/form?id=" + getIdSelections(), '90%', '90%', $('#banQinWmTfHeaderTable'));
}

function audit() {
    commonMethod('audit');
}

function cancelAudit() {
    commonMethod('cancelAudit');
}

function transfer() {
    commonMethod('transfer');
}

function closeOrder() {
    commonMethod('closeOrder');
}

function cancelOrder() {
    commonMethod('cancelOrder');
}

function commonMethod(method) {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.get("${ctx}/wms/inventory/banQinWmTfHeader/" + method + "?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#banQinWmTfHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

function printTfOrder() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var ids = getIdSelections();
    if (ids.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/inventory/banQinWmTfHeader/printTfOrder", 'ids', ids.join(','), '打印转移单');
}

function exportData() {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    bq.exportExcel("${ctx}/wms/inventory/banQinWmTfHeader/export", "库存转移单记录", $("#searchForm"), function () {
        jp.close();
    });
}

</script>