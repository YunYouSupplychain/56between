<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#fmDate").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#toDate").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initTable();

    $("#search").click("click", function () {
        $("#tmTransportationProfitTable").bootstrapTable('refresh', {url: "${ctx}/tms/report/transportationProfit/data"});
    });
    $("#reset").click("click", function () {
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
    });
    $("#export").click("click", exportExcel);
});

function initTable() {
    var $table = $('#tmTransportationProfitTable');
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
            field: 'transportNo',
            title: '运输单号',
            sortable: true
        }, {
            field: 'customerName',
            title: '客户',
            sortable: true
        }, {
            field: 'orderTime',
            title: '订单时间',
            sortable: true
        }, {
            field: 'shipCity',
            title: '发货地区'
        }, {
            field: 'consigneeCity',
            title: '收货地区'
        }, {
            field: 'income',
            title: '收入金额'
        }, {
            field: 'expenditure',
            title: '成本金额'
        }, {
            field: 'profit',
            title: '利润金额'
        }, {
            field: 'profitRate',
            title: '利润率'
        }]
    });
}

function exportExcel() {
    var searchParam = $("#searchForm").serializeJSON();
    searchParam.baseOrgId = tmOrg.id;
    searchParam.orgId = jp.getCurrentOrg().orgId;
    bq.exportExcelNew("${ctx}/tms/report/transportationProfit/export", "运输利润率报表", searchParam);
}
</script>