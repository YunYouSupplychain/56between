<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>波次计划管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmWvHeaderList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">波次列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmWvHeaderEntity" class="form">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">波次单号</label></td>
                                    <td class="width-15">
                                        <form:input path="waveNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">状态</label></td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_WV_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">是否打印</label></td>
                                    <td class="width-15">
                                        <form:select path="isPrint" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">创建人</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/sys/user/popDate" title="选择用户" cssClass="form-control"
                                                       fieldId="createById" fieldName="createBy.id" fieldKeyName="id" fieldValue=""
                                                       displayFieldId="createByName" displayFieldName="createBy.name" displayFieldKeyName="name" displayFieldValue=""
                                                       selectButtonId="createBySelectButton" deleteButtonId="createByDelButton"
                                                       fieldLabels="登录名|姓名" fieldKeys="loginName|name"
                                                       searchLabels="登录名|姓名" searchKeys="loginName|name">
                                        </sys:popSelect>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">创建时间从</label></td>
                                    <td style="width: 15%; vertical-align:middle;">
                                        <div class='input-group date' id='createDateFm'>
                                            <input type='text' name="createDateFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">创建时间到</label></td>
                                    <td style="width: 15%; vertical-align:middle;">
                                        <div class='input-group date' id='createDateTo'>
                                            <input type='text' name="createDateTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">是否获取过面单</label></td>
                                    <td class="width-15">
                                        <form:select path="def2" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">是否打印过面单</label></td>
                                    <td class="width-15">
                                        <form:select path="def3" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="outbound:banQinWmWvHeader:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()">修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvHeader:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvHeader:alloc">
                    <button id="alloc" class="btn btn-primary" disabled onclick="alloc()">分配</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvHeader:dispathPk">
                    <button id="dispathPk" class="btn btn-primary" disabled onclick="dispathPk()">分派拣货</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvHeader:picking">
                    <button id="picking" class="btn btn-primary" disabled onclick="picking()">拣货确认</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvHeader:shipment">
                    <button id="shipment" class="btn btn-primary" disabled onclick="shipment()">发货确认</button>
                </shiro:hasPermission>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">更多<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <shiro:hasPermission name="outbound:banQinWmWvHeader:cancelAlloc">
                            <li><a id="cancelAlloc" onclick="cancelAlloc()">取消分配</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmWvHeader:cancelPicking">
                            <li><a id="cancelPicking" onclick="cancelPicking()">取消拣货</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmWvHeader:cancelShipment">
                            <li><a id="cancelShipment" onclick="cancelShipment()">取消发货</a></li>
                        </shiro:hasPermission>
                    </ul>
                </div>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">打印<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <shiro:hasPermission name="outbound:banQinWmWvHeader:printWaveSorting">
                            <li><a id="printPickingOrder" onclick="printWaveSorting()">打印分拣单</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmWvHeader:printWaveCombinePicking">
                            <li><a id="printWaveCombinePicking" onclick="printWaveCombinePicking()">打印合拣单</a></li>
                            <li><a id="printWaveCombinePickingLandscape" onclick="printWaveCombinePickingLandscape()">打印合拣单（横版）</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmWvHeader:printPackingList">
                            <li><a id="printPackingList" onclick="printPickingList()">打印分拣清单</a></li>
                        </shiro:hasPermission>
                    </ul>
                </div>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">获取面单<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <shiro:hasPermission name="outbound:banQinWmWvHeader:getWaybill">
                            <li><a id="getWaybill" onclick="getWaybill()">获取面单</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmWvHeader:printWaybill">
                            <li><a id="printWaybill" onclick="printWaybill()">打印面单</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmWvHeader:getAndPrintWaybill">
                            <li><a id="getAndPrintWaybill" onclick="getAndPrintWaybill()">获取面单并打印</a></li>
                        </shiro:hasPermission>
                    </ul>
                </div>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmWvHeaderTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
<!-- 分派拣货 -->
<div class="modal fade" id="dispatchPKModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 500px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">分派拣货</h4>
            </div>
            <div class="modal-body">
                <form id="dispatchPKForm">
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td class="width-20"><label class="pull-left">分库区</label></td>
                        </tr>
                        <tr>
                            <td class="width-20"><input id="isByZone" type="checkbox" class="myCheckbox" name="isByZone" onclick="isByZoneChange(this.checked)"></td>
                        </tr>
                        <tr>
                            <td class="width-20"><label class="pull-left">托盘任务</label></td>
                            <td class="width-40"><label class="pull-left">任务数</label></td>
                            <td class="width-40"><label class="pull-left">浮动比例%</label></td>
                        </tr>
                        <tr>
                            <td class="width-20"><input id="isPlTask" type="checkbox" class="myCheckbox" name="isPlTask" onclick="isPlTaskChange(this.checked)"></td>
                            <td class="width-40"><input id="plLimit" type="text" class="form-control" name="plLimit" readonly></td>
                            <td class="width-40"><input id="plFloat" type="text" class="form-control" name="plFloat" readonly></td>
                        </tr>
                        <tr>
                            <td class="width-20"><label class="pull-left">箱拣任务</label></td>
                            <td class="width-40"><label class="pull-left">任务数</label></td>
                            <td class="width-40"><label class="pull-left">浮动比例%</label></td>
                        </tr>
                        <tr>
                            <td class="width-20"><input id="isCsTask" type="checkbox" class="myCheckbox" name="isCsTask" onclick="isCsTaskChange(this.checked)"></td>
                            <td class="width-40"><input id="csLimit" type="text" class="form-control" name="csLimit" readonly></td>
                            <td class="width-40"><input id="csFloat" type="text" class="form-control" name="csFloat" readonly></td>
                        </tr>
                        <tr>
                            <td class="width-20"><label class="pull-left">件拣任务</label></td>
                            <td class="width-40"><label class="pull-left">任务数</label></td>
                            <td class="width-40"><label class="pull-left">浮动比例%</label></td>
                        </tr>
                        <tr>
                            <td class="width-20"><input id="isEaTask" type="checkbox" class="myCheckbox" name="isEaTask" onclick="isEaTaskChange(this.checked)"></td>
                            <td class="width-40"><input id="eaLimit" type="text" class="form-control" name="eaLimit" readonly></td>
                            <td class="width-40"><input id="eaFloat" type="text" class="form-control" name="eaFloat" readonly></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="dispatchPKConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
    <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
</body>
</html>