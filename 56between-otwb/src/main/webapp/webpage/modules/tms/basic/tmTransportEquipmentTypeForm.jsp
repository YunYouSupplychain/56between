<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>运输设备类型管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp"%>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmTransportEquipmentTypeForm.js" %>
</head>
<body>
<form:form id="inputForm" modelAttribute="tmTransportEquipmentTypeEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <form:hidden path="delFlag"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">基本信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">运输设备类型编码</label></td>
                        <td class="width-15">
                            <form:input path="transportEquipmentTypeCode" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">中文名称</label></td>
                        <td class="width-15">
                            <form:input path="transportEquipmentTypeNameCn" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">英文名称</label></td>
                        <td class="width-15">
                            <form:input path="transportEquipmentTypeNameEn" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">温度类别</label></td>
                        <td class="width-15">
                            <form:select path="temperatureType" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_TEMPERATURE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">是否温度控制</label></td>
                        <td class="width-15">
                            <form:select path="isTemperatureControl" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">最低允许温度(℃)</label></td>
                        <td class="width-15">
                            <form:input path="minAllowTemperature" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">最高允许温度(℃)</label></td>
                        <td class="width-15">
                            <form:input path="maxAllowTemperature" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">是否湿度控制</label></td>
                        <td class="width-15">
                            <form:select path="isHumidityControl" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">最低允许湿度</label></td>
                        <td class="width-15">
                            <form:input path="minAllowHumidity" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">最高允许湿度</label></td>
                        <td class="width-15">
                            <form:input path="maxAllowHumidity" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">是否集装箱</label></td>
                        <td class="width-15">
                            <form:select path="isContainer" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">只允许后进后出</label></td>
                        <td class="width-15">
                            <form:select path="onlyAllowLastInLastOut" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">设备空间是否固定</label></td>
                        <td class="width-15">
                            <form:select path="isFixedEquipmentSpace" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right">供应商：</label></td>
                        <td class="width-12">
                            <input type="hidden" id="supplierType" value="SUPPLIER"/>
                            <sys:grid title="供应商" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="supplierCode" fieldName="supplierCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmTransportEquipmentTypeEntity.supplierCode}"
                                      displayFieldId="supplierName" displayFieldName="supplierName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmTransportEquipmentTypeEntity.supplierName}"
                                      fieldLabels="编码|名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="编码|名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="supplierType|orgId"
                                      cssClass="form-control"/>
                        </td>
                        <td class="width-10"></td>
                        <td class="width-15"></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">设备参数</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tr>
                        <td class="width-10"><label class="pull-right">内部长度</label></td>
                        <td class="width-15">
                            <form:input path="internalLength" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">内部宽度</label></td>
                        <td class="width-15">
                            <form:input path="internalWidth" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">内部高度</label></td>
                        <td class="width-15">
                            <form:input path="internalHeight" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">门宽</label></td>
                        <td class="width-15">
                            <form:input path="doorWidth" htmlEscape="false" class="form-control" onkeyup="bq.bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">门高</label></td>
                        <td class="width-15">
                            <form:input path="doorHeight" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">外部长度</label></td>
                        <td class="width-15">
                            <form:input path="externalLength" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">外部宽度</label></td>
                        <td class="width-15">
                            <form:input path="externalWidth" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">外部高度</label></td>
                        <td class="width-15">
                            <form:input path="externalHeight" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">最小载重量</label></td>
                        <td class="width-15">
                            <form:input path="minLoadWeight" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">最大载重量</label></td>
                        <td class="width-15">
                            <form:input path="maxLoadWeight" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">左侧限制最大载重</label></td>
                        <td class="width-15">
                            <form:input path="leftLimitMaxLoadWeight" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">右侧限制最大载重</label></td>
                        <td class="width-15">
                            <form:input path="rightLimitMaxLoadWeight" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">左右差异限制最大载重</label></td>
                        <td class="width-15">
                            <form:input path="leftRightDiffLimitMaxLoadWeight" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">最小装载容积</label></td>
                        <td class="width-15">
                            <form:input path="minLoadCubic" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">最大装载容积</label></td>
                        <td class="width-15">
                            <form:input path="maxLoadCubic" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">允许超重比例</label></td>
                        <td class="width-15">
                            <form:input path="allowOverweightRate" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</form:form>
<div class="wrapper wrapper-content-my">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">设备空间信息</h3>
        </div>
        <div class="panel-body">
            <div id="toolbar">
                <shiro:hasPermission name="basic:tmTransportEquipmentSpace:add">
                    <a id="equipmentSpace_add" class="btn btn-primary" disabled onclick="addEquipmentSpace()"> 添加</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmTransportEquipmentSpace:edit">
                    <a id="equipmentSpace_edit" class="btn btn-primary" disabled onclick="editEquipmentSpace()"> 修改</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmTransportEquipmentSpace:del">
                    <a id="equipmentSpace_remove" class="btn btn-danger" disabled onclick="delEquipmentSpace()"> 删除</a>
                </shiro:hasPermission>
            </div>
            <!-- 表格 -->
            <table id="tmTransportEquipmentSpaceTable" class="text-nowrap" data-toolbar="#toolbar"></table>
            <div id="tmTransportEquipmentSpaceModal" class="modal fade" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel" aria-hidden="true">
                <form id="tmTransportEquipmentSpaceForm" class="form-horizontal">
                    <input type="hidden" id="equipmentSpace_id" name="id">
                    <input type="hidden" id="equipmentSpace_transportEquipmentTypeCode" name="transportEquipmentTypeCode">
                    <input type="hidden" id="equipmentSpace_orgId" name="orgId">
                    <div class="modal-dialog" style="width:1200px; height: 500px">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">设备空间</h4>
                            </div>
                            <div class="modal-body">
                                <table class="table">
                                    <tr>
                                        <td class="width-10"><label class="pull-right asterisk">设备编号</label></td>
                                        <td class="width-15">
                                            <input id="equipmentSpace_transportEquipmentNo" name="transportEquipmentNo" class="form-control required" maxlength="64"/>
                                        </td>
                                        <td class="width-10"><label class="pull-right">设备位置</label></td>
                                        <td class="width-15">
                                            <input id="equipmentSpace_transportEquipmentLocation" name="transportEquipmentLocation" class="form-control" maxlength="255"/>
                                        </td>
                                        <td class="width-10"><label class="pull-right">是否温度控制</label></td>
                                        <td class="width-15">
                                            <select id="equipmentSpace_isTemperatureControl" name="isTemperatureControl" class="form-control">
                                                <option></option>
                                                <c:forEach items="${fns:getDictList('SYS_YES_NO')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-10"><label class="pull-right">是否湿度控制</label></td>
                                        <td class="width-15">
                                            <select id="equipmentSpace_isHumidityControl" name="isHumidityControl" class="form-control">
                                                <option></option>
                                                <c:forEach items="${fns:getDictList('SYS_YES_NO')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-10"><label class="pull-right">装载重量</label></td>
                                        <td class="width-15">
                                            <input id="equipmentSpace_loadWeight" name="loadWeight" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)"/>
                                        </td>
                                        <td class="width-10"><label class="pull-right">装载容积</label></td>
                                        <td class="width-15">
                                            <input id="equipmentSpace_loadCubic" name="loadCubic" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)"/>
                                        </td>
                                        <td class="width-10"><label class="pull-right">备注</label></td>
                                        <td class="width-15" colspan="5">
                                            <input id="equipmentSpace_remarks" name="remarks" class="form-control"/>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" onclick="saveEquipmentSpace()">确认 </button>
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