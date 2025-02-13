<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>批量修改</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <script>
        function save(index) {
            var validate = beforeCheck();
            if (validate.isSuccess) {
                var disabled = bq.openDisabled('#form');
                jp.loading();
                jp.post("${ctx}/tms/order/tmDispatchPlan/saveConfig", $('#form').bq_serialize(), function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        jp.close(index);
                    } else {
                        bq.closeDisabled(disabled);
                        jp.bqError(data.msg);
                    }
                })
            } else {
                jp.bqError(validate.msg);
            }
        }

        function beforeCheck() {
            var length = $('#detailList').children().length;
            var errorMsg = '', flag = true;
            for (var i = 0; i < length; i++) {
                var index = i + 1;
                var deliveryPoint = $('#detailList' + i + '_deliveryPoint').val();
                var sku = $('#detailList' + i + '_skuCode').val();
                var qty = $('#detailList' + i + '_qty').val();
                var vehicleNo = $('#detailList' + i + '_vehicleNo').val();
                var wareRoom = $('#detailList' + i + '_wareRoom').val();
                var pickUpPoint = $('#detailList' + i + '_pickUpPoint').val();
                var trip = $('#detailList' + i + '_trip').val();
                if (!deliveryPoint && !sku && !qty && !pickUpPoint) continue;
                if (!deliveryPoint) return setErrorMsg(index, '客户不能为空');
                if (!sku) return setErrorMsg(index, '商品不能为空');
                if (!qty) return setErrorMsg(index, '配载数量不能为空');
                if (!vehicleNo) return setErrorMsg(index, '车牌号不能为空');
                if (!wareRoom) return setErrorMsg(index, '仓室不能为空');
                if (!pickUpPoint) return setErrorMsg(index, '提货点不能为空');
                if (!trip) return setErrorMsg(index, '仓室不能为空');
                if (deliveryPoint === pickUpPoint) {
                    return setErrorMsg(index, '提货点和送货点不能相同');
                }
            }
            if (length === 0) {
                errorMsg = '调度结果列表没有数据，无法确认！';
                flag = false;
            }
            return {isSuccess: flag, msg: errorMsg};
        }

        function setErrorMsg(index, msg) {
            return {isSuccess: false, msg: '第' + index + '行' + msg};
        }

        function addRow(list, idx, tpl, row) {
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            $(list + idx).find("select").each(function () {
                $(this).val($(this).attr("data-value"));
            });
            var lineNo = $(list + idx + '_lineNo').val();
            $(list + idx + '_lineNo').val(parseFloat(lineNo) + 1);
            if (row.transportNo || row.dispatchNo) {
                $(list + idx + '_deliveryPointName').prop('readonly', 'readonly');
                $(list + idx + '_deliveryPointSBtnId').prop('disabled', 'true');
                $(list + idx + '_deliveryPointDBtnId').prop('disabled', 'true');
                $(list + idx + '_skuName').prop('readonly', 'readonly');
                $(list + idx + '_skuSBtnId').prop('disabled', 'true');
                $(list + idx + '_skuDBtnId').prop('disabled', 'true');
                $(list + idx + '_qty').prop('readonly', 'readonly');
                $(list + idx + '_pickUpPointName').prop('readonly', 'readonly');
                $(list + idx + '_pickUpPointNameSBtnId').prop('disabled', 'true');
                $(list + idx + '_pickUpPointNameDBtnId').prop('disabled', 'true');
                $(list + idx + '_delete').prop('disabled', 'true');
            }
        }

        function queryDetail() {
            if (!$('#trip').val()) {
                jp.bqError("请选择车次!");
                return;
            }
            var index = jp.loading();
            $('#detailList').empty();
            var params = $('#queryForm').serializeJSON();
            jp.post("${ctx}/tms/order/tmDispatchPlan/getConfigInfo", params, function (data) {
                if (data.success && data.body.data) {
                    var rowData = data.body.data;
                    if (rowData.length === 0) {
                        jp.bqError("未找到符合条件的数据！");
                        return;
                    }
                    for (var i = 0; i < rowData.length; i++) {
                        addRow('#detailList', i, detailTpl, rowData[i]);
                    }
                    deliveryKeyup();
                    skuKeyup();
                }
                jp.close(index);
            });
        }

        function resetForm() {
            $("#queryForm table input").val("");
            $('#detailList').empty();
        }

        function deliverySelect(idx) {
            var orgId = $('#baseOrgId').val();
            var codeAndName = $('#detailList' + idx + "_deliveryPointName").val();
            top.layer.open({
                type: 2,
                area: ['1000px', '800px'],
                title: "选择客户",
                auto: true,
                name: 'friend',
                content: "${ctx}/tag/popSelect?url=" + encodeURIComponent("${ctx}/tms/order/tmDispatchPlan/getCustomer?orgId=" + orgId + "|codeAndName=" + codeAndName)
                    + "&fieldLabels=" + encodeURIComponent("客户编码|客户名称") + "&fieldKeys=" + encodeURIComponent("ownerCode|ownerName")
                    + "&searchLabels=" + encodeURIComponent("客户编码|客户名称") + "&searchKeys=" + encodeURIComponent("ownerCode|ownerName") + "&isMultiSelected=false",
                btn: ['确定', '关闭'],
                yes: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    var items = iframeWin.getSelections();
                    if (items == "") {
                        jp.warning("必须选择一条数据!");
                        return;
                    }
                    $('#detailList' + idx + "_deliveryPoint").val(row.ownerCode);
                    $('#detailList' + idx + "_deliveryPointName").val(row.ownerName);
                    $('#detailList' + idx + "_lineName").val(row.lineName);
                },
                cancel: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    var row = iframeWin.currentRow;
                    if (row) {
                        $('#detailList' + idx + "_deliveryPoint").val(row.ownerCode);
                        $('#detailList' + idx + "_deliveryPointName").val(row.ownerName);
                    }
                }
            });
        }

        function deliveryDelete(idx) {
            $('#detailList' + idx + "_deliveryPoint").val('');
            $('#detailList' + idx + "_deliveryPointName").val('');
            $('#detailList' + idx + "_skuCode").val('');
            $('#detailList' + idx + "_skuName").val('');
        }

        function deliveryKeyup() {
            $('.deliveryPoint').on('keyup', function (e) {
                if (e.keyCode === 13 && !$(this).prop('readonly')) {// 回车
                    var idx = $(this).data("idx");
                    var orgId = $('#baseOrgId').val();
                    var codeAndName = $(this).val();
                    jp.get("${ctx}/tms/order/tmDispatchPlan/getCustomer?orgId=" + encodeURIComponent(orgId) + "&codeAndName=" + encodeURIComponent(codeAndName), function (data) {
                        if (data.total > 1) {
                            deliverySelect(idx);
                        } else if (data.total === 1) {
                            var row = data.rows[0];
                            $('#detailList' + idx + "_deliveryPoint").val(row.ownerCode);
                            $('#detailList' + idx + "_deliveryPointName").val(row.ownerName);
                        } else if (data.total === 0) {
                            deliveryDelete(idx);
                        }
                    });
                }
            });
        }

        function skuSelect(idx) {
            var orgId = $('#baseOrgId').val();
            var ownerCode = $('#detailList' + idx + "_deliveryPoint").val();
            var skuType = $('#detailList' + idx + "_vehicleType").val();
            var codeAndName = $('#detailList' + idx + "_skuName").val();
            top.layer.open({
                type: 2,
                area: ['1000px', '800px'],
                title: "选择商品",
                auto: true,
                name: 'friend',
                content: "${ctx}/tag/popSelect?url=" + encodeURIComponent("${ctx}/tms/order/tmDispatchPlan/getSku?orgId=" + orgId + "|ownerCode=" + ownerCode + "|skuType=" + skuType + "|codeAndName=" + codeAndName)
                    + "&fieldLabels=" + encodeURIComponent("客户编码|客户名称|商品编码|商品名称|商品类型") + "&fieldKeys=" + encodeURIComponent("ownerCode|ownerName|skuCode|skuName|skuTypeDesc")
                    + "&searchLabels=" + encodeURIComponent("商品编码|商品名称") + "&searchKeys=" + encodeURIComponent("skuCode|skuName") + "&isMultiSelected=false",
                btn: ['确定', '关闭'],
                yes: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    var items = iframeWin.getSelections();
                    if (items == "") {
                        jp.warning("必须选择一条数据!");
                        return;
                    }
                    afterSelectSku(idx, items[0]);
                },
                cancel: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    var row = iframeWin.currentRow;
                    if (row) {
                        afterSelectSku(idx, row);
                    }
                }
            });
        }

        function skuDelete(idx) {
            $('#detailList' + idx + "_skuCode").val('');
            $('#detailList' + idx + "_skuName").val('');
            $('#detailList' + idx + "_planQty").val('');
        }

        function skuKeyup() {
            $('.sku').on('keyup', function (e) {
                if (e.keyCode === 13 && !$(this).prop('readonly')) {// 回车
                    var idx = $(this).data("idx");
                    var orgId = $('#baseOrgId').val();
                    var ownerCode = $('#detailList' + idx + "_deliveryPoint").val();
                    var skuType = $('#detailList' + idx + "_vehicleType").val();
                    var codeAndName = $(this).val();
                    jp.get("${ctx}/tms/order/tmDispatchPlan/getSku?orgId=" + encodeURIComponent(orgId) + "&ownerCode=" + encodeURIComponent(ownerCode) + "&skuType=" + encodeURIComponent(skuType) + "&codeAndName=" + encodeURIComponent(codeAndName), function (data) {
                        if (data.total > 1) {
                            skuSelect(idx);
                        } else if (data.total === 1) {
                            var row = data.rows[0];
                            afterSelectSku(idx, row);
                        } else if (data.total === 0) {
                            skuDelete(idx);
                        }
                    });
                }
            });
        }

        function afterSelectSku(idx, row) {
            $('#detailList' + idx + "_skuCode").val(row.skuCode);
            $('#detailList' + idx + "_skuName").val(row.skuName);
            $('#detailList' + idx + "_skuType").val(row.skuType);
            if (!$('#detailList' + idx + "_ownerCode").val()) {
                $('#detailList' + idx + "_deliveryPoint").val(row.ownerCode);
                $('#detailList' + idx + "_deliveryPointName").val(row.ownerName);
                $('#detailList' + idx + "_lineName").val(row.lineName);
            }
        }

        function deleteDetail(idx) {
            jp.confirm("请确认是否要删除该条配载信息？", function () {
                jp.loading();
                var configId = $('#detailList' + idx + '_id').val();
                if (!configId) {
                    $('#detailList' + idx + '_id').parent().parent().remove();
                    reCalcLineNo();
                    jp.bqError("操作成功");
                    return;
                }
                jp.loading();
                jp.get("${ctx}/tms/order/tmDispatchPlan/deleteConfig?configId=" + configId, function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        queryDetail();
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            })
        }

        function reCalcLineNo() {
            var index = 1;
            $("input[id$='_lineNo']").each(function () {
                $(this).val(index);
                index++;
            });
        }

    </script>
</head>
<body>
<div class="hidden">
    <input id="outletType" value="OUTLET" type="hidden">
</div>
<div class="modal-body">
    <fieldset>
        <legend>查询条件</legend>
        <form id="queryForm">
            <input id="planNo" name="planNo" value="${entity.planNo}" type="hidden">
            <input id="orgId" name="orgId" value="${entity.orgId}" type="hidden">
            <input id="baseOrgId" name="baseOrgId" value="${entity.baseOrgId}" type="hidden">
            <table class="bq-table" width="90%">
                <tr>
                    <td width="20%"><label class="pull-left"><font color="red">*</font>车次</label></td>
                    <td width="20%"><label class="pull-left">车牌号</label></td>
                    <td width="20%"><label class="pull-left">客户</label></td>
                    <td width="20%"><label class="pull-left">商品</label></td>
                    <td width="20%"><label class="pull-left">仓室</label></td>
                </tr>
                <tr>
                    <td width="20%">
                        <select id="trip" name="trip" class="form-control m-b required">
                            <c:forEach items="${fns:getDictList('TMS_TRIP')}" var="dict">
                                <option value="${dict.value}">${dict.label}</option>
                            </c:forEach>
                        </select>
                    </td>
                    <td width="20%">
                        <sys:grid title="车辆" url="${ctx}/tms/order/tmDispatchPlan/getVehicle"
                                  fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                  displayFieldId="vehicleNo" displayFieldName="vehicleNo" displayFieldKeyName="vehicleNo"
                                  fieldLabels="车牌号|车辆类型|设备类型" fieldKeys="vehicleNo|vehicleTypeDesc|equipmentTypeName"
                                  searchLabels="车牌号" searchKeys="vehicleNo"
                                  queryParams="orgId|planNo|baseOrgId" queryParamValues="orgId|planNo|baseOrgId"
                                  cssClass="form-control"/>
                    </td>
                    <td width="20%">
                        <sys:grid title="选择客户" url="${ctx}/tms/order/tmDispatchPlan/getCustomer"
                                  fieldId="deliveryPoint" fieldName="deliveryPoint" fieldKeyName="ownerCode"
                                  displayFieldId="deliveryPointName" displayFieldName="deliveryPointName" displayFieldKeyName="ownerName"
                                  fieldLabels="客户编码|客户名称" fieldKeys="ownerCode|ownerName"
                                  searchLabels="客户编码|客户名称" searchKeys="ownerCode|ownerName"
                                  queryParams="planNo|orgId" queryParamValues="planNo|orgId"
                                  cssClass="form-control"/>
                    </td>
                    <td width="20%">
                        <sys:grid title="选择商品" url="${ctx}/tms/order/tmDispatchPlan/getSku"
                                  fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode"
                                  displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName"
                                  fieldLabels="客户编码|客户名称|商品编码|商品名称|商品类型" fieldKeys="ownerCode|ownerName|skuCode|skuName|skuTypeDesc"
                                  searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                                  queryParams="planNo|orgId|ownerCode" queryParamValues="planNo|orgId|deliveryPoint"
                                  cssClass="form-control"/>
                    </td>
                    <td width="20%">
                        <select id="wareRoom" name="wareRoom" class="form-control required">
                            <option value=""></option>
                            <c:forEach items="${fns:getDictList('TMS_WARE_ROOM')}" var="dict">
                                <option value="${dict.value}">${dict.label}</option>
                            </c:forEach>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td width="20%"><label class="pull-left">提货点</label></td>
                    <td width="20%"><label class="pull-left">路线</label></td>
                    <td width="20%"><label class="pull-left">驾驶员</label></td>
                    <td width="20%"><label class="pull-left">押送员</label></td>
                    <td width="20%"></td>
                </tr>
                <tr>
                    <td width="20%">
                        <sys:grid title="选择提货点" url="${ctx}/tms/basic/tmTransportObj/grid"
                                  fieldId="pickUpPoint" fieldName="pickUpPoint" fieldKeyName="transportObjCode"
                                  displayFieldId="pickUpPointName" displayFieldName="pickUpPointName" displayFieldKeyName="transportObjName"
                                  fieldLabels="提货点编码|提货点名称" fieldKeys="transportObjCode|transportObjName"
                                  searchLabels="提货点编码|提货点名称" searchKeys="transportObjCode|transportObjName"
                                  queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                                  cssClass="form-control"/>
                    </td>
                    <td width="20%">
                        <sys:grid title="选择路线" url="${ctx}/tms/basic/tmBusinessRoute/grid"
                                  fieldId="lineCode" fieldName="lineCode" fieldKeyName="code"
                                  displayFieldId="lineName" displayFieldName="lineName" displayFieldKeyName="name"
                                  fieldLabels="路线编码|路线名称" fieldKeys="code|name"
                                  searchLabels="路线编码|路线名称" searchKeys="code|name"
                                  queryParams="orgId" queryParamValues="baseOrgId"
                                  cssClass="form-control"/>
                    </td>
                    <td width="20%">
                        <sys:grid title="选择驾驶员" url="${ctx}/tms/basic/tmDriver/grid"
                                  fieldId="driver" fieldName="driver" fieldKeyName="code"
                                  displayFieldId="driverName" displayFieldName="driverName" displayFieldKeyName="name"
                                  fieldLabels="司机编码|司机名称" fieldKeys="code|name"
                                  searchLabels="司机编码|司机名称" searchKeys="code|name"
                                  queryParams="orgId" queryParamValues="baseOrgId"
                                  cssClass="form-control" afterSelect="afterSelectDriver"/>
                    </td>
                    <td width="20%">
                        <sys:grid title="选择押送员" url="${ctx}/tms/basic/tmDriver/grid"
                                  fieldId="escort" fieldName="escort" fieldKeyName="code" fieldValue="${entity.escort}"
                                  displayFieldId="escortName" displayFieldName="escortName" displayFieldKeyName="name" displayFieldValue="${entity.escortName}"
                                  fieldLabels="押送员编码|押送员名称" fieldKeys="code|name"
                                  searchLabels="押送员编码|押送员名称" searchKeys="code|name"
                                  queryParams="orgId" queryParamValues="baseOrgId"
                                  cssClass="form-control required" afterSelect="afterSelectEscort"/>
                    </td>
                    <td width="20%">
                        <button type="button" class="btn btn-primary btn-sm" onclick="queryDetail()"> &nbsp;查询&nbsp;</button>
                        <button type="button" class="btn btn-danger btn-sm" onclick="resetForm()"> &nbsp;重置&nbsp;</button>
                    </td>
                </tr>
            </table>
        </form>
    </fieldset>
    <fieldset>
        <legend>调度结果</legend>
        <form id="form" class="form-horizontal">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th class="hide"></th>
                    <th>行号</th>
                    <th><font color="red">*</font>客户</th>
                    <th><font color="red">*</font>商品</th>
                    <th><font color="red">*</font>配载数量</th>
                    <th><font color="red">*</font>车牌号</th>
                    <th><font color="red">*</font>仓室</th>
                    <th><font color="red">*</font>提货点</th>
                    <th><font color="red">*</font>车次</th>
                    <th>路线</th>
                    <th>驾驶员</th>
                    <th>押送员</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="detailList"></tbody>
            </table>
        </form>
    </fieldset>
</div>
<script type="text/template" id="detailTpl">//<!--
<tr id="detailList{{idx}}">
    <td class="hide">
        <input id="detailList{{idx}}_id" name="detailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="detailList{{idx}}_delFlag" name="detailList[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="detailList{{idx}}_planNo" name="detailList[{{idx}}].planNo" type="hidden" value="{{row.planNo}}"/>
        <input id="detailList{{idx}}_orgId" name="detailList[{{idx}}].orgId" type="hidden" value="{{row.orgId}}"/>
        <input id="detailList{{idx}}_recVer" name="detailList[{{idx}}].recVer" type="hidden" value="{{row.recVer}}"/>
        <input id="detailList{{idx}}_baseOrgId" name="detailList[{{idx}}].baseOrgId" type="hidden" value="{{row.baseOrgId}}"/>
        <input id="detailList{{idx}}_transportNo" name="detailList[{{idx}}].transportNo" type="hidden" value="{{row.transportNo}}"/>
        <input id="detailList{{idx}}_dispatchNo" name="detailList[{{idx}}].dispatchNo" type="hidden" value="{{row.dispatchNo}}"/>
        <input id="detailList{{idx}}_vehicleType" type="hidden" value="{{row.vehicleType}}"/>
        <input id="detailList{{idx}}_skuType" type="hidden" value="{{row.skuType}}"/>
    </td>
    <td width="80px"><input id="detailList{{idx}}_lineNo" type="text" value="{{idx}}" class="form-control" readonly/></td>
    <td>
        <div class="input-group" style="width: 100%">
            <input id="detailList{{idx}}_deliveryPoint" name="detailList[{{idx}}].deliveryPoint" value="{{row.deliveryPoint}}" type="hidden"/>
            <input id="detailList{{idx}}_deliveryPointName" name="detailList[{{idx}}].deliveryPointName" value="{{row.deliveryPointName}}" class="form-control required deliveryPoint" data-idx="{{idx}}" style="border-radius: 4px;"/>
            <span class="input-group-btn">
                <button type="button" id="detailList{{idx}}_deliveryPointSBtnId" onclick="$('#detailList{{idx}}_deliveryPointName').val('');deliverySelect({{idx}})" class="btn btn-primary"><i class="fa fa-search"></i></button>
                <button type="button" id="detailList{{idx}}_deliveryPointDBtnId" onclick="deliveryDelete({{idx}})" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
            </span>
        </div>
    </td>
    <td>
        <div class="input-group" style="width: 100%">
            <input id="detailList{{idx}}_skuCode" name="detailList[{{idx}}].skuCode" value="{{row.skuCode}}" type="hidden"/>
            <input id="detailList{{idx}}_skuName" name="detailList[{{idx}}].skuName" value="{{row.skuName}}" class="form-control required sku" data-idx="{{idx}}" style="border-radius: 4px;"/>
            <span class="input-group-btn">
                <button type="button" id="detailList{{idx}}_skuSBtnId" onclick="$('#detailList{{idx}}_skuName').val('');skuSelect({{idx}})" class="btn btn-primary"><i class="fa fa-search"></i></button>
                <button type="button" id="detailList{{idx}}_skuDBtnId" onclick="skuDelete({{idx}})" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
            </span>
        </div>
    </td>
    <td><input id="detailList{{idx}}_qty" name="detailList[{{idx}}].qty" type="text" value="{{row.qty}}" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0)" maxlength="10"/></td>
    <td>
        <sys:grid title="车辆" url="${ctx}/tms/order/tmDispatchPlan/getVehicle"
              fieldId="" fieldName="" fieldKeyName="" fieldValue=""
              displayFieldId="detailList{{idx}}_vehicleNo" displayFieldName="detailList[{{idx}}].vehicleNo" displayFieldKeyName="carNo" displayFieldValue="{{row.vehicleNo}}"
              fieldLabels="车牌号|车辆类型|设备类型" fieldKeys="vehicleNo|vehicleTypeDesc|equipmentTypeName"
              searchLabels="车牌号" searchKeys="vehicleNo"
              queryParams="orgId|planNo|baseOrgId|vehicleType" queryParamValues="orgId|planNo|baseOrgId|detailList{{idx}}_skuType"
              cssClass="form-control" disabled="true" readonly="true"/>

    </td>
    <td width="150px">
        <select id="detailList{{idx}}_wareRoom" name="detailList[{{idx}}].wareRoom" data-value="{{row.wareRoom}}" class="form-control m-b required" disabled>
            <option value=""></option>
            <c:forEach items="${fns:getDictList('TMS_WARE_ROOM')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td>
        <sys:grid title="选择提货点" url="${ctx}/tms/basic/tmTransportObj/grid"
                  fieldId="detailList{{idx}}_pickUpPoint" fieldName="detailList[{{idx}}].pickUpPoint" fieldKeyName="transportObjCode" fieldValue="{{row.pickUpPoint}}"
                  displayFieldId="detailList{{idx}}_pickUpPointName" displayFieldName="detailList[{{idx}}].pickUpPointName" displayFieldKeyName="transportObjName" displayFieldValue="{{row.pickUpPointName}}"
                  fieldLabels="提货点编码|提货点名称" fieldKeys="transportObjCode|transportObjName"
                  searchLabels="提货点编码|提货点名称" searchKeys="transportObjCode|transportObjName"
                  queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                  cssClass="form-control required"/>
    </td>
    <td width="150px">
        <select id="detailList{{idx}}_trip" name="detailList[{{idx}}].trip" data-value="{{row.trip}}" class="form-control m-b required" disabled>
            <option value=""></option>
            <c:forEach items="${fns:getDictList('TMS_TRIP')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td><input id="detailList{{idx}}_lineName" name="detailList[{{idx}}].lineName" type="text" value="{{row.lineName}}" class="form-control" readonly/></td>
    <td>
        <sys:grid title="选择驾驶员" url="${ctx}/tms/basic/tmDriver/grid"
                  fieldId="detailList{{idx}}_driver" fieldName="detailList[{{idx}}].driver" fieldKeyName="code" fieldValue="{{row.driver}}"
                  displayFieldId="detailList{{idx}}_driverName" displayFieldName="detailList[{{idx}}].driverName" displayFieldKeyName="name" displayFieldValue="{{row.driverName}}"
                  fieldLabels="司机编码|司机名称" fieldKeys="code|name"
                  searchLabels="司机编码|司机名称" searchKeys="code|name"
                  queryParams="orgId" queryParamValues="baseOrgId"
                  cssClass="form-control required" afterSelect="afterSelectDriver" disabled="true" readonly="true"/>
    </td>
    <td>
        <sys:grid title="选择押送员" url="${ctx}/tms/basic/tmDriver/grid"
                  fieldId="detailList{{idx}}_escort" fieldName="detailList[{{idx}}].escort" fieldKeyName="code" fieldValue="{{row.escort}}"
                  displayFieldId="detailList{{idx}}_escortName" displayFieldName="detailList[{{idx}}].escortName" displayFieldKeyName="name" displayFieldValue="{{row.escortName}}"
                  fieldLabels="押送员编码|押送员名称" fieldKeys="code|name"
                  searchLabels="押送员编码|押送员名称" searchKeys="code|name"
                  queryParams="orgId" queryParamValues="baseOrgId"
                  cssClass="form-control required" afterSelect="afterSelectEscort" disabled="true" readonly="true"/>
    </td>
    <td><button id="detailList{{idx}}_delete" type="button" class="btn btn-danger btn-sm" onclick="deleteDetail({{idx}})">删除</button></td>
</tr>//-->
</script>
<script type="text/javascript">
    var detailTpl = $("#detailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
</script>
</body>
</html>