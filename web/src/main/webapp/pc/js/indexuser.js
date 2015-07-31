$(function(){
	$("#memberLi").mouseenter(function(){
		$(".lbpo").fadeIn("fast");
		$(".zhizheni").fadeIn("fast");
	});
	$(".lbpo").mouseleave(function(){
		$(this).fadeOut("fast");
		$(".zhizheni").fadeOut("fast");
	});
	$("#btn_newMember").click(function(){
		$("#addMember").show();
	});
	$("#closeAddMember").click(function(){
		$("#addMember").hide();
	});
	$("#txt_memberName,#txt_memberphoneNo").focus(function(){
		$("#add_Memberinfo").text("");
	});
	$("#u109_input").change(function(){
		if(this.checked){
			$(":input[name='memberInfo']").attr("checked",true);
		}else{
			$(":input[name='memberInfo']").attr("checked",false);
		}
	});

});
$(function() {
	$("#btn_chooseExistClass").click(function() {
		$.ajax({
			url:path+"/pc/loadAvailableClassTeams",
			type:"post",
			dataType:"json",
			success:function(result){
				if(result.status==0){
					noClassMemberArrageClass();
					alert(result.message);
				}else if(result.status==1){
					$("#noClassMemberArrageClassDiv").hide();
					$("#existClassDiv").show();
					var sel = $("#existClassSelect");
					sel.empty();
					for(var i=0;i<result.body.length;i++) {
						$("<option value='"+ result.body[i].id+"'>"+
							result.body[i].className+
							"</option>").appendTo(sel);
					}
				}
			},
			error:function(){
				alert("系统异常,加载班级列表失败");
			}
		});
	})
});

$(function() {
	$("#btn_chooseExistExam").click(function() {
		$.ajax({
			url:path+"/pc/loadAvailableExamTeams",
			type:"post",
			dataType:"json",
			success:function(result){
				if(result.status==0){
					noClassMemberArrageClass();
					alert(result.message);
				}else if(result.status==1){
					$("#noClassMemberArrageClassDiv").hide();
					$("#existClassDiv").show();
					var sel = $("#existClassSelect");
					sel.empty();
					for(var i=0;i<result.body.length;i++) {
						$("<option value='"+ result.body[i].id+"'>"+
							result.body[i].className+
							"</option>").appendTo(sel);
					}
				}
			},
			error:function(){
				alert("系统异常,加载班级列表失败");
			}
		});
	})
});

$(function() {
	$("#memberLi").mouseenter(function(){
		$(".zhizheno").fadeIn("fast");
		$(".lbpoo").fadeIn("fast");
	});
	$(".lbpoo").mouseleave(function(){
		$(this).fadeOut("fast");
		$(".zhizheno").fadeOut("fast");
	});
});

function addMember(){
	var reg=/^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
	var realName=$("#txt_memberName").val().trim();
	var sex=$(":input[name='sex']:checked").val();
	var phoneNo=$("#txt_memberphoneNo").val();
	if(realName==""){
		$("#add_Memberinfo").text("用户名不能为空");
		return false;
	}
	console.log(reg+" "+phoneNo);
	if(!reg.test(phoneNo)){
		$("#add_Memberinfo").text("手机号码格式不正确!");
		return false;
	}

	$.ajax({
		url:path+"/pc/addMembers",
		type:"post",
		data:{"realName":realName,"sex":sex,"phoneNo":phoneNo},
		dataType:"json",
		success:function(result){
			if(result.status==0){
				//alert(result.message);
				$("#addMember").hide();
				location.reload();
			}else if(result.status==1){
				$("#add_Memberinfo").text(result.message);
			}
		},
		error:function(){
			alert("系统异常,添加学员失败");
		}
	});
}




