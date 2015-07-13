package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Tutor;
import com.huotu.hotedu.service.TutorService;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by shiliting on 2015/6/10.
 * 师资力量有关的Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class TutorController {
    @Autowired
    TutorService tutorService;
    @Autowired
    StaticResourceService staticResourceService;
    public static final int PAGE_SIZE=10;//每张页面的记录数
    public static final String FILE_PATH="C:/Users/Administrator/IdeaProjects/hotedu/web/src/main/webapp/image/company/";
    //后台单击师资力量显示的消息
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
        model.addAttribute("sumElement",sumElement);
        return "/backend/tutor";
    }

    //后台单机搜索按钮显示的师资力量消息
    @RequestMapping("/backend/searchTutor")
    public String searchTutor(String searchSort,String keywords,String dateStart,String dateEnd,Model model) throws Exception{
        System.out.println(dateEnd);
        System.out.println(dateStart);
        System.out.println(searchSort);
        Page<Tutor> pages=null;
        if("date".equals(searchSort)){
            System.out.println("进入date");
            if("".equals(dateStart)||"".equals(dateEnd)){
              return "redirect:/backend/loadTutor";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            try {
                Date DStart=sdf.parse(dateStart);
                Date DEnd=sdf.parse(dateEnd);
                pages=tutorService.searchTutorDate(0,PAGE_SIZE,DStart,DEnd);
            } catch (ParseException e) {
                e.printStackTrace();
                //日期格式不正确
                throw new Exception("日期格式错误！");
            }
        }else if("all".equals(searchSort)){
            System.out.println("进入all");
            pages=tutorService.searchTutorAll(0,PAGE_SIZE,keywords);
            System.out.println("进出all");

        }else{
            pages=tutorService.searchTutorType(0,PAGE_SIZE,keywords,searchSort);

        }
        if(pages==null){
            throw new Exception("没有数据！");
        }
        long sumElement=pages.getTotalElements();
        model.addAttribute("allTutorList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords",keywords);
        model.addAttribute("dateStart",dateStart);
        model.addAttribute("dateEnd",dateEnd);
        model.addAttribute("searchSort",searchSort);
        model.addAttribute("sumElement",sumElement);
        return "/backend/tutor";
    }

    //后台单击师资力量的分页
    @RequestMapping("/backend/pageTutor")
    public String pageTutor(int n,int sumpage,String searchSort,String keywords,String dateStart,String dateEnd,Model model) throws Exception{
        //如果已经到分页的第一页了，将页数设置为0
        if (n < 0){
            n++;
        }else if(n + 1 > sumpage){//如果超过分页的最后一页了，将页数设置为最后一页
            n--;
        }
        Page<Tutor> pages=null;
        if("date".equals(searchSort)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            try {
                Date DStart=sdf.parse(dateStart);
                Date DEnd=sdf.parse(dateEnd);
                pages=tutorService.searchTutorDate(n,PAGE_SIZE,DStart,DEnd);
            } catch (ParseException e) {
                e.printStackTrace();
                //日期格式不正确
            }
        }else if("all".equals(searchSort)){
            pages=tutorService.searchTutorAll(n,PAGE_SIZE,keywords);

        }else{
            pages=tutorService.searchTutorType(n,PAGE_SIZE,keywords,searchSort);
        }
        if(pages==null){
            throw new Exception("没有数据！");
        }
        System.out.println("进入pageTutor ,pages");
        model.addAttribute("allTutorList",pages);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("searchSort",searchSort);
        model.addAttribute("dateStart",dateStart);
        model.addAttribute("dateEnd",dateEnd);
        model.addAttribute("sumElement",pages.getTotalElements());
        return "/backend/tutor";
    }

    //后台单击删除按钮返回的信息
    @RequestMapping("/backend/delTutor")
    public String delTutor(int n,int sumpage,String searchSort,String keywords,String dateStart,String dateEnd,Long id,Long sumElement,Model model){
        File file=new File(tutorService.findOneById(id).getPictureUri());//创建要删除的文件
        tutorService.delTutor(id);//删除记录
        if(file.exists()){
            file.delete();//删除文件
        }
        //如果删除一条记录后总记录数为10的倍数，则修改总页数
        if((sumElement-1)%PAGE_SIZE==0){
            if(n>0&&n+1==sumpage){n--;}
            sumpage--;
        }
        sumElement--;

        Page<Tutor> pages=null;
        if("date".equals(searchSort)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            try {
                Date DStart=sdf.parse(dateStart);
                Date DEnd=sdf.parse(dateEnd);
                pages=tutorService.searchTutorDate(n,PAGE_SIZE,DStart,DEnd);
            } catch (ParseException e) {
                e.printStackTrace();
                //日期格式不正确
            }
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
        model.addAttribute("dateStart",dateStart);
        model.addAttribute("dateEnd",dateEnd);
        model.addAttribute("sumElement",sumElement);
        return "/backend/tutor";
    }




    //后台单击新建按钮
    @RequestMapping("/backend/addTutor")
    public String AddTutor(Model model){
        return "/backend/newtutor";
    }
    //后台单机修改按钮
    @RequestMapping("/backend/modifyTutor")
    public String ModifyTutor(Long id, Model model){
        Tutor tutor=tutorService.findOneById(id);
        model.addAttribute("tutor",tutor);
        return "/backend/modifytutor";
    }


    //后台单击添加保存按钮
    @RequestMapping(value = "/backend/addSaveTutor",method = RequestMethod.POST)
    public String addSaveTutor(String name,String introduction,String qualification,String area,@RequestParam("smallimg") MultipartFile file,Model model) throws Exception{
        try {
            if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
            System.out.println("文件大小：" + file.getSize());
            if(file.getSize()==0){throw new Exception("文件为空！");}
            if(file.getSize()>1024*1024*5){throw new Exception("文件太大");}
//            String fileName=new Date().getTime()+String.valueOf(file.getOriginalFilename());
            String fileName = StaticResourceService.TUTOR_ICON + UUID.randomUUID().toString() + ".png";
            staticResourceService.uploadResource(fileName,file.getInputStream());
//            File tempFile = new File(FILE_PATH, filePath);
//            if (!tempFile.getParentFile().exists()) {
//                tempFile.getParentFile().mkdir();
//            }
//            if (!tempFile.exists()) {
//                tempFile.createNewFile();
//            }
//            file.transferTo(tempFile);//保存图片
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


    //后台单击修改保存按钮
    @RequestMapping("/backend/modifySaveTutor")
    public String ModifySaveTutor(Long id,String name,String introduction,String qualification,String area,@RequestParam("smallimg") MultipartFile file,Model model) throws Exception{
        if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
        System.out.println("文件大小：" + file.getSize());
        if(file.getSize()==0){throw new Exception("文件为空！");}
        if(file.getSize()>1024*1024*5){throw new Exception("文件太大");}
        File delfile=new File(tutorService.findOneById(id).getPictureUri());//创建要删除的文件
        if(delfile.exists()){
            delfile.delete();//删除文件
        }
        String filePath=new Date().getTime()+String.valueOf(file.getOriginalFilename());
        File tempFile = new File(FILE_PATH, filePath);
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdir();
        }
        if (!tempFile.exists()) {
            tempFile.createNewFile();
        }
        file.transferTo(tempFile);//保存图片


        Tutor tutor=tutorService.findOneById(id);
        tutor.setPictureUri(FILE_PATH+filePath);
        tutor.setQualification(qualification);
        tutor.setArea(area);
        tutor.setIntroduction(introduction);
        tutor.setName(name);
        tutor.setLastUploadDate(new Date());
        tutorService.modify(tutor);
        return "redirect:/backend/loadTutor";
    }

}
