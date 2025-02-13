<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>拣货任务管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script>
        $(function () {
            $('#qtyPkUom').val($('#qtyUom').val());
            $('#qtyPkEa').val($('#qtyEa').val());
            if (!$('#pickOp').val()) {
                $('#pickOp').val('${fns:getUser().loginName}');
            }
            if (!$('#pickTime').val()) {
                $('#pickTime').val(jp.dateFormat(new Date(), 'yyyy-MM-dd hh:mm:ss'));
            }
            buttonControl();
        });

        function buttonControl() {
            var status = $('#status').val();
            // 如果是完全分配，那么不能执行取消拣货、发运完成、取消发货
            if (status === '40') {
                $('#picking').attr('disabled', false);
                $('#cancelAlloc').attr('disabled', false);
                $('#cancelPicking').attr('disabled', true);
                $('#shipment').attr('disabled', true);
                $('#cancelShipment').attr('disabled', true);
            }
            // 如果是完全拣货，那么不能执行取消分配\拣货\取消发货
            else if (status === '60') {
                $('#picking').attr('disabled', true);
                $('#cancelAlloc').attr('disabled', true);
                $('#cancelPicking').attr('disabled', false);
                $('#shipment').attr('disabled', false);
                $('#cancelShipment').attr('disabled', true);
            }
            // 如果是完全发运，那么不能执行拣货、取消分配、取消拣货、发运
            else if (status === '80') {
                $('#picking').attr('disabled', true);
                $('#cancelAlloc').attr('disabled', true);
                $('#cancelPicking').attr('disabled', true);
                $('#shipment').attr('disabled', true);
                $('#cancelShipment').attr('disabled', false);
            }
        }

        /**
         * 数量同步到EA
         */
        function pickChange() {
            // 单位换算数量
            var uomQty = !$('#uomQty').val() ? 0 : $('#uomQty').val();
            // 分配数
            var qtyUom = !$('#qtyPkUom').val() ? 0 : $('#qtyPkUom').val();
            $('#qtyPkEa').val(Math.floor(qtyUom * 100) / 100 * uomQty);
        }

        /**
         * 拣货确认
         */
        function picking() {
            commonMethod('picking');
        }

        /**
         * 发货确认
         */
        function shipment() {
            commonMethod('shipment');
        }

        /**
         * 取消分配
         */
        function cancelAlloc() {
            var isValidate = bq.headerSubmitCheck('#inputForm');
            if (isValidate.isSuccess) {
                var disabledObjs = bq.openDisabled('#inputForm');
                var rows = [];
                rows.push($('#inputForm').serializeJSON());
                bq.closeDisabled(disabledObjs);
                jp.loading();
                $.ajax({
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    type: 'POST',
                    dataType: "json",
                    data: JSON.stringify(rows),
                    url: "${ctx}/wms/outbound/banQinWmSoAlloc/cancelAlloc",
                    success: function (data) {
                        if (data.success) {
                            jp.success(data.msg);
                            jp.close(parent.layer.getFrameIndex(window.name));
                        } else {
                            jp.bqError(data.msg);
                        }
                    }
                });
            } else {
                jp.bqError(isValidate.msg);
            }
        }

        /**
         * 取消拣货
         */
        function cancelPick() {
            initPaTaskWindow();
        }

        function initPaTaskWindow() {
            $('#createTaskPaModal').modal();
            $('#isTaskPa').prop('checked', false).prop('disabled', false).val('N');
            $('#allocLoc').prop('checked', true).prop('disabled', true);
            $('#paRuleLoc').prop('checked', false).prop('disabled', true);
        }

        function createTaskPaConfirm() {
            var disabledObjs = bq.openDisabled('#inputForm');
            var allocRows = [];
            allocRows.push($('#inputForm').serializeJSON());
            var isAllocLoc = $('#allocLoc').prop('checked') ? 'Y' : 'N';
            var isTaskPa = $('#isTaskPa').val();
            for (var index = 0, length = allocRows.length; index < length; index++) {
                allocRows[index].isTaskPa = isTaskPa;
                allocRows[index].isAllocLoc = isAllocLoc;
            }
            bq.closeDisabled(disabledObjs);
            jp.loading();
            $.ajax({
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                type: 'POST',
                dataType: "json",
                data: JSON.stringify(allocRows),
                url: "${ctx}/wms/outbound/banQinWmSoAlloc/cancelPick",
                success: function (data) {
                    if (data.success) {
                        jp.close(parent.layer.getFrameIndex(window.name))
                        jp.success(data.msg);
                    } else {
                        jp.bqError(data.msg);
                    }
                }
            });
        }

        /**
         * 取消发货
         */
        function cancelShipment() {
            commonMethod('cancelShipment');
        }

        function commonMethod(method) {
            var isValidate = bq.headerSubmitCheck('#inputForm');
            if (isValidate.isSuccess) {
                var disabledObjs = bq.openDisabled('#inputForm');
                var rows = [];
                rows.push($('#inputForm').serializeJSON());
                bq.closeDisabled(disabledObjs);
                jp.loading();
                $.ajax({
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    type: 'POST',
                    dataType: "json",
                    data: JSON.stringify(rows),
                    url: "${ctx}/wms/outbound/banQinWmSoAlloc/" + method,
                    success: function (data) {
                        if (data.success) {
                            window.location = "${ctx}/wms/outbound/banQinWmSoAlloc/form?id=" + $('#id').val();
                            jp.success(data.msg);
                        } else {
                            jp.bqError(data.msg);
                        }
                    }
                });
            } else {
                jp.bqError(isValidate.msg);
            }
        }

        function isTaskPaChange(flag) {
            $('#isTaskPa').val(flag ? 'Y' : 'N');
            $('#allocLoc').prop('disabled', !flag);
            $('#paRuleLoc').prop('disabled', !flag);
            if (!flag) {
                $('#allocLoc').prop('checked', true);
                $('#paRuleLoc').prop('checked', false);
            }
        }
    </script>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="outbound:banQinWmSoAlloc:picking">
        <a class="btn btn-primary btn-sm" id="picking" onclick="picking()">拣货确认</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoAlloc:shipment">
        <a class="btn btn-primary btn-sm" id="shipment" onclick="shipment()">发货确认</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoAlloc:cancelAlloc">
        <a class="btn btn-primary btn-sm" id="cancelAlloc" onclick="cancelAlloc()">取消分配</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoAlloc:cancelPick">
        <a class="btn btn-primary btn-sm" id="cancelPick" onclick="cancelPick()">取消拣货</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoAlloc:cancelShipment">
        <a class="btn btn-primary btn-sm" id="cancelShipment" onclick="cancelShipment()">取消发货</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="banQinWmSoAllocEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="preallocId"/>
    <form:hidden path="qtyUom"/>
    <form:hidden path="recVer"/>
    <form:hidden path="qtyEa"/>
    <form:hidden path="uomQty"/>
    <form:hidden path="remarks"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-10"><label class="pull-right">拣货任务Id</label></td>
                <td class="width-15">
                    <form:input path="allocId" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">状态</label></td>
                <td class="width-15">
                    <form:select path="status" class="form-control" disabled="true">
                        <form:options items="${fns:getDictList('SYS_WM_ALLOC_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">出库单号</label></td>
                <td class="width-15">
                    <form:input path="soNo" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">出库单行号</label></td>
                <td class="width-15">
                    <form:input path="lineNo" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">波次单号</label></td>
                <td class="width-15">
                    <form:input path="waveNo" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">拣货单号</label></td>
                <td class="width-15">
                    <form:input path="pickNo" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">拣货人</label></td>
                <td class="width-15">
                    <form:input path="pickOp" htmlEscape="false" class="form-control" maxlength="32"/>
                </td>
                <td class="width-10"><label class="pull-right">拣货时间</label></td>
                <td class="width-15">
                    <input id="pickTime" name="pickTime" class="form-control" value="<fmt:formatDate value="${banQinWmSoAllocEntity.pickTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">货主</label></td>
                <td class="width-15">
                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                   fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="${banQinWmSoAllocEntity.ownerCode}" allowInput="false"
                                   displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${banQinWmSoAllocEntity.ownerName}"
                                   selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" disabled="disabled">
                    </sys:popSelect>
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
                <td class="width-10"><label class="pull-right">源库位</label></td>
                <td class="width-15">
                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                   displayFieldId="locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue="${banQinWmSoAllocEntity.locCode}"
                                   selectButtonId="locSelectId" deleteButtonId="locDeleteId"
                                   fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                   searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" disabled="disabled">
                    </sys:popSelect>
                </td>
                <td class="width-10"><label class="pull-right">源跟踪号</label></td>
                <td class="width-15">
                    <form:input path="traceId" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">包装规格</label></td>
                <td class="width-15">
                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control"
                                   fieldId="packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue="${banQinWmSoAllocEntity.packCode}" allowInput="false"
                                   displayFieldId="packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue="${banQinWmSoAllocEntity.packDesc}"
                                   selectButtonId="packSelectId" deleteButtonId="packDeleteId"
                                   fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                   searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" disabled="disabled">
                    </sys:popSelect>
                </td>
                <td class="width-10"><label class="pull-right"><font color="red">*</font>包装单位</label></td>
                <td class="width-15">
                    <sys:popSelect url="${ctx}//wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                   fieldId="uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue="${banQinWmSoAllocEntity.uom}" allowInput="true"
                                   displayFieldId="uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue="${banQinWmSoAllocEntity.uomDesc}"
                                   selectButtonId="uomSelectId" deleteButtonId="uomDeleteId"
                                   fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                   searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                   queryParams="packCode" queryParamValues="packCode" afterSelect="afterSelectAllocPack">
                    </sys:popSelect>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">收货人</label></td>
                <td class="width-15">
                    <form:input path="consigneeName" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">拣货数</label></td>
                <td class="width-15">
                    <form:input path="qtyPkUom" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0);" oninput="pickChange()"/>
                </td>
                <td class="width-10"><label class="pull-right">拣货数EA</label></td>
                <td class="width-15">
                    <form:input path="qtyPkEa" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right"><font color="red">*</font>目标库位</label></td>
                <td class="width-15">
                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                   fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                   displayFieldId="toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue="${banQinWmSoAllocEntity.toLoc}"
                                   selectButtonId="toLocSelectId" deleteButtonId="toLocDeleteId"
                                   fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                   searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                    </sys:popSelect>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right"><font color="red">*</font>目标跟踪号</label></td>
                <td class="width-15">
                    <form:input path="toId" htmlEscape="false" class="form-control" maxlength="32"/>
                </td>
                <td class="width-10"><label class="pull-right">打包箱号</label></td>
                <td class="width-15">
                    <form:input path="caseNo" htmlEscape="false" class="form-control" maxlength="64" readonly="true"/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</form:form>
<!-- 生成上架任务 -->
<div class="modal fade" id="createTaskPaModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 400px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">生成上架任务</h4>
            </div>
            <div class="modal-body">
                <table class="bq-table">
                    <tbody>
                    <tr>
                        <td class="width-20">
                            <input id="isTaskPa" type="checkBox" class="myCheckbox" onclick="isTaskPaChange(this.checked)">
                        </td>
                        <td class="width-80">
                            <label class="pull-left">是否生成上架任务</label>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-20">
                            <input type="radio" id="allocLoc" name="loc" class="myRadio">
                        </td>
                        <td class="width-80">
                            <label class="pull-left">推荐分配库位</label>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-20">
                            <input type="radio" id="paRuleLoc" name="loc" class="myRadio">
                        </td>
                        <td class="width-80">
                            <label class="pull-left">上架规则计算库位</label>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="createTaskPaConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>