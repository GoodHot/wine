package com.goodhot.wine.subscribe;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CustomerData {

    private List<Customer> customers;

    @Data
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

        private String member;
        private String name;
        @JSONField(name = "id-card")
        private String idCard;
        private String tel;
        private String ticket;
        private String setoff;
        private String arrive;
        @JSONField(name = "flt-date")
        private String fltDate;
        @JSONField(name = "enroll-date")
        private List<String> enrollDate;
        private String code;
        private String service;
        private String secret;

        public Map<String, String> toMap(String enrollDate) {
            Map<String, String> r = new HashMap<>();
            r.put(MEMBER, member);
            r.put(NAME, name);
            r.put(ID_CARD, idCard);
            r.put(TEL, tel);
            r.put(TICKET, ticket);
            r.put(SETOFF, setoff);
            r.put(ARRIVE, arrive);
            r.put(FLT_DATE, fltDate);
            r.put(ENROLL_DATE, enrollDate);
            r.put(CODE, code);
            r.put(SERVICE, "on");
            r.put(SECRET, "on");

            return r;
        }
    }

    public static String mockCustomerDate() {
        String d = "{\n" +
                "\"customers\":\n" +
                "[\n" +
                "{\n" +
                "\"member\": 1,\n" +
                "\"name\":   \"王星\",\n" +
                "\"id-card\":    \"420984198402170419\",\n" +
                "\"tel\": \"15806199718\",\n" +
                "\"ticket\": \"7848529343786\",\n" +
                "\"setoff\": \"贵阳\",\n" +
                "\"arrive\": \"珠海\",\n" +
                "\"flt-date\": \"11.9号\",\n" +
                "\"enroll-date\":\n" +
                "[\n" +
                "\"11月13日 上午\",\n" +
                "\"11月13日 下午\",\n" +
                "\"11月15日 上午\"\n" +
                "],\n" +
                "\"code\": null,\n" +
                "\"service\": \"on\",\n" +
                "\"secret\": \"on\"\n" +
                "},\n" +
                "{\n" +
                "\"member\": 1,\n" +
                "\"name\":   \"王星\",\n" +
                "\"id-card\":    \"420984198402170419\",\n" +
                "\"tel\": \"15806199718\",\n" +
                "\"ticket\": \"7848529343786\",\n" +
                "\"setoff\": \"贵阳\",\n" +
                "\"arrive\": \"珠海\",\n" +
                "\"flt-date\": \"11.9号\",\n" +
                "\"enroll-date\":\n" +
                "[\n" +
                "],\n" +
                "\"code\": null,\n" +
                "\"service\": \"on\",\n" +
                "\"secret\": \"on\"\n" +
                "}\n" +
                "]\n" +
                "}";
        return d;
    }

}
