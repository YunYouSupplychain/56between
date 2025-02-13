<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#rpTimeF').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#rpTimeT').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinWmTaskRpTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmTaskRpTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#banQinWmTaskRpTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/task/banQinWmTaskRp/data",
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
            field: 'rpId',
            title: '补货任务Id',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_TASK_STATUS'))}, value, "-");
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
            field: 'toLoc',
            title: '目标库位',
            sortable: true
        }, {
            field: 'toId',
            title: '目标跟踪号',
            sortable: true
        }, {
            field: 'packDesc',
            title: '包装规格',
            sortable: true
        }, {
            field: 'uom',
            title: '包装单位',
            sortable: true
        }, {
            field: 'qtyRpUom',
            title: '补货数',
            sortable: true
        }, {
            field: 'qtyRpEa',
            title: '补货数EA',
            sortable: true
        }, {
            field: 'rpOp',
            title: '操作人',
            sortable: true
        }, {
            field: 'rpTime',
            title: '补货时间',
            sortable: true
        }, {
            field: 'qty',
            title: '库存数',
            sortable: true
        }, {
            field: 'qtyUse',
            title: '库存可用数',
            sortable: true
        }, {
            field: 'qtyHold',
            title: '冻结数',
            sortable: true
        }, {
            field: 'qtyAlloc',
            title: '分配数',
            sortable: true
        }, {
            field: 'qtyPaOut',
            title: '上架待出数',
            sortable: true
        }, {
            field: 'qtyRpOut',
            title: '补货待出数',
            sortable: true
        }, {
            field: 'qtyMvOut',
            title: '移动待出数',
            sortable: true
        }, {
            field: 'lotAtt01',
            title: '生产日期',
            sortable: true
        }, {
            field: 'lotAtt02',
            title: '失效日期',
            sortable: true
        }, {
            field: 'lotAtt03',
            title: '入库日期',
            sortable: true
        }, {
            field: 'lotAtt04',
            title: '品质',
            sortable: true
        }, {
            field: 'lotAtt05',
            title: '批次属性05',
            sortable: true
        }, {
            field: 'lotAtt06',
            title: '批次属性06',
            sortable: true
        }, {
            field: 'lotAtt07',
            title: '批次属性07',
            sortable: true
        }, {
            field: 'lotAtt08',
            title: '批次属性08',
            sortable: true
        }, {
            field: 'lotAtt09',
            title: '批次属性09',
            sortable: true
        }, {
            field: 'lotAtt10',
            title: '批次属性10',
            sortable: true
        }, {
            field: 'lotAtt11',
            title: '批次属性11',
            sortable: true
        }, {
            field: 'lotAtt12',
            title: '批次属性12',
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
    $('#banQinWmTaskRpTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinWmTaskRpTable').bootstrapTable('getSelections').length;
        $('#cancelTask').prop('disabled', !length);
        $('#confirmTask').prop('disabled', !length);
    });
}

function getSelections() {
    return $.map($("#banQinWmTaskRpTable").bootstrapTable('getSelections'), function (row) {
        return row
    });
}

function getIdSelections() {
    return $.map($("#banQinWmTaskRpTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmTaskRpTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}

function deleteAll() {
    jp.confirm('确认要删除该补货任务记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/task/banQinWmTaskRp/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinWmTaskRpTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

function view(id) {
    jp.openBQDialog("查看补货任务", "${ctx}/wms/task/banQinWmTaskRp/form?id=" + id, "100%", "100%", $('#banQinWmTaskRpTable'));
}

function createTask() {
    $('#ownerCodeC').val('');
    $('#ownerNameC').val('');
    $('#skuCodeC').val('');
    $('#skuNameC').val('');
    $('#createRpModal').modal();
}

function confirm() {
    var ownerCode = $('#ownerCodeC').val();
    var skuCode = $('#skuCodeC').val();
    var orgId = jp.getCurrentOrg().orgId;
    if (ownerCode || skuCode) {
        jp.loading();
        jp.post("${ctx}/wms/task/banQinWmTaskRp/createTask", {ownerCode: ownerCode, skuCode: skuCode, orgId: orgId}, function (data) {
            if (data.success) {
                jp.success(data.msg);
                $('#createRpModal').modal('hide');
                $('#banQinWmTaskRpTable').bootstrapTable('refresh');
            } else {
                jp.bqError(data.msg);
            }
        });
    } else {
        jp.bqError("货主或商品不能为空");
    }
}

function cancelTask() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }

    var rowIds = getIdSelections();
    if (rowIds.length < 1) {
        jp.bqError("请选择记录");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/task/banQinWmTaskRp/cancelTask?ids=" + rowIds.join(","), function(data) {
        if (data.success) {
            jp.success(data.msg);
            $('#banQinWmTaskRpTable').bootstrapTable('refresh');
        } else {
            jp.bqError(data.msg);
        }
    });
}

function confirmTask() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }

    var rowIds = getIdSelections();
    if (rowIds.length < 1) {
        jp.bqError("请选择记录");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/task/banQinWmTaskRp/confirmTask?ids=" + rowIds.join(","), function(data) {
        if (data.success) {
            jp.success(data.msg);
            $('#banQinWmTaskRpTable').bootstrapTable('refresh');
        } else {
            jp.bqError(data.msg);
        }
    });
}

function exportData() {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    bq.exportExcel("${ctx}/wms/task/banQinWmTaskRp/export", "补货任务记录" + jp.dateFormat(new Date(), "yyyyMMddhhmmss"), $("#searchForm"), function () {
        jp.close();
    });
}

</script>