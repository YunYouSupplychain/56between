<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#sysCommonDataSetTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#sysCommonDataSetTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#sysCommonDataSetTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/common/dataSet/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'code',
            title: '编码',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'name',
            title: '名称',
            sortable: true
        }, {
            field: 'isDefault',
            title: '是否默认',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }]
    });
    $('#sysCommonDataSetTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#sysCommonDataSetTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#setDefault').prop('disabled', length !== 1);
        $('#syncData').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#sysCommonDataSetTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该数据套记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/sys/common/dataSet/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#sysCommonDataSetTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增数据套', "${ctx}/sys/common/dataSet/form", '1000px', '600px', $('#sysCommonDataSetTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openDialog('编辑数据套', "${ctx}/sys/common/dataSet/form?id=" + id, '1000px', '600px', $('#sysCommonDataSetTable'));
}

function setDefault() {
    jp.get("${ctx}/sys/common/dataSet/setDefault?id=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#sysCommonDataSetTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

function syncData() {
    var row = $("#sysCommonDataSetTable").bootstrapTable('getSelections')[0];
    jp.openDialogView('同步【' + row.name + '】数据', "${ctx}/sys/common/dataSet/sync/form?id=" + row.id, '50%', '60%', $('#sysCommonDataSetTable'));
}
</script>