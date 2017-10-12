function indexInit(){
    var size = $("#trCount").val();
    var ext = size*30 + 308;
    $("div.cashier-index-leftbox").css("height", ext+"px");
    GetSignType(false);
}

function isIE() { //ie?
    if (!!window.ActiveXObject || "ActiveXObject" in window)
        return true;
    else
        return false;
}

function refresh(){
    window.location.reload();//刷新当前页面.
}

function fillOrderDetailInfo(res) {
    if(res != undefined && res.data != undefined){
        var data = res.data;
        var customComment = data.customComment;
        var manageComment = data.manageComment;
        var orderList = data.orderDishInfoList;
        $("#order-detail-customcomment").html(customComment);
        $("#order-detail-managecomment").html(manageComment);
        $("#order-detail-tabletr-box").html("");
        if(orderList != undefined){
            $.each(orderList,function(n,value) {
                var oname = '<div class="order-detail-bodyitem-dishname">'+value.dishName+'</div>';
                var osingleprice = '<div class="order-detail-bodyitem">'+value.singlePrice+'</div>';
                var ocount = "";
                if(res.data.status == "1"){
                    ocount = '<div class="order-detail-bodyitem"><a href="javascript:;"><div class="order-detail-minus">-</div></a><div class="cashier-detail-count" did='+value.dishId+'>'+value.count+'</div><a href="javascript:;"><div class="order-detail-plus">+</div></a></div>';
                }else if(res.data.status == "2"){
                    ocount = '<div class="order-detail-bodyitem">'+value.count+'</div>';
                }
                var ototalprice = '<div class="order-detail-bodyitem" type="price">'+value.dishPrice+'</div>';
                var ooriginprice = '<div class="order-detail-bodyitem">'+value.originPrice+'</div>';
                var odiscount = '<div class="order-detail-bodyitem">'+value.disount+'</div>';
                var ohtml = '<div class="order-detail-tabletr">'
                    + oname  +osingleprice
                    +ocount  +ototalprice
                    +ooriginprice  +odiscount
                    +'</div>';
                $("#order-detail-tabletr-box").append(ohtml);

            });
        }
    }
}


function getDIshStr() {
    var didStr = "";
    $(".cashier-detail-count").each(function(){
        var did = $(this).attr("did");
        var count = $(this).html();
        didStr = didStr + did + ":" +count +";";
    });
    return didStr;
}

function calTotalPrice() {
    var tprice = 0;
    $(".order-detail-bodyitem").each(function(){
        if($(this).attr("type") == "price"){
            var price = parseFloat($(this).html());
            tprice = parseFloat(tprice) + price;
        }
    });
    var tpriceStr = tprice + "";
    if(tpriceStr.indexOf(".") >= 0){
        tpriceStr = tpriceStr.substring(0,tpriceStr.indexOf(".") + 3);
    }
    $("#order-detail-totalPrice").html("订单总价：" + tpriceStr);
    $("#order-detail-buttonbox").attr("price",tpriceStr);
}

$(document).on("click","div.order-detail-minus",function(){
    var countBox = $(this).parent().next();
    var singlePriceBox = $(this).parent().parent().prev();
    var totalPriceBox = $(this).parent().parent().next();
    var count = parseInt(countBox.html());
    if(count > 0){
        count = count - 1;
        countBox.html(count);
        var singlePrice = parseFloat(singlePriceBox.html());
        var totalPrice = singlePrice * count;
        var totalStr = totalPrice + "";
        if(totalStr.indexOf(".") >= 0){
            totalStr = totalStr.substring(0,totalStr.indexOf(".") + 3);
        }
        totalPriceBox.html(totalStr);
    }else{
        countBox.html(0);
        totalPriceBox.html(0);
    }
    calTotalPrice();
});

$(document).on("click","div.order-detail-plus",function(){
    var countBox = $(this).parent().prev();
    var singlePriceBox = $(this).parent().parent().prev();
    var totalPriceBox = $(this).parent().parent().next();
    var count = parseInt(countBox.html());
    count = count + 1;
    countBox.html(count);
    var singlePrice = parseFloat(singlePriceBox.html());
    var totalPrice = singlePrice * count;
    var totalStr = totalPrice + "";
    if(totalStr.indexOf(".") >= 0){
        totalStr = totalStr.substring(0,totalStr.indexOf(".") + 3);
    }
    totalPriceBox.html(totalStr);
    calTotalPrice();
});

$("button.cashier-manage-inputbt").click(function(){
    var opt = $(this).attr("operate");
    var storeId = $("#storeId").val();
    var tableId = $(this).parent().parent().attr("tid");
    if(opt == "parse"){
        var msg1 = "是否暂停桌号"+tableId+"？";
        if (confirm(msg1) == false){
            return;
        }
        $.ajax({
            url:'/shopManage/parseTable/' + storeId,
            type:'POST',
            async:true,
            data:{
                tableId:tableId
            },
            timeout:6000,
            dataType:'json',
            success:function(res){
                if(res.code === '200'){
                    alert(res.message);
                    refresh();
                }else{
                    alert(res.message);
                    refresh();
                }
            },
            error:function(data){
                alert(data.statusText);
                refresh();
            }
        });
    }
    if(opt == "resume"){
        var msg2 = "是否恢复桌号"+tableId+"营业？";
        if (confirm(msg2) == false){
            return;
        }
        $.ajax({
            url:'/shopManage/resumeTable/' + storeId,
            type:'POST',
            async:true,
            data:{
                tableId:tableId
            },
            timeout:6000,
            dataType:'json',
            success:function(res){
                if(res.code === '200'){
                    alert(res.message);
                    refresh();
                }else{
                    alert(res.message);
                    refresh();
                }
            },
            error:function(data){
                alert(data.statusText);
                refresh();
            }
        });
    }
    if(opt == "detail"){
        var orderId = $(this).prev().val();
        if(orderId == "-1"){
            return;
        }
        $.ajax({
            url:'/shopManage/getOrderDetail/'+storeId,
            type:'POST',
            async:true,
            data:{
                tableId:tableId,
                orderId:orderId
            },
            timeout:6000,
            dataType:'json',
            success:function(res){
                if(res.code === '200'){
                    $(window).attr('location',"#cashier-header-logo");
                    $("#cashier-mask-grey").css("display","block");
                    $("#cashier-order-detail-box").css("display","block");
                    $("#order-detail-orderId").html("桌号：" + tableId);
                    var totalPrice = res.data.totalPrice;
                    var orderSize = res.data.orderCount;
                    if(res.data.status == "1"){
                        $("#order-detail-totalPrice").html("订单总价：" + totalPrice);
                        $("#order-detail-buttonbox").css("display","inline");
                        $("#order-detail-buttonbox-finished").css("display","none");
                    }else if(res.data.status == "2"){
                        $("#order-detail-totalPrice").html("订单状态：已结单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;订单总价：" + totalPrice);
                        $("#order-detail-buttonbox").css("display","none");
                        $("#order-detail-buttonbox-finished").css("display","inline");
                    }
                    $("#order-detail-buttonbox").attr("orderId", orderId);
                    $("#order-detail-buttonbox").attr("price", totalPrice);
                    $("#order-detail-buttonbox").attr("tableId", tableId);
                    $("#order-detail-buttonbox").attr("size", orderSize);
                    fillOrderDetailInfo(res);
                }else{
                    alert(res.message);
                }
            },
            error:function(data){
                alert(data.statusText);
            }
        });
    }
    if(opt == "print"){
        var orderId = $(this).prev().prev().val();
        if(orderId == "-1"){
            return;
        }
        $.ajax({
            url:'/shopManage/getOrderDetail/'+storeId,
            type:'POST',
            async:true,
            data:{
                tableId:tableId,
                orderId:orderId
            },
            timeout:6000,
            dataType:'json',
            success:function(res){
                if(res.code === '200'){
                    var orderList = res.data.orderDishInfoList;
                    $("div#print-receive-box").html("");
                    var printhtml = '';
                    if(orderList != undefined){
                        $.each(orderList,function(n,value) {
                            printhtml = printhtml + '<div class="print-receive-text">' + value.dishName + '</div>';
                        });
                    }
                    $("div#print-receive-box").html(printhtml);
                    $("div#print-receive-box").printArea();
                    $("div#print-receive-box").html("");
                }else{
                    alert(res.message);
                }
            },
            error:function(data){
                alert(data.statusText);
            }
        });
    }
    if(opt == "pay"){
        var payOrderId = $(this).prev().prev().val();
        var orderPrice = $(this).prev().val();
        if(orderPrice == "-1" || payOrderId == "-1" || tableId == "-1"){
            return;
        }
        var msg1 = "桌号"+tableId+ "总价"+orderPrice+ "，确认付款？";
        if (confirm(msg1) == false){
            return;
        }
        $.ajax({
            url:'/shopManage/payPost/',
            type:'POST',
            async:true,
            data:{
                tableId:tableId,
                orderId:payOrderId,
                storeId:storeId
            },
            timeout:6000,
            dataType:'json',
            success:function(res){
                if(res.code === '200'){
                    $("#cashier-mask-grey").css("display","none");
                    $("#cashier-order-detail-box").css("display","none");
                    refresh();
                }else{
                    alert(res.message);
                }
            },
            error:function(data){
                alert(data.statusText);
            }
        });
    }
    if(opt == "parseDish" || opt == "resumeDish"){
        var dishId = $(this).parent().next().val();
        var status = "1";
        if(opt == "parseDish"){
            status = "2";
        }
        var msg3 = "确认将该菜品设置为售罄？";
        if (confirm(msg3) == false){
            return;
        }
        $.ajax({
            url:'/shopManage/postDishStatus/',
            type:'POST',
            async:true,
            data:{
                dishId:dishId,
                status:status,
                storeId:storeId
            },
            timeout:6000,
            dataType:'json',
            success:function(res){
                if(res.code === '200'){
                    refresh();
                }else{
                    alert(res.message);
                }
            },
            error:function(data){
                alert(data.statusText);
            }
        });
    }
});

$("#cashier-mask-grey").click(function(){
    $("#cashier-mask-grey").css("display","none");
    $("#cashier-order-detail-box").css("display","none");
    $("#add-order-box").css("display","none");
    window.location.reload();
});

$("#order-detail-close").click(function(){
    $("#cashier-mask-grey").css("display","none");
    $("#cashier-order-detail-box").css("display","none");
    $("#add-order-box").css("display","none");
    window.location.reload();
});

$("#order-detail-button-cancle").click(function(){
    $("#cashier-mask-grey").css("display","none");
    $("#cashier-order-detail-box").css("display","none");
    $("#add-order-box").css("display","none");
    window.location.reload();
});

$("#cashier-header-logout").click(function(){
    $.ajax({
        url:'/shopManage/logoutPost/',
        type:'POST',
        async:true,
        timeout:6000,
        dataType:'json',
        success:function(){
            refresh();
        }
    });
});

$("#cashier-back-top").click(function(){
    $(window).attr('location',"#cashier-header-logo");
});

$("#cashier-search-dish").click(function(){
    var trId = "dname-" + $("#cashier-search-dish").prev().val();
    $(window).attr('location',"#"+trId);
});


$("#order-detail-paybt").click(function(){
    var price = $("#order-detail-buttonbox").attr("price");
    var orderId = $(this).parent().parent().attr("orderId");
    var tableId = $(this).parent().parent().attr("tableId");
    var storeId = $("#storeId").val();
    var comment = $("#order-detail-managecomment").val();
    var dishStr = getDIshStr();
    if(price == "-1" || orderId == "-1" || tableId == "-1"){
        return;
    }
    var msg1 = "桌号"+tableId+ "总价"+price+ "，确认付款？";
    if (confirm(msg1) == false){
        return;
    }
    $.ajax({
        url:'/shopManage/payPost/',
        type:'POST',
        async:true,
        data:{
            tableId:tableId,
            orderId:orderId,
            storeId:storeId,
            comment:comment,
            dishStr:dishStr,
            price:price
        },
        timeout:6000,
        dataType:'json',
        success:function(res){
            if(res.code === '200'){
                $("#cashier-mask-grey").css("display","none");
                $("#cashier-order-detail-box").css("display","none");
                $("#add-order-box").css("display","none");
                $(this).parent().parent().attr("price","-1");
                $(this).parent().parent().attr("orderId","-1");
                $(this).parent().parent().attr("tableId","-1");
                refresh();
            }else{
                alert(res.message);
            }
        },
        error:function(data){
            alert(data.statusText);
        }
    });
});

$("#order-detail-canclebt").click(function(){
    var price = $("#order-detail-buttonbox").attr("price");
    var orderId = $(this).parent().parent().attr("orderId");
    var tableId = $(this).parent().parent().attr("tableId");
    var storeId = $("#storeId").val();
    if(price == "-1" || orderId == "-1" || tableId == "-1"){
        return;
    }
    var msg1 = "桌号"+tableId+ "总价"+price+ "，撤销点菜？";
    if (confirm(msg1) == false){
        return;
    }
    $.ajax({
        url:'/shopManage/cancelPost/',
        type:'POST',
        async:true,
        data:{
            tableId:tableId,
            orderId:orderId,
            storeId:storeId
        },
        timeout:6000,
        dataType:'json',
        success:function(res){
            if(res.code === '200'){
                $("#cashier-mask-grey").css("display","none");
                $("#cashier-order-detail-box").css("display","none");
                $("#add-order-box").css("display","none");
                $(this).parent().parent().attr("price","-1");
                $(this).parent().parent().attr("orderId","-1");
                $(this).parent().parent().attr("tableId","-1");
                $(this).parent().parent().attr("size","0");
                refresh();
            }else{
                alert(res.message);
            }
        },
        error:function(data){
            alert(data.statusText);
        }
    });
});

$("#order-detail-addbt").click(function(){
    var box = $("#add-order-box");
    if(box.css("display") == "none"){
        box.css("display","block");
        var orderCount =  $(this).parent().parent().attr("size");
        box.css("top",orderCount*34+112);
    }else{
        box.css("display","none");
    }
});

$("#order-addbox-close").click(function(){
    $("#add-order-box").css("display","none");
});

function addDishToOrder(did,dname,dSinglePrice,dDiscount,dOriPrice) {
    var addoname = '<div class="order-detail-bodyitem-dishname">'+dname+'</div>';
    var addosingleprice = '<div class="order-detail-bodyitem">'+dSinglePrice+'</div>';
    var addocount = '<div class="order-detail-bodyitem"><a href="javascript:;"><div class="order-detail-minus">-</div></a><div class="cashier-detail-count" did='+did+'>1</div><a href="javascript:;"><div class="order-detail-plus">+</div></a></div>';
    var addototalprice = '<div class="order-detail-bodyitem" type="price">'+dSinglePrice+'</div>';
    var addooriginprice = '<div class="order-detail-bodyitem">'+dOriPrice+'</div>';
    var addodiscount = '<div class="order-detail-bodyitem">'+dDiscount+'</div>';
    var addohtml = '<div class="order-detail-tabletr">'
        + addoname  +addosingleprice
        +addocount  +addototalprice
        +addooriginprice  +addodiscount
        +'</div>';
    $("#order-detail-tabletr-box").append(addohtml);
}
$("div.add-order-dish-add").click(function() {
    var dname = $(this).parent().prev().html();
    var did = $(this).parent().parent().attr("did");
    var dSinglePrice = $(this).parent().parent().attr("finalPrice");
    var dDiscount = $(this).parent().parent().attr("discount");
    var dOriPrice = $(this).parent().parent().attr("oriPrice");
    var dishExist = false;
    $(".cashier-detail-count").each(function () {
        var didInOrder = $(this).attr("did");
        if (didInOrder == did) {
            alert("该菜品已经被点，您可以修改该菜品的数量");
            dishExist= true;
            return false;
        }
    });
    if(dishExist === true){
        return;
    }
    addDishToOrder(did,dname,dSinglePrice,dDiscount,dOriPrice);
});

$("#cashier-search-date").click(function() {
    var start = $("#cashier-cal-start").val();
    var end = $("#cashier-cal-end").val();
    if(start == "" && end == ""){
        return;
    }
    var storeId = $("#storeId").val();
    var queryStr = "";
    if(start != ""){
        queryStr = queryStr + "&startTime=" + start;
    }
    if(end != ""){
        queryStr = queryStr + "&endTime=" + end;
    }
    location.href = "/shopManage/cashierDateIndex/" + storeId + "?" + queryStr;
});

function GetSignType(reload){
    //ie报错
    if(isIE()){
        alert("检测到您的浏览器为ie版本，请选择最新chrome或者火狐浏览器，否则菜单将无法正确服务");
    }
    var storeId = $("#storeId").val();
    $.ajax({
        url:'/shopManage/getCashierModifyType/' + storeId,
        type:'GET',
        async:true,
        data:{
            storeId:storeId
        },
        timeout:6000,
        dataType:'json',
        success:function(res){
            if(res.code === '200'){
                var signIndex = $("#signIndex").val();
                if(signIndex !=  res.data){
                    if($("#cashier-mask-grey").css("display") == undefined
                        || $("#cashier-order-detail-box").css("display") == undefined
                        || ($("#cashier-mask-grey").css("display") == "none"
                        && $("#cashier-order-detail-box").css("display") == "none")){
                        $("#signIndex").val(res.data);
                        if(reload){
                            window.location.reload();
                        }

                    }
                }
            }
        }
    });
}

function gotoTid(searchBt) {
    var tid = parseInt($(searchBt).prev().val());
    $(window).attr('location',"#tid-"+tid);
}

function postComment(closeAfterFinish) {
    var orderId = $("#order-detail-buttonbox").attr("orderId");
    var tableId = $("#order-detail-buttonbox").attr("tableId");
    var storeId = $("#storeId").val();
    var comment = $("#order-detail-managecomment").val();
    if(orderId == "-1" || tableId == "-1" || comment == ""){
        return;
    }
    $.ajax({
        url:'/shopManage/postComment/',
        type:'POST',
        async:true,
        data:{
            tableId:tableId,
            orderId:orderId,
            storeId:storeId,
            comment:comment
        },
        timeout:6000,
        dataType:'json',
        success:function(res){
            if(res.code === '200'){
                if(closeAfterFinish){
                    $("#cashier-mask-grey").css("display","none");
                    $("#cashier-order-detail-box").css("display","none");
                    $("#add-order-box").css("display","none");
                    window.location.reload();
                }
            }else{
                alert(res.message);
            }
        },
        error:function(data){
            alert(data.statusText);
        }
    });
}