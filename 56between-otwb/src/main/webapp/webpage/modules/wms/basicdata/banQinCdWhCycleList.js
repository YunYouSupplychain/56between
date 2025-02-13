<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#banQinCdWhCycleTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/basicdata/banQinCdWhCycle/data",
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
            field: 'cycleCode',
            title: '循环级别编码',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        },{
            field: 'cycleName',
            title: '循环级别名称',
            sortable: true
        },{
            field: 'cycleLife',
            title: '循环周期',
            sortable: true
        }]
    });
    $('#banQinCdWhCycleTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinCdWhCycleTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinCdWhCycleTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinCdWhCycleTable').bootstrapTable('refresh');
    });
});

function getIdSelections() {
    return $.map($("#banQinCdWhCycleTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该循环级别记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/basicdata/banQinCdWhCycle/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinCdWhCycleTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增循环级别', "${ctx}/wms/basicdata/banQinCdWhCycle/form", '800px', '300px', $('#banQinCdWhCycleTable'));
}

function edit(id) {
    if(id == undefined){
        id = getIdSelections();
    }
    <shiro:hasPermission name="basicdata:banQinCdWhCycle:edit">
        jp.openDialog('编辑循环级别', "${ctx}/wms/basicdata/banQinCdWhCycle/form?id=" + id, '800px', '300px', $('#banQinCdWhCycleTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="basicdata:banQinCdWhCycle:edit">
        jp.openDialogView('查看循环级别', "${ctx}/wms/basicdata/banQinCdWhCycle/form?id=" + id, '800px', '300px', $('#banQinCdWhCycleTable'));
    </shiro:lacksPermission>
}

</script>