package com.example.formandocodigo.psicotimes.login.repository.net.entity;

import java.util.List;
import java.util.Map;

/**
 * Created by FormandoCodigo on 01/12/2017.
 */

public class ApiError {

    String message;
    Map<String, List<String>> errors;

    public String getMessage() {
        return message;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }
}
