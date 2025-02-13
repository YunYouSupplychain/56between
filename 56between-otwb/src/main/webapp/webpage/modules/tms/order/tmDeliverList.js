<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#baseOrgId").val(tmOrg.id);
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$("#tmDeliverTable").bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$("#baseOrgId").val(tmOrg.id);
	});
});

function initTable() {
    var $table = $('#tmDeliverTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url:"${ctx}/tms/order/tmDeliver/page",
        queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onLoadSuccess: function (data) {
            buttonControl();
        },
        columns: [{
            checkbox: true
        }, {
            field: 'labelNo',
            title: '标签号',
            sortable: true
        }, {
            field: 'transportNo',
            title: '运输订单',
            sortable: true
        }, {
            field: 'customerNo',
            title: '客户单号',
            sortable: true
        }, {
            field: 'dispatchNo',
            title: '派车单',
            sortable: true
        }, {
            field: 'nowOutletName',
            title: '货物所在网点',
            sortable: true
        }, {
            field: 'nextOutletName',
            title: '派送网点',
            sortable: true
        }, {
            field: 'shipCity',
            title: '发货城市',
            sortable: true
        }, {
            field: 'shipCode',
            title: '发货单位编码',
            sortable: true
        }, {
            field: 'shipName',
            title: '发货单位名称',
            sortable: true
        }, {
            field: 'shipper',
            title: '发货人',
            sortable: true
        }, {
            field: 'shipperTel',
            title: '发货人电话',
            sortable: true
        }, {
            field: 'shipAddress',
            title: '发货人地址',
            sortable: true
        }, {
            field: 'consigneeCity',
            title: '目的地城市',
            sortable: true
        }, {
            field: 'consigneeCode',
            title: '收货单位编码',
            sortable: true
        }, {
            field: 'consigneeName',
            title: '收货单位名称',
            sortable: true
        }, {
            field: 'consignee',
            title: '收货人',
            sortable: true
        }, {
            field: 'consigneeTel',
            title: '收货人电话',
            sortable: true
        }, {
            field: 'consigneeAddress',
            title: '收货人地址',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        $('#deliver').prop('disabled', !$table.bootstrapTable('getSelections').length);
    });
}

/*按钮使用控制*/
function buttonControl() {
	if ($('#tmDeliverTable').bootstrapTable('getData').length > 0) {
		$('#deliverByCondition').prop('disabled', false);
	} else {
		$('#deliverByCondition').prop('disabled', true);// 列表没有记录，则按扭禁用
	}
}

/*发货*/
function deliver() {
	jp.loading();
	var rows = $("#tmDeliverTable").bootstrapTable('getSelections');
	if (rows.length <= 0){
		jp.warning("请选择记录");
		return;
	}
	$.ajax({
		type: "post",
		url: "${ctx}/tms/order/tmDeliver/deliver",
		data: JSON.stringify(rows),
		dataType: "json",
		cache: false,
		contentType: "application/json",
		success: function (data) {
			$("#tmDeliverTable").bootstrapTable('refresh');
			if (data.success) {
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		}
	});
}

/*按条件发货*/
function deliverByCondition() {
	jp.loading();
	var searchParams = $("#searchForm").serializeJSON();
    searchParams.orgId = jp.getCurrentOrg().orgId;
	jp.post("${ctx}/tms/order/tmDeliver/deliverByCondition", searchParams, function (data) {
		$("#tmDeliverTable").bootstrapTable('refresh');
		if (data.success) {
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}
</script>