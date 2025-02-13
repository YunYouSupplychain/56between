<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>追加需求计划</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <script>
        $(document).ready(function () {
            lay('.laydate').each(function(){
                laydate.render({
                    elem: this, theme: '#393D49', type: 'datetime'
                });
            });

            $('#demandPlanTable').bootstrapTable({
                showRefresh: true,// 显示刷新按钮
                showColumns: true,// 显示内容列下拉框
                showExport: true,// 显示到处按钮
                cache: false,// 是否使用缓存
                pagination: true,// 是否显示分页
                sidePagination: "server",// client客户端分页，server服务端分页
                detailView: true,//显示详情按钮
                detailFormatter: "detailFormatter",
                queryParams: function (params) {
                    var searchParam = $("#queryForm").serializeJSON();
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                columns: [{
                    checkbox: true
                }, {
                    field: 'planOrderNo',
                    title: '需求计划单号',
                    sortable: true
                }, {
                    field: 'status',
                    title: '状态',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_DEMAND_PLAN_STATUS'))}, value, "-");
                    }
                }, {
                    field: 'orderTime',
                    title: '订单时间',
                    sortable: true
                }, {
                    field: 'ownerName',
                    title: '客户',
                    sortable: true
                }, {
                    field: 'arrivalTime',
                    title: '到达时间',
                    sortable: true
                }]
            });
        });

        function getNoSelections() {
            return $.map($('#demandPlanTable').bootstrapTable('getSelections'), function (row) {
                return row.planOrderNo
            });
        }

        function save(index) {
            var orderNos = getNoSelections();
            if (orderNos.length === 0) {
                jp.bqError("请选择一条记录");
                return;
            }
            jp.loading();
            jp.post("${ctx}/tms/order/tmDispatchPlan/addDemandPlanConfirm", {planNo: $('#planNo').val(), orgId: $('#orgId').val(), demandPlanNos: orderNos.join(',')}, function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    jp.close(index);
                } else {
                    jp.bqError(data.msg);
                }
            })
        }

        function queryDetail() {
            $("#demandPlanTable").bootstrapTable('refresh', {url:"${ctx}/tms/order/tmDemandPlan/grid"});
        }

        function resetForm() {
            $("#queryForm table input").val("");
        }

        function detailFormatter(index, row) {
            var htmltpl =  $("#detailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
            var html = Mustache.render(htmltpl, {
                idx:row.id
            });
            $.get("${ctx}/tms/order/tmDemandPlan/detail/data?planOrderNo=" + row.planOrderNo + "&orgId=" + row.orgId, function(data){
                var detailRowIdx = 0, detailRowTpl = $("#detailRowTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                var data1 = data;
                for (var i = 0; i<data1.length; i++){
                    addRow('#detail-' + row.id + '-List', detailRowIdx, detailRowTpl, data1[i]);
                    detailRowIdx = detailRowIdx + 1;
                }
            })
            return html;
        }

        function addRow(list, idx, tpl, row){
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
        }
    </script>
    <script type="text/template" id="detailTpl">//<!--
        <div style="padding-left: 60px; padding-right: 60px;">
            <table class="table table-condensed">
            <thead>
                <tr>
                    <th>商品编码</th>
                    <th>商品名称</th>
                    <th>数量</th>
                </tr>
            </thead>
            <tbody id="detail-{{idx}}-List"></tbody>
            </table>
		</div>//-->
    </script>
    <script type="text/template" id="detailRowTpl">//<!--
        <tr>
            <td>{{row.skuCode}}</td>
            <td>{{row.skuName}}</td>
            <td>{{row.qty}}</td>
        </tr>//-->
    </script>
</head>
<body>
<div class="hidden">
    <input id="owner" value="OWNER" type="hidden">
</div>
<div class="modal-body">
    <fieldset>
        <legend>查询条件</legend>
        <form id="queryForm">
            <input id="planNo" name="planNo" value="${entity.planNo}" type="hidden">
            <input id="orgId" name="orgId" value="${entity.orgId}" type="hidden">
            <input id="baseOrgId" name="baseOrgId" value="${entity.baseOrgId}" type="hidden">
            <input id="status" name="status" value="00" type="hidden">
            <table class="bq-table">
            <tr>
                <td><label class="pull-left">需求计划单号</label></td>
                <td><label class="pull-left">客户</label></td>
                <td><label class="pull-left">订单时间从</label></td>
                <td><label class="pull-left">订单时间到</label></td>
                <td><label class="pull-left">到达时间从</label></td>
                <td><label class="pull-left">到达时间到</label></td>
                <td></td>
            </tr>
            <tr>
                <td><input id="planOrderNo" name="planOrderNo" class="form-control" maxlength="32"/></td>
                <td>
                    <sys:grid title="选择客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                              fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ownerCode"
                              displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ownerName"
                              fieldLabels="客户编码|客户名称" fieldKeys="ownerCode|ownerName"
                              searchLabels="客户编码|客户名称" searchKeys="ownerCode|ownerName"
                              queryParams="orgId|transportObjType" queryParamValues="baseOrgId|owner"
                              cssClass="form-control"/>
                </td>
                <td><input id="orderTimeFm" name="orderTimeFm" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td><input id="orderTimeTo" name="orderTimeTo" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td><input id="arrivalTimeFm" name="arrivalTimeFm" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td><input id="arrivalTimeTo" name="arrivalTimeTo" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td>
                    <button type="button" class="btn btn-primary btn-sm" onclick="queryDetail()"> &nbsp;查询&nbsp; </button>
                    <button type="button" class="btn btn-danger btn-sm" onclick="resetForm()"> &nbsp;重置&nbsp; </button>
                </td>
            </tr>
        </table>
        </form>
    </fieldset>
</div>
<div style="padding-left: 15px; padding-right: 15px;">
    <table id="demandPlanTable" class="text-nowrap table-condensed"></table>
</div>
</body>
</html>