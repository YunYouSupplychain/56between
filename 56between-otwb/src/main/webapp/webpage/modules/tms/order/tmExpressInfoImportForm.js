<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initV();

    initDetail();
    showLabel();
    // 默认展示第一个选项卡
    $("#detailTabs a:first").tab('show');
});

function serializeJson($form) {
    var o = {};
    $.each($form.serializeArray(), function () {
        if (o[this.name] !== undefined && o[this.name] !== null) {
            o[this.name] = o[this.name] + "," + this.value;
        } else {
            o[this.name] = this.value || null;
        }
    });
    return o;
}

/*初始化值*/
function initV() {
    $("#orgId").val(jp.getCurrentOrg().orgId);
}

/*初始化明细*/
function initDetail() {
    var $detailTable = $("#tmExpressInforImportDetailTable");
    $detailTable.bootstrapTable('destroy');
    $detailTable.bootstrapTable({
        showExport: false,//显示到处按钮
        cache: false,//是否使用缓存，默认为true
        pagination: true,//是否显示分页
        queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
            var searchParam = {};
            searchParam.importNo = $("#importNo").val();
            searchParam.orgId = $("#orgId").val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        sidePagination: "server",//分页方式：client客户端分页，server服务端分页
        columns: [{
            checkbox: true
        }, {
            field: 'customerNo',
            title: '客户订单号',
            sortable: true
        }, {
            field: 'mailNo',
            title: '面单号',
            sortable: true
        }, {
            field: 'transDate',
            title: '运输时间',
            sortable: true
        }, {
            field: 'carrierCode',
            title: '承运商编码',
            sortable: true
        }]
    });
}

/*显示标签*/
function showLabel() {
    $("#tmExpressInforImportDetailTable").bootstrapTable('refresh', {url: "${ctx}/tms/order/tmExpressInfoImport/detail/data"});
}
</script>