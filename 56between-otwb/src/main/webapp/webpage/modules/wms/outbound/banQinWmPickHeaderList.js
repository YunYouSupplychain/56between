<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#createDateFm').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#createDateTo').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinWmPickHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmPickHeaderTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#banQinWmPickHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/outbound/banQinWmPickHeader/data",
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
            field: 'pickNo',
            title: '拣货单号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'createBy.name',
            title: '创建人',
            sortable: true
        }, {
            field: 'createDate',
            title: '创建时间',
            sortable: true
        }, {
            field: 'updateBy.name',
            title: '修改人',
            sortable: true
        }, {
            field: 'updateDate',
            title: '修改时间',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });
    $('#banQinWmPickHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' + 'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinWmPickHeaderTable').bootstrapTable('getSelections').length;
        var cStyle = length ? "auto" : "none";
        var fColor = length ? "#1E1E1E" : "#8A8A8A";
        $('#remove').prop('disabled', !length);
        $('#printPickOrder').css({"pointer-events": cStyle, "color": fColor});
    });
}

function getIdSelections() {
    return $.map($("#banQinWmPickHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getPickNosSelections() {
    return $.map($("#banQinWmPickHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.pickNo
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmPickHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}

/**
 * 查看
 * @param id
 */
function view(id) {
    jp.openBQDialog('查看拣货单', "${ctx}/wms/outbound/banQinWmPickHeader/form?id=" + id, '80%', '90%', $('#banQinWmPickHeaderTable'));
}

/**
 * 修改
 */
function edit() {
    jp.openBQDialog('编辑拣货单', "${ctx}/wms/outbound/banQinWmPickHeader/form?id=" + getIdSelections(), '80%', '90%', $('#banQinWmPickHeaderTable'));
}

/**
 * 删除
 */
function deleteAll() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.confirm('确认要删除该拣货单记录吗？', function () {
        if (!jp.isExistDifOrg(getOrgIdSelections())) {
            jp.warning("不同平台数据不能批量操作");
            return;
        } 
        commonMethod("deleteAll");
    })
}

/**
 * 公共调用方法
 * @param method 方法名
 */
function commonMethod(method) {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/outbound/banQinWmPickHeader/" + method + "?pickNo=" + getPickNosSelections() + "&orgId=" + jp.getCurrentOrg().orgId, function (data) {
        if (data.success) {
            $('#banQinWmPickHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 打印拣货单
 */
function printPickOrder() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/outbound/banQinWmPickHeader/printPickOrder", 'ids', rowIds.join(','), '打印拣货单');
}
</script>