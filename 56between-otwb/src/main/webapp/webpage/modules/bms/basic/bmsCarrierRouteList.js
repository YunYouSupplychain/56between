<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    initTable();

    $("#btnImport").click(function () {
        jp.open({
            type: 1,
            area: [500, 300],
            title: "导入数据",
            content: $("#importBox").html(),
            btn: ['下载模板', '确定', '关闭'],
            btn1: function (index, layero) {
                window.location = '${ctx}/bms/basic/bmsCarrierRoute/import/template';
            },
            btn2: function (index, layero) {
                var inputForm = top.$("#importForm");
                inputForm.find("input[name='orgId']").val(jp.getCurrentOrg().orgId);
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
        $('#sysRouteTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#sysRouteTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#sysRouteTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/bms/basic/bmsCarrierRoute/data",
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
            field: 'routeCode',
            title: '路由编码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'routeName',
            title: '路由名称',
            sortable: true
        }, {
            field: 'carrierName',
            title: '承运商',
            sortable: true
        }, {
            field: 'startAreaName',
            title: '起始地',
            sortable: true
        }, {
            field: 'endAreaName',
            title: '目的地',
            sortable: true
        }, {
            field: 'mileage',
            title: '标准里程',
            sortable: true
        }, {
            field: 'timeliness',
            title: '标准时效',
            sortable: true
        }]
    });
    $('#sysRouteTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#sysRouteTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#sysRouteTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该路由记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/basic/bmsCarrierRoute/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#sysRouteTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增路由', "${ctx}/bms/basic/bmsCarrierRoute/form", '800px', '320px', $('#sysRouteTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openDialog('编辑路由', "${ctx}/bms/basic/bmsCarrierRoute/form?id=" + id, '800px', '320px', $('#sysRouteTable'));
}

</script>