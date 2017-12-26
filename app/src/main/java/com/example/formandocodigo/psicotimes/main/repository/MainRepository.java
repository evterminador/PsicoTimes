package com.example.formandocodigo.psicotimes.main.repository;

import android.app.Activity;

import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.main.net.AppOrderResponse;
import com.example.formandocodigo.psicotimes.main.net.StateUserOrderResponse;

import java.util.HashMap;
import java.util.List;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public interface MainRepository {
    List<StateUse> findByIdAll(Integer id);
    List<StateUse> findAll();
    List<StateUse> findCacheAll(Activity activity);
    HashMap<String, String> getUserEmailAndPassword(Activity activity);
    void storeApp(Activity activity, AppOrderResponse response);
    void storeStateUser(Activity activity, StateUserOrderResponse response);
}
