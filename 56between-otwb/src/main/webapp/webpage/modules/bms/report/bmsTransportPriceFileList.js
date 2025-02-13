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
    $('#bmsTransportPriceFileListTable').bootstrapTable({
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
            field: 'settleObjectName',
            title: '结算对象',
            sortable: true
        }, {
            field: 'carType',
            title: '车型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_CAR_TYPE'))}, value, "-");
            }
        }, {
            field: 'billSubjectName',
            title: '费用科目',
            sortable: true
        }, {
            field: 'fm',
            title: '阶梯范围从',
            sortable: true
        }, {
            field: 'to',
            title: '阶梯范围到',
            sortable: true
        }, {
            field: 'price',
            title: '单价',
            sortable: true
        }]
    });
}

function search() {
    $('#bmsTransportPriceFileListTable').bootstrapTable('refresh', {url: "${ctx}/bms/report/transportPriceFileList/data"});
}

function exportExcel() {
    var searchParam = $("#searchForm").serializeJSON();
    searchParam.orgId = jp.getCurrentOrg().orgId;
    bq.exportExcelNew("${ctx}/bms/report/transportPriceFileList/export", "运输价格档案清单", searchParam);
}

</script>