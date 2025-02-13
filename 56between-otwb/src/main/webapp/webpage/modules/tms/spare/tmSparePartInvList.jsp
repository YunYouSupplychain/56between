<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>备件库存管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@ include file="tmSparePartInvList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="baseOrgId"/>
    <input type="hidden" id="supplierType" value="SUPPLIER"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">备件库存列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse in">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmSparePartInvEntity" class="form form-horizontal well clearfix" cssStyle="padding: 0;margin: 0">
                            <table class="table table-striped table-bordered table-condensed" style="margin: 0">
                                <tbody>
                                <tr>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">入库时间从：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <div class='input-group date' id='inboundTimeFm'>
                                            <input type='text' name="inboundTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">入库时间到：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <div class='input-group date' id='inboundTimeTo'>
                                            <input type='text' name="inboundTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">备件：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <sys:grid title="选择备件" url="${ctx}/tms/basic/tmFitting/grid" cssClass="form-control"
                                                  fieldId="fittingCode" fieldName="fittingCode" fieldKeyName="fittingCode"
                                                  displayFieldId="fittingName" displayFieldName="fittingName" displayFieldKeyName="fittingName"
                                                  fieldLabels="备件编码|备件名称" fieldKeys="fittingCode|fittingName"
                                                  searchLabels="备件编码|备件名称" searchKeys="fittingCode|fittingName"
                                                  queryParams="orgId" queryParamValues="baseOrgId"/>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">供应商：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <sys:grid title="选择供应商" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control"
                                                  fieldId="supplierCode" fieldName="supplierCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="supplierName" displayFieldName="supplierName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="供应商编码|供应商名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="供应商编码|供应商名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="orgId|transportObjType" queryParamValues="baseOrgId|supplierType"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">条码：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:input path="barcode" cssClass="form-control" htmlEscape="false"/>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"></td>
                                    <td style="width:15%;vertical-align:middle;"></td>
                                    <td style="width:10%;vertical-align:middle;"></td>
                                    <td style="width:15%;vertical-align:middle;"></td>
                                    <td style="width:10%;vertical-align:middle;"></td>
                                    <td style="width:15%;vertical-align:middle;"></td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <a id="search" class="btn btn-primary btn-rounded btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary btn-rounded btn-bordered btn-sm"><i class="fa fa-refresh"></i> 重置</a>
            </div>
            <!-- 表格 -->
            <table id="tmSparePartInvTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>