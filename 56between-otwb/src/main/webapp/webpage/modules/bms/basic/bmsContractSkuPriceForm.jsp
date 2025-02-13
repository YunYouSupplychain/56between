<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>合同商品价格管理</title>
    <meta name="decorator" content="ani"/>
</head>
<body>
<div class="hide">
    <input id="orgId" value="${bmsContractEntity.orgId}"/>
</div>
<form id="inputForm" class="form">
    <table class="table well">
        <tbody>
        <tr>
            <td class="width-10"><label class="pull-right">系统合同编号</label></td>
            <td class="width-15">
                <input id="sysContractNo" value="${bmsContractEntity.sysContractNo}" class="form-control " readonly/>
            </td>
            <td class="width-10"><label class="pull-right">结算对象编码</label></td>
            <td class="width-15">
                <input id="settleObjectCode" value="${bmsContractEntity.settleObjectCode}" class="form-control " readonly/>
            </td>
            <td class="width-10"><label class="pull-right">结算对象名称</label></td>
            <td class="width-15">
                <input id="settleObjectName" value="${bmsContractEntity.settleObjectName}" class="form-control " readonly/>
            </td>
            <td class="width-10"></td>
            <td class="width-15"></td>
        </tr>
        </tbody>
    </table>
    <div class="tab-content">
        <shiro:hasPermission name="bms:bmsContractSkuPrice:add">
            <a class="btn btn-primary" onclick="addSkuPrice();">新增</a>
        </shiro:hasPermission>
        <table class="table table-bordered text-nowrap detail">
            <thead>
            <tr>
                <th class="hide"></th>
                <th>品类</th>
                <th>商品编码</th>
                <th>商品名称</th>
                <th class="asterisk">未税单价</th>
                <th class="asterisk">含税单价</th>
                <th>单位</th>
                <th width="20">&nbsp;</th>
            </tr>
            </thead>
            <tbody id="skuPriceList"></tbody>
        </table>
    </div>
</form>
<script type="text/template" id="skuPriceTpl">//<!--
<tr id="skuPriceList{{idx}}">
    <td class="hide">
        <input id="skuPriceList{{idx}}_id" name="skuPriceList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="skuPriceList{{idx}}_recVer" name="skuPriceList[{{idx}}].recVer" type="hidden" value="{{row.recVer}}"/>
        <input id="skuPriceList{{idx}}_orgId" name="skuPriceList[{{idx}}].orgId" type="hidden" value="{{row.orgId}}"/>
        <input id="skuPriceList{{idx}}_sysContractNo" name="skuPriceList[{{idx}}].sysContractNo" type="hidden" value="{{row.sysContractNo}}"/>
    </td>
    <td>
        <sys:grid title="选择商品分类" url="${ctx}/bms/basic/skuClassification/grid" cssClass="form-control"
                  fieldId="skuPriceList{{idx}}_skuClass" fieldName="skuPriceList[{{idx}}].skuClass"
                  fieldKeyName="code" fieldValue="{{row.skuClass}}"
                  displayFieldId="skuPriceList{{idx}}_skuClassName" displayFieldName="skuPriceList[{{idx}}].skuClassName"
                  displayFieldKeyName="name" displayFieldValue="{{row.skuClassName}}"
                  fieldLabels="编码|名称" fieldKeys="code|name"
                  searchLabels="编码|名称" searchKeys="code|name"
                  queryParams="orgId" queryParamValues="orgId"/>
    </td>
    <td>
        <sys:popSelect title="选择商品" url="${ctx}/bms/basic/settlementSku/grid" cssClass="form-control"
                       fieldId="" fieldKeyName="" fieldName="" fieldValue=""
                       displayFieldId="skuPriceList{{idx}}_skuCode" displayFieldKeyName="skuCode"
                       displayFieldName="skuPriceList[{{idx}}].skuCode" displayFieldValue="{{row.skuCode}}"
                       selectButtonId="skuPriceList{{idx}}_skuSBtnId" deleteButtonId="skuPriceList{{idx}}_skuDBtnId"
                       fieldLabels="商品编码|商品名称|货主编码|货主名称" fieldKeys="skuCode|skuName|ownerCode|ownerName"
                       searchLabels="商品编码|商品名称|货主编码|货主名称" searchKeys="skuCode|skuName|ownerCode|ownerName"
                       concatId="skuPriceList{{idx}}_skuName,skuPriceList{{idx}}_skuClass,skuPriceList{{idx}}_skuClassName"
                       concatName="skuName,skuClass,skuClassName"/>
    </td>
    <td>
        <input id="skuPriceList{{idx}}_skuName" name="skuPriceList[{{idx}}].skuName" type="text" value="{{row.skuName}}" class="form-control" readonly/>
    </td>
    <td>
        <input id="skuPriceList{{idx}}_price" name="skuPriceList[{{idx}}].price" type="text" value="{{row.price}}" class="form-control required" maxlength="18" onkeyup="bq.numberValidator(this, 8, 0)"/>
    </td>
    <td>
        <input id="skuPriceList{{idx}}_taxPrice" name="skuPriceList[{{idx}}].taxPrice" type="text" value="{{row.taxPrice}}" class="form-control required" maxlength="18" onkeyup="bq.numberValidator(this, 8, 0)"/>
    </td>
    <td>
        <select id="skuPriceList{{idx}}_unit" name="skuPriceList[{{idx}}].unit" data-value="{{row.unit}}" class="form-control">
            <option value="" label=""/>
            <c:forEach items="${fns:getDictList('BMS_CONTRACT_UNIT')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td class="text-center" width="10">
        <a class="btn btn-danger" onclick="delRow('#skuPriceList{{idx}}')">删除</a>
    </td>
</tr>//-->
</script>
<script type="text/javascript">
    var skuPriceRowIdx = 0, skuPriceTpl = $("#skuPriceTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    var validateForm;
    var $topIndex;//弹出窗口的 index
    function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
        if (validateForm.form()) {
            $topIndex = index;
            jp.loading();
            $("#inputForm").submit();
            return true;
        }
        return false;
    }

    $(document).ready(function () {
        validateForm = $("#inputForm").validate({
            submitHandler: function (form) {
                if (!$("#skuPriceList").has("tr").length > 0) {
                    jp.warning("请维护商品价格");
                    return;
                }
                jp.post("${ctx}/bms/basic/bmsContractSkuPrice/batchSave", $('#inputForm').serialize(), function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        jp.close($topIndex);//关闭dialog

                    } else {
                        jp.error(data.msg);
                    }
                })
            },
            errorContainer: "#messageBox",
            errorPlace: function (error, ele) {
                $("#messageBox").text("输入有误，请先更正。");
                if (ele.is(":checkbox") || ele.is(":radio") || ele.parent().is(".input-append")) {
                    error.appendTo(ele.parent().parent());
                } else {
                    error.insertAfter(ele);
                }
            }
        });

        if (!$("#orgId").val()) {
            $("#orgId").val(jp.getCurrentOrg().orgId);
        }
        var skuPriceList = ${fns:toJson(bmsContractEntity.skuPriceList)};
        if (skuPriceList) {
            for (var i = 0; i < skuPriceList.length; i++) {
                addSkuPrice(skuPriceList[i])
            }
        }
    });

    function addRow(list, idx, tpl, row) {
        $(list).append(Mustache.render(tpl, {
            idx: idx, delBtn: true, row: row
        }));
        $(list + idx).find("select").each(function () {
            $(this).val($(this).attr("data-value"));
        });
        $(list + idx).find("input[type='checkbox'], input[type='radio']").each(function () {
            if ($(this).attr("data-value")) {
                var ss = $(this).attr("data-value").split(',');
                for (var i = 0; i < ss.length; i++) {
                    if ($(this).val() == ss[i]) {
                        $(this).attr("checked", "checked");
                    }
                }
            }
        });
        $(list + idx).find(".form_datetime").each(function () {
            $(this).datetimepicker({
                format: "YYYY-MM-DD HH:mm:ss"
            });
        });
    }

    function delRow(prefix) {
        var $id = $(prefix + "_id");
        if ($id.val()) {
            jp.confirm("确认要删除该记录吗？", function () {
                jp.get("${ctx}/bms/basic/bmsContractSkuPrice/delete?id=" + $id.val(), function (data) {
                    if (data.success) {
                        $(prefix).remove();
                    } else {
                        jp.bqError(data.msg);
                    }
                });
            })
        } else {
            $(prefix).remove();
        }
    }

    function scrollBottom(list, idx) {
        $('html, body').animate({scrollTop: $(list + idx).offset().top}, 100);
    }

    function addSkuPrice(row) {
        if (!row) {
            row = {sysContractNo: $("#sysContractNo").val(), orgId: $("#orgId").val(), recVer: 0};
        }
        addRow('#skuPriceList', skuPriceRowIdx, skuPriceTpl, row);
        scrollBottom('#skuPriceList', skuPriceRowIdx);
        skuPriceRowIdx = skuPriceRowIdx + 1;
    }
</script>
</body>
</html>