package com.example.formandocodigo.psicotimes.login.entity;

import com.squareup.moshi.Json;

import java.util.List;
import java.util.Map;

/**
 * Created by FormandoCodigo on 01/12/2017.
 */

public class RegisterResponse {
    @Json(name = "message")
    String message;
    @Json(name = "success")
    Map<String, List<String>> success;

    public String getMessage() {
        return message;
    }

    public Map<String, List<String>> getSuccess() {
        return success;
    }
}
