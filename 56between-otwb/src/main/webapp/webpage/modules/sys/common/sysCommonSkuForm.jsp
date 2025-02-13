<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>商品信息管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="sysCommonSkuForm.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="ownerType" value="OWNER"/>
</div>
<form:form id="inputForm" modelAttribute="sysCommonSku" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <div class="tabs-container">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#baseInfo" aria-expanded="true">基础信息</a></li>
            <li class=""><a data-toggle="tab" href="#wmsInfo" aria-expanded="true">仓库系统</a></li>
        </ul>
        <div class="tab-content">
            <div id="baseInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">货主</label></td>
                        <td class="width-15">
                            <sys:grid title="选择货主" url="${ctx}/sys/common/customer/grid"
                                      fieldId="ownerCode" fieldName="ownerCode"
                                      fieldKeyName="code" fieldValue="${sysCommonSku.ownerCode}"
                                      displayFieldId="ownerName" displayFieldName="ownerName"
                                      displayFieldKeyName="name" displayFieldValue="${sysCommonSku.ownerName}"
                                      fieldLabels="货主编码|货主名称|数据套" fieldKeys="code|name|dataSet"
                                      searchLabels="货主编码|货主名称" searchKeys="code|name"
                                      queryParams="type|dataSet" queryParamValues="ownerType|dataSet"
                                      cssClass="form-control required" afterSelect="ownerSelect"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">商品编码</label></td>
                        <td class="width-15">
                            <form:input path="skuCode" htmlEscape="false" class="form-control reuqired" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">商品名称</label></td>
                        <td class="width-15">
                            <form:input path="skuName" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">商品简称</label></td>
                        <td class="width-15">
                            <form:input path="skuShortName" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">商品外语名称</label></td>
                        <td class="width-15">
                            <form:input path="skuForeignName" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">数据套</label></td>
                        <td class="width-15">
                            <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid"
                                      fieldId="dataSet" fieldName="dataSet"
                                      fieldKeyName="code" fieldValue="${sysCommonSku.dataSet}"
                                      displayFieldId="dataSetName" displayFieldName="dataSetName"
                                      displayFieldKeyName="name" displayFieldValue="${sysCommonSku.dataSetName}"
                                      fieldLabels="编码|名称" fieldKeys="code|name"
                                      searchLabels="编码|名称" searchKeys="code|name"
                                      cssClass="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">包装规格</label></td>
                        <td class="width-15">
                            <sys:grid title="选择包装" url="${ctx}/sys/common/package/grid"
                                      fieldId="packCode" fieldName="packCode"
                                      fieldKeyName="cdpaCode" fieldValue="${sysCommonSku.packCode}"
                                      displayFieldId="packFormat" displayFieldName="packFormat"
                                      displayFieldKeyName="cdpaFormat" displayFieldValue="${sysCommonSku.packFormat}"
                                      fieldLabels="包装代码|包装类型|包装规格|数据套" fieldKeys="cdpaCode|cdpaType|cdpaFormat|dataSet"
                                      searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat"
                                      queryParams="dataSet" queryParamValues="dataSet" cssClass="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right">规格</label></td>
                        <td class="width-15">
                            <form:input path="skuSpec" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">保质期(天数)</label></td>
                        <td class="width-15">
                            <form:input path="shelfLife" htmlEscape="false" class="form-control" maxlength="64" onkeyup="bq.numberValidator(this, 0, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">建档时间</label></td>
                        <td class="width-15">
                            <div class='input-group date' id="filingTime">
                                <input type='text' name="filingTime" class="form-control"
                                       value="<fmt:formatDate value="${sysCommonSku.filingTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">品类</label></td>
                        <td class="width-15">
                            <sys:grid title="选择商品分类" url="${ctx}/sys/common/skuClassification/grid"
                                      fieldId="skuClass" fieldName="skuClass"
                                      fieldKeyName="code" fieldValue="${sysCommonSku.skuClass}"
                                      displayFieldId="skuClassName" displayFieldName="skuClassName"
                                      displayFieldKeyName="name" displayFieldValue="${sysCommonSku.skuClassName}"
                                      fieldLabels="编码|名称" fieldKeys="code|name"
                                      searchLabels="编码|名称" searchKeys="code|name"
                                      queryParams="dataSet" queryParamValues="dataSet" cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">商品温层</label></td>
                        <td class="width-15">
                            <form:select path="tempLevel" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_TEMPR_CATEGORY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">客户商品编码</label></td>
                        <td class="width-15">
                            <form:input path="skuCustomerCode" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">商品形态</label></td>
                        <td class="width-15">
                            <form:select path="formCode" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_MATERIAL_MORPHOLOGY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">是否允许超收</label></td>
                        <td class="width-15">
                            <input id="isOverRcv" name="isOverRcv" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isOverRcvControl(this.checked)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">超收百分比</label></td>
                        <td class="width-15">
                            <form:input path="overRcvPct" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">币别</label></td>
                        <td class="width-15">
                            <form:select path="stockCurId" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_CURRENCY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">毛重</label></td>
                        <td class="width-15">
                            <form:input path="grossWeight" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">净重</label></td>
                        <td class="width-15">
                            <form:input path="netWeight" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">体积</label></td>
                        <td class="width-15">
                            <form:input path="volume" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">单价</label></td>
                        <td class="width-15">
                            <form:input path="price" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 8, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">长</label></td>
                        <td class="width-15">
                            <form:input path="length" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">宽</label></td>
                        <td class="width-15">
                            <form:input path="width" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">高</label></td>
                        <td class="width-15">
                            <form:input path="height" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">单位</label></td>
                        <td class="width-15">
                            <form:select path="unit" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_WARE_UNIT')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">备注</label></td>
                        <td class="width-15" colspan="5">
                            <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div class="tabs-container">
                    <ul class="nav nav-tabs">
                        <li class="active"><a data-toggle="tab" href="#barcodeInfo" aria-expanded="true">条码信息</a></li>
                    </ul>
                    <div class="tab-content">
                        <div id="barcodeInfo" class="tab-pane fade in active">
                            <div id="barcodeToolbar" style="padding: 5px 0;">
                                <shiro:hasPermission name="sys:common:sku:barcode:add">
                                    <a id="barcode_add" class="btn btn-primary" onclick="addBarcode()"> 新增</a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="sys:common:sku:barcode:del">
                                    <a id="barcode_remove" class="btn btn-danger" onclick="delBarcode()"> 删除</a>
                                </shiro:hasPermission>
                            </div>
                            <table id="skuBarcodeTable" class="table table-bordered table-condensed">
                                <thead>
                                <tr>
                                    <th class="hide"></th>
                                    <th style="width:36px;vertical-align:middle">
                                        <label><input type="checkbox" name="btSelectAll" style="width: 16px;height: 16px;"/></label>
                                    </th>
                                    <th class="asterisk">条码</th>
                                    <th>是否默认</th>
                                </tr>
                                </thead>
                                <tbody id="skuBarcodeList"></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div id="wmsInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">批次属性</label></td>
                        <td class="width-15">
                            <sys:grid title="选择批次属性" url="${ctx}/sys/common/wms/lotHeader/grid" cssClass="form-control"
                                      fieldId="lotCode" fieldName="lotCode" fieldKeyName="lotCode" fieldValue="${sysCommonSku.lotCode}"
                                      displayFieldId="lotName" displayFieldName="lotName" displayFieldKeyName="lotName" displayFieldValue="${sysCommonSku.lotName}"
                                      fieldLabels="批次属性编码|批次属性名称" fieldKeys="lotCode|lotName"
                                      searchLabels="批次属性编码|批次属性名称" searchKeys="lotCode|lotName"
                                      queryParams="dataSet" queryParamValues="dataSet"/>
                        </td>
                        <td class="width-10"><label class="pull-right">上架库位指定规则</label></td>
                        <td class="width-15">
                            <form:select path="reserveCode" class="form-control m-b ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_RESERVE_CODE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">上架规则</label></td>
                        <td class="width-15">
                            <sys:grid title="选择上架规则" url="${ctx}/sys/common/wms/rulePaHeader/grid" cssClass="form-control"
                                      fieldId="paRule" fieldName="paRule" fieldKeyName="ruleCode" fieldValue="${sysCommonSku.paRule}"
                                      displayFieldId="paRuleName" displayFieldName="paRuleName" displayFieldKeyName="ruleName" displayFieldValue="${sysCommonSku.paRuleName}"
                                      fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                      searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName"
                                      queryParams="dataSet" queryParamValues="dataSet"/>
                        </td>
                        <td class="width-10"><label class="pull-right">上架库区</label></td>
                        <td class="width-15">
                            <sys:grid title="选择库区" url="${ctx}/sys/common/wms/zone/grid" cssClass="form-control"
                                      fieldId="paZone" fieldName="paZone" fieldKeyName="zoneCode" fieldValue="${sysCommonSku.paZone}"
                                      displayFieldId="paZoneName" displayFieldName="paZoneName" displayFieldKeyName="zoneName" displayFieldValue="${sysCommonSku.paZoneName}"
                                      fieldLabels="库区编码|库区名称" fieldKeys="zoneCode|zoneName"
                                      searchLabels="库区编码|库区名称" searchKeys="zoneCode|zoneName"
                                      queryParams="dataSet" queryParamValues="dataSet"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">上架库位</label></td>
                        <td class="width-15">
                            <sys:grid title="选择库位" url="${ctx}/sys/common/wms/loc/grid" cssClass="form-control"
                                      fieldId="" fieldName="" fieldKeyName=""
                                      displayFieldId="paLoc" displayFieldName="paLoc" displayFieldKeyName="locCode" displayFieldValue="${sysCommonSku.paLoc}"
                                      fieldLabels="库位编码|库区编码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                      searchLabels="库位编码|库区编码|库区名称" searchKeys="locCode|zoneCode|zoneName"
                                      queryParams="dataSet" queryParamValues="dataSet"/>
                        </td>
                        <td class="width-10"><label class="pull-right">库存周转规则</label></td>
                        <td class="width-15">
                            <sys:grid title="选择库存周转规则" url="${ctx}/sys/common/wms/ruleRotationHeader/grid" cssClass="form-control"
                                      fieldId="rotationRule" fieldName="rotationRule" fieldKeyName="ruleCode" fieldValue="${sysCommonSku.rotationRule}"
                                      displayFieldId="rotationRuleName" displayFieldName="rotationRuleName" displayFieldKeyName="ruleName" displayFieldValue="${sysCommonSku.rotationRuleName}"
                                      fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                      searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName"
                                      queryParams="dataSet" queryParamValues="dataSet"/>
                        </td>
                        <td class="width-10"><label class="pull-right">分配规则</label></td>
                        <td class="width-15">
                            <sys:grid title="选择分配规则" url="${ctx}/sys/common/wms/ruleAllocHeader/grid" cssClass="form-control"
                                      fieldId="allocRule" fieldName="allocRule" fieldKeyName="ruleCode" fieldValue="${sysCommonSku.allocRule}"
                                      displayFieldId="allocRuleName" displayFieldName="allocRuleName" displayFieldKeyName="ruleName" displayFieldValue="${sysCommonSku.allocRuleName}"
                                      fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                      searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName"
                                      queryParams="dataSet" queryParamValues="dataSet"/>
                        </td>
                        <td class="width-10"><label class="pull-right">循环级别</label></td>
                        <td class="width-15">
                            <sys:grid title="选择循环级别" url="${ctx}/sys/common/wms/cycle/grid" cssClass="form-control"
                                      fieldId="cycleCode" fieldName="cycleCode" fieldKeyName="cycleCode" fieldValue="${sysCommonSku.cycleCode}"
                                      displayFieldId="cycleName" displayFieldName="cycleName" displayFieldKeyName="cycleName" displayFieldValue="${sysCommonSku.cycleName}"
                                      fieldLabels="循环级别编码|循环级别名称" fieldKeys="cycleCode|cycleName"
                                      searchLabels="循环级别编码|循环级别名称" searchKeys="cycleCode|cycleName"
                                      queryParams="dataSet" queryParamValues="dataSet"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">闪点</label></td>
                        <td class="width-15">
                            <form:input path="flashPoint" htmlEscape="false" class="form-control" maxlength="64" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">燃点</label></td>
                        <td class="width-15">
                            <form:input path="burningPoint" htmlEscape="false" class="form-control" maxlength="64" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">是否温控</label></td>
                        <td class="width-15">
                            <input id="isCold" name="isCold" type="checkbox" class="myCheckbox" onclick="isColdControl(this.checked)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">最低温度</label></td>
                        <td class="width-15">
                            <form:input path="minTemp" htmlEscape="false" class="form-control" maxlength="64" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">最高温度</label></td>
                        <td class="width-15">
                            <form:input path="maxTemp" htmlEscape="false" class="form-control" maxlength="64" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">生效日期</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='effectiveDate'>
                                <input name="effectiveDate" class="form-control" value="<fmt:formatDate value="${sysCommonSku.effectiveDate}" pattern="yyyy-MM-dd"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">失效日期</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='expirationDate'>
                                <input name="expirationDate" class="form-control" value="<fmt:formatDate value="${sysCommonSku.expirationDate}" pattern="yyyy-MM-dd"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">是否危险品</label></td>
                        <td class="width-15">
                            <input id="isDg" name="isDg" type="checkbox" class="myCheckbox" onclick="isDgControl(this.checked)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">危险品等级</label></td>
                        <td class="width-15">
                            <form:select path="dgClass" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_DG_CLASS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">UNNO</label></td>
                        <td class="width-15">
                            <form:input path="unno" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">越库级别</label></td>
                        <td class="width-15">
                            <form:select path="cdClass" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_CD_CLASS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">商品ABC</label></td>
                        <td class="width-15">
                            <form:select path="abc" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_ABC')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">是否做效期控制</label></td>
                        <td class="width-15">
                            <input id="isValidity" name="isValidity" type="checkbox" class="myCheckbox" onclick="isValidityControl(this.checked)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">周期类型</label></td>
                        <td class="width-15">
                            <form:select path="lifeType" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_LIFE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">入库效期(天数)</label></td>
                        <td class="width-15">
                            <form:input path="inLifeDays" htmlEscape="false" class="form-control" maxlength="64" onkeyup="bq.numberValidator(this, 0, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">出库效期(天数)</label></td>
                        <td class="width-15">
                            <form:input path="outLifeDays" htmlEscape="false" class="form-control" maxlength="64" onkeyup="bq.numberValidator(this, 0, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">库存上限</label></td>
                        <td class="width-15">
                            <form:input path="maxLimit" htmlEscape="false" class="form-control" maxlength="64" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">是否为父件</label></td>
                        <td class="width-15">
                            <input id="isParent" name="isParent" type="checkbox" class="myCheckbox" onclick="isParentControl(this.checked)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">缺省收货单位</label></td>
                        <td class="width-15">
                            <sys:grid title="选择缺省单位" url="${ctx}/sys/common/packageRelation/grid" cssClass="form-control"
                                      fieldId="rcvUom" fieldName="rcvUom" fieldKeyName="cdprUnitLevel" fieldValue="${sysCommonSku.rcvUom}"
                                      displayFieldId="rcvUomName" displayFieldName="rcvUomName" displayFieldKeyName="cdprDesc" displayFieldValue="${sysCommonSku.rcvUomName}"
                                      fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                      searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                      queryParams="packCode|dataSet" queryParamValues="packCode|dataSet"/>
                        </td>
                        <td class="width-10"><label class="pull-right">缺省发货单位</label></td>
                        <td class="width-15">
                            <sys:grid title="选择缺省单位" url="${ctx}/sys/common/packageRelation/grid" cssClass="form-control"
                                      fieldId="shipUom" fieldName="shipUom" fieldKeyName="cdprUnitLevel" fieldValue="${sysCommonSku.shipUom}"
                                      displayFieldId="shipUomName" displayFieldName="shipUomName" displayFieldKeyName="cdprDesc" displayFieldValue="${sysCommonSku.shipUomName}"
                                      fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                      searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                      queryParams="packCode|dataSet" queryParamValues="packCode|dataSet"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">缺省打印单位</label></td>
                        <td class="width-15">
                            <sys:grid title="选择缺省单位" url="${ctx}/sys/common/packageRelation/grid" cssClass="form-control"
                                      fieldId="printUom" fieldName="printUom" fieldKeyName="cdprUnitLevel" fieldValue="${sysCommonSku.printUom}"
                                      displayFieldId="printUomName" displayFieldName="printUomName" displayFieldKeyName="cdprDesc" displayFieldValue="${sysCommonSku.printUomName}"
                                      fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                      searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                      queryParams="packCode|dataSet" queryParamValues="packCode|dataSet"/>
                        </td>
                        <td class="width-10"><label class="pull-right">库存下限</label></td>
                        <td class="width-15">
                            <form:input path="minLimit" htmlEscape="false" class="form-control" maxlength="64" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">是否质检管理</label></td>
                        <td class="width-15">
                            <input id="isQc" name="isQc" type="checkbox" class="myCheckbox" onclick="isQcControl(this.checked)"/>
                        </td>
                        <td class="width-10"><label class="pull-right" id="qcPhaseLabel">质检阶段</label></td>
                        <td class="width-15">
                            <form:select path="qcPhase" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_QC_PHASE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right" id="qcRuleLabel">质检规则</label></td>
                        <td class="width-15">
                            <sys:grid title="选择质检规则" url="${ctx}/sys/common/wms/ruleQcHeader/grid" cssClass="form-control"
                                      fieldId="qcRule" fieldName="qcRule" fieldKeyName="ruleCode" fieldValue="${sysCommonSku.qcRule}"
                                      displayFieldId="qcRuleName" displayFieldName="qcRuleName" displayFieldKeyName="ruleName" displayFieldValue="${sysCommonSku.qcRuleName}"
                                      fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                      searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName"
                                      queryParams="dataSet" queryParamValues="dataSet"/>
                        </td>
                        <td class="width-10"><label class="pull-right" id="itemGroupLabel">质检项</label></td>
                        <td class="width-15">
                            <sys:grid title="选择质检项" url="${ctx}/sys/common/wms/qcItemHeader/grid" cssClass="form-control"
                                      fieldId="itemGroupCode" fieldName="itemGroupCode" fieldKeyName="itemGroupCode" fieldValue="${sysCommonSku.itemGroupCode}"
                                      displayFieldId="itemGroupName" displayFieldName="itemGroupName" displayFieldKeyName="itemGroupName" displayFieldValue="${sysCommonSku.itemGroupName}"
                                      fieldLabels="质检项编码|质检项名称" fieldKeys="itemGroupCode|itemGroupName"
                                      searchLabels="质检项编码|质检项名称" searchKeys="itemGroupCode|itemGroupName"
                                      queryParams="dataSet" queryParamValues="dataSet"/>
                        </td>
                        <td class="width-10"><label class="pull-right">费率组</label></td>
                        <td class="width-15">
                            <form:input path="rateGroup" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">是否序列号管理</label></td>
                        <td class="width-15">
                            <input id="isSerial" name="isSerial" type="checkbox" class="myCheckbox" onclick="isSerialControl(this.checked)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">商品组编码</label></td>
                        <td class="width-15">
                            <form:input path="groupCode" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">商品条码</label></td>
                        <td class="width-15">
                            <form:input path="barCode" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">供应商商品条码</label></td>
                        <td class="width-15">
                            <form:input path="supBarCode" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">海关商品编码</label></td>
                        <td class="width-15">
                            <form:input path="hsCode" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">应急电话</label></td>
                        <td class="width-15">
                            <form:input path="emergencyTel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">尺码</label></td>
                        <td class="width-15">
                            <form:input path="skuSize" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">款号</label></td>
                        <td class="width-15">
                            <form:input path="style" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">颜色</label></td>
                        <td class="width-15">
                            <form:input path="color" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div class="tabs-container">
                    <ul class="nav nav-tabs">
                        <li class="active"><a data-toggle="tab" href="#locInfo" aria-expanded="true">拣货信息</a></li>
                    </ul>
                    <div class="tab-content">
                        <div id="locInfo" class="tab-pane fade in active">
                            <div id="locToolbar" style="padding: 3px 0;">
                                <shiro:hasPermission name="sys:common:sku:loc:add">
                                    <a id="loc_add" class="btn btn-primary" onclick="addLoc()"> 新增</a>
                                </shiro:hasPermission>
                                <shiro:hasPermission name="sys:common:sku:loc:del">
                                    <a id="loc_remove" class="btn btn-danger" onclick="delLoc()"> 删除</a>
                                </shiro:hasPermission>
                            </div>
                            <table id="skuLocTable" class="table table-bordered table-condensed">
                                <thead>
                                <tr>
                                    <th class="hide"></th>
                                    <th style="width:36px;vertical-align:middle">
                                        <label><input type="checkbox" name="btSelectAll" style="width: 16px;height: 16px;"/></label>
                                    </th>
                                    <th class="asterisk">拣货位</th>
                                    <th class="asterisk">商品拣货位类型</th>
                                    <th>库存上限</th>
                                    <th>库存下限</th>
                                    <th>最小补货数</th>
                                    <th>最小补货单位</th>
                                    <th>是否超量分配</th>
                                    <th>是否补被占用库存</th>
                                    <th>是否超量补货</th>
                                    <th>是否从存储位补货</th>
                                    <th>是否从箱拣货位补货</th>
                                </tr>
                                </thead>
                                <tbody id="skuLocList"></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form:form>
<script type="text/template" id="skuBarcodeTpl">//<!--
    <tr id="skuBarcodeList{{idx}}" data-index="{{idx}}">
        <td class="hide">
            <input type="hidden" id="skuBarcodeList{{idx}}_id" name="skuBarcodeList[{{idx}}].id" value="{{row.id}}"/>
            <input type="hidden" id="skuBarcodeList{{idx}}_dataSet" name="skuBarcodeList[{{idx}}].dataSet" value="{{row.dataSet}}"/>
            <input type="hidden" id="skuBarcodeList{{idx}}_recVer" name="skuBarcodeList[{{idx}}].recVer" value="{{row.recVer}}"/>
            <input type="hidden" id="skuBarcodeList{{idx}}_ownerCode" name="skuBarcodeList[{{idx}}].ownerCode" value="{{row.ownerCode}}"/>
            <input type="hidden" id="skuBarcodeList{{idx}}_skuCode" name="skuBarcodeList[{{idx}}].skuCode" value="{{row.skuCode}}"/>
            <input type="hidden" id="skuBarcodeList{{idx}}_headerId" name="skuBarcodeList[{{idx}}].headerId" value="{{row.headerId}}"/>
        </td>
        <td style="vertical-align: middle">
            <input type="checkbox" name="btSelectItem" data-index="{{idx}}" class="form-control" style="width: 16px;height: 16px;"/>
        </td>
        <td>
            <input type="text" id="skuBarcodeList{{idx}}_barcode" name="skuBarcodeList[{{idx}}].barcode" class="form-control" value="{{row.barcode}}" maxlength="64"/>
        </td>
        <td width="80px">
            <input id="skuBarcodeList{{idx}}_isDefault" name="skuBarcodeList[{{idx}}].isDefault" type="checkbox" style="zoom:1.3;" value="{{row.isDefault}}" onclick="isDefaultChange('#skuBarcodeList', {{idx}})"/>
        </td>
    </tr>//-->
</script>
<script type="text/template" id="skuSupplierTpl">//<!--
    <tr id="skuSupplierList{{idx}}">
        <td class="hide">
            <input type="hidden" id="skuSupplierList{{idx}}_id" name="skuSupplierList[{{idx}}].id" value="{{row.id}}"/>
            <input type="hidden" id="skuSupplierList{{idx}}_delFlag" name="skuSupplierList[{{idx}}].delFlag" value="0"/>
            <input type="hidden" id="skuSupplierList{{idx}}_dataSet" name="skuSupplierList[{{idx}}].dataSet" value="{{row.dataSet}}"/>
        </td>
        <td style="vertical-align: middle">
            <input type="checkbox" name="btSelectItem" data-index="{{idx}}" style="width: 16px;height: 16px;"/>
        </td>
        <td>
            <sys:grid title="选择供应商" url="${ctx}/sys/common/customer/grid" cssClass="form-control required"
                      fieldId="" fieldName="" fieldKeyName=""
                      displayFieldId="skuSupplierList{{idx}}_supplierCode" displayFieldName="skuSupplierList[{{idx}}].supplierCode" displayFieldKeyName="code" displayFieldValue="{{row.supplierCode}}"
                      fieldLabels="供应商编码|供应商名称" fieldKeys="code|name"
                      searchLabels="供应商编码|供应商名称" searchKeys="code|name"
                      afterSelect="supplierAfterSelect({{idx}})"/>
        </td>
        <td>
            <input type="text" id="skuSupplierList{{idx}}_supplierName" name="skuSupplierList[{{idx}}].supplierName" class="form-control" value="{{row.supplierName}}" readonly/>
        </td>
        <td width="80px">
            <input id="skuSupplierList{{idx}}_isDefault" name="skuSupplierList[{{idx}}].isDefault" type="checkbox" style="zoom:1.3;" value="{{row.isDefault}}" onclick="isDefaultChange('#skuSupplierList', {{idx}})"/>
        </td>
    </tr>//-->
</script>
<script type="text/template" id="skuLocTpl">//<!--
    <tr id="skuLocList{{idx}}">
        <td class="hide">
            <input type="hidden" id="skuLocList{{idx}}_id" name="skuLocList[{{idx}}].id" value="{{row.id}}"/>
            <input type="hidden" id="skuLocList{{idx}}_recVer" name="skuLocList[{{idx}}].recVer" value="{{row.recVer}}"/>
            <input type="hidden" id="skuLocList{{idx}}_dataSet" name="skuLocList[{{idx}}].dataSet" value="{{row.dataSet}}"/>
            <input type="hidden" id="skuLocList{{idx}}_ownerCode" name="skuLocList[{{idx}}].ownerCode" value="{{row.ownerCode}}"/>
            <input type="hidden" id="skuLocList{{idx}}_skuCode" name="skuLocList[{{idx}}].skuCode" value="{{row.skuCode}}"/>
            <input type="hidden" id="skuLocList{{idx}}_headerId" name="skuLocList[{{idx}}].headerId" value="{{row.headerId}}"/>
        </td>
        <td style="vertical-align: middle">
            <input type="checkbox" name="btSelectItem" data-index="{{idx}}" style="width: 16px;height: 16px;"/>
        </td>
        <td>
            <sys:grid title="选择拣货位" url="${ctx}/sys/common/wms/loc/grid" cssClass="form-control required"
                      fieldId="" fieldName="" fieldKeyName=""
                      displayFieldId="skuLocList{{idx}}_locCode" displayFieldName="skuLocList[{{idx}}].locCode"
                      displayFieldKeyName="locCode" displayFieldValue="{{row.locCode}}"
                      fieldLabels="库位编码|库区编码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                      searchLabels="库位编码|库区编码|库区名称" searchKeys="locCode|zoneCode|zoneName"
                      queryParams="dataSet" queryParamValues="dataSet"/>
        </td>
        <td>
            <select id="skuLocList{{idx}}_skuLocType" name="skuLocList[{{idx}}].skuLocType" data-value="{{row.skuLocType}}" class="form-control m-b required">
                <option value=""></option>
                <c:forEach items="${fns:getDictList('SYS_WM_SKU_LOC_TYPE')}" var="dict">
                    <option value="${dict.value}">${dict.label}</option>
                </c:forEach>
            </select>
        </td>
        <td>
            <input id="skuLocList{{idx}}_maxLimit" name="skuLocList[{{idx}}].maxLimit" class="form-control" value="{{row.maxLimit}}" onkeyup="bq.numberValidator(this, 2, 0)">
        </td>
        <td>
            <input id="skuLocList{{idx}}_minLimit" name="skuLocList[{{idx}}].minLimit" class="form-control" value="{{row.minLimit}}" onkeyup="bq.numberValidator(this, 2, 0)">
        </td>
        <td>
            <input id="skuLocList{{idx}}_minRp" name="skuLocList[{{idx}}].minRp" class="form-control" value="{{row.minRp}}" onkeyup="bq.numberValidator(this, 2, 0)"/>
        </td>
        <td>
            <sys:grid title="选择最小补货单位" url="${ctx}/sys/common/packageRelation/grid" cssClass="form-control"
                      fieldId="" fieldName="" fieldKeyName=""
                      displayFieldId="skuLocList{{idx}}_rpUom" displayFieldName="skuLocList[{{idx}}].rpUom"
                      displayFieldKeyName="cdprUnitLevel" displayFieldValue="{{row.rpUom}}"
                      fieldLabels="包装单位|数量" fieldKeys="cdprUnitLevel|cdprQuantity"
                      searchLabels="包装单位" searchKeys="cdprUnitLevel"
                      queryParams="packCode|dataSet" queryParamValues="packCode|dataSet"/>
        </td>
        <td>
            <input id="skuLocList{{idx}}_isOverAlloc" name="skuLocList[{{idx}}].isOverAlloc" type="checkbox" class="myCheckbox" value="{{row.isOverAlloc}}" onclick="checkboxChange(this.checked, '#skuLocList{{idx}}_isOverAlloc')">
        </td>
        <td>
            <input id="skuLocList{{idx}}_isRpAlloc" name="skuLocList[{{idx}}].isRpAlloc" type="checkbox" class="myCheckbox" value="{{row.isRpAlloc}}" onclick="checkboxChange(this.checked, '#skuLocList{{idx}}_isRpAlloc')">
        </td>
        <td>
            <input id="skuLocList{{idx}}_isOverRp" name="skuLocList[{{idx}}].isOverRp" type="checkbox" class="myCheckbox" value="{{row.isOverRp}}" onclick="checkboxChange(this.checked, '#skuLocList{{idx}}_isOverRp')">
        </td>
        <td>
            <input id="skuLocList{{idx}}_isFmRs" name="skuLocList[{{idx}}].isFmRs" type="checkbox" class="myCheckbox" value="{{row.isFmRs}}" onclick="checkboxChange(this.checked, '#skuLocList{{idx}}_isFmRs')"/>
        </td>
        <td>
            <input id="skuLocList{{idx}}_isFmCs" name="skuLocList[{{idx}}].isFmCs" type="checkbox" class="myCheckbox" value="{{row.isFmCs}}" onclick="checkboxChange(this.checked, '#skuLocList{{idx}}_isFmCs')">
        </td>
    </tr>//-->
</script>
</body>
</html>