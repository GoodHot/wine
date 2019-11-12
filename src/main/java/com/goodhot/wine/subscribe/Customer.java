package com.goodhot.wine.subscribe;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Customer {
    public static final String MEMBER = "member";
    public static final String NAME = "name";
    public static final String ID_CARD = "id-card";
    public static final String TEL = "tel";
    public static final String TICKET = "ticket";
    public static final String SETOFF = "setoff";
    public static final String ARRIVE = "arrive";
    public static final String FLT_DATE = "flt-date";
    public static final String ENROLL_DATE = "enroll-date";
    public static final String CODE = "code";
    public static final String SERVICE = "service";
    public static final String SECRET = "secret";

    /**
     * 测试获取客户信息
     *
     * @return
     */
    public Map<String, String> testGetOne() {
        return new HashMap<>();
    }

    /**
     * 判断是否有匹配日期的客户
     *
     * @Param date 例如：11月14日 上午
     */
    public Boolean whetherMatchDateCustomer(String date) {
        return true;
    }

    /**
     * 列出所有符合预约日期的客户
     *
     * @param date
     * @return
     */
    public List<Map<String, String>> listMatchDate(String date) {
        return new ArrayList<>();
    }
}
