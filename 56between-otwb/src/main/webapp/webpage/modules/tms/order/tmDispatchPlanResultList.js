<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#baseOrgId").val(tmOrg.id);

	$('#tmDispatchPlanResultTable').bootstrapTable({
		cache: false,// 是否使用缓存
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/tms/order/tmDispatchPlan/resultData",
		queryParams: function (params) {
			var searchParam = $("#searchForm").serializeJSON();
			searchParam.orgId = $('#orgId').val();
			searchParam.planNo = $('#planNo').val();
			searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
			searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
			return searchParam;
		},
		rowStyle: function (row, index) {
			if (row.dispatchQty !== 0) {
				if (row.planQty !== row.dispatchQty) {
					return {css: {"background-color": "#f5f19d"}}
				} else if (row.planQty === row.dispatchQty) {
					return {css: {"background-color": "#a1f3a1"}};
				} else {
					return {css: {"background-color": "#fafafa"}};
				}
			} else {
				return {css: {"background-color": "#fafafa"}};
			}
		},
		columns: [{
			field: 'classification',
			title: '分类',
			sortable: false,
			align: 'center',
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_OBJ_CLASSIFICATION'))}, value, "-");
			}
		}, {
			field: 'ownerName',
			title: '加油站',
			sortable: false,
			align: 'center',
		}, {
			field: 'skuName',
			title: '油品',
			sortable: false,
			align: 'center',
		}, {
			field: 'planQty',
			title: '计划量',
			sortable: false,
			align: 'center',
		}, {
			field: 'dispatchQty',
			title: '调度量',
			sortable: false,
			align: 'center',
		}, {
			field: 'unDispatchQty',
			title: '未调度量',
			sortable: false,
			align: 'center'
		}]
	});

	$("#search").click("click", function () {
		$("#tmDispatchPlanResultTable").bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$("#baseOrgId").val(tmOrg.id);
	});
});
</script>