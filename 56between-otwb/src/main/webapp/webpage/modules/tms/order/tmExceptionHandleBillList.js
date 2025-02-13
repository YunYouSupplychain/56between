<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#baseOrgId").val(tmOrg.id);
	$("#registerTimeFrom").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$("#registerTimeTo").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmExceptionHandleBillTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		/*$("#orgId").val(jp.getCurrentOrg().orgId);*/
		$("#baseOrgId").val(tmOrg.id);
		$('#tmExceptionHandleBillTable').bootstrapTable('refresh');
	});
});

function initTable() {
    var $table = $('#tmExceptionHandleBillTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/order/tmExceptionHandleBill/data",
        //查询参数,每次调用是会带上这个参数，可自定义
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
            field: 'billNo',
            title: '异常单号',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'billStatus',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_EXCEPTION_BILL_STATUS'))}, value, "-");
            }
        }, {
            field: 'transportNo',
            title: '运输订单号',
            sortable: true
        }, {
            field: 'customerNo',
            title: '客户订单号',
            sortable: true
        }, {
            field: 'dispatchNo',
            title: '派车单号',
            sortable: true
        }, {
            field: 'labelNo',
            title: '标签号',
            sortable: true
        }, {
            field: 'consigneeName',
            title: '收货人名称',
            sortable: true
        }, {
            field: 'consigneePhone',
            title: '收货人电话',
            sortable: true
        }, {
            field: 'outletName',
            title: '网点',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注信息',
            sortable: true
        }, {
            field: 'exceptionType',
            title: '异常类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('exception_type'))}, value, "-");
            }
        }, {
            field: 'processNo',
            title: '流程单号',
            sortable: true
        }, {
            field: 'carNo',
            title: '车牌号',
            sortable: true
        }, {
            field: 'driver',
            title: '司机',
            sortable: true
        }, {
            field: 'registerPerson',
            title: '登记人',
            sortable: true
        }, {
            field: 'registerTime',
            title: '登记时间',
            sortable: true
        }, {
            field: 'happenTime',
            title: '发生时间',
            sortable: true
        }, {
            field: 'happenSysAreaName',
            title: '发生地点',
            sortable: true
        }, {
            field: 'liabilityPerson',
            title: '责任人',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#addFeeShare').prop('disabled', length !== 1);
        $('#submit').prop('disabled', !length);
        $('#audit').prop('disabled', !length);
        $('#returnBack').prop('disabled', !length);
    });
}

function getIdSelections() {
	return $.map($("#tmExceptionHandleBillTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该异常处理单记录吗？', function () {
		jp.loading();
		jp.post("${ctx}/tms/order/tmExceptionHandleBill/deleteAll", {ids: getIdSelections().join(',')}, function (data) {
			if (data.success) {
				$('#tmExceptionHandleBillTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openBQDialog('异常处理单', "${ctx}/tms/order/tmExceptionHandleBill/form", '90%', '90%', $('#tmExceptionHandleBillTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openBQDialog('异常处理单', "${ctx}/tms/order/tmExceptionHandleBill/form?id=" + id, '90%', '90%', $('#tmExceptionHandleBillTable'));
}
</script>