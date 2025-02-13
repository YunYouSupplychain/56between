<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var $tmOutletRelationTreeTable;
$(document).ready(function () {
	$tmOutletRelationTreeTable = $('#tmOutletRelationTreeTable').treeTable({
		theme: 'vsStyle',
		expandLevel: 2,
		column: 0,
		checkbox: false,
		url: '${ctx}/sys/common/tms/outletRelation/getChildren?parentId=',
		callback: function (item) {
			item.delFlag = jp.getDictLabel(${fns:toJson(fns:getDictList('del_flag'))}, item.delFlag, "-");
			var treeTableTpl = $("#tmOutletRelationTreeTableTpl").html();
			return laytpl(treeTableTpl).render({
				row: item
			});
		},
		beforeClick: function ($tmOutletRelationTreeTable, id) {
			$tmOutletRelationTreeTable.refreshPoint(id);//异步获取数据 这里模拟替换处理
		},
		beforeExpand: function ($tmOutletRelationTreeTable, id) {
		},
		afterExpand: function ($tmOutletRelationTreeTable, id) {
		},
		beforeClose: function ($tmOutletRelationTreeTable, id) {
		}
	});
	$tmOutletRelationTreeTable.initParents('${parentIds}', "0");//在保存编辑时定位展开当前节点
});

function del(con, id) {
	jp.confirm('确认要删除网点拓扑图吗？', function () {
		jp.loading();
		$.get("${ctx}/sys/common/tms/outletRelation/delete?id=" + id, function (data) {
			if (data.success) {
				$tmOutletRelationTreeTable.del(id);
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function enable(id) {
	jp.loading();
	$.get("${ctx}/sys/common/tms/outletRelation/enable?id=" + id, function (data) {
		if (data.success) {
			refresh();
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}


function refresh() {//刷新
	var index = jp.loading("正在加载，请稍等...");
	$tmOutletRelationTreeTable.refresh();
	jp.close(index);
}

function syncAll() {
	jp.confirm("确认同步全部数据？", function () {
		jp.loading("同步中...");
		jp.get("${ctx}/sys/common/tms/outletRelation/syncAll", function (data) {
			jp.alert(data.msg);
		});
	});
}
</script>