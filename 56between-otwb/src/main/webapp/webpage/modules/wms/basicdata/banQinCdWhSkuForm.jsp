<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>商品管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript">
        var currentRow; // 明细当前行
        var isShowTab = false; // 是否显示右边tab页
        $(document).ready(function () {
            // 初始化
            init();
            // 初始化明细table
            initCdWhSkuLocTable();
            // 初始化条码table
            initBarcodeTable();
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
            var isValidate = bq.headerSubmitCheck('#inputForm');
            if (isValidate.isSuccess) {
                jp.loading();
                var disabledObjs = bq.openDisabled('#inputForm');
                var params = $('#inputForm').bq_serialize();
                bq.closeDisabled(disabledObjs);
                jp.post("${ctx}/wms/basicdata/banQinCdWhSku/save", params, function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        window.location = "${ctx}/wms/basicdata/banQinCdWhSku/form?id=" + data.body.entity.id;
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
                $('#orgId').val(jp.getCurrentOrg().orgId);
                $('#addDetail').attr('disabled', true);
                // 是否温控
                isColdControl(false);
                // 是否危险品
                isDgControl(false);
                // 是否做效期控制
                isValidityControl(false);
                // 是否允许超收控制
                isOverRcvControl(false);
                // 是否质检管理控制
                isQcControl(false);
                // 是否序列号控制
                isSerialControl(false);
                isUnSerialControl(false);
                // 是否父件控制
                isParentControl(false);
                // 是否启用控制
                isEnableControl(true);
            } else {
                $('#ownerSelectId').prop('disabled', true);
                $('#ownerDeleteId').prop('disabled', true);
                $('#ownerCode').prop('readonly', true);
                $('#ownerName').prop('readonly', true);
                $('#skuCode').prop('readonly', true);
                // 是否温控
                var isCold = '${banQinCdWhSkuEntity.isCold}';
                $('#isCold').prop('checked', isCold === 'Y');
                isColdControl(isCold === 'Y');
                // 是否危险品
                var isDg = '${banQinCdWhSkuEntity.isDg}';
                $('#isDg').prop('checked', isDg === 'Y');
                isDgControl(isDg === 'Y');
                // 是否做效期控制
                var isValidity = '${banQinCdWhSkuEntity.isValidity}';
                $('#isValidity').prop('checked', isValidity === 'Y');
                isValidityControl(isValidity === 'Y');
                // 是否允许超收
                var isOverRcv = '${banQinCdWhSkuEntity.isOverRcv}';
                $('#isOverRcv').prop('checked', isOverRcv === 'Y');
                isOverRcvControl(isOverRcv === 'Y');
                // 是否质检管理
                var isQc = '${banQinCdWhSkuEntity.isQc}';
                $('#isQc').prop('checked', isQc === 'Y');
                isQcControl(isQc === 'Y');
                // 是否序列号管理
                $('#isSerial').prop('checked', '${banQinCdWhSkuEntity.isSerial}' === 'Y').val('${banQinCdWhSkuEntity.isSerial}');
                $('#isUnSerial').prop('checked', '${banQinCdWhSkuEntity.isUnSerial}' === 'Y').val('${banQinCdWhSkuEntity.isUnSerial}');
                // 是否父件
                $('#isParent').prop('checked', '${banQinCdWhSkuEntity.isParent}' === 'Y').val('${banQinCdWhSkuEntity.isParent}');
                // 是否启用
                $('#isEnable').prop('checked', '${banQinCdWhSkuEntity.isEnable}' === 'Y').val('${banQinCdWhSkuEntity.isEnable}');
            }
            // 公共信息
            $('#effectiveDate').datetimepicker({format: "YYYY-MM-DD"});
            $('#expirationDate').datetimepicker({format: "YYYY-MM-DD"});
        }
        
        /**
         * 是否温控控制
         */ 
        function isColdControl(flag) {
            $('#isCold').val(flag ? "Y" : "N");
            $('#minTemp').prop('readonly', !flag);
            $('#maxTemp').prop('readonly', !flag);
        }

        /**
         * 是否危险品控制
         */
        function isDgControl(flag) {
            $('#isDg').val(flag ? "Y" : "N");
            $('#dgClass').prop('disabled', !flag);
            $('#unno').prop('readonly', !flag);
        }
        
        /**
         * 是否做效期控制
         */ 
        function isValidityControl(flag) {
            $('#isValidity').val(flag ? "Y" : "N");
            $('#lifeType').prop('disabled', !flag);
            $('#inLifeDays').prop('readonly', !flag);
            $('#outLifeDays').prop('readonly', !flag);
        }
        
        /**
         * 是否允许超收控制
         */ 
        function isOverRcvControl(flag) {
            $('#isOverRcv').val(flag ? "Y" : "N");
            $('#overRcvPct').prop('readonly', !flag);
        }
        
        /**
         * 是否序列号管理
         */ 
        function isSerialControl(flag) {
            $('#isSerial').val(flag ? "Y" : "N");
            if (flag) {
                $('#isUnSerial').prop('checked', false).val('N');
            }
        }
        
        function isUnSerialControl(flag) {
            $('#isUnSerial').val(flag ? "Y" : "N");
            if (flag) {
                $('#isSerial').prop('checked', false).val('N');
            }
        }
        
        /**
         * 是否质检管理控制
         */
        function isQcControl(flag) {
            $('#isQc').val(flag ? "Y" : "N");
            $('#qcPhase').prop('disabled', !flag);
            $('#qcRuleSelectId').prop('disabled', !flag);
            $('#qcRuleDeleteId').prop('disabled', !flag);
            $('#qcRule').prop('readonly', !flag);
            $('#qcRuleName').prop('readonly', !flag);
            $('#itemGroupSelectId').prop('disabled', !flag);
            $('#itemGroupDeleteId').prop('disabled', !flag);
            $('#itemGroupCode').prop('readonly', !flag);
            $('#itemGroupName').prop('readonly', !flag);
            if (flag) {
                $('#qcPhase').addClass('required');
                $('#qcRuleName').addClass('required');
                $('#itemGroupName').addClass('required');
                $('#qcPhaseLabel').html(getStartStyle() + $('#qcPhaseLabel').text());
                $('#qcRuleLabel').html(getStartStyle() + $('#qcRuleLabel').text());
                $('#itemGroupLabel').html(getStartStyle() + $('#itemGroupLabel').text());
            } else {
                $('#qcPhase').removeClass('required');
                $('#qcRuleName').removeClass('required');
                $('#itemGroupName').removeClass('required');
                $('.myStart').remove();
            }
        }
        
        function getStartStyle() {
            return '<font class="myStart" color="red">*</font>';
        }
        
        /**
         * 是否父件控制
         */ 
        function isParentControl(flag) {
            $('#isParent').val(flag ? "Y" : "N");
        }
        
        /**
         * 是否可用控制
         */ 
        function isEnableControl(flag) {
            $('#isEnable').val(flag ? "Y" : "N");
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
        function initCdWhSkuLocTable() {
            $('#cdWhSkuLocTable').bootstrapTable({
                cache: false,// 是否使用缓存
                sidePagination: "server",// client客户端分页，server服务端分页
                url: "${ctx}/wms/basicdata/banQinCdWhSkuLoc/data",
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
                    jp.changeTableStyle($el);
                    clickDetail(row);
                },
                onDblClickRow: function (row, $el) {
                    dbClickDetail(row);
                },
                columns: [{
                    checkbox: true
                }, {
                    field: 'locCode',
                    title: '拣货位',
                    sortable: true
                }, {
                    field: 'skuLocType',
                    title: '商品拣货位类型',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SKU_LOC_TYPE'))}, value, "-");
                    }
                }, {
                    field: 'maxLimit',
                    title: '库存上限',
                    sortable: true
                }, {
                    field: 'minLimit',
                    title: '库存下限',
                    sortable: true
                }, {
                    field: 'minRp',
                    title: '最小补货数',
                    sortable: true
                }, {
                    field: 'rpUom',
                    title: '最小补货单位',
                    sortable: true
                }, {
                    field: 'isOverAlloc',
                    title: '是否超量分配',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
                    }
                }, {
                    field: 'isRpAlloc',
                    title: '是否补被占用库存',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
                    }
                }, {
                    field: 'isOverRp',
                    title: '是否超量补货',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
                    }
                }, {
                    field: 'isFmRs',
                    title: '是否从存储位补货',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
                    }
                }, {
                    field: 'isFmCs',
                    title: '是否从箱拣货位补货',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
                    }
                }]
            });
        }

        /**
         * 初始化条码table
         */
        function initBarcodeTable() {
            $('#barcodeList').empty();
            var params = "headerId=" + ($('#id').val() ? $('#id').val() : '#');
            params += "&orgId=" + ($('#orgId').val() ? $('#orgId').val() : jp.getCurrentOrg.orgId);
            jp.post("${ctx}/wms/basicdata/banQinCdWhSkuBarcode/data?" + params, null, function (data) {
                barcodeRowIdx = 0;
                for (var i = 0; i < data.length; i++) {
                    addBarcodeRow('#barcodeList', barcodeRowIdx, barcodeTpl, data[i]);
                    barcodeRowIdx = barcodeRowIdx + 1;
                }
            })
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
            $("#cdWhSkuLocTable").bootstrapTable('resetView');
        }

        /**
         * 赋值
         */
        function evaluate() {
            $("input[id^=detail]").each(function() {
                var $Id = $(this).attr('id');
                var $Name = $(this).attr('name');
                $('#' + $Id).val(eval("currentRow." + $Name));
                if ($Id === 'detail_isOverAlloc' || $Id === 'detail_isRpAlloc'
                    || $Id === 'detail_isOverRp' || $Id === 'detail_isFmRs'
                    || $Id === 'detail_isFmCs') {
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
         * 新增明细
         */
        function addDetail() {
            // 显示右边Tab
            showTabRight();
            // 清空表单
            $(':input', '#inputForm1').val('');
            // 初始化checkbox
            $('#detail_isOverAlloc').val('N');
            $('#detail_isRpAlloc').val('N');
            $('#detail_isOverRp').val('N');
            $('#detail_isFmRs').val('N');
            $('#detail_isFmCs').val('N');
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
            var validate = bq.detailSubmitCheck('#inputForm1');
            if (validate.isSuccess) {
                var disabledObjs = bq.openDisabled('#inputForm1');
                // 保存前赋值
                beforeSave();
                var params = $('#inputForm1').bq_serialize();
                bq.closeDisabled(disabledObjs);
                jp.post("${ctx}/wms/basicdata/banQinCdWhSkuLoc/save", params, function (data) {
                    if (data.success) {
                        $('#cdWhSkuLocTable').bootstrapTable('refresh');
                        hideTabRight();
                        jp.success(data.msg);
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            } else {
                jp.bqError(validate.msg);
            }
        }

        /**
         * 保存前赋值
         */
        function beforeSave() {
            if (!$('#detail_id').val()) {
                $('#detail_orgId').val($('#orgId').val());
                $('#detail_ownerCode').val($('#ownerCode').val());
                $('#detail_skuCode').val($('#skuCode').val());
                $('#detail_headerId').val($('#id').val());
            }
        }

        /**
         * 获取表格勾选行Ids
         */
        function getIdSelections() {
            return $.map($('#cdWhSkuLocTable').bootstrapTable('getSelections'), function (row) {
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
                jp.loading();
                jp.get("${ctx}/wms/basicdata/banQinCdWhSkuLoc/deleteAll?ids=" + rowIds, function (data) {
                    if (data.success) {
                        $('#cdWhSkuLocTable').bootstrapTable('refresh');
                        hideTabRight();
                        jp.success(data.msg);
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            })
        }

        /**
         * 明细checkbox事件
         */
        function detailChange(flag, obj) {
            $(obj).val(flag ? 'Y' : 'N');
        }

        /**
         * 计算体积
         */
        function calcVolume() {
            var length = $('#length').val();
            var width = $('#width').val();
            var height = $('#height').val();
            if (length && width && height && (!(parseFloat(length).toString() === 'NaN') && !(parseFloat(width).toString() === 'NaN') && !(parseFloat(height).toString() === 'NaN'))) {
                $('#cubic').val(parseFloat(length).mul(parseFloat(width)).mul(parseFloat(height)));
            }
        }
        
        function addBarcode() {
            addBarcodeRow('#barcodeList', barcodeRowIdx, barcodeTpl);
            barcodeRowIdx = barcodeRowIdx + 1;
        }

        function addBarcodeRow(list, idx, tpl, row) {
            if (!row) {
                tpl = tpl.replace("{{row.orgId}}", $('#orgId').val());
                tpl = tpl.replace("{{row.headerId}}", $('#id').val());
                tpl = tpl.replace("{{row.ownerCode}}", $('#ownerCode').val());
                tpl = tpl.replace("{{row.skuCode}}", $('#skuCode').val());
            }
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            if (row) {
                $(list + idx + '_isDefault').prop('checked', row.isDefault === 'Y').val(row.isDefault);
            } else {
                $(list + idx + '_isDefault').prop('checked', false).val('N');
            }
        }

        /**
         * 保存条码
         */
        function saveBarCode() {
            if (!$('#id').val()) {
                jp.warning('请先保存单头!');
                return;
            }

            // 校验数据
            if (!checkBarcode()) {
                return;
            }
            jp.loading();
            jp.post("${ctx}/wms/basicdata/banQinCdWhSkuBarcode/save", $('#inputForm2').bq_serialize(), function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    initBarcodeTable();
                } else {
                    jp.bqError(data.msg);
                }
            })
        }

        /**
         * 校验条码
         */
        function checkBarcode() {
            var flag = true;
            var message = '';
            var dataArray = $('#inputForm2').serializeArray();
            for (var i in dataArray) {
                if (dataArray[i].name.indexOf('.barcode') !== -1 && !dataArray[i].value) {
                    flag = false;
                    message += '条码不能为空!<br>';
                }
            }
            if (!flag) {
                jp.warning(message);
            }
            return flag;
        }
        
        function removeBarcode() {
            var ids = [], idRows = [];
            $("input[name='barcodeSelect']:checked").each(function() {
                var idx = $(this).prop('id').split('_')[0];
                ids.push($('#' + idx + '_id').val());
                idRows.push(idx + '_id');
            });
            if (ids.length === 0) {
                jp.warning("请勾选记录");
                return;
            }
            jp.loading();
            jp.get("${ctx}/wms/basicdata/banQinCdWhSkuBarcode/deleteAll?ids=" + ids.join(","), function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    for (var i in idRows) {
                        $('#' + idRows[i]).parent().parent().remove();
                    }
                    barcodeRowIdx -= ids.length;
                } else {
                    jp.bqError(data.msg);
                }
            })
        }

        function barcodeSelectChange(flag) {
            $("input[name='barcodeSelect']:checkbox").prop('checked', flag);
        }

        /**
         * 是否默认
         * @param idx
         */
        function isDefaultChange(idx) {
            $('#barcodeList').find("input[type='checkbox']").each(function () {
                if ($(this).prop('name').indexOf('isDefault') !== -1) {
                    $(this).prop('checked', false).val('N');
                }
            });
            $('#barcodeList' + idx + '_isDefault').prop('checked', true).val('Y');
        }
    </script>
</head>
<body>
<div style="width: 100%;">
    <div id="toolbar" style="padding-left: 10px;">
        <shiro:hasPermission name="basicdata:banQinCdWhSku:edit">
            <a class="btn btn-primary btn-sm" id="header_save" onclick="save()">保存</a>
        </shiro:hasPermission>
    </div>
    <form:form id="inputForm" modelAttribute="banQinCdWhSkuEntity" method="post">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="orgId"/>
    <div class="tabs-container" style="height: 300px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#baseInfo" aria-expanded="true">基础信息</a></li>
            <li class=""><a data-toggle="tab" href="#skuRule" aria-expanded="true">商品规则</a></li>
            <li class=""><a data-toggle="tab" href="#commonInfo" aria-expanded="true">公共信息</a></li>
            <li class=""><a data-toggle="tab" href="#warehouseControlInfo" aria-expanded="true">仓库控制信息</a></li>
            <li class=""><a data-toggle="tab" href="#reservedInfo" aria-expanded="true">预留信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="baseInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>货主</label></td>
                        <td class="" width="12%">
                            <input id="ebcuType" value="OWNER" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control required"
                                           fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="${banQinCdWhSkuEntity.ownerCode}" allowInput="true"
                                           displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${banQinCdWhSkuEntity.ownerName}"
                                           selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                           fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           queryParams="ebcuType" queryParamValues="ebcuType">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>商品编码</label></td>
                        <td class="" width="12%">
                            <form:input path="skuCode" htmlEscape="false" class="form-control required" maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>商品名称</label></td>
                        <td class="" width="12%">
                            <form:input path="skuName" htmlEscape="false" class="form-control required" maxlength="256"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">商品简称</label></td>
                        <td class="" width="12%">
                            <form:input path="shortName" htmlEscape="false" class="form-control " maxlength="128"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">商品外语名称</label></td>
                        <td class="" width="12%">
                            <form:input path="foreignName" htmlEscape="false" class="form-control" maxlength="256"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>包装规格</label></td>
                        <td class="" width="12%">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control required"
                                           fieldId="packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue="${banQinCdWhSkuEntity.packCode}" allowInput="true"
                                           displayFieldId="cdpaFormat" displayFieldName="cdpaFormat" displayFieldKeyName="cdpaFormat" displayFieldValue="${banQinCdWhSkuEntity.cdpaFormat}"
                                           selectButtonId="packSelectId" deleteButtonId="packDeleteId"
                                           fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                           searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" inputSearchKey="codeAndName"
                                           concatId="rcvUom,rcvUomName,shipUom,shipUomName,printUom,printUomName" concatName="unitCode,unitDesc,unitCode,unitDesc,unitCode,unitDesc">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right">商品条码</label></td>
                        <td class="" width="12%">
                            <form:input path="barCode" htmlEscape="false" class="form-control " maxlength="128"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">供应商商品条码</label></td>
                        <td class="" width="12%">
                            <form:input path="supBarCode" htmlEscape="false" class="form-control " maxlength="128"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">商品组</label></td>
                        <td class="" width="12%">
                            <form:input path="groupCode" htmlEscape="false" class="form-control " maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">商品分类</label></td>
                        <td class="" width="12%">
                            <sys:grid title="选择商品分类" url="${ctx}/wms/basic/skuClassification/grid" cssClass="form-control"
                                      fieldId="typeCode" fieldName="typeCode" fieldKeyName="code" fieldValue="${banQinCdWhSkuEntity.typeCode}"
                                      displayFieldId="typeName" displayFieldName="typeName"
                                      displayFieldKeyName="name" displayFieldValue="${banQinCdWhSkuEntity.typeName}"
                                      fieldLabels="编码|名称" fieldKeys="code|name" searchLabels="编码|名称" searchKeys="code|name"
                                      queryParams="orgId" queryParamValues="orgId"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">保质期(天数)</label></td>
                        <td class="" width="12%">
                            <form:input path="shelfLife" htmlEscape="false" class="form-control " onkeyup="value=value.replace(/[^\d]/g,'')"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">毛重</label></td>
                        <td class="" width="12%">
                            <form:input path="grossWeight" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 8, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">净重</label></td>
                        <td class="" width="12%">
                            <form:input path="netWeight" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 8, 0)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">体积</label></td>
                        <td class="" width="12%">
                            <form:input path="cubic" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 8, 0)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">商品形态</label></td>
                        <td class="" width="12%">
                            <form:select path="formCode" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_MATERIAL_MORPHOLOGY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="" width="8%"><label class="pull-right">长</label></td>
                        <td class="" width="12%">
                            <form:input path="length" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 8, 0);calcVolume()"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">宽</label></td>
                        <td class="" width="12%">
                            <form:input path="width" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 8, 0);calcVolume()"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">高</label></td>
                        <td class="" width="12%">
                            <form:input path="height" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 8, 0);calcVolume()"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">币别</label></td>
                        <td class="" width="12%">
                            <form:select path="stockCurId"  class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_CURRENCY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="" width="8%"><label class="pull-right">单价</label></td>
                        <td class="" width="12%">
                            <form:input path="price" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 8, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">款号</label></td>
                        <td class="" width="12%">
                            <form:input path="style" htmlEscape="false" class="form-control " maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">颜色</label></td>
                        <td class="" width="12%">
                            <form:input path="color" htmlEscape="false" class="form-control " maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">尺码</label></td>
                        <td class="" width="12%">
                            <form:input path="skuSize" htmlEscape="false" class="form-control " maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">HS编码</label></td>
                        <td class="" width="12%">
                            <form:input path="hsCode" htmlEscape="false" class="form-control " maxlength="32"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="skuRule" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>批次属性</label></td>
                        <td class="" width="12%">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLotHeader/grid" title="选择批次属性" cssClass="form-control required"
                                           fieldId="lotCode" fieldName="lotCode" fieldKeyName="lotCode" fieldValue="${banQinCdWhSkuEntity.lotCode}" allowInput="true"
                                           displayFieldId="lotName" displayFieldName="lotName" displayFieldKeyName="lotName" displayFieldValue="${banQinCdWhSkuEntity.lotName}"
                                           selectButtonId="lotSelectId" deleteButtonId="lotDeleteId"
                                           fieldLabels="批次属性编码|批次属性名称" fieldKeys="lotCode|lotName"
                                           searchLabels="批次属性编码|批次属性名称" searchKeys="lotCode|lotName" inputSearchKey="lotCodeAndName">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>上架库位指定规则</label></td>
                        <td class="" width="12%">
                            <form:select path="reserveCode" class="form-control m-b required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_RESERVE_CODE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>上架规则</label></td>
                        <td class="" width="12%">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRulePaHeader/grid" title="选择上架规则" cssClass="form-control required"
                                           fieldId="paRule" fieldName="paRule" fieldKeyName="ruleCode" fieldValue="${banQinCdWhSkuEntity.paRule}" allowInput="true"
                                           displayFieldId="paRuleName" displayFieldName="paRuleName" displayFieldKeyName="ruleName" displayFieldValue="${banQinCdWhSkuEntity.paRuleName}"
                                           selectButtonId="paRuleSelectId" deleteButtonId="paRuleDeleteId"
                                           fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                           searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right">上架库区</label></td>
                        <td class="" width="12%">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择上架库区" cssClass="form-control"
                                           fieldId="paZone" fieldName="paZone" fieldKeyName="zoneCode" fieldValue="${banQinCdWhSkuEntity.paZone}" allowInput="true"
                                           displayFieldId="paZoneName" displayFieldName="paZoneName" displayFieldKeyName="zoneName" displayFieldValue="${banQinCdWhSkuEntity.paZoneName}"
                                           selectButtonId="paZoneSelectId" deleteButtonId="paZoneDeleteId"
                                           fieldLabels="库区编码|库区名称" fieldKeys="zoneCode|zoneName"
                                           searchLabels="库区编码|库区名称" searchKeys="zoneCode|zoneName" inputSearchKey="zoneCodeAndName">
                            </sys:popSelect>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">上架库位</label></td>
                        <td class="" width="12%">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择上架库位" cssClass="form-control"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="paLoc" displayFieldName="paLoc" displayFieldKeyName="locCode" displayFieldValue="${banQinCdWhSkuEntity.paLoc}"
                                           selectButtonId="paLocSelectId" deleteButtonId="paLocDeleteId"
                                           fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                           searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>库存周转规则</label></td>
                        <td class="" width="12%">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRuleRotationHeader/grid" title="选择库存周转规则" cssClass="form-control required"
                                           fieldId="rotationRule" fieldName="rotationRule" fieldKeyName="ruleCode" fieldValue="${banQinCdWhSkuEntity.rotationRule}" allowInput="true"
                                           displayFieldId="rotationRuleName" displayFieldName="rotationRuleName" displayFieldKeyName="ruleName" displayFieldValue="${banQinCdWhSkuEntity.rotationRuleName}"
                                           selectButtonId="rotationRuleSelectId" deleteButtonId="rotationRuleDeleteId"
                                           fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                           searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>分配规则</label></td>
                        <td class="" width="12%">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRuleAllocHeader/grid" title="选择分配规则" cssClass="form-control required"
                                           fieldId="allocRule" fieldName="allocRule" fieldKeyName="ruleCode" fieldValue="${banQinCdWhSkuEntity.allocRule}" allowInput="true"
                                           displayFieldId="allocRuleName" displayFieldName="allocRuleName" displayFieldKeyName="ruleName" displayFieldValue="${banQinCdWhSkuEntity.allocRuleName}"
                                           selectButtonId="allocRuleSelectId" deleteButtonId="allocRuleDeleteId"
                                           fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                           searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right">循环级别</label></td>
                        <td class="" width="12%">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhCycle/grid" title="选择循环级别" cssClass="form-control"
                                           fieldId="cycleCode" fieldName="cycleCode" fieldKeyName="cycleCode" fieldValue="${banQinCdWhSkuEntity.cycleCode}" allowInput="true"
                                           displayFieldId="cycleName" displayFieldName="cycleName" displayFieldKeyName="cycleName" displayFieldValue="${banQinCdWhSkuEntity.cycleName}"
                                           selectButtonId="cycleSelectId" deleteButtonId="cycleDeleteId"
                                           fieldLabels="循环级别编码|循环级别名称" fieldKeys="cycleCode|cycleName"
                                           searchLabels="循环级别编码|循环级别名称" searchKeys="cycleCode|cycleName" inputSearchKey="cycleCodeAndName">
                            </sys:popSelect>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">上次循环盘点时间</label></td>
                        <td class="" width="12%">
                            <div class='input-group form_datetime' id='lastCountTime'>
                                <input name="lastCountTime" class="form-control" value="<fmt:formatDate value="${banQinCdWhSkuEntity.lastCountTime}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="commonInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">是否温控</label></td>
                        <td class="" width="12%">
                            <input id="isCold" name="isCold" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isColdControl(this.checked)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">最低温度</label></td>
                        <td class="" width="12%">
                            <form:input path="minTemp" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">最高温度</label></td>
                        <td class="" width="12%">
                            <form:input path="maxTemp" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">商品温层</label></td>
                        <td class="" width="12%">
                            <form:select path="tempLevel" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_TEMPR_CATEGORY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">闪点</label></td>
                        <td class="" width="12%">
                            <form:input path="flashPoint" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">燃点</label></td>
                        <td class="" width="12%">
                            <form:input path="burningPoint" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">是否危险品</label></td>
                        <td class="" width="12%">
                            <input id="isDg" name="isDg" type="checkbox" class="myCheckbox" onclick="isDgControl(this.checked)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">危险品等级</label></td>
                        <td class="" width="12%">
                            <form:select path="dgClass" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_DG_CLASS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">UNNO</label></td>
                        <td class="" width="12%">
                            <form:input path="unno" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">生效日期</label></td>
                        <td class="" width="12%">
                            <div class='input-group form_datetime' id='effectiveDate'>
                                <input name="effectiveDate" class="form-control" value="<fmt:formatDate value="${banQinCdWhSkuEntity.effectiveDate}" pattern="yyyy-MM-dd"/>"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </td>
                        <td class="" width="8%"><label class="pull-right">失效日期</label></td>
                        <td class="" width="12%">
                            <div class='input-group form_datetime' id='expirationDate'>
                                <input name="expirationDate" class="form-control" value="<fmt:formatDate value="${banQinCdWhSkuEntity.expirationDate}" pattern="yyyy-MM-dd"/>"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </td>
                        <td class="" width="8%"><label class="pull-right">应急电话</label></td>
                        <td class="" width="12%">
                            <form:input path="emergencyTel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">客户商品编码</label></td>
                        <td class="" width="12%">
                            <form:input path="skuCustomerCode" htmlEscape="false" class="form-control" maxlength="20"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">规格</label></td>
                        <td class="" width="12%">
                            <form:input path="spec" htmlEscape="false" class="form-control" maxlength="128"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">备注</label></td>
                        <td class="" width="12%">
                            <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="256"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="warehouseControlInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">是否做效期控制</label></td>
                        <td class="" width="12%">
                            <input id="isValidity" name="isValidity" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isValidityControl(this.checked)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">周期类型</label></td>
                        <td class="" width="12%">
                            <form:select path="lifeType" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_LIFE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="" width="8%"><label class="pull-right">入库效期(天数)</label></td>
                        <td class="" width="12%">
                            <form:input path="inLifeDays" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">出库效期(天数)</label></td>
                        <td class="" width="12%">
                            <form:input path="outLifeDays" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">商品ABC</label></td>
                        <td class="" width="12%">
                            <form:select path="abc" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_ABC')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="" width="8%"><label class="pull-right">是否允许超收</label></td>
                        <td class="" width="12%">
                            <input id="isOverRcv" name="isOverRcv" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isOverRcvControl(this.checked)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">超收百分比</label></td>
                        <td class="" width="12%">
                            <form:input path="overRcvPct" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>缺省收货单位</label></td>
                        <td class="" width="12%">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid" title="选择缺省收货单位" cssClass="form-control required"
                                           fieldId="rcvUom" fieldName="rcvUom" fieldKeyName="cdprUnitLevel" fieldValue="${banQinCdWhSkuEntity.rcvUom}" allowInput="true"
                                           displayFieldId="rcvUomName" displayFieldName="rcvUomName" displayFieldKeyName="cdprDesc" displayFieldValue="${banQinCdWhSkuEntity.rcvUomName}"
                                           selectButtonId="rcvUomSelectId" deleteButtonId="rcvUomDeleteId"
                                           fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                           searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                           queryParams="packCode" queryParamValues="packCode">
                            </sys:popSelect>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>缺省发货单位</label></td>
                        <td class="" width="12%">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid" title="选择缺省收货单位" cssClass="form-control required"
                                           fieldId="shipUom" fieldName="shipUom" fieldKeyName="cdprUnitLevel" fieldValue="${banQinCdWhSkuEntity.shipUom}" allowInput="true"
                                           displayFieldId="shipUomName" displayFieldName="shipUomName" displayFieldKeyName="cdprDesc" displayFieldValue="${banQinCdWhSkuEntity.shipUomName}"
                                           selectButtonId="shipUomSelectId" deleteButtonId="shipUomDeleteId"
                                           fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                           searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                           queryParams="packCode" queryParamValues="packCode">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>缺省打印单位</label></td>
                        <td class="" width="12%">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid" title="选择缺省收货单位" cssClass="form-control required"
                                           fieldId="printUom" fieldName="printUom" fieldKeyName="cdprUnitLevel" fieldValue="${banQinCdWhSkuEntity.printUom}" allowInput="true"
                                           displayFieldId="printUomName" displayFieldName="printUomName" displayFieldKeyName="cdprDesc" displayFieldValue="${banQinCdWhSkuEntity.printUomName}"
                                           selectButtonId="printUomSelectId" deleteButtonId="printUomDeleteId"
                                           fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                           searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                           queryParams="packCode" queryParamValues="packCode">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right">是否库存序列号管理</label></td>
                        <td class="" width="12%">
                            <input id="isSerial" name="isSerial" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isSerialControl(this.checked)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">库存上限EA</label></td>
                        <td class="" width="12%">
                            <form:input path="maxLimit" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">库存下限EA</label></td>
                        <td class="" width="12%">
                            <form:input path="minLimit" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">首次入库时间</label></td>
                        <td class="" width="12%">
                            <div class='input-group form_datetime' id='firstInTime'>
                                <input name="firstInTime" class="form-control" value="<fmt:formatDate value="${banQinCdWhSkuEntity.firstInTime}" pattern="yyyy-MM-dd"/>" readonly="readonly"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </td>
                        <td class="" width="8%"><label class="pull-right">费率组</label></td>
                        <td class="" width="12%">
                            <form:input path="rateGroup" htmlEscape="false" class="form-control " maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">是否质检管理</label></td>
                        <td class="" width="12%">
                            <input id="isQc" name="isQc" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isQcControl(this.checked)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right" id="qcPhaseLabel">质检阶段</label></td>
                        <td class="" width="12%">
                            <form:select path="qcPhase" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_QC_PHASE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="" width="8%"><label class="pull-right" id="qcRuleLabel">质检规则</label></td>
                        <td class="" width="12%">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRuleQcHeader/grid" title="选择质检规则" cssClass="form-control"
                                           fieldId="qcRule" fieldName="qcRule" fieldKeyName="ruleCode" fieldValue="${banQinCdWhSkuEntity.qcRule}" allowInput="true"
                                           displayFieldId="qcRuleName" displayFieldName="qcRuleName" displayFieldKeyName="ruleName" displayFieldValue="${banQinCdWhSkuEntity.qcRuleName}"
                                           selectButtonId="qcRuleSelectId" deleteButtonId="qcRuleDeleteId"
                                           fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                           searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" queryParamValues="ruleCodeAndName">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right" id="itemGroupLabel">质检项</label></td>
                        <td class="" width="12%">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhQcItemHeader/grid" title="选择质检项" cssClass="form-control"
                                           fieldId="itemGroupCode" fieldName="itemGroupCode" fieldKeyName="itemGroupCode" fieldValue="${banQinCdWhSkuEntity.itemGroupCode}" allowInput="true"
                                           displayFieldId="itemGroupName" displayFieldName="itemGroupName" displayFieldKeyName="itemGroupName" displayFieldValue="${banQinCdWhSkuEntity.itemGroupName}"
                                           selectButtonId="itemGroupSelectId" deleteButtonId="itemGroupDeleteId"
                                           fieldLabels="质检项编码|质检项名称" fieldKeys="itemGroupCode|itemGroupName"
                                           searchLabels="质检项编码|质检项名称" searchKeys="itemGroupCode|itemGroupName" inputSearchKey="itemGroupCodeAndName">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right">是否启用</label></td>
                        <td class="" width="12%">
                            <input id="isEnable" name="isEnable" type="checkbox" checked htmlEscape="false" class="myCheckbox" onclick="isEnableControl(this.checked)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">是否父件</label></td>
                        <td class="" width="12%">
                            <input id="isParent" name="isParent" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isParentControl(this.checked)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">越库级别</label></td>
                        <td class="" width="12%">
                            <form:select path="cdClass" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_CD_CLASS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="" width="8%"><label class="pull-right">是否非库存序列号管理</label></td>
                        <td class="" width="12%">
                            <input id="isUnSerial" name="isUnSerial" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isUnSerialControl(this.checked)"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="reservedInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">自定义1</label></td>
                        <td class="" width="12%">
                            <form:input path="def1" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义2</label></td>
                        <td class="" width="12%">
                            <form:input path="def2" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义3</label></td>
                        <td class="" width="12%">
                            <form:input path="def3" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义4</label></td>
                        <td class="" width="12%">
                            <form:input path="def4" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">自定义5</label></td>
                        <td class="" width="12%">
                            <form:input path="def5" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义6</label></td>
                        <td class="" width="12%">
                            <form:input path="def6" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义7</label></td>
                        <td class="" width="12%">
                            <form:input path="def7" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义8</label></td>
                        <td class="" width="12%">
                            <form:input path="def8" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">自定义9</label></td>
                        <td class="" width="12%">
                            <form:input path="def9" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义10</label></td>
                        <td class="" width="12%">
                            <form:input path="def10" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义11</label></td>
                        <td class="" width="12%">
                            <form:input path="def11" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义12</label></td>
                        <td class="" width="12%">
                            <form:input path="def12" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">自定义13</label></td>
                        <td class="" width="12%">
                            <form:input path="def13" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义14</label></td>
                        <td class="" width="12%">
                            <form:input path="def14" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义15</label></td>
                        <td class="" width="12%">
                            <form:input path="def15" htmlEscape="false" class="form-control " maxlength="64"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    </form:form>
</div>
<div style="width: 100%;">
    <div class="tabs-container">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#pickInfo" aria-expanded="true">拣货信息</a></li>
            <li class=""><a data-toggle="tab" href="#barcodeInfo" aria-expanded="true">条码信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="pickInfo" class="tab-pane fade in active">
                <div id="detailToolbar" style="width: 100%; padding: 5px 0px;">
                    <shiro:hasPermission name="basicdata:banQinCdWhSku:addDetail">
                        <a class="btn btn-primary btn-sm" id="addDetail" onclick="addDetail()">新增</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="basicdata:banQinCdWhSku:saveDetail">
                        <a class="btn btn-primary btn-sm" id="saveDetail" onclick="saveDetail()">保存</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="basicdata:banQinCdWhSku:removeDetail">
                        <a class="btn btn-danger btn-sm" id="removeDetail" onclick="removeDetail()">删除</a>
                    </shiro:hasPermission>
                </div>
                <div id="tab-left">
                    <table id="cdWhSkuLocTable" class="table text-nowrap"></table>
                </div>
                <div id="tab-right" style="overflow: scroll; height: 280px; border: 1px solid #dddddd; display: none;">
                    <form:form id="inputForm1" method="post" class="form-horizontal">
                    <input type="hidden" id="detail_id" name="id"/>
                    <input type="hidden" id="detail_orgId" name="orgId"/>
                    <input type="hidden" id="detail_ownerCode" name="ownerCode"/>
                    <input type="hidden" id="detail_skuCode" name="skuCode"/>
                    <input type="hidden" id="detail_headerId" name="headerId"/>
                    <input type="hidden" id="detail_recVer" name="recVer"/>
                    <div class="tabs-container" style="height: 100%; width: 100%;">
                        <table class="bq-table">
                            <tbody>
                            <tr>
                                <td width="20%">
                                    <label class="pull-left"><font color="red">*</font>拣货位</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left"><font color="red">*</font>商品拣货位类型</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">库位上限</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">库存下限</label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择拣货位" cssClass="form-control required"
                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                   displayFieldId="detail_locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue=""
                                                   selectButtonId="detailLocSelectId" deleteButtonId="detailLocDeleteId"
                                                   fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                   searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                    </sys:popSelect>
                                </td>
                                <td width="20%">
                                    <select id="detail_skuLocType" name="skuLocType" class="form-control m-b required">
                                        <option value=""></option>
                                        <c:forEach items="${fns:getDictList('SYS_WM_SKU_LOC_TYPE')}" var="dict">
                                            <option value="${dict.value}">${dict.label}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td width="20%">
                                    <input id="detail_maxLimit" name="maxLimit" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)">
                                </td>
                                <td width="20%">
                                    <input id="detail_minLimit" name="minLimit" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)">
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label class="pull-left">最小补货数</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">最小补货单位</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">是否超量分配</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">是否补被占用库存</label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <input id="detail_minRp" name="minRp" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                                </td>
                                <td width="20%">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid" title="最小补货单位" cssClass="form-control"
                                                   fieldId="detail_rpUom" fieldName="rpUom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                                   displayFieldId="detail_cdprDesc" displayFieldName="cdprDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                                   selectButtonId="rpUomSelectId" deleteButtonId="rpUomDeleteId"
                                                   fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                   searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                                   queryParams="packCode" queryParamValues="packCode">
                                    </sys:popSelect>
                                </td>
                                <td width="20%">
                                    <input id="detail_isOverAlloc" name="isOverAlloc" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="detailChange(this.checked, '#detail_isOverAlloc')">
                                </td>
                                <td width="20%">
                                    <input id="detail_isRpAlloc" name="isRpAlloc" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="detailChange(this.checked, '#detail_isRpAlloc')">
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label class="pull-left">是否超量补货</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">是否从存储位补货</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">是否从箱拣货位补货</label>
                                </td>
                                <td width="20%">
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <input id="detail_isOverRp" name="isOverRp" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="detailChange(this.checked, '#detail_isOverRp')">
                                </td>
                                <td width="20%">
                                    <input id="detail_isFmRs" name="isFmRs" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="detailChange(this.checked, '#detail_isFmRs')"/>
                                </td>
                                <td width="20%">
                                    <input id="detail_isFmCs" name="isFmCs" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="detailChange(this.checked, '#detail_isFmCs')">
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
            <div id="barcodeInfo" class="tab-pane fade">
                <div id="barcodeToolbar" style="width: 100%; padding: 5px 0px;">
                    <shiro:hasPermission name="basicdata:banQinCdWhSkuBarcode:addBarcode">
                        <a class="btn btn-primary btn-sm" id="addBarcode" onclick="addBarcode()">新增</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="basicdata:banQinCdWhSkuBarcode:saveBarcode">
                        <a class="btn btn-primary btn-sm" id="saveBarCode" onclick="saveBarCode()">保存</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="basicdata:banQinCdWhSkuBarcode:removeBarcode">
                        <a class="btn btn-danger btn-sm" id="removeBarcode" onclick="removeBarcode()">删除</a>
                    </shiro:hasPermission>
                </div>
                <form:form id="inputForm2" method="post" class="form-horizontal">
                <table class="table well">
                    <thead>
                    <tr>
                        <th class="th-inner" width="20"><input type="checkbox" onclick="barcodeSelectChange(this.checked)"/></th>
                        <th class="hide"></th>
                        <th><font color="red">*</font>条码</th>
                        <th>是否默认</th>
                    </tr>
                    </thead>
                    <tbody id="barcodeList">
                    </tbody>
                </table>
                <script type="text/template" id="barcodeTpl">//<!--
                <tr id="barcodeList{{idx}}">
                    <td>
                        <input id="barcodeList{{idx}}_checkbox" type="checkbox" class="customizeCheckbox" name="barcodeSelect"/>
                    </td>
                    <td class="hide">
                        <input id="barcodeList{{idx}}_id" name="barcodeList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
                        <input id="barcodeList{{idx}}_delFlag" name="barcodeList[{{idx}}].delFlag" type="hidden" value="0"/>
                        <input id="barcodeList{{idx}}_orgId" name="barcodeList[{{idx}}].orgId" type="hidden" value="{{row.orgId}}"/>
                        <input id="barcodeList{{idx}}_headerId" name="barcodeList[{{idx}}].headerId" type="hidden" value="{{row.headerId}}"/>
                        <input id="barcodeList{{idx}}_ownerCode" name="barcodeList[{{idx}}].ownerCode" type="hidden" value="{{row.ownerCode}}"/>
                        <input id="barcodeList{{idx}}_skuCode" name="barcodeList[{{idx}}].skuCode" type="hidden" value="{{row.skuCode}}"/>
                    </td>
                    <td>
                        <input id="barcodeList{{idx}}_barcode" name="barcodeList[{{idx}}].barcode" type="text" value="{{row.barcode}}" class="form-control required" maxlength="32"/>
                    </td>
                    <td width="80px">
                        <input id="barcodeList{{idx}}_isDefault" name="barcodeList[{{idx}}].isDefault" type="checkbox" class="myCheckbox" onclick="isDefaultChange({{idx}})"/>
					</td>
                </tr>//-->
                </script>
                <script type="text/javascript">
                    var barcodeRowIdx = 0, barcodeTpl = $("#barcodeTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
                </script>
                </form:form>
            </div>
        </div>
    </div>
</div>
</body>
</html>