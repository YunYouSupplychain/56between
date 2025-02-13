<%--标准弹出窗--%>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题" %>
<%@ attribute name="url" type="java.lang.String" required="true" description="数据地址" %>
<%@ attribute name="fieldId" type="java.lang.String" required="true" description="隐藏属性Id" %>
<%@ attribute name="fieldName" type="java.lang.String" required="true" description="隐藏属性名" %>
<%@ attribute name="fieldKeyName" type="java.lang.String" required="true" description="隐藏属性对应字段名" %>
<%@ attribute name="fieldValue" type="java.lang.String" required="false" description="隐藏属性值" %>
<%@ attribute name="displayFieldId" type="java.lang.String" required="true" description="显示属性Id" %>
<%@ attribute name="displayFieldName" type="java.lang.String" required="true" description="显示属性名" %>
<%@ attribute name="displayFieldKeyName" type="java.lang.String" required="true" description="显示属性对应字段名" %>
<%@ attribute name="displayFieldValue" type="java.lang.String" required="false" description="显示属性值" %>
<%@ attribute name="fieldLabels" type="java.lang.String" required="true" description="表格Th里显示的名字" %>
<%@ attribute name="fieldKeys" type="java.lang.String" required="true" description="表格Td里显示的值" %>
<%@ attribute name="searchLabels" type="java.lang.String" required="true" description="表格Td里显示的值" %>
<%@ attribute name="searchKeys" type="java.lang.String" required="true" description="表格Td里显示的值" %>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式" %>
<%@ attribute name="queryParams" type="java.lang.String" required="false" description="查询参数key" %>
<%@ attribute name="queryParamValues" type="java.lang.String" required="false" description="查询参数value" %>
<%@ attribute name="isMultiSelected" type="java.lang.Boolean" required="false" description="是否多选(true/false)" %>
<%@ attribute name="disabled" type="java.lang.Boolean" required="false" description="是否按钮不可用(true/false)" %>
<%@ attribute name="readonly" type="java.lang.Boolean" required="false" description="是否限制选择(true/false)" %>
<%@ attribute name="afterSelect" type="java.lang.String" required="false" description="选择记录之后调用的函数" %>
<div class="input-group" style="width: 100%">
    <c:if test="${not empty fieldId}"><input id="${fieldId}" name="${fieldName}" type="hidden" value="${fieldValue}"/></c:if>
    <input id="${displayFieldId}" name="${displayFieldName}" type="text" ${readonly eq true ? 'readonly' : ''} value="${displayFieldValue}" class="${cssClass}" autocomplete="off" style="border-radius: 2px;"/>
    <span class="input-group-btn">
        <button type="button" id="${displayFieldId}SBtnId" class="btn btn-primary" ${disabled eq true ? 'disabled' : ''}><i class="fa fa-search"></i></button>
        <button type="button" id="${displayFieldId}DBtnId" class="close" ${disabled eq true ? 'disabled' : ''} data-dismiss="alert" style="position: absolute; top: 5px; right: 32px; z-index: 999; display: block;">×</button>
    </span>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        <c:if test="${not empty fieldId}">
        var ${fieldId}V = "";
        </c:if>
        var ${displayFieldId}V = "";

        $("#${displayFieldId}").off('blur keyup focus').on('blur', function (e) {// 失去焦点
            if ($(this).val() !== ${displayFieldId}V) {
                <c:if test="${not empty fieldId}">
                $("#${fieldId}").val("");
                </c:if>
                $("#${displayFieldId}").val("");
            }
        }).on('focus', function (e) {
            <c:if test="${not empty fieldId}">
            ${fieldId}V = $("#${fieldId}").val();
            </c:if>
            ${displayFieldId}V = $("#${displayFieldId}").val();
        }).on('keyup', function (e) {
            if (e.keyCode === 13 && !$(this).prop('readonly')) {// 输入框回车
                var queryParams = "${queryParams}", queryParamValues = "${queryParamValues}";
                // 将输入框中的值作为“codeAndName”的参数值传入后台
                <c:choose>
                    <c:when test="${not empty queryParams}">
                    queryParams = queryParams + "|codeAndName";
                    queryParamValues = queryParamValues + "|${displayFieldId}";
                    </c:when>
                    <c:otherwise>
                    queryParams = "codeAndName";
                    queryParamValues = "${displayFieldId}";
                    </c:otherwise>
                </c:choose>
                var params = bq.parseParams(queryParams, queryParamValues);
                var keys = queryParams, values = bq.getQueryParamsValue(queryParamValues);
                $("#${displayFieldId}").blur();
                jp.get("${url}?a=b" + params, function (data) {
                    if (data.total > 1) {
                        eval("${displayFieldId}Grid('" + keys + "','" + values + "')");
                    } else if (data.total === 1) {
                        eval("${displayFieldId}SetValue(data.rows[0])");
                    } else if (data.total === 0) {
                        <c:if test="${not empty fieldId}">
                        $("#${fieldId}").val("");
                        </c:if>
                        $("#${displayFieldId}").val("").focus();
                    }
                });
            }
        });

        $("#${displayFieldId}SBtnId").off('click').on('click', function (e) {
            <c:if test="${not empty fieldId}">
            ${fieldId}V = $("#${fieldId}").val();
            </c:if>
            ${displayFieldId}V = $("#${displayFieldId}").val();

            var keys = "${queryParams}", values = bq.getQueryParamsValue("${queryParamValues}");
            eval("${displayFieldId}Grid('" + keys + "', '" + values + "')");
        });

        $("#${displayFieldId}DBtnId").click(function (e) {
            <c:if test="${not empty fieldId}">
            $("#${fieldId}").val("");
            ${fieldId}V = "";
            </c:if>
            $("#${displayFieldId}").val("").focus();
            ${displayFieldId}V = "";
        });

        function ${displayFieldId}Grid(keys, values) {
            top.layer.open({
                type: 2,
                area: ['1000px', '600px'],
                title: "${title}",
                auto: true,
                name: 'friend',
                content: "${ctx}/tag/grid?url=" + encodeURIComponent("${url}?a=b")
                    + "&queryParams=" + encodeURIComponent(keys) + "&queryParamValues=" + encodeURIComponent(values)
                    + "&fieldLabels=" + encodeURIComponent("${fieldLabels}") + "&fieldKeys=" + encodeURIComponent("${fieldKeys}")
                    + "&searchLabels=" + encodeURIComponent("${searchLabels}") + "&searchKeys=" + encodeURIComponent("${searchKeys}")
                    + "&isMultiSelected=${isMultiSelected eq true ? true : false}",
                btn: ['确定', '关闭'],
                yes: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    var items = iframeWin.getSelections();
                    if (items.length <= 0) {
                        jp.warning("必须选择一条数据!");
                        return;
                    }
                    if (${isMultiSelected eq true ? true : false}) {
                        eval("${displayFieldId}SetValue(items)");
                    } else {
                        eval("${displayFieldId}SetValue(items[0])");
                    }
                    top.layer.close(index);
                },
                btn2: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    eval("${displayFieldId}SetValue(iframeWin.currentRow)");
                },
                cancel: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    eval("${displayFieldId}SetValue(iframeWin.currentRow)");
                }
            });
        }

        function ${displayFieldId}SetValue(rows) {
            $("#${displayFieldId}").focus();
            if (rows) {
                var fieldIdV, displayFieldIdV;
                if (Array.isArray(rows)) {
                    <c:if test="${not empty fieldKeyName}">
                    fieldIdV = $.map(rows, function (row) {
                        return eval("row.${fieldKeyName}");
                    }).join(',');
                    </c:if>
                    displayFieldIdV = $.map(rows, function (row) {
                        return eval("row.${displayFieldKeyName}");
                    }).join(',');
                } else {
                    var row = rows;
                    <c:if test="${not empty fieldKeyName}">
                    fieldIdV = eval("row.${fieldKeyName}");
                    </c:if>
                    displayFieldIdV = eval("row.${displayFieldKeyName}");
                }
                <c:if test="${not empty fieldId}">
                    $("#${fieldId}").val(fieldIdV);
                    ${fieldId}V = fieldIdV;
                </c:if>
                $("#${displayFieldId}").val(displayFieldIdV);
                ${displayFieldId}V = displayFieldIdV;

                <c:if test="${not empty afterSelect}">
                var index = "${afterSelect}".indexOf("(");
                var func, funcName;
                if (index === -1) {
                    func = "${afterSelect}" + "(rows)";
                    funcName = "${afterSelect}";
                } else {
                    func = "${afterSelect}".replace("(", "(rows, ");
                    funcName = "${afterSelect}".substring(0, index);
                }
                try {
                    if (typeof (eval(funcName)) == "function") {
                        eval(func);
                    }
                } catch (e) {

                }
                </c:if>
            }
        }
    });
</script>