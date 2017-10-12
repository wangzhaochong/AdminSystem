$("#cashier-login-inputbt").click(function(){
    var storeId = $("#cashier-storeid").val();
    var uname = $("#cashier-uname-input").val();
    var passwd = $("#cashier-passwd-input").val();
    if(storeId == "" || storeId == "-1"){
        var msg = "您输入的管理界面网址有误。请登录葫芦微站(www.huluweizhan.cn)查询正确的网址。\n"
                    +"位置：进入“我的菜单”——“菜单管理”——“进入收银管理界面”，点击跳转链接。";
        alert(msg);
        return;
    }
    if(uname == "" || passwd == ""){
        $("#cashier-login-infomessage").html("");
        $("#cashier-login-infomessage").html("请填写用户名、密码");
        return;
    }
    passwd = $.md5(passwd);
    $.ajax({
        url:'/shopManage/loginPost/' + storeId,
        type:'POST',
        async:true,
        data:{
            uname:uname,
            passwd:passwd
        },
        timeout:6000,
        dataType:'json',
        success:function(res){
            if(res.code === '200'){
                location.href = "/shopManage/cashierIndex/" + storeId;
            }else{
                $("#cashier-login-infomessage").html(res.message);
            }
        },
        error:function(data){
            $("#cashier-login-infomessage").html(data.statusText);
        }
    });
});
