package com.huotu.hotedu.config;

import org.luffy.lib.libspring.config.LibJpaConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Configuration
@DependsOn("entityManagerFactory")
@EnableJpaRepositories("com.huotu.hotedu.repository")
public class JpaConfig extends LibJpaConfig {
}
