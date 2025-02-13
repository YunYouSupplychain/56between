<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>补货任务管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        $(function () {
            loadLotAtt();
        });

        /**
         * 加载批次属性控件
         */
        function loadLotAtt() {
            var row = JSON.parse('${banQinWmTaskRp}');
            var params = "ownerCode=" + $('#ownerCode').val() + "&skuCode=" + $('#skuCode').val() + "&orgId=" + $('#orgId').val();
            bq.get("${ctx}/wms/basicdata/banQinCdWhLotDetail/getInfo?" + params, false, function (data) {
                if (data) {
                    $('#lotAttTable').empty();
                    var html = bq.getHorizontalLotAttTab(data, 'lotAtt');
                    html = html.replace(new RegExp("required", "g"), "");
                    html = html.split('<font color="red">*</font>').join('');
                    $('#lotAttTable').append(html);
                    $('#lotAttTable .form-control').each(function () {
                        $(this).prop('disabled', true);
                        var $Id = $(this).prop('id');
                        var $Name = $(this).prop('name');
                        $('#' + $Id).val(eval("row." + $Name));
                    });
                }
            })
        }

        function cancelTask() {
            jp.loading();
            jp.get("${ctx}/wms/task/banQinWmTaskRp/cancelTask?ids=" + $('#id').val(), function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    jp.close(parent.layer.getFrameIndex(window.name));
                } else {
                    jp.bqError(data.msg);
                }
            });
        }

        function confirmTask() {
            jp.loading();
            jp.get("${ctx}/wms/task/banQinWmTaskRp/confirmTask?ids=" + $('#id').val(), function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    window.location = "${ctx}/wms/task/banQinWmTaskRp/form?id=" + $('#id').val();
                } else {
                    jp.bqError(data.msg);
                }
            });
        }
    </script>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="task:banQinWmTaskRp:cancelTask">
        <a class="btn btn-primary" id="cancelTask" onclick="cancelTask()">取消补货任务</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="task:banQinWmTaskRp:confirmTask">
        <a class="btn btn-primary" id="confirmTask" onclick="confirmTask()">补货确认</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="banQinWmTaskRpEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <form:hidden path="ownerCode"/>
    <sys:message content="${message}"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">基础信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">补货任务Id</label></td>
                        <td class="width-15">
                            <form:input path="rpId" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">状态</label></td>
                        <td class="width-15">
                            <form:select path="status" class="form-control" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_TASK_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">货主</label></td>
                        <td class="width-15">
                            <form:input path="ownerName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">商品编码</label></td>
                        <td class="width-15">
                            <form:input path="skuCode" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">商品名称</label></td>
                        <td class="width-15">
                            <form:input path="skuName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">批次号</label></td>
                        <td class="width-15">
                            <form:input path="lotNum" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">包装规格</label></td>
                        <td class="width-15">
                            <form:input path="packDesc" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">包装单位</label></td>
                        <td class="width-15">
                            <input name="printUom" htmlEscape="false" class="form-control" value="EA" readonly>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">库存数</label></td>
                        <td class="width-15">
                            <form:input path="qty" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">库存可用数</label></td>
                        <td class="width-15">
                            <form:input path="qtyUse" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">冻结数</label></td>
                        <td class="width-15">
                            <form:input path="qtyHold" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">分配数</label></td>
                        <td class="width-15">
                            <form:input path="qtyAlloc" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">拣货数</label></td>
                        <td class="width-15">
                            <form:input path="qtyPk" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">上架待出数</label></td>
                        <td class="width-15">
                            <form:input path="qtyPaOut" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">补货待出数</label></td>
                        <td class="width-15">
                            <form:input path="qtyRpOut" htmlEscape="false" class="form-control" readonly="true"/>
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
                <h3 class="panel-title">补货信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">源库位</label></td>
                        <td class="width-15">
                            <form:input path="fmLoc" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">源跟踪号</label></td>
                        <td class="width-15">
                            <form:input path="fmId" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">>目标库位</label></td>
                        <td class="width-15">
                            <form:input path="toLoc" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">目标跟踪号</label></td>
                        <td class="width-15">
                            <form:input path="toId" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">包装单位</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control"
                                           fieldId="uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue="${banQinWmTaskRpEntity.uom}"
                                           displayFieldId="cdprDesc" displayFieldName="cdprDesc" displayFieldKeyName="cdprDesc" displayFieldValue="${banQinWmTaskRpEntity.cdprDesc}"
                                           selectButtonId="uomSelectId" deleteButtonId="uomDeleteId"
                                           fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                           searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                           queryParams="packCode" queryParamValues="packCode" disabled="disabled">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">补货数</label></td>
                        <td class="width-15">
                            <form:input path="qtyRpUom" htmlEscape="false" class="form-control required" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">移动数EA</label></td>
                        <td class="width-15">
                            <form:input path="qtyRpEa" htmlEscape="false" class="form-control" readonly="true"/>
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
                <h3 class="panel-title">批次属性</h3>
            </div>
            <div class="panel-body">
                <table id="lotAttTable" class="table"></table>
            </div>
        </div>
    </div>
</form:form>
</body>
</html>