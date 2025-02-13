<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>字典管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="dictList.js" %>
    <style>
        #left {
            -webkit-transition: width 0.5s;
            transition: width 0.5s;
        }
    </style>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">字典列表</h3>
        </div>
        <div class="panel-body">
            <div class="row">
                <div id="left" class="col-sm-12">
                    <!-- 搜索 -->
                    <div class="accordion-group">
                        <div id="collapseTwo" class="accordion-body collapse">
                            <div class="accordion-inner">
                                <form id="searchForm">
                                    <table class="table well">
                                        <tbody>
                                        <tr>
                                            <td class="width-15"><label class="pull-right">类型</label></td>
                                            <td class="width-35">
                                                <input id="type" name="type" class="form-control m-b"/>
                                            </td>
                                            <td class="width-15"><label class="pull-right">描述</label></td>
                                            <td class="width-35">
                                                <input id="description" name="description" class="form-control m-b"/>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </form>
                            </div>
                        </div>
                    </div>
                    <!-- 工具栏 -->
                    <div id="toolbar">
                        <shiro:hasPermission name="sys:dict:add">
                            <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:dict:edit">
                            <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="sys:dict:del">
                            <button id="remove" class="btn btn-danger" disabled onclick="del()"> 删除</button>
                        </shiro:hasPermission>
                        <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                        <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                        <a class="accordion-toggle btn btn-default " data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                            <i class="fa fa-search"></i> 检索
                        </a>
                    </div>
                    <!-- 表格 -->
                    <table id="table" data-toolbar="#toolbar" data-id-field="id"></table>
                </div>

                <div id="right" class="panel panel-default col-sm-6" style="display:none">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <label>键值列表，所属类型：</label><span id="dictTypeLabel"></span>
                            <input type="hidden" id="dictTypeId"/>
                        </h3>
                    </div>
                    <div class="panel-body">
                        <div id="dictValueToolbar">
                            <button id="dictValueButton" class="btn btn-primary" title="添加键值"><i class="fa fa-plus-circle"></i> 添加键值</button>
                        </div>
                        <!-- 表格 -->
                        <table id="dictValueTable" data-toolbar="#dictValueToolbar" data-id-field="id"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>