package com.goodhot.wine.subscribe;

import com.github.kevinsawicki.http.HttpRequest;
import com.goodhot.wine.proxy.HttpProxyServer;
import com.goodhot.wine.proxy.HttpProxyTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SubscribeTask {

    @Autowired
    private HttpProxyTask httpProxyTask;

    @Autowired
    private NanHangRequest nanHangRequest;

    @Autowired
    private CustomerUtil customerUtil;

    public void task() throws Exception {
        // 获取代理服务器
        HttpProxyServer proxyServer = httpProxyTask.getOne();
        // 初始化用户信息
        // TODO: 2019-11-13 读取文件获取用户信息
        customerUtil.init(CustomerData.mockCustomerDate());
        System.out.println("================开始获取数据 " + proxyServer + "================");
        // HttpRequest.proxyHost(proxyServer.getHost());
        // HttpRequest.proxyPort(Integer.parseInt(proxyServer.getPort()));
        HttpRequest nanHangReq = nanHangRequest.getPage(NanHangRequest.NAN_HANG_URL, proxyServer);
        String body = nanHangReq.body();
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
                // 例：11月12日 上午
                String date = status.replace("可预约", "");
                if (customerUtil.whetherMatchDateCustomer(date)) {
                    // TODO: 2019-11-13 多线程
                    for (CustomerData.Customer c : customerUtil.listMatchDate(date)) {
                        HttpProxyServer proxy = httpProxyTask.getOne();
                        c.setCode(nanHangRequest.getCaptcha(nanHangRequest.getCookieMaotai(), proxy));
                        nanHangRequest.postSubscribe(c.toMap(), proxy);
                    }
                }

            } else {
                System.out.println(status);
            }
        }
        System.out.println("================获取数据结束================");
    }

}
