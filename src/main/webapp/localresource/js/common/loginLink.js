
if(islogin == false){
	$("#loginLink").css("display","inline");
	$("#logoutLink").css("display","none");
}else{
	$("#loginLink").css("display","none");
	$("#logoutLink").css("display","inline");
}

jQuery.fn.slideLeftShow = function( speed, callback ) {
	this.animate({
		width : "show",
		paddingLeft : "show",
		paddingRight : "show",
		marginLeft : "show",
		marginRight : "show"
	}, speed, callback );
};

$(document).ready(function(){
	$("#broadcast-bobble").slideLeftShow(1000);
});

$("#broadcast-bobble-close").click(function() {
	$("#broadcast-bobble").css("display","none");
});