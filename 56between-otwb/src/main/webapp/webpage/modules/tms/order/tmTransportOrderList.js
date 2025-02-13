<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var url = "${ctx}/tms/order/tmTransportOrder/data";
var curTab = "#allTable";
/*全部Tab查询参数*/
var allParams = function (params) {
	var searchParam = $("#searchForm").serializeJSON();
	searchParam.orgId = jp.getCurrentOrg().orgId;
	searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
	searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
	searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
	return searchParam;
};
/*待审核Tab查询参数*/
var toAuditParams = function (params) {
	var searchParam = $("#searchForm").serializeJSON();
	searchParam.orderStatus = '00';
	searchParam.orgId = jp.getCurrentOrg().orgId;
	searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
	searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
	searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
	return searchParam;
};
/*待收货Tab查询参数*/
var toReceiveParams = function (params) {
	var searchParam = $("#searchForm").serializeJSON();
	searchParam.isToReceive = 'Y';
	searchParam.orgId = jp.getCurrentOrg().orgId;
	searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
	searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
	searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
	return searchParam;
};
/*待配载Tab查询参数*/
var toDispatchParams = function (params) {
	var searchParam = $("#searchForm").serializeJSON();
	searchParam.isToDispatch = 'Y';
	searchParam.orgId = jp.getCurrentOrg().orgId;
	searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
	searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
	searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
	return searchParam;
};
/*待签收Tab查询参数*/
var toSignParams = function (params) {
	var searchParam = $("#searchForm").serializeJSON();
	searchParam.isToSign = 'Y';
	searchParam.orgId = jp.getCurrentOrg().orgId;
	searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
	searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
	searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
	return searchParam;
};
/*已签收Tab查询参数*/
var signedParams = function (params) {
	var searchParam = $("#searchForm").serializeJSON();
	searchParam.orderStatus = '40';
	searchParam.orgId = jp.getCurrentOrg().orgId;
	searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
	searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
	searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
	return searchParam;
};

$(document).ready(function () {
	$("#baseOrgId").val(tmOrg.id);
	$("#orderTimeFm").datetimepicker({format: "YYYY-MM-DD 00:00:00"});
	$("#orderTimeTo").datetimepicker({format: "YYYY-MM-DD 23:59:59"});

	// 初始化各页签中Bootstrap表格
	$("#allTable").bootstrapTable(getOption(allParams));
	$("#toAuditTable").bootstrapTable(getOption(toAuditParams));
	$("#toReceiveTable").bootstrapTable(getOption(toReceiveParams));
	$("#toDispatchTable").bootstrapTable(getOption(toDispatchParams));
	$("#toSignTable").bootstrapTable(getOption(toSignParams));
	$("#signedTable").bootstrapTable(getOption(signedParams));
	// 加载刷新默认Bootstrap表格数据
	refreshTable();
	// 默认加载页签中Bootstrap表格绑定checkbox事件
	$(curTab).on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', checkboxTab);
	/*页签切换绑定事件*/
	$("#tmTransportOrderTabs a").click(function (e) {
		//阻止a链接的跳转行为
		e.preventDefault();
		// 按钮可用重置
		btnDefaultDisabled();
		// 获取切换目标页签关联的Bootstrap表格ID
		curTab = $(this).data("target-table");
		// 页签中Bootstrap表格绑定checkbox事件
		$(curTab).on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', checkboxTab);
		//显示当前选中的链接及关联的content
		$(this).tab('show');
		// 刷新加载表格数据
		refreshTable();
	});
	// 查询
	$("#search").click("click", function () {
		refreshTable();
	});
	// 重置
	$("#reset").click("click", function () {
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$("#baseOrgId").val(tmOrg.id);
		refreshTable();
	});
});
/*表格checkbox事件*/
function checkboxTab() {
    var length = $(curTab).bootstrapTable('getSelections').length;
	var cStyle = length ? "auto" : "none";
	var fColor = length ? "#1E1E1E" : "#8A8A8A";

	$('#addLabel').prop('disabled', length !== 1);
    $('#copy').prop('disabled', length !== 1);
	$('#printTransportOrder').css({"pointer-events": cStyle, "color": fColor});

    if (length === 1) {
		$('#checkDispatch').removeClass('disabled');
		$('#annex').removeClass('disabled');
		$('#tracking').removeClass('disabled');
		$('#runTrack').removeClass('disabled');
		$('#vehicleLocation').removeClass('disabled');
	} else {
		$('#checkDispatch').addClass('disabled');
		$('#annex').addClass('disabled');
		$('#tracking').addClass('disabled');
		$('#runTrack').addClass('disabled');
		$('#vehicleLocation').addClass('disabled');
	}

	if (curTab === "#allTable") {
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
		$('#audit').prop('disabled', !length);
		$('#cancelAudit').prop('disabled', !length);
		$('#receive').prop('disabled', !length);
        $('#cancelReceive').prop('disabled', !length);
		$('#sign').prop('disabled', length !== 1);
		$('#receipt').prop('disabled', length !== 1);
		$('#directDispatch').prop('disabled', !length);
		$('#directDispatchNew').prop('disabled', !length);
	} else if (curTab === "#toAuditTable") {
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
		$('#audit').prop('disabled', !length);
	} else if (curTab === "#toReceiveTable") {
		$('#cancelAudit').prop('disabled', !length);
		$('#receive').prop('disabled', !length);
        $('#directDispatch').prop('disabled', !length);
        $('#directDispatchNew').prop('disabled', !length);
	} else if (curTab === "#toDispatchTable") {
		$('#cancelAudit').prop('disabled', !length);
		$('#receive').prop('disabled', !length);
        $('#directDispatch').prop('disabled', !length);
        $('#directDispatchNew').prop('disabled', !length);
	} else if (curTab === "#toSignTable") {
        $('#cancelReceive').prop('disabled', !length);
		$('#sign').prop('disabled', length !== 1);
        $('#directDispatch').prop('disabled', !length);
        $('#directDispatchNew').prop('disabled', !length);
	} else if (curTab === "#signedTable") {
		$('#receipt').prop('disabled', length !== 1);
	}
}
/*按钮默认不可用*/
function btnDefaultDisabled() {
	$('#edit').prop('disabled', true);
	$('#remove').prop('disabled', true);
	$('#audit').prop('disabled', true);
	$('#cancelAudit').prop('disabled', true);
	$('#addLabel').prop('disabled', true);
	$('#receive').prop('disabled', true);
	$('#cancelReceive').prop('disabled', true);
	$('#sign').prop('disabled', true);
	$('#receipt').prop('disabled', true);
	$('#directDispatch').prop('disabled', true);
	$('#directDispatchNew').prop('disabled', true);
	$('#copy').prop('disabled', true);

	$('#checkDispatch').addClass('disabled');
	$('#vehicleLocation').addClass('disabled');
	$('#tracking').addClass('disabled');
	$('#runTrack').addClass('disabled');
	$('#annex').addClass('disabled');
}
/*刷新表格数据*/
function refreshTable() {
	$(curTab).bootstrapTable('refresh', {url: url});
}
/*设置页签上的数量*/
function setTabOrderQty() {
	var searchParam = $("#searchForm").serializeJSON();
	searchParam.orgId = jp.getCurrentOrg().orgId;
	jp.post("${ctx}/tms/order/tmTransportOrder/getEachStatusOrderQty", searchParam, function (data) {
		$("#toAuditOrderQty").text(data.toAuditOrderQty);
		$("#toReceiveOrderQty").text(data.toReceiveOrderQty);
		$("#toDispatchOrderQty").text(data.toDispatchOrderQty);
		$("#toSignOrderQty").text(data.toSignOrderQty);
		$("#signedOrderQty").text(data.signedOrderQty);
	});
}
/*Bootstrap初始化参数*/
function getOption(params) {
	return {
		cache: false,//是否使用缓存，默认为true
		pagination: true,//是否显示分页
		sidePagination: "server",//分页方式：client客户端分页，server服务端分页
		queryParams: params,
		onLoadSuccess: function (data) {
			setTabOrderQty();
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
			field: 'orderTime',
			title: '订单时间',
			sortable: true
        }, {
			field: 'orderStatus',
			title: '订单状态',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_STATUS'))}, value, "-");
			}
		}, {
			field: 'orderType',
			title: '订单类型',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_ORDER_TYPE'))}, value, "-");
			}
		}, {
			field: 'customerCode',
			title: '客户编码',
			sortable: true
        }, {
            field: 'customerName',
            title: '客户名称',
            sortable: true
        }, {
			field: 'customerNo',
			title: '客户单号',
			sortable: true
		}, {
            field: 'deliveryMethod',
            title: '交货方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_DELIVERY_METHOD'))}, value, "-");
            }
        }, {
            field: 'hasDispatch',
            title: '是否已派车',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            },
            cellStyle: function(value,row,index) {
                var style = {};
                if (row.hasDispatch == 'N') {
                    style = {css: {"color": "#FF0033"}}
                }
                return style
            }
        }, {
            field: 'orderDelivery.totalEaQty',
            title: '总数量',
            sortable: true
        }, {
            field: 'orderDelivery.totalWeight',
            title: '总重量',
            sortable: true
        }, {
            field: 'orderDelivery.totalCubic',
            title: '总体积',
            sortable: true
        }, {
            field: 'orderDelivery.receiptCount',
            title: '回单份数',
            sortable: true
        }, {
            field: 'consigneeAddress',
            title: '收货详细地址',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注信息',
            sortable: true
		}, {
			field: 'principalCode',
			title: '委托方编码',
			sortable: true
        }, {
			field: 'principalName',
			title: '委托方名称',
			sortable: true
		}, {
			field: 'consigneeCode',
			title: '收货方编码',
			sortable: true
		}, {
			field: 'consigneeName',
			title: '收货方名称',
			sortable: true
		}, {
            field: 'consignee',
            title: '收货人',
            sortable: true
        }, {
            field: 'consigneeTel',
            title: '收货人联系电话',
            sortable: true
        }, {
            field: 'consigneeCity',
            title: '目的地城市',
            sortable: true
		}, {
			field: 'receiveOutletCode',
			title: '提货网点编码',
			sortable: true
		}, {
			field: 'receiveOutletName',
			title: '提货网点',
			sortable: true
		}, {
			field: 'outletCode',
			title: '配送网点编码',
			sortable: true
		}, {
			field: 'outletName',
			title: '配送网点名称',
			sortable: true
		}, {
			field: 'transportMethod',
			title: '运输方式',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_METHOD'))}, value, "-");
			}
		}, {
            field: 'trackingNo',
            title: '物流单号',
            sortable: true
        }, {
            field: 'dispatchPlanNo',
            title: '调度计划单号',
            sortable: true
        }, {
            field: 'taskNo',
            title: '任务单号',
            sortable: true
		}, {
			field: 'shipCode',
			title: '发货方编码',
			sortable: true
        }, {
			field: 'shipName',
			title: '发货方名称',
			sortable: true
		}, {
			field: 'shipper',
			title: '发货人',
			sortable: true
		}, {
			field: 'shipperTel',
			title: '发货人联系电话',
			sortable: true
		}, {
			field: 'shipCity',
			title: '发货城市',
			sortable: true
		}, {
			field: 'shipAddress',
			title: '发货地址',
			sortable: true
        }, {
            field: 'brand',
            title: '品牌',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_OBJ_BRAND'))}, value, "-");
            }
        }, {
			field: 'designatedSignBy',
			title: '指定签收人',
			sortable: true
		}, {
            field: 'dataSource',
            title: '数据来源',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_DATA_SOURCE'))}, value, "-");
            }
		}]
	};
}
/*获取表格选中行id*/
function getIdSelections() {
	return $.map($(curTab).bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}
/*删除*/
function deleteAll() {
	jp.confirm('确认要删除该运输订单信息记录吗？', function () {
		jp.loading();
		jp.post("${ctx}/tms/order/tmTransportOrder/deleteAll", {ids: getIdSelections().join(',')}, function (data) {
			refreshTable();
			if (data.success) {
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}
/*新增*/
function add() {
	jp.openBQDialog('运输订单信息', "${ctx}/tms/order/tmTransportOrder/form", '90%', '90%', $(curTab));
}
/*编辑*/
function edit(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openBQDialog('运输订单信息', "${ctx}/tms/order/tmTransportOrder/form?id=" + id, '90%', '90%', $(curTab));
}
/*打开添加标签模态框*/
function openAddLabelModal() {
	var row = $(curTab).bootstrapTable('getSelections')[0];

	$('#addLabelForm input').val('');
	$('#addLabel_transportNo').val(row.transportNo);
	$('#addLabel_orgId').val(row.orgId);
	$("#addLabelModal").modal({backdrop: 'static', keyboard: false});
}
/*添加标签*/
function addLabel() {
	jp.loading();
	var validator = bq.headerSubmitCheck("#addLabelForm");
	if (!validator.isSuccess) {
		jp.warning(validator.msg);
		return;
	}
	var labelQty = $("#labelQty").val();
	var lineNo = $("#lineNo").val();
	var skuCode = $("#skuCode").val();
    var totalQty = Number($("#totalQty").val());
	var totalWeight = Number($("#totalWeight").val());
	var totalCubic = Number($("#totalCubic").val());
	var data = {ids: getIdSelections().join(','), lineNo: lineNo, skuCode: skuCode, labelQty: labelQty, totalQty: totalQty, totalWeight: totalWeight, totalCubic: totalCubic};
	jp.post("${ctx}/tms/order/transport/label/add", data, function (data) {
		if (data.success) {
			$("#addLabelModal").modal('hide');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}
function afterSelectSku(row) {
    $('#lineNo').val(row.lineNo);
}
/*审核*/
function audit() {
	jp.loading();
	jp.post("${ctx}/tms/order/tmTransportOrder/audit", {ids: getIdSelections().join(',')}, function (data) {
		refreshTable();
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
	jp.post("${ctx}/tms/order/tmTransportOrder/cancelAudit", {ids: getIdSelections().join(',')}, function (data) {
		refreshTable();
		if (data.success) {
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}
/*揽收*/
function receive() {
	jp.loading();
	jp.post("${ctx}/tms/order/tmTransportOrder/receive", {ids: getIdSelections().join(',')}, function (data) {
		refreshTable();
		if (data.success) {
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}
/*取消揽收*/
function cancelReceive() {
    jp.loading();
    jp.post("${ctx}/tms/order/tmTransportOrder/cancelReceive", {ids: getIdSelections().join(',')}, function (data) {
        refreshTable();
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}
/*打开签收模态框*/
function openSignModal() {
	$("#signForm input").val('');
	$("#sign_signTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$("#signModal").modal({backdrop: 'static', keyboard: false});
}
/*签收*/
function sign() {
	jp.loading();
	var validator = bq.headerSubmitCheck("#signForm");
	if (!validator.isSuccess) {
		jp.warning(validator.msg);
		return;
	}
	var signBy = $("#sign_signBy").val();
	var signTime = $("#sign_signTime").find("input").val();
	var remarks = $("#sign_remarks").val();
	var data = {signBy: signBy, signTime: signTime, remarks: remarks};
    jp.post("${ctx}/tms/order/tmTransportOrder/sign?ids=" + getIdSelections(), data, function (data) {
        if (data.success) {
			refreshTable();
            $("#signModal").modal('hide');
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}
/*打开回单模态框*/
function openReceiptModal() {
	$("#receiptForm input").val('');
	$("#receipt_receiptTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$("#receiptModal").modal({backdrop: 'static', keyboard: false});
}
/*回单*/
function receipt() {
	jp.loading();
	var validator = bq.headerSubmitCheck("#receiptForm");
	if (!validator.isSuccess) {
		jp.warning(validator.msg);
		return;
	}
	var receiptBy = $("#receipt_receiptBy").val();
	var receiptTime = $("#receipt_receiptTime").find("input").val();
    var remarks = $("#receipt_remarks").val();
	var data = {ids: getIdSelections().join(','), receiptBy: receiptBy, receiptTime: receiptTime, remarks: remarks};
	jp.post("${ctx}/tms/order/tmTransportOrder/receipt?ids=" + getIdSelections(), data, function (data) {
		if (data.success) {
			refreshTable();
			$("#receiptModal").modal('hide');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}

/*查看行车轨迹*/
function runTrack() {
	jp.openDialogView('查看行车轨迹', "${ctx}/tms/order/tmTransportOrder/runTrack?id=" + getIdSelections(), '90%', '90%');
}

/*查看车辆位置*/
function vehicleLocation() {
	jp.openDialogView('查看车辆位置', "${ctx}/tms/order/tmTransportOrder/vehicleLocation?id=" + getIdSelections(), '90%', '90%');
}

function copy() {
    jp.loading();
    jp.get("${ctx}/tms/order/tmTransportOrder/copy?id=" + getIdSelections(), function (data) {
        refreshTable();
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

/*物流节点跟踪*/
function tracking() {
    top.layer.open({
        type: 2,
        area: ['600px', '1000px'],
        title: '物流节点跟踪',
        auto: true,
        maxmin: false, //关闭最大化最小化按钮
        content: "${ctx}/tms/order/tmTransportOrder/trackingForm?id=" + getIdSelections(),
        btn: ['关闭'],
        cancel: function (index) {
        }
    });
}

/*查看派车情况*/
function checkDispatch() {
    jp.openDialogView('查看派车情况', "${ctx}/tms/order/tmTransportOrder/checkDispatch?id=" + getIdSelections(), '90%', '90%');
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
		queryParams: function (params) {
			var searchParam = {};
			searchParam.type = 4;
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
    formData.append("type", 4);
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

function directDispatch() {
    var ids = getIdSelections();
    if (!ids) {
        jp.warning("请选择记录");
        return;
    }
    $("#directDispatchForm input").val("");
    $('#directDispatchForm #dispatchOutletCode').val('DDS');
	$('#directDispatchForm #dispatchTime input').val(jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"));
	$('#directDispatchForm #dispatchType').val('5');
    $("#directDispatchModal").modal({backdrop: 'static', keyboard: false});
}

function directDispatchConfirm() {
    var validator = bq.headerSubmitCheck("#directDispatchForm");
    if (!validator.isSuccess) {
        jp.warning(validator.msg);
        return;
    }
    jp.loading();
    var data = $('#directDispatchForm').serializeJSON();
    data.ids = getIdSelections().join(',');
    data.baseOrgId = tmOrg.id;
    data.orgId = jp.getCurrentOrg().orgId;
    jp.post('${ctx}/tms/order/tmTransportOrder/directDispatch', data, function (data) {
        if (data.success) {
            jp.alert(data.msg);
            $("#directDispatchModal").modal('hide');
            refreshTable();
        } else {
            jp.error(data.msg);
        }
    });
}

function directDispatchNew() {
	var index = jp.loading();
	var ids = getIdSelections();
	if (!ids) {
		jp.warning("请选择记录");
		return;
	}
	jp.post('${ctx}/tms/order/tmDirectDispatch/checkOrders', {ids: ids.join(',')}, function (data) {
		if (data.success) {
			if (data.body.hasDispatch) {
				jp.confirm('选择的运输订单存在已经配载的订单，是否继续派车调度？', function () {
					openDirectDispatchForm(ids);
				});
			} else {
				openDirectDispatchForm(ids);
			}
			jp.close(index);
		} else {
			jp.error(data.msg);
		}
	});

}

function openDirectDispatchForm(ids) {
	top.layer.open({
		type: 2,
		area: ['90%', '90%'],
		title: '新建调度派车',
		auto: false,
		maxmin: false, //关闭最大化最小化按钮
		content: "${ctx}/tms/order/tmDirectDispatch/directDispatchForm?ids=" + ids.join(','),
		btn: ['确认', '关闭'],
		yes: function (index, layero) {
			var iframeWin = layero.find('iframe')[0];
			iframeWin.contentWindow.doSubmit(index);
		},
		cancel: function (index) {
			top.layer.close(index);
		},
		end: function () {
			$(curTab).bootstrapTable('refresh');
		}
	});
}

/*承运商选择后*/
function carrierSelect(data) {
    $("#carNo").val("");
    $("#driver").val("");
    $("#driverName").val("");
    $("#driverTel").val("");
}

/*车辆选择后*/
function carSelect(data) {
    if (data) {
        $("#driver").val(data.mainDriver);
        $("#driverName").val(data.mainDriverName);
        $("#driverTel").val(data.mainDriverTel);
    }
}

/*司机选择后*/
function driverSelect(data) {
    if (data) {
        $("#driverTel").val(data.phone);
    }
}

/******************************************打印start***********************************************************/
function printTransportOrder() {
	var rowIds = getIdSelections();
	if (rowIds.length <= 0) {
		jp.error("请至少选择一条数据！");
		return;
	}
	bq.openPostWindow("${ctx}/tms/print/transportOrder/printTransportOrder", 'ids', rowIds.join(','), '打印运输订单');
}
/******************************************打印end***********************************************************/
</script>