<%
    response.setStatus(400);
    // 获取异常类
    Throwable ex = ExceptionUtil.getThrowable(request);
    // 编译错误信息
    StringBuilder sb = new StringBuilder("错误信息：\n");
    if (ex != null) {
        if (ex instanceof BindException) {
            for (ObjectError e : ((BindException) ex).getGlobalErrors()) {
                sb.append("☆").append(e.getDefaultMessage()).append("(").append(e.getObjectName()).append(")\n");
            }
            for (FieldError e : ((BindException) ex).getFieldErrors()) {
                sb.append("☆").append(e.getDefaultMessage()).append("(").append(e.getField()).append(")\n");
            }
            LoggerFactory.getLogger("400.jsp").warn(ex.getMessage(), ex);
        } else if (ex instanceof ConstraintViolationException) {
            for (ConstraintViolation<?> v : ((ConstraintViolationException) ex).getConstraintViolations()) {
                sb.append("☆").append(v.getMessage()).append("(").append(v.getPropertyPath()).append(")\n");
            }
        } else {
            sb.append("☆").append(ex.getMessage());
        }
    } else {
        sb.append("未知错误.\n\n");
    }
    if (Servlets.isAjaxRequest(request)) {
        // 如果是异步请求或是手机端，则直接返回信息
        out.print(sb);
    } else {
        // 输出异常信息页面
%>
<%@page import="javax.validation.ConstraintViolation" %>
<%@page import="javax.validation.ConstraintViolationException" %>
<%@page import="org.springframework.validation.BindException" %>
<%@page import="org.springframework.validation.ObjectError" %>
<%@page import="org.springframework.validation.FieldError" %>
<%@page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>
<%@page import="com.yunyou.core.web.Servlets" %>
<%@page import="com.yunyou.common.utils.ExceptionUtil" %>
<%@page import="com.yunyou.common.utils.StringUtils" %>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>400 - 请求出错</title>
    <%@include file="/webpage/include/anihead.jsp" %>
</head>
<body>
<div class="container-fluid">
    <div class="page-header"><h1>参数有误，服务器无法解析.</h1></div>
    <%--<div class="errorMessage">
        <%=StringUtils.toHtml(sb.toString())%> <br/>
    </div>--%>
    <a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a> &nbsp;
    <br/> <br/>
    <script>top.layer.closeAll("dialog")</script>
</div>
</body>
</html>
<%
    }
    out = pageContext.pushBody();
%>