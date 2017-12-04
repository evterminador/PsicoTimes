package com.example.formandocodigo.psicotimes.login.repository;

import com.example.formandocodigo.psicotimes.login.repository.net.entity.RegisterResponse;

/**
 * Created by FormandoCodigo on 01/12/2017.
 */

public interface LoginRepository {
    void signIn(RegisterResponse response);
}
