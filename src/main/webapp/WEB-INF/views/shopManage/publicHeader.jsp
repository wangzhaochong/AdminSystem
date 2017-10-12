<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="cashier-header-box">
    <div class="cashier-header-logotext" id="cashier-header-logo">葫芦微站</div>
    <div class="cashier-header-titletext">收银管理平台</div>
    <div class="cashier-header-rightbox">
        <c:if test="${not empty cashierName}">
            <div class="cashier-header-username">${cashierName}</div>
        </c:if>
        <a href="javascript:;" class="cashier-header-username" id="cashier-header-logout">退出</a>
    </div>
</div>
