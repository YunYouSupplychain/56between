package com.yunyou.core.mapper;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.TimeZone;

/**
 * 简单封装Jackson，实现JSON String<->Java Object的Mapper.
 * 封装不同的输出风格, 使用不同的builder函数创建实例.
 *
 * @author yunyou
 * @version 2016-11-15
 */
public class JsonMapper extends ObjectMapper {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(JsonMapper.class);
    private static JsonMapper mapper;

    public JsonMapper() {
        // 值为null或者其他形式null值或者为空的属性不会被序列化
        this(Include.NON_EMPTY);
    }

    public JsonMapper(Include include) {
        // 设置输出时包含属性的风格
        if (include != null) {
            this.setSerializationInclusion(include);
        }
        // 允许单引号、允许不带引号的字段名称
        this.enableSimple();
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 空值处理为空串
        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString("");
            }
        });
        // 数字科学计数法处理
        this.registerNumberModule();
        // xss攻击避免
        this.registerXssModule();
        // 设置时区
        this.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
    }

    /**
     * 取出Mapper做进一步的设置或使用其他序列化API.
     */
    public ObjectMapper getMapper() {
        return this;
    }

    /**
     * 允许单引号与不带引号的字段名称
     */
    public JsonMapper enableSimple() {
        this.enable(Feature.ALLOW_SINGLE_QUOTES);
        this.enable(Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        return this;
    }

    /**
     * 設定是否使用Enum的toString函數來讀寫Enum,
     * 為False時時使用Enum的name()函數來讀寫Enum, 默認為False.
     * 注意本函數一定要在Mapper創建後, 所有的讀寫動作之前調用.
     */
    public JsonMapper enableEnumUseToString() {
        this.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        this.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        return this;
    }

    /**
     * 支持使用Jaxb的Annotation，使得POJO上的annotation不用与Jackson耦合。
     * 默认会先查找jaxb的annotation，如果找不到再找jackson的。
     */
    public JsonMapper registerJaxbAnnotationModule() {
        this.registerModule(new JaxbAnnotationModule());
        return this;
    }

    /**
     * 数字科学计数法处理
     */
    public JsonMapper registerNumberModule() {
        this.registerModule(new SimpleModule().addSerializer(Double.class, new JsonSerializer<Double>() {
            @Override
            public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(BigDecimal.valueOf(value).stripTrailingZeros().toPlainString());
            }

            @Override
            public Class<Double> handledType() {
                return Double.class;
            }
        }));
        this.registerModule(new SimpleModule().addSerializer(double.class, new JsonSerializer<Double>() {
            @Override
            public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(BigDecimal.valueOf(value).stripTrailingZeros().toPlainString());
            }

            @Override
            public Class<Double> handledType() {
                return Double.class;
            }
        }));
        this.registerModule(new SimpleModule().addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
            @Override
            public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.stripTrailingZeros().toPlainString());
            }

            @Override
            public Class<BigDecimal> handledType() {
                return BigDecimal.class;
            }
        }));
        return this;
    }

    /**
     * xss攻击避免处理
     */
    public JsonMapper registerXssModule() {
        this.registerModule(new SimpleModule().addSerializer(String.class, new JsonSerializer<String>() {
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                // xss攻击避免
                gen.writeString(value);
            }

            @Override
            public Class<String> handledType() {
                return String.class;
            }
        }));
        return this;
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     */
    public String toJson(Object object) {
        try {
            return this.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return null;
        }
    }

    /**
     * 輸出JSONP格式數據.
     */
    public String toJsonP(String functionName, Object object) {
        return toJson(new JSONPObject(functionName, object));
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     * <p>
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * <p>
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     *
     * @see #fromJson(String, JavaType)
     */
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return this.readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>, 先使用函數createCollectionType构造类型,然后调用本函数.
     *
     * @see #createCollectionType(Class, Class...)
     */
    public <T> T fromJson(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return this.readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 構造泛型的Collection Type如:
     * ArrayList<MyBean>, 则调用constructCollectionType(ArrayList.class,MyBean.class)
     * HashMap<String,MyBean>, 则调用(HashMap.class,String.class, MyBean.class)
     */
    public JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return this.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 當JSON裡只含有Bean的部分屬性時，更新一個已存在Bean，只覆蓋該部分的屬性.
     */
    public <T> T update(String jsonString, T object) {
        try {
            return this.readerForUpdating(object).readValue(jsonString);
        } catch (IOException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
        return null;
    }


    /**
     * 创建只输出非Null且非Empty(如List.isEmpty)的属性到Json字符串的Mapper,建议在外部接口中使用.
     */
    public static JsonMapper getInstance() {
        if (mapper == null) {
            mapper = new JsonMapper();
        }
        return mapper;
    }

    /**
     * 对象转换为JSON字符串
     */
    public static String toJsonString(Object object) {
        return JsonMapper.getInstance().toJson(object);
    }

    /**
     * JSON字符串转换为对象
     */
    public static Object fromJsonString(String jsonString) {
        return fromJsonString(jsonString, Object.class);
    }

    /**
     * JSON字符串转换为对象
     */
    public static <T> T fromJsonString(String jsonString, Class<T> clazz) {
        return JsonMapper.getInstance().fromJson(jsonString, clazz);
    }

}