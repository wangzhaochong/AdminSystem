<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${resourcePath}/js/jquery-1.8.3.min.js?v=${resourceVersion}"></script>
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/common.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/shopManage/shopManage.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/shopManage/common.css?v=${resourceVersion}">
    <script language="javascript" type="text/javascript" src="${resourcePath}/js/My97DatePicker/WdatePicker.js?v=${resourceVersion}"></script>
    <script type="text/javascript">
        $(function() {
            setInterval("GetTime()", 5000);
            setInterval("GetSignType(true)", 2900);
        });

        function GetTime() {

            now = new Date();
            hour = now.getHours();
            min = now.getMinutes();
            if (hour < 10) {
                hour = "0" + hour;
            }
            if (min < 10) {
                min = "0" + min;
            }
            $("#Timer").html(hour + ":" + min );

        }
    </script>
</head>
<body class="body">
<jsp:include page="/WEB-INF/views/shopManage/publicHeader.jsp"/>
<div class="cashier-index-content">
    <div class="cashier-index-leftbox">
        <img class="cashier-index-moneylogo" src="${resourcePath}/images/cashierManage/cash.png"/>
        <div class="cashier-index-shopname">${store.storeName}</div>
        <div class="cashier-index-greygap"></div>
        <a href="/shopManage/cashierIndex/${storeId}">
            <div class="cashier-index-unchoosemark"></div>
            <div class="cashier-index-firtitle">按桌号展示</div>
        </a>
        <a href="/shopManage/cashierDateIndex/${storeId}">
            <div class="cashier-index-choosemark"></div>
            <div class="cashier-index-firtitle">按日期展示</div>
        </a>
        <a href="/shopManage/dishManageIndex/${storeId}">
            <div class="cashier-index-unchoosemark"></div>
            <div class="cashier-index-firtitle">菜品管理</div>
        </a>
    </div>
    <div class="cashier-index-rightbox">
        <div class="cashier-index-superview-box">
            <div class="cashier-index-superview">
                <div class="superview-box-item">
                    <div class="superview-box-test">今日总单数</div>
                    <div class="superview-box-test">${cashierStaticInfo.orderCount}</div>
                </div>
                <div class="superview-box-item">
                    <div class="superview-box-test">今日总收入</div>
                    <div class="superview-box-test">${cashierStaticInfo.totalMoney}</div>
                </div>
                <div class="superview-box-item">
                    <div class="superview-box-test">当前进行中</div>
                    <div class="superview-box-test">${finishCount}</div>
                </div>
                <div class="superview-box-item">
                    <div class="superview-box-test">当前时间</div>
                    <div class="superview-box-test" id="Timer">00:00:00</div>
                </div>
            </div>
        </div>
        <div class="cashier-index-mainbox">
            <div class="mainbox-title">历史订单列表</div>
            <div class="mainbox-select-lable">开始时间</div>
            <input type="text" id="cashier-cal-start" value="${startTime}" class="cashier-cal-input" onClick="WdatePicker({lang:'zh-cn',maxDate:new Date(),dateFmt:'yyyy年MM月dd日',skin:'twoer'})"/>
            <div class="mainbox-select-lable">结束时间</div>
            <input type="text" id="cashier-cal-end" value="${endTime}" class="cashier-cal-input" onClick="WdatePicker({lang:'zh-cn',maxDate:new Date(),dateFmt:'yyyy年MM月dd日',skin:'twoer'})"/>
            <button class="cashier-search-inputbt" type="button" id="cashier-search-date">搜索</button>
            <div class="mainbox-right-count-box">
                <div class="mainbox-right-test">总单数</div>
                <div class="mainbox-black-num">${orderRecodeSize}</div>
                <div class="mainbox-right-test">总收银</div>
                <div class="mainbox-black-num">${orderRecodeSumPrice}</div>
            </div>
            <div class="mainbox-right-table">
                <div class="mainbox-table-header">
                    <div class="mainbox-head-item">单号</div>
                    <div class="mainbox-head-item">状态</div>
                    <div class="mainbox-head-item">桌号</div>
                    <div class="mainbox-head-item">总价</div>
                    <div class="mainbox-head-item">下单时间</div>
                    <div class="mainbox-head-item">完成时间</div>
                    <div class="mainbox-head-item">商家备注</div>
                    <div class="mainbox-head-item">详情</div>
                </div>
                <div class="mainbox-table-body">
                    <c:forEach items="${orderRecodes}" var="vo" varStatus="status">
                        <div class="mainbox-table-bodyline" tid="${vo.tableId}" id="tid-${vo.tableId}">
                            <div class="mainbox-body-item">${vo.orderId}</div>
                            <c:if test="${vo.status == 1}">
                                <div class="mainbox-body-item font-green">进行中</div>
                            </c:if>
                            <c:if test="${vo.status == 2}">
                                <div class="mainbox-body-item font-yellow">已完成</div>
                            </c:if>
                            <div class="mainbox-body-item">${vo.tableId}</div>
                            <div class="mainbox-body-item">￥${vo.totalPrice}</div>
                            <div class="mainbox-body-item"> <fmt:formatDate value="${vo.summitTime}" pattern="MM/dd HH时mm分"/></div>
                            <c:if test="${vo.status == 1}">
                                <div class="mainbox-body-item"></div>
                            </c:if>
                            <c:if test="${vo.status == 2}">
                                <div class="mainbox-body-item"> <fmt:formatDate value="${vo.finishTime}" pattern="MM/dd HH时mm分"/></div>
                            </c:if>
                            <div class="mainbox-body-item">${vo.manageComment}</div>
                            <div class="mainbox-body-item">
                                <c:choose>
                                    <c:when test="${not empty vo}">
                                        <input type="hidden" value="${vo.id}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" value="-1"/>
                                    </c:otherwise>
                                </c:choose>
                                <button class="cashier-manage-inputbt" type="button" operate="detail">查看</button>
                            </div>
                        </div>
                    </c:forEach>
                    <input type="hidden" id="trCount" value="${fn:length(orderRecodes)}"/>
                    <input type="hidden" id="storeId" value="${storeId}"/>
                    <input type="hidden" id="signIndex" value="-1"/>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="cashier-mask-grey" id="cashier-mask-grey"></div>

<a href="javascript:;"><div class="cashier-back-top" id="cashier-back-top">回到顶部</div></a>

<div class="cashier-order-detail-box" id="cashier-order-detail-box">
    <div class="order-detail-content-box">
        <div class="order-detail-orderId" id="order-detail-orderId">桌号：</div>
        <div class="order-detail-price" id="order-detail-totalPrice">订单总价：</div>
        <div class="order-detail-tableheader">
            <div class="order-detail-headeritem">菜名</div>
            <div class="order-detail-headeritem">单价</div>
            <div class="order-detail-headeritem">数量</div>
            <div class="order-detail-headeritem">总价</div>
            <div class="order-detail-headeritem">原价</div>
            <div class="order-detail-headeritem">折扣</div>
        </div>
        <div class="order-detail-tabletr-box" id="order-detail-tabletr-box"></div>
        <%--<div class="order-detail-tabletr">--%>
        <%--<div class="order-detail-bodyitem-dishname">鸡鸡菜一二三四五六七八九十</div>--%>
        <%--<div class="order-detail-bodyitem">22</div>--%>
        <%--<div class="order-detail-bodyitem">--%>
        <%--<div class="order-detail-minus">-</div>--%>
        <%--1--%>
        <%--<div class="order-detail-plus">+</div>--%>
        <%--</div>--%>
        <%--<div class="order-detail-bodyitem">22</div>--%>
        <%--<div class="order-detail-bodyitem">28</div>--%>
        <%--<div class="order-detail-bodyitem">8.2</div>--%>
        <%--</div>--%>
        <div class="order-detail-commenttitle">用户备注</div>
        <textarea id = "order-detail-customcomment" class="order-detail-commentbox font-grey" readonly="readonly">顾客的评论</textarea>
        <div class="order-detail-commenttitle">商家备注</div>
        <textarea id = "order-detail-managecomment" onblur="postComment(false)" class="order-detail-commentbox">商家的备注</textarea>
        <a href="javascript:;"><div class="order-detail-button-left" id="order-detail-button-cancle">取消</div></a>
        <div class="order-detail-buttonbox" orderId="-1" price="-1" tableId="-1" size="0" id="order-detail-buttonbox">
            <a href="javascript:;"><div class="order-detail-button" id="order-detail-addbt">加菜</div></a>
            <a href="javascript:;"><div class="order-detail-button" id="order-detail-paybt">保存并结单</div></a>
            <a href="javascript:;"><div class="order-detail-button" id="order-detail-canclebt">撤单</div></a>
        </div>
        <div class="order-detail-buttonbox" id="order-detail-buttonbox-finished">
            <a href="javascript:;"><div class="order-detail-button" onclick="postComment(true)">保存备注</div></a>
        </div>
    </div>
    <div class="add-order-box" id="add-order-box">
        <div class="add-order-header">添加菜品<img src="${resourcePath}/images/customMenu/closemark.png" class="order-detail-close" id="order-addbox-close"/></div>
        <div class="add-order-listbox">
            <c:forEach items="${cateWithDishList}" var="cateMap">
                <div class="add-order-cateitem">${cateMap.key}</div>
                <c:forEach items="${cateMap.value}" var="dish">
                    <div class="add-order-dish-box" did="${dish.dishId}" oriPrice="${dish.dishPriceOrigin}" discount="${dish.dishPriceDiscount}" finalPrice="${dish.dishPriceFinal}">
                        <div class="add-order-dish-name">${dish.dishName}</div>
                        <a href="javascript:;"><div class="add-order-dish-add">添加</div></a>
                    </div>
                </c:forEach>
            </c:forEach>
        </div>
    </div>
    <img src="${resourcePath}/images/customMenu/closemark.png" class="order-detail-close" id="order-detail-close"/>
</div>
<script type="text/javascript">
    $(document).ready(function(){
        GetTime();
        indexInit();
    });
</script>
<script type="text/javascript" src="${resourcePath}/js/cashierManage/cashindex.js?v=${resourceVersion}"></script>
</body>
</html>
