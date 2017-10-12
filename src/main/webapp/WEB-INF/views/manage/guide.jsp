<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${resourcePath}/js/jquery-1.8.3.min.js?v=${resourceVersion}"></script>
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/manage/guide.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/manage/common.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/common.css?v=${resourceVersion}">
    <script type="text/javascript" src="${resourcePath}/js/jquery.PrintArea.js"></script>
    <script>
        $(document).ready(function(){
            $("input#biuuu_button").click(function(){

                $("div#myPrintArea").printArea();

            });
        });

    </script>
</head>
<body class="body">
<jsp:include page="/WEB-INF/views/common/publicHeader.jsp"/>

<jsp:include page="/WEB-INF/views/common/publicNav.jsp"/>

<div class="guide-context-module">

    <input id="biuuu_button" type="button" value="打印"/>

    <div id="myPrintArea">.....文本打印部分.....</div>


</div>

<jsp:include page="/WEB-INF/views/common/publicFooter.jsp"/>

</body>
</html>
