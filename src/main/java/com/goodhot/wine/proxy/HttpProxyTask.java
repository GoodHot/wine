package com.goodhot.wine.proxy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
public class HttpProxyTask {

    private static BlockingQueue<HttpProxyServer> PROXY_QUEUE = new LinkedBlockingQueue<>();

    public void getProxy() throws IOException {
        // https://www.xicidaili.com/wt/
        System.out.println("抓取代理服务器");
        Document doc = Jsoup.connect("https://www.xicidaili.com/wt/").get();
        Element ipList = doc.getElementById("ip_list");
        Elements trs = ipList.getElementsByTag("tr");
        for (int i = 1; i < trs.size(); i++) {
            Element elem = trs.get(i);
            Elements data = elem.getElementsByTag("td");
            Element host = data.get(1);
            Element port = data.get(2);
            // TODO: 2019-11-13 0013 查询代理服务器用户密码
            HttpProxyServer server = new HttpProxyServer(host.html(), port.html(), false, null, null);
            System.out.println("获取代理服务器：" + server);
            PROXY_QUEUE.add(server);
        }
    }

    public HttpProxyServer getOne() throws IOException {
        HttpProxyServer server = null;
        try {
            server = PROXY_QUEUE.poll(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (server == null) {
            // 代理池为空，开始获取数据
            this.getProxy();
            server = this.getOne();
        }
        return server;
    }

}
