<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#banQinCdTrackingInfoTable').bootstrapTable({
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
        url: "${ctx}/wms/basicdata/banQinCdTrackingInfo/data",
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
            field: 'description',
            title: '接口描述',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        },
        {
            field: 'type',
            title: '接口类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_TRACKING_INTERFACE_TYPE'))}, value, "-");
            }
        },
        {
            field: 'url',
            title: '接口地址',
            sortable: true
        },
        {
            field: 'params',
            title: '接口参数',
            sortable: true
        }]
    });

    if (navigator.userAgent.match(/(iPhone|iPod|Android|ios)/i)) {
        $('#banQinCdTrackingInfoTable').bootstrapTable("toggleView");
    }

    $('#banQinCdTrackingInfoTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' + 'check-all.bs.table uncheck-all.bs.table', function () {
        $('#remove').prop('disabled', !$('#banQinCdTrackingInfoTable').bootstrapTable('getSelections').length);
        $('#edit').prop('disabled', $('#banQinCdTrackingInfoTable').bootstrapTable('getSelections').length != 1);
    });

    $("#search").click("click", function () {
        $('#banQinCdTrackingInfoTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinCdTrackingInfoTable').bootstrapTable('refresh');
    });
});

function getIdSelections() {
    return $.map($("#banQinCdTrackingInfoTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该接口信息吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/basicdata/banQinCdTrackingInfo/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinCdTrackingInfoTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增快递接口信息', "${ctx}/wms/basicdata/banQinCdTrackingInfo/form", '650px', '320px', $('#banQinCdTrackingInfoTable'));
}

function edit(id) {
    if (id == undefined) {
        id = getIdSelections();
    }
    jp.openDialog('编辑快递接口信息', "${ctx}/wms/basicdata/banQinCdTrackingInfo/form?id=" + id, '650px', '320px', $('#banQinCdTrackingInfoTable'));
}

</script>