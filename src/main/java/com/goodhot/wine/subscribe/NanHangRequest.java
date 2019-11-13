package com.goodhot.wine.subscribe;

import com.github.kevinsawicki.http.HttpRequest;
import com.goodhot.wine.fateadm.Api;
import com.goodhot.wine.fateadm.Util;
import com.goodhot.wine.proxy.HttpProxyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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


    @Autowired
    private Api fateadmApi;

    /**
     * 网页两个cookie之一
     */
    private String cookiePhpSession;

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
        HttpRequest req =
                HttpRequest.get(url)
                        .header(USER_AGENT_NAME, USER_AGENT_VALUE);
        setProxyServer(req, proxyServer);
        setCookieMaotai(req, url);
        setCookiePhpSession(req, url);
        return req;
    }

    /**
     * 获取cookie maotai
     *
     * @param req
     * @param url
     */
    public void setCookieMaotai(HttpRequest req, String url) {
        if (!url.equals(NAN_HANG_URL)) {
            return;
        }
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
     * @param url
     */
    public void setCookiePhpSession(HttpRequest req, String url) {
        if (!url.equals(NAN_HANG_VERIFY_URL)) {
            return;
        }
        this.cookiePhpSession = req.header(SET_COOKIE_NAME).split(";")[0].split("=")[1];
        System.out.println("coolie phpsession: " + this.cookiePhpSession);
    }

    /**
     * 提交预约请求
     *
     * @param formData
     */
    public void postSubscribe(Map<String, String> formData, HttpProxyServer proxy) throws Exception {
        if (StringUtils.isEmpty(cookiePhpSession)) {
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

        HttpRequest req = HttpRequest.post(NAN_HANG_SUBSCRIBE_URL)
                .header(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE_X_WWW_FORM)
                .header(COOKIE_NAME, cookiePhpSession + " ;" + cookieMaotai)
                .form(formData);
        setProxyServer(req, proxy);
        String body = req.body();
        if (body.contains(ILLEGAL) || body.contains(OUTTIME_CAPTCHA)) {
            System.out.println(ILLEGAL + " " + formData);
            // 刷新
            // cookie maotai -> 验证码 -> 打码平台 -> 提交
        } else if (body.contains(ALREADY_DONE)) {
            System.out.println(ALREADY_DONE + " " + formData);
        } else if (body.contains(NO_EMPTY)) {
            System.out.println(NO_EMPTY + " " + formData);
        } else {
            System.out.println(OK + " " + formData);
        }
    }

    /**
     * 获取验证码
     *
     * @param cookieMaotai
     * @return
     */
    public String getCaptcha(String cookieMaotai, HttpProxyServer proxyServer) throws Exception {
        HttpRequest req = HttpRequest.get(NAN_HANG_VERIFY_URL)
                .header(USER_AGENT_NAME, USER_AGENT_VALUE)
                .header(COOKIE_NAME, cookieMaotai);
        setProxyServer(req, proxyServer);
        Util.HttpResp rst = fateadmApi.Predict("10500", req.body().getBytes());
        if (rst.ret_code != 0) {
            throw new Exception(rst.err_msg);
        }
        return rst.pred_resl;
    }

    private void setProxyServer(HttpRequest req, HttpProxyServer proxyServer){
        if (proxyServer != null) {
            req.useProxy(proxyServer.getHost(), Integer.parseInt(proxyServer.getPort()));
            if (proxyServer.isNeedAuth()) {
                req.proxyBasic(proxyServer.getUsername(), proxyServer.getPassword());
            }
        }
    }

    public static void main(String[] args) {
        HttpRequest r = HttpRequest.get(NAN_HANG_VERIFY_URL)
                .header(USER_AGENT_NAME, USER_AGENT_VALUE)
                .header(COOKIE_NAME, "maotai=aaaaaaa");
        System.out.println();
    }

}
