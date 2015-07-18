package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Enterprise;
import com.huotu.hotedu.service.EnterpriseService;
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
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by shiliting on 2015/6/10.
 * 企业有关的Controller,主要负责处理企业有关的请求
 * @Time 2015-6-10
 * @author shiliting shiliting741@163.com
 */
@Controller
public class EnterprisesController {

    /**企业的Service层*/
    @Autowired
    EnterpriseService enterpriseService;

    /**  用来储存处理静态资源的接口 */
    @Autowired
    StaticResourceService staticResourceService;

    /**  PAGE_SIZE用来储存分页中每页的记录数    */
    public static final int PAGE_SIZE=10;

    /**
     * 接收一个显示多条发布企业信息的请求，根据分页控制显示企业信息的条数，
     * 请求完成之后model层会添加
     * 1.多条企业信息的集合
     * 2.分页的总页数
     * 3.默认检索时使用的关键字(默认为空)
     * 4.默认显示第几页(默认为0：第一页)
     * 5.默认起始时间(以时间检索的时候有效)
     * 6.默认结束时间(以时间检索的时候有效)
     * 7.默认的检索类型
     * 8.企业的总记录数
     *
     * 返回给客户端一个HTML页面，并且在页面上添加model
     * @param model 准备向客户端发送的参数集合
     * @return enterprises.html
     */
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


    /**
     * 接收一个检索请求，返回检索之后包含企业信息的一个HTML页面，并在model添加
     * 1.多条企业信息的集合
     * 2.分页的总页数
     * 3.默认检索时使用的关键字(默认为空)
     * 4.默认显示第几页(默认为0：第一页)
     * 5.默认起始时间(以时间检索的时候有效)
     * 6.默认结束时间(以时间检索的时候有效)
     * 7.默认的检索类型
     * 8.企业的总记录数
     *
     * @param searchSort 从客户端返回的检索类型
     * @param keywords   从客户端返回的检索关键字
     * @param dateStart  从客户端返回的检索起始时间(以时间检索的时候有效)
     * @param dateEnd    从客户端返回的检索结束时间(以时间检索的时候有效)
     * @param model      准备向客户端发送的参数集合
     * @return           enterprises.html
     * @throws Exception
     */
    @RequestMapping("/backend/searchEnterprises")
    public String searchEnterprises(String searchSort,String keywords,@DateTimeFormat(pattern = "yyyy.MM.dd")Date dateStart,@DateTimeFormat(pattern = "yyyy.MM.dd")Date dateEnd,Model model) throws Exception{
        Page<Enterprise> pages=null;
        DateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
        if("date".equals(searchSort)){
            if("".equals(dateStart)||"".equals(dateEnd)){
                return "redirect:/backend/loadTutor";
            }
            pages=enterpriseService.searchEnterpriseDate(0, PAGE_SIZE, dateStart, dateEnd);

        }else if("all".equals(searchSort)){
            pages=enterpriseService.searchEnterpriseAll(0, PAGE_SIZE, keywords);

        }else{
            pages=enterpriseService.searchEnterpriseType(0, PAGE_SIZE, keywords, searchSort);

        }
        if(pages==null){
            throw new Exception("没有数据！");
        }
        long sumElement=pages.getTotalElements();
        model.addAttribute("allEnterprisesList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords",keywords);
        model.addAttribute("dateStart",format1.format(dateStart));
        model.addAttribute("dateEnd",format1.format(dateEnd));
        model.addAttribute("searchSort",searchSort);
        model.addAttribute("sumElement",sumElement);
        return "/backend/enterprises";
    }


    //后台单击发布企业的分页

    /**
     * 接收一个分页请求，返回一个分页之后的HTML页面，并在model添加
     * 1.多条企业信息的集合
     * 2.分页的总页数
     * 3.默认检索时使用的关键字(默认为空)
     * 4.默认显示第几页(默认为0：第一页)
     * 5.默认起始时间(以时间检索的时候有效)
     * 6.默认结束时间(以时间检索的时候有效)
     * 7.默认的检索类型
     * 8.企业的总记录数
     * @param n           显示第几页
     * @param sumpage     分页总页数
     * @param searchSort  检索类型(使用检索功能后有效)
     * @param keywords    检索关键字(使用检索功能后有效)
     * @param dateStart   检索起始时间(使用检索功能后有效)
     * @param dateEnd     检索结束时间(使用检索功能后有效)
     * @param model       准备向客户端发送的参数集合
     * @return            enterprises.html
     * @throws Exception
     */
    @RequestMapping("/backend/pageEnterprise")
    public String pageEnterprise(int n,int sumpage,String searchSort,String keywords,@DateTimeFormat(pattern = "yyyy.MM.dd")Date dateStart,@DateTimeFormat(pattern = "yyyy.MM.dd")Date dateEnd,Model model) throws Exception{

        if (n < 0){                     //如果已经到分页的第一页了，将页数设置为0
            n++;
        }else if(n + 1 > sumpage){      //如果超过分页的最后一页了，将页数设置为最后一页
            n--;
        }
        Page<Enterprise> pages=null;
        DateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
        if("date".equals(searchSort)){
            pages=enterpriseService.searchEnterpriseDate(n, PAGE_SIZE, dateStart, dateEnd);
        }else if("all".equals(searchSort)){
            pages=enterpriseService.searchEnterpriseAll(n, PAGE_SIZE, keywords);

        }else{
            pages=enterpriseService.searchEnterpriseType(n, PAGE_SIZE, keywords, searchSort);
        }
        if(pages==null){
            throw new Exception("没有数据！");
        }
        model.addAttribute("allEnterprisesList",pages);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("searchSort",searchSort);
        model.addAttribute("dateStart",format1.format(dateStart));
        model.addAttribute("dateEnd",format1.format(dateEnd));
        model.addAttribute("sumElement",pages.getTotalElements());
        return "/backend/enterprises";
    }




    /**
     * 接收一个删除发布企业信息的请求，根据id删除对应的记录，返回一个HTML页面，并在model层添加
     * 1.多条企业信息的集合
     * 2.分页的总页数
     * 3.默认检索时使用的关键字(默认为空)
     * 4.默认显示第几页(默认为0：第一页)
     * 5.默认起始时间(以时间检索的时候有效)
     * 6.默认结束时间(以时间检索的时候有效)
     * 7.默认的检索类型
     * 8.企业的总记录数
     *
     * @param keywords    检索关键字(使用检索功能后有效)
     * @param dateStart   检索起始时间(使用检索功能后有效)
     * @param dateEnd     检索结束时间(使用检索功能后有效)
     * @param model       准备向客户端发送的参数集合
     * @param n           显示第几页
     * @param sumpage     分页总页数
     * @param searchSort  检索类型(使用检索功能后有效)
     * @param id          需要被删除的记录id
     * @param sumElement  总记录数
     * @return            enterprises.html
     */
    @RequestMapping("/backend/delEnterprise")
    public String delEnterprise(int n,int sumpage,String searchSort,String keywords,@DateTimeFormat(pattern = "yyyy.MM.dd")Date dateStart,@DateTimeFormat(pattern = "yyyy.MM.dd")Date dateEnd,Long id,Long sumElement,Model model){
        try {
            staticResourceService.deleteResource(enterpriseService.findOneById(id).getLogoUri());//删除静态资源
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
        DateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
        if("date".equals(searchSort)){
            pages=enterpriseService.searchEnterpriseDate(n, PAGE_SIZE, dateStart, dateEnd);
        }else if("all".equals(searchSort)){
            pages=enterpriseService.searchEnterpriseAll(n, PAGE_SIZE, keywords);

        }else{
            pages=enterpriseService.searchEnterpriseType(n, PAGE_SIZE, keywords, searchSort);

        }
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("allEnterprisesList",pages);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("searchSort",searchSort);
        model.addAttribute("dateStart",format1.format(dateStart));
        model.addAttribute("dateEnd",format1.format(dateEnd));
        model.addAttribute("sumElement",sumElement);
        return "/backend/enterprises";
    }



    /**
     * 接收跳转新建页面请求，返回一个HTML页面
     * @return   newenterprise.html
     */
    @RequestMapping("/backend/addEnterprise")
    public String addEnterprise(){
        return "/backend/newenterprise";
    }

    /**
     * 接收跳转修改企业页面的请求，返回一个HTML页面
     * @param id     需要修改记录的id
     * @param model  准备向客户端发送的参数集合
     * @return       modifyenterprise.html
     */
    @RequestMapping("/backend/modifyEnterprise")
    public String modifyEnterprise(Long id, Model model){
        Enterprise enterprise=enterpriseService.findOneById(id);
        model.addAttribute("enterprise",enterprise);
        return "/backend/modifyenterprise";
    }


    /**
     * 接收一个添加发布企业信息的请求，返回一个HTML页面
     * @param name         企业的名称
     * @param information  企业的招聘信息
     * @param tel          企业的电话
     * @param file         企业的LOGO
     * @return             不出异常重定向：/backend/loadEnterprises 抛出异常重定向：/backend/error
     * @throws Exception
     */
    @RequestMapping(value = "/backend/addSaveEnterprise",method = RequestMethod.POST)
    public String addSaveEnterprise(String name,String information,String tel,@RequestParam("smallimg") MultipartFile file) throws Exception{
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
            enterprise.setIsPutaway(false);
            enterprise.setInformation(information);
            enterprise.setLogoUri(fileName);
            enterprise.setLastUploadDate(new Date());
            System.out.println("准备添加");
            enterpriseService.addEnterprise(enterprise);
            System.out.println("添加完毕");
            return "redirect:/backend/loadEnterprises";

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("出错了");
            return"redirect:/backend/error";

        }
    }



    /**
     * 接收一个修改发布企业的请求，返回一个HTML页面
     * @param id            需要修改的记录id
     * @param name          企业名称
     * @param introduction  企业招聘简介
     * @param tel           企业电话
     * @param isPutaway     是否下架
     * @param file          企业LOGO
     * @return              重定向到：/backend/loadEnterprises
     * @throws Exception
     */
    @RequestMapping("/backend/modifySaveEnterprise")
    public String ModifySaveEnterprise(Long id,String name,String introduction,String tel,boolean isPutaway,@RequestParam("smallimg") MultipartFile file) throws Exception{

        if(file.getSize()!=0){
            if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
        }
        if(file.getSize()>1024*1024*5){throw new Exception("文件太大");}


        staticResourceService.deleteResource(staticResourceService.getResource(enterpriseService.findOneById(id).getLogoUri()));    //获取需要修改的图片路径，并删除
        String fileName = StaticResourceService.TUTOR_ICON + UUID.randomUUID().toString() + ".png";                                 //保存图片
        staticResourceService.uploadResource(fileName,file.getInputStream());                                                       //添加静态图片资源
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
