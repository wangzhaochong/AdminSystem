$("#first-edit").click(function(){
	if($("div.left-sec-subedit").css("display") == 'none'){
		$($("div.left-sec-subedit").css("display","inline"));
	}else{
		$($("div.left-sec-subedit").css("display","none"));
	}
});

$("#first-release").click(function(){
	if($("div.left-sec-subrelease").css("display") == 'none'){
		$("div.left-sec-subrelease").css("display","inline");
	}else{
		$("div.left-sec-subrelease").css("display","none");
	}
});

/*悬停*/
$("div.left-sec-subedit").hover(
	function () {
		$(this).css("background-color","#efefef");
	},
	function () {
		$(this).css("background-color","#FFFFFF");
	}
);

$("div.left-sec-subrelease").hover(
	function () {
		$(this).css("background-color","#efefef");
	},
	function () {
		$(this).css("background-color","#FFFFFF");
	}
);


$("#edit-headphoto").click(function(){
	$("#shop-memulist-body").css("display","none");
	$("#preview-web").css("display","none");
	$("#edit-manage-role-box").css("display","none");
	$("#edit-category-box").css("display","none");
	$("#get-bincode-box").css("display","none");
	if($("#shop-headphoto-body").css("display") == 'none'){
		$("#shop-headphoto-body").css("display","inline");
	}else{
		$("#shop-headphoto-body").css("display","none");
	}
});

$("#edit-menulist").click(function(){
	$("#shop-headphoto-body").css("display","none");
	$("#preview-web").css("display","none");
	$("#edit-manage-role-box").css("display","none");
	$("#edit-category-box").css("display","none");
	$("#get-bincode-box").css("display","none");
	if($("#shop-memulist-body").css("display") == 'none'){
		$("#shop-memulist-body").css("display","inline");
	}else{
		$("#shop-memulist-body").css("display","none");
	}
});

$("#preview-website").click(function(){
	$("#shop-headphoto-body").css("display","none");
	$("#shop-memulist-body").css("display","none");
	$("#edit-manage-role-box").css("display","none");
	$("#edit-category-box").css("display","none");
	$("#get-bincode-box").css("display","none");
	if($("#preview-web").css("display") == 'none'){
		$("#preview-web").css("display","inline");
	}else{
		$("#preview-web").css("display","none");
	}
});

$("#edit-manage-role").click(function(){
	$("#shop-headphoto-body").css("display","none");
	$("#shop-memulist-body").css("display","none");
	$("#preview-web").css("display","none");
	$("#edit-category-box").css("display","none");
	$("#get-bincode-box").css("display","none");
	if($("#edit-manage-role-box").css("display") == 'none'){
		$("#edit-manage-role-box").css("display","inline");
	}else{
		$("#edit-manage-role-box").css("display","none");
	}
});


$("#generate-bincode").click(function(){
	$("#shop-headphoto-body").css("display","none");
	$("#shop-memulist-body").css("display","none");
	$("#preview-web").css("display","none");
	$("#edit-category-box").css("display","none");
	$("#edit-manage-role-box").css("display","none");
	if($("#get-bincode-box").css("display") == 'none'){
		$("#get-bincode-box").css("display","inline");
		previewBinCode();
	}else{
		$("#get-bincode-box").css("display","none");
	}
});

$("#edit-category").click(function(){
	$("#shop-headphoto-body").css("display","none");
	$("#shop-memulist-body").css("display","none");
	$("#preview-web").css("display","none");
	$("#edit-manage-role-box").css("display","none");
	$("#get-bincode-box").css("display","none");
	if($("#edit-category-box").css("display") == 'none'){
		$("#edit-category-box").css("display","inline");
	}else{
		$("#edit-category-box").css("display","none");
	}
});

$("#release-menu").click(function(){
	$("#shop-headphoto-body").css("display","none");
	$("#shop-memulist-body").css("display","none");
	$("#preview-web").css("display","none");
});

$("#generate-bincode").click(function(){
	$("#shop-headphoto-body").css("display","none");
	$("#shop-memulist-body").css("display","none");
	$("#preview-web").css("display","none");
});

$("#extend-bincode").click(function(){
	$("#shop-headphoto-body").css("display","none");
	$("#shop-memulist-body").css("display","none");
	$("#preview-web").css("display","none");
});

$("#bincode-button-preview").click(function(){
	previewBinCode();
});

function previewBinCode(){
	if(uid < 0) return;
	$("#bincode-button-text-preview").html("");
	$("#bincode-button-text-generate").html("");
	$("#bincode-button-text-download").html("");
	$.ajax({
		url:'/menu/previewBincode',
		type:'POST',
		async:true,
		data:{
			uid:uid
		},
		timeout:6000,
		dataType:'json',
		success:function(res){
			if(res.code === '200'){
				var binprev = $("#manage-bincode-prewiev");
				var timestamp = (new Date()).valueOf();
				binprev.html("");
				binprev.html('<img src="/menu/getBincode/'+uid+ '/?v=' +timestamp+ '" class="manage-bincode-prewiev"/>');
				var message = '扫描预览二维码，可以手机预览您的菜单';
				$("#bincode-button-text-preview").html(message);
			}else if(res.code === '419'){
				var message = '请在&nbsp;<a href="/user/userCenter#user-center-upgradebox" style="color:#4F4AFF;text-decoration: underline">个人中心</a>&nbsp;升级您的用户级别';
				$("#bincode-button-text-preview").html(message);
			}else{
				$("#bincode-button-text-preview").html(data.message);
			}
		},
		error:function(data){
			alert(data.statusText);
		}
	});
}

$("#bincode-button-generate").click(function(){
	if(uid < 0) return;
	$("#bincode-button-text-preview").html("");
	$("#bincode-button-text-generate").html("");
	$("#bincode-button-text-download").html("");
	var message = '请耐心等待二维码生成';
	$("#bincode-button-text-generate").html(message);
	$.ajax({
		url:'/menu/generateBincode',
		type:'POST',
		async:true,
		data:{
			uid:uid
		},
		dataType:'json',
		success:function(res){
			if(res.code === '200'){
				var message = '为您生成' +res.data+ '幅二维码图片，请点击 下载二维码 下载所有二维码';
				$("#bincode-button-text-generate").html(message);
				$("#bincode-button-download").removeAttr("disabled");
			}else if(res.code === '419'){
				var message = '请在&nbsp;<a href="/user/userCenter#user-center-upgradebox" style="color:#4F4AFF;text-decoration: underline">个人中心</a>&nbsp;升级您的用户级别';
				$("#bincode-button-text-generate").html(message);
			}else{
				$("#bincode-button-text-generate").html(data.message);
			}
		},
		error:function(data){
			alert(data.statusText);
		}
	});
});

$("#bincode-button-download").click(function(){
	$("#bincode-button-download").attr("disabled","disabled");
});

$(document).on("click","div.scroll-menulist-menuitem",function(){
	if($(this).css("background-color") == "rgb(255, 255, 255)"){
		$("div.scroll-menulist-menuitem").css("background-color","#ffffff");
		$(this).css("background-color","#efefef");
		var did = $(this).attr("dishId");
		$("#scroll_dishlist_operate").attr("dishID", did);
		editDish();
	}else{
		$(this).css("background-color","#ffffff");
		$("#menulist-editbox").attr("dishID",-1);
		$("#scroll_dishlist_operate").attr("dishID",-1);
		if( "edit" == $("#menulist-editbox").attr("operate")){
			$("#menulist-editbox").css("display","none");
		}
	}
});

$(document).on("click","div.scroll-catelist-menuitem",function(){
	if($(this).css("background-color") == "rgb(255, 255, 255)"){
		$("div.scroll-catelist-menuitem").css("background-color","#ffffff");
		$(this).css("background-color","#efefef");
		var cateId = $(this).attr("cateId");
		$("#scroll_catelist_operate").attr("cateID", cateId);
		editCate($(this).html());
	}else{
		$(this).css("background-color","#ffffff");
		$("#scroll_catelist_operate").attr("cateID",-1);
		$("#catelist-editbox").attr("cateID",-1);
		if( "edit" == $("#catelist-editbox").attr("operate")){
			$("#catelist-editbox").css("display","none");
		}
	}
});

$("#catelist-insert").click(function() {
	var cateId = $("#scroll_dishlist_operate").attr("cateID");
	if("none" == $("#catelist-editbox").css("display")
		||  "insert" != $("#catelist-editbox").attr("operate")){
		$("#catelist-editbox").css("display","inline");
		eleanCateInput();
		$("#catelist_editbox_title").html("添加菜品信息");
		$("#catelist-editbox").attr("operate","insert");
		$("#catelist-editbox").attr("cateID",cateId);
	}else{
		$("#catelist-editbox").css("display","none");
	}
});

$("#menulist-insert").click(function(){
	var did = $("#scroll_dishlist_operate").attr("dishID");
	if("none" == $("#menulist-editbox").css("display")
		||  "insert" != $("#menulist-editbox").attr("operate")){
		$("#menulist-editbox").css("display","inline");
		$("#dihsinfo_uploadimg_msg").html("");
		eleanDishInput();
		$("#menulist_editbox_title").html("添加菜品信息");
		$("#menulist-editbox").attr("operate","insert");
		$("#menulist-editbox").attr("dishID",did);
	}else{
		$("#menulist-editbox").css("display","none");
	}

});

function editDish(){
	$("#menulist-editbox").css("display","inline");
	$("#dihsinfo_uploadimg_msg").html("");
	eleanDishInput();
	var did = $("#scroll_dishlist_operate").attr("dishID");
	if(did > 0){
		$.ajax({
			url:'/menu/getDishInfo',
			type:'GET',
			async:true,
			data:{
				dishId:did
			},
			timeout:6000,
			dataType:'json',
			success:function(res){
				if(res.code === '200'){
					var dish = res.data;
					$("#dishinfo_name").val(dish.dishName);
					$("#dishinfo_price_origin").val(dish.dishPriceOrigin);
					$("#dishinfo_price_discount").val(dish.dishPriceDiscount);
					$("#dishinfo_price_final").val(dish.dishPriceFinal);
					$("#dishinfo_indigrent").val(dish.dishIngredient);
					$("#dishinfo_abstract").val(dish.dishAbstract);
					$("#dishinfo_discript").val(dish.dishDescription);
					if(dish.status == "1"){
						$("#menuitem-redio-sale").attr("checked","checked");
					}else if(dish.status == "2"){
						$("#menuitem-redio-saleout").attr("checked","checked");
					}
					var cateId = dish.cateId;
					$("#select-type-editbox").find("option[value="+cateId+"]").attr("selected","selected");
					var img = document.getElementById("dishinfo_imgshow");
					img.src = dish.dishImg;
				}
			}
		});
		$("#menulist-editbox").css("display","inline");
		$("#menulist_editbox_title").html("编辑菜品信息");
		$("#menulist-editbox").attr("operate","edit");
		$("#menulist-editbox").attr("dishID",did);
	}
}

function editCate(name){
	$("#catelist-editbox").css("display","inline");
	eleanCateInput();
	var cateId = $("#scroll_catelist_operate").attr("cateID");
	$("#cateinfo_name").val(name);
	$("#catelist_editbox_title").html("编辑菜品信息");
	$("#catelist-editbox").attr("operate","edit");
	$("#catelist-editbox").attr("cateID",cateId);
}

function eleanCateInput(){
	$("#cateinfo_name").val("");
}

function eleanDishInput(){
	$("#dishinfo_name").val("");
	$("#dishinfo_price_origin").val("");
	$("#dishinfo_price_final").val("");
	$("#dishinfo_price_discount").val("");
	$("#dishinfo_indigrent").val("");
	$("#dishinfo_abstract").val("");
	$("#dishinfo_discript").val("");
	var img = document.getElementById("dishinfo_imgshow");
	img.src = "/images/common/img404.png";
	var cateId = $('#select-type option:selected').val();
	$("#select-type-editbox").find("option[value="+cateId+"]").attr("selected","selected");
}



$("#menulist-upmove").click(function(){
	var did = $("#scroll_dishlist_operate").attr("dishID");
	if(did > 0){
		moveDishInfo('up',did);
	}
});

$("#menulist-downmove").click(function(){
	var did = $("#scroll_dishlist_operate").attr("dishID");
	if(did > 0){
		moveDishInfo('down',did);
	}
});

$("#catelist-upmove").click(function(){
	var cateId = $("#scroll_catelist_operate").attr("cateID");
	if(cateId > 0){
		moveCateInfo('up',cateId);
	}
});

$("#catelist-downmove").click(function(){
	var cateId = $("#scroll_catelist_operate").attr("cateID");
	if(cateId > 0){
		moveCateInfo('down',cateId);
	}
});

$("#menulist-delete").click(function(){
	var msg = "您确定要删除吗？";
	if (confirm(msg) == false){
		return;
	}
	var did = $("#scroll_dishlist_operate").attr("dishID");
	if(did > 0){
		$("#menulist-editbox").css("display","none");
		$.ajax({
			url:'/menu/deleteDishInfo',
			type:'POST',
			async:true,
			data:{
				dishId:did
			},
			dataType:'json',
			success:function(res){
				if(res.code === '200'){
					refreshDishList(res.data);
					//alert(res.message);
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

$("#catelist-delete").click(function(){
	var msg = "您确定要删除吗？";
	if (confirm(msg) == false){
		return;
	}
	var cateId = $("#scroll_catelist_operate").attr("cateID");
	if(cateId > 0){
		$("#catelist-editbox").css("display","none");
		$.ajax({
			url:'/menu/deleteCateInfo',
			type:'POST',
			async:true,
			data:{
				cateId:cateId
			},
			dataType:'json',
			success:function(res){
				if(res.code === '200'){
					refreshCateList(res.data);
					//alert(res.message);
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



function moveDishInfo(direct, dishId){
	$.ajax({
		url:'/menu/moveDishInfo',
		type:'POST',
		async:true,
		data:{
			dishId:dishId,
			direct:direct
		},
		dataType:'json',
		success:function(res){
			if(res.code === '200'){
				refreshDishList(res.data);
				//alert(res.message);
			}else{
				alert(res.message);
			}
		},
		error:function(data){
			alert(data.statusText);
		}
	});
}

function selectType(){
	var cateId = $('#select-type option:selected').val();
	$.ajax({
		url:'/menu/getDishInfoByCate',
		type:'GET',
		async:true,
		data:{
			cateId:cateId
		},
		timeout:6000,
		dataType:'json',
		success:function(res){
			if(res.code === '200'){
				$("#scroll_dishlist_operate").attr("dishID",-1);
				$("#menulist-editbox").css("display","none");
				refreshDishList(res.data);
			}
		}
	});
}

function moveCateInfo(direct, cateId){
	$.ajax({
		url:'/menu/moveCateInfo',
		type:'POST',
		async:true,
		data:{
			cateId:cateId,
			direct:direct
		},
		dataType:'json',
		success:function(res){
			if(res.code === '200'){
				refreshCateList(res.data);
				//alert(res.message);
			}else{
				alert(res.message);
			}
		},
		error:function(data){
			alert(data.statusText);
		}
	});
}

$('#dishinfo_price_origin').bind('input propertychange', function() {
	var originPrice = $('#dishinfo_price_origin').val();
	if(originPrice != null && originPrice != ''){
		$('#dishinfo_price_discount').val(10);
		$('#dishinfo_price_final').val(originPrice);
	}
});

$('#dishinfo_price_discount').bind('input propertychange', function() {
	var discountStr = $('#dishinfo_price_discount').val();
	if(discountStr == null || discountStr == ''){
		return;
	}
	var originPrice = $('#dishinfo_price_origin').val();
	if(originPrice == null || originPrice == ''){
		alert("请先设定菜品价格");
		$('#dishinfo_price_final').val('');
		$('#dishinfo_price_discount').val('');
	}else{
		var discount = parseFloat(discountStr);
		if(discount > 10){
			discount = 10;
		}
		if(discount < 0){
			discount = 0;
		}
		discount = discount/10;
		var originPriceStr = $('#dishinfo_price_origin').val();
		var price = parseFloat(originPriceStr) * discount;
		var priceStr = price + "";
		if(priceStr.indexOf(".") >= 0){
			priceStr = priceStr.substring(0,priceStr.indexOf(".") + 3);
		}
		$('#dishinfo_price_final').val(priceStr);
	}
});

$('#dishinfo_price_final').bind('input propertychange', function() {
	var finalStr = $('#dishinfo_price_final').val();
	if(finalStr == null || finalStr == ''){
		return;
	}
	var originPrice = $('#dishinfo_price_origin').val();
	if(originPrice == null || originPrice == ''){
		alert("请先设定菜品价格");
		$('#dishinfo_price_final').val('');
		$('#dishinfo_price_discount').val('');
	}else{
		var final = parseFloat(finalStr);
		if(final < 0){
			final = 0;
		}
		var originPriceStr = $('#dishinfo_price_origin').val();
		var discount = final/parseFloat(originPriceStr);
		discount = discount * 10;
		var discountStr = discount + "";
		if(discountStr.indexOf(".") >= 0){
			discountStr = discountStr.substring(0,discountStr.indexOf(".") + 3);
		}
		$('#dishinfo_price_discount').val(discountStr);
	}
});

