package com.lagab.blank.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

import lombok.Getter;
import lombok.Setter;

@Getter
@ConfigurationProperties(prefix = "common")
public class CommonProperties {
    private final CorsConfiguration cors = new CorsConfiguration();
    private final Security security = new Security();
    private final Feign feign = new Feign();

    @Getter
    public static class Security {
        private final Jwt jwt = new Jwt();

        @Getter
        @Setter
        public static class Jwt {
            private String secretKey;
            private long expirationTime;
        }
    }

    @Getter
    @Setter
    public static class Feign {

        private int defaultConnectTimeout;
        private int defaultReadTimeout;
        private int defaultRetries;
        private int maxIdleConnection;
        private int connectionKeepALiveDuration;
    }

}
