package com.kcc.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

  public static final ObjectMapper mapper = new ObjectMapper();
  static {
    mapper.setSerializationInclusion(Include.NON_NULL);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    mapper.setDateFormat(dateFormat);
    mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setTimeZone(dateFormat.getTimeZone());
  }
  
  public static <E> String toJson(Object object) throws IOException {
    return mapper.writeValueAsString(object);
  }

  public static <E> String toJson(Object object, Class<E> clazz) throws IOException {
    return mapper.writerFor(clazz).writeValueAsString(object);
  }

  public static <E> String toJson(Object object, TypeReference<E> clazz) throws IOException {
    return mapper.writerFor(clazz).writeValueAsString(object);
  }

  public static <E> E fromJson(String jsonString, Class<E> clazz) throws IOException {
    return mapper.readValue(jsonString, clazz);
  }

  public static <E> E fromJson(String jsonString, TypeReference<E> clazz) throws IOException {
    return mapper.readValue(jsonString, clazz);
  }

  public static <S, T> T copy(Object obj, Class<S> source, Class<T> target) throws IOException {
    return fromJson(toJson(obj, source), target);
  }

  public static <S, T> T copy(Object obj, TypeReference<S> source, TypeReference<T> target)
      throws IOException {
    return fromJson(toJson(obj, source), target);
  }

}

