package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Enterprise;
import com.huotu.hotedu.service.EnterpriseService;
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
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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
        System.out.println("类型："+searchSort);
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


    //后台单击发布企业的分页
    @RequestMapping("/backend/pageEnterprise")
    public String pageEnterprise(int n,int sumpage,String searchSort,String keywords,String dateStart,String dateEnd,Model model) throws Exception{
        //如果已经到分页的第一页了，将页数设置为0
        if (n < 0){
            n++;
        }else if(n + 1 > sumpage){//如果超过分页的最后一页了，将页数设置为最后一页
            n--;
        }
        Page<Enterprise> pages=null;
        if("date".equals(searchSort)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            try {
                Date DStart=sdf.parse(dateStart);
                Date DEnd=sdf.parse(dateEnd);
                pages=enterpriseService.searchEnterpriseDate(n, PAGE_SIZE, DStart, DEnd);
            } catch (ParseException e) {
                e.printStackTrace();
                //日期格式不正确
            }
        }else if("all".equals(searchSort)){
            pages=enterpriseService.searchEnterpriseAll(n, PAGE_SIZE, keywords);

        }else{
            pages=enterpriseService.searchEnterpriseType(n, PAGE_SIZE, keywords, searchSort);
        }
        if(pages==null){
            throw new Exception("没有数据！");
        }
        model.addAttribute("allEnterpriseList",pages);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("searchSort",searchSort);
        model.addAttribute("dateStart",dateStart);
        model.addAttribute("dateEnd",dateEnd);
        model.addAttribute("sumElement",pages.getTotalElements());
        return "/backend/enterprises";
    }



    //后台单击删除按钮返回的信息
    @RequestMapping("/backend/delEnterprise")
    public String delEnterprise(int n,int sumpage,String searchSort,String keywords,String dateStart,String dateEnd,Long id,Long sumElement,Model model){
        try {
            staticResourceService.deleteResource(enterpriseService.findOneById(id).getLogoUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        enterpriseService.delEnterprise(id);//删除记录
        //如果删除一条记录后总记录数为10的倍数，则修改总页数
        if((sumElement-1)%PAGE_SIZE==0){
            if(n>0&&n+1==sumpage){n--;}
            sumpage--;
        }
        sumElement--;

        Page<Enterprise> pages=null;
        if("date".equals(searchSort)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            try {
                Date DStart=sdf.parse(dateStart);
                Date DEnd=sdf.parse(dateEnd);
                pages=enterpriseService.searchEnterpriseDate(n, PAGE_SIZE, DStart, DEnd);
            } catch (ParseException e) {
                e.printStackTrace();
                //日期格式不正确
            }
        }else if("all".equals(searchSort)){
            pages=enterpriseService.searchEnterpriseAll(n, PAGE_SIZE, keywords);

        }else{
            pages=enterpriseService.searchEnterpriseType(n, PAGE_SIZE, keywords, searchSort);

        }
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("allEnterpriseList",pages);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("searchSort",searchSort);
        model.addAttribute("dateStart",dateStart);
        model.addAttribute("dateEnd",dateEnd);
        model.addAttribute("sumElement",sumElement);
        return "/backend/enterprises";
    }



















    //后台单击新建按钮
    @RequestMapping("/backend/addEnterprise")
    public String AddEnterprise(Model model){
        return "/backend/newenterprise";
    }
    //后台单机修改按钮
    @RequestMapping("/backend/modifyEnterprise")
    public String ModifyEnterprise(Long id, Model model){
        Enterprise enterprise=enterpriseService.findOneById(id);
        model.addAttribute("enterprise",enterprise);
        return "/backend/modifyenterprise";
    }


    //后台单击添加保存按钮
    @RequestMapping(value = "/backend/addSaveEnterprise",method = RequestMethod.POST)
    public String addSaveEnterprise(String name,String introduction,String tel,boolean isPutaway,@RequestParam("smallimg") MultipartFile file,Model model) throws Exception{
        try {


            //文件格式判断
            if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
            if(file.getSize()==0){throw new Exception("文件为空！");}
            if(file.getSize()>1024*1024*5){throw new Exception("文件太大");}

            //保存图片
            String fileName = StaticResourceService.COMPANY_LOGO + UUID.randomUUID().toString() + ".png";
            staticResourceService.uploadResource(fileName,file.getInputStream());



            Enterprise enterprise=new Enterprise();
            enterprise.setName(name);
            enterprise.setTel(tel);
            enterprise.setStatus(0);
            enterprise.setIsPutaway(isPutaway);
            enterprise.setInformation(introduction);
            enterprise.setLogoUri(fileName);
            enterprise.setLastUploadDate(new Date());
            enterpriseService.addEnterprise(enterprise);
            return "redirect:/backend/loadEnterprises";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/backend/error";

    }


    //后台单击修改保存按钮
    @RequestMapping("/backend/modifySaveEnterprise")
    public String ModifySaveEnterprise(Long id,String name,String introduction,String tel,boolean isPutaway,@RequestParam("smallimg") MultipartFile file,Model model) throws Exception{



        if(file.getSize()!=0){
            if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
        }
        if(file.getSize()>1024*1024*5){throw new Exception("文件太大");}


        //获取需要修改的图片路径，并删除
        staticResourceService.deleteResource(staticResourceService.getResource(enterpriseService.findOneById(id).getLogoUri()));
        //保存图片
        String fileName = StaticResourceService.TUTOR_ICON + UUID.randomUUID().toString() + ".png";
        staticResourceService.uploadResource(fileName,file.getInputStream());

        Enterprise enterprise=enterpriseService.findOneById(id);
        enterprise.setTel(tel);
        enterprise.setName(name);
        enterprise.setIsPutaway(isPutaway);
        enterprise.setInformation(introduction);
        if(file.getSize()!=0){
            enterprise.setLogoUri(fileName);
        }
        enterprise.setLastUploadDate(new Date());
        enterpriseService.modify(enterprise);
        return "redirect:/backend/loadEnterprises";
    }




}
