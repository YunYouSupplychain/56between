<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>计费公式管理</title>
    <meta name="decorator" content="ani"/>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="bmsBillFormulaEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <table class="table well">
        <tbody>
        <tr>
            <td class="width-8"><label class="pull-right asterisk">公式编码</label></td>
            <td class="width-12">
                <form:input path="formulaCode" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-8"><label class="pull-right asterisk">公式</label></td>
            <td class="width-12">
                <form:input path="formula" htmlEscape="false" class="form-control required" placeholder="变量以p+数字组成，如:p1*p2*p3"/>
            </td>
            <td class="width-8"><label class="pull-right">公式名称</label></td>
            <td class="width-12">
                <form:input path="formulaName" htmlEscape="false" class="form-control" readonly="true"/>
            </td>
            <td class="width-8"><label class="pull-right">公式说明</label></td>
            <td class="width-12">
                <form:input path="remarks" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        </tbody>
    </table>
    <div class="tabs-container">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">公式参数：</a></li>
        </ul>
        <div class="tab-content">
            <div id="tab-1" class="tab-pane fade in  active">
                <table class="detail table">
                    <thead>
                    <tr>
                        <th class="hide"></th>
                        <th>参数名</th>
                        <th><font color="red">*</font>参数值</th>
                        <th width="10">&nbsp;</th>
                    </tr>
                    </thead>
                    <tbody id="parameters"></tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<script type="text/template" id="parametersTpl">//<!--
<tr id="parameters{{idx}}">
    <td class="hide">
        <input id="parameters{{idx}}_id" name="parameters[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="parameters{{idx}}_delFlag" name="parameters[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="parameters{{idx}}_recVer" name="parameters[{{idx}}].recVer" type="hidden" value="{{row.recVer}}"/>
        <input id="parameters{{idx}}_formulaCode" name="parameters[{{idx}}].formulaCode" type="hidden" value="{{row.formulaCode}}"/>
    </td>
    <td>
        <input id="parameters{{idx}}_parameterName" name="parameters[{{idx}}].parameterName" type="text" value="{{row.parameterName}}" class="form-control " readonly/>
    </td>
    <td>
        <select id="parameters{{idx}}_parameterValue" name="parameters[{{idx}}].parameterValue" data-value="{{row.parameterValue}}" class="form-control required">
            <option value=""></option>
            <c:forEach items="${fns:getDictList('BMS_BILL_FORMULA_PARAM')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
</tr>
//-->
</script>
<script type="text/javascript">
    var parametersIdx = 0, parametersTpl = $("#parametersTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $(document).ready(function () {
        var parameters = ${fns:toJson(bmsBillFormulaEntity.parameters)};
        for (var i = 0; i < parameters.length; i++) {
            addRow("#parameters", parametersIdx, parametersTpl, parameters[i]);
            parametersIdx++;
        }
        if ($("#id").val()) {
            $("#formulaCode").prop("readonly", true);
            $("#formulaName").prop("readonly", true);
        }
        $('#formula').on('keyup', function (e) {
            if (e.keyCode !== 13) {
                return;
            }
            var formula = $("#formula").val();
            var formulaSplit = formula.split(/[*/()+-]/);
            var params = [];
            for (var i = 0; i < formulaSplit.length; i++) {
                var parameterName = formulaSplit[i];
                if (parameterName.match(/[a-z]/ig) != null
                    && params.indexOf(parameterName) === -1) {
                    params.push(parameterName);
                }
            }
            params.sort();
            $("#parameters").empty();
            parametersIdx = 0;
            for (var j = 0; j < params.length; j++) {
                addRow("#parameters", parametersIdx, parametersTpl, {parameterName: params[j], recVer: 0});
                parametersIdx++;
            }
        });
    });

    function doSubmit($table, $topIndex) {
        var validate1 = bq.headerSubmitCheck("table[class~='head']");
        var validate2 = bq.tableValidate("table[class~='detail']");
        if (!(validate1.isSuccess && validate2.isSuccess)) {
            jp.bqError(!validate1.msg ? validate2.msg : validate1.msg);
            return;
        }
        jp.loading();
        jp.post("${ctx}/bms/basic/bmsBillFormula/save", $('#inputForm').serialize(), function (data) {
            if (data.success) {
                $table.bootstrapTable('refresh');
                jp.success(data.msg);
                jp.close($topIndex);//关闭dialog
            } else {
                jp.bqError(data.msg);
            }
        });
    }

    function addRow(list, idx, tpl, row) {
        $(list).append(Mustache.render(tpl, {
            idx: idx, delBtn: true, row: row
        }));
        $(list + idx).find("select").each(function () {
            $(this).val($(this).attr("data-value"));
        });
    }
</script>
</body>
</html>