<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#tmsRoutingNodeExceptionTable').bootstrapTable({
        //请求方法
        method: 'get',
        //类型json
        dataType: "json",
        //显示刷新按钮
        showRefresh: true,
        //显示切换手机试图按钮
        showToggle: false,
        //显示 内容列下拉框
        showColumns: true,
        //显示到处按钮
        showExport: true,
        //显示切换分页按钮
        showPaginationSwitch: false,
        //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        cache: false,
        //是否显示分页（*）
        pagination: true,
        //排序方式
        sortOrder: "asc",
        //初始化加载第一页，默认第一页
        pageNumber: 1,
        //每页的记录行数（*）
        pageSize: 10,
        //可供选择的每页的行数（*）
        pageList: [10, 25, 50, 100],
        //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
        url: "${ctx}/tms/tmsRoutingNodeException/data",
        //默认值为 'limit',传给服务端的参数为：limit, offset, search, sort, order Else
        //queryParamsType:'',
        ////查询参数,每次调用是会带上这个参数，可自定义
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        //分页方式：client客户端分页，server服务端分页（*）
        sidePagination: "server",
        contextMenuTrigger: "right",//pc端 按右键弹出菜单
        contextMenuTriggerMobile: "press",//手机端 弹出菜单，click：单击， press：长按。
        contextMenu: '#context-menu',
        onClickRow: function (row, $el) {
        },
        columns: [{
            checkbox: true
        },
        {
            field: 'orderNo',
            title: '订单号',
            sortable: true
        },
        {
            field: 'trackingNo',
            title: '快递单号',
            sortable: true
        },
        {
            field: 'message',
            title: '错误信息',
            sortable: true
        },
        {
            field: 'type',
            title: '承运商类型',
            sortable: true
        }]
    });

    if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
        $('#tmsRoutingNodeExceptionTable').bootstrapTable("toggleView");
    }

    $("#search").click("click", function () {
        $('#tmsRoutingNodeExceptionTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#tmsRoutingNodeExceptionTable').bootstrapTable('refresh');
    });
});

</script>