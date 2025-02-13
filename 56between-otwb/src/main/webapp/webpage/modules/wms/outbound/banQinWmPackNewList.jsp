<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>打包管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="banQinWmPackNewList.js" %>
    <style>
        ul {
            padding: 0;
        }

        ul li {
            display: inline;
            float: left;
            margin-right: 10px;
        }

        input[type='radio'], input[type='checkbox'] {
            cursor: pointer;
            width: 16px;
            height: 16px;
            vertical-align: text-bottom;
        }
    </style>
</head>
<body>
<div style="width: 100%;background-color: #FFFFFF">
    <!-- 工具栏 -->
    <div id="toolbar" style="width: 100%; padding: 10px;">
        <button id="packConfirm" class="btn btn-primary btn-sm">关箱</button>
        <button id="cancelPack" class="btn btn-primary btn-sm">取消打包</button>
        <button id="batchPack" class="btn btn-primary btn-sm">批量打包</button>
        <button id="querySoSerialNo" class="btn btn-primary btn-sm">查询出库序列号</button>
    </div>
    <!-- 搜索 -->
    <div id="wmPackForm" style="width: 100%; float: left; border: 1px solid #dddddd; margin: 0 10px;">
        <div style="width: 25%; float: left">
            <table class="table table-condensed">
                <tbody>
                <tr>
                    <td></td>
                    <td>
                        <ul>
                            <li><input name="isPacked" type="radio" value="N"/>&nbsp;未打包</li>
                            <li><input name="isPacked" type="radio" value="Y"/>&nbsp;已打包</li>
                        </ul>
                    </td>
                </tr>
                <tr>
                    <td><label class="pull-right">包材</label></td>
                    <td><input id="packMaterial" type="text" class="form-control" maxlength="32"></td>
                </tr>
                <tr>
                    <td><label class="pull-right">出库单号</label></td>
                    <td><input id="soNo" type="text" class="form-control" maxlength="32"></td>
                </tr>
                <tr>
                    <td><label class="pull-right">扫描商品</label></td>
                    <td><input id="skuCode" type="text" class="form-control" maxlength="32"></td>
                </tr>
                <tr>
                    <td><label class="pull-right">序列号</label></td>
                    <td><input id="serialNo" type="text" class="form-control" maxlength="64" readonly></td>
                </tr>
                <tr>
                    <td><label class="pull-right">扫描箱号</label></td>
                    <td><input id="caseNo" type="text" class="form-control" maxlength="32"></td>
                </tr>
                <tr>
                    <td><label class="pull-right">扫描方式</label></td>
                    <td><input id="isSingle" type="checkbox"/>&nbsp;单件</td>
                </tr>
                <tr>
                    <td><label class="pull-right">是否称重</label></td>
                    <td>
                        <div class="input-group">
                            <span class="input-group-addon">
                                <input id="isWeight" type="checkbox" >
                            </span>
                            <input id="weight" type="text" class="form-control" maxlength="32" readonly onkeyup="bq.numberValidator(this, 3, 0)">
                        </div>
                    </td>
                </tr>
                <tr>
                    <td><label class="pull-right">打包箱号</label></td>
                    <td><input id="toId" type="text" class="form-control" readonly></td>
                </tr>
                <tr>
                    <td><label class="pull-right">操作人</label></td>
                    <td><input id="op" type="text" class="form-control" readonly></td>
                </tr>
                </tbody>
            </table>
        </div>
        <div style="width: 2%; float: left;">&nbsp;</div>
        <div style="width: 70%; float: left;">
            <table class="table">
                <tbody>
                <tr>
                    <td style="width: 40%;">
                        <div style="text-align: center; font-size: 20px; font-weight: bold;">数量</div>
                    </td>
                    <td style="width: 60%;">
                        <div style="text-align: center; font-size: 20px; font-weight: bold;">状态</div>
                    </td>
                </tr>
                <tr>
                    <td style="width: 40%;">
                        <textarea id="scanQty" class="form-control" style="resize: none; font-size: 130px; height: 280px; color: red; line-height: 250px; text-align: center;" wrap="off" rows="1" maxlength="4" readonly onkeyup="bq.numberValidator(this, 0, 0)">1</textarea>
                    </td>
                    <td style="width: 60%;">
                        <textarea id="status" class="form-control" style="resize: none; font-size: 125px; height: 280px; line-height: 250px; text-align: center;" wrap="off" rows="1" maxlength="4" readonly></textarea>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <div style="width: 100%; padding: 0px 10px;">
        <table id="banQinWmPackTable" class="text-nowrap table-condensed"></table>
    </div>
</div>
<!-- 出库序列号查询pop -->
<div class="modal fade" id="soSerialListModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
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

<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
    <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
</body>
</html>