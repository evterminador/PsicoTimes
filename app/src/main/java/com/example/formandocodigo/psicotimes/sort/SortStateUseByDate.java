package com.example.formandocodigo.psicotimes.sort;

import com.example.formandocodigo.psicotimes.entity.StateUse;

import java.util.Comparator;

/**
 * Created by FormandoCodigo on 16/12/2017.
 */

public class SortStateUseByDate implements Comparator<StateUse> {

    @Override
    public int compare(StateUse o1, StateUse o2) {
        return o1.getCreated_at().compareTo(o2.getCreated_at());
    }
}
