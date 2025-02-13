<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#sysCommonRegionTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#sysCommonRegionTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#sysCommonRegionTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/common/region/data",
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
            field: 'dataSetName',
            title: '数据套',
            sortable: true
        }, {
            field: 'operate',
            title: '操作',
            align: 'center',
            events: {
                'click .area': function (e, value, row, index) {
                    jp.openDialog('区域管理 - 【' + row.code + "/" + row.name + "】", '${ctx}/sys/common/region/place?id=' + row.id, '400px', '650px');
                }
            },
            formatter: function operateFormatter(value, row, index) {
                return ['<a href="#" class="area" title="区域设置"><i class="fa fa-cog"></i> </a>'].join('');
            }
        }]
    });
    $('#sysCommonRegionTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#sysCommonRegionTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#sysCommonRegionTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该数据套记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/sys/common/region/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#sysCommonRegionTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增数据套', "${ctx}/sys/common/region/form", '600px', '280px', $('#sysCommonRegionTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openDialog('编辑数据套', "${ctx}/sys/common/region/form?id=" + id, '600px', '280px', $('#sysCommonRegionTable'));
}
</script>