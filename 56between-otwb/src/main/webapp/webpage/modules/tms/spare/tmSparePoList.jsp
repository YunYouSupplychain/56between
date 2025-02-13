<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>备件入库管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@ include file="tmSparePoList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">备件入库列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmSparePoEntity" class="form form-horizontal well clearfix">
                            <table class="table table-striped table-bordered table-condensed">
                                <tbody>
                                <tr>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">订单时间从：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <div class='input-group date' id='orderTimeFm'>
                                            <input type='text' name="orderTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">订单时间到：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <div class='input-group date' id='orderTimeTo'>
                                            <input type='text' name="orderTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">入库单号：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:input path="sparePoNo" cssClass="form-control" htmlEscape="false"/>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">状态：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:select path="orderStatus" class="form-control ">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_SPARE_ASN_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">类型：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:select path="orderType" class="form-control ">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_SPARE_ASN_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">客户单号：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:input path="customerNo" cssClass="form-control" htmlEscape="false" maxlength="20"/>
                                    </td>
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
                <shiro:hasPermission name="tms:spare:po:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:spare:po:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:spare:po:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="tmSparePoTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>