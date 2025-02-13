<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#orderDateFm').datetimepicker({format: "YYYY-MM-DD"});
    $('#orderDateTo').datetimepicker({format: "YYYY-MM-DD"});
    $('#dispatchDateFm').datetimepicker({format: "YYYY-MM-DD"});
    $('#dispatchDateTo').datetimepicker({format: "YYYY-MM-DD"});
    $("#orgId").val(jp.getCurrentOrg().orgId);
    $('#orderDateFm input').val(jp.dateFormat(new Date().addTime("Month", -1), "yyyy-MM-dd"));
    $('#orderDateTo input').val(jp.dateFormat(new Date(), "yyyy-MM-dd"));
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        var validate = bq.headerSubmitCheck("#searchForm");
        if (!validate.isSuccess) {
            jp.error(validate.msg);
            return;
        }
        $('#bmsExceptionDataTable').bootstrapTable('refresh', {url: "${ctx}/bms/business/bmsExceptionData/data"});
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
    });
    $("#btnImport").click(function () {
        bq.importExcel('${ctx}/bms/business/bmsExceptionData/import');
    });
});

function initTable() {
    $('#bmsExceptionDataTable').bootstrapTable({
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
            title: '异常单号',
            sortable: true
        }, {
            field: 'orderDate',
            title: '发生日期',
            sortable: true
        }, {
            field: 'exceptionType',
            title: '异常类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_EXCEPTION_TYPE'))}, value, value);
            }
        }, {
            field: 'dispatchNo',
            title: '派车单号',
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
            field: 'skuClassName',
            title: '品类',
            sortable: true
        }, {
            field: 'exceptionQty',
            title: '异常数量',
            sortable: true
        }, {
            field: 'exceptionQtyCs',
            title: '异常箱数',
            sortable: true
        }, {
            field: 'exceptionQtyPl',
            title: '异常托数',
            sortable: true
        }, {
            field: 'exceptionReason',
            title: '异常原因',
            sortable: true
        }, {
            field: 'responsibility',
            title: '责任方',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_RESPONSIBILITY'))}, value, value);
            }
        }, {
            field: 'responsiblePerson',
            title: '责任人',
            sortable: true
        }, {
            field: 'orderNoA',
            title: '订单号A(车辆门店单号)',
            sortable: true
        }, {
            field: 'orderNoB',
            title: '订单号B(母单号)',
            sortable: true
        }, {
            field: 'orderNoC',
            title: '订单号C(子单号)',
            sortable: true
        }, {
            field: 'customerCode',
            title: '客户编码',
            sortable: true
        }, {
            field: 'customerName',
            title: '客户名称',
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
            sortable: true
        }, {
            field: 'carNo',
            title: '车牌号',
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
    }).on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#bmsExceptionDataTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#cancelFee').prop('disabled', !length);
        $('#addFee').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#bmsExceptionDataTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该异常数据记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/business/bmsExceptionData/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#bmsExceptionDataTable').bootstrapTable('refresh');
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
        url: "${ctx}/bms/business/bmsExceptionData/pullData",
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
    jp.confirm('是否确认操作？', function(){
        jp.loading("正在处理中……");
        jp.get("${ctx}/bms/business/bmsExceptionData/isFee?ids=" + getIdSelections() + "&isFee=" + flag, function (data) {
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
    bq.exportExcelNew("${ctx}/bms/business/bmsExceptionData/export", "异常数据", searchParam)
}

</script>