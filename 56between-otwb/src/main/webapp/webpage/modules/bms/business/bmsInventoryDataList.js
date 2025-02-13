<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#dateFm').datetimepicker({format: "YYYY-MM-DD"});
    $('#dateTo').datetimepicker({format: "YYYY-MM-DD"});
    $("#orgId").val(jp.getCurrentOrg().orgId);
    $('#dateFm input').val(jp.dateFormat(new Date().addTime("Month", -1), "yyyy-MM-dd"));
    $('#dateTo input').val(jp.dateFormat(new Date(), "yyyy-MM-dd"));
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        var validate = bq.headerSubmitCheck("#searchForm");
        if (!validate.isSuccess) {
            jp.alert(validate.msg);
            return;
        }
        $('#bmsInventoryDataTable').bootstrapTable('refresh', {url: "${ctx}/bms/business/bmsInventoryData/data"});
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
    });
    $("#btnImport").click(function () {
        bq.importExcel('${ctx}/bms/business/bmsInventoryData/import');
    });
});

function initTable() {
    $('#bmsInventoryDataTable').bootstrapTable({
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
            field: 'invDate',
            title: '日期',
            sortable: true
        }, {
            field: 'businessModel',
            title: '库存模式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BUSINESS_TYPE'))}, value, value);
            }
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
            field: 'lotNo',
            title: '批次号',
            sortable: true
        }, {
            field: 'endQty',
            title: '数量',
            sortable: true
        }, {
            field: 'endQtyCs',
            title: '箱数',
            sortable: true
        }, {
            field: 'endQtyPl',
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
        var length = $('#bmsInventoryDataTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#cancelFee').prop('disabled', !length);
        $('#addFee').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#bmsInventoryDataTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该库存数据记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/business/bmsInventoryData/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#bmsInventoryDataTable').bootstrapTable('refresh');
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
        url: "${ctx}/bms/business/bmsInventoryData/pullData",
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
        jp.get("${ctx}/bms/business/bmsInventoryData/isFee?ids=" + getIdSelections() + "&isFee=" + flag, function (data) {
            if (data.success) {
                $('#bmsInventoryDataTable').bootstrapTable('refresh');
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
    bq.exportExcelNew("${ctx}/bms/business/bmsInventoryData/export", "库存数据", searchParam)
}
</script>