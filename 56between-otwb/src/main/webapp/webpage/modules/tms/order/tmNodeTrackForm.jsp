<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>物流节点跟踪</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <script type="text/javascript">
        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        $(document).ready(function () {
            init();
        });

        function init() {
            appendList(${fns:toJson(dataInfo.logisticsData)});
        }

        function datePicker(date) {
            return jp.dateFormat(new Date(date), "yyyy-MM-dd hh:mm:ss");
        }

        function appendList(logisticsData) {
            $('#nodeList').children().remove();
            if (logisticsData != null && logisticsData != undefined && logisticsData.length != undefined && logisticsData.length > 0) {
                for (var i = 0; i < logisticsData.length; i++) {
                    $('#nodeList').append('<li><div class="status-content-before">' + logisticsData[i].operation + '</div>' +
                        '<div class="status-time-before">' + datePicker(logisticsData[i].opTime) + '</div>' +
                        '<div class="status-line"></div></li>');
                    if (i == 0) {
                        $('#nodeList li').addClass("current");
                    }
                }
            } else {
                $('#nodeList').append('<li class="current"><div class="status-content-before">暂无物流信息</div></li>');
            }
        }

        function selectWayBillNo() {
            var wayBillNo = $('#wayBillNo').val();
            var l = jp.loading();
            jp.get("${ctx}/tms/order/tmTransportOrder/nodeTrackList?orderNo=" + $("#orderNo").val() + "&wayBillNo=" + wayBillNo, function (data) {
                appendList(data.body.logisticsData);
                jp.close(l);
            });
        }

        function add() {
            var auto = false;//是否使用响应式，使用百分比时，应设置为false
            top.layer.open({
                type: 2,
                area: ['90%', '80%'],
                title: '物流节点',
                auto: auto,
                maxmin: true, //开启最大化最小化按钮
                content: "${ctx}/tms/order/transport/track/formByTransport?transportNo=" + $("#orderNo").val()
                    + "&customerNo=" + $("#customerNo").val() + "&wayBillNo=" + $('#wayBillNo').val() + "&orgId=" + $("#orgId").val(),
                cancel: function (index) {
                    sessionStorage.clear();
                },
                end: function () {
                    selectWayBillNo();
                }
            });
        }
    </script>
    <style type="text/css">
        ul li {
            list-style: none;
        }

        .package-status {
            padding: 10px 0 0 0;
            width: 80%;
        }

        .package-status .status-list {
            margin: 0;
            padding: 0;
            list-style: none;
        }

        .package-status .status-list > li {
            border-left: 2px solid #0278D8;
            text-align: left;
            padding-left: 20px;
            height: auto;
            width: 85%;
            float: right;
        }

        .package-status .status-list > li:before {
            /* 流程点的样式 */
            content: '▲';
            color: #0278D8;
            font-size: 21px;
            /*border: 5px solid #0278D8;*/
            /*background-color: #0278D8;*/
            display: inline-block;
            width: 6px;
            height: 6px;
            border-radius: 10px;
            margin-left: -32px;
            margin-right: 10px
        }

        .package-status .status-box {
            overflow: hidden;
            position: relative;
        }

        .package-status .status-box:before {
            content: " ";
            background-color: #f3f3f3;
            display: block;
            position: absolute;
            top: -8px;
            left: 20px;
            width: 10px;
            height: 4px
        }

        .status-list > li:not(:first-child) {
            padding-top: 10px;
        }

        .status-content-before {
            text-align: left;
            margin-left: 25px;
            margin-top: -20px;
        }

        .status-time-before {
            text-align: left;
            margin-left: 25px;
            font-size: 10px;
            margin-top: 5px;
        }

        .current {
            color: #0278D8;
        }

        .status-line {
            border-bottom: 1px solid #ccc;
            margin-left: 25px;
            margin-top: 10px;
        }

        .table-div {
            margin-bottom: 20px;
            border: 1px solid #ccc;
        }
    </style>
</head>
<body>
<div style="width: 100%;">
    <div class="modal-body" style="width: 100%;">
        <div class="table-div">
            <table class="table table-condensed" style="margin-top: 15px; margin-bottom: 15px;">
                <tbody>
                <tr>
                    <td class="width-20"><label class="pull-right">客户订单号：</label></td>
                    <td class="width-50">
                        <label>${dataInfo.customerNo}</label>
                        <input type="hidden" id="customerNo" value="${dataInfo.customerNo}">
                        <input type="hidden" id="orderNo" value="${dataInfo.orderNo}">
                        <input type="hidden" id="orgId" value="${dataInfo.orgId}">
                    </td>
                    <td class="width-30">
                        <button id="add" class="btn btn-primary" onclick="add()">新增路由</button>
                    </td>
                </tr>
                <tr>
                    <td class="width-20"><label class="pull-right">客户：</label></td>
                    <td class="width-50">
                        <label>${dataInfo.customerName}</label>
                    </td>
                    <td class="width-30"></td>
                </tr>
                <tr>
                    <td class="width-20"><label class="pull-right">物流单号：</label></td>
                    <td class="width-50">
                        <select id="wayBillNo" class="form-control" onchange="selectWayBillNo()">
                            <c:forEach items="${dataInfo.wayBillNo.split(',')}" var="dict">
                                <option value="${dict}">${dict}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td class="width-30"></td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="package-status">
            <div class="status-box">
                <ul id="nodeList" class="status-list"></ul>
            </div>
        </div>
    </div>
</body>
</html>