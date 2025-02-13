<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>车辆信息管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp"%>
    <%@include file="sysTmsVehicleForm.js" %>
</head>
<body>
<div class="hidden">
    <input type="hidden" id="carrierTransportObjType" value="CARRIER"/>
    <input type="hidden" id="supplierTransportObjType" value="SUPPLIER"/>
    <input type="hidden" id="dispatchCenter" value="6"/>
</div>
<form:form id="inputForm" modelAttribute="sysTmsVehicleEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <%--基本信息--%>
    <div class="tabs-container" style="max-height: 250px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#basicInfo" aria-expanded="true">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="basicInfo" class="tab-pane fade in active">
                <table class="table table-bordered">
                    <tr>
                        <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>车牌号：</label></td>
                        <td style="width: 12%;">
                            <form:input path="carNo" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">设备类型：</label></td>
                        <td style="width: 12%;">
                            <sys:grid title="运输设备类型" url="${ctx}/sys/common/tms/transportEquipmentType/grid"
                                      fieldId="transportEquipmentTypeCode" fieldName="transportEquipmentTypeCode" fieldKeyName="transportEquipmentTypeCode" fieldValue="${sysTmsVehicleEntity.transportEquipmentTypeCode}"
                                      displayFieldId="transportEquipmentTypeName" displayFieldName="transportEquipmentTypeName" displayFieldKeyName="transportEquipmentTypeNameCn" displayFieldValue="${sysTmsVehicleEntity.transportEquipmentTypeName}"
                                      fieldLabels="运输设备类型编码|运输设备类型名称" fieldKeys="transportEquipmentTypeCode|transportEquipmentTypeNameCn"
                                      searchLabels="运输设备类型编码|运输设备类型名称" searchKeys="transportEquipmentTypeCode|transportEquipmentTypeNameCn"
                                      queryParams="dataSet" queryParamValues="dataSet"
                                      cssClass="form-control"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">承运商：</label></td>
                        <td style="width: 12%;">
                            <sys:grid title="承运商" url="${ctx}/sys/common/tms/transportObj/grid"
                                      fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="transportObjCode" fieldValue="${sysTmsVehicleEntity.carrierCode}"
                                      displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="transportObjName" displayFieldValue="${sysTmsVehicleEntity.carrierName}"
                                      fieldLabels="承运商编码|承运商名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="承运商编码|承运商名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|dataSet" queryParamValues="carrierTransportObjType|dataSet"
                                      cssClass="form-control "/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车辆类型：</label></td>
                        <td style="width: 12%;">
                            <form:select path="carType" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_CAR_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">调度中心：</label></td>
                        <td style="width: 12%;">
                            <sys:grid title="选择机构" url="${ctx}/sys/office/companyData" cssClass="form-control"
                                      fieldId="dispatchBase" fieldName="dispatchBase" fieldKeyName="id" fieldValue="${sysTmsVehicleEntity.dispatchBase}"
                                      displayFieldId="dispatchBaseName" displayFieldName="dispatchBaseName" displayFieldKeyName="name" displayFieldValue="${sysTmsVehicleEntity.dispatchBaseName}"
                                      fieldLabels="机构编码|机构名称" fieldKeys="code|name"
                                      searchLabels="机构编码|机构名称" searchKeys="code|name"
                                      queryParams="type" queryParamValues="dispatchCenter"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">主驾驶：</label></td>
                        <td style="width: 12%;">
                            <sys:grid title="运输人员" url="${ctx}/sys/common/tms/driver/grid"
                                      fieldId="mainDriver" fieldName="mainDriver" fieldKeyName="code" fieldValue="${sysTmsVehicleEntity.mainDriver}"
                                      displayFieldId="mainDriverName" displayFieldName="mainDriverName" displayFieldKeyName="name" displayFieldValue="${sysTmsVehicleEntity.mainDriverName}"
                                      fieldLabels="运输人员编码|运输人员名称" fieldKeys="code|name" searchLabels="运输人员编码|运输人员名称" searchKeys="code|name"
                                      queryParams="carrierCode|dataSet" queryParamValues="carrierCode|dataSet"
                                      cssClass="form-control"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">副驾驶：</label></td>
                        <td style="width: 12%;">
                            <sys:grid title="运输人员" url="${ctx}/sys/common/tms/driver/grid"
                                      fieldId="copilot" fieldName="copilot" fieldKeyName="code" fieldValue="${sysTmsVehicleEntity.copilot}"
                                      displayFieldId="copilotName" displayFieldName="copilotName" displayFieldKeyName="name" displayFieldValue="${sysTmsVehicleEntity.copilotName}"
                                      fieldLabels="运输人员编码|运输人员名称" fieldKeys="code|name" searchLabels="运输人员编码|运输人员名称" searchKeys="code|name"
                                      queryParams="carrierCode|dataSet" queryParamValues="carrierCode|dataSet"
                                      cssClass="form-control"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">挂车：</label></td>
                        <td style="width: 12%;">
                            <form:input path="trailer" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车辆品牌：</label></td>
                        <td style="width: 12%;">
                            <form:input path="carBrand" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车辆型号：</label></td>
                        <td style="width: 12%;">
                            <form:input path="carModel" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">车辆颜色：</label></td>
                        <td style="width: 12%;">
                            <form:input path="carColor" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车身号码：</label></td>
                        <td style="width: 12%;">
                            <form:input path="carBodyNo" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">供应商：</label></td>
                        <td style="width: 12%;">
                            <sys:grid title="供应商" url="${ctx}/sys/common/tms/transportObj/grid"
                                      fieldId="supplierCode" fieldName="supplierCode" fieldKeyName="transportObjCode" fieldValue="${sysTmsVehicleEntity.supplierCode}"
                                      displayFieldId="supplierName" displayFieldName="supplierName" displayFieldKeyName="transportObjName" displayFieldValue="${sysTmsVehicleEntity.supplierName}"
                                      fieldLabels="供应商编码|供应商名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="供应商编码|供应商名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|dataSet" queryParamValues="supplierTransportObjType|dataSet"
                                      cssClass="form-control"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">所有权：</label></td>
                        <td style="width: 12%;">
                            <form:input path="ownership" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">启用停用：</label></td>
                        <td style="width: 12%;">
                            <form:select path="delFlag" class="form-control " disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">状态：</label></td>
                        <td style="width: 12%;">
                            <form:select path="status" class="form-control " disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_VEHICLE_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10 active"><label class="pull-right"><font color="red">*</font>数据套：</label></td>
                        <td class="width-15">
                            <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                                      fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysTmsVehicleEntity.dataSet}"
                                      displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysTmsVehicleEntity.dataSetName}"
                                      fieldLabels="编码|名称" fieldKeys="code|name"
                                      searchLabels="编码|名称" searchKeys="code|name"/>
                        </td>
                        <td style="width: 8%;"></td>
                        <td style="width: 12%;"></td>
                        <td style="width: 8%;"></td>
                        <td style="width: 12%;"></td>
                        <td style="width: 8%;"></td>
                        <td style="width: 12%;"></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <%--车辆装载信息--%>
    <div class="tabs-container" style="max-height: 250px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#loadInfo" aria-expanded="true">车辆装载信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="loadInfo" class="tab-pane fade in active">
                <table class="table table-bordered">
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">核定装载重量(吨)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="approvedLoadingWeight" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">核定装载体积：</label></td>
                        <td style="width: 12%;">
                            <form:input path="approvedLoadingCubic" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">牵引总重量(吨)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="totalTractionWeight" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">装备质量(吨)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="equipmentQuality" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车门个数：</label></td>
                        <td style="width: 12%;">
                            <form:input path="doorNumber" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 0, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">车长(m)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="length" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车宽(m)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="width" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车高(m)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="height" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">是否温控：</label></td>
                        <td style="width: 12%;">
                            <form:select path="isTemperatureControl" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车辆温别：</label></td>
                        <td style="width: 12%;">
                            <form:select path="temperatureType" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_TEMPERATURE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">最低温(℃)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="minTemperature" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">最高温(℃)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="maxTemperature" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">制冷设备编码：</label></td>
                        <td style="width: 12%;">
                            <form:input path="refrigerationEquipmentCode" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">是否危品车：</label></td>
                        <td style="width: 12%;">
                            <form:select path="isRisk" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">危险等级：</label></td>
                        <td style="width: 12%;">
                            <form:select path="riskLevel" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_RISK_LEVEL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <%--购车及使用信息--%>
    <div class="tabs-container" style="max-height: 250px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#buyAndUseInfo" aria-expanded="true">购车及使用信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="buyAndUseInfo" class="tab-pane fade in active">
                <table class="table table-bordered">
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">购车时间：</label></td>
                        <td style="width: 12%;">
                            <div class='input-group form_datetime' id='buyingTime'>
                                <input type='text' name="buyingTime" class="form-control" value="<fmt:formatDate value="${sysTmsVehicleEntity.buyingTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">购车地点：</label></td>
                        <td style="width: 12%;">
                            <form:input path="purchaseLocation" htmlEscape="false" class="form-control " maxlength="255"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">购车金额：</label></td>
                        <td style="width: 12%;">
                            <form:input path="purchaseAmount" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车辆排放标准：</label></td>
                        <td style="width: 12%;">
                            <form:input path="emissionStandard" htmlEscape="false" class="form-control " maxlength="20"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">油耗(升)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="oilConsumption" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">行车里程(公里)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="mileage" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">马力(匹)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="horsepower" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">折旧年限(年)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="depreciableLife" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">报废年限(年)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="scrappedLife" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车辆启用时间：</label></td>
                        <td style="width: 12%;">
                            <div class='input-group form_datetime' id='activeTime'>
                                <input type='text' name="activeTime" class="form-control" value="<fmt:formatDate value="${sysTmsVehicleEntity.activeTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">车辆报废时间：</label></td>
                        <td style="width: 12%;">
                            <div class='input-group form_datetime' id='scrappedTime'>
                                <input type='text' name="scrappedTime" class="form-control" value="<fmt:formatDate value="${sysTmsVehicleEntity.scrappedTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">残值率(%)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="salvageRate" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">轴数：</label></td>
                        <td style="width: 12%;">
                            <form:input path="axleNumber" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 0, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">发动机号：</label></td>
                        <td style="width: 12%;">
                            <form:input path="engineNo" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">用油型号：</label></td>
                        <td style="width: 12%;">
                            <form:input path="oilType" htmlEscape="false" class="form-control " maxlength="20"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <%--证书资质信息--%>
    <div class="tabs-container" style="max-height: 250px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#drivingLicenseInfo" aria-expanded="true">证书资质信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="drivingLicenseInfo" class="tab-pane fade in active">
                <table class="table table-bordered">
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">注册时间：</label></td>
                        <td style="width: 12%;">
                            <div class='input-group form_datetime' id='registeredTime'>
                                <input type='text' name="registeredTime" class="form-control" value="<fmt:formatDate value="${sysTmsVehicleEntity.registeredTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">注册地点：</label></td>
                        <td style="width: 12%;">
                            <form:input path="registeredLocation" htmlEscape="false" class="form-control " maxlength="255"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">行驶证号：</label></td>
                        <td style="width: 12%;">
                            <form:input path="drivingLicenseNo" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">行驶证有效期：</label></td>
                        <td style="width: 12%;">
                            <div class='input-group form_datetime' id='drivingLicenseExpiryTime'>
                                <input type='text' name="drivingLicenseExpiryTime" class="form-control" value="<fmt:formatDate value="${sysTmsVehicleEntity.drivingLicenseExpiryTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">运营证有效期：</label></td>
                        <td style="width: 12%;">
                            <div class='input-group form_datetime' id='operatingLicenseExpiryTime'>
                                <input type='text' name="operatingLicenseExpiryTime" class="form-control" value="<fmt:formatDate value="${sysTmsVehicleEntity.operatingLicenseExpiryTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">运营时长(h)：</label></td>
                        <td style="width: 12%;">
                            <form:input path="operatingDuration" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">通行缴费时间：</label></td>
                        <td style="width: 12%;">
                            <div class='input-group form_datetime' id='tollCollectionTime'>
                                <input type='text' name="tollCollectionTime" class="form-control" value="<fmt:formatDate value="${sysTmsVehicleEntity.tollCollectionTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车辆年审到期时间：</label></td>
                        <td style="width: 12%;">
                            <div class='input-group form_datetime' id='annualReviewExpiryTime'>
                                <input type='text' name="annualReviewExpiryTime" class="form-control" value="<fmt:formatDate value="${sysTmsVehicleEntity.annualReviewExpiryTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车辆二维到期时间：</label></td>
                        <td style="width: 12%;">
                            <div class='input-group form_datetime' id='twoDimensionExpiryTime'>
                                <input type='text' name="twoDimensionExpiryTime" class="form-control" value="<fmt:formatDate value="${sysTmsVehicleEntity.twoDimensionExpiryTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车辆罐检到期时间：</label></td>
                        <td style="width: 12%;">
                            <div class='input-group form_datetime' id='tankInspectionExpiryTime'>
                                <input type='text' name="tankInspectionExpiryTime" class="form-control" value="<fmt:formatDate value="${sysTmsVehicleEntity.tankInspectionExpiryTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td style="width: 8%;"><label class="pull-right">保险有效期：</label></td>
                        <td style="width: 12%;">
                            <div class='input-group form_datetime' id='insuranceExpiryTime'>
                                <input type='text' name="insuranceExpiryTime" class="form-control" value="<fmt:formatDate value="${sysTmsVehicleEntity.insuranceExpiryTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车辆保养里程数：</label></td>
                        <td style="width: 12%;">
                            <form:input path="vehicleMaintenanceMileage" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车辆打黄油保养里程数：</label></td>
                        <td style="width: 12%;">
                            <form:input path="vehicleApplyGreaseMileage" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td style="width: 8%;"><label class="pull-right">车辆换机油保养里程数：</label></td>
                        <td style="width: 12%;">
                            <form:input path="vehicleOilChangeMileage" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</form:form>
<%--车辆资质--%>
<div class="tabs-container" style="margin-left: 10px;max-height: 500px;">
    <ul class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#carQualificationInfo" aria-expanded="true">车辆资质</a></li>
    </ul>
    <div class="tab-content">
        <div id="carQualificationInfo" class="tab-pane fade in active">
            <div id="carQualificationToolbar">
                <shiro:hasPermission name="sys:common:tms:vehicleQualification:add">
                    <a id="carQualification_add" class="btn btn-primary" disabled onclick="addCarQualification()"> 添加</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:tms:vehicleQualification:edit">
                    <a id="carQualification_edit" class="btn btn-primary" disabled onclick="editCarQualification()"> 修改</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:tms:vehicleQualification:del">
                    <a id="carQualification_remove" class="btn btn-danger" disabled onclick="delCarQualification()"> 删除</a>
                </shiro:hasPermission>
            </div>
            <!-- 表格 -->
            <table id="tmVehicleQualificationTable" class="text-nowrap" data-toolbar="#carQualificationToolbar"></table>
            <div id="tmVehicleQualificationModal" class="modal fade" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel" aria-hidden="true">
                <form id="tmVehicleQualificationForm" class="form-horizontal">
                    <input type="hidden" id="carQualification_id" name="id">
                    <input type="hidden" id="carQualification_carNo" name="carNo">
                    <input type="hidden" id="carQualification_dataSet" name="dataSet">
                    <div class="modal-dialog" style="width:1200px; height: 500px">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">车辆资质</h4>
                            </div>
                            <div class="modal-body">
                                <table class="table table-striped table-bordered table-condensed">
                                    <tr>
                                        <td style="width: 13%;"><label class="pull-right"><font color="red">*</font>资质代码：</label></td>
                                        <td style="width: 20%;">
                                            <input id="carQualification_qualificationCode" name="qualificationCode" class="form-control required" maxlength="64"/>
                                        </td>
                                        <td style="width: 13%;"><label class="pull-right">中文名称：</label></td>
                                        <td style="width: 20%;">
                                            <input id="carQualification_qualificationNameCn" name="qualificationNameCn" class="form-control " maxlength="64"/>
                                        </td>
                                        <td style="width: 13%;"><label class="pull-right">英文名称：</label></td>
                                        <td style="width: 20%;">
                                            <input id="carQualification_qualificationNameEn" name="qualificationNameEn" class="form-control " maxlength="64"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 13%;"><label class="pull-right">资质简称：</label></td>
                                        <td style="width: 20%;">
                                            <input id="carQualification_qualificationShortName" name="qualificationShortName" class="form-control " maxlength="64"/>
                                        </td>
                                        <td style="width: 13%;"><label class="pull-right">生效日期：</label></td>
                                        <td style="width: 20%;">
                                            <div class='input-group form_datetime' id='carQualification_effectiveDate'>
                                                <input type='text' name="effectiveDate" class="form-control"/>
                                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </td>
                                        <td style="width: 13%;"><label class="pull-right">失效日期：</label></td>
                                        <td style="width: 20%;">
                                            <div class='input-group form_datetime' id='carQualification_expireDate'>
                                                <input type='text' name="expireDate" class="form-control"/>
                                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 13%;"><label class="pull-right">资质描述：</label></td>
                                        <td colspan="5">
                                            <input id="carQualification_remarks" name="remarks" class="form-control " maxlength="255"/>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" onclick="saveCarQualification()">确认</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<%--车辆配件--%>
<div class="tabs-container" style="margin-left: 10px;max-height: 500px;">
    <ul class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#carPartInfo" aria-expanded="true">车辆配件</a></li>
    </ul>
    <div class="tab-content">
        <div id="carPartInfo" class="tab-pane fade in active">
            <div class="carPartToolbar">
                <shiro:hasPermission name="sys:common:tms:vehiclePart:add">
                    <a id="carPart_add" class="btn btn-primary" disabled onclick="addCarPart()"> 添加</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:tms:vehiclePart:edit">
                    <a id="carPart_edit" class="btn btn-primary" disabled onclick="editCarPart()"> 修改</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:tms:vehiclePart:del">
                    <a id="carPart_remove" class="btn btn-danger" disabled onclick="delCarPart()"> 删除</a>
                </shiro:hasPermission>
            </div>
            <!-- 表格 -->
            <table id="tmVehiclePartTable" class="text-nowrap" data-toolbar="#carPartToolbar"></table>
            <div id="tmVehiclePartModal" class="modal fade" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel" aria-hidden="true">
                <form id="tmVehiclePartForm" class="form-horizontal">
                    <input type="hidden" id="carPart_id" name="id">
                    <input type="hidden" id="carPart_carNo" name="carNo">
                    <input type="hidden" id="carPart_dataSet" name="dataSet">
                    <div class="modal-dialog" style="width:1200px; height: 500px">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">车辆配件</h4>
                            </div>
                            <div class="modal-body">
                                <table class="table table-striped table-bordered table-condensed">
                                    <tr>
                                        <td style="width: 13%;"><label class="pull-right"><font color="red">*</font>配件编号：</label></td>
                                        <td style="width: 20%;">
                                            <input id="carPart_partNo" name="partNo" class="form-control required" maxlength="64"/>
                                        </td>
                                        <td style="width: 13%;"><label class="pull-right">配件名称：</label></td>
                                        <td style="width: 20%;">
                                            <input id="carPart_partName" name="partName" class="form-control " maxlength="64"/>
                                        </td>
                                        <td style="width: 13%;"><label class="pull-right">配件数量：</label></td>
                                        <td style="width: 20%;">
                                            <input id="carPart_partNumber" name="partNumber" class="form-control " onkeyup="bq.numberValidator(this, 0, 0)"/>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" onclick="saveCarPart()">确认</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>