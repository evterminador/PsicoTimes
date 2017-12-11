package com.example.formandocodigo.psicotimes.login.interactor;

import com.example.formandocodigo.psicotimes.login.presenter.LoginPresenter;
import com.example.formandocodigo.psicotimes.login.repository.LoginRepository;

/**
 * Created by FormandoCodigo on 10/12/2017.
 */

public class LoginInteractorImpl implements LoginInteractor {

    private LoginRepository repository;
    private LoginPresenter presenter;

    public LoginInteractorImpl(LoginPresenter presenter) {

    }

}
