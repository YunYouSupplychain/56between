<%@ page contentType="text/html;charset=UTF-8" %>
    <script>
    $(document).ready(function () {
        $('#orgId').val(jp.getCurrentOrg().orgId);
        initTable();

        $("#search").click("click", function () {// 绑定查询按扭
            $('#bmsInvoiceObjectTable').bootstrapTable('refresh');
        });
        $("#reset").click("click", function () {// 绑定查询按扭
            $("#searchForm  input").val("");
            $("#searchForm  select").val("");
            $("#searchForm  .select-item").html("");
            $('#bmsInvoiceObjectTable').bootstrapTable('refresh');
            $('#orgId').val(jp.getCurrentOrg().orgId);
        });
    });

function initTable() {
    $('#bmsInvoiceObjectTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/bms/basic/bmsInvoiceObject/data",
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
            field: 'code',
            title: '编码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'name',
            title: '名称',
            sortable: true
        }, {
            field: 'principal',
            title: '负责人',
            sortable: true
        }, {
            field: 'bank',
            title: '开户行',
            sortable: true
        }, {
            field: 'bankAccount',
            title: '银行账户',
            sortable: true
        }, {
            field: 'phone',
            title: '电话',
            sortable: true
        }, {
            field: 'address',
            title: '地址',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注信息',
            sortable: true
        }]
    }).on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#bmsInvoiceObjectTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#bmsInvoiceObjectTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该开票对象记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/basic/bmsInvoiceObject/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#bmsInvoiceObjectTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        });
    });
}

function add() {
    jp.openDialog('新增开票对象', "${ctx}/bms/basic/bmsInvoiceObject/form", '80%', '80%', $('#bmsInvoiceObjectTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="bms:bmsInvoiceObject:edit">
        jp.openDialog('编辑开票对象', "${ctx}/bms/basic/bmsInvoiceObject/form?id=" + id, '80%', '80%', $('#bmsInvoiceObjectTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="bms:bmsInvoiceObject:edit">
        jp.openDialogView('查看开票对象', "${ctx}/bms/basic/bmsInvoiceObject/form?id=" + id, '80%', '80%', $('#bmsInvoiceObjectTable'));
    </shiro:lacksPermission>
}
</script>
