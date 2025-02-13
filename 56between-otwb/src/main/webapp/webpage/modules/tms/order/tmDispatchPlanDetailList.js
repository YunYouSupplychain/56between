<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#arrivalTimeFm").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$("#arrivalTimeTo").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$("#baseOrgId").val(tmOrg.id);

	$('#tmDispatchPlanDetailTable').bootstrapTable({
		cache: false,// 是否使用缓存
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/tms/order/tmDispatchPlan/detailData",
		queryParams: function (params) {
			var searchParam = $("#searchForm").serializeJSON();
			searchParam.orgId = $('#orgId').val();
			searchParam.planNo = $('#planNo').val();
			searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
			searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
			searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
			return searchParam;
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
			field: 'arrivalTime',
			title: '到达时间',
			sortable: false,
			align: 'center',
		}, {
			field: 'planQty',
			title: '计划量',
			sortable: false,
			align: 'center',
		}]
	});

	$("#search").click("click", function () {
		$("#tmDispatchPlanDetailTable").bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$("#baseOrgId").val(tmOrg.id);
	});
});
</script>