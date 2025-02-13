<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>交接单管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="application/javascript">
        $(document).ready(function () {
            $('#handoverTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
            $('#shipTimeFm').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
            $('#shipTimeTo').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
            initTable();
        });

        function initTable() {
            $('#banQinWmOutHandoverDetailTable').bootstrapTable({
                cache: false,// 是否使用缓存
                pagination: true,// 是否显示分页
                sidePagination: "server",// client客户端分页，server服务端分页
                url: "${ctx}/wms/outbound/banQinWmOutHandover/detailData",
                queryParams: function (params) {
                    var searchParam = {};
                    searchParam.handoverNo = $("#handoverNo").val();
                    searchParam.orgId = $("#orgId").val();
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                columns: [{
                    checkbox: true
                }, {
                    field: 'soNo',
                    title: '出库单号',
                    sortable: true
                }, {
                    field: 'trackingNo',
                    title: '快递单号',
                    sortable: true
                }, {
                    field: 'customerOrderNo',
                    title: '客户订单号',
                    sortable: true
                }, {
                    field: 'externalNo',
                    title: '外部单号',
                    sortable: true
                }, {
                    field: 'soType',
                    title: '出库单类型',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_TYPE'))}, value, "-");
                    }
                }, {
                    field: 'orderDate',
                    title: '订单时间',
                    sortable: true
                }, {
                    field: 'ownerCode',
                    title: '货主编码',
                    sortable: true
                }, {
                    field: 'ownerName',
                    title: '货主名称',
                    sortable: true
                }, {
                    field: 'skuCode',
                    title: '商品编码',
                    sortable: true
                }, {
                    field: 'skuName',
                    title: '商品名称',
                    sortable: true
                }, {
                    field: 'consignee',
                    title: '收货人',
                    sortable: true
                }, {
                    field: 'consigneeTel',
                    title: '收货人电话',
                    sortable: true
                }, {
                    field: 'consigneeAddress',
                    title: '收货人地址',
                    sortable: true
                }, {
                    field: 'consigneeZip',
                    title: '收货人邮编',
                    sortable: true
                }, {
                    field: 'consigneeArea',
                    title: '收货人区域',
                    sortable: true
                }, {
                    field: 'deliveryName',
                    title: '发货人',
                    sortable: true
                }, {
                    field: 'deliveryTel',
                    title: '发货人电话',
                    sortable: true
                }, {
                    field: 'deliveryAddress',
                    title: '发货人地址',
                    sortable: true
                }, {
                    field: 'deliveryZip',
                    title: '发货人邮编',
                    sortable: true
                }, {
                    field: 'deliveryArea',
                    title: '发货人区域',
                    sortable: true
                }, {
                    field: 'businessNo',
                    title: '商流订单号',
                    sortable: true
                }, {
                    field: 'chainNo',
                    title: '供应链订单号',
                    sortable: true
                }, {
                    field: 'taskNo',
                    title: '供应链任务号',
                    sortable: true
                }, {
                    field: 'allocId',
                    title: '分配明细ID',
                    sortable: true
                }, {
                    field: 'waveNo',
                    title: '波次单号',
                    sortable: true
                }, {
                    field: 'locCode',
                    title: '库位编码',
                    sortable: true
                }, {
                    field: 'traceId',
                    title: '跟踪号',
                    sortable: true
                }, {
                    field: 'qty',
                    title: '数量',
                    sortable: true
                }, {
                    field: 'toLoc',
                    title: '目标库位编码',
                    sortable: true
                }, {
                    field: 'toId',
                    title: '目标跟踪号',
                    sortable: true
                }, {
                    field: 'pickOp',
                    title: '拣货人',
                    sortable: true
                }, {
                    field: 'pickTime',
                    title: '拣货时间',
                    sortable: true
                }, {
                    field: 'checkOp',
                    title: '复核人',
                    sortable: true
                }, {
                    field: 'checkTime',
                    title: '复核时间',
                    sortable: true
                }, {
                    field: 'packOp',
                    title: '打包人',
                    sortable: true
                }, {
                    field: 'packTime',
                    title: '打包时间',
                    sortable: true
                }, {
                    field: 'orgName',
                    title: '仓库',
                    sortable: true
                }, {
                    field: 'caseNo',
                    title: '打包箱号',
                    sortable: true
                }, {
                    field: 'shipTime',
                    title: '发运时间',
                    sortable: true
                }]
            });
            $('#banQinWmOutHandoverDetailTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
                var length = getIdSelections().length;
                $('#remove').prop('disabled', !length);
            });
        }

        function getIdSelections() {
            return $.map($("#banQinWmOutHandoverDetailTable").bootstrapTable('getSelections'), function (row) {
                return row.id
            });
        }

        function deleteAll() {
            jp.confirm('确认要删除该记录吗？', function () {
                jp.loading();
                jp.get("${ctx}/wms/outbound/banQinWmOutHandover/delDetail?ids=" + getIdSelections(), function (data) {
                    if (data.success) {
                        $('#banQinWmOutHandoverDetailTable').bootstrapTable('refresh');
                        jp.success(data.msg);
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            })
        }
    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="banQinWmOutHandoverHeaderEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-10"><label class="pull-right">交接单号</label></td>
                <td class="width-15">
                    <form:input path="handoverNo" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">承运商编码</label></td>
                <td class="width-15">
                    <form:input path="carrierCode" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">承运商名称</label></td>
                <td class="width-15">
                    <form:input path="carrierName" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">交接人</label></td>
                <td class="width-15">
                    <form:input path="handoverOp" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">交接时间</label></td>
                <td class="width-15">
                    <div class='input-group form_datetime' id='handoverTime'>
                        <input name="handoverTime" class="form-control" readonly
                               value="<fmt:formatDate value="${banQinWmOutHandoverHeaderEntity.handoverTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        <span class="input-group-addon">
                        <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                    </div>
                </td>
                <td class="width-10"><label class="pull-right">发运时间从</label></td>
                <td class="width-15">
                    <div class='input-group form_datetime' id='shipTimeFm'>
                        <input name="shipTimeFm" class="form-control" readonly
                               value="<fmt:formatDate value="${banQinWmOutHandoverHeaderEntity.shipTimeFm}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </td>
                <td class="width-10"><label class="pull-right">发运时间到</label></td>
                <td class="width-15">
                    <div class='input-group form_datetime' id='shipTimeTo'>
                        <input name="shipTimeTo" class="form-control" readonly
                               value="<fmt:formatDate value="${banQinWmOutHandoverHeaderEntity.shipTimeTo}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</form:form>
<div class="tabs-container">
    <div class="tab-content">
        <div id="toolbar" style="width: 100%; padding: 5px 0;">
            <shiro:hasPermission name="outbound:banQinWmOutHandover:del">
                <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
            </shiro:hasPermission>
        </div>
        <!-- 交接单明细表格 -->
        <table id="banQinWmOutHandoverDetailTable" data-toolbar="#toolbar" class="text-nowrap"></table>
    </div>
</div>
</body>
</html>