package com.example.formandocodigo.psicotimes.view.repository;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.formandocodigo.psicotimes.data.cache.FileManager;
import com.example.formandocodigo.psicotimes.data.cache.StateUseCacheImpl;
import com.example.formandocodigo.psicotimes.data.cache.serializer.Serializer;
import com.example.formandocodigo.psicotimes.data.entity.mapper.StateUseEntityDataMapper;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.utils.Continual;
import com.example.formandocodigo.psicotimes.view.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public class MainRepositoryImpl implements MainRepository {

    private MainPresenter presenter;

    public MainRepositoryImpl(MainPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public List<StateUse> findAll(Activity activity) {
        List<StateUse> stateUses = new ArrayList<>();

        StateUseCacheImpl read = new StateUseCacheImpl(activity, new Serializer(), new FileManager());
        StateUseEntityDataMapper stateUseMapper = new StateUseEntityDataMapper();
        if (read.getAll() != null) {
            stateUses = stateUseMapper.transformArrayList(read.getAll());
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
}
