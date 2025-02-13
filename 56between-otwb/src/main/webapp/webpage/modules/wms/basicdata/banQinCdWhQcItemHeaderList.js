<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#banQinCdWhQcItemHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/basicdata/banQinCdWhQcItemHeader/data",
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
            field: 'itemGroupCode',
            title: '质检项组编码',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        },{
            field: 'itemGroupName',
            title: '质检项组名称',
            sortable: true
        }]
    });
    $('#banQinCdWhQcItemHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinCdWhQcItemHeaderTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinCdWhQcItemHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinCdWhQcItemHeaderTable').bootstrapTable('refresh');
    });
});

function getIdSelections() {
    return $.map($("#banQinCdWhQcItemHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该质检项记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/basicdata/banQinCdWhQcItemHeader/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinCdWhQcItemHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

function add() {
    jp.openBQDialog('新增质检项', "${ctx}/wms/basicdata/banQinCdWhQcItemHeader/form", '80%', '80%', $('#banQinCdWhQcItemHeaderTable'));
}

function edit(id) {
    if (id == undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="basicdata:banQinCdWhQcItemHeader:edit">
        jp.openBQDialog('编辑质检项', "${ctx}/wms/basicdata/banQinCdWhQcItemHeader/form?id=" + id, '80%', '80%', $('#banQinCdWhQcItemHeaderTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="basicdata:banQinCdWhQcItemHeader:edit">
        jp.openBQDialog('查看质检项', "${ctx}/wms/basicdata/banQinCdWhQcItemHeader/form?id=" + id, '80%', '80%', $('#banQinCdWhQcItemHeaderTable'));
    </shiro:lacksPermission>
}

</script>