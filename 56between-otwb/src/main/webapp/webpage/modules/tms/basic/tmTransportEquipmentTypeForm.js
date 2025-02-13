<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	if ($("#id").val().length > 0) {
		$('#transportEquipmentTypeCode').prop('readonly', true);
		$('#equipmentSpace_add').attr('disabled', false);
	} else {
		$("#orgId").val(tmOrg.id);
	}

	$('#tmTransportEquipmentSpaceTable').bootstrapTable({
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/tms/basic/tmTransportEquipmentSpace/data",
		queryParams: function (params) {
			var searchParam = $("#searchForm").serializeJSON();
			searchParam.transportEquipmentTypeCode = $("#transportEquipmentTypeCode").val();
			searchParam.orgId = $("#orgId").val();
			searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
			searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
			searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
			return searchParam;
		},
		onDblClickRow: function (row, $el) {
		<shiro:hasPermission name="basic:tmTransportEquipmentSpace:edit">
			editEquipmentSpace(row);
		</shiro:hasPermission>
		},
		columns: [{
			checkbox: true
		},{
			field: 'transportEquipmentNo',
			title: '设备编号',
			sortable: true
		},{
			field: 'transportEquipmentLocation',
			title: '设备位置',
			sortable: true
		},{
			field: 'isTemperatureControl',
			title: '是否温度控制',
			sortable: true,
			formatter:function(value, row , index){
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
			}
		},{
			field: 'isHumidityControl',
			title: '是否湿度控制',
			sortable: true,
			formatter:function(value, row , index){
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
			}
		},{
			field: 'loadWeight',
			title: '装载重量',
			sortable: true
		},{
			field: 'loadCubic',
			title: '装载容积',
			sortable: true
		},{
			field: 'remarks',
			title: '备注'
		}]
	}).on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $("#tmTransportEquipmentSpaceTable").bootstrapTable('getSelections').length;
		$('#equipmentSpace_remove').attr('disabled', !length);
		$('#equipmentSpace_edit').attr('disabled', length !== 1);
	});
});

function doSubmit($table, index) {
	jp.loading();
	var validator = bq.headerSubmitCheck("#inputForm");
	if (!validator.isSuccess) {
		jp.bqWaring(validator.msg);
		return;
	}
	var disabledObjs = bq.openDisabled("#inputForm");
	jp.post("${ctx}/tms/basic/tmTransportEquipmentType/save", $('#inputForm').serialize(), function (data) {
		if (data.success) {
			jp.success(data.msg);
			$table.bootstrapTable('refresh');
			jp.close(index);
		} else {
			bq.closeDisabled(disabledObjs);
			jp.bqError(data.msg);
		}
	});
}

function getEquipmentSpaceIdSelections() {
	return $.map($("#tmTransportEquipmentSpaceTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function addEquipmentSpace() {
	equipmentSpaceModal();
}

function editEquipmentSpace(row) {
	if(row===undefined){
		row = $("#tmTransportEquipmentSpaceTable").bootstrapTable('getSelections')[0];
	}
	equipmentSpaceModal(row);
}

function equipmentSpaceModal(row) {
	$("#tmTransportEquipmentSpaceForm input").val("");
	$("#tmTransportEquipmentSpaceForm select").val("");
	if (row !== undefined) {
		for (var attr in row) {
			if ($("#equipmentSpace_" + attr).length > 0) {
				$("#equipmentSpace_" + attr).val(row[attr]);
			}
		}
	}
	$("#equipmentSpace_transportEquipmentTypeCode").val($("#transportEquipmentTypeCode").val());
	$("#equipmentSpace_orgId").val($("#orgId").val());
	$("#equipmentSpace_transportEquipmentNo").prop('readonly', $("#equipmentSpace_id").val().length > 0);
	$("#tmTransportEquipmentSpaceModal").modal();
}

function saveEquipmentSpace() {
	jp.loading();
	var validator = bq.headerSubmitCheck("#tmTransportEquipmentSpaceForm");
	if (!validator.isSuccess){
		jp.bqWaring(validator.msg);
		return;
	}
	jp.post("${ctx}/tms/basic/tmTransportEquipmentSpace/save", $('#tmTransportEquipmentSpaceForm').serialize(), function (data) {
		if (data.success) {
			jp.success(data.msg);
			$('#tmTransportEquipmentSpaceTable').bootstrapTable('refresh');
			$("#tmTransportEquipmentSpaceModal").modal('hide');
		} else {
			jp.bqError(data.msg);
		}
	});
}

function delEquipmentSpace() {
	jp.confirm('确认要删除该运输设备空间记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/tms/basic/tmTransportEquipmentSpace/deleteAll?ids=" + getEquipmentSpaceIdSelections(), function (data) {
			if (data.success) {
				$('#tmTransportEquipmentSpaceTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

</script>