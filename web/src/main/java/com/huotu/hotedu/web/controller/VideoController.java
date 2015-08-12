package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Video;
import com.huotu.hotedu.service.VideoService;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by shiliting on 2015/6/10.
 * 视频有关的Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class VideoController {

    /**
     * 教学视频的service层
     */
    @Autowired
    VideoService videoService;
    @Autowired
    StaticResourceService staticResourceService;

//    @Autowired
//    IqiyiVideoRepository iqiyiVideoRepository;

    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE = 5;//每张页面的记录数
    public static final int PAGE_SIZE_F = 21;

    //后台显示所有视频信息
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/loadVideo")
    public String searchVideo(@RequestParam(required = false) Integer pageNo,
                              @RequestParam(required = false) String keywords,
                              Model model) throws Exception {
        String turnPage="/backend/video";
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        Page<Video> pages = videoService.searchVideo(pageNo, PAGE_SIZE, keywords);
        long totalRecords = pages.getTotalElements();
        int numEl =  pages.getNumberOfElements();
        if(numEl==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages = videoService.searchVideo(pageNo, PAGE_SIZE, keywords);
            totalRecords = pages.getTotalElements();
        }
        for(Video v:pages){
            v.setThumbnail(staticResourceService.getResource(v.getThumbnail()).toString());
        }

        model.addAttribute("AllVideoList", pages);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalRecords", totalRecords);
        return turnPage;
    }


    @RequestMapping("/pc/loadVideo")
    public String loadVideo(@RequestParam(required = false) Integer pageNo,
                            @RequestParam(required = false) String keywords,Model model) throws Exception {
        String turnPage = "/pc/yun-jxspnew";
        if (pageNo == null || pageNo < 0) {
            pageNo = 0;
        }
        Page<Video> pages = videoService.searchVideo(pageNo, PAGE_SIZE_F, keywords);
        long totalRecords = pages.getTotalElements();
        int numEl = pages.getNumberOfElements();
        if (numEl == 0) {
            pageNo = pages.getTotalPages() - 1;
            if (pageNo < 0) {
                pageNo = 0;
            }
            pages = videoService.searchVideo(pageNo, PAGE_SIZE_F, keywords);
            totalRecords = pages.getTotalElements();
        }
        List<Video> video1 = new ArrayList<>();
        List<Video> videos1 = new ArrayList<>();
        int sum = 0;
        for (Video video : pages) {
            video.setThumbnail(staticResourceService.getResource(video.getThumbnail()).toURL().toString());
            if(sum==0) {
                video1.add(video);
            }else if (sum < 21) {
                videos1.add(video);
            }
            sum++;
        }
        model.addAttribute("video1", video1);
        model.addAttribute("videos1", videos1);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalRecords", totalRecords);
        return turnPage;
    }


    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/addVideo")
    public String addVideo() {
        return "/backend/newvideo";
    }


    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifyVideo")
    public String ModifyVideo(Long id, Model model) throws Exception{
        Video video=videoService.findOneById(id);
        video.setThumbnail(staticResourceService.getResource(video.getThumbnail()).toString());
        model.addAttribute("video",video);
        return "/backend/modifyvideo";
    }



    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping(value = "/backend/addSaveVideo", method = RequestMethod.POST)
    public String addSaveVideo(String videoName,String content,String playUrl,Boolean free,@RequestParam("thumbnail") MultipartFile file) throws Exception {
        //文件格式判断
        if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
        if(file.getSize()==0){throw new Exception("文件为空！");}
        //保存图片
        String fileName = StaticResourceService.VIDEO_ICON + UUID.randomUUID().toString() + ".png";
        staticResourceService.uploadResource(fileName, file.getInputStream());
        Video video=new Video();
        video.setFree(free);
        video.setPlayUrl(playUrl);
        video.setThumbnail(fileName);
        video.setContent(content);
        video.setUploadTime(new Date());
        video.setVideoName(videoName);
        videoService.addVideo(video);
        return "redirect:/backend/loadVideo";
    }



    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifySaveVideo")
    public String modifySaveVideo(Long id,String videoName,String content,String playUrl,Boolean free,@RequestParam("thumbnail") MultipartFile file) throws Exception{
        Video video=videoService.findOneById(id);
        if(file.getSize()!=0){
            if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
            staticResourceService.deleteResource(staticResourceService.getResource(videoService.findOneById(id).getThumbnail()));
            String fileName = StaticResourceService.VIDEO_ICON + UUID.randomUUID().toString() + ".png";
            staticResourceService.uploadResource(fileName,file.getInputStream());
            video.setThumbnail(fileName);
        }
        video.setFree(free);
        video.setVideoName(videoName);
        video.setContent(content);
        video.setPlayUrl(playUrl);
        video.setUploadTime(new Date());
        videoService.modifyVideo(video);
        return "redirect:/backend/loadVideo";
    }

    /**
     * 删除视频
     * @param id
     * @return
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/delVideo")
    public String delVideo(@RequestParam(required = false)Integer pageNo,@RequestParam(required = false)String keywords,Long id,Model model) throws Exception {
        String returnPage="redirect:/backend/loadVideo";
        videoService.delVideo(id);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        return returnPage;
    }

}

