package com.example.formandocodigo.psicotimes.main.interactor;

import android.app.Activity;

import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.entity.StatisticsDetail;
import com.example.formandocodigo.psicotimes.utils.net.OrderService;

import java.util.List;

import retrofit2.Call;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public interface MainInteractor {
    List<StateUse> findAll();

    List<StateUse> findAllId(Integer id);

    List<StatisticsDetail> getStatisticsDetailCurrentDate();

    int quantityUnlockScreen(Activity activity);

    int quantityNroApps(Activity activity);
}
