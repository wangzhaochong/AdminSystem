$("#user-center-upgradebt").click(function(){
	$(window).attr('location',"#user-center-upgradebox");
});

$("#userCenter-mask-grey").click(function(){
	$("#userCenter-modify-box").css("display","none");
	$("#userCenter-mask-grey").css("display","none");
	$("#userCenter-modifypsd-box").css("display","none");
});

$("#user-center-pswcancelbt").click(function(){
	$("#userCenter-modifypsd-box").css("display","none");
	$("#userCenter-mask-grey").css("display","none");
});

$("#user-center-modifycancelbt").click(function(){
	$("#userCenter-modify-box").css("display","none");
	$("#userCenter-mask-grey").css("display","none");
});



$("#user-center-setQ-button").click(function(){
	$("#userCenter-modify-box").css("display","block");
	$("#userCenter-mask-grey").css("display","block");
});

$("#user-center-modifypsw-button").click(function(){
	$("#user-center-pwdinfotext").html("");
	$("#userCenter-modifypsd-box").css("display","block");
	$("#userCenter-mask-grey").css("display","block");
});

$("#user-center-modifysummitbt").click(function(){
	var answer1 = $("#usercenter-anwers1");
	var answer2 = $("#usercenter-anwers2");
	var answer3 = $("#usercenter-anwers3");
	if(answer1.val() == "" || answer2.val() == "" || answer3.val() == ""){
		alert("需要设置并回答3个密保问题");
		return;
	}else if(answer1.val().length > 10 || answer2.val().length > 10 || answer3.val().length > 10){
		alert("答案不能超过10个字符");
		return;
	}
	var q1 = $("#usercenter-selct-ques1").find("option:selected");
	var q2 = $("#usercenter-selct-ques2").find("option:selected");
	var q3 = $("#usercenter-selct-ques3").find("option:selected");
	if(q1.val() == q2.val() || q1.val() == q3.val() || q2.val() == q3.val()){
		alert("3个问题不能够重复，请重新选择问题");
		return;
	}

	$.ajax({
		url:'/user/answerPost/',
		type:'POST',
		async:true,
		data:{
			q1:q1.val(),
			q2:q2.val(),
			q3:q3.val(),
			answer1:$.md5(answer1.val()),
			answer2:$.md5(answer2.val()),
			answer3:$.md5(answer3.val())
		},
		timeout:6000,
		dataType:'json',
		success:function(res){
			if(res.code === '200'){
				alert("设置成功");
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

$("#user-center-pswsummitbt").click(function(){
	var oldpsw = $("#usercenter-oldpsw");
	var newpsw1 = $("#usercenter-newpsw1");
	var newpsw2 = $("#usercenter-newpsw2");
	if(oldpsw.val() == "" || newpsw1.val() == "" || newpsw2.val() == ""){
		$("#user-center-pwdinfotext").html("信息不完整");
		return;
	}else if(newpsw1.val() != newpsw2.val()){
		$("#user-center-pwdinfotext").html("两次新密码输入不一致，请检查新密码");
		return;
	}else{
		var testPwd = verifyPassword(newpsw1.val());
		if(testPwd === 1){
			$("#user-center-pwdinfotext").html('密码长度10-20位，不能由纯数字组成');
			return;
		}
	}

	$.ajax({
		url:'/user/modifyPswPost/',
		type:'POST',
		async:true,
		data:{
			psw:$.md5(newpsw1.val()),
			oldpsw:$.md5(oldpsw.val())
		},
		timeout:6000,
		dataType:'json',
		success:function(res){
			if(res.code === '200'){
				alert("修改成功");
				window.location.reload();//刷新当前页面.
			}else{
				$("#user-center-pwdinfotext").html(res.message);
			}
		},
		error:function(data){
			$("#user-center-pwdinfotext").html(data.statusText);
		}
	});

});


function verifyPassword(password){
	if(password.length >= 10 && password.length <= 20){
		var ret = /^[0-9]+$/;
		if(!ret.test(password)) {
			return 0;
		}
	}
	return 1;
}

$("input.input-redio-usercenter").click(function() {
	flustUpgrateCost();
});

function flustUpgrateCost(){
	var grateTimeRat;
	var minusPrice = $("#usercenter-minusPrice").val();
	if($("#ratio-time-6m").attr("checked") == 'checked'){
		grateTimeRat = $("#ratio-time-6m");
	}else if($("#ratio-time-1y").attr("checked") == 'checked'){
		grateTimeRat = $("#ratio-time-1y");
	}else if($("#ratio-time-2y").attr("checked") == 'checked'){
		grateTimeRat = $("#ratio-time-2y");
	}
	var price = 0;
	if($("#ratio-upgrate-v1").attr("checked") == 'checked'){
		price = grateTimeRat.attr("v1");
	}else if($("#ratio-upgrate-v2").attr("checked") == 'checked'){
		price = grateTimeRat.attr("v2");
	}
	if(parseInt(minusPrice) > 0){
		price = price - minusPrice;
	}
	if(price < 0){
		price = 0;
	}
	$("#user-center-upgratecost").html("升级费用：" + price + "元");
}


