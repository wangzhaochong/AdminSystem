function setTableMaxCount(maxCount) {
	if(maxCount != undefined){
		$("#shouyin-max-tableno").val(maxCount);
	}

}
$(document).ready(function(){
	if(uid < 0) return;
	$.ajax({
		url:'/menu/getStoreInfo',
		type:'GET',
		async:true,
		timeout:6000,
		dataType:'json',
		success:function(res){
			if(res.code === '200'){
				var store = res.data.store;
				$("#storeinfo_name").val(store.storeName);
				$("#storeinfo_owner").val(store.storeOnwer);
				$("#storeinfo_address").val(store.storeAddress);
				$("#storeinfo_mobile").val(store.storeMobile);
				$("#storeinfo_description").val(store.storeDescription);
				var img = document.getElementById("storeinfo_imgshow");
				img.src = store.storeHeadimg;
				setCateList(res.data);
				setDishList(res.data);
				setCashierList(res.data.cashieres);
				setTableMaxCount(res.data.maxCount);
			}
		}
	});

});

function setCateList(data){
	var cateList = data.cates;
	if(cateList != undefined){
		$.each(cateList,function(n,value) {
			$("#cate_list").append('<div class="scroll-catelist-menuitem" cateId=' + value.cateId + '>'+ value.cateName +'</div>')
		});
	}
}


function setCashierList(cashierList){
	if(cashierList != undefined){
		$("#shouyin-manage-cashierbox").html("");
		$.each(cashierList,function(n,value) {
			if(n < 3){
				var cashiername = '<div class="shouyin-manage-usertable-item">'+value.cashierName+'</div>';
				var cashierdes = '<div class="shouyin-manage-usertable-item">'+value.description+'</div>';
				var cashieridstr = '<a href="javascript:;" class="shouyin-manage-usertable-delete" cid='+value.cashierid +'>删除</div>';
				$("#shouyin-manage-cashierbox").append(cashiername+cashierdes+cashieridstr);
			}
		});
	}
}

function setDishList(data){
	var dishList = data.dishes;
	if(dishList != undefined){
		$.each(dishList,function(n,value) {
			$("#dish_list").append('<div class="scroll-menulist-menuitem" dishId=' + value.dishId + '>'+ value.dishName +'</div>')
		});
	}
	var cateList = data.cates;
	if(cateList != undefined){
		$.each(cateList,function(n,value) {
			$("#select-type").append('<option value="'+value.cateId+'">'+value.cateName+'</option>')
			$("#select-type-editbox").append('<option value="'+value.cateId+'">'+value.cateName+'</option>')
		});
	}
	var defaultCate = cateList[0].cateId;
	refreshSelectType(defaultCate);
}

function refreshSelectType(cateId){
	$("#select-type").find("option[value="+cateId+"]").attr("selected","selected");
}

function refreshSelectList(cateList){
	$("#select-type").html("");
	$("#select-type-editbox").html("");
	if(cateList != undefined){
		$.each(cateList,function(n,value) {
			$("#select-type").append('<option value="'+value.cateId+'">'+value.cateName+'</option>')
			$("#select-type-editbox").append('<option value="'+value.cateId+'">'+value.cateName+'</option>')
		});
	}

}

function uploadimg(){
	$("#storeimg_btn").click();
}

$('#imgform').submit(function() {
	$(this).ajaxSubmit(function(data) {
		//alert(data.data);
		if(data.code == 200){
			var img = document.getElementById("storeinfo_imgshow");
			img.src = data.data;
			$("#storeinfo_uploadimg_msg").html("上传成功");
		}else{
			$("#storeinfo_uploadimg_msg").html(data.message);
		}

	});
	return false;
});



$("#storeinfo_summit").click(function(){

	if(uid < 0){
		$("#storeinfo_summit_message").css("display","inline");
		return ;
	}

	var name = $("#storeinfo_name").val();
	var add = $("#storeinfo_address").val();
	var img = document.getElementById("storeinfo_imgshow").src;
	if(name == null || name == ''
		|| add == null || add == ''
		|| img == null || img == ''){
		alert("商铺名称、商铺地址、商铺背景图是必传信息");
		return;
	}

	var postdata={
		"storeName":$("#storeinfo_name").val(),
		"storeOnwer":$("#storeinfo_owner").val(),
		"storeAddress":$("#storeinfo_address").val(),
		"storeMobile":$("#storeinfo_mobile").val(),
		"storeDescription":$("#storeinfo_description").val(),
		"storeHeadimg":document.getElementById("storeinfo_imgshow").src
	};

	$.ajax({
		url:'/menu/updateStoreInfo',
		type:'POST',
		async:true,
		data:JSON.stringify(postdata),
		timeout:6000,
		dataType:'json',
		contentType:"application/json",
		success:function(res){
			if(res.code === '200'){
				alert(res.message);
			}else{
				alert(res.message);
			}
		},
		error:function(data){
			alert(data.statusText);
		}
	});
});

function clearCashierInput() {
	$("#shouyin-input-cashierName").val("");
	$("#shouyin-input-pwd").val("");
	$("#shouyin-input-cdes").val("");
}
$("#shouyin-manage-inputbt").click(function(){

	if(uid < 0){
		alert("请先登录");
	}

	var cname = $("#shouyin-input-cashierName").val();
	var cpwd = $("#shouyin-input-pwd").val();
	var cdes = $("#shouyin-input-cdes").val();
	if(cname == null || cname == ''
		|| cpwd == null || cpwd == ''
		|| cdes == null || cdes == ''){
		alert("收银账户名、密码、备注名是必传信息");
		return;
	}

	var postdata={
		"cashierName":cname,
		"password":$.md5(cpwd),
		"description":cdes
	};

	$.ajax({
		url:'/menu/addCashier',
		type:'POST',
		async:true,
		data:JSON.stringify(postdata),
		timeout:6000,
		dataType:'json',
		contentType:"application/json",
		success:function(res){
			if(res.code === '200'){
				clearCashierInput();
				setCashierList(res.data);
			}else{
				alert(res.message);
			}
		},
		error:function(data){
			alert(data.statusText);
		}
	});
});

$(document).on("click","a.shouyin-manage-usertable-delete",function(){
	var cid = $(this).attr("cid");
	if(cid <= 0){
		return;
	}
	$.ajax({
		url:'/menu/deleteCashier',
		type:'POST',
		async:true,
		data:{
			"cashierId":cid
		},
		timeout:6000,
		dataType:'json',
		success:function(res){
			if(res.code === '200'){
				clearCashierInput();
				setCashierList(res.data);
			}else{
				alert(res.message);
			}
		},
		error:function(data){
			alert(data.statusText);
		}
	});
});


function uploaddishimg(){
	$("#dishimg_btn").click();
}

$('#dishImgFrom').submit(function() {
	$(this).ajaxSubmit(function(data) {
		if(data.code == 200){
			var img = document.getElementById("dishinfo_imgshow");
			img.src = data.data;
			$("#dihsinfo_uploadimg_msg").html("上传成功");
		}else{
			$("#dihsinfo_uploadimg_msg").html(data.message);
		}

	});
	return false;
});

//function showmask(){
//	var bh = $(document).height();
//	var bw = $(document).width();
//	$("#mask_background").css("width",bw);
//	$("#mask_background").css("height",bh);
//	$("#mask_background").css("display","block");
//	$("#mask_form").css("display","block");
//
//}


$("#dishinfo_summit").click(function(){

	var dishname = $("#dishinfo_name").val();
	var origin = $("#dishinfo_price_origin").val();
	var final = $("#dishinfo_price_final").val();
	var discount = $("#dishinfo_price_discount").val();
	var img = document.getElementById("dishinfo_imgshow").src;
	var cateId = $('#select-type-editbox option:selected').val();
	if(dishname == null || dishname == ''
		|| origin == null || origin == ''
		|| final == null || final == ''
		|| discount == null || discount == ''
		|| img == null || img == ''
		|| cateId == null || cateId == ''){
		alert("类型、菜名、价格、图片是必传信息");
		return;
	}

	var status = 1;
	if($("#menuitem-redio-sale").attr("checked") == "checked"){
		status = 1;
	}else if($("#menuitem-redio-saleout").attr("checked") == "checked"){
		status = 2;
	}

	var postdata={
		"dishName":$("#dishinfo_name").val(),
		"dishPriceOrigin":$("#dishinfo_price_origin").val(),
		"dishPriceFinal":$("#dishinfo_price_final").val(),
		"dishPriceDiscount":$("#dishinfo_price_discount").val(),
		"dishIngredient":$("#dishinfo_indigrent").val(),
		"dishAbstract":$("#dishinfo_abstract").val(),
		"dishDescription":$("#dishinfo_discript").val(),
		"dishImg":document.getElementById("dishinfo_imgshow").src,
		"cateId":cateId,
		"status":status
	};

	var type = $("#menulist-editbox").attr("operate");
	var insertId = $("#menulist-editbox").attr("dishID");
	$.ajax({
		url:'/menu/uploadDishInfo?optType=' + type + '&insertId=' + insertId,
		type:'POST',
		async:true,
		data:JSON.stringify(postdata),
		dataType:'json',
		contentType:"application/json",
		success:function(res){
			if(res.code === '200'){
				alert(res.message);
				refreshDishList(res.data);
				refreshSelectType(cateId);
				if(type == "insert"){
					$("#menulist-editbox").css("display","none");
				}
			}else{
				alert(res.message);
			}
		},
		error:function(data){
			alert(data.statusText);
		}
	});

});


$("#cateinfo_summit").click(function(){

	var catename = $("#cateinfo_name").val();
	if(catename == null || catename == ''){
		alert("菜品类名是必传信息");
		return;
	}

	var postdata={
		"cateName":$("#cateinfo_name").val()
	};

	var type = $("#catelist-editbox").attr("operate");
	var insertId = $("#catelist-editbox").attr("cateID");
	$.ajax({
		url:'/menu/uploadCateInfo?optType=' + type + '&insertId=' + insertId,
		type:'POST',
		async:true,
		data:JSON.stringify(postdata),
		dataType:'json',
		contentType:"application/json",
		success:function(res){
			if(res.code === '200'){
				alert(res.message);
				refreshCateList(res.data);
				refreshSelectList(res.data);
				if(type == "insert"){
					$("#catelist-editbox").css("display","none");
				}
			}else{
				alert(res.message);
			}
		},
		error:function(data){
			alert(data.statusText);
		}
	});

});

function refreshDishList(dishList){
	$("#dish_list").html("");
	var did = $("#scroll_dishlist_operate").attr("dishID");
	if(dishList != undefined){
		$.each(dishList,function(n,value) {
			$("#dish_list").append('<div class="scroll-menulist-menuitem" dishId=' + value.dishId + '>'+ value.dishName +'</div>')
		});
	}
	if(did > -1){
		$(".scroll-menulist-menuitem").each(function(){
			if($(this).attr("dishId") == did){
				$(this).css("background-color","#efefef");
			}
		});
	}
}

function refreshCateList(cateList){
	$("#cate_list").html("");
	var cateId = $("#scroll_catelist_operate").attr("cateID");
	if(cateList != undefined){
		$.each(cateList,function(n,value) {
			$("#cate_list").append('<div class="scroll-catelist-menuitem" cateId=' + value.cateId + '>'+ value.cateName +'</div>')
		});
	}
	if(cateId > -1){
		$(".scroll-catelist-menuitem").each(function(){
			if($(this).attr("cateId") == cateId){
				$(this).css("background-color","#efefef");
			}
		});
	}
}

$("#shouyin-manage-href").click(function() {
	window.open("/shopManage/cashierIndex/"+uid);
});

$("#shouyin-manage-computer").click(function() {
	window.open("/shopManage/cashierIndex/"+uid);
});

$("#shouyin-manage-inputbt-tableid").click(function() {
	if(uid < 0) return;
	var maxNumber = $("#shouyin-max-tableno").val();
	if(maxNumber < 0) return;
	$.ajax({
		url:'/menu/updateTableMaxNo',
		type:'POST',
		async:true,
		timeout:6000,
		dataType:'json',
		data:{
			"maxNumber":maxNumber
		},
		success:function(res){
			if(res.code === '200'){
				setTableMaxCount(res.data);
			}
			else{
				$("#shouyin-max-tableno").val("");
				alert(res.message);
			}

		},
		error:function(){
			$("#shouyin-max-tableno").val("");
		}
	});
});




