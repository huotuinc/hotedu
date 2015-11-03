package com.huotu.hotedu.web.controller.admin;

import com.huotu.hotedu.common.CommonEnum;
import com.huotu.hotedu.entity.Notice;
import com.huotu.hotedu.entity.Result;
import com.huotu.hotedu.repository.NoticeRepository;
import com.huotu.hotedu.service.NoticeService;
import com.huotu.hotedu.web.model.NoticeModel;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by WenbinChen on 2015/10/28 11:52.
 */
@Controller
public class NoticeController {

    private static final int PAGE_SIZE = 10;

    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    NoticeService noticeService;

    @Autowired
    StaticResourceService staticResourceService;

    /**
     * 前台公告列表
     * @return
     */
    @RequestMapping(value = "/pc/notices",method = RequestMethod.GET)
    public ModelAndView loadNoticeList() {
        List<Notice> noticeList = noticeRepository.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("pc/noticeList");
        modelAndView.addObject("noticeList",noticeList);
        return modelAndView;
    }

    /**
     * 后台显示公告列表页面
     * @return
     */
    @RequestMapping(value = "/backend/notices",method = RequestMethod.GET)
    public ModelAndView showNoticeList(Integer pageNo) {
        if(pageNo==null||pageNo<1) {
            pageNo = 1;
        }
        Page<Notice> noticeList = noticeService.getPage(pageNo-1,PAGE_SIZE,null);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("backend/noticeList");
        modelAndView.addObject("currentPage",pageNo);
        modelAndView.addObject("totalPages",noticeList.getTotalPages());
        modelAndView.addObject("totalRecords",noticeList.getTotalElements());
        modelAndView.addObject("noticeList",noticeList);
        return  modelAndView;
    }

    @RequestMapping("/backend/addNotice")
    public ModelAndView showAddNoticePage() {
        ModelAndView modelAndView  = new ModelAndView();
        modelAndView.setViewName("backend/newNoticePage");
        return modelAndView;
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

    @RequestMapping(value = "/backend/notices",method = RequestMethod.POST)
    public ModelAndView saveNotice(MultipartFile pic,Notice notice) throws Exception{
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/backend/notices");
        if(pic.getSize()!=0) {
            //取得扩展名
            String fileExt = pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
            String fileName = StaticResourceService.NOTICE_IMG + UUID.randomUUID().toString() + "."+fileExt;
            staticResourceService.uploadResource(fileName, pic.getInputStream());
            notice.setPicUrl(fileName);
        }
        notice.setLastUpdateTime(new Date());
        notice.setType(CommonEnum.NoticeType.Course);
        notice.setEnabled(false);
        noticeRepository.save(notice);
        return  modelAndView;
    }

    /**
     * 修改公告
     * @param pic
     * @param noticeId
     * @param linkUrl
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/backend/notices/{noticeId}",method = RequestMethod.POST)
    @Transactional
    public ModelAndView modifyNotice(MultipartFile pic,@PathVariable long noticeId,String linkUrl,String title,String synopsis,String content) throws Exception {
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
        notice.setTitle(title);
        notice.setSynopsis(synopsis);
        notice.setContent(content);
        notice.setLinkUrl(linkUrl);
        noticeRepository.save(notice);
        return modelAndView;
    }

    @RequestMapping(value = "/backend/changeNoticeStatus",method = RequestMethod.POST,consumes="application/json")
    @ResponseBody
    @Transactional
    public Result changeNoticeStatus(@RequestBody NoticeModel noticeModel) {
        Notice notice = noticeRepository.findOne(noticeModel.getNoticeId());
        notice.setEnabled(noticeModel.isEnabled());
        noticeRepository.save(notice);
        Result result = new Result();
        result.setStatus(1);
        return  result;
    }

}
