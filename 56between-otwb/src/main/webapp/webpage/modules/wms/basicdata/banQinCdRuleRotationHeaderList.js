<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#banQinCdRuleRotationHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/basicdata/banQinCdRuleRotationHeader/data",
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
            field: 'ruleCode',
            title: '规则编码',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        },{
            field: 'ruleName',
            title: '规则名称',
            sortable: true
        },{
            field: 'rotationType',
            title: '周转类型',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ROTATION_TYPE'))}, value, "-");
            }
        },{
            field: 'lotName',
            title: '批次属性',
            sortable: true
        }]
    });
    $('#banQinCdRuleRotationHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinCdRuleRotationHeaderTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinCdRuleRotationHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinCdRuleRotationHeaderTable').bootstrapTable('refresh');
    });
});

function getIdSelections() {
    return $.map($("#banQinCdRuleRotationHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该库存周转规则记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/basicdata/banQinCdRuleRotationHeader/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinCdRuleRotationHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    });
}

function add() {
    jp.openBQDialog('新增库存周转规则', "${ctx}/wms/basicdata/banQinCdRuleRotationHeader/form", '80%', '80%', $('#banQinCdRuleRotationHeaderTable'));
}

function edit(id) {
    if(id == undefined){
        id = getIdSelections();
    }
    <shiro:hasPermission name="basicdata:banQinCdRuleRotationHeader:edit">
        jp.openBQDialog('编辑库存周转规则', "${ctx}/wms/basicdata/banQinCdRuleRotationHeader/form?id=" + id,'80%', '80%', $('#banQinCdRuleRotationHeaderTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="basicdata:banQinCdRuleRotationHeader:edit">
        jp.openBQDialog('查看库存周转规则', "${ctx}/wms/basicdata/banQinCdRuleRotationHeader/form?id=" + id,'80%', '80%', $('#banQinCdRuleRotationHeaderTable'));
    </shiro:lacksPermission>
}

</script>