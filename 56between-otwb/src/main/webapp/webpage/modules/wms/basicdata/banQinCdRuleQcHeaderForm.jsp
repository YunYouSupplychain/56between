<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>质检规则管理</title>
	<meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
	<script type="text/javascript">
        var currentRow; // 明细当前行
        var isShowTab1 = false; // 是否显示右边tab页
        var isShowTab2 = false; // 是否显示右边tab页

        $(document).ready(function () {
            // 初始化
            init();
            // 初始化级差table
            initCdRuleQcClassTable();
            // 初始化质检处理意见table
            initCdRuleQcDetailTable();
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
         * 保存表头
         */ 
        function save() {
            var isValidate = jp.validateForm('#inputForm');
            if (!isValidate) {
                return false;
            } else {
                jp.loading();
                jp.post("${ctx}/wms/basicdata/banQinCdRuleQcHeader/save", $('#inputForm').serialize(), function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        window.location = "${ctx}/wms/basicdata/banQinCdRuleQcHeader/form?id=" + data.body.entity.id;
                        // jp.close(parent.layer.getFrameIndex(window.name));//关闭dialog
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            }
        }
        
        /**
         * 显示右边tab
         * @param index tab页索引
         */
        function showTabRight(index) {
            $('#tab' + index +'-right').show();
            $('#tab' + index +'-left').addClass("div-left");
            if (index == 1) {
                isShowTab1 = true;
            } else if (index == 2) {
                isShowTab2 = true;
            }
        }

        /**
         * 隐藏右边tab
         * @param index tab页索引
         */
        function hideTabRight(index) {
            $('#tab' + index + '-right').hide();
            $('#tab' + index + '-left').removeClass("div-left");
            if (index == 1) {
                isShowTab1 = false;
            } else if (index == 2) {
                isShowTab2 = false;
            }
        }

        /**
         * 初始化
         */
        function init() {
            if (!$('#id').val()) {
                $('#orgId').val(jp.getCurrentOrg().orgId);
                $('#addClass').attr('disabled', true);
                $('#addDetail').attr('disabled', true);
            } else {
                $('#ruleCode').prop('readonly', true);
            }
        }

        /**
         * 初始化级差table
         */
        function initCdRuleQcClassTable() {
            $('#cdRuleQcClassTable').bootstrapTable({
                cache: false,// 是否使用缓存
                sidePagination: "server",// client客户端分页，server服务端分页
                url: "${ctx}/wms/basicdata/banQinCdRuleQcClass/data",
                queryParams: function (params) {
                    var searchParam = {};
                    searchParam.headerId = !$('#id').val() == true ? "#" : $('#id').val();
                    searchParam.orgId = $('#orgId').val();
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                onClickRow: function(row, $el) {
                    clickDetail(row, 'class', 1);
                },
                onDblClickRow: function (row, $el) {
                    dbClickDetail(row, 'class', 1);
                },
                columns: [{
                    checkbox: true
                },{
                    field: 'lineNo',
                    title: '行号',
                    sortable: true
                },{
                    field: 'fmClass',
                    title: '级差区间从(大于)',
                    sortable: true
                },{
                    field: 'toClass',
                    title: '级差区间到(小于等于)',
                    sortable: true
                },{
                    field: 'qtyType',
                    title: '数量类型',
                    sortable: true,
                    formatter:function(value, row , index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QTY_TYPE'))}, value, "-");
                    }
                },{
                    field: 'qty',
                    title: '数量比例',
                    sortable: true
                }]
            });
        }
        
        /**
         * 初始化质检处理意见
         */
        function initCdRuleQcDetailTable() {
            $('#cdRuleQcDetailTable').bootstrapTable({
                cache: false,// 是否使用缓存
                sidePagination: "server",// client客户端分页，server服务端分页
                url: "${ctx}/wms/basicdata/banQinCdRuleQcDetail/data",
                queryParams: function (params) {
                    var searchParam = {};
                    searchParam.headerId = !$('#id').val() == true ? "#" : $('#id').val();
                    searchParam.orgId = $('#orgId').val();
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                onClickRow: function(row, $el) {
                    clickDetail(row, 'detail', 2);
                },
                onDblClickRow: function (row, $el) {
                    dbClickDetail(row, 'detail', 2);
                },
                columns: [{
                    checkbox: true
                },{
                    field: 'lineNo',
                    title: '行号',
                    sortable: true
                },{
                    field: 'fmRate',
                    title: '合格率区间从(大于)',
                    sortable: true
                },{
                    field: 'toRate',
                    title: '合格率区间到(小于等于)',
                    sortable: true
                },{
                    field: 'qcSuggest',
                    title: '质检处理建议',
                    sortable: true,
                    formatter:function(value, row , index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_SUGGEST'))}, value, "-");
                    }
                }]
            });
        }

        /**
         * 单击表格事件
         * @param row 当前行
         * @param prefix 前缀
         */
        function clickDetail(row, prefix, index) {
            currentRow = row;
            if ((index === 1 && isShowTab1) || (index === 2 && isShowTab2)) {
                // 表单赋值
                evaluate(prefix);
            }
        }

        /**
         * 双击表格事件
         * @param row 当前行
         */
        function dbClickDetail(row, prefix, index) {
            currentRow = row;
            if ((index === 1 && !isShowTab1) || (index === 2 && !isShowTab2)) {
                // 显示右边tab
                showTabRight(index);
                // 表单赋值
                evaluate(prefix);
            } else {
                // 隐藏右边tab
                hideTabRight(index);
            }
            $("#cdRuleQcClassTable").bootstrapTable('resetView');
            $("#cdRuleQcDetailTable").bootstrapTable('resetView');
        }

        /**
         * 赋值
         * @param prefix 前缀
         */
        function evaluate(prefix) {
            $("input[id^=" + prefix + "]").each(function() {
                var $Id = $(this).attr('id');
                var $Name = $(this).attr('name');
                $('#' + $Id).val(eval("currentRow." + $Name));
            });

            $("select[id^=" + prefix + "]").each(function() {
                var $Id = $(this).attr('id');
                var $Name = $(this).attr('name');
                $('#' + $Id).val(eval("currentRow." + $Name));
            });
        }

        /**
         * 明细表单验证
         * @param index tab页索引
         * @returns {boolean}
         */
        function detailSubmitCheck(index) {
            var flag = true;
            $('#tab' + index + '-right').find(".required").each(function() {
                if (!$(this).val()) {
                    $(this).addClass('form-error');
                    $(this).attr("placeholder", "不能为空");
                    flag = false;
                }
            })
            return flag;
        }

        /**
         * 根据当前控件值判断是否移除样式
         */
        function removeStyle(obj) {
            if ($(obj).val()) {
                $(obj).removeClass('form-error');
            }
        }

        /**
         * 新增明细
         * @param index tab页索引
         */
        function addDetail(index) {
            // 显示右边Tab
            showTabRight(index);
            // 清空表单
            $(':input', '#inputForm' + index).val('');
            if (1 == index) {
                $('#class_recVer').val('0');
            } else {
                $('#detail_recVer').val('0');
            }
        }

        /**
         * 保存明细
         * @param index tab页索引
         */
        function saveDetail(index) {
            if ((index === 1 && !isShowTab1) || (index === 2 && !isShowTab2)) {
                jp.warning("当前无保存记录!");
                return;
            }
            if (!$('#id').val()) {
                jp.warning("请先保存订单头!");
                return;
            }
            // 表单验证
            if (detailSubmitCheck(index)) {
                // 保存前赋值
                beforeSave(index);
                var data = $('#inputForm' + index).serialize();
                if (index === 1) {
                    saveQcClass(index, data);
                } else if (index === 2) {
                    saveQcDetail(index, data);
                }
            }
        }
        
        /**
         * 保存前赋值
         */ 
        function beforeSave(index) {
            var orgId = $('#orgId').val();
            var ruleCode = $('#ruleCode').val();
            var headerId = $('#id').val();
            if (index === 1 && !$('#class_id').val()) {
                $('#class_orgId').val(orgId);
                $('#class_ruleCode').val(ruleCode);
                $('#class_headerId').val(headerId);
            } else if (index == 2 && !$('#detail_id').val()) {
                $('#detail_orgId').val(orgId);
                $('#detail_ruleCode').val(ruleCode);
                $('#detail_headerId').val(headerId);
            }
        }

        /**
         * 保存级差明细
         * @param index
         * @param data
         */
        function saveQcClass(index, data) {
            jp.post("${ctx}/wms/basicdata/banQinCdRuleQcClass/save", data, function (data) {
                if (data.success) {
                    $('#cdRuleQcClassTable').bootstrapTable('refresh');
                    hideTabRight(index);
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            })
        }

        /**
         * 保存质检处理意见
         * @param index
         * @param data
         */
        function saveQcDetail(index, data) {
            jp.post("${ctx}/wms/basicdata/banQinCdRuleQcDetail/save", data, function (data) {
                if (data.success) {
                    $('#cdRuleQcDetailTable').bootstrapTable('refresh');
                    hideTabRight(index);
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            })
        }

        /**
         * 获取表格勾选行Ids
         * @index
         */
        function getIdSelections(index) {
            var $table = index == 1 ? "#cdRuleQcClassTable" : "#cdRuleQcDetailTable";
            return $.map($($table).bootstrapTable('getSelections'), function (row) {
                return row.id
            });
        }
        
        /**
         * 删除明细
         * @param index
         */
        function removeDetail(index) {
            var rowIds = getIdSelections(index);
            if (rowIds.length == 0) {
                jp.bqError("请选择一条记录!");
                return;
            }
            jp.confirm('确认要删除吗？删除将不能恢复？', function () {
                if (index == 1) {
                    removeQcClass(rowIds, index);
                } else if (index == 2) {
                    removeQcDetail(rowIds, index);
                }
            })
        }

        /**
         * 删除级差明细
         * @param rowIds 明细行Ids
         */
        function removeQcClass(rowIds, index) {
            jp.get("${ctx}/wms/basicdata/banQinCdRuleQcClass/deleteAll?ids=" + rowIds, function (data) {
                if (data.success) {
                    $('#cdRuleQcClassTable').bootstrapTable('refresh');
                    hideTabRight(index);
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            })
        }

        /**
         * 删除质检处理意见明细
         * @param rowIds 明细行Ids
         */
        function removeQcDetail(rowIds, index) {
            jp.get("${ctx}/wms/basicdata/banQinCdRuleQcDetail/deleteAll?ids=" + rowIds, function (data) {
                if (data.success) {
                    $('#cdRuleQcDetailTable').bootstrapTable('refresh');
                    hideTabRight(index);
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            })
        }
        
	</script>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 20px;">
    <shiro:hasPermission name="basicdata:banQinCdRuleQcHeader:edit">
        <a class="btn btn-primary btn-sm" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
</div>
<div class="wrapper wrapper-content-my">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">基础信息</h3>
        </div>
        <div class="panel-body">
            <form:form id="inputForm" modelAttribute="banQinCdRuleQcHeader" method="post">
            <form:hidden path="id"/>
            <form:hidden path="recVer"/>
            <form:hidden path="orgId"/>
            <table class="table">
                <tbody>
                <tr>
                    <td width="8%"><label class="pull-right"><font color="red">*</font>规则编码</label></td>
                    <td width="12%">
                        <form:input path="ruleCode" htmlEscape="false" class="form-control required" maxlength="32"/>
                    </td>
                    <td width="8%"><label class="pull-right"><font color="red">*</font>规则名称</label></td>
                    <td width="12%">
                        <form:input path="ruleName" htmlEscape="false" class="form-control required" maxlength="64"/>
                    </td>
                    <td width="8%"><label class="pull-right">质检类型</label></td>
                    <td width="12%">
                        <form:select path="qcType" class="form-control m-b">
                            <form:option value="" label=""/>
                            <form:options items="${fns:getDictList('SYS_WM_QC_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                        </form:select>
                    </td>
                    <td width="8%"><label class="pull-right">备注</label></td>
                    <td width="12%">
                        <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="256"/>
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
        <div class="panel-body">
            <div class="tabs-container">
                <ul class="nav nav-tabs">
                    <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">级差</a></li>
                    <li class=""><a data-toggle="tab" href="#tab-2" aria-expanded="false">质检处理意见</a></li>
                </ul>
                <div class="tab-content">
                    <div id="tab-1" class="tab-pane fade in active">
                        <div id="toolbar1" style="padding: 10px 0px;">
                            <shiro:hasPermission name="basicdata:banQinCdRuleQcClass:addDetail">
                                <a class="btn btn-primary btn-sm" id="addClass" onclick="addDetail(1)">新增</a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="basicdata:banQinCdRuleQcClass:saveDetail">
                                <a class="btn btn-primary btn-sm" id="saveClass" onclick="saveDetail(1)">保存</a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="basicdata:banQinCdRuleQcClass:removeDetail">
                                <a class="btn btn-danger btn-sm" id="deleteClass" onclick="removeDetail(1)">删除</a>
                            </shiro:hasPermission>
                        </div>
                        <div id="tab1-left">
                            <table id="cdRuleQcClassTable" class="table text-nowrap"></table>
                        </div>
                        <div id="tab1-right" style="overflow: scroll; height: 200px; display: none;">
                            <div style="background-color: #2C3E50; width: 100%; height: 30px; color: white; line-height: 35px;">&nbsp;</div>
                            <form:form id="inputForm1" method="post" class="form-horizontal">
                                <input type="hidden" id="class_id" name="id"/>
                                <input type="hidden" id="class_ruleCode" name="ruleCode"/>
                                <input type="hidden" id="class_orgId" name="orgId"/>
                                <input type="hidden" id="class_lineNo" name="lineNo"/>
                                <input type="hidden" id="class_headerId" name="headerId"/>
                                <input type="hidden" id="class_recVer" name="recVer"/>
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td width="20%">
                                            <label class="pull-left"><font color="red">*</font>级差区间从(大于)</label>
                                        </td>
                                        <td width="20%">
                                            <label class="pull-left"><font color="red">*</font>级差区间到(小于等于)</label>
                                        </td>
                                        <td width="20%">
                                            <label class="pull-left"><font color="red">*</font>数量类型</label>
                                        </td>
                                        <td width="20%">
                                            <label class="pull-left"><font color="red">*</font>数量/比例</label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="20%">
                                            <input id="class_fmClass" name="fmClass" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0)"/>
                                        </td>
                                        <td width="20%">
                                            <input id="class_toClass" name="toClass" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0)"/>
                                        </td>
                                        <td width="20%">
                                            <select id="class_qtyType" name="qtyType" class="form-control m-b required">
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_QTY_TYPE')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td width="20%">
                                            <input id="class_qty" name="qty" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0)"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </form:form>
                        </div>
                    </div>
                    <div id="tab-2" class="tab-pane fade">
                        <div id="toolbar2" style="padding: 10px 0px;">
                            <shiro:hasPermission name="basicdata:banQinCdRuleQcDetail:addDetail">
                                <a class="btn btn-primary btn-sm" id="addDetail" onclick="addDetail(2)">新增</a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="basicdata:banQinCdRuleQcDetail:saveDetail">
                                <a class="btn btn-primary btn-sm" id="saveDetail" onclick="saveDetail(2)">保存</a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="basicdata:banQinCdRuleQcDetail:removeDetail">
                                <a class="btn btn-danger btn-sm" id="delDetail" onclick="removeDetail(2)">删除</a>
                            </shiro:hasPermission>
                        </div>
                        <div id="tab2-left">
                            <table id="cdRuleQcDetailTable" class="table text-nowrap"></table>
                        </div>
                        <div id="tab2-right" style="overflow: scroll; height: 200px; display: none;">
                            <div style="background-color: #2C3E50; width: 100%; height: 30px; color: white; line-height: 35px;">&nbsp;</div>
                            <form:form id="inputForm2" method="post" class="form-horizontal">
                                <input type="hidden" id="detail_id" name="id"/>
                                <input type="hidden" id="detail_ruleCode" name="ruleCode"/>
                                <input type="hidden" id="detail_orgId" name="orgId"/>
                                <input type="hidden" id="detail_lineNo" name="lineNo"/>
                                <input type="hidden" id="detail_headerId" name="headerId"/>
                                <input type="hidden" id="detail_recVer" name="recVer"/>
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td width="20%">
                                            <label class="pull-left"><font color="red">*</font>合格率区间从(大于)</label>
                                        </td>
                                        <td width="20%">
                                            <label class="pull-left"><font color="red">*</font>合格率区间到(小于等于)</label>
                                        </td>
                                        <td width="20%">
                                            <label class="pull-left"><font color="red">*</font>质检处理建议</label>
                                        </td>
                                        <td width="20%">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="20%">
                                            <input id="detail_fmRate" name="fmRate" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0)"/>
                                        </td>
                                        <td width="20%">
                                            <input id="detail_toRate" name="toRate" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0)"/>
                                        </td>
                                        <td width="20%">
                                            <select id="detail_qcSuggest" name="qcSuggest" htmlEscape="false" class="form-control m-b required">
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_QC_SUGGEST')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td width="20%">
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </form:form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>