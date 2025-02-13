<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#banQinWmQcHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}//wms/qc/banQinWmQcHeader/data",
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
            field: 'qcNo',
            title: '质检单号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'qcPhase',
            title: '质检阶段',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_PHASE'))}, value, "-");
            }
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_STATUS'))}, value, "-");
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
            field: 'priority',
            title: '优先级别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_PRIORITY'))}, value, "-");
            }
        }, {
            field: 'orderType',
            title: '单据类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ORDER_TYPE'))}, value, "-");
            }
        }, {
            field: 'orderNo',
            title: '单据号',
            sortable: true
        }, {
            field: 'orderTime',
            title: '订单时间',
            sortable: true
        }, {
            field: 'fmEtq',
            title: '预计质检时间从',
            sortable: true
        }, {
            field: 'toEtq',
            title: '预计质检时间到',
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
            field: 'remarks',
            title: '备注',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });
    $('#banQinWmQcHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinWmQcHeaderTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#audit').prop('disabled', length !== 1);
        $('#cancelAudit').prop('disabled', length !== 1);
        $('#close').prop('disabled', length !== 1);
    });

    $("#search").click("click", function () {
        $('#banQinWmQcHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmQcHeaderTable').bootstrapTable('refresh');
    });
});

function getIdSelections() {
    return $.map($("#banQinWmQcHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmQcHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}

function view(id) {
    jp.openBQDialog("查看质检单", "${ctx}/wms/qc/banQinWmQcHeader/form?id=" + id, "90%", "90%", $("#banQinWmQcHeaderTable"));
}

function edit() {
    jp.openBQDialog("编辑质检单", "${ctx}/wms/qc/banQinWmQcHeader/form?id=" + getIdSelections(), "90%", "90%", $("#banQinWmQcHeaderTable"));
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
    jp.confirm('确认要删除该质检单记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/qc/banQinWmQcHeader/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinWmQcHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
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
    jp.get("${ctx}/wms/qc/banQinWmQcHeader/" + method + "?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#banQinWmQcHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

</script>