/*!
 * jQuery Mousewheel 3.1.12
 *
 * Copyright 2014 jQuery Foundation and other contributors
 * Released under the MIT license.
 * http://jquery.org/license
 */
(function(a){if(typeof define==="function"&&define.amd){define(["jquery"],a)}else{if(typeof exports==="object"){module.exports=a}else{a(jQuery)}}}(function(c){var d=["wheel","mousewheel","DOMMouseScroll","MozMousePixelScroll"],k=("onwheel" in document||document.documentMode>=9)?["wheel"]:["mousewheel","DomMouseScroll","MozMousePixelScroll"],h=Array.prototype.slice,j,b;if(c.event.fixHooks){for(var e=d.length;e;){c.event.fixHooks[d[--e]]=c.event.mouseHooks}}var f=c.event.special.mousewheel={version:"3.1.12",setup:function(){if(this.addEventListener){for(var m=k.length;m;){this.addEventListener(k[--m],l,false)}}else{this.onmousewheel=l}c.data(this,"mousewheel-line-height",f.getLineHeight(this));c.data(this,"mousewheel-page-height",f.getPageHeight(this))},teardown:function(){if(this.removeEventListener){for(var m=k.length;m;){this.removeEventListener(k[--m],l,false)}}else{this.onmousewheel=null}c.removeData(this,"mousewheel-line-height");c.removeData(this,"mousewheel-page-height")},getLineHeight:function(m){var i=c(m),n=i["offsetParent" in c.fn?"offsetParent":"parent"]();if(!n.length){n=c("body")}return parseInt(n.css("fontSize"),10)||parseInt(i.css("fontSize"),10)||16},getPageHeight:function(i){return c(i).height()},settings:{adjustOldDeltas:true,normalizeOffset:true}};c.fn.extend({mousewheel:function(i){return i?this.bind("mousewheel",i):this.trigger("mousewheel")},unmousewheel:function(i){return this.unbind("mousewheel",i)}});function l(i){var o=i||window.event,u=h.call(arguments,1),w=0,q=0,p=0,t=0,s=0,r=0;i=c.event.fix(o);i.type="mousewheel";if("detail" in o){p=o.detail*-1}if("wheelDelta" in o){p=o.wheelDelta}if("wheelDeltaY" in o){p=o.wheelDeltaY}if("wheelDeltaX" in o){q=o.wheelDeltaX*-1}if("axis" in o&&o.axis===o.HORIZONTAL_AXIS){q=p*-1;p=0}w=p===0?q:p;if("deltaY" in o){p=o.deltaY*-1;w=p}if("deltaX" in o){q=o.deltaX;if(p===0){w=q*-1}}if(p===0&&q===0){return}if(o.deltaMode===1){var v=c.data(this,"mousewheel-line-height");w*=v;p*=v;q*=v}else{if(o.deltaMode===2){var n=c.data(this,"mousewheel-page-height");w*=n;p*=n;q*=n}}t=Math.max(Math.abs(p),Math.abs(q));if(!b||t<b){b=t;if(a(o,t)){b/=40}}if(a(o,t)){w/=40;q/=40;p/=40}w=Math[w>=1?"floor":"ceil"](w/b);q=Math[q>=1?"floor":"ceil"](q/b);p=Math[p>=1?"floor":"ceil"](p/b);if(f.settings.normalizeOffset&&this.getBoundingClientRect){var m=this.getBoundingClientRect();s=i.clientX-m.left;r=i.clientY-m.top}i.deltaX=q;i.deltaY=p;i.deltaFactor=b;i.offsetX=s;i.offsetY=r;i.deltaMode=0;u.unshift(i,w,q,p);if(j){clearTimeout(j)}j=setTimeout(g,200);return(c.event.dispatch||c.event.handle).apply(this,u)}function g(){b=null}function a(m,i){return f.settings.adjustOldDeltas&&m.type==="mousewheel"&&i%120===0}}));$(function(){var b=$(".jp-container").jScrollPane({verticalGutter:-16}),c={extPluginOpts:{mouseLeaveFadeSpeed:500,hovertimeout_t:1000,useTimeout:true,deviceWidth:980},hovertimeout:null,isScrollbarHover:false,elementtimeout:null,isScrolling:false,addHoverFunc:function(){if($(window).width()<=this.extPluginOpts.deviceWidth){return false}var d=this;$.fn.jspmouseenter=$.fn.show;$.fn.jspmouseleave=$.fn.fadeOut;var e=this.getContentPane().siblings(".jspVerticalBar").hide();b.bind("mouseenter.jsp",function(){e.stop(true,true).jspmouseenter();if(!d.extPluginOpts.useTimeout){return false}clearTimeout(d.hovertimeout);d.hovertimeout=setTimeout(function(){if(!d.isScrolling){e.stop(true,true).jspmouseleave(d.extPluginOpts.mouseLeaveFadeSpeed||0)}},d.extPluginOpts.hovertimeout_t)}).bind("mouseleave.jsp",function(){if(!d.extPluginOpts.useTimeout){e.stop(true,true).jspmouseleave(d.extPluginOpts.mouseLeaveFadeSpeed||0)}else{clearTimeout(d.elementtimeout);if(!d.isScrolling){e.stop(true,true).jspmouseleave(d.extPluginOpts.mouseLeaveFadeSpeed||0)}}});if(this.extPluginOpts.useTimeout){b.bind("scrollstart.jsp",function(){clearTimeout(d.hovertimeout);d.isScrolling=true;e.stop(true,true).jspmouseenter()}).bind("scrollstop.jsp",function(){clearTimeout(d.hovertimeout);d.isScrolling=false;d.hovertimeout=setTimeout(function(){if(!d.isScrollbarHover){e.stop(true,true).jspmouseleave(d.extPluginOpts.mouseLeaveFadeSpeed||0)}},d.extPluginOpts.hovertimeout_t)});var f=$("<div/>").css({position:"absolute",left:e.css("left"),top:e.css("top"),right:e.css("right"),bottom:e.css("bottom"),width:e.width(),height:e.height()}).bind("mouseenter.jsp",function(){clearTimeout(d.hovertimeout);clearTimeout(d.elementtimeout);d.isScrollbarHover=true;d.elementtimeout=setTimeout(function(){e.stop(true,true).jspmouseenter()},100)}).bind("mouseleave.jsp",function(){clearTimeout(d.hovertimeout);d.isScrollbarHover=false;d.hovertimeout=setTimeout(function(){if(!d.isScrolling){e.stop(true,true).jspmouseleave(d.extPluginOpts.mouseLeaveFadeSpeed||0)}},d.extPluginOpts.hovertimeout_t)});e.wrap(f)}}},a=b.data("jsp");$.extend(true,a,c);a.addHoverFunc()});$(function(){var b=$(".jp-container2").jScrollPane({verticalGutter:-16}),c={extPluginOpts:{mouseLeaveFadeSpeed:500,hovertimeout_t:1000,useTimeout:true,deviceWidth:980},hovertimeout:null,isScrollbarHover:false,elementtimeout:null,isScrolling:false,addHoverFunc:function(){if($(window).width()<=this.extPluginOpts.deviceWidth){return false
}var d=this;$.fn.jspmouseenter=$.fn.show;$.fn.jspmouseleave=$.fn.fadeOut;var e=this.getContentPane().siblings(".jspVerticalBar").hide();b.bind("mouseenter.jsp",function(){e.stop(true,true).jspmouseenter();if(!d.extPluginOpts.useTimeout){return false}clearTimeout(d.hovertimeout);d.hovertimeout=setTimeout(function(){if(!d.isScrolling){e.stop(true,true).jspmouseleave(d.extPluginOpts.mouseLeaveFadeSpeed||0)}},d.extPluginOpts.hovertimeout_t)}).bind("mouseleave.jsp",function(){if(!d.extPluginOpts.useTimeout){e.stop(true,true).jspmouseleave(d.extPluginOpts.mouseLeaveFadeSpeed||0)}else{clearTimeout(d.elementtimeout);if(!d.isScrolling){e.stop(true,true).jspmouseleave(d.extPluginOpts.mouseLeaveFadeSpeed||0)}}});if(this.extPluginOpts.useTimeout){b.bind("scrollstart.jsp",function(){clearTimeout(d.hovertimeout);d.isScrolling=true;e.stop(true,true).jspmouseenter()}).bind("scrollstop.jsp",function(){clearTimeout(d.hovertimeout);d.isScrolling=false;d.hovertimeout=setTimeout(function(){if(!d.isScrollbarHover){e.stop(true,true).jspmouseleave(d.extPluginOpts.mouseLeaveFadeSpeed||0)}},d.extPluginOpts.hovertimeout_t)});var f=$("<div/>").css({position:"absolute",left:e.css("left"),top:e.css("top"),right:e.css("right"),bottom:e.css("bottom"),width:e.width(),height:e.height()}).bind("mouseenter.jsp",function(){clearTimeout(d.hovertimeout);clearTimeout(d.elementtimeout);d.isScrollbarHover=true;d.elementtimeout=setTimeout(function(){e.stop(true,true).jspmouseenter()},100)}).bind("mouseleave.jsp",function(){clearTimeout(d.hovertimeout);d.isScrollbarHover=false;d.hovertimeout=setTimeout(function(){if(!d.isScrolling){e.stop(true,true).jspmouseleave(d.extPluginOpts.mouseLeaveFadeSpeed||0)}},d.extPluginOpts.hovertimeout_t)});e.wrap(f)}}},a=b.data("jsp");$.extend(true,a,c);a.addHoverFunc()});