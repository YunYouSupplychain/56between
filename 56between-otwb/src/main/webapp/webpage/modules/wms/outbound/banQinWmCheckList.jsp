<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>复核管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmCheckList.js" %>
    <style>
        .myRadio {cursor: pointer; width: 16px; height: 16px; position: relative;}
        .labelClass{ font-size: medium; }
    </style>
</head>
<body>
<!-- 工具栏 -->
<div id="toolbar" style="width: 100%; padding-left: 10px; padding-top: 10px;">
    <shiro:hasPermission name="outbound:banQinWmCheck:query">
        <button id="query" class="btn btn-primary" onclick="query()">查询</button>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmCheck:reset">
        <button id="reset" class="btn btn-primary" onclick="reset()">重置</button>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmCheck:checkConfirm">
        <button id="checkConfirm" class="btn btn-primary" disabled onclick="checkConfirmHandler()">复核确认</button>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmCheck:cancelCheck">
        <button id="cancelCheck" class="btn btn-primary" disabled onclick="cancelCheckHandler()">取消复核</button>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmCheck:batchCheck">
        <button id="batchCheck" class="btn btn-primary" disabled onclick="batchCheckHandler()">批量复核</button>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmCheck:getSoSerial">
        <button id="getSoSerial" class="btn btn-primary" onclick="getSoSerial()">查询出库序列号</button>
    </shiro:hasPermission>
</div>
<!-- 搜索 -->
<div class="tabs-container">
    <div class="tab-content" style="padding-bottom: 10px;">
        <form id="searchForm" class="form">
            <input id="uomQty_editor" type="hidden"/>
            <input id="packCode_editor" type="hidden"/>
            <input id="isPrintContainer" type="hidden">
            <table class="bq-table">
                <tbody>
                <tr>
                    <td><label class="pull-left">
                        <div class="radio-inline"><label class="radio"><input id="soRadio" type="radio" name="group" class="myRadio" onchange="clickRadioButton()">出库单号</label></div>
                    </label></td>
                    <td><label class="pull-left">
                        <div class="radio-inline"><label class="radio"><input id="boxRadio" type="radio" name="group" class="myRadio" onchange="clickRadioButton()">箱号</label></div>
                    </label></td>
                    <td><label class="pull-left">快递单号</label></td>
                    <td><label class="pull-left">是否更改快递单号</label></td>
                    <td><label class="pull-left">是否单件扫描</label></td>
                    <td><label class="pull-left">是否扣数</label></td>
                </tr>
                <tr>
                    <td><input id="soNo_query" class="form-control" maxlength="32"></td>
                    <td><input id="toId_query" class="form-control" maxlength="32" readonly></td>
                    <td><input id="trackingNo_editor" class="form-control" maxlength="32" readonly></td>
                    <td><input id="isTrack" type="checkbox" class="myCheckbox" onchange="isTrackChange()"></td>
                    <td><input id="isSingle" type="checkbox" class="myCheckbox" onchange="isSingleScan()"></td>
                    <td><input id="isSubtract" type="checkbox" class="myCheckbox"></td>
                </tr>
                <tr>
                    <td><label class="pull-left">商品</label></td>
                    <td><label class="pull-left">序列号</label></td>
                    <td><label class="pull-left">包装单位</label></td>
                    <td><label class="pull-left">数量</label></td>
                    <td><label class="pull-left">数量EA</label></td>
                    <td><label class="pull-left">需扫描数</label></td>
                    <td><label class="pull-left">未扫描数</label></td>
                </tr>
                <tr>
                    <td><input id="skuCode_editor" class="form-control" maxlength="32"></td>
                    <td><input id="serialNo_editor" class="form-control" maxlength="32" readonly></td>
                    <td>
                        <select id="uom_editor" class="form-control m-b required" onchange="changeUom()">
                            <option value=""></option>
                            <c:forEach items="${fns:getDictList('SYS_WM_WARE_UNIT')}" var="dict">
                                <option value="${dict.value}">${dict.label}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td><input id="qty_editor" class="form-control" onkeyup="bq.numberValidator(this, 0, 0)" oninput="changeQty()"></td>
                    <td><input id="qtyEa_editor" class="form-control" readonly></td>
                    <td><input id="qtySkuPickEa_editor" class="form-control" readonly></td>
                    <td><input id="qtySkuNotScanEa_editor" class="form-control" readonly></td>
                </tr>
                </tbody>
            </table>
        </form>
    </div>
</div>
<div class="tabs-container">
    <table id="banQinWmCheckTable" class="text-nowrap"></table>
    <div id="footer" style="border: 1px solid #dddddd; padding-bottom: 10px; margin-top: 5px;">
        <table class="bq-table">
            <tbody>
            <tr>
                <td><label class="pull-left labelClass">商品数</label></td>
                <td><label class="pull-left labelClass">需扫描数EA</label></td>
                <td><label class="pull-left labelClass">已扫描数EA</label></td>
                <td><label class="pull-left labelClass">未扫描数EA</label></td>
                <td><label class="pull-left labelClass">总重量</label></td>
                <td><label class="pull-left labelClass">总体积</label></td>
            </tr>
            <tr>
                <td><input id="qtySku_editor" class="form-control" readonly></td>
                <td><input id="qtyAllEa_editor" class="form-control" readonly></td>
                <td><input id="qtyAllScanEa_editor" class="form-control" readonly></td>
                <td><input id="qtyAllNotScanEa_editor" class="form-control" readonly></td>
                <td><input id="grossWeight_editor" class="form-control" readonly></td>
                <td><input id="cubic_editor" class="form-control" readonly></td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<!-- 出库序列号查询pop -->
<div class="modal fade" id="soSerialListModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 1200px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">出库序列号</h4>
            </div>
            <div class="modal-body">
                <table id="soSerialTable" class="text-nowrap table-condensed"></table>
            </div>
        </div>
    </div>
</div>
</body>
</html>