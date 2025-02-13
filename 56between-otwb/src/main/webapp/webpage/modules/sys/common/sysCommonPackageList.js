<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#sysCommonPackageTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#sysCommonPackageTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#sysCommonPackageTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/common/package/data",
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
            field: 'cdpaCode',
            title: '包装代码',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'cdpaType',
            title: '包装类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_PACKAGE_TYPE'))}, value, "-");
            }
        }, {
            field: 'cdpaFormat',
            title: '包装规格',
            sortable: true
        }, {
            field: 'dataSetName',
            title: '数据套',
            sortable: true
        }, {
            field: 'cdpaIsUse',
            title: '是否启用',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_IS_STOP'))}, value, "-");
            }
        }]
    });
    $('#sysCommonPackageTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#sysCommonPackageTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#syncSelect').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#sysCommonPackageTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该包装规格记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/sys/common/package/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#sysCommonPackageTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增包装规格', "${ctx}/sys/common/package/form", '80%', '80%', $('#sysCommonPackageTable'));
}

function edit(id) {
    if(id === undefined){
        id = getIdSelections();
    }
    jp.openDialog('编辑包装规格', "${ctx}/sys/common/package/form?id=" + id,'80%', '80%', $('#sysCommonPackageTable'));
}

function syncSelect() {
    var rowIds = getIdSelections();
    if (rowIds.length < 1) {
        jp.warning("请勾选需要同步的记录");
        return;
    }
    jp.loading("同步中...");
    jp.post("${ctx}/sys/common/package/syncSelect", {"ids": rowIds.join(',')}, function (data) {
        jp.alert(data.msg);
    });
}

function syncAll() {
    jp.confirm("确认同步全部数据？", function () {
        jp.loading("同步中...");
        jp.get("${ctx}/sys/common/package/syncAll", function (data) {
            jp.alert(data.msg);
        });
    });
}
</script>