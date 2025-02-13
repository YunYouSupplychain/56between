<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>上架规则管理</title>
	<meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
	<script type="text/javascript">
        var currentRow; // 明细当前行
        var isShowTab = false; // 是否显示右边tab页

        $(document).ready(function () {
            // 初始化
            init();
            // 初始化明细table
            initCdRulePaDetailTable();
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
         * 表头保存
         */ 
        function save() {
            var isValidate = jp.validateForm('#inputForm');
            if (!isValidate) {
                return false;
            } else {
                jp.loading();
                jp.post("${ctx}/wms/basicdata/banQinCdRulePaHeader/save", $('#inputForm').serialize(), function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        window.location = "${ctx}/wms/basicdata/banQinCdRulePaHeader/form?id=" + data.body.entity.id;
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
                $('#ruleCode').prop('readonly', true);
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
        function initCdRulePaDetailTable() {
            $('#cdRulePaDetailTable').bootstrapTable({
                cache: false,// 是否使用缓存
                sidePagination: "server",// client客户端分页，server服务端分页
                url: "${ctx}/wms/basicdata/banQinCdRulePaDetail/data",
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
                    clickDetail(row);
                },
                onDblClickRow: function (row, $el) {
                    dbClickDetail(row);
                },
                columns: [{
                    checkbox: true
                },{
                    field: 'lineNo',
                    title: '行号',
                    sortable: true
                },{
                    field: 'isEnable',
                    title: '是否启用',
                    sortable: true,
                    formatter:function(value, row , index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
                    }
                },{
                    field: 'fmLoc',
                    title: '源库位',
                    sortable: true
                },{
                    field: 'toLoc',
                    title: '目标库位',
                    sortable: true
                },{
                    field: 'toZone',
                    title: '目标库区',
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
            $("#cdRulePaDetailTable").bootstrapTable('resetView');
        }
        
        /**
         * 控制明细表单
         */
        function controlDetailForm() {
            // 库位空间限制
            initLocSpace($('#detail_isSpaceRestrict').val() === 'N');
            // 库位使用类型
            initLocUserType($('#detail_isUseTypeRestrict').val() === 'N');
            // 库位种类限制
            initLocCategory($('#detail_isCategoryRestrict').val() === 'N');
            // 库位ABC限制
            initLocAbc($('#detail_isAbcRestrict').val() === 'N');
            // 包装限制
            initPack($('#detail_isPackageRestrict').val() === 'N');
            // 入库单类型限制
            initAsnType($('#detail_isAsnTypeRestrict').val() === 'N');
            // 批次属性限制
            initLotAtt($('#detail_isLotAttRestrict').val() === 'N');
        }

        /**
         * 赋值
         */
        function evaluate() {
            $("input[id^=detail]").each(function() {
                var $Id = $(this).attr('id');
                var $Name = $(this).attr('name');
                $('#' + $Id).val(eval("currentRow." + $Name));
                if ($Id === 'detail_isEnable' || $Id === 'detail_isSpaceRestrict'
                    || $Id === 'detail_isPlRestrict' || $Id === 'detail_isCubicRestrict'
                    || $Id === 'detail_isWeightRestrict' || $Id === 'detail_isUseTypeRestrict'
                    || $Id === 'detail_isCategoryRestrict' || $Id === 'detail_isAbcRestrict'
                    || $Id === 'detail_isPackageRestrict' || $Id === 'detail_isLessCs'
                    || $Id === 'detail_isMoreCsLessPl' || $Id === 'detail_isMorePl'
                    || $Id === 'detail_isAsnTypeRestrict' || $Id === 'detail_isLotAttRestrict') {
                    $('#' + $Id).prop('checked', $('#' + $Id).val() === 'Y');
                }
            });

            $("select[id^=detail]").each(function() {
                var $Id = $(this).attr('id');
                var $Name = $(this).attr('name');
                if ($(this).hasClass('selectpicker')) {
                    var seArray = eval("currentRow." + $Name);
                    $('#' + $Id).selectpicker('val', seArray.split(',')).selectpicker('refresh');
                } else {
                    $('#' + $Id).val(eval("currentRow." + $Name));
                }
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
            $(" input[ type='checkbox' ]").prop("checked", false);
            $('#detail_recVer').val('0');
            // 初始化控件状态
            initDetailLabel();
        }

        /**
         * 初始化明细表单
         */
        function initDetailLabel() {
            $('#detail_isEnable').prop('checked', true).val("Y");
            // 库位空间限制
            initLocSpace(true);
            // 库位使用类型
            initLocUserType(true);
            // 库位种类限制
            initLocCategory(true);
            // 库位ABC限制
            initLocAbc(true);
            // 包装限制
            initPack(true);
            // 入库单类型限制
            initAsnType(true);
            // 批次属性限制
            initLotAtt(true);
        }

        /**
         * 初始化库位空间限制label
         */
        function initLocSpace(flag) {
            $('#detail_isSpaceRestrict').val(!flag ? 'Y' : 'N');
            $('#detail_isPlRestrict').prop('disabled', flag).prop('checked', false).val('N');
            $('#detail_isCubicRestrict').prop('disabled', flag).prop('checked', false).val('N');
            $('#detail_isWeightRestrict').prop('disabled', flag).prop('checked', false).val('N');
        }

        /**
         * 初始化库位使用类型label
         */
        function initLocUserType(flag) {
            $('#detail_isUseTypeRestrict').val(!flag ? 'Y' : 'N');
            $('#detail_includeUseType').prop('disabled', flag).selectpicker('refresh');
            $('#detail_excludeUseType').prop('disabled', flag).selectpicker('refresh');
        }

        /**
         * 初始化库位种类限制label
         */
        function initLocCategory(flag) {
            $('#detail_isCategoryRestrict').val(!flag ? 'Y' : 'N');
            $('#detail_includeCategory').prop('disabled', flag).selectpicker('refresh');
            $('#detail_excludeCategory').prop('disabled', flag).selectpicker('refresh');
        }

        /**
         * 初始化库位ABC限制label
         */
        function initLocAbc(flag) {
            $('#detail_isAbcRestrict').val(!flag ? 'Y' : 'N');
            $('#detail_includeAbc').prop('disabled', flag).selectpicker('refresh');
            $('#detail_excludeAbc').prop('disabled', flag).selectpicker('refresh');
        }

        /**
         * 初始化包装限制label
         */
        function initPack(flag) {
            $('#detail_isPackageRestrict').val(!flag ? 'Y' : 'N');
            $('#detail_isLessCs').prop('disabled', flag).prop('checked', false).val('N');
            $('#detail_isMoreCsLessPl').prop('disabled', flag).prop('checked', false).val('N');
            $('#detail_isMorePl').prop('disabled', flag).prop('checked', false).val('N');
        }

        /**
         * 初始化入库单类型限制label
         */
        function initAsnType(flag) {
            $('#detail_isAsnTypeRestrict').val(!flag ? 'Y' : 'N');
            $('#detail_includeAsnType').prop('disabled', flag).selectpicker('refresh');
            $('#detail_excludeAsnType').prop('disabled', flag).selectpicker('refresh');
        }

        /**
         * 初始化批次属性限制label
         */
        function initLotAtt(flag) {
            $('#detail_isLotAttRestrict').val(!flag ? 'Y' : 'N');
            $('#detail_lotAtt04Equal').prop('disabled', flag).selectpicker('refresh');
            $('#detail_lotAtt04Unequal').prop('disabled', flag).selectpicker('refresh');
            $('#detail_lotAtt05Equal').prop('disabled', flag);
            $('#detail_lotAtt05Unequal').prop('disabled', flag);
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
                var data = $('#inputForm1').bq_serialize();
                data += '&headerId=' + $('#id').val();
                jp.post("${ctx}/wms/basicdata/banQinCdRulePaDetail/save", data, function (data) {
                    if (data.success) {
                        $('#cdRulePaDetailTable').bootstrapTable('refresh');
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
                $('#detail_ruleCode').val($('#ruleCode').val());
            }
        }
        
        /**
         * 获取表格勾选行Ids
         */
        function getIdSelections() {
            return $.map($('#cdRulePaDetailTable').bootstrapTable('getSelections'), function (row) {
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
                jp.get("${ctx}/wms/basicdata/banQinCdRulePaDetail/deleteAll?ids=" + rowIds, function (data) {
                    if (data.success) {
                        $('#cdRulePaDetailTable').bootstrapTable('refresh');
                        hideTabRight();
                        jp.success(data.msg);
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            })
        }

        /**
         * 是否启用
         * @param flag
         */ 
        function isEnableChange(flag, obj) {
            $(obj).val(flag ? "Y" : "N");
        }
        
        /**
         * 是否库位空间限制
         * @param flag
         */
        function locSpaceChange(flag) {
            initLocSpace(!flag);
        }

        /**
         * 是否库位使用限制
         * @param flag
         */
        function locUserTypeChange(flag) {
            initLocUserType(!flag);
        }

        /**
         * 是否库位种类限制
         * @param flag
         */
        function locCategoryChange(flag) {
            initLocCategory(!flag);
        }

        /**
         * 是否库位ABC限制
         * @param flag
         */
        function locAbcChange(flag) {
            initLocAbc(!flag);
        }

        /**
         * 是否包装限制
         * @param flag
         */
        function packChange(flag) {
            initPack(!flag);
        }

        /**
         * 入库单类型限制
         * @param flag
         */
        function asnTypeChange(flag) {
            initAsnType(!flag);
        }

        /**
         * 批次属性限制
         * @param flag
         */
        function lotAttChange(flag) {
            initLotAtt(!flag);
        }
        
	</script>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 20px;">
    <shiro:hasPermission name="basicdata:banQinCdRulePaHeader:edit">
        <a class="btn btn-primary" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
</div>
<div class="wrapper wrapper-content-my">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h3 class="panel-title">基础信息</h3>
        </div>
        <div class="panel-body">
            <form:form id="inputForm" modelAttribute="banQinCdRulePaHeader" method="post" class="form-horizontal">
            <form:hidden path="id"/>
            <form:hidden path="orgId"/>
            <form:hidden path="recVer"/>
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
            <div style="width: 100%; padding-left: 10px;">
                <div id="detailToolbar" style="width: 100%; padding: 10px 0px;">
                    <shiro:hasPermission name="basicdata:banQinCdRulePaDetail:addDetail">
                        <a class="btn btn-primary" id="addDetail" onclick="addDetail()">新增</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="basicdata:banQinCdRulePaDetail:saveDetail">
                        <a class="btn btn-primary" id="saveDetail" onclick="saveDetail()">保存</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="basicdata:banQinCdRulePaDetail:removeDetail">
                        <a class="btn btn-danger" id="removeDetail" onclick="removeDetail()">删除</a>
                    </shiro:hasPermission>
                </div>
                <div id="tab-left">
                    <table id="cdRulePaDetailTable" class="table text-nowrap"></table>
                </div>
                <div id="tab-right" style="overflow: scroll; height: 500px; border: 1px solid #dddddd; display: none;">
                    <form:form id="inputForm1" method="post" class="form-horizontal">
                        <input type="hidden" id="detail_id" name="id"/>
                        <input type="hidden" id="detail_ruleCode" name="ruleCode"/>
                        <input type="hidden" id="detail_orgId" name="orgId"/>
                        <input type="hidden" id="detail_recVer" name="recVer"/>
                        <div class="tabs-container" style="height: 100%; width: 100%;">
                            <ul class="nav nav-tabs">
                                <li class="active"><a data-toggle="tab" href="#mainRule" aria-expanded="true">主规则</a></li>
                                <li class=""><a data-toggle="tab" href="#effectiveRule" aria-expanded="true">生效条件</a></li>
                            </ul>
                            <div class="tab-content">
                                <div id="mainRule" class="tab-pane fade in active">
                                    <table class="bq-table">
                                        <tbody>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left"><font color="red">*</font>行号</label>
                                                </td>
                                                <td class="pad" width="20%" colspan="3">
                                                    <label class="pull-left"><font color="red">*</font>主规则</label>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">是否启用</label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <input id="detail_lineNo" name="lineNo" htmlEscape="false" class="form-control required" maxlength="4"/>
                                                </td>
                                                <td class="pad" width="20%" colspan="3">
                                                    <select id="detail_mainCode" name="mainCode" class="form-control m-b required">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_PA_RULE')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <input id="detail_isEnable" name="isEnable" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isEnableChange(this.checked, '#detail_isEnable')"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">源库位</label>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">目标库位</label>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">目标库区</label>
                                                </td>
                                                <td class="pad" width="20%">
                                                </td>
                                                <td class="pad" width="20%">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择源库位" cssClass="form-control"
                                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                                   displayFieldId="detail_fmLoc" displayFieldName="fmLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                                   selectButtonId="detailFmLocSelectId" deleteButtonId="detailFmLocDeleteId"
                                                                   fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                                   searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择目标库位" cssClass="form-control"
                                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                                   displayFieldId="detail_toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                                   selectButtonId="detailToLocSelectId" deleteButtonId="detailToLocDeleteId"
                                                                   fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                                   searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择目标库区" cssClass="form-control"
                                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                                   displayFieldId="detail_toZone" displayFieldName="toZone" displayFieldKeyName="zoneCode" displayFieldValue=""
                                                                   selectButtonId="detailToZoneSelectId" deleteButtonId="detailToZoneDeleteId"
                                                                   fieldLabels="库区代码|库区名称" fieldKeys="zoneCode|zoneName"
                                                                   searchLabels="库区代码|库区名称" searchKeys="zoneCode|zoneName" inputSearchKey="zoneCodeAndName">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="pad" width="20%">
                                                </td>
                                                <td class="pad" width="20%">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">库位空间限制</label>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">托盘数限制</label>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">体积限制</label>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">重量限制</label>
                                                </td>
                                                <td class="pad" width="20%">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <input id="detail_isSpaceRestrict" name="isSpaceRestrict" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="locSpaceChange(this.checked)"/>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <input id="detail_isPlRestrict" name="isPlRestrict" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isEnableChange(this.checked, '#detail_isPlRestrict')"/>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <input id="detail_isCubicRestrict" name="isCubicRestrict" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isEnableChange(this.checked, '#detail_isCubicRestrict')"/>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <input id="detail_isWeightRestrict" name="isWeightRestrict" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isEnableChange(this.checked, '#detail_isWeightRestrict')"/>
                                                </td>
                                                <td class="pad" width="20%">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">库位使用类型限制</label>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <label class="pull-left">包含库位使用类型</label>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <label class="pull-left">不包含库位使用类型</label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <input id="detail_isUseTypeRestrict" name="isUseTypeRestrict" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="locUserTypeChange(this.checked)"/>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <select id="detail_includeUseType" name="includeUseType" class="selectpicker" multiple title="">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_LOC_USE_TYPE')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <select id="detail_excludeUseType" name="excludeUseType" class="selectpicker" multiple title="">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_LOC_USE_TYPE')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">库位种类限制</label>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <label class="pull-left">包含库位种类类型</label>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <label class="pull-left">不包含库位种类类型</label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <input id="detail_isCategoryRestrict" name="isCategoryRestrict" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="locCategoryChange(this.checked)"/>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <select id="detail_includeCategory" name="includeCategory" class="selectpicker" multiple title="">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_CATEGORY')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <select id="detail_excludeCategory" name="excludeCategory" class="selectpicker" multiple title="">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_CATEGORY')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">库位ABC限制</label>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <label class="pull-left">包含库位ABC</label>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <label class="pull-left">不包含库位ABC</label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <input id="detail_isAbcRestrict" name="isAbcRestrict" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="locAbcChange(this.checked)"/>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <select id="detail_includeAbc" name="includeAbc" class="selectpicker" multiple title="">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_ABC')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <select id="detail_excludeAbc" name="excludeAbc" class="selectpicker" multiple title="">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_ABC')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                                <div id="effectiveRule" class="tab-pane fade">
                                    <table class="bq-table">
                                        <tbody>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">包装限制</label>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">不满箱</label>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">满箱不满托</label>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">满托</label>
                                                </td>
                                                <td class="pad" width="20%">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <input id="detail_isPackageRestrict" name="isPackageRestrict" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="packChange(this.checked)"/>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <input id="detail_isLessCs" name="isLessCs" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isEnableChange(this.checked, '#detail_isLessCs')"/>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <input id="detail_isMoreCsLessPl" name="isMoreCsLessPl" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isEnableChange(this.checked, '#detail_isMoreCsLessPl')"/>
                                                </td>
                                                <td class="pad" width="20%">
                                                    <input id="detail_isMorePl" name="isMorePl" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isEnableChange(this.checked, '#detail_isMorePl')"/>
                                                </td>
                                                <td class="pad" width="20%">
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">入库单类型限制</label>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <label class="pull-left">包含入库单类型</label>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <label class="pull-left">不包含入库单类型</label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <input id="detail_isAsnTypeRestrict" name="isAsnTypeRestrict" type="checkbox" htmlEscape="false" value="Y" class="myCheckbox" onclick="asnTypeChange(this.checked)"/>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <select id="detail_includeAsnType" name="includeAsnType" class="selectpicker" multiple title="">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_ASN_TYPE')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <select id="detail_excludeAsnType" name="excludeAsnType" class="selectpicker" multiple title="">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_ASN_TYPE')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <label class="pull-left">批次属性限制</label>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <label class="pull-left">品质等于</label>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <label class="pull-left">品质不等于</label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                    <input id="detail_isLotAttRestrict" name="isLotAttRestrict" type="checkbox" htmlEscape="false" value="Y" class="myCheckbox" onclick="lotAttChange(this.checked)"/>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <select id="detail_lotAtt04Equal" name="lotAtt04Equal" class="selectpicker" multiple title="">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_QUALIFY')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <select id="detail_lotAtt04Unequal" name="lotAtt04Unequal" class="selectpicker" multiple title="">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_QUALIFY')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <label class="pull-left">批次属性05等于</label>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <label class="pull-left">批次属性05不等于</label>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="pad" width="20%">
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <input id="detail_lotAtt05Equal" name="lotAtt05Equal" htmlEscape="false" class="form-control" maxlength="64"/>
                                                </td>
                                                <td class="pad" width="20%" colspan="2">
                                                    <input id="detail_lotAtt05Unequal" name="lotAtt05Unequal" htmlEscape="false" class="form-control" maxlength="64"/>
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