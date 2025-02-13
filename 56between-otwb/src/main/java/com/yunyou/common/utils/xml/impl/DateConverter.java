package com.yunyou.common.utils.xml.impl;

import com.yunyou.common.utils.xml.Converter;
import com.yunyou.common.utils.xml.XmlObjectUtils;
import org.w3c.dom.Node;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateConverter implements Converter {

    private static DateFormat format;

    static {
        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    @Override
    public Object convertXml(Node node, Field field) {
        Node firstChild = node.getFirstChild();
        try {
            return format.parse(firstChild.getNodeValue().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String xmlReverse(Object propValue, Field prop) {
        String propName = prop.getName();
        return XmlObjectUtils.appendXmlNode(propName, propValue.toString());
    }

    @Override
    public Object convertJson(Object value, Field field) {
        try {
            return format.parse(value.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
