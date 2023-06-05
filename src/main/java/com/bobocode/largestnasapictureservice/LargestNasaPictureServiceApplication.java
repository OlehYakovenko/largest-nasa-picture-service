package com.bobocode.largestnasapictureservice;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

@Log4j2
@SpringBootApplication
@EnableCaching
@EnableScheduling
public class LargestNasaPictureServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LargestNasaPictureServiceApplication.class, args);
    }

    @Scheduled(fixedDelay = 10*1000) // method is called every 10 second for clear cache
    @CacheEvict(value = "picture", allEntries = true)
    public void clearCache(){
        log.warn("Clearing NASA pictures cache");
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

}
