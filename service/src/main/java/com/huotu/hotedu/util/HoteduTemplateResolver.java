package com.huotu.hotedu.util;

import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.exceptions.ConfigurationException;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.resourceresolver.ServletContextResourceResolver;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.templateresolver.TemplateResolver;
import org.thymeleaf.util.Validate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by cwb on 2015/8/24.
 */
public class HoteduTemplateResolver extends TemplateResolver {


    public HoteduTemplateResolver() {
        super();
        super.setResourceResolver(new HoteduResourceResolver());
    }

    @Override
    protected String computeResourceName(TemplateProcessingParameters templateProcessingParameters) {
        checkInitialized();

        final String templateName = templateProcessingParameters.getTemplateName();
        StringBuilder newTemplateName = new StringBuilder();
        //检查访问方式是否为移动端
        SpringWebContext context =  (SpringWebContext)templateProcessingParameters.getContext();
        HttpServletRequest request = context.getHttpServletRequest();
        HttpSession session = request.getSession();
        //检查是否已经记录访问方式（移动端或pc端）
        boolean isFromMobile = false;
        if(null==session.getAttribute("ua")){
            try{
                //获取ua，用来判断是否为移动端访问
                String userAgent = request.getHeader("USER-AGENT").toLowerCase();
                if(null == userAgent){
                    userAgent = "";
                }
                isFromMobile= CheckMobile.check(userAgent);
                //判断是否为移动端访问
                if(isFromMobile){
                    session.setAttribute("ua","mobile");
                } else {
                    session.setAttribute("ua","pc");
                }
            }catch(Exception e){}
        }else{
            isFromMobile=session.getAttribute("ua").equals("mobile");
        }
        if(isFromMobile) {
            newTemplateName.append(templateName).append("_mobile");
        }else {
            newTemplateName.append(templateName);
        }

        Validate.notNull(newTemplateName.toString(), "Template name cannot be null");

        String unaliasedName = newTemplateName.toString();

        final StringBuilder resourceName = new StringBuilder();
        if(!unaliasedName.startsWith("/")) {
            resourceName.append("/");
        }
        resourceName.append(unaliasedName);
        resourceName.append(".html");

        return resourceName.toString();
    }

    /**
     * <p>
     *   This method <b>should not be called</b>, because the resource resolver is
     *   fixed to be {@link ServletContextResourceResolver}. Every execution of this method
     *   will result in an exception.
     * </p>
     * <p>
     *   If you need to select a different resource resolver, use the {@link TemplateResolver}
     *   class instead.
     * </p>
     *
     * @param resourceResolver the new resource resolver
     */
    @Override
    public void setResourceResolver(final IResourceResolver resourceResolver) {
        throw new ConfigurationException(
                "Cannot set a resource resolver on " + this.getClass().getName() + ". If " +
                        "you want to set your own resource resolver, use " + TemplateResolver.class.getName() +
                        "instead");
    }

}
