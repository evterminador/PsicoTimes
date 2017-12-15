package com.example.formandocodigo.psicotimes.utils;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by FormandoCodigo on 29/11/2017.
 */

public class CompareTwoStateArrayList {
    private ArrayList<StateUseEntity> a1;
    private ArrayList<StateUseEntity> a2;

    public CompareTwoStateArrayList(ArrayList<StateUseEntity> a1, ArrayList<StateUseEntity> a2) {
        this.a1 = a1;
        this.a2 = a2;
    }

    public ArrayList<StateUseEntity> fixArray() {
        Iterator i = a1.iterator();

        while (i.hasNext()) {
            StateUseEntity state = (StateUseEntity) i.next();

            if (isExistsStateUse(state)) {
                addStateUseOld(state);
            }
        }

        return a2;
    }

    private boolean isExistsStateUse(StateUseEntity stateUseEntity) {
        for (StateUseEntity state : a2) {
            if (state.getNameApplication().equalsIgnoreCase(stateUseEntity.getNameApplication())) {
                state.setUseTime(stateUseEntity.getUseTime());
                state.setQuantity(state.getQuantity() + stateUseEntity.getQuantity());
                return false;
            }
        }
        return true;
    }

    private void addStateUseOld(StateUseEntity stateUseEntity) {
        a2.add(stateUseEntity);
    }

}
