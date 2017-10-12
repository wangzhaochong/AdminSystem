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
    <script type="text/javascript" src="${resourcePath}/js/jquery.PrintArea.js"></script>
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
            <div class="cashier-index-choosemark"></div>
            <div class="cashier-index-firtitle">按桌号展示</div>
        </a>
        <a href="/shopManage/cashierDateIndex/${storeId}">
            <div class="cashier-index-unchoosemark"></div>
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
                    <div class="superview-box-test">${cashierStaticInfo.unfinishTable}</div>
                </div>
                <div class="superview-box-item">
                    <div class="superview-box-test">当前时间</div>
                    <div class="superview-box-test" id="Timer">00:00:00</div>
                </div>
            </div>
        </div>
        <div class="cashier-index-mainbox">
            <div class="mainbox-title">餐桌当前状态</div>
            <div class="mainbox-select-indexlable">桌号</div>
            <input type="text" class="cashier-table-input"/>
            <button class="cashier-search-inputbt" type="button" onclick="gotoTid(this)">前往</button>
            <div class="mainbox-right-superview-box">
                <div class="mainbox-right-test">总桌数</div>
                <div class="mainbox-black-num">${cashierStaticInfo.totalTable}</div>
                <div class="mainbox-right-test">已暂停</div>
                <div class="mainbox-black-num">${cashierStaticInfo.parsedTable}</div>
                <div class="mainbox-right-test">进行中</div>
                <div class="mainbox-red-num">${cashierStaticInfo.unfinishTable}</div>
                <div class="mainbox-right-test">空闲中</div>
                <div class="mainbox-black-num">${cashierStaticInfo.finishTable}</div>
            </div>
            <div class="mainbox-right-tableup">
                <div class="mainbox-table-header">
                    <div class="mainbox-head-item">桌号</div>
                    <div class="mainbox-head-item">状态</div>
                    <div class="mainbox-head-item">总价</div>
                    <div class="mainbox-head-item">下单时间</div>
                    <div class="mainbox-head-item">顾客备注</div>
                    <div class="mainbox-head-item">订单详情</div>
                    <div class="mainbox-head-item">打印收据</div>
                    <div class="mainbox-head-item">暂停/恢复接单</div>
                </div>
                <div class="mainbox-table-body">
                    <c:forEach items="${cashierOrderVos}" var="vo" varStatus="status">
                        <div class="mainbox-table-bodyline" tid="${vo.tableId}" id="tid-${vo.tableId}">
                            <div class="mainbox-body-item">${vo.tableId}</div>
                                <%--暂停状态--%>
                            <c:if test="${vo.exclude}">
                                <div class="mainbox-body-item font-red">暂停中</div>
                                <div class="mainbox-body-item"></div>
                                <div class="mainbox-body-item"></div>
                                <div class="mainbox-body-item"></div>
                            </c:if>
                            <c:if test="${not vo.exclude && not empty vo.orderRecode}">
                                <%--非暂停状态，存在下单记录，并且是进行状态--%>
                                <c:if test="${vo.orderRecode.status == 1}">
                                    <div class="mainbox-body-item font-yellow">进行中</div>
                                    <div class="mainbox-body-item">￥${vo.orderRecode.totalPrice}</div>
                                    <div class="mainbox-body-item"> <fmt:formatDate value="${vo.orderRecode.summitTime}" pattern="MM/dd HH时mm分"/></div>
                                    <div class="mainbox-body-item">${vo.orderRecode.customComment}</div>
                                </c:if>
                                <%--非暂停状态，存在下单记录，并且是空闲状态--%>
                                <c:if test="${vo.orderRecode.status == 2}">
                                    <div class="mainbox-body-item font-green">空闲中</div>
                                    <div class="mainbox-body-item"></div>
                                    <div class="mainbox-body-item"></div>
                                    <div class="mainbox-body-item"></div>
                                </c:if>
                            </c:if>
                                <%--非暂停状态，不存在下单记录，认为是空闲状态--%>
                            <c:if test="${not vo.exclude && empty vo.orderRecode}">
                                <div class="mainbox-body-item font-green">空闲中</div>
                                <div class="mainbox-body-item"></div>
                                <div class="mainbox-body-item"></div>
                                <div class="mainbox-body-item"></div>
                            </c:if>

                            <div class="mainbox-body-item">
                                <c:choose>
                                    <c:when test="${not vo.exclude && not empty vo.orderRecode}">
                                        <input type="hidden" value="${vo.orderRecode.id}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" value="-1"/>
                                    </c:otherwise>
                                </c:choose>
                                <button class="cashier-manage-inputbt" type="button" operate="detail">查看</button>
                            </div>

                            <div class="mainbox-body-item">
                                <c:choose>
                                    <c:when test="${not vo.exclude && not empty vo.orderRecode}">
                                        <input type="hidden" value="${vo.orderRecode.id}"/>
                                        <input type="hidden" value="${vo.orderRecode.totalPrice}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" value="-1"/>
                                        <input type="hidden" value="-1"/>
                                    </c:otherwise>
                                </c:choose>
                                    <%--<button class="cashier-manage-inputbt" type="button" operate="pay">完成</button>--%>
                                <button class="cashier-manage-inputbt" type="button" operate="print">打印</button>
                            </div>
                            <div class="mainbox-body-item">
                                <c:choose>
                                    <c:when test="${vo.exclude}">
                                        <button class="cashier-manage-inputbt" type="button" operate="resume">恢复</button>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="cashier-manage-inputbt" type="button" operate="parse">暂停</button>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </c:forEach>
                    <input type="hidden" id="trCount" value="${fn:length(cashierOrderVos)}"/>
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
        <textarea id = "order-detail-managecomment" onblur="postComment()" class="order-detail-commentbox">商家的备注</textarea>
        <a href="javascript:;"><div class="order-detail-button-left" id="order-detail-button-cancle">取消</div></a>
        <div class="order-detail-buttonbox" orderId="-1" price="-1" tableId="-1" size="0" id="order-detail-buttonbox">
            <a href="javascript:;"><div class="order-detail-button" id="order-detail-addbt">加菜</div></a>
            <a href="javascript:;"><div class="order-detail-button" id="order-detail-paybt">保存并结单</div></a>
            <a href="javascript:;"><div class="order-detail-button" id="order-detail-canclebt">撤单</div></a>
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

<div id="print-receive-box" class="print-receive-box">
    打印的地方
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
