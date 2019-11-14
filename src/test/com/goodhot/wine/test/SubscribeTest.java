package com.goodhot.wine.test;

import com.goodhot.wine.proxy.HttpProxyServer;
import com.goodhot.wine.proxy.HttpProxyTask;
import com.goodhot.wine.subscribe.CustomerData;
import com.goodhot.wine.subscribe.CustomerUtil;
import com.goodhot.wine.subscribe.NanHangRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscribeTest {
    @Autowired
    private HttpProxyTask httpProxyTask;

    @Autowired
    private NanHangRequest nanHangRequest;

    @Autowired
    private CustomerUtil customerUtil;

    @Test
    public void test() throws Exception {
        HttpProxyServer proxy = httpProxyTask.getOne();
        nanHangRequest.getPage(NanHangRequest.NAN_HANG_URL, proxy);
        customerUtil.init(CustomerData.mockCustomerDate());
        for (CustomerData.Customer c : customerUtil.listMatchDate("11月15日 上午")) {
            c.setCode(nanHangRequest.getCaptcha(nanHangRequest.getCookieMaotai(), proxy));
            nanHangRequest.postSubscribe(c.toMap("11月15日 上午"), proxy);
        }
    }
}
