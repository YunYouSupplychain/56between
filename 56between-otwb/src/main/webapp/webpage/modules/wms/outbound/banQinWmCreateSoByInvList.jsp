<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>按库存生成出库单</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmCreateSoByInvList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel-body" style="background-color: #FFFFFF">
        <div id="left" style="width: 40%; float: left;">
            <div style="padding-bottom: 5px;">
                <shiro:hasPermission name="outbound:banQinWmCreateSoByInv:createSo">
                    <button id="createSo" class="btn btn-primary" onclick="createSo()">生成出库单</button>
                </shiro:hasPermission>
                <span id="total" style="float: right;font-size: 16px;font-weight: bold"></span>
            </div>
            <div style="background-color: #2c3e50; width: 100%; height: 30px; color: white; line-height: 30px; margin-bottom: 5px;">&nbsp;&nbsp;已选择数据</div>
            <table id="soTable" class="table-condensed text-nowrap"></table>
        </div>
        <div id="middle" style="width: 3%; margin-top: 300px; float: left; text-align:center;">
            <button class="btn btn-default" onclick="moveToLeft()"><i class="glyphicon glyphicon-arrow-left"></i></button>
            <button class="btn btn-default" style="margin-top: 10px;" onclick="moveToRight()"><i class="glyphicon glyphicon-arrow-right"></i></button>
        </div>
        <div id="right" style="width: 57%; height: 100%; float: right;">
            <div style="padding-bottom: 5px;">
                <button class="btn btn-default" onclick="searchInv()">检索库存数据</button>
            </div>
            <div style="background-color: #2c3e50; width: 100%; height: 30px; color: white; line-height: 30px; margin-bottom: 5px;">&nbsp;&nbsp;库存数据</div>
            <table id="invTable" class="text-nowrap"></table>
        </div>
    </div>
</div>
<div class="modal fade" id="searchModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 800px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">查询条件</h4>
            </div>
            <div class="modal-body">
                <form id="searchForm">
                    <table class="table">
                        <tbody>
                        <tr>
                            <td class="width-25"><label class="pull-left">货主</label></td>
                            <td class="width-25"><label class="pull-left">商品</label></td>
                            <td class="width-25"><label class="pull-left">批次号</label></td>
                            <td class="width-25"><label class="pull-left">库位</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="ownerType" value="OWNER" type="hidden">
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                               fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                               displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="ownerSelectId" deleteButtonId="query_ownerDeleteId"
                                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                               queryParams="ebcuType" queryParamValues="ownerType">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                               fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                               displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                               selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                               fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                               searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input name="lotNum" maxlength="64" class="form-control"/>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                               displayFieldId="locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue=""
                                               selectButtonId="locCodeSelectId" deleteButtonId="locCodeDeleteId"
                                               fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                               searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                </sys:popSelect>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">库区</label></td>
                            <td class="width-25"><label class="pull-left">跟踪号</label></td>
                            <td class="width-25"><label class="pull-left">生产日期</label></td>
                            <td class="width-25"><label class="pull-left">失效日期</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择库区" cssClass="form-control"
                                               fieldId="zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue="" allowInput="true"
                                               displayFieldId="zoneName" displayFieldName="zoneName" displayFieldKeyName="zoneName" displayFieldValue=""
                                               selectButtonId="zoneSelectId" deleteButtonId="zoneDeleteId"
                                               fieldLabels="库区编码|库区名称" fieldKeys="zoneCode|zoneName"
                                               searchLabels="库区编码|库区名称" searchKeys="zoneCode|zoneName" inputSearchKey="zoneCodeAndName">
                                </sys:popSelect>
                            </td>
                            <td class="width-25"><input name="traceId" maxlength="64" class="form-control"/></td>
                            <td class="width-25"><input id="lotAtt01" name="lotAtt01" class="form-control" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td class="width-25"><input id="lotAtt02" name="lotAtt02" class="form-control" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">入库日期</label></td>
                            <td class="width-25"><label class="pull-left">品质</label></td>
                            <td class="width-25"><label class="pull-left">批次属性5</label></td>
                            <td class="width-25"><label class="pull-left">批次属性6</label></td>
                        </tr>
                        <tr>
                            <td class="width-25"><input id="lotAtt03" name="lotAtt03" class="form-control" pattern="yyyy-MM-dd HH:mm:ss"/>
                            <td class="width-25">
                                <select name="lotAtt04" class="form-control m-b">
                                    <option value=""></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_QC_ATT')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-25"><input name="lotAtt05" maxlength="64" class="form-control"/></td>
                            <td class="width-25"><input name="lotAtt06" maxlength="64" class="form-control"/></td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">批次属性7</label></td>
                            <td class="width-25"><label class="pull-left">批次属性8</label></td>
                            <td class="width-25"><label class="pull-left">批次属性9</label></td>
                            <td class="width-25"><label class="pull-left">批次属性10</label></td>
                        </tr>
                        <tr>
                            <td class="width-25"><input name="lotAtt07" maxlength="64" class="form-control"/></td>
                            <td class="width-25"><input name="lotAtt08" maxlength="64" class="form-control"/></td>
                            <td class="width-25"><input name="lotAtt09" maxlength="64" class="form-control"/></td>
                            <td class="width-25"><input name="lotAtt10" maxlength="64" class="form-control"/></td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">批次属性11</label></td>
                            <td class="width-25"><label class="pull-left">批次属性12</label></td>
                        </tr>
                        <tr>
                            <td class="width-25"><input name="lotAtt11" maxlength="64" class="form-control"/></td>
                            <td class="width-25"><input name="lotAtt12" maxlength="64" class="form-control"/></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="queryConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="confirmModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 800px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title asterisk">出库时间</h4>
            </div>
            <div class="modal-body">
                <table class="table">
                    <tr>
                        <td class="width-25"><label class="pull-left asterisk">订单时间</label></td>
                        <td class="width-25"><label class="pull-left">出库时间</label></td>
                        <td class="width-25"><label class="pull-left">承运商</label></td>
                        <td class="width-25"><label class="pull-left">客户单号</label></td>
                    </tr>
                    <tr>
                        <td class="width-25"><input id="soOrderTime" name="soOrderTime" class="form-control required" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td class="width-25"><input id="outboundTime" name="outboundTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td class="width-25">
                            <input id="carrierType" value="CARRIER" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择承运商" cssClass="form-control"
                                           fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                           displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                           selectButtonId="carrierSBtnId" deleteButtonId="carrierDBtnId"
                                           fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           inputSearchKey="codeAndName" allowInput="true"
                                           queryParams="ebcuType" queryParamValues="carrierType">
                            </sys:popSelect>
                        </td>
                        <td class="width-25"><input id="customerNo" name="customerNo" class="form-control" maxlength="30"/></td>
                    </tr>
                    <tr>
                        <td class="width-25"><label class="pull-left">车牌号</label></td>
                        <td class="width-25"><label class="pull-left">驾驶员</label></td>
                        <td class="width-25"><label class="pull-left">电话</label></td>
                    </tr>
                    <tr>
                        <td class="width-25"><input id="vehicleNo" name="vehicleNo" class="form-control" maxlength="30"/></td>
                        <td class="width-25"><input id="driver" name="driver" class="form-control" maxlength="30"/></td>
                        <td class="width-25"><input id="driverTel" name="driverTel" class="form-control" maxlength="20"/></td>
                    </tr>
                    <tr>
                        <td class="width-25"><label class="pull-left">收货人地址</label></td>
                    </tr>
                    <tr>
                        <td colspan="4"><input id="consigneeAddr" name="consigneeAddr" class="form-control" maxlength="512"/></td>
                    </tr>
                    <tr>
                        <td class="width-25"><label class="pull-left">备注</label></td>
                    </tr>
                    <tr>
                        <td colspan="4"><input id="remarks" name="remarks" class="form-control" maxlength="255"/></td>
                    </tr>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="confirm()">确认</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>