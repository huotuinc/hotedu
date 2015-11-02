package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Result;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

/**
 * Created by shiliting on 2015/8/18.
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class FileUploadController {
    @Autowired
    StaticResourceService staticResourceService;

    static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

        @RequestMapping(value="/backend/fileUploadImage",method = RequestMethod.POST)
    @ResponseBody
    public Result fileUploadUeImage(HttpServletRequest request) throws Exception {
        Result result=new Result();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        MultipartFile file=fileMap.get("imgFile");
        if(ImageIO.read(file.getInputStream())==null||file.getSize()==0){
            result.setError(1);
            result.setMessage("图片格式错误！");
            return result;
        }
        String fileName = StaticResourceService.RICHTEXT_IMG + UUID.randomUUID().toString() + ".png";
        URI uri =staticResourceService.uploadResource(fileName, file.getInputStream());
        result.setError(0);
        result.setUrl(uri.toString());
        return result;
    }

    /**
     * Created by jiashubing on 2015/9/9.
     * ctrl+v富文本粘贴图片的ajax上传
     * @param imgsrc    图片网络地址
     * @throws Exception
     */
    @RequestMapping("/backend/ajaxEditorFileUpload")
    @ResponseBody
    public Result ajaxEditorFileUpload(String imgsrc) throws Exception {
        Result result = new Result();
        //去掉字符串前面多余的字符"data:image/png;base64,"，获得纯粹的二进制地址
        imgsrc = imgsrc.substring(22);
            try {
                //将String转换成InputStream流
                byte[] bytes1 = decoder.decodeBuffer(imgsrc);
                ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);

                //上传至服务器
                String fileName = StaticResourceService.RICHTEXT_IMG + UUID.randomUUID().toString() + ".png";
                URI uri =staticResourceService.uploadResource(fileName, bais);

                result.setStatus(0);
                result.setMessage(fileName);
                result.setUrl(uri.toString());
            } catch (IOException e) {
                e.printStackTrace();
        }
        return result;
    }

}
