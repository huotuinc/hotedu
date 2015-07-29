package com.huotu.hotedu.web.service;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.Huotu;
import com.huotu.hotedu.entity.Manager;
import com.huotu.hotedu.repository.*;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.service.EnterpriseService;
import com.huotu.hotedu.service.LoginService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author CJ
 */
@Service
public class AppService implements ApplicationListener<ContextRefreshedEvent> {

    private static final Log log = LogFactory.getLog(AppService.class);

    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private LoginService loginService;
    @Autowired
    private ExamGuideRepository examGuideRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private EnterpriseRepository enterpriseRepository;
    @Autowired
    private HuotuRepository huotuRepository;
    @Autowired
    private QaRepository qaRepository;
    @Autowired
    EnterpriseService enterpriseService;
    @Autowired
    private MessageContentRepository messageContentRepository;
    @Autowired
    private AgentRepository agentRepository;
    @Autowired
    private AgentService agentService;

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (event.getApplicationContext().getParent() == null) {
            log.info("user.dir="+System.getProperty("user.dir"));
            if (managerRepository.count()==0){
                Manager manager = new Manager();
                manager.setLoginName("admin");
                loginService.newLogin(manager,"admin");
                System.out.println("测试用户以添加！");
            }
        }

        if (event.getApplicationContext().getParent() == null) {
            if(huotuRepository.count()==0){
                Huotu huotu=new Huotu();
                huotu.setTitle("杭州火图科技有限公司");
                huotu.setIntroduction("杭州火图科技有限公司成立于2013年6月，是专注于移动互联领域的新型科技公司，目前已完成千万级pre A轮融资，拥有2家全资子公司：水图电子商务和非凡传媒。\n" +
                        "　　火图科技坐落于美丽的天堂城市—杭州，周边集结了诺基亚，大华股份，西门子，阿里巴巴等著名公司。公司目前员工超过100人，其中50%为技术研发人员，核心团队成员主要来自UT斯达康、奇艺、方正、百度等行业资深专家。\n" +
                        "火图科技经过2014年的创新及努力，成功的打造了“伙伴+”移动电商服务平台。伙伴+为品牌企业提供“移动商城搭建服务”，“大数据挖掘分析服务”，“精准广告投放服务”，“企业内容分发服务”。以此帮助企业快速、低成本的拥有移动电商能力。\n" +
                        "2015年1月28日，CEO杨震昊携其团队荣获“新商业合伙人峰会优秀项目奖");
                huotuRepository.save(huotu);
            }
            // 做一些初始化工作 比如
            /*if (examGuideRepository.count()==0){
                ExamGuide examGuide =new ExamGuide();
                examGuide.setContent("examguide");
                examGuide.setTitle("title examguide1");
                examGuide.setLastUploadDate(new Date());
                examGuide.setIsTop(true);
                examGuideRepository.save(examGuide);
                examGuide =new ExamGuide();
                examGuide.setContent("examguide");
                examGuide.setTitle("title examguide2");
                examGuide.setLastUploadDate(new Date());
                examGuide.setIsTop(true);
                examGuideRepository.save(examGuide);
                examGuide =new ExamGuide();
                examGuide.setContent("examguide");
                examGuide.setTitle("title examguide3");
                examGuide.setLastUploadDate(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
                examGuide.setIsTop(true);
                examGuideRepository.save(examGuide);
                examGuide =new ExamGuide();
                examGuide.setContent("examguide");
                examGuide.setTitle("title examguide4");
                examGuide.setLastUploadDate(new Date(System.currentTimeMillis() - 5 * 60 * 60 * 1000));
                examGuide.setIsTop(true);
                examGuideRepository.save(examGuide);
                Random r=new Random();
                for(int i=0;i<34;i++){
                    examGuide=new ExamGuide();
                    examGuide.setTitle(String.valueOf(r.nextInt()*20));
                    examGuide.setContent(String.valueOf(r.nextDouble()));
                    examGuide.setLastUploadDate(new Date(System.currentTimeMillis()+i*100000));
                    examGuide.setIsTop(i % 3 == 0 ? true : false);
                    examGuideRepository.save(examGuide);
                }
                examGuide =new ExamGuide();
                examGuide.setContent("中文测试标题");
                examGuide.setTitle("中文测试内容");
                examGuide.setLastUploadDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 2000));
                examGuide.setIsTop(true);
                examGuideRepository.save(examGuide);
                /// do something
            }*/
          /*if(linkRepository.count()==0){
              Link link=new Link();
              link.setTitle("spring");
              link.setUrl("spring.io");
              link.setLastUploadDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 2000));
              linkRepository.save(link);
              link=new Link();
              link.setTitle("百度");
              link.setLastUploadDate(new Date(System.currentTimeMillis() + 24 * 60 * 60));
              link.setUrl("www.baidu.com");
              linkRepository.save(link);
              link=new Link();
              link.setTitle("粉猫");
              link.setUrl("www.fanmore.cn");
              link.setLastUploadDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 6000));
              linkRepository.save(link);
          }*/

            /*if(tutorRepository.count()==0){
                Tutor tutor=new Tutor();
                tutor.setName("诸葛亮");
                tutor.setIntroduction("军师");
                tutor.setArea("杭州");
                tutor.setQualification("博士");
                tutor.setPictureUri("D://");
                tutor.setLastUploadDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 2000));
                tutorRepository.save(tutor);
                tutor.setName("刘备");
                tutor.setIntroduction("主公");
                tutor.setArea("杭州");
                tutor.setQualification("硕士");
                tutor.setPictureUri("D://");
                tutor.setLastUploadDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 2000));
                tutorRepository.save(tutor);
            }*/

            /*if(messageContentRepository.count()==0){
                MessageContent messageContent;
                for(int i=0;i<5;i++){
                    messageContent=new MessageContent();
                    messageContent.setTitle("messageContent"+i);
                    messageContent.setContent("content message Content"+i);
                    messageContent.setLastUploadDate(new Date(System.currentTimeMillis()+24*60*60*1000*(i+1)));
                    messageContentRepository.save(messageContent);
                }
            }*/

            /*if (tutorRepository.count()==0){
                Tutor tutor;
                for(int i=0;i<27;i++){
                    tutor=new Tutor();
                    tutor.setName("tutor"+i);
                    tutor.setIntroduction("i am a tutor my name is"+i);
                    tutor.setArea("杭州");
                    tutor.setQualification("教授");
                    tutor.setLastUploadDate(new Date(System.currentTimeMillis()+i*60*60*1000));
                    tutor.setPictureUri("slt.jpg");
                    tutorRepository.save(tutor);
                }
            }*/


           /* if (qaRepository.count()==0){
                Qa qa;
                for(int i=0;i<4;i++){
                    qa=new Qa();
                    qa.setTitle("qa"+i);
                    qa.setContent("问题"+(i+1));
                    qa.setLastUploadDate(new Date(System.currentTimeMillis()+24*60*60*1000*(i+1)));
                    qa.setTop(true);
                    qaRepository.save(qa);
                }
            }*/
//            if(memberRepository.count()==4){
//                Random random=new Random();
//                int sum=(int)agentRepository.count();
//                List<Agent> lists=agentRepository.findAll();
//
//                Member member;
//                for(int i=0;i<sum*20;i++){
//                    Agent agent=lists.get(0);
//                    member=new Member();
//                    member.setAgent(agent);
//                    member.setRealName("ceshi"+i);
//                    member.setPayed(random.nextBoolean());
//                    member.setPassed(random.nextBoolean());
//                    member.setRegisterDate(new Date());
//                    memberRepository.save(member);
//
//                }
//            }




            if(agentRepository.count()==0){
                Agent agent=new Agent();
                Date d = new Date();
                agent.setName("火图科技");
                agent.setLoginName("hotedu");
                agent.setArea("杭州");
                agent.setAreaId("001");
                agent.setCertificateNumber(600);
                agent.setSendCertificateNumber(0);
                agent.setLevel("一级");
                agent.setRegisterDate(d);
                loginService.newLogin(agent,"123456");
            }

        }





    }






}
