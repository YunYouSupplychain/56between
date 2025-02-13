<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>入库序列号管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmAsnSerialList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div id="toolbar" style="width: 100%; padding: 5px">
            <shiro:hasPermission name="inbound:banQinWmAsnSerial:query">
                <button id="query" class="btn btn-primary" onclick="query()">查询</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="inbound:banQinWmAsnSerial:reset">
                <button id="rest" class="btn btn-primary" onclick="reset()">重置</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="inbound:banQinWmAsnSerial:focusLock">
                <button id="focusLock" class="btn btn-primary" onclick="focusLock()">锁定序列号</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="inbound:banQinWmAsnSerial:focusUnLock">
                <button id="focusUnLock" class="btn btn-primary" onclick="focusUnLock()">解锁序列号</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="inbound:banQinWmAsnSerial:serialReceiving">
                <button id="receiveConfirm" class="btn btn-primary" onclick="receiveConfirm()">收货确认</button>
            </shiro:hasPermission>
            <shiro:hasPermission name="inbound:banQinWmAsnSerial:changeOrder">
                <button id="changeOrder" class="btn btn-primary" onclick="changeOrder()">换单</button>
            </shiro:hasPermission>
        </div>
        <div class="panel-body">
            <form id="searchForm" class="form">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">入库单号</label></td>
                        <td class="width-15">
                            <input id="asnNo" name="asnNo" htmlEscape="false" class="form-control" maxlength="32">
                        </td>
                        <td class="width-10"><label class="pull-right">商品</label></td>
                        <td class="width-15">
                            <input id="skuCode" name="skuCode" htmlEscape="false" class="form-control" maxlength="32">
                        </td>
                        <td class="width-10"><label class="pull-right">跟踪号</label></td>
                        <td class="width-15">
                            <input id="traceId" name="traceId" htmlEscape="false" class="form-control" maxlength="32">
                        </td>
                        <td class="width-10"><label class="pull-right">序列号</label></td>
                        <td class="width-15">
                            <input id="serialNo" name="serialNo" htmlEscape="false" class="form-control" maxlength="32">
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">是否扣数</label></td>
                        <td class="width-15">
                            <input id="isSubtract" name="isSubtract" type="checkbox" class="myCheckbox">
                        </td>
                        <td class="width-10"><label class="pull-right">收货明细行号</label></td>
                        <td class="width-15">
                            <input id="editor_lineNo" name="lineNo" htmlEscape="false" maxlength="32" class="form-control" readonly>
                        </td>
                        <td class="width-10"><label class="pull-right">预收数EA</label></td>
                        <td class="width-15">
                            <input id="editor_qtyPlanEa" name="qtyPlanEa" htmlEscape="false" class="form-control" readonly>
                        </td>
                        <td class="width-10"><label class="pull-right">已扫描数</label></td>
                        <td class="width-15">
                            <input id="editor_qtyScanned" name="qtyScanned" htmlEscape="false" class="form-control" readonly>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">收货跟踪号</label></td>
                        <td class="width-15">
                            <input id="editor_toId" name="toId" htmlEscape="false" class="form-control" maxlength="32">
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">收货库位</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择收货库位" cssClass="form-control required"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="editor_toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                           selectButtonId="toLocSelectId" deleteButtonId="toLocDeleteId"
                                           fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                           searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                            </sys:popSelect>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <table id="lotAttTab" class="table text-nowrap"></table>
            </form>
        </div>
        <div class="panel-body">
            <div id="left" style="width: 44%; float: left;">
                <div style="background-color: #2c3e50; width: 100%; height: 35px; color: white; line-height: 35px;">&nbsp;&nbsp;序列号</div>
                <table id="banQinWmAsnSerialTable" class="table text-nowrap"></table>
            </div>
            <div id="right" style="width: 56%; float: right; padding-left: 10px; padding-right: 10px;">
                <div style="background-color: #2c3e50; width: 100%; height: 35px; color: white; line-height: 35px;">&nbsp;&nbsp;收货明细</div>
                <table id="banQinWmAsnReceiveTable" class="table text-nowrap"></table>
            </div>
        </div>
    </div>
</div>
</body>
</html>