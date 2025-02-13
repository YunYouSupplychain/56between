<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#bmsTransportGroupTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#bmsTransportGroupTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#bmsTransportGroupTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/bms/basic/bmsTransportGroup/data",
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
            field: 'transportGroupCode',
            title: '运输价格体系编码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'transportGroupName',
            title: '运输价格体系名称',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注信息',
            sortable: true
        }]
    });
    $('#bmsTransportGroupTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#bmsTransportGroupTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#copy').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#bmsTransportGroupTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/basic/bmsTransportGroup/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#bmsTransportGroupTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.alert(data.msg);
            }
        });
    });
}

function add() {
    jp.openBQDialog('新增运输价格体系', "${ctx}/bms/basic/bmsTransportGroup/form", '90%', '90%', $('#bmsTransportGroupTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openBQDialog('编辑运输价格体系', "${ctx}/bms/basic/bmsTransportGroup/form?id=" + id, '90%', '90%', $('#bmsTransportGroupTable'));
}

function view(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openBQDialog('查看运输价格体系', "${ctx}/bms/basic/bmsTransportGroup/form?id=" + id, '90%', '90%', $('#bmsTransportGroupTable'));
}

function copy() {
    $("#copyModal").modal();
    $("#copyForm input").val('');
    $("#copyForm select").val('');
}

function copySave() {
    var validate = bq.headerSubmitCheck("#copyForm");
    if (!validate.isSuccess) {
        jp.alert(validate.msg);
        return;
    }
    var orgId = $("#copyForm").find("#orgId").eq(0).val();
    jp.get("${ctx}/bms/basic/bmsTransportGroup/copy?id=" + getIdSelections() + "&orgId=" + orgId, function (data) {
        if (data.success) {
            $('#bmsTransportGroupTable').bootstrapTable('refresh');
            jp.success(data.msg);
            $("#copyModal").modal('hide');
        } else {
            jp.bqError(data.msg);
        }
    });
}

</script>