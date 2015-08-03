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
		$("#td_key").html('<input id="input_search1" style="border: 1px solid #ddd;" title="" class="yssp " placeholder="关键字"name="keywords" th:value="${keywords}" value="" type="text"/>');

	}else if(type=="payed"){
		$("#td_key").html('<select id="select_search2" name="keywords" class="yssp" style=" height:29px;padding: 0px 90px 0px 4px; border:1px solid #ddd; vertical-align:middle"> <option value="1">是</option> <option value="0">否</option> </select>');

	}else if(type=="passed"){
		$("#td_key").html('<select id="select_search3" name="keywords" class="yssp" style=" height:29px;padding: 0px 90px 0px 4px; border:1px solid #ddd; vertical-align:middle"> <option value="1">通过考试</option> <option value="2">未通过考试</option> <option value="0">未参加考试</option></select>');

	}else if(type=="haveLicense"){
		$("#td_key").html('<select id="select_search4" name="keywords" class="yssp" style=" height:29px;padding: 0px 90px 0px 4px; border:1px solid #ddd; vertical-align:middle"> <option value="1">是</option> <option value="0">否</option> </select>');

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
			url:"loadAvailableExam",
			type:"post",
			dataType:"json",
			success:function(result){
				if(result.status==0){
					alert(result.message);
				}else if(result.status==1){
					$("#classArrageExamDiv").hide();
					$("#classArrageExistExamDiv").show();
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
				alert("系统异常,加载考场列表失败");
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
		url:"addMembers",
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




