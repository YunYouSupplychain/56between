<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>异常处理单管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmExceptionHandleBillForm.js" %>
</head>
<body>
<div class="hidden">
    <input type="hidden" id="principalType" value="CUSTOMER">
    <input type="hidden" id="settlementType" value="SETTLEMENT"/>
    <input type="hidden" id="customerType" value="OWNER">
    <input type="hidden" id="outletType" value="OUTLET">
    <input type="hidden" id="carrierType" value="CARRIER">
    <input type="hidden" id="shipType" value="SHIP">
    <input type="hidden" id="consigneeType" value="CONSIGNEE">
</div>
<div style="margin-left: 10px;">
    <shiro:hasPermission name="tms:order:tmExceptionHandle:edit">
        <a id="save" class="btn btn-primary" disabled onclick="save()"> 保存</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="tmExceptionHandleBillEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="baseOrgId"/>
    <form:hidden path="recVer"/>
    <form:hidden path="receiptAmount"/>
    <form:hidden path="payAmount"/>
    <form:hidden path="consigneeCode"/>
    <sys:message content="${message}"/>
    <%--基本信息--%>
    <div class="tabs-container" style="min-height: 300px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#basicInfo" aria-expanded="true">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="basicInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">异常单号</label></td>
                        <td class="width-15">
                            <form:input path="billNo" htmlEscape="false" readonly="true" class="form-control "/>
                        </td>
                        <td class="width-10"><label class="pull-right">运输订单号</label></td>
                        <td class="width-15">
                            <sys:grid title="运输订单" url="${ctx}/tms/order/tmTransportOrder/data"
                                      fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                      displayFieldId="transportNo" displayFieldName="transportNo"
                                      displayFieldKeyName="transportNo" displayFieldValue="${tmExceptionHandleBillEntity.transportNo}"
                                      fieldLabels="运输单号|客户单号|订单时间|客户" fieldKeys="transportNo|customerNo|orderTime|customerName"
                                      searchLabels="运输单号|客户单号" searchKeys="transportNo|customerNo"
                                      queryParams="orgId" queryParamValues="orgId"
                                      cssClass="form-control" afterSelect="transportSelect"/>
                        </td>
                        <td class="width-10"><label class="pull-right">客户订单号</label></td>
                        <td class="width-15">
                            <form:input path="customerNo" htmlEscape="false" class="form-control "/>
                        </td>
                        <td class="width-10"><label class="pull-right">派车单号</label></td>
                        <td class="width-15">
                            <sys:grid title="派车单" url="${ctx}/tms/order/tmDispatchOrder/page"
                                      fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                      displayFieldId="dispatchNo" displayFieldName="dispatchNo"
                                      displayFieldKeyName="dispatchNo" displayFieldValue="${tmExceptionHandleBillEntity.dispatchNo}"
                                      fieldLabels="派车单号|派车时间|车牌号|司机" fieldKeys="dispatchNo|dispatchTime|carNo|driver"
                                      searchLabels="派车单号|车牌号|司机" searchKeys="dispatchNo|carNo|driver"
                                      queryParams="orgId" queryParamValues="orgId"
                                      cssClass="form-control" afterSelect="dispatchSelect"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">标签号</label></td>
                        <td class="width-15">
                            <form:input path="labelNo" htmlEscape="false" onkeydown="onKeyPressByCaseCode(event)" class="form-control "/>
                        </td>
                        <td class="width-10"><label class="pull-right">收货人名称</label></td>
                        <td class="width-15">
                            <form:input path="consigneeName" htmlEscape="false" readonly="true" class="form-control "/>
                        </td>
                        <td class="width-10"><label class="pull-right">收货人电话</label></td>
                        <td class="width-15">
                            <form:input path="consigneePhone" htmlEscape="false" readonly="true" class="form-control "/>
                        </td>
                        <td class="width-10"><label class="pull-right">网点</label></td>
                        <td class="width-15">
                            <sys:grid title="派车网点" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="dispatchOutletCode" fieldName="dispatchOutletCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmExceptionHandleBillEntity.outletCode}"
                                      displayFieldId="dispatchOutletName" displayFieldName="dispatchOutletName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmExceptionHandleBillEntity.outletName}"
                                      fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                                      cssClass="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">车牌号</label></td>
                        <td class="width-15">
                            <form:input path="carNo" htmlEscape="false" class="form-control "/>
                        </td>
                        <td class="width-10"><label class="pull-right">司机姓名</label></td>
                        <td class="width-15">
                            <form:input path="driver" htmlEscape="false" class="form-control "/>
                        </td>
                        <td class="width-10"><label class="pull-right">责任方</label></td>
                        <td class="width-15">
                            <form:select path="responsibility" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('responsibility')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">客户</label></td>
                        <td class="width-15">
                            <sys:grid title="客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="customerCode" fieldName="customerCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmExceptionHandleBillEntity.customerCode}"
                                      displayFieldId="customerName" displayFieldName="customerName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmExceptionHandleBillEntity.customerName}"
                                      fieldLabels="客户编码|客户名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="客户编码|客户名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="customerType|baseOrgId"
                                      cssClass="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">运输状况类型</label></td>
                        <td class="width-15">
                            <form:select path="exceptionType" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_EXCEPTION_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">登记人</label></td>
                        <td class="width-15">
                            <sys:grid title="登记人" url="${ctx}/sys/user/popDate"
                                      fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                      displayFieldId="registerPerson" displayFieldName="registerPerson"
                                      displayFieldKeyName="loginName" displayFieldValue="${tmExceptionHandleBillEntity.registerPerson}"
                                      fieldLabels="登录名|工号|姓名" fieldKeys="loginName|no|name"
                                      searchLabels="登录名|工号|姓名" searchKeys="loginName|no|name"
                                      cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">登记时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='registerTime'>
                                <input type='text' name="registerTime" id="registerTimeValue" class="form-control required"
                                       value="<fmt:formatDate value="${tmExceptionHandleBillEntity.registerTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">发生时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='happenTime'>
                                <input type='text' name="happenTime" id="happenTimeValue" class="form-control"
                                       value="<fmt:formatDate value="${tmExceptionHandleBillEntity.happenTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <form:hidden path="happenSysAreaCode"/>
                        <td class="width-10"><label class="pull-right">发生地点</label></td>
                        <td class="width-15">
                            <sys:area id="happenSysArea" name="happenSysAreaId" value="${tmExceptionHandleBillEntity.happenSysAreaId}"
                                      labelName="happenSysAreaName" labelValue="${tmExceptionHandleBillEntity.happenSysAreaName}"
                                      cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">状态</label></td>
                        <td class="width-15">
                            <form:select path="billStatus" class="form-control" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_EXCEPTION_BILL_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">流程单号</label></td>
                        <td class="width-15">
                            <form:input path="processNo" htmlEscape="false" class="form-control "/>
                        </td>
                        <td class="width-10"><label class="pull-right">费用确认流程单号</label></td>
                        <td class="width-15">
                            <form:input path="feeProcessNo" htmlEscape="false" class="form-control "/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">异常原因</label></td>
                        <td class="width-15" colspan="7">
                            <form:input path="exceptionReason" htmlEscape="false" class="form-control "/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<%--明细信息列表--%>
<div class="tabs-container">
    <ul class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">运输状况处理明细</a></li>
        <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">照片上传明细</a></li>
        <%--<li class=""><a data-toggle="tab" href="#tab-3" aria-expanded="false">费用明细</a></li>--%>
    </ul>
    <div class="tab-content">
        <div id="tab-1" class="tab-pane fade  in  active">
            <div id="detailToolbar" style="margin: 5px 0;">
                <shiro:hasPermission name="tms:order:tmExceptionHandle:detail:add">
                    <a id="detail_add" class="btn btn-primary" disabled onclick="addDetail()"> 新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmExceptionHandle:detail:save">
                    <a id="detail_save" class="btn btn-primary" disabled onclick="saveDetail()"> 保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmExceptionHandle:detail:del">
                    <a id="detail_remove" class="btn btn-danger" disabled onclick="delDetail()"> 删除</a>
                </shiro:hasPermission>
            </div>
            <div class="fixed-table-container" style="padding-bottom: 0;">
                <form id="tmExceptionHandleBillDetailForm">
                    <table class="table well" id="tmExceptionHandleBillDetailTable" data-toolbar="#detailToolbar">
                        <thead>
                        <tr>
                            <th class="hide"></th>
                            <th style="width:36px;vertical-align:middle">
                                <label><input type="checkbox" name="btSelectAll" style="width: 16px;height: 16px;"/></label>
                            </th>
                            <th>运输状况处理人</th>
                            <th>运输状况处理时间</th>
                            <th>运输状况处理描述</th>
                        </tr>
                        </thead>
                        <tbody id="tmExceptionHandleBillDetailList">
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
        <div id="tab-2" class="tab-pane fade">
            <div id="attachmentDetailToolbar" style="margin: 5px 0;">
                <a id="uploadPic" class="btn btn-primary" disabled onclick="openUploadPic()"> 上传</a>
            </div>
            <table id="tmExceptionHandleBillAttachmentDetailTable" class="text-nowrap"></table>
        </div>
        <div id="tab-3" class="tab-pane fade">
            <div id="feeToolbar" style="margin: 5px 0;">
                <shiro:hasPermission name="tms:order:tmExceptionHandle:fee:add">
                    <a id="fee_add" class="btn btn-primary" disabled onclick="addFeeDetail()"> 新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmExceptionHandle:fee:save">
                    <a id="fee_save" class="btn btn-primary" disabled onclick="saveFeeDetail()"> 保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmExceptionHandle:fee:del">
                    <a id="fee_remove" class="btn btn-danger" disabled onclick="delFeeDetail()"> 删除</a>
                </shiro:hasPermission>
            </div>
            <div class="fixed-table-container" style="padding-bottom: 0;">
                <form id="tmExceptionHandleBillFeeForm">
                    <table class="table well" id="tmExceptionHandleBillFeeTable" data-toolbar="#feeToolbar">
                        <thead>
                        <tr>
                            <th class="hide"></th>
                            <th style="width:36px;vertical-align:middle">
                                <label><input type="checkbox" name="btSelectAll" style="width: 16px;height: 16px;"/></label>
                            </th>
                            <th>应收应付</th>
                            <th>责任人</th>
                            <th>费用名称</th>
                            <th>金额</th>
                        </tr>
                        </thead>
                        <tbody id="tmExceptionHandleBillFeeList">
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
<!-- 照片上传弹出框 -->
<div class="modal fade" id="uploadPicModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <form class="form-horizontal" id="fileUploadForm" enctype="multipart/form-data">
        <div class="modal-dialog" style="width:500px">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">上传照片</h4>
                </div>
                <div class="modal-body">
                    <table class="table well">
                        <tbody>
                        <tr>
                            <td class="active" style="width:70%">
                                <input type="file" id="uploadFileName" name="upload"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="uploadPic()">上传</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </form>
</div>
<script type="text/template" id="tmExceptionHandleBillDetailTpl">//<!--
<tr id="tmExceptionHandleBillDetailList{{idx}}">
    <td class="hide">
        <input id="tmExceptionHandleBillDetailList{{idx}}_id" name="tmExceptionHandleBillDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="tmExceptionHandleBillDetailList{{idx}}_delFlag" name="tmExceptionHandleBillDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="tmExceptionHandleBillDetailList{{idx}}_billNo" name="tmExceptionHandleBillDetailList[{{idx}}].billNo" type="hidden" value="{{row.billNo}}"/>
        <input id="tmExceptionHandleBillDetailList{{idx}}_orgId" name="tmExceptionHandleBillDetailList[{{idx}}].orgId" type="hidden" value="{{row.orgId}}"/>
        <input id="tmExceptionHandleBillDetailList{{idx}}_baseOrgId" name="tmExceptionHandleBillDetailList[{{idx}}].baseOrgId" type="hidden" value="{{row.baseOrgId}}"/>
        <input id="tmExceptionHandleBillDetailList{{idx}}_recVer" name="tmExceptionHandleBillDetailList[{{idx}}].recVer" type="hidden" value="{{row.recVer}}"/>
    </td>
    <td style="vertical-align: middle">
        <input type="checkbox" name="btSelectItem" data-index="{{idx}}" style="width: 16px;height: 16px;"/>
    </td>
    <td>
        <input id="tmExceptionHandleBillDetailList{{idx}}_handleUser" readonly name="tmExceptionHandleBillDetailList[{{idx}}].handleUser" type="text" value="{{row.handleUser}}"    class="form-control "/>
    </td>
    <td>
        <div class='input-group form_datetime' id="tmExceptionHandleBillDetailList{{idx}}_handleTime">
            <input type='text' readonly name="tmExceptionHandleBillDetailList[{{idx}}].handleTime" id="tmExceptionHandleBillDetailList{{idx}}_handleTimeValue" class="form-control "  value="{{row.handleTime}}"/>
            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
        </div>
    </td>
    <td>
        <input id="tmExceptionHandleBillDetailList{{idx}}_handleDescription" name="tmExceptionHandleBillDetailList[{{idx}}].handleDescription" AUTOCOMPLETE="off" type="text" value="{{row.handleDescription}}"    class="form-control "/>
    </td>
</tr>//-->
</script>
<script type="text/template" id="tmExceptionHandleBillFeeTpl">//<!--
  <tr id="tmExceptionHandleBillFeeList{{idx}}">
    <td class="hide">
        <input id="tmExceptionHandleBillFeeList{{idx}}_id" name="tmExceptionHandleBillFeeList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="tmExceptionHandleBillFeeList{{idx}}_delFlag" name="tmExceptionHandleBillFeeList[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="tmExceptionHandleBillFeeList{{idx}}_billNo" name="tmExceptionHandleBillFeeList[{{idx}}].billNo" type="hidden" value="{{row.billNo}}"/>
        <input id="tmExceptionHandleBillFeeList{{idx}}_orgId" name="tmExceptionHandleBillFeeList[{{idx}}].orgId" type="hidden" value="{{row.orgId}}"/>
        <input id="tmExceptionHandleBillFeeList{{idx}}_baseOrgId" name="tmExceptionHandleBillFeeList[{{idx}}].baseOrgId" type="hidden" value="{{row.baseOrgId}}"/>
        <input id="tmExceptionHandleBillFeeList{{idx}}_recVer" name="tmExceptionHandleBillFeeList[{{idx}}].recVer" type="hidden" value="{{row.recVer}}"/>
    </td>
    <td style="vertical-align: middle">
        <input type="checkbox" name="btSelectItem" data-index="{{idx}}" style="width: 16px;height: 16px;"/>
    </td>
    <td>
        <select id="tmExceptionHandleBillFeeList{{idx}}_rpFlag" name="tmExceptionHandleBillFeeList[{{idx}}].rpFlag" data-value="{{row.rpFlag}}" class="form-control m-b  required">
            <option value=""></option>
            <c:forEach items="${fns:getDictList('RP_FLAG')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td>
        <sys:grid title="责任人" url="${ctx}/tms/basic/tmTransportObj/grid"
              fieldId="tmExceptionHandleBillFeeList{{idx}}_liabilitySysUserCode" fieldName="tmExceptionHandleBillFeeList[{{idx}}].liabilitySysUserCode" fieldKeyName="transportObjCode" fieldValue="{{row.liabilitySysUserCode}}"
              displayFieldId="tmExceptionHandleBillFeeList{{idx}}_liabilitySysUserName" displayFieldName="tmExceptionHandleBillFeeList[{{idx}}].liabilitySysUserName" displayFieldKeyName="transportObjName" displayFieldValue="{{row.liabilitySysUserName}}"
              fieldLabels="责任人编码|责任人名称" fieldKeys="transportObjCode|transportObjName"
              searchLabels="责任人编码|责任人名称" searchKeys= "transportObjCode|transportObjName"
              queryParams="orgId" queryParamValues="baseOrgId"
              cssClass="form-control required"/>
    </td>
    <td>
        <select id="tmExceptionHandleBillFeeList{{idx}}_chargeName" name="tmExceptionHandleBillFeeList[{{idx}}].chargeName" data-value="{{row.chargeName}}" class="form-control m-b  required">
            <option value=""></option>
            <c:forEach items="${fns:getDictList('TMS_EXCEPTION_CHARGE_TYPE')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td>
        <input id="tmExceptionHandleBillFeeList{{idx}}_amount" name="tmExceptionHandleBillFeeList[{{idx}}].amount" AUTOCOMPLETE="off" type="text" value="{{row.amount}}"  onkeyup="onlyNumber(this)" onblur="onlyNumber(this)"  class="form-control required"/>
    </td>
</tr>//-->
</script>
</body>
</html>