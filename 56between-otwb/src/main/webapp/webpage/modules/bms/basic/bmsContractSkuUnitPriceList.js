<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    initTable();

    $("#btnImport").click(function () {
        bq.importExcel("${ctx}/bms/basic/bmsContractSkuPrice/import")
    });
    $("#search").click("click", function () {// 绑定查询按扭
        $("#orgId").val(jp.getCurrentOrg().orgId);
        $('#bmsContractSkuPriceTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定重置按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $("#orgId").val(jp.getCurrentOrg().orgId);
        $('#bmsContractSkuPriceTable').bootstrapTable('refresh');
    });
});

function initTable(){
    $('#bmsContractSkuPriceTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/bms/basic/bmsContractSkuPrice/data",
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
            field: 'sysContractNo',
            title: '系统合同编号',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'settleObjectName',
            title: '结算对象',
            sortable: true
        }, {
            field: 'skuClassName',
            title: '品类',
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
            field: 'price',
            title: '未税单价',
            sortable: true
        }, {
            field: 'taxPrice',
            title: '含税单价',
            sortable: true
        }, {
            field: 'unit',
            title: '单位',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_CONTRACT_UNIT'))}, value, "-");
            }
        }, {
            field: 'updateBy.name',
            title: '更新人',
            sortable: true
        }, {
            field: 'updateDate',
            title: '更新时间',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注信息',
            sortable: true
        }]
    }).on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#bmsContractSkuPriceTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#bmsContractSkuPriceTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该合同商品价格记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/basic/bmsContractSkuPrice/deleteAll?ids=" + getIdSelections(), function (data) {
            $('#bmsContractSkuPriceTable').bootstrapTable('refresh');
            if (data.success) {
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        });
    });
}

function add() {
    jp.openDialog('新增合同商品价格', "${ctx}/bms/basic/bmsContractSkuPrice/form", '800px', '500px', $('#bmsContractSkuPriceTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="bms:bmsContractSkuPrice:edit">
        jp.openDialog('编辑合同商品价格', "${ctx}/bms/basic/bmsContractSkuPrice/form?id=" + id, '800px', '500px', $('#bmsContractSkuPriceTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="bms:bmsContractSkuPrice:edit">
        jp.openDialogView('查看合同商品价格', "${ctx}/bms/basic/bmsContractSkuPrice/form?id=" + id, '800px', '500px', $('#bmsContractSkuPriceTable'));
    </shiro:lacksPermission>
}

function exportExcel() {
    var searchParam = $("#searchForm").serializeJSON();
    searchParam.orgId = jp.getCurrentOrg().orgId;
    bq.exportExcelNew("${ctx}/bms/basic/bmsContractSkuPrice/export", "仓储商品价格", searchParam);
}

</script>