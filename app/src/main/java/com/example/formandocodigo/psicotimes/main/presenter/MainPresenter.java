package com.example.formandocodigo.psicotimes.main.presenter;

import android.app.Activity;

import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.entity.StatisticsDetail;
import com.example.formandocodigo.psicotimes.utils.net.OrderService;

import java.util.List;

import retrofit2.Call;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public interface MainPresenter {
    List<StateUse> findAll();

    List<StateUse> findAllById(Integer id);

    List<StatisticsDetail> getStatisticsDetailCurrentDate();

    int quantityUnlockScreen(Activity activity);

    int quantityNroApps(Activity activity);
}
