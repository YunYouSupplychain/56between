<%
    response.setStatus(500);
    // 获取异常类
    Throwable ex = ExceptionUtil.getThrowable(request);
    if (ex != null) {
        LoggerFactory.getLogger("500.jsp").error(ex.getMessage(), ex);
    }
    // 编译错误信息
    StringBuilder sb = new StringBuilder("错误信息：\n");
    if (ex != null) {
        sb.append(ExceptionUtil.getStackTraceAsString(ex));
    } else {
        sb.append("未知错误.\n\n");
    }
    if (Servlets.isAjaxRequest(request)) {
        // 如果是异步请求或是手机端，则直接返回信息
        out.print(sb);
    } else {
        // 输出异常信息页面
%>
<%@page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%@page import="com.yunyou.core.web.Servlets" %>
<%@page import="com.yunyou.common.utils.ExceptionUtil" %>
<%@page import="com.yunyou.common.utils.StringUtils" %>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>500 - 内部错误</title>
    <%@include file="/webpage/include/anihead.jsp" %>
</head>
<body>
<div class="container-fluid">
    <div class="page-header"><h2>内部错误.</h2></div>
    <%--<div class="errorMessage">
        错误信息：<%=ex == null ? "未知错误." : StringUtils.toHtml(ex.getMessage())%> <br/> <br/>
        请点击“查看详细信息”按钮，将详细错误信息发送给系统管理员，谢谢！<br/> <br/>
        <a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a> &nbsp;
        <a href="javascript:" onclick="$('.errorMessage').toggle();" class="btn">查看详细信息</a>
    </div>
    <div class="errorMessage" style="display: none">
        <%=StringUtils.toHtml(sb.toString())%> <br/>
        <a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a> &nbsp;
        <a href="javascript:" onclick="$('.errorMessage').toggle();" class="btn">隐藏详细信息</a>
    </div>--%>
    <script>top.layer.closeAll("dialog")</script>
</div>
</body>
</html>
<%
    }
    out = pageContext.pushBody();
%>