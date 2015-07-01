$(function (){
    $("#datepicker").datepicker({
        dateFormat:'yy.mm.dd',  //更改时间显示模式
        showAnim:"fadeIn",       //显示日历的效果slide、fadeIn、show等
        changeMonth:false,       //是否显示月份的下拉菜单，默认为false
        changeYear:true,        //是否显示年份的下拉菜单，默认为false
        showWeek:false,          //是否显示星期,默认为false
        showButtonPanel:true,   //是否显示取消按钮，并含有today按钮，默认为false
        yearRange:'2010:2060',  //显示可供选择的年份
        defaultDate:+0          //表示默认日期是在当前日期加0天

    });

    $.datepicker.regional['zh-CN'] = {
        closeText: '关闭',
        prevText: '&#x3c;上月',
        nextText: '下月&#x3e;',
        currentText: '今天',
        monthNames: ['1 月','2 月','3 月','4 月','5 月','6 月','7 月','8 月','9 月','10月','11月','12月'],
        monthNamesShort: ['一','二','三','四','五','六','七','八','九','十','十一','十二'],
        dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],
        dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],
        dayNamesMin: ['日','一','二','三','四','五','六'],
        weekHeader: '周',
        firstDay: 1,
        isRTL: false,
        showMonthAfterYear: true,
        yearSuffix: '年'};
    $.datepicker.setDefaults($.datepicker.regional['zh-CN']);
});