<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	$('#userTable').bootstrapTable({
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/role/assign",
		queryParams: function (params) {
			var searchParam = $("#searchForm").serializeJSON();
			searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
			searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
			searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
			return searchParam;
		},
		columns: [{
			field: 'loginName',
			title: '登录名'
		}, {
			field: 'name',
			title: '姓名',
			formatter: function (value, row, index) {
				return '<a  href="#" onclick="jp.openDialogView(\'查看用户\', \'${ctx}/sys/user/form?id=' + row.id + '\',\'800px\', \'680px\')">' + value + '</a>'
			}
		}, {
			field: 'company.name',
			title: '归属公司'
		}, {
			field: 'office.name',
			title: '归属机构'
		}, {
			field: 'phone',
			title: '电话'
		}, {
			field: 'operate',
			title: '操作',
			align: 'center',
			events: {
				'click .del': function (e, value, row, index) {
					jp.confirm('确认要用户从角色中移除吗？', function () {
						jp.loading();
						$.get('${ctx}/sys/role/outrole?userId=' + row.id + '&roleId=' + $("#roleId").val(), function (data) {
							if (data.success) {
								$('#userTable').bootstrapTable("refresh", {query: {id: $("#roleId").val()}});
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
					<shiro:hasPermission name="sys:role:assign">
						'<a href="#" class="del" title="移除">[移除] </a>'
					</shiro:hasPermission>
				].join('');
			}
		}]
	});

	$("#assignButton").click(function () {
		jp.openUserSelectDialog(true, function (ids) {
			jp.loading();
			$.post("${ctx}/sys/role/assignrole", {id: $("#roleId").val(), ids: ids}, function (data) {
				if (data.success) {
					$('#userTable').bootstrapTable("refresh", {query: {id: $("#roleId").val()}});
					jp.success(data.msg);
				} else {
					jp.error(data.msg);
				}
			})
		})
	});
});

</script>