package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shiliting on 2015/6/10.
 * 师资力量有关的Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class VideoController {
    //后台显示所有视频信息
    @RequestMapping("/backend/load/video")
    public String loadWantedesController() {
        return "/backend/video";
    }

    //后台显示检索之后的视频信息
    @RequestMapping("/backend/search/video")
    public String searchVideosController() {
        return "";
    }

}
