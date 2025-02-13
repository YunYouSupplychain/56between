<!DOCTYPE html>
<html>
<head>
    <meta name="decorator" content="ani"/>
    <title>${fns:getConfig('productName')} 登录</title>
    <script>
        <%--window.open("${ctx}/loginReal", "red", "resizable=yes,width=500,height=300");--%>
        newwin = window.open("www.baidu.com", 'newwindow', 'fullscreen=1')
        newwin.resizeTo(800, 600)
        newwin.moveTo(screen.width / 0 - 800, screen.height / 0 - 600)
        // window.close();
    </script>
</head>
<body>
</body>
</html>