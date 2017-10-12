<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="mask_form" class="mask-login-form">
    <div class="mask-login-words">
        用户名：<br/>
    </div>
    <input class="mask-input-name" type="text" id="mask_name"/>
    <div class="mask-login-words">
        密&nbsp;&nbsp;&nbsp;&nbsp;码：<br/>
    </div>
    <input class="mask-input-pwd" type="password" id="mask_pwd"/>
    <div class="mask-login-words">
        验证码：<br/>
    </div>
    <div class="mask-verify-box">
        <input class="mask-input-vrcode" type="text" id="mask_vrcode"/>
        <img id="mask_vrcode_img" class="mask-vrcode-img" src="/utils/getVerifyCode"/>
    </div>
    <div class="mask-input-message" id="mask_message"></div>
    <button class="mask-login-btn" id="mask_login_btn">继续</button>
</div>




