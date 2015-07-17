$(function(){
		$("#userLi").mouseenter(function(){
			$(this).children("p").fadeIn("fast");
			$("#zhizhenImg").fadeIn("fast");
		})
		$("#userLi").children("p").last().mouseleave(function(){
			$(this).fadeOut("fast");
			$("#zhizhenImg").fadeOut("fast");
		})
});


