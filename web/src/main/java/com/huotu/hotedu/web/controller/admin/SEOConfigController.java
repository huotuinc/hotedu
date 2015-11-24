package com.huotu.hotedu.web.controller.admin;

import com.huotu.hotedu.entity.SEOConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Administrator on 2015/11/12.
 */
@Controller
public interface SEOConfigController {

     ModelAndView modelAndView = new ModelAndView();

    /**
     * 后台显示seo配置列表，目前有且只有一个
     * @return
     *      view:"backend/seo"
     *      model:"seoList":List<SEOConfig>
     * @throws Exception
     */
    @RequestMapping(value = "/backend/seos",method = RequestMethod.GET)
    ModelAndView showSEOConfigList() throws Exception;


    /**
     * 根据Id加载seo配置修改页
     * @param seoId
     * @return
     *      view:"backend/seoEditPage"
     *      model:"seo":SEOConfig
     * @throws Exception
     */
    @RequestMapping(value = "/backend/seos/{seoId}",method = RequestMethod.GET)
    ModelAndView showSEOConfigDetail(@PathVariable(value = "seoId") long seoId) throws Exception;

    /**
     * 修改seo配置
     * @RequestMapping(value = "/backend/seos/{seoId}",method = RequestMethod.POST)
     * @param seoId
     * @param seoConfig
     * @return
     *      view:"redirect:/backend/seos"
     *
     * @throws Exception
     */
    @RequestMapping(value = "/backend/seos/{seoId}",method = RequestMethod.POST)
    ModelAndView modifySEOConfig(@PathVariable(value = "seoId") long seoId,SEOConfig seoConfig) throws Exception;
}
