<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	if ($("#id").val().length > 0) {
		$('#transportEquipmentTypeCode').prop('readonly', true);
		$('#equipmentSpace_add').attr('disabled', false);
	} else {
		var $dataSet = ${fns:toJson(fns:getUserDataSet())};
		$("#dataSet").val($dataSet.code);
		$("#dataSetName").val($dataSet.name);
	}
	$('#dataSetName').prop('readonly', true);
	$('#dataSetNameSBtnId').prop('disabled', true);
	$('#dataSetNameDBtnId').prop('disabled', true);

	$('#tmTransportEquipmentSpaceTable').bootstrapTable({
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/common/tms/transportEquipmentSpace/data",
		queryParams: function (params) {
			var searchParam = $("#searchForm").serializeJSON();
			searchParam.transportEquipmentTypeCode = $("#transportEquipmentTypeCode").val();
			searchParam.dataSet = $("#dataSet").val();
			searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
			searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
			searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
			return searchParam;
		},
		onDblClickRow: function (row, $el) {
		<shiro:hasPermission name="sys:common:tms:transportEquipmentSpace:edit">
				editEquipmentSpace(row);
		</shiro:hasPermission>
		},
		columns: [{
			checkbox: true
		},{
			field: 'transportEquipmentNo',
			title: '设备编号*',
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
	});
	$('#tmTransportEquipmentSpaceTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var ids = getEquipmentSpaceIdSelections();
		$('#equipmentSpace_remove').attr('disabled', !ids.length);
		$('#equipmentSpace_edit').attr('disabled', ids.length !== 1);
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
	jp.post("${ctx}/sys/common/tms/transportEquipmentType/save", $('#inputForm').serialize(), function (data) {
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
	$("#equipmentSpace_dataSet").val($("#dataSet").val());
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
	jp.post("${ctx}/sys/common/tms/transportEquipmentSpace/save", $('#tmTransportEquipmentSpaceForm').serialize(), function (data) {
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
		jp.get("${ctx}/sys/common/tms/transportEquipmentSpace/deleteAll?ids=" + getEquipmentSpaceIdSelections(), function (data) {
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