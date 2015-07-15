$(function(){
    var height = $(window).height();
    var contentHeight = height - (50);
    var contentHeight2 = height - (115);
    $(".sidebar-wrap").css("height",contentHeight);
    $(".jp-container").css("max-height",contentHeight2);
});
$(window).resize(function () {
    var height = $(window).height();
    var contentHeight = height - (50);
    var contentHeight2 = height - (115);
    $(".sidebar-wrap").css("height",contentHeight);
    $(".jp-container").css("max-height",contentHeight2);
    //刷新
    location.reload();
});