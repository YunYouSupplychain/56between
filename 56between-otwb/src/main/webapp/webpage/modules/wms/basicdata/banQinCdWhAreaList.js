<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#banQinCdWhAreaTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/basicdata/banQinCdWhArea/data",
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
        },{
            field: 'areaCode',
            title: '区域编码',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        },{
            field: 'areaName',
            title: '区域名称',
            sortable: true
        }]
    });
    $('#banQinCdWhAreaTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinCdWhAreaTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinCdWhAreaTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinCdWhAreaTable').bootstrapTable('refresh');
    });
});

function getIdSelections() {
    return $.map($("#banQinCdWhAreaTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该区域记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/basicdata/banQinCdWhArea/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinCdWhAreaTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增区域', "${ctx}/wms/basicdata/banQinCdWhArea/form", '650px', '300px', $('#banQinCdWhAreaTable'));
}

function edit(id) {
    if(id == undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="basicdata:banQinCdWhArea:edit">
        jp.openDialog('编辑区域', "${ctx}/wms/basicdata/banQinCdWhArea/form?id=" + id,'650px', '300px', $('#banQinCdWhAreaTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="basicdata:banQinCdWhArea:edit">
        jp.openDialogView('查看区域', "${ctx}/wms/basicdata/banQinCdWhArea/form?id=" + id,'650px', '300px', $('#banQinCdWhAreaTable'));
    </shiro:lacksPermission>
}

</script>