$(function () {
    $("#examDate").datetimepicker({
        dateFormat:'yy-mm-dd',
        showSecond: true,
        timeFormat: 'hh:mm:ss',
        stepHour: 1,
        stepMinute: 1,
        stepSecond: 1,
        showAnim:"fadeIn",       //显示日历的效果slide、fadeIn、show等
        changeMonth:false,       //是否显示月份的下拉菜单，默认为false
        changeYear:true,        //是否显示年份的下拉菜单，默认为false
        showWeek:false,          //是否显示星期,默认为false
        showButtonPanel:true,   //是否显示取消按钮，并含有today按钮，默认为false
        yearRange:'2010:2060',  //显示可供选择的年份
        defaultDate:+0          //表示默认日期是在当前日期加0天
    })
});
