<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>修改</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <script>
        $(document).ready(function () {
            $('#trip').val('${entity.trip}');
            var params = "planNo=" + $('#planNo').val() + "&orgId=" + $('#orgId').val() + "&baseOrgId=" + $('#baseOrgId').val() + "&vehicleNo=" + encodeURIComponent($('#vehicleNo').val()) + "&trip=" + $('#trip').val();
            $.get("${ctx}/tms/order/tmDispatchPlan/getWareRoomConfigInfo?" + params, function (data) {
                for (var i = 0; i < data.length; i++) {
                    addRow('#detailList', i, detailTpl, data[i])
                }
                deliveryKeyup();
                skuKeyup();
            });
        });

        function save(index) {
            var validate = beforeCheck();
            if (validate.isSuccess) {
                jp.loading();
                var disabled = bq.openDisabled('#form');
                var params = $('#form').bq_serialize() + "&remarks=" + encodeURIComponent($('#remarks').val());
                bq.closeDisabled(disabled);
                jp.post("${ctx}/tms/order/tmDispatchPlan/saveConfig", params, function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        jp.close(index);
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            } else {
                jp.bqError(validate.msg);
            }
        }
        
        function beforeCheck() {
            var length = $('#detailList').children().length;
            var errorMsg = '', flag = true, driver = $('#driver').val(), escort = $('#escort').val();
            if (driver && escort && driver === escort) {
                flag = false;
                errorMsg = '驾驶员和押送员不能相同!';
                return {isSuccess: flag, msg: errorMsg};
            }
            for (var i = 0; i < length; i++) {
                var index = i + 1;
                var deliveryPoint = $('#detailList' + i + '_deliveryPoint').val();
                var sku = $('#detailList' + i + '_skuCode').val();
                var qty = $('#detailList' + i + '_qty').val();
                var pickUpPoint = $('#detailList' + i + '_pickUpPoint').val();
                $('#detailList' + i + '_driver').val($('#driver').val());
                $('#detailList' + i + '_escort').val($('#escort').val());
                if (!deliveryPoint && !sku && !qty && !pickUpPoint) {
                    continue;
                }
                if (!deliveryPoint) {
                    return setErrorMsg(index, '客户不能为空');
                }
                if (!sku) {
                    return setErrorMsg(index, '商品不能为空');
                }
                if (!qty) {
                    return setErrorMsg(index, '配载数量不能为空');
                }
                if (!pickUpPoint) {
                    return setErrorMsg(index, '提货点不能为空');
                }
                if (deliveryPoint === pickUpPoint) {
                    return setErrorMsg(index, '提货点和送货点不能相同');
                }
            }
            return {isSuccess: flag, msg: errorMsg};
        }
        
        function setErrorMsg(index, msg) {
            var errorMsg = '第' + index + '行' + msg;
            return {isSuccess: false, msg: errorMsg};
        }

        function addRow(list, idx, tpl, row) {
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            $(list + idx).find("select").each(function () {
                $(this).val($(this).attr("data-value"));
            });
            if (row.transportNo || row.dispatchNo) {
                $(list + idx + '_deliveryPointName').prop('readonly', 'readonly');
                $(list + idx + '_deliverySBtnId').prop('disabled', 'true');
                $(list + idx + '_deliveryDBtnId').prop('disabled', 'true');
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
            $('#detailList' + idx + "_planQty").val('');
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
            var skuType = $('#vehicleType').val();
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
                    var skuType = $('#vehicleType').val();
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
            if (!$('#detailList' + idx + "_ownerCode").val()) {
                $('#detailList' + idx + "_deliveryPoint").val(row.ownerCode);
                $('#detailList' + idx + "_deliveryPointName").val(row.ownerName);
            }
        }
        
        function afterSelectDriver(row) {
            var length = $('#detailList').children().length;
            for (var i = 0; i < length; i++) {
                $('#detailList' + i + '_driver').val(row.code);
            }
        }

        function afterSelectEscort(row) {
            var length = $('#detailList').children().length;
            for (var i = 0; i < length; i++) {
                $('#detailList' + i + '_escort').val(row.code);
            }
        }
        
        function deleteDetail(idx) {
            if ($('#detailList' + idx + '_transportNo').val() || $('#detailList' + idx + '_dispatchNo').val()) {
                jp.bqError("该条记录已审核，无法清空！");
                return;
            }
            $('#detailList' + idx + '_deliveryPoint').val('');
            $('#detailList' + idx + '_deliveryPointName').val('');
            $('#detailList' + idx + '_skuCode').val('');
            $('#detailList' + idx + '_skuName').val('');
            $('#detailList' + idx + '_qty').val('');
            $('#detailList' + idx + '_pickUpPoint').val('');
            $('#detailList' + idx + '_pickUpPointName').val('');
        }

    </script>
</head>
<body>
<div class="hidden">
    <input id="planNo" value="${entity.planNo}" type="hidden">
    <input id="orgId" value="${entity.orgId}" type="hidden">
    <input id="baseOrgId" value="${entity.baseOrgId}" type="hidden">
    <input id="outletType" value="OUTLET" type="hidden">
    <input id="vehicleType" value="${entity.vehicleType}" type="hidden">
</div>
<div class="modal-body">
    <fieldset>
        <legend>车次信息</legend>
        <div class="form-group row">
            <label class="col-sm-1" style="text-align: right;">车牌号：</label>
            <div class="col-sm-2">
                <sys:grid title="选择车辆" url="${ctx}/tms/basic/tmVehicle/grid"
                          fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                          displayFieldId="vehicleNo" displayFieldName="vehicleNo" displayFieldKeyName="carNo" displayFieldValue="${entity.vehicleNo}"
                          fieldLabels="车牌号|承运商|车辆类型|设备类型" fieldKeys="carNo|carrierName|vehicleTypeDesc|transportEquipmentTypeName"
                          searchLabels="车牌号" searchKeys="carNo"
                          queryParams="orgId" queryParamValues="baseOrgId"
                          cssClass="form-control" readonly="true" disabled="true"/>
            </div>
            <label class="col-sm-1" style="text-align: right;">车次：</label>
            <div class="col-sm-2">
                <select id="trip" name="trip" class="form-control m-b" disabled>
                    <option value=""></option>
                    <c:forEach items="${fns:getDictList('TMS_TRIP')}" var="dict">
                        <option value="${dict.value}">${dict.label}</option>
                    </c:forEach>
                </select>
            </div>
            <label class="col-sm-1" style="text-align: right;">驾驶员：</label>
            <div class="col-sm-2">
                <sys:grid title="选择驾驶员" url="${ctx}/tms/basic/tmDriver/grid"
                          fieldId="driver" fieldName="driver" fieldKeyName="code" fieldValue="${entity.driver}"
                          displayFieldId="driverName" displayFieldName="driverName" displayFieldKeyName="name" displayFieldValue="${entity.driverName}"
                          fieldLabels="司机编码|司机名称" fieldKeys="code|name"
                          searchLabels="司机编码|司机名称" searchKeys="code|name"
                          queryParams="orgId" queryParamValues="baseOrgId"
                          cssClass="form-control" afterSelect="afterSelectDriver"/>
            </div>
            <label class="col-sm-1" style="text-align: right;">押运员：</label>
            <div class="col-sm-2">
                <sys:grid title="选择押送员" url="${ctx}/tms/basic/tmDriver/grid"
                          fieldId="escort" fieldName="escort" fieldKeyName="code" fieldValue="${entity.escort}"
                          displayFieldId="escortName" displayFieldName="escortName" displayFieldKeyName="name" displayFieldValue="${entity.escortName}"
                          fieldLabels="押送员编码|押送员名称" fieldKeys="code|name"
                          searchLabels="押送员编码|押送员名称" searchKeys="code|name"
                          queryParams="orgId" queryParamValues="baseOrgId"
                          cssClass="form-control" afterSelect="afterSelectEscort"/>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-1" style="text-align: right;">备注：</label>
            <div class="col-sm-11">
                <input id="remarks" name="remarks" value="${entity.remarks}" class="form-control"/>
            </div>
        </div>
    </fieldset>
    <form id="form" class="form-horizontal">
        <table class="table table-bordered">
            <thead>
            <tr>
                <th class="hide"></th>
                <th>客户<font color="red">*</font></th>
                <th>商品<font color="red">*</font></th>
                <th><font color="red">*</font>配载数量</th>
                <th>仓室</th>
                <th><font color="red">*</font>提货点</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody id="detailList">
            </tbody>
        </table>
    </form>
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
        <input id="detailList{{idx}}_vehicleNo" name="detailList[{{idx}}].vehicleNo" type="hidden" value="{{row.vehicleNo}}"/>
        <input id="detailList{{idx}}_trip" name="detailList[{{idx}}].trip" type="hidden" value="{{row.trip}}"/>
        <input id="detailList{{idx}}_driver" name="detailList[{{idx}}].driver" type="hidden" value="{{row.driver}}"/>
        <input id="detailList{{idx}}_escort" name="detailList[{{idx}}].escort" type="hidden" value="{{row.escort}}"/>
        <input id="detailList{{idx}}_transportNo" name="detailList[{{idx}}].transportNo" type="hidden" value="{{row.transportNo}}"/>
        <input id="detailList{{idx}}_dispatchNo" name="detailList[{{idx}}].dispatchNo" type="hidden" value="{{row.dispatchNo}}"/>
        <input id="detailList{{idx}}_remarks" name="detailList[{{idx}}].remarks" type="hidden" value="{{row.remarks}}"/>
    </td>
    <td>
        <div class="input-group" style="width: 100%">
            <input id="detailList{{idx}}_deliveryPoint" name="detailList[{{idx}}].deliveryPoint" value="{{row.deliveryPoint}}" type="hidden"/>
            <input id="detailList{{idx}}_deliveryPointName" name="detailList[{{idx}}].deliveryPointName" value="{{row.deliveryPointName}}" class="form-control required deliveryPoint" data-idx="{{idx}}" style="border-radius: 4px;"/>
            <span class="input-group-btn">
                <button type="button" id="detailList{{idx}}_deliverySBtnId" onclick="$('#detailList{{idx}}_deliveryPointName').val('');deliverySelect({{idx}})" class="btn btn-primary"><i class="fa fa-search"></i></button>
                <button type="button" id="detailList{{idx}}_deliveryDBtnId" onclick="deliveryDelete({{idx}})" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
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
    <td width="200px">
        <select id="detailList{{idx}}_wareRoom" name="detailList[{{idx}}].wareRoom" data-value="{{row.wareRoom}}" class="form-control m-b" disabled>
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
    <td><button id="detailList{{idx}}_delete" type="button" class="btn btn-danger btn-sm" onclick="deleteDetail({{idx}})">清空</button></td>
</tr>//-->
</script>
<script type="text/javascript">
    var detailTpl = $("#detailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
</script>
</body>
</html>