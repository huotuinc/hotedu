package com.huotu.hotedu.web.controller.admin;

import com.huotu.hotedu.entity.Notice;
import com.huotu.hotedu.entity.Result;
import com.huotu.hotedu.repository.NoticeRepository;
import com.huotu.hotedu.web.model.NoticeModel;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;


/**
 * Created by WenbinChen on 2015/10/28 11:52.
 */
@Controller
public class NoticeController {

    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    StaticResourceService staticResourceService;

    /**
     * 显示公告列表页面
     * @return
     */
    @RequestMapping(value = "/backend/notices",method = RequestMethod.GET)
    public ModelAndView showNoticeList() {
        List<Notice> noticeList = noticeRepository.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("backend/notice");
        modelAndView.addObject("noticeList",noticeList);
        return  modelAndView;
    }

    /**
     * 根据ID加载公告修改页面
     * @param noticeId
     * @return
     */
    @RequestMapping(value = "/backend/notices/{noticeId}",method = RequestMethod.GET)
    public ModelAndView showNoticeDetail(@PathVariable long noticeId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("backend/noticeEditPage");
        Notice notice = noticeRepository.findOne(noticeId);
        modelAndView.addObject("notice",notice);
        return modelAndView;
    }

    /**
     * 修改公告图片和链接地址
     * @param pic
     * @param noticeId
     * @param linkUrl
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/backend/notices/{noticeId}",method = RequestMethod.POST)
    @Transactional
    public ModelAndView modifyNotice(MultipartFile pic,@PathVariable long noticeId,String linkUrl) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/backend/notices");
        Notice notice = noticeRepository.findOne(noticeId);
        if(pic.getSize()!=0){
            if(ImageIO.read(pic.getInputStream())==null){
                throw new Exception("不是图片！");
            }
            if(notice.getPicUrl()!=null&&!"".equals(notice.getLinkUrl())) {
                staticResourceService.deleteResource(staticResourceService.getResource(notice.getPicUrl()));
            }
            String fileName = StaticResourceService.NOTICE_IMG + UUID.randomUUID().toString() + ".png";
            staticResourceService.uploadResource(fileName, pic.getInputStream());
            notice.setPicUrl(fileName);
        }
        notice.setLinkUrl(linkUrl);
        noticeRepository.save(notice);
        return modelAndView;
    }

    @RequestMapping(value = "/backend/changeNoticeStatus",method = RequestMethod.POST,consumes="application/json")
    @ResponseBody
    @Transactional
    public Result changeNoticeStatus(@RequestBody NoticeModel noticeModel) {
        Notice notice = noticeRepository.findOne(Long.parseLong(noticeModel.getNoticeId()));
        notice.setEnabled("1".equals(noticeModel.getEnabled())?true:false);
        noticeRepository.save(notice);
        Result result = new Result();
        result.setStatus(1);
        return  result;
    }

}