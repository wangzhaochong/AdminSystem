<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${resourcePath}/js/jquery-1.8.3.min.js?v=${resourceVersion}"></script>
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/customMenu/list.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/common.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/customMenu/common.css?v=${resourceVersion}">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="format-detection" content="telephone=no" />
    <script>
        var storeId = ${storeId};
        var tableId = ${tableId};
    </script>
</head>

<body class="body">

<jsp:include page="/WEB-INF/views/customMenu/publicHeader.jsp"/>

<div class="order-suc-block">
<img src="${resourcePath}/images/customMenu/suc.png" class="order-suc-sucimg"/>
    <div class="order-suc-word1">恭喜您！</div>
    <div class="order-suc-word2">下单成功</div>
</div>
<div class="gap-item">订单信息</div>
<div class="order-list-totalbox" id="order-list-totalbox">
    <c:forEach var="dish" items="${list}">
        <div class="order-list-itembox">
            <div class="order-list-leftwords">${dish.dishName}</div>
            <div class="order-list-price">￥${dish.dishPrice}</div>
            <div class="order-list-count">x${dish.count}</div>
        </div>
    </c:forEach>
</div>
<div class="order-list-pricebox">
    <div class="order-list-leftwords">总价格</div>
    <div class="order-list-price" id="order-list-price">￥${price}</div>
</div>
<div class="gap-item">备注信息</div>
<c:choose>
    <c:when test="${not empty comment}">
        <textarea class="order-list-comment" id="order-list-comment" readonly="readonly">${comment}</textarea>
        <button class="order-list-comment-edit" type="button" id="order-list-comment-edit" operate="edit">编辑</button>
    </c:when>
    <c:otherwise>
        <textarea class="order-list-comment" id="order-list-comment" readonly="readonly">*输入您对菜品的备注或评论</textarea>
        <button class="order-list-comment-edit" type="button" id="order-list-comment-edit" operate="new">编辑</button>
    </c:otherwise>
</c:choose>



<button class="order-list-backbt" type="button" id="order-list-backbt" >返回菜单</button>
<script type="text/javascript" src="${resourcePath}/js/customMenu/list.js?v=${resourceVersion}"></script>
<%--<script>--%>
    <%--$(document).ready(function(){--%>
        <%--initOrderBasic();--%>
    <%--});--%>
<%--</script>--%>
</body>
</html>
