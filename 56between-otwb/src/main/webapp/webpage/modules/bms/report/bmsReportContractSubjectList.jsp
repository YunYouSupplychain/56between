<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>合同科目列表</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="/webpage/include/treeview.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#effectiveDateFm').datetimepicker({format: "YYYY-MM-DD"});
            $('#effectiveDateTo').datetimepicker({format: "YYYY-MM-DD"});
            $("#orgId").val(jp.getCurrentOrg().orgId);

            $('#bmsReportContractSubjectTable').bootstrapTable({
                cache: false,// 是否使用缓存
                pagination: true,// 是否显示分页
                sidePagination: "server",// client客户端分页，server服务端分页
                queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
                    var searchParam = $("#searchForm").serializeJSON();
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                columns: [{
                    field: 'sysContractNo',
                    title: '系统合同编号',
                    sortable: true
                }, {
                    field: 'contractNo',
                    title: '合同编号',
                    sortable: true
                }, {
                    field: 'settleCode',
                    title: '结算对象编码',
                    sortable: true
                }, {
                    field: 'settleName',
                    title: '结算对象名称',
                    sortable: true
                }, {
                    field: 'orgName',
                    title: '机构名称',
                    sortable: true
                }, {
                    field: 'billModule',
                    title: '费用模块',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_MODULE'))}, value, "-");
                    }
                }, {
                    field: 'billSubjectCode',
                    title: '费用科目编码',
                    sortable: true
                }, {
                    field: 'billSubjectName',
                    title: '费用科目',
                    sortable: true
                }, {
                    field: 'billTermsCode',
                    title: '计费条款编码',
                    sortable: true
                }, {
                    field: 'billTermsDesc',
                    title: '计费条款',
                    sortable: true
                }, {
                    field: 'methodName',
                    title: '条款计算公式',
                    sortable: true
                }, {
                    field: 'operator',
                    title: '操作人',
                    sortable: true
                }, {
                    field: 'operateTime',
                    title: '操作时间',
                    sortable: true
                }, {
                    field: 'fm',
                    title: '阶梯范围从',
                    sortable: true
                }, {
                    field: 'to',
                    title: '阶梯范围到',
                    sortable: true
                }, {
                    field: 'price',
                    title: '单价',
                    sortable: true
                }, {
                    field: 'logisticsPoints',
                    title: '物流点数',
                    sortable: true
                }, {
                    field: 'taxRate',
                    title: '税点',
                    sortable: true
                }, {
                    field: 'effectiveDateFm',
                    title: '有效期从',
                    sortable: true
                }, {
                    field: 'effectiveDateTo',
                    title: '有效期到',
                    sortable: true
                }, {
                    field: 'remarks',
                    title: '备注',
                    sortable: true
                }]
            });
            $("#search").click("click", function () {
                var validate = bq.headerSubmitCheck("#searchForm");
                if (!validate.isSuccess) {
                    jp.alert(validate.msg);
                    return;
                }
                $('#bmsReportContractSubjectTable').bootstrapTable('refresh', {url: "${ctx}/bms/report/contractSubject/data"});
            });
            $("#reset").click("click", function () {
                $("#searchForm  input").val("");
                $("#searchForm  select").val("");
                $("#searchForm  .select-item").html("");
                $("#orgId").val(jp.getCurrentOrg().orgId);
                $('#bmsReportContractSubjectTable').bootstrapTable('refresh');
            });
        });

        function exportExcel(){
            var validate = bq.headerSubmitCheck("#searchForm");
            if (!validate.isSuccess) {
                jp.alert(validate.msg);
                return;
            }
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            bq.exportExcelNew("${ctx}/bms/report/contractSubject/export", "合同科目", searchParam);
        }
    </script>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">合同科目列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="bmsReportContractSubjectQuery" class="form">
                            <form:hidden path="orgId"/>
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">结算对象</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择结算对象" url="${ctx}/bms/basic/bmsSettleObject/grid" cssClass="form-control"
                                                  fieldId="settleCode" fieldName="settleCode" fieldKeyName="settleObjectCode"
                                                  displayFieldId="settleName" displayFieldName="settleName" displayFieldKeyName="settleObjectName"
                                                  fieldLabels="结算对象编码|结算对象名称" fieldKeys="settleObjectCode|settleObjectName"
                                                  searchLabels="结算对象编码|结算对象名称" searchKeys="settleObjectCode|settleObjectName"
                                                  queryParams="orgId" queryParamValues="orgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">系统合同编号</label></td>
                                    <td class="width-15">
                                        <form:input path="sysContractNo" class="form-control" htmlEscape="false"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">合同编号</label></td>
                                    <td class="width-15">
                                        <form:input path="contractNo" class="form-control" htmlEscape="false"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">费用科目编号</label></td>
                                    <td class="width-15">
                                        <form:input path="billSubjectCode" class="form-control" htmlEscape="false"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">有效期从</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id="effectiveDateFm">
                                            <input type='text' name="effectiveDateFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">有效期到</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id="effectiveDateTo">
                                            <input type='text' name="effectiveDateTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">操作人</label></td>
                                    <td class="width-15">
                                        <form:input path="operator" class="form-control" htmlEscape="false"/>
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
                <a id="export" class="btn btn-info" onclick="exportExcel()"><i class="fa fa-file-excel-o"></i> 导出</a>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="bmsReportContractSubjectTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>