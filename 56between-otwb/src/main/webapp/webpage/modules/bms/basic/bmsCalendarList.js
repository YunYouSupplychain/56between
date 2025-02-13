<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#fmDate').datetimepicker({format: "YYYY-MM-DD"});
    $('#toDate').datetimepicker({format: "YYYY-MM-DD"});

    initTable();

    $("#btnImport").click(function () {
        jp.open({
            type: 1,
            area: [500, 300],
            title: "导入数据",
            content: $("#importBox").html(),
            btn: ['下载模板', '确定', '关闭'],
            btn1: function (index, layero) {
                bq.exportExcel("${ctx}/bms/basic/calendar/import/template", "导入模板", $('#searchForm'));
            },
            btn2: function (index, layero) {
                var inputForm = top.$("#importForm");
                var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe
                inputForm.attr("target", top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
                jp.loading('  正在导入，请稍等...');
                inputForm.submit();
                jp.close(index);
            },
            btn3: function (index) {
                jp.close(index);
            }
        });
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#bmsCalendarTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#bmsCalendarTable').bootstrapTable('refresh');
    });
});

function initTable() {
    var $table = $('#bmsCalendarTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/bms/basic/calendar/data",
        //查询参数,每次调用是会带上这个参数，可自定义
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
            field: 'date',
            title: '日期',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'type',
            title: '类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_CALENDAR_TYPE'))}, value, "-");
            }
        }, {
            field: 'coefficient',
            title: '日历系数',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注信息',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#bmsCalendarTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/basic/calendar/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#bmsCalendarTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.alert(data.msg);
            }
        });
    });
}

function add() {
    jp.openDialog('新增日历', "${ctx}/bms/basic/calendar/form", '600px', '300px', $('#bmsCalendarTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
    if (id == undefined) {
        id = getIdSelections();
    }
<shiro:hasPermission name="bms:calendar:edit">
    jp.openDialog('编辑日历', "${ctx}/bms/basic/calendar/form?id=" + id, '600px', '300px', $('#bmsCalendarTable'));
</shiro:hasPermission>
<shiro:lacksPermission name="bms:calendar:view">
    jp.openDialogView('查看日历', "${ctx}/bms/basic/calendar/form?id=" + id, '600px', '300px', $('#bmsCalendarTable'));
</shiro:lacksPermission>
}

</script>