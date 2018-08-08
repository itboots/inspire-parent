/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.ObjectUtils;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

/**
 * @author LANXE
 */
public final class Serializing {
    private static final String EMPTY = "";
    private static final Logger LOG = Logger.getLogger(Serializing.class.getName());

    public static SerializeFunction json() {
        return JsonHolder.JSON;
    }

    public static SerializeFunction json(SerializeFunction function) {
        return JsonHolder.JSON;
    }

    public static SerializeFunction xml() {
        return XmlHolder.xml;
    }

    public static class JsonHolder {
        public static String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT+8");
        static SerializeFunction JSON = new Jackson2SerializeFunction();

    }

    public static class XmlHolder {
        static SerializeFunction xml = new JAXBSerializeFunction();
    }

    /**
     */
    public interface SerializeFunction {
        /**
         * 序列成字符串。
         *
         * @param object 要转为字符串的对象
         * @return 返回序列化后的字符串
         */
        String toString(Object object);


        /**
         * 转化为对象。
         *
         * @param content      序列化后的字符串。
         * @param requiredType 请求转化的类型。
         * @param <T>          转化的类型。
         * @return 返回请求的对象类型。如果失败则返回 <code>null</code>
         */
        <T> T toObject(String content, Class<T> requiredType);

        /**
         * 转化为对象。
         *
         * @param content          序列化后的字符串。
         * @param requiredTypeName 请求转化的类型名称
         * @param <T>              转化的类型
         * @return 返回请求的对象类型。如果失败则返回 <code>null</code>
         */
        <T> T toObject(String content, String requiredTypeName);

        /**
         * 转化为对象。
         *
         * @param content     序列化后的字符串。
         * @param elementType 请求转化的类型。
         * @param <T>         转化的类型。
         * @return 返回请求的对象类型。如果失败则返回 <code>null</code>
         */
        <T> List<T> toList(String content, Class<T> elementType);
    }


    static final class Jackson2SerializeFunction implements SerializeFunction {
        private final ObjectMapper objectMapper = new ObjectMapper();

        public class DateTimeModule extends SimpleModule {
            private DateTimeFormatter format = DateTimeFormat.forPattern(JsonHolder.DEFAULT_DATE_TIME_FORMAT);

            public DateTimeModule() {
                super();
                addSerializer(DateTime.class, new StdScalarSerializer<DateTime>(DateTime.class) {

                    @Override
                    public void serialize(DateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider provider)
                            throws IOException {
                        if (!ObjectUtils.isEmpty(dateTime)) {
                            String dateTimeAsString = format.print(dateTime);
                            jsonGenerator.writeString(dateTimeAsString);
                        }
                    }
                });

                addDeserializer(DateTime.class, new StdScalarDeserializer<DateTime>(DateTime.class) {

                    @Override
                    public DateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                            throws IOException, JsonProcessingException {
                        JsonToken currentToken = jsonParser.getCurrentToken();
                        if (currentToken == JsonToken.VALUE_STRING) {
                            String dateTimeAsString = jsonParser.getText().trim();
                            return format.parseDateTime(dateTimeAsString);
                        } else {
                            throw JsonMappingException.from(jsonParser,
                                    String.format("Cannot deserialize instance of %s out of %s token",
                                            ClassUtil.nameOf(DateTime.class), jsonParser.getCurrentToken()));
                        }
                    }
                });
            }
        }

        Jackson2SerializeFunction() {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            SimpleDateFormat format = new SimpleDateFormat(JsonHolder.DEFAULT_DATE_TIME_FORMAT);
            format.setTimeZone(JsonHolder.DEFAULT_TIME_ZONE);
            objectMapper.setDateFormat(format);
            objectMapper.setTimeZone(JsonHolder.DEFAULT_TIME_ZONE);
            objectMapper.registerModule(new DateTimeModule());
        }

        /**
         * 序列成字符串。
         *
         * @param object 要转为JSON字符串的对象
         * @return 返回 JSON 字符串
         */
        @Override
        public String toString(Object object) {
            try {
                return objectMapper.writeValueAsString(object);
            } catch (IOException e) {
                e.printStackTrace();
                return EMPTY;
            }
        }

        /**
         * 转化为对象。
         *
         * @param content          用字符串表示的 JSON 串
         * @param requiredTypeName 请求转化的类型名称
         * @param <T>              转化的类型
         * @return 返回请求的对象类型。如果失败则返回 <code>null</code>
         */
        @Override
        @SuppressWarnings("unchecked")
        public <T> T toObject(String content, String requiredTypeName) {
            try {
                return (T) toObject(content, Class.forName(requiredTypeName));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public <T> List<T> toList(String content, Class<T> elementType) {
            try {
                JavaType jt = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
                return (List<T>) objectMapper.readValue(content, jt);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * 转化为对象。
         *
         * @param content      用字符串表示的 JSON 串
         * @param requiredType 请求转化的类型
         * @param <T>          转化的类型
         * @return 返回请求的对象类型。如果失败则返回 <code>null</code>
         */
        @Override
        @SuppressWarnings("unchecked")
        public <T> T toObject(String content, Class<T> requiredType) {
            try {
                return objectMapper.readValue(content, requiredType);

            } catch (IOException e) {
                e.printStackTrace();
                return null;

            }
        }
    }

    static final class JAXBSerializeFunction implements SerializeFunction {

        @Override
        public String toString(Object object) {
            StringWriter writer = new StringWriter();
            try {
                JAXB.marshal(object, writer);
            } finally {
                writer.flush();
            }
            return writer.getBuffer().toString();
        }

        @Override
        public <T> T toObject(String content, Class<T> requiredType) {
            StringReader reader = new StringReader(content);
            try {
                return JAXB.unmarshal(reader, requiredType);
            } finally {
                reader.close();
            }

        }

        @Override
        public <T> T toObject(String content, String requiredTypeName) {
            try {
                return (T) toObject(content, Class.forName(requiredTypeName));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public <T> List<T> toList(String content, Class<T> requiredType) {
            StringReader reader = new StringReader(content);
            try {
                return (List<T>) JAXB.unmarshal(reader, requiredType);
            } finally {
                reader.close();
            }
        }
    }

//    static final class FastJsonSerializeFunction implements SerializeFunction {
//        private static final SerializeConfig SERIALIZE_CONFIG = SerializeConfig.getGlobalInstance();
//        private static final ParserConfig PARSER_CONFIG = ParserConfig.getGlobalInstance();
//
//        private static final SerializerFeature[] SERIALIZER_FEATURES = {
//                SerializerFeature.WriteDateUseDateFormat,
//                SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
//                SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
//                SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
//                SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
//
////                SerializerFeature.WriteMapNullValue, // 输出空置字段
//        };
//
//        private static final Feature[] PARSE_FEATURES = {
////                Feature.AllowISO8601DateFormat
//        };
//
//        private static final ValueFilter DATETIME_VALUEFILTER = new ValueFilter() {
//
//            @Override
//            public Object process(Object object, String name, Object value) {
//                if (value instanceof DateTime) {
//                    return ((DateTime) value).toDate();
//                } else {
//                    return value;
//                }
//            }
//        };
//
//        FastJsonSerializeFunction() {
//            JSON.DEFFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
//
//            SERIALIZE_CONFIG.put(DateTime.class, new SimpleDateFormatSerializer(JSON.DEFFAULT_DATE_FORMAT) {
//                @Override
//                public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType,
//                                  int features) throws IOException {
//                    super.write(serializer, ((DateTime) object).toDate(), fieldName, fieldType, features);
//                }
//            });
//
//            PARSER_CONFIG.putDeserializer(DateTime.class, new DateDeserializer() {
//
//                @Override
//                protected <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {
//                    java.util.Date date = (java.util.Date) super.cast(parser, clazz, fieldName, val);
//                    return (T) new DateTime(date);
//                }
//            });
//
//        }
//
//
//        /**
//         * 序列成字符串。
//         *
//         * @param object 要转为JSON字符串的对象
//         * @return 返回 JSON 字符串
//         */
//        public String toString(Object object) {
//            return JSON.toJSONString(object, DATETIME_VALUEFILTER, SERIALIZER_FEATURES);
//
//        }
//
//        /**
//         * 转化为对象。
//         *
//         * @param content          用字符串表示的 JSON 串
//         * @param requiredTypeName 请求转化的类型名称
//         * @param <T>              转化的类型
//         * @return 返回请求的对象类型。如果失败则返回 <code>null</code>
//         */
//        public <T> T toObject(String content, String requiredTypeName) {
//
//            try {
//                return (T) toObject(content, Class.forName(requiredTypeName));
//
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        /**
//         * 转化为对象。
//         *
//         * @param content      用字符串表示的 JSON 串
//         * @param requiredType 请求转化的类型
//         * @param <T>          转化的类型
//         * @return 返回请求的对象类型。如果失败则返回 <code>null</code>
//         */
//        public <T> T toObject(String content, Class<T> requiredType) {
//            return JSON.parseObject(content, requiredType, PARSE_FEATURES);
//        }
//
//
//    }

    //    static final class JacksonSerializeFunction implements SerializeFunction {
//        private final ObjectMapper objectMapper = new ObjectMapper();
//
//        JacksonSerializeFunction() {
////            objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
//            objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
//        }
//
//        /**
//         * 序列成字符串。
//         *
//         * @param object 要转为JSON字符串的对象
//         * @return 返回 JSON 字符串
//         */
//        public String toString(Object object) {
//            try {
//                return objectMapper.writeValueAsString(object);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return StringUtils.EMPTY;
//            }
//        }
//
//        /**
//         * 转化为对象。
//         *
//         * @param content          用字符串表示的 JSON 串
//         * @param requiredTypeName 请求转化的类型名称
//         * @param <T>              转化的类型
//         * @return 返回请求的对象类型。如果失败则返回 <code>null</code>
//         */
//        @SuppressWarnings("unchecked")
//        public <T> T toObject(String content, String requiredTypeName) {
//            try {
//                return (T) toObject(content, Class.forName(requiredTypeName));
//
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        /**
//         * 转化为对象。
//         *
//         * @param content      用字符串表示的 JSON 串
//         * @param requiredType 请求转化的类型
//         * @param <T>          转化的类型
//         * @return 返回请求的对象类型。如果失败则返回 <code>null</code>
//         */
//        @SuppressWarnings("unchecked")
//        public <T> T toObject(String content, Class<T> requiredType) {
//            try {
//                return objectMapper.readValue(content, requiredType);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//
//            }
//        }
//    }
}
