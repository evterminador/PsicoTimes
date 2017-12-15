package com.example.formandocodigo.psicotimes.main.repository;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.formandocodigo.psicotimes.data.cache.FileManager;
import com.example.formandocodigo.psicotimes.data.cache.StateUseCacheImpl;
import com.example.formandocodigo.psicotimes.data.cache.serializer.Serializer;
import com.example.formandocodigo.psicotimes.data.disk.StateUseDiskImpl;
import com.example.formandocodigo.psicotimes.data.entity.StateUserEntity;
import com.example.formandocodigo.psicotimes.data.entity.mapper.StateUseEntityDataMapper;
import com.example.formandocodigo.psicotimes.data.entity.mapper.StateUserEntityDataMapper;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.entity.StateUser;
import com.example.formandocodigo.psicotimes.main.net.entity.AppOrderResponse;
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

    public MainRepositoryImpl(MainPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public List<StateUse> findAll(Activity activity) {
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
    public void fetchApp(Activity activity, AppOrderResponse response) {
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

    private ArrayList<StateUser> transformStateUserEntityToStateUser(List<StateUserEntity> stateUserEntities) {
        ArrayList<StateUser> stateUsers;

        StateUserEntityDataMapper mapper = new StateUserEntityDataMapper();

        stateUsers = new ArrayList<>(mapper.transformArrayList((ArrayList<StateUserEntity>) stateUserEntities));

        return stateUsers;
    }

}
