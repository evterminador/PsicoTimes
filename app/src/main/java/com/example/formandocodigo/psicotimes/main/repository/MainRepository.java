package com.example.formandocodigo.psicotimes.main.repository;

import android.app.Activity;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.example.formandocodigo.psicotimes.entity.StateUse;
import com.example.formandocodigo.psicotimes.entity.StatisticsDetail;

import java.util.HashMap;
import java.util.List;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public interface MainRepository {
    List<StateUse> findByIdAll(Integer id);
    List<StateUse> findAll();
    List<StatisticsDetail> getStatisticsDetailCurrentDate();
    HashMap<String, String> getUserEmailAndPassword(Activity activity);
    int quantityUnlockScreen(Activity activity);
    int quantityNroApps(Activity activity);
}
