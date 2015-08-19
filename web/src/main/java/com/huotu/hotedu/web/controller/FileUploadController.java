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

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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


}
