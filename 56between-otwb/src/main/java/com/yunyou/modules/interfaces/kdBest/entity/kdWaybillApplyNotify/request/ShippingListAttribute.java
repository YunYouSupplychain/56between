package com.yunyou.modules.interfaces.kdBest.entity.kdWaybillApplyNotify.request;

import java.util.List;

public class ShippingListAttribute {
	private String code;
	private List<ShippingListAttributeValue> shippingListAttributeValue;

    public String getCode()
    {
        return this.code;
    }

    public void setCode(String value)
    {
        this.code = value;
    }

    public List<ShippingListAttributeValue>  getShippingListAttributeValue()
    {
        return this.shippingListAttributeValue;
    }

    public void setShippingListAttributeValue(List<ShippingListAttributeValue>  value)
    {
        this.shippingListAttributeValue = value;
    }

}
