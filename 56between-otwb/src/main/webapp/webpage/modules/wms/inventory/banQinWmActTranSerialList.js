<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#banQinWmActTranSerialTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/wms/inventory/banQinWmActTranSerial/data",
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
			field: 'serialTranId',
			title: '序列号交易Id',
			sortable: true
		}, {
			field: 'serialTranType',
			title: '交易类型',
			sortable: true,
			formatter:function(value, row , index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SERIAL_TRAN_TYPE'))}, value, "-");
			}
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
			field: 'serialNo',
			title: '序列号',
			sortable: true
		}, {
			field: 'orderType',
			title: '订单类型',
			sortable: true
		}, {
			field: 'orderNo',
			title: '单据号',
			sortable: true
		}, {
			field: 'lineNo',
			title: '行号',
			sortable: true
		}, {
			field: 'tranId',
			title: '库存交易Id',
			sortable: true
		}, {
			field: 'tranOp',
			title: '操作人',
			sortable: true
		}, {
			field: 'tranTime',
			title: '交易时间',
			sortable: true
		}, {
			field: 'lotAtt01',
			title: '生产日期',
			sortable: true
		}, {
			field: 'lotAtt02',
			title: '失效日期',
			sortable: true
		}, {
			field: 'lotAtt03',
			title: '入库日期',
			sortable: true
		}, {
			field: 'lotAtt04',
			title: '批次属性4',
			sortable: true
		}, {
			field: 'lotAtt05',
			title: '批次属性05',
			sortable: true
		}, {
			field: 'lotAtt06',
			title: '批次属性06',
			sortable: true
		}, {
			field: 'lotAtt07',
			title: '批次属性07',
			sortable: true
		}, {
			field: 'lotAtt08',
			title: '批次属性08',
			sortable: true
		}, {
			field: 'lotAtt09',
			title: '批次属性09',
			sortable: true
		}, {
			field: 'lotAtt10',
			title: '批次属性10',
			sortable: true
		}, {
			field: 'lotAtt11',
			title: '批次属性11',
			sortable: true
		}, {
			field: 'lotAtt12',
			title: '批次属性12',
			sortable: true
		}, {
			field: 'createBy.name',
			title: '创建人',
			sortable: true
		}, {
			field: 'createDate',
			title: '创建时间',
			sortable: true
		}, {
			field: 'updateBy.name',
			title: '修改人',
			sortable: true
		}, {
			field: 'updateDate',
			title: '修改时间',
			sortable: true
		}, {
			field: 'orgName',
			title: '仓库',
			sortable: true
		}]
	});

	$("#search").click("click", function () {
		$('#banQinWmActTranSerialTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$('#banQinWmActTranSerialTable').bootstrapTable('refresh');
	});
});

function exportData() {
	$('#orgId').val(jp.getCurrentOrg().orgId);
	bq.exportExcel("${ctx}/wms/inventory/banQinWmActTranSerial/export", "库存序列号交易记录", $("#searchForm"), function () {
		jp.close();
	});
}
</script>