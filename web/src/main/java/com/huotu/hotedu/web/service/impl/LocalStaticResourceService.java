package com.huotu.hotedu.web.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 本地测试的资源处理器
 * @author CJ
 */
@Service
@Profile("!container")
public class LocalStaticResourceService extends AbstractStaticResourceService{

    private static final Log log = LogFactory.getLog(LocalStaticResourceService.class);

    @Autowired
    public void setWebApplicationContext(WebApplicationContext context){
        File file = new File(context.getServletContext().getRealPath("/uploadResources/"));
        this.fileHome = file.toURI();
        String url=System.getProperty("user.dir");
        StringBuilder stringBuilder = new StringBuilder("http://localhost:8080");
        stringBuilder.append(context.getServletContext().getContextPath())
        .append("/uploadResources/");
        try {
            this.uriPrefix = new URI(stringBuilder.toString());
        } catch (URISyntaxException e) {
            log.error("解析失败",e);
            throw new InternalError("解析"+stringBuilder.toString()+"失败");
        }
    }



}
