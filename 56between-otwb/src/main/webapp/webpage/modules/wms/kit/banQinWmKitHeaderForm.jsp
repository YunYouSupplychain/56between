<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>加工单管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="banQinWmKitHeaderForm.js" %>
</head>
<body>
<div id="kitHeaderToolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="kit:banQinWmKitHeader:edit">
        <a class="btn btn-primary" id="btn_save" onclick="saveKitHeader()">保存</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="kit:banQinWmKitHeader:audit">
        <button id="btn_audit" class="btn btn-primary" onclick="auditHandler()">审核</button>
    </shiro:hasPermission>
    <shiro:hasPermission name="kit:banQinWmKitHeader:cancelAudit">
        <button id="btn_cancelAudit" class="btn btn-primary" onclick="cancelAuditHandler()">取消审核</button>
    </shiro:hasPermission>
    <shiro:hasPermission name="kit:banQinWmKitHeader:close">
        <button id="btn_close" class="btn btn-primary" onclick="closeHandler()">关闭订单</button>
    </shiro:hasPermission>
    <shiro:hasPermission name="kit:banQinWmKitHeader:cancel">
        <button id="btn_cancel" class="btn btn-primary" onclick="cancelHeader()">取消订单</button>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="banQinWmKitEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <form:hidden path="auditOp"/>
    <sys:message content="${message}"/>
    <div class="tabs-container" style="height:200px;">
        <ul id="headerTab" class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#baseInfo" aria-expanded="true">基本信息</a></li>
            <li class=""><a data-toggle="tab" href="#reserveInfo" aria-expanded="true">预留信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="baseInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">加工单号</label></td>
                        <td class="width-15">
                            <form:input path="kitNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">加工类型</label></td>
                        <td class="width-15">
                            <form:select path="kitType" class="form-control required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_KIT_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">状态</label></td>
                        <td class="width-15">
                            <form:select path="status" class="form-control required" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_KIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">审核状态</label></td>
                        <td class="width-15">
                            <form:select path="auditStatus" class="form-control required" disabled="true">
                                <form:options items="${fns:getDictList('SYS_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">货主编码</label></td>
                        <td class="width-15">
                            <input id="ownerType" value="OWNER" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control required"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="ownerCode" displayFieldName="ownerCode" displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinWmKitEntity.ownerCode}"
                                           selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                           fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           queryParams="ebcuType" queryParamValues="ownerType"
                                           concatId="ownerName" concatName="ebcuNameCn">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">货主名称</label></td>
                        <td class="width-15">
                            <form:input path="ownerName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">物流单号</label></td>
                        <td class="width-15">
                            <form:input path="logisticNo" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">计划加工台</label></td>
                        <td class="width-15">
                            <input id="locUseType" value="KT" type="hidden">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="kitLoc" displayFieldName="kitLoc" displayFieldKeyName="locCode" displayFieldValue="${banQinWmKitEntity.kitLoc}"
                                           selectButtonId="kitLocSelectId" deleteButtonId="kitLocDeleteId"
                                           fieldLabels="库位编码|库区编码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                           searchLabels="库位编码|库区编码" searchKeys="locCode|zoneCode" inputSearchKey="codeAndName"
                                           queryParams="locUseType" queryParamValues="locUseType">
                            </sys:popSelect>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">预计加工时间从</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='fmEtk'>
                                <input type='text' name="fmEtk" class="form-control"
                                       value="<fmt:formatDate value="${banQinWmKitEntity.fmEtk}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">预计加工时间到</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='toEtk'>
                                <input type='text' name="toEtk" class="form-control"
                                       value="<fmt:formatDate value="${banQinWmKitEntity.toEtk}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">审核人</label></td>
                        <td class="width-15">
                            <form:input path="auditOpName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">审核时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='auditTime'>
                                <input type='text' name="auditTime" class="form-control" readonly
                                       value="<fmt:formatDate value="${banQinWmKitEntity.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="reserveInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">自定义1</label></td>
                        <td class="width-15">
                            <form:input path="def1" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义2</label></td>
                        <td class="width-15">
                            <form:input path="def2" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义3</label></td>
                        <td class="width-15">
                            <form:input path="def3" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义4</label></td>
                        <td class="width-15">
                            <form:input path="def4" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">自定义5</label></td>
                        <td class="width-15">
                            <form:input path="def5" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">备注</label></td>
                        <td class="width-15" colspan="7">
                            <form:input path="remarks" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<div class="tabs-container">
    <ul id="detailTab" class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#kitParentDetailInfo" aria-expanded="true" onclick="detailTabChange(0)">父件明细</a></li>
        <li class=""><a data-toggle="tab" href="#kitSubDetailInfo" aria-expanded="true" onclick="detailTabChange(1)">子件明细</a></li>
        <li class=""><a data-toggle="tab" href="#kitTaskInfo" aria-expanded="true" onclick="detailTabChange(2)">加工任务</a></li>
        <li class=""><a data-toggle="tab" href="#kitDetailInfo" aria-expanded="true" onclick="detailTabChange(3)">加工明细</a></li>
        <li class=""><a data-toggle="tab" href="#paTaskInfo" aria-expanded="true" onclick="initPaTaskTable()">上架任务</a></li>
        <li class=""><a data-toggle="tab" href="#cancelAllocInfo" aria-expanded="true" onclick="initCancelAllocTable()">取消分配日志</a></li>
    </ul>
    <div class="tab-content">
        <div id="kitParentDetailInfo" class="tab-pane fade in active">
            <div id="kitParentDetailToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="kit:banQinWmKitParentDetail:add">
                    <a class="btn btn-primary" id="btn_parent_add" onclick="addParentHandler()">新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitParentDetail:edit">
                    <a class="btn btn-primary" id="btn_parent_save" onclick="saveParentHandler()">保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitParentDetail:del">
                    <a class="btn btn-danger" id="btn_parent_remove" onclick="removeParentHandler()">删除</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitParentDetail:duplicate">
                    <a class="btn btn-primary" id="btn_parent_duplicate" onclick="duplicateParentHandler()">复制</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitParentDetail:genSub">
                    <a class="btn btn-primary" id="btn_parent_generateSub" onclick="generateSubHandler()">生成子件</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitParentDetail:cancelGenSub">
                    <a class="btn btn-primary" id="btn_parent_cancelGenerateSub" onclick="cancelGenerateSubHandler()">取消生成子件</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitParentDetail:cancelLine">
                    <a class="btn btn-primary" id="btn_parent_cancel" onclick="cancelParentHandler()">取消订单行</a>
                </shiro:hasPermission>
            </div>
            <div id="kitParentDetail_tab-left">
                <table id="kitParentDetailTable" class="table text-nowrap"></table>
            </div>
            <div id="kitParentDetail_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="kitParentDetailForm" method="post" class="form">
                    <input id="kitParentDetail_id" name="id" type="hidden"/>
                    <input id="kitParentDetail_headerId" name="headerId" type="hidden"/>
                    <input id="kitParentDetail_kitNo" name="kitNo" type="hidden"/>
                    <input id="kitParentDetail_ownerCode" name="ownerCode" type="hidden"/>
                    <input id="kitParentDetail_orgId" name="orgId" type="hidden"/>
                    <input id="kitParentDetail_uomQty" name="uomQty" type="hidden"/>
                    <div class="tabs-container" style="margin: 0 0 10px 0;">
                        <ul id="kitParentDetailTab" class="nav nav-tabs">
                            <li class="active"><a data-toggle="tab" href="#kitParentDetailBaseInfo" aria-expanded="true">基本信息</a></li>
                            <li class=""><a data-toggle="tab" href="#kitParentDetailLotAttInfo" aria-expanded="true">批次属性</a></li>
                            <li class=""><a data-toggle="tab" href="#kitParentDetailReserveInfo" aria-expanded="true">预留信息</a></li>
                        </ul>
                        <div class="tab-content">
                            <div id="kitParentDetailBaseInfo" class="tab-pane fade in active">
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">行号</label></td>
                                        <td class="width-25"><label class="pull-left">状态</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">父件编码</label></td>
                                        <td class="width-25"><label class="pull-left">父件名称</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="kitParentDetail_parentLineNo" name="parentLineNo" class="form-control" readonly/>
                                        </td>
                                        <td class="width-25">
                                            <select id="kitParentDetail_status" name="status" class="form-control" disabled>
                                                <option></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_KIT_STATUS')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/kit/banQinCdWhBomHeader/grid" title="选择父件" cssClass="form-control required"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                           displayFieldId="kitParentDetail_parentSkuCode" displayFieldName="parentSkuCode" displayFieldKeyName="parentSkuCode" displayFieldValue=""
                                                           selectButtonId="kitParentDetail_parentSkuSelectId" deleteButtonId="kitParentDetail_parentSkuDeleteId"
                                                           fieldLabels="商品编码|商品名称" fieldKeys="parentSkuCode|parentSkuName"
                                                           searchLabels="商品编码|商品名称" searchKeys="parentSkuCode|parentSkuName"
                                                           concatId="kitParentDetail_parentSkuName,kitParentDetail_packCode,kitParentDetail_packDesc,kitParentDetail_uom,kitParentDetail_uomDesc,kitParentDetail_uomQty,kitParentDetail_paRule,kitParentDetail_paRuleName"
                                                           concatName="parentSkuName,packCode,packDesc,uom,uomDesc,uomQty,paRule,paRuleName"
                                                           queryParams="ownerCode|kitType" queryParamValues="ownerCode|kitType" afterSelect="kitParentSkuAfterSelect">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <input id="kitParentDetail_parentSkuName" name="parentSkuName" class="form-control" readonly/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left asterisk">包装规格</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">包装单位</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">预加工数</label></td>
                                        <td class="width-25"><label class="pull-left">预加工数EA</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control required"
                                                           fieldId="kitParentDetail_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue="" allowInput="false"
                                                           displayFieldId="kitParentDetail_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                                           selectButtonId="kitParentDetail_packSelectId" deleteButtonId="kitParentDetail_packDeleteId"
                                                           fieldLabels="包装代码|包装规格" fieldKeys="cdpaCode|cdpaFormat"
                                                           searchLabels="包装代码|包装规格" searchKeys="cdpaCode|cdpaFormat">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                                           fieldId="kitParentDetail_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                                           displayFieldId="kitParentDetail_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                                           selectButtonId="kitParentDetail_uomSelectId" deleteButtonId="kitParentDetail_uomDeleteId"
                                                           queryParams="packCode" queryParamValues="kitParentDetail_packCode"
                                                           fieldLabels="包装单位|数量|描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                           searchLabels="包装单位|描述" searchKeys="cdprUnitLevel|cdprDesc"
                                                           concatId="kitParentDetail_uomQty" concatName="cdprQuantity" afterSelect="kitParentDetailQtyChange">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <input id="kitParentDetail_qtyPlanUom" name="qtyPlanUom" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0);kitParentDetailQtyChange()"/>
                                        </td>
                                        <td class="width-25">
                                            <input id="kitParentDetail_qtyPlanEa" name="qtyPlanEa" class="form-control" readonly/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">已加工数</label></td>
                                        <td class="width-25"><label class="pull-left">已加工数EA</label></td>
                                        <td class="width-25"><label class="pull-left">计划加工台</label></td>
                                        <td class="width-25"><label class="pull-left">上架规则</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="kitParentDetail_qtyKitUom" name="qtyKitUom" class="form-control" readonly/>
                                        </td>
                                        <td class="width-25">
                                            <input id="kitParentDetail_qtyKitEa" name="qtyKitEa" class="form-control" readonly/>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择加工台" cssClass="form-control"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                           displayFieldId="kitParentDetail_planKitLoc" displayFieldName="planKitLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                           selectButtonId="kitParentDetail_planKitLocSelectId" deleteButtonId="kitParentDetail_planKitLocDeleteId"
                                                           fieldLabels="库位编码|库区编码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                           searchLabels="库位编码|库区编码" searchKeys="locCode|zoneCode"
                                                           queryParams="locUseType" queryParamValues="locUseType">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRulePaHeader/grid" title="选择上架规则" cssClass="form-control"
                                                           fieldId="kitParentDetail_paRule" fieldName="paRule" fieldKeyName="ruleCode" fieldValue="" allowInput="true"
                                                           displayFieldId="kitParentDetail_paRuleName" displayFieldName="paRuleName" displayFieldKeyName="ruleName" displayFieldValue=""
                                                           selectButtonId="kitParentDetail_paRuleSelectId" deleteButtonId="kitParentDetail_paRuleDeleteId"
                                                           fieldLabels="上架规则代码|上架规则名称" fieldKeys="ruleCode|ruleName"
                                                           searchLabels="上架规则代码|上架规则名称" searchKeys="ruleCode|ruleName">
                                            </sys:popSelect>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">是否生成子件</label></td>
                                        <td class="width-25"><label class="pull-left">物流单号</label></td>
                                        <td class="width-25"><label class="pull-left">物流单行号</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="kitParentDetail_isCreateSub" name="isCreateSub" type="checkbox" class="myCheckbox" disabled/>
                                        </td>
                                        <td class="width-25">
                                            <input id="kitParentDetail_logisticNo" name="logisticNo" class="form-control"/>
                                        </td>
                                        <td class="width-25">
                                            <input id="kitParentDetail_logisticLineNo" name="logisticLineNo" class="form-control"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div id="kitParentDetailLotAttInfo" class="tab-pane fade">
                                <table id="kitParentDetailLotAttTab" class="bq-table"></table>
                            </div>
                            <div id="kitParentDetailReserveInfo" class="tab-pane fade">
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">自定义1</label></td>
                                        <td class="width-25"><label class="pull-left">自定义2</label></td>
                                        <td class="width-25"><label class="pull-left">自定义3</label></td>
                                        <td class="width-25"><label class="pull-left">自定义4</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><input id="kitParentDetail_def1" name="def1" class="form-control"/></td>
                                        <td class="width-25"><input id="kitParentDetail_def2" name="def2" class="form-control"/></td>
                                        <td class="width-25"><input id="kitParentDetail_def3" name="def3" class="form-control"/></td>
                                        <td class="width-25"><input id="kitParentDetail_def4" name="def4" class="form-control"/></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">自定义5</label></td>
                                        <td class="width-25"colspan="4"><label class="pull-left">备注</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><input id="kitParentDetail_def5" name="def5" class="form-control"/></td>
                                        <td colspan="4"><input id="kitParentDetail_remarks" name="remarks" class="form-control"/></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
        <div id="kitSubDetailInfo" class="tab-pane fade">
            <div id="kitSubDetailToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="kit:banQinWmKitSubDetail:edit">
                    <a class="btn btn-primary" id="btn_sub_save" onclick="saveSubHandler()">保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitSubDetail:alloc">
                    <a class="btn btn-primary" id="btn_sub_alloc" onclick="allocBySubHandler()">分配</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitSubDetail:manualAlloc">
                    <a class="btn btn-primary" id="btn_sub_manualAlloc" onclick="manualAllocHandler()">手工分配</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitSubDetail:picking">
                    <a class="btn btn-primary" id="btn_sub_picking" onclick="pickingBySubHandler()">拣货确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitSubDetail:cancelAlloc">
                    <a class="btn btn-primary" id="btn_sub_cancelAlloc" onclick="cancelAllocBySubHandler()">取消分配</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitSubDetail:cancelPicking">
                    <a class="btn btn-primary" id="btn_sub_cancelPicking" onclick="cancelPickingBySubHandler()">取消拣货</a>
                </shiro:hasPermission>
            </div>
            <div id="kitSubDetail_tab-left">
                <table id="kitSubDetailTable" class="table text-nowrap"></table>
            </div>
            <div id="kitSubDetail_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="kitSubDetailForm" method="post" class="form">
                    <input id="kitSubDetail_id" name="id" type="hidden"/>
                    <input id="kitSubDetail_kitNo" name="kitNo" type="hidden"/>
                    <input id="kitSubDetail_ownerCode" name="ownerCode" type="hidden"/>
                    <input id="kitSubDetail_orgId" name="orgId" type="hidden"/>
                    <input id="kitSubDetail_headerId" name="headerId" type="hidden"/>
                    <input id="kitSubDetail_qtyBomEa" name="qtyBomEa" type="hidden"/>
                    <div class="tabs-container" style="margin: 0 0 10px 0;">
                        <ul id="kitSubDetailTab" class="nav nav-tabs">
                            <li class="active"><a data-toggle="tab" href="#kitSubDetailBaseInfo" aria-expanded="true">基本信息</a></li>
                            <li class=""><a data-toggle="tab" href="#kitSubDetailLotAttInfo" aria-expanded="true">批次信息</a></li>
                            <li class=""><a data-toggle="tab" href="#kitSubDetailReserveInfo" aria-expanded="true">预留信息</a></li>
                        </ul>
                        <div class="tab-content">
                            <div id="kitSubDetailBaseInfo" class="tab-pane fade in active">
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">行号</label></td>
                                        <td class="width-25"><label class="pull-left">状态</label></td>
                                        <td class="width-25"><label class="pull-left">子件编码</label></td>
                                        <td class="width-25"><label class="pull-left">子件名称</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="kitSubDetail_subLineNo" name="subLineNo" class="form-control" readonly/>
                                        </td>
                                        <td class="width-25">
                                            <select id="kitSubDetail_status" name="status" class="form-control required" disabled>
                                                <option></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_SUB_KIT_STATUS')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/kit/banQinCdWhBomHeader/grid" title="选择子件" cssClass="form-control required"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                           displayFieldId="kitSubDetail_subSkuCode" displayFieldName="subSkuCode" displayFieldKeyName="subSkuCode" displayFieldValue=""
                                                           selectButtonId="kitSubDetail_subSkuSelectId" deleteButtonId="kitSubDetail_subSkuDeleteId"
                                                           fieldLabels="商品编码|商品名称" fieldKeys="parentSkuCode|parentSkuName"
                                                           searchLabels="商品编码|商品名称" searchKeys="parentSkuCode|parentSkuName">
                                            </sys:popSelect>
                                        <td class="width-25">
                                            <input id="kitSubDetail_subSkuName" name="subSkuName" class="form-control" readonly/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">包装规格</label></td>
                                        <td class="width-25"><label class="pull-left">包装单位</label></td>
                                        <td class="width-25"><label class="pull-left">父件行号</label></td>
                                        <td class="width-25"><label class="pull-left">子件类型</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control required"
                                                           fieldId="kitSubDetail_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue="" allowInput="false"
                                                           displayFieldId="kitSubDetail_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                                           selectButtonId="kitSubDetail_packSelectId" deleteButtonId="kitSubDetail_packDeleteId"
                                                           fieldLabels="包装代码|包装规格" fieldKeys="cdpaCode|cdpaFormat"
                                                           searchLabels="包装代码|包装规格" searchKeys="cdpaCode|cdpaFormat">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                                           fieldId="kitSubDetail_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue=""
                                                           displayFieldId="kitSubDetail_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                                           selectButtonId="kitSubDetail_uomSelectId" deleteButtonId="kitSubDetail_uomDeleteId"
                                                           queryParams="packCode" queryParamValues="kitParentDetail_packCode"
                                                           fieldLabels="包装单位|数量|描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                           searchLabels="包装单位|描述" searchKeys="cdprUnitLevel|cdprDesc">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25"><input id="kitSubDetail_parentLineNo" name="parentLineNo" class="form-control" readonly/></td>
                                        <td class="width-25">
                                            <select id="kitSubDetail_subSkuType" name="subSkuType" class="form-control required" disabled>
                                                <option></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_SUB_SKU_TYPE')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">待加工数</label></td>
                                        <td class="width-25"><label class="pull-left">待加工数EA</label></td>
                                        <td class="width-25"><label class="pull-left">分配数</label></td>
                                        <td class="width-25"><label class="pull-left">分配数EA</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><input id="kitSubDetail_qtyPlanUom" name="qtyPlanUom" class="form-control" readonly/></td>
                                        <td class="width-25"><input id="kitSubDetail_qtyPlanEa" name="qtyPlanEa" class="form-control" readonly/></td>
                                        <td class="width-25"><input id="kitSubDetail_qtyAllocUom" name="qtyAllocUom" class="form-control" readonly/></td>
                                        <td class="width-25"><input id="kitSubDetail_qtyAllocEa" name="qtyAllocEa" class="form-control" readonly/></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">拣货数</label></td>
                                        <td class="width-25"><label class="pull-left">拣货数EA</label></td>
                                        <td class="width-25"><label class="pull-left">已加工数</label></td>
                                        <td class="width-25"><label class="pull-left">已加工数EA</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><input id="kitSubDetail_qtyPkUom" name="qtyPkUom" class="form-control" readonly/></td>
                                        <td class="width-25"><input id="kitSubDetail_qtyPkEa" name="qtyPkEa" class="form-control" readonly/></td>
                                        <td class="width-25"><input id="kitSubDetail_qtyKitUom" name="qtyKitUom" class="form-control" readonly/></td>
                                        <td class="width-25"><input id="kitSubDetail_qtyKitEa" name="qtyKitEa" class="form-control" readonly/></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left asterisk">库存周转规则</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">分配规则</label></td>
                                        <td class="width-25"><label class="pull-left">区域</label></td>
                                        <td class="width-25"><label class="pull-left">库区</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRuleRotationHeader/grid" title="选择库存周转规则" cssClass="form-control required"
                                                           fieldId="kitSubDetail_rotationRule" fieldName="rotationRule" fieldKeyName="ruleCode" fieldValue="" allowInput="true"
                                                           displayFieldId="kitSubDetail_rotationRuleName" displayFieldName="rotationRuleName" displayFieldKeyName="ruleName" displayFieldValue=""
                                                           selectButtonId="kitSubDetail_rotationRuleSelectId" deleteButtonId="kitSubDetail_rotationRuleDeleteId"
                                                           fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                                           searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRuleAllocHeader/grid" title="选择分配规则" cssClass="form-control required"
                                                           fieldId="kitSubDetail_allocRule" fieldName="allocRule" fieldKeyName="ruleCode" fieldValue="" allowInput="true"
                                                           displayFieldId="kitSubDetail_allocRuleName" displayFieldName="allocRuleName" displayFieldKeyName="ruleName" displayFieldValue=""
                                                           selectButtonId="kitSubDetail_allocRuleSelectId" deleteButtonId="kitSubDetail_allocRuleDeleteId"
                                                           fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                                           searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhArea/grid" title="选择区域" cssClass="form-control"
                                                           fieldId="kitSubDetail_areaCode" fieldName="areaCode" fieldKeyName="areaCode" fieldValue="" allowInput="true"
                                                           displayFieldId="kitSubDetail_areaName" displayFieldName="areaName" displayFieldKeyName="areaName" displayFieldValue=""
                                                           selectButtonId="kitSubDetail_areaSelectId" deleteButtonId="kitSubDetail_areaDeleteId"
                                                           fieldLabels="区域编码|区域名称" fieldKeys="areaCode|areaName"
                                                           searchLabels="区域编码|区域名称" searchKeys="areaCode|areaName">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择库区" cssClass="form-control"
                                                           fieldId="kitSubDetail_zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue="" allowInput="true"
                                                           displayFieldId="kitSubDetail_zoneName" displayFieldName="zoneName" displayFieldKeyName="zoneName" displayFieldValue=""
                                                           selectButtonId="kitSubDetail_zoneSelectId" deleteButtonId="kitSubDetail_zoneDeleteId"
                                                           fieldLabels="库区编码|库区名称" fieldKeys="zoneCode|zoneName"
                                                           searchLabels="库区编码|库区名称" searchKeys="zoneCode|zoneName"
                                                           queryParams="areaCode" queryParamValues="kitSubDetail_areaCode">
                                            </sys:popSelect>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">库位</label></td>
                                        <td class="width-25"><label class="pull-left">跟踪号</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                           displayFieldId="kitSubDetail_locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue=""
                                                           selectButtonId="kitSubDetail_locSelectId" deleteButtonId="kitSubDetail_locDeleteId"
                                                           fieldLabels="库位编码|库区编码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                           searchLabels="库位编码" searchKeys="locCode"
                                                           queryParams="zoneCode" queryParamValues="kitSubDetail_zoneCode">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25"><input id="kitSubDetail_traceId" name="traceId" class="form-control"/></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div id="kitSubDetailLotAttInfo" class="tab-pane fade">
                                <table id="kitSubDetailLotAttTab" class="bq-table"></table>
                            </div>
                            <div id="kitSubDetailReserveInfo" class="tab-pane fade">
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">自定义1</label></td>
                                        <td class="width-25"><label class="pull-left">自定义2</label></td>
                                        <td class="width-25"><label class="pull-left">自定义3</label></td>
                                        <td class="width-25"><label class="pull-left">自定义4</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><input id="kitSubDetail_def1" name="def1" class="form-control"/></td>
                                        <td class="width-25"><input id="kitSubDetail_def2" name="def2" class="form-control"/></td>
                                        <td class="width-25"><input id="kitSubDetail_def3" name="def3" class="form-control"/></td>
                                        <td class="width-25"><input id="kitSubDetail_def4" name="def4" class="form-control"/></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">自定义5</label></td>
                                        <td class="width-25" colspan="4"><label class="pull-left">备注</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><input id="kitSubDetail_def5" name="def5" class="form-control"/></td>
                                        <td class="width-25" colspan="4"><input id="kitSubDetail_remarks" name="remarks" class="form-control"/></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
        <div id="kitTaskInfo" class="tab-pane fade">
            <div id="kitTaskToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="kit:banQinWmTaskKit:add">
                    <a class="btn btn-primary" id="btn_task_add" onclick="addTaskHandler()">新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmTaskKit:manualAlloc">
                    <a class="btn btn-primary" id="btn_task_save" onclick="saveTaskHandler()">保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmTaskKit:picking">
                    <a class="btn btn-primary" id="btn_task_picking" onclick="pickingByTaskHandler()">拣货确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmTaskKit:cancelAlloc">
                    <a class="btn btn-primary" id="btn_task_cancelAlloc" onclick="cancelAllocByTaskHandler()">取消分配</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmTaskKit:cancelPick">
                    <button id="btn_task_cancelPicking" class="btn btn-primary" onclick="cancelPickingByTaskHandler()">取消拣货</button>
                </shiro:hasPermission>
            </div>
            <div id="kitTask_tab-left">
                <table id="kitTaskTable" class="table text-nowrap"></table>
            </div>
            <div id="kitTask_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="kitTaskForm" method="post" class="form">
                    <input id="kitTask_id" name="id" type="hidden"/>
                    <input id="kitTask_kitNo" name="kitNo" type="hidden"/>
                    <input id="kitTask_ownerCode" name="ownerCode" type="hidden"/>
                    <input id="kitTask_headerId" name="headerId" type="hidden"/>
                    <input id="kitTask_orgId" name="orgId" type="hidden"/>
                    <input id="kitTask_uomQty" name="uomQty" type="hidden"/>
                    <input id="kitTask_def1" name="def1" type="hidden"/>
                    <input id="kitTask_def2" name="def2" type="hidden"/>
                    <input id="kitTask_def3" name="def3" type="hidden"/>
                    <input id="kitTask_def4" name="def4" type="hidden"/>
                    <input id="kitTask_def5" name="def5" type="hidden"/>
                    <input id="kitTask_skuCodeParam" type="hidden"/>
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td class="width-25"><label class="pull-left">加工任务ID</label></td>
                            <td class="width-25"><label class="pull-left">状态</label></td>
                            <td class="width-25"><label class="pull-left">子件行号</label></td>
                            <td class="width-25"><label class="pull-left">父件行号</label></td>
                        </tr>
                        <tr>
                            <td class="width-25"><input id="kitTask_kitTaskId" name="kitTaskId" class="form-control" readonly/></td>
                            <td class="width-25">
                                <select id="kitTask_status" name="status" class="form-control" disabled>
                                    <option></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_SUB_KIT_STATUS')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-25"><input id="kitTask_subLineNo" name="subLineNo" class="form-control" readonly/></td>
                            <td class="width-25"><input id="kitTask_parentLineNo" name="parentLineNo" class="form-control" readonly/></td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left asterisk">子件</label></td>
                            <td class="width-25"><label class="pull-left">包装规格</label></td>
                            <td class="width-25"><label class="pull-left asterisk">包装单位</label></td>
                            <td class="width-25"><label class="pull-left">批次号</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/inventory/banQinWmInvLotLoc/data" title="选择商品批次库位库存" cssClass="form-control required"
                                               fieldId="kitTask_subSkuCode" fieldName="subSkuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                               displayFieldId="kitTask_subSkuName" displayFieldName="subSkuName" displayFieldKeyName="skuName" displayFieldValue=""
                                               selectButtonId="kitTask_subSkuSelectId" deleteButtonId="kitTask_subSkuDeleteId"
                                               fieldLabels="货主编码|货主名称|商品编码|商品名称|批次号|库位|跟踪号|库存数|库存可用数|冻结数|分配数|拣货数|上架待出数|移动待出数|补货待出数|批次属性01|批次属性02|批次属性03|批次属性04|批次属性05|批次属性05|批次属性07|批次属性08|批次属性09|批次属性10|批次属性11|批次属性12"
                                               fieldKeys="ownerCode|ownerName|skuCode|skuName|lotNum|locCode|traceId|qty|qtyAvailable|qtyHold|qtyAlloc|qtyPk|qtyPaOut|qtyMvOut|qtyRpOut|lotAtt01|lotAtt02|lotAtt03|lotAtt04|lotAtt05|lotAtt06|lotAtt07|lotAtt08|lotAtt09|lotAtt10|lotAtt11|lotAtt12"
                                               searchLabels="批次号|库位|跟踪号" searchKeys="lotNum|locCode|traceId" inputSearchKey="codeAndName"
                                               queryParams="ownerCode|skuCode" queryParamValues="ownerCode|kitTask_skuCodeParam"
                                               concatId="kitTask_lotNum,kitTask_locCode,kitTask_traceId,kitTask_packCode,kitTask_packDesc,kitTask_uom,kitTask_uomDesc,kitTask_qtyUom,kitTask_qtyEa,kitTask_uomQty"
                                               concatName="lotNum,locCode,traceId,packCode,packDesc,printUom,uomDesc,qtyAvailable,qtyAvailable,uomQty">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control"
                                               fieldId="kitTask_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue=""
                                               displayFieldId="kitTask_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                               selectButtonId="kitTask_packSelectId" deleteButtonId="kitTask_packDeleteId"
                                               fieldLabels="包装代码|包装规格" fieldKeys="cdpaCode|cdpaFormat"
                                               searchLabels="包装代码|包装规格" searchKeys="cdpaCode|cdpaFormat" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                               fieldId="kitTask_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                               displayFieldId="kitTask_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                               selectButtonId="kitTask_uomSelectId" deleteButtonId="kitTask_uomDeleteId"
                                               queryParams="packCode" queryParamValues="kitTask_packCode"
                                               fieldLabels="包装单位|数量|描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                               searchLabels="包装单位|描述" searchKeys="cdprUnitLevel|cdprDesc" inputSearchKey="codeAndName">
                                </sys:popSelect>
                            </td>
                            <td class="width-25"><input id="kitTask_lotNum" name="lotNum" class="form-control" readonly/></td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">库位</label></td>
                            <td class="width-25"><label class="pull-left">跟踪号</label></td>
                            <td class="width-25"><label class="pull-left asterisk">目标库位</label></td>
                            <td class="width-25"><label class="pull-left asterisk">目标跟踪号</label></td>
                        </tr>
                        <tr>
                            <td class="width-25"><input id="kitTask_locCode" name="locCode" class="form-control" readonly/></td>
                            <td class="width-25"><input id="kitTask_traceId" name="traceId" class="form-control" readonly/></td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                               displayFieldId="kitTask_toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                               selectButtonId="kitTask_toLocSelectId" deleteButtonId="kitTask_toLocDeleteId"
                                               fieldLabels="库位编码|库区编码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                               searchLabels="库位编码" searchKeys="locCode">
                                </sys:popSelect>
                            </td>
                            <td class="width-25"><input id="kitTask_toId" name="toId" class="form-control"/></td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">拣货人</label></td>
                            <td class="width-25"><label class="pull-left">拣货时间</label></td>
                            <td class="width-25"><label class="pull-left">加工人</label></td>
                            <td class="width-25"><label class="pull-left">加工时间</label></td>
                        </tr>
                        <tr>
                            <td class="width-25"><input id="kitTask_pickOp" name="pickOp" class="form-control" readonly/></td>
                            <td class="width-25"><input id="kitTask_pickTime" name="pickTime" class="form-control" readonly/></td>
                            <td class="width-25"><input id="kitTask_kitOp" name="kitOp" class="form-control" readonly/></td>
                            <td class="width-25"><input id="kitTask_kitTime" name="kitTime" class="form-control" readonly/></td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">加工操作行号</label></td>
                            <td class="width-25"><label class="pull-left asterisk">数量</label></td>
                            <td class="width-25"><label class="pull-left">数量EA</label></td>
                        </tr>
                        <tr>
                            <td class="width-25"><input id="kitTask_kitLineNo" name="kitLineNo" class="form-control" readonly/></td>
                            <td class="width-25"><input id="kitTask_qtyUom" name="qtyUom" class="form-control" onkeyup="bq.numberValidator(this, 2, 0);kitTaskQtyChange()"/></td>
                            <td class="width-25"><input id="kitTask_qtyEa" name="qtyEa" class="form-control" readonly/></td>
                        </tr>
                        </tbody>
                    </table>
                </form:form>
            </div>
        </div>
        <div id="kitDetailInfo" class="tab-pane fade">
            <div id="kitDetailToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="kit:banQinWmKitResultDetail:kitConfirm">
                    <a class="btn btn-primary" id="btn_result_kitConfirm" onclick="kitConfirmHandler()">加工确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitResultDetail:cancelKit">
                    <a class="btn btn-primary" id="btn_result_cancelKit" onclick="cancelKitHandler()">取消加工</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitResultDetail:createTask">
                    <a class="btn btn-primary" id="btn_result_createTaskPa" onclick="createTaskPaHandler()">生成上架任务</a>
                </shiro:hasPermission>
            </div>
            <div id="kitDetail_tab-left">
                <table id="kitDetailTable" class="table text-nowrap"></table>
            </div>
            <div id="kitDetail_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="kitDetailForm" method="post" class="form">
                    <input id="kitDetail_id" name="id" type="hidden"/>
                    <input id="kitDetail_kitNo" name="kitNo" type="hidden"/>
                    <input id="kitDetail_ownerCode" name="ownerCode" type="hidden"/>
                    <input id="kitDetail_paId" name="paId" type="hidden"/>
                    <input id="kitDetail_orgId" name="orgId" type="hidden"/>
                    <input id="kitDetail_headerId" name="headerId" type="hidden"/>
                    <input id="kitDetail_uomQty" name="uomQty" type="hidden"/>
                    <input id="kitDetail_qtyCompleteEa" name="qtyCompleteEa" type="hidden"/>
                    <div class="tabs-container" style="margin: 0 0 10px 0;">
                        <ul id="kitDetailTab" class="nav nav-tabs">
                            <li class="active"><a data-toggle="tab" href="#kitDetailBaseInfo" aria-expanded="true">基本信息</a></li>
                            <li class=""><a data-toggle="tab" href="#kitDetailLotAttInfo" aria-expanded="true">批次信息</a></li>
                            <li class=""><a data-toggle="tab" href="#kitDetailReserveInfo" aria-expanded="true">预留信息</a></li>
                            <li class=""><a data-toggle="tab" href="#kitDetailStepInfo" aria-expanded="true">加工工序</a></li>
                        </ul>
                        <div class="tab-content">
                            <div id="kitDetailBaseInfo" class="tab-pane fade in active">
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">加工行号</label></td>
                                        <td class="width-25"><label class="pull-left">状态</label></td>
                                        <td class="width-25"><label class="pull-left">父件行号</label></td>
                                        <td class="width-25"><label class="pull-left">父件</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><input id="kitDetail_kitLineNo" name="kitLineNo" class="form-control" readonly/></td>
                                        <td class="width-25">
                                            <select id="kitDetail_status" name="status" class="form-control" disabled>
                                                <option></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_SUB_KIT_STATUS')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25"><input id="kitDetail_parentLineNo" name="parentLineNo" class="form-control" readonly/></td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/kit/banQinCdWhBomHeader/grid" title="选择父件" cssClass="form-control"
                                                           fieldId="kitDetail_parentSkuCode" fieldName="parentSkuCode" fieldKeyName="parentSkuCode" fieldValue=""
                                                           displayFieldId="kitDetail_parentSkuName" displayFieldName="parentSkuName" displayFieldKeyName="parentSkuName" displayFieldValue=""
                                                           selectButtonId="kitDetail_parentSkuSelectId" deleteButtonId="kitDetail_parentSkuDeleteId"
                                                           fieldLabels="商品编码|商品名称" fieldKeys="parentSkuCode|parentSkuName"
                                                           searchLabels="商品编码|商品名称" searchKeys="parentSkuCode|parentSkuName"
                                                           concatId="kitDetail_packCode,kitDetail_packDesc,kitDetail_uom,kitDetail_uomDesc,kitDetail_uomQty"
                                                           concatName="packCode,packDesc,uom,uomDesc,uomQty"
                                                           queryParams="ownerCode|kitType" queryParamValues="ownerCode|kitType" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">包装规格</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">包装单位</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">加工台</label></td>
                                        <td class="width-25"><label class="pull-left">跟踪号</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control"
                                                           fieldId="kitDetail_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue=""
                                                           displayFieldId="kitDetail_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                                           selectButtonId="kitDetail_packSelectId" deleteButtonId="kitDetail_packDeleteId"
                                                           fieldLabels="包装代码|包装规格" fieldKeys="cdpaCode|cdpaFormat"
                                                           searchLabels="包装代码|包装规格" searchKeys="cdpaCode|cdpaFormat" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                                           fieldId="kitDetail_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                                           displayFieldId="kitDetail_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                                           selectButtonId="kitDetail_uomSelectId" deleteButtonId="kitDetail_uomDeleteId"
                                                           queryParams="packCode" queryParamValues="kitDetail_packCode"
                                                           fieldLabels="包装单位|数量|描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                           searchLabels="包装单位|描述" searchKeys="cdprUnitLevel|cdprDesc" inputSearchKey="codeAndName">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择加工台" cssClass="form-control required"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                           displayFieldId="kitDetail_kitLoc" displayFieldName="kitLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                           selectButtonId="kitDetail_kitLocSelectId" deleteButtonId="kitDetail_kitLocDeleteId"
                                                           fieldLabels="库位编码|库区编码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                           searchLabels="库位编码|库区编码" searchKeys="locCode|zoneCode"
                                                           queryParams="locUseType" queryParamValues="locUseType">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25"><input id="kitDetail_kitTraceId" name="kitTraceId" class="form-control"/></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">批次号</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">子件指定规则</label></td>
                                        <td class="width-25"><label class="pull-left">上架库位指定规则</label></td>
                                        <td class="width-25"><label class="pull-left">上架规则</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><input id="kitDetail_lotNum" name="lotNum" class="form-control" readonly/></td>
                                        <td class="width-25">
                                            <select id="kitDetail_subSelectCode" name="subSelectCode" class="form-control required">
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_SUB_SELECT_CODE')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <select id="kitDetail_reserveCode" name="reserveCode" class="form-control">
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_KIT_RESERVE_CODE')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRulePaHeader/grid" title="选择上架规则" cssClass="form-control"
                                                           fieldId="kitDetail_paRule" fieldName="paRule" fieldKeyName="ruleCode" fieldValue="" allowInput="true"
                                                           displayFieldId="kitDetail_paRuleName" displayFieldName="paRuleName" displayFieldKeyName="ruleName" displayFieldValue=""
                                                           selectButtonId="kitDetail_paRuleSelectId" deleteButtonId="kitDetail_paRuleDeleteId"
                                                           fieldLabels="上架规则代码|上架规则名称" fieldKeys="ruleCode|ruleName"
                                                           searchLabels="上架规则代码|上架规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName">
                                            </sys:popSelect>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">预加工数</label></td>
                                        <td class="width-25"><label class="pull-left">预加工数EA</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">可加工数</label></td>
                                        <td class="width-25"><label class="pull-left">可加工数EA</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><input id="kitDetail_qtyPlanUom" name="qtyPlanUom" class="form-control" readonly/></td>
                                        <td class="width-25"><input id="kitDetail_qtyPlanEa" name="qtyPlanEa" class="form-control" readonly/></td>
                                        <td class="width-25"><input id="kitDetail_qtyCurrentKitUom" name="qtyCurrentKitUom" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0);kitDetailQtyChange()"/></td>
                                        <td class="width-25"><input id="kitDetail_qtyCurrentKitEa" name="qtyCurrentKitEa" class="form-control" readonly/></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div id="kitDetailLotAttInfo" class="tab-pane fade">
                                <table id="kitDetailLotAttTab" class="bq-table"></table>
                            </div>
                            <div id="kitDetailReserveInfo" class="tab-pane fade">
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">自定义1</label></td>
                                        <td class="width-25"><label class="pull-left">自定义2</label></td>
                                        <td class="width-25"><label class="pull-left">自定义3</label></td>
                                        <td class="width-25"><label class="pull-left">自定义4</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><input id="kitDetail_def1" name="def1" class="form-control"/></td>
                                        <td class="width-25"><input id="kitDetail_def2" name="def2" class="form-control"/></td>
                                        <td class="width-25"><input id="kitDetail_def3" name="def3" class="form-control"/></td>
                                        <td class="width-25"><input id="kitDetail_def4" name="def4" class="form-control"/></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">自定义5</label></td>
                                        <td class="width-25" colspan="4"><label class="pull-left">备注</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><input id="kitDetail_def5" name="def5" class="form-control"/></td>
                                        <td class="width-25" colspan="4"><input id="kitDetail_remarks" name="remarks" class="form-control"/></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div id="kitDetailStepInfo" class="tab-pane fade">
                                <table id="kitDetailStepTable" class="table table-condensed text-nowrap"></table>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
        <div id="paTaskInfo" class="tab-pane fade">
            <div id="paTaskToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="kit:banQinWmKitResultDetail:removePaTask">
                    <a class="btn btn-danger" id="btn_pa_removeTaskPa" onclick="removePaTask()">删除上架任务</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinWmKitResultDetail:paConfirm">
                    <a class="btn btn-primary" id="btn_pa_putawayConfirm" onclick="confirmPaTask()">上架确认</a>
                </shiro:hasPermission>
            </div>
            <div id="paTask_tab-left">
                <table id="paTaskTable" class="table text-nowrap"></table>
            </div>
            <div id="paTask_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="paTaskForm" method="post" class="form">
                    <input type="hidden" id="paTask_id" name="id"/>
                    <input type="hidden" id="paTask_orgId" name="orgId"/>
                    <input type="hidden" id="paTask_qtyPaEa" name="qtyPaEa"/>
                    <input type="hidden" id="paTask_orderType" name="orderType"/>
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">上架任务Id</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">行号</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">状态</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">单据号</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="paTask_paId" name="paId" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <input id="paTask_lineNo" name="lineNo" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <select id="paTask_status" name="status" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_TASK_STATUS')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-25">
                                <input id="paTask_orderNo" name="orderNo" htmlEscape="false" class="form-control" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">货主</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">商品编码</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">商品名称</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">包装规格</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                               fieldId="paTask_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                               displayFieldId="paTask_ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="paTask_ownerSelectId" deleteButtonId="paTask_ownerDeleteId"
                                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                               displayFieldId="paTask_skuCode" displayFieldName="skuCode" displayFieldKeyName="skuCode" displayFieldValue=""
                                               selectButtonId="paTask_skuSelectId" deleteButtonId="paTask_skuDeleteId"
                                               fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                               searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input id="paTask_skuName" name="skuName" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control"
                                               fieldId="paTask_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue=""
                                               displayFieldId="paTask_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                               selectButtonId="paTask_packSelectId" deleteButtonId="paTask_packDeleteId"
                                               fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                               searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" disabled="disabled">
                                </sys:popSelect>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">包装单位</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">批次号</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">源库位</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">源跟踪号</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control"
                                               fieldId="paTask_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue=""
                                               displayFieldId="paTask_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                               selectButtonId="paTask_uomSelectId" deleteButtonId="paTask_uomDeleteId"
                                               fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                               searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input id="paTask_lotNum" name="lotNum" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择源库位" cssClass="form-control"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                               displayFieldId="paTask_fmLoc" displayFieldName="fmLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                               selectButtonId="paTask_fmLocSelectId" deleteButtonId="paTask_fmLocDeleteId"
                                               fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                               searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input id="paTask_fmId" name="fmId" htmlEscape="false" class="form-control" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">上架库位指定规则</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">上架规则</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">上架时间</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">上架人</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <select id="paTask_reserveCode" name="reserveCode" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_RESERVE_CODE')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRulePaHeader/grid" title="选择上架规则" cssClass="form-control"
                                               fieldId="paTask_paRule" fieldName="paRule" fieldKeyName="ruleCode" fieldValue=""
                                               displayFieldId="paTask_paRuleName" displayFieldName="paRuleName" displayFieldKeyName="ruleName" displayFieldValue=""
                                               selectButtonId="paTask_paRuleSelectId" deleteButtonId="paTask_paRuleDeleteId"
                                               fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                               searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <div class='input-group form_datetime' id='paTask_paTimeF'>
                                    <input id="paTask_paTime" name="paTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td class="width-25">
                                <input id="paTask_paOp" name="paOp" htmlEscape="false" class="form-control" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">推荐库位</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">上架库位</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">上架跟踪号</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">上架数</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择推荐库位" cssClass="form-control"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                               displayFieldId="paTask_suggestLoc" displayFieldName="suggestLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                               selectButtonId="paTask_suggestLocSelectId" deleteButtonId="paTask_suggestLocDeleteId"
                                               fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                               searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择上架库位" cssClass="form-control required"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                               displayFieldId="paTask_toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                               selectButtonId="paTask_toLocSelectId" deleteButtonId="paTask_toLocDeleteId"
                                               fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                               searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input id="paTask_toId" name="toId" htmlEscape="false" class="form-control required" maxlength="64">
                            </td>
                            <td class="width-25">
                                <input id="paTask_qtyPaUom" name="qtyPaUom" htmlEscape="false" class="form-control" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">上架数EA</label>
                            </td>
                            <td class="width-25" colspan="3">
                                <label class="pull-left">备注</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="paTask_currentPaQtyEa" name="currentPaQtyEa" onkeyup="bq.numberValidator(this, 2, 0);" htmlEscape="false" class="form-control">
                            </td>
                            <td class="width-25" colspan="3">
                                <input id="paTask_remarks" name="remarks" htmlEscape="false" class="form-control" maxlength="64">
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form:form>
            </div>
        </div>
        <div id="cancelAllocInfo" class="tab-pane fade">
            <div id="cancelAllocToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="kit:banQinWmKitResultDetail:reCreateTask">
                    <button id="btn_delAlloc_createTaskPa" class="btn btn-primary" onclick="createTaskPaByDelAllocHandler()">生成上架任务</button>
                </shiro:hasPermission>
            </div>
            <table id="cancelAllocTable" class="table text-nowrap"></table>
        </div>
    </div>
</div>
<!-- 生成上架任务 -->
<div class="modal fade" id="createTaskPaModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 400px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">生成上架任务</h4>
            </div>
            <div class="modal-body">
                <table class="bq-table">
                    <tbody>
                    <tr>
                        <td class="width-20"><input id="isTaskPa" type="checkBox" class="myCheckbox" onclick="isTaskPaChange(this.checked)"></td>
                        <td class="width-80"><label class="pull-left">是否生成上架任务</label></td>
                    </tr>
                    <tr>
                        <td class="width-20"><input type="radio" id="allocLoc" name="loc" class="myRadio"></td>
                        <td class="width-80"><label class="pull-left">推荐分配库位</label></td>
                    </tr>
                    <tr>
                        <td class="width-20"><input type="radio" id="paRuleLoc" name="loc" class="myRadio"></td>
                        <td class="width-80"><label class="pull-left">上架规则计算库位</label></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="createTaskPaConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>