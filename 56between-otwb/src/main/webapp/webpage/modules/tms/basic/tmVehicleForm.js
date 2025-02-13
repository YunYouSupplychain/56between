<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#buyingTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$('#activeTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$('#scrappedTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$('#registeredTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$('#drivingLicenseExpiryTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$('#operatingLicenseExpiryTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$('#tollCollectionTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#annualReviewExpiryTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#twoDimensionExpiryTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#tankInspectionExpiryTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#insuranceExpiryTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	if ($("#id").val().length > 0) {
		$('#carNo').prop('readonly', true);
		$('#carQualification_add').attr('disabled', false);
		$('#carPart_add').attr('disabled', false);
	} else {
		$("#orgId").val(tmOrg.id);
        $("#status").val("00");
	}

    initQualificationTable();
    initPartTable();
});

function initQualificationTable() {
    var $table = $('#tmVehicleQualificationTable');
    $table.bootstrapTable({
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/basic/tmVehicleQualification/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.carNo = $("#carNo").val();
            searchParam.orgId = $("#orgId").val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onDblClickRow: function (row, $el) {
            <shiro:hasPermission name="basic:tmVehicleQualification:edit">
                editCarQualification(row);
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
            title: '资质简称',
            sortable: true
        },{
            field: 'effectiveDate',
            title: '生效日期',
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
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#carQualification_remove').attr('disabled', !length);
        $('#carQualification_edit').attr('disabled', length !== 1);
    });
}

function initPartTable() {
    var $table = $('#tmVehiclePartTable');
    $table.bootstrapTable({
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/basic/tmVehiclePart/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.carNo = $("#carNo").val();
            searchParam.orgId = $("#orgId").val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onDblClickRow: function (row, $el) {
            <shiro:hasPermission name="basic:tmVehiclePart:edit">
                editCarPart(row);
            </shiro:hasPermission>
        },
        columns: [{
            checkbox: true
        },{
            field: 'partNo',
            title: '配件编号*',
            sortable: true
        },{
            field: 'partName',
            title: '配件名称',
            sortable: true
        },{
            field: 'partNumber',
            title: '配件数量',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#carPart_remove').attr('disabled', !length);
        $('#carPart_edit').attr('disabled', length !== 1);
    });
}

function doSubmit($table, index) {
    jp.loading();
    var validator = bq.headerSubmitCheck("#inputForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var objs = bq.openDisabled("#inputForm");
    var data = $('#inputForm').serialize();
    bq.closeDisabled(objs);

    jp.post("${ctx}/tms/basic/tmVehicle/save", data, function (data) {
        if (data.success) {
            jp.success(data.msg);
            $table.bootstrapTable('refresh');
            jp.close(index);
        } else {
            jp.bqError(data.msg);
        }
    });
}

function getCarQualificationIdSelections() {
	return $.map($("#tmVehicleQualificationTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function addCarQualification() {
	carQualificationModal();
}

function editCarQualification(row) {
	if (row === undefined) {
		row = $("#tmVehicleQualificationTable").bootstrapTable('getSelections')[0];
	}
	carQualificationModal(row);
}

function carQualificationModal(row){
	$("#tmVehicleQualificationForm input").val("");
	$("#tmVehicleQualificationForm select").val("");
	$('#carQualification_effectiveDate').datetimepicker({format: "YYYY-MM-DD"});
	$('#carQualification_expireDate').datetimepicker({format: "YYYY-MM-DD"});
	if (row !== undefined) {
		for (var attr in row) {
			if ($("#carQualification_" + attr).length > 0) {
				$("#carQualification_" + attr).val(row[attr]);
			}
		}
	}
	$("#carQualification_carNo").val($("#carNo").val());
	$("#carQualification_orgId").val($("#orgId").val());
	$("#carQualification_qualificationCode").prop('readonly', $("#carQualification_id").val().length > 0);
	$("#tmVehicleQualificationModal").modal();
}

function saveCarQualification() {
	jp.loading();
	var validator = bq.headerSubmitCheck("#tmVehicleQualificationForm");
	if (!validator.isSuccess){
		jp.bqWaring(validator.msg);
		return;
	}
	jp.post("${ctx}/tms/basic/tmVehicleQualification/save", $('#tmVehicleQualificationForm').serialize(), function (data) {
		if (data.success) {
			$('#tmVehicleQualificationTable').bootstrapTable('refresh');
			$("#tmVehicleQualificationModal").modal('hide');
			jp.success(data.msg);
		} else {
			jp.bqError(data.msg);
		}
	});
}

function delCarQualification() {
	jp.confirm('确认要删除该车辆资质记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/tms/basic/tmVehicleQualification/deleteAll?ids=" + getCarQualificationIdSelections(), function (data) {
			if (data.success) {
				$('#tmVehicleQualificationTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function getCarPartIdSelections() {
	return $.map($("#tmVehiclePartTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function addCarPart() {
	carPart();
}

function editCarPart(row) {
	if (row === undefined) {
		row = $("#tmVehiclePartTable").bootstrapTable('getSelections')[0];
	}
	carPart(row);
}

function carPart(row){
	$("#tmVehiclePartForm input").val("");
	$("#tmVehiclePartForm select").val("");
	if (row !== undefined) {
		for (var attr in row) {
			if ($("#carPart_" + attr).length > 0) {
				$("#carPart_" + attr).val(row[attr]);
			}
		}
	}
	$("#carPart_carNo").val($("#carNo").val());
	$("#carPart_orgId").val($("#orgId").val());
	$("#carPart_partNo").prop('readonly', $("#carPart_id").val().length > 0);
	$("#tmVehiclePartModal").modal();
}

function saveCarPart() {
	jp.loading();
	var validator = bq.headerSubmitCheck("#tmVehiclePartForm");
	if (!validator.isSuccess){
		jp.bqWaring(validator.msg);
		return;
	}
	jp.post("${ctx}/tms/basic/tmVehiclePart/save", $('#tmVehiclePartForm').serialize(), function (data) {
		if (data.success) {
			$('#tmVehiclePartTable').bootstrapTable('refresh');
			$("#tmVehiclePartModal").modal('hide');
			jp.success(data.msg);
		} else {
			jp.bqError(data.msg);
		}
	});
}

function delCarPart() {
	jp.confirm('确认要删除该车辆配件记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/tms/basic/tmVehiclePart/deleteAll?ids=" + getCarPartIdSelections(), function (data) {
			if (data.success) {
				$('#tmVehiclePartTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

</script>