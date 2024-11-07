package com.lagab.blank.config;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;

import com.lagab.blank.common.web.client.DynamicFeignBuilder;
import com.lagab.blank.common.web.client.FeignTarget;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FeignConfiguration {

    private final ApplicationProperties properties;

    private final DynamicFeignBuilder dynamicFeignBuilder;

    //    private ObjectMapper customObjectMapper() {
    //        return new ObjectMapper()
    //                .registerModule(new JavaTimeModule())
    //                .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
    //                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    //    }

    private <T> T createFeignResource(Class<T> tClass) {
        Optional<ApplicationProperties.FeignResource> feignResource = properties.getFeignResources().stream()
                                                                                .filter(r -> r.getResources().stream()
                                                                                              .map(ApplicationProperties.Resource::getName)
                                                                                              .anyMatch(name -> name.equals(tClass.getSimpleName())))
                                                                                .findFirst();

        if (feignResource.isEmpty()) {
            throw new IllegalArgumentException("Fail to find config info for Resource %s".formatted(tClass.getSimpleName()));
        }

        Optional<ApplicationProperties.Resource> resource = feignResource.get().getResources().stream()
                                                                         .filter(r -> r.getName().equals(tClass.getSimpleName()))
                                                                         .findFirst();

        if (resource.isEmpty()) {
            throw new IllegalArgumentException("Fail to find config info for Resource %s".formatted(tClass.getSimpleName()));
        }

        FeignTarget<T> target = FeignTarget.<T>builder()
                                           .name(feignResource.get().getName())
                                           .tClass(tClass)
                                           .login(feignResource.get().getLogin())
                                           .password(feignResource.get().getPassword())
                                           .connectTimeout(resource.get().getConnectTimeout())
                                           .readTimeout(resource.get().getReadTimeout())
                                           .host(feignResource.get().getHost())
                                           .useDirectAccess(true)
                                           .disableCookieManagement(true)
                                           .retries(resource.get().getRetries())
                                           .build();

        return dynamicFeignBuilder.createFeignClient(target);
    }
}
