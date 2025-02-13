<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>库存移动管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        $(function () {
            $('#isAllowMv').prop('checked', '${banQinWmInvMvEntity.isAllowMv}' === 'Y').val('${banQinWmInvMvEntity.isAllowMv}');
            loadLotAtt();
        });

        /**
         * 加载批次属性控件
         */
        function loadLotAtt() {
            var row = JSON.parse('${banQinWmInvMv}');
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

        /**
         * 移动数输入同步到EA
         */
        function qtyChange() {
            var num = !$('#cdprQuantity').val() ? 0 : $('#cdprQuantity').val();
            var toQtyUom = !$('#toQtyUom').val() ? 0 : $('#toQtyUom').val();
            var availableQty = $('#availableQty').val();
            var qtyHold = $('#qtyHold').val();
            if ($('#isAllowMv').val() === "Y") {
                if (toQtyUom * num > qtyHold) {
                    jp.bqError("库存移动待入数量不足，不能操作");
                    $('#toQtyUom').val('');
                    $('#toQty').val('');
                    return;
                }
                $('#toQty').val(toQtyUom * num);
            } else {
                if (toQtyUom * num > availableQty) {
                    jp.bqError("库存移动待入数量不足，不能操作");
                    $('#toQtyUom').val('');
                    $('#toQty').val('');
                    return;
                }
                $('#toQty').val(toQtyUom * num);
            }
        }

        function moveConfirm() {
            if ($('#fmLoc').val() === $('#toLoc').val() && $('#fmTraceId').val() === $('#toTraceId').val()) {
                jp.bqError("库位和跟踪号的位置没有变化,不能操作");
                return;
            }
            var isValidate = bq.headerSubmitCheck('#inputForm');
            if (isValidate.isSuccess) {
                jp.loading();
                var disabledObjs = bq.openDisabled('#inputForm');
                var params = $('#inputForm').bq_serialize();
                bq.closeDisabled(disabledObjs);
                jp.post("${ctx}/wms/inventory/banQinWmInvMv/moveConfirm", params, function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        jp.close(parent.layer.getFrameIndex(window.name));
                    } else {
                        jp.bqError(data.msg);
                    }
                });
            } else {
                jp.bqError(isValidate.msg);
            }
        }

        function afterSelectPack(row) {
            var num = row.cdprQuantity;
            var toQtyUom = $('#toQtyUom').val();
            var availQty = $('#availableQty').val();
            var qtyHold = $('#qtyHold').val();
            if ($('#isAllowMv').val() === "Y") {
                if (toQtyUom * num > qtyHold) {
                    jp.bqError("库存移动待入数量不足，不能操作");
                    $('#toQtyUom').val('');
                    $('#toQty').val('');
                    $('#cdprQuantity').val(num);
                    return;
                }
                $('#toQty').val(toQtyUom * num);
                $('#cdprQuantity').val(num);
            } else {
                if (toQtyUom * num > availQty) {
                    jp.bqError("库存移动待入数量不足，不能操作");
                    $('#toQtyUom').val('');
                    $('#toQty').val('');
                    $('#cdprQuantity').val(num);
                    return;
                }
                $('#toQty').val(toQtyUom * num);
                $('#cdprQuantity').val(num);
            }
        }
    </script>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="inventory:banQinWmInvMv:moveConfirm">
        <a class="btn btn-primary" id="moveConfirm" onclick="moveConfirm()">执行移动</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="banQinWmInvMvEntity" method="post" class="form">
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <form:hidden path="packCode"/>
    <form:hidden path="cdprQuantity"/>
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
                        <td class="width-10"><label class="pull-right">货主</label></td>
                        <td class="width-15">
                            <form:input path="ownerCode" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">商品编码</label></td>
                        <td class="width-15">
                            <form:input path="skuCode" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">商品名称</label></td>
                        <td class="width-15">
                            <form:input path="skuName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">批次号</label></td>
                        <td class="width-15">
                            <form:input path="lotNum" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">包装规格</label></td>
                        <td class="width-15">
                            <form:input path="cdpaFormat" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">包装单位</label></td>
                        <td class="width-15">
                            <input name="printUom" htmlEscape="false" class="form-control" value="EA" readonly>
                        </td>
                        <td class="width-10"><label class="pull-right">库存数</label></td>
                        <td class="width-15">
                            <form:input path="fmQty" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">库存可用数</label></td>
                        <td class="width-15">
                            <form:input path="availableQty" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">冻结数</label></td>
                        <td class="width-15">
                            <form:input path="qtyHold" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">冻结可移动</label></td>
                        <td class="width-15">
                            <input id="isAllowMv" name="isAllowMv" type="checkbox" class="myCheckbox" disabled/>
                        </td>
                        <td class="width-10"><label class="pull-right">分配数</label></td>
                        <td class="width-15">
                            <form:input path="qtyAlloc" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">拣货数</label></td>
                        <td class="width-15">
                            <form:input path="qtyPk" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
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
                <h3 class="panel-title">移动信息</h3>
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
                            <form:input path="fmTraceId" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">目标库位</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                           selectButtonId="toLocSelectId" deleteButtonId="toLocDeleteId"
                                           fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                           searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">目标跟踪号</label></td>
                        <td class="width-15">
                            <form:input path="toTraceId" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">包装单位</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}//wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                           fieldId="toUom" fieldName="toUom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                           displayFieldId="toUomDesc" displayFieldName="toUomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                           selectButtonId="toUomSelectId" deleteButtonId="toUomDeleteId"
                                           fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                           searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                           queryParams="packCode" queryParamValues="packCode" afterSelect="afterSelectPack">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">移动数</label></td>
                        <td class="width-15">
                            <form:input path="toQtyUom" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0);" oninput="qtyChange()"/>
                        </td>
                        <td class="width-10"><label class="pull-right">移动数EA</label></td>
                        <td class="width-15">
                            <form:input path="toQty" htmlEscape="false" class="form-control" readonly="true"/>
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