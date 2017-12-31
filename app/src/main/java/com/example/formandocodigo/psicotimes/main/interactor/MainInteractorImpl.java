package com.example.formandocodigo.psicotimes.main.interactor;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.utils.net.OrderService;
import com.example.formandocodigo.psicotimes.main.net.AppOrderResponse;
import com.example.formandocodigo.psicotimes.main.net.StateUserOrder;
import com.example.formandocodigo.psicotimes.main.net.StateUserOrderResponse;
import com.example.formandocodigo.psicotimes.main.presenter.MainPresenter;
import com.example.formandocodigo.psicotimes.main.repository.MainRepository;
import com.example.formandocodigo.psicotimes.main.repository.MainRepositoryImpl;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public class MainInteractorImpl implements MainInteractor {

    private static final String TAG = "MainActivity";

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
    public Integer quantityUnlockScreen(Activity activity) {
        return repository.quantityUnlockScreen(activity);
    }

    @Override
    public void updateApp(Activity activity, OrderService service, Call<AppOrderResponse> call) {
        call = service.appOrder();

        Toast.makeText(activity, "Espere un momento estamos trayendo toda la data", Toast.LENGTH_LONG).show();

        call.enqueue(new Callback<AppOrderResponse>() {
            @Override
            public void onResponse(Call<AppOrderResponse> call, Response<AppOrderResponse> response) {
                if (response.isSuccessful()) {
                    Log.w(TAG, "onResponse: " + response.body().getApplications().size());
                    repository.storeApp(activity, response.body());
                } else {
                    Log.w(TAG, "onResponse: " + response.errorBody());
                    presenter.updateAppError(response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<AppOrderResponse> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    @Override
    public void syncUp(Activity activity, OrderService service,  Call<StateUserOrderResponse> call) {
        List<StateUseEntity> stateUseEntities = repository.findCacheAll(activity);

        if (stateUseEntities != null) {
            StateUserOrder order = new StateUserOrder();

            HashMap<String, String> data = repository.getUserEmailAndPassword(activity);

            order.setToken(data.get("token"));
            order.setEmail(data.get("email"));
            order.setStateUseEntities(stateUseEntities);

            call = service.stateUserOrder(order);

            Toast.makeText(activity, "Sincronizando " + stateUseEntities.size() + " elementos", Toast.LENGTH_LONG).show();

            call.enqueue(new Callback<StateUserOrderResponse>() {

                @Override
                public void onResponse(Call<StateUserOrderResponse> call, Response<StateUserOrderResponse> response) {
                    if (response.isSuccessful()) {
                        Log.w(TAG, "onResponse: " + response.body());

                        repository.storeStateUser(activity, response.body());

                    } else {
                        Log.w(TAG, "onResponse: " + response.errorBody());
                        //handleErrors(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<StateUserOrderResponse> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

}
