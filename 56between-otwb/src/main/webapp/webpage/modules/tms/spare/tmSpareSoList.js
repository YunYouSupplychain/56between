<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#orderTimeFm").datetimepicker({format: "YYYY-MM-DD 00:00:00"});
    $("#orderTimeTo").datetimepicker({format: "YYYY-MM-DD 23:59:59"});
    initTable();

    $('#search').click(function () {
        $('#tmSpareSoTable').bootstrapTable('refresh');
    });
    $('#reset').click(function () {
        $('#searchForm input').val('');
        $('#searchForm select').val('');
        $('#searchForm .select-item').val('');
        $('#tmSpareSoTable').bootstrapTable('refresh');
    });
});

function initTable() {
    var $table = $('#tmSpareSoTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/spare/so/data",
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
            field: 'spareSoNo',
            title: '出库单号',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'orderTime',
            title: '订单时间',
            sortable: true
        }, {
            field: 'orderStatus',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_SPARE_SO_STATUS'))}, value, "-");
            }
        }, {
            field: 'orderType',
            title: '类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_SPARE_SO_TYPE'))}, value, "-");
            }
        }, {
            field: 'customerNo',
            title: '客户单号',
            sortable: true
        }, {
            field: 'operator',
            title: '操作人',
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
            title: '更新人',
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
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#edit').prop('disabled', length !== 1);
        $('#remove').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#tmSpareSoTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function add() {
    jp.openBQDialog("备件出库单", "${ctx}/tms/spare/so/form", "100%", "100%", $("#tmSpareSoTable"));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openBQDialog("备件出库单", "${ctx}/tms/spare/so/form?id=" + id, "100%", "100%", $("#tmSpareSoTable"));
}

function deleteAll() {
    jp.loading();
    jp.post("${ctx}/tms/spare/so/deleteAll?ids=" + getIdSelections(), {}, function (data) {
        $("#tmSpareSoTable").bootstrapTable('refresh');
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}
</script>