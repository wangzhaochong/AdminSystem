<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${resourcePath}/js/jquery-1.8.3.min.js?v=${resourceVersion}"></script>
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/common.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/shopManage/shopManage.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/shopManage/common.css?v=${resourceVersion}">
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
            <div class="cashier-index-unchoosemark"></div>
            <div class="cashier-index-firtitle">按日期展示</div>
        </a>
        <a href="/shopManage/dishManageIndex/${storeId}">
            <div class="cashier-index-choosemark"></div>
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
            <div class="mainbox-select-indexlable">菜名</div>
            <input type="text" class="cashier-table-input"/>
            <button class="cashier-search-inputbt" type="button" id="cashier-search-dish">前往</button>
            <div class="mainbox-right-parseDish-box">
                <div class="mainbox-right-test">总数量</div>
                <div class="mainbox-black-num">${totalDishSize}</div>
                <div class="mainbox-right-test">已暂停</div>
                <div class="mainbox-red-num">${parsedDishSize}</div>
            </div>
            <div class="mainbox-right-table">
                <div class="mainbox-table-header">
                    <div class="mainbox-head-item">菜名</div>
                    <div class="mainbox-head-item">分类</div>
                    <div class="mainbox-head-item">优惠价</div>
                    <div class="mainbox-head-item">折扣</div>
                    <div class="mainbox-head-item">原价</div>
                    <div class="mainbox-head-item">今日消费单数</div>
                    <div class="mainbox-head-item">状态</div>
                    <div class="mainbox-head-item">设置售罄</div>
                </div>
                <div class="mainbox-table-body">
                    <c:forEach items="${dishList}" var="vo" varStatus="status">
                        <div class="mainbox-table-bodyline" id="dname-${vo.dishName}">
                            <div class="mainbox-body-item">${vo.dishName}</div>
                            <div class="mainbox-body-item">${vo.cateName}</div>
                            <div class="mainbox-body-item">${vo.dishPriceFinal}</div>
                            <div class="mainbox-body-item">${vo.dishPriceDiscount}</div>
                            <div class="mainbox-body-item">${vo.dishPriceOrigin}</div>
                            <div class="mainbox-body-item">${vo.consumeCount}</div>
                            <c:if test="${vo.status == 1}">
                                <div class="mainbox-body-item font-green">销售中</div>
                                <div class="mainbox-body-item">
                                    <button class="cashier-manage-inputbt" type="button" operate="parseDish">售罄</button>
                                </div>
                            </c:if>
                            <c:if test="${vo.status == 2}">
                                <div class="mainbox-body-item font-red">已售罄</div>
                                <div class="mainbox-body-item">
                                    <button class="cashier-manage-inputbt" type="button"operate="resumeDish">恢复</button>
                                </div>
                            </c:if>
                            <input type="hidden" value="${vo.dishId}"/>
                        </div>
                    </c:forEach>
                    <input type="hidden" id="trCount" value="${fn:length(dishList)}"/>
                    <input type="hidden" id="storeId" value="${storeId}"/>
                    <input type="hidden" id="signIndex" value="-1"/>
                </div>
            </div>
        </div>
    </div>
</div>

<a href="javascript:;"><div class="cashier-back-top" id="cashier-back-top">回到顶部</div></a>

<script type="text/javascript">
    $(document).ready(function(){
        GetTime();
        indexInit();
    });
</script>
<script type="text/javascript" src="${resourcePath}/js/cashierManage/cashindex.js?v=${resourceVersion}"></script>
</body>
</html>
