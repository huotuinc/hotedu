package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Qa;
import com.huotu.hotedu.service.QaService;
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
 * 常见问题有关的Controller
 * @author shiliting shiliting741@163.com
 */
@Controller
public class QaController {

    /**
     * 常见问题的service层
     */
    @Autowired
    private QaService qaService;

    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE=10;
    /**
     * 用来储存处理静态资源的接口
     */
    @Autowired
    StaticResourceService staticResourceService;



    /**
     * 搜索符合条件的常见问题信息
     * @param keywords  搜索关键字
     * @param model     返回客户端参数集
     * @return      qa.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/searchQa")
    public String searchQa(@RequestParam(required = false)Integer pageNo,
                           @RequestParam(required = false) String keywords, Model model) {
        String turnPage="/backend/qa";
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        Page<Qa> pages = qaService.searchQa(pageNo, PAGE_SIZE, keywords);
        long totalRecords = pages.getTotalElements();
        int numEl =  pages.getNumberOfElements();
        if(numEl==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages = qaService.searchQa(pageNo, PAGE_SIZE, keywords);
            totalRecords = pages.getTotalElements();
        }
        model.addAttribute("allQaList", pages);
        model.addAttribute("totalPages",pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("totalRecords", totalRecords);
        return turnPage;
    }



    /**
     * 删除视频信息
     * @param keywords      检索关键字(使用检索功能后有效)
     * @param id            需要被删除的记录id
     * @param model         返回客户端集合
     * @return      video.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/delQa")
    public String delQa(@RequestParam(required = false)Integer pageNo,@RequestParam(required = false)String keywords, Long id, Model model) {
        String returnPage="redirect:/backend/searchQa";
        qaService.delQa(id);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        return returnPage;
    }

    /**
     * qa.html页面单击新建跳转
     * @return newqa.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/addQa")
    public String addQa(){
        return "/backend/newqa";
    }

    /**
     * qa.html页面点击修改后跳转
     * @param id        需要修改的id
     * @param model     返回客户端集
     * @return      modifyqa.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifyQa")
    public String modifyQa(Long id, Model model) throws Exception{
        Qa qa=qaService.findOneById(id);
        qa.setPictureUri(staticResourceService.getResource(qa.getPictureUri()).toURL().toString());
        model.addAttribute("qa",qa);
        return "/backend/modifyqa";
    }

    /**
     * newqa.html页面点击保存添加后跳转
     * @param title     标题
     * @param content   描述
     * @return      不出异常重定向：/backend/searchQa
     */
    //TODO 是否搞抛出异常
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/addSaveQa")
    public String addSaveQa(String title,String content,Boolean top,@RequestParam("smallimg") MultipartFile file) throws Exception{
        //文件格式判断
        if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
        if(file.getSize()==0){throw new Exception("文件为空！");}

        //保存图片
        String fileName = StaticResourceService.QA_ICON + UUID.randomUUID().toString() + ".png";
        staticResourceService.uploadResource(fileName,file.getInputStream());

        Qa qa=new Qa();
        qa.setPictureUri(fileName);
        qa.setTitle(title);
        qa.setContent(content);
        qa.setLastUploadDate(new Date());
        qa.setTop(top);
        qaService.addQa(qa);
        return "redirect:/backend/searchQa";
    }

    /**
     * modifyqa.html页面点击保存修改后跳转
     * @param id    修改后的id
     * @param title     标题
     * @param content     描述
     * @param top    是否置顶
     * @return      重定向到：/backend/searchQa
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifySaveQa")
    public String modifySaveQa(Long id,String title,String content,Boolean top,@RequestParam("smallimg") MultipartFile file) throws Exception{
        Qa qa=qaService.findOneById(id);
        if(file.getSize()!=0){
            if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
            //获取需要修改的图片路径，并删除
            staticResourceService.deleteResource(staticResourceService.getResource(qaService.findOneById(id).getPictureUri()));
            //保存图片
            String fileName = StaticResourceService.QA_ICON + UUID.randomUUID().toString() + ".png";
            staticResourceService.uploadResource(fileName,file.getInputStream());
            qa.setPictureUri(fileName);
        }
        qa.setTitle(title);
        qa.setContent(content);
        qa.setTop(top);
        qa.setLastUploadDate(new Date());
        qaService.modify(qa);
        return "redirect:/backend/searchQa";
    }

    /**
     * Created by jiashubing on 2015/8/11.
     * 前台加载常见问题
     * @param pageNo    第几页
     * @param model     返回客户端集
     * @return          yun-changjianwt.html
     */
    @RequestMapping("/pc/loadQa")
    public String loadQa(@RequestParam(required = false)Integer pageNo,Model model) throws Exception{
        String turnPage="/pc/yun-changjianwt";
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        Page<Qa> pages = qaService.loadPcQa(pageNo, PAGE_SIZE);
        long totalRecords = pages.getTotalElements();
        int numEl =  pages.getNumberOfElements();
        if(numEl==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages = qaService.loadPcQa(pageNo, PAGE_SIZE);
            totalRecords = pages.getTotalElements();
        }

        for(Qa qa : pages){
            qa.setPictureUri(staticResourceService.getResource(qa.getPictureUri()).toURL().toString());
        }

        Date today = new Date();
        model.addAttribute("flag","yun-index.html");  //此属性用来给前台确定当前是哪个页面
        model.addAttribute("allQaList", pages);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("today", today);
        model.addAttribute("totalRecords", totalRecords);
        return turnPage;
    }

    /**
     * 加载每一条考试指南的详细信息
     * @param id    要查看的考试指南的id
     * @param model 返回客户端集
     * @return      yun-xqwenti.html
     */
    @RequestMapping("/pc/loadDetailQa")
    public String loadDetailQa(Long id,Model model) throws Exception{
        String turnPage="/pc/yun-xqwenti";
        Qa qa = qaService.findOneById(id);
        qa.setPictureUri(staticResourceService.getResource(qa.getPictureUri()).toURL().toString());
        model.addAttribute("qa",qa);
        model.addAttribute("flag","yun-index.html");  //此属性用来给前台确定当前是哪个页面
        return turnPage;
    }

}
