<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${resourcePath}/js/jquery-1.8.3.min.js?v=${resourceVersion}"></script>
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/manage/index.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/manage/common.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/common.css?v=${resourceVersion}">
</head>
<body class="body">
<jsp:include page="/WEB-INF/views/common/publicHeader.jsp"/>

<div class="index-lunbotu-module">
    <div class="index-lunbo-combine">
        <a href="/menu/menuIndex/">
            <img class="index-start-img" src="${resourcePath}/images/manage/start.png">
        </a>
        <div class="index-description-text1">葫芦微站&nbsp;自助点菜</div>
        <div class="index-description-text2">
            <ul>
                <li>在线编辑</li>
                <li>一键生成</li>
                <li>十分钟&nbsp;全搞定</li>
            </ul>
        </div>
    </div>
</div>

<div class="index-function-module">
    <div class="index-function-logos">
        <a href="/index/guide/">
            <img  class="index-function-logo" src="${resourcePath}/images/manage/guide.png"/>
        </a>
        <a href="/menu/menuIndex/">
            <img  class="index-function-logo" src="${resourcePath}/images/manage/menu.png"/>
        </a>
        <a href="javascript:;">
            <img  class="index-function-logo" src="${resourcePath}/images/manage/developing.png"/>
        </a>
    </div>
</div>


<jsp:include page="/WEB-INF/views/common/publicFooter.jsp"/>


</body>
</html>
