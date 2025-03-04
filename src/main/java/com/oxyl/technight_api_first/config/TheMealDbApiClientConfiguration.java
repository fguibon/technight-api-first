package com.oxyl.technight_api_first.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.oxyl.technight_api_first.themealdb.api.FilterApi;
import com.oxyl.technight_api_first.themealdb.api.ListApi;
import com.oxyl.technight_api_first.themealdb.api.LookupApi;
import com.oxyl.technight_api_first.themealdb.api.SearchApi;
import com.oxyl.technight_api_first.themealdb.client.ApiClient;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TheMealDbApiClientConfiguration {

    @Bean
    protected ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new JsonNullableModule());
        return objectMapper;
    }

    @Value("${themealdb.api.url}")
    private String theMealDbApiUrl;

    @Bean
    public FilterApi filterApi() {
        return buildTheMealDbApiClient(FilterApi.class);
    }

    @Bean
    public ListApi listApi() {
        return buildTheMealDbApiClient(ListApi.class);
    }

    @Bean
    public LookupApi lookupApi() {
        return buildTheMealDbApiClient(LookupApi.class);
    }

    @Bean
    public SearchApi searchApi() {
        return buildTheMealDbApiClient(SearchApi.class);
    }

    private <T extends ApiClient.Api> T buildTheMealDbApiClient(Class<T> clazz) {
        return new ApiClient()
                .setBasePath(theMealDbApiUrl)
                .setFeignBuilder(
                        Feign.builder()
                                .requestInterceptor(new AuthRequestInterceptor())
                                .encoder(new JacksonEncoder(objectMapper()))
                                .decoder(new JacksonDecoder(objectMapper()))
                                .retryer(Retryer.NEVER_RETRY)
                                .logLevel(Logger.Level.BASIC)
                                .logger(new CustomFeignRequestLogging())
                )
                .buildClient(clazz);
    }
}
