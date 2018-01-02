package com.example.formandocodigo.psicotimes.main.repository;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.formandocodigo.psicotimes.data.cache.FileManager;
import com.example.formandocodigo.psicotimes.data.cache.StateUseCacheImpl;
import com.example.formandocodigo.psicotimes.data.cache.serializer.Serializer;
import com.example.formandocodigo.psicotimes.data.disk.StateUseDiskImpl;
import com.example.formandocodigo.psicotimes.data.entity.AppEntity;
import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.example.formandocodigo.psicotimes.data.entity.StateUserEntity;
import com.example.formandocodigo.psicotimes.data.entity.mapper.AppEntityDataMapper;
import com.example.formandocodigo.psicotimes.data.entity.mapper.StateUserEntityDataMapper;
import com.example.formandocodigo.psicotimes.domain.StateUseCase;
import com.example.formandocodigo.psicotimes.domain.StateUseCaseImpl;
import com.example.formandocodigo.psicotimes.entity.App;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.entity.StateUser;
import com.example.formandocodigo.psicotimes.main.net.AppOrderResponse;
import com.example.formandocodigo.psicotimes.main.net.StateUserOrderResponse;
import com.example.formandocodigo.psicotimes.utils.Continual;
import com.example.formandocodigo.psicotimes.main.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public class MainRepositoryImpl implements MainRepository {

    private MainPresenter presenter;
    private StateUseCacheImpl useCache = null;
    private StateUseCase useCase;

    public MainRepositoryImpl(MainPresenter presenter) {
        this.presenter = presenter;
        useCase = new StateUseCaseImpl();
    }

    @Override
    public List<StateUse> findByIdAll(Integer id) {
        return useCase.findStateUseByIdAll(id);
    }

    @Override
    public List<StateUse> findAll() {
        return useCase.getStateUseAll();
    }

    @Override
    public List<StateUseEntity> findCacheAll(Activity activity) {
        List<StateUseEntity> stateUses = new ArrayList<>();

        if (useCache == null) {
            useCache = new StateUseCacheImpl(activity, new Serializer(), new FileManager());
        }

        if (useCache.getAll() != null) {
            stateUses = useCache.getAll();
        }
        return stateUses;
    }


    @Override
    public HashMap<String, String> getUserEmailAndPassword(Activity activity) {
        HashMap<String, String> data = new HashMap<>();

        SharedPreferences preferences = activity.getSharedPreferences(Continual.Shared.DEFAULT_FILE_NAME, Context.MODE_PRIVATE);

        String email = preferences.getString("email", null);
        String token = preferences.getString("token", null);

        data.put("email", email);
        data.put("token", token);

        return data;
    }

    @Override
    public void storeApp(Activity activity, AppOrderResponse response) {
        StateUseDiskImpl disk = new StateUseDiskImpl();

        ArrayList<App> res = transformAppEntityToApp(response.getApplications());

        int result = disk.putApplicationAll(res);
        if (result != -1) {
            presenter.updateAppSuccess("Aplicaciones añadidas: " + result);
        } else {
            presenter.updateAppError("Error al grabar las aplicaiones");
        }
    }

    @Override
    public void storeStateUser(Activity activity, StateUserOrderResponse response) {
        StateUseDiskImpl disk = new StateUseDiskImpl();

        if (useCache == null) {
            useCache = new StateUseCacheImpl(activity, new Serializer(), new FileManager());
        }

        ArrayList<StateUser> res = transformStateUserEntityToStateUser(response.getStateUses());

        if (disk.putStateUserAll(res) != -1) {
            presenter.syncSuccess(response.getMessage());
            useCache.evictAll();
        } else {
            presenter.syncError("Error al tratar de grabar");
        }
    }

    @Override
    public Integer quantityUnlockScreen(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences(Continual.Shared.LockScreen.FILE_NAME, Context.MODE_PRIVATE);
        return (preferences.getInt(Continual.Shared.LockScreen.KEY_SCREEN, 0));
    }

    private ArrayList<StateUser> transformStateUserEntityToStateUser(List<StateUserEntity> stateUserEntities) {
        ArrayList<StateUser> stateUsers;

        StateUserEntityDataMapper mapper = new StateUserEntityDataMapper();

        stateUsers = new ArrayList<>(mapper.transformArrayList((ArrayList<StateUserEntity>) stateUserEntities));

        return stateUsers;
    }

    private ArrayList<App> transformAppEntityToApp(List<AppEntity> appEntities) {
        ArrayList<App> apps;

        AppEntityDataMapper mapper = new AppEntityDataMapper();

        apps = new ArrayList<>(mapper.transformArrayList((ArrayList<AppEntity>) appEntities));

        return apps;
    }

}
