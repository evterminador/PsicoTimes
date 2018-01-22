package com.example.formandocodigo.psicotimes.sort;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;

import java.util.Comparator;

/**
 * Created by FormandoCodigo on 27/01/2018.
 */

public class SortStateUseEntityByDate implements Comparator<StateUseEntity> {

    @Override
    public int compare(StateUseEntity o1, StateUseEntity o2) {
        return o1.getCreated_at().compareTo(o2.getCreated_at());
    }
}
