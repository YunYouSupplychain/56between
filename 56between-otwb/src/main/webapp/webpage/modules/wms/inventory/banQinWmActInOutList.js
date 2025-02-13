<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#beginTimeF').datetimepicker({format: "YYYY-MM-DD"});
    $('#endTimeF').datetimepicker({format: "YYYY-MM-DD"});

    $('#banQinWmActInOutTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
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
        columns: [
            [{
                title: "统计期",
                align: "center",
                colspan: 3,
                rowspan: 1
            }, {
                title: "期初库存",
                align: "center",
                colspan: 3,
                rowspan: 1
            }, {
                title: "本期进库",
                align: "center",
                colspan: 3,
                rowspan: 1
            }, {
                title: "本期出库",
                align: "center",
                colspan: 3,
                rowspan: 1
            }, {
                title: "本期库存",
                align: "center",
                colspan: 3,
                rowspan: 1
            }],
            [{
                field: "year",
                title: "年",
                align: "center"
            }, {
                field: "month",
                title: "月",
                align: "center"
            }, {
                field: "days",
                title: "日",
                align: "center"
            }, {
                field: 'plBefore',
                title: "托盘(个)",
                align: "center"
            }, {
                field: 'qtyBefore',
                title: "数量(盒/件)",
                align: "center"
            }, {
                field: 'grossWeightBefore',
                title: "毛重(千克)",
                align: "center"
            }, {
                field: 'plIn',
                title: "托盘(个)",
                align: "center"
            }, {
                field: 'qtyIn',
                title: "数量(盒/件)",
                align: "center"
            }, {
                field: 'grossWeightIn',
                title: "毛重(千克)",
                align: "center"
            }, {
                field: 'plOut',
                title: "托盘(个)",
                align: "center"
            }, {
                field: 'qtyOut',
                title: "数量(盒/件)",
                align: "center"
            }, {
                field: 'grossWeightOut',
                title: "毛重(千克)",
                align: "center"
            }, {
                field: 'plAfter',
                title: "托盘(个)",
                align: "center"
            }, {
                field: 'qtyAfter',
                title: "数量(盒/件)",
                align: "center"
            }, {
                field: 'grossWeightAfter',
                title: "毛重(千克)",
                align: "center"
            }]
        ]
    });

    $("#search").click("click", function () {
        var validate = bq.headerSubmitCheck('#searchForm');
        if (!validate.isSuccess) {
            jp.bqWaring(validate.msg);
            return;
        }
        $('#banQinWmActInOutTable').bootstrapTable('refresh', {url: "${ctx}/wms/inventory/banQinWmActInOut/data"});
    });
    $("#reset").click("click", function () {
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
    });
});

function exportData() {
    $('#banQinWmActInOutTable').tableExport({type: 'excel', escape: 'false', fileName: '进出存数据'});
}

</script>