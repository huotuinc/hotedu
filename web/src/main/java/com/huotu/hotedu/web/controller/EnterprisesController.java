package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Enterprise;
import com.huotu.hotedu.service.EnterpriseService;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shiliting on 2015/6/10.
 * 企业有关的Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class EnterprisesController {
    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    StaticResourceService staticResourceService;
    public static final int PAGE_SIZE=10;//每张页面的记录数

    //后台显示所有招聘信息
    @RequestMapping("/backend/loadWantedes")
    public String loadWantedesController() {
        return "/backend/wantedes";
    }


    //后台显示所有发布企业的信息
    @RequestMapping("/backend/loadEnterprises")
    public String loadEnterprisesController(Model model) {
        Page<Enterprise> pages=enterpriseService.loadEnterprise(0, PAGE_SIZE);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allEnterprisesList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords","");
        model.addAttribute("dateStart","");
        model.addAttribute("dateEnd","");
        model.addAttribute("searchSort","all");
        model.addAttribute("sumElement",sumElement);
        return "/backend/enterprises";
    }


    //后台单机搜索按钮显示的发布企业消息
    @RequestMapping("/backend/searchEnterprises")
    public String searchEnterprises(String searchSort,String keywords,String dateStart,String dateEnd,Model model) throws Exception{
        Page<Enterprise> pages=null;
        if("date".equals(searchSort)){
            if("".equals(dateStart)||"".equals(dateEnd)){
                return "redirect:/backend/loadTutor";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            try {
                Date DStart=sdf.parse(dateStart);
                Date DEnd=sdf.parse(dateEnd);
                pages=enterpriseService.searchEnterpriseDate(0, PAGE_SIZE, DStart, DEnd);
            } catch (ParseException e) {
                e.printStackTrace();
                //日期格式不正确
                throw new Exception("日期格式错误！");
            }
        }else if("all".equals(searchSort)){
            pages=enterpriseService.searchEnterpriseAll(0, PAGE_SIZE, keywords);

        }else{
            pages=enterpriseService.searchEnterpriseType(0, PAGE_SIZE, keywords, searchSort);

        }
        if(pages==null){
            throw new Exception("没有数据！");
        }
        long sumElement=pages.getTotalElements();
        model.addAttribute("allEnterpriseList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords",keywords);
        model.addAttribute("dateStart",dateStart);
        model.addAttribute("dateEnd",dateEnd);
        model.addAttribute("searchSort",searchSort);
        model.addAttribute("sumElement",sumElement);
        return "/backend/enterprises";
    }

}
