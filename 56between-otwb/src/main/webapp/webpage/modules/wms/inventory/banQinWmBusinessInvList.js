<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#lot').datetimepicker({format: "YYYY-MM"});
    laydate.render({elem: '#settleMonth', type: 'month'});
    laydate.render({elem: '#orderTimeFm', theme: '#393D49', type: 'datetime'});
    laydate.render({elem: '#orderTimeTo', theme: '#393D49', type: 'datetime'});

    $('#banQinWmBusinessInvTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inventory/banQinWmBusinessInv/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.beginTime = searchParam.beginTime + ' 00:00:00';
            searchParam.endTime = searchParam.endTime + ' 23:59:59';
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            field: 'orderType',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_ACT_STATUS'))}, value, "-");
            }
        }, {
            field: 'zoneName',
            title: '仓库',
            sortable: true
        }, {
            field: 'ownerName',
            title: '当前客户',
            sortable: true
        }, {
            field: 'firstOwner',
            title: '第一货主',
            sortable: true
        }, {
            field: 'toOwner',
            title: '货主后货主',
            sortable: true
        }, {
            field: 'tfDate',
            title: '货转日期',
            sortable: true
        }, {
            field: 'adDate',
            title: '调整日期',
            sortable: true
        }, {
            field: 'orderNo',
            title: '订单号',
            sortable: true
        }, {
            field: 'opTime',
            title: '业务时间',
            sortable: true
        }, {
            field: 'lotAtt07',
            title: '包库',
            sortable: true
        }, {
            field: 'lotAtt06',
            title: '柜号',
            sortable: true
        }, {
            field: 'lotAtt03',
            title: '入库日期',
            sortable: true
        }, {
            field: 'skuName',
            title: '品名',
            sortable: true
        }, {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        }, {
            field: 'lotAtt05',
            title: '重量',
            sortable: true
        }, {
            field: 'bgDate',
            title: '期初日期',
            sortable: true
        }, {
            field: 'outboundDate',
            title: '出库日期',
            sortable: true
        }, {
            field: 'qtyIn',
            title: '数量(入)',
            sortable: true
        }, {
            field: 'weightIn',
            title: '重量(入)',
            sortable: true
        }, {
            field: 'qtyOut',
            title: '数量(出)',
            sortable: true
        }, {
            field: 'weightOut',
            title: '重量(出)',
            sortable: true
        }, {
            field: 'qtyEaBefore',
            title: '期初库存',
            sortable: true
        }, {
            field: 'qtyEaAfter',
            title: '期末库存',
            sortable: true
        }, {
            field: 'weightInv',
            title: '库存重量',
            sortable: true
        }, {
            field: 'lotAtt09',
            title: '分色件数',
            sortable: true
        }, {
            field: 'lotAtt08',
            title: '抄码件数',
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
            field: 'lot',
            title: '结算月份',
            sortable: true
        }, {
            field: 'storeDays',
            title: '仓储天数',
            sortable: true
        }, {
            field: 'def1',
            title: '自定义1',
            sortable: true
        }, {
            field: 'def2',
            title: '自定义2',
            sortable: true
        }, {
            field: 'def3',
            title: '自定义3',
            sortable: true
        }, {
            field: 'def4',
            title: '自定义4',
            sortable: true
        }, {
            field: 'def5',
            title: '自定义5',
            sortable: true
        }, {
            field: 'def6',
            title: '自定义6',
            sortable: true
        }, {
            field: 'def7',
            title: '自定义7',
            sortable: true
        }, {
            field: 'def8',
            title: '自定义8',
            sortable: true
        }, {
            field: 'def9',
            title: '自定义9',
            sortable: true
        }, {
            field: 'def10',
            title: '自定义10',
            sortable: true
        }, {
            field: 'def11',
            title: '自定义11',
            sortable: true
        }, {
            field: 'def12',
            title: '自定义12',
            sortable: true
        }]
    });

    $("#search").click("click", function () {
        $('#banQinWmBusinessInvTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
    });
    countQty();
});

function countQty() {
    var searchParam = $("#searchForm").serializeJSON();
    searchParam.orgId = jp.getCurrentOrg().orgId;
    jp.post("${ctx}/wms/inventory/banQinWmBusinessInv/count", searchParam, function (data) {
        $('#totalBgQty').val(data.totalBgQty);
        $('#totalInQty').val(data.totalInQty);
        $('#totalOutQty').val(data.totalOutQty);
        $('#totalEdQty').val(data.totalEdQty);
    })
}

function exportData() {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    bq.exportExcel("${ctx}/wms/inventory/banQinWmBusinessInv/export", "业务库存记录" + jp.dateFormat(new Date(), "yyyyMMddhhmmss"), $("#searchForm"), function () {
        jp.close();
    });
}

function settle() {
    $('#settleModal').modal();
}

function settleConfirm() {
    if (!$('#settleMonth').val()) {
        jp.bqWaring("请选择结算月份");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/inventory/banQinWmBusinessInv/settle?settleMonth=" + $('#settleMonth').val() + "&orgId=" + jp.getCurrentOrg().orgId, function (data) {
        $('#settleModal').modal('hide');
        if (data.success) {
            $('#banQinWmBusinessInvTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqWaring(data.msg)
        }
    })
}

function reCalcAndSettle() {
    $('#calcModal').modal();
}

function calcConfirm() {
    if (!$('#orderTimeFm').val() || !$('#orderTimeTo')) {
        jp.bqWaring("订单时间范围不能为空");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/inventory/banQinWmBusinessInv/reCalcAndSettle?orderTimeFm=" + $('#orderTimeFm').val() + "&orderTimeTo=" + $('#orderTimeTo').val() + "&orgId=" + jp.getCurrentOrg().orgId, function (data) {
        $('#calcModal').modal('hide');
        if (data.success) {
            $('#banQinWmBusinessInvTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqWaring(data.msg)
        }
    })
}

</script>