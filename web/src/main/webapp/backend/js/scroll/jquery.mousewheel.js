/*! Copyright (c) 2010 Brandon Aaron (http://brandonaaron.net)
 * Licensed under the MIT License (LICENSE.txt).
 *
 * Thanks to: http://adomas.org/javascript-mouse-wheel/ for some pointers.
 * Thanks to: Mathias Bank(http://www.mathias-bank.de) for a scope bug fix.
 * Thanks to: Seamus Leahy for adding deltaX and deltaY
 *
 * Version: 3.0.4
 *
 * Requires: 1.2.2+
 */

(function($) {

    var types = ['DOMMouseScroll', 'mousewheel'];

    $.event.special.mousewheel = {
        setup: function() {
            if ( this.addEventListener ) {
                for ( var i=types.length; i; ) {
                    this.addEventListener( types[--i], handler, false );
                }
            } else {
                this.onmousewheel = handler;
            }
        },

        teardown: function() {
            if ( this.removeEventListener ) {
                for ( var i=types.length; i; ) {
                    this.removeEventListener( types[--i], handler, false );
                }
            } else {
                this.onmousewheel = null;
            }
        }
    };

    $.fn.extend({
        mousewheel: function(fn) {
            return fn ? this.bind("mousewheel", fn) : this.trigger("mousewheel");
        },

        unmousewheel: function(fn) {
            return this.unbind("mousewheel", fn);
        }
    });


    function handler(event) {
        var orgEvent = event || window.event, args = [].slice.call( arguments, 1 ), delta = 0, returnValue = true, deltaX = 0, deltaY = 0;
        event = $.event.fix(orgEvent);
        event.type = "mousewheel";

        // Old school scrollwheel delta
        if ( event.wheelDelta ) { delta = event.wheelDelta/120; }
        if ( event.detail     ) { delta = -event.detail/3; }

        // New school multidimensional scroll (touchpads) deltas
        deltaY = delta;

        // Gecko
        if ( orgEvent.axis !== undefined && orgEvent.axis === orgEvent.HORIZONTAL_AXIS ) {
            deltaY = 0;
            deltaX = -1*delta;
        }

        // Webkit
        if ( orgEvent.wheelDeltaY !== undefined ) { deltaY = orgEvent.wheelDeltaY/120; }
        if ( orgEvent.wheelDeltaX !== undefined ) { deltaX = -1*orgEvent.wheelDeltaX/120; }

        // Add event and delta to the front of the arguments
        args.unshift(event, delta, deltaX, deltaY);

        //ÐÞ¸ÄhandleÎªdispatch
        return $.event.dispatch.apply(this, args);
    }

})(jQuery);

$(function() {

    // the element we want to apply the jScrollPane
    var $el	= $('.jp-container').jScrollPane({
            verticalGutter 	: -16
        }),

    // the extension functions and options
        extensionPlugin 	= {

            extPluginOpts	: {
                // speed for the fadeOut animation
                mouseLeaveFadeSpeed	: 500,
                // scrollbar fades out after hovertimeout_t milliseconds
                hovertimeout_t		: 1000,
                // also, it will be shown when we start to scroll and hidden when stopping
                useTimeout			: true,
                // the extension only applies for devices with width > deviceWidth
                deviceWidth			: 980
            },
            hovertimeout	: null, // timeout to hide the scrollbar
            isScrollbarHover: false,// true if the mouse is over the scrollbar
            elementtimeout	: null,	// avoids showing the scrollbar when moving from inside the element to outside, passing over the scrollbar
            isScrolling		: false,// true if scrolling
            addHoverFunc	: function() {

                // run only if the window has a width bigger than deviceWidth
                if( $(window).width() <= this.extPluginOpts.deviceWidth ) return false;

                var instance		= this;

                // functions to show / hide the scrollbar
                $.fn.jspmouseenter 	= $.fn.show;
                $.fn.jspmouseleave 	= $.fn.fadeOut;

                // hide the jScrollPane vertical bar
                var $vBar			= this.getContentPane().siblings('.jspVerticalBar').hide();

                /*
                 * mouseenter / mouseleave events on the main element
                 * also scrollstart / scrollstop - @James Padolsey : http://james.padolsey.com/javascript/special-scroll-events-for-jquery/
                 */
                $el.bind('mouseenter.jsp',function() {

                    // show the scrollbar
                    $vBar.stop( true, true ).jspmouseenter();

                    if( !instance.extPluginOpts.useTimeout ) return false;

                    // hide the scrollbar after hovertimeout_t ms
                    clearTimeout( instance.hovertimeout );
                    instance.hovertimeout 	= setTimeout(function() {
                        // if scrolling at the moment don't hide it
                        if( !instance.isScrolling )
                            $vBar.stop( true, true ).jspmouseleave( instance.extPluginOpts.mouseLeaveFadeSpeed || 0 );
                    }, instance.extPluginOpts.hovertimeout_t );


                }).bind('mouseleave.jsp',function() {

                    // hide the scrollbar
                    if( !instance.extPluginOpts.useTimeout )
                        $vBar.stop( true, true ).jspmouseleave( instance.extPluginOpts.mouseLeaveFadeSpeed || 0 );
                    else {
                        clearTimeout( instance.elementtimeout );
                        if( !instance.isScrolling )
                            $vBar.stop( true, true ).jspmouseleave( instance.extPluginOpts.mouseLeaveFadeSpeed || 0 );
                    }

                });

                if( this.extPluginOpts.useTimeout ) {

                    $el.bind('scrollstart.jsp', function() {

                        // when scrolling show the scrollbar
                        clearTimeout( instance.hovertimeout );
                        instance.isScrolling	= true;
                        $vBar.stop( true, true ).jspmouseenter();

                    }).bind('scrollstop.jsp', function() {

                        // when stop scrolling hide the scrollbar (if not hovering it at the moment)
                        clearTimeout( instance.hovertimeout );
                        instance.isScrolling	= false;
                        instance.hovertimeout 	= setTimeout(function() {
                            if( !instance.isScrollbarHover )
                                $vBar.stop( true, true ).jspmouseleave( instance.extPluginOpts.mouseLeaveFadeSpeed || 0 );
                        }, instance.extPluginOpts.hovertimeout_t );

                    });

                    // wrap the scrollbar
                    // we need this to be able to add the mouseenter / mouseleave events to the scrollbar
                    var $vBarWrapper	= $('<div/>').css({
                        position	: 'absolute',
                        left		: $vBar.css('left'),
                        top			: $vBar.css('top'),
                        right		: $vBar.css('right'),
                        bottom		: $vBar.css('bottom'),
                        width		: $vBar.width(),
                        height		: $vBar.height()
                    }).bind('mouseenter.jsp',function() {

                        clearTimeout( instance.hovertimeout );
                        clearTimeout( instance.elementtimeout );

                        instance.isScrollbarHover	= true;

                        // show the scrollbar after 100 ms.
                        // avoids showing the scrollbar when moving from inside the element to outside, passing over the scrollbar
                        instance.elementtimeout	= setTimeout(function() {
                            $vBar.stop( true, true ).jspmouseenter();
                        }, 100 );

                    }).bind('mouseleave.jsp',function() {

                        // hide the scrollbar after hovertimeout_t
                        clearTimeout( instance.hovertimeout );
                        instance.isScrollbarHover	= false;
                        instance.hovertimeout = setTimeout(function() {
                            // if scrolling at the moment don't hide it
                            if( !instance.isScrolling )
                                $vBar.stop( true, true ).jspmouseleave( instance.extPluginOpts.mouseLeaveFadeSpeed || 0 );
                        }, instance.extPluginOpts.hovertimeout_t );

                    });

                    $vBar.wrap( $vBarWrapper );

                }

            }

        },

    // the jScrollPane instance
        jspapi = $el.data('jsp');

    // extend the jScollPane by merging
    $.extend( true, jspapi, extensionPlugin );
    jspapi.addHoverFunc();

});