package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shiliting on 2015/6/10.
 * 师资力量有关的Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class VideosController {
    @RequestMapping("/backend/load/videos")
    public String loadWantedesController() {
        return "/backend/videos";

    }

}
