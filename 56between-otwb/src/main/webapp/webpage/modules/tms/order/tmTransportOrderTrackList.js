<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#baseOrgId").val(tmOrg.id);
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmTransportOrderTrackTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		/*$("#orgId").val(jp.getCurrentOrg().orgId);*/
		$("#baseOrgId").val(tmOrg.id);
		$('#tmTransportOrderTrackTable').bootstrapTable('refresh');
	});
});

function initTable() {
    var $table = $('#tmTransportOrderTrackTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/order/transport/track/page",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'transportNo',
            title: '运输单号',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'customerNo',
            title: '客户单号',
            sortable: true
        }, {
            field: 'labelNo',
            title: '标签号',
            sortable: true
        }, {
            field: 'deliverOutletName',
            title: '发货网点',
            sortable: true
        }, {
            field: 'receiveOutletName',
            title: '收货网点',
            sortable: true
        }, {
            field: 'opPerson',
            title: '操作人',
            sortable: true
        }, {
            field: 'opNode',
            title: '操作节点',
            sortable: true
        }, {
            field: 'operation',
            title: '操作内容',
            sortable: true
        }, {
            field: 'opTime',
            title: '操作时间',
            sortable: true
        }
        ]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $("#tmTransportOrderTrackTable").bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
	return $.map($("#tmTransportOrderTrackTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该需求计划记录吗？', function () {
		jp.loading();
		jp.post("${ctx}/tms/order/transport/track/delete", {ids: getIdSelections().join(',')}, function (data) {
			if (data.success) {
				$('#tmTransportOrderTrackTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openBQDialog('物流节点', "${ctx}/tms/order/transport/track/form", '80%', '60%', $('#tmTransportOrderTrackTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openBQDialog('物流节点', "${ctx}/tms/order/transport/track/form?id=" + id, '80%', '60%', $('#tmTransportOrderTrackTable'));
}

function openUploadFile() {
    $(function () {
        $("#uploadModal").modal();
        $("#uploadFileName").val('');
    });
}

function downloadTemplate() {
    window.location = '${ctx}/tms/order/transport/track/import/template';
}

function uploadFile() {
    if ($("#uploadFileName").val() == null || $("#uploadFileName").val() == "") {
        jp.alert("请选择需要上传的文件");
        return;
    }
    var file = $("#uploadFileName").get(0).files[0];
    var fm = new FormData();
    fm.append('file', file);
    fm.append('orgId', jp.getCurrentOrg().orgId);
        $.ajax({
            type: "post",
            url: "${ctx}/tms/order/transport/track/import",
            data: fm,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            // 传送请求数据
            success: function (data) {
                $("#uploadModal").modal('hide');  // 手动关闭
                $('#tmTransportOrderTrackTable').bootstrapTable('refresh');
                jp.alert(data.msg);
            },
            error: function (data) {
                $("#uploadModal").modal('hide');  // 手动关闭
                jp.alert(data.msg);
            }
        });
}
</script>