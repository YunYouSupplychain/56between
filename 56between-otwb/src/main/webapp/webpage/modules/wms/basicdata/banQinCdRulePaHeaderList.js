<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#banQinCdRulePaHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/basicdata/banQinCdRulePaHeader/data",
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
        },{
            field: 'ruleCode',
            title: '规则编码',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        },{
            field: 'ruleName',
            title: '规则名称',
            sortable: true
        }]
    });
    $('#banQinCdRulePaHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinCdRulePaHeaderTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinCdRulePaHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinCdRulePaHeaderTable').bootstrapTable('refresh');
    });
});

function getIdSelections() {
    return $.map($("#banQinCdRulePaHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该上架规则记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/basicdata/banQinCdRulePaHeader/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinCdRulePaHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

function add() {
    jp.openBQDialog('新增上架规则', "${ctx}/wms/basicdata/banQinCdRulePaHeader/form", '90%', '90%', $('#banQinCdRulePaHeaderTable'));
}

function edit(id) {
    if(id == undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="basicdata:banQinCdRulePaHeader:edit">
        jp.openBQDialog('编辑上架规则', "${ctx}/wms/basicdata/banQinCdRulePaHeader/form?id=" + id,'90%', '90%', $('#banQinCdRulePaHeaderTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="basicdata:banQinCdRulePaHeader:edit">
        jp.openBQDialog('查看上架规则', "${ctx}/wms/basicdata/banQinCdRulePaHeader/form?id=" + id,'90%', '90%', $('#banQinCdRulePaHeaderTable'));
    </shiro:lacksPermission>
}

</script>