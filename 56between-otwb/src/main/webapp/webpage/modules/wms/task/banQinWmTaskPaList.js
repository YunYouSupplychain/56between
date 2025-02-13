<%@ page contentType="text/html;charset=UTF-8" %> 
<script>
$(document).ready(function () {
    $('#banQinWmTaskPaTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/task/banQinWmTaskPa/data",
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
            field: 'paId',
            title: '上架任务Id',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'lineNo',
            title: '上架任务行号',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_TASK_STATUS'))}, value, "-");
            }
        }, {
            field: 'orderType',
            title: '单据类型',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ORDER_TYPE'))}, value, "-");
            }
        }, {
            field: 'orderNo',
            title: '单据号',
            sortable: true
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        }, {
            field: 'skuName',
            title: '商品名称',
            sortable: true
        }, {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        }, {
            field: 'fmLoc',
            title: '源库位',
            sortable: true
        }, {
            field: 'fmId',
            title: '源跟踪号',
            sortable: true
        }, {
            field: 'suggestLoc',
            title: '推荐库位',
            sortable: true
        }, {
            field: 'packCode',
            title: '包装规格',
            sortable: true
        }, {
            field: 'uom',
            title: '包装单位',
            sortable: true
        }, {
            field: 'qtyPaUom',
            title: '上架数',
            sortable: true
        }, {
            field: 'qtyPaEa',
            title: '上架数EA',
            sortable: true
        }, {
            field: 'toLoc',
            title: '目标库位',
            sortable: true
        }, {
            field: 'toId',
            title: '目标跟踪号',
            sortable: true
        }, {
            field: 'paOp',
            title: '上架人',
            sortable: true
        }, {
            field: 'paTime',
            title: '上架时间',
            sortable: true
        }, {
            field: 'reserveCode',
            title: '上架库位指定规则',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_RESERVE_CODE'))}, value, "-");
            }
        }, {
            field: 'paRule',
            title: '上架规则',
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
    $('#banQinWmTaskPaTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinWmTaskPaTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#confirm').prop('disabled', !length);
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinWmTaskPaTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmTaskPaTable').bootstrapTable('refresh');
    });
});

function getIdSelections() {
    return $.map($("#banQinWmTaskPaTable").bootstrapTable('getSelections'), function (row) {
        return row
    });
}

function getIdsSelections() {
    return $.map($("#banQinWmTaskPaTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmTaskPaTable").bootstrapTable('getSelections'), function (row) {
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
    jp.confirm('确认要删除该上架任务记录吗？', function () {
        jp.loading();
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: 'POST',
            dataType: "json",
            data: JSON.stringify(getIdSelections()),
            url: "${ctx}/wms/task/banQinWmTaskPa/deleteAll",
            success: function (data) {
                if (data.success) {
                    $('#banQinWmTaskPaTable').bootstrapTable('refresh');
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            }
        });
    })
}

function confirm() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(getIdSelections()),
        url: "${ctx}/wms/task/banQinWmTaskPa/putAway",
        success: function (data) {
            if (data.success) {
                $('#banQinWmTaskPaTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

function view(id) {
    jp.openBQDialog('查看上架任务', "${ctx}/wms/task/banQinWmTaskPa/form?id=" + id, '80%', '60%', $('#banQinWmTaskPaTable'));
}

/**
 * 打印上架任务单
 */
function printPaTask() {
    var rowIds = getIdsSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/task/banQinWmTaskPa/printPaTask", 'ids', rowIds.join(','), '打印上架任务单');
}

function exportData() {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    bq.exportExcel("${ctx}/wms/task/banQinWmTaskPa/export", "上架任务记录" + jp.dateFormat(new Date(), "yyyyMMddhhmmss"), $("#searchForm"), function () {
        jp.close();
    });
}

</script>