package com.oxyl.technight_api_first.config;

import com.oxyl.technight_api_first.themealdb.api.FilterApi;
import com.oxyl.technight_api_first.themealdb.api.ListApi;
import com.oxyl.technight_api_first.themealdb.api.LookupApi;
import com.oxyl.technight_api_first.themealdb.api.SearchApi;
import com.oxyl.technight_api_first.themealdb.client.ApiClient;
import feign.Feign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TheMealDbApiClientConfiguration {

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
                        Feign.builder().requestInterceptor(new AuthRequestInterceptor())
                )
                .buildClient(clazz);
    }
}
