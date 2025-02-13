<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var url = "${ctx}/tms/order/tmPreTransportOrder/data";
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

$(document).ready(function () {
	$("#baseOrgId").val(tmOrg.id);
	$("#orderTimeFm").datetimepicker({format: "YYYY-MM-DD 00:00:00"});
	$("#orderTimeTo").datetimepicker({format: "YYYY-MM-DD 23:59:59"});

	// 初始化各页签中Bootstrap表格
	$("#allTable").bootstrapTable(getOption(allParams));
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
    $('#copy').prop('disabled', length !== 1);
	$('#remove').prop('disabled', !length);
}
/*按钮默认不可用*/
function btnDefaultDisabled() {
	$('#remove').prop('disabled', true);
	$('#edit').prop('disabled', true);
	$('#audit').prop('disabled', true);
	$('#cancelAudit').prop('disabled', true);
	$('#addLabel').prop('disabled', true);
	$('#receive').prop('disabled', true);
	$('#sign').prop('disabled', true);
	$('#receipt').prop('disabled', true);
	$('#tracking').prop('disabled', true);
	$('#runTrack').prop('disabled', true);
	$('#vehicleLocation').prop('disabled', true);
    $('#copy').prop('disabled', true);
}
/*刷新表格数据*/
function refreshTable() {
	$(curTab).bootstrapTable('refresh', {url: url});
}
/*Bootstrap初始化参数*/
function getOption(params) {
	return {
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		queryParams: params,
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
            field: 'customerName',
            title: '客户',
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
			field: 'principalName',
			title: '委托方',
			sortable: true

		}, {
			field: 'consigneeName',
			title: '收货方',
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
			field: 'receiveOutletName',
			title: '提货网点',
			sortable: true
		}, {
			field: 'outletName',
			title: '配送网点',
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
			field: 'shipName',
			title: '发货方',
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
	jp.confirm('确认要删除该计划订单信息记录吗？', function () {
		jp.loading();
		jp.post("${ctx}/tms/order/tmPreTransportOrder/deleteAll", {ids: getIdSelections().join(',')}, function (data) {
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
	jp.openBQDialog('计划订单信息', "${ctx}/tms/order/tmPreTransportOrder/form", '90%', '90%', $(curTab));
}
/*编辑*/
function edit(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openBQDialog('计划订单信息', "${ctx}/tms/order/tmPreTransportOrder/form?id=" + id, '90%', '90%', $(curTab));
}

function copy() {
    jp.loading();
    jp.get("${ctx}/tms/order/tmPreTransportOrder/copy?id=" + getIdSelections(), function (data) {
        refreshTable();
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}
</script>