<%
    response.setStatus(403);
    //获取异常类
    Throwable ex = ExceptionUtil.getThrowable(request);
    if (Servlets.isAjaxRequest(request)) {
        // 如果是异步请求或是手机端，则直接返回信息
        if (ex != null && StringUtils.startsWith(ex.getMessage(), "msg:")) {
            out.print(StringUtils.replace(ex.getMessage(), "msg:", ""));
        } else {
            out.print("操作权限不足.");
        }
    } else {
        // 输出异常信息页面
%>
<%@page import="com.yunyou.core.web.Servlets" %>
<%@page import="com.yunyou.common.utils.ExceptionUtil" %>
<%@page import="com.yunyou.common.utils.StringUtils" %>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>403 - 操作权限不足</title>
    <%@include file="/webpage/include/anihead.jsp" %>
</head>
<body>
<div class="container-fluid">
    <div class="page-header"><h1>操作权限不足.</h1></div>
    <%--<%
        if (ex != null && StringUtils.startsWith(ex.getMessage(), "msg:")) {
            out.print("<div>" + StringUtils.replace(ex.getMessage(), "msg:", "") + " <br/> <br/></div>");
        }
    %>--%>
    <div><a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a></div>
    <script>top.layer.closeAll("dialog")</script>
</div>
</body>
</html>
<%
    }
    out = pageContext.pushBody();
%>