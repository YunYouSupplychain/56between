<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>路由节点异常管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="/webpage/include/treeview.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmsRoutingNodeExceptionList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">路由节点异常列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmsRoutingNodeException">
                            <div id="baseInfo" class="tab-pane fade in  active">
                                <table class="table well">
                                    <tbody>
                                    <tr>
                                        <td style="width:10%;vertical-align:middle;"><label class="pull-right">订单号</label></td>
                                        <td style="width:15%;vertical-align:middle;">
                                            <form:input path="orderNo" htmlEscape="false" class="form-control required"/>
                                        </td>
                                        <td style="width:10%;vertical-align:middle;"><label class="pull-right">快递单号</label></td>
                                        <td style="width:15%;vertical-align:middle;">
                                            <form:input path="trackingNo" htmlEscape="false" class="form-control required"/>
                                        </td>
                                        <td style="width:10%;vertical-align:middle;"></td>
                                        <td style="width:15%;vertical-align:middle;"></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
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
            </div>
            <!-- 表格 -->
            <table id="tmsRoutingNodeExceptionTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>