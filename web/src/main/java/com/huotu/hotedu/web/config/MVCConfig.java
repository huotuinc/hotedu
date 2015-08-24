package com.huotu.hotedu.web.config;

import com.huotu.hotedu.util.HoteduTemplateResolver;
import com.huotu.iqiyi.sdk.springboot.IqiyiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.mobile.device.DeviceHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import java.util.List;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Configuration
@EnableWebMvc
@Import(IqiyiConfig.class)  // add by CJ for IqiyiVideoRepository
@ComponentScan({
        "com.huotu.hotedu.web.service",
        "com.huotu.hotedu.web.advice",
        "com.huotu.hotedu.web.controller"
})
public class MVCConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment env;

    /**
     * for upload
     */
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    @Bean
    public DeviceHandlerMethodArgumentResolver deviceHandlerMethodArgumentResolver() {
        return new DeviceHandlerMethodArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(deviceHandlerMethodArgumentResolver());
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    //错误处理
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        super.configureHandlerExceptionResolvers(exceptionResolvers);

    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        super.addResourceHandlers(registry);
//        registry.addResourceHandler("/js/**").addResourceLocations("/backend/js/");
//        registry.addResourceHandler("/css/**").addResourceLocations("/backend/css/");
//        registry.addResourceHandler("/font/**").addResourceLocations("/backend/font/");
//        registry.addResourceHandler("/images/**").addResourceLocations("/backend/images/");
//
//    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        super.configureViewResolvers(registry);
        registry.viewResolver(viewResolver());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setStatusCode()
//        registry.addViewController("/").setViewName("index");
    }

    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.addDialect(new SpringSecurityDialect());
        engine.addDialect(new Java8TimeDialect());
        HoteduTemplateResolver templateResolver = new HoteduTemplateResolver();
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        /*if (env.acceptsProfiles("test")) {
            System.out.println("Develement Mode");
            templateResolver.setCacheable(false);
        }*/
        engine.setTemplateResolver(templateResolver);
        /*ServletContextTemplateResolver rootTemplateResolver = new ServletContextTemplateResolver();
        rootTemplateResolver.setPrefix("/");
        rootTemplateResolver.setSuffix(".html");
        rootTemplateResolver.setCharacterEncoding("UTF-8");


        // start cache
        if (env.acceptsProfiles("dev")) {
            System.out.println("Develement Mode");
            rootTemplateResolver.setCacheable(false);
        }

        engine.setTemplateResolver(rootTemplateResolver);*/

        resolver.setTemplateEngine(engine);
        resolver.setOrder(1);
//        resolver.setViewNames(new String[]{"*.html"});
        resolver.setCharacterEncoding("UTF-8");


        //在初始化Thymeleaf的时候 应该增加它的方言,spring添加方言
//        engine.addDialect(new SpringSecurityDialect());
//        engine.addDialect(new org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect());
//      resolver.setPrefix("/WEB-INF/views/");
//      resolver.setSuffix(".jsp");
        return resolver;
    }

}