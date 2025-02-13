<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#orgId").val(jp.getCurrentOrg().orgId);

	$('#tmExpressInfoImportTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/tms/order/tmExpressInfoImport/data",
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
			field: 'importNo',
			title: '导入批次号',
			sortable: true
			, formatter: function (value, row, index) {
				return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
			}
		}, {
            field: 'fileName',
            title: '文件名',
            sortable: true
        }, {
            field: 'createDate',
            title: '导入时间',
            sortable: true
        }, {
			field: 'importReason',
			title: '导入原因',
			sortable: true
		}]
	});

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmExpressInfoImportTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$("#orgId").val(jp.getCurrentOrg().orgId);
		$('#tmExpressInfoImportTable').bootstrapTable('refresh');
	});
});

function getIdSelections() {
	return $.map($("#tmExpressInfoImportTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function view(id) {//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialogView('快递单号导入更新明细', "${ctx}/tms/order/tmExpressInfoImport/form?id=" + id, '90%', '90%', $('#tmExpressInfoImportTable'));
}

function importOrder() {
    $("#uploadModal").modal();
    $("#uploadFileName").val('');
}

function downloadTemplate() {
    window.location = '${ctx}/tms/order/tmExpressInfoImport/import/template';
}

function uploadFile() {
    if (!$("#uploadFileName").val()) {
    jp.alert("请选择需要上传的文件");
        return;
    }
    jp.loading("正在导入中...");
    var file = $("#uploadFileName").get(0).files[0];
    var fm = new FormData();
    fm.append('file', file);
    fm.append('orgId', jp.getCurrentOrg().orgId);
    fm.append('importReason', $("#importReason").val());
    $.ajax({
        type: "post",
        url: "${ctx}/tms/order/tmExpressInfoImport/import",
        data: fm,
        cache: false,
        contentType: false,
        processData: false,
        // 传送请求数据
        success: function (data) {
            $("#uploadModal").modal('hide');
            $('#tmExpressInfoImportTable').bootstrapTable('refresh');
            jp.alert(data.msg);
        },
        error: function (data) {
            $("#uploadModal").modal('hide');
            jp.alert(data.msg);
        }
    });
}

</script>