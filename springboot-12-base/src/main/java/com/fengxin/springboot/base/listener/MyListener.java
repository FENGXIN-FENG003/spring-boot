package com.fengxin.springboot.base.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.Duration;

/**
 * @author FENGXIN
 * @date 2024/8/31
 * @project springboot-part
 * @description
 **/
@Slf4j
public class MyListener implements SpringApplicationRunListener {
    @Override
    public void starting (ConfigurableBootstrapContext bootstrapContext) {
        log.info ("starting");
    }
    
    @Override
    public void environmentPrepared (ConfigurableBootstrapContext bootstrapContext , ConfigurableEnvironment environment) {
        log.info ("environmentPrepared");
    }
    
    @Override
    public void contextPrepared (ConfigurableApplicationContext context) {
        log.info ("contextPrepared");
    }
    
    @Override
    public void contextLoaded (ConfigurableApplicationContext context) {
        log.info ("contextLoaded");
    }
    
    @Override
    public void started (ConfigurableApplicationContext context , Duration timeTaken) {
        log.info ("started");
    }
    
    @Override
    public void ready (ConfigurableApplicationContext context , Duration timeTaken) {
        log.info ("ready");
    }
    
    @Override
    public void failed (ConfigurableApplicationContext context , Throwable exception) {
        log.info ("failed");
    }
}
