<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#lotAtt01').datetimepicker({format: "YYYY-MM-DD"});
    $('#lotAtt02').datetimepicker({format: "YYYY-MM-DD"});
    $('#lotAtt03').datetimepicker({format: "YYYY-MM-DD"});
	$('#wmRepInvDailyTable').bootstrapTable({
		showRefresh: false,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: false,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/wms/report/banQinWmRepInvDaily/zone/data",
		queryParams: function (params) {
			var searchParam = $("#searchForm").serializeJSON();
			searchParam.orgId = jp.getCurrentOrg().orgId;
			searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
			searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
			searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
			return searchParam;
		},
        onLoadSuccess: function () {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
		    jp.post("${ctx}/wms/report/banQinWmRepInvDaily/zone/total", searchParam, function (data) {
		        if (data) {
                    $('#totalQty').val(data.totalQty);
                    $('#totalWeight').val(data.totalWeight);
                }
            });
        },
		columns: [{
			checkbox: true
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
			field: 'lotNum',
			title: '批次号',
			sortable: true
		},
		{
			field: 'invQty',
			title: '数量',
			sortable: true
		},
        {
            field: 'weight',
            title: '重量',
            sortable: true
        },
		{
			field: 'lotAtt01',
			title: '生产日期',
			sortable: true
		},
		{
			field: 'lotAtt02',
			title: '失效日期',
			sortable: true
		},
		{
			field: 'lotAtt02',
			title: '入库日期',
			sortable: true
		},
        {
            field: 'lotAtt04',
            title: '品质',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_ATT'))}, value, "-");
            }
        },
		{
			field: 'lotAtt05',
			title: '批次属性5',
			sortable: true
		},
		{
			field: 'lotAtt06',
			title: '批次属性6',
			sortable: true
		},
		{
			field: 'lotAtt07',
			title: '批次属性7',
			sortable: true
		},
		{
			field: 'lotAtt08',
			title: '批次属性8',
			sortable: true
		},
		{
			field: 'lotAtt09',
			title: '批次属性9',
			sortable: true
		},
		{
			field: 'lotAtt10',
			title: '批次属性10',
			sortable: true
		},
		{
			field: 'lotAtt11',
			title: '批次属性11',
			sortable: true
		},
		{
			field: 'lotAtt12',
			title: '批次属性12',
			sortable: true
		}]
	});

	if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
		$('#wmRepInvDailyTable').bootstrapTable("toggleView");
	}

	$("#search").click("click", function () {
		$('#wmRepInvDailyTable').bootstrapTable('refresh');
	});

	$("#reset").click("click", function () {
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$('#wmRepInvDailyTable').bootstrapTable('refresh');
	});

});

function exportData() {
	$('#orgId').val(jp.getCurrentOrg().orgId);
	bq.exportExcel("${ctx}/wms/report/banQinWmRepInvDaily/zone/export", "库区库存报表", $("#searchForm"));
}

</script>