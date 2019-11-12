package com.goodhot.wine.test;

import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import org.junit.Test;

public class SessionTest {

    @Test
    public void session(){
        HttpRequest req = HttpRequest.get("http://kwemobile.bceapp.com/maotai.php").header("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Mobile Safari/537.36");
        System.out.println(JSON.toJSONString(req.headers()));
    }

}
