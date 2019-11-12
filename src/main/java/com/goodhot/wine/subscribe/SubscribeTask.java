package com.goodhot.wine.subscribe;

import com.github.kevinsawicki.http.HttpRequest;
import com.goodhot.wine.proxy.HttpProxyTask;
import com.goodhot.wine.proxy.HttpServer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SubscribeTask {

    @Autowired
    private HttpProxyTask httpProxyTask;

    public void task() throws IOException {
        // 获取代理服务器
        HttpServer proxyServer = httpProxyTask.getOne();
        System.out.println("================开始获取数据 " + proxyServer + "================");
        // http://kwemobile.bceapp.com/maotai.php
        HttpRequest.proxyHost(proxyServer.getHost());
        HttpRequest.proxyPort(Integer.parseInt(proxyServer.getPort()));
        HttpRequest req =
                HttpRequest.get(NanHangRequest.NAN_HANG_URL)
                        .header(NanHangRequest.USER_AGENT_NAME, NanHangRequest.USER_AGENT_VALUE);
        String body = req.body();
        Document doc = Jsoup.parse(body);
        Elements result = doc.select(".pet_hd_con_gp_list");
        Element sub = result.get(1);
        Elements span = sub.getElementsByTag("span");
        for (int i = 0; i < span.size(); i++) {
            Element content = span.get(i);
            String status = content.text();
            if (status.contains("可预约")) {
                // TODO: 2019-11-11 0011 发送短信
                System.out.println("\t\t" + status);
            } else {
                System.out.println(status);
            }
        }
        System.out.println("================获取数据结束================");
    }

}
