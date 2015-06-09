package com.huotu.hotedu.test;

import org.luffy.lib.libspring.config.RuntimeConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaDialect;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Configuration
@ComponentScan({"com.huotu.hotedu.config","com.huotu.hotedu.web.config"})
public class TestWebConfig {
}
