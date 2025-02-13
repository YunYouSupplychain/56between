<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    buildTransportPriceTable();
    buildTransportStepPriceTable();
    if ($('#transportGroupCode').val()) {
        loadTransportPriceTable();
    }
});

function doSubmit($table, $topIndex) {
    jp.loading();
    var validate = bq.headerSubmitCheck("#inputForm");
    if (!validate.isSuccess) {
        jp.bqError(validate.msg);
        return false;
    }
    var disabledObj = bq.openDisabled("#inputForm");
    var params = $('#inputForm').serialize();
    bq.closeDisabled(disabledObj);
    jp.post("${ctx}/bms/basic/bmsContract/associateTransportGroup", params, function (data) {
        if (data.success) {
            $table.bootstrapTable('refresh');
            jp.success(data.msg);
            jp.close($topIndex);//关闭dialog
        } else {
            jp.bqError(data.msg);
        }
    });
    return true;
}

function buildTransportPriceTable() {
    $('#bmsTransportPriceTable').bootstrapTable({
        cache: false,//是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.transportGroupCode = $("#transportGroupCode").val();
            searchParam.orgId = $("#orgId").val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function (row, $el) {
            $(".info").removeClass("info");
            $el.addClass("info");
            loadTransportStepPriceTable(row.id);
        },
        onLoadSuccess: function () {
            var rows = $("#bmsTransportPriceTable").bootstrapTable('getData');
            if (rows.length > 0) {
                $(".info").removeClass("info");
                $("#bmsTransportPriceTable > tbody").find("tr").eq(0).addClass("info");
                loadTransportStepPriceTable(rows[0].id);
            }
        },
        formatNoMatches: function () {
            return '-';
        },
        columns: [{
            field: 'carTypeCode',
            title: '车型',
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_CAR_TYPE'))}, value, "-");
            }
        }, {
            field: 'startPlaceCode',
            title: '起点编码'
        }, {
            field: 'startPlaceName',
            title: '起点名称'
        }, {
            field: 'endPlaceCode',
            title: '终点编码'
        }, {
            field: 'endPlaceName',
            title: '终点名称'
        }, {
            field: 'regionCode',
            title: '区域编码'
        }, {
            field: 'regionName',
            title: '区域名称'
        }, {
            field: 'price',
            title: '单价'
        }, {
            field: 'logisticsPoints',
            title: '物流点数'
        }, {
            field: 'isAccumulationMethod',
            title: '是否阶梯分段累加方式计算',
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }]
    });
}

function loadTransportPriceTable() {
    $("#bmsTransportPriceTable").bootstrapTable('refresh', {url: "${ctx}/bms/basic/bmsTransportGroup/transportPriceData"});
}

function buildTransportStepPriceTable() {
    $('#bmsTransportStepPriceTable').bootstrapTable({
        cache: false,//是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        formatNoMatches: function () {
            return '-';
        },
        columns: [{
            field: 'fm',
            title: '从(含当前值)'
        }, {
            field: 'to',
            title: '到(不含当前值)'
        }, {
            field: 'price',
            title: '单价'
        }]
    });
}

function loadTransportStepPriceTable(fkId) {
    $('#bmsTransportStepPriceTable').bootstrapTable('refresh', {url: "${ctx}/bms/basic/bmsTransportGroup/transportStepPriceData?fkId=" + fkId});
}

function transportGroupAfterSelect() {
    loadTransportPriceTable();
}

function jumpTransportGroup() {
    var transportGroupCode = $('#transportGroupCode').val();
    var orgId = $('#orgId').val();
    jp.openBQDialog("运输价格体系", "${ctx}/bms/basic/bmsTransportGroup/jump/form?transportGroupCode=" + transportGroupCode + "&orgId=" + orgId, "80%", "90%", $('#bmsTransportPriceTable'));
}
</script>