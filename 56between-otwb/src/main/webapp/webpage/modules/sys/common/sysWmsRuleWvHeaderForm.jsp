<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>波次规则管理</title>
	<meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript">
        var currentRow; // 明细当前行
        var isShowTab = false; // 是否显示右边tab页

        $(document).ready(function () {
            // 初始化
            init();
            // 初始化明细table
            initCdRuleWvDetailTable();
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
         * 修改表单中disable状态
         */
        function openDisable(obj) {
            $(obj + " :disabled").each(function () {
                if (parseInt($(this).val()) != -1) {
                    $(this).prop("disabled", false);
                }
            });
            return true;
        }

        /**
         * 保存单头
         */ 
        function save() {
            var isValidate = bq.headerSubmitCheck('#inputForm');
            if (isValidate.isSuccess) {
                jp.loading();
                jp.post("${ctx}/sys/common/wms/ruleWvHeader/save", $('#inputForm').serialize(), function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        window.location = "${ctx}/sys/common/wms/ruleWvHeader/form?id=" + data.body.entity.id;
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            } else {
                jp.bqError(isValidate.msg);
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
        function initCdRuleWvDetailTable() {
            $('#cdRuleWvDetailTable').bootstrapTable({
                //请求方法
                method: 'get',
                //类型json
                dataType: "json",
                //显示刷新按钮
                showRefresh: false,
                //显示切换手机试图按钮
                showToggle: false,
                //显示 内容列下拉框
                showColumns: false,
                //显示到处按钮
                showExport: false,
                //显示切换分页按钮
                showPaginationSwitch: false,
                //显示详情按钮
                detailView: false,
                //显示详细内容函数
                detailFormatter: "detailFormatter",
                //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                cache: false,
                //是否显示分页（*）  
                pagination: false,
                //排序方式 
                sortOrder: "asc",
                //初始化加载第一页，默认第一页
                pageNumber: 1,
                //每页的记录行数（*）   
                pageSize: 10,
                //可供选择的每页的行数（*）    
                pageList: [10, 25, 50, 100],
                //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据  
                url: "${ctx}/sys/common/wms/ruleWvDetail/data",
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
                    field: 'mainCode',
                    title: '主规则',
                    sortable: true,
                    formatter:function(value, row , index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_WAVE_RULE'))}, value, "-");
                    }
                },
                {
                    field: 'isEnable',
                    title: '是否启用',
                    sortable: true,
                    formatter:function(value, row , index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
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
                // 加载波次限制列表
                initDetailWv();
                // 加载订单限制列表
                initDetailOrder();
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
                // 加载波次限制列表
                initDetailWv();
                // 加载订单限制列表
                initDetailOrder();
            } else {
                // 隐藏右边tab
                hideTabRight();
            }
            $("#cdRuleWvDetailTable").bootstrapTable('resetView');
        }

        /**
         * 赋值
         */
        function evaluate() {
            $("input[id^=detail]").each(function() {
                var $Id = $(this).attr('id');
                var $Name = $(this).attr('name');
                $('#' + $Id).val(eval("currentRow." + $Name));
                if ($Id === 'detail_isEnable') {
                    $('#' + $Id).prop('checked', $('#' + $Id).val() === 'Y');
                }
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
            });
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
            // 是否启用默认值
            $('#detail_isEnable').prop('checked', true).val('Y');
            $('#detail_recVer').val('0');
            // 移除波次限制
            $('#ruleWvDetailWvList').empty();
            // 移除订单限制
            $('#ruleWvDetailOrderList').empty();
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
                openDisable("#inputForm1");
                // 保存前赋值
                beforeSave();
                jp.post("${ctx}/sys/common/wms/ruleWvDetail/save", $('#inputForm1').bq_serialize(), function (data) {
                    if (data.success) {
                        $('#cdRuleWvDetailTable').bootstrapTable('refresh');
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
            return $.map($('#cdRuleWvDetailTable').bootstrapTable('getSelections'), function (row) {
                return row.id
            });
        }

        /**
         * 删除明细
         */
        function removeDetail() {
            var rowIds = getIdSelections();
            if (rowIds.length === 0) {
                jp.bqError("请选择一条记录!");
                return;
            }
            jp.confirm('确认要删除吗？删除将不能恢复？', function () {
                jp.get("${ctx}/sys/common/wms/ruleWvDetail/deleteAll?ids=" + rowIds, function (data) {
                    if (data.success) {
                        $('#cdRuleWvDetailTable').bootstrapTable('refresh');
                        hideTabRight();
                        jp.success(data.msg);
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            })
        }
        
        /**
         * 新增
         */ 
        function addDetailWvRow(list, idx, tpl, row) {
            if (!row) {
                tpl = tpl.replace("{{row.dataSet}}", currentRow.dataSet);
                tpl = tpl.replace("{{row.ruleCode}}", currentRow.ruleCode);
                tpl = tpl.replace("{{row.lineNo}}", currentRow.lineNo);
                tpl = tpl.replace("{{row.headerId}}", currentRow.id);
                tpl = tpl.replace("{{row.recVer}}", '0');
            }
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            $(list + idx).find("select").each(function() {
                $(this).val($(this).attr("data-value"));
            });
        }
        
        /**
         * 新增波次限制
         */ 
        function addDetailWv() {
            addDetailWvRow('#ruleWvDetailWvList', ruleWvDetailWvRowIdx, ruleWvDetailWvTpl);
            ruleWvDetailWvRowIdx = ruleWvDetailWvRowIdx + 1;
        }
        
        /**
         * 初始化波次限制
         */ 
        function initDetailWv() {
            // 先移除当前数据
            $('#ruleWvDetailWvList').empty();
            var params = "headerId=" + currentRow.id;
            params += "&dataSet=" + currentRow.dataSet;
            jp.post("${ctx}/sys/common/wms/ruleWvDetailWv/data?" + params, null, function (data) {
                ruleWvDetailWvRowIdx = 0;
                for (var i = 0; i < data.length; i++) {
                    addDetailWvRow('#ruleWvDetailWvList', ruleWvDetailWvRowIdx, ruleWvDetailWvTpl, data[i]);
                    ruleWvDetailWvRowIdx = ruleWvDetailWvRowIdx + 1;
                }
            })
        }

        /**
         * 保存波次限制明细
         */
        function saveDetailWv() {
            if (!$('#detail_id').val()) {
                jp.warning('请先保存单头!');
                return;
            }
            
            // 校验数据
            if (!checkDetailWv()) {
                return;
            } 
            jp.post("${ctx}/sys/common/wms/ruleWvDetailWv/save", $('#inputForm2').serialize(), function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    hideTabRight();
                    $('#cdRuleWvDetailTable').bootstrapTable('refresh');
                } else {
                    jp.bqError(data.msg);
                }
            })
        }
        
        /**
         * 校验波次限制
         */ 
        function checkDetailWv() {
            var flag = true;
            var message = '';
            var dataArray = $('#inputForm2').serializeArray();
            for (var i in dataArray) {
                if (dataArray[i].name.indexOf('condition') !== -1 && !dataArray[i].value) {
                    flag = false;
                    message += '限制条件不能为空!<br>';
                }
                if (dataArray[i].name.indexOf('operator') !== -1 && !dataArray[i].value) {
                    flag = false;
                    message += '运算符不能为空!<br>';
                }
                if (dataArray[i].name.indexOf('value') !== -1 && !dataArray[i].value) {
                    flag = false;
                    message += '值不能为空!<br>';
                }
            }
            if (!flag) {
                jp.warning(message);
            }
            return flag;
        }
        
        /**
         * 波次限制多选
         */ 
        function wvSelectChange(flag) {
            $("input[name='detailWvSelect']:checkbox").prop('checked', flag);
        }
        
        /**
         * 删除波次限制明细
         */
        function removeDetailWv() {
            var ids = [];
            var idRows = [];
            $("input[name='detailWvSelect']:checked").each(function() {
                var idx = $(this).prop('id').split('_')[0];
                ids.push($('#' + idx + '_id').val());
                idRows.push(idx + '_id');
            });
            if (ids.length === 0) {
                jp.warning("请勾选记录");
                return;
            }
            jp.get("${ctx}/sys/common/wms/ruleWvDetailWv/deleteAll?ids=" + ids.join(","), function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    // 移除选中行
                    for (var i in idRows) {
                        $('#' + idRows[i]).parent().parent().remove();
                    }
                    ruleWvDetailWvRowIdx -= ids.length;
                } else {
                    jp.bqError(data.msg);
                }
            })
        }

        /**
         * 新增
         */
        function addDetailOrderRow(list, idx, tpl, row) {
            if (!row) {
                tpl = tpl.replace("{{row.dataSet}}", currentRow.dataSet);
                tpl = tpl.replace("{{row.ruleCode}}", currentRow.ruleCode);
                tpl = tpl.replace("{{row.lineNo}}", currentRow.lineNo);
                tpl = tpl.replace("{{row.headerId}}", currentRow.id);
                tpl = tpl.replace("{{row.recVer}}", '0');
            }
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            $(list+idx).find("select").each(function() {
                $(this).val($(this).attr("data-value"));
            });
        }
        
        /**
         * 新增订单限制
         */ 
        function addDetailOrder() {
            addDetailOrderRow('#ruleWvDetailOrderList', ruleWvDetailOrderRowIdx, ruleWvDetailOrderTpl);
            ruleWvDetailOrderRowIdx = ruleWvDetailOrderRowIdx + 1;
        }
        
        /**
         * 初始化订单限制
         */ 
        function initDetailOrder() {
            // 先移除当前元素
            $('#ruleWvDetailOrderList').empty();
            var params = "headerId=" + currentRow.id;
            params += "&dataSet=" + currentRow.dataSet;
            jp.post("${ctx}/sys/common/wms/ruleWvDetailOrder/data?" + params, null, function (data) {
                ruleWvDetailOrderRowIdx = 0;
                for (var i = 0; i < data.length; i++) {
                    addDetailOrderRow('#ruleWvDetailOrderList', ruleWvDetailOrderRowIdx, ruleWvDetailOrderTpl, data[i]);
                    ruleWvDetailOrderRowIdx = ruleWvDetailOrderRowIdx + 1;
                }
            })
        }
        
        /**
         * 保存订单限制明细
         */
        function saveDetailOrder() {
            if (!$('#detail_id').val()) {
                jp.warning('请先保存单头!');
                return;
            }
            // 校验数据
            if (!checkDetailOrder()) {
                return;
            }
            jp.loading();
            jp.post("${ctx}/sys/common/wms/ruleWvDetailOrder/save", $('#inputForm3').serialize(), function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    hideTabRight();
                    $('#cdRuleWvDetailTable').bootstrapTable('refresh');
                } else {
                    jp.bqError(data.msg);
                }
            })
        }

        /**
         * 校验订单限制
         */
        function checkDetailOrder() {
            var flag = true;
            var message = '';
            var dataArray = $('#inputForm3').serializeArray();
            for (var i in dataArray) {
                if (dataArray[i].name.indexOf('andOr') !== -1 && !dataArray[i].value) {
                    flag = false;
                    message += 'AND/OR不能为空!<br>';
                }
                if (dataArray[i].name.indexOf('orderAttCode') !== -1 && !dataArray[i].value) {
                    flag = false;
                    message += '出库单属性名称不能为空!<br>';
                }
                if (dataArray[i].name.indexOf('operator') !== -1 && !dataArray[i].value) {
                    flag = false;
                    message += '运算符不能为空!<br>';
                }
                if (dataArray[i].name.indexOf('value') !== -1 && !dataArray[i].value) {
                    flag = false;
                    message += '值不能为空!<br>';
                }
            }
            if (!flag) {
                jp.warning(message);
            }
            return flag;
        }
        
        /**
         * 订单限制多选
         */ 
        function orderSelectChange(flag) {
            $("input[name='detailOrderSelect']:checkbox").prop('checked', flag);
        }

        /**
         * 删除订单限制明细
         */
        function removeDetailOrder() {
            var ids = [];
            var idRows = [];
            $("input[name='detailOrderSelect']:checked").each(function() {
                var idx = $(this).prop('id').split('_')[0];
                ids.push($('#' + idx + '_id').val());
                idRows.push(idx + '_id');
            });
            if (ids.length === 0) {
                jp.warning("请勾选记录");
                return;
            }
            jp.loading();
            jp.get("${ctx}/sys/common/wms/ruleWvDetailOrder/deleteAll?ids=" + ids.join(","), function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    // 移除选中行
                    for (var i in idRows) {
                        $('#' + idRows[i]).parent().parent().remove();
                    }
                    ruleWvDetailOrderRowIdx -= ids.length;
                } else {
                    jp.bqError(data.msg);
                }
            })
        }

        /**
         * 是否启用选择事件
         */
        function isEnableChange(flag) {
            $('#detail_isEnable').val(flag ? 'Y' : 'N');
        }
    </script>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 20px;">
    <shiro:hasPermission name="sys:common:wms:ruleWv:edit">
        <a class="btn btn-primary" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
</div>
<div class="wrapper wrapper-content-my">
    <div class="panel panel-default">
        <div class="panel-body">
            <form:form id="inputForm" modelAttribute="sysWmsRuleWvHeader" method="post" class="form-horizontal">
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
                        <td class="width-8"><label class="pull-right"><font color="red">*</font>数据套</label></td>
                        <td class="width-12">
                            <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                                      fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysWmsRuleWvHeader.dataSet}"
                                      displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysWmsRuleWvHeader.dataSetName}"
                                      fieldLabels="编码|名称" fieldKeys="code|name"
                                      searchLabels="编码|名称" searchKeys="code|name"/>
                        </td>
                        <td class="width-8"></td>
                        <td class="width-12"></td>
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
                <shiro:hasPermission name="sys:common:wms:ruleWv:addDetail">
                    <a class="btn btn-primary" id="addDetail" onclick="addDetail()">新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:wms:ruleWv:saveDetail">
                    <a class="btn btn-primary" id="saveDetail" onclick="saveDetail()">保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:wms:ruleWv:removeDetail">
                    <a class="btn btn-primary" id="deleteDetail" onclick="removeDetail()">删除</a>
                </shiro:hasPermission>
            </div>
            <div id="tab-left">
                <table id="cdRuleWvDetailTable" class="table text-nowrap"></table>
            </div>
            <div id="tab-right" style="overflow: scroll; border: 1px solid #dddddd; display: none;">
                <div class="tabs-container" style="height: 100%; width: 100%; padding: 10px 10px;">
                    <ul class="nav nav-tabs">
                        <li class="active"><a data-toggle="tab" href="#mainRule" aria-expanded="true">主规则</a></li>
                        <li class=""><a data-toggle="tab" href="#orderCondition" aria-expanded="true">订单限制</a></li>
                    </ul>
                    <div class="tab-content">
                        <div id="mainRule" class="tab-pane fade in active">
                            <form:form id="inputForm1" method="post" class="form-horizontal">
                                <input type="hidden" id="detail_id" name="id"/>
                                <input type="hidden" id="detail_ruleCode" name="ruleCode"/>
                                <input type="hidden" id="detail_dataSet" name="dataSet"/>
                                <input type="hidden" id="detail_recVer" name="recVer"/>
                                <input type="hidden" id="detail_headerId" name="headerId"/>
                                <input type="hidden" id="detail_sql" name="sql"/>
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td class="pad" width="20%">
                                            <label class="pull-left"><font color="red">*</font>行号</label>
                                        </td>
                                        <td class="pad" width="20%">
                                            <label class="pull-left"><font color="red">*</font>主规则</label>
                                        </td>
                                        <td class="pad" width="20%">
                                            <label class="pull-left">描述</label>
                                        </td>
                                        <td class="pad" width="20%">
                                            <label class="pull-left">是否启用</label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="pad" width="20%">
                                            <input id="detail_lineNo" name="lineNo" htmlEscape="false" class="form-control required" maxlength="4"/>
                                        </td>
                                        <td class="pad" width="20%">
                                            <select id="detail_mainCode" name="mainCode" class="form-control m-b required">
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_WAVE_RULE')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="pad" width="20%">
                                            <input id="detail_desc" name="desc" htmlEscape="false" class="form-control" maxlength="128"/>
                                        </td>
                                        <td class="pad" width="20%">
                                            <input id="detail_isEnable" name="isEnable" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isEnableChange(this.checked)"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </form:form>
                            <div style="width: 100%; background-color: #2c3e50; height: 30px; line-height: 30px; margin-top: 20px; margin-left: 10px; color: white;">&nbsp;波次限制</div>
                            <div style="width: 100%; padding-top: 10px; padding-left: 10px;">
                                <a class="btn btn-primary" id="addDetailWv" onclick="addDetailWv()">新增</a>
                                <a class="btn btn-primary" id="saveDetailWv" onclick="saveDetailWv()">保存</a>
                                <a class="btn btn-primary" id="removeDetailWv" onclick="removeDetailWv()">删除</a>
                            </div>
                            <form:form id="inputForm2" method="post" class="form-horizontal">
                                <table class="table table-striped table-bordered table-condensed">
                                    <thead>
                                    <tr>
                                        <th class="th-inner"><input type="checkbox" onclick="wvSelectChange(this.checked)"/></th>
                                        <th class="hide"></th>
                                        <th><font color="red">*</font>限制条件</th>
                                        <th><font color="red">*</font>运算符</th>
                                        <th><font color="red">*</font>值</th>
                                    </tr>
                                    </thead>
                                    <tbody id="ruleWvDetailWvList">
                                    </tbody>
                                </table>
                                <script type="text/template" id="ruleWvDetailWvTpl">//<!--
                        <tr id="ruleWvDetailWvList{{idx}}">
                            <td>
                                <input id="ruleWvDetailWvList{{idx}}_checkbox" type="checkbox" name="detailWvSelect" class="customizeCheckbox"/>
                            </td>
                            <td class="hide">
                                <input id="ruleWvDetailWvList{{idx}}_id" name="ruleWvDetailWvList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
                                <input id="ruleWvDetailWvList{{idx}}_delFlag" name="ruleWvDetailWvList[{{idx}}].delFlag" type="hidden" value="0"/>
                                <input id="ruleWvDetailWvList{{idx}}_dataSet" name="ruleWvDetailWvList[{{idx}}].dataSet" type="hidden" value="{{row.dataSet}}"/>
                                <input id="ruleWvDetailWvList{{idx}}_headerId" name="ruleWvDetailWvList[{{idx}}].headerId" type="hidden" value="{{row.headerId}}"/>
                                <input id="ruleWvDetailWvList{{idx}}_ruleCode" name="ruleWvDetailWvList[{{idx}}].ruleCode" type="hidden" value="{{row.ruleCode}}"/>
                                <input id="ruleWvDetailWvList{{idx}}_lineNo" name="ruleWvDetailWvList[{{idx}}].lineNo" type="hidden" value="{{row.lineNo}}"/>
                                <input id="ruleWvDetailWvList{{idx}}_recVer" name="ruleWvDetailWvList[{{idx}}].recVer" type="hidden" value="{{row.recVer}}"/>
                            </td>
                            <td>
                                <select id="ruleWvDetailWvList{{idx}}_condition" name="ruleWvDetailWvList[{{idx}}].condition" data-value="{{row.condition}}" class="form-control m-b">
                                    <option value=""></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_WAVE_CONDITION')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>
                                <select id="ruleWvDetailWvList{{idx}}_operator" name="ruleWvDetailWvList[{{idx}}].operator" data-value="{{row.operator}}" class="form-control m-b">
                                    <option value=""></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_WV_OPERATOR')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>
                                <input id="ruleWvDetailWvList{{idx}}_value" name="ruleWvDetailWvList[{{idx}}].value" type="text" value="{{row.value}}" class="form-control" maxlength="32"/>
                            </td>
                        </tr>//-->
                                </script>
                                <script type="text/javascript">
                                    var ruleWvDetailWvRowIdx = 0, ruleWvDetailWvTpl = $("#ruleWvDetailWvTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                                </script>
                            </form:form>
                        </div>
                        <div id="orderCondition" class="tab-pane fade">
                            <div style="width: 100%; padding-top: 10px; padding-left: 10px;">
                                <a class="btn btn-primary" id="addDetailOrder" onclick="addDetailOrder()">新增</a>
                                <a class="btn btn-primary" id="saveDetailOrder" onclick="saveDetailOrder()">保存</a>
                                <a class="btn btn-primary" id="removeDetailOrder" onclick="removeDetailOrder()">删除</a>
                            </div>
                            <form:form id="inputForm3" method="post" class="form-horizontal">
                                <table class="table table-striped table-bordered table-condensed">
                                    <thead>
                                    <tr>
                                        <th class="th-inner"><input type="checkbox" onclick="orderSelectChange(this.checked)"/></th>
                                        <th class="hide"></th>
                                        <th><font color="red">*</font>AND/OR</th>
                                        <th><font color="red">*</font>出库单属性名称</th>
                                        <th><font color="red">*</font>运算符</th>
                                        <th><font color="red">*</font>值</th>
                                    </tr>
                                    </thead>
                                    <tbody id="ruleWvDetailOrderList">
                                    </tbody>
                                </table>
                                <script type="text/template" id="ruleWvDetailOrderTpl">//<!--
                        <tr id="ruleWvDetailOrderList{{idx}}">
                            <td>
                                <input id="ruleWvDetailOrderList{{idx}}_checkbox" type="checkbox" name="detailOrderSelect" class="customizeCheckbox"/>
                            </td>
                            <td class="hide">
                                <input id="ruleWvDetailOrderList{{idx}}_id" name="ruleWvDetailOrderList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
                                <input id="ruleWvDetailOrderList{{idx}}_headerId" name="ruleWvDetailOrderList[{{idx}}].headerId" type="hidden" value="{{row.headerId}}"/>
                                <input id="ruleWvDetailOrderList{{idx}}_delFlag" name="ruleWvDetailOrderList[{{idx}}].delFlag" type="hidden" value="0"/>
                                <input id="ruleWvDetailOrderList{{idx}}_dataSet" name="ruleWvDetailOrderList[{{idx}}].dataSet" type="hidden" value="{{row.dataSet}}"/>
                                <input id="ruleWvDetailOrderList{{idx}}_ruleCode" name="ruleWvDetailOrderList[{{idx}}].ruleCode" type="hidden" value="{{row.ruleCode}}"/>
                                <input id="ruleWvDetailOrderList{{idx}}_lineNo" name="ruleWvDetailOrderList[{{idx}}].lineNo" type="hidden" value="{{row.lineNo}}"/>
                                <input id="ruleWvDetailOrderList{{idx}}_leftBracket" name="ruleWvDetailOrderList[{{idx}}].leftBracket" type="hidden" value="("/>
                                <input id="ruleWvDetailOrderList{{idx}}_rightBracket" name="ruleWvDetailOrderList[{{idx}}].rightBracket" type="hidden" value=")"/>
                                <input id="ruleWvDetailOrderList{{idx}}_recVer" name="ruleWvDetailOrderList[{{idx}}].recVer" type="hidden" value="{{row.recVer}}"/>
                            </td>
                            <td>
                                <select id="ruleWvDetailOrderList{{idx}}_andOr" name="ruleWvDetailOrderList[{{idx}}].andOr" data-value="{{row.andOr}}" class="form-control m-b  ">
                                    <option value=""></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_AND_OR')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>
                                <select id="ruleWvDetailOrderList{{idx}}_orderAttCode" name="ruleWvDetailOrderList[{{idx}}].orderAttCode" data-value="{{row.orderAttCode}}" class="form-control m-b">
                                    <option value=""></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_ORDER_ATT')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>
                                <select id="ruleWvDetailOrderList{{idx}}_operator" name="ruleWvDetailOrderList[{{idx}}].operator" data-value="{{row.operator}}" class="form-control m-b">
                                    <option value=""></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_OPERATOR')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>
                                <input id="ruleWvDetailOrderList{{idx}}_value" name="ruleWvDetailOrderList[{{idx}}].value" value="{{row.value}}" type="text" class="form-control" maxlength="32"/>
                            </td>
                        </tr>//-->
                                </script>
                                <script type="text/javascript">
                                    var ruleWvDetailOrderRowIdx = 0, ruleWvDetailOrderTpl = $("#ruleWvDetailOrderTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                                </script>
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