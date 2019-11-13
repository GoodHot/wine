package com.goodhot.wine.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HttpProxyServer {

    private String host;
    private String port;
    private boolean needAuth;
    private String username;
    private String password;

}
