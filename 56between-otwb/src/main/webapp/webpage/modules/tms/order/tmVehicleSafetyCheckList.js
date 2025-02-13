<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#baseOrgId").val(tmOrg.id);
	$("#checkDateFm").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$("#checkDateTo").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmVehicleSafetyCheckTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		/*$("#orgId").val(jp.getCurrentOrg().orgId);*/
		$("#baseOrgId").val(tmOrg.id);
		$('#tmVehicleSafetyCheckTable').bootstrapTable('refresh');
	});
});

function initTable() {
    var $table = $('#tmVehicleSafetyCheckTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/order/tmVehicleSafeTyCheck/data",
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
            field: 'vehicleNo',
            title: '需求计划单号',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'checkDate',
            title: '检查日期',
            sortable: true
        }, {
            field: 'confirmConclusion',
            title: '确认结论',
            sortable: true
        }, {
            field: 'safetySign',
            title: '安管员',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $("#tmVehicleSafetyCheckTable").bootstrapTable('getSelections').length;
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
	return $.map($("#tmVehicleSafetyCheckTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function add() {
	jp.openBQDialog('需求计划', "${ctx}/tms/order/tmVehicleSafeTyCheck/form", '100%', '100%', $('#tmVehicleSafetyCheckTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openBQDialog('需求计划', "${ctx}/tms/order/tmVehicleSafeTyCheck/form?id=" + id, '100%', '100%', $('#tmVehicleSafetyCheckTable'));
}
</script>