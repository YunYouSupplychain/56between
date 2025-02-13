<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html style="overflow-x:auto;overflow-y:auto;">
<head>
    <title><sitemesh:title/></title>
    <%@ include file="/webpage/include/anihead.jsp" %>
    <sitemesh:head/>
</head>
<body id="<sitemesh:getProperty property='body.id'/>" class="<sitemesh:getProperty property='body.class'/>" style="<sitemesh:getProperty property='body.style'/>">
<sitemesh:body/>
</body>
</html>