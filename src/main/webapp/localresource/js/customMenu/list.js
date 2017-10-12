$("#dish-xiadan-shopcar").click(function(){
	showShopCarList();
});

$("#dish-xiadan-price").click(function(){
	showShopCarList();
});

function showShopCarList(){
	if($("#shopcar-menu-list").css("display") == "none"){
		$("#shopcar-menu-list").css("display","block");
	}else{
		$("#shopcar-menu-list").css("display","none");
	}
}

$("div.dish-item-diancai").click(function(){
	var id = $(this).parent().parent().attr("did");
	var name = $(this).parent().parent().attr("dname");
	var price = $(this).parent().parent().attr("dprice");
	if(!checkIsOrdered(id)){
		sendShopCar(id, name, price);
	}
});


function checkIsOrdered(did){
	var res = false;
	$(".shopcar-menu-count").each(function(){
		var didStr = $(this).parent().attr("did");
		if(didStr == did){
			var count = parseInt($(this).html());
			count = count + 1;
			$(this).html(count);
			res = true;
			return false;
		}
	});
	if(res){
		refreshPrice();
		return true;
	}else{
		return false;
	}
}


function sendShopCar(id, name, price){
	var didHtml = '<div class="shopcar-menu-item" did='+ id +'>';
	var nameHtml = '<div class="shopcar-menu-name">'+ name +'当红炸子鸡</div>';
	var priceHtml = '<div class="shopcar-menu-item-price">￥'+ price +'</div>';
	var html = didHtml + nameHtml + priceHtml
				+ '<div class="shopcar-menu-item-minus">-</div>'
				+'<div class="shopcar-menu-count">1</div>'
				+'<div class="shopcar-menu-item-plus">+</div>'
				+'</div>';
	$("#shopcar-menu-item-box").append(html);
	refreshPrice()
}


$(document).on("click","div.shopcar-menu-item-minus",function(){
	var node = $(this).next();
	var count = parseInt(node.html());
	count = count - 1;
	node.html(count);
	if(count <= 0){
		$(this).parent().remove();
	}
	refreshPrice()
});


$(document).on("click","div.shopcar-menu-item-plus",function(){
	var node = $(this).prev();
	var count = parseInt(node.html());
	count = count + 1;
	node.html(count);
	refreshPrice()
});

function refreshPrice(){
	var price = 0;
	$(".shopcar-menu-item-price").each(function(){
		var itemPriceStr = $(this).html();
		itemPriceStr = itemPriceStr.substr(1, itemPriceStr.length);
		var count = parseInt($(this).next().next().html());
		var itemPrice = itemPriceStr * count;
		price = price + itemPrice;
	});
	var priceStr = price + "";
	if(priceStr.indexOf(".") >= 0){
		priceStr = priceStr.substring(0,priceStr.indexOf(".") + 3);
	}
	$("#dish-xiadan-price").html("￥" + priceStr);
}

function cleanShopCar(){
	$(".shopcar-menu-item-box").html("");
	$("#dish-xiadan-price").html("￥0");
}


$("#dish-xiadan-button").click(function(){

	var msg = "即将为您下单";
	if (confirm(msg) == false){
		return;
	}

	var postStr = '';
	var post = false;
	$(".shopcar-menu-count").each(function(){
		post = true;
		var didStr = $(this).parent().attr("did");
		var count = parseInt($(this).html());
		postStr = postStr + didStr + ":" + count + ";";
	});
	if(post){
		$.ajax({
			url:"/menuManage/orderPost",
			type:'POST',
			async:true,
			data:{
				idStr:postStr,
				tableId:tableId,
				storeId:storeId
			},
			dataType:'json',
			success:function(data){
				if(data.code === '200'){
					cleanShopCar();
					$(window).attr('location',"/customMenu/orderResult/" + storeId + "/" + tableId);
				}else {
					alert(data.message);
				}
			},
			error:function(data){
				alert(data.statusText);
			}
		})
	}
});

$("#order-list-comment-edit").click(function() {
	var but = $("#order-list-comment-edit");
	if("new" == but.attr("operate")){
		$("#order-list-comment").html("");
		$('#order-list-comment').removeAttr('readonly');
		but.attr("operate","save");
		but.html("保存");
	}else if("save" == but.attr("operate")){
		var comment = $("#order-list-comment").val();
		$.ajax({
			url:"/menuManage/commentPost",
			type:'POST',
			async:true,
			data:{
				comment:comment,
				storeId:storeId,
				tableId:tableId
			},
			dataType:'json',
			success:function(data){
				if(data.code === '200'){
					$("#order-list-comment").html(data.data);
					$("#order-list-comment-edit").attr("operate","edit");
				}else {
					alert(data.message);
				}
			},
			error:function(data){
				alert(data.statusText);
			}
		});
		$("#order-list-comment").attr("readonly","readonly");
		but.attr("operate","new");
		but.html("编辑");
	}else if("edit" == but.attr("operate")){
		$('#order-list-comment').removeAttr('readonly');
		but.attr("operate","save");
		but.html("保存");
	}
});

$("#order-list-backbt").click(function() {
	$(window).attr('location',"/customMenu/orderList/" + storeId + "/" + tableId);
});

$("#header-custommenu-btn").click(function() {
	if("none" != $("#header-list-arror").css("display")
		|| "none" != $("#header-list").css("display") ){
		$("#header-list-arror").css("display","none");
		$("#header-list").css("display","none");
	}else{
		$("#header-list-arror").css("display","inline");
		$("#header-list").css("display","inline");
	}
});

$("#header-list").click(function() {
	$(window).attr('location',"/customMenu/orderResult/" + storeId + "/" + tableId);
});

$("#order-wait-ahref").click(function() {
	$(window).attr('location',"/customMenu/orderResult/" + storeId + "/" + tableId);
});

$("#orderlist-category-bgimg").click(function() {
	showCateList();
});
$("#orderlist-open-category").click(function() {
	showCateList();
});

function showCateList(){
	$("#orderlist-catelist").css("display","block");
}

$("#orderlist-catelist-closemark").click(function() {
	$("#orderlist-catelist").css("display","none");
});

$("div.orderlist-catelist-catename").click(function() {
	var hrefurl = $(this).attr("refaddr");
	$(window).attr('location',hrefurl);
});
