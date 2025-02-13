<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#tranTimeFrom').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#tranTimeTo').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});

    $('#banQinWmActTranTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inventory/banQinWmActTran/data",
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
            field: 'tranId',
            title: '交易ID',
            sortable: true
        }, {
            field: 'tranType',
            title: '交易类型',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_TRAN_TYPE'))}, value, "-");
            }
        }, {
            field: 'orderNo',
            title: '单据号',
            sortable: true
        }, {
            field: 'lineNo',
            title: '单据行号',
            sortable: true
        }, {
            field: 'tranOp',
            title: '操作人',
            sortable: true
        }, {
            field: 'tranTime',
            title: '交易时间',
            sortable: true
        }, {
            field: 'fmOwnerName',
            title: '源货主',
            sortable: true
        }, {
            field: 'fmSkuName',
            title: '源商品',
            sortable: true
        }, {
            field: 'fmLot',
            title: '源批次',
            sortable: true
        }, {
            field: 'fmLoc',
            title: '源库位',
            sortable: true
        }, {
            field: 'fmId',
            title: '源跟踪号',
            sortable: true
        }, {
            field: 'fmPack',
            title: '源包装规格',
            sortable: true
        }, {
            field: 'fmUom',
            title: '源单位',
            sortable: true
        }, {
            field: 'fmQtyUomOp',
            title: '源操作数量（增量）',
            sortable: true
        }, {
            field: 'fmQtyEaOp',
            title: '源操作数量EA（增量）',
            sortable: true
        }, {
            field: 'fmQtyEaBefore',
            title: '源操作前库存数',
            sortable: true
        }, {
            field: 'fmQtyEaAfter',
            title: '源操作后库存数',
            sortable: true
        }, {
            field: 'toOwnerName',
            title: '目标货主',
            sortable: true
        }, {
            field: 'toSkuName',
            title: '目标商品',
            sortable: true
        }, {
            field: 'toLot',
            title: '目标批次',
            sortable: true
        }, {
            field: 'toLoc',
            title: '目标库位',
            sortable: true
        }, {
            field: 'toId',
            title: '目标跟踪号',
            sortable: true
        }, {
            field: 'toPack',
            title: '目标包装规格',
            sortable: true
        }, {
            field: 'toUom',
            title: '目标单位',
            sortable: true
        }, {
            field: 'toQtyUomOp',
            title: '目标操作数量（增量）',
            sortable: true
        }, {
            field: 'toQtyEaOp',
            title: '目标操作数量EA（增量）',
            sortable: true
        }, {
            field: 'toQtyEaBefore',
            title: '目标操作前库存数',
            sortable: true
        }, {
            field: 'toQtyEaAfter',
            title: '目标操作后库存数',
            sortable: true
        }, {
            field: 'taskId',
            title: '任务ID',
            sortable: true
        }, {
            field: 'taskLineNo',
            title: '任务行号',
            sortable: true
        }, {
            field: 'lotAtt01',
            title: '生产日期',
            sortable: true
        }, {
            field: 'lotAtt02',
            title: '失效日期',
            sortable: true
        }, {
            field: 'lotAtt03',
            title: '入库日期',
            sortable: true
        }, {
            field: 'lotAtt04',
            title: '批次属性4',
            sortable: true
        }, {
            field: 'lotAtt05',
            title: '批次属性05',
            sortable: true
        }, {
            field: 'lotAtt06',
            title: '批次属性06',
            sortable: true
        }, {
            field: 'lotAtt07',
            title: '批次属性07',
            sortable: true
        }, {
            field: 'lotAtt08',
            title: '批次属性08',
            sortable: true
        }, {
            field: 'lotAtt09',
            title: '批次属性09',
            sortable: true
        }, {
            field: 'lotAtt10',
            title: '批次属性10',
            sortable: true
        }, {
            field: 'lotAtt11',
            title: '批次属性11',
            sortable: true
        }, {
            field: 'lotAtt12',
            title: '批次属性12',
            sortable: true
        }, {
            field: 'tlotAtt01',
            title: '目标生产日期',
            sortable: true
        }, {
            field: 'tlotAtt02',
            title: '目标失效日期',
            sortable: true
        }, {
            field: 'tlotAtt03',
            title: '目标入库日期',
            sortable: true
        }, {
            field: 'tlotAtt04',
            title: '目标批次属性04',
            sortable: true
        }, {
            field: 'tlotAtt05',
            title: '目标批次属性05',
            sortable: true
        }, {
            field: 'tlotAtt06',
            title: '目标批次属性06',
            sortable: true
        }, {
            field: 'tlotAtt07',
            title: '目标批次属性07',
            sortable: true
        }, {
            field: 'tlotAtt08',
            title: '目标批次属性08',
            sortable: true
        }, {
            field: 'tlotAtt09',
            title: '目标批次属性09',
            sortable: true
        }, {
            field: 'tlotAtt10',
            title: '目标批次属性10',
            sortable: true
        }, {
            field: 'tlotAtt11',
            title: '目标批次属性11',
            sortable: true
        }, {
            field: 'tlotAtt12',
            title: '目标批次属性12',
            sortable: true
        }, {
            field: 'createBy.name',
            title: '创建人',
            sortable: true
        }, {
            field: 'createDate',
            title: '创建时间',
            sortable: true
        }, {
            field: 'updateBy.name',
            title: '修改人',
            sortable: true
        }, {
            field: 'updateDate',
            title: '修改时间',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });

    $("#search").click("click", function () {
        $('#banQinWmActTranTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmActTranTable').bootstrapTable('refresh');
    });
});

function exportData() {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    bq.exportExcel("${ctx}/wms/inventory/banQinWmActTran/export", "库存交易记录" + jp.dateFormat(new Date(), "yyyyMMddhhmmss"), $("#searchForm"), function () {
        jp.close();
    });
}

</script>