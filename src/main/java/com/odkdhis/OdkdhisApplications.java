package com.odkdhis;

import com.odkdhis.util.ODKErrorHandler;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
@ComponentScan
@EnableAutoConfiguration

public class OdkdhisApplications extends SpringBootServletInitializer implements SchedulingConfigurer{

    public static void main(String[] args) {
        SpringApplication.run(OdkdhisApplications.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(OdkdhisApplications.class);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        //RestTemplate resttemp = builder.setConnectTimeout(Duration.ofMillis(10000)).setReadTimeout(Duration.ofMillis(10000)).build();
        RestTemplate resttemp = builder.build();
        resttemp.setErrorHandler(new ODKErrorHandler());
        return resttemp;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
       taskRegistrar.setScheduler(taskExecutor());
    }
    
     @Bean(destroyMethod = "shutdownNow")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(100);
    }
}
