package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Tutor;
import com.huotu.hotedu.service.TutorService;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by shiliting on 2015/6/10.
 * 师资力量有关的Controller
 * @Time 2015/6/10
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class TutorController {
    /**
     * 师资力量的Service层
     */
    @Autowired
    TutorService tutorService;

    /**
     * 用来储存处理静态资源的接口
     */
    @Autowired
    StaticResourceService staticResourceService;

    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE=10;


    /**
     *
     * @param searchSort 客户端返回的检索类型
     * @param keywords   从客户端返回的检索关键字
     * @param dateStart  从客户端返回的检索起始时间(以时间检索的时候有效)
     * @param dateEnd    从客户端返回的检索结束时间(以时间检索的时候有效)
     * @param model      准备向客户端发送的参数集合
     * @return           tutor.html
     * @throws Exception
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/searchTutor")
    public String searchTutor(@RequestParam(required = false,value = "searchSort")String searchSort,
                              @RequestParam(required = false)Integer pageNo,
                              @RequestParam(required = false) String keywords,
                              @DateTimeFormat(pattern = "yyyy.MM.dd")Date dateStart,
                              @DateTimeFormat(pattern = "yyyy.MM.dd")Date dateEnd,Model model) throws Exception{
        String turnPage="/backend/tutor";
        DateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        Page<Tutor> pages = tutorService.searchTutorType(pageNo, PAGE_SIZE, dateStart, dateEnd, keywords, searchSort);
        long totalRecords = pages.getTotalElements();
        int numEl =  pages.getNumberOfElements();
        if(numEl==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages = tutorService.searchTutorType(pageNo, PAGE_SIZE, dateStart, dateEnd, keywords, searchSort);
            totalRecords = pages.getTotalElements();
        }

        model.addAttribute("allTutorList", pages);
        model.addAttribute("totalPages",pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("dateStart",dateStart==null?"":format1.format(dateStart));
        model.addAttribute("dateEnd",dateEnd==null?"":format1.format(dateEnd));
        model.addAttribute("searchSort",searchSort);
        return turnPage;
    }


    /**
     * 师资力量删除请求
     * @param pageNo             显示第几页
     * @param searchSort    检索类型(使用检索功能后有效)
     * @param keywords      检索关键字(使用检索功能后有效)
     * @param dateStart     检索起始时间(使用检索功能后有效)
     * @param dateEnd       检索结束时间(使用检索功能后有效)
     * @param id            需要被删除的记录id
     * @param model         准备向客户端发送的参数集合
     * @return              tutor.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/delTutor")
    public String delTutor(
                           @RequestParam(required = false)Integer pageNo,
                           @RequestParam(required = false) String keywords,
                           @RequestParam("searchSort")String searchSort,
                           @DateTimeFormat(pattern = "yyyy.MM.dd")Date dateStart,
                           @DateTimeFormat(pattern = "yyyy.MM.dd")Date dateEnd,
                           Long id,Model model) throws IOException{
        DateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
        String returnPage="redirect:/backend/searchTutor";
        staticResourceService.deleteResource(tutorService.findOneById(id).getPictureUri());
        tutorService.delTutor(id);//删除记录
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("dateStart",dateStart==null?"":format1.format(dateStart));
        model.addAttribute("dateEnd",dateEnd==null?"":format1.format(dateEnd));
        model.addAttribute("searchSort",searchSort);
        return returnPage;
    }

    /**
     * 师资力量新建
     * @return newtutor.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/addTutor")
    public String addTutor(){
        return "/backend/newtutor";
    }

    /**
     * tutor.html页面 点击修改 跳转
     * @param id        需要修改记录的id
     * @param model     准备向客户端发送的参数集合
     * @param request   网页请求
     * @return          modifytutor.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifyTutor")
    public String modifyTutor(Long id, Model model,HttpServletRequest request) throws Exception {
        Tutor tutor=tutorService.findOneById(id);
        //返回
//        tutor.setPictureUri(request.getContextPath()+tutor.getPictureUri());
        tutor.setPictureUri(staticResourceService.getResource(tutor.getPictureUri()).toURL().toString());
        model.addAttribute("tutor",tutor);
        return "/backend/modifytutor";
    }

    /**
     * 在newtutor.html页面接收保存添加师资力量的请求
     * @param name              教师名字
     * @param introduction      描述
     * @param qualification     职称
     * @param area              地区
     * @param file              照片
     * @return                  不出异常重定向：/backend/loadTutor 抛出异常重定向：/backend/error
     * @throws Exception
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping(value = "/backend/addSaveTutor",method = RequestMethod.POST)
    public String addSaveTutor(String name,String introduction,String qualification,String area,@RequestParam("smallimg") MultipartFile file) throws Exception{
            //文件格式判断
            if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
            if(file.getSize()==0){throw new Exception("文件为空！");}

            //保存图片
            String fileName = StaticResourceService.TUTOR_ICON + UUID.randomUUID().toString() + ".png";
            staticResourceService.uploadResource(fileName,file.getInputStream());

            Tutor tutor=new Tutor();
            tutor.setPictureUri(fileName);
            tutor.setQualification(qualification);
            tutor.setArea(area);
            tutor.setIntroduction(introduction);
            tutor.setName(name);
            tutor.setLastUploadDate(new Date());
            tutorService.addTutor(tutor);

            return "redirect:/backend/searchTutor";
    }

    /**
     * 在modifytutor.html页面接收保存修改师资力量的请求
     * @param id                教师id
     * @param name              教师名字
     * @param introduction      描述
     * @param qualification     职称
     * @param area              地区
     * @param file              照片
     * @return                  重定向到：/backend/loadTutor
     * @throws Exception
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifySaveTutor")
    public String ModifySaveTutor(Long id,String name,String introduction,String qualification,String area,@RequestParam("smallimg") MultipartFile file) throws Exception{
        Tutor tutor=tutorService.findOneById(id);
        if(file.getSize()!=0){
            if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
            //获取需要修改的图片路径，并删除
            staticResourceService.deleteResource(staticResourceService.getResource(tutorService.findOneById(id).getPictureUri()));
            //保存图片
            String fileName = StaticResourceService.TUTOR_ICON + UUID.randomUUID().toString() + ".png";
            staticResourceService.uploadResource(fileName,file.getInputStream());
            tutor.setPictureUri(fileName);

        }
        tutor.setQualification(qualification);
        tutor.setArea(area);
        tutor.setIntroduction(introduction);
        tutor.setName(name);
        tutor.setLastUploadDate(new Date());
        tutorService.modify(tutor);
        return "redirect:/backend/searchTutor";
    }

    @RequestMapping("/pc/loadTutors")
    public String loadTutors(Model model) {
        model.addAttribute("flag","yun-shizi.html");  //此属性用来给前台确定当前是哪个页面
        String turnPage = "pc/yun-shizi";
        return turnPage;
    }

}
