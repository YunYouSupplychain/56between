<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#sysRuleGenNoTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/ruleGenNo/data",
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
            field: 'billType',
            title: '编号类型',
            sortable: true
        }, {
            field: 'prefix',
            title: '前缀',
            sortable: true
        }, {
            field: 'stamp',
            title: '时间戳',
            sortable: true
        }, {
            field: 'clearCycle',
            title: '规则清空周期',
            sortable: true
        }, {
            field: 'serialNoMaxLength',
            title: '流水长度',
            sortable: true
        }, {
            field: 'currentSerialNo',
            title: '当前流水号',
            sortable: true
        }, {
            field: 'remarks',
            title: '说明',
            sortable: true
        }]
    }).on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#sysRuleGenNoTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#sysRuleGenNoTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#sysRuleGenNoTable').bootstrapTable('refresh');
    });
});

function getIdSelections() {
    return $.map($("#sysRuleGenNoTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该编号生成规则记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/sys/ruleGenNo/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#sysRuleGenNoTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增编号生成规则', "${ctx}/sys/ruleGenNo/form", '800px', '280px', $('#sysRuleGenNoTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="sys:ruleGenNo:edit">
        jp.openDialog('编辑编号生成规则', "${ctx}/sys/ruleGenNo/form?id=" + id, '800px', '280px', $('#sysRuleGenNoTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="sys:ruleGenNo:edit">
        jp.openDialogView('查看编号生成规则', "${ctx}/sys/ruleGenNo/form?id=" + id, '800px', '280px', $('#sysRuleGenNoTable'));
    </shiro:lacksPermission>
}

</script>