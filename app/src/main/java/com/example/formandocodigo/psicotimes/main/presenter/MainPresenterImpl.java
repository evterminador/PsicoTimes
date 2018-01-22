package com.example.formandocodigo.psicotimes.main.presenter;

import android.app.Activity;

import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.entity.StatisticsDetail;
import com.example.formandocodigo.psicotimes.main.interactor.MainInteractor;
import com.example.formandocodigo.psicotimes.main.interactor.MainInteractorImpl;
import com.example.formandocodigo.psicotimes.utils.net.OrderService;
import com.example.formandocodigo.psicotimes.main.view.MainView;

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
    public List<StateUse> findAll() {
        return interactor.findAll();
    }

    @Override
    public List<StateUse> findAllById(Integer id) {
        return interactor.findAllId(id);
    }

    @Override
    public List<StatisticsDetail> getStatisticsDetailCurrentDate() {
        return interactor.getStatisticsDetailCurrentDate();
    }

    @Override
    public int quantityUnlockScreen(Activity activity) {
        return interactor.quantityUnlockScreen(activity);
    }

    @Override
    public int quantityNroApps(Activity activity) {
        return interactor.quantityNroApps(activity);
    }
}
