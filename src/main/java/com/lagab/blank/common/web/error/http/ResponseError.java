package com.lagab.blank.common.web.error.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

/**
 * This class is used to create an error response when a process failed.
 * <p>
 * To create a {@link ResponseError}, you can do this :
 * <pre class="code">
 * public ResponseEntity throwError() {
 *     return ResponseError.create()
 *         .error(HttpStatus.NOT_FOUND)
 *         .errorDescription("The entity does not exist")
 *         .build();
 * }
 * </pre>
 * As response, you will get an 404 HTTP status with this body :
 * <pre class="code">
 * {
 *     "error": "Not Found",
 *     "error_description": "The entity does not exist"
 * }
 * </pre>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseError {

    @Builder.Default
    private String error = "";

    @JsonIgnore
    @Builder.Default
    private HttpStatus status = HttpStatus.OK;

    @Builder.Default
    private String code = "";

    @Singular
    @JsonProperty("message")
    private List<String> errorDescriptions = new ArrayList<>();

    @JsonProperty("details")
    private Map<String, String> errorDescriptionMap = new HashMap<>();

    /**
     * Sets the main error message.
     *
     * @param status       the HTTP status
     * @param errorMessage the error message
     * @return the response error
     */
    public ResponseError error(HttpStatus status, String errorMessage) {
        this.status = status;
        this.code = String.valueOf(status.value());
        this.error = errorMessage;
        return this;
    }

    /**
     * Sets the main error message using a {@link HttpStatus#getReasonPhrase()}.
     *
     * @param status the HTTP status
     * @return the response error
     */
    public ResponseError error(HttpStatus status) {
        this.status = status;
        this.code = String.valueOf(status.value());
        this.error = status.getReasonPhrase();
        return this;
    }

    /**
     * Sets a list of error descriptions.
     *
     * @param errorDescription the error description
     * @return the response error
     */
    public ResponseError errorDescription(String... errorDescription) {
        this.errorDescriptions.addAll(Arrays.asList(errorDescription));
        return this;
    }

    public ResponseError errorDescriptions(List<String> errorDescription) {
        this.errorDescriptions.addAll(errorDescription);
        return this;
    }

    public ResponseError errorDescriptionsMap(Map<String, String> errorDescription) {
        this.errorDescriptionMap.putAll(errorDescription);
        return this;
    }

    public ResponseEntity<ResponseError> toResponseEntity() {
        return ResponseEntity.status(getStatus()).body(this);
    }
}
