package com.goodhot.wine.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HttpServer {

    private String host;
    private String port;

}
