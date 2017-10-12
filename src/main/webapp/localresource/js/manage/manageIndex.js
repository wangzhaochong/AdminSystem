$("#manageIndex-manage-search-button").click(function(){
	var id = $("#manageIndex-manage-id").val();
	var name = $("#manageIndex-manage-accountName").val();
	var queryStr = '';
	if(id != ''){
		queryStr = queryStr + '&reqUid=' + id;
	}
	if(name != ''){
		queryStr = queryStr + '&accountName=' + name;
	}
	$(window).attr('location',"/manage/index?searchType=0" + queryStr);
});

$("#manageIndex-user-search-button").click(function(){
	var id = $("#manageIndex-user-id").val();
	var name = $("#manageIndex-user-accountName").val();
	var queryStr = '';
	if(id != ''){
		queryStr = queryStr + '&reqUid=' + id;
	}
	if(name != ''){
		queryStr = queryStr + '&accountName=' + name;
	}
	$(window).attr('location',"/manage/index?searchType=1" + queryStr);
});

$("#manageIndex-user-upgrate-button").click(function(){
	var id = $("#manageIndex-user-commonuid").val();
	var grateType = $("#manageIndex-usertype-select").find("option:selected").val();
	var grateTime = $("#manageIndex-usertime-select").find("option:selected").val();
	$.ajax({
		url:'/manage/upgrateUser/',
		type:'POST',
		async:true,
		data:{
			uid:id,
			grateType:grateType,
			grateTime:grateTime
		},
		timeout:6000,
		dataType:'json',
		success:function(res){
			if(res.code === '200'){
				alert("升级成功");
				window.location.reload();//刷新当前页面.
			}else{
				alert(res.message);
			}
		},
		error:function(data){
			alert(data.statusText);
		}
	});
});



function flushPriceInput() {
	var grateTypeSel = $("#manageIndex-usertype-select").find("option:selected");
	var grateTimeSel = $("#manageIndex-usertime-select").find("option:selected");
	var minusPrice = $("#manageIndex-user-minusPrice").val();
	var price = 0;
	if(grateTypeSel.val() == '2'){
		price = grateTimeSel.attr("v1");
		if(parseInt(minusPrice) > 0){
			price = price - minusPrice;
		}
		if(price < 0){
			price = 0;
		}
		$("#manageIndex-upgrate-cost").html("升级费用：" + price + "元");
	}else if(grateTypeSel.val() == '3'){
		price = grateTimeSel.attr("v2");
		if(parseInt(minusPrice) > 0){
			price = price - minusPrice;
		}
		if(price < 0){
			price = 0;
		}
		$("#manageIndex-upgrate-cost").html("升级费用：" + price + "元");
	}else{
		$("#manageIndex-upgrate-cost").html("升级费用：0元");
	}
}



