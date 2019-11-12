package com.goodhot.wine.config;

import com.goodhot.wine.fateadm.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBean {
    @Bean
    public Api fateadmApi() {
        Api api = new Api();
        api.Init("318195",
                "rB/aZfdljpMd6SC9YshPu6PG+8W3tONd",
                "118195",
                "YUP735ZY1EtuQwbufeo+1yt14TIr6EeO");
        return api;
    }
}
