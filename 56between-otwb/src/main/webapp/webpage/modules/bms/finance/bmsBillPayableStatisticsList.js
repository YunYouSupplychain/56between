<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#createTimeFm').datetimepicker({format: "YYYY-MM-DD 00:00:00"});
    $('#createTimeTo').datetimepicker({format: "YYYY-MM-DD 23:59:59"});
    $('#auditTimeFm').datetimepicker({format: "YYYY-MM-DD 00:00:00"});
    $('#auditTimeTo').datetimepicker({format: "YYYY-MM-DD 23:59:59"});
    $("#filterZero").val("Y");
    $("#receivablePayable").val("02");
    $("#parentId").val(jp.getCurrentOrg().orgId);
    $("#orgId").val(jp.getCurrentOrg().orgId);
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#bmsBillStatisticsTable').bootstrapTable('refresh', {url: "${ctx}/bms/finance/bmsBillStatistics/data"});
        getTotal();
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $("#filterZero").val("Y");
        $("#receivablePayable").val("02");
        $("#parentId").val(jp.getCurrentOrg().orgId);
        $("#orgId").val(jp.getCurrentOrg().orgId);
    });
});

function initTable() {
    $('#bmsBillStatisticsTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
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
            field: 'confirmNo',
            title: '账单号',
            sortable: true
        }, {
            field: 'dateRange',
            title: '日期范围',
            sortable: true
        }, {
            field: 'warehouseCode',
            title: '仓库编码',
            sortable: true

        }, {
            field: 'warehouseName',
            title: '仓库名称',
            sortable: true
        }, {
            field: 'contractNo',
            title: '客户合同编号',
            sortable: true
        }, {
            field: 'settleObjectCode',
            title: '结算对象代码',
            sortable: true
        }, {
            field: 'settleObjectName',
            title: '结算对象名称',
            sortable: true
        }, {
            field: 'billModule',
            title: '费用模块',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_MODULE'))}, value, "-");
            }
        }, {
            field: 'billSubjectName',
            title: '费用科目名称',
            sortable: true
        }, {
            field: 'occurrenceQty',
            title: '发生量',
            sortable: true,
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'billQty',
            title: '计费量',
            sortable: true,
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'billStandard',
            title: '计费标准值',
            sortable: true,
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'cost',
            title: '费用',
            sortable: true,
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'remarks',
            title: '备注',
            sortable: true
        }, {
            field: 'settleModelCode',
            title: '结算模型编码',
            sortable: true
        }, {
            field: 'sysContractNo',
            title: '系统合同编号',
            sortable: true
        }, {
            field: 'settleMethod',
            title: '结算方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_SETTLEMENT_TYPE'))}, value, "-");
            }
        }, {
            field: 'settleCategory',
            title: '结算类别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_SETTLEMENT_CATEGORY'))}, value, "-");
            }
        }, {
            field: 'billSubjectCode',
            title: '费用科目代码',
            sortable: true
        }, {
            field: 'billCategory',
            title: '费用类别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_SUBJECT_CATEGORY'))}, value, "-");
            }
        }, {
            field: 'billTermsCode',
            title: '计费条款代码',
            sortable: true
        }, {
            field: 'billTermsDesc',
            title: '计费条款说明',
            sortable: true
        }, {
            field: 'subcontractNo',
            title: '子合同编号',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }]
    });
}

function getTotal() {
    jp.post("${ctx}/bms/finance/bmsBillStatistics/getTotal", $("#searchForm").serialize(), function (data) {
        if (data.success) {
            $('#footer').show();
            $('#occurrenceQty').html(data.body.sumOccurrenceQty);
            $('#billQty').html(data.body.sumBillQty);
            $('#cost').html(data.body.sumCost);
        }
    })
}

function exportExcel() {
    var validate = bq.headerSubmitCheck("#searchForm");
    if (!validate.isSuccess) {
        jp.alert(validate.msg);
        return;
    }
    bq.exportExcelNew("${ctx}/bms/finance/bmsBillStatistics/export", "费用统计", $("#searchForm").serializeJSON());
}

</script>