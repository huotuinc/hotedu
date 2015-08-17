package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.entity.Result;
import com.huotu.hotedu.service.ExamGuideService;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public String addExamGuide(Model model) {
        String serverPath = StaticResourceService.EXAMGUIDE_ICON;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String imageName = df.format(new Date()) + "_" + new Random().nextInt(1000) + ".png";
        model.addAttribute("serverPath",serverPath);
        model.addAttribute("imageName",imageName);
        return "/backend/newguide";
    }

    /**
     * 图片空间
     * @param request
     * @return
     */
    @RequestMapping("/backend/examGuideFileManager")
    @ResponseBody
    public Result examGuideFileManager(HttpServletRequest request) {
        Result result = new Result();

        //根目录路径，可以指定绝对路径，比如 /var/www/attached/
        String rootPath = request.getServletContext().getRealPath("/") + "image/examGuide/";
        //根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/
        String rootUrl  = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath() + "/backend/";
        //图片扩展名
        String[] fileTypes = new String[]{"gif", "jpg", "jpeg", "png", "bmp"};

        String dirName = request.getParameter("dir");
        if (dirName != null) {
            if(!Arrays.<String>asList(new String[]{"image", "flash", "media", "file"}).contains(dirName)){
                result.setMessage("Invalid Directory name.");
                return result;
            }
            rootUrl += "images" + "/examGuide/";
        }
        //根据path参数，设置各路径和URL
        String path = request.getParameter("path") != null ? request.getParameter("path") : "";
        String currentPath = rootPath + path;
        String currentUrl = rootUrl + path;
        String currentDirPath = path;
        String moveupDirPath = "";
        if (!"".equals(path)) {
            String str = currentDirPath.substring(0, currentDirPath.length() - 1);
            moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
        }

        //排序形式，name or size or type
        String order = request.getParameter("order") != null ? request.getParameter("order").toLowerCase() : "name";

        //不允许使用..移动到上一级目录
        if (path.indexOf("..") >= 0) {
            result.setMessage("Access is not allowed.");
            return result;
        }
        //最后一个字符不是/
        if (!"".equals(path) && !path.endsWith("/")) {
            result.setMessage("Parameter is not valid.");
            return result;
        }
        //目录不存在或不是目录
        File currentPathFile = new File(currentPath);
        if(!currentPathFile.isDirectory()){
            result.setMessage("Directory does not exist.");
            return result;
        }
        //遍历目录取的文件信息
        List<Hashtable> fileList = new ArrayList<Hashtable>();
        if(currentPathFile.listFiles() != null) {
            for (File file : currentPathFile.listFiles()) {
                Hashtable<String, Object> hash = new Hashtable<String, Object>();
                String fileName = file.getName();
                if(file.isDirectory()) {
                    hash.put("is_dir", true);
                    hash.put("has_file", (file.listFiles() != null));
                    hash.put("filesize", 0L);
                    hash.put("is_photo", false);
                    hash.put("filetype", "");
                } else if(file.isFile()){
                    String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
                    hash.put("is_dir", false);
                    hash.put("has_file", false);
                    hash.put("filesize", file.length());
                    hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
                    hash.put("filetype", fileExt);
                }
                hash.put("filename", fileName);
                hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
                fileList.add(hash);
            }
        }
        /*if ("size".equals(order)) {
            Collections.sort(fileList, new SizeComparator());
        } else if ("type".equals(order)) {
            Collections.sort(fileList, new TypeComparator());
        } else {
            Collections.sort(fileList, new NameComparator());
        }
        JSONObject result = new JSONObject();*/
        result.setMoveup_dir_path(moveupDirPath);
        result.setCurrent_dir_path(currentDirPath);
        result.setCurrent_url(currentUrl);
        result.setTotal_count(fileList.size());
        result.setFile_list(fileList);

        return result;
    }

    @RequestMapping("/backend/examGuideUpload")
    @ResponseBody
    public Result editorUpload(@RequestParam("imgFile")MultipartFile file,HttpResponse response) {
        Result obj = new Result();
        //文件保存目录URL
        String saveUrl  = StaticResourceService.EXAMGUIDE_ICON;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + ".png";
        //文件保存目录路径
        String savePath = saveUrl + newFileName;
        URI uri = null;
        try {
            uri = staticResourceService.uploadResource(savePath, file.getInputStream());
        } catch (Exception e) {
            obj.setStatus(1);
            obj.setMessage("上传文件失败");
            return obj;
        }
        obj.setError(0);
        obj.setUrl(uri.toString());
        response.setHeader("X-frame-Options","SAMEORIGIN");
        return obj;
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
        String editorUpload = "uploadFile";
        ExamGuide examGuide = examGuideService.findOneById(id);
        examGuide.setPictureUri(staticResourceService.getResource(examGuide.getPictureUri()).toString());
        model.addAttribute("editorUpload", editorUpload);
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
    public String addSaveExamGuide(String title, String content,String detail, Boolean top ,@RequestParam("smallimg") MultipartFile file) throws Exception{
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
        examGuide.setDetail(detail);
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
    public String modifySaveExamGuide(Long id, String title, String content,String detail, Boolean top ,@RequestParam("smallimg") MultipartFile file) throws Exception{
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
        examGuide.setDetail(detail);
        examGuide.setLastUploadDate(new Date());
        examGuideService.modify(examGuide);
        return "redirect:/backend/searchExamGuide";
    }

    /**
     * Created by jiashubing on 2015/8/7.
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
        Page<ExamGuide> pages = examGuideService.loadPcExamGuide(pageNo, PAGE_SIZE);
        long totalRecords = pages.getTotalElements();
        int numEl =  pages.getNumberOfElements();
        if(numEl==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages = examGuideService.loadPcExamGuide(pageNo, PAGE_SIZE);
            totalRecords = pages.getTotalElements();
        }

        for(ExamGuide examGuide : pages){
            examGuide.setPictureUri(staticResourceService.getResource(examGuide.getPictureUri()).toURL().toString());
        }

        Date today = new Date();
        model.addAttribute("allExamGuideList", pages);
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
     * @return      yun-xqkaoshi.html
     */
    @RequestMapping("/pc/loadDetailExamGuide")
    public String loadDetailExamGuide(Long id,Model model) throws Exception{
        String turnPage="/pc/yun-xqkaoshi";
        ExamGuide examGuide = examGuideService.findOneById(id);
        examGuide.setPictureUri(staticResourceService.getResource(examGuide.getPictureUri()).toURL().toString());
        model.addAttribute("examGuide",examGuide);
        return turnPage;
    }
}
