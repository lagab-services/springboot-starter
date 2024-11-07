package com.lagab.blank.common.web.client;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.With;

@Data
@Builder
public class FeignTarget<T> implements Serializable {

    private final String host;
    @NonNull
    private final String name;
    @NonNull
    private Class<T> tClass;
    private final String login;
    private final String password;
    @With
    @Builder.Default
    private final boolean useResponseError = true;
    @With
    @Builder.Default
    private final boolean useDirectAccess = true;
    @With
    @Builder.Default
    private final boolean disableCookieManagement = true;

    private final Integer connectTimeout;

    private final Integer readTimeout;

    private final Integer retries;

    public static <T> FeignTarget<T> create(String name, Class<T> tClass) {
        return FeignTarget.<T>builder()
                          .name(name)
                          .tClass(tClass)
                          .build();
    }

    /*public FeignTarget<T> authenticate(String login, String password) {
        return this.login(login).password(password).build();
    }

    public FeignTarget<T> withConnectTimeout(int connectTimeout) {
        return this.toBuilder().connectTimeout(connectTimeout).build();
    }

    public FeignTarget<T> withReadTimeout(int readTimeout) {
        return this.toBuilder().readTimeout(readTimeout).build();
    }

    public FeignTarget<T> withRetries(int retries) {
        return this.toBuilder().retries(retries).build();
    }

    public FeignTarget<T> withHost(String host) {
        return this.toBuilder().host(host).build();
    }*/
}
