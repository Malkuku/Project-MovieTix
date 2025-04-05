package com.movietix.xiazihao.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.text.SimpleDateFormat;
import java.util.List;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 全局配置
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    // 解析普通对象
    public static <T> T parseJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("Failed to parse JSON to " + clazz.getSimpleName(), e);
        }
    }

    // 解析集合
    public static <T> List<T> parseJsonToList(String json, Class<T> clazz) {
        try {
            CollectionType type = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("Failed to parse JSON to List<" + clazz.getSimpleName() + ">", e);
        }
    }

    // 封装对象为JSON
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JsonParseException("Failed to convert object to JSON", e);
        }
    }

    // 自定义异常
    public static class JsonParseException extends RuntimeException {
        public JsonParseException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}