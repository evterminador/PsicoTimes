package com.example.formandocodigo.psicotimes.login.repository;

import com.example.formandocodigo.psicotimes.login.entity.RegisterResponse;
import com.example.formandocodigo.psicotimes.model.User;

/**
 * Created by FormandoCodigo on 01/12/2017.
 */

public interface LoginActivityRepository {
    void signIn(RegisterResponse response);
}
