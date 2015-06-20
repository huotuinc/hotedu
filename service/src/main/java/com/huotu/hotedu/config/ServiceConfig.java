package com.huotu.hotedu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Configuration
@DependsOn("entityManagerFactory")
@ComponentScan({"com.huotu.hotedu.service"})
@EnableTransactionManagement
public class ServiceConfig {

    /**
     * 可以替换为其他Password
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
