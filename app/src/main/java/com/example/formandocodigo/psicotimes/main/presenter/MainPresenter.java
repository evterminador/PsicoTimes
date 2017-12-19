package com.example.formandocodigo.psicotimes.main.presenter;

import android.app.Activity;

import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.main.net.OrderService;
import com.example.formandocodigo.psicotimes.main.net.entity.AppOrderResponse;
import com.example.formandocodigo.psicotimes.main.net.entity.StateUserOrderResponse;

import java.util.List;

import retrofit2.Call;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public interface MainPresenter {
    List<StateUse> findAll();

    List<StateUse> findAllById(Integer id);

    void updateApp(Activity activity, OrderService service, Call<AppOrderResponse> call);

    void updateAppSuccess(String message);

    void updateAppError(String error);

    void syncUp(Activity activity, OrderService service,  Call<StateUserOrderResponse> call);

    void syncSuccess(String message);

    void syncError(String error);
}
