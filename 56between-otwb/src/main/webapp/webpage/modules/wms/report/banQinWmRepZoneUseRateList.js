<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

	$("#search").click("click", function () {
		$('#wmRepZoneUseRateTable').bootstrapTable('refresh', {url: "${ctx}/wms/report/wmRepZoneUseRate/data"});
	});

	$("#reset").click("click", function () {
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
	});

	$('#export').click("click", function () {
        exportData();
    });
});

function initTable() {
    $('#wmRepZoneUseRateTable').bootstrapTable({
        showRefresh: false,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: false,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
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
        },
        {
            field: 'zoneCode',
            title: '库区编码',
            sortable: true
        },
        {
            field: 'zoneName',
            title: '库区名称',
            sortable: true
        },
        {
            field: 'total',
            title: '总数',
            sortable: true
        },
        {
            field: 'use',
            title: '使用',
            sortable: true
        },
        {
            field: 'spare',
            title: '空余',
            sortable: true
        },
        {
            field: 'rate',
            title: '使用率',
            sortable: true
        }]
    });
}

function exportData() {
    jp.loading();
	$('#orgId').val(jp.getCurrentOrg().orgId);
	bq.exportExcel("${ctx}/wms/report/wmRepZoneUseRate/export", "仓储使用率", $("#searchForm"));
}

</script>