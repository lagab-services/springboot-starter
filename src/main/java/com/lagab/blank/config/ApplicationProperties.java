package com.lagab.blank.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    private List<FeignResource> feignResources = new ArrayList<>();

    @Getter
    @Setter
    public static class FeignResource {
        private String name;
        private String host;
        private String login;
        private String password;
        private List<Resource> resources = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class Resource {
        private String name;
        private Integer connectTimeout;
        private Integer readTimeout;
        private Integer retries;
    }
}
