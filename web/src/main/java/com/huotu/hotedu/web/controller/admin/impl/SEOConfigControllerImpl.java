package com.huotu.hotedu.web.controller.admin.impl;

import com.huotu.hotedu.entity.SEOConfig;
import com.huotu.hotedu.repository.SEOConfigRepository;
import com.huotu.hotedu.web.controller.admin.SEOConfigController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Administrator on 2015/11/12.
 */
@Controller
public class SEOConfigControllerImpl implements SEOConfigController {

    @Autowired
    SEOConfigRepository seoConfigRepository;

    @Override
    public ModelAndView showSEOConfigList() throws Exception {
        modelAndView.setViewName("backend/seo");
        List<SEOConfig> configs = seoConfigRepository.findAll();
        modelAndView.addObject("seoList",configs);
        return modelAndView;
    }

    @Override
    public ModelAndView showSEOConfigDetail(@PathVariable(value = "seoId") long seoId) throws Exception{
        modelAndView.setViewName("backend/seoEditPage");
        SEOConfig seoConfig = seoConfigRepository.findOne(seoId);
        modelAndView.addObject("seo",seoConfig);
        return modelAndView;
    }

    @Override
    @Transactional
    public ModelAndView modifySEOConfig(@PathVariable(value = "seoId") long seoId, SEOConfig seoConfig) throws Exception {
        SEOConfig seo = seoConfigRepository.findOne(seoId);
        seo.setTitle(seoConfig.getTitle());
        seo.setKeywords(seoConfig.getKeywords());
        seo.setDescription(seoConfig.getDescription());
        seoConfigRepository.save(seo);
        modelAndView.setViewName("redirect:/backend/seos");
        return modelAndView;
    }
}
