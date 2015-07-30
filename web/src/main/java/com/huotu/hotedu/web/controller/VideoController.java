package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Video;
import com.huotu.hotedu.service.VideoService;
import com.huotu.iqiyi.sdk.IqiyiVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * Created by shiliting on 2015/6/10.
 * 视频有关的Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class VideoController {

    @Autowired
    private IqiyiVideoRepository iqiyiVideoRepository;

    /**
     * 教学视频的service层
     */
    @Autowired
    VideoService videoService;

    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE=10;//每张页面的记录数
    //后台显示所有视频信息

    /**
     * 显示教学视频信息
     * @param model   返回页面集
     * @return  video.html
     */
    @RequestMapping("/backend/loadVideo")
    public String loadVideo(Model model) {
        Page<Video> pages = videoService.loadVideo(0,PAGE_SIZE);
        long totalRecords = pages.getTotalElements();
        model.addAttribute("videoList",pages);
        model.addAttribute("totalPages",totalRecords/pages.getSize()+1);
        model.addAttribute("n",0);
        model.addAttribute("keywords","");
        model.addAttribute("totalRecords",totalRecords);
        return "/backend/video";
    }

    /**
     * 搜索符合条件的视频信息
     * @param keywords  搜索关键字
     * @param model     返回客户端参数集
     * @return      video.html
     */
    @RequestMapping("/backend/searchVideo")
    public String searchVideo(String keywords,Model model) {
        Page<Video> pages=videoService.searchVideo(0, PAGE_SIZE, keywords);
        long totalRecords=pages.getTotalElements();
        model.addAttribute("videoList",pages);
        model.addAttribute("totalPages",totalRecords/pages.getSize()+1);
        model.addAttribute("n",0);
        model.addAttribute("keywords",keywords);
        model.addAttribute("totalRecords",totalRecords);
        return "/backend/video";
    }

    /**
     * 分页显示
     * @param n             显示第几页
     * @param totalPages    分页总页数
     * @param keywords      检索关键字(使用检索功能后有效)
     * @param model         返回客户端集合
     * @return          video.html
     */
    @RequestMapping("/backend/pageVideo")
    public String pageVideo(int n,int totalPages,String keywords,Model model) {
        //如果已经到分页的第一页了，将页数设置为0
        if (n < 0){
            n++;
        }else if(n + 1 > totalPages){//如果超过分页的最后一页了，将页数设置为最后一页
            n--;
        }
        Page<Video> pages = videoService.searchVideo(n, PAGE_SIZE, keywords);
        model.addAttribute("videoList",pages);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("totalRecords",pages.getTotalElements());
        return "/backend/video";
    }

    /**
     * 删除视频信息
     * @param n             显示第几页
     * @param totalPages    分页总页数
     * @param keywords      检索关键字(使用检索功能后有效)
     * @param id            需要被删除的记录id
     * @param totalRecords  总记录数
     * @param model         返回客户端集合
     * @return      video.html
     */
    @RequestMapping("/backend/delVideo")
    public String delVideo(int n,int totalPages,String keywords,Long id,Long totalRecords,Model model){
        videoService.delVideo(id);
        if((totalRecords-1)%PAGE_SIZE==0){
            if(n>0&&n+1==totalPages){n--;}
            totalPages--;
        }
        totalRecords--;
        Page<Video> pages = videoService.searchVideo(n, PAGE_SIZE, keywords);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("videoList",pages);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("totalRecords",totalRecords);
        return "/backend/video";
    }

    /**
     * video.html页面单击新建跳转
     * @return newvideo.html
     */
    @RequestMapping("/backend/addVideo")
    public String addQa(){
        return "/backend/newvideo";
    }

    /**
     * video.html页面点击修改后跳转
     * @param id        需要修改视频的id
     * @param model     返回客户端集
     * @return      modifyvideo.html
     */
    @RequestMapping("/backend/modifyVideo")
    public String modifyVideo(Long id, Model model){
        Video video=videoService.findOneById(id);
        model.addAttribute("video",video);
        return "/backend/modifyvideo";
    }

    /**
     * newvideo.html页面点击保存添加后跳转
     * @param videoName     新建后视频的名字
     * @param videoAddr     新建后视频的地址
     * @param free          视频是否免费
     * @return      不出异常重定向：/backend/loadVideo
     */
    //TODO 是否搞抛出异常
    @RequestMapping("/backend/addSaveVideo")
    public String addSaveVideo(String videoName,String videoAddr,String free){
        Video video=new Video();
        video.setVideoName(videoName);
        video.setVideoAddr(videoAddr);
        video.setUploadTime(new Date());
        video.setFree("1".equals(free)?true:false);
        videoService.addVideo(video);
        return "redirect:/backend/loadVideo";
    }

    /**
     * modifyvideo.html页面点击保存修改后跳转
     * @param id    修改后的视频id
     * @param videoName     修改后的视频名字
     * @param videoAddr     修改后的视频地址
     * @param free      是否免费
     * @return      重定向到：/backend/loadVideo
     */
    @RequestMapping("/backend/modifySaveVideo")
    public String modifySaveQa(Long id,String videoName,String videoAddr,String free){
        Video video=videoService.findOneById(id);
        video.setVideoName(videoName);
        video.setVideoAddr(videoAddr);
        video.setUploadTime(new Date());
        video.setFree("1".equals(free)?true:false);
        videoService.modifyVideo(video);
        return "redirect:/backend/loadVideo";
    }

}
