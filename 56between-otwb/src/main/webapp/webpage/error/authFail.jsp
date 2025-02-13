<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>授权失败 页面</title>
    <style>
        body {
            text-align: center;
        }
        .font-bold {
            font-weight: 600;
        }
    </style>
</head>
<body>
    <h2 class="font-bold"><%=request.getAttribute("message")%></h2>
</body>
</html>