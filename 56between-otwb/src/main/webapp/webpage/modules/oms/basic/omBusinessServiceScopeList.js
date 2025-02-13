<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#omBusinessServiceScopeTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#omBusinessServiceScopeTable').bootstrapTable('refresh');
    });
});

function initTable() {
    var $table = $('#omBusinessServiceScopeTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/oms/basic/omBusinessServiceScope/data",
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
            field: 'groupCode',
            title: '组编码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'groupName',
            title: '组名称',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注信息',
            sortable: true
        }, {
            field: 'operate',
            title: '操作',
            align: 'center',
            events: {
                'click .area': function (e, value, row, index) {
                    jp.openDialog('区域管理 - 【' + row.groupCode + "/" + row.groupName + "】", '${ctx}/oms/basic/omBusinessServiceScope/area?id=' + row.id, '1000px', '650px');
                }
            },
            formatter: function operateFormatter(value, row, index) {
                return ['<a href="#" class="area" title="区域设置"><i class="fa fa-cog"></i> </a>'].join('');
            }
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#copy').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#omBusinessServiceScopeTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该业务服务范围记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/oms/basic/omBusinessServiceScope/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#omBusinessServiceScopeTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        });
    });
}

function add() {
    jp.openDialog('新增业务服务范围', "${ctx}/oms/basic/omBusinessServiceScope/form", '800px', '500px', $('#omBusinessServiceScopeTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openDialog('编辑业务服务范围', "${ctx}/oms/basic/omBusinessServiceScope/form?id=" + id, '800px', '500px', $('#omBusinessServiceScopeTable'));
}

function view(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openDialogView('查看业务服务范围', "${ctx}/oms/basic/omBusinessServiceScope/form?id=" + id, '800px', '500px', $('#omBusinessServiceScopeTable'));
}

function copy() {
    jp.loading();
    jp.get("${ctx}/oms/basic/omBusinessServiceScope/copy?id=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omBusinessServiceScopeTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}
</script>