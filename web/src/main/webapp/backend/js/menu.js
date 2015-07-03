//菜单栏动态隐藏效果
$(function(){
    $(".sidebar-list > li > a").click(function(){
		 $(this).parents().siblings().find(".sidebar-list").hide(300);
		 $(this).siblings(".sub-menu").toggle(300);
		 $(this).parents().siblings().find(".sub-menu").hide(300);	
	})	
    $(".sub-menu > li > a").click(function(){   
        $(this).parents().siblings().find(".sidebar-list").hide(300);	
	    $(this).siblings(".sub-menu").toggle(300);	
	})
})


 




























