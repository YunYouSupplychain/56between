<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>调度管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmDispatchPlanResult.js" %>
    <style>
        #tmDispatchPlanTable>thead>tr>th,
        #tmDispatchPlanTable>tbody>tr>td {
            border: 1px solid black;
            text-align: center;
            vertical-align: middle;
        }
        .myCheckbox {
            cursor: pointer;
            width: 16px;
            height: 16px;
            position: relative;
        }

        .scrollNav {
            position: fixed;
            background: white;
            left: 0;
            top: 0;
            z-index:999;
        }

        .remarks {
            width: 120px;
            margin: 0;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }

        .remarks + p:before {
            content: "";
            position: relative;
            top: -15px;
            left: 10px;
            width: 0;
            height: 0;
            display: block;
            border-left: 10px solid transparent;
            border-right: 10px solid transparent;
            border-bottom: 10px solid #f7f6e0;
        }

        .remarks + p {
            position: absolute;
            display: none;
            padding: 5px 0px;
            width: 120px;
            background: #f7f6e0;
            border-radius: 6px;
            word-wrap: break-word;
            word-break: break-all;
            white-space: normal;
        }
    </style>
</head>
<body>
<div class="hidden">
    <input id="baseOrgId" type="hidden" value="${entity.baseOrgId}">
    <input id="planNo" value="${entity.planNo}" type="hidden">
    <input id="orgId" value="${entity.orgId}" type="hidden">
</div>
<div id="toolbar" style="width: 100%; padding-left: 10px; padding-top: 10px;">
    <shiro:hasPermission name="tms:order:tmDispatchPlan:dispatchAll">
        <a class="btn btn-primary btn-sm" id="dispatchAll" onclick="dispatchAll()">批量调度</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="tms:order:tmDispatchPlan:editAll">
        <a class="btn btn-primary btn-sm" id="editAll" onclick="editAll()">批量修改</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="tms:order:tmDispatchPlan:audit">
        <a class="btn btn-primary btn-sm" id="audit" onclick="audit()">审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="tms:order:tmDispatchPlan:cancelAudit">
        <a class="btn btn-primary btn-sm" id="cancelAudit" onclick="cancelAudit()">取消审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="tms:order:tmDispatchPlan:queryPlanResult">
    <a class="btn btn-primary btn-sm" id="queryPlanResult" onclick="queryPlanResult()">需求调度结果查询</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="tms:order:tmDispatchPlan:queryPlanDetail">
        <a class="btn btn-primary btn-sm" id="queryPlanDetail" onclick="queryPlanDetail()">需求计划明细查询</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="tms:order:tmDispatchPlan:recall">
        <a class="btn btn-primary btn-sm" id="recall" disabled onclick="recall()">撤回</a>
    </shiro:hasPermission>
    <a style="font-size: 25px; color: #0d1318; font-weight: bold; padding-left: 200px;">调度计划单号：<span>${entity.planNo}</span></a>
</div>
<div style="padding-left: 10px; padding-top: 10px; padding-right: 10px;">
    <table id="tmDispatchPlanTable" class="table-hover text-nowrap" width="100%">
        <thead>
        <tr>
            <th rowspan="2" style="font-size: 18px;">车牌号</th>
            <th rowspan="2"><input id="selectAll" type="checkbox" class="myCheckbox" onclick="detailSelectChange(this.checked)"/></th>
            <th rowspan="2" style="font-size: 18px;">车次</th>
            <th rowspan="2" style="font-size: 18px;">操作</th>
            <th rowspan="1" colspan="4">第一仓</th>
            <th rowspan="1" colspan="4">第二仓</th>
            <th rowspan="1" colspan="4">第三仓</th>
            <th rowspan="1" colspan="4">第四仓</th>
            <th rowspan="1" colspan="2"></th>
            <th rowspan="2" style="font-size: 18px;width: 120px">备注</th>
        </tr>
        <tr>
            <th>提货</th>
            <th>送货</th>
            <th>油品</th>
            <th>数量</th>
            <th>提货</th>
            <th>送货</th>
            <th>油品</th>
            <th>数量</th>
            <th>提货</th>
            <th>送货</th>
            <th>油品</th>
            <th>数量</th>
            <th>提货</th>
            <th>送货</th>
            <th>油品</th>
            <th>数量</th>
            <th>驾驶员</th>
            <th>押运员</th>
        </tr>
        </thead>
        <tbody id="tbody"></tbody>
    </table>
</div>
</body>
</html>