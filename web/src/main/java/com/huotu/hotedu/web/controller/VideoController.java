package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Video;
import com.huotu.hotedu.service.VideoService;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.atteo.evo.inflector.English;
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
import java.net.URISyntaxException;
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

    /**
     * 后台显示所有视频信息
     */
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

    /**
     * 前台显示所有视频信息
     */
    @RequestMapping("/pc/loadVideo")
    public String loadVideo(Model model) throws Exception {
        String turnPage = "/pc/yun-jxspnewone";
        List<Video> videoList1=videoService.loadPcSmallVideo(0, 6).getContent();
        for(Video v1:videoList1){
            v1.setThumbnail(staticResourceService.getResource(v1.getThumbnail()).toString());
        }
        List<Video> videoList2=videoService.loadPcSmallVideo(1, 6).getContent();
        for(Video v2:videoList2){
            v2.setThumbnail(staticResourceService.getResource(v2.getThumbnail()).toString());
        }
        List<Video> videoList3=videoService.loadPcSmallVideo(2, 6).getContent();
        for(Video v3:videoList3){
            v3.setThumbnail(staticResourceService.getResource(v3.getThumbnail()).toString());
        }
        List<Video> videoList4=videoService.loadPcSmallVideo(3, 6).getContent();
        for(Video v4:videoList4){
            v4.setThumbnail(staticResourceService.getResource(v4.getThumbnail()).toString());
        }

        model.addAttribute("videoList1",videoList1);
        model.addAttribute("videoList2",videoList2);
        model.addAttribute("videoList3",videoList3);
        model.addAttribute("videoList4",videoList4);
        model.addAttribute("flag","yun-jxspnewone.html");  //此属性用来给前台确定当前是哪个页面

        return turnPage;
    }


    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/addVideo")
    public String addVideo(Integer pageNo, Model model) {
        model.addAttribute("pageNo",pageNo);
        return "/backend/newvideo";
    }


    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifyVideo")
    public String modifyVideo(Long id,Integer pageNo, Model model) throws Exception{
        Video video=videoService.findOneById(id);
        video.setThumbnail(staticResourceService.getResource(video.getThumbnail()).toString());
        model.addAttribute("video",video);
        model.addAttribute("pageNo",pageNo);
        return "/backend/modifyvideo";
    }



    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping(value = "/backend/addSaveVideo", method = RequestMethod.POST)
    public String addSaveVideo(Integer pageNo,String duration,String videoName,String content,String playUrl,Integer videoNo,Boolean complete,Boolean free,@RequestParam("thumbnail") MultipartFile file,Model model) throws Exception {
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
        video.setDuration(duration);
        video.setContent(content);
        video.setVideoNo(videoNo);
        video.setComplete(complete);
        video.setUploadTime(new Date());
        video.setVideoName(videoName);
        videoService.addVideo(video);
        model.addAttribute("pageNo",pageNo);
        return "redirect:/backend/loadVideo";
    }



    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifySaveVideo")
    public String modifySaveVideo(Long id,Integer pageNo,String duration,String videoName,String content,String playUrl,Integer videoNo,Boolean complete,
                                  Boolean free,@RequestParam("thumbnail") MultipartFile file,Model model) throws Exception{
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
        video.setDuration(duration);
        video.setContent(content);
        video.setPlayUrl(playUrl);
        video.setVideoNo(videoNo);
        video.setComplete(complete);
        video.setUploadTime(new Date());
        videoService.modifyVideo(video);
        model.addAttribute("pageNo",pageNo);
        return "redirect:/backend/loadVideo";
    }

    /**
     * 删除视频
     * @param id 删除视频的ID
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

    @RequestMapping("/pc/playVideo")
    public String playVideo(Model model,int videoNo) throws URISyntaxException {
        String turnPage = "/pc/yun-jxspxx";
        Video video = videoService.findByVideoNo(videoNo);
        video.setThumbnail(staticResourceService.getResource(video.getThumbnail()).toString());
        List<Video> videos = videoService.findByCompleteAndVideoNoNotOrderByVideoNoAsc(video.isComplete(),videoNo);
        List<Video> videoList = new ArrayList<>();
        videoList.add(video);
        for(Video v:videos) {
            v.setThumbnail(staticResourceService.getResource(v.getThumbnail()).toString());
            videoList.add(v);
        }
        model.addAttribute("video",video);
        model.addAttribute("videoList",videoList);
        model.addAttribute("flag","yun-jxspnewone.html");  //此属性用来给前台确定当前是哪个页面
        return turnPage;
    }

}

