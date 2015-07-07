//jquery����򵯴�
(function($) {
    $.MsgBox = {
        Alert: function (title, msg) {
            GenerateHtml("alert", title, msg);
            btnOk();  //alertֻ�ǵ�����Ϣ�����û��Ҫ�õ��ص�����callback
            btnNo();
        },
        Confirm: function (title, msg, callback) {
            GenerateHtml("confirm", title, msg);
            btnOk(callback);
            btnNo();
        },
        Save: function (title, msg) {
            GenerateHtml("confirm", title, msg);
            btnOk();
            btnNo();
        }
    }
    //����Html
    var GenerateHtml = function (type, title, msg) {
        var _html = "";
        _html += '<div id="mb_box"></div><div id="mb_con"><span id="mb_tit">' + title + '</span>';
        _html += '<a id="mb_ico">x</a><div id="mb_msg">' + msg + '</div><div id="mb_btnbox">';
        if (type == "alert") {
            _html += '<input id="mb_btn_ok" type="button" value="ȷ��" />';
        }
        if (type == "confirm" ||type=="Save") {
            _html += '<input id="mb_btn_ok" type="button" value="ȷ��" />';
            _html += '<input id="mb_btn_no" type="button" value="ȡ��" />';
        }
        _html += '</div></div>';
        //�����Ƚ�_html��ӵ�body��������Css��ʽ
        $("body").append(_html); GenerateCss();
    }
    //����Css
    var GenerateCss = function () {
        $("#mb_box").css({ width: '100%', height: '100%', zIndex: '99999', position: 'fixed',
            filter: 'Alpha(opacity=60)', backgroundColor: 'black', top: '0', left: '0', opacity: '0.6'
        });
        $("#mb_con").css({ zIndex: '999999', width: '400px', position: 'fixed',
            backgroundColor: 'White', borderRadius: '15px'
        });
        $("#mb_tit").css({ display: 'block', fontSize: '14px', color: '#444', padding: '10px 15px',
            backgroundColor: '#DDD', borderRadius: '15px 15px 0 0',
            borderBottom: '3px solid #009BFE', fontWeight: 'bold'
        });
        $("#mb_msg").css({ padding: '20px', lineHeight: '20px',
            borderBottom: '1px dashed #DDD', fontSize: '13px'
        });
        $("#mb_ico").css({ display: 'block', position: 'absolute', right: '10px', top: '9px',
            border: '1px solid Gray', width: '18px', height: '18px', textAlign: 'center',
            lineHeight: '16px', cursor: 'pointer', borderRadius: '12px', fontFamily: '΢���ź�'
        });
        $("#mb_btnbox").css({ margin: '15px 0 10px 0', textAlign: 'center' });
        $("#mb_btn_ok,#mb_btn_no").css({ width: '85px', height: '30px', color: 'white', border: 'none' });
        $("#mb_btn_ok").css({ backgroundColor: '#168bbb' });
        $("#mb_btn_no").css({ backgroundColor: 'gray', marginLeft: '20px' });
        //���Ͻǹرհ�ťhover��ʽ
        $("#mb_ico").hover(function () {
            $(this).css({ backgroundColor: 'Red', color: 'White' });
        }, function () {
            $(this).css({ backgroundColor: '#DDD', color: 'black' });
        });
        var _widht = document.documentElement.clientWidth;  //��Ļ��
        var _height = document.documentElement.clientHeight; //��Ļ��
        var boxWidth = $("#mb_con").width();
        var boxHeight = $("#mb_con").height();
        //����ʾ�����
        $("#mb_con").css({ top: (_height - boxHeight) / 2 + "px", left: (_widht - boxWidth) / 2 + "px" });
    }
    //ȷ����ť�¼�
    var btnOk = function (callback) {
        $("#mb_btn_ok").click(function () {
            $("#mb_box,#mb_con").remove();
            if (typeof (callback) == 'function') {
                callback();
            }
        });
    }
    //ȡ����ť�¼�
    var btnNo = function () {
        $("#mb_btn_no,#mb_ico").click(function () {
            $("#mb_box,#mb_con").remove();
        });
    }
})(jQuery);

$(document).ready(function () {
            //��̬ҳ�浯�����ʱ��Ӧ��
            //$("#link-add").bind("click", function () {
            //    $.MsgBox.Alert("��Ϣ", "��ӳɹ���");
            //});
//              �ص���������ֱ��д����function(){}
            $(".link-delete").bind("click", function () {
                $.MsgBox.Confirm("��ܰ��ʾ", "ִ��ɾ�����޷��ָ���ȷ��������", function () {
                    alert("���Ȼ���ɾ����..."); });
            });
//            function test() {
//                alert("������ȷ��,�������޸�");
//            }
//            //Ҳ���Դ������� test
//            $("#update").bind("click", function () {
//                $.MsgBox.Confirm("��ܰ��ʾ", "ȷ��Ҫ�����޸���", test);
//            });
});

//��ȡͬtd�µĵ�3�������ӣ�����ɾ����a href
//ע�⴫�ݵĲ�����$.MsgBox�Ѿ���this��Ϊ��������
//Confirm�����������js���涨�壬����������Ϊ���ȷ����Ĺ���
//$(h).parents().children().eq(3)[0]���ǻ�ȡ����ͬtd�µĵ��������� .find()����׼ȷ�ҵ� .next()Ҳѡ���У�������ǰ̨����
//.click() ���ǵ����ǰ���ӵ�Ч��
function check_del(h){
    $.MsgBox.Confirm("��ܰ��ʾ", "ִ��ɾ�����޷��ָ���ȷ��������", function () {$(h).parents().children().eq(3)[0].click();});
}

function check_save(){
    if(true){
        $.MsgBox.Save("��ܰ��ʾ", "�����������ݣ�ȷ��������");
        return false;
    }
}

function check_add(){
     $.MsgBox.Alert("��Ϣ", "��ӳɹ���");
}