KindEditor.ready(function (K) {
    window.editor = K.create('#content', {
        resizeType: '0',
        uploadJson: 'fileUploadImage',
        items: [
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
                            var imgsrc = e.target.result;
                            img.src = imgsrc;
                            $.ajax({
                                type: "POST",
                                url: "ajaxEditorFileUpload",
                                data: {"imgsrc":imgsrc},
                                success: function (result) {
                                    //console.log("上传成功");
                                    img.src = "backend/"+result.url;
                                    $body.appendChild( img );
                                },
                                error:function() {
                                    alert("图片上传失败，请使用工具栏图片上传工具上传")
                                    //console.log("上传失败");
                                }
                            });
                        };
                        reader.readAsDataURL( blob );
                };
                //获取粘贴事件
                $body.addEventListener( 'paste', function( e ){
                    window.clipboardData = e.clipboardData;
                    var i = 0, items, item, types;
                    if( clipboardData ){
                        items = clipboardData.items;
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
                            //上传图片
                            imgReader( item );
                        }

                    }
                });
            })();
        }
    });
});