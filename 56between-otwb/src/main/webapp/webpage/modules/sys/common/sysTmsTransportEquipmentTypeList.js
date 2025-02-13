<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmTransportEquipmentTypeTable').bootstrapTable('refresh');
	});

	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$('#tmTransportEquipmentTypeTable').bootstrapTable('refresh');
	});

});

function initTable() {
	$('#tmTransportEquipmentTypeTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/common/tms/transportEquipmentType/data",
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
			field: 'transportEquipmentTypeCode',
			title: '设备类型编码',
			sortable: true
			, formatter: function (value, row, index) {
				return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
			}
		}, {
			field: 'transportEquipmentTypeNameCn',
			title: '中文名称',
			sortable: true
		}, {
			field: 'transportEquipmentTypeNameEn',
			title: '英文名称',
			sortable: true
		}, {
			field: 'temperatureType',
			title: '温度类别',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TEMPERATURE_TYPE'))}, value, "-");
			}
		}, {
			field: 'delFlag',
			title: '启用停用',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('del_flag'))}, value, "-");
			}
		}, {
			field: 'isTemperatureControl',
			title: '是否温度控制',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
			}
		}, {
			field: 'minAllowTemperature',
			title: '最低允许温度',
			sortable: true
		}, {
			field: 'maxAllowTemperature',
			title: '最高允许温度',
			sortable: true
		}, {
			field: 'isHumidityControl',
			title: '是否湿度控制',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
			}
		}, {
			field: 'minAllowHumidity',
			title: '最低允许湿度',
			sortable: true
		}, {
			field: 'maxAllowHumidity',
			title: '最高允许湿度',
			sortable: true
		}, {
			field: 'isContainer',
			title: '是否集装箱',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
			}
		}, {
			field: 'onlyAllowLastInLastOut',
			title: '只允许后进后出',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
			}
		}, {
			field: 'isFixedEquipmentSpace',
			title: '设备空间是否固定',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
			}
		}, {
			field: 'internalLength',
			title: '内部长度',
			sortable: true
		}, {
			field: 'internalWidth',
			title: '内部宽度',
			sortable: true
		}, {
			field: 'internalHeight',
			title: '内部高度',
			sortable: true
		}, {
			field: 'externalLength',
			title: '外部长度',
			sortable: true
		}, {
			field: 'externalWidth',
			title: '外部宽度',
			sortable: true
		}, {
			field: 'externalHeight',
			title: '外部高度',
			sortable: true
		}, {
			field: 'doorWidth',
			title: '门宽',
			sortable: true
		}, {
			field: 'doorHeight',
			title: '门高',
			sortable: true
		}, {
			field: 'minLoadWeight',
			title: '最小载重量',
			sortable: true
		}, {
			field: 'maxLoadWeight',
			title: '最大载重量',
			sortable: true
		}, {
			field: 'leftLimitMaxLoadWeight',
			title: '左侧限制最大载重',
			sortable: true
		}, {
			field: 'rightLimitMaxLoadWeight',
			title: '右侧限制最大载重',
			sortable: true
		}, {
			field: 'leftRightDiffLimitMaxLoadWeight',
			title: '左右差异限制最大载重',
			sortable: true
		}, {
			field: 'minLoadCubic',
			title: '最小装载容积',
			sortable: true
		}, {
			field: 'maxLoadCubic',
			title: '最大装载容积',
			sortable: true
		}, {
			field: 'allowOverweightRate',
			title: '允许超重比例(%)',
			sortable: true
		}]
	});
	$('#tmTransportEquipmentTypeTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var ids = getIdSelections();
		$('#remove').prop('disabled', !ids.length);
		$('#edit').prop('disabled', ids.length !== 1);
		$('#enable').prop('disabled', !ids.length);
		$('#unable').prop('disabled', !ids.length);
		$('#syncSelect').prop('disabled', !length);
	});
}

function getIdSelections() {
	return $.map($("#tmTransportEquipmentTypeTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该运输设备类型记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/sys/common/tms/transportEquipmentType/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#tmTransportEquipmentTypeTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openDialog('运输设备类型', "${ctx}/sys/common/tms/transportEquipmentType/form", '100%', '100%', $('#tmTransportEquipmentTypeTable'));
}

function edit(id){
	if (id === undefined) {
		id = getIdSelections();
	}
  jp.openDialog('运输设备类型', "${ctx}/sys/common/tms/transportEquipmentType/form?id=" + id,'100%', '100%', $('#tmTransportEquipmentTypeTable'));
}

function enable(flag) {
	jp.loading();
	jp.post("${ctx}/sys/common/tms/transportEquipmentType/enable?ids=" + getIdSelections() + "&flag=" + flag, {}, function (data) {
		if (data.success) {
			$('#tmTransportEquipmentTypeTable').bootstrapTable('refresh');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}

function syncSelect() {
	var rowIds = getIdSelections();
	if (rowIds.length < 1) {
		jp.warning("请勾选需要同步的记录");
		return;
	}
	jp.loading("同步中...");
	jp.post("${ctx}/sys/common/tms/transportEquipmentType/syncSelect", {"ids": rowIds.join(',')}, function (data) {
		jp.alert(data.msg);
	});
}

function syncAll() {
	jp.confirm("确认同步全部数据？", function () {
		jp.loading("同步中...");
		jp.get("${ctx}/sys/common/tms/transportEquipmentType/syncAll", function (data) {
			jp.alert(data.msg);
		});
	});
}

</script>