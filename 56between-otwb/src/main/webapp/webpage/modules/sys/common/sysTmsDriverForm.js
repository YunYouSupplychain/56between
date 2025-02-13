<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#birthDate').datetimepicker({format: "YYYY-MM-DD"});
	if ($("#id").val().length > 0) {
		$('#code').prop('readonly', true);
		$('#driverQualification_add').attr('disabled', false);
	} else {
		var $dataSet = ${fns:toJson(fns:getUserDataSet())};
		$("#dataSet").val($dataSet.code);
		$("#dataSetName").val($dataSet.name);
	}
	$('#dataSetName').prop('readonly', true);
	$('#dataSetNameSBtnId').prop('disabled', true);
	$('#dataSetNameDBtnId').prop('disabled', true);

	$('#tmDriverQualificationTable').bootstrapTable({
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/common/tms/driverQualification/data",
		//查询参数,每次调用是会带上这个参数，可自定义
		queryParams: function (params) {
			var searchParam = $("#searchForm").serializeJSON();
			searchParam.driverCode = $("#code").val();
			searchParam.dataSet = $("#dataSet").val();
			searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
			searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
			searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
			return searchParam;
		},
		onDblClickRow: function (row, $el) {
		<shiro:hasPermission name="sys:common:tms:driverQualification:edit">
			editDriverQualification(row);
		</shiro:hasPermission>
		},
		columns: [{
			checkbox: true
		},{
			field: 'qualificationCode',
			title: '资质编码*',
			sortable: true
		},{
			field: 'qualificationNameCn',
			title: '中文名称',
			sortable: true
		},{
			field: 'qualificationNameEn',
			title: '英文名称',
			sortable: true
		},{
			field: 'qualificationShortName',
			title: '简称',
			sortable: true
		},{
			field: 'expireDate',
			title: '失效日期',
			sortable: true
		},{
			field: 'remarks',
			title: '资质描述',
			sortable: true
		}]
	});
	$('#tmDriverQualificationTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var ids = getDriverQualificationIdSelections();
		$('#driverQualification_remove').attr('disabled', !ids.length);
		$('#driverQualification_edit').attr('disabled', ids.length !== 1);
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
	jp.post("${ctx}/sys/common/tms/driver/save", $('#inputForm').serialize(), function (data) {
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

function getDriverQualificationIdSelections() {
	return $.map($("#tmDriverQualificationTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function addDriverQualification() {
	driverQualificationModal();
}

function editDriverQualification(row) {
	if(row === undefined){
		row = $("#tmDriverQualificationTable").bootstrapTable('getSelections')[0];
	}
	driverQualificationModal(row);
}

function driverQualificationModal(row) {
	$("#tmDriverQualificationForm input").val("");
	$("#tmDriverQualificationForm select").val("");
	$('#driverQualification_effectiveDate').datetimepicker({format: "YYYY-MM-DD"});
	$('#driverQualification_expireDate').datetimepicker({format: "YYYY-MM-DD"});
	if (row !== undefined) {
		for (var attr in row) {
			if ($("#driverQualification_" + attr).length > 0) {
				$("#driverQualification_" + attr).val(row[attr]);
			}
		}
	}
	$("#driverQualification_driverCode").val($("#code").val());
	$("#driverQualification_dataSet").val($("#dataSet").val());
	$("#driverQualification_qualificationCode").prop('readonly', $("#driverQualification_id").val().length > 0);
	$("#tmDriverQualificationModal").modal();
}

function saveDriverQualification() {
	jp.loading();
	var validator = bq.headerSubmitCheck("#tmDriverQualificationForm");
	if (!validator.isSuccess){
		jp.bqWaring(validator.msg);
		return;
	}
	jp.post("${ctx}/sys/common/tms/driverQualification/save", $('#tmDriverQualificationForm').serialize(), function (data) {
		if (data.success) {
			jp.success(data.msg);
			$('#tmDriverQualificationTable').bootstrapTable('refresh');
			$("#tmDriverQualificationModal").modal('hide');
		} else {
			jp.bqError(data.msg);
		}
	});
}

function delDriverQualification() {
	jp.confirm('确认要删除该资质记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/sys/common/tms/driverQualification/deleteAll?ids=" + getDriverQualificationIdSelections(), function (data) {
			if (data.success) {
				$('#tmDriverQualificationTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

</script>