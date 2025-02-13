package com.yunyou.modules.tms.common;

import com.google.common.collect.Maps;

import java.util.Map;

public class TmsCustomerType {
    private static final String CONSIGNEE = "CONSIGNEE";
    private static final String SHIPPER = "SHIPPER";
    private static final String CUSTOMER = "CUSTOMER";
    public static final Map<String, String> customerTypeMap = Maps.newHashMap();

    static {
        customerTypeMap.put(CUSTOMER, "1");
        customerTypeMap.put(CONSIGNEE, "2");
        customerTypeMap.put(SHIPPER, "3");
    }
}
