<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#banQinCdWhPackageTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/basicdata/banQinCdWhPackage/data",
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
            field: 'cdpaCode',
            title: '包装代码',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'cdpaType',
            title: '包装类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_PACKAGE_TYPE'))}, value, "-");
            }
        }, {
            field: 'cdpaFormat',
            title: '包装规格',
            sortable: true
        }, {
            field: 'cdpaIsUse',
            title: '是否启用',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_IS_STOP'))}, value, "-");
            }
        }]
    });
    $('#banQinCdWhPackageTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinCdWhPackageTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinCdWhPackageTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinCdWhPackageTable').bootstrapTable('refresh');
    });
});

function getIdSelections() {
    return $.map($("#banQinCdWhPackageTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该包装规格记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/basicdata/banQinCdWhPackage/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinCdWhPackageTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增包装规格', "${ctx}/wms/basicdata/banQinCdWhPackage/form", '80%', '80%', $('#banQinCdWhPackageTable'));
}

function edit(id) {
    if (id == undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="basicdata:banQinCdWhPackage:edit">
        jp.openDialog('编辑包装规格', "${ctx}/wms/basicdata/banQinCdWhPackage/form?id=" + id,'80%', '80%', $('#banQinCdWhPackageTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="basicdata:banQinCdWhPackage:edit">
        jp.openDialogView('查看包装规格', "${ctx}/wms/basicdata/banQinCdWhPackage/form?id=" + id,'80%', '80%', $('#banQinCdWhPackageTable'));
    </shiro:lacksPermission>
}

</script>