package com.goodhot.wine.subscribe;

import com.github.kevinsawicki.http.HttpRequest;
import com.goodhot.wine.proxy.HttpProxyTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class NanHangRequest {
    // 网址
    public static final String NAN_HANG_URL = "http://kwemobile.bceapp.com/maotai.php";
    public static final String NAN_HANG_VERIFY_URL = "http://kwemobile.bceapp.com/maotai.php/index/verify";
    public static final String NAN_HANG_SUBSCRIBE_URL = "http://kwemobile.bceapp.com/maotai.php/index/verify";

    // Header名
    public static final String USER_AGENT_NAME = "User-Agent";
    public static final String USER_AGENT_VALUE = "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1";

    public static final String CONTENT_TYPE_NAME = "Content-Type";
    public static final String CONTENT_TYPE_VALUE_X_WWW_FORM = "application/x-www-form-urlencoded";

    public static final String SET_COOKIE_NAME = "Set-Cookie";
    public static final String COOKIE_NAME = "Cookie";


    @Autowired
    private HttpProxyTask httpProxyTask;

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
    public HttpRequest getPage(String url) throws IOException {
        HttpRequest req =
                HttpRequest.get(url)
                        .header(USER_AGENT_NAME, USER_AGENT_VALUE);
        return req;
    }

    /**
     * 获取cookie maotai
     *
     * @param req
     */
    public void setCookieMaotai(HttpRequest req) {
        this.cookieMaotai = req.header(SET_COOKIE_NAME);
        System.out.println("cookie maotai: " + this.cookieMaotai);
    }

    /**
     * 获取cookie phpsession
     *
     * @param req
     */
    public void setCookiePhpSession(HttpRequest req) {
        this.cookiePhpSession = req.header(SET_COOKIE_NAME);
        System.out.println("coolie phpsession: " + this.cookiePhpSession);
    }

    /**
     * 提交预约请求
     *
     * @param formData
     * @return
     */
    public void postSubscribe(HttpRequest req, Map<String, String> formData, String cookiePhpSession, String cookieMaotai) {
        final String ILLEGAL = "非法访问";
        final String OUTTIME_CAPTCHA = "验证码错误"; // 可能不存在了
        final String ALREADY_DONE = "该票号已经预约过";
        final String NO_EMPTY = "该日期预约数量已满";
        final String OK = "预约成功";

        req.post(NAN_HANG_SUBSCRIBE_URL)
                .header(CONTENT_TYPE_NAME, CONTENT_TYPE_VALUE_X_WWW_FORM)
                .header(COOKIE_NAME, cookiePhpSession + " ;" + cookieMaotai)
                .form(formData);
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
    public String getCaptcha(String cookieMaotai) {
        return "";
    }

}
