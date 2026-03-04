package com.fuhouyu.framework.web.utils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * <p>
 * json maaper 工具类
 * </p>
 *
 * @author fuhouyu
 * @since 2026/3/4 22:35
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JacksonCustomizer {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd");

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 创建json mapper
     *
     * @return json mapper
     */
    public static JsonMapper createJsonMapper() {
        // 不显示为null的字段, 将Long类型，转换为String类型，否则前端会精度丢失
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DATE_TIME_FORMATTER);

        // 日期时间序列化与反序列化
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(DATE_FORMATTER));
        simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMATTER));
        simpleModule.addSerializer(OffsetDateTime.class, new ValueSerializer<>() {
            @Override
            public void serialize(OffsetDateTime value, JsonGenerator gen, SerializationContext ctx) throws JacksonException {
                gen.writeString(value.atZoneSameInstant(ZoneId.systemDefault()).format(DATE_TIME_FORMATTER));
            }
        });

        // 日期时间序列化与反序列化
        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DATE_TIME_FORMATTER));
        simpleModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);

        simpleModule.addDeserializer(OffsetDateTime.class, new ValueDeserializer<OffsetDateTime>() {
            @Override
            public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
                String str = p.getString();
                // 将不带时区的字符串解析为 LocalDateTime，再关联系统默认时区转为 OffsetDateTime
                LocalDateTime localDateTime = LocalDateTime.parse(str, DATE_TIME_FORMATTER);
                return localDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime();
            }
        });

        JsonMapper.Builder mapperBuilder = JsonMapper.builder();
        mapperBuilder.addModule(simpleModule);
        return mapperBuilder.build();
    }
}
