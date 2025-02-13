<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>运输价格档案清单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsTransportPriceFileList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="orgId"/>
    <input type="hidden" id="billModule" value="02"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading"><h3 class="panel-title">运输价格档案清单列表</h3></div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="bmsTransportPriceFileList" class="form">
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">结算对象</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择结算对象" url="${ctx}/bms/basic/bmsSettleObject/grid" cssClass="form-control"
                                                  fieldId="settleObjectCode" fieldName="settleObjectCode" fieldKeyName="settleObjectCode"
                                                  displayFieldId="settleObjectName" displayFieldName="settleObjectName" displayFieldKeyName="settleObjectName"
                                                  fieldLabels="结算对象代码|结算对象名称" fieldKeys="settleObjectCode|settleObjectName"
                                                  searchLabels="结算对象代码|结算对象名称" searchKeys="settleObjectCode|settleObjectName"
                                                  queryParams="orgId" queryParamValues="orgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">费用科目</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择费用科目" url="${ctx}/bms/basic/bmsBillSubject/grid" cssClass="form-control"
                                                  fieldId="billSubjectCode" fieldName="billSubjectCode" fieldKeyName="billSubjectCode"
                                                  displayFieldId="billSubjectName" displayFieldName="billSubjectName" displayFieldKeyName="billSubjectName"
                                                  fieldLabels="费用科目代码|费用科目名称" fieldKeys="billSubjectCode|billSubjectName"
                                                  searchLabels="费用科目代码|费用科目名称" searchKeys="billSubjectCode|billSubjectName"
                                                  queryParams="billModule|orgId" queryParamValues="billModule|orgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">车型</label></td>
                                    <td class="width-15">
                                        <form:select path="carType" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_CAR_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="bms:report:transportPriceFileList:export">
                    <a id="export" class="btn btn-info"><i class="fa fa-file-excel-o"></i> 导出</a>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="bmsTransportPriceFileListTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>