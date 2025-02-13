<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>结算模型管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsSettleModelForm.js" %>
</head>
<body>
<div style="margin-left: 10px;">
    <shiro:hasPermission name="bms:finance:bmsSettleModel:edit">
        <button id="save" class="btn btn-primary" onclick="save()">保存</button>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="bmsSettleModelEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-10"><label class="pull-right">模型编码</label></td>
                <td class="width-15">
                    <form:input path="settleModelCode" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right asterisk">结算对象编码</label></td>
                <td class="width-15">
                    <sys:popSelect title="选择结算对象" url="${ctx}/bms/basic/bmsSettleObject/grid" cssClass="form-control required"
                                   fieldId="" fieldKeyName="" fieldName="" fieldValue=""
                                   displayFieldId="settleObjectCode" displayFieldKeyName="settleObjectCode"
                                   displayFieldName="settleObjectCode" displayFieldValue="${bmsSettleModelEntity.settleObjectCode}"
                                   selectButtonId="settleObjectSBtnId" deleteButtonId="settleObjectDBtnId"
                                   fieldLabels="结算对象编码|结算对象名称" fieldKeys="settleObjectCode|settleObjectName"
                                   searchLabels="结算对象编码|结算对象名称" searchKeys="settleObjectCode|settleObjectName"
                                   concatId="settleObjectName" concatName="settleObjectName"/>
                </td>
                <td class="width-10"><label class="pull-right">结算对象名称</label></td>
                <td class="width-15">
                    <form:input path="settleObjectName" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">负责人</label></td>
                <td class="width-15">
                    <form:input path="responsiblePerson" htmlEscape="false" class="form-control"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">备注信息</label></td>
                <td class="width-15" colspan="7">
                    <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="255"/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</form:form>
<div class="tabs-container">
    <ul class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#tab-detail" aria-expanded="true">模型明细</a></li>
    </ul>
    <div class="tab-content">
        <div id="tab-detail" class="tab-pane fade in active">
            <div style="width: 100%; padding: 5px 0;">
                <a id="refresh" class="btn btn-default" onclick="refresh()"><i class="glyphicon glyphicon-refresh icon-refresh"></i> 刷新</a>
                <a id="generate" class="btn btn-primary" disabled onclick="generate()">生成条款明细</a>
                <shiro:hasPermission name="bms:finance:bmsSettleModel:edit">
                    <a id="confirm" class="btn btn-primary" disabled="true" onclick="paramSubmit();">确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:finance:bmsSettleModel:del">
                    <a id="del" class="btn btn-danger" disabled="true" onclick="delRow()">删除</a>
                </shiro:hasPermission>
            </div>
            <div id="tab-detail-left">
                <table id="bmsSettleModelTermsTable" class="table well text-nowrap"></table>
            </div>
            <div id="tab-detail-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form id="detailForm" class="form">
                    <div class="tabs-container" style="margin: 0">
                        <ul class="nav nav-tabs">
                            <li class="active"><a data-toggle="tab" href="#include-params" aria-expanded="true">包含参数配置</a></li>
                            <li class=""><a data-toggle="tab" href="#exclude-params" aria-expanded="true">排除参数配置</a></li>
                            <li class=""><a data-toggle="tab" href="#detail-info" aria-expanded="true">明细信息</a></li>
                        </ul>
                        <div class="tab-content">
                            <div id="include-params" class="tab-pane fade in active">
                                <table id="includeParams" class="table well"></table>
                            </div>
                            <div id="exclude-params" class="tab-pane fade">
                                <table id="excludeParams" class="table well"></table>
                            </div>
                            <div id="detail-info" class="tab-pane fade">
                                <table id="detailInfo" class="table well"></table>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script type="text/template" id="detailTpl">//<!--
<tbody>
<tr>
    <td class="hide">
        <input type="hidden" id="detail_id" name="id" value="{{row.id}}"/>
        <input type='hidden' id="detail_orgId" name="orgId" value="{{row.orgId}}"/>
    </td>
    <td class="width-10"><label class="pull-right">系统合同编号</label></td>
    <td class="width-15">
        <input type='text' class="form-control" name="sysContractNo" value="{{row.sysContractNo}}" readonly/>
    </td>
    <td class="width-10"><label class="pull-right">客户合同编号</label></td>
    <td class="width-15">
        <input type='text' class="form-control" name="contractNo" value="{{row.contractNo}}" readonly/>
    </td>
    <td class="width-10"><label class="pull-right">子合同编号</label></td>
    <td class="width-15">
        <input type='text' class="form-control" name="subcontractNo" value="{{row.subcontractNo}}" readonly/>
    </td>
</tr>
<tr>
    <td class="width-10"><label class="pull-right">费用模块</label></td>
    <td class="width-15">
        <select class="form-control m-b" name="billModule" data-value="{{row.billModule}}" disabled>
            <option value=""></option>
            <c:forEach items="${fns:getDictList('BMS_BILL_MODULE')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td class="width-10"><label class="pull-right">费用科目代码</label></td>
    <td class="width-15">
        <input type='text' class="form-control" name="billSubjectCode" value="{{row.billSubjectCode}}" readonly/>
    </td>
    <td class="width-10"><label class="pull-right">费用科目名称</label></td>
    <td class="width-15">
        <input type='text' class="form-control" name="billSubjectName" value="{{row.billSubjectName}}" readonly/>
    </td>
</tr>
<tr>
    <td class="width-10"><label class="pull-right">费用类别</label></td>
    <td class="width-15">
        <select class="form-control m-b" name="billCategory" data-value="{{row.billCategory}}" disabled>
            <option value=""></option>
            <c:forEach items="${fns:getDictList('BMS_BILL_SUBJECT_CATEGORY')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td class="width-10"><label class="pull-right">计费条款代码</label></td>
    <td class="width-15">
        <input type='text' class="form-control" name="billTermsCode" value="{{row.billTermsCode}}" readonly/>
    </td>
    <td class="width-10"><label class="pull-right">计费条款说明</label></td>
    <td class="width-15">
        <input type='text' class="form-control" name="billTermsDesc" value="{{row.billTermsDesc}}" readonly/>
    </td>
</tr>
<tr>
    <td class="width-10"><label class="pull-right">应收应付</label></td>
    <td class="width-15">
        <select class="form-control m-b" name="receivablePayable" data-value="{{row.receivablePayable}}" disabled>
            <option value=""></option>
            <c:forEach items="${fns:getDictList('BMS_RECEIVABLE_PAYABLE')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td class="width-10"><label class="pull-right">输出对象</label></td>
    <td class="width-15">
        <select class="form-control m-b" name="outputObjects" data-value="{{row.outputObjects}}" disabled>
            <option value=""></option>
            <c:forEach items="${fns:getDictList('BMS_OUTPUT_OBJECT')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td class="width-10"><label class="pull-right">发生量</label></td>
    <td class="width-15">
        <select class="form-control m-b" name="occurrenceQuantity" data-value="{{row.occurrenceQuantity}}" disabled>
            <option value=""></option>
            <c:forEach items="${fns:getDictList('BMS_OCCURRENCE_QUANTITY')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
</tr>
<tr>
    <td class="width-10"><label class="pull-right">公式</label></td>
    <td class="width-15">
        <input type='text' class="form-control" name="formulaName" value="{{row.formulaName}}" readonly/>
    </td>
    <td class="width-10"><label class="pull-right">机构</label></td>
    <td class="width-15">
        <input type='text' class="form-control" name="orgName" value="{{row.orgName}}" readonly/>
    </td>
    <td class="width-10"></td>
    <td class="width-15"></td>
</tr>
</tbody>//-->
</script>
</body>
</html>