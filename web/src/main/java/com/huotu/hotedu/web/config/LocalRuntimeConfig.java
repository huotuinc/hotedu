package com.huotu.hotedu.web.config;

import org.luffy.lib.libspring.config.RuntimeConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;

/**
 * Created by luffy on 2015/6/10.
 * 为了允许本地运行整个webapplication而设置的本地专用配置项
 * @author luffy luffy.ja at gmail.com
 */
@Profile("!prod")
@Configuration
public class LocalRuntimeConfig extends RuntimeConfig {
    @Override
    public boolean containerEnv() {
        return false;
    }

    @Override
    public String persistenceUnitName() {
        return "hotedu_deve";
    }

    @Override
    public Class<? extends JpaDialect> dialectClass() {
        return EclipseLinkJpaDialect.class;
    }
}
