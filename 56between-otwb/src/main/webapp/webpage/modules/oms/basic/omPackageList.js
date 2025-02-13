<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#omPackageTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#omPackageTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#omPackageTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/oms/basic/omPackage/data",
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
            field: 'cdpaIsUse',
            title: '是否启用',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_IS_STOP'))}, value, "-");
            }
        }]
    });
    $('#omPackageTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#omPackageTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#omPackageTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该包装规格记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/oms/basic/omPackage/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#omPackageTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增包装规格', "${ctx}/oms/basic/omPackage/form", '80%', '80%', $('#omPackageTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="basic:omPackage:edit">
        jp.openDialog('编辑包装规格', "${ctx}/oms/basic/omPackage/form?id=" + id,'80%', '80%', $('#omPackageTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="basic:omPackage:edit">
        jp.openDialogView('查看包装规格', "${ctx}/oms/basic/omPackage/form?id=" + id,'80%', '80%', $('#omPackageTable'));
    </shiro:lacksPermission>
}

</script>