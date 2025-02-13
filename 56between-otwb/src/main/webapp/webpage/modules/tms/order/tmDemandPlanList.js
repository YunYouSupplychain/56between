<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#baseOrgId").val(tmOrg.id);
	$("#orderTimeFm").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$("#orderTimeTo").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#arrivalTimeFm").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#arrivalTimeTo").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmDemandPlanTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		/*$("#orgId").val(jp.getCurrentOrg().orgId);*/
		$("#baseOrgId").val(tmOrg.id);
		$('#tmDemandPlanTable').bootstrapTable('refresh');
	});
});

function initTable() {
    var $table = $('#tmDemandPlanTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/order/tmDemandPlan/data",
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
            field: 'planOrderNo',
            title: '需求计划单号',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_DEMAND_PLAN_STATUS'))}, value, "-");
            }
        }, {
            field: 'orderTime',
            title: '订单时间',
            sortable: true
        }, {
            field: 'ownerName',
            title: '客户',
            sortable: true
        }, {
            field: 'arrivalTime',
            title: '到达时间',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
	return $.map($("#tmDemandPlanTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该需求计划记录吗？', function () {
		jp.loading();
		jp.post("${ctx}/tms/order/tmDemandPlan/deleteAll", {ids: getIdSelections().join(',')}, function (data) {
			if (data.success) {
				$('#tmDemandPlanTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openBQDialog('需求计划', "${ctx}/tms/order/tmDemandPlan/form", '100%', '100%', $('#tmDemandPlanTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openBQDialog('需求计划', "${ctx}/tms/order/tmDemandPlan/form?id=" + id, '100%', '100%', $('#tmDemandPlanTable'));
}
</script>