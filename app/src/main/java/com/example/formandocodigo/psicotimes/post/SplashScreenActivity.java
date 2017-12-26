package com.example.formandocodigo.psicotimes.post;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.formandocodigo.psicotimes.R;
import com.example.formandocodigo.psicotimes.data.cache.FileManager;
import com.example.formandocodigo.psicotimes.data.cache.StateUseCacheImpl;
import com.example.formandocodigo.psicotimes.data.cache.serializer.Serializer;
import com.example.formandocodigo.psicotimes.data.disk.StateUseDiskImpl;
import com.example.formandocodigo.psicotimes.data.entity.AppEntity;
import com.example.formandocodigo.psicotimes.data.entity.StateUserEntity;
import com.example.formandocodigo.psicotimes.data.entity.mapper.AppEntityDataMapper;
import com.example.formandocodigo.psicotimes.data.entity.mapper.StateUseEntityDataMapper;
import com.example.formandocodigo.psicotimes.data.entity.mapper.StateUserEntityDataMapper;
import com.example.formandocodigo.psicotimes.domain.StateUseCase;
import com.example.formandocodigo.psicotimes.domain.StateUseCaseImpl;
import com.example.formandocodigo.psicotimes.entity.App;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.entity.StateUser;
import com.example.formandocodigo.psicotimes.utils.net.OrderService;
import com.example.formandocodigo.psicotimes.utils.net.RetrofitBuilder;
import com.example.formandocodigo.psicotimes.main.net.AppOrderResponse;
import com.example.formandocodigo.psicotimes.main.net.StateUserOrder;
import com.example.formandocodigo.psicotimes.main.net.StateUserOrderResponse;
import com.example.formandocodigo.psicotimes.main.view.MainActivity;
import com.example.formandocodigo.psicotimes.utils.Continual;
import com.example.formandocodigo.psicotimes.utils.net.NetworkStatus;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by FormandoCodigo on 23/12/2017.
 */

public class SplashScreenActivity extends Activity implements SplashScreenView {

    private StateUseCacheImpl useCache = null;
    private StateUseCase useCase;

    AVLoadingIndicatorView avi;
    AVLoadingIndicatorView aviPacman;
    AVLoadingIndicatorView aviStateUse;

    @BindView(R.id.screen_status)
    TextView txtScreenStatus;

    OrderService service;
    Call<StateUserOrderResponse> stateUserCall;
    Call<AppOrderResponse> appCall;

    private int attempt = 0;
    private int attemptLimit = 5;
    private int isUpdateSuccess = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ButterKnife.bind(this);

        avi = findViewById(R.id.avi);
        aviPacman = findViewById(R.id.avi_pacman);
        aviStateUse = findViewById(R.id.avi_state_use);

        useCase = new StateUseCaseImpl();

        service = RetrofitBuilder.createService(OrderService.class);


        Thread tr = new Thread(new Runnable() {
            @Override
            public void run() {
                while (attempt <= attemptLimit) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (attempt >= attemptLimit) {
                        initializeActivity();
                    }

                    if (NetworkStatus.isOnline(SplashScreenActivity.this)) {
                        if (isUpdateSuccess == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    avi.hide();
                                    aviPacman.show();
                                    txtScreenStatus.setText("Consumiendo data");
                                }
                            });
                            serviceApp();
                            isUpdateSuccess = 1;
                        } else if (isUpdateSuccess == 1){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    aviPacman.hide();
                                    aviStateUse.show();
                                    txtScreenStatus.setText("Buscando estados de uso");
                                }
                            });
                            serviceStateUser();
                            isUpdateSuccess = 2;
                        } else {
                            initializeActivity();
                        }
                    } else {
                        attempt = attemptLimit;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                txtScreenStatus.setText("Por favor conectese a una red");
                            }
                        });
                    }
                    attempt++;
                }
            }
        });
        tr.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (stateUserCall != null)  {
            stateUserCall.cancel();
            stateUserCall = null;
        }
        if (appCall != null) {
            appCall.cancel();
            appCall = null;
        }
    }

    /**
     * Initialize activity
     * */
    protected void initializeActivity() {
        attempt = attemptLimit;
        finish();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void serviceApp() {
        appCall = service.appOrder();

        appCall.enqueue(new Callback<AppOrderResponse>() {
            @Override
            public void onResponse(Call<AppOrderResponse> call, Response<AppOrderResponse> response) {
                if (response.isSuccessful()) {
                    storeApp(response.body());
                } else {
                    updateAppError(response.errorBody().toString());
                }
            }
            @Override
            public void onFailure(Call<AppOrderResponse> call, Throwable t) {
                txtScreenStatus.setText(t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void serviceStateUser() {
        List<StateUse> stateUses = findCacheAll(this);

        if (stateUses != null) {

            StateUserOrder order = new StateUserOrder();

            HashMap<String, String> data = getUserEmailAndPassword(this);

            order.setToken(data.get("token"));
            order.setEmail(data.get("email"));
            order.setStateUses(stateUses);

            stateUserCall = service.stateUserOrder(order);

            stateUserCall.enqueue(new Callback<StateUserOrderResponse>() {

                @Override
                public void onResponse(Call<StateUserOrderResponse> call, Response<StateUserOrderResponse> response) {
                    if (response.isSuccessful()) {
                        storeStateUser(response.body());
                    } else {
                        updateStateUserError("No se pudo conectar sincronizar o no hay elementos");
                        //handleErrors(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<StateUserOrderResponse> call, Throwable t) {
                    txtScreenStatus.setText(t.getMessage());
                    t.printStackTrace();
                }
            });
        }
    }

    private void storeApp(AppOrderResponse response) {
        StateUseDiskImpl disk = new StateUseDiskImpl();

        ArrayList<App> res = transformAppEntityToApp(response.getApplications());

        int result = disk.putApplicationAll(res);

        if (result != -1) {
            if (result == 0) {
                txtScreenStatus.setText("Se ve, que entra muy seguido");
            } else {
                txtScreenStatus.setText("Data actualizada: " + result + " apps");
            }
        } else {
            txtScreenStatus.setText("Ups! no pudimos guardar la data. Verifique su conexión");
        }
    }

    private void storeStateUser(StateUserOrderResponse response) {
        StateUseDiskImpl disk = new StateUseDiskImpl();

        if (useCache == null) {
            useCache = new StateUseCacheImpl(this, new Serializer(), new FileManager());
        }

        ArrayList<StateUser> res = transformStateUserEntityToStateUser(response.getStateUses());

        int result = disk.putStateUserAll(res);

        if (result != -1) {
            txtScreenStatus.setText(response.getMessage());
            useCache.evictAll();
        } else {
            txtScreenStatus.setText("Error al tratar de grabar");
        }
    }

    private void updateAppError(String error) {
        txtScreenStatus.setText(error);
    }

    private void updateStateUserError(String error) {
        txtScreenStatus.setText(error);
    }

    /**
     *  Return {List<StateUse> of cache the system}
     * */
    private List<StateUse> findCacheAll(Activity activity) {
        List<StateUse> stateUses = new ArrayList<>();

        if (useCache == null) {
            useCache = new StateUseCacheImpl(activity, new Serializer(), new FileManager());
        }

        StateUseEntityDataMapper stateUseMapper = new StateUseEntityDataMapper();
        if (useCache.getAll() != null) {
            stateUses = stateUseMapper.transformArrayList(useCache.getAll());
        }
        return stateUses;
    }

    public HashMap<String, String> getUserEmailAndPassword(Activity activity) {
        HashMap<String, String> data = new HashMap<>();

        SharedPreferences preferences = activity.getSharedPreferences(Continual.Shared.DEFAULT_FILE_NAME, Context.MODE_PRIVATE);

        String email = preferences.getString("email", null);
        String token = preferences.getString("token", null);

        data.put("email", email);
        data.put("token", token);

        return data;
    }

    private ArrayList<App> transformAppEntityToApp(List<AppEntity> appEntities) {
        ArrayList<App> apps;

        AppEntityDataMapper mapper = new AppEntityDataMapper();

        apps = new ArrayList<>(mapper.transformArrayList((ArrayList<AppEntity>) appEntities));

        return apps;
    }

    private ArrayList<StateUser> transformStateUserEntityToStateUser(List<StateUserEntity> stateUserEntities) {
        ArrayList<StateUser> stateUsers;

        StateUserEntityDataMapper mapper = new StateUserEntityDataMapper();

        stateUsers = new ArrayList<>(mapper.transformArrayList((ArrayList<StateUserEntity>) stateUserEntities));

        return stateUsers;
    }

}
