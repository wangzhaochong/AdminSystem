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
        var storeId = ${store.uid};
        var tableId = ${tableId};
    </script>
</head>

<body class="body">

<jsp:include page="/WEB-INF/views/customMenu/publicHeader.jsp"/>

<c:if test="${not empty orderWait}">
    <div class="order-wait-box">
        您有&#10;
        <div class="order-wait-ahref" id="order-wait-ahref">订单&#10;</div>
        正在&#10;
        处理&#10;
    </div>
</c:if>

<img id="orderlist-category-bgimg" class="orderlist-category-bgimg" src="${resourcePath}/images/customMenu/category.png">
<div id="orderlist-open-category" class="orderlist-open-category">
    菜&#10;
    品&#10;
    分&#10;
    类&#10;
</div>
<div id="orderlist-catelist" class="orderlist-catelist">
    <div class="orderlist-catelist-listname">菜品分类</div>
    <img src="${resourcePath}/images/customMenu/closemark.png" class="orderlist-catelist-closemark" id="orderlist-catelist-closemark"/>
    <c:forEach items="${cateNames}" var="cateName" varStatus="status">
        <div class="orderlist-catelist-catename" refaddr="#cateIndex:${status.index}">${cateName}</div>
    </c:forEach>
</div>

<img class="store-info-maskimg" src="${resourcePath}/images/customMenu/storename.png"/>
<div class="store-info-maskname">${store.storeName}</div>
<div class="store-info-maskaddress">${store.storeAddress}</div>
<div class="store-img-block">
    <img src="${store.storeHeadimg}" class="store-img"/>
</div>

<div class="gap-item">商家信息</div>

<div class="store-info-block">
    <div class="store-info-name">
        <img src="${resourcePath}/images/customMenu/fork.png" class="store-info-logo"/>
        ${store.storeName}
    </div>
    <div class="store-info-location">
        <img src="${resourcePath}/images/customMenu/map.png" class="store-info-logo"/>
        <div class="store-address-box">
            ${store.storeAddress}
        </div>
    </div>
    <div class="store-info-tel">
        <img src="${resourcePath}/images/customMenu/phone.png" class="store-info-logo"/>
        <c:if test="${not empty store.storeMobile}"> ${store.storeMobile}</c:if>
        <c:if test="${empty store.storeMobile}">暂无</c:if>
    </div>
    <div class="dish-info-triangle-box">
        <img src="${resourcePath}/images/customMenu/triangle.png" class="dish-info-triangle"/>
    </div>
</div>
<div class="gap-bare"></div>


<div class="dish-list-block">
    <c:forEach items="${cateWithDishList}" var="cateMap" varStatus="status">
        <div class="cate-name" id="cateIndex:${status.index}">${cateMap.key}</div>
        <c:forEach items="${cateMap.value}" var="dish">
            <div did="${dish.dishId}" dname="${dish.dishName}" dprice="${dish.dishPriceFinal}" class="dish-item-border">
                <img src="${dish.dishImg}" class="dish-item-img"/>
                <div class="dish-item-leftsec">
                    <div class="dish-item-name">${dish.dishName}</div>
                    <div class="dish-item-ingred">主要配料：${dish.dishIngredient}</div>
                </div>
                <div class="dish-item-rightsec">
                    <div class="dish-item-rightsec-price">
                        <c:if test="${dish.dishPriceOrigin != dish.dishPriceFinal}">
                            <div class="dish-item-price-origin">￥${dish.dishPriceOrigin}</div>
                        </c:if>
                        <div class="dish-item-price-final">￥${dish.dishPriceFinal}</div>
                    </div>
                    <c:if test="${dish.status == 1}">
                        <div class="dish-item-diancai">点&nbsp;菜</div>
                    </c:if>
                    <c:if test="${dish.status == 2}">
                        <div class="dish-item-soldout">售&nbsp;罄</div>
                    </c:if>
                </div>
            </div>
            <div class="dish-item-description">*备注信息：${dish.dishAbstract}</div>
        </c:forEach>
    </c:forEach>
</div>

<div class="dish-xiadan-block">
    <div class="dish-xiadan-price" id="dish-xiadan-price">￥0</div>
    <div class="dish-xiadan-button" id="dish-xiadan-button">立即下单</div>
</div>
<img src="${resourcePath}/images/customMenu/shopcar.png" class="dish-xiadan-shopchar" id="dish-xiadan-shopcar"/>
<div class="shopcar-menu-list" id="shopcar-menu-list">
    <div class="shopcar-menu-title">购物车</div>
    <div class="shopcar-menu-item-box" id="shopcar-menu-item-box">
        <%--<div class="shopcar-menu-item" >--%>
        <%--<div class="shopcar-menu-name">烧鸭鸭</div>--%>
        <%--<div class="shopcar-menu-item-price">￥50</div>--%>
        <%--<div class="shopcar-menu-item-minus">-</div>--%>
        <%--<div class="shopcar-menu-count">1</div>--%>
        <%--<div class="shopcar-menu-item-plus">+</div>--%>
        <%--</div>--%>
    </div>
</div>


</body>
</html>
