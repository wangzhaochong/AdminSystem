changeShowType("login");

$("#login").click(function(){
	var name = $("#name").val();
	var pwd = $.md5($("#pwd").val());
	var vrcode = $("#vrcode").val();
	$.ajax({
		url:postUrl,
		type:'POST',
		async:true,
		data:{
			name:name,
			pwd:pwd,
			vrcode:vrcode
		},
		timeout:6000,
		dataType:'json',
		success:function(data){
			if(data.code === '200'){
				$(window).attr('location',returnUrl);
			}else {
				$("#message").html(data.message);
				refreshVerifyCode();
			}
		},
		error:function(data){
			alert(data.statusText);
		}
	})
});

$("#resister").click(function(){
	if($("#vrcode").val().length <= 0){
		$("#message").html('请输入验证码');
		return;
	}
	var testName = verifyUsername($("#name").val());
	if(testName === 1){
		$("#message").html('用户名长度小于20，不能由数字开头，不能包含"<>"');
		return;
	}
	var testPwd = verifyPassword($("#pwd").val());
	if(testPwd === 1){
		$("#message").html('密码长度10-20位，不能由纯数字组成');
		return;
	}
	var name = $("#name").val();
	var pwd =  $.md5($("#pwd").val());
	var vrcode = $("#vrcode").val();
	$.ajax({
		url:postRegUrl,
		type:'POST',
		async:true,
		data:{
			name:name,
			pwd:pwd,
			vrcode:vrcode
		},
		timeout:6000,
		dataType:'json',
		success:function(data){
			if(data.code === '200'){
				$(window).attr('location',returnUrl);
			}else {
				$("#message").html(data.message);
				refreshVerifyCode();
			}
		},
		error:function(data){
			alert(data.statusText);
		}
	})
});

$("#selectLogin").click(function(){
	changeShowType("login");
});

$("#selectRegister").click(function(){
	changeShowType("register");
});

function changeShowType(type){
	if(type != "register"){
		$("#login").css("display","inline");
		$("#resister").css("display","none");
		$("#selectLogin").css("text-decoration","underline");
		$("#selectRegister").css("text-decoration","none");
		$("#find-password").css("display","inline");
	}else{
		$("#login").css("display","none");
		$("#resister").css("display","inline");
		$("#selectLogin").css("text-decoration","none");
		$("#selectRegister").css("text-decoration","underline");
		$("#find-password").css("display","none");
	}
}


$("#find-password").click(function(){
	var accountName = $("#name").val();
	if(accountName == undefined || accountName == ''){
		alert("缺少用户名");
		return;
	}
	$(window).attr('location',"/user/findPsw/?name=" + accountName);
});

$(document).on("click","img.vrcode_img",function(){
	refreshVerifyCode();
});

function refreshVerifyCode(){
	var timestamp = (new Date()).valueOf();
	$("#vrcode_img_box").html("");
	$("#vrcode_img_box").append('<img id="vrcode_img" class="vrcode_img" src="/utils/getVerifyCode/'+timestamp+'"/>')
}

function verifyPassword(password){
	if(password.length >= 10 && password.length <= 20){
		var ret = /^[0-9]+$/;
		if(!ret.test(password)) {
			return 0;
		}
	}
	return 1;
}

function verifyUsername(name){
	if(name.length <= 20){
		var num = /^[0-9]+/;
		if(!num.test(name)) {
			return 0;
		}
	}
	return 1;
}