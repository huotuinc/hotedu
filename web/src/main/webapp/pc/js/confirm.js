//jquery警告框弹窗
(function ($) {
    $.MsgBox = {
        Alert: function (title, msg) {
            GenerateHtml("alert", title, msg);
            btnOk();  //alert只是弹出消息，因此没必要用到回调函数callback
            btnNo();
        },
        Confirm: function (title, msg, callback) {
            GenerateHtml("confirm", title, msg);
            btnOk(callback);
            btnNo();
        },
        AjaxAlert: function (title, msg, callback) {
            GenerateHtml("ajaxAlert", title, msg);
            btnOk(callback);
            btnNo(callback);
        }
    };
    //生成Html
    var GenerateHtml = function (type, title, msg) {
        var _html = "";
        _html += '<div id="mb_box"></div><div id="mb_con_pop"><span id="mb_tit">' + title + '</span>';
        if (type == "alert") {
            _html += '<a id="mb_ico">X</a><div id="mb_msg"><div id="mb_img"><img src="images/info.gif" alt="警告"/></div><div id="mb_txt">' + msg + '</div></div><div id="mb_btnbox">';
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
        }
        if (type == "confirm" || type == "Save") {
            _html += '<a id="mb_ico">X</a><div id="mb_msg"><div id="mb_img"><img src="images/important.gif" alt="警告"/></div><div id="mb_txt">' + msg + '</div></div><div id="mb_btnbox">';
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
            _html += '<input id="mb_btn_no" type="button" value="取消" />';
        }
        if (type == "ajaxAlert") {
            _html += '<div id="mb_msg"><div id="mb_img"><img src="images/info.gif" alt="警告"/></div><div id="mb_txt">' + msg + '</div></div><div id="mb_btnbox">';
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
        }
        _html += '</div></div>';
        //必须先将_html添加到body，再设置Css样式
        $("body").append(_html);
        GenerateCss();
    }
    //生成Css
    var GenerateCss = function () {
        var $mb_ico = $("#mb_ico"),
            $mb_con_pop = $("#mb_con_pop");

        $("#mb_box").css({
            width: '100%', height: '100%', zIndex: '99999', position: 'fixed',
            filter: 'Alpha(opacity=60)', backgroundColor: 'black', top: '0', left: '0', opacity: '0.6'
        });
        $mb_con_pop.css({
            zIndex: '999999', width: '400px', position: 'fixed',
            backgroundColor: 'White', borderRadius: '15px'
        });
        $("#mb_tit").css({
            height:'13px',display: 'block', fontSize: '14px', color: '#444', padding: '10px 15px',
            backgroundColor: '#DDD', borderRadius: '15px 15px 0 0',
            borderBottom: '3px solid #006EFF', fontWeight: 'bold'
        });
        $("#mb_img").css({
            width:'20px',float:'left', padding: '14px 20px 14px 20px',borderBottom: '1px dashed #DDD'
        });
        $("#mb_txt").css({
            width:'300px',float:'left', padding: '20px', borderBottom: '1px dashed #DDD'
        });
        $("#mb_msg").css({
            lineHeight: '20px', border:'1px',
            borderBottom: '1px dashed #DDD', fontSize: '13px'
        });
        $mb_ico.css({
            display: 'block', position: 'absolute', right: '10px', top: '8px',
            border: '1px solid Gray', width: '18px', height: '18px',textAlign: 'center',
            lineHeight: '18px', cursor: 'pointer', borderRadius: '12px', fontFamily: '微软雅黑'
        });
        $("#mb_btnbox").css({clear:'both',margin: '73px 0 10px 0', textAlign: 'center'});
        $("#mb_btn_ok,#mb_btn_no").css({width: '85px', height: '30px', color: 'white', border: 'none','border-radius': '5px',cursor: 'pointer'});
        $("#mb_btn_ok").css({backgroundColor: '#006EFF'});
        $("#mb_btn_no").css({backgroundColor: 'gray', marginLeft: '20px'});
        //右上角关闭按钮hover样式
        $mb_ico.hover(function () {
            $(this).css({backgroundColor: 'Red', color: 'White'});
        }, function () {
            $(this).css({backgroundColor: '#DDD', color: 'black'});
        });
        var _widht = document.documentElement.clientWidth;  //屏幕宽
        var _height = document.documentElement.clientHeight; //屏幕高
        var boxWidth = $mb_con_pop.width();
        var boxHeight = $mb_con_pop.height();
        //让提示框居中
        $mb_con_pop.css({top: (_height - boxHeight) / 2 + "px", left: (_widht - boxWidth) / 2 + "px"});
    }
    //确定按钮事件
    var btnOk = function (callback) {
        $("#mb_btn_ok").click(function () {
            $("#mb_box,#mb_con_pop").remove();
            if (typeof (callback) == 'function') {
                callback();
            }
        });
    }
    //取消按钮事件
    var btnNo = function () {
        $("#mb_btn_no,#mb_ico").click(function () {
            $("#mb_box,#mb_con_pop").remove();
        });
    }
})(jQuery);

$(document).ready(function () {
    $(".link-delete").bind("click", function () {
        $.MsgBox.Confirm("友情提示", "执行删除后将无法恢复，确定继续吗？", function () {
            /*alert("你居然真的删除了...");*/
        });
    });
});

function check_del(h) {
    $.MsgBox.Confirm("友情提示", "执行删除后将无法恢复，确定继续吗？", function () {
        $(h).siblings(".real-delete")[0].click();
    });
}

function check_noClassMember() {
    $.MsgBox.Alert("友情提示", "请选择需要安排分班的学员！");
}

function check_classExam() {
    $.MsgBox.Alert("友情提示", "请选择需要安排考场的班级！");
}

function check_payMember() {
    $.MsgBox.Alert("友情提示", "请选择需要确认缴费的学员！");
}

function check_ExamMember() {
    $.MsgBox.Alert("友情提示", "请选择需要确认通过的学员！");
}

function check_payEnter() {
    $.MsgBox.Confirm("友情提示", "确认要将选中学员的状态改为已交费吗？", function () {
        var arrayLis = $("#checkPayLis").val().trim();
        $.ajax({
            url:"checkPayList",
            type:"post",
            data:{"checkPayLis":arrayLis},
            dataType:"json",
            success:function(result){
                $.MsgBox.AjaxAlert("友情提示",result.message,function(){
                    //$("#searchMemberInfo").submit();
                    window.location.href = window.location.href.replace(/#/g,'');
                });
            },
            error:function(){
                alert("安排失败");
            }
        });
    });
}

function check_ExamMemberEnter() {
    $.MsgBox.Confirm("友情提示", "确认要将选中学员的状态改为通过吗？", function () {
        var arrayLis = $("#checkExamMemberList").val().trim();
        $.ajax({
            url:"allMemberPassExam",
            type:"post",
            data:{"checkExamMemberList":arrayLis},
            dataType:"json",
            success:function(result){
                $.MsgBox.AjaxAlert("友情提示",result.message,function(){
                    //$("#searchGraduationMembers").submit();
                    window.location.href = window.location.href.replace(/#/g,'');
                });
            },
            error:function(){
                alert("安排失败");
            }
         });
    });
}

function check_arrangeNewClass() {
    var className = $("#className").val().trim();
    var $text = $("#addNewClassTeamError");
    if(className=="") {
        $text.text("班级名称不能为空!");
        return;
    }
    $.MsgBox.Confirm("友情提示", "确认要将选中学员安排到新建班级中吗？", function () {
        var noClassMemberArrayLis = $("#noClassMemberArrayLis").val().trim();
        $.ajax({
            url:"addSaveNewClassTeam",
            type:"post",
            data:{"className":className,"noClassMemberArrayLis":noClassMemberArrayLis},
            dataType:"json",
            success:function(result){
                if(result.status==0){
                    $text.text(result.message);
                }else if(result.status==1){
                    $.MsgBox.AjaxAlert("友情提示",result.message,function(){
                        //$("#searchClassMembers").submit();
                        window.location.href = window.location.href.replace(/#/g,'');
                    });
                }
            },
            error:function(){
                alert("安排失败");
            }
        });
    });
}

function check_arrangeNewExam() {
    var examDate = $("#examDate").val().trim();
    var examAddress = $("#examAddress").val().trim();
    var $text = $("#addNewExamError");
    if(examDate==""){
        $text.text("考试时间不能为空!");
        return;
    }
    if(examAddress==""){
        $text.text("考试地点不能为空!");
        return;
    }
    $.MsgBox.Confirm("友情提示", "确认要将选中学员安排到新建考场中吗？", function () {
        var classExamArrayLis = $("#classExamArrayLis").val().trim();
        $.ajax({
            url:"addSaveNewExam",
            type:"post",
            data:{"examDate":examDate,"examAddress":examAddress,"classExamArrayLis":classExamArrayLis},
            dataType:"json",
            success:function(result){
                if(result.status==0){
                    $text.text(result.message);
                }else if(result.status==1){
                    $.MsgBox.AjaxAlert("友情提示",result.message,function(){
                        //$("#searchClassExam").submit();
                        window.location.href = window.location.href.replace(/#/g,'');
                    });
                }
            },
            error:function(){
                alert("安排失败");
            }
        });
    });
}

function check_ArrangeExistClass() {
    $.MsgBox.Confirm("友情提示", "确认要将选中学员安排到选中班级中吗？", function () {
        var noClassMemberArrayLis = $("#noClassMemberArrayLis").val().trim();
        var existClassSelect = $("#existClassSelect").val().trim();
        $.ajax({
            url:"addMembersIntoExitClass",
            type:"post",
            data:{"className":existClassSelect,"noClassMemberArrayLis":noClassMemberArrayLis},
            dataType:"json",
            success:function(result){
                if(result.status==0){
                    $("#errInfo_existClass").text(result.message);
                }else if(result.status==1){
                    $.MsgBox.AjaxAlert("友情提示",result.message,function(){
                        //$("#searchClassMembers").submit();
                        window.location.href = window.location.href.replace(/#/g,'');
                    });
                }
            },
            error:function(){
                alert("安排失败");
            }
        });
    });
}

function check_arrangeExistExam() {
    $.MsgBox.Confirm("友情提示", "确认要将选中学员安排到选中考场中吗？", function () {
        var classExamArrayLis = $("#classExamArrayLis").val().trim();
        var existExamSelect = $("#existExamSelect").val().trim();
        $.ajax({
            url:"addClassIntoExistExam",
            type:"post",
            data:{"examName":existExamSelect,"classExamArrayLis":classExamArrayLis},
            dataType:"json",
            success:function(result){
                if(result.status==0){
                    $("#errInfo_existExam").text(result.message);
                }else if(result.status==1){
                    $.MsgBox.AjaxAlert("友情提示",result.message,function(){
                        //$("#searchClassExam").submit();
                        window.location.href = window.location.href.replace(/#/g,'');
                    });
                }
            },
            error:function(){
                alert("安排失败");
            }
        });
    });
}

function btn_setExamPass(h) {
    $.MsgBox.Confirm("友情提示", "确认要将选中学员考试通过吗？", function () {
        var memberId = $(h).parent().parent().children().eq(1).text();
        $.ajax({
            url:"setExamPass",
            type:"post",
            data:{"id":memberId},
            dataType:"json",
            success:function(result){
                $.MsgBox.AjaxAlert("友情提示",result.message,function(){
                    //$("#searchGraduationMembers").submit();
                    window.location.href = window.location.href.replace(/#/g,'');
                });
            },
            error:function(){
                alert("操作失败");
            }
        });
    });
}

function btn_setExamNoPass(h) {
    $.MsgBox.Confirm("友情提示", "确认要将选中学员考试不通过吗？", function () {
        var memberId = $(h).parent().parent().children().eq(1).text();
        $.ajax({
            url:"setExamNoPass",
            type:"post",
            data:{"id":memberId},
            dataType:"json",
            success:function(result){
                $.MsgBox.AjaxAlert("友情提示",result.message,function(){
                    //$("#searchGraduationMembers").submit();
                    window.location.href = window.location.href.replace(/#/g,'');
                });
            },
            error:function(){
                alert("操作失败");
            }
        });
    });
}

function btn_modifyClassTeamInfo(h) {
    $.MsgBox.Confirm("友情提示", "确认要修改班级信息？", function () {
        var classId = $("#classTeamDetailInfoId").val().trim();
        var className = $("#classTeamDetailInfoName").val().trim();
        $.ajax({
            url:"modifyClassTeamName",
            type:"post",
            data:{"id":classId,"className":className},
            dataType:"json",
            success:function(result){
                $.MsgBox.AjaxAlert("友情提示",result.message,function(){
                    //$("#searchClassExam").submit();
                    window.location.href = window.location.href.replace(/#/g,'');
                });
            },
            error:function(){
                alert("操作失败");
            }
        });
    });
}

function btn_examRePass() {
    $.MsgBox.Confirm("友情提示", "确认要让该学员通过考试吗？", function () {
        var memberId = $("#examPassInput").val().trim();
        $.ajax({
            url:"setExamPass",
            type:"post",
            data:{"id":memberId},
            dataType:"json",
            success:function(result){
                $.MsgBox.AjaxAlert("友情提示",result.message,function(){
                    history.go(0);  });
            },
            error:function(){
                alert("操作失败");
            }
        });
    });
}

$(function() {
    $("#submitNewPwdButton").click(function () {
        var oldPwd = $("#oldPwd").val();
        var newPwd = $("#newPwd").val();
        var confirmPwd = $("#confirmPwd").val();
        if(oldPwd==""){
            $("#changePwdFailed").text("密码不能为空！");
        }else if(newPwd==""){
            $("#changePwdFailed").text("密码不能为空！");
        }else if(newPwd!==confirmPwd){
            $("#changePwdFailed").text("两次密码不同！");
        }else {
            $.ajax({
                url:"../backend/changePassword",
                type:"post",
                dataType:"json",
                data:{"oldPd":oldPwd,"newPd":newPwd},
                success:function(result){
                    if(result.status==0){
                        $("#changePwdSuccess").text(result.message);
                        $("#changePwdFailed").text("");
                    }else if(result.status==1){
                        $("#changePwdFailed").text(result.message);
                        $("#changePwdSuccess").text("");
                    }
                },
                error:function(){
                    alert("系统错误,修改密码失败！");
                }
            });
        }
        $("#oldPwd").val("");
        $("#newPwd").val("");
        $("#confirmPwd").val("");
    })
});

function btn_applyForCertificateSubmit(){
    var receiveName= $("#receiveName").val();
    var receiveAddress= $("#receiveAddress").val();
    var phoneNo= $("#phoneNo").val();
    if(receiveName=="") {
        $.MsgBox.Alert("友情提示","收件人不能为空！");
        return;
    }
    if(receiveAddress=="") {
        $.MsgBox.Alert("友情提示","收件地址不能为空！");
        return;
    }
    if(phoneNo=="") {
        $.MsgBox.Alert("友情提示","联系号码不能为空！");
        return;
    }
    $.MsgBox.Confirm("友情提示", "确认提交申请领证信息吗？", function () {
        $.ajax({
            url:"applyForCertificate",
            type:"post",
            data:{"receiveName":receiveName,"receiveAddress":receiveAddress,"phoneNo":phoneNo},
            dataType:"json",
            success:function(result){
                if(result.status==0){
                    $.MsgBox.Alert("友情提示",result.message);
                }else if(result.status==1){
                    $.MsgBox.AjaxAlert("友情提示",result.message,function(){
                        $("#applyForCertificateDiv").hide();
                        location.reload();
                    });
                }
            },
            error:function(e){
                $.MsgBox.Alert("错误提示", e.print());
            }
        });
    });
}

$("#pictureImg").live("change",function(){
    ajaxFileUpload();
});

//ajax文件上传
function ajaxFileUpload() {
    $.ajaxFileUpload
    ({
        url: 'ajaxFileUpload',
        secureuri: false,
        fileElementId: 'pictureImg',
        dataType: 'json',
        data: "",
        success:function(result){
            if(result.status==1){
                $("#img_photo").attr("src",result.body.pictureUri);
            }else {
                $.MsgBox.Alert("友情提示","程序出错了");
            }
        },
        error:function(e){
            $.MsgBox.Alert("错误信息", e.print());
        }
    });
}

//学员注册、报名
$(function() {
    $("#baominSubmit").click(function() {
        var reg=/^(13|14|15|17|18)\d{9}$/;
        var phoneNo = $("#phoneNo").val();
        var realName = $("#realName").val();
        var areaId = jQuery("#areaId  option:selected").val();
        var sex = $("input[name='sex']:checked").val();
        if(phoneNo=="") {
            $("#msgInfo").text("");
            $("#errInfo").text("手机号不能为空");
            return;
        }
        if(!reg.test(phoneNo)){
            $("#msgInfo").text("");
            $("#errInfo").text("请填入正确的手机号！");
            return false;
        }
        if(realName=="") {
            $("#msgInfo").text("");
            $("#errInfo").text("姓名不能为空");
            return;
        }
        if(sex==""||sex==null) {
            $("#msgInfo").text("");
            $("#errInfo").text("请选择性别");
            return;
        }
        if(areaId==""||areaId==null){
            $("#msgInfo").text("");
            $("#errInfo").text("请选择报名区域");
            return;
        }
        $.ajax({
            url:"baomin",
            type:"post",
            dataType:"json",
            data:{"phoneNo":phoneNo,"realName":realName,"sex":sex,"areaId":areaId},
            success:function(result){
                if(result.status==0) {
                    $("#msgInfo").text("");
                    $("#errInfo").text(result.message);
                }else if(result.status==1) {
                    $("#errInfo").text("");
                    $.MsgBox.AjaxAlert("友情提示",result.message,function() {
                        window.location.reload();
                    });
                }
            },
            error:function(){
                $.MsgBox.Alert("系统异常","请求失败");
            }
        });
    });
    $("#goToRegister").click(function() {
        $("#asaimsg").hide();
        $("#regDiv").show();
    });
    $("#goToLogin").click(function() {
        $("#regDiv").hide();
        $("#asaimsg").show();
    });
    $("#regPhoneNo").blur(checkPhoneNo);
    function checkPhoneNo() {
        var reg=/^(13|14|15|17|18)\d{9}$/;
        var phoneNo = $("#regPhoneNo").val();
        if(phoneNo=="") {
            $("#phoneRig").text("");
            $("#phoneErr").text("手机号不能为空");
            $("#regPhoneNo").focus();
            return false;
        }
        if(!reg.test(phoneNo)){
            $("#phoneRig").text("");
            $("#phoneErr").text("请填入正确的手机号！");
            $("#regPhoneNo").focus();
            return false;
        }
        $.ajax({
            url:"checkPhoneNo",
            type:"post",
            dataType:"json",
            data:{"phoneNo":phoneNo},
            success:function(result){
                if(result.status==0) {
                    $("#phoneRig").text("");
                    $("#phoneErr").text(result.message);
                }else if(result.status==1) {
                    $("#phoneErr").text("");
                    $("#phoneRig").text(result.message);
                }
            },
            error:function(){
                $.MsgBox.Alert("系统异常","请求失败");
                return false;
            }
        });
    }
    $("#registerBtn").click(function(){
        var reg=/^(13|14|15|17|18)\d{9}$/;
        var phoneNo = $("#regPhoneNo").val();
        var authCode = $("#regAuthCode").val();
        if(phoneNo=="") {
            $("#regMsgInfo").text("");
            $("#phoneErr").text("手机号不能为空");
            return;
        }
        if(!reg.test(phoneNo)){
            $("#regMsgInfo").text("");
            $("#phoneErr").text("请填入正确的手机号！");
            return;
        }
        if(authCode=="") {
            $.MsgBox.Alert("友情提示","验证码不能为空");
            return;
        }
        $.ajax({
            url:"register",
            type:"post",
            dataType:"json",
            data:{"phoneNo":phoneNo,"authCode":authCode},
            success:function(result){
                if(result.status==0) {
                    $("#regMsgInfo").text("");
                    $("#regErrInfo").text(result.message);
                }else if(result.status==1) {
                    $("#regPhoneNo").val("");
                    $("#regRealName").val("");
                    $("#regErrInfo").text("");
                    $("#phoneErr").text("");
                    $("#phoneRig").text("");
                    alert("注册成功，初始密码为您的手机后四位，请牢记！");
                    $("#username").val(result.message);
                    $("#password").val(result.message.substring(7,11));
                    $("#btn_login").click();
                }
            },
            error:function(){
                $.MsgBox.Alert("系统异常","请求失败");
            }
        });
    });
    $("#regAuthCodeBtn").click(function() {
        var phoneNo = $("#regPhoneNo").val();
        $.ajax({
            url:"sendSMS",
            type:"post",
            dataType:"json",
            data:{"phone":phoneNo,"type":1},
            success:function(result) {
                if(result.status==1) {
                    var o = document.getElementById("regAuthCodeBtn");
                    settime(o);
                    $.MsgBox.Alert("友情提示",result.message);
                }else if(result.status==0) {
                    $.MsgBox.Alert("错误信息",result.message);
                }
            },
            error:function() {
                $.MsgBox.Alert("系统异常","请求失败");
            }
        });
    });
    var wait=60;
    function settime(o) {
        if (wait == 0) {
            o.removeAttribute("disabled");
            o.setAttribute("style","background-color : #258BFF");
            o.value="获取验证码";
            wait = 60;
        } else {
            o.setAttribute("disabled", true);
            o.setAttribute("style","background-color : #999999");
            o.value="重新发送(" + wait + ")";
            wait--;
            setTimeout(function() {settime(o)}, 1000)
        }
    }
});
function hideRegisterForm() {
    $("#regPhoneNo").val("");
    $("#regRealName").val("");
    $("input[name='regSex']").removeAttr('checked');
    $("#regErrInfo").text("");
    $("#regMsgInfo").text("");
    $("#phoneErr").text("");
    $("#phoneRig").text("");
    document.getElementById('regDiv').style.display='none';document.getElementById('asaimsgbg').style.display='none';
}

$(function () {
   $("#showLoginForm").click(function(){
       $("#loginFailedInfo").text("");
       $("#asaimsg").show();
       $("#asaimsgbg").show();
   });
});

function pleaseLoginFirst() {
    $.MsgBox.Confirm("友情提示","您需要先登录才可以进行此项操作噢~",function() {
        document.getElementById('asaimsg').style.display='block';document.getElementById('asaimsgbg').style.display='block';
    });
}

