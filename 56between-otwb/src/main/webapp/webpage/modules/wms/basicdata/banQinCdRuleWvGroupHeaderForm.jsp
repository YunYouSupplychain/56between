<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>波次规则组管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var disabledObj = [];
        $(document).ready(function () {
            init();
        });
        
        function init() {
            if ($('#id').val()) {
                $('#groupCode').prop('readonly', true);
                initDetail();
            } else {
                $('#orgId').val(jp.getCurrentOrg().orgId);
            }
            $('#orderDateFm').datetimepicker({format: "HH:mm:ss"});
            $('#orderDateTo').datetimepicker({format: "HH:mm:ss"});
        }

        /**
         * 单头保存
         */
        function save() {
            var isValidate = bq.headerSubmitCheck('#inputForm');
            if (isValidate.isSuccess) {
                openDisable('#inputForm');
                jp.loading();
                var params = bq.serializeJson($('#inputForm'));
                if (params.addrArea) {
                    var addrArray = params.addrArea.split(':');
                    if (addrArray.length !== 3) {
                        closeDisable();
                        jp.bqError("三级地址格式维护错误");
                        return;
                    }
                }
                params.orderDateFm = '1970-01-01 ' + params.orderDateFm;
                params.orderDateTo = '1970-01-01 ' + params.orderDateTo;
                jp.post("${ctx}/wms/basicdata/banQinCdRuleWvGroupHeader/save", params, function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        window.location = "${ctx}/wms/basicdata/banQinCdRuleWvGroupHeader/form?id=" + data.body.entity.id;
                    } else {
                        closeDisable();
                        jp.bqError(data.msg);
                    }
                })
            } else {
                jp.bqError(isValidate.msg);
            }
        }

        /**
         * 修改表单中disable状态
         */
        function openDisable(obj) {
            $(obj + " :disabled").each(function () {
                if ($(this).val()) {
                    $(this).prop("disabled", false);
                    disabledObj.push($(this));
                }
            });
            return true;
        }

        /**
         * 打开disabled状态
         */
        function closeDisable() {
            for (var i = 0; i < disabledObj.length; i++) {
                $(disabledObj[i]).prop("disabled", true);
            }
            disabledObj = [];
            return true;
        }

        /**
         * 初始化明细
         */
        function initDetail() {
            $('#detailList').empty();
            var params = "headerId=" + ($('#id').val() ? $('#id').val() : '#');
            params += "&orgId=" + ($('#orgId').val() ? $('#orgId').val() : jp.getCurrentOrg.orgId);
            jp.post("${ctx}/wms/basicdata/banQinCdRuleWvGroupDetail/data?" + params, null, function (data) {
                ruleDetailRowIdx = 0;
                for (var i = 0; i < data.length; i++) {
                    addDetailRow('#detailList', ruleDetailRowIdx, ruleDetailTpl, data[i]);
                    ruleDetailRowIdx = ruleDetailRowIdx + 1;
                }
            })
        }

        function addDetail() {
            addDetailRow('#detailList', ruleDetailRowIdx, ruleDetailTpl);
            ruleDetailRowIdx = ruleDetailRowIdx + 1;
        }

        function addDetailRow(list, idx, tpl, row) {
            if (!row) {
                tpl = tpl.replace("{{row.orgId}}", $('#orgId').val());
                tpl = tpl.replace("{{row.headerId}}", $('#id').val());
                tpl = tpl.replace("{{row.groupCode}}", $('#groupCode').val());
            }
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
        }

        /**
         * 保存明细
         */
        function saveDetail() {
            if (!$('#id').val()) {
                jp.warning('请先保存单头!');
                return;
            }

            // 校验数据
            var isValidate = bq.tableValidate('#detailTable');
            if (!isValidate.isSuccess) {
                jp.warning(isValidate.msg);
                return;
            }
            jp.loading();
            jp.post("${ctx}/wms/basicdata/banQinCdRuleWvGroupDetail/save", $('#inputForm2').bq_serialize(), function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    initDetail();
                } else {
                    jp.bqError(data.msg);
                }
            })
        }

        function removeDetail() {
            var ids = [], idRows = [];
            $("input[name='detailSelect']:checked").each(function() {
                var idx = $(this).prop('id').split('_')[0];
                ids.push($('#' + idx + '_id').val());
                idRows.push(idx + '_id');
            });
            if (ids.length === 0) {
                jp.warning("请勾选记录");
                return;
            }
            jp.loading();
            jp.get("${ctx}/wms/basicdata/banQinCdRuleWvGroupDetail/deleteAll?ids=" + ids.join(","), function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    for (var i in idRows) {
                        $('#' + idRows[i]).parent().parent().remove();
                    }
                    ruleDetailRowIdx -= ids.length;
                } else {
                    jp.bqError(data.msg);
                }
            })
        }

        function detailSelectChange(flag) {
            $("input[name='detailSelect']:checkbox").prop('checked', flag);
        }

        function ruleSelect(idx) {
            top.layer.open({
                type: 2,
                area: ['1000px', '600px'],
                title: "选择波次规则",
                auto: true,
                name: 'friend',
                content: "${ctx}/tag/popSelect?url=" + encodeURIComponent("${ctx}/wms/basicdata/banQinCdRuleWvHeader/grid?orgId=" + jp.getCurrentOrg().orgId) + "&fieldLabels=" + encodeURIComponent("规则编码|规则名称") + "&fieldKeys=" + encodeURIComponent("ruleCode|ruleName") + "&searchLabels=" + encodeURIComponent("规则编码|规则名称") + "&searchKeys=" + encodeURIComponent("ruleCode|ruleName") + "&isMultiSelected=false",
                btn: ['确定', '关闭'],
                yes: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    var items = iframeWin.getSelections();
                    if (items == "") {
                        jp.warning("必须选择一条数据!");
                        return;
                    }
                    $('#detailList' + idx + "_ruleCode").val(items[0].ruleCode);
                    $('#detailList' + idx + "_ruleName").val(items[0].ruleName);
                    top.layer.close(index);
                },
                cancel: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    var row = iframeWin.currentRow;
                    if (row) {
                        $('#detailList' + idx + "_ruleName").val(row.ruleName);
                        $('#detailList' + idx + "_ruleCode").val(row.ruleCode);
                    }
                }
            });
        }

        function skuSelect() {
            if (!$('#ownerCode').val()) {
                jp.warning("货主不能为空");
                return;
            }

            top.layer.open({
                type: 2,
                area: ['1000px', '600px'],
                title: "选择商品",
                auto: true,
                name: 'friend',
                content: "${ctx}/tag/popSelect?url=" + encodeURIComponent("${ctx}/wms/basicdata/banQinCdWhSku/grid?orgId=" + jp.getCurrentOrg().orgId + "|ownerCode=" + $('#ownerCode').val()) + "&fieldLabels=" + encodeURIComponent("货主编码|货主名称|商品编码|商品名称") + "&fieldKeys=" + encodeURIComponent("ownerCode|ownerName|skuCode|skuName") + "&searchLabels=" + encodeURIComponent("商品编码|商品名称") + "&searchKeys=" + encodeURIComponent("skuCode|skuName") + "&isMultiSelected=true",
                btn: ['确定', '关闭'],
                yes: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    var items = iframeWin.getSelections();
                    if (items == "") {
                        jp.warning("必须选择一条数据!");
                        return;
                    }
                    var skuCodes = [], skuNames = [];
                    for (var i in items) {
                        skuCodes.push(items[i].skuCode);
                        skuNames.push(items[i].skuName);
                    }
                    $("#skuCode").val(skuCodes.join(","));
                    $("#skuName").val(skuNames.join(","));
                    top.layer.close(index);
                },
                cancel: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    var row = iframeWin.currentRow;
                    if (row) {
                        $("#skuCode").val(row.skuCode);
                        $("#skuName").val(row.skuName);
                    }
                }
            });
        }

        function skuDelete() {
            $("#skuCode").val('');
            $("#skuName").val('');
        }
    </script>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 20px;">
    <shiro:hasPermission name="basicdata:banQinCdRuleWvGroupHeader:edit">
        <a class="btn btn-primary btn-sm" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
</div>
<div class="wrapper wrapper-content-my">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">基础信息</h3>
        </div>
        <div class="panel-body">
            <form:form id="inputForm" modelAttribute="banQinCdRuleWvGroupHeader" method="post" class="form-horizontal">
                <form:hidden path="id"/>
                <form:hidden path="orgId"/>
                <table class="table">
                    <tbody>
                    <tr>
                        <td width="8%"><label class="pull-right"><font color="red">*</font>规则组编码</label></td>
                        <td width="12%">
                            <form:input path="groupCode" htmlEscape="false" class="form-control required" maxlength="32"/>
                        </td>
                        <td width="8%"><label class="pull-right"><font color="red">*</font>规则组名称</label></td>
                        <td width="12%">
                            <form:input path="groupName" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td width="8%"><label class="pull-right"><font color="red">*</font>订单时间从</label></td>
                        <td width="12%">
                            <div class='input-group form_datetime' id='orderDateFm'>
                                <input name="orderDateFm" class="form-control required"
                                       value="<fmt:formatDate value="${banQinCdRuleWvGroupHeader.orderDateFm}" pattern="HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td width="8%"><label class="pull-right"><font color="red">*</font>订单时间到</label></td>
                        <td width="12%">
                            <div class='input-group form_datetime' id='orderDateTo'>
                                <input name="orderDateTo" class="form-control required"
                                       value="<fmt:formatDate value="${banQinCdRuleWvGroupHeader.orderDateTo}" pattern="HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">货主</label></td>
                        <td class="" width="12%">
                            <input id="ownerType" value="OWNER" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                           fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="${banQinCdRuleWvGroupHeader.ownerCode}" allowInput="true"
                                           displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${banQinCdRuleWvGroupHeader.ownerName}"
                                           selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                           fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           queryParams="ebcuType" queryParamValues="ownerType" isMultiSelected="false">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right">商品</label></td>
                        <td class="" colspan="3">
                            <div class="input-group" style="width: 100%">
                                <input id="skuCode" name="skuCode" type="hidden">
                                <input id="skuName" readonly name="skuName" value="${banQinCdRuleWvGroupHeader.skuName}" data-msg-required="" class="form-control width:0px" style="" aria-required="true" type="text">
                                <span class="input-group-btn">
                                    <button type="button" onclick="skuSelect()" class="btn btn-primary"><i class="fa fa-search"></i></button>
                                    <button type="button" onclick="skuDelete()" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
                                </span>
                            </div>
                        </td>
                        <td width="8%"><label class="pull-right">三级地址</label></td>
                        <td width="12%">
                            <input id="addrArea" name="addrArea" class="form-control" value="${banQinCdRuleWvGroupHeader.addrArea}" placeholder="示例江苏省:苏州市:吴中区" maxlength="64">
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form:form>
        </div>
    </div>
</div>
<div class="wrapper wrapper-content-my">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">明细信息</h3>
        </div>
        <div class="panel-body">
            <div id="detailToolbar" style="width: 100%; padding-top: 5px; padding-left: 5px;">
                <shiro:hasPermission name="basicdata:banQinCdRuleWvGroupDetail:addDetail">
                    <a class="btn btn-primary btn-sm" onclick="addDetail()">新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basicdata:banQinCdRuleWvGroupDetail:saveDetail">
                    <a class="btn btn-primary btn-sm" onclick="saveDetail()">保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basicdata:banQinCdRuleWvGroupDetail:removeDetail">
                    <a class="btn btn-danger btn-sm" onclick="removeDetail()">删除</a>
                </shiro:hasPermission>
            </div>
            <form:form id="inputForm2" method="post" class="form-horizontal">
            <table id="detailTable" class="table">
                <thead>
                <tr>
                    <th class="hide"></th>
                    <th class="th-inner" width="20"><input type="checkbox" onclick="detailSelectChange(this.checked)"/></th>
                    <th>行号<font color="red">*</font></th>
                    <th>规则编码<font color="red">*</font></th>
                    <th>规则名称</th>
                </tr>
                </thead>
                <tbody id="detailList">
                </tbody>
            </table>
            <script type="text/template" id="ruleDetailTpl">//<!--
            <tr id="detailList{{idx}}">
                <td class="hide">
                    <input id="detailList{{idx}}_id" name="detailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
                    <input id="detailList{{idx}}_delFlag" name="detailList[{{idx}}].delFlag" type="hidden" value="0"/>
                    <input id="detailList{{idx}}_orgId" name="detailList[{{idx}}].orgId" type="hidden" value="{{row.orgId}}"/>
                    <input id="detailList{{idx}}_headerId" name="detailList[{{idx}}].headerId" type="hidden" value="{{row.headerId}}"/>
                    <input id="detailList{{idx}}_groupCode" name="detailList[{{idx}}].groupCode" type="hidden" value="{{row.groupCode}}"/>
                </td>
                <td>
                    <input id="detailList{{idx}}_checkbox" type="checkbox" class="customizeCheckbox" name="detailSelect"/>
                </td>
                <td>
                    <input id="detailList{{idx}}_lineNo" name="detailList[{{idx}}].lineNo" type="text" value="{{row.lineNo}}" class="form-control required" maxLength="2"/>
                </td>
                <td>
                    <div class="input-group" style="width: 100%">
                        <input id="detailList{{idx}}_ruleCode" readonly name="detailList[{{idx}}].ruleCode" value="{{row.ruleCode}}" data-msg-required="" class="form-control required width:0px" style="" aria-required="true" type="text">
                        <span class="input-group-btn">
                            <button type="button" onclick="ruleSelect({{idx}})" class="btn btn-primary"><i class="fa fa-search"></i></button>
                            <button type="button" onclick="ruleDelete({{idx}})" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
                        </span>
                    </div>
                </td>
                <td>
                    <input id="detailList{{idx}}_ruleName" name="detailList[{{idx}}].ruleName" type="text" value="{{row.ruleName}}" readonly class="form-control"/>
                </td>
            </tr>//-->
            </script>
            <script type="text/javascript">
                var ruleDetailRowIdx = 0, ruleDetailTpl = $("#ruleDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
            </script>
            </form:form>
        </div>
    </div>
</div>
</body>
</html>