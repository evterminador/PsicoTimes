package com.example.formandocodigo.psicotimes.data.repository.datasource;

import com.example.formandocodigo.psicotimes.data.entity.StateUseEntity;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by FormandoCodigo on 29/11/2017.
 */

public interface StateUseDataStore {
    Observable<List<StateUseEntity>> stateUseEntityList();

    Observable<StateUseEntity> stateUseEntityDetails(final Integer stateUseId);
}
