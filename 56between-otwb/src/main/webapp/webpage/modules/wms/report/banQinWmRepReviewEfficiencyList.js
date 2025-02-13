<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#orgId").val(jp.getCurrentOrg().orgId);
    $("#reportType").val("REVIEW");
    $('#date').datetimepicker({format: "YYYY-MM-DD"});
    $('#banQinWmRepWorkEfficiencyTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/report/wmRepWorkEfficiency/workEfficiency/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [
        {
            field: 'operateDate',
            title: '日期'
        },
        {
            field: 'serialNo',
            title: '序号'
        },
        {
            field: 'operator',
            title: '姓名'
        },
        {
            field: 'totalEaNum',
            title: '总件数'
        },
        {
            field: 'totalOrderNum',
            title: '总单数'
        },
        {
            field: 'averageEaNum',
            title: '单均件'
        },
        {
            field: 'workingTime',
            title: '作业时长'
        },
        {
            field: 'eaEfficiency',
            title: '件效率（件/H）'
        },
        {
            field: 'orderEfficiency',
            title: '单效率（件/H）'
        }]
    });

    $("#search").click("click", function () {
        var validate = bq.headerSubmitCheck("#searchForm");
        if(!validate.isSuccess){
            jp.bqError(validate.msg);
            return;
        }
        $('#banQinWmRepWorkEfficiencyTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $("#orgId").val(jp.getCurrentOrg().orgId);
        $("#reportType").val("REVIEW");
    });
    $("#export").click("click", function () {
        var validate = bq.headerSubmitCheck("#searchForm");
        if(!validate.isSuccess){
            jp.bqError(validate.msg);
            return;
        }
        bq.exportExcel("${ctx}/wms/report/wmRepWorkEfficiency/workEfficiency/export", "验货效率", $("#searchForm"));
    });
});


</script>