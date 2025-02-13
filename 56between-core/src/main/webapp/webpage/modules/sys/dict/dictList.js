<%@ page contentType="text/html;charset=UTF-8" %>
<script type="text/javascript">
$(document).ready(function () {
	$('#table').bootstrapTable({
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/dict/data",
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
			field: 'type',
			title: '类型',
			sortable: true
		}, {
			field: 'description',
			title: '描述',
			sortable: true
		}, {
			field: 'operate',
			title: '操作',
			align: 'center',
			events: {
				'click .view': function (e, value, row, index) {
					jp.openDialogView('查看字典', '${ctx}/sys/dict/form?id=' + row.id, '650px', '230px');
				},
				'click .edit': function (e, value, row, index) {
					jp.openDialog('编辑字典', '${ctx}/sys/dict/form?id=' + row.id, '650px', '230px');
				},
				'click .del': function (e, value, row, index) {
					del(row.id);
				},
				'click .setDictValue': function (e, value, row, index) {
					var dictTypeId = $("#dictTypeId").val();
					if (!dictTypeId || row.id !== dictTypeId) {
						$("#left").attr("class", "col-sm-6");
						setTimeout(function () {
							$("#right").fadeIn(500);
						}, 500);
						$("#dictTypeLabel").html(row.type);
						$("#dictTypeId").val(row.id);
						$('#dictValueTable').bootstrapTable("refresh", {query: {dictTypeId: row.id}});
					} else {
						$("#right").fadeOut("fast", function () {
							$("#dictTypeId").val("");
							$("#dictTypeLabel").html("");
							$("#left").attr("class", "col-sm-12");
						});
					}
				}
			},
			formatter: function operateFormatter(value, row, index) {
				return [
					<shiro:hasPermission name="sys:dict:view">
						'<a href="#" class="view" title="查看"><i class="fa fa-eye"></i> </a>',
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:dict:edit">
						'<a href="#" class="edit" title="修改"><i class="fa fa-edit"></i> </a>',
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:dict:del">
						'<a href="#" class="del" title="删除"><i class="fa fa-trash"></i> </a>',
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:dict:edit">
						'<a href="#" class="setDictValue" title="管理键值"><i class="fa fa-cog"></i> </a>'
					</shiro:hasPermission>
				].join('');
			}
		}]
	});

	$('#table').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $('#table').bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
	});

	$("#search").click("click", function () {// 绑定查询按扭
		$('#table').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$('#table').bootstrapTable('refresh');
	});
});

function getIdSelections() {
	return $.map($("#table").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function add() {
	jp.openDialog('新建字典', '${ctx}/sys/dict/form', '650px', '230px')
}

function edit(id) {
	if (!id) {
		id = getIdSelections();
	}
	jp.openDialog('编辑字典', "${ctx}/sys/dict/form?id=" + id, '650px', '230px')
}

function del(ids) {
	if (!ids) {
		ids = getIdSelections();
	}
	jp.loading();
	jp.confirm('确认要删除选中字典吗？', function () {
		$.get("${ctx}/sys/dict/deleteAll?ids=" + ids, function (data) {
			if (data.success) {
				$('#table').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		})
	})
}

$(document).ready(function () {
	var $dictValueTable = $('#dictValueTable').bootstrapTable({
		cache: false,//是否使用缓存
		showRefresh: true,
		sidePagination: "server",//分页方式：client客户端分页，server服务端分页
		url: "${ctx}/sys/dict/getDictValue",
		queryParams: function (params) {
			return {dictTypeId: $("#dictTypeId").val()};
		},
		columns: [{
			field: 'label',
			title: '标签'
		}, {
			field: 'value',
			title: '键值'
		}, {
			field: 'sort',
			title: '排序'
		}, {
			field: 'operate',
			title: '操作',
			align: 'center',
			events: {
				'click .edit': function (e, value, row, index) {
					jp.openDialog('编辑键值', '${ctx}/sys/dict/dictValueForm?dictTypeId=' + $("#dictTypeId").val() + "&dictValueId=" + row.id, '800px', '300px', $dictValueTable);
				},
				'click .del': function (e, value, row, index) {
					jp.confirm('确认要删除键值吗？', function () {
						jp.loading();
						$.get('${ctx}/sys/dict/deleteDictValue?dictValueId=' + row.id + '&dictTypeId=' + $("#dictTypeId").val(), function (data) {
							if (data.success) {
								$('#dictValueTable').bootstrapTable("refresh");
								jp.success(data.msg);
							} else {
								jp.error(data.msg);
							}
						})
					});
				}
			},
			formatter: function operateFormatter(value, row, index) {
				return [
					<shiro:hasPermission name="sys:dict:edit">
						'<a href="#" class="edit" title="编辑">[编辑] </a>',
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:dict:edit">
						'<a href="#" class="del" title="删除">[删除] </a>'
					</shiro:hasPermission>
				].join('');
			}
		}]
	});
	$("#dictValueButton").click(function () {
		jp.openDialog('添加键值', '${ctx}/sys/dict/dictValueForm?dictTypeId=' + $("#dictTypeId").val(), '800px', '300px', $dictValueTable);
	});
});
</script>