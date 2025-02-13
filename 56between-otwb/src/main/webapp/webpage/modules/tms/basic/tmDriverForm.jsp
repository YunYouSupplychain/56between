<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>运输人员信息管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@ include file="tmDriverForm.js" %>
</head>
<body>
<form:form id="inputForm" modelAttribute="tmDriverEntity" class="form-horizontal">
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
                        <td class="width-10"><label class="pull-right asterisk">编码</label></td>
                        <td class="width-15">
                            <form:input path="code" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">姓名</label></td>
                        <td class="width-15">
                            <form:input path="name" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">承运商</label></td>
                        <td class="width-15">
                            <input type="hidden" id="transportObjType" value="CARRIER"/>
                            <sys:grid title="承运商" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="carrierCode" fieldName="carrierCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmDriverEntity.carrierCode}"
                                      displayFieldId="carrierName" displayFieldName="carrierName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmDriverEntity.carrierName}"
                                      fieldLabels="承运商编码|承运商名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="承运商编码|承运商名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="transportObjType|orgId"
                                      cssClass="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">手机</label></td>
                        <td class="width-15">
                            <form:input path="phone" htmlEscape="false" class="form-control required" maxlength="20"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">身份证</label></td>
                        <td class="width-15">
                            <form:input path="idCard" htmlEscape="false" class="form-control" maxlength="20"/>
                        </td>
                        <td class="width-10"><label class="pull-right">出生日期</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='birthDate'>
                                <input type='text' name="birthDate" class="form-control"
                                       value="<fmt:formatDate value="${tmDriverEntity.birthDate}" pattern="yyyy-MM-dd"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">暂住证号</label></td>
                        <td class="width-15">
                            <form:input path="tempResidenceCertificateNo" htmlEscape="false" class="form-control" maxlength="30"/>
                        </td>
                        <td class="width-10"><label class="pull-right">民族</label></td>
                        <td class="width-15">
                            <form:input path="nation" htmlEscape="false" class="form-control" maxlength="10"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">籍贯</label></td>
                        <td class="width-15">
                            <form:input path="nativePlace" htmlEscape="false" class="form-control" maxlength="255"/>
                        </td>
                        <td class="width-10"><label class="pull-right">文化程度</label></td>
                        <td class="width-15">
                            <form:select path="educationLevel" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_EDUCATION_LEVEL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">婚姻状况</label></td>
                        <td class="width-15">
                            <form:select path="maritalStatus" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_MARITAL_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">政治面貌</label></td>
                        <td class="width-15">
                            <form:select path="politicalAffiliation" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_POLITICAL_AFFILIATION')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">准驾车型</label></td>
                        <td class="width-15">
                            <sys:grid title="车型" url="${ctx}/tms/basic/tmVehicleType/grid"
                                      fieldId="allowDriverCarType" fieldName="allowDriverCarType"
                                      fieldKeyName="code" fieldValue="${tmDriverEntity.allowDriverCarType}"
                                      displayFieldId="allowDriverCarTypeName" displayFieldName="allowDriverCarTypeName"
                                      displayFieldKeyName="name" displayFieldValue="${tmDriverEntity.allowDriverCarTypeName}"
                                      fieldLabels="编码|名称" fieldKeys="code|name"
                                      searchLabels="编码|名称" searchKeys="code|name"
                                      queryParams="orgId" queryParamValues="orgId"
                                      cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">人员性质</label></td>
                        <td class="width-15">
                            <form:select path="personnelNature" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_PERSONNEL_NATURE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">驾龄</label></td>
                        <td class="width-15">
                            <form:input path="drivingAge" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 0, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">是否服兵役</label></td>
                        <td class="width-15">
                            <form:select path="isMilitaryService" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">是否内部驾驶员</label></td>
                        <td class="width-15">
                            <form:select path="isInternalDriver" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">账号</label></td>
                        <td class="width-15">
                            <sys:grid title="账号" url="${ctx}/sys/user/popDate"
                                      fieldId="account" fieldName="account"
                                      fieldKeyName="loginName" fieldValue="${tmDriverEntity.account}"
                                      displayFieldId="accountName" displayFieldName="accountName"
                                      displayFieldKeyName="name" displayFieldValue="${tmDriverEntity.accountName}"
                                      fieldLabels="登录名|工号|姓名" fieldKeys="loginName|no|name"
                                      searchLabels="登录名|工号|姓名" searchKeys="loginName|no|name"
                                      cssClass="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"></td>
                        <td class="width-15"></td>
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
                <h3 class="panel-title">联系人信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tr>
                        <td class="width-10"><label class="pull-right">现居地址</label></td>
                        <td colspan="3">
                            <form:input path="currentAddress" htmlEscape="false" class="form-control" maxlength="255"/>
                        </td>
                        <td class="width-10"><label class="pull-right">紧急联系人</label></td>
                        <td class="width-15">
                            <form:input path="emergencyContact" htmlEscape="false" class="form-control" maxlength="18"/>
                        </td>
                        <td class="width-10"><label class="pull-right">紧急联系电话</label></td>
                        <td class="width-15">
                            <form:input path="emergencyContactTel" htmlEscape="false" class="form-control" maxlength="20"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">户口地址</label></td>
                        <td colspan="3">
                            <form:input path="registeredAddress" htmlEscape="false" class="form-control" maxlength="255"/>
                        </td>
                        <td class="width-10"><label class="pull-right">与联系人关系</label></td>
                        <td class="width-15">
                            <form:input path="emergencyContactRelation" htmlEscape="false" class="form-control" maxlength="10"/>
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
                <h3 class="panel-title">工作信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tr>
                        <td class="width-10"><label class="pull-right">劳务合同号</label></td>
                        <td class="width-15">
                            <form:input path="contractNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">到岗日期</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='reportDate'>
                                <input type='text' name="reportDate" class="form-control" value="<fmt:formatDate value="${tmDriverEntity.reportDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">合同有效期</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='contractExpireDate'>
                                <input type='text' name="contractExpireDate" class="form-control" value="<fmt:formatDate value="${tmDriverEntity.contractExpireDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">基本工资</label></td>
                        <td class="width-15">
                            <form:input path="basicWage" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">从业资格证号</label></td>
                        <td class="width-15">
                            <form:input path="employmentQualificationCertificateNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">综合保险号</label></td>
                        <td class="width-15">
                            <form:input path="comprehensiveInsuranceNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">社会保险号</label></td>
                        <td class="width-15">
                            <form:input path="socialInsuranceNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">工作卡号</label></td>
                        <td class="width-15">
                            <form:input path="wordCardNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">驾驶证号</label></td>
                        <td class="width-15">
                            <form:input path="driverLicenseNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">驾驶证类型</label></td>
                        <td class="width-15">
                            <form:select path="driverLicenseType" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_DRIVER_LICENSE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">初领证日期</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='firstReceiveLicenseDate'>
                                <input type='text' name="firstReceiveLicenseDate" class="form-control" value="<fmt:formatDate value="${tmDriverEntity.firstReceiveLicenseDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">驾驶证年检日期</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='driverLicenseAnnualInspectionDate'>
                                <input type='text' name="driverLicenseAnnualInspectionDate" class="form-control" value="<fmt:formatDate value="${tmDriverEntity.driverLicenseAnnualInspectionDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">交通违规扣分</label></td>
                        <td class="width-15">
                            <form:input path="deductPoint" htmlEscape="false" class="form-control" maxlength="2" onkeyup="bq.numberValidator(this, 0, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">停运日期从</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='banDrivingDateFm'>
                                <input type='text' name="banDrivingDateFm" class="form-control" value="<fmt:formatDate value="${tmDriverEntity.banDrivingDateFm}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">停运日期到</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='banDriverDateTo'>
                                <input type='text' name="banDriverDateTo" class="form-control" value="<fmt:formatDate value="${tmDriverEntity.banDriverDateTo}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">停运原因</label></td>
                        <td colspan="3">
                            <form:input path="banDrivingReason" htmlEscape="false" class="form-control" maxlength="255"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">健康状况</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tr>
                        <td class="width-10"><label class="pull-right">身高</label></td>
                        <td class="width-15">
                            <form:input path="height" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">体重</label></td>
                        <td class="width-15">
                            <form:input path="weight" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">血型</label></td>
                        <td class="width-15">
                            <form:select path="bloodType" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_BLOOD_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">视力</label></td>
                        <td class="width-15">
                            <form:input path="vision" htmlEscape="false" class="form-control" maxlength="10"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">鞋码</label></td>
                        <td class="width-15">
                            <form:input path="shoeSize" htmlEscape="false" class="form-control" maxlength="10"/>
                        </td>
                        <td class="width-10"><label class="pull-right">健康状况</label></td>
                        <td class="width-15">
                            <form:select path="health" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_HEALTH')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">健康证号</label></td>
                        <td class="width-15">
                            <form:input path="healthCertificateNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">健康证失效日期</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='healthCertificateExpireDate'>
                                <input type='text' name="healthCertificateExpireDate" class="form-control" value="<fmt:formatDate value="${tmDriverEntity.healthCertificateExpireDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">评价信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tr>
                        <td class="width-10"><label class="pull-right">心理素质</label></td>
                        <td colspan="3">
                            <form:input path="mentalityQuality" htmlEscape="false" class="form-control" maxlength="100"/>
                        </td>
                        <td class="width-10"><label class="pull-right">人际关系</label></td>
                        <td colspan="3">
                            <form:input path="interpersonalRelationship" htmlEscape="false" class="form-control" maxlength="100"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">团队精神</label></td>
                        <td colspan="3">
                            <form:input path="teamSpirit" htmlEscape="false" class="form-control" maxlength="100"/>
                        </td>
                        <td class="width-10"></td>
                        <td colspan="3"></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</form:form>
<div class="wrapper wrapper-content-my">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">资质信息</h3>
        </div>
        <div class="panel-body">
            <div id="toolbar">
                <shiro:hasPermission name="basic:tmDriverQualification:add">
                    <a id="driverQualification_add" class="btn btn-primary" disabled onclick="addDriverQualification()"> 添加</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmDriverQualification:edit">
                    <a id="driverQualification_edit" class="btn btn-primary" disabled onclick="editDriverQualification()"> 修改</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmDriverQualification:del">
                    <a id="driverQualification_remove" class="btn btn-danger" disabled onclick="delDriverQualification()"> 删除</a>
                </shiro:hasPermission>
            </div>
            <!-- 表格 -->
            <table id="tmDriverQualificationTable" class="text-nowrap" data-toolbar="#toolbar"></table>
            <div id="tmDriverQualificationModal" class="modal fade" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel" aria-hidden="true">
                <form id="tmDriverQualificationForm" class="form-horizontal">
                    <input type="hidden" id="driverQualification_id" name="id">
                    <input type="hidden" id="driverQualification_driverCode" name="driverCode">
                    <input type="hidden" id="driverQualification_orgId" name="orgId">
                    <div class="modal-dialog" style="width:1200px; height: 500px">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                                <h4 class="modal-title">资质信息</h4>
                            </div>
                            <div class="modal-body">
                                <table class="table table-striped table-bordered table-condensed">
                                    <tr>
                                        <td style="width: 13%;"><label class="pull-right asterisk">资质代码</label></td>
                                        <td style="width: 20%;">
                                            <input id="driverQualification_qualificationCode" name="qualificationCode" class="form-control required" maxlength="64"/>
                                        </td>
                                        <td style="width: 13%;"><label class="pull-right">中文名称</label></td>
                                        <td style="width: 20%;">
                                            <input id="driverQualification_qualificationNameCn" name="qualificationNameCn" class="form-control" maxlength="64"/>
                                        </td>
                                        <td style="width: 13%;"><label class="pull-right">英文名称</label></td>
                                        <td style="width: 20%;">
                                            <input id="driverQualification_qualificationNameEn" name="qualificationNameEn" class="form-control" maxlength="64"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 13%;"><label class="pull-right">资质简称</label></td>
                                        <td style="width: 20%;">
                                            <input id="driverQualification_qualificationShortName" name="qualificationShortName" class="form-control" maxlength="64"/>
                                        </td>
                                        <td style="width: 13%;"><label class="pull-right">生效日期</label></td>
                                        <td style="width: 20%;">
                                            <div class='input-group form_datetime' id='driverQualification_effectiveDate'>
                                                <input type='text' name="effectiveDate" class="form-control"/>
                                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </td>
                                        <td style="width: 13%;"><label class="pull-right">失效日期</label></td>
                                        <td style="width: 20%;">
                                            <div class='input-group form_datetime' id='driverQualification_expireDate'>
                                                <input type='text' name="expireDate" class="form-control"/>
                                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 13%;"><label class="pull-right">资质描述</label></td>
                                        <td colspan="5">
                                            <input id="driverQualification_remarks" name="remarks" class="form-control" maxlength="255"/>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" onclick="saveDriverQualification()">确认 </button>
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