package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.*;
import com.huotu.hotedu.repository.LinkRepository;
import com.huotu.hotedu.repository.NoticeRepository;
import com.huotu.hotedu.repository.SEOConfigRepository;
import com.huotu.hotedu.service.ExamGuideService;
import com.huotu.hotedu.service.MessageContentService;
import com.huotu.hotedu.service.NoticeService;
import com.huotu.hotedu.service.QaService;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by shiliting on 2015/6/10.
 * 进入后台首页的Controller
 * @author shiliting741@163.com
 */
@Controller
public class IndexController {


    @Autowired
    StaticResourceService staticResourceService;

    @Autowired
    MessageContentService messageContentService;
    @Autowired
    LinkRepository linkRepository;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    NoticeService noticeService;
    @Autowired
    QaService qaService;
    @Autowired
    ExamGuideService examGuideService;
    @Autowired
    SEOConfigRepository seoConfigRepository;

    /**
     * 前台首页控制器
     * <ul>
     *     <li>该请求可以被匿名访问。</li>
     *     <li>model中包含learned 类型为Long 定义为参与免费课程学习的人数</li>
     *     <li>model中包含completeLearned 类型为Long 定义为参与收费课程学习的人数</li>
     *     <li>model中包含enterprise 类型为Long 定义为企业数量</li>
     *     <li>model中包含recruitment 类型为Long 定义为企业招聘数量</li>
     *     <li>model中包含wxfollowers 类型为Long 定义为微信公众号关注者</li>
     *     <li>model中包含qa1 类型为Qa 定义为首个常见问题</li>
     *     <li>model中包含qa2 类型为Qa 定义为第二常见问题</li>
     *     <li>model中包含otherQas 类型为List&lt;Qa&gt; 定义为其他需要展示的问题</li>
     *     <li>model中包含information1 类型为Information 定义为首个动态资讯</li>
     *     <li>model中包含otherInformations 类型为List&lt;Information&gt; 定义为其他需要展示的动态资讯</li>
     *
     * </ul>
     * @return 首页视图
     */
    public String index(){
        throw new NoSuchMethodError("尚未实现");
    }

    /**
     * 测试用例
     * @param who 登录用户
     * @return  用户名
     */
    @RequestMapping("/sayMyname")
    @ResponseBody
    public String sayMyname(@AuthenticationPrincipal Login who){
        return who.getLoginName();
    }

    /**
     * 显示后台主页信息
     * @return
     */
    @RequestMapping("/backend/index")
    public String loadIndex(){
        return "/backend/index";
    }

    /**
     * 显示网站主页
     * @return
     */
    @RequestMapping("/")
    public String loadWeb() {
        return "redirect:/pc/index";
    }

    @RequestMapping("/pc/index")
    public String index(Model model) throws Exception{
        String turnPage = "pc/yun-index";
        //SEO配置
        List<SEOConfig> seoConfigs = seoConfigRepository.findAll();
        SEOConfig seoConfig = null;
        if(seoConfigs.size()==0) {
            seoConfig = new SEOConfig();
            seoConfig.setTitle("赢在微商，连接一切，全国首批微商运营师证书-伙聚教育云商学院");
            seoConfig.setKeywords("微商培训,微信营销课程,微信营销技巧,赢在微商,伙聚教育,火图科技,云商学院");
            seoConfig.setDescription("赢在微商，连接一切。伙聚教育云商学院是国内首家获得工信部微商运营师证书的微商培训学院。" +
                    "我们提供整套移动营销解决方案，我们只做最专业的学习平台、最系统的微商培训、最权威的考证服务！一次缴费，免费复训；一证在手，工作不愁！");
        }else {
            seoConfig = seoConfigs.get(0);
        }
        model.addAttribute("seo",seoConfig);

        //最新公告
        List<Notice> noticeList = noticeService.getPage(0,3,false).getContent();
        for(Notice notice:noticeList) {
            if(notice.getPicUrl()!=null) {
                notice.setPicUrl(staticResourceService.getResource(notice.getPicUrl()).toString());
            }
        }
        model.addAttribute("noticeList",noticeList);
        //浮动广告
        List<Notice> notices = noticeRepository.findByEnabled(true);
        Notice notice = null;
        if(notices.size()>0) {
            notice = notices.get(0);
            if(notice.getPicUrl()!=null) {
                notice.setPicUrl(staticResourceService.getResource(notice.getPicUrl()).toString());
            }
        }
        model.addAttribute("notice",notice);

        //友情链接
        List<Link> linkList=linkRepository.findAll();
        if(linkList.size()>0) {
            model.addAttribute("linkList", linkList);
        }
        //咨询动态
        List<MessageContent> messageContentList=messageContentService.loadPcMessageContent(0,4).getContent();
        for(MessageContent mc:messageContentList){
            mc.setPictureUri(staticResourceService.getResource(mc.getPictureUri()).toString());
        }
        model.addAttribute("messageContentList",messageContentList);
        //常见问题
        List<Qa> qaList=qaService.loadPcQa(0,3).getContent();
        for(Qa qa:qaList){
            qa.setPictureUri(staticResourceService.getResource(qa.getPictureUri()).toString());
        }
        model.addAttribute("qaList",qaList);
        //考试指南
       /* List<ExamGuide> examGuideList=examGuideService.loadPcExamGuide(0,3).getContent();
        for(ExamGuide eg:examGuideList){
            eg.setPictureUri(staticResourceService.getResource(eg.getPictureUri()).toString());
        }
        model.addAttribute("examGuideList",examGuideList);*/
        model.addAttribute("flag","yun-index.html");  //此属性用来给前台确定当前是哪个页面
        return turnPage;
    }

}
