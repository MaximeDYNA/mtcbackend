package com.catis;

import com.catis.repository.FilesStorageService;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;


@EnableAsync
@SpringBootApplication
public class MtcApplication implements CommandLineRunner {


    @Bean
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }

    @Resource
    FilesStorageService storageService;

    @Autowired
    private Environment env;

    Logger log = LoggerFactory.getLogger(MtcApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MtcApplication.class, args);
    }

    @Override
    public void run(String... arg) {
        try {
            System.err.println("tetst");
            storageService.init();
        } catch (Exception e) {}
    }

    //@PostConstruct
    public void test(){
        RestTemplate r = new RestTemplate();

        String urlDuFou =
                env
                        .getProperty("control.service.ip");
        Object response = r.postForObject(urlDuFou + 1,null , Object.class);
        System.out.println("Post reussi voici la reponse "+ ToStringBuilder.reflectionToString(response));
    }

    // @Primary
    // @Bean(name = "taskExecutorDefault")
    // public ThreadPoolTaskExecutor taskExecutorDefault() {
    //     ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //     executor.setCorePoolSize(4);
    //     executor.setMaxPoolSize(4);
    //     executor.setQueueCapacity(500);
    //     executor.setRejectedExecutionHandler((r, executor1) -> log.warn("Task rejected, thread pool is full and queue is also full"));
    //     executor.setWaitForTasksToCompleteOnShutdown(true);
    //     executor.setThreadNamePrefix("MainAsync-");
    //     executor.initialize();
    //     return executor;
    // }
    
    // @Bean(name = "taskExecutorForHeavyTasks")
    // public ThreadPoolTaskExecutor taskExecutorRegistration() {
    //     ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //     executor.setCorePoolSize(5);
    //     executor.setMaxPoolSize(10);
    //     executor.setQueueCapacity(25);
    //     executor.setThreadNamePrefix("taskthread-");
    //     executor.setRejectedExecutionHandler((r, executor1) -> log.warn("heavy Task rejected, thread pool is full and queue is also full"));
    //     executor.setWaitForTasksToCompleteOnShutdown(true);
    //      executor.initialize();
    //      return executor;
    //     }
}
