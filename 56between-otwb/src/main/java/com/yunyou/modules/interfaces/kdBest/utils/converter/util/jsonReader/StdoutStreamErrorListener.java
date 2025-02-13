package com.yunyou.modules.interfaces.kdBest.utils.converter.util.jsonReader;

public class StdoutStreamErrorListener extends BufferErrorListener {
    
    public void end() {
        System.out.print(buffer.toString());
    }
}
