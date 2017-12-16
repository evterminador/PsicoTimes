package com.example.formandocodigo.psicotimes.domain;

import com.example.formandocodigo.psicotimes.data.disk.StateUseDisk;
import com.example.formandocodigo.psicotimes.data.disk.StateUseDiskImpl;
import com.example.formandocodigo.psicotimes.entity.StateUse;

import java.util.ArrayList;

/**
 * Created by FormandoCodigo on 15/12/2017.
 */

public class StateUseCaseImpl implements StateUseCase {

    private StateUseDisk stateUseDisk;

    public StateUseCaseImpl() {
        stateUseDisk = new StateUseDiskImpl();
    }

    @Override
    public ArrayList<StateUse> getStateUseAll() {
        return new ArrayList<>(stateUseDisk.getStateUseAll());
    }

}
