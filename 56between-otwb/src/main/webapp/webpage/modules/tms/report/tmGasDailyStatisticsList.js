<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#queryDate").datetimepicker({format: "YYYY-MM-DD"});
    initTable();

    $("#search").click("click", function () {
        $("#tmGasDailyStatisticsTable").bootstrapTable('refresh', {url: "${ctx}/tms/report/gasStationDailyStatistics/data"});
    });

    $("#reset").click("click", function () {
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
    });

    $("#export").click("click", exportExcel);
});

function initTable() {
    var $table = $('#tmGasDailyStatisticsTable');
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
            field: 'date',
            title: '日期',
            sortable: true
        }, {
            field: 'gasName',
            title: '加油站',
            sortable: true
        }, {
            field: 'oilName',
            title: '油品'
        }, {
            field: 'orderQty',
            title: '调度量'
        }, {
            field: 'pickupQty',
            title: '提油量'
        }, {
            field: 'dischargeQty',
            title: '卸油量'
        }]
    });
}

function exportExcel() {
    var searchParam = $("#searchForm").serializeJSON();
    searchParam.baseOrgId = tmOrg.id;
    searchParam.orgId = jp.getCurrentOrg().orgId;
    bq.exportExcelNew("${ctx}/tms/report/gasStationDailyStatistics/export", "加油站每日统计表", searchParam);
}
</script>