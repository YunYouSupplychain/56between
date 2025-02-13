<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>运输价格管理</title>
    <meta name="decorator" content="ani"/>
    <style type="text/css">
        #transport-price-table {
            width: 100%;
            height: calc(100% - 80px);
            padding: 0 10px;
        }

        #transport-price {
            float: left;
            width: 70%;
            height: 100%;
            background-color: #FFFFFF;
            overflow: auto;
        }

        #transport-step-price {
            float: left;
            width: 30%;
            height: 100%;
            background-color: #FFFFFF;
            overflow: auto
        }
    </style>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsContractTransportPriceList.js" %>
</head>
<body>
<form:form id="inputForm" modelAttribute="bmsContractCostItemEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <div class="tabs-container">
        <table class="table well">
            <tr>
                <td class="width-10"><label class="pull-right asterisk">运输价格体系</label></td>
                <td class="width-25">
                    <sys:grid title="选择运输价格体系" url="${ctx}/bms/basic/bmsTransportGroup/data" cssClass="form-control required"
                              fieldId="transportGroupCode" fieldKeyName="transportGroupCode"
                              fieldName="transportGroupCode" fieldValue="${bmsContractCostItemEntity.transportGroupCode}"
                              displayFieldId="transportGroupName" displayFieldKeyName="transportGroupName"
                              displayFieldName="transportGroupName" displayFieldValue="${bmsContractCostItemEntity.transportGroupName}"
                              fieldLabels="运输体系编码|运输体系名称" fieldKeys="transportGroupCode|transportGroupName"
                              searchLabels="运输体系编码|运输体系名称" searchKeys="transportGroupCode|transportGroupName"
                              queryParams="orgId" queryParamValues="orgId"
                              afterSelect="transportGroupAfterSelect"/>
                </td>
                <td class="width-25">
                    <a class="btn btn-primary" onclick="jumpTransportGroup();">运输价格体系</a>
                </td>
                <td class="width-15"></td>
                <td class="width-10"></td>
                <td class="width-15"></td>
            </tr>
        </table>
    </div>
</form:form>
<div id="transport-price-table">
    <div id="transport-price">
        <table id="bmsTransportPriceTable" class="table text-nowrap"></table>
    </div>
    <div id="transport-step-price">
        <table id="bmsTransportStepPriceTable" class="table text-nowrap"></table>
    </div>
</div>
</body>
</html>