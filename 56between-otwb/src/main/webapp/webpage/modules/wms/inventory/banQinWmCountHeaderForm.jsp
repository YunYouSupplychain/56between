<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>库存盘点管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmCountHeaderForm.js" %>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="inventory:banQinWmCountHeader:edit">
        <a class="btn btn-primary" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmCountHeader:createCount">
        <a class="btn btn-primary" id="header_createCount" onclick="createCount()">生成普通盘点任务</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmCountHeader:createRecount">
        <a class="btn btn-primary" id="header_createRecount" onclick="createRecount()">生成复盘任务</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmCountHeader:cancelCountTask">
        <a class="btn btn-primary" id="header_cancelCountTask" onclick="cancelCountTask()">取消盘点任务</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmCountHeader:closeCount">
        <a class="btn btn-primary" id="header_closeCount" onclick="closeCount()">关闭盘点</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmCountHeader:cancelCount">
        <a class="btn btn-primary" id="header_cancelCount" onclick="cancelCount()">取消盘点</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmCountHeader:createAd">
        <a class="btn btn-primary" id="header_createAd" onclick="createAd()">生成调整单</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="banQinWmCountHeaderEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <form:hidden path="isCreateCheck"/>
    <form:hidden path="isSerial"/>
    <sys:message content="${message}"/>
    <div class="tabs-container" style="height:200px">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#baseInfo" aria-expanded="true">基础信息</a></li>
            <li class=""><a data-toggle="tab" href="#conditionInfo" aria-expanded="true">盘点条件</a></li>
            <li class=""><a data-toggle="tab" href="#lotInfo" aria-expanded="true">批次属性</a></li>
        </ul>
        <div class="tab-content">
            <div id="baseInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">盘点单号</label></td>
                        <td class="width-15">
                            <form:input path="countNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">状态</label></td>
                        <td class="width-15">
                            <form:select path="status" class="form-control m-b" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_COUNT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">盘点单类型</label></td>
                        <td class="width-15">
                            <form:select path="countType" class="form-control m-b" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_COUNT_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right"><font color="red">*</font>盘点范围</label></td>
                        <td class="width-15">
                            <form:select path="countRange" class="form-control m-b required" onchange="countRangeChange()">
                                <form:option value=""/>
                                <form:options items="${fns:getDictList('SYS_WM_COUNT_RANGE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right"><font color="red">*</font>静态/动态盘点</label></td>
                        <td class="width-15">
                            <form:select path="countMethod" class="form-control m-b required">
                                <form:option value=""/>
                                <form:options items="${fns:getDictList('SYS_WM_COUNT_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right"><font color="red">*</font>明盘/盲盘</label></td>
                        <td class="width-15">
                            <form:select path="countMode" class="form-control m-b required">
                                <form:option value=""/>
                                <form:options items="${fns:getDictList('SYS_WM_COUNT_MODE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">监盘人</label></td>
                        <td class="width-15">
                            <form:input path="monitorOp" htmlEscape="false" class="form-control " maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">上次盘点单号</label></td>
                        <td class="width-15">
                            <form:input path="parentCountNo" htmlEscape="false" class="form-control " readonly="true"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="conditionInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">货主</label></td>
                        <td class="width-15">
                            <input id="ownerType" value="OWNER" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                           fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="${banQinWmCountHeaderEntity.ownerCode}" allowInput="true"
                                           displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${banQinWmCountHeaderEntity.ownerName}"
                                           selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                           fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           queryParams="ebcuType" queryParamValues="ownerType" isMultiSelected="true" afterSelect="selectOwner">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">商品</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                           fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="${banQinWmCountHeaderEntity.skuCode}" allowInput="true"
                                           displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue="${banQinWmCountHeaderEntity.skuName}"
                                           selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                           fieldLabels="货主编码|货主名称|商品编码|商品名称" fieldKeys="ownerCode|ownerName|skuCode|skuName"
                                           searchLabels="货主编码|商品编码|商品名称" searchKeys="ownerCode|skuCode|skuName" inputSearchKey="skuCodeAndName"
                                           queryParams="ownerCodes" queryParamValues="ownerCode" isMultiSelected="true" afterSelect="selectSku">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">批次号</label></td>
                        <td class="width-15">
                            <form:input path="lotNum" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">跟踪号</label></td>
                        <td class="width-15">
                            <form:input path="traceId" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">库位从</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="fmLoc" displayFieldName="fmLoc" displayFieldKeyName="locCode" displayFieldValue="${banQinWmCountHeaderEntity.fmLoc}"
                                           selectButtonId="fmLocSelectId" deleteButtonId="fmLocDeleteId"
                                           fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                           searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">库位到</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue="${banQinWmCountHeaderEntity.toLoc}"
                                           selectButtonId="toLocSelectId" deleteButtonId="toLocDeleteId"
                                           fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                           searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">动盘时间从</label></td>
                        <td class="width-15">
                            <input id="takeStartTime" name="takeStartTime" disabled class="form-control" value="<fmt:formatDate value="${banQinWmCountHeaderEntity.takeStartTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        </td>
                        <td class="width-10"><label class="pull-right">动盘时间到</label></td>
                        <td class="width-15">
                            <input id="takeEndTime" name="takeEndTime" disabled class="form-control" value="<fmt:formatDate value="${banQinWmCountHeaderEntity.takeEndTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">抽盘比例</label></td>
                        <td class="width-15">
                            <form:input path="randomRate" htmlEscape="false" class="form-control" readonly="true" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">抽盘数量</label></td>
                        <td class="width-15">
                            <form:input path="randomNum" htmlEscape="false" class="form-control" readonly="true" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">区域</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhArea/grid" title="选择区域" cssClass="form-control"
                                           fieldId="areaCode" fieldName="areaCode" fieldKeyName="areaCode" fieldValue="${banQinWmCountHeaderEntity.areaCode}" allowInput="true"
                                           displayFieldId="areaName" displayFieldName="areaName" displayFieldKeyName="areaName" displayFieldValue="${banQinWmCountHeaderEntity.areaName}"
                                           selectButtonId="areaSelectId" deleteButtonId="skuDetail_areaDeleteId"
                                           fieldLabels="区域编码|区域名称" fieldKeys="areaCode|areaName"
                                           searchLabels="区域编码|区域名称" searchKeys="areaCode|areaName" inputSearchKey="areaCodeAndName"
                                           isMultiSelected="true" afterSelect="selectArea">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">库区</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择目标库区" cssClass="form-control"
                                           fieldId="zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue="${banQinWmCountHeaderEntity.zoneCode}" allowInput="true"
                                           displayFieldId="zoneName" displayFieldName="zoneName" displayFieldKeyName="zoneName" displayFieldValue="${banQinWmCountHeaderEntity.zoneName}"
                                           selectButtonId="zoneSelectId" deleteButtonId="zoneDeleteId"
                                           fieldLabels="库区代码|库区名称" fieldKeys="zoneCode|zoneName"
                                           searchLabels="库区代码|库区名称" searchKeys="zoneCode|zoneName" inputSearchKey="zoneCodeAndName"
                                           isMultiSelected="true" afterSelect="selectZone">
                            </sys:popSelect>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="lotInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">生产日期</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='lotAtt01'>
                                <input name="lotAtt01" class="form-control"
                                       value="<fmt:formatDate value="${banQinWmCountHeaderEntity.lotAtt01}" pattern="yyyy-MM-dd"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">失效日期</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='lotAtt02'>
                                <input name="lotAtt02" class="form-control"
                                       value="<fmt:formatDate value="${banQinWmCountHeaderEntity.lotAtt02}" pattern="yyyy-MM-dd"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">入库日期</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='lotAtt03'>
                                <input name="lotAtt03" class="form-control"
                                       value="<fmt:formatDate value="${banQinWmCountHeaderEntity.lotAtt03}" pattern="yyyy-MM-dd"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">品质</label></td>
                        <td class="width-15">
                            <form:select path="lotAtt04" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_QC_ATT')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">批次属性05</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt05" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">批次属性06</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt06" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">批次属性07</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt07" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">批次属性08</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt08" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">批次属性09</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt09" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">批次属性10</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt10" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">批次属性11</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt11" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">批次属性12</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt12" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<div class="tabs-container">
    <div id="detailToolbar" style="width: 100%; padding: 5px 0;">
        <shiro:hasPermission name="inventory:banQinWmTaskCount:confirmDetail">
            <a class="btn btn-primary" id="detail_confirm" onclick="confirmDetail()">盘点确认</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inventory:banQinWmTaskCount:addConfirmCount">
            <a class="btn btn-primary" id="detail_add" onclick="addDetail()">新增</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inventory:banQinWmTaskCount:del">
            <a class="btn btn-danger" id="detail_remove" onclick="removeDetail()">删除</a>
        </shiro:hasPermission>
    </div>
    <div id="tab1-left">
        <table id="wmTaskCountTable" class="table well text-nowrap"></table>
    </div>
    <div id="tab1-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
        <form:form id="inputForm1" method="post" class="form">
            <input type="hidden" id="detail_id" name="id"/>
            <input type="hidden" id="detail_orgId" name="orgId"/>
            <input type="hidden" id="detail_countNo" name="countNo"/>
            <input type="hidden" id="detail_headerId" name="headerId"/>
            <input type="hidden" id="detail_countOp" name="countOp"/>
            <input type="hidden" id="detail_countMethod" name="countMethod"/>
            <input type="hidden" id="detail_countMode" name="countMode"/>
            <input type="hidden" id="detail_packCode" name="packCode"/>
            <input type="hidden" id="detail_packDesc" name="packDesc"/>
            <input type="hidden" id="detail_uom" name="uom"/>
            <input type="hidden" id="detail_dataSource" name="dataSource"/>
            <input type="hidden" id="detail_uomDesc" name="uomDesc"/>
            <input type="hidden" id="detail_qtyCountUom" name="qtyCountUom"/>
            <input type="hidden" id="detail_uomQuantity" name="uomQuantity"/>
            <div class="tabs-container" style="margin: 0 0 10px 0;">
                <ul id="detailTab" class="nav nav-tabs">
                    <li class="active"><a data-toggle="tab" href="#skuDetail_baseInfo" aria-expanded="true">基本信息</a></li>
                    <li class=""><a data-toggle="tab" href="#skuDetail_lotInfo" aria-expanded="true">批次属性</a></li>
                </ul>
                <div class="tab-content">
                    <div id="skuDetail_baseInfo" class="tab-pane fade in active">
                        <table class="bq-table text-nowrap" style="overflow-x: auto;">
                            <tbody>
                            <tr>
                                <td class="width-25"><label class="pull-left">盘点任务Id</label></td>
                                <td class="width-25"><label class="pull-left">盘点状态</label></td>
                                <td class="width-25"><label class="pull-left asterisk">货主</label></td>
                                <td class="width-25"><label class="pull-left asterisk">商品</label></td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <input id="detail_countId" name="countId" htmlEscape="false" class="form-control" readonly="readonly"/>
                                </td>
                                <td class="width-25">
                                    <select id="detail_status" name="status" class="form-control m-b" disabled>
                                        <c:forEach items="${fns:getDictList('SYS_WM_TASK_STATUS')}" var="dict">
                                            <option value="${dict.value}">${dict.label}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control required"
                                                   fieldId="detail_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                   displayFieldId="detail_ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                   selectButtonId="detail_ownerSelectId" deleteButtonId="detail_ownerDeleteId"
                                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                   queryParams="ebcuType" queryParamValues="ownerType">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control required"
                                                   fieldId="detail_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="skuCode" allowInput="true"
                                                   displayFieldId="detail_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                                   selectButtonId="detail_skuSelectId" deleteButtonId="detail_skuDeleteId"
                                                   fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                   searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName"
                                                   concatId="detail_skuName,detail_packCode,detail_packDesc,detail_uom,detail_cdprDesc,detail_cdprQuantity"
                                                   concatName="skuName,packCode,cdpaFormat,rcvUom,rcvUomName,uomQty"
                                                   queryParams="ownerCode" queryParamValues="detail_ownerCode" afterSelect="afterSelectSku">
                                    </sys:popSelect>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25"><label class="pull-left">批次号</label></td>
                                <td class="width-25"><label class="pull-left asterisk">库位</label></td>
                                <td class="width-25"><label class="pull-left asterisk">跟踪号</label></td>
                                <td class="width-25"><label class="pull-left">库存数EA</label></td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <input id="detail_lotNum" name="lotNum" htmlEscape="false" class="form-control" readonly="readonly"/>
                                </td>
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
                                    <input id="detail_qty" name="qty" htmlEscape="false" class="form-control" readonly/>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25"><label class="pull-left asterisk">盘点数EA</label></td>
                                <td class="width-25"><label class="pull-left asterisk">损益数EA</label></td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <input id="detail_qtyCountEa" name="qtyCountEa" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                                </td>
                                <td class="width-25">
                                    <input id="detail_qtyDiff" name="qtyDiff" htmlEscape="false" class="form-control" readonly/>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div id="skuDetail_lotInfo" class="tab-pane fade">
                        <table id="detailLotAttTab" class="bq-table"></table>
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</div>
</body>
</html>