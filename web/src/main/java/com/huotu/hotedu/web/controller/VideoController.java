package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Video;
import com.huotu.hotedu.service.VideoService;
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
    VideoService videoService;
    public static final int PAGE_SIZE=10;//每张页面的记录数
    //后台显示所有视频信息
    @RequestMapping("/backend/loadVideos")
    public String loadWantedes(Model model) {
        Page<Video> pages = videoService.loadVideo(0,PAGE_SIZE);
        long totalRecords = pages.getTotalElements();
        model.addAttribute("videoList",pages);
        model.addAttribute("totalPages",totalRecords/pages.getSize()+1);
        model.addAttribute("n",0);
        model.addAttribute("keywords","");
        model.addAttribute("totalRecords",totalRecords);
        return "/backend/video";
    }

    //后台显示检索之后的视频信息
    @RequestMapping("/backend/searchVideo")
    public String searchVideos(String keywords,Model model) {
        Page<Video> pages=videoService.searchVideo(0, PAGE_SIZE, keywords);
        long totalRecords=pages.getTotalElements();
        model.addAttribute("videoList",pages);
        model.addAttribute("totalPages",totalRecords/pages.getSize()+1);
        model.addAttribute("n",0);
        model.addAttribute("keywords",keywords);
        model.addAttribute("totalRecords",totalRecords);
        return "/backend/video";
    }

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

    //后台单击新建按钮
    @RequestMapping("/backend/addVideo")
    public String addQa(Model model){
        return "/backend/newvideo";
    }
    //后台单机修改按钮
    @RequestMapping("/backend/modifyVideo")
    public String modifyVideo(Long id, Model model){
        Video video=videoService.findOneById(id);
        model.addAttribute("video",video);
        return "/backend/modifyvideo";
    }


    //后台单击添加保存按钮
    @RequestMapping("/backend/addSaveVideo")
    public String addSaveVideo(String videoName,String videoAddr,String free){
        Video video=new Video();
        video.setVideoName(videoName);
        video.setVideoAddr(videoAddr);
        video.setUploadTime(new Date());
        video.setFree("1".equals(free)?true:false);
        videoService.addVideo(video);
        return "redirect:/backend/loadVideos";
    }


    //后台单击修改保存按钮
    @RequestMapping("/backend/modifySaveVideo")
    public String modifySaveQa(Long id,String videoName,String videoAddr,String free){
        Video video=videoService.findOneById(id);
        video.setVideoName(videoName);
        video.setVideoAddr(videoAddr);
        video.setUploadTime(new Date());
        video.setFree("1".equals(free)?true:false);
        videoService.modifyVideo(video);
        return "redirect:/backend/loadVideos";
    }

}
