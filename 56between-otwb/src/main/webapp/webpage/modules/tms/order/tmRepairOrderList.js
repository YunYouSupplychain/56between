<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#baseOrgId").val(tmOrg.id);
	$("#orderTimeFm").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$("#orderTimeTo").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmRepairOrderTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		/*$("#orgId").val(jp.getCurrentOrg().orgId);*/
		$("#baseOrgId").val(tmOrg.id);
		$('#tmRepairOrderTable').bootstrapTable('refresh');
	});
});

function initTable() {
    var $table = $('#tmRepairOrderTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/order/tmRepairOrder/data",
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
            field: 'repairNo',
            title: '维修单号',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_REPAIR_STATUS'))}, value, "-");
            }
        }, {
            field: 'orderTime',
            title: '报修时间',
            sortable: true
        }, {
            field: 'ownerName',
            title: '客户',
            sortable: true
        }, {
            field: 'carNo',
            title: '车牌号',
            sortable: true
        }, {
            field: 'driverName',
            title: '驾驶员',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        var cStyle = length ? "auto" : "none";
        var fColor = length ? "#1E1E1E" : "#8A8A8A";

        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#printRepairOrder').css({"pointer-events": cStyle, "color": fColor});
    });
}

function getIdSelections() {
	return $.map($("#tmRepairOrderTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该维修工单记录吗？', function () {
		jp.loading();
		jp.post("${ctx}/tms/order/tmRepairOrder/deleteAll", {ids: getIdSelections().join(',')}, function (data) {
			if (data.success) {
				$('#tmRepairOrderTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openBQDialog('维修工单', "${ctx}/tms/order/tmRepairOrder/form", '100%', '100%', $('#tmRepairOrderTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openBQDialog('维修工单', "${ctx}/tms/order/tmRepairOrder/form?id=" + id, '100%', '100%', $('#tmRepairOrderTable'));
}

function printRepairOrder() {
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/tms/print/repairOrder", 'ids', rowIds.join(','), '打印维修单');
}
</script>