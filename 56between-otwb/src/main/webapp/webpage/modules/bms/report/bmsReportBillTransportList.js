<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#orgId").val(jp.getCurrentOrg().orgId);
    $("#billModule").val("02");
    $("#receivablePayable").val("02");
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        search();
    });
    $("#reset").click("click", function () {// 绑定重置按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $("#receivablePayable").val("02");
    });
    $("#export").click("click", function () {// 绑定导出按钮
        exportExcel();
    });
});

function initTable() {
    $('#bmsBillTransportDetailTable').bootstrapTable({
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.billModule = "02";
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            field: 'billNo',
            title: '费用单号',
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
            field: 'settleModelCode',
            title: '结算模型编码',
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
            field: 'receivablePayable',
            title: '应收应付',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_RECEIVABLE_PAYABLE'))}, value, "-");
            }
        }, {
            field: 'businessDate',
            title: '业务日期',
            sortable: true
        }, {
            field: 'sysOrderNo',
            title: '系统订单号',
            sortable: true
        }, {
            field: 'customerOrderNo',
            title: '客户订单号',
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
            field: 'carNo',
            title: '车牌号',
            sortable: true
        }, {
            field: 'driver',
            title: '司机',
            sortable: true
        }, {
            field: 'carType',
            title: '车型',
            sortable: true
        }, {
            field: 'route',
            title: '路线',
            sortable: true
        }, {
            field: 'billSubjectName',
            title: '费用科目名称',
            sortable: true
        }, {
            field: 'billStandard',
            title: '计费标准',
            sortable: true,
            cellStyle: function () {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'occurrenceQty',
            title: '发生量',
            sortable: true,
            cellStyle: function () {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'billQty',
            title: '计费量',
            sortable: true,
            cellStyle: function () {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'cost',
            title: '费用',
            sortable: true,
            cellStyle: function () {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'sysContractNo',
            title: '系统合同编号',
            sortable: true
        }, {
            field: 'subcontractNo',
            title: '子合同编号',
            sortable: true
        }, {
            field: 'billSubjectCode',
            title: '费用科目代码',
            sortable: true
        }, {
            field: 'formula',
            title: '公式',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注',
            sortable: true
        }]
    });
}

function getTotal() {
    var searchParam = $("#searchForm").serializeJSON();
    searchParam.orgId = jp.getCurrentOrg().orgId;
    searchParam.billModule = "02";
    jp.post("${ctx}/bms/report/billTransport/getTotal", searchParam, function (data) {
        if (data.success) {
            $('#footer').show();
            $('#occurrenceQty').html(data.body.sumOccurrenceQty);
            $('#billQty').html(data.body.sumBillQty);
            $('#cost').html(data.body.sumCost);
        }
    });
}

function search() {
    $('#bmsBillTransportDetailTable').bootstrapTable('refresh', {url: "${ctx}/bms/report/billTransport/data"});
    getTotal();
}

function exportExcel() {
    var searchParam = $("#searchForm").serializeJSON();
    searchParam.orgId = jp.getCurrentOrg().orgId;
    searchParam.billModule = "02";
    bq.exportExcelNew("${ctx}/bms/report/billTransport/exportExcel", "运输费用", searchParam);
}

</script>