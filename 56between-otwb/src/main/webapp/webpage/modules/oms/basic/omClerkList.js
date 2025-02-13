<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#omClerkTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/oms/basic/omClerk/data",
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
            field: 'clerkCode',
            title: '业务员代码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'clerkName',
            title: '业务员名称',
            sortable: true
        }, {
            field: 'phone',
            title: '联系电话',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注信息',
            sortable: true
        }]
    });
    $('#omClerkTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#omClerkTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
    $("#search").click("click", function () {// 绑定查询按扭
        $('#omClerkTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#omClerkTable').bootstrapTable('refresh');
    });
});

function getIdSelections() {
    return $.map($("#omClerkTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该业务员记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/oms/basic/omClerk/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#omClerkTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增业务员', "${ctx}/oms/basic/omClerk/form", '800px', '500px', $('#omClerkTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="basic:omClerk:edit">
        jp.openDialog('编辑业务员', "${ctx}/oms/basic/omClerk/form?id=" + id, '800px', '500px', $('#omClerkTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="basic:omClerk:edit">
        jp.openDialogView('查看业务员', "${ctx}/oms/basic/omClerk/form?id=" + id, '800px', '500px', $('#omClerkTable'));
    </shiro:lacksPermission>
}

</script>