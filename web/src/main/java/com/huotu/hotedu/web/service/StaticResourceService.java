package com.huotu.hotedu.web.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 静态资源处理服务
 * <p>静态资源我们认为它拥有几个属性</p>
 * <ul>
 *     <li>URLPrefix 资源定位符前置 比如http://www.baidu.com/</li>
 *     <li>URLPath   资源路径      比如resource/taskimg/abcdefg.png</li>
 *     <li>URL = URLPrefix+URLPath比如http://www.baidu.com/resource/taskimg/abcdefg.png</li>
 *     <li>URLFile   资源实际位置</li>
 * </ul>
 * <p>
 *     服务端需要保证URL可以访问(读权限)URLFile
 *     同时服务端程序具有写入URLFile的权限或者拥有该权限的用户信息
 * </p>
 *
 * @author CJ
 */
public interface StaticResourceService {
    /**
     * 企业的LOGO
     */
    public  String COMPANY_LOGO="/image/company/";
    /**
     * 导师的头像
     */
    public  String TUTOR_ICON="/image/tutor/";
    /**
     * 咨询动态的图片
     */
    public  String MESSAGECONTENT_ICON="/image/messagecontent/";
    /**
     * 会员的头像
     */
    public  String MEMBER_ICON="/image/member/";

    /**
     * 上传资源
     *
     * @param path 资源路径
     * @param data 数据
     * @return 新资源的资源定位符
     * @throws IOException 保存是出错
     * @throws IllegalStateException 如果该资源已存在
     */
    URI uploadResource(String path, InputStream data) throws IOException,IllegalStateException,URISyntaxException;

    /**
     * 获取指定资源的资源定位符
     * @param path
     * @return
     * @throws URISyntaxException
     */
    URI getResource(String path) throws URISyntaxException;

    /**
     * 删除资源
     * @param path
     * @throws IOException
     */
    void deleteResource(String path) throws IOException;

    /**
     * 删除资源
     * @param uri
     * @throws IOException
     */
    void deleteResource(URI uri) throws IOException;

}
