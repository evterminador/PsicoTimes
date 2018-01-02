package com.example.formandocodigo.psicotimes.post;

import android.app.Activity;
import android.app.ActivityManager;
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
import com.example.formandocodigo.psicotimes.data.entity.HistoricStateEntity;
import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.example.formandocodigo.psicotimes.data.entity.StateUserEntity;
import com.example.formandocodigo.psicotimes.data.entity.mapper.AppEntityDataMapper;
import com.example.formandocodigo.psicotimes.data.entity.mapper.HistoricStateEntityDataMapper;
import com.example.formandocodigo.psicotimes.data.entity.mapper.StateUserEntityDataMapper;
import com.example.formandocodigo.psicotimes.domain.StateUseCase;
import com.example.formandocodigo.psicotimes.domain.StateUseCaseImpl;
import com.example.formandocodigo.psicotimes.entity.App;
import com.example.formandocodigo.psicotimes.entity.HistoricState;
import com.example.formandocodigo.psicotimes.entity.StateUser;
import com.example.formandocodigo.psicotimes.post.net.HistoricStateOrder;
import com.example.formandocodigo.psicotimes.post.net.HistoricStateResponse;
import com.example.formandocodigo.psicotimes.service.StateUseService;
import com.example.formandocodigo.psicotimes.sort.SortStateUseEntityByUseTime;
import com.example.formandocodigo.psicotimes.utils.Converts;
import com.example.formandocodigo.psicotimes.utils.StateUseAll;
import com.example.formandocodigo.psicotimes.utils.net.OrderService;
import com.example.formandocodigo.psicotimes.utils.net.RetrofitBuilder;
import com.example.formandocodigo.psicotimes.main.net.AppOrderResponse;
import com.example.formandocodigo.psicotimes.main.net.StateUserOrder;
import com.example.formandocodigo.psicotimes.main.net.StateUserOrderResponse;
import com.example.formandocodigo.psicotimes.main.view.MainActivity;
import com.example.formandocodigo.psicotimes.utils.Continual;
import com.example.formandocodigo.psicotimes.utils.net.NetworkStatus;
import com.example.formandocodigo.psicotimes.utils.permission.UsageStatsPermission;
import com.wang.avi.AVLoadingIndicatorView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
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
    private StateUseDiskImpl disk;
    private StateUseCase useCase;

    AVLoadingIndicatorView avi;
    AVLoadingIndicatorView aviPacman;
    AVLoadingIndicatorView aviStateUse;
    AVLoadingIndicatorView aviHistoricState;

    @BindView(R.id.screen_status)
    TextView txtScreenStatus;

    OrderService service;
    Call<StateUserOrderResponse> stateUserCall;
    Call<AppOrderResponse> appCall;
    Call<HistoricStateResponse> historicCall;

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
        aviHistoricState = findViewById(R.id.avi_historic_state);

        useCase = new StateUseCaseImpl();
        disk = new StateUseDiskImpl();

        service = RetrofitBuilder.createService(OrderService.class);

        stopServiceState();

        Thread tr = new Thread(new Runnable() {
            @Override
            public void run() {
                while (attempt <= attemptLimit) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
                        } else if (isUpdateSuccess == 2) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    aviStateUse.hide();
                                    aviHistoricState.show();
                                    txtScreenStatus.setText("Actualizando estado General");
                                }
                            });
                            serviceHistoricState();
                            isUpdateSuccess = 3;
                        }else {
                            attempt = attemptLimit;
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

                if (attempt >= attemptLimit) {
                    initializeActivity();
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
        if (historicCall != null) {
            historicCall.cancel();
            historicCall = null;
        }
    }

    /**
     * Initialize activity
     * */
    protected void initializeActivity() {
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
        List<StateUseEntity> stateUseEntities = findCacheAll(this);

        if (stateUseEntities != null && stateUseEntities.size() > 0) {

            StateUserOrder order = new StateUserOrder();

            HashMap<String, String> data = getUserEmailAndPassword(this);

            order.setToken(data.get("token"));
            order.setEmail(data.get("email"));
            order.setStateUseEntities(stateUseEntities);

            stateUserCall = service.stateUserOrder(order);

            stateUserCall.enqueue(new Callback<StateUserOrderResponse>() {

                @Override
                public void onResponse(Call<StateUserOrderResponse> call, Response<StateUserOrderResponse> response) {
                    if (response.isSuccessful()) {
                        storeStateUser(response.body());
                    } else {
                        updateStateUserError("No se pudo sincronizar error en la red");
                        //handleErrors(response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<StateUserOrderResponse> call, Throwable t) {
                    txtScreenStatus.setText(t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtScreenStatus.setText("Ups! No hay elementos");
                }
            });
        }
    }

    private void serviceHistoricState() {
        if (checkPermission()) {
            StateUseAll stateUseAll = new StateUseAll(this);
            List<StateUseEntity> stateUseEntities = stateUseAll.getStateUseEntityAll();

            if (stateUseEntities != null && stateUseEntities.size() > 0) {
                HistoricStateOrder order = new HistoricStateOrder();

                order.setEmail(getUserEmailAndPassword(this).get("email"));
                order.setToken(getUserEmailAndPassword(this).get("token"));
                order.setQuantityScreenUnlock(getQuantityScreenUnlock());

                HistoricStateEntity historicStateEntity = new HistoricStateEntity();

                Timestamp current = new Timestamp(System.currentTimeMillis());

                historicStateEntity.setNameTop(getUseNameAppTop(new ArrayList<>(stateUseEntities)));
                historicStateEntity.setQuantity(stateUseEntities.size());
                historicStateEntity.setTimeUse(getTotalUseTime(stateUseEntities));
                historicStateEntity.setCreated_at(Converts.convertTimestampToString(current));
                historicStateEntity.setUpdated_at(Converts.convertTimestampToString(current));

                order.setHistoricState(historicStateEntity);

                historicCall = service.historicOrder(order);

                historicCall.enqueue(new Callback<HistoricStateResponse>() {
                    @Override
                    public void onResponse(Call<HistoricStateResponse> call, Response<HistoricStateResponse> response) {
                        if (response.isSuccessful()) {
                            storeHistoricState(response.body());
                        } else {
                            updateHistoricStateError("No pudimos conectarnos con el servicio inténtelo más tarde");
                        }
                    }

                    @Override
                    public void onFailure(Call<HistoricStateResponse> call, Throwable t) {
                        txtScreenStatus.setText(t.getMessage());
                        t.printStackTrace();
                    }
                });

            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtScreenStatus.setText("Oh! La app no tiene permisos de uso");
                }
            });
        }
    }

    private int getQuantityScreenUnlock() {
        int quantity = 0;
        SharedPreferences preferences = getSharedPreferences(Continual.Shared.LockScreen.FILE_NAME, Context.MODE_PRIVATE);

        int result = preferences.getInt(Continual.Shared.LockScreen.KEY_SCREEN, -1);

        if (result > 0) {
            quantity = result;
        }

        return (quantity);
    }

    private void storeApp(AppOrderResponse response) {
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
        if (useCache == null) {
            useCache = new StateUseCacheImpl(this, new Serializer(), new FileManager());
        }

        ArrayList<StateUser> res = transformStateUserEntityToStateUser(response.getStateUses());

        int result = disk.putStateUserAll(res);

        if (result != -1) {
            txtScreenStatus.setText(response.getMessage());
            useCache.evictAll();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtScreenStatus.setText("Error al tratar de grabar");
                }
            });
        }
    }

    private void storeHistoricState(HistoricStateResponse response) {
        ArrayList<HistoricState> res = new ArrayList<>();

        res.add(transformHistoricStateEntityToHistoricState(response.getHistoricStateEntity()));

        int result = disk.putHistoricStateAll(res);

        String message = response.getMessage();

        if (message.equalsIgnoreCase("Error")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtScreenStatus.setText("Tuvimos problemas en la nube");
                }
            });
        } else {
            if (result != -1) {
                txtScreenStatus.setText(response.getMessage());
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtScreenStatus.setText("Lo sentimos no pudimos guardar su info");
                    }
                });
            }
        }
    }

    private void updateAppError(String error) {
        txtScreenStatus.setText(error);
    }

    private void updateStateUserError(String error) {
        txtScreenStatus.setText(error);
    }

    private void updateHistoricStateError(String error) {
        txtScreenStatus.setText(error);
    }

    private void stopServiceState() {
        if (UsageStatsPermission.isExistsPermission(this)) {

            boolean isService = isMyServiceRunning(StateUseService.class);

            if (isService) {
                stopService(new Intent(this, StateUseService.class));
            }
        } else {
            stopService(new Intent(this, StateUseService.class));
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     *  Return {List<StateUse> of cache the system}
     * */
    private List<StateUseEntity> findCacheAll(Activity activity) {
        List<StateUseEntity> stateUseEntities = new ArrayList<>();

        if (useCache == null) {
            useCache = new StateUseCacheImpl(activity, new Serializer(), new FileManager());
        }

        if (useCache.getAll() != null) {
            stateUseEntities = useCache.getAll();
        }
        return stateUseEntities;
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

    private Boolean checkPermission() {
        return UsageStatsPermission.isExistsPermission(this);
    }

    private String getUseNameAppTop(List<StateUseEntity> list) {
        Collections.sort(list, new SortStateUseEntityByUseTime());
        Collections.reverse(list);

        return list.get(0).getNameApplication();
    }

    private long getTotalUseTime(List<StateUseEntity> stateUseEntities) {
        long total = 0;

        for (StateUseEntity s : stateUseEntities) {
            total += s.getUseTime();
        }
        return total;
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

    private HistoricState transformHistoricStateEntityToHistoricState(HistoricStateEntity historicStateEntity) {
        HistoricState historicState;

        HistoricStateEntityDataMapper mapper = new HistoricStateEntityDataMapper();

        historicState = mapper.transform(historicStateEntity);

        return historicState;
    }

}
