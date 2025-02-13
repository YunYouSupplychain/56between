<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#banQinWmMvTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inventory/banQinWmMv/data",
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
            field: 'mvNo',
            title: '移动单号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        },
        {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        },
        {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_MV_STATUS'))}, value, "-");
            }
        },
        {
            field: 'auditStatus',
            title: '审核状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_AUDIT_STATUS'))}, value, "-");
            }
        },
        {
            field: 'reasonCode',
            title: '原因编码',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_MV_REASON'))}, value, "-");
            }
        },
        {
            field: 'reason',
            title: '原因描述',
            sortable: true
        },
        {
            field: 'auditOp',
            title: '审核人',
            sortable: true
        },
        {
            field: 'auditTime',
            title: '审核时间',
            sortable: true
        },
        {
            field: 'auditComment',
            title: '审核意见',
            sortable: true
        },
        {
            field: 'mvOp',
            title: '移动操作人',
            sortable: true
        },
        {
            field: 'mvTime',
            title: '移动时间',
            sortable: true
        },
        {
            field: 'def1',
            title: '自定义1',
            sortable: true
        },
        {
            field: 'def2',
            title: '自定义2',
            sortable: true
        },
        {
            field: 'def3',
            title: '自定义3',
            sortable: true
        },
        {
            field: 'def4',
            title: '自定义4',
            sortable: true
        },
        {
            field: 'def5',
            title: '自定义5',
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
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });

    if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {//如果是移动端
        $('#banQinWmMvTable').bootstrapTable("toggleView");
    }

    $('#banQinWmMvTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinWmMvTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length != 1);
        $('#audit').prop('disabled', !length);
        $('#cancelAudit').prop('disabled', !length);
        $('#transfer').prop('disabled', !length);
        $('#closeOrder').prop('disabled', !length);
        $('#cancelOrder').prop('disabled', !length);
    });

    $("#btnImport").click(function () {
        jp.open({
            type: 1,
            area: [500, 300],
            title: "导入数据",
            content: $("#importBox").html(),
            btn: ['下载模板', '确定', '关闭'],
            btn1: function (index, layero) {
                window.location = '${ctx}/inventory/banQinWmMv/import/template';
            },
            btn2: function (index, layero) {
                var inputForm = top.$("#importForm");
                var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe 
                inputForm.attr("target", top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
                inputForm.onsubmit = function () {
                    jp.loading('  正在导入，请稍等...');
                }
                inputForm.submit();
                jp.close(index);
            },
            btn3: function (index) {
                jp.close(index);
            }
        });
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinWmMvTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmMvTable').bootstrapTable('refresh');
    });
});

function getIdSelections() {
    return $.map($("#banQinWmMvTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmMvTable").bootstrapTable('getSelections'), function (row) {
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
        jp.get("${ctx}/wms/inventory/banQinWmMv/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinWmMvTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

function add() {
    jp.openBQDialog('新增库存移动', "${ctx}/wms/inventory/banQinWmMv/form", '90%', '90%', $('#banQinWmMvTable'));
}

function view(id) {
    jp.openBQDialog('查看库存移动', "${ctx}/wms/inventory/banQinWmMv/form?id=" + id, '90%', '90%', $('#banQinWmMvTable'));
}

function edit() {
    jp.openBQDialog('编辑库存移动', "${ctx}/wms/inventory/banQinWmMv/form?id=" + getIdSelections(), '90%', '90%', $('#banQinWmMvTable'));
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
    jp.get("${ctx}/wms/inventory/banQinWmMv/" + method + "?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#banQinWmMvTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

function printMvOrder() {
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
    bq.openPostWindow("${ctx}/wms/inventory/banQinWmMv/printMvOrder", 'ids', ids.join(','), '打印移动单');
}

function exportData() {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    bq.exportExcel("${ctx}/wms/inventory/banQinWmMv/export", "库存移动单", $("#searchForm"), function () {
        jp.close();
    });
}

</script>