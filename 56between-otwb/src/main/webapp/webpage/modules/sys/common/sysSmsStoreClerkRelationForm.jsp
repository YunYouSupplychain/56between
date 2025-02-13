<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>门店操作人关联关系管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript">
        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        $(document).ready(function () {
            // 初始化
            init();
            // 监听事件
            listener();
        });

        /**
         * 监听失去焦点事件
         */
        function listener() {
            $('.required').on('blur', function () {
                if ($(this).hasClass('form-error') && $(this).val()) {
                    $(this).removeClass('form-error');
                }
            });
        }

        function init() {
            if (!$("#clerk").val()) {
                var $dataSet = ${fns:toJson(fns:getUserDataSet())};
                $('#dataSet').val($dataSet.code);
                $('#dataSetName').val($dataSet.name);
            } else {
                $('#clerkSelectButton').prop('disabled', true);
                $('#clerkDelButton').prop('disabled', true);
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);

            $('#smsStoreClerkRelationTable').bootstrapTable({
                //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                cache: false,
                //是否显示分页（*）
                pagination: true,
                //可供选择的每页的行数（*）
                pageList: [20],
                //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
                url: "${ctx}/sys/common/sms/storeClerkRelation/data",
                //查询参数,每次调用是会带上这个参数，可自定义
                queryParams: function (params) {
                    var searchParam = $("#inputForm").serializeJSON();
                    searchParam.clerk = !$('#clerk').val() ? "#" : $('#clerk').val();
                    searchParam.dataSet = $('#dataSet').val();
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                //分页方式client客户端分页，server服务端分页（*）
                sidePagination: "server",
                contextMenuTrigger: "right",//pc端 按右键弹出菜单
                contextMenuTriggerMobile: "press",//手机端 弹出菜单，click单击， press长按。
                contextMenu: '#context-menu',
                onClickRow: function (row, $el) {
                },
                columns: [{
                    checkbox: true
                }
                    , {
                        field: 'storeCode',
                        title: '门店编码',
                        sortable: true
                    }
                    , {
                        field: 'storeName',
                        title: '门店名称',
                        sortable: true
                    }
                ]
            });

            $('#smsStoreClerkRelationTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
                var length = $('#smsStoreClerkRelationTable').bootstrapTable('getSelections').length;
                $('#deleteDetail').attr('disabled', !length);
                $('#editDetail').attr('disabled', length !== 1);
            });
        }

        function getDetailIdSelections() {
            return $.map($("#smsStoreClerkRelationTable").bootstrapTable('getSelections'), function (row) {
                return row.id
            });
        }

        function addDetail() {
            $("#detail_id").val("");
            $("#detail_dataSet").val($("#dataSet").val());
            $("#detail_clerk").val($("#clerk").val());
            $("#detail_oldClerk").val($("#clerk").val());
            $("#detail_oldStoreCode").val("");
            $("#detail_storeCode").val("");
            $("#detail_storeName").val("");
            $("#detailEditModal").modal();
        }

        function editDetail() {
            var row = $("#smsStoreClerkRelationTable").bootstrapTable('getSelections')[0];
            $("#detail_id").val(row.id);
            $("#detail_dataSet").val(row.dataSet);
            $("#detail_clerk").val(row.clerk);
            $("#detail_oldClerk").val(row.oldClerk);
            $("#detail_oldStoreCode").val(row.oldStoreCode);
            $("#detail_storeCode").val(row.storeCode);
            $("#detail_storeName").val(row.storeName);
            $("#detailEditModal").modal();
        }

        function deleteDetail() {
            jp.confirm('确认要删除该栈板明细记录吗？', function () {
                jp.loading();
                jp.get("${ctx}/sys/common/sms/storeClerkRelation/deleteAll?ids=" + getDetailIdSelections(), function (data) {
                    if (data.success) {
                        $('#smsStoreClerkRelationTable').bootstrapTable('refresh');
                        jp.success(data.msg);
                    } else {
                        jp.error(data.msg);
                    }
                })
            })
        }

        function confirmSave() {
            var validate = bq.headerSubmitCheck('#detailSaveForm');
            if (validate.isSuccess) {
                var disableObjs = bq.openDisabled('#detailSaveForm');
                jp.loading();
                jp.post("${ctx}/sys/common/sms/storeClerkRelation/save", $('#detailSaveForm').serialize(), function (data) {
                    if (data.success) {
                        $('#clerkSelectButton').prop('disabled', true);
                        $('#clerkDelButton').prop('disabled', true);
                        $('#smsStoreClerkRelationTable').bootstrapTable('refresh');
                        jp.success(data.msg);
                        $("#detailEditModal").modal('hide');
                    } else {
                        jp.error(data.msg);
                    }
                })
                bq.closeDisabled(disableObjs);
            }
        }

        function afterSelectClerk(row) {
            $("#clerkNo").val(row.no);
            $("#clerkLoginName").val(row.loginName);
        }
    </script>
</head>
<div class="hide">
    <input id="storeType" type="hidden" value="CONSIGNEE"/>
</div>
<body>
<form:form id="inputForm" modelAttribute="sysSmsStoreClerkRelationEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td width="8%"><label class="pull-right"><font color="red">*</font>操作人</label></td>
                        <td width="12%">
                            <sys:popSelect url="${ctx}/sys/user/popDate" title="选择用户" cssClass="form-control required"
                                           fieldId="clerk" fieldName="clerk" fieldKeyName="id" fieldValue="${sysSmsStoreClerkRelationEntity.clerk}"
                                           displayFieldId="clerkName" displayFieldName="clerkName" displayFieldKeyName="name" displayFieldValue="${sysSmsStoreClerkRelationEntity.clerkName}"
                                           selectButtonId="clerkSelectButton" deleteButtonId="clerkDelButton"
                                           fieldLabels="工号|姓名|登录名" fieldKeys="no|name|loginName"
                                           searchLabels="工号|姓名|登录名" searchKeys="no|name|loginName"
                                           afterSelect="afterSelectClerk">
                            </sys:popSelect>
                        </td>
                        <td width="8%"><label class="pull-right">操作人工号</label></td>
                        <td width="12%">
                            <form:input path="clerkNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td width="8%"><label class="pull-right">操作人登录名</label></td>
                        <td width="12%">
                            <form:input path="clerkLoginName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td width="8%"><label class="pull-right"><font color="red">*</font>数据套</label></td>
                        <td width="12%">
                            <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                                      fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysSmsStoreClerkRelationEntity.dataSet}"
                                      displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysSmsStoreClerkRelationEntity.dataSetName}"
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
                <h3 class="panel-title">门店明细</h3>
            </div>
            <div class="panel-body">
                <div id="detailToolbar" style="width: 100%; padding: 5px 0;">
                    <a class="btn btn-primary" id="addDetail" onclick="addDetail()">新增</a>
                    <a class="btn btn-primary" id="editDetail" onclick="editDetail()">修改</a>
                    <a class="btn btn-danger" id="deleteDetail" onclick="deleteDetail()">删除</a>
                </div>
                <!-- 表格 -->
                <table id="smsStoreClerkRelationTable"></table>
            </div>
        </div>
    </div>
</form:form>
<!-- 修改弹出框 -->
<div class="modal fade" id="detailEditModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:360px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="orderSelect">门店录入</h4>
            </div>
            <div class="modal-body" id="pallet_skuInfo">
                <div id="collapse1" class="accordion-body collapse in">
                    <div class="accordion-inner">
                        <form:form id="detailSaveForm" modelAttribute="sysSmsStoreClerkRelationEntity" class="form well clearfix">
                            <div class="col-md-12">
                                <label class="label-item single-overflow pull-left" title="门店"><font color="red">*</font>门店</label>
                                <sys:grid title="选择门店" url="${ctx}/sys/common/sms/customer/grid" cssClass="form-control required"
                                          fieldId="detail_storeCode" fieldName="storeCode" fieldKeyName="code"
                                          displayFieldId="detail_storeName" displayFieldName="storeName" displayFieldKeyName="nameCn"
                                          fieldLabels="门店编码|门店名称" fieldKeys="code|nameCn"
                                          searchLabels="门店编码|门店名称" searchKeys="code|nameCn"
                                          queryParams="dataSet|customerType" queryParamValues="dataSet|storeType"/>
                                <input id="detail_id" name="id" type="hidden"/>
                                <input id="detail_dataSet" name="dataSet" type="hidden"/>
                                <input id="detail_clerk" name="clerk" type="hidden"/>
                                <input id="detail_oldStoreCode" name="oldStoreCode" type="hidden"/>
                                <input id="detail_oldClerk" name="oldClerk" type="hidden"/>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="confirmSave()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal -->
</div>
</body>
</html>