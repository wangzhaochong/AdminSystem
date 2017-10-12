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
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/shopManage/cashierLogin.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/shopManage/common.css?v=${resourceVersion}">
    <script type="text/javascript" src="${resourcePath}/js/jquery.md5.js?v=${resourceVersion}"></script>
</head>
<body class="body">
<div class="background-grey">
    <div class="cashier-login-box">
        <div class="cashier-login-header">
            <div class="cashier-login-text logo-text">葫芦微站</div>
            <div class="cashier-login-text title-text">收银管理平台</div>
        </div>
        <div class="cashier-login-inputbox">
            <div class="cashier-login-inputlable">用户名：</div>
            <input class="cashier-login-input" id="cashier-uname-input"/>
            <div class="cashier-login-inputlable">密&nbsp;&nbsp;&nbsp;&nbsp;码：</div>
            <input class="cashier-login-input" id="cashier-passwd-input"/>
            <a href="javascript:;"><div class="cashier-login-inputbt" id="cashier-login-inputbt">登&nbsp;录</div></a>
            <div class="cashier-login-infomessage" id="cashier-login-infomessage">${cashier_message}</div>
            <input type="hidden" value="${storeId}" id="cashier-storeid"/>
        </div>
    </div>
</div>
<script type="text/javascript" src="${resourcePath}/js/cashierManage/cashierLogin.js?v=${resourceVersion}"></script>
</body>
</html>
