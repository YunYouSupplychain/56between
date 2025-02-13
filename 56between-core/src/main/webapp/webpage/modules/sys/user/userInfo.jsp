<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>个人信息</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#userPassWordBtn").click(function () {
                jp.open({
                    type: 2,
                    area: ['450px', '350px'],
                    title: "修改密码",
                    content: "${ctx}/sys/user/modifyPwd",
                    btn: ['确定', '关闭'],
                    yes: function (index, layero) {
                        var body = top.layer.getChildFrame('body', index);
                        var inputForm = $(body).find('#inputForm');
                        var btn = body.find('#btnSubmit');
                        var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe
                        inputForm.attr("target", top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
                        if (!inputForm.valid()) {
                            return;
                        }
                        if (inputForm.find("#newPassword").val() !== inputForm.find("#confirmNewPassword").val()) {
                            jp.alert("输入的2次新密码不一致，请重新输入！")
                            return;
                        }
                        jp.post("${ctx}/sys/user/savePwd?oldPassword=" + inputForm.find("#oldPassword").val() + "&newPassword=" + inputForm.find("#newPassword").val(), $('#inputForm').serialize(), function (data) {
                            if (data.success) {
                                jp.success(data.msg);
                                jp.close(index);//关闭dialog
                            } else {
                                jp.error(data.msg);
                            }
                        });
                    },
                    cancel: function (index) {
                    }
                });
            });
            $("#userInfoBtn").click(function () {
                jp.open({
                    type: 2,
                    area: ['400px', '350px'],
                    title: "个人信息编辑",
                    content: "${ctx}/sys/user/infoEdit",
                    btn: ['确定', '关闭'],
                    yes: function (index, layero) {
                        var body = top.layer.getChildFrame('body', index);
                        var inputForm = body.find('#inputForm');
                        var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe
                        inputForm.attr("target", top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
                        inputForm.validate();
                        if (!inputForm.valid()) {
                            return;
                        }
                        jp.loading();
                        inputForm.submit();
                        jp.success("保存成功!");
                        jp.close(index);//关闭对话框。
                    },
                    cancel: function (index) {
                    }
                });
            });
            $("#userImageBtn").click(function () {
                jp.open({
                    type: 2,
                    area: ['700px', '500px'],
                    title: "上传头像",
                    content: "${ctx}/sys/user/imageEdit",
                    yes: function (index, layero) {
                        var body = top.layer.getChildFrame('body', index);
                        var inputForm = body.find('#inputForm');
                        var top_iframe = top.getActiveTab().attr("name");//获取当前active的tab的iframe
                        inputForm.attr("target", top_iframe);//表单提交成功后，从服务器返回的url在当前tab中展示
                        inputForm.validate();
                        if (inputForm.valid()) {
                            jp.loading();
                            inputForm.submit();
                            jp.success("保存成功!");
                        } else {
                            return;
                        }
                        jp.close(index);//关闭对话框。
                    },
                    cancel: function (index) {
                    }
                });
            });
        });
    </script>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-md-6">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">个人资料
                        <a id="userImageBtn" title="更换头像" class="panelButton pull-right"><i class="fa fa-user"></i></a>
                        <a id="userInfoBtn" title="编辑资料" class="panelButton pull-right"><i class="fa fa-edit"></i></a>
                    </h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-sm-4" style="margin-bottom: 10px;">
                            <img alt="image" class="img-responsive" src="${user.photo }"/>
                        </div>
                        <div class="col-sm-8">
                            <div class="table-responsive">
                                <table class="table table-bordered">
                                    <tbody>
                                    <tr>
                                        <td><strong>姓名</strong></td>
                                        <td>${user.name}</td>
                                    </tr>
                                    <tr>
                                        <td><strong>邮箱</strong></td>
                                        <td>${user.email}</td>
                                    </tr>
                                    <tr>
                                        <td><strong>手机</strong></td>
                                        <td>${user.mobile}</td>
                                    </tr>
                                    <tr>
                                        <td><strong>电话</strong></td>
                                        <td>${user.phone}</td>
                                    </tr>
                                    <tr>
                                        <td><strong>公司</strong></td>
                                        <td>${user.company.name}</td>
                                    </tr>
                                    <tr>
                                        <td><strong>归属机构</strong></td>
                                        <td>${user.office.name}</td>
                                    </tr>
                                    <tr>
                                        <td><strong>备注</strong></td>
                                        <td>${user.remarks}</td>
                                    </tr>
                                    </tbody>
                                </table>
                                <strong>上次登录</strong>
                                IP: ${user.oldLoginIp}&nbsp;&nbsp;&nbsp;&nbsp;<fmt:formatDate value="${user.oldLoginDate}" type="both" dateStyle="full"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">登录信息
                        <a id="userPassWordBtn" title="修改密码" class="panelButton pull-right"><i class="fa fa-venus"></i>更换密码</a>
                    </h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-sm-8">
                            <div class="table-responsive">
                                <table class="table table-bordered">
                                    <tbody>
                                    <tr>
                                        <td><strong>用户名</strong></td>
                                        <td>${user.loginName}</td>
                                    </tr>
                                    <tr>
                                        <td><strong>手机号码</strong></td>
                                        <td>${user.mobile}</td>
                                    </tr>
                                    <tr>
                                        <td><strong>用户角色</strong></td>
                                        <td>${user.roleNames}</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="col-sm-4">
                            <img width="100%" style="max-width:200px;max-height:200px;" src="${user.qrCode}" alt="">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>