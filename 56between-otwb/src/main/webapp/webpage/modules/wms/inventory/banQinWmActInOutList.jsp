<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>进出存统计</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmActInOutList.js" %>
    <style>
        #banQinWmActInOutTable > thead > tr > th,
        #banQinWmActInOutTable > tbody > tr > td {
            border: 1px solid black;
        }
    </style>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">进出存统计</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmActLogEntity" class="form">
                            <input id="orgId" name="orgId" type="hidden">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="库区：">库区</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择库区" cssClass="form-control"
                                                       fieldId="zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue="" allowInput="true"
                                                       displayFieldId="zoneName" displayFieldName="zoneName" displayFieldKeyName="zoneName" displayFieldValue=""
                                                       selectButtonId="zoneSelectId" deleteButtonId="zoneDeleteId"
                                                       fieldLabels="库区代码|库区名称" fieldKeys="zoneCode|zoneName"
                                                       searchLabels="库区代码|库区名称" searchKeys="zoneCode|zoneName" inputSearchKey="zoneCodeAndName">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="货主：">货主</label>
                                    </td>
                                    <td class="width-15">
                                        <input id="ownerType" value="OWNER" type="hidden">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                       fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="ownerType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="结算日期从："><font color="red">*</font>结算日期从</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='beginTimeF'>
                                            <input id="beginTime" type='text' name="beginTime" class="form-control required"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="结算日期到："><font color="red">*</font>结算日期到</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='endTimeF'>
                                            <input id="endTime" type='text' name="endTime" class="form-control required"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
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
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
                <button id="export" class="btn btn-primary" onclick="exportData()"><i class="fa fa-file-excel-o"></i> 导出</button>
            </div>
            <!-- 表格 -->
            <table id="banQinWmActInOutTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>