package com.lagab.blank.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NoArgsConstructor;

/**
 * Utility class for Response entities.
 */
@NoArgsConstructor
public class ResponseUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Convert a response error to json.
     *
     * @param object the object to convert
     * @return the json converted
     * @throws JsonProcessingException the json exception
     */
    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        return object == null ? null : mapper.writeValueAsString(object);
    }
}
