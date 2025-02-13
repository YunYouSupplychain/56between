<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#orderDateFm").datetimepicker({format: "YYYY-MM-DD"});
    $("#orderDateTo").datetimepicker({format: "YYYY-MM-DD"});
    $("#dispatchTimeFm").datetimepicker({format: "YYYY-MM-DD 00:00:00"});
    $("#dispatchTimeTo").datetimepicker({format: "YYYY-MM-DD 23:59:59"});
    $("#parentId").val(jp.getCurrentOrg().orgId);
    $("#orgId").val(jp.getCurrentOrg().orgId);
    $("#orderDateFm input").val(jp.dateFormat(new Date().addTime("Month", -1), "yyyy-MM-dd"));
    $("#orderDateTo input").val(jp.dateFormat(new Date(), "yyyy-MM-dd"));
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        var validate = bq.headerSubmitCheck("#searchForm");
        if (!validate.isSuccess) {
            jp.error(validate.msg);
            return;
        }
        $('#bmsDispatchDataTable').bootstrapTable('refresh', {url: "${ctx}/bms/business/bmsDispatchData/data"});
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
    });
    $("#btnImport").click(function () {
        bq.importExcel('${ctx}/bms/business/bmsDispatchData/import');
    });
});

function initTable() {
    $('#bmsDispatchDataTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            if (!searchParam.hasOwnProperty("orgId") || searchParam.orgId.length <= 0) {
                searchParam.orgId = jp.getCurrentOrg().orgId;
            }
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'orderNo',
            title: '派车单号',
            sortable: true
        }, {
            field: 'orderDate',
            title: '订单日期',
            sortable: true
        }, {
            field: 'dispatchTime',
            title: '派车时间',
            sortable: true
        }, {
            field: 'businessType',
            title: '业务订单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_BUSINESS_ORDER_TYPE'))}, value, value);
            }
        }, {
            field: 'orderNoA',
            title: '订单A(运输订单号)',
            sortable: true
        }, {
            field: 'transportMethod',
            title: '运输方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_METHOD'))}, value, value);
            }
        }, {
            field: 'principalCode',
            title: '委托方编码',
            sortable: true
        }, {
            field: 'principalName',
            title: '委托方名称',
            sortable: true
        }, {
            field: 'shipCode',
            title: '发货方编号',
            sortable: true
        }, {
            field: 'shipName',
            title: '发货方名称',
            sortable: true
        }, {
            field: 'shipCityName',
            title: '发货城市名称',
            sortable: true
        }, {
            field: 'consigneeCode',
            title: '收货方编号',
            sortable: true
        }, {
            field: 'consigneeName',
            title: '收货方名称',
            sortable: true
        }, {
            field: 'consigneeCityName',
            title: '收货城市名称',
            sortable: true
        }, {
            field: 'carrierCode',
            title: '承运商编码',
            sortable: true
        }, {
            field: 'carrierName',
            title: '承运商名称',
            sortable: true
        }, {
            field: 'carType',
            title: '车型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_CAR_TYPE'))}, value, value);
            }
        }, {
            field: 'vehicleNo',
            title: '车牌号',
            sortable: true
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主名称',
            sortable: true
        }, {
            field: 'supplierCode',
            title: '供应商编码',
            sortable: true
        }, {
            field: 'supplierName',
            title: '供应商名称',
            sortable: true
        }, {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        }, {
            field: 'skuName',
            title: '商品名称',
            sortable: true
        }, {
            field: 'qty',
            title: '数量',
            sortable: true
        }, {
            field: 'qtyCs',
            title: '箱数',
            sortable: true
        }, {
            field: 'qtyPl',
            title: '托数',
            sortable: true
        }, {
            field: 'weight',
            title: '重量',
            sortable: true
        }, {
            field: 'volume',
            title: '体积',
            sortable: true
        }, {
            field: 'origin',
            title: '起始地',
            sortable: true
        }, {
            field: 'destination',
            title: '目的地',
            sortable: true
        }, {
            field: 'isFee',
            title: '是否参与计费',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, value);
            }
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }]
    });
    $('#bmsDispatchDataTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#bmsDispatchDataTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#cancelFee').prop('disabled', !length);
        $('#addFee').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#bmsDispatchDataTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该派车配载数据记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/business/bmsDispatchData/deleteAll?ids=" + getIdSelections(), function (data) {
            $('#bmsDispatchDataTable').bootstrapTable('refresh');
            if (data.success) {
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function pull() {
    $("#pullModal").modal();
    $("#fmDate").datetimepicker({format: "YYYY-MM-DD"});
    $("#toDate").datetimepicker({format: "YYYY-MM-DD"});
    $("#pullDataForm input").val("");
    $("#pullDataForm select").val("");
}

function pullData() {
    var validate = bq.headerSubmitCheck("#pullDataForm");
    if (!validate.isSuccess) {
        jp.error(validate.msg);
        return;
    }
    jp.loading('  正在同步，请稍等...');
    var pullDataParam = $("#pullDataForm").serializeJSON();
    if (!pullDataParam.hasOwnProperty("orgId") || pullDataParam.orgId.length <= 0) {
        pullDataParam.orgId = jp.getCurrentOrg().orgId;
    }
    $.ajax({
        type: "POST",
        url: "${ctx}/bms/business/bmsDispatchData/pullData",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(pullDataParam),
        success: function (data) {
            if (data.success) {
                jp.success("同步完成");
                $("#pullModal").modal('hide');
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

function isFee(flag) {
    jp.confirm('是否确认操作？', function () {
        jp.loading("正在处理中……");
        jp.get("${ctx}/bms/business/bmsDispatchData/isFee?ids=" + getIdSelections() + "&isFee=" + flag, function (data) {
            if (data.success) {
                $('#bmsDispatchDataTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        });
    });
}

function exportExcel() {
    var validate = bq.headerSubmitCheck("#searchForm");
    if (!validate.isSuccess) {
        jp.alert(validate.msg);
        return;
    }
    var searchParam = $("#searchForm").serializeJSON();
    if (!searchParam.hasOwnProperty("orgId") || searchParam.orgId.length <= 0) {
        searchParam.orgId = jp.getCurrentOrg().orgId;
    }
    bq.exportExcelNew("${ctx}/bms/business/bmsDispatchData/export", "派车配载数据", searchParam)
}

</script>