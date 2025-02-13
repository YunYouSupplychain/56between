<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#orgId").val(jp.getCurrentOrg().orgId);
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        search();
    });
    $("#reset").click("click", function () {// 绑定重置按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
    });
    $("#export").click("click", function () {// 绑定导出按钮
        exportExcel();
    });
});

function initTable() {
    $('#bmsContractAccountTable').bootstrapTable({
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            field: 'regionName',
            title: '区域',
            sortable: true
        }, {
            field: 'area',
            title: '城市',
            sortable: true
        }, {
            field: 'project',
            title: '项目',
            sortable: true
        }, {
            field: 'orgCode',
            title: '仓别',
            sortable: true
        }, {
            field: 'sysContractNo',
            title: '合同号',
            sortable: true
        }, {
            field: 'receivablePayable',
            title: '应收应付',
            sortable: true,
            formatter: function (value, row, index) {
                var valueArray = value.split(",");
                var labelArray = [];
                for (var i = 0; i < valueArray.length; i++) {
                    labelArray[i] = jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_RECEIVABLE_PAYABLE'))}, valueArray[i], "-");
                }
                return labelArray.join(",");
            }
        }, {
            field: 'settleObjectName',
            title: '结算对象',
            sortable: true
        }, {
            field: 'invoiceObjectName',
            title: '开票对象',
            sortable: true
        }, {
            field: 'effectiveDateFm',
            title: '合同起租日',
            sortable: true
        }, {
            field: 'effectiveDateTo',
            title: '合同终止日',
            sortable: true
        }, {
            field: 'contractCategory',
            title: '合同类别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_CONTRACT_CATEGORY'))}, value, "-");
            }
        }, {
            field: 'contractType',
            title: '合同类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_CONTRACT_TYPE'))}, value, "-");
            }
        }, {
            field: 'contractDesc',
            title: '合同概述',
            sortable: true
        }, {
            field: 'belongToCompany',
            title: '归属公司',
            sortable: true
        }, {
            field: 'contractor',
            title: '签约人',
            sortable: true
        }, {
            field: 'checkAccountsPerson',
            title: '客户对账人',
            sortable: true
        }, {
            field: 'checkAccountsDirector',
            title: '对账负责人',
            sortable: true
        }, {
            field: 'checkAccountsTime',
            title: '对账时间',
            sortable: true
        }, {
            field: 'billingRequirement',
            title: '开票要求',
            sortable: true
        }, {
            field: 'invoiceType',
            title: '发票类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_INVOICE_TYPE'))}, value, "-");
            }
        }, {
            field: 'invoiceTaxRate',
            title: '发票税率',
            sortable: true
        }, {
            field: 'contractStatus',
            title: '合同状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_CONTRACT_STATUS'))}, value, "-");
            }
        }]
    });
}

function search() {
    $('#bmsContractAccountTable').bootstrapTable('refresh', {url: "${ctx}/bms/report/contractAccount/data"});
}

function exportExcel() {
    var searchParam = $("#searchForm").serializeJSON();
    searchParam.orgId = jp.getCurrentOrg().orgId;
    bq.exportExcelNew("${ctx}/bms/report/contractAccount/export", "合同台账", searchParam);
}

</script>