<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${resourcePath}/js/jquery-1.8.3.min.js?v=${resourceVersion}"></script>
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/manage/common.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/common.css?v=${resourceVersion}">
    <script type="text/javascript" src="${resourcePath}/js/jquery.md5.js?v=${resourceVersion}"></script>
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/manage/findPsw.css?v=${resourceVersion}">
</head>
<body class="body">
<div class="find-total-box">
    <div class="find-psw-box" id="find-psw-box">
        <div class="findPsw-modify-titletext">找回密码</div>
        <c:if test="${empty msg}">
            <div class="find-selct-lable">问题1.&nbsp;${questions1}</div>
            <div class="find-answer-lable">答案&nbsp;</div>
            <input class="find-anwers" type="text" id="find-anwers1" qid="${qid1}"/>
            <div class="find-selct-lable">问题2.&nbsp;${questions2}</div>
            <div class="find-answer-lable">答案&nbsp;</div>
            <input class="find-anwers" type="text" id="find-anwers2"  qid="${qid2}"/>
            <div class="find-selct-lable">问题3.&nbsp;${questions3}</div>
            <div class="find-answer-lable">答案&nbsp;</div>
            <input class="find-anwers" type="text" id="find-anwers3"  qid="${qid3}"/>
            <div class="find-psw-summitbt" id="find-psw-summitbt">验证</div>
            <input type="hidden" id="accountName"  value="${accountName}"/>
        </c:if>
        <c:if test="${not empty msg}">
            <div class="findr-pwdinfotext">${msg}</div>
        </c:if>
    </div>
    <div class="reset-psw-box" id="reset-psw-box">
        <div class="findPsw-reset-titletext">设置新密码</div>
        <div class="reset-answer-lable">设置新密码&nbsp;</div>
        <input class="find-anwers" type="password" id="set-psw1"/>
        <div class="reset-answer-lable">再次输入密码</div>
        <input class="find-anwers" type="password" id="set-psw2"/>
        <div class="find-psw-reset" id="reset-psw-summitbt">更新密码</div>
    </div>
</div>

<script type="text/javascript" src="${resourcePath}/js/manage/findPsw.js?v=${resourceVersion}"></script>
</body>
</html>
