<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${resourcePath}/js/jquery-1.8.3.min.js?v=${resourceVersion}"></script>
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/manage/guide.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/manage/common.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/common.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/manage/manageIndex.css?v=${resourceVersion}">
    <script type="text/javascript" src="${resourcePath}/js/jquery.md5.js?v=${resourceVersion}"></script>
    <script type="text/javascript">
        function GetTime() {
            now = new Date();
            year = now.getYear();
            month = now.getMonth()+1;
            day = now.getDate();
            if(year > 100 && year < 1000){
                year = year + 1900;
            }
            if(month < 10){
                month = '0' + month;
            }
            if(day < 10){
                day = '0' + day;
            }
            $("#Timer").html(year + "/" + month + "/" + day);
        }
    </script>
</head>
<body class="body">

<div class="manageIndex-outer-box">
    <c:if test="${not empty userType && userType == 'root'}">
        <div class="manageIndex-info-box">
            <div class="manageIndex-info-title">编辑信息</div>
            <div class="manageIndex-info-text">V1原价(6个月)</div>
            <input class="manageIndex-info-input" value="${priceMap['v1_origin_price_6m']}"/>
            <div class="manageIndex-info-text">V1现价(6个月)</div>
            <input class="manageIndex-info-input" value="${priceMap['v1_final_price_6m']}"/>
            <div class="manageIndex-info-text">V1原价(12个月)</div>
            <input class="manageIndex-info-input" value="${priceMap['v1_origin_price_1y']}"/>
            <div class="manageIndex-info-text">V1现价(12个月)</div>
            <input class="manageIndex-info-input" value="${priceMap['v1_final_price_1y']}"/>
            <div class="manageIndex-info-text">V1原价(24个月)</div>
            <input class="manageIndex-info-input" value="${priceMap['v1_origin_price_2y']}"/>
            <div class="manageIndex-info-text">V1现价(24个月)</div>
            <input class="manageIndex-info-input" value="${priceMap['v1_final_price_2y']}"/>
            <div class="manageIndex-info-text">V2原价(6个月)</div>
            <input class="manageIndex-info-input" value="${priceMap['v2_origin_price_6m']}"/>
            <div class="manageIndex-info-text">V2现价(6个月)</div>
            <input class="manageIndex-info-input" value="${priceMap['v2_final_price_6m']}"/>
            <div class="manageIndex-info-text">V2原价(12个月)</div>
            <input class="manageIndex-info-input" value="${priceMap['v2_origin_price_1y']}"/>
            <div class="manageIndex-info-text">V2现价(12个月)</div>
            <input class="manageIndex-info-input" value="${priceMap['v2_final_price_1y']}"/>
            <div class="manageIndex-info-text">V2原价(24个月)</div>
            <input class="manageIndex-info-input" value="${priceMap['v2_origin_price_2y']}"/>
            <div class="manageIndex-info-text">V2现价(24个月)</div>
            <input class="manageIndex-info-input" value="${priceMap['v2_final_price_2y']}"/>
            <div class="manageIndex-info-button" id="manageIndex-info-button">修改</div>
        </div>

        <div class="manageIndex-info-box">
            <div class="manageIndex-info-title">添加管理员</div>
            <div class="manageIndex-info-text">id</div>
            <input class="manageIndex-info-input" id="manageIndex-manage-id" value="${manageUid}"/>
            <div class="manageIndex-info-text">账号名</div>
            <input class="manageIndex-info-input" id="manageIndex-manage-accountName"  value="${manageName}"/>
            <div class="manageIndex-info-button" id="manageIndex-manage-search-button">查找</div>
            <c:if test="${not empty userreq}">
                <div class="manageIndex-info-title">管理员搜索结果</div>
                <div class="manageIndex-info-text">id：${userreq.uid}</div>
                <div class="manageIndex-info-text">账户名：${userreq.accountName}</div>
                <div class="manageIndex-info-text">用户类型：${userreqType}</div>
                <select class="manageIndex-info-input" id="manageIndex-info-select">
                    <option value=0>root管理员</option>
                    <option value=1 selected="selected">普通管理员</option>
                </select>
                <div class="manageIndex-info-button" id="manageIndex-manage-add-button">添加</div>
            </c:if>
        </div>
    </c:if>

    <div class="manageIndex-info-box">
        <div class="manageIndex-info-title" >添加会员</div>
        <div class="manageIndex-info-text">id</div>
        <input class="manageIndex-info-input" id="manageIndex-user-id" value="${userUid}"/>
        <div class="manageIndex-info-text">账号名</div>
        <input class="manageIndex-info-input" id="manageIndex-user-accountName"  value="${userName}"/>
        <div class="manageIndex-info-button" id="manageIndex-user-search-button">查找</div>
        <c:if test="${not empty commonreq}">
            <div class="manageIndex-info-title">管理员搜索结果</div>
            <div class="manageIndex-info-text">id：${commonreq.uid}</div>
            <div class="manageIndex-info-text">账户名：${commonreq.accountName}</div>
            <div class="manageIndex-info-text">用户类型：${userreqType}</div>
            <div class="manageIndex-info-text">到期时间：<fmt:formatDate value="${expireTime}" pattern="yyyy/MM/dd"/></div>
            <div class="manageIndex-info-text">当前日期：<div id="Timer" class="text-inline"></div></div>
            <div class="manageIndex-info-text" id="manageIndex-upgrate-cost">升级费用：0元</div>
            <c:if test="${not empty store}">
                <div class="manageIndex-info-text">商户名：${store.storeName}</div>
                <div class="manageIndex-info-text">商户地址：${store.storeAddress}</div>
            </c:if>
            <select  class="manageIndex-info-input" onchange="flushPriceInput()" id="manageIndex-usertype-select">
                <option value=1>普通户用</option>
                <option value=2 >V1会员</option>
                <option value=3 >V2会员</option>
            </select>
            <select class="manageIndex-info-input" onchange="flushPriceInput()" id="manageIndex-usertime-select">
                <option value=0 v1="${priceMap['v1_final_price_6m']}" v2="${priceMap['v2_final_price_6m']}">6个月</option>
                <option value=1 v1="${priceMap['v1_final_price_1y']}" v2="${priceMap['v2_final_price_1y']}">12个月</option>
                <option value=2 v1="${priceMap['v1_final_price_2y']}" v2="${priceMap['v2_final_price_2y']}">24个月</option>
            </select>
            <div class="manageIndex-info-button" id="manageIndex-user-upgrate-button">提交信息</div>
            <input type="hidden" value="${commonreq.uid}" id="manageIndex-user-commonuid"/>
            <input type="hidden" value="${minusPrice}" id="manageIndex-user-minusPrice"/>
        </c:if>
    </div>
</div>

<script type="text/javascript" src="${resourcePath}/js/manage/manageIndex.js?v=${resourceVersion}"></script>
<script type="text/javascript">
    $(document).ready(function(){
        GetTime();
    });
</script>
</body>
</html>
