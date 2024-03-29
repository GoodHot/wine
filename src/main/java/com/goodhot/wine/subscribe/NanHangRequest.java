package com.goodhot.wine.subscribe;

import com.github.kevinsawicki.http.HttpRequest;
import com.goodhot.wine.fateadm.Api;
import com.goodhot.wine.fateadm.Util;
import com.goodhot.wine.proxy.HttpProxyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class NanHangRequest {
    // 网址
    public static final String NAN_HANG_URL = "http://kwemobile.bceapp.com/maotai.php";
    public static final String NAN_HANG_VERIFY_URL = "http://kwemobile.bceapp.com/maotai.php/index/verify";
    public static final String NAN_HANG_SUBSCRIBE_URL = "http://kwemobile.bceapp.com/maotai.php/index/addenroll.html";

    // Header名
    public static final String USER_AGENT_NAME = "User-Agent";
    public static final String USER_AGENT_VALUE = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1";

    public static final String CONTENT_TYPE_NAME = "Content-Type";
    public static final String CONTENT_TYPE_VALUE_X_WWW_FORM = "application/x-www-form-urlencoded";

    public static final String SET_COOKIE_NAME = "Set-Cookie";
    public static final String COOKIE_NAME = "Cookie";
    public static final String COOKIE_PHPSESSID_NAME = "PHPSESSID";
    public static final String COOKIE_MAOTAI_NAME = "maotai";


    @Autowired
    private Api fateadmApi;

    /**
     * 网页两个cookie之一
     */
    private String cookiePHPSESSID;

    /**
     * 网页两个cookie之二
     */
    private String cookieMaotai;

    /**
     * 获取网页
     *
     * @return
     * @throws IOException
     */
    public HttpRequest getPage(String url, HttpProxyServer proxyServer) throws IOException {
        HttpRequest req = HttpRequest.get(url);
        setProxyServer(req, proxyServer);
        req.header(USER_AGENT_NAME, USER_AGENT_VALUE);
        setCookieMaotai(req);
        return req;
    }

    /**
     * 获取cookie maotai
     *
     * @param req
     */
    public void setCookieMaotai(HttpRequest req) {
        this.cookieMaotai = req.header(SET_COOKIE_NAME).split(";")[0].split("=")[1];
        System.out.println("cookie maotai: " + this.cookieMaotai);
    }

    public String getCookieMaotai() throws Exception {
        if (StringUtils.isEmpty(this.cookieMaotai)) {
            throw new Exception("cookieMaotai为空");
        }
        return this.cookieMaotai;
    }

    /**
     * 获取cookie phpsession
     *
     * @param req
     */
    public void setCookiePHPSESSID(HttpRequest req) {
        this.cookiePHPSESSID = req.header(SET_COOKIE_NAME).split(";")[0].split("=")[1];
        System.out.println("coolie phpsession: " + this.cookiePHPSESSID);
    }

    /**
     * 提交预约请求
     *
     * @param formData
     */
    public void postSubscribe(Map<String, String> formData, HttpProxyServer proxy) throws Exception {
        if (StringUtils.isEmpty(cookiePHPSESSID)) {
            throw new Exception("cookiePhpSession为空");
        }
        if (StringUtils.isEmpty(cookieMaotai)) {
            throw new Exception("cookieMaotai为空");
        }

        final String ILLEGAL = "非法访问";
        final String OUTTIME_CAPTCHA = "验证码错误"; // 可能不存在了
        final String ALREADY_DONE = "该票号已经预约过";
        final String NO_EMPTY = "该日期预约数量已满";
        final String OK = "预约成功";

        HttpRequest req = HttpRequest.post(NAN_HANG_SUBSCRIBE_URL);
        setProxyServer(req, proxy);
        req.header(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE_X_WWW_FORM)
                .header(COOKIE_NAME,
                        COOKIE_PHPSESSID_NAME + "=" + cookiePHPSESSID + " ;" + COOKIE_MAOTAI_NAME + "=" + cookieMaotai)
                .form(formData);
        String body = req.body();
        System.out.println("Response body: " + body);
        if (body.contains(ILLEGAL) || body.contains(OUTTIME_CAPTCHA)) {
            System.out.println(ILLEGAL +
                    " cookiePHPSESSID：" + cookiePHPSESSID +
                    " cookieMaotai：" + cookieMaotai +
                    " - " + formData);
            // 刷新
            // cookie maotai -> 验证码 -> 打码平台 -> 提交
        } else if (body.contains(ALREADY_DONE)) {
            System.out.println(ALREADY_DONE +
                    " cookiePHPSESSID：" + cookiePHPSESSID +
                    " cookieMaotai：" + cookieMaotai +
                    " - " + formData);
        } else if (body.contains(NO_EMPTY)) {
            System.out.println(NO_EMPTY +
                    " cookiePHPSESSID：" + cookiePHPSESSID +
                    " cookieMaotai：" + cookieMaotai +
                    " - " + formData);
        } else {
            System.out.println(OK +
                    " cookiePHPSESSID：" + cookiePHPSESSID +
                    " cookieMaotai：" + cookieMaotai +
                    " - " + formData);
        }
    }

    /**
     * 获取验证码
     *
     * @param cookieMaotai
     * @return
     */
    public String getCaptcha(String cookieMaotai, HttpProxyServer proxyServer) throws Exception {
        HttpRequest req = HttpRequest.get(NAN_HANG_VERIFY_URL);
        setProxyServer(req, proxyServer);
        req.header(USER_AGENT_NAME, USER_AGENT_VALUE)
                .header(COOKIE_NAME, cookieMaotai);
        setCookiePHPSESSID(req);

        ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();
        BufferedInputStream is = req.buffer();
        byte[] buffer = new byte[1024];
        int buf = 0;
        while (-1 != (buf = is.read(buffer))) {
            bufferOut.write(buffer, 0, buf);
        }
        is.close();
        bufferOut.close();
        byte[] response = bufferOut.toByteArray();

        // 10500 代表 5位数字验证码
        Util.HttpResp rst = fateadmApi.Predict("40500", response);
        if (rst.ret_code != 0) {
            throw new Exception(rst.err_msg);
        }
        System.out.println("验证码：" + rst.pred_resl);
        return rst.pred_resl;
    }

    private void setProxyServer(HttpRequest req, HttpProxyServer proxyServer) {
        if (proxyServer != null) {
            req.useProxy(proxyServer.getHost(), Integer.parseInt(proxyServer.getPort()));
            if (proxyServer.isNeedAuth()) {
                req.proxyBasic(proxyServer.getUsername(), proxyServer.getPassword());
            }
        }
    }

    private void setCommonHeader(HttpRequest req) {
        req.header(USER_AGENT_NAME, USER_AGENT_VALUE)
                .header("Accept", "*/*")
                .header("Cache-Control", "no-cache")
                .header("Host", "no-cache")
        ;
    }
}
