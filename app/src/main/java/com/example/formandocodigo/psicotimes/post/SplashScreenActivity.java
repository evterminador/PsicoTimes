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
import com.example.formandocodigo.psicotimes.data.entity.HistoricStateEntity;
import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.example.formandocodigo.psicotimes.data.entity.mapper.HistoricStateEntityDataMapper;
import com.example.formandocodigo.psicotimes.data.entity.mapper.StateUseEntityDataMapper;
import com.example.formandocodigo.psicotimes.domain.StateUseCase;
import com.example.formandocodigo.psicotimes.domain.StateUseCaseImpl;
import com.example.formandocodigo.psicotimes.entity.AppTop;
import com.example.formandocodigo.psicotimes.entity.HistoricState;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.entity.StatisticsDetail;
import com.example.formandocodigo.psicotimes.post.net.HistoricStateOrder;
import com.example.formandocodigo.psicotimes.post.net.HistoricStateResponse;
import com.example.formandocodigo.psicotimes.service.StateUseService;
import com.example.formandocodigo.psicotimes.sort.SortStateUseEntityByDate;
import com.example.formandocodigo.psicotimes.sort.SortStateUseEntityByUseTime;
import com.example.formandocodigo.psicotimes.utils.Converts;
import com.example.formandocodigo.psicotimes.utils.net.OrderService;
import com.example.formandocodigo.psicotimes.utils.net.RetrofitBuilder;
import com.example.formandocodigo.psicotimes.main.view.MainActivity;
import com.example.formandocodigo.psicotimes.utils.Continual;
import com.example.formandocodigo.psicotimes.utils.net.NetworkStatus;
import com.example.formandocodigo.psicotimes.utils.permission.UsageStatsPermission;
import com.wang.avi.AVLoadingIndicatorView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
    AVLoadingIndicatorView aviHistoricState;

    @BindView(R.id.screen_status)
    TextView txtScreenStatus;

    OrderService service;
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
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (NetworkStatus.isOnline(SplashScreenActivity.this)) {
                        if (isUpdateSuccess == 0) {
                            isUpdateSuccess = 1;
                        } else if (isUpdateSuccess == 1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    avi.hide();
                                    aviHistoricState.show();
                                    txtScreenStatus.setText("Actualizando estado General");
                                }
                            });
                            serviceHistoricState();
                            isUpdateSuccess = 2;
                        } else {
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
                    //storeAppTop();
                    storeStatisticsDetail();

                    initializeActivity();
                }
            }
        });
        tr.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    private void serviceHistoricState() {
        if (checkPermission()) {
            List<StateUseEntity> stateUseEntities = findCacheAll(this);

            if (stateUseEntities != null && stateUseEntities.size() > 0) {
                HistoricStateOrder order = new HistoricStateOrder();

                order.setEmail(getUserEmailAndPassword(this).get("email"));
                order.setToken(getUserEmailAndPassword(this).get("token"));

                List<HistoricStateEntity> historicStateEntities = new ArrayList<>();

                StateUseEntity min = Collections.min(stateUseEntities, new SortStateUseEntityByDate());
                StateUseEntity max = Collections.max(stateUseEntities, new SortStateUseEntityByDate());

                Calendar minD = Calendar.getInstance();
                Calendar maxD = Calendar.getInstance();

                minD.setTimeInMillis(min.getCreated_at().getTime());
                maxD.setTimeInMillis(max.getCreated_at().getTime());

                Converts.setTimeToBeginningOfDay(minD);
                Converts.setTimeToEndOfDay(maxD);

                long diff = maxD.getTimeInMillis() - minD.getTimeInMillis();
                long diffInDays = diff / (24 * 60 * 60 * 1000);

                HistoricStateEntity historic;
                Calendar aux = Calendar.getInstance();
                for (int i = 0; i <= diffInDays; i++) {
                    aux.setTimeInMillis(minD.getTimeInMillis());

                    Converts.setTimeToEndOfDay(aux);
                    List<StateUseEntity> entities = new ArrayList<>();
                    for (StateUseEntity s : stateUseEntities) {
                        if (s.getCreated_at().getTime() > minD.getTimeInMillis() && s.getCreated_at().getTime() < aux.getTimeInMillis()) {
                            entities.add(s);
                        }
                    }

                    Timestamp current = new Timestamp(System.currentTimeMillis());
                    if (entities.size() > 0) {
                        historic =  new HistoricStateEntity();

                        historic.setNameTop(getUseNameAppTop(new ArrayList<>(entities)));
                        historic.setQuantity(entities.size());
                        historic.setTimeUse(getTotalUseTime(entities));
                        historic.setNroUnlock(getQuantityScreenUnlock());
                        historic.setCreated_at(Converts.convertTimestampToString(entities.get(0).getCreated_at()));
                        historic.setUpdated_at(Converts.convertTimestampToString(current));

                        historicStateEntities.add(historic);
                    }
                    minD.add(Calendar.DAY_OF_YEAR, 1);
                }

                order.setHistoricStateEntities(historicStateEntities);

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

    private void storeHistoricState(HistoricStateResponse response) {
        ArrayList<HistoricState> res = transformHistoricStateEntityToHistoricState(response.getHistoricStateEntities());

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

    private void storeAppTop(){
        List<StateUseEntity> stateUseEntities = findCacheAll(this);

        if (stateUseEntities != null && stateUseEntities.size() > 0) {

        }
    }

    private void storeStatisticsDetail() {
        List<StateUseEntity> stateUseEntities = findCacheAll(this);

        if (stateUseEntities != null && stateUseEntities.size() > 0) {
            ArrayList<StatisticsDetail> list = transformStateUseEntityToStatisticsDetail(stateUseEntities);

            int result = disk.putStatisticsDetailAll(list);

            if (result != -1) {
                useCache.evictAll();
            }
        }
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

    private ArrayList<HistoricState> transformHistoricStateEntityToHistoricState(List<HistoricStateEntity> historicStateEntities) {
        ArrayList<HistoricState> list;

        HistoricStateEntityDataMapper mapper = new HistoricStateEntityDataMapper();

        list = new ArrayList<>(mapper.transformArrayList((ArrayList<HistoricStateEntity>) historicStateEntities));

        return list;
    }

    private ArrayList<StatisticsDetail> transformStateUseEntityToStatisticsDetail(List<StateUseEntity> stateUseEntities) {
        ArrayList<StateUse> list;
        ArrayList<StatisticsDetail> statisticsDetails = null;

        StateUseEntityDataMapper mapper = new StateUseEntityDataMapper();

        list = new ArrayList<>(mapper.transform(stateUseEntities));

        if (list.size() > 0) {
            statisticsDetails = new ArrayList<>();

            StatisticsDetail detail;
            for (StateUse s : list) {
                detail = new StatisticsDetail();
                detail.setNameApp(s.getNameApplication());
                detail.setImage(s.getImageApp());
                detail.setQuantity(s.getQuantity());
                detail.setTimeUse(s.getUseTime());
                detail.setLastUseTime(s.getLastUseTime());
                detail.setCreated_at(s.getCreated_at());
                detail.setUpdated_at(s.getUpdated_at());

                statisticsDetails.add(detail);
            }
        }

        return statisticsDetails;
    }

    private ArrayList<AppTop> transformStateUseEntityToAppTop(ArrayList<StateUseEntity> stateUseEntities) {
        ArrayList<StateUse> list;
        ArrayList<AppTop> appTops = null;

        StateUseEntityDataMapper mapper = new StateUseEntityDataMapper();

        list = new ArrayList<>(mapper.transform(stateUseEntities));
        if (list.size() > 0) {
            appTops = new ArrayList<>();

            AppTop a;

        }
        return appTops;
    }

}
