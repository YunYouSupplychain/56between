<%@ page contentType="text/html;charset=UTF-8" %>
<script type="text/javascript">
var $treeTable = null;
$(document).ready(function () {
	$treeTable = $('#treeTable').treeTable({
		theme: 'vsStyle',
		expandLevel: 1,
		column: 0,
		checkbox: false,
		url: '${ctx}/sys/menu/getChildren?parentId=',
		callback: function (item) {
			item.isShow = (item.isShow == '1' ? true : false);
			if (item.href && item.href.length > 4) {
				item.hideFullName = item.href.substring(0, 4) + "...";
			} else if (item.href == undefined) {
				item.hideFullName = "";
			} else {
				item.hideFullName = item.href;
			}
			if (item.permission && item.permission.length > 4) {
				item.hidePermission = item.permission.substring(0, 4) + "...";
			} else if (item.permission == undefined) {
				item.hidePermission = "";
			} else {
				item.hidePermission = item.permission;
			}
			var menuItemTpl = $("#menuItemTpl").html();
			return laytpl(menuItemTpl).render(item);
		},
		beforeClick: function ($treeTable, id) {
			//异步获取数据 这里模拟替换处理
			$treeTable.refreshPoint(id);
		},
		beforeExpand: function ($treeTable, id) {
		},
		beforeClose: function ($treeTable, id) {
		}
	});
});

function sort(id, isUpSort) {
	var $ctr = $("tr[id='" + id + "']", $treeTable);
	var $trs = null;
	if ($ctr.attr("pId") != null)
		$trs = $("tr[pId='" + $ctr.attr("pId") + "']", $treeTable);
	else
		$trs = $("tr[depth='1']", $treeTable);
	if ($trs.size() > 1) {
		//递归深度
		if (isUpSort) {
			if ($trs[0].id == $ctr[0].id) {
				jp.error('已经置顶了!');
				return;
			}
			var i = 0;
			$trs.each(function (k) {
				if (this.id == $ctr[0].id) i = k;
			});
			var $ptr = $($trs[i - 1]);
			//封装
			var indexB = 0, indexE = 0, depth = parseInt($ctr.attr("depth"), 10);
			var $alltrs = $("tr", $treeTable);
			for (var k = 0; k < $alltrs.size(); k++) {
				if ($alltrs[k].id == $ctr[0].id) {
					indexB = k;
				} else if (indexB > 0 && parseInt($($alltrs[k]).attr("depth"), 10) > depth) {
					indexE = k;
				} else if (indexB > 0 && parseInt($($alltrs[k]).attr("depth"), 10) <= depth) {
					break;
				}
			}

			var selector = "tr:eq(" + indexB + ")";
			indexB++;
			while (indexB <= indexE) {
				selector += ",tr:eq(" + indexB + ")";
				indexB++;
			}
			var moveNode = $(selector, $treeTable);
			var targetNode = $ptr;

			var id1 = moveNode.attr("id");
			var sort1 = parseInt(moveNode.attr("sort"));//交换排序值
			var id2 = targetNode.attr("id");
			var sort2 = parseInt(targetNode.attr("sort"));
			if (sort1 > sort2) {
				var c = sort1;
				sort1 = sort2;
				sort2 = c;
			} else if (sort1 == sort2) {
				if (sort1 > 0) {
					sort1 = sort1 - 1;//向上移动，相等减1
				} else {
					sort2 = sort2 + 1;
				}
			}
			//交换
			$.get("${ctx}/sys/menu/sort?id1=" + id1 + "&sort1=" + sort1 + "&id2=" + id2 + "&sort2=" + sort2, function (data) {
				if (data.success) {
					moveNode.attr("sort", sort1);
					targetNode.attr("sort", sort2);
					moveNode.insertBefore(targetNode);
					jp.success(data.msg);
				} else {
					jp.error(data.msg);
				}
			})
		} else {
			if ($trs[$trs.size() - 1].id == $ctr[0].id) {
				jp.error('已经末端了!');
				return;
			}
			var i = 0;
			$trs.each(function (k) {
				if (this.id == $ctr[0].id) i = k;
			});
			var $ntr = $($trs[i + 1]);
			//封装
			var indexB = 0, indexE = 0, depth = parseInt($ntr.attr("depth"), 10);
			var $alltrs = $("tr", $treeTable);
			for (var k = 0; k < $alltrs.size(); k++) {
				if ($alltrs[k].id == $ntr[0].id) {
					indexB = k;
				} else if (indexB > 0 && parseInt($($alltrs[k]).attr("depth"), 10) > depth) {
					indexE = k;
				} else if (indexB > 0 && parseInt($($alltrs[k]).attr("depth"), 10) <= depth) {
					break;
				}
			}
			var selector = "tr:eq(" + indexB + ")";
			indexB++;
			while (indexB <= indexE) {
				selector += ",tr:eq(" + indexB + ")";
				indexB++;
			}
			var moveNode = $(selector, $treeTable);
			var targetNode = $ctr;
			var id1 = moveNode.attr("id");
			var sort1 = parseInt(targetNode.attr("sort"));//交换排序值
			var id2 = targetNode.attr("id");
			var sort2 = parseInt(moveNode.attr("sort"));
			if (sort1 > sort2) {
				var c = sort1;
				sort1 = sort2;
				sort2 = c;
			} else if (sort1 == sort2) {
				sort2 = sort2 + 1;//向下移动，相等加1
			}
			//交换
			$.get("${ctx}/sys/menu/sort?id1=" + id1 + "&sort1=" + sort1 + "&id2=" + id2 + "&sort2=" + sort2, function (data) {
				if (data.success) {
					moveNode.attr("sort", sort1);
					targetNode.attr("sort", sort2);
					moveNode.insertBefore(targetNode);
					jp.success(data.msg)
				} else {
					jp.error(data.msg);
				}
			})
		}
	}
}

function del(con, id) {
	jp.confirm('确认要删除菜单吗？', function () {
		jp.loading();
		$.get("${ctx}/sys/menu/delete?id=" + id, function (data) {
			if (data.success) {
				$treeTable.del(id);
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		})
	});
}

function refresh() {
	var index = jp.loading();
	$treeTable.refresh();
	jp.close(index);
}

function setDataRule(id, name) {
	$("#left").attr("class", "col-sm-6");
	setTimeout(function () {
		$("#right").fadeIn(500);
	}, 500)
	$("#menuLabel").html(name);
	$("#menuId").val(id);
	$('#dataRuleTable').bootstrapTable("refresh", {query: {menuId: id}})
}
</script>