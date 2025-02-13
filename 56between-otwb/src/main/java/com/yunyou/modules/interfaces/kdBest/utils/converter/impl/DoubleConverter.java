package com.yunyou.modules.interfaces.kdBest.utils.converter.impl;

import com.yunyou.modules.interfaces.kdBest.utils.converter.Converter;
import com.yunyou.modules.interfaces.kdBest.utils.Parser;
import org.w3c.dom.Node;

import java.lang.reflect.Field;

public class DoubleConverter implements Converter {
    @Override
    public Object convertXml(Node node, Field field) {
        Node firstChild = node.getFirstChild();
        return Double.valueOf(firstChild.getNodeValue());
    }

    @Override
    public String xmlReverse(Object propValue, Field prop) {
        String propName = prop.getName();
        return Parser.appendXmlNode(propName, propValue.toString());
    }

    @Override
    public Object convertJson(Object value, Field field) {
        return Double.valueOf(value.toString());
    }
}
