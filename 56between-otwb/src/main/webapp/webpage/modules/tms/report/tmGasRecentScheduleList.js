<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {
        $("#tmGasRecentScheduleTable").bootstrapTable('refresh', {url: "${ctx}/tms/report/gasRecentSchedule/data"});
    });
    $("#reset").click("click", function () {
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
    });
    $("#export").click("click", exportExcel);
});

function initTable() {
    var $table = $('#tmGasRecentScheduleTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.baseOrgId = tmOrg.id;
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'ownerName',
            title: '客户',
            sortable: true
        }, {
            field: 'skuName',
            title: '商品',
            sortable: true
        }, {
            field: 'lastPlanTime',
            title: '最近一次计划时间',
            sortable: true
        }, {
            field: 'planQty',
            title: '计划量'
        }, {
            field: 'dailyAverageQty',
            title: '近30日日均量'
        }]
    });
}

function exportExcel() {
    var searchParam = $("#searchForm").serializeJSON();
    searchParam.baseOrgId = tmOrg.id;
    searchParam.orgId = jp.getCurrentOrg().orgId;
    bq.exportExcelNew("${ctx}/tms/report/gasRecentSchedule/export", "加油站最近计划表", searchParam);
}
</script>