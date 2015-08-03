//修改密码
$(function(){
	$(":input[name='oldPd']").focus(function(){
		$(this).next("span").text("");
	});
	$(":input[name='newPd']").focus(function(){
		$(this).next("span").text("");
	});
	$(":input[name='newPdCheck']").focus(function(){
		$(this).next("span").text("");
	});
	$("#btn_pd").click(function(){
		var oldPd=$(":input[name='oldPd']").val();
		var newPd=$(":input[name='newPd']").val();
		var newPdCheck=$(":input[name='newPdCheck']").val();
		if(oldPd==""){
			$(":input[name='oldPd']").next("span").text("密码不能为空！");
		    return;
		}
		if(newPd==""){
			$(":input[name='newPd']").next("span").text("密码不能为空！");
			return;
		}
		if(newPd!==newPdCheck){
			$(":input[name='newPdCheck']").next("span").text("两次密码不同！");
			return;
		}
		$.ajax({
			url:"changePassword",
			type:"post",
			dataType:"json",
			data:{"oldPd":oldPd,"newPd":newPd},
			success:function(result){
				if(result.status==0){
					$.MsgBox.AjaxAlert("温馨提示",result.message);
					$(":input[type='password']").val("");
				}else if(result.status==1){
					$(":input[name='oldPd']").next("span").text(result.message);
				}
			},
			error:function(){
				alert("系统错误,修改密码失败！");
			}
		});
	});

})


 




























