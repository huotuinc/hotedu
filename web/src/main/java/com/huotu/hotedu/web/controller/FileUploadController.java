package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Certificate;
import com.huotu.hotedu.entity.Login;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.entity.Result;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

    @RequestMapping(value="/backend/fileUploadImage",method = RequestMethod.POST)
    @ResponseBody
    public Result fileUploadUeImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Result result=new Result();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        MultipartFile file=fileMap.get("imgFile");
        response.setHeader("X-frame-Options","SAMEORIGIN");
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
        //将String转换成inputStream流
        InputStream in_nocode  =  new ByteArrayInputStream(imgsrc.getBytes());
        String fileName = StaticResourceService.RICHTEXT_IMG + UUID.randomUUID().toString() + ".png";
        //上传至服务器
        URI uri =staticResourceService.uploadResource(fileName, in_nocode);
        staticResourceService.deleteResource(uri);
        result.setStatus(0);
        return result;
    }



}
