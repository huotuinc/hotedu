KindEditor.ready(function (K) {
    window.editor = K.create('#content', {
        resizeType: '0',
        uploadJson: 'fileUploadImage'
        , items: [
            'source', 'undo', 'redo', '|', 'preview', 'cut', 'copy', 'paste',
            'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
            'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
            'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
            'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
            'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|','image',
            'table', 'hr',  'pagebreak',
            'anchor', 'link', 'unlink'
        ]
        , afterCreate : function() {
            //获取富文本框body
            var $body = $('.ke-edit-iframe').contents().find('body')[0];
            (function(){
                //读取图片方法
                var imgReader = function( item ){
                    var blob = item.getAsFile(),
                        reader = new FileReader();
                        reader.onload = function( e ){
                            var img = new Image();
                            img.src = e.target.result;
                            deal(img);
                            $body.appendChild( img );
                        };
                        reader.readAsDataURL( blob );
                };
                //获取粘贴事件
                $body.addEventListener( 'paste', function( e ){
                    console.log(e);
                    window.clipboardData = e.clipboardData;
                    var i = 0, items, item, types;
                    if( clipboardData ){
                        items = clipboardData.items;
                        console.log(items)
                        if( !items ){
                            return;
                        }

                        item = items[0];
                        types = clipboardData.types || [];
                        for( ; i < types.length; i++ ){
                            if( types[i] === 'Files' ){
                                item = items[i];
                                break;
                            }
                        }
                        if( item && item.kind === 'file' && item.type.match(/^image\//i) ){
                            imgReader( item );
                        }

                    }
                });
            })();
        }
    });
    //上传图片的ajax
    function deal(img) {
        var src = img.src;
            $.ajax({
                type: "POST",
                url: "ajaxEditorFileUpload",
                data: {"imgsrc":src},
                success: function (org) {
                    //console.log("上传成功");
                },
                error:function() {
                    //console.log("上传失败");
                }
            });
      }
});