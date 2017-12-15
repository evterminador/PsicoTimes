package com.example.formandocodigo.psicotimes.main.presenter;

import android.app.Activity;

import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.main.net.OrderService;
import com.example.formandocodigo.psicotimes.main.net.entity.AppOrderResponse;

import java.util.List;

import retrofit2.Call;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public interface MainPresenter {
    List<StateUse> findAll(Activity activity);
    void syncUp(Activity activity, OrderService service,  Call<AppOrderResponse> call);
    void syncSuccess(String error);
    void syncError(String error);
}
