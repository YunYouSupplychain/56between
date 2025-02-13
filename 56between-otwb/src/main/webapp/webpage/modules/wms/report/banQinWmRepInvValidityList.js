<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#tranTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});

	$('#wmRepInvValidityTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/wms/report/banQinWmRepInvValidity/data",
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
			field: 'invQty',
			title: '数量',
			sortable: true
		}, {
            field: 'lifeType',
            title: '周期类型',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_LIFE_TYPE'))}, value, "-");
            }
        }, {
            field: 'outLifeDays',
            title: '出库效期',
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
			title: '品质',
			sortable: true
		}, {
			field: 'lotAtt05',
			title: '批次属性5',
			sortable: true
		}, {
			field: 'lotAtt06',
			title: '批次属性6',
			sortable: true
		}, {
			field: 'lotAtt07',
			title: '批次属性7',
			sortable: true
		}, {
			field: 'lotAtt08',
			title: '批次属性8',
			sortable: true
		}, {
			field: 'lotAtt09',
			title: '批次属性9',
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
		}]
	});

	$("#search").click("click", function () {
		$('#wmRepInvValidityTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$('#wmRepInvValidityTable').bootstrapTable('refresh');
	});
});

function exportData() {
	$('#orgId').val(jp.getCurrentOrg().orgId);
	bq.exportExcel("${ctx}/wms/report/banQinWmRepInvValidity/export", "库存效期报表", $("#searchForm"), function () {
		jp.close();
	});
}

</script>