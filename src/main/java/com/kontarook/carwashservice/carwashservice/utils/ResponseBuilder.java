package com.kontarook.carwashservice.carwashservice.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseBuilder {
    private Map<String, Object> jsonObject = new HashMap<>();

    public ResponseBuilder(String message) {
        this.put("msg", message);
    }

    public ResponseBuilder() {
        this("Success");
    }

    public ResponseBuilder put(String key, Object value) {
        jsonObject.put(key, value);
        return this;
    }

    public Map<String, Object> build() {
        return jsonObject;
    }

}
