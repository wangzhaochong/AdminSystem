$("#find-psw-summitbt").click(function(){
    var answer1 = $("#find-anwers1");
    var answer2 = $("#find-anwers2");
    var answer3 = $("#find-anwers3");
    if(answer1.val() == "" || answer2.val() == "" || answer3.val() == ""){
        alert("请回答3个密保问题");
        return;
    }
    var qid1 = answer1.attr("qid");
    var qid2 = answer2.attr("qid");
    var qid3 = answer3.attr("qid");

    $.ajax({
        url:'/user/answerVerify/',
        type:'POST',
        async:true,
        data:{
            accountName:$("#accountName").val(),
            qid1:qid1,
            qid2:qid2,
            qid3:qid3,
            answer1:$.md5(answer1.val()),
            answer2:$.md5(answer2.val()),
            answer3:$.md5(answer3.val())
        },
        timeout:6000,
        dataType:'json',
        success:function(res){
            if(res.code === '200'){
                showPassWordBox();
            }else{
                alert(res.message);
            }
        },
        error:function(data){
            alert(data.statusText);
        }
    });

});

function showPassWordBox(){
    $("#reset-psw-box").css("display","inline");
    $(window).attr('location',"#reset-psw-box");
}

$("#reset-psw-summitbt").click(function(){
    var answer1 = $("#find-anwers1");
    var answer2 = $("#find-anwers2");
    var answer3 = $("#find-anwers3");
    if(answer1.val() == "" || answer2.val() == "" || answer3.val() == ""){
        alert("请回答3个密保问题");
        $(window).attr('location',"#find-psw-box");
        return;
    }
    var qid1 = answer1.attr("qid");
    var qid2 = answer2.attr("qid");
    var qid3 = answer3.attr("qid");

    var newpsw1 = $("#set-psw1");
    var newpsw2 = $("#set-psw2");
    if(newpsw1.val() == "" || newpsw2.val() == ""){
        alert("信息不完整");
        return;
    }else if(newpsw1.val() != newpsw2.val()){
        alert("两次新密码输入不一致，请检查新密码");
        return;
    }else{
        var testPwd = verifyPassword(newpsw1.val());
        if(testPwd === 1){
            alert('密码长度10-20位，不能由纯数字组成');
            return;
        }
    }

    $.ajax({
        url:'/user/findResetPsw/',
        type:'POST',
        async:true,
        data:{
            accountName:$("#accountName").val(),
            qid1:qid1,
            qid2:qid2,
            qid3:qid3,
            answer1:$.md5(answer1.val()),
            answer2:$.md5(answer2.val()),
            answer3:$.md5(answer3.val()),
            newPsw:$.md5(newpsw1.val())
        },
        timeout:6000,
        dataType:'json',
        success:function(res){
            if(res.code === '200'){
                $(window).attr('location',"/user/login/");
            }else{
                alert(res.message);
                $(window).attr('location',"#find-psw-box");
            }
        },
        error:function(data){
            alert(data.statusText);
            $(window).attr('location',"#find-psw-box");
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