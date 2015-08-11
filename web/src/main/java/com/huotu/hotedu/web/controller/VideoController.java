package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Result;
import com.huotu.hotedu.service.VideoService;
import com.huotu.iqiyi.sdk.IqiyiVideoRepository;
import com.huotu.iqiyi.sdk.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    IqiyiVideoRepository iqiyiVideoRepository;

    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE = 5;//每张页面的记录数
    public static final int PAGE_SIZE_F = 21;

    //后台显示所有视频信息
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/loadVideo")
    public String searchVideo(@RequestParam(required = false) Integer pageNo,Model model) throws IOException {
        String turnPage = "/backend/video";
        if (pageNo == null || pageNo < 0) {
            pageNo = 0;
        }
        Page<Video> pages = iqiyiVideoRepository.find(new PageRequest(pageNo, PAGE_SIZE));
        long totalRecords = pages.getTotalElements();
        int numEl = pages.getNumberOfElements();
        if (numEl == 0) {
            pageNo = pages.getTotalPages() - 1;
            if (pageNo < 0) {
                pageNo = 0;
            }
            pages = iqiyiVideoRepository.find(new PageRequest(pageNo, PAGE_SIZE));
            totalRecords = pages.getTotalElements();
        }

        model.addAttribute("AllVideoList", pages);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalRecords", totalRecords);
        return turnPage;
    }

    @RequestMapping("/pc/loadVideo")
    public String loadVideo(@RequestParam(required = false) Integer pageNo, Model model) throws IOException {
        String turnPage = "/pc/yun-jxspnew";
        if (pageNo == null || pageNo < 0) {
            pageNo = 0;
        }
        Page<Video> pages = iqiyiVideoRepository.find(new PageRequest(pageNo, PAGE_SIZE_F));
        long totalRecords = pages.getTotalElements();
        int numEl = pages.getNumberOfElements();
        if (numEl == 0) {
            pageNo = pages.getTotalPages() - 1;
            if (pageNo < 0) {
                pageNo = 0;
            }
            pages = iqiyiVideoRepository.find(new PageRequest(pageNo, PAGE_SIZE_F));
            totalRecords = pages.getTotalElements();
        }
        List<Video> video1 = new ArrayList<>();
        List<Video> videos1 = new ArrayList<>();
        int sum = 0;
        for (Video video : pages) {
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
    @RequestMapping(value = "/backend/addSaveVideo", method = RequestMethod.POST)
    public String addSaveVideo(String videoName, @RequestParam("videoFile") MultipartFile file, String tags, String description) throws IOException {
        iqiyiVideoRepository.upload(file.getInputStream(), file.getSize(), "mov", videoName, description, tags, null);
        return "redirect:/backend/loadVideo";
    }

//    @PreAuthorize("hasRole('EDITOR')")
//    @RequestMapping(value = "/backend/addSaveVideo", method = RequestMethod.POST)
//    @ResponseBody
//    public Result addSaveVideo(String videoName, @RequestParam("videoFile") MultipartFile file, String description, String tags) throws IOException {
//        Result result = new Result();
//        iqiyiVideoRepository.upload(file.getInputStream(), file.getSize(), "mov", videoName, description, tags, null);
//        result.setStatus(1);
//        result.setMessage("视频上传成功！");
//        return result;
//    }

    /**
     * 删除视频
     * @param fileIds
     * @return
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/delVideo")
    @ResponseBody
    public Result delVideo(String fileIds) throws IOException {
        Result result = new Result();
        iqiyiVideoRepository.deleteVideo(1,fileIds);
        result.setStatus(1);
        result.setMessage("删除成功");
        return result;
    }

}

