<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#bmsBillFormulaTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#bmsBillFormulaTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#bmsBillFormulaTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/bms/basic/bmsBillFormula/data",
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
            field: 'formulaCode',
            title: '公式编码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'formulaName',
            title: '公式名称',
            sortable: true
        }, {
            field: 'formula',
            title: '公式',
            sortable: true
        }, {
            field: 'remarks',
            title: '公式说明',
            sortable: true
        }]
    });
    $('#bmsBillFormulaTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#bmsBillFormulaTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#bmsBillFormulaTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该计费公式记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/basic/bmsBillFormula/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#bmsBillFormulaTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.alert(data.msg);
            }
        });
    });
}

function add() {
    jp.openDialog('新增计费公式', "${ctx}/bms/basic/bmsBillFormula/form", '80%', '80%', $('#bmsBillFormulaTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
    if (id === undefined) {
        id = getIdSelections();
    }
<shiro:hasPermission name="bms:bmsBillTerms:edit">
        jp.openDialog('编辑计费公式', "${ctx}/bms/basic/bmsBillFormula/form?id=" + id, '80%', '80%', $('#bmsBillFormulaTable'));
</shiro:hasPermission>
    <shiro:lacksPermission name="bms:bmsBillTerms:edit">
        jp.openDialogView('查看计费公式', "${ctx}/bms/basic/bmsBillFormula/form?id=" + id, '80%', '80%', $('#bmsBillFormulaTable'));
</shiro:lacksPermission>
}

</script>