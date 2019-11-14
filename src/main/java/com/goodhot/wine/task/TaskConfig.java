package com.goodhot.wine.task;

import com.goodhot.wine.proxy.HttpProxyTask;
import com.goodhot.wine.subscribe.SubscribeTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;

@Configuration
public class TaskConfig {

    @Autowired
    private SubscribeTask subscribeService;

    @Autowired
    private HttpProxyTask httpProxyTask;

    @Scheduled(cron = "0/5 * * * * ?")
    public void schedule() throws Exception {
        try {
            subscribeService.task();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
