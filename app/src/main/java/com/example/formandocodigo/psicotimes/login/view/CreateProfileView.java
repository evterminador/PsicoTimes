package com.example.formandocodigo.psicotimes.login.view;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public interface CreateProfileView {
    void showProgressBar();
    void hideProgressBar();
    void createProfileError(String error);

    void goHome();
}
