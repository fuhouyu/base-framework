package com.fuhouyu.framework.web;

import com.fuhouyu.framework.web.utils.JacksonCustomizer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.JacksonJsonDecoder;
import org.springframework.http.codec.json.JacksonJsonEncoder;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import tools.jackson.databind.json.JsonMapper;

/**
 * <p>
 * 响应式web配置
 * </p>
 *
 * @author fuhouyu
 * @since 2026/3/4 22:32
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class WebFluxAutoConfiguration implements WebFluxConfigurer {

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        JsonMapper mapper = JacksonCustomizer.createJsonMapper();
        configurer.defaultCodecs().jacksonJsonEncoder(
                new JacksonJsonEncoder(mapper, MediaType.APPLICATION_JSON)
        );
        configurer.defaultCodecs().jacksonJsonDecoder(
                new JacksonJsonDecoder(mapper, MediaType.APPLICATION_JSON)
        );
    }
}
