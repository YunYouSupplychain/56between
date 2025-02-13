<%@ page contentType="text/html;charset=UTF-8" %>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=9,IE=10"/>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Cache-Control" content="no-store">
<!-- 移动端禁止缩放事件 -->
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<!-- 定义全局变量 -->
<script type="text/javascript">
    var ctx = "${ctx}", ctxStatic = "${ctxStatic}";
    var PRODUCT_NAME = "${fns:getConfig('productName')}";
    var LODOP_VIEW = "${fns:getConfig('lodop.view')}";
    var LODOP_ZH_CN_NAME = "${fns:getConfig('lodop.zh.cn.name')}", LODOP_ZH_CN_LICENSES="${fns:getConfig('lodop.zh.cn.licenses')}";
    var LODOP_ZH_TW_NAME = "${fns:getConfig('lodop.zh.tw.name')}", LODOP_ZH_TW_LICENSES="${fns:getConfig('lodop.zh.tw.licenses')}";
    var LODOP_THIRD_NAME = "${fns:getConfig('lodop.third.name')}", LODOP_THIRD_LICENSES="${fns:getConfig('lodop.third.licenses')}";
    var LODOP_EB_NAME = "${fns:getConfig('lodop.en.name')}", LODOP_EN_LICENSES="${fns:getConfig('lodop.en.licenses')}";
</script>
<!-- 设置浏览器图标 -->
<link rel="shortcut icon" href="${ctxStatic}/56between.ico">
<link rel="stylesheet" href="${ctxStatic}/common/css/vendor.css"/>
<script src="${ctxStatic}/common/js/vendor.js?v=${fns:getConfig('js.version')}"></script>
<!-- 该插件已经集成jquery 无需重复引入 -->
<script src="${ctxStatic}/common/js/moment-locales.js"></script>
<!-- 语言环境插件 -->
<link href="${ctxStatic}/plugin/awesome/4.4/css/font-awesome.min.css" rel="stylesheet"/>
<!-- 引入依赖的第三方插件 -->
<script src="${ctxStatic}/plugin/jquery-plugin/jquery.serializejson.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/plugin/jquery-plugin/jquery.form.js" type="text/javascript"></script>
<script src="${ctxStatic}/plugin/jquery-plugin/jquery.contextMenu.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/plugin/jquery-plugin/jquery.ui.position.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/plugin/jquery-plugin/jquery.contextMenu.min.css">
<script src="${ctxStatic}/plugin/jquery-validation/1.14.0/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/plugin/jquery-validation/1.14.0/localization/messages_zh.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/plugin/jquery-validation/1.14.0/validation-methods.js?v=1.0" type="text/javascript"></script>
<link href="${ctxStatic}/plugin/icheck-1.x/skins/square/_all.css" rel="stylesheet"/>
<script src="${ctxStatic}/plugin/icheck-1.x/icheck.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $('input.i-checks').iCheck({
            checkboxClass: 'icheckbox_square-blue',
            radioClass: 'iradio_square-blue',
            increaseArea: '20%' // optional
        });
    });
</script>
<link href="${ctxStatic}/plugin/city-picker/css/city-picker.css" rel="stylesheet" type="text/css"/>
<script src="${ctxStatic}/plugin/city-picker/js/city-picker.data.js" type="text/javascript"></script>
<script src="${ctxStatic}/plugin/city-picker/js/city-picker.js" type="text/javascript"></script>
<!-- 引入layer插件 -->
<script src="${ctxStatic}/plugin/layui/layer/layer.js"></script>
<script src="${ctxStatic}/plugin/layui/laytpl/laytpl.js"></script>
<!--引入另一个模板文件当做laytpl模板的备用-->
<script src="${ctxStatic}/plugin/tpl/mustache.min.js" type="text/javascript"></script>
<!--引入webuploader-->
<link href="${ctxStatic}/plugin/webuploader-0.1.5/webuploader.css" rel="stylesheet" type="text/css">
<script src="${ctxStatic}/plugin/webuploader-0.1.5/webuploader.js" type="text/javascript"></script>
<!-- 引入toastr -->
<link href="${ctxStatic}/plugin/toastr/toastr.css" rel="stylesheet" type="text/css">
<script src="${ctxStatic}/plugin/toastr/toastr.min.js" type="text/javascript"></script>
<!-- 引入Math.js -->
<script src="${ctxStatic}/plugin/math/math.min.js" type="text/javascript"></script>
<!-- 引入打印插件 -->
<script src="${ctxStatic}/common/js/LodopFuncs.js"></script>
<%--引入自定义文件--%>
<link href="${ctxStatic}/common/css/app-blue.css?v=${fns:getConfig('css.version')}" rel="stylesheet" id="theme"/>
<link href="${ctxStatic}/common/css/56between.css?v=${fns:getConfig('css.version')}" rel="stylesheet"/>
<script src="${ctxStatic}/common/js/56between.js?v=${fns:getConfig('js.version')}"></script>
<script src="${ctxStatic}/common/js/laydate.js"></script>