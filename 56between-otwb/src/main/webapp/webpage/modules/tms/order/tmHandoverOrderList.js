<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#baseOrgId").val(tmOrg.id);
	$("#dispatchTimeFm").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$("#dispatchTimeTo").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmHandoverOrderTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		/*$("#orgId").val(jp.getCurrentOrg().orgId);*/
		$("#baseOrgId").val(tmOrg.id);
		$('#tmHandoverOrderTable').bootstrapTable('refresh');
	});
});

function initTable() {
    var $table = $('#tmHandoverOrderTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/order/tmHandoverOrder/data",
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
            field: 'handoverNo',
            title: '交接单号',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'status',
            title: '交接单状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_HANDOVER_STATUS'))}, value, "-");
            }
        }, {
            field: 'dispatchNo',
            title: '派车单号',
            sortable: true
        }, {
            field: 'dispatchTime',
            title: '派车时间',
            sortable: true
        }, {
            field: 'dispatchOutletName',
            title: '派车网点',
            sortable: true
        }, {
            field: 'deliveryOutletName',
            title: '配送网点',
            sortable: true
        }, {
            field: 'carrierName',
            title: '承运商',
            sortable: true
        }, {
            field: 'carNo',
            title: '车牌号',
            sortable: true
        }, {
            field: 'driverName',
            title: '司机',
            sortable: true
        }, {
            field: 'driverTel',
            title: '司机联系电话',
            sortable: true
        }, {
            field: 'handoverPerson',
            title: '交接人',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#handover').prop('disabled', !length);
    });
}

function getIdSelections() {
	return $.map($("#tmHandoverOrderTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该交接单记录吗？', function () {
		jp.loading();
		jp.post("${ctx}/tms/order/tmHandoverOrder/deleteAll", {ids: getIdSelections().join(',')}, function (data) {
			if (data.success) {
				$('#tmHandoverOrderTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openBQDialog('交接单', "${ctx}/tms/order/tmHandoverOrder/form", '90%', '90%', $('#tmHandoverOrderTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openBQDialog('交接单', "${ctx}/tms/order/tmHandoverOrder/form?id=" + id, '90%', '90%', $('#tmHandoverOrderTable'));
}

function handover() {
    jp.loading();
    jp.post("${ctx}/tms/order/tmHandoverOrder/handover", {ids: getIdSelections().join(',')}, function (data) {
        $('#tmHandoverOrderTable').bootstrapTable('refresh');
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}
</script>