<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>客户信息管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="sysCommonCustomerForm.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="outletOfficeType" value="7"/>
    <input type="hidden" id="carrierOfficeType" value="8"/>
    <input type="hidden" id="settleType" value="SETTLEMENT"/>
</div>
<form:form id="inputForm" modelAttribute="sysCommonCustomerEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="warehouse"/>
    <form:hidden path="def3"/>
    <form:hidden path="def4"/>
    <form:hidden path="def5"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">基础信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right"><font color="red">*</font>编码</label></td>
                        <td class="width-15">
                            <form:input path="code" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right"><font color="red">*</font>名称</label></td>
                        <td class="width-15">
                            <form:input path="name" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right">简称</label></td>
                        <td class="width-15">
                            <form:input path="shortName" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">外语名称</label></td>
                        <td class="width-15">
                            <form:input path="foreignName" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right"><font color="red">*</font>数据套</label></td>
                        <td class="width-15">
                            <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                                      fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysCommonCustomerEntity.dataSet}"
                                      displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysCommonCustomerEntity.dataSetName}"
                                      fieldLabels="编码|名称" fieldKeys="code|name"
                                      searchLabels="编码|名称" searchKeys="code|name"/>
                        </td>
                        <td class="width-10"><label class="pull-right">大类</label></td>
                        <td class="width-15">
                            <form:select path="categories" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_MAJOR_CLASS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">行业类型</label></td>
                        <td class="width-15">
                            <form:select path="industryType" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_INDUSTRY_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">范围</label></td>
                        <td class="width-15">
                            <form:select path="scope" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_RANGE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right"><font color="red">*</font>客户类型</label></td>
                        <td class="width-15" colspan="7">
                            <sys:checkbox id="type" name="type" items="${fns:getDictList('SYS_COMMON_CUSTOMER_TYPE')}" values="${sysCommonCustomerEntity.type}" cssClass="i-checks required"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">联系人</label></td>
                        <td class="width-15">
                            <form:input path="contacts" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">电话</label></td>
                        <td class="width-15">
                            <form:input path="tel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">邮箱</label></td>
                        <td class="width-15">
                            <form:input path="mail" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">传真</label></td>
                        <td class="width-15">
                            <form:input path="fax" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">邮编</label></td>
                        <td class="width-15">
                            <form:input path="zipCode" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">省</label></td>
                        <td class="width-15">
                            <form:input path="province" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">市</label></td>
                        <td class="width-15">
                            <form:input path="city" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">区</label></td>
                        <td class="width-15">
                            <form:input path="area" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">所属城市</label></td>
                        <td class="width-15">
                            <sys:area id="areaId" name="areaId" value="${sysCommonCustomerEntity.areaId}"
                                      labelName="areaName" labelValue="${sysCommonCustomerEntity.areaName}"
                                      cssClass="form-control" allowSearch="true" showFullName="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">详细地址</label></td>
                        <td class="width-15" colspan="3">
                            <form:input path="address" htmlEscape="false" class="form-control" maxlength="256"/>
                        </td>
                        <td class="width-10"><label class="pull-right">网址</label></td>
                        <td class="width-15">
                            <form:input path="url" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">主营业务</label></td>
                        <td class="width-15">
                            <form:input path="mainBusiness" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">财务代码</label></td>
                        <td class="width-15">
                            <form:input path="financeCode" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">平台税务登记号</label></td>
                        <td class="width-15">
                            <form:input path="taxRegisterNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">工商登记号</label></td>
                        <td class="width-15">
                            <form:input path="businessNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">统一码</label></td>
                        <td class="width-15">
                            <form:input path="unCode" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">是否一般纳税人</label></td>
                        <td class="width-15">
                            <input type="checkbox" id="isGeneralTaxpayer" name="isGeneralTaxpayer" htmlEscape="false" class="myCheckbox" onclick="isGeneralTaxpayerChange(this.checked)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">税率</label></td>
                        <td class="width-15">
                            <form:input path="taxRate" htmlEscape="false" class="form-control" maxlength="64" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">税率值</label></td>
                        <td class="width-15">
                            <form:input path="taxRateValue" htmlEscape="false" class="form-control" maxlength="64" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">项目</label></td>
                        <td class="width-15">
                            <form:input path="project" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">订单系统</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">业务员</label></td>
                        <td class="width-15">
                            <sys:grid title="选择业务员" url="${ctx}/sys/common/oms/clerk/grid" cssClass="form-control"
                                      fieldId="clerkCode" fieldName="clerkCode" fieldKeyName="clerkCode" fieldValue="${sysCommonCustomerEntity.clerkCode}"
                                      displayFieldId="clerkName" displayFieldName="clerkName" displayFieldKeyName="clerkName" displayFieldValue="${sysCommonCustomerEntity.clerkName}"
                                      fieldLabels="业务员代码|业务员名称" fieldKeys="clerkCode|clerkName"
                                      searchLabels="业务员代码|业务员名称" searchKeys="clerkCode|clerkName"
                                      queryParams="dataSet" queryParamValues="dataSet"/>
                        </td>
                        <td class="width-10"></td>
                        <td class="width-15"></td>
                        <td class="width-10"></td>
                        <td class="width-15"></td>
                        <td class="width-10"></td>
                        <td class="width-15"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">运输系统</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">承运商对应机构</label></td>
                        <td class="width-15">
                            <sys:grid title="选择机构" url="${ctx}/sys/office/companyData" cssClass="form-control"
                                      fieldId="carrierMatchedOrgId" fieldName="carrierMatchedOrgId" fieldKeyName="id" fieldValue="${sysCommonCustomerEntity.carrierMatchedOrgId}"
                                      displayFieldId="carrierMatchedOrg" displayFieldName="carrierMatchedOrg" displayFieldKeyName="name" displayFieldValue="${sysCommonCustomerEntity.carrierMatchedOrg}"
                                      fieldLabels="机构编码|机构名称" fieldKeys="code|name"
                                      searchLabels="机构编码|机构名称" searchKeys="code|name"
                                      queryParams="type" queryParamValues="carrierOfficeType"/>
                        </td>
                        <td class="width-10"><label class="pull-right">网点对应机构</label></td>
                        <td class="width-15">
                            <sys:grid title="选择机构" url="${ctx}/sys/office/outletMatchedOrg" cssClass="form-control"
                                      fieldId="outletMatchedOrgId" fieldName="outletMatchedOrgId" fieldKeyName="id" fieldValue="${sysCommonCustomerEntity.outletMatchedOrgId}"
                                      displayFieldId="outletMatchedOrg" displayFieldName="outletMatchedOrg" displayFieldKeyName="name" displayFieldValue="${sysCommonCustomerEntity.outletMatchedOrg}"
                                      fieldLabels="机构编码|机构名称" fieldKeys="code|name"
                                      searchLabels="机构编码|机构名称" searchKeys="code|name"
                                      queryParams="type" queryParamValues="carrierOfficeType"/>
                        </td>
                        <td class="width-10"><label class="pull-right">品牌</label></td>
                        <td class="width-15">
                            <form:select path="brand" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_TRANSPORT_OBJ_BRAND')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"></td>
                        <td class="width-15"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<script type="text/template" id="contactsTpl">//<!--
    <tr id="contactsList{{idx}}" data-index="{{idx}}">
        <td class="hide">
            <input type="hidden" id="contactsList{{idx}}_id" name="contactsList[{{idx}}].id" value="{{row.id}}"/>
            <input type="hidden" id="contactsList{{idx}}_dataSet" name="contactsList[{{idx}}].dataSet" value="{{row.dataSet}}"/>
            <input type="hidden" id="contactsList{{idx}}_recVer" name="contactsList[{{idx}}].recVer" value="{{row.recVer}}"/>
        </td>
        <td style="vertical-align: middle">
            <input type="checkbox" name="btSelectItem" data-index="{{idx}}" style="width: 16px;height: 16px;"/>
        </td>
        <td>
            <input type="text" id="contactsList{{idx}}_code" name="contactsList[{{idx}}].code" class="form-control required" value="{{row.code}}"/>
        </td>
        <td>
            <input type="text" id="contactsList{{idx}}_name" name="contactsList[{{idx}}].name" class="form-control required" value="{{row.name}}"/>
        </td>
        <td>
            <input type="text" id="contactsList{{idx}}_title" name="contactsList[{{idx}}].title" class="form-control" value="{{row.title}}"/>
        </td>
        <td>
            <input type="text" id="contactsList{{idx}}_tel" name="contactsList[{{idx}}].tel" class="form-control required" value="{{row.tel}}"/>
        </td>
        <td>
            <input type="text" id="contactsList{{idx}}_mobilePhone" name="contactsList[{{idx}}].mobilePhone" class="form-control" value="{{row.mobilePhone}}"/>
        </td>
        <td>
            <input type="text" id="contactsList{{idx}}_fax" name="contactsList[{{idx}}].fax" class="form-control" value="{{row.fax}}"/>
        </td>
        <td>
            <input type="text" id="contactsList{{idx}}_email" name="contactsList[{{idx}}].email" class="form-control" value="{{row.email}}"/>
        </td>
        <td>
            <input type="text" id="contactsList{{idx}}_addressName" name="contactsList[{{idx}}].addressName" class="form-control" value="{{row.addressName}}"/>
        </td>
        <td>
            <input type="text" id="contactsList{{idx}}_address" name="contactsList[{{idx}}].address" class="form-control" value="{{row.address}}"/>
        </td>
        <td width="80px">
            <input type="checkbox" id="contactsList{{idx}}_isDefault" name="contactsList[{{idx}}].isDefault" style="zoom:1.3;" value="{{row.isDefault}}" onclick="isDefaultChange('#contactsList', {{idx}})"/>
        </td>
    </tr>//-->
</script>
</body>
</html>