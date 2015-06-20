package com.huotu.hotedu.config;

import org.luffy.lib.libspring.config.LibJpaConfig;
import org.luffy.lib.libspring.data.ClassicsRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Configuration
@DependsOn("entityManagerFactory")
@EnableJpaRepositories(value="com.huotu.hotedu.repository",repositoryFactoryBeanClass = ClassicsRepositoryFactoryBean.class)
@EnableTransactionManagement
public class JpaConfig extends LibJpaConfig {
}
