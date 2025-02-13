<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#dispatchTimeFm").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#dispatchTimeTo").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#createDateFm").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#createDateTo").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#baseOrgId").val(tmOrg.id);
    initTable();

    $("#search").click("click", function () {
        $("#tmDispatchPlanTable").bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $("#baseOrgId").val(tmOrg.id);
    });
});

function initTable() {
    $('#tmDispatchPlanTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/order/tmDispatchPlan/data",
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
            field: 'planNo',
            title: '调度计划单号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:goToDispatchResult(\"" + row.planNo + "\", \"" + row.orgId + "\")'>" + value + "</a>";
            }
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_DISPATCH_PLAN_STATUS'))}, value, "-");
            }
        }, {
            field: 'dispatchTime',
            title: '调度时间',
            sortable: true
        }, {
            field: 'carrierName',
            title: '承运商',
            sortable: true
        }, {
            field: 'dispatchOutletName',
            title: '派车网点',
            sortable: true
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
            title: '更新人',
            sortable: true
        }, {
            field: 'updateDate',
            title: '更新时间',
            sortable: true
        }]
    });
    $('#tmDispatchPlanTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' + 'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#tmDispatchPlanTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#addDemandPlan').prop('disabled', !length);
        $('#addVehicle').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($('#tmDispatchPlanTable').bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getSelections() {
    return $.map($('#tmDispatchPlanTable').bootstrapTable('getSelections'), function (row) {
        return row
    });
}

function deleteAll() {
    jp.confirm('确认要删除该调度计划信息记录吗？', function () {
        jp.loading();
        jp.post("${ctx}/tms/order/tmDispatchPlan/deleteAll", {ids: getIdSelections().join(',')}, function (data) {
            if (data.success) {
                $("#tmDispatchPlanTable").bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        });
    });
}

function createInitPlan() {
    $('#createModal').modal();
    $('#carrierCode').val('80000');
    $('#carrierName').val('百发物流');
    $('#dispatchOutletCode').val('DDS');
    $('#dispatchOutletName').val('默认提货网点');
    $('#dispatchTime').val(jp.dateFormat(new Date(), 'YYYY-MM-dd hh:mm:ss'));
    $('#demandBeginTime').val('');
    $('#demandEndTime').val('');
    var dispatchTime = laydate.render({elem: '#dispatchTime', theme: '#393D49', type: 'datetime'});
    var demandBeginTime = laydate.render({
        elem: '#demandBeginTime', theme: '#393D49', type: 'datetime',
        ready: function (date) {
            lay.extend(demandBeginTime.config.dateTime, {hours: '00', minutes: '00', seconds: '00'});
        }
    });
    var demandEndTime = laydate.render({
        elem: '#demandEndTime', theme: '#393D49', type: 'datetime',
        ready: function (date) {
            lay.extend(demandEndTime.config.dateTime, {hours: '23', minutes: '59', seconds: '59'});
        }
    });
}

function createConfirm() {
    var carrierCode = $('#carrierCode').val();
    if (!carrierCode) {
        jp.bqError('承运商不能为空');
        return;
    }
    var dispatchOutletCode = $('#dispatchOutletCode').val();
    if (!dispatchOutletCode) {
        jp.bqError('派车网点不能为空');
        return;
    }
    var dispatchTime = $('#dispatchTime').val();
    if (!dispatchTime) {
        jp.bqError('调度时间不能为空');
        return;
    }
    var demandBeginTime = $('#demandBeginTime').val();
    if (!demandBeginTime) {
        jp.bqError('需求开始时间不能为空');
    }
    var demandEndTime = $('#demandEndTime').val();
    if (!demandEndTime) {
        jp.bqError('需求结束时间不能为空');
    }
    jp.loading();
    var params = "carrierCode=" + carrierCode + "&dispatchOutletCode=" + dispatchOutletCode + "&dispatchTime=" + dispatchTime +
        "&demandBeginTime=" + demandBeginTime + "&demandEndTime=" + demandEndTime + "&orgId=" + jp.getCurrentOrg().orgId + "&baseOrgId=" + $('#baseOrgId').val();
    jp.post("${ctx}/tms/order/tmDispatchPlan/createInitPlan?" + params, null, function (data) {
        if (data.success) {
            jp.success(data.msg);
            $('#tmDispatchPlanTable').bootstrapTable('refresh');
            $('#createModal').modal('hide');
        } else {
            jp.bqError(data.msg);
        }
    })
}

function goToDispatchResult(planNo, orgId) {
    jp.openTab("${ctx}/tms/order/tmDispatchPlan/result?planNo=" + planNo + "&orgId=" + orgId + "&baseOrgId=" + $('#baseOrgId').val(), "调度结果", false);
}

function addDemandPlan() {
    var row = getSelections();
    var params = "planNo=" + row[0].planNo + "&orgId=" + row[0].orgId + "&baseOrgId=" + row[0].baseOrgId;
    top.layer.open({
        type: 2,
        area: ["85%", "70%"],
        title: "追加需求计划",
        maxmin: false,
        content: "${ctx}/tms/order/tmDispatchPlan/addDemandPlan?" + params,
        btn: ['确认', '关闭'],
        yes: function (index, layero) {
            var iframeWin = layero.find('iframe')[0];
            iframeWin.contentWindow.save(index);
        },
        cancel: function (index) {
        },
        end: function () {
            $("#tmDispatchPlanTable").bootstrapTable('refresh');
        }
    });
}

function addVehicle() {
    var row = getSelections();
    var params = "planNo=" + row[0].planNo + "&orgId=" + row[0].orgId + "&baseOrgId=" + row[0].baseOrgId;
    top.layer.open({
        type: 2,
        area: ["40%", "60%"],
        title: "追加车辆",
        maxmin: false,
        content: "${ctx}/tms/order/tmDispatchPlan/addVehicle?" + params,
        btn: ['确认', '关闭'],
        yes: function (index, layero) {
            var iframeWin = layero.find('iframe')[0];
            iframeWin.contentWindow.save(index);
        },
        cancel: function (index) {
        },
        end: function () {
            $("#tmDispatchPlanTable").bootstrapTable('refresh');
        }
    });
}

</script>