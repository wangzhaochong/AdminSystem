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
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/manage/userCenter.css?v=${resourceVersion}">
    <script type="text/javascript" src="${resourcePath}/js/jquery.md5.js?v=${resourceVersion}"></script>
</head>
<body class="body">
<jsp:include page="/WEB-INF/views/common/publicHeader.jsp"/>

<jsp:include page="/WEB-INF/views/common/publicNav.jsp"/>

<div class="guide-context-module">
    <div class="user-center-infobox">
        <c:if test="${not setQuestionAready}">
            <div class="user-center-infoitem">
                <div class="user-center-nomaltext">您尚未设置密保问题，请尽快设置，用于密码找回</div>
                <div class="user-center-button" id="user-center-setQ-button">设置密保问题</div>
            </div>
        </c:if>
        <div class="user-center-infoitem">
            <div class="user-center-boldtext">用户名：${accountName}</div>
            <div class="user-center-button" id="user-center-modifypsw-button">修改密码</div>
        </div>
        <div class="user-center-infoitem">
            <div class="user-center-boldtext">等级：${usertype}（到期时间：${expireTime}）</div>
            <div class="user-center-button" id="user-center-upgradebt">升级</div>
        </div>
        <div class="user-center-infoitem">
            <div class="user-center-boldtext">商铺名：${storeName}</div>
        </div>
        <div class="user-center-infoitem">
            <div class="user-center-boldtext">总桌数：${maxCount}</div>
        </div>
        <div class="user-center-infoitem">
            <div class="user-center-boldtext">收银员账号数：${cashierCount}</div>
        </div>
    </div>
    <div id="user-center-upgradebox" class="user-center-upgradebox">
        <div class="user-center-infoitem">
            <div class="user-center-boldtext" style="color:#d3b482">会员升级</div>
        </div>
        <div class="user-center-infoitem">
            <div class="user-center-boldtext">等级：${usertype}</div>
        </div>
        <div class="user-center-infoitem">
            <div class="user-center-boldtext">到期时间：${expireTime}</div>
        </div>
        <div class="user-center-infoitem">
            <form>
                <div class="user-center-boldtext">升级到：</div>
                <c:if test="${usertype == '普通用户'}">
                    <input type="radio" name="status" class="input-redio-usercenter" id="ratio-upgrate-v1"/>
                    <div class="user-center-radiotext">V1（支持配置桌号0-200）</div>
                </c:if>
                <c:if test="${usertype == '普通用户'|| usertype == 'V1'}">
                    <input type="radio" name="status" class="input-redio-usercenter" checked="checked" id="ratio-upgrate-v2"/>
                    <div class="user-center-radiotext">V2（支持配置桌号0-500）</div>
                </c:if>
            </form>
        </div>
        <div class="user-center-infoitem">
            <form>
                <div class="user-center-boldtext">升级时长：</div>
                <input type="radio" name="status" id="ratio-time-6m" class="input-redio-usercenter" v1="${priceMap['v1_final_price_6m']}" v2="${priceMap['v2_final_price_6m']}"/>
                <div class="user-center-radiotext" >6个月</div>
                <input type="radio" name="status" id="ratio-time-1y" class="input-redio-usercenter"  checked="checked" v1="${priceMap['v1_final_price_1y']}" v2="${priceMap['v2_final_price_1y']}"/>
                <div class="user-center-radiotext" >12个月</div>
                <input type="radio" name="status" id="ratio-time-2y" class="input-redio-usercenter" v1="${priceMap['v1_final_price_2y']}" v2="${priceMap['v2_final_price_2y']}"/>
                <div class="user-center-radiotext" >24个月</div>
            </form>
        </div>
        <div class="user-center-infoitem">
            <div class="user-center-boldtext" id="user-center-upgratecost">升级费用：0元</div>
            <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=2184067686&site=qq&menu=yes">
                <div class="user-contact-button">联系管理员升级</div>
                <img border="0" src="http://wpa.qq.com/pa?p=2:420013983:52" class="user-qq-logo" alt="联系管理员" title="联系管理员"/>
            </a>
        </div>
        <input type="hidden" value="${minusPrice}" id="usercenter-minusPrice"/>
        <div class="user-center-paragraph">
            *费用说明:<br/>
            1.各个会员等级的收费标准参考下方的价格表<br/>
            2.升级或续费时产生的费用，已经扣除了您尚未消费完的会员费，您只需支付差价<br/>
            3.您尚未消费完的会员费用，是按照您升级前的会员年费（12个月）平均到每天的费用来计算<br/>
            4.最终解释权归葫芦微站所有<br/>
        </div>
        <div class="user-center-costtable">
            <div class="usercenter-costtable-item">价格表</div>
            <div class="usercenter-costtable-item">6个月</div>
            <div class="usercenter-costtable-item">12个月</div>
            <div class="usercenter-costtable-item">24个月</div>
            <div class="usercenter-costtable-item">V1会员</div>
            <div class="usercenter-costtable-item">
                <div class="usercenter-costprice-origin">${priceMap['v1_origin_price_6m']}</div>
                <div class="usercenter-costprice-final">${priceMap['v1_final_price_6m']}</div>
            </div>
            <div class="usercenter-costtable-item">
                <div class="usercenter-costprice-origin">${priceMap['v1_origin_price_1y']}</div>
                <div class="usercenter-costprice-final">${priceMap['v1_final_price_1y']}</div>
            </div>
            <div class="usercenter-costtable-item">
                <div class="usercenter-costprice-origin">${priceMap['v1_origin_price_2y']}</div>
                <div class="usercenter-costprice-final">${priceMap['v1_final_price_2y']}</div>
            </div>
            <div class="usercenter-costtable-item">V2会员</div>
            <div class="usercenter-costtable-item">
                <div class="usercenter-costprice-origin">${priceMap['v2_origin_price_6m']}</div>
                <div class="usercenter-costprice-final">${priceMap['v2_final_price_6m']}</div>
            </div>
            <div class="usercenter-costtable-item">
                <div class="usercenter-costprice-origin">${priceMap['v2_origin_price_1y']}</div>
                <div class="usercenter-costprice-final">${priceMap['v2_final_price_1y']}</div>
            </div>
            <div class="usercenter-costtable-item">
                <div class="usercenter-costprice-origin">${priceMap['v2_origin_price_2y']}</div>
                <div class="usercenter-costprice-final">${priceMap['v2_final_price_2y']}</div>
            </div>
        </div>
    </div>
</div>

<div class="userCenter-mask-grey" id="userCenter-mask-grey"></div>
<div class="userCenter-modify-box" id="userCenter-modify-box">
    <div class="userCenter-modify-content">
        <div class="userCenter-modify-titletext">设置密保问题</div>
        <div class="usercenter-selct-lable">问题1.&nbsp;</div>
        <select  id="usercenter-selct-ques1" class="usercenter-selct-ques">
            <c:forEach items="${questionMap}" var="questionMap" varStatus="status">
                <c:if test="${status.index == 0}">
                    <option value="${questionMap.key}" selected="selected">${questionMap.value}</option>
                </c:if>
                <c:if test="${status.index != 0}">
                    <option value=${questionMap.key}>${questionMap.value}</option>
                </c:if>
            </c:forEach>
        </select>
        <div class="usercenter-selct-lable">答案&nbsp;</div>
        <input class="usercenter-anwers" type="text" id="usercenter-anwers1"/>
        <div class="usercenter-selct-lable">问题2.&nbsp;</div>
        <select  id="usercenter-selct-ques2" class="usercenter-selct-ques">
            <c:forEach items="${questionMap}" var="questionMap" varStatus="status">
                <c:if test="${status.index == 1}">
                    <option value="${questionMap.key}" selected="selected">${questionMap.value}</option>
                </c:if>
                <c:if test="${status.index != 1}">
                    <option value=${questionMap.key}>${questionMap.value}</option>
                </c:if>
            </c:forEach>
        </select>
        <div class="usercenter-selct-lable">答案&nbsp;</div>
        <input class="usercenter-anwers" type="text" id="usercenter-anwers2"/>
        <div class="usercenter-selct-lable">问题3.&nbsp;</div>
        <select  id="usercenter-selct-ques3" class="usercenter-selct-ques">
            <c:forEach items="${questionMap}" var="questionMap" varStatus="status">
                <c:if test="${status.index == 2}">
                    <option value="${questionMap.key}" selected="selected">${questionMap.value}</option>
                </c:if>
                <c:if test="${status.index != 2}">
                    <option value=${questionMap.key}>${questionMap.value}</option>
                </c:if>
            </c:forEach>
        </select>
        <div class="usercenter-selct-lable">答案&nbsp;</div>
        <input class="usercenter-anwers" type="text" id="usercenter-anwers3"/>
        <div class="user-center-modifysummitbt" id="user-center-modifysummitbt">提交</div>
        <div class="user-center-modifycancelbt" id="user-center-modifycancelbt">取消</div>
    </div>
</div>

<div class="userCenter-modify-box" id="userCenter-modifypsd-box">
    <div class="userCenter-modify-content">
        <div class="userCenter-modify-titletext">修改密码</div>
        <div class="usercenter-modifypwd-lable">原密码&nbsp;</div>
        <input class="usercenter-pwd-input" type="password" id="usercenter-oldpsw"/>
        <div class="usercenter-modifypwd-lable">新密码&nbsp;</div>
        <input class="usercenter-pwd-input" type="password" id="usercenter-newpsw1"/>
        <div class="usercenter-modifypwd-lable">新密码&nbsp;</div>
        <input class="usercenter-pwd-input" type="password" id="usercenter-newpsw2"/>
        <div class="user-center-pwdinfotext" id="user-center-pwdinfotext"></div>
        <div class="user-center-pswsummitbt" id="user-center-pswsummitbt">提交</div>
        <div class="user-center-psdcancelbt" id="user-center-pswcancelbt">取消</div>
    </div>
</div>

<jsp:include page="/WEB-INF/views/common/publicFooter.jsp"/>
<script type="text/javascript" src="${resourcePath}/js/manage/userCenter.js?v=${resourceVersion}"></script>
<script type="text/javascript">
    $(document).ready(function(){
        flustUpgrateCost();
    });
</script>
</body>
</html>
