<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>库存周转规则管理</title>
	<meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript">
        var currentRow; // 明细当前行
        var isShowTab = false; // 是否显示右边tab页

        $(document).ready(function () {
            // 初始化
            init();
            // 初始化明细table
            initCdRuleRotationDetailTable();
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

        /**
         * 单头保存
         */ 
        function save() {
            var isValidate = jp.validateForm('#inputForm');
            if (!isValidate) {
                return false;
            } else {
                jp.loading();
                jp.post("${ctx}/sys/common/wms/ruleRotationHeader/save", $('#inputForm').serialize(), function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        window.location = "${ctx}/sys/common/wms/ruleRotationHeader/form?id=" + data.body.entity.id;
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            }
        }

        /**
         * 初始化
         */
        function init() {
            if (!$('#id').val()) {
                var $dataSet = ${fns:toJson(fns:getUserDataSet())};
                $('#dataSet').val($dataSet.code);
                $('#dataSetName').val($dataSet.name);
                $('#addDetail').attr('disabled', true);
            } else {
                $('#ruleCode').prop('readonly', true);
                $('#lotCode').prop('readonly', true);
                $('#lotName').prop('readonly', true);
                $('#lotNameSBtnId').prop('disabled', true);
                $('#lotNameDBtnId').prop('disabled', true);
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);
        }

        /**
         * 显示右边tab
         */
        function showTabRight() {
            $('#tab-right').show();
            $('#tab-left').addClass("div-left");
            isShowTab = true;
        }

        /**
         * 隐藏右边tab
         */
        function hideTabRight() {
            $('#tab-right').hide();
            $('#tab-left').removeClass("div-left");
            isShowTab = false;
        }

        /**
         * 初始化明细table
         */
        function initCdRuleRotationDetailTable() {
            $('#cdRuleRotationDetailTable').bootstrapTable({
                //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                cache: false,
                //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
                url: "${ctx}/sys/common/wms/ruleRotationDetail/data",
                //查询参数,每次调用是会带上这个参数，可自定义
                queryParams: function (params) {
                    var searchParam = {};
                    searchParam.headerId = !$('#id').val() == true ? "#" : $('#id').val();
                    searchParam.dataSet = $('#dataSet').val();
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                //分页方式client客户端分页，server服务端分页（*）
                sidePagination: "server",
                onClickRow: function(row, $el) {
                    clickDetail(row);
                },
                onDblClickRow: function (row, $el) {
                    dbClickDetail(row);
                },
                columns: [{
                    checkbox: true
                },
                {
                    field: 'lineNo',
                    title: '行号',
                    sortable: true
                },
                {
                    field: 'lotAttName',
                    title: '批次属性',
                    sortable: true
                },
                {
                    field: 'orderBy',
                    title: '排序方式',
                    sortable: true,
                    formatter:function(value, row , index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ORDER_BY'))}, value, "-");
                    }
                }]
            });
        }

        /**
         * 单击表格事件
         * @param row 当前行
         */
        function clickDetail(row) {
            currentRow = row;
            if (isShowTab) {
                // 表单赋值
                evaluate();
            }
        }

        /**
         * 双击表格事件
         * @param row 当前行
         */
        function dbClickDetail(row) {
            currentRow = row;
            if (!isShowTab) {
                // 显示右边tab
                showTabRight();
                // 表单赋值
                evaluate();
            } else {
                // 隐藏右边tab
                hideTabRight();
            }
            $("#cdRuleRotationDetailTable").bootstrapTable('resetView');
        }

        /**
         * 赋值
         */
        function evaluate() {
            $("input[id^=detail]").each(function() {
                var $Id = $(this).attr('id');
                var $Name = $(this).attr('name');
                $('#' + $Id).val(eval("currentRow." + $Name));
            });

            $("select[id^=detail]").each(function() {
                var $Id = $(this).attr('id');
                var $Name = $(this).attr('name');
                $('#' + $Id).val(eval("currentRow." + $Name));
            });
        }

        /**
         * 明细表单验证
         * @returns {boolean}
         */
        function detailSubmitCheck() {
            var flag = true;
            $('#tab-right').find(".required").each(function() {
                if (!$(this).val()) {
                    $(this).addClass('form-error');
                    $(this).attr("placeholder", "不能为空");
                    flag = false;
                }
            })
            return flag;
        }

        /**
         * 新增明细
         */
        function addDetail() {
            // 显示右边Tab
            showTabRight();
            // 清空表单
            $(':input', '#inputForm1').val('');
            $('#detail_recVer').val('0');
        }

        /**
         * 保存明细
         */
        function saveDetail() {
            if (!isShowTab) {
                jp.warning("当前无保存记录!");
                return;
            }
            if (!$('#id').val()) {
                jp.warning("请先保存订单头!");
                return;
            }
            // 表单验证
            if (detailSubmitCheck()) {
                // 保存前赋值
                beforeSave();
                jp.post("${ctx}/sys/common/wms/ruleRotationDetail/save", $('#inputForm1').serialize(), function (data) {
                    if (data.success) {
                        $('#cdRuleRotationDetailTable').bootstrapTable('refresh');
                        hideTabRight();
                        jp.success(data.msg);
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            }
        }

        /**
         * 保存前赋值
         */
        function beforeSave() {
            if (!$('#detail_id').val()) {
                $('#detail_dataSet').val($('#dataSet').val());
                $('#detail_ruleCode').val($('#ruleCode').val());
                $('#detail_headerId').val($('#id').val());
            }
        }

        /**
         * 获取表格勾选行Ids
         */
        function getIdSelections() {
            return $.map($('#cdRuleRotationDetailTable').bootstrapTable('getSelections'), function (row) {
                return row.id
            });
        }

        /**
         * 删除明细
         */
        function removeDetail() {
            var rowIds = getIdSelections();
            if (rowIds.length == 0) {
                jp.bqError("请选择一条记录!");
                return;
            }
            jp.confirm('确认要删除吗？删除将不能恢复？', function () {
                jp.get("${ctx}/sys/common/wms/ruleRotationDetail/deleteAll?ids=" + rowIds, function (data) {
                    if (data.success) {
                        $('#cdRuleRotationDetailTable').bootstrapTable('refresh');
                        hideTabRight();
                        jp.success(data.msg);
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            })
        }

    </script>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 20px;">
    <shiro:hasPermission name="sys:common:wms:ruleRotation:edit">
        <a class="btn btn-primary" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
</div>
<div class="wrapper wrapper-content-my">
    <div class="panel panel-default">
        <div class="panel-body">
            <form:form id="inputForm" modelAttribute="sysWmsRuleRotationHeader" method="post" class="form-horizontal">
                <form:hidden path="id"/>
                <form:hidden path="recVer"/>
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>规则编码</label></td>
                        <td class="width-12">
                            <form:input path="ruleCode" htmlEscape="false" class="form-control required" maxlength="32"/>
                        </td>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>规则名称</label></td>
                        <td class="width-12">
                            <form:input path="ruleName" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>周转类型</label></td>
                        <td class="width-12">
                            <form:select path="rotationType" class="form-control m-b required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_ROTATION_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>批次属性</label></td>
                        <td class="width-12">
                            <sys:grid title="选择批次属性" url="${ctx}/sys/common/wms/lotHeader/grid" cssClass="form-control required"
                                      fieldId="lotCode" fieldName="lotCode" fieldKeyName="lotCode" fieldValue="${sysWmsRuleRotationHeader.lotCode}"
                                      displayFieldId="lotName" displayFieldName="lotName" displayFieldKeyName="lotName" displayFieldValue="${sysWmsRuleRotationHeader.lotName}"
                                      fieldLabels="批次属性编码|批次属性名称" fieldKeys="lotCode|lotName"
                                      searchLabels="批次属性编码|批次属性名称" searchKeys="lotCode|lotName"
                                      queryParams="dataSet" queryParamValues="dataSet"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>数据套</label></td>
                        <td class="width-12">
                            <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                                      fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysWmsRuleRotationHeader.dataSet}"
                                      displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysWmsRuleRotationHeader.dataSetName}"
                                      fieldLabels="编码|名称" fieldKeys="code|name"
                                      searchLabels="编码|名称" searchKeys="code|name"/>
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
            <h3 class="panel-title">规则明细</h3>
        </div>
        <div class="panel-body">
            <div id="detailToolbar" style="width: 100%; padding: 0 0 10px 0;">
                <shiro:hasPermission name="sys:common:wms:ruleRotation:addDetail">
                    <a class="btn btn-primary" id="addDetail" onclick="addDetail()">新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:wms:ruleRotation:saveDetail">
                    <a class="btn btn-primary" id="saveDetail" onclick="saveDetail()">保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:wms:ruleRotation:removeDetail">
                    <a class="btn btn-primary" id="removeDetail" onclick="removeDetail()">删除</a>
                </shiro:hasPermission>
            </div>
            <div id="tab-left">
                <table id="cdRuleRotationDetailTable" class="table text-nowrap"></table>
            </div>
            <div id="tab-right" style="overflow: scroll; height: 200px; border: 1px solid #dddddd; display: none;">
                <form:form id="inputForm1" method="post" class="form-horizontal">
                    <input type="hidden" id="detail_id" name="id"/>
                    <input type="hidden" id="detail_ruleCode" name="ruleCode"/>
                    <input type="hidden" id="detail_dataSet" name="dataSet"/>
                    <input type="hidden" id="detail_headerId" name="headerId"/>
                    <input type="hidden" id="detail_recVer" name="recVer"/>
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td width="20%">
                                <label class="pull-left"><font color="red">*</font>行号</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left"><font color="red">*</font>批次属性标签</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left"><font color="red">*</font>排序方式</label>
                            </td>
                            <td width="20%">
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">
                                <input id="detail_lineNo" name="lineNo" htmlEscape="false" class="form-control required" maxlength="4"/>
                            </td>
                            <td width="20%">
                                <sys:grid title="选择批次属性标签" url="${ctx}/sys/common/wms/lotDetail/grid" cssClass="form-control required"
                                          fieldId="detail_lotAtt" fieldName="lotAtt" fieldKeyName="lotAtt"
                                          displayFieldId="detail_lotAttName" displayFieldName="lotAttName" displayFieldKeyName="title"
                                          fieldLabels="批次属性|批次标签" fieldKeys="lotAtt|title"
                                          searchLabels="批次属性|批次标签" searchKeys="lotAtt|title"
                                          queryParams="lotCode|dataSet" queryParamValues="lotCode|dataSet"/>
                            </td>
                            <td width="20%">
                                <select id="detail_orderBy" name="orderBy" class="form-control m-b required">
                                    <option value=""></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_ORDER_BY')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td width="20%"></td>
                        </tr>
                        </tbody>
                    </table>
                </form:form>
            </div>
        </div>
    </div>
</div>
</body>
</html>