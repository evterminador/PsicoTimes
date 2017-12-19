package com.example.formandocodigo.psicotimes.main.view;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public interface MainView {
    void updateAppSuccess(String message);
    void updateAppError(String error);
    void syncError(String error);
    void showFab();
    void hiddenFab();
}
