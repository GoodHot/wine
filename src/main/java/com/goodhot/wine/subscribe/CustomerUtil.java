package com.goodhot.wine.subscribe;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.stereotype.Service;

/**
 * 用户信息
 */
@Service
public class CustomerUtil {
    private CustomerData customerData;

    private Multimap<String, CustomerData.Customer> customerDataMap;

    /**
     * @param customerJson {
     *                     customers:
     *                     [
     *                     {
     *                     member: 1,
     *                     name:   "王星",
     *                     id-card:    "420984198402170419",
     *                     tel: "15806199718",
     *                     ticket: "7848529343786",
     *                     setoff: "贵阳",
     *                     arrive: "珠海",
     *                     flt-date: "11.9号",
     *                     enroll-date:
     *                     [
     *                     "11月13日 上午",
     *                     "11月13日 下午",
     *                     "11月15日 上午"
     *                     ],
     *                     code: null,
     *                     service: "on",
     *                     secret: "on"
     *                     }
     *                     ]
     *                     }
     */
    public CustomerUtil init(String customerJson) {
        this.customerData = JSON.parseObject(customerJson, CustomerData.class);
        Multimap<String, CustomerData.Customer> temp = ArrayListMultimap.create();
        for (CustomerData.Customer customer : this.customerData.getCustomers()) {
            for (String enrollDate : customer.getEnrollDate()) {
                temp.put(enrollDate, customer);
            }
        }
        this.customerDataMap = temp;
        return this;
    }

    /**
     * 判断是否有匹配日期的客户
     *
     * @Param date 例如：11月14日 上午
     */
    public Boolean whetherMatchDateCustomer(String date) {
        return !this.customerDataMap.get(date).isEmpty();
    }

    /**
     * 列出所有符合预约日期的客户
     *
     * @param date
     * @return
     */
    public CustomerData.Customer[] listMatchDate(String date) {
        return this.customerDataMap.get(date).toArray(new CustomerData.Customer[]{});
    }

    public static void main(String[] args) {
        CustomerUtil customerUtil = new CustomerUtil();
        customerUtil.init(CustomerData.mockCustomerDate());
        System.out.println();
    }
}
