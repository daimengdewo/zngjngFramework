package com.awen.shiro.config.shiro;

import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        /*
         * filter配置规则参考官网
         * http://shiro.apache.org/web.html#urls-
         * 默认过滤器对照表
         * https://shiro.apache.org/web.html#default-filters
         */

        Map<String, String> filterRuleMap = new HashMap<>();

        filterRuleMap.put("/**", "anon");
        //↑配置不参与验证的映射路径。

        factoryBean.setGlobalFilters(Collections.singletonList("noSessionCreation"));
        //↑ 关键：全局配置NoSessionCreationFilter，把整个项目切换成无状态服务。

        factoryBean.setSecurityManager(securityManager);
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);
        return factoryBean;
    }

    @Bean
    protected Authorizer authorizer() {
        return new ModularRealmAuthorizer();
    }

}
