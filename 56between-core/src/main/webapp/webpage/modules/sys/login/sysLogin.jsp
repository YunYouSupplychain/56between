<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<!-- _login_page_ --><!--登录超时标记 勿删-->
<html>
<head>
    <meta name="decorator" content="ani"/>
    <title>${fns:getConfig('productName')} 登录</title>
    <style>
        body {
            background-image: url("${ctxStatic}/login.jpg");
            background-repeat: no-repeat;
            background-size: 100%;
            background-position: 0 -50px;
        }

        #login-box {
            background-color: rgba(246, 244, 244, 0.41);
            border-radius: 13px;
            text-align: center;
            margin: 150px auto 0;
            width: 600px;
            height: 400px;
        }

        #login-box h2 {
            padding-top: 50px;
            color: #170202;
        }

        #login-box h5 {
            margin: 0;
        }

        #login-box .input-box {
            margin-top: 25px;
        }

        #login-box .input-box input {
            border: none;
            background: none;
            border-bottom: #007bff 2px solid;
            padding: 5px 10px;
            outline: none;
            color: #170101;
            width: 400px;
            font-size: 14px;
        }

        #login-box #login {
            width: 330px;
            height: 35px;
            line-height: 35px;
            margin-top: 40px;
            border-radius: 8px;
            outline: none;
            border: none;
            background-color: #007bff;
            color: #FFFFFF;
        }

        #login-box button:hover {
            background-color: #0069d9;
        }

        #login-box .input-box i {
            color: #007bff;
        }

        #login-box #to {
            padding-top: 10px;
            text-align: center;
            font-size: 15px;
            text-decoration: underline;
        }

        @font-face {
            font-family: "iconfont";
            src: url('iconfont.eot?t=1606872294248'); /* IE9 */
            src: url('iconfont.eot?t=1606872294248#iefix') format('embedded-opentype'), url('data:application/x-font-woff2;charset=utf-8;base64,d09GMgABAAAAAAO8AAsAAAAAB6wAAANvAAEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAHEIGVgCDBgqDFIJ/ATYCJAMMCwgABCAFhG0HNxvGBsiemjxBRAhmg9koBWDBEY/gaezbvN09E2tu9cATkUQo4k2s0cgQGo1MJGn3Ev6hzjw/EOq5GNxK4DwA+R3JcUiuIpdxLk/9seUZAA1Tfy7h3JxCAM/H3P+Lw2nTw3A+UE5zbIy6AOOAAhp7URpggSToDWM3ETTE1QTqDVtEOBbb+yFFAToF4paqSJCSUSgN6aFWqK4YWcQbSFSbnsa9APCafz/+gydSSKoy0PP85no/rPqV+Cs1P/q/2zhFAHM6HYxxZCwChbhRabguPgosilfvCRKa46sVIU3lrgxn+Lxq+IdHEkQ1kNoIZpVg4lciZiTCVJl11StlUI0aPAE+ApLOoT2rlnJzctIzmu5a1mCRbQ+y/H7D6N9tdhQY+hzHG9LfFtv2ytf2MbPjpjlwvDCba5Gj1jtrj/2+5AVvzd3GG2PbsZt9cbtv1Gc9u9Uff/Z2A816YKC4c+b67Md2XfysRY49fXsNoDG+2kNjeiEzHlvg7cSko+dDeHzh+Qfw6P36nWnUoesdCHq2PTeaJhaMXxr258lFW08COVuxcRA6RnV91UmhL34gfr2WLBx6N81l3/Me3eat/uHZJKxO1ipqXLzQM0+s2FwhdiH8JrGyW7Ap2XWzWAGuL8+qT2ePV12vqq2+kDmefRrVtNb+Ims8i+B+1o6rGq+6UVde3SfhXZ08JgstktTSpgrHjoV6+edlT8PIenGkPicrZ13OcSxAtD5MA1TKl+MHeEiejd/w437j88f37qnsZf9qKODnt8xfsq2iQLdkUCvRmfmbmB2Lis8GylxcBmaE4exY4kugyTRT6OdkQru54aYukOhCrcmUIakxBFmtSWTBLkKVBstQrdYe1FvgG9+gAxMvSh3mHZAgtLoCSbNPkLV6iSzYD1Cl2zeo1hrxUO9q5M/YYDrUtLUMSxz70cwWpMo0SpCap4n0XqzoYYml2ZwwiFlMdSO3w5UP+3AUsyn6xDYqHs4JIoxGkCith8NhijRGg1jmjgDn2hKnk1Q9ySHTCKQ5wmASDvNDZmyBqGRUFKHlubTC53thCl2YhDUU9KgHYUyMWj3i5uDqgPQJop0KbuWcmI0UHhxHIARDRSCipMPCMEAhWnWzIEzGOQQG+DRLOJVSpKvAsbwo8n4roB5YMEeKHEX1bRpVAnpCRI1IAAA=') format('woff2'),
            url('iconfont.woff?t=1606872294248') format('woff'),
            url('iconfont.ttf?t=1606872294248') format('truetype'), url('iconfont.svg?t=1606872294248#iconfont') format('svg');
        }

        .iconfont {
            font-family: "iconfont", serif !important;
            font-size: 18px;
            font-style: normal;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
        }

        .icon-yonghu:before {
            content: "\e609";
        }

        .icon-mima:before {
            content: "\e605";
        }

        .alert-success {
            border-color: transparent;
            background-color: transparent;
        }

        #messageBox {
            text-align: center;
            color: red;
        }
    </style>
</head>
<body>
<div id="login-box">
    <h2>${fns:getConfig('productName')}</h2>
    <h5>${fns:getConfig('version')}</h5>
    <form id="loginForm" role="form" action="${ctx}/login" method="post">
        <div class="input-box">
            <i class="iconfont">&#xe609;</i>
            <input type="text" id="username" name="username" value="" placeholder="用户名"/>
        </div>
        <div class="input-box">
            <i class="iconfont">&#xe605;</i>
            <input type="password" id="password" name="password" value="" placeholder="密码"/>
        </div>
        <div class="col-lg-12 col-md-12 col-sm-12">
            <button id="login" type="submit">登录</button>
        </div>
    </form>
    <sys:message content="${message}" showType="1"/>
</div>
<c:if test="${fns:isShowLogin()}">
<div style="padding-right: 10%;text-align: right;font-size: 16px">
    <span>账号: demo1&nbsp;&nbsp;&nbsp;&nbsp;密码: Demo1234</span>
</div>
</c:if>
<script type="text/javascript">
    $(document).ready(function () {
        $("#loginForm").validate({
            rules: {
                validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
            },
            messages: {
                username: {required: "请填写用户名."}, password: {required: "请填写密码."},
                validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
            },
            errorLabelContainer: "#messageBox",
            errorPlacement: function (error, element) {
                error.appendTo($("#loginError").parent());
            }
        });
        $("#username").focus();
    });
    // 如果在框架或在对话框中，则弹出提示并跳转到首页
    if (self.frameElement && self.frameElement.tagName === "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0) {
        alert('未登录或登录超时。请重新登录，谢谢！');
        top.location = "${ctx}";
    }
    if (window.top !== window.self) {
        window.top.location = window.location;
    }
</script>
</body>
</html>