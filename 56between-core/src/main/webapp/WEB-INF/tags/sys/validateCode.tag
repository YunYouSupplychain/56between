<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ attribute name="name" type="java.lang.String" required="true" description="验证码输入框名称" %>
<input type="text" id="${name}" name="${name}" class="txt required" maxlength="4"
       style="font-weight:bold;width:80px;height:30px;margin-bottom:5px;padding-left:5px;"/>
<img src="${pageContext.request.contextPath}/servlet/validateCodeServlet" onclick="$('.${name}Refresh').click();"
     class="mid ${name}" style="width:120px;"/>
<a href="javascript:"
   onclick="$('.${name}').attr('src','${pageContext.request.contextPath}/servlet/validateCodeServlet?'+new Date().getTime());"
   class="mid ${name}Refresh" style="color:blue">看不清</a>