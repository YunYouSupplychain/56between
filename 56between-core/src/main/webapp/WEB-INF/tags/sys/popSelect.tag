<%--弃用，标准弹出窗使用<sys:grid>--%>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题" %>
<%@ attribute name="url" type="java.lang.String" required="true" description="数据地址" %>
<%@ attribute name="fieldId" type="java.lang.String" required="true" description="隐藏属性Id" %>
<%@ attribute name="fieldKeyName" type="java.lang.String" required="true" description="隐藏属性对应字段名" %>
<%@ attribute name="fieldName" type="java.lang.String" required="true" description="隐藏属性名" %>
<%@ attribute name="fieldValue" type="java.lang.String" required="true" description="隐藏属性值" %>
<%@ attribute name="displayFieldId" type="java.lang.String" required="true" description="显示属性Id" %>
<%@ attribute name="displayFieldName" type="java.lang.String" required="true" description="显示属性名" %>
<%@ attribute name="displayFieldKeyName" type="java.lang.String" required="true" description="显示属性对应字段名" %>
<%@ attribute name="displayFieldValue" type="java.lang.String" required="true" description="显示属性值" %>
<%@ attribute name="fieldLabels" type="java.lang.String" required="true" description="表格Th里显示的名字" %>
<%@ attribute name="fieldKeys" type="java.lang.String" required="true" description="表格Td里显示的值" %>
<%@ attribute name="searchLabels" type="java.lang.String" required="true" description="表格Td里显示的值" %>
<%@ attribute name="searchKeys" type="java.lang.String" required="true" description="表格Td里显示的值" %>
<%@ attribute name="selectButtonId" type="java.lang.String" required="true" description="选择按钮Id" %>
<%@ attribute name="deleteButtonId" type="java.lang.String" required="true" description="删除按钮Id" %>
<%@ attribute name="queryParams" type="java.lang.String" required="false" description="查询参数key" %>
<%@ attribute name="queryParamValues" type="java.lang.String" required="false" description="查询参数value" %>
<%@ attribute name="concatId" type="java.lang.String" required="false" description="级联结果属性字段名" %>
<%@ attribute name="concatName" type="java.lang.String" required="false" description="级联结果Id集合" %>
<%@ attribute name="inputSearchKey" type="java.lang.String" required="false" description="文本框查询的key" %>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式" %>
<%@ attribute name="isMultiSelected" type="java.lang.Boolean" required="false" description="css样式" %>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式" %>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled" %>
<%@ attribute name="allowInput" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为readonly" %>
<%@ attribute name="afterSelect" type="java.lang.String" required="false" description="选择记录之后调用的函数" %>
<div class="input-group" style="width: 100%">
    <c:if test="${not empty fieldId}">
    <input id="${fieldId}" name="${fieldName}" type="hidden" value="${fieldValue}"/>
    </c:if>
    <input id="${displayFieldId}" name="${displayFieldName}" ${allowInput ? '' : 'readonly'} type="text" value="${displayFieldValue}" data-msg-required="${dataMsgRequired}" class="${cssClass}" style="border-radius: 2px;"/>
    <span class="input-group-btn">
        <button type="button" id="${selectButtonId}" class="btn btn-primary ${disabled} ${hideBtn ? 'hide' : ''}"><i class="fa fa-search"></i></button>
        <button type="button" id="${deleteButtonId}" ${disabled} class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 32px; z-index: 999; display: block;">×</button>
    </span>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        window['${displayFieldId}'] = "${displayFieldValue}";
        $("#${selectButtonId}").click(function () {
            var lastVar = "${displayFieldId}";
            if ($("#${selectButtonId}").hasClass("disabled")) {
                return true;
            }
            top.layer.open({
                type: 2,
                area: ['1200px', '750px'],
                title: "${title}",
                auto: true,
                name: 'friend',
                content: "${ctx}/tag/popSelect?url=" + encodeURIComponent("${url}" + "?orgId=" + jp.getCurrentOrg().orgId)
                    + "&queryParams=" + encodeURIComponent("${queryParams}")
                    + "&queryParamValues=" + encodeURIComponent(bq.getQueryParamsValue("${queryParamValues}"))
                    + "&fieldLabels=" + encodeURIComponent("${fieldLabels}")
                    + "&fieldKeys=" + encodeURIComponent("${fieldKeys}")
                    + "&searchLabels=" + encodeURIComponent("${searchLabels}")
                    + "&searchKeys=" + encodeURIComponent("${searchKeys}")
                    + "&inputSearchKey=" + encodeURIComponent("codeAndName")
                    + "&inputSearchValue="
                    + "&isMultiSelected=${isMultiSelected? true:false}",
                btn: ['确定', '关闭'],
                yes: function (idx, layero) {
                    // 得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    var items = iframeWin.getSelections();
                    if (items == '') {
                        jp.warning("必须选择一条数据!");
                        return;
                    }
                    if ("${fieldId}") {
                        $("#${fieldId}").val(eval("items[0].${fieldKeyName}"));
                    }
                    $("#${displayFieldId}").val(eval("items[0].${displayFieldKeyName}"));
                    if ("${concatId}" && "${concatName}") {
                        var fieldIds = "${concatId}".split(",");
                        var fieldName = "${concatName}".split(",");
                        for (var i in fieldIds) {
                            $("#" + fieldIds[i]).val((eval("items[0]." + fieldName[i])));
                            eval(fieldIds[i] + " = '" + $("#" + fieldIds[i]).val() + "'");
                        }
                    }
                    window[lastVar] = $("#${displayFieldId}").val();
                    <c:if test="${not empty afterSelect}">
                    var index = "${afterSelect}".indexOf("(");
                    var func, funcName;
                    if (index === -1) {
                        func = "${afterSelect}" + ('${isMultiSelected}' ? "(items)" : "(items[0])");
                        funcName = "${afterSelect}";
                    } else {
                        func = "${afterSelect}".replace("(", ('${isMultiSelected}' ? "(items, " : "(items[0], "));
                        funcName = "${afterSelect}".substring(0, index);
                    }
                    try {
                        if (typeof (eval(funcName)) == "function") {
                            eval(func);
                        }
                    } catch (e) {
                        console.log("function " + "${afterSelect}" + "不存在");
                    }
                    </c:if>
                    top.layer.close(idx);
                },
                cancel: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    var row = iframeWin.currentRow;
                    if (row) {
                        setValue(row);
                    }
                }
            });

            var setValue = function (row) {
                var item = row;
                if ("${fieldId}") {
                    $("#${fieldId}").val(eval("item.${fieldKeyName}"));
                }
                $("#${displayFieldId}").val(eval("item.${displayFieldKeyName}"));
                if ("${concatId}" && "${concatName}") {
                    var fieldIds = "${concatId}".split(",");
                    var fieldName = "${concatName}".split(",");
                    for (var i in fieldIds) {
                        $("#" + fieldIds[i]).val((eval("item." + fieldName[i])));
                        window[fieldIds[i]] = $("#" + fieldIds[i]).val();
                    }
                }
                window[lastVar] = $("#${displayFieldId}").val();
                <c:if test="${not empty afterSelect}">
                var index = "${afterSelect}".indexOf("(");
                var func, funcName;
                if (index === -1) {
                    func = "${afterSelect}" + "(item)";
                    funcName = "${afterSelect}";
                } else {
                    func = "${afterSelect}".replace("(", "(item, ");
                    funcName = "${afterSelect}".substring(0, index);
                }
                try {
                    if (typeof (eval(funcName)) == "function") {
                        eval(func);
                    }
                } catch (e) {
                    console.log("function " + "${afterSelect}" + "不存在");
                }
                </c:if>
            }
        });

        $("#${deleteButtonId}").click(function () {
            // 是否限制选择，如果限制，设置为disabled
            if ($("#${deleteButtonId}").hasClass("disabled")) {
                return true;
            }
            // 清除	
            if ("${fieldId}") {
                $("#${fieldId}").val("");
            }
            // 清除级联结果属性值
            if ("${concatId}") {
                var fieldIds = "${concatId}".split(",");
                for (var i in fieldIds) {
                    $("#" + fieldIds[i]).val("");
                }
            }
            $("#${displayFieldId}").val("").focus();
        });

        $("#${displayFieldId}").keydown(function () {
            var lastVar = "${displayFieldId}";
            if (event.keyCode === 13 && $("#${displayFieldId}").attr("readonly") !== 'readonly') {
                var currentValue = $("#${displayFieldId}").val();
                $("#${displayFieldId}").blur();
                jp.get("${url}" + "?orgId=" + jp.getCurrentOrg().orgId + bq.parseParams("${queryParams}", "${queryParamValues}") + "&codeAndName=" + currentValue, function (data) {
                    if (data.total === 0) {
                        jp.error("未查询到数据");
                    } else if (data.total === 1) {
                        var items = data.rows;
                        setValue(items[0]);
                    } else {
                        top.layer.open({
                            type: 2,
                            area: ['1200px', '750px'],
                            title: "${title}",
                            auto: true,
                            name: 'friend',
                            content: "${ctx}/tag/popSelect?url=" + encodeURIComponent("${url}" + "?orgId=" + jp.getCurrentOrg().orgId)
                                + "&queryParams=" + encodeURIComponent("${queryParams}")
                                + "&queryParamValues=" + encodeURIComponent(bq.getQueryParamsValue("${queryParamValues}"))
                                + "&fieldLabels=" + encodeURIComponent("${fieldLabels}")
                                + "&fieldKeys=" + encodeURIComponent("${fieldKeys}")
                                + "&searchLabels=" + encodeURIComponent("${searchLabels}")
                                + "&searchKeys=" + encodeURIComponent("${searchKeys}")
                                + "&inputSearchKey=" + encodeURIComponent("codeAndName")
                                + "&inputSearchValue=" + currentValue
                                + "&isMultiSelected=${isMultiSelected? true:false}",
                            btn: ['确定', '关闭'],
                            yes: function (index, layero) {
                                // 得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                                var iframeWin = layero.find('iframe')[0].contentWindow;
                                var items = iframeWin.getSelections();
                                if (items == '') {
                                    jp.warning("必须选择一条数据!");
                                    return;
                                }
                                setValue(items[0]);
                                top.layer.close(index);
                            },
                            cancel: function (index, layero) {
                                var iframeWin = layero.find('iframe')[0].contentWindow;
                                var row = iframeWin.currentRow;
                                if (row) {
                                    setValue(row);
                                }
                            }
                        });
                    }
                });
            }
            var setValue = function (row) {
                var item = row;
                if ("${fieldId}") {
                    $("#${fieldId}").val(eval("item.${fieldKeyName}"));
                }
                $("#${displayFieldId}").val(eval("item.${displayFieldKeyName}"));
                if ("${concatId}" && "${concatName}") {
                    var fieldIds = "${concatId}".split(",");
                    var fieldName = "${concatName}".split(",");
                    for (var i in fieldIds) {
                        $("#" + fieldIds[i]).val((eval("item." + fieldName[i])));
                        window[fieldIds[i]] = $("#" + fieldIds[i]).val();
                    }
                }
                window[lastVar] = $("#${displayFieldId}").val();
                <c:if test="${not empty afterSelect}">
                var index = "${afterSelect}".indexOf("(");
                var func, funcName;
                if (index === -1) {
                    func = "${afterSelect}" + "(item)";
                    funcName = "${afterSelect}";
                } else {
                    func = "${afterSelect}".replace("(", "(item, ");
                    funcName = "${afterSelect}".substring(0, index);
                }
                try {
                    if (typeof (eval(funcName)) == "function") {
                        eval(func);
                    }
                } catch (e) {
                    console.log("function " + "${afterSelect}" + "不存在");
                }
                </c:if>
            }
        });

        $("#${displayFieldId}").blur(function () {
            var lastVar = "${displayFieldId}";
            var lastValue = window[lastVar];
            if ('${displayFieldId}' && $('#${displayFieldId}').val() !== lastValue) {
                $("#${displayFieldId}").val('');
                window[lastVar] = '';
            }
        });

        $("#${displayFieldId}").focus(function () {
            var lastVar = "${displayFieldId}";
            window[lastVar] = $("#${displayFieldId}").val();
        });
    })

</script>
