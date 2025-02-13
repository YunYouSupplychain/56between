<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    if (!$("#id").val()) {
        $("#orgId").val(jp.getCurrentOrg().orgId);
        $("#orgName").val(jp.getCurrentOrg().orgName);
    } else {
        $("#addDetail").attr('disabled', false);
        $("#importPrice").attr('disabled', false);
        $("#exportPrice").attr('disabled', false);
    }

    buildTransportPriceTable();
    buildTransportStepPriceTable();
    bindEvent();
});

function bindEvent() {
    $("#importPrice").click(function () {
        $("#uploadModal").modal();
        $("#uploadFile").val('');
    });
    $("#exportPrice").click(function () {
        var searchParams = {"fkId": $('#id').val(), "orgId": $('#orgId').val()};
        bq.exportExcelNew("${ctx}/bms/basic/bmsTransportGroup/exportPrice", $('#transportGroupName').val(), searchParams);
    });
}

function downloadTemplate() {
    bq.exportExcelNew('${ctx}/bms/basic/bmsTransportGroup/import/template', "价格导入", {});
}

function uploadFile() {
    jp.loading("正在导入，请稍等...");
    var formData = new FormData();
    formData.append("file", $("#uploadFile")[0].files[0]);
    formData.append("id", $('#id').val());
    setTimeout(function () {
        $.ajax({
            url: "${ctx}/bms/basic/bmsTransportGroup/import/transportPrice",
            type: 'POST',
            async: false,
            data: formData,
            processData: false,
            contentType: false,
            success: function (data) {
                if (data.success) {
                    $("#uploadModal").modal('hide');  //手动关闭
                    $('#bmsTransportPriceTable').bootstrapTable('refresh', {url: "${ctx}/bms/basic/bmsTransportGroup/transportPriceData"});
                    jp.success(data.msg);
                } else {
                    jp.alert(data.msg);
                }
            },
            error: function () {
                jp.error("导入异常");
            }
        });
    }, 100);
}

function save() {
    jp.loading();
    var validate = bq.headerSubmitCheck("#inputForm");
    if (!validate.isSuccess) {
        jp.bqError(validate.msg);
        return;
    }
    jp.post("${ctx}/bms/basic/bmsTransportGroup/save", $('#inputForm').serialize(), function (data) {
        if (data.success) {
            window.location = "${ctx}/bms/basic/bmsTransportGroup/form?id=" + data.body.id;
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

function buildTransportPriceTable() {
    var $bmsTransportPriceTable = $('#bmsTransportPriceTable');
    $bmsTransportPriceTable.bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/bms/basic/bmsTransportGroup/transportPriceData",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.transportGroupCode = $("#transportGroupCode").val();
            searchParam.orgId = $("#orgId").val();
            searchParam.startPlaceCode = $("#startPlace").val();
            searchParam.endPlaceCode = $("#endPlace").val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function (row, $el) {
            $(".info").removeClass("info");
            $el.addClass("info");
            loadTransportStepPriceTable(row.id);
            $bmsTransportPriceTable.bootstrapTable('uncheckAll');
            $bmsTransportPriceTable.bootstrapTable("checkBy", {field: 'id', values: [row.id]});
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
            checkbox: true
        }, {
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
    }).on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var selections = $('#bmsTransportPriceTable').bootstrapTable('getSelections');
        $('#editDetail').attr('disabled', selections.length !== 1);
        $('#delDetail').attr('disabled', !selections.length);
    });
}

function getDetailIdSelections() {
    return $.map($("#bmsTransportPriceTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function addDetail() {
    jp.openDialog("新增运输价格", "${ctx}/bms/basic/bmsTransportGroup/transportPriceForm?fkId=" + $("#id").val() + "&orgId=" + $("#orgId").val(), "80%", "80%", $("#bmsTransportPriceTable"));
}

function editDetail(id) {
    if (id === undefined) {
        id = getDetailIdSelections();
    }
    jp.openDialog("编辑运输价格", "${ctx}/bms/basic/bmsTransportGroup/transportPriceForm?id=" + id + "&fkId=" + $("#id").val() + "&orgId=" + $("#orgId").val(), "80%", "80%", $("#bmsTransportPriceTable"));
}

function delDetail(ids) {
    jp.confirm('确认要删除吗？', function () {
        jp.loading();
        if (ids === undefined) {
            ids = getDetailIdSelections();
        }
        jp.post("${ctx}/bms/basic/bmsTransportGroup/deleteTransportPrice?ids=" + ids, {}, function (data) {
            if (data.success) {
                $("#bmsTransportPriceTable").bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        });
    });
}

function buildTransportStepPriceTable() {
    $('#bmsTransportStepPriceTable').bootstrapTable({
        cache: false,// 是否使用缓存
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

function routeSearch(e) {
    if (e.keyCode === 13 || e.which === 13) {
        $('#bmsTransportPriceTable').bootstrapTable('refresh');
    }
}

</script>