package com.yunyou.modules.interfaces.kdBest.utils.converter.util.jsonReader;

public interface JSONErrorListener {
    void start(String text);
    void error(String message, int column);
    void end();
}
