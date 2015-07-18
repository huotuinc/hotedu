function isShowMember(){
	addCookie("memberId","123456dd",300); //测试，向服务器发送数据
	var sessionId=getCookie("memberId");  //测试，从本地获取cookie值
	console.log(sessionId);               //输出cookie值
	if(sessionId!=null) {
		$.ajax({
			url: path + "/pc/checkMemberLogin",
			type: "get",
			data: {"MemberId": sessionId},
			dataType: "json",
			success: function (result) {
				if(result.status==1) {
					console.log(path + result.body.pictureUri);
					console.log(result.body.realName);
					console.log(result.body.loginName);
					$li = '<li id="memberLi" style="padding:0px;">' +
						'<a href="#"><img class="lodingimg" src=' + path + result.body.pictureUri + ' width="100%"/></a>' +
						'<p class="zhizhen"><img src="images/lodingj.png" width="18px" height="7px"/></p>' +
						'<p class="lbpo"> <a href="#" th:href="@{/pc/person}">' + result.body.loginName + '</a>' +
						'<a href="#">个人中心</a> <a href="#">修改密码</a>' +
						'<a href="#">退出</a>' +
						'</p>' +
						'</li>';
					$(".nav").append($li);//添加会员Li
					$("#loginLi").hide();
				}else{
					console.log("没有用户！");
				}

			},
			error: function () {
				alert("系统异常,获取用户失败");
			}
		});
	}
}
function showMemberBody(){
	$(document).on("mouseenter","#memberLi",function(){
		$(this).children("p").fadeIn("fast");
		$(".zhizhen").fadeIn("fast");
	});
	$(document).on("mouseleave",".lbpo",function(){
		$(this).fadeOut("fast");
		$(".zhizhen").fadeOut("fast");
	});
}
