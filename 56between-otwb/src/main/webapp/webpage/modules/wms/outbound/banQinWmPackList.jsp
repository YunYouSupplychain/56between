<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>打包管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmPackList.js" %>
    <style>
        .myRadio {
            cursor: pointer;
            width: 16px;
            height: 16px;
            position: relative;
        }

        .myCheckboxO {
            cursor: pointer;
            width: 18px;
            height: 19px;
            position: relative;
        }
    </style>
</head>
<body>
<div style="width: 100%">
    <div id="left" style="width: 50%; float: left">
        <!-- 工具栏 -->
        <div id="toolbar_left" style="width: 100%; padding-left: 10px; padding-top: 10px;">
            <button id="query" class="btn btn-primary" onclick="query()">查询</button>
            <button id="reset" class="btn btn-primary" onclick="reset()">重置</button>
            <button id="batchCheck" class="btn btn-primary" disabled onclick="batchPackHandler()">批量打包</button>
        </div>
        <!-- 搜索 -->
        <div>
            <form id="searchForm_left" class="form form-horizontal well" style="padding-top: 5px; height: 250px; margin-bottom: 5px;">
                <input id="uomQty_fmEditor" type="hidden">
                <input id="uomDesc_fmEditor" type="hidden">
                <input id="packCode_fmEditor" type="hidden">
                <table class="bq-table">
                    <tbody>
                    <tr>
                        <td><label class="pull-left">
                            <div class="radio-inline"><label class="radio"><input id="soRadio" type="radio" name="group" class="myRadio" onchange="clickRadioButton()">出库单号:</label></div>
                        </label></td>
                        <td><label class="pull-left">
                            <div class="radio-inline"><label class="radio"><input id="boxRadio" type="radio" name="group" class="myRadio" onchange="clickRadioButton()">原箱号:</label></div>
                        </label></td>
                        <td><label class="pull-left">扫描方式:</label></td>
                        <td><label class="pull-left">数量:</label></td>
                    </tr>
                    <tr>
                        <td><input id="soNo_query" class="form-control" maxlength="32" autocomplete="off"></td>
                        <td><input id="toId_query" class="form-control" maxlength="32" autocomplete="off"></td>
                        <td>
                            <div class="form-group" style="padding-left: 14px; line-height: 25px; height: 25px;">
                                <label class="checkbox-inline"><input id="isSingle" type="checkbox" class="myCheckboxO" onchange="isSingleScan()">单件</label>
                                <label class="checkbox-inline"><input id="isSingleCs" type="checkbox" class="myCheckboxO" onchange="isSingleCsScan()">整箱</label>
                            </div>
                        </td>
                        <td><input id="qty_fmEditor" class="form-control" onkeyup="bq.numberValidator(this, 0, 0)" oninput="changeQty()" autocomplete="off"></td>
                    </tr>
                    <tr>
                        <td><label class="pull-left">扫描商品:</label></td>
                        <td><label class="pull-left">序列号:</label></td>
                        <td><label class="pull-left">包装单位:</label></td>
                        <td><label class="pull-left">数量EA:</label></td>
                    </tr>
                    <tr>
                        <td><input id="skuCode_fmEditor" class="form-control" maxlength="32" autocomplete="off"></td>
                        <td><input id="serialNo_fmEditor" class="form-control" maxlength="32" readonly></td>
                        <td>
                            <select id="uom_fmEditor" class="form-control m-b required" onchange="changeUom()">
                                <option value=""></option>
                                <c:forEach items="${fns:getDictList('SYS_WM_WARE_UNIT')}" var="dict">
                                    <option value="${dict.value}">${dict.label}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td><input id="qtyEa_fmEditor" class="form-control" readonly></td>
                    </tr>
                    <tr>
                        <td><label class="pull-left">是否扣数:</label></td>
                        <td><label class="pull-left">是否复核:</label></td>
                        <td><label class="pull-left">需扫描数:</label></td>
                        <td><label class="pull-left">已扫描数:</label></td>
                    </tr>
                    <tr>
                        <td><input id="isSubtract" type="checkbox" class="myCheckbox"></td>
                        <td><input id="isCheck" type="checkbox" class="myCheckbox"></td>
                        <td><input id="qtySkuPickEa_fmEditor" class="form-control" readonly></td>
                        <td><input id="qtySkuScanEa_fmEditor" class="form-control" readonly></td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </div>
        <div style="width: 100%; padding-left: 10px; padding-right: 10px;">
            <div style="background-color: #2c3e50; width: 100%; height: 35px; color: white; line-height: 35px;">&nbsp;&nbsp;</div>
            <table id="fmTable" class="text-nowrap table-condensed"></table>
            <div id="footer_left" style="border: 1px solid #dddddd; padding-bottom: 10px; margin-top: 5px;">
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
                        <td><input id="qtySku_fmEditor" class="form-control" readonly></td>
                        <td><input id="qtyAllEa_fmEditor" class="form-control" readonly></td>
                        <td><input id="qtyAllScanEa_fmEditor" class="form-control" readonly></td>
                        <td><input id="qtyAllNotScanEa_fmEditor" class="form-control" readonly></td>
                        <td><input id="grossWeight_fmEditor" class="form-control" readonly></td>
                        <td><input id="cubic_fmEditor" class="form-control" readonly></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div id="right" style="width: 50%; float: right">
        <!-- 工具栏 -->
        <div id="toolbar_right" style="width: 100%; padding-left: 10px; padding-top: 10px;">
            <button id="changeId" class="btn btn-primary" onclick="packConfirmHandler()">关箱/换箱</button>
            <button id="cancelPack" class="btn btn-primary" onclick="cancelPackHandler()">取消打包</button>
            <button id="getSoSerial" class="btn btn-primary" onclick="getSoSerial()">查询出库序列号</button>
        </div>
        <!-- 搜索 -->
        <div>
            <form id="searchForm_right" class="form form-horizontal well" style="padding-top: 5px; height: 250px; margin-bottom: 5px;">
                <table class="bq-table">
                    <tbody>
                    <tr>
                        <td><label class="pull-left">目标箱号：</label></td>
                        <td><label class="pull-left">是否自动生成箱号：</label></td>
                        <td><label class="pull-left">是否打印装箱清单：</label></td>
                        <td><label class="pull-left">是否打印装箱标签：</label></td>
                    </tr>
                    <tr>
                        <td><input id="toId_toEditor" class="form-control" maxlength="32" autocomplete="off"></td>
                        <td><input id="isAutoCartonNo" type="checkbox" class="myCheckbox" onchange="changeIsAutoCartonNo()"></td>
                        <td><input id="isPrintContainer" type="checkbox" class="myCheckbox"></td>
                        <td><input id="isPrintLabel" type="checkbox" class="myCheckbox"></td>
                    </tr>
                    <tr>
                        <td><label class="pull-left">快递单号：</label></td>
                        <td><label class="pull-left">是否更改快递单号：</label></td>
                        <td><label class="pull-left">重量：</label></td>
                        <td><label class="pull-left">是否称重：</label></td>
                    </tr>
                    <tr>
                        <td><input id="trackingNo_toEditor" class="form-control" maxlength="32" autocomplete="off"></td>
                        <td><input id="isTrack" type="checkbox" class="myCheckbox" onchange="isTrackChange()"></td>
                        <td><input id="packWeight_toEditor" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)" autocomplete="off"></td>
                        <td><input id="isWeigh" type="checkbox" class="myCheckbox" onchange="isWeighChange()"></td>
                    </tr>
                    <tr>
                        <td><label class="pull-left">包材：</label></td>
                    </tr>
                    <tr>
                        <td><input id="packMaterial_toEditor" class="form-control" maxlength="32" autocomplete="off"></td>
                    </tr>
                    </tbody>
                </table>
            </form>
        </div>

        <div style="width: 100%; padding-left: 10px;">
            <div style="background-color: #2c3e50; width: 100%; height: 35px; color: white; line-height: 35px;">&nbsp;&nbsp;已扫描明细</div>
            <table id="toTable" class="text-nowrap table-condensed"></table>
            <div id="footer_right" style="border: 1px solid #dddddd; padding-bottom: 10px; margin-top: 5px;">
                <table class="bq-table">
                    <tbody>
                    <tr>
                        <td><label class="pull-left labelClass">商品数</label></td>
                        <td><label class="pull-left labelClass">需扫描数EA</label></td>
                        <td><label class="pull-left labelClass">总重量</label></td>
                        <td><label class="pull-left labelClass">总体积</label></td>
                    </tr>
                    <tr>
                        <td><input id="qtySku_toEditor" class="form-control" readonly></td>
                        <td><input id="qtyAllEa_toEditor" class="form-control" readonly></td>
                        <td><input id="grossWeight_toEditor" class="form-control" readonly></td>
                        <td><input id="cubic_toEditor" class="form-control" readonly></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
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
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
    <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
</html>