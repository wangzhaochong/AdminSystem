<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript" src="${resourcePath}/js/jquery-1.8.3.min.js?v=${resourceVersion}"></script>
    <script type="text/javascript" src="${resourcePath}/js/jquery.md5.js?v=${resourceVersion}"></script>
    <script type="text/javascript" src="${resourcePath}/js/jquery.form.js?v=${resourceVersion}"></script>
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/manage/menu.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/manage/common.css?v=${resourceVersion}">
    <link rel="stylesheet" type="text/css" href="${resourcePath}/css/common.css?v=${resourceVersion}">
    <script>
        var uid = ${uid};
    </script>
</head>
<body class="body">
<jsp:include page="/WEB-INF/views/common/publicHeader.jsp"/>

<jsp:include page="/WEB-INF/views/common/publicNav.jsp"/>

<div class="menu-context-module">

    <div class="left-menu">
        <div class="left-fir-title">我的菜单</div>
        <div class="left-fir-item" id="first-edit">菜单管理<img class="dwarrow" src="${resourcePath}/images/common/arrow.png"></div>
        <div class="left-sec-subedit" id="edit-headphoto">编辑我的商铺</div>
        <div class="left-sec-subedit" id="edit-category">编辑菜品分类</div>
        <div class="left-sec-subedit" id="edit-menulist">编辑我的菜品</div>
        <div class="left-sec-subedit" id="preview-website">预览我的菜单</div>
        <div class="left-sec-subedit" id="edit-manage-role">管理收银界面</div>
        <div class="left-fir-item"  id="first-release" >二维码<img class="dwarrow" src="${resourcePath}/images/common/arrow.png"></div>
        <div class="left-sec-subrelease"  id="generate-bincode">下载二维码</div>
    </div>


    <div class="shop-headphoto-body" id="shop-headphoto-body">
        <div class="right-fir-title">编辑我的商铺</div>
        <div class="headphoto-margin">
            <div class="input-label"><div class="font-bitian">*</div>店铺名称</div>
            <input class="input-box" type="text" id="storeinfo_name"/>
            <div class="input-label">负责人</div>
            <input class="input-box" type="text" id="storeinfo_owner"/>
            <div class="input-label"><div class="font-bitian">*</div>店铺地址</div>
            <input class="input-box" type="text" id="storeinfo_address"/>
            <div class="input-label">联系电话</div>
            <input class="input-box" type="text" id="storeinfo_mobile"/>
            <div class="input-label">&nbsp;店铺简介</div>
            <textarea class="input-textbox" id="storeinfo_description"></textarea>
            <div class="input-label"><div class="font-bitian">*</div>店铺背景图</div>
            <div class="input-file">
                <button onclick="$('#storeimg').click()" class="input-file-img">浏览</button>
                <img class="store-img-show" src="${resourcePath}/images/common/img404.png" id="storeinfo_imgshow"/>
                <form action="/utils/imgUpload/1/${uid}" method="post"  enctype="multipart/form-data" id="imgform">
                    <input type="file" name="storeimgnm" id="storeimg" style="display:none" onchange="uploadimg()"/>
                    <input type="submit" style="display:none" id="storeimg_btn"/>
                </form>
                <div class="input-file-msg">*上传图片小于100k，支持png，jpeg，png格式</div>
                <div class="input-file-rtmsg" id="storeinfo_uploadimg_msg"></div>
            </div>

            <button class="input-summit" type="button" id="storeinfo_summit">保存</button>
            <div class="input-summit-message" id="storeinfo_summit_message"><a href="/user/login">点此登录</a>，继续上传商铺信息</div>
        </div>
    </div>

    <div class="shop-headphoto-body" id="edit-category-box">
        <div class="right-fir-title">编辑菜品分类</div>

        <div class="scroll-menulist">
            <div class="scroll-menulist-titlebox" cateID="-1" id="scroll_catelist_operate">
                <a href="javascript:;"><div class="scroll-catelist-title" id="catelist-insert">添加</div></a>
                <a href="javascript:;"><div class="scroll-catelist-title" id="catelist-delete">删除</div></a>
                <a href="javascript:;"><div class="scroll-catelist-title" id="catelist-upmove">上移</div></a>
                <a href="javascript:;"> <div class="scroll-catelist-title" id="catelist-downmove">下移</div></a>
            </div>
            <div class="scroll-menulist-list">
                <div id="cate_list"></div>
            </div>
        </div>
        <div class="menulist-editbox" operate="insert" cateID="-1" id="catelist-editbox">
            <div class="menulist-editbox-titlebox " id="catelist_editbox_title">编辑菜品分类</div>
            <div class="menulist-editbox-contentbox">
                <div class="input-label-cateitem"><div class="font-bitian">*</div>菜品类名</div>
                <input class="input-box-cateitem" type="text" id="cateinfo_name"/>
                <button class="input-summit-cateitem" type="button" id="cateinfo_summit">保存</button>
            </div>
        </div>
    </div>

    <div class="shop-headphoto-body" id="shop-memulist-body">
        <div class="right-fir-title">编辑我的菜品</div>
        <div class="menulist-margin">
            <div class="menulist-selectType-box">
                <div class="input-label-menutype"><div class="font-bitian">*</div>选择所属类</div>
                <select  id="select-type" onchange="selectType()" class="select-type-module">
                </select>
            </div>
            <div class="scroll-menulist">
                <div class="scroll-menulist-titlebox" dishID="-1" id="scroll_dishlist_operate">
                    <a href="javascript:;"><div class="scroll-menulist-title" id="menulist-insert">添加</div></a>
                    <a href="javascript:;"><div class="scroll-menulist-title" id="menulist-delete">删除</div></a>
                    <a href="javascript:;"><div class="scroll-menulist-title" id="menulist-upmove">上移</div></a>
                    <a href="javascript:;"> <div class="scroll-menulist-title" id="menulist-downmove">下移</div></a>
                </div>
                <div class="scroll-menulist-list">
                    <div id="dish_list"></div>
                </div>
            </div>
            <div class="menulist-editbox" operate="insert" dishID="-1" id="menulist-editbox">
                <div class="menulist-editbox-titlebox " id="menulist_editbox_title">编辑菜品信息</div>
                <div class="menulist-editbox-contentbox">
                    <div class="input-label-menuitem"><div class="font-bitian">*</div>所属类</div>
                    <select  id="select-type-editbox" class="select-type-editbox">
                    </select>
                    <div class="input-label-menuitem"><div class="font-bitian">*</div>菜名</div>
                    <input class="input-box-menuitem" type="text" id="dishinfo_name"/>
                    <div class="input-label-menuitem"><div class="font-bitian">*</div>价格</div>
                    <input class="input-box-menuitem" type="text" id="dishinfo_price_origin"/>
                    <div class="input-label-menuitem"><div class="font-bitian">*</div>折扣</div>
                    <input class="input-box-menuitem" type="text" id="dishinfo_price_discount"/>
                    <div class="input-label-menuitem"><div class="font-bitian">*</div>优惠价</div>
                    <input class="input-box-menuitem" type="text" id="dishinfo_price_final"/>
                    <div class="input-label-menuitem">&nbsp;主配料</div>
                    <input class="input-box-menuitem" type="text" id="dishinfo_indigrent"/>
                    <div class="input-label-menuitem">&nbsp;&nbsp;备注</div>
                    <input class="input-box-menuitem" type="text" id="dishinfo_abstract"/>
                    <div class="input-label-menuitem">&nbsp;菜品描述</div>
                    <textarea class="input-textbox-menuitem" id="dishinfo_discript"></textarea>
                    <div class="input-label-menuitem"><div class="font-bitian">*</div>设置状态</div>
                    <div class="input-rediobox-menuitem">
                        <input type="radio" checked="checked" value=1 name="status" class="input-redio-menuitem" id="menuitem-redio-sale"/>
                        <div class="input-rediolabel-menuitem">在售中</div>
                        <input type="radio" value=2 name="status" class="input-redio-menuitem" id="menuitem-redio-saleout"/>
                        <div class="input-rediolabel-menuitem">已售罄</div>
                    </div>
                    <div class="input-label-menuitem"><div class="font-bitian">*</div>图片</div>
                    <%--<input class="input-file-menuitem" type="file"/>--%>
                    <div class="input-file-dish">
                        <button onclick="$('#dishImg').click()" class="input-file-dishimg">浏览</button>
                        <img class="dish-img-show" src="${resourcePath}/images/common/img404.png" id="dishinfo_imgshow"/>
                        <form action="/utils/imgUpload/2/${uid}" method="post"  enctype="multipart/form-data" id="dishImgFrom">
                            <input type="file"  name="dishImgNm" id="dishImg" style="display:none" onchange="uploaddishimg()"/>
                            <input type="submit" style="display:none" id="dishimg_btn"/>
                        </form>
                        <div class="dish-input-file-msg">*上传图片小于100k，支持png，jpeg，png格式</div>
                        <div class="dish-input-file-rtmsg" id="dihsinfo_uploadimg_msg"></div>
                    </div>
                    <button class="input-summit-menuitem" type="button" id="dishinfo_summit">保存</button>
                </div>
            </div>
        </div>
    </div>


    <div class="preview-web-body" id="preview-web">
        <img src="${resourcePath}/images/manage/phonemargin.png" class="preview-phone-margin"/>
        <div class="preview-frame-box">
            <Iframe src=${IframeSrc} width="360" height="500" scrolling="yes" frameborder="0"></iframe>
        </div>
        <div class="preview-instruct-margin">
            预览提示：<br>
            <br>
            &bull;实际菜单页面和预览页面可能有<br>
            部分细节上的差异<br>
            <br>
            &bull;预览界面不支持点击操作<br>
        </div>
    </div>

    <div class="shop-headphoto-body" id="edit-manage-role-box">
        <div class="right-fir-title">配置管理权限</div>
        <div class="shouyin-namege-leyer">
            <div class="shouyin-manage-userheadtestbox">
                <div class="shouyin-manage-headtest">创建收银员账号：</div>
            </div>
            <div class="shouyin-manage-userinputbox">
                <div class="shouyin-manage-labletest">用户名：</div>
                <input type="text" class="shouyin-manage-input" id="shouyin-input-cashierName"/>
                <div class="shouyin-manage-labletest">密&nbsp;&nbsp;码：</div>
                <input type="text" class="shouyin-manage-input" id="shouyin-input-pwd"/>
                <div class="shouyin-manage-labletest">备注名：</div>
                <input type="text" class="shouyin-manage-input" id="shouyin-input-cdes"/>
                <button class="shouyin-manage-inputbt" type="button" id="shouyin-manage-inputbt">创建</button>
            </div>
            <div class="shouyin-manage-usertable">
                <div class="shouyin-manage-usertable-head">用户名</div>
                <div class="shouyin-manage-usertable-head">备注</div>
                <div class="shouyin-manage-usertable-del"></div>
                <div class="shouyin-manage-cashierbox" id="shouyin-manage-cashierbox"></div>
            </div>
        </div>
        <div class="shouyin-namege-leyer">
            <div class="shouyin-manage-userheadtestbox">
                <div class="shouyin-manage-headtest">配置最大桌号：</div>
                <div class="shouyin-manage-sectest">用于配置二维码</div>
            </div>
            <div class="shouyin-manage-lablelong">请输入您所需要的最大桌号：</div>
            <input type="text" class="shouyin-manage-input" id="shouyin-max-tableno"/>
            <button class="shouyin-manage-inputbt-tableid" type="button" id="shouyin-manage-inputbt-tableid">配置</button>
        </div>
        <div class="shouyin-manage-headtest" id="shouyin-manage-href">进入收银管理界面>></div>
        <img src="${resourcePath}/images/manage/computer.png" id="shouyin-manage-computer" class="shouyin-manage-computer"/>
        <a href="/shopManage/cashierIndex/${uid}" target="_blank" class="shouyin-manage-trdtest">如果跳转失败，可以点此访问收银管理界面</a>
        <div class="shouyin-manage-sectest-red">注意：<br>禁止使用ie浏览器跳转。<br>选择最新chrome或者火狐浏览器</div>
    </div>


    <div class="shop-headphoto-body" id="get-bincode-box">
        <div class="right-fir-title">下载二维码</div>
        <div class="manage-bincode-box">
            <div class="manage-bincode-text">扫码点菜</div>
            <div id="manage-bincode-prewiev"><img src="" class="manage-bincode-prewiev"/></div>
            <div class="manage-bincode-text">桌号：1</div>
        </div>
        <div class="manage-bincode-buttonbox">
            <button class="manage-bincode-button" type="button" id="bincode-button-preview">预览二维码</button>
            <div class="manage-bincode-button-text" id="bincode-button-text-preview"></div>
            <button class="manage-bincode-button" type="button" id="bincode-button-generate">生成二维码</button>
            <div class="manage-bincode-button-text" id="bincode-button-text-generate"></div>
            <a href="/localresource/uploadResources/${uid}.pdf"><button class="manage-bincode-button" type="button" id="bincode-button-download" disabled="disabled">下载二维码</button></a>
            <div class="manage-bincode-button-text" id="bincode-button-text-download"></div>
        </div>
    </div>
</div>



<jsp:include page="/WEB-INF/views/common/publicFooter.jsp"/>

<script type="text/javascript" src="${resourcePath}/js/manage/menu.js?v=${resourceVersion}"></script>
<script type="text/javascript" src="${resourcePath}/js/manage/menusummit.js?v=${resourceVersion}"></script>

</body>
</html>
