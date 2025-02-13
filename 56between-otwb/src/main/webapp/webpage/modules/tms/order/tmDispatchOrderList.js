<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#baseOrgId").val(tmOrg.id);
    $("#dispatchTimeFm").datetimepicker({format: "YYYY-MM-DD 00:00:00"});
    $("#dispatchTimeTo").datetimepicker({format: "YYYY-MM-DD 23:59:59"});
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmDispatchOrderTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$("#baseOrgId").val(tmOrg.id);
	});
});

function initTable() {
    var $table = $('#tmDispatchOrderTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/order/tmDispatchOrder/page",
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
            field: 'dispatchNo',
            title: '派车单号',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'dispatchStatus',
            title: '派车单状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_DISPATCH_STATUS'))}, value, "-");
            }
        }, {
            field: 'dispatchTime',
            title: '派车时间',
            sortable: true
        }, {
            field: 'dispatchType',
            title: '派车单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_DISPATCH_TYPE'))}, value, "-");
            }
        }, {
            field: 'transportType',
            title: '运输方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_METHOD'))}, value, "-");
            }
        }, {
            field: 'dispatchOutletName',
            title: '派车网点',
            sortable: true
        }, {
            field: 'dispatcher',
            title: '派车人',
            sortable: true
        }, {
            field: 'departureTime',
            title: '发车时间',
            sortable: true
        }, {
            field: 'startAreaName',
            title: '始发地',
            sortable: true
        }, {
            field: 'endAreaName',
            title: '目的地',
            sortable: true
        }, {
            field: 'carrierName',
            title: '承运商',
            sortable: true
        }, {
            field: 'carNo',
            title: '车牌号',
            sortable: true
        }, {
            field: 'driverName',
            title: '司机',
            sortable: true
        }, {
            field: 'copilotName',
            title: '副驾驶',
            sortable: true
        }, {
            field: 'driverTel',
            title: '司机电话',
            sortable: true
        }, {
            field: 'totalQty',
            title: '总件数',
            sortable: true
        }, {
            field: 'totalWeight',
            title: '总重量',
            sortable: true
        }, {
            field: 'totalCubic',
            title: '总体积',
            sortable: true
        }, {
            field: 'receivedQty',
            title: '实收件数',
            sortable: true
        }, {
            field: 'prepaidAmount',
            title: '预付金额',
            sortable: true
        }, {
            field: 'shift',
            title: '班次',
            sortable: true
        }, {
            field: 'platform',
            title: '月台',
            sortable: true
        }, {
            field: 'updateBy.name',
            title: '修改人',
            sortable: true
        }, {
            field: 'updateDate',
            title: '修改时间',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注信息',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        var cStyle = length ? "auto" : "none";
        var fColor = length ? "#1E1E1E" : "#8A8A8A";

        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#audit').prop('disabled', !length);
        $('#cancelAudit').prop('disabled', !length);
        $('#depart').prop('disabled', !length);
        $('#generateOrderAuditData').prop('disabled', !length);
        $('#copy').prop('disabled', length !== 1);
        $('#annex').prop('disabled', length !== 1);

        $('#printDispatchOrder').css({"pointer-events": cStyle, "color": fColor});
    });
}

function getIdSelections() {
	return $.map($("#tmDispatchOrderTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该派车单记录吗？', function () {
		jp.loading();
		jp.post("${ctx}/tms/order/tmDispatchOrder/deleteAll", {ids: getIdSelections().join(',')}, function (data) {
            $('#tmDispatchOrderTable').bootstrapTable('refresh');
			if (data.success) {
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openBQDialog('派车单', "${ctx}/tms/order/tmDispatchOrder/form", '90%', '90%', $('#tmDispatchOrderTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openBQDialog('派车单', "${ctx}/tms/order/tmDispatchOrder/form?id=" + id, '90%', '90%', $('#tmDispatchOrderTable'));
}
/*审核*/
function audit() {
	jp.loading();
	jp.post("${ctx}/tms/order/tmDispatchOrder/audit", {ids: getIdSelections().join(',')}, function (data) {
        $('#tmDispatchOrderTable').bootstrapTable('refresh');
		if (data.success) {
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}
/*取消审核*/
function cancelAudit() {
	jp.loading();
	jp.post("${ctx}/tms/order/tmDispatchOrder/cancelAudit", {ids: getIdSelections().join(',')}, function (data) {
        $('#tmDispatchOrderTable').bootstrapTable('refresh');
		if (data.success) {
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}

/*发车*/
function depart() {
	jp.loading();
	jp.post("${ctx}/tms/order/tmDispatchOrder/depart", {ids: getIdSelections().join(',')}, function (data) {
        $('#tmDispatchOrderTable').bootstrapTable('refresh');
		if (data.success) {
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}

function copy() {
    jp.loading();
    jp.get("${ctx}/tms/order/tmDispatchOrder/copy?id=" + getIdSelections(), function (data) {
        $('#tmDispatchOrderTable').bootstrapTable('refresh');
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });

}


/******************************************附件start***********************************************************/
function annex() {
    $("#annexModal").modal({backdrop: 'static', keyboard: false});
    initAnnexTable();
}

function initAnnexTable() {
    $("#annexTable").bootstrapTable('destroy');
    $("#annexTable").bootstrapTable({
        height: 450,
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/annex/data",
        //查询参数,每次调用是会带上这个参数，可自定义
        queryParams: function (params) {
            var searchParam = {};
            searchParam.type = 5;
            searchParam.pkId = getIdSelections()[0];
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'fileName',
            title: '附件名称'
        }, {
            field: 'fileSize',
            title: '附件大小'
        }, {
            field: 'uploadBy.name',
            title: '上传人'
        }, {
            field: 'uploadDate',
            title: '上传时间'
        }, {
            field: 'operate',
            title: '操作',
            align: 'center',
            formatter: function operateFormatter(value, row, index) {
                return [
                    '<a href="${ctx}/sys/annex/download?id=' + row.id + '" class="btn btn-primary">下载附件 </a>',
                ].join('');
            }
        }]
    });
}

function uploadAnnex() {
    $("#uploadFileModal").modal({backdrop: 'static', keyboard: false});
    $("#uploadFile").val('');
}

function removeAnnex() {
    var ids = $.map($("#annexTable").bootstrapTable('getSelections'), function (row) {
        return row.id;
    });
    if (ids.length <= 0) {
        jp.warning("请选择记录");
        return;
    }
    jp.loading();
    jp.post("${ctx}/sys/annex/delete", {ids: ids.join(",")}, function (data) {
        $("#annexTable").bootstrapTable('refresh');
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

function uploadFileFnc() {
    jp.loading("上传中...");
    var formData = new FormData();
    formData.append("type", 5);
    formData.append("pkId", getIdSelections()[0]);
    var files = $("#uploadFile")[0].files;
    for (var i = 0; i < files.length; i++) {
        formData.append("files", files[i]);
    }
    $.ajax({
        url: "${ctx}/sys/annex/upload",
        type: "post",
        cache: false,
        contentType: false,
        processData: false,
        data: formData,
        success: function (data) {
            if (data.success) {
                jp.success(data.msg);
                $("#uploadFileModal").modal('hide');
                $("#annexTable").bootstrapTable('refresh');
            } else {
                jp.error(data.msg);
            }
        }, error: function (xhr, textStatus, errorThrown) {
            jp.error("附件大小超出限制");
        }
    });
}
/******************************************附件end***********************************************************/

/******************************************打印start***********************************************************/
function printDispatchOrder() {
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/tms/print/dispatchOrder/printDispatchOrder", 'ids', rowIds.join(','), '打印派车单');
}
/******************************************打印end***********************************************************/
</script>