<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>应收账款结算单</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="/webpage/include/treeview.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#settleDateFm').datetimepicker({format: "YYYY-MM-DD"});
            $('#settleDateTo').datetimepicker({format: "YYYY-MM-DD"});
            $("#status").val("02");
            $("#orgId").val(jp.getCurrentOrg().orgId);
            $("#parentId").val(jp.getCurrentOrg().orgId);

            $('#bmsReportAccountReceivableTable').bootstrapTable({
                cache: false,// 是否使用缓存
                pagination: true,// 是否显示分页
                sidePagination: "server",// client客户端分页，server服务端分页
                queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
                    var searchParam = $("#searchForm").serializeJSON();
                    searchParam.orgId = jp.getCurrentOrg().orgId;
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                columns: [{
                    field: 'billNo',
                    title: '费用单号',
                    sortable: true
                }, {
                    field: 'status',
                    title: '状态',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_STATUS'))}, value, "-");
                    }
                }, {
                    field: 'settleCycle',
                    title: '结算周期',
                    sortable: true
                }, {
                    field: 'invoiceObjectCode',
                    title: '开票对象编码',
                    sortable: true
                }, {
                    field: 'invoiceObjectName',
                    title: '开票对象名称',
                    sortable: true
                }, {
                    field: 'warehouseName',
                    title: '仓别',
                    sortable: true
                }, {
                    field: 'settleObjectCode',
                    title: '结算对象编码',
                    sortable: true
                }, {
                    field: 'settleObjectName',
                    title: '结算对象名称',
                    sortable: true
                }, {
                    field: 'billSubjectName',
                    title: '费用客户名称',
                    sortable: true
                }, {
                    field: 'taxRate',
                    title: '税率',
                    sortable: true
                }, {
                    field: 'billStandard',
                    title: '计费标准',
                    sortable: true
                }, {
                    field: 'occurrenceQty',
                    title: '发生量',
                    sortable: true
                }, {
                    field: 'billQty',
                    title: '计费量',
                    sortable: true
                }, {
                    field: 'cost',
                    title: '含税费用',
                    sortable: true
                }, {
                    field: 'creator',
                    title: '创建人',
                    sortable: true
                }, {
                    field: 'auditor',
                    title: '确认人',
                    sortable: true
                }, {
                    field: 'costRemarks',
                    title: '备注',
                    sortable: true
                }]
            });
        });

        function searchForm() {
            var validate = bq.headerSubmitCheck("#searchForm");
            if (!validate.isSuccess) {
                jp.alert(validate.msg);
                return;
            }
            $('#bmsReportAccountReceivableTable').bootstrapTable('refresh', {url: "${ctx}/bms/report/accountReceivable/data"});
        }

        function resetForm() {
            $("#searchForm  input").val("");
            $("#searchForm  select").val("");
            $("#searchForm  .select-item").html("");
            $("#orgId").val(jp.getCurrentOrg().orgId);
            $("#parentId").val(jp.getCurrentOrg().orgId);
        }

        function exportExcel() {
            var index = jp.loading("");
            var validate = bq.headerSubmitCheck("#searchForm");
            if (!validate.isSuccess) {
                jp.alert(validate.msg);
                return;
            }
            var orgId = jp.getCurrentOrg().orgId;
            $("#orgId").val(orgId);
            $("#parentId").val(orgId);

            bq.exportExcelNew("${ctx}/bms/report/accountReceivable/export", "应收账款结算单", $("#searchForm").serializeJSON());
        }

        function print() {
            var validate = bq.headerSubmitCheck("#searchForm");
            if (!validate.isSuccess) {
                jp.alert(validate.msg);
                return;
            }

            var data = $("#searchForm").serializeJSON();
            var params = "", paramValues = "";
            for (var field in data) {
                params = params + "|" + field;
                paramValues = paramValues + "|" + data[field];
            }
            bq.openPostWindow("${ctx}/bms/report/accountReceivable/print", params, paramValues, '应收账款结算单');
        }
    </script>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">应收账款结算单</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="bmsReportAccountReceivableQuery" class="form">
                            <form:hidden path="orgId"/>
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">开票对象</label></td>
                                    <td class="width-15">
                                        <sys:popSelect title="选择开票对象" url="${ctx}/bms/basic/baseInvoiceObject/grid" cssClass="form-control"
                                                       fieldId="invoiceObjectCode" fieldKeyName="code" fieldName="invoiceObjectCode" fieldValue=""
                                                       displayFieldId="invoiceObjectName" displayFieldKeyName="name"
                                                       displayFieldName="invoiceObjectName" displayFieldValue=""
                                                       selectButtonId="invoiceObjectSBtnId" deleteButtonId="invoiceObjectDBtnId"
                                                       fieldLabels="开票对象编码|开票对象名称" fieldKeys="code|name"
                                                       searchLabels="开票对象编码|开票对象名称" searchKeys="code|name"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right asterisk">结算日期从</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id="settleDateFm">
                                            <input type='text' name="settleDateFm" class="form-control required"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right asterisk">结算日期到</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id="settleDateTo">
                                            <input type='text' name="settleDateTo" class="form-control required"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">仓库编码</label></td>
                                    <td class="width-15">
                                        <input id="parentId" value="" type="hidden">
                                        <sys:popSelect url="${ctx}/sys/office/companyData" title="选择仓库" cssClass="form-control"
                                                       fieldId="warehouseCode" fieldKeyName="code" fieldName="warehouseCode" fieldValue=""
                                                       displayFieldId="warehouseName" displayFieldKeyName="name"
                                                       displayFieldName="warehouseName" displayFieldValue=""
                                                       selectButtonId="warhouseSelectId" deleteButtonId="warhouseDeleteId"
                                                       fieldLabels="仓库编码|仓库名称" fieldKeys="code|name"
                                                       searchLabels="仓库编码|仓库名称" searchKeys="code|name"
                                                       queryParams="id" queryParamValues="parentId">
                                        </sys:popSelect>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:8%;vertical-align:middle;"><label class="label-item single-overflow pull-right">结算对象</label></td>
                                    <td style="width:12%;vertical-align:middle;">
                                        <sys:popSelect title="选择结算对象" url="${ctx}/bms/basic/bmsSettleObject/grid" cssClass="form-control"
                                                       fieldId="settleObjectCode" fieldKeyName="settleObjectCode" fieldName="settleObjectCode" fieldValue=""
                                                       displayFieldId="" displayFieldKeyName="settleObjectName" displayFieldName="" displayFieldValue=""
                                                       selectButtonId="settleObjectSBtnId" deleteButtonId="settleObjectDBtnId"
                                                       fieldLabels="结算对象代码|结算对象名称" fieldKeys="settleObjectCode|settleObjectName"
                                                       searchLabels="结算对象代码|结算对象名称" searchKeys="settleObjectCode|settleObjectName"/>
                                    </td>
                                    <td style="width:8%;vertical-align:middle;"><label class="label-item single-overflow pull-right">结算对象名称</label></td>
                                    <td style="width:12%;vertical-align:middle;">
                                        <form:input path="settleObjectName" htmlEscape="false" class="form-control"/>
                                    </td>
                                    <td style="width:8%;vertical-align:middle;"><label class="label-item single-overflow pull-right">费用单号</label></td>
                                    <td style="width:12%;vertical-align:middle;">
                                        <form:input path="billNo" htmlEscape="false" class="form-control"/>
                                    </td>
                                    <td style="width:8%;vertical-align:middle;"><label class="label-item single-overflow pull-right">状态</label></td>
                                    <td style="width:12%;vertical-align:middle;">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('BMS_BILL_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="bms:report:accountReceivable:export">
                    <a id="export" class="btn btn-info" onclick="exportExcel()"><i class="fa fa-file-excel-o"></i> 导出</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:report:accountReceivable:print">
                    <a id="print" class="btn btn-primary" onclick="print()"><i class="glyphicon glyphicon-print"></i> 打印</a>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary" onclick="searchForm()"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary" onclick="resetForm()"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="bmsReportAccountReceivableTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>