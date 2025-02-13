<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta name="decorator" content="ani"/>
    <title>${fns:getConfig('productName')}</title>
    <link href="${ctxStatic}/common/css/jp-menu.css?v=${fns:getConfig('css.version')}" rel="stylesheet"/>
    <script src="${ctxStatic}/plugin/js-menu/contabs.js"></script>
    <link id="theme-tab" href="${ctxStatic}/plugin/js-menu/menuTab-${cookie.theme.value==null?'blue':cookie.theme.value}.css" rel="stylesheet"/>
    <style>
        .push-right #sidebar-nav {
            display: block;
        }

        .navbar-inverse .navbar-toggle:hover, .navbar-inverse .navbar-toggle:focus {
            background-color: transparent !important;
        }

        .navbar-inverse .navbar-toggle {
            border-color: transparent !important;
        }

        .selectCompany {
            background-color: #141414;
            border-color: #0d1318;
            padding: 6px 12px;
            height: 30px;
            line-height: 1.5;
        }

        .short-name {
            width: 50px !important;
            max-height: 30px !important;
            float: right !important;
            overflow: hidden !important;
            text-overflow: ellipsis !important;
            -o-text-overflow: ellipsis !important;
            white-space: nowrap !important;
        }

        .collapse ul > li > a {
            font-size: 14px;
        }
    </style>
</head>

<body class="jp-hr">
<nav class="navbar navbar-inverse display-default opnav-navbar navbar-fixed-top" role="navigation">
    <div class="topnav-navbar">
        <div class="navbar-header text-center">
            <button type="button" class="navbar-toggle" id="showMenu">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand J_menuItem" href="${ctx}/home" data-index="0" style="color: white">${fns:getConfig('productName')}</a>
        </div>
    </div>
    <div class="navbar-container container-fluid">
        <div class="collapse navbar-collapse navbar-collapse-toolbar row" id="jpMenu">
            <ul id="o1" class="nav navbar-toolbar nav-tabs navbar-left" role="tablist" style="width: calc( 100% - 560px)">
                <t:jpMenu menu="${fns:getTopMenu()}" position="top"></t:jpMenu>
            </ul>
            <ul class="nav navbar-nav pull-right navbar-right" style="max-width: 365px;">
                <li>
                    <div class="row" style="margin: 10px 10px 0 0;">
                        <label class="pull-left" style="margin-right: 8px;color: white;" title="机构">机构</label>
                        <div class="input-group" style="width: 200px;">
                            <input id="companyId" type="hidden"/>
                            <input id="companyCode" type="hidden"/>
                            <input id="companyName" type="text" class="form-control" readonly="readonly" style="background-color: #e7e7e7;"/>
                            <span class="input-group-btn">
                                <button type="button" class="btn btn-primary selectCompany" onclick="selectCompany()" ><i class="fa fa-search"></i></button>
                            </span>
                        </div>
                    </div>
                </li>
                <li class="dropdown admin-dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
                        <img src="<c:if test="${fns:getUser().photo == null || fns:getUser().photo==''}">${ctxStatic}/common/images/flat-avatar.png</c:if><c:if test="${fns:getUser().photo != null && fns:getUser().photo!=''}">${fns:getUser().photo}</c:if>"
                             class="topnav-img" style="max-width: 28px;max-height: 28px;margin-right: 5px;" alt="">
                        <span class="hidden-sm short-name">${fns:getUser().name}</span>
                    </a>
                    <ul class="dropdown-menu animated fadeIn" role="menu">
                        <li><a href="#" onclick="modifyPw()">修改密码</a></li>
                        <li><a href="${ctx}/logout">安全退出</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>
<aside id="sidebar">
    <div class="tab-content">
        <div id="page-wrapper">
            <div id="nav-col" class="nav-col">
                <section id="col-left" class="nano">
                    <div id="col-left-inner" class="nano-content">
                        <div class="collapse navbar-collapse navbar-ex1-collapse tab-content" id="sidebar-nav">
                            <t:jpMenu menu="${fns:getTopMenu()}" position="left"></t:jpMenu>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    </div>
</aside>
<section id="body-container" class="animation">
    <!--选项卡  -->
    <div class="main-container" id="main-container">
        <div class="main-content">
            <div class="main-content-inner">
                <div id="breadcrumbs" class="${cookie.tab.value!=false?'breadcrumbs':'un-breadcrumbs'}">
                    <div class="content-tabs">
                        <a id="hideMenu" class="roll-nav roll-left-0 J_tabLeft"><i class="fa fa-align-justify"></i></a>
                        <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i></button>
                        <nav class="page-tabs J_menuTabs">
                            <div class="page-tabs-content">
                                <a href="javascript:" class="active J_menuTab" data-id="${ctx}/home">首页</a>
                            </div>
                        </nav>
                        <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i></button>
                        <a href="${ctx}/logout" class="roll-nav roll-right J_tabExit"><i class="fa fa-sign-out"></i> 退出</a>
                    </div>
                </div>
                <div class="J_mainContent" id="content-main" style="${cookie.tab.value!=false?'height:calc(100% - 40px)':'height:calc(100%)'}">
                    <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="${ctx}/home" data-id="${ctx}/home" frameborder="0" seamless></iframe>
                </div>
            </div>
        </div>
    </div>
    <div class="footer">
        <div class="pull-left"><a href="#" target="_blank">上海芸柚供应链科技有限公司</a> &copy; 2025-2032</div>
    </div>
</section>

<script>
    var lastValue;
    var currentOrgId;
    $(function () {
        $('#showMenu').click(function () {
            $('body').toggleClass('push-right');
        });
        $('#hideMenu').click(function () {
            $('#page-wrapper').toggleClass('nav-small');
            $('#col-left').toggleClass('nano');
            $('body').removeClass('push-right').toggleClass('push-left');
        });

        $(".theme-icons label").click(function () {
            $("#theme-font").remove();
            $(this).append('<i id="theme-font" class=" anticon anticon-check ng-star-inserted fa fa-check "></i>');
            changeTheme($(this).attr('data-theme'));
        });

        currentOrgId = "${fns:getUser().office.id}";
        var company = ${fns:toJson(fns:getOffice(fns:getUser().office.id))};
        if (company) {
            $('#companyCode').val(company.code);
            $('#companyName').val(company.name);
            $('#companyType').val(company.type);
        }
        $('#companyId').val(currentOrgId);
        lastValue = $('#companyId').val();
        $(window).resize();
    });

    function selectCompany() {
        top.layer.open({
            type: 2,
            area: ['1000px', '650px'],
            title: "选择机构",
            auto: true,
            name: 'friend',
            content: "${ctx}/tag/popSelect?url=" + encodeURIComponent("${ctx}/sys/office/companyData?id=") + currentOrgId
                + "&fieldLabels=" + encodeURIComponent("机构代码|机构名称") + "&fieldKeys=" + encodeURIComponent("code|name")
                + "&searchLabels=" + encodeURIComponent("机构代码|机构名称") + "&searchKeys=" + encodeURIComponent("code|name")
                + "&isMultiSelected=false",
            btn: ['确定', '关闭'],
            yes: function (index, layero) {
                // 得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                var iframeWin = layero.find('iframe')[0].contentWindow;
                var items = iframeWin.getSelections();
                if (items === "") {
                    jp.warning("必须选择一条数据!");
                    return;
                }
                $("#companyId").val(items[0].id);
                $("#companyName").val(items[0].name);
                top.layer.close(index);
                if (lastValue !== items[0].id) {
                    lastValue = $("#companyId").val();
                    closeAllTabs();
                }
            },
            cancel: function (index, layero) {
                var row = layero.find('iframe')[0].contentWindow.currentRow;
                if (row) {
                    $("#companyId").val(row.id);
                    $("#companyName").val(row.name);
                    if (lastValue !== row.id) {
                        lastValue = $("#companyId").val();
                        closeAllTabs();
                    }
                }
            }
        });
    }

    $(function ($) {
        $('#sidebar-nav  a').on('click', function (e) {
            var _this = $(this);
            $('#sidebar-nav  a').each(function () {
                if ($(this) != _this) {
                    if ($(this).hasClass("active")) {
                        $(this).removeClass("active");
                    }
                }
            });
            if (!_this.hasClass("active")) {
                _this.addClass("active")
                _this.parent("li.open").find("a").first().addClass("active");
            }
        });
        $('#sidebar-nav .dropdown-toggle').on('click', function (e) {
            e.preventDefault();
            var $item = $(this).parent();
            if (!$item.hasClass('open')) {
                $item.parent().find('.open .submenu').slideUp('fast');
                $item.parent().find('.open').toggleClass('open');
            }
            $item.toggleClass('open');
            if ($item.hasClass('open')) {
                $item.children('.submenu').slideDown('fast');
            } else {
                $item.children('.submenu').slideUp('fast');
            }
            if ($("#user-menu").parent().hasClass('open')) {
                $("#user-menu").slideUp('fast');
                $("#user-menu").parent().toggleClass('open');
            }
        });
        $('#user-link').on('click', function () {
            var $item = $("#user-menu");
            $item.parent().toggleClass('open');
            if ($item.parent().hasClass('open')) {
                $item.slideDown('fast');
            } else {
                $item.slideUp('fast');
            }
            $("#sidebar-nav .tab-pane").find('.open .submenu').slideUp('fast');
            $("#sidebar-nav .tab-pane").find('.open').toggleClass('open');
        });
        $('body').on('mouseenter', '#page-wrapper.nav-small #sidebar-nav .dropdown-toggle', function (e) {
            var $sidebar = $(this).parents('#sidebar-nav');
            if ($(document).width() >= 992) {
                var $item = $(this).parent();
                $item.addClass('open');
                $item.children('.submenu').slideDown('fast');
            }
        });
        $('body').on('mouseleave', '#page-wrapper.nav-small #sidebar-nav > div > .nav-pills > li', function (e) {
            var $sidebar = $(this).parents('#sidebar-nav');
            if ($(document).width() >= 992) {
                var $item = $(this);
                if ($item.hasClass('open')) {
                    $item.find('.open .submenu').slideUp('fast');
                    $item.find('.open').removeClass('open');
                    $item.children('.submenu').slideUp('fast');
                }
                $item.removeClass('open');
            }
        });
    });

    function modifyPw() {
        top.layer.open({
            type: 2,
            area: ['600px', '320px'],
            title: '修改密码',
            maxmin: false,
            content: '${ctx}/sys/user/modifyPwd',
            btn: ['保存', '取消'],
            yes: function (index, layero) {
                var iframeWin = layero.find('iframe')[0];
                iframeWin.contentWindow.doSubmit(index);
            },
            cancel: function (index) {
            }
        });
    }
</script>
</body>
</html>