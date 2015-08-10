package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.service.ExamGuideService;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.util.Date;
import java.util.UUID;

/**
 * Created by shiliting on 2015/6/10.
 * 考试指南的Controller
 *
 * @author shiliting741@163.com
 */
@Controller
public class ExamGuideController {
    /**
     * 考试指南的service层
     */
    @Autowired
    private ExamGuideService examGuideService;
    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE = 10;
    /**
     * 用来储存处理静态资源的接口
     */
    @Autowired
    StaticResourceService staticResourceService;


    /**
     * 搜索符合条件的考试指南信息
     * @param pageNo       当前显示的页数
     * @param keywords     关键字
     * @param model        返回的参数
     * @return             examguide.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/searchExamGuide")
    public String searchExamGuide(@RequestParam(required = false)Integer pageNo,
                                  @RequestParam(required = false) String keywords, Model model) {
        String turnPage="/backend/examguide";
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        Page<ExamGuide> pages = examGuideService.searchExamGuide(pageNo, PAGE_SIZE, keywords);
        long totalRecords = pages.getTotalElements();
        int numEl =  pages.getNumberOfElements();
        if(numEl==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages = examGuideService.searchExamGuide(pageNo, PAGE_SIZE, keywords);
            totalRecords = pages.getTotalElements();
        }
        model.addAttribute("allGuideList", pages);
        model.addAttribute("totalPages",pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("totalRecords", totalRecords);
        return turnPage;
    }

    /**
     * 删除考试指南信息以及查询
     *
     * @param pageNo     显示第几页
     * @param keywords   检索关键字(使用检索功能后有效)
     * @param id         需要被删除的记录id
     * @param model      返回客户端集合
     * @return examguide.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/delExamGuide")
    public String delExamGuide(@RequestParam(required = false)Integer pageNo,@RequestParam(required = false)String keywords, Long id, Model model) {
        String returnPage="redirect:/backend/searchExamGuide";
        examGuideService.delExamGuide(id);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        return returnPage;
    }

    /**
     * examguide.html页面单击新建跳转
     *
     * @return newguide.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/addExamGuide")
    public String addExamGuide() {
        return "/backend/newguide";
    }

    /**
     * examguide.html页面点击修改后跳转
     *
     * @param id    需要修改的id
     * @param model 返回客户端集
     * @return modifyguide.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifyExamGuide")
    public String modifyExamGuide(Long id, Model model) throws Exception {
        ExamGuide examGuide = examGuideService.findOneById(id);
        examGuide.setPictureUri(staticResourceService.getResource(examGuide.getPictureUri()).toURL().toString());
        model.addAttribute("examGuide", examGuide);
        return "/backend/modifyguide";
    }


    /**
     * newguide.html页面点击保存添加后跳转
     *
     * @param title   标题
     * @param content 描述
     * @param top     是否置顶
     * @return 不出异常重定向：/backend/searchExamGuide
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/addSaveExamGuide")
    public String addSaveExamGuide(String title, String content, Boolean top ,@RequestParam("smallimg") MultipartFile file) throws Exception{
        //文件格式判断
        if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
        if(file.getSize()==0){throw new Exception("文件为空！");}

        //保存图片
        String fileName = StaticResourceService.EXAMGUIDE_ICON + UUID.randomUUID().toString() + ".png";
        staticResourceService.uploadResource(fileName,file.getInputStream());

        ExamGuide examGuide = new ExamGuide();
        examGuide.setPictureUri(fileName);
        examGuide.setTitle(title);
        examGuide.setContent(content);
        examGuide.setLastUploadDate(new Date());
        examGuide.setTop(top);
        examGuideService.addExamGuide(examGuide);
        return "redirect:/backend/searchExamGuide";
    }

    /**
     * modifyguide.html页面点击保存修改后跳转
     *
     * @param id      修改后的id
     * @param title   标题
     * @param content 描述
     * @param top     是否置顶
     * @return 重定向到：/backend/searchExamGuide
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifySaveExamGuide")
    public String modifySaveExamGuide(Long id, String title, String content, Boolean top ,@RequestParam("smallimg") MultipartFile file) throws Exception{
        ExamGuide examGuide = examGuideService.findOneById(id);
        if(file.getSize()!=0){
            if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
            //获取需要修改的图片路径，并删除
            staticResourceService.deleteResource(staticResourceService.getResource(examGuideService.findOneById(id).getPictureUri()));
            //保存图片
            String fileName = StaticResourceService.EXAMGUIDE_ICON + UUID.randomUUID().toString() + ".png";
            staticResourceService.uploadResource(fileName,file.getInputStream());
            examGuide.setPictureUri(fileName);
        }
        examGuide.setTitle(title);
        examGuide.setContent(content);
        examGuide.setTop(top);
        examGuide.setLastUploadDate(new Date());
        examGuideService.modify(examGuide);
        return "redirect:/backend/searchExamGuide";
    }

    /**
     * Created by shiliting on 2015/8/7.
     * 前台加载考试指南
     * @param pageNo    第几页
     * @param model     返回客户端集
     * @return          yun-kaoshizn.html
     */
    @RequestMapping("/pc/loadExamGuide")
    public String loadExamGuide(@RequestParam(required = false)Integer pageNo,Model model) throws Exception{
        String turnPage="/pc/yun-kaoshizn";
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        Page<ExamGuide> pages = examGuideService.loadExamGuide(pageNo, PAGE_SIZE);
        long totalRecords = pages.getTotalElements();
        int numEl =  pages.getNumberOfElements();
        if(numEl==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages = examGuideService.loadExamGuide(pageNo, PAGE_SIZE);
            totalRecords = pages.getTotalElements();
        }

        for(ExamGuide examGuide : pages){
            examGuide.setPictureUri(staticResourceService.getResource(examGuide.getPictureUri()).toURL().toString());
        }

        Date today = new Date();
        model.addAttribute("allExamGuideList", pages);
        model.addAttribute("totalPages",pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("today", today);
        model.addAttribute("totalRecords", totalRecords);
        return turnPage;
    }
}
