<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${resourcePath}/js/jquery-1.8.3.min.js?v=${resourceVersion}"></script>
    <script type="text/javascript" src="${resourcePath}/js/jquery.md5.js?v=${resourceVersion}"></script>
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/manage/login.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/manage/common.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/common.css?v=${resourceVersion}">

</head>
<body class="body">
<jsp:include page="/WEB-INF/views/common/publicHeader.jsp"/>


<div class="login-background-module">
    <div class="login-form-module">
        <a href="javascript:;">
            <div class="login-type" id="selectLogin">登录</div>
        </a>
        <a href="javascript:;">
            <div class="login-type" id="selectRegister">注册</div>
        </a>
        <div id="form" class="login-form">
            <div class="login-words">
                用户名：<br/>
            </div>
            <input class="input-name" type="text" id="name"/>
            <div class="login-words">
                密&nbsp;&nbsp;&nbsp;&nbsp;码：<br/>
            </div>
            <input class="input-pwd" type="password" id="pwd"/>
            <div class="login-words">
                验证码：<br/>
            </div>
            <div class="find-password" id="find-password">忘记密码</div>
            <div class="verify-box">
                <input class="input-vrcode" type="text" id="vrcode"/>
                <div id="vrcode_img_box" class="vrcode_img_box">
                    <img id="vrcode_img" class="vrcode_img" src="/utils/getVerifyCode/${timeStamp}"/>
                </div>
            </div>
            <div class="input-message" id="message"></div>
            <a href="javascript:;">
                <div id="login" class="input-button">登录</div>
            </a>
            <a href="javascript:;">
                <div id="resister" class="input-button">注册</div>
            </a>
        </div>
    </div>

</div>


<jsp:include page="/WEB-INF/views/common/publicFooter.jsp"/>

<script>
    var returnUrl = "${returnUrl}";
    var postUrl = "/user/loginPost";
    var postRegUrl = "/user/registerPost";
</script>

<script type="text/javascript" src="${resourcePath}/js/manage/login.js?v=${resourceVersion}"></script>
</body>
</html>
