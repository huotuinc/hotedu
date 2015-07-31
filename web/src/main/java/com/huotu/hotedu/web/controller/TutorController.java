package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Tutor;
import com.huotu.hotedu.service.TutorService;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
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
     * 接收显示多条师资力量的请求，加载tutor页面
     * 根据分页控制显示企业信息的条数，
     * 请求完成之后model层会添加
     * 1.多条师资力量信息的集合
     * 2.分页的总页数
     * 3.默认检索时使用的关键字(默认为空)
     * 4.默认显示第几页(默认为0：第一页)
     * 5.默认起始时间(以时间检索的时候有效)
     * 6.默认结束时间(以时间检索的时候有效)
     * 7.默认的检索类型
     * 8.企业的总记录数
     * @param model 准备向客户端发送的参数集合
     * @return tutor.html
     */
    @RequestMapping("/backend/loadTutor")
    public String loadTutor(Model model){
        Page<Tutor> pages=tutorService.loadTutor(0, PAGE_SIZE);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allTutorList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords","");
        model.addAttribute("dateStart","");
        model.addAttribute("dateEnd","");
        model.addAttribute("searchSort","all");
        model.addAttribute("sumElement", sumElement);
        return "/backend/tutor";
    }

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
    @RequestMapping("/backend/searchTutor")
    public String searchTutor(@RequestParam("searchSort")String searchSort,String keywords,
                              @DateTimeFormat(pattern = "yyyy.MM.dd")Date dateStart,@DateTimeFormat(pattern = "yyyy.MM.dd")Date dateEnd,Model model) throws Exception{
        Page<Tutor> pages=null;
        DateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
        if("date".equals(searchSort)){
            pages=tutorService.searchTutorDate(0,PAGE_SIZE,dateStart,dateEnd);
        }else if("all".equals(searchSort)){
            pages=tutorService.searchTutorAll(0,PAGE_SIZE,keywords);
        }else{
            pages=tutorService.searchTutorType(0,PAGE_SIZE,keywords,searchSort);

        }
        if(pages==null){
            throw new Exception("没有数据！");
        }
        long sumElement=pages.getTotalElements();
        model.addAttribute("allTutorList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+1);
        model.addAttribute("n",0);
        model.addAttribute("keywords",keywords);

        model.addAttribute("dateStart",dateStart==null?"":format1.format(dateStart));
        model.addAttribute("dateEnd",dateEnd==null?"":format1.format(dateEnd));
        model.addAttribute("searchSort",searchSort);
        model.addAttribute("sumElement",sumElement);
        return "/backend/tutor";
    }

    /**
     * 师资力量接收一个分页请求，返回一个分页之后的HTML页面
     * @param n             显示第几页
     * @param sumpage       分页总页数
     * @param searchSort    检索类型(使用检索功能后有效)
     * @param keywords      检索关键字(使用检索功能后有效)
     * @param dateStart     检索起始时间(使用检索功能后有效)
     * @param dateEnd       检索结束时间(使用检索功能后有效)
     * @param model         准备向客户端发送的参数集合
     * @return              tutor.html
     * @throws Exception
     */
    @RequestMapping("/backend/pageTutor")
    public String pageTutor(int n,int sumpage,@RequestParam("searchSort")String searchSort,String keywords,@DateTimeFormat(pattern = "yyyy.MM.dd")Date dateStart,@DateTimeFormat(pattern = "yyyy.MM.dd")Date dateEnd,Model model) throws Exception{
        //如果已经到分页的第一页了，将页数设置为0
        if (n < 0){
            n++;
        }else if(n + 1 > sumpage){//如果超过分页的最后一页了，将页数设置为最后一页
            n--;
        }
        DateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
        Page<Tutor> pages=null;
        if("date".equals(searchSort)){
            pages=tutorService.searchTutorDate(n,PAGE_SIZE,dateStart,dateEnd);

        }else if("all".equals(searchSort)){
            pages=tutorService.searchTutorAll(n,PAGE_SIZE,keywords);
        }else{
            pages=tutorService.searchTutorType(n,PAGE_SIZE,keywords,searchSort);
        }
        if(pages==null){
            throw new Exception("没有数据！");
        }
        model.addAttribute("allTutorList",pages);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("searchSort",searchSort);
        model.addAttribute("dateStart",dateStart==null?"":format1.format(dateStart));
        model.addAttribute("dateEnd",dateEnd==null?"":format1.format(dateEnd));
        model.addAttribute("sumElement",pages.getTotalElements());
        return "/backend/tutor";
    }

    /**
     * 师资力量删除请求
     * @param n             显示第几页
     * @param sumpage       分页总页数
     * @param searchSort    检索类型(使用检索功能后有效)
     * @param keywords      检索关键字(使用检索功能后有效)
     * @param dateStart     检索起始时间(使用检索功能后有效)
     * @param dateEnd       检索结束时间(使用检索功能后有效)
     * @param id            需要被删除的记录id
     * @param sumElement    总记录数
     * @param model         准备向客户端发送的参数集合
     * @return              tutor.html
     */
    @RequestMapping("/backend/delTutor")
    public String delTutor(int n,int sumpage,@RequestParam("searchSort")String searchSort,String keywords,@DateTimeFormat(pattern = "yyyy.MM.dd")Date dateStart,@DateTimeFormat(pattern = "yyyy.MM.dd")Date dateEnd,Long id,Long sumElement,Model model){
        try {
            staticResourceService.deleteResource(tutorService.findOneById(id).getPictureUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        tutorService.delTutor(id);//删除记录
        //如果删除一条记录后总记录数为10的倍数，则修改总页数
        if((sumElement-1)%PAGE_SIZE==0){
            if(n>0&&n+1==sumpage){n--;}
            sumpage--;
        }
        sumElement--;

        Page<Tutor> pages=null;
        DateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");

        if("date".equals(searchSort)){
                pages=tutorService.searchTutorDate(n,PAGE_SIZE,dateStart,dateEnd);
        }else if("all".equals(searchSort)){
            pages=tutorService.searchTutorAll(n,PAGE_SIZE,keywords);

        }else{
            pages=tutorService.searchTutorType(n,PAGE_SIZE,keywords,searchSort);

        }
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("allTutorList",pages);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("searchSort",searchSort);
        model.addAttribute("dateStart",dateStart==null?"":format1.format(dateStart));
        model.addAttribute("dateEnd",dateEnd==null?"":format1.format(dateEnd));
        model.addAttribute("sumElement",sumElement);
        return "/backend/tutor";
    }

    /**
     * 师资力量新建
     * @return newtutor.html
     */
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
    @RequestMapping("/backend/modifyTutor")
    public String modifyTutor(Long id, Model model,HttpServletRequest request){
        Tutor tutor=tutorService.findOneById(id);
        //返回
        tutor.setPictureUri(request.getContextPath() + "/uploadResources"+tutor.getPictureUri());
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
    @RequestMapping(value = "/backend/addSaveTutor",method = RequestMethod.POST)
    public String addSaveTutor(String name,String introduction,String qualification,String area,@RequestParam("smallimg") MultipartFile file) throws Exception{
        try {
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

            return "redirect:/backend/loadTutor";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/backend/error";

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
    @RequestMapping("/backend/modifySaveTutor")
    public String ModifySaveTutor(Long id,String name,String introduction,String qualification,String area,@RequestParam("smallimg") MultipartFile file) throws Exception{

        if(file.getSize()!=0){
            if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
        }
        //获取需要修改的图片路径，并删除
        staticResourceService.deleteResource(staticResourceService.getResource(tutorService.findOneById(id).getPictureUri()));
        //保存图片
        String fileName = StaticResourceService.TUTOR_ICON + UUID.randomUUID().toString() + ".png";
        staticResourceService.uploadResource(fileName,file.getInputStream());

        Tutor tutor=tutorService.findOneById(id);
        if(file.getSize()!=0){
            tutor.setPictureUri(fileName);
        }
        tutor.setQualification(qualification);
        tutor.setArea(area);
        tutor.setIntroduction(introduction);
        tutor.setName(name);
        tutor.setLastUploadDate(new Date());
        tutorService.modify(tutor);
        return "redirect:/backend/loadTutor";
    }

}
