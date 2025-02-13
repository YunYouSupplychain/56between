<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#createTimeFm').datetimepicker({format: "YYYY-MM-DD 00:00:00"});
    $('#createTimeTo').datetimepicker({format: "YYYY-MM-DD 23:59:59"});
    $('#opOrgId').val(jp.getCurrentOrg().orgId);
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        search();
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
    });
});

function initTable() {
    var $table = $('#bmsBillTable');
    $table.bootstrapTable({
        showRefresh: true,
        showColumns: true,
        showExport: true,
        cache: false,
        pagination: true,
        sidePagination: "server",
        url: "${ctx}/bms/finance/bmsBill/data",
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
        }
        , {
            field: 'confirmNo',
            title: '账单号',
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }
        , {
            field: 'status',
            title: '状态',
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_STATUS'))}, value, "-");
            }
        }
        , {
            field: 'settleObjCode',
            title: '结算对象代码'
        }
        , {
            field: 'settleObjName',
            title: '结算对象名称',
            sortable: true
        }
        , {
            field: 'amount',
            title: '总费用金额',
            sortable: true
        }
        , {
            field: 'writeOffAmount',
            title: '核销金额',
            sortable: true
        }
        , {
            field: 'remarks',
            title: '备注',
            sortable: true
        }
        , {
            field: 'orgName',
            title: '机构',
            sortable: true
        }
        , {
            field: 'createBy.name',
            title: '创建人',
            sortable: true
        }
        , {
            field: 'createDate',
            title: '创建时间',
            sortable: true
        }
        , {
            field: 'updateBy.name',
            title: '修改人',
            sortable: true
        }
        , {
            field: 'updateDate',
            title: '修改时间',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#export').prop('disabled', length !== 1);
        $('#export1').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#bmsBillTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该费用明细记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/finance/bmsBill/deleteAll?ids=" + getIdSelections(), function (data) {
            search();
            if (data.success) {
                jp.success(data.msg);
            } else {
                jp.alert(data.msg);
            }
        });
    });
}

function add() {
    jp.openBQDialog('新增账单', "${ctx}/bms/finance/bmsBill/form", '90%', '90%', $('#bmsBillTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openBQDialog('编辑账单', "${ctx}/bms/finance/bmsBill/form?id=" + id, '90%', '90%', $('#bmsBillTable'));
}

function view(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openDialogView('查看账单', "${ctx}/bms/finance/bmsBill/form?id=" + id, '90%', '90%');
}

function exportExcel() {
    bq.exportExcelNew("${ctx}/bms/finance/bmsBill/export", "费用明细账单", $("#bmsBillTable").bootstrapTable('getSelections')[0]);
}

function exportExcel1() {
    bq.exportExcelNew("${ctx}/bms/finance/bmsBill/export1", "费用统计账单", $("#bmsBillTable").bootstrapTable('getSelections')[0]);
}

function search() {
    $('#bmsBillTable').bootstrapTable('refresh');
}

</script>