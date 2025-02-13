<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>承运商类型关系管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            $table = table;
            $topIndex = index;
            jp.loading();
            save();
        }

        $(document).ready(function () {
            init();
        });

        /**
         * 保存
         */
        function save() {
            var validate = bq.headerSubmitCheck('#inputForm');
            if (validate.isSuccess) {
                jp.loading();
                jp.post("${ctx}/sys/common/wms/carrierTypeRelation/save", $('#inputForm').bq_serialize(), function (data) {
                    if (data.success) {
                        $table.bootstrapTable('refresh');
                        jp.success(data.msg);
                        jp.close($topIndex);
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            } else {
                jp.bqError(validate.msg);
            }
        }

        function init() {
            if (!$('#id').val()) {
                var $dataSet = ${fns:toJson(fns:getUserDataSet())};
                $('#dataSet').val($dataSet.code);
                $('#dataSetName').val($dataSet.name);
            } else {
                $("#carrierName").prop('readonly', true);
                $('#carrierNameSBtnId').prop('disabled', true);
                $('#carrierNameDBtnId').prop('disabled', true);
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);
        }
    </script>
</head>
<div class="hide">
    <input id="carrierType" value="CARRIER" type="hidden">
</div>
<body>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-md-12">
            <form:form id="inputForm" modelAttribute="sysWmsCarrierTypeRelationEntity" class="form-horizontal">
                <form:hidden path="id"/>
                <form:hidden path="recVer"/>
                <sys:message content="${message}"/>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><font color="red">*</font>承运商：</label>
                    <div class="col-sm-10">
                        <sys:grid url="${ctx}/sys/common/wms/customer/grid" title="选择承运商" cssClass="form-control required"
                                  fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo" fieldValue="${sysWmsCarrierTypeRelationEntity.carrierCode}"
                                  displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn"
                                  displayFieldValue="${sysWmsCarrierTypeRelationEntity.carrierName}"
                                  fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                  searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                  queryParams="ebcuType|dataSet" queryParamValues="carrierType|dataSet"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><font color="red">*</font>类型：</label>
                    <div class="col-sm-10">
                        <form:select path="type" class="form-control m-b required">
                            <form:option value="" label=""/>
                            <form:options items="${fns:getDictList('SYS_WM_CARRIER_TYPE_RELATION')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                        </form:select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label"><font color="red">*</font>数据套：</label>
                    <div class="col-sm-10">
                        <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                                  fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysWmsCarrierTypeRelationEntity.dataSet}"
                                  displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysWmsCarrierTypeRelationEntity.dataSetName}"
                                  fieldLabels="编码|名称" fieldKeys="code|name"
                                  searchLabels="编码|名称" searchKeys="code|name"/>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>
</body>
</html>