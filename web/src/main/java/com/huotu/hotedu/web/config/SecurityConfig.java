package com.huotu.hotedu.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Configuration
//@DependsOn("LoginService")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//, securedEnabled = true, jsr250Enabled = true
public class SecurityConfig {

    /**
     * 登录请求的URI
     */
    public static final String pcLoginURI = "/pc/index";
    public static final String pcLoginSuccessURI = "/pc/loginSuccess";
    public static final String pcLoginFailedURI = "/pc/loginFailed";
    public static final String pcLogoutSuccessURI = "/pc/logoutSuccess";

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;

    // Since MultiSecurityConfig does not extend GlobalMethodSecurityConfiguration and
    // define an AuthenticationManager, it will try using the globally defined
    // AuthenticationManagerBuilder to create one

    // The @Enable*Security annotations create a global AuthenticationManagerBuilder
    // that can optionally be used for creating an AuthenticationManager that is shared
    // The key to using it is to use the @Autowired annotation
    @Autowired
    public void registerSharedAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Configuration
    public static class ClassicWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Autowired
        private Environment env;
        // Since we didn't specify an AuthenticationManager for this class,
        // the global instance is used

        //设置拦截规则
        protected void configure(HttpSecurity http) throws Exception {
            http

                    //确保任何请求应用程序的用户需要通过身份验证
                    .authorizeRequests()
                    .antMatchers(
                            "/backend/css/**",
                            "/backend/images/**",
                            "/backend/fonts/**",
                            "/backend/js/**",
                            "/backend/**.html",
                            "/mobile/**.html",
                            "/mobile/images/**",
                            "/mobile/css/**",
                            "/mobile/js/**",
                            "/pc/css/**",
                            "/pc/images/**",
                            "/pc/fonts/**",
                            "/pc/js/**",
                            "/pc/**.html",
                            "/pc/loadMemberRegister",
                            "/pc/loadExamGuide",
                            "/pc/yun-baomin",
                            "/pc/register",
                            "/pc/baomin",
                            "/pc/loadVideo",
                            "/pc/loadMessageContent",
                            "/pc/loadDetailMessageContent",
                            "/pc/loadTutors",
                            "/pc/loadCompanyIntroduction",
                            "/image/**",
                            "/uploadResources/**",
                            "/pc/checkPhoneNo",
                            "/pc/playVideo",
                            "/pc/sendSMS",
                            "/pc/loadPeixun",
                            "/pc/loadRecruitment",
                            "/pc/logoutSuccess",
                            "/ueditor/**"
                    ).permitAll()   // 允许未登录用户访问静态资源
                    .anyRequest().authenticated()
                    .and()
                            //开启默认登录页面,允许用户进行身份验证和基于表单的登录
                    .csrf().disable()
                    .formLogin()
                    .loginPage(pcLoginURI)
                    .defaultSuccessUrl(pcLoginSuccessURI, true)
                    .failureUrl(pcLoginFailedURI)
                    .permitAll()
                    .and()
                    .logout().logoutSuccessUrl(pcLogoutSuccessURI);
//                    .and()
            //允许用户进行HTTP基本身份验证
//                    .httpBasic();
//            http
//                    .authorizeRequests()
//                    .anyRequest().hasRole("USER")
//                    .and()
//                    .formLogin()
//                    .loginPage("/login")
//                    .permitAll();
        }
    }
}
