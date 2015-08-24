package com.huotu.hotedu.util;

import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.IWebContext;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.util.Validate;

import javax.servlet.ServletContext;
import java.io.InputStream;

/**
 * Created by cwb on 2015/8/24.
 */
public class HoteduResourceResolver implements IResourceResolver {
    public static final String NAME = "SERVLETCONTEXT";

    public HoteduResourceResolver() {
        super();
    }

    public String getName() {
        return NAME;
    }

    public InputStream getResourceAsStream(TemplateProcessingParameters templateProcessingParameters, String resourceName) {

        Validate.notNull(templateProcessingParameters, "Template Processing Parameters cannot be null");
        Validate.notNull(resourceName, "Resource name cannot be null");

        final IContext context = templateProcessingParameters.getContext();
        if (!(context instanceof IWebContext)) {
            throw new TemplateProcessingException(
                    "Resource resolution by ServletContext with " +
                            this.getClass().getName() + " can only be performed " +
                            "when context implements " + IWebContext.class.getName() +
                            " [current context: " + context.getClass().getName() + "]");
        }

        final ServletContext servletContext =
                ((IWebContext)context).getServletContext();
        if (servletContext == null) {
            throw new TemplateProcessingException("Thymeleaf context returned a null ServletContext");
        }
        InputStream inputStream;
        if(resourceName.endsWith("_mobile.html")) {
            inputStream = servletContext.getResourceAsStream(resourceName);
            String newResourceName = "";
            if(inputStream==null) {
                newResourceName = resourceName.substring(0,resourceName.indexOf("_"))+".html";
                inputStream = servletContext.getResourceAsStream(newResourceName);
            }
        }else {
            inputStream = servletContext.getResourceAsStream(resourceName);
        }
        return inputStream;
    }
}
