package com.example.formandocodigo.psicotimes.sort;

import com.example.formandocodigo.psicotimes.entity.StateUse;

import java.util.Comparator;

/**
 * Created by FormandoCodigo on 27/12/2017.
 */

public class SortStateUseByUseTime implements Comparator<StateUse> {

    @Override
    public int compare(StateUse t0, StateUse t1) {
        return t0.getUseTime().compareTo(t1.getUseTime());
    }
}
