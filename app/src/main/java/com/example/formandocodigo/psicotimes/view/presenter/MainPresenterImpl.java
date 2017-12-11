package com.example.formandocodigo.psicotimes.view.presenter;

import android.app.Activity;

import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.view.interactor.MainInteractor;
import com.example.formandocodigo.psicotimes.view.interactor.MainInteractorImpl;
import com.example.formandocodigo.psicotimes.view.net.OrderService;
import com.example.formandocodigo.psicotimes.view.net.entity.AppOrderResponse;
import com.example.formandocodigo.psicotimes.view.view.MainView;

import java.util.List;

import retrofit2.Call;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public class MainPresenterImpl implements MainPresenter {

    private MainView mainView;
    private MainInteractor interactor;

    public MainPresenterImpl(MainView mainView) {
        this.mainView = mainView;
        interactor = new MainInteractorImpl(this);
    }

    @Override
    public List<StateUse> findAll(Activity activity) {
        return interactor.findAll(activity);
    }

    @Override
    public void syncUp(Activity activity, OrderService service, Call<AppOrderResponse> call) {
        interactor.syncUp(activity, service, call);
    }

    @Override
    public void syncSuccess() {

    }

    @Override
    public void syncError() {

    }
}
