package com.example.formandocodigo.psicotimes.main.interactor;

import android.app.Activity;

import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.entity.StatisticsDetail;
import com.example.formandocodigo.psicotimes.main.presenter.MainPresenter;
import com.example.formandocodigo.psicotimes.main.repository.MainRepository;
import com.example.formandocodigo.psicotimes.main.repository.MainRepositoryImpl;

import java.util.List;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public class MainInteractorImpl implements MainInteractor {

    private MainRepository repository;
    private MainPresenter presenter;

    public MainInteractorImpl(MainPresenter presenter) {
        this.presenter = presenter;
        repository = new MainRepositoryImpl(presenter);
    }

    @Override
    public List<StateUse> findAll() {
        return repository.findAll();
    }

    @Override
    public List<StateUse> findAllId(Integer id) {
        return repository.findByIdAll(id);
    }

    @Override
    public List<StatisticsDetail> getStatisticsDetailCurrentDate() {
        return repository.getStatisticsDetailCurrentDate();
    }

    @Override
    public int quantityUnlockScreen(Activity activity) {
        return repository.quantityUnlockScreen(activity);
    }

    @Override
    public int quantityNroApps(Activity activity) {
        return repository.quantityNroApps(activity);
    }
}
