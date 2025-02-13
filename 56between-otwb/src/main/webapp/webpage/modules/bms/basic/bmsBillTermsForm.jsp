<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>计费条款管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="bmsBillTermsForm.js" %>
    <style type="text/css">
        input[type='checkbox'] {
            width: 20px;
            height: 20px;
        }

        .btn-group .btn {
            margin: 5px 5px 5px 0;
        }
    </style>
</head>
<body>
<form:form id="inputForm" modelAttribute="bmsBillTermsEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-8"><label class="pull-right asterisk">计费条款代码</label></td>
            <td class="width-12">
                <form:input path="billTermsCode" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-8"><label class="pull-right asterisk">计费条款说明</label></td>
            <td class="width-12" colspan="3">
                <form:input path="billTermsDesc" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-8"><label class="pull-right asterisk">费用模块</label></td>
            <td class="width-12">
                <form:select path="billModule" class="form-control required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('BMS_BILL_MODULE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-8"><label class="pull-right asterisk">处理方法名称</label></td>
            <td class="width-12">
                <form:select path="methodName" class="form-control required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('BMS_BILL_TERMS_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-8"><label class="pull-right asterisk">输出对象</label></td>
            <td class="width-12">
                <form:select path="outputObjects" class="form-control required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('BMS_OUTPUT_OBJECT')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-8"><label class="pull-right">发生量</label></td>
            <td class="width-12">
                <form:select path="occurrenceQuantity" class="form-control">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('BMS_OCCURRENCE_QUANTITY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-8"><label class="pull-right">行业</label></td>
            <td class="width-12">
                <form:input path="useIndustry" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        <tr>
            <td class="width-8"><label class="pull-right">逻辑说明</label></td>
            <td class="width-12" colspan="9">
                <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="255"/>
            </td>
        </tr>
        </tbody>
    </table>
    <div class="tabs-container">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">条款参数条件配置：</a></li>
        </ul>
        <div class="tab-content">
            <div id="tab-1" class="tab-pane fade in  active">
                <div class="btn-group">
                    <a class="btn btn-primary" onclick="addRow();" title="新增"><i class="fa fa-plus"></i> 新增</a>
                    <a class="btn btn-danger" onclick="delRow();" title="删除"><i class="fa fa-trash"></i> 删除</a>
                </div>
                <table id="parameterTable" class="table text-nowrap">
                    <thead>
                    <tr>
                        <th class="hide"></th>
                        <th style="width:36px;vertical-align:middle">
                            <label><input type="checkbox" name="btSelectAll" style="width: 16px;height: 16px;"/></label>
                        </th>
                        <th><font color="red">*</font>字段</th>
                        <th><font color="red">*</font>描述</th>
                        <th><font color="red">*</font>格式</th>
                        <th>选项</th>
                        <th>默认值</th>
                        <th>启用</th>
                        <th>显示/隐藏</th>
                        <th>是否作为结算日期</th>
                    </tr>
                    </thead>
                    <tbody id="parameters"></tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<script type="text/template" id="parameterTpl">//<!--
<tr id="parameters{{idx}}">
    <td class="hide">
        <input type="hidden" id="parameters{{idx}}_id" name="parameters[{{idx}}].id" value="{{row.id}}"/>
        <input type="hidden" id="parameters{{idx}}_delFlag" name="parameters[{{idx}}].delFlag" value="0"/>
        <input type="hidden" id="parameters{{idx}}_recVer" name="parameters[{{idx}}].recVer" value="{{row.recVer}}"/>
        <input type="hidden" id="parameters{{idx}}_billTermsCode" name="parameters[{{idx}}].billTermsCode" value="{{row.billTermsCode}}"/>
        <input type="hidden" id="parameters{{idx}}_seqNo" name="parameters[{{idx}}].seqNo" value="{{idx}}"/>
    </td>
    <td style="vertical-align: middle">
        <input type="checkbox" name="btSelectItem" data-index="{{idx}}" style="width: 16px;height: 16px;"/>
    </td>
    <td>
        <input type="text" class="form-control" id="parameters{{idx}}_field" name="parameters[{{idx}}].field" value="{{row.field}}"/>
    </td>
    <td>
        <input type="text" class="form-control" id="parameters{{idx}}_title" name="parameters[{{idx}}].title" value="{{row.title}}"/>
    </td>
    <td>
        <select class="form-control required" id="parameters{{idx}}_type" name="parameters[{{idx}}].type" data-value="{{row.type}}" onchange="typeChange({{idx}})">
            <option value=""></option>
            <c:forEach items="${fns:getDictList('BMS_BILL_TERMS_PARAM_TYPE')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td>
        <input type="text" class="form-control" id="parameters{{idx}}_fieldOption" name="parameters[{{idx}}].fieldOption" value="{{row.fieldOption}}" readonly/>
    </td>
    <td>
        <input type="text" class="form-control" id="parameters{{idx}}_defaultValue" name="parameters[{{idx}}].defaultValue" value="{{row.defaultValue}}"/>
    </td>
    <td>
        <input type="checkbox" id="parameters{{idx}}_isEnable" name="parameters[{{idx}}].isEnable" value="{{row.isEnable}}" onchange="chxChange({{idx}}, '_isEnable')"/>
    </td>
    <td>
        <input type="checkbox" class="isShow" id="parameters{{idx}}_isShow" name="parameters[{{idx}}].isShow" value="{{row.isShow}}" onchange="chxChange({{idx}}, '_isShow')"/>
    </td>
    <td>
        <input type="checkbox" class="isSettleDate" id="parameters{{idx}}_isSettleDate" name="parameters[{{idx}}].isSettleDate" value="{{row.isSettleDate}}" onchange="chxChange({{idx}}, '_isSettleDate')" disabled/>
    </td>
</tr>//-->
</script>
</body>
</html>