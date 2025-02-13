<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#orgId").val(tmOrg.id);
	initTable();

	$("#btnImport").click(function () {
		jp.open({
			type: 1,
			area: [500, 300],
			title: "导入数据",
			content: $("#importBox").html(),
			btn: ['下载模板', '确定', '关闭'],
			btn1: function (index, layero) {
				window.location = '${ctx}/tms/basic/tmCarrierRouteRelation/import/template';
			},
			btn2: function (index, layero) {
				var inputForm = top.$("#importForm");
				var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe
				inputForm.attr("target", top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
				inputForm.onsubmit = function () {
					jp.loading('  正在导入，请稍等...');
				}
				inputForm.submit();
				jp.close(index);
			},
			btn3: function (index) {
				jp.close(index);
			}
		});
	});
	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmCarrierRouteTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$("#orgId").val(tmOrg.id);
		$("#transportObjType").val("CARRIER");
		$('#tmCarrierRouteTable').bootstrapTable('refresh');
	});
});

function initTable() {
	$('#tmCarrierRouteTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/tms/basic/tmCarrierRouteRelation/data",
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
			field: 'code',
			title: '编码',
			sortable: true,
			formatter: function (value, row, index) {
				return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
			}
		}, {
			field: 'name',
			title: '名称',
			sortable: true
		}, {
			field: 'carrierName',
			title: '承运商',
			sortable: true
		}, {
			field: 'origin',
			title: '起始地',
			sortable: true
		}, {
			field: 'destination',
			title: '目的地',
			sortable: true
		}, {
			field: 'mileage',
			title: '标准里程(km)',
			sortable: true
		}, {
			field: 'time',
			title: '标准时效(h)',
			sortable: true
		}]
	}).on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $("#tmCarrierRouteTable").bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
		$('#enable').prop('disabled', !length);
		$('#unable').prop('disabled', !length);
	});
}

function getIdSelections() {
	return $.map($("#tmCarrierRouteTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该路由信息记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/tms/basic/tmCarrierRouteRelation/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#tmCarrierRouteTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openDialog('承运商路由信息', "${ctx}/tms/basic/tmCarrierRouteRelation/form", '80%', '250px', $('#tmCarrierRouteTable'));
}

function edit(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialog('承运商路由信息', "${ctx}/tms/basic/tmCarrierRouteRelation/form?id=" + id, '80%', '250px', $('#tmCarrierRouteTable'));
}

function enable(flag) {
	jp.loading();
	jp.post("${ctx}/tms/basic/tmCarrierRouteRelation/enable?ids=" + getIdSelections() + "&flag=" + flag, {}, function (data) {
		if (data.success) {
			$('#tmCarrierRouteTable').bootstrapTable('refresh');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}
</script>