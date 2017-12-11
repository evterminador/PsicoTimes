package com.example.formandocodigo.psicotimes.sort;

import com.example.formandocodigo.psicotimes.entity.StateUse;

import java.util.Comparator;

/**
 * Created by FormandoCodigo on 11/12/2017.
 */

public class SortStateUseByQuantity implements Comparator<StateUse> {

    @Override
    public int compare(StateUse t0, StateUse t1) {
        return t0.getQuantity().compareTo(t1.getQuantity());
    }
}
