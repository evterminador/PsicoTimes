package com.example.formandocodigo.psicotimes.view.interactor;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.view.net.OrderService;
import com.example.formandocodigo.psicotimes.view.net.entity.AppOrder;
import com.example.formandocodigo.psicotimes.view.net.entity.AppOrderResponse;
import com.example.formandocodigo.psicotimes.view.presenter.MainPresenter;
import com.example.formandocodigo.psicotimes.view.repository.MainRepository;
import com.example.formandocodigo.psicotimes.view.repository.MainRepositoryImpl;

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
    public List<StateUse> findAll(Activity activity) {
        return repository.findAll(activity);
    }

    @Override
    public void syncUp(Activity activity, OrderService service,  Call<AppOrderResponse> call) {
        List<StateUse> stateUses = findAll(activity);

        if (stateUses != null) {
            AppOrder order = new AppOrder();

            HashMap<String, String> data = repository.getUserEmailAndPassword(activity);

            order.setToken(data.get("token"));
            order.setEmail(data.get("email"));
            order.setStateUses(stateUses);

            call = service.appOrder(order);

            Toast.makeText(activity, "Sincronizando " + stateUses.size() + " elementos", Toast.LENGTH_LONG).show();

            call.enqueue(new Callback<AppOrderResponse>() {

                @Override
                public void onResponse(Call<AppOrderResponse> call, Response<AppOrderResponse> response) {
                    if (response.isSuccessful()) {
                        Log.w(TAG, "onResponse: " + response.body());
                        //repository.signIn(response.body());
                    } else {
                        Log.w(TAG, "onResponse: " + response.errorBody());
                        //handleErrors(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<AppOrderResponse> call, Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());
                }
            });
        }
    }

}
