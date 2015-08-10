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
            _html += '<a id="mb_ico">x</a><div id="mb_msg">' + msg + '</div><div id="mb_btnbox">';
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
        }
        if (type == "confirm" || type == "Save") {
            _html += '<a id="mb_ico">x</a><div id="mb_msg">' + msg + '</div><div id="mb_btnbox">';
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
            _html += '<input id="mb_btn_no" type="button" value="取消" />';
        }
        if (type == "ajaxAlert") {
            _html += '<div id="mb_msg">' + msg + '</div><div id="mb_btnbox">';
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
            display: 'block', fontSize: '14px', color: '#444', padding: '10px 15px',
            backgroundColor: '#DDD', borderRadius: '15px 15px 0 0',
            borderBottom: '3px solid #009BFE', fontWeight: 'bold'
        });
        $("#mb_msg").css({
            padding: '20px', lineHeight: '20px',
            borderBottom: '1px dashed #DDD', fontSize: '13px'
        });
        $mb_ico.css({
            display: 'block', position: 'absolute', right: '10px', top: '9px',
            border: '1px solid Gray', width: '18px', height: '18px', textAlign: 'center',
            lineHeight: '16px', cursor: 'pointer', borderRadius: '12px', fontFamily: '微软雅黑'
        });
        $("#mb_btnbox").css({margin: '15px 0 10px 0', textAlign: 'center'});
        $("#mb_btn_ok,#mb_btn_no").css({width: '85px', height: '30px', color: 'white', border: 'none'});
        $("#mb_btn_ok").css({backgroundColor: '#007CFF'});
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
        $.MsgBox.Confirm("温馨提示", "执行删除后将无法恢复，确定继续吗？", function () {
            /*alert("你居然真的删除了...");*/
        });
    });
});


function check_noClassMember() {
    $.MsgBox.Alert("温馨提示", "请选择需要安排分班的学员！");
}

function check_classExam() {
    $.MsgBox.Alert("温馨提示", "请选择需要安排考场的班级！");
}

function check_payMember() {
    $.MsgBox.Alert("温馨提示", "请选择需要确认缴费的学员！");
}

function check_ExamMember() {
    $.MsgBox.Alert("温馨提示", "请选择需要确认通过的学员！");
}

function check_payEnter() {
    $.MsgBox.Confirm("温馨提示", "确认要将选中学员的状态改为已交费吗？", function () {
        var arrayLis = $("#checkPayLis").val().trim();
        $.ajax({
            url:"checkPayList",
            type:"post",
            data:{"checkPayLis":arrayLis},
            dataType:"json",
            success:function(result){
                $.MsgBox.AjaxAlert("温馨提示",result.message,function(){ $("#searchMemberInfo").submit();});
            },
            error:function(){
                alert("安排失败");
            }
        });
    });
}

function check_ExamMemberEnter() {
    $.MsgBox.Confirm("温馨提示", "确认要将选中学员的状态改为通过吗？", function () {
        var arrayLis = $("#checkExamMemberList").val().trim();
        $.ajax({
            url:"allMemberPassExam",
            type:"post",
            data:{"checkExamMemberList":arrayLis},
            dataType:"json",
            success:function(result){
                $.MsgBox.AjaxAlert("温馨提示",result.message,function(){ $("#searchGraduationMembers").submit();});
            },
            error:function(){
                alert("安排失败");
            }
         });
    });
}

function check_arrangeNewClass() {
    var className = $("#className").val().trim();
    if(className=="") {
        $("#addNewClassTeamError").html("班级名称不能为空!");
        return;
    }
    $.MsgBox.Confirm("温馨提示", "确认要将选中学员安排到新建班级中吗？", function () {
        var noClassMemberArrayLis = $("#noClassMemberArrayLis").val().trim();
        $.ajax({
            url:"addSaveNewClassTeam",
            type:"post",
            data:{"className":className,"noClassMemberArrayLis":noClassMemberArrayLis},
            dataType:"json",
            success:function(result){
                if(result.status==0){
                    $("#addNewClassTeamError").text(result.message);
                }else if(result.status==1){
                    $.MsgBox.AjaxAlert("温馨提示",result.message,function(){ $("#searchClassMembers").submit();});
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
    if(examDate==""){
        $("#addNewExamError").html("考试时间不能为空!");
        return;
    }
    if(examAddress==""){
        $("#addNewExamError").html("考试地点不能为空!");
        return;
    }
    $.MsgBox.Confirm("温馨提示", "确认要将选中学员安排到新建考场中吗？", function () {
        var classExamArrayLis = $("#classExamArrayLis").val().trim();
        $.ajax({
            url:"addSaveNewExam",
            type:"post",
            data:{"examDate":examDate,"examAddress":examAddress,"classExamArrayLis":classExamArrayLis},
            dataType:"json",
            success:function(result){
                if(result.status==0){
                    $("#addNewExamError").text(result.message);
                }else if(result.status==1){
                    $.MsgBox.AjaxAlert("温馨提示",result.message,function(){ $("#searchClassExam").submit();});
                }
            },
            error:function(){
                alert("安排失败");
            }
        });
    });
}

function check_ArrangeExistClass() {
    $.MsgBox.Confirm("温馨提示", "确认要将选中学员安排到选中班级中吗？", function () {
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
                    $.MsgBox.AjaxAlert("温馨提示",result.message,function(){ $("#searchClassMembers").submit();});
                }
            },
            error:function(){
                alert("安排失败");
            }
        });
    });
}

function check_arrangeExistExam() {
    $.MsgBox.Confirm("温馨提示", "确认要将选中学员安排到选中考场中吗？", function () {
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
                    $.MsgBox.AjaxAlert("温馨提示",result.message,function(){ $("#searchClassExam").submit();});
                }
            },
            error:function(){
                alert("安排失败");
            }
        });
    });
}

function btn_setExamPass(h) {
    $.MsgBox.Confirm("温馨提示", "确认要将选中学员考试通过吗？", function () {
        var memberId = $(h).parent().parent().children().eq(1).text();
        $.ajax({
            url:"setExamPass",
            type:"post",
            data:{"id":memberId},
            dataType:"json",
            success:function(result){
                $.MsgBox.AjaxAlert("温馨提示",result.message,function(){ $("#searchGraduationMembers").submit();});
            },
            error:function(){
                alert("操作失败");
            }
        });
    });
}

function btn_setExamNoPass(h) {
    $.MsgBox.Confirm("温馨提示", "确认要将选中学员考试不通过吗？", function () {
        var memberId = $(h).parent().parent().children().eq(1).text();
        $.ajax({
            url:"setExamNoPass",
            type:"post",
            data:{"id":memberId},
            dataType:"json",
            success:function(result){
                $.MsgBox.AjaxAlert("温馨提示",result.message,function(){ $("#searchGraduationMembers").submit();});
            },
            error:function(){
                alert("操作失败");
            }
        });
    });
}

function btn_modifyClassTeamInfo(h) {
    $.MsgBox.Confirm("温馨提示", "确认要修改班级信息？", function () {
        var classId = $("#classTeamDetailInfoId").val().trim();
        var className = $("#classTeamDetailInfoName").val().trim();
        $.ajax({
            url:"modifyClassTeamName",
            type:"post",
            data:{"id":classId,"className":className},
            dataType:"json",
            success:function(result){
                $.MsgBox.AjaxAlert("温馨提示",result.message,function(){ $("#searchClassExam").submit();});
            },
            error:function(){
                alert("操作失败");
            }
        });
    });
}

function btn_examRePass() {
    $.MsgBox.Confirm("温馨提示", "确认要让该学员通过考试吗？", function () {
        var memberId = $("#examPassInput").val().trim();
        $.ajax({
            url:"setExamPass",
            type:"post",
            data:{"id":memberId},
            dataType:"json",
            success:function(result){
                $.MsgBox.AjaxAlert("温馨提示",result.message,function(){
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
    $.MsgBox.Confirm("温馨提示", "确认提交申请领证信息吗？", function () {
        var receiveName= $("#receiveName").val();
        var receiveAddress= $("#receiveAddress").val();
        var contactAddress= $("#contactAddress").val();
        var phoneNo= $("#phoneNo").val();
        if(receiveName=="") {
            $.MsgBox.Alert("温馨提示","收件人不能为空！");
            return;
        }
        if(receiveAddress=="") {
            $.MsgBox.Alert("温馨提示","收件地址不能为空！");
            return;
        }
        if(contactAddress=="") {
            $.MsgBox.Alert("温馨提示","联系地址不能为空！");
            return;
        }
        if(phoneNo=="") {
            $.MsgBox.Alert("温馨提示","联系号码不能为空！");
            return;
        }
        $.ajax({
            url:"applyForCertificate",
            type:"post",
            data:{"receiveName":receiveName,"receiveAddress":receiveAddress,"contactAddress":contactAddress,"phoneNo":phoneNo},
            dataType:"json",
            success:function(result){
                if(result.status==0){
                    $.MsgBox.Alert("温馨提示",result.message);
                }else if(result.status==1){
                    $.MsgBox.AjaxAlert("温馨提示",result.message,function(){
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

$(function() {
   $("#pictureImg").change(function() {
       ajaxFileUpload();
   });
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
                $.MsgBox.AjaxAlert("温馨提示",result.message,function(){
                    $("#img_photo").attr("src",result.body.pictureUri);
                });
            }else {
                $.MsgBox.Alert("温馨提示","程序出错了");
            }
        },
        error:function(e){
            $.MsgBox.Alert("错误信息", e.print());
        }
    });
}
