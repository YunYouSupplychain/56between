<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>包装管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        var packageDetailTpl;
        function doSubmit(table, index) {
            openDisable("#inputForm");
            if (validateForm.form()) {
                $table = table;
                $topIndex = index;
                jp.loading();
                $("#inputForm").submit();
                return true;
            }
            return false;
        }

        $(document).ready(function () {
            // 验证表单
            validateHeaderForm();
            // 初始化
            init();
        });

        /**
         * 验证表单
         */
        function validateHeaderForm() {
            validateForm = $("#inputForm").validate({
                submitHandler: function (form) {
                    jp.post("${ctx}/sys/common/package/save", $('#inputForm').bq_serialize(), function (data) {
                        if (data.success) {
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog
                        } else {
                            jp.bqError(data.msg);
                        }
                    })
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        }

        /**
         * 修改表单中disable状态
         */
        function openDisable(obj) {
            $(obj + " :disabled").each(function () {
                if (parseInt($(this).val()) !== -1) {
                    $(this).prop("disabled", false);
                }
            });
            return true;
        }
        
        /**
         * 初始化
         */ 
        function init() {
            packageDetailTpl = $("#packageDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "")
            if (!$('#id').val()) {
                var $dataSet = ${fns:toJson(fns:getUserDataSet())};
                $('#dataSet').val($dataSet.code);
                $('#dataSetName').val($dataSet.name);
                $.get("${ctx}/sys/common/packageRelation/initialList", function (data) {
                    for (var i = 0; i < data.length; i++) {
                        addRow('#packageDetailList', i, packageDetailTpl, data[i])
                    }
                });
            } else {
                $('#cdpaCode').prop('readOnly', true);
                var params = "cdprCdpaPmCode=" + $('#pmCode').val();
                $.get("${ctx}/sys/common/packageRelation/data?" + params, function (data) {
                    for (var i = 0; i < data.length; i++) {
                        addRow('#packageDetailList', i, packageDetailTpl, data[i])
                    }
                });
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);
        }

        /**
         * 添加明细行
         * @param list
         * @param idx
         * @param tpl
         * @param row
         */
        function addRow(list, idx, tpl, row) {
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            $(list + idx).find("select").each(function () {
                $(this).val($(this).attr("data-value"));
            });
            $(list + idx + '_cdprIsDefault').prop('checked', row.cdprIsDefault === 'Y').val(row.cdprIsDefault);
            $(list + idx + '_cdprIsPackBox').prop('checked', row.cdprIsPackBox === 'Y').val(row.cdprIsPackBox);
        }
        
        /**
         * 是否默认
         * @param idx
         */ 
        function isDefaultChange(idx) {
            $('#packageDetailList').find("input[type='checkbox']").each(function () {
                if ($(this).prop('name').indexOf('cdprIsDefault') !== -1) {
                    $(this).prop('checked', false).val('N');
                }
            });
            $('#packageDetailList' + idx + '_cdprIsDefault').prop('checked', true).val('Y');
        }

        /**
         * 是否需要复核
         * @param idx
         */
        function cdprIsPackBoxChange(idx, flag) {
            $('#packageDetailList' + idx + '_cdprIsPackBox').prop('checked', flag).val(flag ? 'Y' : 'N');
        }

    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="sysCommonPackage" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="pmCode"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">基础信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>包装代码</label></td>
                        <td class="width-12">
                            <form:input path="cdpaCode" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td class="width-8"><label class="pull-right">包装类型</label></td>
                        <td class="width-12">
                            <form:select path="cdpaType" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_PACKAGE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>包装规格</label></td>
                        <td class="width-12">
                            <form:input path="cdpaFormat" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>数据套</label></td>
                        <td class="width-12">
                            <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                                      fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysCommonPackage.dataSet}"
                                      displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysCommonPackage.dataSetName}"
                                      fieldLabels="编码|名称" fieldKeys="code|name"
                                      searchLabels="编码|名称" searchKeys="code|name"/>
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
                <h3 class="panel-title">单位信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th class="hide"></th>
                        <th>序号</th>
                        <th>单位代码</th>
                        <th>名称</th>
                        <th>数量</th>
                        <th>包装材料</th>
                        <th>长</th>
                        <th>宽</th>
                        <th>高</th>
                        <th>体积</th>
                        <th>重量</th>
                        <th>TI</th>
                        <th>HI</th>
                        <th>是否默认</th>
                        <th>是否需要复核</th>
                    </tr>
                    </thead>
                    <tbody id="packageDetailList">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<script type="text/template" id="packageDetailTpl">//<!--
    <tr id="packageDetailList{{idx}}">
        <td class="hide">
            <input id="packageDetailList{{idx}}_id" name="packageDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
            <input id="packageDetailList{{idx}}_delFlag" name="packageDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
            <input id="packageDetailList{{idx}}_recVer" name="packageDetailList[{{idx}}].recVer" type="hidden" value="{{row.recVer}}"/>
            <input id="packageDetailList{{idx}}_pmCode" name="packageDetailList[{{idx}}].pmCode" type="hidden" value="{{row.pmCode}}"/>
            <input id="packageDetailList{{idx}}_cdprCdpaPmCode" name="packageDetailList[{{idx}}].cdprCdpaPmCode" type="hidden" value="{{row.cdprCdpaPmCode}}"/>
            <input id="packageDetailList{{idx}}_cdprUnitLevel" name="packageDetailList[{{idx}}].cdprUnitLevel" type="hidden" value="{{row.cdprUnitLevel}}"/>
            <input id="packageDetailList{{idx}}_cdprIsMain" name="packageDetailList[{{idx}}].cdprIsMain" type="hidden" value="{{row.cdprIsMain}}"/>
            <input id="packageDetailList{{idx}}_cdprIsReview" name="packageDetailList[{{idx}}].cdprIsReview" type="hidden" value="{{row.cdprIsReview}}"/>
            <input id="packageDetailList{{idx}}_cdprIsLableIn" name="packageDetailList[{{idx}}].cdprIsLableIn" type="hidden" value="{{row.cdprIsLableIn}}"/>
            <input id="packageDetailList{{idx}}_cdprIsLableOut" name="packageDetailList[{{idx}}].cdprIsLableOut" type="hidden" value="{{row.cdprIsLableOut}}"/>
        </td>
        <td>
            <input id="packageDetailList{{idx}}_cdprSequencesNo" name="packageDetailList[{{idx}}].cdprSequencesNo" type="text" value="{{row.cdprSequencesNo}}" readonly class="form-control"/>
        </td>
        <td>
            <input id="packageDetailList{{idx}}_cdprUnit" name="packageDetailList[{{idx}}].cdprUnit" type="text" value="{{row.cdprUnit}}" readonly class="form-control"/>
        </td>
        <td>
            <input id="packageDetailList{{idx}}_cdprDesc" name="packageDetailList[{{idx}}].cdprDesc" type="text" value="{{row.cdprDesc}}" class="form-control required"/>
        </td>
        <td>
            <input id="packageDetailList{{idx}}_cdprQuantity" name="packageDetailList[{{idx}}].cdprQuantity" type="text" value="{{row.cdprQuantity}}" class="form-control required" onkeyup="value=value.replace(/[^\d]/g,'')"/>
        </td>
        <td width="100px">
            <select id="packageDetailList{{idx}}_cdprMaterial" name="packageDetailList[{{idx}}].cdprMaterial" data-value="{{row.cdprMaterial}}" class="form-control m-b">
                <option value=""></option>
                <c:forEach items="${fns:getDictList('SYS_PACKAGE_MATERIAL')}" var="dict">
                    <option value="${dict.value}">${dict.label}</option>
                </c:forEach>
            </select>
        </td>
        <td>
            <input id="packageDetailList{{idx}}_cdprLength" name="packageDetailList[{{idx}}].cdprLength" type="text" value="{{row.cdprLength}}" class="form-control" onkeyup="value=value.replace(/[^\d.]/g,'')"/>
        </td>
        <td>
            <input id="packageDetailList{{idx}}_cdprWidth" name="packageDetailList[{{idx}}].cdprWidth" type="text" value="{{row.cdprWidth}}" class="form-control" onkeyup="value=value.replace(/[^\d.]/g,'')"/>
        </td>
        <td>
            <input id="packageDetailList{{idx}}_cdprHeight" name="packageDetailList[{{idx}}].cdprHeight" type="text" value="{{row.cdprHeight}}" class="form-control" onkeyup="value=value.replace(/[^\d.]/g,'')"/>
        </td>
        <td>
            <input id="packageDetailList{{idx}}_cdprVolume" name="packageDetailList[{{idx}}].cdprVolume" type="text" value="{{row.cdprVolume}}" class="form-control" onkeyup="value=value.replace(/[^\d.]/g,'')"/>
        </td>
        <td>
            <input id="packageDetailList{{idx}}_cdprWeight" name="packageDetailList[{{idx}}].cdprWeight" type="text" value="{{row.cdprWeight}}" class="form-control" onkeyup="value=value.replace(/[^\d.]/g,'')"/>
        </td>
        <td>
            <input id="packageDetailList{{idx}}_cdprTi" name="packageDetailList[{{idx}}].cdprTi" type="text" value="{{row.cdprTi}}" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'')"/>
        </td>
        <td>
            <input id="packageDetailList{{idx}}_cdprHi" name="packageDetailList[{{idx}}].cdprHi" type="text" value="{{row.cdprHi}}" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'')"/>
        </td>
        <td width="80px">
            <input id="packageDetailList{{idx}}_cdprIsDefault" name="packageDetailList[{{idx}}].cdprIsDefault" type="checkbox" class="myCheckbox" onclick="isDefaultChange({{idx}})"/>
        </td>
        <td width="80px">
            <input id="packageDetailList{{idx}}_cdprIsPackBox" name="packageDetailList[{{idx}}].cdprIsPackBox" type="checkbox" class="myCheckbox" onclick="cdprIsPackBoxChange({{idx}}, this.checked)"/>
        </td>
    </tr>//-->
</script>
</body>
</html>