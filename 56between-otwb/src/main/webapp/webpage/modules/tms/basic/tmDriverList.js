<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#orgId").val(tmOrg.id);
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmDriverTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$("#orgId").val(tmOrg.id);
		$("#transportObjType").val("CARRIER");
		$('#tmDriverTable').bootstrapTable('refresh');
	});
});

function initTable() {
	$('#tmDriverTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/tms/basic/tmDriver/data",
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
			title: '姓名',
			sortable: true
		}, {
			field: 'carrierCode',
			title: '承运商编码',
			sortable: true
		}, {
			field: 'carrierName',
			title: '承运商名称',
			sortable: true
		}, {
			field: 'phone',
			title: '手机',
			sortable: true
		}, {
			field: 'idCard',
			title: '身份证',
			sortable: true
		}, {
			field: 'allowDriverCarTypeName',
			title: '准驾车型',
			sortable: true
		}, {
			field: 'birthDate',
			title: '出生日期',
			sortable: true
		}, {
			field: 'tempResidenceCertificateNo',
			title: '暂住证号',
			sortable: true
		}, {
			field: 'nation',
			title: '民族',
			sortable: true
		}, {
			field: 'nativePlace',
			title: '籍贯',
			sortable: true
		}, {
			field: 'educationLevel',
			title: '文化程度',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_EDUCATION_LEVEL'))}, value, "-");
			}
		}, {
			field: 'maritalStatus',
			title: '婚姻状况',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_MARITAL_STATUS'))}, value, "-");
			}
		}, {
			field: 'politicalAffiliation',
			title: '政治面貌',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_POLITICAL_AFFILIATION'))}, value, "-");
			}
		}, {
			field: 'personnelNature',
			title: '人员性质',
			sortable: true
		}, {
			field: 'drivingAge',
			title: '驾龄',
			sortable: true
		}, {
			field: 'isMilitaryService',
			title: '是否服兵役',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
			}
		}, {
			field: 'isInternalDriver',
			title: '是否内部驾驶员',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
			}
		}, {
			field: 'emergencyContact',
			title: '紧急联系人',
			sortable: true
		}, {
			field: 'emergencyContactRelation',
			title: '与紧急联系人关系',
			sortable: true
		}, {
			field: 'emergencyContactTel',
			title: '紧急联系电话',
			sortable: true
		}, {
			field: 'currentAddress',
			title: '现居地址',
			sortable: true
		}, {
			field: 'registeredAddress',
			title: '户口地址',
			sortable: true
		}, {
			field: 'contractNo',
			title: '劳务合同号',
			sortable: true
		}, {
			field: 'reportDate',
			title: '到岗日期',
			sortable: true
		}, {
			field: 'contractExpireDate',
			title: '合同有效期',
			sortable: true
		}, {
			field: 'basicWage',
			title: '基本工资',
			sortable: true
		}, {
			field: 'employmentQualificationCertificateNo',
			title: '从业资格证号',
			sortable: true
		}, {
			field: 'comprehensiveInsuranceNo',
			title: '综合保险号',
			sortable: true
		}, {
			field: 'socialInsuranceNo',
			title: '社会保险号',
			sortable: true
		}, {
			field: 'wordCardNo',
			title: '工作卡号',
			sortable: true
		}, {
			field: 'driverLicenseNo',
			title: '驾驶证号',
			sortable: true
		}, {
			field: 'driverLicenseType',
			title: '驾驶证类型',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_DRIVER_LICENSE_TYPE'))}, value, "-");
			}
		}, {
			field: 'firstReceiveLicenseDate',
			title: '初领证日期',
			sortable: true
		}, {
			field: 'driverLicenseAnnualInspectionDate',
			title: '驾驶证年检日期',
			sortable: true
		}, {
			field: 'deductPoint',
			title: '交通违规扣分',
			sortable: true
		}, {
			field: 'banDrivingDateFm',
			title: '停运日期从',
			sortable: true
		}, {
			field: 'banDriverDateTo',
			title: '停运日期到',
			sortable: true
		}, {
			field: 'banDrivingReason',
			title: '停运原因',
			sortable: true
		}, {
			field: 'height',
			title: '身高',
			sortable: true
		}, {
			field: 'weight',
			title: '体重',
			sortable: true
		}, {
			field: 'bloodType',
			title: '血型',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_BLOOD_TYPE'))}, value, "-");
			}
		}, {
			field: 'vision',
			title: '视力',
			sortable: true
		}, {
			field: 'shoeSize',
			title: '鞋码',
			sortable: true
		}, {
			field: 'health',
			title: '健康状况',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_HEALTH'))}, value, "-");
			}
		}, {
			field: 'healthCertificateNo',
			title: '健康证号',
			sortable: true
		}, {
			field: 'healthCertificateExpireDate',
			title: '健康证失效日期',
			sortable: true
		}, {
			field: 'mentalityQuality',
			title: '心理素质',
			sortable: true
		}, {
			field: 'interpersonalRelationship',
			title: '人际关系',
			sortable: true
		}, {
			field: 'teamSpirit',
			title: '团队精神',
			sortable: true
		}]
	});
	$('#tmDriverTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var ids = getIdSelections();
		$('#remove').prop('disabled', !ids.length);
		$('#edit').prop('disabled', ids.length !== 1);
		$('#enable').prop('disabled', !ids.length);
		$('#unable').prop('disabled', !ids.length);
	});
}

function getIdSelections() {
	return $.map($("#tmDriverTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该运输人员信息记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/tms/basic/tmDriver/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#tmDriverTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openDialog('运输人员信息', "${ctx}/tms/basic/tmDriver/form", '90%', '90%', $('#tmDriverTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialog('运输人员信息', "${ctx}/tms/basic/tmDriver/form?id=" + id, '90%', '90%', $('#tmDriverTable'));
}

function enable(flag) {
	jp.loading();
	jp.post("${ctx}/tms/basic/tmDriver/enable?ids=" + getIdSelections() + "&flag=" + flag, {}, function (data) {
		if (data.success) {
			$('#tmDriverTable').bootstrapTable('refresh');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}
</script>