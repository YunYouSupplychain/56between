<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#banQinWeighMachineInfoTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/wms/weigh/banQinWeighMachineInfo/data",
		queryParams: function (params) {
			var searchParam = $("#searchForm").serializeJSON();
			searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
			searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
			searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
			return searchParam;
		},
		columns: [{
			checkbox: true
		}, {
			field: 'machineNo',
			title: '设备编码',
			sortable: true
			, formatter: function (value, row, index) {
				return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
			}
		}, {
			field: 'orgName',
			title: '机构',
			sortable: true
		}]
	});
	$('#banQinWeighMachineInfoTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $('#banQinWeighMachineInfoTable').bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
	});

	$("#search").click("click", function () {// 绑定查询按扭
		$('#banQinWeighMachineInfoTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$('#banQinWeighMachineInfoTable').bootstrapTable('refresh');
	});
});

function getIdSelections() {
	return $.map($("#banQinWeighMachineInfoTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该称重设备表记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/wms/weigh/banQinWeighMachineInfo/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#banQinWeighMachineInfoTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		})
	})
}

function add() {
	jp.openDialog('新增称重设备表', "${ctx}/wms/weigh/banQinWeighMachineInfo/form", '600px', '150px%', $('#banQinWeighMachineInfoTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id == undefined) {
		id = getIdSelections();
	}
	<shiro:hasPermission name="wms:weigh:banQinWeighMachineInfo:edit">
		jp.openDialog('编辑称重设备表', "${ctx}/wms/weigh/banQinWeighMachineInfo/form?id=" + id,'600px', '150px', $('#banQinWeighMachineInfoTable'));
	</shiro:hasPermission>
	<shiro:lacksPermission name="wms:weigh:banQinWeighMachineInfo:edit">
		jp.openDialogView('查看称重设备表', "${ctx}/wms/weigh/banQinWeighMachineInfo/form?id=" + id,'600px', '150px', $('#banQinWeighMachineInfoTable'));
	</shiro:lacksPermission>
}

</script>