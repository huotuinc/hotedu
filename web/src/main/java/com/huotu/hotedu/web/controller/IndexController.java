package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Login;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;

/**
 * Created by shiliting on 2015/6/10.
 * 进入后台首页的Controller
 * @author shiliting741@163.com
 */
@Controller
public class IndexController {

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
     * 显示主页信息
     * @return
     */
    @RequestMapping("/backend/index")
    public String loadIndex(){
        return "/backend/index";
    }

}
