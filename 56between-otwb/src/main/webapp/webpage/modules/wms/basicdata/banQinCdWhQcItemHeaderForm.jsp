<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>质检项管理</title>
	<meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript">
        var currentRow; // 明细当前行
        var isShowTab = false; // 是否显示右边tab页

        $(document).ready(function () {
            // 初始化
            init();
            // 初始化明细table
            initCdWhQcItemDetailTable();
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
                jp.post("${ctx}/wms/basicdata/banQinCdWhQcItemHeader/save", $('#inputForm').serialize(), function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        window.location = "${ctx}/wms/basicdata/banQinCdWhQcItemHeader/form?id=" + data.body.entity.id;
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
                $('#orgId').val(jp.getCurrentOrg().orgId);
                $('#addDetail').attr('disabled', true);
            } else {
                $('#itemGroupCode').prop('readonly', true);
            }
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
        function initCdWhQcItemDetailTable() {
            $('#cdWhQcItemDetailTable').bootstrapTable({
                cache: false,// 是否使用缓存
                sidePagination: "server",// client客户端分页，server服务端分页
                url: "${ctx}/wms/basicdata/banQinCdWhQcItemDetail/data",
                queryParams: function (params) {
                    var searchParam = {};
                    searchParam.headerId = !$('#id').val() == true ? "#" : $('#id').val();
                    searchParam.orgId = $('#orgId').val();
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                onClickRow: function (row, $el) {
                    clickDetail(row);
                },
                onDblClickRow: function (row, $el) {
                    dbClickDetail(row);
                },
                columns: [{
                    checkbox: true
                }, {
                    field: 'lineNo',
                    title: '行号',
                    sortable: true
                }, {
                    field: 'qcItem',
                    title: '质检项名称',
                    sortable: true
                }, {
                    field: 'qcWay',
                    title: '质检方式',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_WAY'))}, value, "-");
                    }
                }, {
                    field: 'qcRef',
                    title: '质检参考标准',
                    sortable: true
                }, {
                    field: 'remarks',
                    title: '备注',
                    sortable: true
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
            $("#cdWhQcItemDetailTable").bootstrapTable('resetView');
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
                var disabledObjs = bq.openDisabled("#inputForm1");
                // 保存前赋值
                beforeSave();
                var params = $('#inputForm1').serialize();
                bq.closeDisabled(disabledObjs);
                jp.post("${ctx}/wms/basicdata/banQinCdWhQcItemDetail/save", params, function (data) {
                    if (data.success) {
                        $('#cdWhQcItemDetailTable').bootstrapTable('refresh');
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
                $('#detail_orgId').val($('#orgId').val());
                $('#detail_itemGroupCode').val($('#itemGroupCode').val());
                $('#detail_headerId').val($('#id').val());
            }
        }

        /**
         * 获取表格勾选行Ids
         */
        function getIdSelections() {
            return $.map($('#cdWhQcItemDetailTable').bootstrapTable('getSelections'), function (row) {
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
                jp.get("${ctx}/wms/basicdata/banQinCdWhQcItemDetail/deleteAll?ids=" + rowIds, function (data) {
                    if (data.success) {
                        $('#cdWhQcItemDetailTable').bootstrapTable('refresh');
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
    <shiro:hasPermission name="basicdata:banQinCdWhQcItemHeader:edit">
        <a class="btn btn-primary" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
</div>
<div class="wrapper wrapper-content-my">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">基础信息</h3>
        </div>
        <div class="panel-body">
            <form:form id="inputForm" modelAttribute="banQinCdWhQcItemHeader" method="post">
            <form:hidden path="id"/>
            <form:hidden path="recVer"/>
            <form:hidden path="orgId"/>
            <table class="table">
                <tbody>
                <tr>
                    <td width="8%"><label class="pull-right"><font color="red">*</font>质检项组编码</label></td>
                    <td width="12%">
                        <form:input path="itemGroupCode" htmlEscape="false" class="form-control required" maxlength="32"/>
                    </td>
                    <td width="8%"><label class="pull-right"><font color="red">*</font>质检项组名称</label></td>
                    <td width="12%">
                        <form:input path="itemGroupName" htmlEscape="false" class="form-control required" maxlength="64"/>
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
            <h3 class="panel-title">明细信息</h3>
        </div>
        <div class="panel-body">
            <div style="width: 100%; padding-left: 10px;">
            <div id="detailToolbar" style="width: 100%; padding: 10px 0px;">
                <shiro:hasPermission name="basicdata:banQinCdWhQcItemDetail:addDetail">
                    <a class="btn btn-primary" id="addDetail" onclick="addDetail()">新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basicdata:banQinCdWhQcItemDetail:saveDetail">
                    <a class="btn btn-primary" id="saveDetail" onclick="saveDetail()">保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basicdata:banQinCdWhQcItemDetail:removeDetail">
                    <a class="btn btn-danger" id="deleteDetail" onclick="removeDetail()">删除</a>
                </shiro:hasPermission>
            </div>
            <div id="tab-left">
                <table id="cdWhQcItemDetailTable" class="table text-nowrap"></table>
            </div>
            <div id="tab-right" style="overflow: scroll; height: 240px; border: 1px solid #dddddd; display: none;">
                <form:form id="inputForm1" method="post" class="form-horizontal">
                    <input type="hidden" id="detail_id" name="id"/>
                    <input type="hidden" id="detail_itemGroupCode" name="itemGroupCode"/>
                    <input type="hidden" id="detail_orgId" name="orgId"/>
                    <input type="hidden" id="detail_headerId" name="headerId"/>
                    <input type="hidden" id="detail_recVer" name="recVer"/>
                    <div class="tabs-container" style="height: 100%; width: 100%;">
                        <ul class="nav nav-tabs">
                            <li class="active"><a data-toggle="tab" href="#itemDetail" aria-expanded="true">商品明细</a></li>
                            <li class=""><a data-toggle="tab" href="#reservationInfo" aria-expanded="true">预留信息</a></li>
                        </ul>
                        <div class="tab-content">
                            <div id="itemDetail" class="tab-pane fade in active">
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td width="20%">
                                            <label class="pull-left">行号</label>
                                        </td>
                                        <td width="20%">
                                            <label class="pull-left"><font color="red">*</font>质检项名称</label>
                                        </td>
                                        <td width="20%">
                                            <label class="pull-left">质检方式</label>
                                        </td>
                                        <td width="20%">
                                            <label class="pull-left">质检参考标准</label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="20%">
                                            <input id="detail_lineNo" name="lineNo" htmlEscape="false" class="form-control" readonly="readonly"/>
                                        </td>
                                        <td width="20%">
                                            <input id="detail_qcItem" name="qcItem" htmlEscape="false" class="form-control" maxlength="64"/>
                                        </td>
                                        <td width="20%">
                                            <select id="detail_qcWay" name="qcWay" class="form-control m-b">
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_QC_WAY')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td width="20%" colspan="3">
                                            <input id="detail_qcRef" name="qcRef" htmlEscape="false" class="form-control" maxlength="64">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="20%">
                                            <label class="pull-left">备注</label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="20%" colspan="3">
                                            <input id="detail_remarks" name="remarks" htmlEscape="false" class="form-control" maxlength="256">
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div id="reservationInfo" class="tab-pane fade">
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td width="20%">
                                            <label class="pull-left">自定义1</label>
                                        </td>
                                        <td width="20%">
                                            <label class="pull-left">自定义2</label>
                                        </td>
                                        <td width="20%">
                                            <label class="pull-left">自定义3</label>
                                        </td>
                                        <td width="20%">
                                            <label class="pull-left">自定义4</label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="20%">
                                            <input id="detail_def1" name="def1" htmlEscape="false" class="form-control" maxlength="64"/>
                                        </td>
                                        <td width="20%">
                                            <input id="detail_def2" name="def2" htmlEscape="false" class="form-control" maxlength="64"/>
                                        </td>
                                        <td width="20%">
                                            <input id="detail_def3" name="def3" htmlEscape="false" class="form-control" maxlength="64"/>
                                        </td>
                                        <td width="20%">
                                            <input id="detail_def4" name="def4" htmlEscape="false" class="form-control" maxlength="64"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
        </div>
    </div>
</div>
</body>
</html>