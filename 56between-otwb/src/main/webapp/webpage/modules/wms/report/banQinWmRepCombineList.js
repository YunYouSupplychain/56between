<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#tranTimeFm').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$('#tranTimeTo').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});

	$('#wmRepCombineTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/wms/report/banQinWmRepCombine/data",
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
			field: 'ownerCode',
			title: '货主编码',
			sortable: true
		}, {
			field: 'ownerName',
			title: '货主名称',
			sortable: true
		}, {
			field: 'skuCode',
			title: '商品编码',
			sortable: true
		}, {
			field: 'skuName',
			title: '商品名称',
			sortable: true
		}, {
			field: 'lotNum',
			title: '批次号',
			sortable: true
		}, {
            field: 'lotAtt01',
            title: '生产日期',
            sortable: true
        }, {
			field: 'uom',
			title: '包装单位',
			sortable: true
		}, {
			field: 'qckc',
			title: '期初库存',
			sortable: true
		}, {
			field: 'qckcUom',
			title: '期初库存箱数',
			sortable: true
        }, {
			field: 'bqrk',
			title: '本期入库',
			sortable: true
		}, {
			field: 'bqrkUom',
			title: '本期入库箱数',
			sortable: true
		}, {
			field: 'bqck',
			title: '本期出库',
			sortable: true
		}, {
			field: 'bqckUom',
			title: '本期出库箱数',
			sortable: true
		}, {
			field: 'bqtzzjs',
			title: '本期调整增加数',
			sortable: true
		}, {
			field: 'bqtzzjsUom',
			title: '本期调整增加箱数',
			sortable: true
		}, {
			field: 'bqtzjss',
			title: '本期调整减少数',
			sortable: true
		}, {
			field: 'bqtzjssUom',
			title: '本期调整减少箱数',
			sortable: true
		}, {
			field: 'bqzr',
			title: '本期转入数',
			sortable: true
		}, {
			field: 'bqzrUom',
			title: '本期转入箱数',
			sortable: true
		}, {
			field: 'bqzc',
			title: '本期转出数',
			sortable: true
		}, {
			field: 'bqzcUom',
			title: '本期转出箱数',
			sortable: true
		}, {
			field: 'qmkc',
			title: '期末库存',
			sortable: true
		}, {
			field: 'qmkcUom',
			title: '期末库存箱数',
			sortable: true
		}]
	});

	$("#search").click("click", function () {
		$('#wmRepCombineTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$('#wmRepCombineTable').bootstrapTable('refresh');
	});
});

function exportData() {
	$('#orgId').val(jp.getCurrentOrg().orgId);
	bq.exportExcel("${ctx}/wms/report/banQinWmRepCombine/export", "进出存合并数据记录", $("#searchForm"), function () {
		jp.close();
	});
}

</script>