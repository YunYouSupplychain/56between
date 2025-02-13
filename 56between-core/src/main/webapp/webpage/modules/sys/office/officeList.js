<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var $treeTable = null;
$(document).ready(function () {
	$treeTable = $('#treeTable').treeTable({
		theme: 'vsStyle',
		expandLevel: 2,
		column: 0,
		checkbox: false,
		url: '${ctx}/sys/office/getChildren?parentId=',
		callback: function (item) {
			var officeItemTpl = $("#officeItemTpl").html();
			item.typeLabel = jp.getDictLabel(${fns:toJson(fns:getDictList('sys_office_type'))}, item.type);
			return laytpl(officeItemTpl).render(item);
		},
		beforeClick: function ($treeTable, id) {
			//异步获取数据 这里模拟替换处理
			$treeTable.refreshPoint(id);
		},
		beforeExpand: function ($treeTable, id) {
		},
		afterExpand: function ($treeTable, id) {
			//layer.closeAll();
		},
		beforeClose: function ($treeTable, id) {
		}
	});
});

function del(con, id) {
	jp.confirm('确认要删除机构吗？', function () {
		jp.loading();
		$.get("${ctx}/sys/office/delete?id=" + id, function (data) {
			if (data.success) {
				$treeTable.del(id);
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		})
	});
}

function refresh() {//刷新
	var index = jp.loading("正在加载，请稍等...");
	$treeTable.refresh();
	jp.close(index);
}

</script>