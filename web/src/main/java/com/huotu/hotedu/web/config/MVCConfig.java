package com.huotu.hotedu.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Configuration
@EnableWebMvc
@ComponentScan({
        "com.huotu.hotedu.web.service",
        "com.huotu.hotedu.web.controller"
})
public class MVCConfig  extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment env;

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


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
        ServletContextTemplateResolver rootTemplateResolver = new ServletContextTemplateResolver();
        rootTemplateResolver.setPrefix("/");
        rootTemplateResolver.setSuffix(".html");
        rootTemplateResolver.setCharacterEncoding("UTF-8");

        // start cache
        if (env.acceptsProfiles("dev")) {
            System.out.println("Develement Mode");
            rootTemplateResolver.setCacheable(false);
        }


        engine.setTemplateResolver(rootTemplateResolver);

        resolver.setTemplateEngine(engine);
        resolver.setOrder(1);
//        resolver.setViewNames(new String[]{"*.html"});
        resolver.setCharacterEncoding("UTF-8");
//        resolver.setPrefix("/WEB-INF/views/");
//        resolver.setSuffix(".jsp");
        return resolver;
    }

}