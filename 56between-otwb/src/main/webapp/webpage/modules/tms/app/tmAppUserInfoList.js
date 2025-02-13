<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#tmAppUserInfoTable').bootstrapTable({
		//请求方法
		method: 'get',
		//类型json
		dataType: "json",
		//显示刷新按钮
		showRefresh: true,
		//是否显示行间隔色
		striped: true,
		//是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		cache: false,
		//是否显示分页（*）
		pagination: true,
		//分页方式：client客户端分页，server服务端分页（*）
		sidePagination: "server",
		//这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
		url: "${ctx}/tms/app/tmAppUserInfo/data",
		//查询参数,每次调用是会带上这个参数，可自定义
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
			field: 'loginName',
			title: '登录名',
			sortable: true
			, formatter: function (value, row, index) {
				return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
			}
		}, {
			field: 'name',
			title: '姓名',
			sortable: true
		}, {
			field: 'status',
			title: '状态',
			sortable: true,
			formatter: function (value) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_APP_USER_STATUS'))}, value, "-");
			}
		}, {
			field: 'isDriver',
			title: '是否运输人员',
			sortable: true,
			formatter: function (value) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
			}
		}, {
			field: 'driverName',
			title: '运输人员名称',
			sortable: true
        }, {
			field: 'orgName',
			title: '机构名称',
			sortable: true
		}, {
			field: 'def1',
			title: '微信ID',
			sortable: true
		}, {
			field: 'def2',
			title: '所属公司',
			sortable: true
		}, {
			field: 'def3',
			title: '自定义3',
			sortable: true
		}, {
			field: 'def4',
			title: '自定义4',
			sortable: true
        }, {
            field: 'def5',
            title: '自定义5',
            sortable: true
		}, {
			field: 'remarks',
			title: '备注',
			sortable: true
		}]
	});

	$('#tmAppUserInfoTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var ids = getIdSelections();
		$('#remove').prop('disabled', !ids.length);
		$('#edit').prop('disabled', ids.length != 1);
		$('#audit').prop('disabled', !ids.length);
		$('#unAudit').prop('disabled', !ids.length);
	});

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmAppUserInfoTable').bootstrapTable('refresh');
	});

	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$('#tmAppUserInfoTable').bootstrapTable('refresh');
	});

});

function getIdSelections() {
	return $.map($("#tmAppUserInfoTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/tms/app/tmAppUserInfo/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#tmAppUserInfoTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openDialog('用户信息', "${ctx}/tms/app/tmAppUserInfo/form", '80%', '60%', $('#tmAppUserInfoTable'));
}

function edit(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialog('用户信息', "${ctx}/tms/app/tmAppUserInfo/form?id=" + id, '80%', '60%', $('#tmAppUserInfoTable'));
}

function audit(flag) {
	jp.loading();
	jp.post("${ctx}/tms/app/tmAppUserInfo/audit?ids=" + getIdSelections() + "&flag=" + flag, {}, function (data) {
		if (data.success) {
			$('#tmAppUserInfoTable').bootstrapTable('refresh');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}
</script>