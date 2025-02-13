<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var addTableUrl;
$(document).ready(function () {
	var leftData = [], rightData = [];
	if ($("#id").val().length <= 0) {
		$("#orgId").val(tmOrg.id);
	} else {
		var objList = ${fns:toJson(tmRouteScopeEntity.objList)};
		$.each(objList, function (i, item) {
			if (item.scopeType == "1") {
				leftData.push(item);
			} else if (item.scopeType == "2") {
				rightData.push(item);
			}
		});
	}
	initLeftTable(leftData);
	initRightTable(rightData);
	initAddTable();
});

function doSubmit($table, index) {
	jp.loading();
	var validator = bq.headerSubmitCheck("#inputForm");
	if (!validator.isSuccess) {
		jp.bqWaring(validator.msg);
		return;
	}
	var disabledObjs = bq.openDisabled("#inputForm");
	var params = $('#inputForm').serializeJSON();
	bq.closeDisabled(disabledObjs);

	var leftData = $('#left-table').bootstrapTable('getData');
	var rightData = $('#right-table').bootstrapTable('getData');
	params.objList = leftData.concat(rightData);

	$.ajax({
		url: "${ctx}/tms/basic/tmRouteScope/save",
		method: "post",
		contentType: "application/json;charset=UTF-8",
		data: JSON.stringify(params),
		success: function (data, textStatus, jqXHR) {
			if (data.success) {
				jp.success(data.msg);
				$table.bootstrapTable('refresh');
				jp.close(index);
			} else {
				jp.bqError(data.msg);
			}
		},
		error: function (xhr, textStatus) {
			if (xhr.status == 0) {
				jp.info("连接失败，请检查网络!")
			} else {
				jp.bqError("后台抛出异常了");
			}
		}
	});
}

function initLeftTable(data) {
	$('#left-table').bootstrapTable({
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sortName: "transportObjCode",
		sortOrder: 'desc',
		columns: [{
			checkbox: true
		}, {
			field: 'transportObjCode',
			title: '业务对象编码',
			sortable: true
		}, {
			field: 'transportObjName',
			title: '业务对象名称',
			sortable: true
		}, {
			field: 'transportObjAddress',
			title: '业务对象地址'
		}]
	});
	$('#left-table').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $('#left-table').bootstrapTable('getSelections').length;
		$('#left-toolbar .remove').prop('disabled', !length);
	});
	$('#left-table').bootstrapTable('load', data);
}

function initRightTable(data) {
	$('#right-table').bootstrapTable({
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sortName: "transportObjCode",
		sortOrder: 'desc',
		columns: [{
			checkbox: true
		}, {
			field: 'transportObjCode',
			title: '业务对象编码',
			sortable: true
		}, {
			field: 'transportObjName',
			title: '业务对象名称',
			sortable: true
		}, {
			field: 'transportObjAddress',
			title: '业务对象地址'
		}]
	});
	$('#right-table').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $('#right-table').bootstrapTable('getSelections').length;
		$('#right-toolbar .remove').prop('disabled', !length);
	});
	$('#right-table').bootstrapTable('load', data);
}

function add(type) {
	$('#addModal').modal({backdrop: 'static', keyboard: false});
	$('#searchForm input').val('');
	$('#type').val(type);

	var url = "${ctx}/tms/basic/tmTransportObj/grid?transportObjType=OUTLET";
	$('#table').bootstrapTable('refresh', {url: url});
	$("#search").unbind("click").click("click", function () {// 绑定查询按扭
		$('#table').bootstrapTable('refresh', {url: url});
	});
	$("#reset").unbind("click").click("click", function () {// 绑定查询按扭
		$("#searchForm input:not(.no-reset)").val("");
		$("#searchForm input[name='codeAndName']").val("");
		$("#searchForm select").val("");
		$('#table').bootstrapTable('refresh', {url: url});
	});
}

function addConfirm(flag) {
	var rows = [], data = [], oldData = [];
	var type = $('#type').val(), orgId = $('#orgId').val();
	if (flag == "ALL") {
		rows = $('#table').bootstrapTable('getData');
	} else if (flag == "SELECT") {
		rows = $('#table').bootstrapTable('getSelections');
		if (rows.length <= 0) {
			jp.warning("请选择记录");
			return;
		}
	}
	if (type == "1") {
		oldData = $('#left-table').bootstrapTable('getData');
	} else if (type == "2") {
		oldData = $('#right-table').bootstrapTable('getData');
	}
	$.each(rows, function (index, row) {
		for (var i = 0; i < oldData.length; i++) {
			var v = oldData[i];
			if (v != undefined && row.transportObjCode == v.transportObjCode) {
				data.push(v);
				delete oldData[i];
				return true;
			}
		}
		data.push({
			transportObjCode: row.transportObjCode,
			transportObjName: row.transportObjName,
			transportObjAddress: row.address,
			scopeType: type, orgId: orgId
		});
	});
	data = data.concat(oldData.filter(function (item) {
		return item != undefined
	}));
	if (type == "1") {
		$('#left-table').bootstrapTable('load', data);
	} else if (type == "2") {
		$('#right-table').bootstrapTable('load', data);
	}
	$('#addModal').modal('hide');
}

function remove(type) {
	var allRows = [], rows = [];
	if (type == "1") {
		allRows = $('#left-table').bootstrapTable('getData');
		rows = $('#left-table').bootstrapTable('getSelections');
		$('#left-table').bootstrapTable('load', getDiff(allRows, rows));
	} else if (type == "2") {
		allRows = $('#right-table').bootstrapTable('getData');
		rows = $('#right-table').bootstrapTable('getSelections');
		$('#right-table').bootstrapTable('load', getDiff(allRows, rows));
	}
}

function getDiff(allRows, rows) {
	if (allRows == undefined || allRows.length <= 0) {
		return [];
	}
	if (rows == undefined || rows.length <= 0) {
		return allRows;
	}
	return allRows.filter(function (row) {
		for (var i = 0; i < rows.length; i++) {
			if (row.transportObjCode == rows[i].transportObjCode) {
				return false;
			}
		}
		return true;
	});
}

function initAddTable() {
	$('#table').bootstrapTable({
		cache: false,//是否使用缓存
		pagination: true,//是否显示分页
		sidePagination: "server",//client客户端分页，server服务端分页
		pageList: [10, 25, 50, 'ALL'],//可供选择的每页的行数
		queryParams: function (params) {
			var searchParam = $("#searchForm").serializeJSON();
			searchParam.orgId = $('#orgId').val();
			searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
			searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
			searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
			return searchParam;
		},
		columns: [{
			checkbox: true
		}, {
			field: 'transportObjCode',
			title: '编码',
			sortable: true
		}, {
			field: 'transportObjName',
			title: '名称',
			sortable: true
		}, {
			field: 'address',
			title: '地址',
			sortable: true
		}]
	});
}
</script>