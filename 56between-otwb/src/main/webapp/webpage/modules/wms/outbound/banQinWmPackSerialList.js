<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	init();

	$('#banQinWmPackSerialTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/wms/outbound/banQinWmPackSerial/data",
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
			field: 'soNo',
			title: '出库单号',
			sortable: true
		},
		{
			field: 'trackingNo',
			title: '快递单号',
			sortable: true
		},
		{
			field: 'customerNo',
			title: '客户订单号',
			sortable: true
		},
		{
			field: 'ownerCode',
			title: '货主编码',
			sortable: true
		},
		{
			field: 'ownerName',
			title: '货主名称',
			sortable: true
		},
		{
			field: 'skuCode',
			title: '商品编码',
			sortable: true
		},
		{
			field: 'skuName',
			title: '商品名称',
			sortable: true
		},
		{
			field: 'serialNo',
			title: '序列号',
			sortable: true
		},
		{
			field: 'packOp',
			title: '打包人',
			sortable: true
		},
		{
			field: 'packTime',
			title: '打包时间',
			sortable: true
		},
		{
			field: 'caseNo',
			title: '打包箱号',
			sortable: true
		}]
	});

	if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
		$('#banQinWmPackSerialTable').bootstrapTable("toggleView");
	}

	$("#search").click("click", function () {
		$('#banQinWmPackSerialTable').bootstrapTable('refresh');
	});

	$("#reset").click("click", function () {
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		init();
	});

	$('#packTimeFm').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$('#packTimeTo').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
});

function init() {
	$("#packTimeFm input").val(jp.dateFormat(new Date(), "yyyy-MM-dd") + " 00:00:00");
	$("#packTimeTo input").val(jp.dateFormat(new Date(), "yyyy-MM-dd") + " 23:59:59");
}

function exportData() {
	$('#orgId').val(jp.getCurrentOrg().orgId);
	bq.exportExcel("${ctx}/wms/outbound/banQinWmPackSerial/export", "序列号打包记录", $("#searchForm"), function () {
		jp.close();
	});
}

</script>