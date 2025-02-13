package com.yunyou.modules.interfaces.kdBest.utils.converter.util.jsonReader;

public class JSONValidatingReader extends JSONReader {
    public static final Object INVALID = new Object();
    private JSONValidator validator;
    
    public JSONValidatingReader(JSONValidator validator) {
        this.validator = validator;
    }
    
    public JSONValidatingReader(ExceptionErrorListener listener) {
        this(new JSONValidator(listener));
    }
    
    public Object read(String string) {
        if (!validator.validate(string)) return INVALID;
        return super.read(string);
    }
}
