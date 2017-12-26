package com.example.formandocodigo.psicotimes.login.repository;

import com.example.formandocodigo.psicotimes.login.net.entity.RegisterResponse;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public interface CreateProfileRepository {
    void createProfile(RegisterResponse response);
}
