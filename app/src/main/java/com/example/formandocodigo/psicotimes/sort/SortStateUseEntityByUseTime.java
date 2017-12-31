package com.example.formandocodigo.psicotimes.sort;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;
import com.example.formandocodigo.psicotimes.entity.StateUse;

import java.util.Comparator;

/**
 * Created by FormandoCodigo on 10/12/2017.
 */

public class SortStateUseEntityByUseTime implements Comparator<StateUseEntity> {

    @Override
    public int compare(StateUseEntity t0, StateUseEntity t1) {
        return t0.getUseTime().compareTo(t1.getUseTime());
    }
}
