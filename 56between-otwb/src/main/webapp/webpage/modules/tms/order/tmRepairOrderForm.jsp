<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>维修工单管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmRepairOrderForm.js" %>
</head>
<body>
<div class="hidden">
    <input type="hidden" id="customerType" value="OWNER">
    <input type="hidden" id="repairType" value="REPAIR">
</div>
<form:form id="inputForm" modelAttribute="tmRepairOrderEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="baseOrgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <fieldset>
        <legend>报修信息</legend>
        <shiro:hasPermission name="order:tmRepairOrder:save1">
            <a id="save1" class="btn btn-primary" onclick="save1()"> 保存</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="order:tmRepairOrder:imgList">
            <a id="imgList" class="btn btn-primary" onclick="imgList()"> 图片明细</a>
        </shiro:hasPermission>
        <table class="table table-bordered" style="margin-bottom: 5px;">
            <tr>
                <td style="width:8%;"><label class="pull-left">维修单号：</label></td>
                <td style="width:12%;">
                    <form:input path="repairNo" htmlEscape="false" class="form-control " readonly="true"/>
                </td>
                <td style="width:8%;"><label class="pull-right"><font color="red">*</font>报修时间：</label></td>
                <td style="width:12%;">
                    <div class='input-group form_datetime' id='orderTime'>
                        <input type='text' name="orderTime" class="form-control required"
                               value="<fmt:formatDate value="${tmRepairOrderEntity.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </td>
                <td style="width:8%;"><label class="pull-right">客户：</label></td>
                <td style="width:12%;">
                    <sys:grid title="客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                              fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="transportObjCode" fieldValue="${tmRepairOrderEntity.ownerCode}"
                              displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="transportObjName" displayFieldValue="${tmRepairOrderEntity.ownerName}"
                              fieldLabels="客户编码|客户名称" fieldKeys="transportObjCode|transportObjName"
                              searchLabels="客户编码|客户名称" searchKeys="transportObjCode|transportObjName"
                              queryParams="transportObjType|orgId" queryParamValues="customerType|baseOrgId"
                              cssClass="form-control "/>
                </td>
                <td style="width:8%;"><label class="pull-right"><font color="red">*</font>车牌号：</label></td>
                <td style="width:12%;">
                    <sys:grid title="车辆" url="${ctx}/tms/basic/tmVehicle/grid"
                              fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                              displayFieldId="carNo" displayFieldName="carNo" displayFieldKeyName="carNo" displayFieldValue="${tmRepairOrderEntity.carNo}"
                              fieldLabels="车牌号|承运商|设备类型" fieldKeys="carNo|carrierName|transportEquipmentTypeName"
                              searchLabels="车牌号" searchKeys="carNo" queryParams="carrierCode|orgId" queryParamValues="carrierCode|baseOrgId"
                              cssClass="form-control required" afterSelect="carSelect"/>
                </td>
                <td style="width:8%;"><label class="pull-right"><font color="red">*</font>驾驶员：</label></td>
                <td style="width:12%;">
                    <sys:grid title="驾驶员" url="${ctx}/tms/basic/tmDriver/grid"
                              fieldId="driver" fieldName="driver" fieldKeyName="code" fieldValue="${tmRepairOrderEntity.driver}"
                              displayFieldId="driverName" displayFieldName="driverName" displayFieldKeyName="name" displayFieldValue="${tmRepairOrderEntity.driverName}"
                              fieldLabels="编码|姓名|承运商" fieldKeys="code|name|carrierName" searchLabels="编码|姓名" searchKeys="code|name"
                              queryParams="carrierCode|orgId" queryParamValues="carrierCode|baseOrgId"
                              cssClass="form-control required"/>
                </td>
            </tr>
            <tr>
                <td style="width:8%;"><label class="pull-right">状态：</label></td>
                <td style="width:12%;">
                    <form:select path="status" class="form-control m-b" disabled="true">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('TMS_REPAIR_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td style="width:8%;"></td>
                <td style="width:12%;"></td>
                <td style="width:8%;"></td>
                <td style="width:12%;"></td>
                <td style="width:8%;"></td>
                <td style="width:12%;"></td>
            </tr>
            <tr>
                <td style="width:8%;"><label class="pull-right">需维修项目：</label></td>
                <td colspan="9"><textarea id="needRepairItem" name="needRepairItem" class="form-control" style="height: 100px;resize: none;">${tmRepairOrderEntity.needRepairItem}</textarea></td>
            </tr>
        </table>
    </fieldset>
</form:form>
<div style="margin: 10px 10px 0px 10px">
    <fieldset>
        <legend>维修厂维修</legend>
        <form id="inputForm2">
            <shiro:hasPermission name="order:tmRepairOrder:save2">
                <a id="save2" class="btn btn-primary" onclick="save2()"> 保存</a>
            </shiro:hasPermission>
            <table class="table table-bordered" style="margin-bottom: 5px;">
                <tr>
                    <td style="width:8%;"><label class="pull-right"><font color="red">*</font>维修员：</label></td>
                    <td style="width:12%;">
                        <input id="repairman" name="repairman" class="form-control required" value="${tmRepairOrderEntity.repairman}" maxlength="64">
                    </td>
                    <td style="width:8%;"><label class="pull-right">合计金额：</label></td>
                    <td style="width:12%;">
                        <input id="amount" name="amount" class="form-control" value="${tmRepairOrderEntity.amount}" readonly/>
                    </td>
                    <td style="width:8%;"><label class="pull-right">合计工时：</label></td>
                    <td style="width:12%;">
                        <input id="workHour" name="workHour" class="form-control" value="${tmRepairOrderEntity.workHour}" readonly/>
                    </td>
                    <td style="width:8%;"><label class="pull-right">合计工时费：</label></td>
                    <td style="width:12%;">
                        <input id="workHourCost" name="workHourCost" class="form-control" value="${tmRepairOrderEntity.workHourCost}" readonly/>
                    </td>
                    <td style="width:8%;"><label class="pull-right">小计合计：</label></td>
                    <td style="width:12%;">
                        <input id="totalAmount" name="totalAmount" class="form-control" value="${tmRepairOrderEntity.totalAmount}" readonly/>
                    </td>
                </tr>
                <tr>
                    <td style="width:8%;"><label class="pull-right">维修厂维修意见：</label></td>
                    <td colspan="9"><textarea id="repairSuggestion" name="repairSuggestion" class="form-control" style="height: 100px;resize: none;">${tmRepairOrderEntity.repairSuggestion}</textarea></td>
                </tr>
            </table>
        </form>
        <form id="orderDetailForm">
            <div style="margin-bottom: 5px;">
                <shiro:hasPermission name="order:tmRepairOrderDetail:add">
                    <a id="orderDetail_add" class="btn btn-primary" disabled onclick="addDetail()"> 新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmRepairOrderDetail:save">
                    <a id="orderDetail_save" class="btn btn-primary" disabled onclick="saveDetail()"> 保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmRepairOrderDetail:del">
                    <a id="orderDetail_remove" class="btn btn-danger" disabled onclick="delDetail()"> 删除</a>
                </shiro:hasPermission>
            </div>
            <table id="orderDetailTable" class="table table-hover table-striped text-nowrap" data-toolbar="#orderDetailToolbar">
                <thead>
                <th class="hide"></th>
                <th style="width:36px;vertical-align:middle">
                    <label><input type="checkbox" name="btSelectAll" style="width: 16px;height: 16px;"/></label>
                </th>
                <th>维修单位</th>
                <th>配件名称</th>
                <th>型号</th>
                <th>数量</th>
                <th>单价</th>
                <th>金额</th>
                <th>工时</th>
                <th>工时费</th>
                <th>小计</th>
                </thead>
                <tbody id="tmRepairOrderDetailList">
                </tbody>
            </table>
        </form>
    </fieldset>
</div>
<!-- 图片信息 -->
<div class="modal fade" id="imgInfoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 800px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">图片信息</h4>
            </div>
            <div class="modal-body">
                <table id="tmRepairOrderImgTable" class="text-nowrap"></table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/template" id="tmRepairOrderDetailTpl">//<!--
<tr id="tmRepairOrderDetailList{{idx}}" data-index="{{idx}}">
    <td class="hide">
        <input type="hidden" id="tmRepairOrderDetailList{{idx}}_id" name="tmRepairOrderDetailList[{{idx}}].id" value="{{row.id}}"/>
        <input type="hidden" id="tmRepairOrderDetailList{{idx}}_repairNo" name="tmRepairOrderDetailList[{{idx}}].repairNo" value="{{row.repairNo}}"/>
        <input type="hidden" id="tmRepairOrderDetailList{{idx}}_ownerCode" name="tmRepairOrderDetailList[{{idx}}].ownerCode" value="{{row.ownerCode}}"/>
        <input type="hidden" id="tmRepairOrderDetailList{{idx}}_orgId" name="tmRepairOrderDetailList[{{idx}}].orgId" value="{{row.orgId}}"/>
        <input type="hidden" id="tmRepairOrderDetailList{{idx}}_baseOrgId" name="tmRepairOrderDetailList[{{idx}}].baseOrgId" value="{{row.baseOrgId}}"/>
        <input type="hidden" id="tmRepairOrderDetailList{{idx}}_recVer" name="tmRepairOrderDetailList[{{idx}}].recVer" value="{{row.recVer}}"/>
        <input type="hidden" id="tmRepairOrderDetailList{{idx}}_repairPrice" value="{{row.repairPrice}}"/>
    </td>
    <td style="vertical-align: middle">
        <input type="checkbox" name="btSelectItem" data-index="{{idx}}" style="width: 16px;height: 16px;"/>
    </td>
    <td>
        <sys:grid title="维修单位" url="${ctx}/tms/basic/tmTransportObj/grid"
                  fieldId="tmRepairOrderDetailList{{idx}}_repairCode" fieldName="tmRepairOrderDetailList[{{idx}}].repairCode" fieldKeyName="transportObjCode" fieldValue="{{row.repairCode}}"
                  displayFieldId="tmRepairOrderDetailList{{idx}}_repairName" displayFieldName="tmRepairOrderDetailList[{{idx}}].repairName" displayFieldKeyName="transportObjName" displayFieldValue="{{row.repairName}}"
                  fieldLabels="维修单位编码|维修单位名称" fieldKeys="transportObjCode|transportObjName"
                  searchLabels="维修单位编码|维修单位名称" searchKeys="transportObjCode|transportObjName"
                  queryParams="transportObjType|orgId" queryParamValues="repairType|baseOrgId"
                  cssClass="form-control " afterSelect="repairSelect({{idx}})"/>
    </td>
    <td>
        <sys:grid title="配件" url="${ctx}/tms/basic/tmFitting/grid"
                  fieldId="tmRepairOrderDetailList{{idx}}_skuCode" fieldName="tmRepairOrderDetailList[{{idx}}].skuCode" fieldKeyName="fittingCode" fieldValue="{{row.skuCode}}"
                  displayFieldId="tmRepairOrderDetailList{{idx}}_skuName" displayFieldName="tmRepairOrderDetailList[{{idx}}].skuName" displayFieldKeyName="fittingName" displayFieldValue="{{row.skuName}}"
                  fieldLabels="配件编码|配件名称" fieldKeys="fittingCode|fittingName" searchLabels="配件编码|配件名称" searchKeys="fittingCode|fittingName"
                  queryParams="orgId" queryParamValues="baseOrgId"
                  cssClass="form-control required" afterSelect="skuSelect({{idx}})"/>
    </td>
    <td>
        <select id="tmRepairOrderDetailList{{idx}}_skuModel" name="tmRepairOrderDetailList[{{idx}}].skuModel" data-value="{{row.skuModel}}" class="form-control m-b ">
            <option value=""></option>
            <c:forEach items="${fns:getDictList('TMS_FITTING_MODEL')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td>
        <input id="tmRepairOrderDetailList{{idx}}_qty" name="tmRepairOrderDetailList[{{idx}}].qty" class="form-control required" value="{{row.qty}}" onkeyup="bq.numberValidator(this, 4, 0);amountChange({{idx}});" maxlength="18"/>
    </td>
    <td>
        <input id="tmRepairOrderDetailList{{idx}}_price" name="tmRepairOrderDetailList[{{idx}}].price" class="form-control" value="{{row.price}}" onkeyup="bq.numberValidator(this, 4, 0);amountChange({{idx}});" maxlength="18"/>
    </td>
    <td>
        <input id="tmRepairOrderDetailList{{idx}}_amount" name="tmRepairOrderDetailList[{{idx}}].amount" class="form-control" value="{{row.amount}}" maxlength="18" readonly/>
    </td>
    <td>
        <input id="tmRepairOrderDetailList{{idx}}_workHour" name="tmRepairOrderDetailList[{{idx}}].workHour" class="form-control" value="{{row.workHour}}" onkeyup="bq.numberValidator(this, 4, 0);costChange({{idx}});" maxlength="18"/>
    </td>
    <td>
        <input id="tmRepairOrderDetailList{{idx}}_workHourCost" name="tmRepairOrderDetailList[{{idx}}].workHourCost" class="form-control" value="{{row.workHourCost}}" onkeyup="bq.numberValidator(this, 4, 0);totalAmountChange({{idx}});" maxlength="18"/>
    </td>
    <td>
        <input id="tmRepairOrderDetailList{{idx}}_totalAmount" name="tmRepairOrderDetailList[{{idx}}].totalAmount" class="form-control" value="{{row.totalAmount}}" maxlength="18" readonly/>
    </td>
</tr>//-->
</script>
</body>
</html>