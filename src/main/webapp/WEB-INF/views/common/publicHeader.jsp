<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
    var islogin = false;
    if('true' == '${islogin}'){
        islogin = true;
    }
</script>

<div class="top-item-manage">
    <div class="top-item-logo-module">
        <a href="/index/manageIndex/">
            <img class="top-item-logo" src="${resourcePath}/images/common/logo.png">
        </a>
    </div>
    <div class="top-item-login-module">
        <a href="/user/login/"><div id='loginLink' class="top-item-login">登录</div></a>
        <a href="/user/logout/?curPath=${curPath}"><div id='logoutLink' class="top-item-login">退出</div></a>
    </div>
</div>

<div class="broadcast-bobble" id="broadcast-bobble">
    <div class="broadcast-bobble-innerbox">
        <div class="broadcast-bobble-text">葫芦微站7月特惠，会员产品限期优惠，<a href="/user/userCenter#user-center-upgradebox">查看详情</a>。</div>
        <div class="broadcast-bobble-close" id="broadcast-bobble-close">关闭</div>
    </div>
</div>

<script type="text/javascript" src="${resourcePath}/js/common/loginLink.js?v=${resourceVersion}"></script>

