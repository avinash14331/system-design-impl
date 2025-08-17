package org.avi.javaairflowlite.config;


import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class QuartzSpringConfig {
    private final ApplicationContext ctx;
    public QuartzSpringConfig(ApplicationContext ctx) { this.ctx = ctx; }

    @Bean
    @Primary
    public Scheduler scheduler() throws SchedulerException {
        var factory = new StdSchedulerFactory();
        Scheduler s = factory.getScheduler();
        s.getContext().put("applicationContext", ctx);
        s.start();
        return s;
    }
}
