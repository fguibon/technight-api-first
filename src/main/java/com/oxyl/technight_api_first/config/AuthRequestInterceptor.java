package com.oxyl.technight_api_first.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class AuthRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("User-Agent", "technight-api-first (fguibon@oxyl.fr)");
    }
}
