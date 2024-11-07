package com.lagab.blank.common.web.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.PageableSpringQueryMapEncoder;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lagab.blank.common.config.CommonProperties;
import com.lagab.blank.common.web.error.http.ResponseError;
import com.lagab.blank.common.web.error.http.ResponseErrorException;

import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Retryer;
import feign.Util;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.Protocol;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

@Service
@Slf4j
@RequiredArgsConstructor
@Import(FeignClientsConfiguration.class)
public class DynamicFeignBuilder {
    private final CommonProperties commonProperties;

    private final Decoder decoder;
    private final Encoder encoder;
    private final Contract contract;

    private final ObjectMapper objectMapper;

    public <T> T createFeignClient(@NonNull FeignTarget<T> target) {
        // Configuration settings using default values or those provided by the target
        int clientConnectTimeout =
                target.getConnectTimeout() != null ? target.getConnectTimeout() : commonProperties.getFeign().getDefaultConnectTimeout();
        int clientReadTimeout = target.getReadTimeout() != null ? target.getReadTimeout() : commonProperties.getFeign().getDefaultReadTimeout();
        int clientRetries = target.getRetries() != null ? target.getRetries() : commonProperties.getFeign().getDefaultRetries();

        // OkHttp client with direct access settings
        okhttp3.OkHttpClient rawClient = new okhttp3.OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(
                        commonProperties.getFeign().getMaxIdleConnection(),
                        commonProperties.getFeign().getConnectionKeepALiveDuration(),
                        MILLISECONDS))
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .sslSocketFactory(trustAllSslSocketFactory(), trustAllTrustManager())
                .build();

        Client client = new OkHttpClient(rawClient);
        // Feign Builder
        Feign.Builder feignBuilder = Feign.builder()
                                          .client(client)
                                          .logger(new Slf4jLogger(target.getTClass()))
                                          .logLevel(Logger.Level.FULL)
                                          .encoder(encoder)
                                          .decoder(decoder)
                                          .options(new Request.Options(clientConnectTimeout, MILLISECONDS, clientReadTimeout, MILLISECONDS, true))
                                          .retryer(new Retryer.Default(100, SECONDS.toMillis(1), ++clientRetries))
                                          .queryMapEncoder(new PageableSpringQueryMapEncoder())
                                          .contract(contract);

        // Configure error decoder and authentication if credentials are provided
        feignBuilder.errorDecoder(new ResponseErrorDecoder());

        if (target.getLogin() != null && target.getPassword() != null && !target.getLogin().isEmpty() && !target.getPassword().isEmpty()) {
            feignBuilder.requestInterceptor(new BasicAuthRequestInterceptor(target.getLogin(), target.getPassword()));
        }

        if (target.getHost() == null || target.getHost().isEmpty()) {
            throw new IllegalArgumentException("Feign host is required for direct access.");
        }

        String instanceHost = target.getHost();

        log.info("Creating Feign client for {} at {}", target.getName(), instanceHost);
        return feignBuilder.target(new feign.Target.HardCodedTarget<>(target.getTClass(), target.getName(), instanceHost));
    }

    private class ResponseErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            String method = response.request().httpMethod().name();
            String url = response.request().url();
            StringBuilder message = new StringBuilder("Status %s on %s %s".formatted(response.status(), method, url));

            String body = extractBody(response);
            ResponseError error = null;

            try {
                if (!body.isEmpty()) {
                    error = objectMapper.readValue(body, ResponseError.class);
                    message.append("; content: ").append(error.getErrorDescriptions());
                } else {
                    message.append("Failed to extract response body");
                }
            } catch (Exception e) {
                message.append("; raw content: ").append(body);
            }

            if (error == null) {
                error = ResponseError.builder().errorDescriptions(Arrays.asList("No error detail provided by resource", body)).build();
            }

            error.error(HttpStatus.valueOf(response.status()));
            return new ResponseErrorException(response.status(), message.toString(), error);
        }

        private String extractBody(Response response) {
            try {
                return Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            } catch (IOException e) {
                return "";
            }
        }
    }

    // TODO: To remove
    private SSLSocketFactory trustAllSslSocketFactory() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: To remove
    private X509TrustManager trustAllTrustManager() {
        return (X509TrustManager) new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }
}
