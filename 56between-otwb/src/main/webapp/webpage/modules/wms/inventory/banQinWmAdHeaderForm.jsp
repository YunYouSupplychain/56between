<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>调整单管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmAdHeaderForm.js" %>
    <style>
        .myCheckbox {
            margin-right: 10px;
            cursor: pointer;
            width: 16px;
            height: 16px;
            position: relative;
        }
    </style>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="inventory:banQinWmAdHeader:edit">
        <a class="btn btn-primary btn-sm" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmAdHeader:audit">
        <a class="btn btn-primary btn-sm" id="header_audit" onclick="audit()">审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmAdHeader:cancelAudit">
        <a class="btn btn-primary btn-sm" id="header_cancelAudit" onclick="cancelAudit()">取消审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmAdHeader:adjust">
        <a class="btn btn-primary btn-sm" id="header_adjust" onclick="adjust()">执行调整</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmAdHeader:closeOrder">
        <a class="btn btn-primary btn-sm" id="header_closeOrder" onclick="closeOrder()">关闭订单</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmAdHeader:cancelOrder">
        <a class="btn btn-primary btn-sm" id="header_cancelOrder" onclick="cancelOrder()">取消订单</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="banQinWmAdHeaderEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="orgId"/>
    <sys:message content="${message}"/>
    <div class="tabs-container" style="height:200px">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-10"><label class="pull-right">调整单号</label></td>
                <td class="width-15">
                    <form:input path="adNo" htmlEscape="false" class="form-control "/>
                </td>
                <td class="width-10"><label class="pull-right"><font color="red">*</font>货主</label></td>
                <td class="width-15">
                    <input id="ownerType" value="OWNER" type="hidden">
                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control required"
                                   fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="${banQinWmAdHeaderEntity.ownerCode}" allowInput="true"
                                   displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${banQinWmAdHeaderEntity.ownerName}"
                                   selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                   queryParams="ebcuType" queryParamValues="ownerType">
                    </sys:popSelect>
                </td>
                <td class="width-10"><label class="pull-right">状态</label></td>
                <td class="width-15">
                    <form:select path="status" class="form-control m-b">
                        <form:options items="${fns:getDictList('SYS_WM_AD_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">审核状态</label></td>
                <td class="width-15">
                    <form:select path="auditStatus" class="form-control m-b">
                        <form:options items="${fns:getDictList('SYS_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">盘点单号</label></td>
                <td class="width-15">
                    <form:input path="countNo" htmlEscape="false" class="form-control "/>
                </td>
                <td class="width-10"><label class="pull-right">审核人</label></td>
                <td class="width-15">
                    <form:input path="auditOp" htmlEscape="false" class="form-control "/>
                </td>
                <td class="width-10"><label class="pull-right">审核时间</label></td>
                <td class="width-15">
                    <div class='input-group form_datetime'>
                        <input type='text' id="auditTime" name="auditTime" class="form-control"
                               value="<fmt:formatDate value="${banQinWmAdHeaderEntity.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </td>
                <td class="width-10"><label class="pull-right">审核意见</label></td>
                <td class="width-15">
                    <form:input path="auditComment" htmlEscape="false" class="form-control " maxlength="512"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">调整操作人</label></td>
                <td class="width-15">
                    <form:input path="adOp" htmlEscape="false" class="form-control " maxlength="32"/>
                </td>
                <td class="width-10"><label class="pull-right">调整时间</label></td>
                <td class="width-15">
                    <div class='input-group form_datetime' id='adTimeF'>
                        <input type='text' id="adTime" name="adTime" class="form-control "
                               value="<fmt:formatDate value="${banQinWmAdHeaderEntity.adTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </td>
                <td class="width-10"><label class="pull-right">调整原因</label></td>
                <td class="width-15">
                    <form:select path="reasonCode" class="form-control m-b">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('SYS_WM_AD_REASON')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">原因描述</label></td>
                <td class="width-15">
                    <form:input path="reason" htmlEscape="false" class="form-control " maxlength="512"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">自定义1</label></td>
                <td class="width-15">
                    <form:input path="def1" htmlEscape="false" class="form-control " maxlength="64"/>
                </td>
                <td class="width-10"><label class="pull-right">自定义2</label></td>
                <td class="width-15">
                    <form:input path="def2" htmlEscape="false" class="form-control " maxlength="64"/>
                </td>
                <td class="width-10"><label class="pull-right">自定义3</label></td>
                <td class="width-15">
                    <form:input path="def3" htmlEscape="false" class="form-control " maxlength="64"/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</form:form>
<div class="tabs-container">
    <div id="detailToolbar" style="width: 100%; padding: 5px 0;">
        <shiro:hasPermission name="inventory:banQinWmAdDetail:addDetail">
            <a class="btn btn-primary btn-sm" id="detail_add" onclick="addDetail()">新增</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inventory:banQinWmAdDetail:saveDetail">
            <a class="btn btn-primary btn-sm" id="detail_save" onclick="saveDetail()">保存</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inventory:banQinWmAdDetail:removeDetail">
            <a class="btn btn-danger btn-sm" id="detail_remove" onclick="removeDetail()">删除</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inventory:banQinWmAdDetail:adjustDetail">
            <a class="btn btn-primary btn-sm" id="detail_adjust" onclick="adjustDetail()">执行调整</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inventory:banQinWmAdDetail:cancelDetail">
            <a class="btn btn-primary btn-sm" id="detail_cancel" onclick="cancelDetail()">取消</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inventory:banQinWmAdDetail:getByInventory">
            <a class="btn btn-primary btn-sm" id="detail_getInv" onclick="getByInventory()">按库存获取</a>
        </shiro:hasPermission>
    </div>
    <div id="tab1-left">
        <table id="wmAdDetailTable" class="table well text-nowrap"></table>
    </div>
    <div id="tab1-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
        <form:form id="inputForm1" method="post" class="form">
            <input type="hidden" id="detail_id" name="id"/>
            <input type="hidden" id="detail_orgId" name="orgId"/>
            <input type="hidden" id="detail_adNo" name="adNo"/>
            <input type="hidden" id="detail_ownerCode" name="ownerCode"/>
            <input type="hidden" id="detail_headerId" name="headerId"/>
            <input type="hidden" id="detail_cdprQuantity" name="cdprQuantity"/>
            <div class="tabs-container" style="margin: 0 0 10px 0;">
                <ul class="nav nav-tabs">
                    <li class="active"><a data-toggle="tab" href="#tab-1-1" aria-expanded="true">调整信息</a></li>
                    <li class=""><a data-toggle="tab" href="#tab-1-2" aria-expanded="true">批次属性</a></li>
                    <li class=""><a data-toggle="tab" href="#tab-1-3" aria-expanded="true">序列号</a></li>
                </ul>
                <div class="tab-content">
                    <div id="tab-1-1" class="tab-pane fade in active">
                        <table class="bq-table">
                            <tbody>
                            <tr>
                                <td class="width-25">
                                    <label class="pull-left">行号</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">状态</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">商品</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">批次号</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <input id="detail_lineNo" name="lineNo" htmlEscape="false" class="form-control" readonly="readonly"/>
                                </td>
                                <td class="width-25">
                                    <select id="detail_status" name="status" class="form-control m-b" disabled>
                                        <c:forEach items="${fns:getDictList('SYS_WM_AD_STATUS')}" var="dict">
                                            <option value="${dict.value}">${dict.label}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                   fieldId="detail_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="skuCode" allowInput="true"
                                                   displayFieldId="detail_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                                   selectButtonId="detail_skuSelectId" deleteButtonId="detail_skuDeleteId"
                                                   fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                   searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName"
                                                   concatId="detail_skuName,detail_packCode,detail_packDesc,detail_uom,detail_cdprDesc,detail_cdprQuantity"
                                                   concatName="skuName,packCode,cdpaFormat,rcvUom,rcvUomName,uomQty"
                                                   queryParams="ownerCode" queryParamValues="ownerCode" afterSelect="afterSelectSku">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/inventory/banQinWmInvLotAtt/data" title="选择批次" cssClass="form-control"
                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                   displayFieldId="detail_lotNum" displayFieldName="lotNum" displayFieldKeyName="lotNum" displayFieldValue=""
                                                   selectButtonId="detail_lotNumSelectId" deleteButtonId="detail_lotNumDeleteId"
                                                   fieldLabels="批次号|商品编码|货主编码|生产日期|失效日期|入库日期|品质|批次属性5|批次属性6|批次属性7|批次属性8|批次属性9|批次属性10|批次属性11|批次属性12"
                                                   fieldKeys="lotNum|skuCode|ownerCode|lotAtt01|lotAtt02|lotAtt03|lotAtt04|lotAtt05|lotAtt06|lotAtt07|lotAtt08|lotAtt09|lotAtt10|lotAtt11|lotAtt12"
                                                   searchLabels="批次号" searchKeys="lotNum" inputSearchKey="codeAndName"
                                                   queryParams="ownerCode|skuCode" queryParamValues="ownerCode|detail_skuCode" afterSelect="afterSelectLot">
                                    </sys:popSelect>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <label class="pull-left asterisk">库位</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">跟踪号</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">包装规格</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">包装单位</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                   displayFieldId="detail_locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue=""
                                                   selectButtonId="detail_locSelectId" deleteButtonId="detail_locDeleteId"
                                                   fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                   searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <input id="detail_traceId" name="traceId" htmlEscape="false" class="form-control required" maxlength="32"/>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control required"
                                                   fieldId="detail_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue=""
                                                   displayFieldId="detail_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                                   selectButtonId="detail_packSelectId" deleteButtonId="detail_packDeleteId"
                                                   fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                                   searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" inputSearchKey="codeAndName" disabled="disabled">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <div class="input-group" style="width: 100%">
                                        <sys:popSelect url="${ctx}//wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                                       fieldId="detail_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                                       displayFieldId="detail_cdprDesc" displayFieldName="cdprDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                                       selectButtonId="detail_uomSelectId" deleteButtonId="detail_uomDeleteId"
                                                       fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                       searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                                       queryParams="packCode" queryParamValues="detail_packCode" afterSelect="afterSelectPack">
                                        </sys:popSelect>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <label class="pull-left asterisk">调整方式</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">调整数</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">调整数EA</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">是否序列号管理</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <select id="detail_adMode" name="adMode" class="form-control required m-b">
                                        <c:forEach items="${fns:getDictList('SYS_WM_AD_MODE')}" var="dict">
                                            <option value="${dict.value}">${dict.label}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td class="width-25">
                                    <input id="detail_qtyAdUom" name="qtyAdUom" htmlEscape="false" onkeyup="bq.numberValidator(this, 2, 0);" oninput="qtyAddChange()" class="form-control required"/>
                                </td>
                                <td class="width-25">
                                    <input id="detail_qtyAdEa" name="qtyAdEa" htmlEscape="false" class="form-control " readonly="readonly"/>
                                </td>
                                <td class="width-25">
                                    <input id="detail_isSerial" name="isSerial" disabled type="checkbox" style="margin-right: 10px; cursor: pointer; width: 20px; height: 20px; position: relative;"/>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div id="tab-1-2" class="tab-pane fade in">
                        <table id="detailLotAttTab" class="bq-table"></table>
                    </div>
                    <div id="tab-1-3" class="tab-pane fade in">
                        <div id="serialToolbar" style="width: 100%; padding: 5px 0;">
                            <a class="btn btn-primary btn-sm" id="serial_add" onclick="addSerial()">新增</a>
                            <a class="btn btn-danger btn-sm" id="serial_remove" onclick="removeSerial()">删除</a>
                        </div>
                        <table class="table well text-nowrap">
                            <thead>
                            <tr>
                                <th class="hide"></th>
                                <th><input type="checkbox" class="myCheckbox" onclick="serialSelectChange(this.checked)"/></th>
                                <th class="asterisk">调整方式</th>
                                <th class="asterisk">货主</th>
                                <th class="asterisk">商品</th>
                                <th class="asterisk">批次号</th>
                                <th class="asterisk">序列号</th>
                            </tr>
                            </thead>
                            <tbody id="serialList">
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</div>
<script type="text/template" id="serialTpl">//<!--
<tr id="wmAdSerialEntitys{{idx}}">
    <td class="hide">
        <input id="wmAdSerialEntitys{{idx}}_id" name="wmAdSerialEntitys[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="wmAdSerialEntitys{{idx}}_delFlag" name="wmAdSerialEntitys[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="wmAdSerialEntitys{{idx}}_orgId" name="wmAdSerialEntitys[{{idx}}].orgId" type="hidden" value="{{row.orgId}}"/>
        <input id="wmAdSerialEntitys{{idx}}_headerId" name="wmAdSerialEntitys[{{idx}}].headerId" type="hidden" value="{{row.headerId}}"/>
        <input id="wmAdSerialEntitys{{idx}}_adNo" name="wmAdSerialEntitys[{{idx}}].adNo" type="hidden" value="{{row.adNo}}"/>
        <input id="wmAdSerialEntitys{{idx}}_ownerCode" name="wmAdSerialEntitys[{{idx}}].ownerCode" type="hidden" value="{{row.ownerCode}}"/>
        <input id="wmAdSerialEntitys{{idx}}_skuCode" name="wmAdSerialEntitys[{{idx}}].skuCode" type="hidden" value="{{row.skuCode}}"/>
        <input id="wmAdSerialEntitys{{idx}}_def1" name="wmAdSerialEntitys[{{idx}}].def1" type="hidden" value="{{row.def1}}"/>
        <input id="wmAdSerialEntitys{{idx}}_def2" name="wmAdSerialEntitys[{{idx}}].def2" type="hidden" value="{{row.def2}}"/>
        <input id="wmAdSerialEntitys{{idx}}_def3" name="wmAdSerialEntitys[{{idx}}].def3" type="hidden" value="{{row.def3}}"/>
        <input id="wmAdSerialEntitys{{idx}}_def4" name="wmAdSerialEntitys[{{idx}}].def4" type="hidden" value="{{row.def4}}"/>
        <input id="wmAdSerialEntitys{{idx}}_def5" name="wmAdSerialEntitys[{{idx}}].def5" type="hidden" value="{{row.def5}}"/>
    </td>
    <td><input id="wmAdSerialEntitys{{idx}}_checkbox" type="checkbox" class="myCheckbox" name="serialSelect"/></td>
    <td width="100px">
        <select id="wmAdSerialEntitys{{idx}}_adMode" name="wmAdSerialEntitys[{{idx}}].adMode" data-value="{{row.adMode}}" class="form-control m-b">
            <c:forEach items="${fns:getDictList('SYS_WM_AD_MODE')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td><input id="wmAdSerialEntitys{{idx}}_ownerName" name="wmAdSerialEntitys[{{idx}}].ownerName" type="text" value="{{row.ownerName}}" class="form-control" readonly/></td>
    <td><input id="wmAdSerialEntitys{{idx}}_skuName" name="wmAdSerialEntitys[{{idx}}].skuName" type="text" value="{{row.skuName}}" class="form-control" readonly/></td>
    <td><input id="wmAdSerialEntitys{{idx}}_lotNum" name="wmAdSerialEntitys[{{idx}}].lotNum" type="text" value="{{row.lotNum}}" class="form-control" readonly/></td>
    <td><input id="wmAdSerialEntitys{{idx}}_serialNo" name="wmAdSerialEntitys[{{idx}}].serialNo" type="text" value="{{row.serialNo}}" class="form-control"/></td>
</tr>//-->
</script>
<script type="text/javascript">
    var serialRowIdx = 0, serialTpl = $("#serialTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
</script>
</body>
</html>