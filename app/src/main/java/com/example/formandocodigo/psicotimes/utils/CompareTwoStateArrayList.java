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

    public ArrayList<StateUseEntity> fix() {
        ArrayList<StateUseEntity> newArray = new ArrayList<>();
        Iterator i = a1.iterator();

        Boolean swith;

        while (i.hasNext()) {
            StateUseEntity state = (StateUseEntity) i.next();
            String nomApp;
            swith = true;
            for (StateUseEntity stateUse : a2) {
                nomApp = stateUse.getNameApplication();
                if (state.getNameApplication().equalsIgnoreCase(nomApp)) {
                    state.setUseTime(state.getUseTime());
                    state.setQuantity(state.getQuantity() + stateUse.getQuantity());
                    swith = false;
                    break;
                }
            }
            if (swith) {

            }
        }

        return newArray;
    }

    public int size1() {
        return a1.size();
    }

    public int size2() {
        return  a2.size();
    }

}
