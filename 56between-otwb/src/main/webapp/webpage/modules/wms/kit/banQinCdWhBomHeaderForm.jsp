<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>组合件管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript">
        var bomDetail_currentRow;
        var bomStep_currentRow;
        var bomDetail_isShowTab = false;
        var bomStep_isShowTab = false;
        $(document).ready(function () {
            // 初始化
            init();
            // 初始化子件明细Table
            initBomDetailTab();
            // 初始化加工工序Table
            initBomStepTab();
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
         * 初始化
         */
        function init() {
            if (!$("#id").val()) {
                $("#orgId").val(jp.getCurrentOrg().orgId);
                $('#btn_bomDetail_add').attr('disabled', true);
                $('#btn_bomStep_add').attr('disabled', true);
            }
        }

        /**
         * 显示右边tab
         */
        function showTabRight(obj1, obj2) {
            $(obj1).show();
            $(obj2).addClass("div-left");
        }

        /**
         * 隐藏右边tab
         */
        function hideTabRight(obj1, obj2) {
            $(obj1).hide();
            $(obj2).removeClass("div-left");
        }

        function ownerAfterSelect() {
            $("#parentSkuCode").val('');
            $("#parentSkuName").val('');
        }

        function qtyChange() {
            var $qty = $("#bomDetail_qty");
            var $uomQty = $("#bomDetail_uomQty");
            if (!$qty.val()) {
                return;
            }
            var qty = math.bignumber($qty.val());
            var uomQty = math.bignumber(!$uomQty.val() ? 1 : $uomQty.val());
            $("#bomDetail_qtyEa").val(qty.mul(uomQty));
        }

        /**************************BOM*****************************************/

        function saveBomHeader() {
            jp.loading();
            var validate = bq.headerSubmitCheck("#inputForm");
            if (validate.isSuccess) {
                var disabledObjs = bq.openDisabled("#inputForm");
                var params = $('#inputForm').bq_serialize();
                bq.closeDisabled(disabledObjs);
                jp.post("${ctx}/wms/kit/banQinCdWhBomHeader/save", params, function (data) {
                    if (data.success) {
                        window.location = "${ctx}/wms/kit/banQinCdWhBomHeader/form?id=" + data.body.entity.id;
                        jp.success(data.msg);
                    } else {
                        jp.error(data.msg);
                    }
                });
                return true;
            }
            jp.bqError(validate.msg);
        }

        /************************** 子件明细 *****************************************/
        /**
         * 初始化子件明细table
         */
        function initBomDetailTab() {
            $('#bomDetailTable').bootstrapTable({
                cache: false,// 是否使用缓存
                sidePagination: "server",// client客户端分页，server服务端分页
                url: "${ctx}/wms/kit/banQinCdWhBomDetail/data",
                queryParams: function (params) {
                    var searchParam = {};
                    searchParam.orgId = $('#orgId').val() ? $('#orgId').val() : '#';
                    searchParam.headerId = $('#id').val() ? $('#id').val() : '#';
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                onClickRow: function (row, $el) {
                    bomDetailClick(row);
                    jp.changeTableStyle($el);
                },
                onDblClickRow: function (row, $el) {
                    bomDetailDbClick(row);
                },
                columns: [{
                    checkbox: true
                }, {
                    field: 'lineNo',
                    title: '行号',
                    sortable: true
                }, {
                    field: 'subSkuCode',
                    title: '子件编码',
                    sortable: true
                }, {
                    field: 'subSkuName',
                    title: '子件名称',
                    sortable: true
                }, {
                    field: 'subSkuType',
                    title: '子件类型',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SUB_SKU_TYPE'))}, value, "-");
                    }
                }, {
                    field: 'packDesc',
                    title: '包装规格',
                    sortable: true
                }, {
                    field: 'uom',
                    title: '包装单位',
                    sortable: true
                }, {
                    field: 'qty',
                    title: '数量',
                    sortable: true
                }, {
                    field: 'qtyEa',
                    title: 'EA数量',
                    sortable: true
                }]
            });
        }

        /**
         * 子件明细行单击事件
         * @param tab tab页名称
         * @param row 当前行
         */
        function bomDetailClick(row) {
            bomDetail_currentRow = row;
            if (bomDetail_isShowTab) {
                // 表单赋值
                evaluate('bomDetail', 'bomDetail_currentRow');
            }
        }

        /**
         * 子件明细行双击事件
         * @param row 当前行
         */
        function bomDetailDbClick(row) {
            bomDetail_currentRow = row;
            if (!bomDetail_isShowTab) {
                // 显示右边tab
                showTabRight('#bomDetail_tab-right', '#bomDetail_tab-left');
                bomDetail_isShowTab = true;
                // 表单赋值
                evaluate('bomDetail', 'bomDetail_currentRow');
            } else {
                // 隐藏右边tab
                hideTabRight('#bomDetail_tab-right', '#bomDetail_tab-left');
                bomDetail_isShowTab = false;
            }
            $("#bomDetailTable").bootstrapTable('resetView');
        }

        /**
         * 赋值
         */
        function evaluate(prefix, currentRow) {
            $("input[id^=" + prefix + "]").each(function () {
                var $Id = $(this).attr('id');
                var $Name = $(this).attr('name');
                $('#' + $Id).val(eval("" + currentRow + "." + $Name));
            });
            $("select[id^=" + prefix + "]").each(function () {
                var $Id = $(this).attr('id');
                var $Name = $(this).attr('name');
                $('#' + $Id).val(eval("" + currentRow + "." + $Name));
            });
        }

        function setBomDetailFormData(row) {
            for (var field in row) {
                $("#bomDetail_" + field).val(row[field]);
            }
        }

        function addBomDetail() {
            // 显示右边Tab
            showTabRight('#bomDetail_tab-right', '#bomDetail_tab-left');
            bomDetail_isShowTab = true;
            // 清空表单
            $(':input', '#bomDetailForm').val('');
            $("#bomDetail_ownerCode").val($("#ownerCode").val());
            $("#bomDetail_parentSkuCode").val($("#parentSkuCode").val());
            $("#bomDetail_kitType").val($("#kitType").val());
            $("#bomDetail_orgId").val($("#orgId").val());
            $("#bomDetail_headerId").val($("#id").val());
        }

        function saveBomDetail() {
            if (!$('#id').val()) {
                jp.warning("请先保存单头");
                return;
            }

            if (!bomDetail_isShowTab) {
                jp.warning("当前无保存记录");
                return;
            }
            jp.loading();
            var validate = bq.detailSubmitCheck("#bomDetailForm");
            if (validate.isSuccess) {
                var disabledObjs = bq.openDisabled("#bomDetailForm");
                var params = $('#bomDetailForm').bq_serialize();
                bq.closeDisabled(disabledObjs);
                jp.post("${ctx}/wms/kit/banQinCdWhBomDetail/save", params, function (data) {
                    if (data.success) {
                        hideTabRight('#bomDetail_tab-right', '#bomDetail_tab-left');
                        bomDetail_isShowTab = false;
                        $('#bomDetailTable').bootstrapTable('refresh');
                        jp.success(data.msg);
                    } else {
                        jp.bqError(data.msg);
                    }
                });
                return true;
            }
            jp.bqError(validate.msg);
        }

        function delBomDetail() {
            var ids = $.map($('#bomDetailTable').bootstrapTable('getSelections'), function (row) {
                return row.id;
            });
            if (ids.length === 0) {
                jp.bqError("请选择一条记录");
                return;
            }
            jp.confirm("确认要删除吗？删除将不能恢复", function () {
                jp.loading();
                jp.get("${ctx}/wms/kit/banQinCdWhBomDetail/deleteAll?ids=" + ids, function (data) {
                    if (data.success) {
                        hideTabRight('#bomDetail_tab-right', '#bomDetail_tab-left');
                        bomDetail_isShowTab = false;
                        $('#bomDetailTable').bootstrapTable('refresh');
                        jp.success("删除成功");
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            })
        }

        /************************** 加工工序 *****************************************/
        /**
         * 初始化加工工序明细table
         */
        function initBomStepTab() {
            $('#bomStepTable').bootstrapTable({
                cache: false,// 是否使用缓存
                sidePagination: "server",// client客户端分页，server服务端分页
                url: "${ctx}/wms/kit/banQinCdWhBomStep/data",
                queryParams: function (params) {
                    var searchParam = {};
                    searchParam.orgId = $('#orgId').val() ? $('#orgId').val() : '#';
                    searchParam.headerId = $('#id').val() ? $('#id').val() : '#';
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                onClickRow: function (row, $el) {
                    bomStepClick(row);
                    jp.changeTableStyle($el);
                },
                onDblClickRow: function (row, $el) {
                    bomStepDbClick(row);
                },
                columns: [{
                    checkbox: true
                }, {
                    field: 'lineNo',
                    title: '行号',
                    sortable: true
                }, {
                    field: 'step',
                    title: '工序描述',
                    sortable: true
                }, {
                    field: 'def1',
                    title: '自定义1',
                    sortable: true
                }, {
                    field: 'def2',
                    title: '自定义2',
                    sortable: true
                }, {
                    field: 'def3',
                    title: '自定义3',
                    sortable: true
                }, {
                    field: 'def4',
                    title: '自定义4',
                    sortable: true
                }, {
                    field: 'def5',
                    title: '自定义5',
                    sortable: true
                }]
            });
        }

        /**
         * 加工工序行单击事件
         * @param tab tab页名称
         * @param row 当前行
         */
        function bomStepClick(row) {
            bomStep_currentRow = row;
            if (bomStep_isShowTab) {
                // 表单赋值
                evaluate('bomStep', 'bomStep_currentRow');
            }
        }

        /**
         * 加工工序行双击事件
         * @param row 当前行
         */
        function bomStepDbClick(row) {
            bomStep_currentRow = row;
            if (!bomStep_isShowTab) {
                // 显示右边tab
                showTabRight('#bomStep_tab-right', '#bomStep_tab-left');
                bomStep_isShowTab = true;
                // 表单赋值
                evaluate('bomStep', 'bomStep_currentRow');
            } else {
                // 隐藏右边tab
                hideTabRight('#bomStep_tab-right', '#bomStep_tab-left');
                bomStep_isShowTab = false;
            }
            $("#bomStepTable").bootstrapTable('resetView');
        }

        function setBomStepFormData(row) {
            for (var field in row) {
                $("#bomStep_" + field).val(row[field]);
            }
        }

        function addBomStep() {
            // 显示右边Tab
            showTabRight('#bomStep_tab-right', '#bomStep_tab-left');
            bomStep_isShowTab = true;
            // 清空表单
            $(':input', '#bomStepForm').val('');
            $("#bomStep_ownerCode").val($("#ownerCode").val());
            $("#bomStep_parentSkuCode").val($("#parentSkuCode").val());
            $("#bomStep_kitType").val($("#kitType").val());
            $("#bomStep_orgId").val($("#orgId").val());
            $("#bomStep_headerId").val($("#id").val());
        }

        function saveBomStep() {
            if (!bomStep_isShowTab) {
                jp.warning("当前无保存记录");
                return;
            }
            jp.loading();
            var validate = bq.detailSubmitCheck("#bomStepForm");
            if (validate.isSuccess) {
                var disabledObjs = bq.openDisabled("#bomStepForm");
                var params = $('#bomStepForm').bq_serialize();
                bq.closeDisabled(disabledObjs);
                jp.post("${ctx}/wms/kit/banQinCdWhBomStep/save", params, function (data) {
                    if (data.success) {
                        hideTabRight('#bomStep_tab-right', '#bomStep_tab-left');
                        bomStep_isShowTab = false;
                        $("#bomStepTable").bootstrapTable('refresh');
                        jp.success(data.msg);
                    } else {
                        jp.error(data.msg);
                    }
                });
                return true;
            }
            jp.bqError(validate.msg);
        }

        function delBomStep() {
            var ids = $.map($('#bomStepTable').bootstrapTable('getSelections'), function (row) {
                return row.id;
            }).join(',');
            if (ids.length === 0) {
                jp.bqError("请选择一条记录");
                return;
            }
            jp.confirm("确认要删除吗？删除将不能恢复", function () {
                jp.loading();
                jp.get("${ctx}/wms/kit/banQinCdWhBomStep/deleteAll?ids=" + ids, function (data) {
                    if (data.success) {
                        hideTabRight('#bomStep_tab-right', '#bomStep_tab-left');
                        bomStep_isShowTab = false;
                        $("#bomStepTable").bootstrapTable('refresh');
                        jp.success("删除成功");
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            })
        }

    </script>
</head>
<body>
<div id="bomHeaderToolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="kit:banQinCdWhBomHeader:edit">
        <a class="btn btn-primary" id="btn_bomHeader_save" onclick="saveBomHeader()">保存</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="banQinCdWhBomEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-10"><label class="pull-right asterisk">货主编码</label></td>
                <td class="width-15">
                    <input id="ownerType" value="OWNER" type="hidden">
                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control required"
                                   fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                   displayFieldId="ownerCode" displayFieldName="ownerCode" displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinCdWhBomEntity.ownerCode}"
                                   selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                   queryParams="ebcuType" queryParamValues="ownerType"
                                   concatId="ownerName" concatName="ebcuNameCn" afterSelect="ownerAfterSelect">
                    </sys:popSelect>
                </td>
                <td class="width-10"><label class="pull-right">货主名称</label></td>
                <td class="width-15">
                    <form:input path="ownerName" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right asterisk">父件编码</label></td>
                <td class="width-15">
                    <input id="isParent" value="Y" type="hidden">
                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control required"
                                   fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                   displayFieldId="parentSkuCode" displayFieldName="parentSkuCode" displayFieldKeyName="skuCode" displayFieldValue="${banQinCdWhBomEntity.parentSkuCode}"
                                   selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                   fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                   searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName"
                                   concatId="parentSkuName" concatName="skuName"
                                   queryParams="ownerCode|isParent" queryParamValues="ownerCode|isParent">
                    </sys:popSelect>
                </td>
                <td class="width-10"><label class="pull-right">父件名称</label></td>
                <td class="width-15">
                    <form:input path="parentSkuName" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right asterisk">加工类型</label></td>
                <td class="width-15">
                    <form:select path="kitType" class="form-control required">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('SYS_WM_KIT_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</form:form>
<div class="tabs-container">
    <ul id="detailTab" class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#bomDetailInfo" aria-expanded="true">子件明细</a></li>
        <li class=""><a data-toggle="tab" href="#bomStepInfo" aria-expanded="true">加工工序</a></li>
    </ul>
    <div class="tab-content">
        <div id="bomDetailInfo" class="tab-pane fade in active">
            <div id="bomDetailToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="kit:banQinCdWhBomHeader:add">
                    <a class="btn btn-primary" id="btn_bomDetail_add" onclick="addBomDetail()">新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinCdWhBomHeader:edit">
                    <a class="btn btn-primary" id="btn_bomDetail_save" onclick="saveBomDetail()">保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinCdWhBomHeader:del">
                    <a class="btn btn-danger" id="btn_bomDetail_del" onclick="delBomDetail()">删除</a>
                </shiro:hasPermission>
            </div>
            <div id="bomDetail_tab-left">
                <table id="bomDetailTable" class="table text-nowrap"></table>
            </div>
            <div id="bomDetail_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="bomDetailForm" method="post" class="form">
                    <input id="bomDetail_id" name="id" type="hidden"/>
                    <input id="bomDetail_orgId" name="orgId" type="hidden"/>
                    <input id="bomDetail_headerId" name="headerId" type="hidden"/>
                    <input id="bomDetail_ownerCode" name="ownerCode" type="hidden"/>
                    <input id="bomDetail_parentSkuCode" name="parentSkuCode" type="hidden"/>
                    <input id="bomDetail_kitType" name="kitType" type="hidden"/>
                    <input id="bomDetail_packCode" name="packCode" type="hidden"/>
                    <input id="bomDetail_uom" name="uom" type="hidden"/>
                    <input id="bomDetail_uomQty" name="uomQty" type="hidden"/>
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">行号</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">子件编码</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">子件名称</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">子件类型</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="bomDetail_lineNo" name="lineNo" class="form-control" readonly/>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/kit/banQinCdWhBomDetail/subSkuGrid" title="选择子件" cssClass="form-control required"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                               displayFieldId="bomDetail_subSkuCode" displayFieldName="subSkuCode" displayFieldKeyName="subSkuCode" displayFieldValue=""
                                               selectButtonId="subSkuSelectId" deleteButtonId="subSkuDeleteId"
                                               queryParams="ownerCode|parentSkuCode" queryParamValues="ownerCode|parentSkuCode"
                                               fieldLabels="商品编码|商品名称" fieldKeys="subSkuCode|subSkuName"
                                               searchLabels="商品编码|商品名称" searchKeys="subSkuCode|subSkuName"
                                               concatId="bomDetail_subSkuName,bomDetail_packCode,bomDetail_packDesc,bomDetail_uom,bomDetail_uomDesc,bomDetail_uomQty"
                                               concatName="subSkuName,packCode,packDesc,uom,uomDesc,uomQty" afterSelect="qtyChange">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input id="bomDetail_subSkuName" name="subSkuName" class="form-control" readonly/>
                            </td>
                            <td class="width-25">
                                <select id="bomDetail_subSkuType" name="subSkuType" class="form-control required">
                                    <option></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_SUB_SKU_TYPE')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left asterisk">包装规格</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">包装单位</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">数量</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">数量EA</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="bomDetail_packDesc" name="packDesc" class="form-control" readonly/>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                               displayFieldId="bomDetail_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                               selectButtonId="uomSelectId" deleteButtonId="uomDeleteId"
                                               queryParams="packCode" queryParamValues="bomDetail_packCode"
                                               fieldLabels="包装单位|数量|描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                               searchLabels="包装单位|描述" searchKeys="cdprUnitLevel|cdprDesc" inputSearchKey="codeAndName"
                                               concatId="bomDetail_uom,bomDetail_uomQty" concatName="cdprUnitLevel,cdprQuantity" afterSelect="qtyChange">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input id="bomDetail_qty" name="qty" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0);qtyChange()"/>
                            </td>
                            <td class="width-25">
                                <input id="bomDetail_qtyEa" name="qtyEa" class="form-control" readonly/>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25" colspan="4">
                                <label class="pull-left">备注</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25" colspan="4">
                                <input id="bomDetail_remarks" name="remarks" class="form-control"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form:form>
            </div>
        </div>
        <div id="bomStepInfo" class="tab-pane fade">
            <div id="bomStepToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="kit:banQinCdWhBomHeader:add">
                    <a class="btn btn-primary" id="btn_bomStep_add" onclick="addBomStep()">新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinCdWhBomHeader:edit">
                    <a class="btn btn-primary" id="btn_bomStep_save" onclick="saveBomStep()">保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="kit:banQinCdWhBomHeader:del">
                    <a class="btn btn-danger" id="btn_bomStep_del" onclick="delBomStep()">删除</a>
                </shiro:hasPermission>
            </div>
            <div id="bomStep_tab-left">
                <table id="bomStepTable" class="table text-nowrap"></table>
            </div>
            <div id="bomStep_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="bomStepForm" method="post" class="form">
                    <input id="bomStep_id" name="id" type="hidden"/>
                    <input id="bomStep_orgId" name="orgId" type="hidden"/>
                    <input id="bomStep_headerId" name="headerId" type="hidden"/>
                    <input id="bomStep_ownerCode" name="ownerCode" type="hidden"/>
                    <input id="bomStep_parentSkuCode" name="parentSkuCode" type="hidden"/>
                    <input id="bomStep_kitType" name="kitType" type="hidden"/>
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">行号</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">工序描述</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">自定义1</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">自定义2</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="bomStep_lineNo" name="lineNo" class="form-control" readonly/>
                            </td>
                            <td class="width-25">
                                <input id="bomStep_step" name="step" class="form-control required" maxlength="64"/>
                            </td>
                            <td class="width-25">
                                <input id="bomStep_def1" name="def1" class="form-control" maxlength="64"/>
                            </td>
                            <td class="width-25">
                                <input id="bomStep_def2" name="def2" class="form-control" maxlength="64"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">自定义3</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">自定义4</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">自定义5</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="bomStep_def3" name="def3" class="form-control" maxlength="64"/>
                            </td>
                            <td class="width-25">
                                <input id="bomStep_def4" name="def4" class="form-control" maxlength="64"/>
                            </td>
                            <td class="width-25">
                                <input id="bomStep_def5" name="def5" class="form-control" maxlength="64"/>
                            </td>
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