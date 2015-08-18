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

	$("#sel_searchMember").change(function(){
		changeSearchSelect(this.value);
	});
});
function changeSearchSelect(type){
	if(type=="all"){
		$("#td_key").html('<input style="border: 1px solid #ddd;" title="" class="yssp" placeholder="关键字"name="keywords" th:value="${keywords}" value="" type="text"/>');

	}else if(type=="payed"){
		$("#td_key").html('<select name="keywords" class="yssp" style=" height:29px;padding: 0px 90px 0px 4px; border:1px solid #ddd; vertical-align:middle"> <option value="1">是</option> <option value="0">否</option> </select>');

	}else if(type=="passed"){
		$("#td_key").html('<select name="keywords" class="yssp" style=" height:29px;padding: 0px 90px 0px 4px; border:1px solid #ddd; vertical-align:middle"> <option value="1">通过考试</option> <option value="2">未通过</option> <option value="0">未参加</option></select>');

	}else if(type=="certificateStatus"){
		$("#td_key").html('<select name="keywords" class="yssp" style=" height:29px;padding: 0px 90px 0px 4px; border:1px solid #ddd; vertical-align:middle"> <option value="2">申请中</option><option value="1">已领证</option> <option value="0">未领证</option> </select>');

	}

}

$(function() {
	$("#btn_chooseExistClass").click(function() {
		$.ajax({
			url:"loadAvailableClassTeams",
			type:"post",
			dataType:"json",
			success:function(result){
				if(result.status==0){
					$.MsgBox.Alert("温馨提醒",result.message);
					$("#noClassMemberArrangeClassDiv").hide();
					$("#noClassMemberArrangeNewClassDiv").show();
				}else if(result.status==1){
					$("#noClassMemberArrangeClassDiv").hide();
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
				$.MsgBox.Alert("系统异常","加载班级列表失败");
			}
		});
	})
});

$(function() {
	$("#btn_chooseExistExam").click(function() {
		$.ajax({
			url:"loadAvailableExam",
			type:"post",
			dataType:"json",
			success:function(result){
				if(result.status==0){
					$.MsgBox.Alert("温馨提醒",result.message);
				}else if(result.status==1){
					$("#classArrangeExamDiv").hide();
					$("#classArrangeExistExamDiv").show();
					var sel = $("#existExamSelect");
					sel.empty();
					for(var i=0;i<result.body.length;i++) {
						$("<option value='"+ result.body[i].id+"'>"+
							result.body[i].examName+
							"</option>").appendTo(sel);
					}
				}
			},
			error:function(){
				$.MsgBox.Alert("系统异常","加载考场列表失败");
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
	var reg=/^(13|14|15|17|18)\d{9}$/;
	var realName=$("#txt_memberName").val().trim();
	var sex=$(":input[name='sex']:checked").val();
	var phoneNo=$("#txt_memberphoneNo").val();
	if(realName==""){
		$("#add_Memberinfo").text("用户名不能为空");
		return false;
	}
	if(!reg.test(phoneNo)){
		$("#add_Memberinfo").text("手机号码格式不正确!");
		return false;
	}

	$.ajax({
		url:"addMembers",
		type:"post",
		data:{"realName":realName,"sex":sex,"phoneNo":phoneNo},
		dataType:"json",
		success:function(result){
			if(result.status==0){
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





