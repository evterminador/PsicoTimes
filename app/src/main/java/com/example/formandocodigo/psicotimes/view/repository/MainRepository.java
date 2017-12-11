package com.example.formandocodigo.psicotimes.view.repository;

import android.app.Activity;

import com.example.formandocodigo.psicotimes.entity.StateUse;

import java.util.HashMap;
import java.util.List;

/**
 * Created by FormandoCodigo on 04/12/2017.
 */

public interface MainRepository {
    List<StateUse> findAll(Activity activity);
    HashMap<String, String> getUserEmailAndPassword(Activity activity);
}
