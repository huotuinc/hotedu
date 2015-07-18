package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Wanted;
import com.huotu.hotedu.repository.WantedRepository;
import com.huotu.hotedu.service.WantedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 招聘信息有关的Controller
 */
@Controller
public class WantedController {
    /**
     * 招聘信息的service层
     */
    @Autowired
    private WantedService wantedService;

    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE=10;

    /**
     * 显示招聘信息
     * @return  wanted.html
     */
    @RequestMapping("/backend/loadWanted")
    public String loadWantedController(Model model) {
        Page<Wanted> pages=wantedService.loadWanted(0, PAGE_SIZE);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allWantedList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords","");
        model.addAttribute("dateStart","");
        model.addAttribute("dateEnd","");
        model.addAttribute("searchSort","all");
        model.addAttribute("sumElement",sumElement);
        return "/backend/wanted";
    }

    /**
     * 查看招聘信息
     * @return  information.html
     */
    @RequestMapping("/backend/viewWanted")
    public String viewWantedController() {return "/backend/information";}

    /**
     * 搜索符合条件的招聘信息
     * @return  wanted.html
     */
    @RequestMapping("/backend/searchWanted")
    public String searchWantedController() {
        return "";
    }

    /**
     * 分页显示
     * @param n         显示第几页
     * @param sumpage       分页总页数
     * @param searchSort    检索类型
     * @param keywords      检索关键字(使用检索功能后有效)
     * @param dateStart     检索起始日期
     * @param dateEnd       检索结束日期
     * @param model         返回客户端集
     * @return          wanted.html
     * @throws Exception
     */
    @RequestMapping("backend/pageWanted")
    public String pageWantedController(int n,int sumpage,String searchSort,String keywords,String dateStart,String dateEnd,Model model) throws Exception{
        if (n < 0){                     //如果已经到分页的第一页了，将页数设置为0
            n++;
        }else if(n + 1 > sumpage){      //如果超过分页的最后一页了，将页数设置为最后一页
            n--;
        }
        Page<Wanted> pages = wantedService.searchWanted(n,PAGE_SIZE,keywords);
        if("date".equals(searchSort)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            try {
                Date DStart=sdf.parse(dateStart);
                Date DEnd=sdf.parse(dateEnd);
                pages=wantedService.searchWantedDate(n, PAGE_SIZE, DStart, DEnd);
            } catch (ParseException e) {
                e.printStackTrace();
                //日期格式不正确
            }
        }else if("all".equals(searchSort)){
            pages=wantedService.searchWantedAll(n, PAGE_SIZE, keywords);

        }else{
            pages=wantedService.searchWantedType(n, PAGE_SIZE, keywords, searchSort);
        }
        if(pages==null){
            throw new Exception("没有数据！");
        }
        model.addAttribute("allWantedList",pages);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("searchSort",searchSort);
        model.addAttribute("dateStart",dateStart);
        model.addAttribute("dateEnd",dateEnd);
        model.addAttribute("sumElement",pages.getTotalElements());
        return "/backend/enterprises";
    }

}
